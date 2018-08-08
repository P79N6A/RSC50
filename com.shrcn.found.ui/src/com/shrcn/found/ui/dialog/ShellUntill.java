/*
 * @(#) ShellUntill.java
 *
 * Copyright (c) 2007 - 2013 上海思源弘瑞电力自动化有限公司.
 * All rights reserved. 基于 Eclipse E4 a next generation 
 * platform (e.g., the CSS styling, dependency injection, Modeled UI) 
 * Rich Client Application 开发的U21继电保护装置平台工具软件. 
 * 此系列工具软件包括装置调试下载工具、装置配置工具. 103通信调试工具、
 * 自动化装置测试工具。
 */
package com.shrcn.found.ui.dialog;

import org.eclipse.swt.widgets.Shell;


/**
 * shell相关操作
 *
 * @author 吴小兵
 * @version 1.0 2013-1-5
 */
public class ShellUntill {
	/**
	 * 窗口居中，在要居中显示的窗口代码中shell.open之前调用
	 * @param shell
	 */
	 public  static void centerShell(Shell shell) {  
	      org.eclipse.swt.graphics.Rectangle displayBounds = shell.getDisplay().getPrimaryMonitor()  
	               .getBounds();  
	      org.eclipse.swt.graphics.Rectangle shellBounds = shell.getBounds();  
	      int x = displayBounds.x + (displayBounds.width - shellBounds.width) >> 1;  
	       int y = displayBounds.y + (displayBounds.height - shellBounds.height) >> 1;  
	       shell.setLocation(x, y);  
	    }
}
