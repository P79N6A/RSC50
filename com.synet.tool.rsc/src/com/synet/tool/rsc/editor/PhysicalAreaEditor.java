/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

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
	private PhyscialAreaService areaService;
	
	public PhysicalAreaEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		areaService = new PhyscialAreaService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		tab = SwtUtil.createTabFolder(editArea, SWT.TOP | SWT.BORDER);
		tab.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite cubicleComp = SwtUtil.createComposite(tab, new GridData(), 1);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_CUBICLE, cubicleComp);
		Composite cableComp = SwtUtil.createComposite(tab, new GridData(), 1);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_CABLE, cableComp);
		Composite physConnComp = SwtUtil.createComposite(tab, new GridData(), 1);
		SwtUtil.addTabItem(tab, RSCConstants.TAB_PHYSCONN, physConnComp);
		tab.setSelection(0);
		
		cubicleTable = TableFactory.getCubicleTable(cubicleComp);
		cubicleTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		cableTable = TableFactory.getCableTable(cableComp);
		cableTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		physConnTable = TableFactory.getPhysconnTable(physConnComp);
		physConnTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

	}
	
	protected void addListeners() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
//		List<Tb1050CubicleEntity> cubicleList = 
//				(List<Tb1050CubicleEntity>) areaService.getListByCriteriaRegionCode(Tb1050CubicleEntity.class, "f1049Code", "104901");
//		List<Tb1051CableEntity> cableList = 
//				(List<Tb1051CableEntity>) areaService.getListByCriteriaRegionCode(Tb1051CableEntity.class, "f1049Code", "104901");
//		List<Tb1053PhysconnEntity> physconnList = 
//				(List<Tb1053PhysconnEntity>) areaService.getListByCriteriaRegionCode(Tb1053PhysconnEntity.class, "f1049Code", "104901");
//		
//		if (cubicleList != null)
//			cubicleTable.setInput(cubicleList);
//		if (cableList != null)
//			cableTable.setInput(cableList);
//		if (physconnList != null)
//			physConnTable.setInput(physconnList);
	}
}
