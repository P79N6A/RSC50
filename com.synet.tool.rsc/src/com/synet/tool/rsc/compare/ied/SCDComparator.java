package com.synet.tool.rsc.compare.ied;

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
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.SCLComparator;
import com.synet.tool.rsc.io.ied.Context;

public class SCDComparator extends SCLComparator {

	public SCDComparator(String srcPath, String destPath, IProgressMonitor monitor) {
		super(srcPath, destPath, monitor);
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

	public List<Difference> execute() {
		List<Difference> results = new ArrayList<Difference>();
		List<Element> iedsSrc =  srcXmlHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
		List<Element> iedsDest =  destXmlHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
		Map<String, Element> srcMap = toMap(iedsSrc);
		Map<String, Element> destMap = toMap(iedsDest);
		monitor.beginTask("开始比较SCD", srcMap.size() + 1);
		for (Entry<String, Element> entry : srcMap.entrySet()) {
			String srcIedName = entry.getKey();
			Element srcIed = entry.getValue();
			monitor.setTaskName("正在比较" + srcIedName);
			Element destIed = destMap.get(srcIedName);
			if (destIed == null) {
				results.add(CompareUtil.addDiffByAttName(null, srcIed, "name", OP.DELETE));
			} else {
				Element iedNdSrc = srcXmlHelper.selectSingleNode("/SCL/IED[@name='" + srcIedName + "']");
				Element iedNdDest = destXmlHelper.selectSingleNode("/SCL/IED[@name='" + srcIedName + "']");
				String srcMd5 = FileManipulate.getMD5CodeForStr(iedNdSrc.asXML());
				String destMd5 = FileManipulate.getMD5CodeForStr(iedNdDest.asXML());
				if (!srcMd5.equals(destMd5)) {
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
						Difference diff = new IedCompare(iedNdSrc, iedNdDest).execute();
						if (diff.getChildren().size() > 0 || !StringUtil.isEmpty(diff.getMsg())) {
							results.add(diff);
						}
					}
				}
				destMap.remove(srcIedName);
			}
			monitor.worked(1);
		}
		if (destMap.size() > 0) {
			for (Entry<String, Element> entry : destMap.entrySet()) {
				Element destIed = entry.getValue();
				results.add(CompareUtil.addDiffByAttName(null, destIed, "name", OP.ADD));
			}
		}
		monitor.done();
		return results;
	}
}
