/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 一次拓扑模型->间隔树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PrimaryModelEditor extends BaseConfigEditor {
	
	public PrimaryModelEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(1));
		GridData gdlb = new GridData(SWT.DEFAULT,25);
		SwtUtil.createLabel(comp, "间隔信息列表", gdlb);
		table = TableFactory.getIntervalMsgTable(comp);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		super.initData();
		List<Tb1042BayEntity> bayEntityList = (new BayEntityService()).getBayEntryList();
		table.setInput(bayEntityList);
	}
}
