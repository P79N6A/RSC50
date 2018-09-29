/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
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
		btImport = SwtUtil.createPushButton(cmpRight, "导入设备台账", btData);
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
		
		btImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doImport();
				DialogHelper.showAsynInformation("导入成功！");
			}
		});
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
			}
			Tb1050CubicleEntity cubicle = (Tb1050CubicleEntity)beandao.getObject(Tb1050CubicleEntity.class, "f1050Name", entity.getCubicle());
			if (cubicle != null) {
				iedEntity.setF1050Code(cubicle.getF1050Code());
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
				checkData(list);
				table.setInput(list);
			}
		}
	}
	
	private void checkData(List<IM101IEDListEntity> list) {
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
