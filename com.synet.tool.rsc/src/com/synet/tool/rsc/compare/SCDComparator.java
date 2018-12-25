package com.synet.tool.rsc.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.ied.IedCompare;
import com.synet.tool.rsc.compare.ied.IedCompareParser;
import com.synet.tool.rsc.io.ied.Context;

public class SCDComparator {

	private String srcPath;
	private String destPath;
	private IProgressMonitor monitor;
	private VTDXMLDBHelper srcXmlHelper;
	private VTDXMLDBHelper destXmlHelper;
	
	public SCDComparator(String srcPath, String destPath, IProgressMonitor monitor) {
		this.srcPath = srcPath;
		this.destPath = destPath;
		this.monitor = monitor;
		init();
	}
	
	private void init() {
		srcXmlHelper = new VTDXMLDBHelper();
		destXmlHelper = new VTDXMLDBHelper();
		srcXmlHelper.loadDocument("", srcPath);
		destXmlHelper.loadDocument("", destPath);
	}
	
	private Map<String, Element> toMap(List<Element> ieds) {
		Map<String, Element> iedMap = new HashMap<>();
		for (Element ied : ieds) {
			iedMap.put(ied.attributeValue("name"), ied);
		}
		return iedMap;
	}
	
	private Context createContext(VTDXMLDBHelper xmldbHelper) {
		Element commEl = xmldbHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		Element dtTypeNd = xmldbHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		return new Context(commEl, dtTypeNd);
	}

	public List<CompareResult> execute() {
		List<CompareResult> results = new ArrayList<CompareResult>();
		List<Element> iedsSrc =  srcXmlHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
		List<Element> iedsDest =  destXmlHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
		Map<String, Element> srcMap = toMap(iedsSrc);
		Map<String, Element> destMap = toMap(iedsDest);
		monitor.beginTask("开始比较SCD", srcMap.size() + 1);
		for (Entry<String, Element> entry : srcMap.entrySet()) {
			String srcIedName = entry.getKey();
			Element srcIed = entry.getValue();
			String srcIedDesc = srcIed.attributeValue("desc");
			monitor.setTaskName("正在比较" + srcIedName);
			CompareResult result = new CompareResult(srcIedName, srcIedDesc);
			Element destIed = destMap.get(srcIedName);
			if (destIed == null) {
				result.setOper(OP.DELETE);
			} else {
				Element iedNdSrc = srcXmlHelper.selectSingleNode("/SCL/IED[@name='" + srcIedName + "']");
				Element iedNdDest = destXmlHelper.selectSingleNode("/SCL/IED[@name='" + srcIedName + "']");
				String srcMd5 = FileManipulate.getMD5CodeForStr(iedNdSrc.asXML());
				String destMd5 = FileManipulate.getMD5CodeForStr(iedNdDest.asXML());
				if (!srcMd5.equals(destMd5)) {
					result.setOper(OP.UPDATE);
					iedNdSrc.addAttribute("name", srcIedName + "_old");
					iedNdDest.addAttribute("name", srcIedName + "_new");
					IedCompareParser srcIedComp = new IedCompareParser(iedNdSrc, createContext(srcXmlHelper));
					IedCompareParser destIedComp = new IedCompareParser(iedNdDest, createContext(destXmlHelper));
					srcIedComp.parse();
					destIedComp.parse();
					srcMd5 = FileManipulate.getMD5Code(srcIedComp.getDomPath());
					destMd5 = FileManipulate.getMD5Code(destIedComp.getDomPath());
					if (!srcMd5.equals(destMd5)) {
						iedNdSrc = srcIedComp.getIedNode();
						iedNdDest = destIedComp.getIedNode();
						Difference diff = new IedCompare(srcIedName, iedNdSrc, iedNdDest).execute();
						if (diff.getChildren().size() > 0 || !StringUtil.isEmpty(diff.getMsg())) {
							result.setDifference(diff);
						} else {
							result.setOper(null);
						}
					} else {
						result.setOper(null);
					}
				}
				destMap.remove(srcIedName);
			}
			if (result.getOper() != null) {
				results.add(result);
			}
			monitor.worked(1);
		}
		if (destMap.size() > 0) {
			for (Entry<String, Element> entry : destMap.entrySet()) {
				String destIedName = entry.getKey();
				Element destIed = entry.getValue();
				String destIedDesc = destIed.attributeValue("desc");
				CompareResult result = new CompareResult(destIedName, destIedDesc);
				result.setOper(OP.ADD);
				results.add(result);
			}
		}
		monitor.done();
		return results;
	}
}