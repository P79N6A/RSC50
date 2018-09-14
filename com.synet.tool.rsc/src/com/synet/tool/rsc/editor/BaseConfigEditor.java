/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.editor.BaseEditor;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.ui.table.DevKTable;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2013 5 21
 */
public class BaseConfigEditor extends BaseEditor {
	
	protected static final String DEV_TYPE_TITLE = "装置类型";
	protected static final String DEV_NAME_TITLE = "装置名称";
	
	protected DevKTable table;
	protected BeanDaoImpl beandao;
	protected HqlDaoImpl hqldao;
	
	public BaseConfigEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	@Override
	public void buildUI(Composite container) {
		GridLayout layout = SwtUtil.getGridLayout(1, 0);
		container.setLayout(layout);
	}
	
	@Override
	public void init() {
		super.init();
		beandao = BeanDaoImpl.getInstance();
		hqldao = HqlDaoImpl.getInstance();
	}

	@Override
	public void initData() {
	}

	@Override
	public boolean doSave() {
		return true;
	}
	
	protected void successInfo() {
		setDirty(false);
		ConsoleManager.getInstance().append("装置“" + getName() + "”的" + getEditorName() +
				"保存成功！");
	}

	public String getName() {
		return ((ConfigEditorInput)getInput()).getIedName();
	}

}
