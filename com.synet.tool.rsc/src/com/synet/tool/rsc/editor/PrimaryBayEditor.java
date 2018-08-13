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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ChanelConnectDialog;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 一次拓扑模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PrimaryBayEditor extends BaseConfigEditor {
	
	private Button button;
	private String curEntryName;
	
	public PrimaryBayEditor(Composite container, IEditorInput input) {
		super(container, input);
		EditorConfigData data = (EditorConfigData)input.getData();
		this.curEntryName = data.getIedName();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{RSCConstants.TSF_SCDRAY, RSCConstants.PROTCT_SAMP, RSCConstants.SWICH_STATES};
		CTabFolder tabFolder = SwtUtil.createTab(comp, gridData, tabNames);
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//互感器次级
		Composite compTsf = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		compTsf.setLayout(SwtUtil.getGridLayout(3));
		GridData gdlb = new GridData(SWT.DEFAULT,25);
		
		SwtUtil.createLabel(compTsf, curEntryName + "互感器次级配置", gdlb);
		SwtUtil.createLabel(compTsf, "			", gdlb);
		button = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "通道关联");
		SwtUtil.createLabel(compTsf, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		table = TableFactory.getTsfSecondaryTable(compTsf);
		table.getTable().setLayoutData(gdSpan_3);
		//保护采样值
		Composite compProtect = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		compProtect.setLayout(SwtUtil.getGridLayout(1));
		table = TableFactory.getProtectSampleTalbe(compProtect);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		//开关刀闸状态
		Composite compSwitch = SwtUtil.createComposite((Composite) controls[2], gridData, 3);
		compSwitch.setLayout(SwtUtil.getGridLayout(1));
		table = TableFactory.getSwitchStatusTable(compSwitch);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ChanelConnectDialog(SwtUtil.getDefaultShell()).open();
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
	}
}
