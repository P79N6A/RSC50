package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.excelutils2007.ExcelUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import com.shrcn.found.ui.model.FieldBase;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.RscEventConstants;
import com.synet.tool.rsc.UICommonConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.excel.EnumFileType;
import com.synet.tool.rsc.excel.ImportConfig;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
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
	protected String fileType;
	protected ImportConfig config;

	public ExcelImportEditor(Composite container, IEditorInput input) {
		super(container, input);
		EventManager.getDefault().registEventHandler(this);
	}
	
	@Override
	public void initData() {
		this.fileType = input.getEditorName();
		this.config = ImportConfigFactory.getImportConfig(fileType);
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(EnumFileType.getByTitle(fileType).getId());
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				IEditorInput editinput = getInput();
				int sel = 0;
				Object data = editinput.getData();
				if (data != null && data instanceof String) {
					String filename = (String) data;
					sel = items.indexOf(filename);
				}
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(sel);
				loadFileItems(items.get(sel));
			}
		}
	}
	
	class EditorSelectListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			Object obj = e.getSource();
			if (obj == titleList) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			} else if (obj == btAdd) {
				addItemByTable();
			} else if (obj == btDelete) {
				deleteItemsByTable();
			} else if (obj == btExport) {
				exportExcel();
			} else if (obj == btCheck) {
				//冲突检查
				checkConflict();
			} else if (obj == btImport) {
				importData();
			} else if (obj == btExportCfgData) {
				exportProcessorData();
			}
			
		}
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, config.getEntityClass()));
		SelectionListener listener = new EditorSelectListener();
		titleList.addSelectionListener(listener);
		if (btAdd != null)
			btAdd.addSelectionListener(listener);
		if (btDelete != null)
			btDelete.addSelectionListener(listener);
		if (btExport != null)
			btExport.addSelectionListener(listener);
		if (btCheck != null)
			btCheck.addSelectionListener(listener);
		if (btImport != null)
			btImport.addSelectionListener(listener);
		if (btExportCfgData != null)
			btExportCfgData.addSelectionListener(listener);
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
			FieldBase datField = new FieldBase();
			datField.setName("f1011No");
			datField.setTitle("数据类型");
			list.add(datField);
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
	
	protected abstract void exportExcel();
	
	protected void loadFileItems(String filename) {
		IM100FileInfoEntity fileInfoEntity = map.get(filename);
		if (fileInfoEntity == null) {
			DialogHelper.showAsynError("文名错误！");
		} else {
			List<?> list = improtInfoService.getFileItems(config.getEntityClass(), fileInfoEntity);
			if (list != null) {
				table.setInput(list);
			}
		}
	}
	
	protected void deleteItemsByTable() {
		List<Object> list = table.getSelections();
		if (list != null && list.size() > 0) {
			for (Object o : list) {
				improtInfoService.delete(o);
			}
		}
		table.removeSelected();
	}
	
	protected void addItemByTable() {
		String fileName = null;
		String[] selects = titleList.getSelection();
		if (selects != null && selects.length > 0) {
			fileName =selects[0];
		}
		if (fileName == null) 
			return;
		IM100FileInfoEntity fileInfoEntity = map.get(fileName);
		if (fileInfoEntity == null) 
			return;
		Object entity = config.createEntity();
		ObjectUtil.setProperty(entity, "fileInfoEntity", fileInfoEntity);
		ObjectUtil.setProperty(entity, "matched", DBConstants.MATCHED_NO);
		ObjectUtil.setProperty(entity, "conflict", DBConstants.NO);
		addDbAndTable(entity);
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