/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->设备台账树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpIEDListEditor extends BaseConfigEditor {
	
	private Combo cmbDevType;
	private Text txtDevName;//装置中文名称
	private Button btnSearch;
	private ImprotInfoService improtInfoService;
	private Button btImport;
	
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
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(6));
		GridData noneGridData = new GridData();
		noneGridData.widthHint = 20;
		cmbDevType = SwtUtil.createCombo(container, SwtUtil.bt_hd, true);
		cmbDevType.setItems(new String[]{DEV_TYPE_TITLE});
		cmbDevType.select(0);
		SwtUtil.createLabel(container, "", noneGridData);
		txtDevName = SwtUtil.createText(container, SwtUtil.bt_hd);
		txtDevName.setMessage("装置中文名称");
		SwtUtil.createLabel(container, "", noneGridData);
		btnSearch = SwtUtil.createButton(container, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		btImport = SwtUtil.createPushButton(container, "导入设备台账", btData);
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 6;
		table =TableFactory.getIEDListTable(container);
		table.getTable().setLayoutData(tableGridData);
	}
	
	protected void addListeners() {
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
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

	@SuppressWarnings("unchecked")
	protected void doImport() {
		List<IM101IEDListEntity> list = (List<IM101IEDListEntity>) table.getInput();
		if (list == null || list.size() <= 0)
			return;
		for (IM101IEDListEntity entity : list) {
			try {
				Tb1046IedEntity iedEntity = iedEntityService.getIedEntityByDevName(entity.getDevName());
				if (iedEntity != null) {
					Tb1042BayEntity bayEntity = bayEntityService.getBayEntityByName(entity.getBay());
					if (bayEntity != null) {
						iedEntity.setTb1042BaysByF1042Code(bayEntity);
					}
					iedEntity.setF1046Manufacturor(entity.getManufacturor());
					iedEntity.setF1046version(entity.getDevVersion());
					iedEntity.setF1046protectCategory(entity.getProtClassify());
					iedEntity.setF1046protectType(entity.getProtType());
					iedEntity.setF1046protectModel(entity.getProtModel());
					iedEntity.setF1046OperateDate(entity.getDateService());
					iedEntity.setF1046productDate(entity.getDateProduct());
					iedEntity.setF1046productNo(entity.getProductCode());
					iedEntity.setF1046dataGatType(entity.getDataCollectType());
					iedEntity.setF1046OutType(entity.getOutType());
					int num = 0;
					try {
						num = Integer.parseInt(entity.getBoardNum());
					} catch(Exception e) {
					}
					iedEntity.setF1046boardNum(num);
					//更新
					iedEntityService.update(iedEntity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			improtInfoService.update(entity);
		}
	}

	@Override
	public void initData() {
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		List<IM101IEDListEntity> inputs = new ArrayList<>();
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) { 
				List<IM101IEDListEntity> list = improtInfoService.getIEDListEntityList(fileInfoEntity);
				if (list != null && list.size()> 0) {
					inputs.addAll(list);
				}
			}
			if (inputs != null && inputs.size() > 0) {
				checkData(inputs);
				table.setInput(inputs);
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
					if(iedEntity.getTb1042BaysByF1042Code() != null 
							|| iedEntity.getF1046Manufacturor() != null 
							|| iedEntity.getF1046version() != null
							|| iedEntity.getF1046protectCategory() != null
							|| iedEntity.getF1046protectType() != null
							|| iedEntity.getF1046protectModel() != null
							|| iedEntity.getF1046OperateDate() != null
							|| iedEntity.getF1046productDate() != null
							|| iedEntity.getF1046productNo() != null
							|| iedEntity.getF1046dataGatType() != null
							|| iedEntity.getF1046OutType() != null
							|| iedEntity.getF1046boardNum () != null) {
						entity.setConflict(DBConstants.YES);
						entity.setOverwrite(false);
						continue;
					}
					entity.setConflict(DBConstants.NO);
					entity.setOverwrite(true);
				} else {
					entity.setConflict(DBConstants.YES);
					entity.setOverwrite(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//手动过滤
	private void search() {
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		List<IM101IEDListEntity> inputs = new ArrayList<>();
		if (fileInfoEntities != null && fileInfoEntities.size() > 0) {
			for (IM100FileInfoEntity fileInfoEntity : fileInfoEntities) { 
				List<IM101IEDListEntity> list = improtInfoService.getIEDListEntityList(fileInfoEntity);
				if (list != null && list.size()> 0) {
					inputs.addAll(list);
				}
			}
			if (inputs != null && inputs.size() > 0) {
				List<IM101IEDListEntity> temp = new ArrayList<>();
				String devType = cmbDevType.getText().trim();
				String devName = txtDevName.getText().trim();
				for (IM101IEDListEntity entity : inputs) {
					if (DEV_TYPE_TITLE.equals(devType)) {
						if ("".equals(devName)) {
							return;
						} else {
							if (devName.equals(entity.getDevDesc())) {
								temp.add(entity);
							}
						}
					} else {
						if (devType.equals(entity.getDevType())) {
							if ("".equals(devName)) {
								return;
							} else {
								if (devName.equals(entity.getDevDesc())) {
									temp.add(entity);
								}
							}
						}
					}
				}
				table.setInput(temp);
			}
		}
	}
}
