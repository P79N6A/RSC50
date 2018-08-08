/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * 工具栏按钮监听事件
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-12-1
 */
/**
 * $Log: ToolSelectionListener.java,v $
 * Revision 1.1  2013/03/29 09:36:46  cchun
 * Add:创建
 *
 * Revision 1.1  2011/12/02 03:44:24  cchun
 * Add:工具栏工厂
 *
 */
public class ToolSelectionListener extends SelectionAdapter {
	private String event;
	private ToolHandler toolHandler;
	
	public ToolSelectionListener(String event, ToolHandler toolHandler) {
		this.event = event;
		this.toolHandler = toolHandler;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		toolHandler.handle(event);
	}
}
