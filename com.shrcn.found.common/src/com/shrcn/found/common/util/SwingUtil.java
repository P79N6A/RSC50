/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-7
 */
/**
 * $Log: SwingUtil.java,v $
 * Revision 1.1  2013/07/29 03:50:11  cchun
 * Add:创建
 *
 * Revision 1.1  2011/07/13 09:07:32  cchun
 * Add:swing界面工具类
 *
 */
public class SwingUtil {
	
	private SwingUtil() {}

	/**
	 * 创建对齐约束类
	 * @param gridx
	 * @param gridy
	 * @param weightx
	 * @param weighty
	 * @return
	 */
	public static GridBagConstraints createConstraints(int gridx, int gridy, double weightx, double weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;
		c.gridy = gridy;
		c.weightx = weightx;
		c.weighty = weighty;
		return c;
	}
	
	public static GridBagConstraints createConstraints(int gridx, int gridy, double weightx, double weighty, int anchor, int fill, Insets insets) {
		return new GridBagConstraints(gridx, gridy,
                1, 1,
                weightx, weighty,
                anchor, fill,
                insets, 0, 0);
	}
	
	/**
	 * 设置界面缺省字体
	 */
	public static void setDefaultFont() {
		Font font = new Font("宋体", Font.PLAIN, 12);
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			if (UIManager.get(key) instanceof FontUIResource) {
				UIManager.put(key, new FontUIResource(font));
			} else if (UIManager.get(key) instanceof Font) {
				UIManager.put(key, font);
			}
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
}
