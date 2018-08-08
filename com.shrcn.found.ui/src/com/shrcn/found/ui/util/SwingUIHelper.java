/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.ui.util;

import java.awt.Frame;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-31
 */
/**
 * $Log: SwingUIHelper.java,v $
 * Revision 1.1  2013/07/29 03:50:11  cchun
 * Add:创建
 *
 * Revision 1.4  2011/08/30 02:23:52  cchun
 * Update:整理代码
 *
 * Revision 1.3  2010/08/30 01:38:33  cchun
 * Update:添加getComponentFrame()
 *
 * Revision 1.2  2009/09/16 08:25:41  cchun
 * Update:增加确认对话框
 *
 * Revision 1.1  2009/08/31 08:49:45  cchun
 * Update:完成图形与树的联动
 *
 */
public class SwingUIHelper {
	
	/**
	 * 弹出告警框
	 * @param msg
	 */
	public static void showWarning(String msg) {
		final JPanel panel = new JPanel();
	    JOptionPane.showMessageDialog(panel, msg, "警告",
	        JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 弹出告警框
	 * @param panel
	 * @param msg
	 */
	public static void showWarning(JPanel panel, String msg) {
		JOptionPane.showMessageDialog(panel, msg, "警告",
		        JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * 弹出确认框
	 * @param msg
	 */
	public static int showConfirm(String msg) {
		final JPanel panel = new JPanel();
	    return JOptionPane.showConfirmDialog(panel, msg);
	}
	
	/**
	 * 获取JComponent对象所在frame
	 * @param jc
	 * @return
	 */
	public static Frame getComponentFrame(JComponent jc) {
		return (Frame) SwingUtilities.getWindowAncestor(jc);
	}
}
