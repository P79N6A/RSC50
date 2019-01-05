package com.synet.tool.rsc.incr.scd;

import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.CompareUtil;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.OP;
import com.synet.tool.rsc.compare.XmlHelperCache;
import com.synet.tool.rsc.compare.ied.IedCompare;
import com.synet.tool.rsc.compare.ied.IedCompareParser;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.incr.BaseConflictHandler;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.ied.IedParserNew;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.IedEntityService;

public class IEDConflictHandler extends BaseConflictHandler {

	private IedEntityService iedServ = new IedEntityService();
	private Context context;
	
	public IEDConflictHandler(Difference diff, Context context) {
		super(diff);
		this.context = context;
	}

	@Override
	public void handleAdd() {
		String iedName = diff.getName();
		VTDXMLDBHelper destXmlHelper = XmlHelperCache.getInstance().getDestXmlHelper();
		Element iedNd = destXmlHelper.selectSingleNode(SCL.getIEDXPath(iedName));
		IedParserNew iedParser = new IedParserNew(iedNd, context, null);
		iedParser.parse();
	}

	@Override
	public void setData() {
		String iedName = (diff.getOp()==OP.RENAME) ? diff.getNewName() : diff.getName();
		Tb1046IedEntity ied = iedServ.getIedEntityByDevName(iedName);
		diff.setData(ied);
	}

	@Override
	public void handleDelete() {
		iedServ.deleteOldIed(diff.getName());
	}

	@Override
	public void handleUpate() {
		Map<String, String> updateInfo = getUpdateInfo();
		iedServ.updateIed(updateInfo);
	}

	@Override
	public void mergeDifference() {
		String oldName = diff.getName();
		String newName = diff.getNewName();
		
		VTDXMLDBHelper srcXmlHelper = XmlHelperCache.getInstance().getSrcXmlHelper();
		Element iedNdSrc = srcXmlHelper.selectSingleNode("/SCL/IED[@name='" + oldName + "']");
//		Element iedNdDest = destXmlHelper.selectSingleNode("/SCL/IED[@name='" + srcIedName + "']");
		String srcMd5 = FileManipulate.getMD5CodeForStr(iedNdSrc.asXML());
//		String destMd5 = FileManipulate.getMD5CodeForStr(iedNdDest.asXML());
//		if (!srcMd5.equals(destMd5)) {
//			String desc = CompareUtil.getAttribute(iedNdSrc, "desc");
//			String newdesc = CompareUtil.getAttribute(iedNdDest, "desc");
			iedNdSrc.addAttribute("name", oldName);
//			iedNdDest.addAttribute("name", srcIedName + "_new");
			
			Element commEl = srcXmlHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
			Element dtTypeNd = srcXmlHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
			Context context = new Context(commEl, dtTypeNd);
			
			IedCompareParser srcIedComp = new IedCompareParser(iedNdSrc, context);
//			IedCompareParser destIedComp = new IedCompareParser(iedNdDest, createContext(destXmlHelper));
			srcIedComp.parse();
//			destIedComp.parse();
			srcMd5 = FileManipulate.getMD5Code(srcIedComp.getDomPath());
//			destMd5 = FileManipulate.getMD5Code(destIedComp.getDomPath());
			String rscFilePathOld = srcIedComp.getDomPath();
//		String rscFilePathOld = ProjectManager.getRscFilePath(oldName);
		String rscFilePathNew = ProjectManager.getRscFilePath(newName);
//		String srcMd5 = FileManipulate.getMD5Code(rscFilePathOld);
		String destMd5 = FileManipulate.getMD5Code(rscFilePathNew);
		if (!srcMd5.equals(destMd5)) {
			iedNdSrc = XMLFileManager.loadXMLFile(rscFilePathOld).getRootElement();
			Element iedNdDest = XMLFileManager.loadXMLFile(rscFilePathNew).getRootElement();
			Difference diffNew = new IedCompare(iedNdSrc, iedNdDest).execute();
			diffNew.setName(oldName);
			diffNew.setNewName(newName);
			diffNew.setDesc(iedNdSrc.attributeValue("desc"));
			diffNew.setNewDesc(iedNdDest.attributeValue("desc"));
			diffNew.setOp(OP.RENAME);
			this.diff = diffNew;
		} else {
			this.diff = null;
		}
	}

}
