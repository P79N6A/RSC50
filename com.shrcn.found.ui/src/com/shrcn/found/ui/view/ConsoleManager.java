/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.view;

import org.eclipse.swt.widgets.Display;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.UIConstants;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-23
 */
/**
 * $Log: ConsoleManager.java,v $
 * Revision 1.4  2013/11/06 09:16:46  cchun
 * Update:增加append()时间参数
 *
 * Revision 1.3  2013/06/14 01:36:40  cchun
 * Fix Bug:修复输出窗口为空的bug
 *
 * Revision 1.2  2013/05/20 03:12:21  cchun
 * Update:添加日志时间
 *
 * Revision 1.1  2013/03/29 09:36:45  cchun
 * Add:创建
 *
 * Revision 1.1  2010/12/29 06:46:22  cchun
 * Refactor:移动位置
 *
 * Revision 1.1  2010/12/23 10:11:45  cchun
 * Add:控制台日志类
 *
 */
public class ConsoleManager {
	
	private static ConsoleManager logger = new ConsoleManager();
	private static ConsoleView console = null;
	private String textOldData = "";
	
	private ConsoleManager() {
	}
	
	public static ConsoleManager getInstance() {
		return logger;
	}
	
	private void openView() {
		boolean isExists = ViewManager.existsView(UIConstants.View_Console_ID);
		console = (ConsoleView) ViewManager.findView(UIConstants.View_Console_ID);
		if (!isExists && !StringUtil.isEmpty(textOldData) && (console != null)) {
			console.appendCheckInfo(textOldData);
		}
	}
	
	/**
	 * 清空后再向控制台输出
	 * @param msg
	 */
	public void output(final String msg) {
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				openView();
				if(console != null){
					console.clearInfo();
					textOldData = console.appendCheckInfo(msg);
				}
			}
		});
	}
	
	/**
	 * 向控制台输出
	 * @param msg
	 */
	public void append(final String msg, final boolean showTime) {
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				openView();
				if(console != null && !StringUtil.isEmpty(msg)){
					String prefix = showTime ? (StringUtil.getChineseTime() + "：") : "";
					textOldData = console.appendCheckInfo("\n" + prefix + msg);
				}
			}
		});

	}
	
	public void append(final String msg) {
		append(msg, true);
	}
	
	/**
	 * 清空控制台内容
	 */
	public void clear() {
		Display.getDefault().asyncExec(new Runnable(){
			public void run(){
				openView();
				if(console != null){
					console.clearInfo();
					textOldData = "";
				}
			}
		});
	}
}
