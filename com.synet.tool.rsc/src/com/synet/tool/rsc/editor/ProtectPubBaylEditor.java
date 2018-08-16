/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

/**
 * 保护信息模型->公用间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectPubBaylEditor extends BaseConfigEditor {
	
	private DevKTable tableGatherDev;
	private DevKTable tableOtherDev;
	private Button btnSearch;
	private String[] comboItems;

	public ProtectPubBaylEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		comboItems = new String[]{"装置类型"};
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{"采集单元", "其他设备"};
		CTabFolder tabFolder = SwtUtil.createTab(comp, gridData, tabNames);
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//采集设备
		Composite cmpGatherDev = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		cmpGatherDev.setLayout(SwtUtil.getGridLayout(1));
		tableGatherDev = TableFactory.getGatherTable(cmpGatherDev);
		tableGatherDev.getTable().setLayoutData(gridData);
		
		//其他设备
		Composite cmpOtherDev = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		cmpOtherDev.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		Combo combo = SwtUtil.createCombo(cmpOtherDev, textGridData, true);
		combo.setItems(comboItems);
		combo.select(0);
		Text text = SwtUtil.createText(cmpOtherDev, SwtUtil.bt_hd);
		text.setMessage("装置名称");
		btnSearch = SwtUtil.createButton(cmpOtherDev, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		SwtUtil.createLabel(cmpOtherDev, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableOtherDev = TableFactory.getProtectIntervalTable(cmpOtherDev);
		tableOtherDev.getTable().setLayoutData(gdSpan_3);
		
		
	}
	
	protected void addListeners() {
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetSelected(e);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
	}
}
