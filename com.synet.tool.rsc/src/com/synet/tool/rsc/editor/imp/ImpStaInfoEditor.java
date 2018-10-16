/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->监控信息点表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpStaInfoEditor extends ExcelImportEditor {
	
	private MmsfcdaService mmsfcdaService;
	
	public ImpStaInfoEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		mmsfcdaService = new MmsfcdaService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 2;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		Composite btComp = SwtUtil.createComposite(container, btData, 3);
		btExport = SwtUtil.createPushButton(btComp, "导出Excel", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入监控信息", new GridData());
		table =TableFactory.getStaInfoTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	@Override
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM109StaInfoEntity.class));
		titleList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String[] selects = titleList.getSelection();
				if (selects != null && selects.length > 0) {
					loadFileItems(selects[0]);
				}
			}
		});
		
		btExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exportExcel();
			}
		});
		
		btCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//冲突检查
				checkConflict();
			}
		});
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				importData();
			}
		});
	}

	protected void exportExcel() {
		ExportIedDialog dialog = new ExportIedDialog(getShell());
		if (dialog.open() == IDialogConstants.OK_ID) {
			final String filePath = dialog.getFilePath();
			final List<Tb1046IedEntity> ieds = dialog.getIeds();
			if (filePath == null) return;
			ProgressManager.execute(new IRunnableWithProgress() {
				
				@Override
				public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
					IField[] vfields = getExportFields();
					if (ieds != null && ieds.size() > 0) {
						monitor.beginTask("开始导出", ieds.size());
						long start = System.currentTimeMillis();
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							monitor.setTaskName("正在导出装置[" + ied.getF1046Name() + "]数据");
							List<Object> list = new ArrayList<>();
							List<Tb1058MmsfcdaEntity> mmsList = mmsfcdaService.getMmsfcdaByIed(ied);
							if (mmsList != null && mmsList.size() > 0) {
								for (Tb1058MmsfcdaEntity mms : mmsList) {
									IM109StaInfoEntity staInfoEntity = new IM109StaInfoEntity();
									staInfoEntity.setDevName(ied.getF1046Name());
									staInfoEntity.setDevDesc(ied.getF1046Desc());
									staInfoEntity.setMmsRefAddr(mms.getF1058RefAddr());
									staInfoEntity.setMmsDesc(mms.getF1058Desc());
									list.add(staInfoEntity);
								}
							}
							if (list.size() > 0) {
								String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
								String fileName = filePath + "/" + ied.getF1046Name() + "监控信息点表" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "监控信息点表", vfields, list);
							}
							monitor.worked(1);
						}
						long time = (System.currentTimeMillis() - start) / 1000;
						monitor.setTaskName("导出耗时：" + time + "秒");
						monitor.done();
					}
				}
			}, true);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport() {
		List<IM109StaInfoEntity> list = (List<IM109StaInfoEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM109StaInfoEntity entity : list) {
			if (!entity.isOverwrite())
				continue;
			Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
			if (mmsfcdaEntity != null) {
				mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
			} else {
				String msg = "Mmsfcda不存在：" + entity.getDevName() + "->" + entity.getMmsRefAddr();
				appendError("导入监控信息", "FCDA检查", msg);
			}
			mmsfcdaService.update(mmsfcdaEntity);
			improtInfoService.update(entity);
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE109);
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
				loadFileItems(items.get(0));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		List<IM109StaInfoEntity> list = improtInfoService.getStaInfoEntityList(map.get(filename));
		if (list != null) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM109StaInfoEntity> list = (List<IM109StaInfoEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM109StaInfoEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity != null) {
					if (mmsfcdaEntity.getF1058Desc() == null || "".equals(mmsfcdaEntity.getF1058Desc())) {
						entity.setConflict(DBConstants.NO);
						entity.setOverwrite(true);
						continue;
					} 
				}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
