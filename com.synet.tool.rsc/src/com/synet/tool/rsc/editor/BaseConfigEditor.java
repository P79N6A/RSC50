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

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2013 5 21
 */
public class BaseConfigEditor extends BaseEditor {
	
//	protected DeviceConfig device;
//
//	protected ProjectFileManager prjFileMgr;
//	protected ProjectManager prjMgr;
	
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
//		if (prjFileMgr == null)
//			prjFileMgr = ProjectFileManager.getInstance();
//		if (prjMgr == null)
//			prjMgr = ProjectManager.getInstance();
//		if (!(this instanceof ImportSCDEditor) &&
//				device == null && getName() != null) {
//			device = prjMgr.getDevice(getName());
//		}
		super.init();
	}

	@Override
	public void initData() {
	}

	@Override
	public boolean doSave() {
//		if (isDirty() && device != null) {
//			if (DialogHelper.showConfirm("需要保存当前配置吗？")) {
//				prjMgr.addDevice(device);
//				prjFileMgr.saveDevice(getName());
//				successInfo();
//			} else {
//				prjMgr.removeDeviceCache(getName());
//			}
//		}
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

//	protected String getScdName() {
//		return prjFileMgr.getScdname();
//	}
}
