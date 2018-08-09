/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;

/**
 * 物理信息模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PhysicalModelEditor extends BaseConfigEditor {
	
	private DevKTable table;
	
	public PhysicalModelEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		Composite comp = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), 1);
		comp.setLayout(SwtUtil.getGridLayout(1));
		table =TableFactory.getAreaListTable(comp);
		table.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
	}
	
	protected void addListeners() {
	}

	@Override
	public void initData() {
		super.initData();
	}
}
