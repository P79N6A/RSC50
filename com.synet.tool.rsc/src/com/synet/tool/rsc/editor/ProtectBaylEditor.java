/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 保护信息模型->间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectBaylEditor extends BaseConfigEditor {
	
	private Button btnSearch;
	private String[] comboItems;

	public ProtectBaylEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void init() {
		comboItems = new String[]{"装置"};
		super.init();
	}
	
	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		Combo combo = SwtUtil.createCombo(comp, textGridData, true);
		combo.setItems(comboItems);
		combo.select(0);
		
		Text text = SwtUtil.createText(comp, SwtUtil.bt_hd);
		text.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		SwtUtil.createLabel(comp, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		table = TableFactory.getProtectIntervalTable(comp);
		table.getTable().setLayoutData(gdSpan_3);
	}
	
	protected void addListeners() {
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
	}
}
