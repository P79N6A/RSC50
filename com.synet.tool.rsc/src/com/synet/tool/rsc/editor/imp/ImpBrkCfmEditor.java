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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.service.CircuitEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PinEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->跳合闸反校关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpBrkCfmEditor extends ExcelImportEditor {
	
	private PinEntityService pinEntityService;
	private PoutEntityService poutEntityService;
	private CircuitEntityService circuitEntityService;
	
	public ImpBrkCfmEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		pinEntityService = new PinEntityService();
		poutEntityService = new PoutEntityService();
		circuitEntityService = new CircuitEntityService();
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
		btImport = SwtUtil.createPushButton(btComp, "导入跳合闸反校", new GridData());
		
		table =TableFactory.getBrkCfmTable(container);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
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
//						monitor.beginTask("开始导出", ieds.size());
						long start = System.currentTimeMillis();
						int totalWork = ieds.size() * 2;
						monitor.beginTask("正在导出", totalWork);
						for (Tb1046IedEntity ied : ieds) {
							if (monitor.isCanceled()) {
								break;
							}
							monitor.setTaskName("正在导出装置[" + ied.getF1046Name() + "]数据");
							List<Object> list = new ArrayList<>();
							List<Tb1062PinEntity> pinList = pinEntityService.getByIed(ied);
							if (pinList != null && pinList.size() <= 0) continue; 
							for (Tb1062PinEntity pinEntity : pinList) {
								IM108BrkCfmEntity entity = new IM108BrkCfmEntity();
								entity.setDevName(ied.getF1046Name());
								entity.setDevDesc(ied.getF1046Desc());
								entity.setPinRefAddr(pinEntity.getF1062RefAddr());
								entity.setPinDesc(pinEntity.getF1062Desc());
								Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
								if (circuitEntity != null) {
									Tb1061PoutEntity poutEntity1 = circuitEntity.getTb1061PoutByF1061CodeConvChk1();
									Tb1061PoutEntity poutEntity2 = circuitEntity.getTb1061PoutByF1061CodeConvChk2();
									if (poutEntity1 != null) {
										entity.setCmdAckVpRefAddr(poutEntity1.getF1061RefAddr());
										entity.setCmdAckVpDesc(poutEntity1.getF1061Desc());
									}
									if (poutEntity2 != null) {
										entity.setCmdOutVpRefAddr(poutEntity2.getF1061RefAddr());
										entity.setCmdOutVpDesc(poutEntity2.getF1061Desc());
									}
								}
								list.add(entity);
							}
							monitor.worked(1);
							if (list.size() > 0) {
								String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
								String fileName = filePath + "/" + ied.getF1046Name() + "跳合闸反校关联表" + dateStr + ".xlsx";
								exportTemplateExcel(fileName, "跳合闸反校关联表", vfields, list);
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
	protected void doImport(IProgressMonitor monitor) {
		List<IM108BrkCfmEntity> list = (List<IM108BrkCfmEntity>) table.getInput();
		if(list == null || list.size() <= 0) return;
		monitor.beginTask("正在导入数据...", list.size());
		for (IM108BrkCfmEntity entity : list) {
			if (monitor.isCanceled()) {
				break;
			}
			if (!entity.isOverwrite()){
				monitor.worked(1);
				continue;
			}
			try {
				String ref = entity.getDevName() + "->" + entity.getPinRefAddr();
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity != null) {
					Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
					if (circuitEntity != null) {
						Tb1061PoutEntity poutEntity1 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdAckVpRefAddr());
						if (poutEntity1 != null) {
							circuitEntity.setTb1061PoutByF1061CodeConvChk1(poutEntity1);
							entity.setMatched(DBConstants.MATCHED_OK);
						} else {
							String msg = "命令确认虚端子不存在：" + entity.getDevName() + "->" + entity.getCmdAckVpRefAddr();
							appendError("导入跳合闸反校", "虚端子检查", ref, msg);
						}
						Tb1061PoutEntity poutEntity2 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdOutVpRefAddr());
						if (poutEntity2 != null) {
							circuitEntity.setTb1061PoutByF1061CodeConvChk2(poutEntity2);
							entity.setMatched(DBConstants.MATCHED_OK);
						} else {
							String msg = "命令出口虚端子不存在：" + entity.getDevName() + "->" + entity.getCmdOutVpRefAddr();
							appendError("导入跳合闸反校", "虚端子检查", ref, msg);
						}
						circuitEntityService.save(circuitEntity);
					} else {
						String msg = "开入虚端子虚回路不存在：" + entity.getDevName() + "->" + entity.getPinRefAddr();
						appendError("导入跳合闸反校", "虚回路检查", ref, msg);
					}
				} else {
					String msg = "开入虚端子不存在：" + entity.getDevName() + "->" + entity.getPinRefAddr();
					appendError("导入跳合闸反校", "虚端子检查", ref, msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			improtInfoService.update(entity);
			monitor.worked(1);
		}
		monitor.done();
	}

	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM108BrkCfmEntity> list = (List<IM108BrkCfmEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		try {
			for (IM108BrkCfmEntity entity : list) {
				if (entity.getMatched() == DBConstants.MATCHED_OK) {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
					continue;
				}
					Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
					if (pinEntity != null) {
						Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
						if (circuitEntity != null) {
							if (circuitEntity.getTb1061PoutByF1061CodeConvChk1() == null 
									|| circuitEntity.getTb1061PoutByF1061CodeConvChk2() == null) {
								entity.setConflict(DBConstants.NO);
								entity.setOverwrite(true);
								continue;
							} 
						}
					}
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			console.append("数据异常");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object locate(Problem problem) {
		List<IM108BrkCfmEntity> list = (List<IM108BrkCfmEntity>) table.getInput();
		if (problem != null && list != null && list.size() > 0) {
			String title = problem.getIedName();
			if ("导入跳合闸反校".equals(title)) {
				String ref = problem.getRef();
				if (ref != null && ref.contains("->")) {
					String[] refs = ref.split("->");
					if (refs.length == 2) {
						String devName = refs[0];
						String pinRefAddr = refs[1];
						for (IM108BrkCfmEntity entity : list) {
							if (devName.equals(entity.getDevName()) 
									&& pinRefAddr.equals(entity.getPinRefAddr())) {
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
