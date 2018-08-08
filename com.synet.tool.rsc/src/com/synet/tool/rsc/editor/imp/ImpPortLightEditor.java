/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.editor.BaseConfigEditor;

/**
 * 导入信息->光强与端口关联表 树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpPortLightEditor extends BaseConfigEditor {
	
	public ImpPortLightEditor(Composite container, IEditorInput input) {
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
