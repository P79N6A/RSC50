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
import java.util.Map;

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
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->光强与端口关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpPortLightEditor extends ExcelImportEditor {
	
	private PortEntityService portEntityService;
	private MmsfcdaService mmsfcdaService;
	private AnalogdataService analogdataService;
	
	public ImpPortLightEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		portEntityService = new PortEntityService();
		mmsfcdaService = new MmsfcdaService();
		analogdataService = new AnalogdataService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		gridData.verticalSpan = 3;
		titleList = SwtUtil.createList(container, gridData);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		
		Composite btComp = SwtUtil.createComposite(container, btData, 3);
		btExport = SwtUtil.createPushButton(btComp, "导出Excel", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入光强与端口", new GridData());
		table =TableFactory.getPortLightTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM106PortLightEntity.class));
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
									IM106PortLightEntity entity = new IM106PortLightEntity();
									entity.setDevName(ied.getF1046Name());
									entity.setDevDesc(ied.getF1046Desc());
									entity.setOpticalRefAddr(mms.getF1058RefAddr());
									Tb1006AnalogdataEntity analogdataEntity = (Tb1006AnalogdataEntity) 
											analogdataService.getById(Tb1006AnalogdataEntity.class, mms.getDataCode());
									if (analogdataEntity != null) {
										entity.setOpticalDesc(analogdataEntity.getF1006Desc());
										if (analogdataEntity.getParentCode() != null) {
											Tb1048PortEntity portEntity = (Tb1048PortEntity) portEntityService
													.getById(Tb1048PortEntity.class, analogdataEntity.getParentCode());
											if (portEntity != null) {
												entity.setBoardCode(portEntity.getTb1047BoardByF1047Code().getF1047Slot());
												entity.setPortCode(portEntity.getF1048No());
											}
										}
									}
									list.add(entity);
								}
							}
							if (list.size() > 0) {
								String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
								String fileName = filePath + "/" + ied.getF1046Name() + "光强与端口关联表" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "光强与端口关联表", vfields, list);
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
		List<IM106PortLightEntity> list = (List<IM106PortLightEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		try {
			Map<String, String> portLights = new HashMap<>();
			HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
			Map<String, Object> params = new HashMap<>();
			for (IM106PortLightEntity entity : list) {
				if (!entity.isOverwrite()) {
					continue;
				}
				String devName = entity.getDevName();
				String boardCode = entity.getBoardCode();
				String portCode = entity.getPortCode();
				String opticalRefAddr = entity.getOpticalRefAddr();
				
				params.clear();
				params.put("f1046Name", devName);
				params.put("f1058RefAddr", opticalRefAddr);
				String hql = "from " + Tb1058MmsfcdaEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:f1046Name and f1058RefAddr=:f1058RefAddr";
				List<?> mmsList = hqlDao.getListByHql(hql, params);
				if (mmsList!=null && mmsList.size()>0) {
					Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) mmsList.get(0);
					if (mmsfcdaEntity != null) {
						Tb1006AnalogdataEntity analogdataEntity = (Tb1006AnalogdataEntity) 
								analogdataService.getById(Tb1006AnalogdataEntity.class, mmsfcdaEntity.getDataCode());
						String endMsg = devName + "->" + boardCode + "->" + portCode;
						if (analogdataEntity != null) {
							String ref = portLights.get(endMsg);
							if (ref != null) {
								if (!ref.equals(opticalRefAddr)) {
									String msg = "同一端口不允许关联多个光强：" + endMsg;
									appendError("导入光强与端口", "FCDA数据点检查", msg);
									// 回复parentCode为ied
									analogdataEntity.setParentCode(analogdataEntity.getTb1046IedByF1046Code().getF1046Code());
									analogdataService.update(analogdataEntity);
								}
							} else {
								Tb1048PortEntity portEntity = portEntityService.getPortEntity(devName, boardCode, portCode);
								if (portEntity != null) {
									analogdataEntity.setParentCode(portEntity.getF1048Code());
									analogdataService.update(analogdataEntity);
									portLights.put(endMsg, opticalRefAddr);
									entity.setMatched(DBConstants.MATCHED_OK);
								} else {
									String msg = "装置板卡端口不存在：" + endMsg;
									appendError("导入光强与端口", "端口检查", msg);
								}
							}
						} else {
							String msg = "装置[" + devName + "]不存在Mms信号：" + opticalRefAddr;
							appendError("导入光强与端口", "FCDA检查", msg);
						}
					}
				} else {
					String msg = "装置[" + devName + "]不存在Mms信号：" + opticalRefAddr;
					appendError("导入光强与端口", "FCDA检查", msg);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE106);
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
		List<IM106PortLightEntity> list = improtInfoService.getPortLightEntityList(map.get(filename));
		if (list != null && list.size()> 0) {
			table.setInput(list);
		}
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM106PortLightEntity> list = (List<IM106PortLightEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM106PortLightEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevName(), 
						entity.getBoardCode(), entity.getPortCode());
				if (portEntity != null) {
					Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getOpticalRefAddr());
					if (mmsfcdaEntity != null) {
						Tb1006AnalogdataEntity analogdataEntity = (Tb1006AnalogdataEntity) analogdataService.getById(Tb1006AnalogdataEntity.class,
								mmsfcdaEntity.getDataCode());
						if (analogdataEntity != null && analogdataEntity.getParentCode() == null) {
							entity.setConflict(DBConstants.NO);
							entity.setOverwrite(true);
							continue;
						} 
					}
				}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Object locate(Problem problem) {
		return null;
	}

}
