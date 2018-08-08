/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.chart;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-10-29
 */
/**
 * $Log: JFreeChartBuilder.java,v $
 * Revision 1.1  2013/10/29 12:50:20  cchun
 * Refactor:提取图标创建公共类
 *
 */
public class JFreeChartBuilder {

//	private static final int chartHeightMin = 30;
//	private static final float lw = 0.5F;
	protected static Color phaseDefault = Color.white;
	protected static Map<String, Color> phaseColor = new HashMap<String, Color>();
	
	protected int maxX = 0;

	static {
		//创建主题样式  
	   StandardChartTheme standardChartTheme = new StandardChartTheme("CN");  
	   //设置标题字体  
	   standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));  
	   //设置图例的字体  
	   standardChartTheme.setRegularFont(new Font("宋书", Font.BOLD, 12));  
	   //设置轴向的字体  
	   standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 12));
	   // 阴影样式
	   standardChartTheme.setShadowVisible(false);
	   // 坐标轴文字颜色
	   standardChartTheme.setAxisLabelPaint(ChartColor.WHITE);
	   // 图表背景
	   standardChartTheme.setChartBackgroundPaint(ChartColor.BLACK);
	   //应用主题样式  
	   ChartFactory.setChartTheme(standardChartTheme);
	   
	   phaseColor.put("A", Color.yellow);
	   phaseColor.put("B", Color.green);
	   phaseColor.put("C", Color.red);
	}
	
	public JFreeChartBuilder(){
	}
	
	protected void setPlotStyle(XYPlot xyplot) {
		xyplot.setDomainCrosshairVisible(true);
		xyplot.setDomainGridlinesVisible(false);
		xyplot.setRangeGridlinesVisible(false);
		xyplot.setBackgroundPaint(ChartColor.BLACK);
		xyplot.setDomainCrosshairPaint(ChartColor.WHITE);
		// 横坐标轴
		ValueAxis valueaxis = xyplot.getDomainAxis();
		valueaxis.setVisible(false);
//		valueaxis.setLabelPaint(ChartColor.WHITE);
//		valueaxis.setTickLabelPaint(ChartColor.WHITE);
//		valueaxis.setAxisLinePaint(ChartColor.WHITE);
		// 纵坐标轴
		ValueAxis rangeaxis = xyplot.getRangeAxis();
		rangeaxis.setVisible(false);
//		xyplot.setDomainPannable(true);
//		xyplot.setRangePannable(true);
//		rangeaxis.setUpperMargin(0.14999999999999999D);
//		rangeaxis.setLabelPaint(ChartColor.WHITE);
//		rangeaxis.setTickLabelPaint(ChartColor.WHITE);
//		rangeaxis.setAxisLinePaint(ChartColor.WHITE);
	}
	

	protected Color getPhaseColor(String key) {
		Color color = phaseColor.get(key);
		return color == null ? phaseDefault : color;
	}

	public int getMaxX() {
		return maxX;
	}
}
