/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;

/**
 * 保护信息模型->公用间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectPubBaylEditor extends BaseConfigEditor {
	
	public ProtectPubBaylEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		CTabFolder tab = SwtUtil.createTabFolder(editArea, SWT.TOP | SWT.BORDER);
		tab.setLayoutData(new GridData(GridData.FILL_BOTH));
		
//		// 基本信息
//		Composite baseCmp = SwtUtil.createComposite(tab, new GridData(GridData.FILL_VERTICAL), 1);
//		SwtUtil.addTabItem(tab, "基本信息", baseCmp);
//		baseInfoTbl = TableFactory.getBaseInfoTable(baseCmp);
//		baseInfoTbl.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
//		
//		// 板卡信息
//		Composite cardCmp = SwtUtil.createComposite(tab, new GridData(GridData.FILL_BOTH), 1);
//		SwtUtil.addTabItem(tab, "板卡信息", cardCmp);
//		cardInfoTbl = TableFactory.getCardInfoTable(cardCmp);
//		cardInfoTbl.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
//		
//		tab.setSelection(0);
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		super.initData();
	}
}
