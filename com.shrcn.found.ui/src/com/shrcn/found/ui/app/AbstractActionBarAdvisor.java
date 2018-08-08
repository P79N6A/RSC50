/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.app;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.shrcn.found.ui.util.ExtensionUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-11-23
 */
/**
 * $Log: AbstractActionBarAdvisor.java,v $
 * Revision 1.1  2013/03/29 09:36:51  cchun
 * Add:创建
 *
 * Revision 1.6  2013/01/04 05:09:28  cchun
 * Fix Bug:修复工具栏update()时null异常
 *
 * Revision 1.5  2012/11/27 06:22:31  cchun
 * Fix Bug:修复菜单、工具栏不刷新的bug
 *
 * Revision 1.4  2012/10/30 05:27:54  cchun
 * Update:增加getMenubar()
 *
 * Revision 1.3  2011/04/12 09:17:13  cchun
 * Update:sct.properties改为在第一次调用时加载
 *
 * Revision 1.2  2010/12/01 08:26:04  cchun
 * Refactor:重构
 *
 * Revision 1.1  2010/11/23 06:13:25  cchun
 * Refactor:使用统一的菜单action管理工具
 *
 */
public abstract class AbstractActionBarAdvisor extends ActionBarAdvisor {

	private MenuToolFactory menuToolFactory = MenuToolFactory.getInstance();
	
	public AbstractActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		ExtensionUtil.clearNoUseBar();
	}
	
	@Override
	protected void makeActions(IWorkbenchWindow window) {
		menuToolFactory.initActions(window);
		
		for (IAction action : menuToolFactory.getActions())
			register(action);
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		menuToolFactory.loadMenuBar(menuBar);
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		menuToolFactory.loadToolBar(coolBar);
	}
	
}
