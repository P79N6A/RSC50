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

import com.shrcn.found.common.event.Context;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.common.event.IEventHandler;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.RscEventConstants;
import com.synet.tool.rsc.UICommonConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.util.ExcelFileManager2007;
import com.synet.tool.rsc.util.ProblemManager;

public abstract class ExcelImportEditor extends BaseConfigEditor implements IEventHandler {

	protected static final String CONFLICT = "conflict";
	protected static final String OVERWITE = "overwrite";
	protected static final String MATCHED = "matched";
	protected IedEntityService iedEntityService;
	protected ImprotInfoService improtInfoService;
	protected Map<String, IM100FileInfoEntity> map;
	protected org.eclipse.swt.widgets.List titleList;
	protected Button btImport;
	protected Button btExport;
	protected Button btExportCfgData;
	protected Button btCheck;
	protected Button btAdd;
	protected Button btDelete;
	//导入信息
	protected ProblemManager pmgr = ProblemManager.getInstance();
	protected int errorCount;
	protected int warningCount; 
	protected RSCProperties rscp = RSCProperties.getInstance();

	public ExcelImportEditor(Composite container, IEditorInput input) {
		super(container, input);
		EventManager.getDefault().registEventHandler(this);
	}
	
	@Override
	public void dispose() {
		EventManager.getDefault().removeEventHandler(this);
		super.dispose();
	}
	
	@Override
	public void init() {
		super.init();
		iedEntityService = new IedEntityService();
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
			public void run(final IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						clearMsgCount();
						doImport(monitor);
//						console.append(titleList.getSelection()[0] + "导入完毕！");
						printMsg(titleList.getSelection()[0] + "导入完毕");
					}
				});
			}
		});
	}
	
	protected void doImport(IProgressMonitor monitor){
		
	}
	
	/**
	 * 清除导入异常记录
	 */
	protected void clearMsgCount() {
		errorCount = 0;
		warningCount = 0;
	}
	
	protected void appendError(String title, String subType, String msg) {
		appendError(title, subType, "", msg);
	}
	
	protected void appendError(String title, String subType, String ref, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.ERROR, title, subType, ref, msg));
		errorCount++;
	}
	
	protected void appendWarning(String title, String subType, String msg) {
		appendWarning(title, subType, "", msg);
	}
	
	protected void appendWarning(String title, String subType, String ref, String msg) {
		SCTLogger.error(msg);
		pmgr.append(new Problem(0, LEVEL.WARNING, title, subType, ref, msg));
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
				if (CONFLICT.equals(field.getName()) || OVERWITE.equals(field.getName())
						|| MATCHED.equals(field.getName())) {
					continue;
				}
				list.add(field);
			}
		}
		return list.toArray(new IField[0]);
	}
	
	public void exportTemplateExcel(String fileName, String title, IField[] vfields, List<Object> list) {
		long start = System.currentTimeMillis();
		Map<String, String> mapTitle = new LinkedHashMap<String, String>();
		mapTitle.put("key0", title);
		Map<String, String> mapSep = new LinkedHashMap<String, String>();
		mapSep.put("key0", "");
		Map<String, String> mapTime = new LinkedHashMap<String, String>();
		mapTime.put("printDate", StringUtil.getCurrentTime("yyyy年MM月dd日HH点mm分ss秒"));
		
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
					if ("index".equals(fieldName)) {
						row[i] = "" + index;
					} else {
						Object obj = ObjectUtil.getProperty(o, fieldName);
						if (obj == null) {
							obj = "";
						}
						row[i] = "" + obj;
					}
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
		console.append(fileName + "导出完成");
		System.out.println("导出耗时：" + time + "秒");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Context context) {
		if (RscEventConstants.SELECT_PROBLEM.equals(context.getEventName())) {
			table.clearSelections();
			List<Problem> problems = (List<Problem>) context.getData();
			List<Object> selections = new ArrayList<>();
			for (Problem problem : problems) {
				Object target = locate(problem);
				if (target != null) {
					selections.add(target);
				}
			}
			table.setSelections(selections);
		}
	}

	protected abstract Object locate(Problem problem);
	
	protected void deleteItemsByTable() {
		List<Object> list = table.getSelections();
		if (list != null && list.size() > 0) {
			for (Object o : list) {
				improtInfoService.delete(o);
			}
		}
		table.removeSelected();
	}
	
	protected void addItemByTable(int fileType) {
		String fileName = null;
		String[] selects = titleList.getSelection();
		if (selects != null && selects.length > 0) {
			fileName =selects[0];
		}
		if (fileName == null) return;
		IM100FileInfoEntity fileInfoEntity = map.get(fileName);
		if (fileInfoEntity == null) return;
		
		switch (fileType) {
		case DBConstants.FILE_TYPE101:
			IM101IEDListEntity entity101 = new IM101IEDListEntity();
			entity101.setFileInfoEntity(fileInfoEntity);
			entity101.setIm101Code(rscp.nextTbCode(DBConstants.PR_IEDLIST));
			entity101.setMatched(DBConstants.MATCHED_NO);
			entity101.setConflict(DBConstants.NO);
			addDbAndTable(entity101);
			break;
		case DBConstants.FILE_TYPE102:
			IM102FibreListEntity entity102 = new IM102FibreListEntity();
			entity102.setFileInfoEntity(fileInfoEntity);
			entity102.setIm102Code(rscp.nextTbCode(DBConstants.PR_FIBRELIST));
			entity102.setMatched(DBConstants.MATCHED_NO);
			entity102.setConflict(DBConstants.NO);
			addDbAndTable(entity102);
			break;
		case DBConstants.FILE_TYPE103:
			IM103IEDBoardEntity entity103 = new IM103IEDBoardEntity();
			entity103.setFileInfoEntity(fileInfoEntity);
			entity103.setIm103Code(rscp.nextTbCode(DBConstants.PR_IEDBOARD));
			entity103.setMatched(DBConstants.MATCHED_NO);
			addDbAndTable(entity103);
			break;
		case DBConstants.FILE_TYPE104:
			IM104StatusInEntity entity104 = new IM104StatusInEntity();
			entity104.setFileInfoEntity(fileInfoEntity);
			entity104.setIm104Code(rscp.nextTbCode(DBConstants.PR_STATUSIN));
			entity104.setMatched(DBConstants.MATCHED_NO);
			entity104.setConflict(DBConstants.NO);
			addDbAndTable(entity104);
			break;
		case DBConstants.FILE_TYPE105:
			IM105BoardWarnEntity entity105 = new IM105BoardWarnEntity();
			entity105.setFileInfoEntity(fileInfoEntity);
			entity105.setIm105Code(rscp.nextTbCode(DBConstants.PR_BOARDWARN));
			entity105.setMatched(DBConstants.MATCHED_NO);
			entity105.setConflict(DBConstants.NO);
			addDbAndTable(entity105);
			break;
		case DBConstants.FILE_TYPE106:
			IM106PortLightEntity entity106 = new IM106PortLightEntity();
			entity106.setFileInfoEntity(fileInfoEntity);
			entity106.setIm106Code(rscp.nextTbCode(DBConstants.PR_PORTLIGHT));
			entity106.setMatched(DBConstants.MATCHED_NO);
			entity106.setConflict(DBConstants.NO);
			addDbAndTable(entity106);
			break;
		case DBConstants.FILE_TYPE107:
			IM107TerStrapEntity entity107 = new IM107TerStrapEntity();
			entity107.setFileInfoEntity(fileInfoEntity);
			entity107.setIm107Code(rscp.nextTbCode(DBConstants.PR_TERSTRAP));
			entity107.setMatched(DBConstants.MATCHED_NO);
			entity107.setConflict(DBConstants.NO);
			addDbAndTable(entity107);
			break;
		case DBConstants.FILE_TYPE108:
			IM108BrkCfmEntity entity108 = new IM108BrkCfmEntity();
			entity108.setFileInfoEntity(fileInfoEntity);
			entity108.setIm108Code(rscp.nextTbCode(DBConstants.PR_BRKCFM));
			entity108.setMatched(DBConstants.MATCHED_NO);
			entity108.setConflict(DBConstants.NO);
			addDbAndTable(entity108);
			break;
		case DBConstants.FILE_TYPE109:
			IM109StaInfoEntity entity109 = new IM109StaInfoEntity();
			entity109.setFileInfoEntity(fileInfoEntity);
			entity109.setIm109Code(rscp.nextTbCode(DBConstants.PR_STAINFO));
			entity109.setMatched(DBConstants.MATCHED_NO);
			entity109.setConflict(DBConstants.NO);
			addDbAndTable(entity109);
			break;

		default:
			break;
		}
	}
	
	//导出配置后的数据
	@SuppressWarnings("unchecked")
	protected void exportProcessorData() {
		final String fileName = DialogHelper.getSaveFilePath("保存", "", new String[]{"*.xlsx"});
		if (fileName == null) {
			return;
		}
		final List<Object> list = (List<Object>) table.getInput();
		if (list == null || list.size() <= 0) {
			console.append("导出数据不能为空");
			return;
		}
		ProgressManager.execute(new IRunnableWithProgress() {

			@Override
			public void run(final IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.setTaskName("正在导出数据...");
				try {
					exportTemplateExcel(fileName, table.getTableDesc(), getExportFields(), list);
				} catch (Exception e) {
					console.append("导出失败！");
				}

			}

		});
	}
	
	private void addDbAndTable(Object obj) {
		improtInfoService.save(obj);
		table.addRow(obj);
	}
}