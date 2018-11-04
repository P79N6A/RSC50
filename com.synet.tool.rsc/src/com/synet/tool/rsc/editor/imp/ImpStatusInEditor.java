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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->开入信号映射表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpStatusInEditor extends ExcelImportEditor {
	
	private PinEntityService pinEntityService;
	private MmsfcdaService mmsfcdaService;
	
	public ImpStatusInEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		pinEntityService = new PinEntityService();
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
		Composite btComp = SwtUtil.createComposite(container, btData, 6);
		btAdd = SwtUtil.createPushButton(btComp, "添加", new GridData());
		btDelete = SwtUtil.createPushButton(btComp, "删除", new GridData());
		btExport = SwtUtil.createPushButton(btComp, "导出原始数据", new GridData());
		btExportCfgData  = SwtUtil.createPushButton(btComp, "导出配置数据", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入开入信号", new GridData());
		table =TableFactory.getStatusInTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM104StatusInEntity.class));
		
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if (obj == titleList) {
					String[] selects = titleList.getSelection();
					if (selects != null && selects.length > 0) {
						loadFileItems(selects[0]);
					}
				} else if (obj == btAdd) {
					addItemByTable(DBConstants.FILE_TYPE104);
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
		};
		titleList.addSelectionListener(listener);
		btAdd.addSelectionListener(listener);
		btDelete.addSelectionListener(listener);
		btExport.addSelectionListener(listener);
		btCheck.addSelectionListener(listener);
		btImport.addSelectionListener(listener);
		btExportCfgData.addSelectionListener(listener);
		
//		titleList.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				String[] selects = titleList.getSelection();
//				if (selects != null && selects.length > 0) {
//					loadFileItems(selects[0]);
//				}
//			}
//		});
//		
//		btExport.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				exportExcel();
//			}
//		});
//		
//		btCheck.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				//冲突检查
//				checkConflict();
//			}
//		});
//		
//		btImport.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				importData();
//			}
//		});
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
						int totalWork = ieds.size() * 4;
						monitor.beginTask("正在导出", totalWork);
						long start = System.currentTimeMillis();
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							monitor.setTaskName("正在导出装置[" + ied.getF1046Name() + "]数据");
							String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
							//导出虚端子部分
							List<Object> vpList = getVpData(ied);
							monitor.worked(1);
							if (vpList.size() > 0) {
								String fileName = filePath + "/" + ied.getF1046Name() + "开入信号映射表(虚端子部分)" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "开入信号映射表", vfields, vpList);
							}
							monitor.worked(1);
							//导出MMS部分
							List<Object> mmsList = getMssData(ied);
							monitor.worked(1);
							if (mmsList.size() > 0) {
								String fileName = filePath + "/" + ied.getF1046Name() + "开入信号映射表(MMS信号部分)" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "开入信号映射表", vfields, mmsList);
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
	
	private List<Object> getVpData(Tb1046IedEntity ied) {
		List<Object> list = new ArrayList<>();
		List<Tb1062PinEntity> pinList = pinEntityService.getByIed(ied);
		if (pinList != null && pinList.size() > 0) {
			for (Tb1062PinEntity pinEntity : pinList) {
				IM104StatusInEntity entity = new IM104StatusInEntity();
				entity.setDevName(ied.getF1046Name());
				entity.setDevDesc(ied.getF1046Desc());
				entity.setPinRefAddr(pinEntity.getF1062RefAddr());
				entity.setPinDesc(pinEntity.getF1062Desc());
				list.add(entity);
			}
		}
		return list;
	}
	
	private List<Object> getMssData(Tb1046IedEntity ied) {
		List<Object> list = new ArrayList<>();
		List<Tb1058MmsfcdaEntity> mmsList = mmsfcdaService.getMmsfcdaByIed(ied);
		if (mmsList != null && mmsList.size() > 0) {
			for (Tb1058MmsfcdaEntity mms : mmsList) {
				IM104StatusInEntity entity = new IM104StatusInEntity();
				entity.setDevName(ied.getF1046Name());
				entity.setDevDesc(ied.getF1046Desc());
				entity.setMmsRefAddr(mms.getF1058RefAddr());
				entity.setMmsDesc(mms.getF1058Desc());
				list.add(entity);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport(IProgressMonitor monitor) {
		List<IM104StatusInEntity> list = (List<IM104StatusInEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		monitor.beginTask("正在导入数据...", list.size() * 2);
		for (IM104StatusInEntity entity : list) {
			if (monitor.isCanceled()) {
				break;
			}
			String devName = entity.getDevName();
			if (devName == null || !entity.isOverwrite()) {
				monitor.worked(1);
				continue;
			}
			String pinRefAddr = entity.getPinRefAddr();
			String devDesc = entity.getDevDesc();
			devDesc = devName + ":" + devDesc;
			if (!StringUtil.isEmpty(pinRefAddr)) {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(devName, pinRefAddr);
				if (pinEntity != null) {
					pinEntity.setF1062Desc(entity.getPinDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
					pinEntityService.update(pinEntity);
				} else {
					String msg = "开入虚端子不存在:" + devDesc + "-> " + pinRefAddr;
					String ref = devName + "->" + pinRefAddr;
					appendError("导入开入信号", "虚端子检查", ref, msg);
				}
			} else {
				String msg = "开入虚端子参引不能为空:装置[" + devDesc+ "]";
				appendError("导入开入信号", "虚端子检查", devName, msg);
			}
			monitor.worked(1);
			String mmsRefAddr = entity.getMmsRefAddr();
			if (!StringUtil.isEmpty(mmsRefAddr)) {
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(devName, mmsRefAddr);
				if (mmsfcdaEntity != null) {
					mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
					mmsfcdaService.update(mmsfcdaEntity);
				} else {
					String msg = "MMS不存在:" + devDesc + "-> " + mmsRefAddr;
					String ref =  devName + "-> " + mmsRefAddr;
					appendError("导入开入信号", "FCDA检查", ref, msg);
				}
			} else {
				String msg = "MMS信号参引不能为空:装置[" + devDesc + "]";
				appendError("导入开入信号", "FCDA检查", devName, msg);
			}
			monitor.worked(1);
		}
		monitor.done();
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE104);
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			List<String> items = new ArrayList<>();
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) {
				map.put(fileInfoEntity.getFileName(), fileInfoEntity);
				items.add(fileInfoEntity.getFileName());
			}
			if (items.size() > 0) {
				titleList.setItems(items.toArray(new String[0]));
				titleList.setSelection(0);
				loadFileItems(items.get(0));
			}
		}
	}
	
	private void loadFileItems(String filename) {
		List<IM104StatusInEntity> list = improtInfoService.getStatusInEntityList(map.get(filename));
		if (list != null && list.size()> 0) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM104StatusInEntity> list = (List<IM104StatusInEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM104StatusInEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity == null || pinEntity.getF1062Desc() != null) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity == null || mmsfcdaEntity.getF1058Desc() != null) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
				entity.setConflict(DBConstants.NO);
				entity.setOverwrite(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object locate(Problem problem) {
		List<IM104StatusInEntity> list = (List<IM104StatusInEntity>) table.getInput();
		if (problem != null && list != null && list.size() > 0) {
			String title = problem.getIedName();
			if ("导入光强与端口".equals(title)) {
				String ref = problem.getRef();
				if (ref != null) {
					if (ref.contains("->")) {
						String[] refs = ref.split("->");
						if (refs.length == 2) {
							String devName = refs[0];
							String refStr = refs[1];
							for (IM104StatusInEntity entity : list) {
								if (devName.equals(entity.getDevName()) 
										&& (refStr.equals(entity.getPinRefAddr()) || refStr.equals(entity.getMmsRefAddr()))) {
									return entity;
								}
							}
						}
					} else {
						for (IM104StatusInEntity entity : list) {
							if (ref.equals(entity.getDevName())) {
								return entity;
							}
						}
					}
				}
			}
		}
		return null;
	}

}
