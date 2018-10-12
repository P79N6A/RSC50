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

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.model.IField;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ExportIedDialog;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DateUtils;

/**
 * 导入信息->设备台账树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpIEDListEditor extends ExcelImportEditor {
	
	private IedEntityService iedEntityService;
	private BayEntityService bayEntityService;
	
	public ImpIEDListEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		iedEntityService = new IedEntityService();
		bayEntityService = new BayEntityService();
		map = new HashMap<String, IM100FileInfoEntity>();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		titleList = SwtUtil.createList(container, gridData);
		
		Composite cmpRight = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), 1);
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		
		Composite btComp = SwtUtil.createComposite(cmpRight, btData, 3);
		btExport = SwtUtil.createPushButton(btComp, "导出Excel", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入设备台账", new GridData());
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		table = TableFactory.getIEDListTable(cmpRight);
		table.getTable().setLayoutData(tableGridData);
	}
	
	protected void addListeners() {
		SwtUtil.addMenus(titleList, new DeleteFileAction(titleList, IM101IEDListEntity.class));
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
						monitor.setTaskName("正在导出");
						long start = System.currentTimeMillis();
						List<Object> list = new ArrayList<>();
						for (Tb1046IedEntity ied : ieds) {
							IM101IEDListEntity entity = new IM101IEDListEntity();
							entity.setAorB(ied.getF1046AorB() == DBConstants.SUITE_A ? "A":"B" );
							if (ied.getTb1042BaysByF1042Code() != null) {
								entity.setBay(ied.getTb1042BaysByF1042Code().getF1042Name());
							}
							entity.setBoardNum(ied.getF1046boardNum() + "");
							if (ied.getTb1050CubicleEntity() != null) {
								entity.setCubicle(ied.getTb1050CubicleEntity().getF1050Name());
							}
							entity.setDataCollectType(ied.getF1046dataGatType());
							entity.setDateProduct(ied.getF1046productDate());
							entity.setDateService(ied.getF1046OperateDate());
							entity.setDevDesc(ied.getF1046Desc());
							entity.setDevName(ied.getF1046Name());
							entity.setDevType(ied.getF1046Model());
							entity.setDevVersion(ied.getF1046version());
							entity.setManufacturor(ied.getF1046Manufacturor());
							entity.setNetAIP(ied.getF1046aNetIp());
							entity.setNetBIP(ied.getF1046bNetIp());
							entity.setOutType(ied.getF1046OutType());
							entity.setProductCode(ied.getF1046productNo());
							entity.setProtClassify(ied.getF1046protectCategory());
							entity.setProtModel(ied.getF1046protectModel());
							entity.setProtType(ied.getF1046protectType());
							list.add(entity);
						}
						if (list.size() > 0) {
							String dateStr = DateUtils.getDateStr(new Date(), DateUtils.DATE_DAY_PATTERN_);
							String fileName = filePath + "/" + "设备台账" + dateStr + ".xlsx";
							exportTemplateExcel(fileName, "设备台账", vfields, list);
						}
						long time = (System.currentTimeMillis() - start) / 1000;
						monitor.setTaskName("导出耗时：" + time + "秒");
						monitor.done();
					}
				}
			});
		}
	}

	private void updateIEDIPs(Tb1046IedEntity iedEntity, IM101IEDListEntity entity) {
		String netIPA = entity.getNetAIP();
		String netIPB = entity.getNetBIP();
		iedEntity.setF1046aNetIp(netIPA);
		iedEntity.setF1046bNetIp(netIPB);
		iedEntityService.save(iedEntity);
		if (!StringUtil.isEmpty(netIPA)) {
			Tb1070MmsserverEntity mmsServer = new Tb1070MmsserverEntity();
			mmsServer.setF1070Code(rscp.nextTbCode(DBConstants.PR_MMSSvr));
			mmsServer.setTb1046IedByF1046Code(iedEntity);
			mmsServer.setF1070IpA(netIPA);
			mmsServer.setF1070IpB(netIPB);
			beandao.insert(mmsServer);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport() {
		List<IM101IEDListEntity> list = (List<IM101IEDListEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		DictManager dictMgr = DictManager.getInstance();
		for (IM101IEDListEntity entity : list) {
			if (!entity.isOverwrite()) {
				continue;
			}
			String devName = entity.getDevName();
			Tb1046IedEntity iedEntity = iedEntityService.getIedEntityByDevName(devName);
			if (iedEntity == null) {
				String f1046Code = rscp.nextTbCode(DBConstants.PR_IED);
				String desc = entity.getDevDesc();
				String type = null;
				if (desc.contains(RSCConstants.DEV_TYPE_SWC)) {
					type = RSCConstants.DEV_TYPE_SWC;
				} else if (desc.contains(RSCConstants.DEV_TYPE_GAT)) {
					type = RSCConstants.DEV_TYPE_GAT;
				} else if (desc.contains(RSCConstants.DEV_TYPE_ODF)) {
					type = RSCConstants.DEV_TYPE_ODF;
				}
				if (type == null) {
					String msg = entity.getDevName() + "[" + desc +"],装置类型无法识别";
					appendError("导入设备台账", "装置检查", msg);
					continue;
				}
				type = dictMgr.getIdByName("IED_TYPE", type);
				int f1046Type = Integer.parseInt(type);
				iedEntity = new Tb1046IedEntity(f1046Code, devName, f1046Type);
				iedEntity.setF1046Desc(desc);
				iedEntity.setF1046ConfigVersion(entity.getDevVersion());
				iedEntity.setF1046Manufacturor(entity.getManufacturor());
				iedEntity.setF1046Model(entity.getDevType());
				iedEntity.setF1046boardNum(0);
			}
			Tb1042BayEntity bayEntity = bayEntityService.getBayEntityByName(entity.getBay());
			if (bayEntity != null) {
				iedEntity.setF1042Code(bayEntity.getF1042Code());
			} else {
				String msg = "间隔不存在：" + entity.getDevDesc() + "->" + entity.getBay();
				appendWarning("导入设备台账", "间隔检查", msg);
			}
			Tb1050CubicleEntity cubicle = (Tb1050CubicleEntity)beandao.getObject(Tb1050CubicleEntity.class, "f1050Name", entity.getCubicle());
			if (cubicle != null) {
				iedEntity.setF1050Code(cubicle.getF1050Code());
			} else {
				String msg = "屏柜不存在：" + entity.getDevDesc() + "->" + entity.getCubicle();
				appendWarning("导入设备台账", "屏柜检查", msg);
			}
			iedEntity.setF1046OperateDate(entity.getDateService());
			iedEntity.setF1046productDate(entity.getDateProduct());
			iedEntity.setF1046productNo(entity.getProductCode());
			iedEntity.setF1046dataGatType(entity.getDataCollectType());
			iedEntity.setF1046OutType(entity.getOutType());
			//更新
			updateIEDIPs(iedEntity, entity);
		}
	}

	@Override
	public void initData() {
		table.setInput(new ArrayList<>());
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE101);
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
	
	private void loadFileItems(String filename) {
		IM100FileInfoEntity fileInfoEntity = map.get(filename);
		if (fileInfoEntity == null) {
			DialogHelper.showAsynError("文名错误！");
		} else {
			List<IM101IEDListEntity> list = improtInfoService.getIEDListEntityList(fileInfoEntity);
			if (list != null) {
				table.setInput(list);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM101IEDListEntity> list = (List<IM101IEDListEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM101IEDListEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			try {
				Tb1046IedEntity iedEntity = iedEntityService.getIedEntityByDevName(entity.getDevName());
				if (iedEntity != null) {
					Tb1042BayEntity bay = iedEntity.getTb1042BaysByF1042Code();
					Tb1050CubicleEntity cubicle = iedEntity.getTb1050CubicleEntity();
					if((bay != null && !bay.getF1042Name().equals(entity.getBay()))
							|| (cubicle != null && !cubicle.getF1050Name().equals(entity.getCubicle()))
//							|| iedEntity.getF1046Manufacturor() != null 
//							|| iedEntity.getF1046version() != null
//							|| iedEntity.getF1046protectCategory() != null
//							|| iedEntity.getF1046protectType() != null
//							|| iedEntity.getF1046protectModel() != null
							|| iedEntity.getF1046OperateDate() != null
							|| iedEntity.getF1046productDate() != null
							|| iedEntity.getF1046productNo() != null
							|| iedEntity.getF1046dataGatType() != null
							|| iedEntity.getF1046OutType() != null
//							|| iedEntity.getF1046boardNum () != null
							) {
						entity.setConflict(DBConstants.YES);
						entity.setOverwrite(false);
					} else {
						entity.setConflict(DBConstants.NO);
						entity.setOverwrite(true);
					}
				} else {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
