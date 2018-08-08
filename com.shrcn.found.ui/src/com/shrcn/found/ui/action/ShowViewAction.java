/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.action;

import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.ViewManager;

/**
 * 打开视图操作处理类。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-1
 */
/**
 * $Log: ShowViewAction.java,v $
 * Revision 1.1  2011/09/15 08:38:31  cchun
 * Refactor:将公共菜单项移动到common插件
 *
 * Revision 1.1  2010/12/01 08:26:57  cchun
 * Refactor:使用名称可配置的Action管理框架
 *
 */
public class ShowViewAction extends MenuAction {
	
	private String viewId;
	
	public ShowViewAction(String viewId, String text) {
		super(text);
		this.viewId = viewId;
		setId(viewId);
	}
	
	@Override
	public void run() {
		if (UIConstants.View_Console_ID.equals(viewId)) {
			ConsoleManager.getInstance().append("", false);
		} else {
			ViewManager.showView(viewId);
		}
	}
}
