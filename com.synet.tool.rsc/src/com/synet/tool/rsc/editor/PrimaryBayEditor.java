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

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ChanelConnectDialog;
import com.synet.tool.rsc.dialog.SampleConnectDialog;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 一次拓扑模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PrimaryBayEditor extends BaseConfigEditor {
	
	private Button btnChanelConnect;
	private Button btnSampleConnect;
	private String curEntryName;
	private Button btnMove;
	private String[] comboItems;
	private Button btnSearch;
	
	public PrimaryBayEditor(Composite container, IEditorInput input) {
		super(container, input);
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
		compTsf.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gdlb = new GridData(200,25);
		String tsfLbName = curEntryName + "互感器次级配置";
		SwtUtil.createLabel(compTsf, tsfLbName, gdlb);
		btnChanelConnect = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "通道关联");
		SwtUtil.createLabel(compTsf, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_2 = new GridData(GridData.FILL_BOTH);
		gdSpan_2.horizontalSpan = 2;
		table = TableFactory.getTsfSecondaryTable(compTsf);
		table.getTable().setLayoutData(gdSpan_2);
		//保护采样值
		Composite compProtect = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		compProtect.setLayout(SwtUtil.getGridLayout(2));
		String protLbName = curEntryName + "保护采样值配置";
		SwtUtil.createLabel(compProtect, protLbName, gdlb);
		btnSampleConnect = SwtUtil.createButton(compProtect, SwtUtil.bt_gd, SWT.BUTTON1, "采样关联");
		SwtUtil.createLabel(compProtect, "			", new GridData(SWT.DEFAULT,10));
		table = TableFactory.getProtectSampleTalbe(compProtect);
		table.getTable().setLayoutData(gdSpan_2);
		//开关刀闸状态
		Composite compSwitch = SwtUtil.createComposite((Composite) controls[2], gridData, 1);
		compSwitch.setLayout(SwtUtil.getGridLayout(2));
		//开关刀闸状态-左侧
		Composite comLeft = SwtUtil.createComposite(compSwitch, new GridData(640,405), 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		GridData gdlb_2 = new GridData(200,25);
		gdlb_2.horizontalSpan = 2;
		String switchLbName = curEntryName + "开关刀闸状态配置";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		GridData gdlbSpace_2 = new GridData(SWT.DEFAULT,10);
		gdlbSpace_2.horizontalSpan = 2;
		SwtUtil.createLabel(comLeft, "			", gdlbSpace_2);
		table = TableFactory.getSwitchStatusTable(comLeft);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		btnMove = SwtUtil.createButton(comLeft, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		
		
		//开关刀闸状态-右侧
		Composite comRight = SwtUtil.createComposite(compSwitch, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		
		Combo combo = SwtUtil.createCombo(comRight, SwtUtil.bt_hd);
		combo.setItems(comboItems);
		combo.select(0);
		
		Text text = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		text.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comRight, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		SwtUtil.createLabel(comRight, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		table = TableFactory.getSluiceStatusTable(comRight);
		table.getTable().setLayoutData(gdSpan_3);
	}
	
	@Override
	public void init() {
		EditorConfigData data = (EditorConfigData)super.getInput().getData();
		this.curEntryName = data.getIedName();
		comboItems = new String[]{"智能终端1"};
		super.init();
	}
	
	protected void addListeners() {
		btnChanelConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new ChanelConnectDialog(SwtUtil.getDefaultShell(), curEntryName).open();
			}
		});
		
		btnSampleConnect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new SampleConnectDialog(SwtUtil.getDefaultShell(), curEntryName).open();
			}
		});
		
		btnMove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
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
