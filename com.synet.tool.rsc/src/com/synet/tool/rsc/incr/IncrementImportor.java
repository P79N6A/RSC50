package com.synet.tool.rsc.incr;

import java.io.File;
import java.util.List;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.VTDXMLDBHelper;
import com.synet.tool.rsc.compare.Difference;
import com.synet.tool.rsc.compare.XmlHelperCache;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.incr.scd.IEDConflictHandler;
import com.synet.tool.rsc.io.ied.Context;

public class IncrementImportor extends BaseIncreImportor {
	
	public IncrementImportor(IProgressMonitor monitor, List<Difference> diffs) {
		super(monitor, diffs);
	}
	
	public void handle() {
		if (diffs == null || diffs.size() < 1) {
			return;
		}
		Difference diffFirst = diffs.get(0);
		if ("Substation".equals(diffFirst.getType())) {
			IConflictHandler handler = ConflictHandlerFactory.createConflict(EnumConflict.Sta, diffFirst);
			handler.setMonitor(monitor);
			handler.handle();
		} else {
			monitor.beginTask("开始增量导入SCD配置", diffs.size() + 1);
			Context destContext = XmlHelperCache.getInstance().getDestContext();
			for (Difference diff : diffs) {
				monitor.setTaskName("正在处理" + diff.getName());
				IEDConflictHandler handler = new IEDConflictHandler(diff, destContext);
				handler.setMonitor(monitor);
				handler.handle();
				monitor.worked(1);
			}
			monitor.done();
		}
		resetSclFile();
	}
	
	/**
	 * 替换当前scd或ssd
	 * @return
	 */
	private boolean resetSclFile() {
		Difference diffFirst = diffs.get(0);
		String currPath = null;
		String tempPath = null;
		ProjectManager prjMgr = ProjectManager.getInstance();
		if ("Substation".equals(diffFirst.getType())) {
			currPath = prjMgr.getProjectSsdPath();
			tempPath = prjMgr.getProjectSsdTempPath();
		} else {
			currPath = prjMgr.getProjectScdPath();
			tempPath = prjMgr.getProjectScdTempPath();
		}
		File tempFile = new File(tempPath);
		if (!tempFile.exists()) {
			return false;
		}
		File currFile = new File(currPath);
		currFile.delete();
		new File(tempPath).renameTo(currFile);
//		// 更新SCD、SSD
//		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, currPath);
//		ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
//		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, currPath);
//		String scddir = ProjectManager.getInstance().getProjectCidPath();
//		FileManipulate.initDir(scddir);
		return true;
	}
}
