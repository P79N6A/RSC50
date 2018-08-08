/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-1-11
 */
/**
 * $Log: MenuAction.java,v $
 * Revision 1.2  2013/04/24 03:48:41  zsy
 * Update: 增加icon和shell的get()、set()方法
 *
 * Revision 1.1  2013/03/29 09:37:43  cchun
 * Add:创建
 *
 * Revision 1.1  2011/09/15 08:38:31  cchun
 * Refactor:将公共菜单项移动到common插件
 *
 * Revision 1.4  2011/07/19 06:11:50  cchun
 * Update:增加getTitle()
 *
 * Revision 1.3  2010/12/01 08:26:57  cchun
 * Refactor:使用名称可配置的Action管理框架
 *
 * Revision 1.2  2010/08/10 03:39:32  cchun
 * Refactor:将window改成shell
 *
 * Revision 1.1  2010/02/05 07:36:46  cchun
 * Refactor:将MenuAction移动到common插件下
 *
 * Revision 1.1  2010/01/11 09:11:18  cchun
 * Update:使用自定义扩展点的方式重构菜单action
 *
 */
public class MenuAction extends Action {
	
	protected Shell shell;
	protected String icon;
	protected int uistyle;
	
	public MenuAction(String text) {
		setId(getClass().getName() + "." + text);
		setText(text);
		int p = text.indexOf('(');
		if (p > 0)
			text = text.substring(0, p);
		setToolTipText(text);
	}
	
	protected String getTitle() {
		return getToolTipText();
	}

	public Shell getShell() {
		return shell;
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getUistyle() {
		return uistyle;
	}

	public void setUistyle(int uistyle) {
		this.uistyle = uistyle;
	}
	
}
