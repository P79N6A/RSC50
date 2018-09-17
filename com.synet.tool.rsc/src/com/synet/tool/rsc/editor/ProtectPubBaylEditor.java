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
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

/**
 * 保护信息模型->公用间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectPubBaylEditor extends BaseConfigEditor {
	
	private DevKTable tableOtherDev;
	private Button btnSearch;
	private String[] comboItems;

	public ProtectPubBaylEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		comboItems = new String[]{"装置类型"};
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		int cols = 3;
		Composite comp = SwtUtil.createComposite(container, gridData, cols);
		comp.setLayout(SwtUtil.getGridLayout(1));
		
		//其他设备（测控、交换机、采集单元）
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		Combo combo = SwtUtil.createCombo(comp, textGridData, true);
		combo.setItems(comboItems);
		combo.select(0);
		Text text = SwtUtil.createText(comp, SwtUtil.bt_hd);
		text.setMessage("装置名称");
		btnSearch = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, RSCConstants.SEARCH);
		SwtUtil.createLabel(comp, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = cols;
		tableOtherDev = TableFactory.getProtectIntervalTable(comp);
		tableOtherDev.getTable().setLayoutData(gdSpan_3);
	}
	
	protected void addListeners() {
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
	}
}
