/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.editor.BaseConfigEditor;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->设备台账树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpIEDListEditor extends BaseConfigEditor {
	
	private Combo cmbDevType;
	private Text txtDevName;
	private Button btnSearch;
	
	public ImpIEDListEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(5));
		GridData noneGridData = new GridData();
		noneGridData.widthHint = 20;
		cmbDevType = SwtUtil.createCombo(container, SwtUtil.bt_hd, true);
		cmbDevType.setItems(new String[]{"装置类型"});
		cmbDevType.select(0);
		SwtUtil.createLabel(container, "", noneGridData);
		txtDevName = SwtUtil.createText(container, SwtUtil.bt_hd);
		txtDevName.setMessage("装置名称");
		SwtUtil.createLabel(container, "", noneGridData);
		btnSearch = SwtUtil.createButton(container, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		
		GridData tableGridData = new GridData(GridData.FILL_BOTH);
		tableGridData.horizontalSpan = 5;
		table =TableFactory.getIEDListTable(container);
		table.getTable().setLayoutData(tableGridData);
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		super.initData();
	}
}
