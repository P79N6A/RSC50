package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.UICommonConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.util.ExcelFileManager2007;
import com.synet.tool.rsc.util.ProblemManager;

public class ExcelImportEditor extends BaseConfigEditor {

	protected static final String CONFLICT = "conflict";
	protected static final String OVERWITE = "overwrite";
	protected IedEntityService iedEntityService;
	protected ImprotInfoService improtInfoService;
	protected Map<String, IM100FileInfoEntity> map;
	protected org.eclipse.swt.widgets.List titleList;
	protected Button btImport;
	protected Button btExport;
	protected Button btCheck;
	//导入信息
	protected ProblemManager pmgr = ProblemManager.getInstance();
	protected int errorCount;
	protected int warningCount; 

	public ExcelImportEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		iedEntityService = new IedEntityService();
		super.init();
	}

	@Override
	public void refresh() {
		initData();
	}
	
	/**
	 * 冲突检查
	 */
	protected void checkConflict() {
		ProgressManager.execute(new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						checkData();
						table.refresh();
					}
				});
			}
		});
	}
	
	protected void checkData() {
		
	}
	
	/**
	 * 导入数据
	 */
	protected void importData() {
		ProgressManager.execute(new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						clearMsgCount();
						doImport();
//						console.append(titleList.getSelection()[0] + "导入完毕！");
						printMsg(titleList.getSelection()[0] + "导入完毕");
					}
				});
			}
		});
	}
	
	protected void doImport(){
		
	}
	
	/**
	 * 清除导入异常记录
	 */
	protected void clearMsgCount() {
		errorCount = 0;
		warningCount = 0;
	}
	
	protected void appendError(String title, String subType, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.ERROR, title, subType, "", msg));
		errorCount++;
	}
	
	protected void appendWarning(String title, String subType, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.WARNING, title, subType, "", msg));
		warningCount++;
	}
	
	/**
	 * 打印导入异常信息
	 */
	protected void printMsg(String msg) {
		if (msg == null) 
			msg ="";
		if (errorCount != 0) {
			msg += "，错误：" + errorCount;
		}
		if (warningCount != 0) {
			msg += "，警告：" + warningCount;
		}
		msg += "。";
		console.append(msg);
	}
	
	protected IField[] getExportFields() {
		List<IField> list = new ArrayList<>();
		IField[] fields = table.getExportFields();
		if (fields != null) {
			for (IField field : fields) {
				if (CONFLICT.equals(field.getName()) || OVERWITE.equals(field.getName())) {
					continue;
				}
				list.add(field);
			}
		}
		return list.toArray(new IField[0]);
	}
	
	public void exportTemplateExcel(String fileName, String title, IField[] vfields, List<Object> list) {
//		String fileName = DialogHelper.getSaveFilePath("保存", "", new String[]{"*.xlsx"});
//		if (fileName == null)
//			return;
		long start = System.currentTimeMillis();
		Map<String, String> mapTitle = new LinkedHashMap<String, String>();
		mapTitle.put("key0", title);
		Map<String, String> mapSep = new LinkedHashMap<String, String>();
		mapSep.put("key0", "");
		Map<String, String> mapTime = new LinkedHashMap<String, String>();
		mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
		
//		IField[] vfields = getExportFields();
		int fLen = vfields.length;
		String[] fields = new String[fLen];
		for (int i = 0; i < fields.length; i++) {
			IField f = vfields[i];
			fields[i] = f.getTitle();
		}
		
		List<String[]> exportData = new ArrayList<String[]>();
		if (list != null) {
			int index = 1;
			for (Object o : list) {
				String[] row = new String[fLen];
				for (int i=0; i<fLen; i++) {
					IField f = vfields[i];
					String fieldName = f.getName();
					if ("index".equals(fieldName))
						row[i] = "" + index;
					else
						row[i] = "" + ObjectUtil.getProperty(o, fieldName);
				}
				exportData.add(row);
				index++;
			}
		}
		
		ExcelUtils.addValue("title", mapTitle);
		ExcelUtils.addValue("sep", mapSep);
		ExcelUtils.addValue("time", mapTime);
		ExcelUtils.addValue("width", fLen - 1);
		ExcelUtils.addValue("fields", fields);
		ExcelUtils.addValue("data", exportData);
		ExcelFileManager2007.saveExcelFile(getClass(), UICommonConstants.EXCEL_COMM_EXPORT_2007, fileName);
		long time = (System.currentTimeMillis() - start) / 1000;
		System.out.println("导出耗时：" + time + "秒");
	}
}