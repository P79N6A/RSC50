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

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
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
	private Tb1049RegionEntity entity;
	
	public PhysicalAreaEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		areaService = new PhyscialAreaService();
		EditorConfigData data = (EditorConfigData) getInput().getData();
		entity = (Tb1049RegionEntity) data.getData();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		tab = SwtUtil.createTabFolder(container, SWT.TOP | SWT.BORDER);
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

	@Override
	public void initData() {
		List<Tb1050CubicleEntity> cubicleList = 
				(List<Tb1050CubicleEntity>) areaService.getCubicleList(entity);
		if (cubicleList != null) {
			cubicleTable.setInput(cubicleList);
			List<Tb1051CableEntity> cableList = 
					(List<Tb1051CableEntity>) areaService.getCableList(cubicleList);
			if (cableList != null)
				cableTable.setInput(cableList);
		}
//		List<Tb1053PhysconnEntity> physconnList = 
//				(List<Tb1053PhysconnEntity>) areaService.getListByCriteriaRegionCode(Tb1053PhysconnEntity.class, "f1049Code", "104901");
//		
//		if (physconnList != null)
//			physConnTable.setInput(physconnList);
	}
}
