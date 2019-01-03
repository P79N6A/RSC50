package com.synet.tool.rsc.incr;

import java.io.File;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.util.ProjectFileManager;

public class IncrementImportor extends BaseIncreImportor {
	
	public IncrementImportor(IProgressMonitor monitor, List<Difference> diffs) {
		super(monitor, diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		if (!initXmldb()) {
			DialogHelper.showAsynWarning("未找到增量导入临时文件，导入终止！");
			return;
		}
		Difference diffFirst = diffs.get(0);
		if ("Substation".equals(diffFirst.getType())) {
			IConflictHandler handler = ConflictHandlerFactory.createConflict(EnumConflict.Sta, diffFirst);
			handler.setMonitor(monitor);
			handler.handle();
		} else {
			monitor.beginTask("开始增量导入SCD配置", diffs.size() + 1);
			for (Difference diff : diffs) {
				Element commEl = XMLDBHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
				Element dtTypeNd = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
				Context context = new Context(commEl, dtTypeNd);
				IEDConflictHandler handler = new IEDConflictHandler(diff, context);
				handler.setMonitor(monitor);
				handler.handle();
			}
			monitor.done();
		}
	}
	
	private boolean initXmldb() {
		Difference diffFirst = diffs.get(0);
		String currPath = null;
		String tempPath = null;
		if ("Substation".equals(diffFirst.getType())) {
			currPath = ProjectManager.getInstance().getProjectSsdPath();
			tempPath = currPath + ".bak";
		} else {
			currPath = ProjectManager.getInstance().getProjectScdPath();
			tempPath = currPath + ".bak";
		}
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			return false;
		}
		File currFile = new File(currPath);
		currFile.delete();
		new File(tempPath).renameTo(currFile);
		// 更新SCD、SSD
		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, currPath);
		ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, currPath);
		String scddir = ProjectManager.getInstance().getProjectCidPath();
		FileManipulate.initDir(scddir);
		return true;
	}
}
