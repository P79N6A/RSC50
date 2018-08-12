/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 系统ICD树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ICDModelEditor extends BaseConfigEditor {
	
	private Text txtNmanuf;
	private Text txtModel;
	private Text txtVersion;
	private Combo cmbLnType;
	private Button btnAdd;
	private Button btnExport;
	
	public ICDModelEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		
		editArea.setLayout(SwtUtil.getGridLayout(1));
		
		Composite topComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_HORIZONTAL), 6);
		GridData textGridData = new GridData();
		textGridData.widthHint = 80;
		txtNmanuf = SwtUtil.createText(topComp, textGridData, "厂商");
		txtModel = SwtUtil.createText(topComp, textGridData, "型号");
		txtVersion = SwtUtil.createText(topComp, textGridData, "版本");
		cmbLnType = SwtUtil.createCombo(topComp, textGridData, true);
		cmbLnType.setItems(new String[]{"逻辑节点类型"});
		cmbLnType.select(0);
		btnAdd = SwtUtil.createButton(topComp, new GridData(), SWT.NONE, "添加");
		btnExport = SwtUtil.createButton(topComp, new GridData(), SWT.NONE, "导出");
		
		Composite centerComp = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), 1);
		table = TableFactory.getIcdTable(centerComp);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		super.initData();
	}
}
