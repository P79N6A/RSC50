/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.chart;

import java.awt.Dimension;
import java.awt.event.MouseListener;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-05-26
 */
public class JFreeChartFactory {
	
	public static final int SwingChartHeightMin = 30;
	public static final int SwtChartHeightMin = 60;

	protected static ChartPanel createChartPanel(JFreeChart jfreechart, int width) {
		ChartPanel chartPanel = new ChartPanel(jfreechart);
		chartPanel.setPreferredSize(new Dimension(width, SwingChartHeightMin));
		chartPanel.setPopupMenu(null);
//		MouseListener[] listeners = chartPanel.getMouseListeners();
//		for (MouseListener listener : listeners) {
//			chartPanel.removeMouseListener(listener);
//		}
		return chartPanel;
	}
}
