/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.app.WrappedMonitorDialog;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-28
 */
/**
 * $Log: ProgressManager.java,v $
 * Revision 1.2  2013/06/28 08:55:02  cchun
 * Refactor:重构
 *
 * Revision 1.1  2013/03/29 09:36:50  cchun
 * Add:创建
 *
 * Revision 1.1  2010/12/29 06:46:39  cchun
 * Add:添加进度条对话框管理类
 *
 */
public class ProgressManager {
	
	private ProgressManager() {}
	
	/**
	 * 执行带进度条的任务
	 * @param runnable
	 */
	public static void execute(IRunnableWithProgress runnable, boolean cancelAble) {
		try {
			/**
			 * 创建一个进度条对话框，并将runnable传入。
			 * 第一个参数推荐设为true，如果设为false则处理程序会运行在UI线程里，界面将有一点停滞感。
			 * 第二个参数：true＝对话框的“取消”按钮有效。
			 */
			new WrappedMonitorDialog(new Shell()).run(true, cancelAble, runnable);
		} catch (InvocationTargetException e) {
			SCTLogger.error("目标调用异常：", e);
		} catch (InterruptedException e) {
			SCTLogger.error("中断异常：", e);
		}
	}
	
	public static void execute(IRunnableWithProgress runnable) {
		execute(runnable, true);
	}
}
