/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.action;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.table.VTTableField;
import com.shrcn.business.scl.table.VTViewTable;
import com.shrcn.business.scl.table.VTViewTableModel;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.excel.ExcelFileManager;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.util.FileDialogHelper;
import com.shrcn.found.ui.util.TaskManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-8-28
 */
/**
 * $Log: VTExcelAction.java,v $
 * Revision 1.2  2013/07/09 03:58:39  cchun
 * Fix Bug:修复excel导出错误
 *
 * Revision 1.1  2013/06/28 08:45:26  cchun
 * Update:添加模型检查和虚端子关联查看
 *
 * Revision 1.1  2011/08/28 06:28:20  cchun
 * Add:添加虚端子报表导出功能
 *
 */
public class VTExcelAction extends Action {

	private VTViewTableModel model;
	
	public VTExcelAction(VTViewTable table) {
		setText("导出&Excel");
		this.model = (VTViewTableModel)table.getModel();
	}
	
	@Override
	public void run() {
		Shell shell = Display.getDefault().getActiveShell();
		final String fileName = FileDialogHelper.selectExcelFile(shell);
		if (fileName == null)
			return;
		TaskManager.addTask(new Job("导出虚端子表格") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Map<String, String> mapTitle = new LinkedHashMap<String, String>();
				mapTitle.put("aaa", model.getIedName() + "虚端子报告");
				Map<String, String> mapSep = new LinkedHashMap<String, String>();
				mapSep.put("aaa", "");
				Map<String, String> mapTime = new LinkedHashMap<String, String>();
				mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
				
				VTTableField[] vfields = model.getFields();
				String[] fields = new String[vfields.length];
				for (int i=0; i<fields.length; i++) {
					VTTableField f = vfields[i];
					fields[i] = f.getHead();
				}
				
				List<String[]> exportData = getData();
				
				ExcelUtils.addValue("title", mapTitle);
				ExcelUtils.addValue("sep", mapSep);
				ExcelUtils.addValue("time", mapTime);
				ExcelUtils.addValue("width", fields.length - 1);
				ExcelUtils.addValue("fields", fields);
				ExcelUtils.addValue("data", exportData);
				
				File tplFile = new File(SCLConstants.EXCEL_FILE_VT);
				if (!tplFile.exists()) {
					FileManipulate.copyResource(getClass(), SCLConstants.EXCEL_PKG_VT, Constants.cfgDir, SCLConstants.EXCEL_NAME_VT);
				}
				ExcelFileManager.saveExcelFile(SCLConstants.EXCEL_FILE_VT, fileName);
				return Status.OK_STATUS;
			}
	    });
	}
	
	private List<String[]> getData() {
		List<String[]> exportData = new ArrayList<String[]>();
		int rowCount = model.doGetRowCount();
		int colCount = model.doGetColumnCount();
		for (int i=1; i<rowCount; i++) {
			String[] row = new String[colCount];
			for (int j=0; j<colCount; j++) {
				row[j] = "" + model.doGetContentAt(j, i);
			}
			exportData.add(row);
		}
		return exportData;
	}
}
