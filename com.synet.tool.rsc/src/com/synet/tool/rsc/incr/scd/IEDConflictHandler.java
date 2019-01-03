package com.synet.tool.rsc.incr.scd;

import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.ied.IedCompare;
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
		Element iedNd = XMLDBHelper.selectSingleNode(SCL.getIEDXPath(iedName));
		IedParserNew iedParser = new IedParserNew(iedNd, context, null);
		iedParser.parse();
	}

	@Override
	public void setData() {
		String iedName = diff.getName();
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
		String rscFilePathOld = ProjectManager.getRscFilePath(oldName);
		String rscFilePathNew = ProjectManager.getRscFilePath(newName);
		String srcMd5 = FileManipulate.getMD5Code(rscFilePathOld);
		String destMd5 = FileManipulate.getMD5Code(rscFilePathNew);
		if (!srcMd5.equals(destMd5)) {
			Element iedNdSrc = XMLFileManager.loadXMLFile(rscFilePathOld).getRootElement();
			Element iedNdDest = XMLFileManager.loadXMLFile(rscFilePathNew).getRootElement();
			Difference diff = new IedCompare(iedNdSrc, iedNdDest).execute();
			diff.setName(oldName);
			diff.setNewName(newName);
			diff.setDesc(iedNdSrc.attributeValue("desc"));
			diff.setNewDesc(iedNdDest.attributeValue("desc"));
			this.diff = diff;
		} else {
			this.diff = null;
		}
	}

}
