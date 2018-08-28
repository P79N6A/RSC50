/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import oracle.net.aso.s;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.dialog.AddRegionDialog;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.PhysicalModelService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.RscObjectUtils;

/**
 * 物理信息模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PhysicalModelEditor extends BaseConfigEditor {
	
	private Button btnAddRegion;
	private Button btnDelRegion;
	private PhysicalModelService service;
	
	public PhysicalModelEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		service = new PhysicalModelService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		btnAddRegion = SwtUtil.createButton(container, SwtUtil.bt_gd, SWT.BUTTON1, "添加");
		btnDelRegion = SwtUtil.createButton(container, SwtUtil.bt_gd, SWT.BUTTON1, "删除");
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 2;
		table =TableFactory.getAreaListTable(container);
		table.getTable().setLayoutData(tableGridData);
	}
	
	protected void addListeners() {
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if (obj == btnAddRegion) {
					addRegion();
				} else if (obj == btnDelRegion) {
					delRegion();
				}
			}

		};
		btnAddRegion.addSelectionListener(listener);
		btnDelRegion.addSelectionListener(listener);
	}
	

	private void delRegion() {
		Object entity = table.getSelection();
		if (entity != null) {
			service.delete(entity);
			initData();
		}
	}

	private void addRegion() {
//		AddRegionDialog dialog = new AddRegionDialog(getShell());
//		if (dialog.open() == 0) {
//			initData();
//		}
		table.addRow(RscObjectUtils.createRegion());
	}

	@Override
	public void initData() {
		List<Tb1049RegionEntity> list= service.getAllRegionList();
		if (list != null) {
			table.setInput(list);
		}
	}
}
