/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.RscObjectUtils;

/**
 * 物理信息模型->区域树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PhysicalAreaEditor extends BaseConfigEditor {
	private CTabFolder tab;
	private DevKTable cubicleTable;
	private DevKTable cableTable;
	private DevKTable physConnTable;
	//查询条件
	private Text txtCubicleName;
	private Text txtCubicleDesc;
	private Text txtCableName;
	private Text txtCableDesc;
	private Text txtIedName;
	private Text txtIedDesc;
	
	//查询
	private Button btnSearchCubicle;
	private Button btnSearchCable;
	private Button btnSearchPhysconn;
	//添加
	private Button btnAddCubicle;
	private Button btnAddCable;
	private Button btnAddPhysconn;
	//删除
	private Button btnDelCubicle;
	private Button btnDelCable;
	private Button btnDelPhysconn;
	private PhyscialAreaService areaService;
	private Tb1049RegionEntity regionEntity;
	
	public PhysicalAreaEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		areaService = new PhyscialAreaService();
		EditorConfigData data = (EditorConfigData) getInput().getData();
		regionEntity = (Tb1049RegionEntity) data.getData();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		tab = SwtUtil.createTabFolder(container, SWT.TOP | SWT.BORDER);
		tab.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite cubicleComp = SwtUtil.createComposite(tab, new GridData(), 5);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_CUBICLE, cubicleComp);
		Composite cableComp = SwtUtil.createComposite(tab, new GridData(), 5);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_CABLE, cableComp);
		Composite physConnComp = SwtUtil.createComposite(tab, new GridData(), 5);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_PHYSCONN, physConnComp);
		tab.setSelection(0);
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 5;
		
		txtCubicleName = SwtUtil.createText(cubicleComp, SwtUtil.bt_hd); 
		txtCubicleName.setMessage("屏柜名称");
		txtCubicleDesc = SwtUtil.createText(cubicleComp, SwtUtil.bt_hd); 
		txtCubicleDesc.setMessage("屏柜描述");
		btnSearchCubicle = SwtUtil.createButton(cubicleComp, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		btnAddCubicle = SwtUtil.createButton(cubicleComp, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelCubicle = SwtUtil.createButton(cubicleComp, SwtUtil.bt_gd, SWT.BUTTON1, "删除");		
		cubicleTable = TableFactory.getCubicleTable(cubicleComp);
		cubicleTable.getTable().setLayoutData(tableGridData);
		
		txtCableName = SwtUtil.createText(cableComp, SwtUtil.bt_hd); 
		txtCableName.setMessage("线缆名称");
		txtCableDesc = SwtUtil.createText(cableComp, SwtUtil.bt_hd); 
		txtCableDesc.setMessage("线缆描述");
		btnSearchCable = SwtUtil.createButton(cableComp, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		btnAddCable = SwtUtil.createButton(cableComp, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelCable = SwtUtil.createButton(cableComp, SwtUtil.bt_gd, SWT.BUTTON1, "删除");	
		cableTable = TableFactory.getCableTable(cableComp);
		cableTable.getTable().setLayoutData(tableGridData);
		
		txtIedName = SwtUtil.createText(physConnComp, SwtUtil.bt_hd); 
		txtIedName.setMessage("装置名称");
		txtIedDesc = SwtUtil.createText(physConnComp, SwtUtil.bt_hd); 
		txtIedDesc.setMessage("装置描述");
		btnSearchPhysconn = SwtUtil.createButton(physConnComp, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		btnAddPhysconn = SwtUtil.createButton(physConnComp, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelPhysconn = SwtUtil.createButton(physConnComp, SwtUtil.bt_gd, SWT.BUTTON1, "删除");	
		physConnTable = TableFactory.getPhysconnTable(physConnComp);
		physConnTable.getTable().setLayoutData(tableGridData);

	}
	
	protected void addListeners() {
		SelectionAdapter listener = new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if (obj == btnAddCubicle) {
					addCubicle();
				} else if (obj == btnDelCubicle) {
					delCubicle();
				} else if (obj == btnAddCable) {
					addCable();
				} else if (obj == btnDelCable) {
					delCable();
				} else if (obj == btnAddPhysconn) {
					addPhysconn();
				} else if (obj == btnDelPhysconn) {
					delPhysconn();
				} else if (obj == btnSearchCubicle) {
					searchCubicle();
				} else if (obj == btnSearchCable) {
					searchCable();
				} else if (obj == btnSearchPhysconn) {
					searchPhysconn();
				}
			}
		};
		
		btnAddCubicle.addSelectionListener(listener);
		btnAddCable.addSelectionListener(listener);
		btnAddPhysconn.addSelectionListener(listener);
		btnDelCubicle.addSelectionListener(listener);
		btnDelCable.addSelectionListener(listener);
		btnDelPhysconn.addSelectionListener(listener);
		btnSearchCubicle.addSelectionListener(listener);
		btnSearchCable.addSelectionListener(listener);
		btnSearchPhysconn.addSelectionListener(listener);
	}

	@Override
	public void initData() {
		List<Tb1050CubicleEntity> cubicleList = 
				(List<Tb1050CubicleEntity>) areaService.getCubicleList(regionEntity);
		if (cubicleList != null) {
			cubicleTable.setInput(cubicleList);
			
			List<Tb1051CableEntity> cableList = 
					(List<Tb1051CableEntity>) areaService.getCableList(cubicleList);
			if (cableList != null){
				cableTable.setInput(cableList);
			}
			
			List<Tb1053PhysconnEntity> physconnList = 
					(List<Tb1053PhysconnEntity>) areaService.getPhysconnList(cubicleList);
			if (physconnList != null)
				physConnTable.setInput(physconnList);
		}
		
	}

	private void delPhysconn() {
		List<Object> list = physConnTable.getSelections();
		if (list != null) {
			for (Object obj : list) {
				areaService.delete(obj);
			}
			physConnTable.removeSelected();
		}
//		if (physConnTable.getSelection() != null) {
//			areaService.delete(physConnTable.getSelection());
//			physConnTable.removeSelected();
//		}
	}

	private void addPhysconn() {
		physConnTable.addRow(RscObjectUtils.createPhysconn());
	}

	private void delCable() {
		List<Object> list = cableTable.getSelections();
		if (list != null) {
			for (Object obj : list) {
				areaService.delete(obj);
			}
			cableTable.removeSelected();
		}
//		if (cableTable.getSelection() != null) {
//			areaService.delete(cableTable.getSelection());
//			cableTable.removeSelected();
//		}
	}

	private void addCable() {
		cableTable.addRow(RscObjectUtils.createCable());
	}

	private void delCubicle() {
		List<Object> list = cubicleTable.getSelections();
		if (list != null) {
			for (Object obj : list) {
				areaService.delete(obj);
			}
			cubicleTable.removeSelected();
		}
	}

	private void addCubicle() {
		Tb1050CubicleEntity cubicleEntity = RscObjectUtils.createCubicle();
		cubicleEntity.setTb1049RegionByF1049Code(regionEntity);
		cubicleTable.addRow(cubicleEntity);
	}
	

	private void searchCubicle() {
		String cubicleName = txtCubicleName.getText().trim(); 
		String cubicleDesc = txtCubicleDesc.getText().trim();
		cubicleName = "".equals(cubicleName) ? null : cubicleName;
		cubicleDesc = "".equals(cubicleDesc) ? null : cubicleDesc;
		List<Tb1050CubicleEntity> cubicleList = 
				(List<Tb1050CubicleEntity>) areaService.getCubicleList(regionEntity, cubicleName, cubicleDesc);
		if (cubicleList != null) {
			cubicleTable.setInput(cubicleList);
		}
	}
	
	private void searchCable() {
		String cableName = txtCableName.getText().trim();
		String cableDesc = txtCableDesc.getText().trim();
		cableName = "".equals(cableName) ? null : cableName;
		cableDesc = "".equals(cableDesc) ? null : cableDesc;
		List<Tb1050CubicleEntity> cubicleList = 
				(List<Tb1050CubicleEntity>) areaService.getCubicleList(regionEntity);
		if (cubicleList != null) {
			List<Tb1051CableEntity> cableList = 
					(List<Tb1051CableEntity>) areaService.getCableList(cubicleList, cableName, cableDesc);
			if (cableList != null){
				cableTable.setInput(cableList);
			}
		}
	}

	private void searchPhysconn() {
		String iedName = txtIedName.getText().trim();
		String iedDesc = txtIedDesc.getText().trim();
		iedName = "".equals(iedName) ? null : iedName;
		iedDesc = "".equals(iedDesc) ? null : iedDesc;
		List<Tb1050CubicleEntity> cubicleList = 
				(List<Tb1050CubicleEntity>) areaService.getCubicleList(regionEntity);
		if (cubicleList != null) {
			List<Tb1053PhysconnEntity> physconnList = 
					(List<Tb1053PhysconnEntity>) areaService.getPhysconnList(cubicleList, iedName, iedDesc);
			if (physconnList != null)
				physConnTable.setInput(physconnList);
		}
	
	}
}
