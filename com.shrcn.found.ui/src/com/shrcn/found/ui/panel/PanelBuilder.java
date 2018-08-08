/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.panel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.Form;


public class PanelBuilder {

	private PanelBuilder() {}
	
	public static ConfigPanel createPanel(Class<?> clazz, Composite container, Form form) {
		ConfigPanel panel = (ConfigPanel) ObjectUtil.newInstance(clazz, 
				new Class[] {Composite.class, Form.class}, 
				new Object[] {container, form});
		panel.setBackground(UIConstants.Content_BG);
		panel.setBackgroundMode(SWT.INHERIT_FORCE);
		return panel;
	}
	
	public static ConfigPanel createPanel(Composite container, Form form) {
		return (ConfigPanel) createPanel(ConfigPanel.class, container, form);
	}
	
	/**
	 * 根据定义文件创建表单界面。
	 * @param container
	 * @param panelConfig
	 * @param obj
	 * @return
	 */
	public static DeviceConfigPanel createDevPanel(Composite container, Form form) {
		return (DeviceConfigPanel) createPanel(DeviceConfigPanel.class, container, form);
	}
	
}
