/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-12-1
 */
/**
 * $Log: ToolFactory.java,v $
 * Revision 1.1  2013/03/29 09:36:47  cchun
 * Add:创建
 *
 * Revision 1.1  2011/12/02 03:44:24  cchun
 * Add:工具栏工厂
 *
 */
public class ToolFactory {

	private ResourceBundle boundle;
	private static ToolFactory inst = new ToolFactory();
	
	private ToolFactory() {
		this.boundle = ResourceBundle.getBundle(ToolFactory.class.getPackage()
				.getName() + ".toolbar", Locale.getDefault());
	}
	
	public static ToolFactory getInstance() {
		if (inst == null) {
			inst = new ToolFactory();
		}
		return inst;
	}
	
	/**
	 * 创建工具栏工具
	 * @param toolBar
	 * @param name
	 * @return
	 */
	public ToolItem createTool(ToolBar toolBar, String name, ToolHandler toolHandler) {
		ToolItem tool = new ToolItem(toolBar, SWT.PUSH);
		tool.setImage(IconsManager.getInstance().getImage(boundle.getString(name + ".IMAGE")));
		tool.setToolTipText(boundle.getString(name + ".TEXT"));
		tool.addSelectionListener(new ToolSelectionListener(boundle.getString(name + ".EVENT"), toolHandler));
		return tool;
	}
	
	/**
	 * 创建工具栏
	 * @param parent
	 * @return
	 */
	public ToolBar createToolBar(Composite parent, String barName, ToolHandler toolHandler) {
		ToolBar toolBar = new ToolBar(parent, SWT.NONE);
		String[] tools = boundle.getString(barName).split(",");
		for (String toolName : tools) {
			createTool(toolBar, toolName, toolHandler);
		}
		return toolBar;
	}
}
