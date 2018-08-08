/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.shrcn.found.file.dxf.IDXFWriter;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-6-29
 */
/**
 * $Log: IGefDxfWriter.java,v $
 * Revision 1.2  2012/07/31 09:31:21  cchun
 * Update:1、为writeText()增加旋转角度参数；2、增加writeCircle()
 *
 * Revision 1.1  2012/07/02 02:17:22  cchun
 * Add:重构后的dxf输出class
 *
 */
public interface IGefDxfWriter extends IDXFWriter {
	
	/**
	 * 字体高度
	 */
	public static final int FONT_HEIGHT = 8;
	public static final int FONT_HEIGHT12 = 12;
	public static final int FONT_HEIGHT16 = 16;
	public static final int FONT_HEIGHT24 = 24;
	
	/**
	 * 输出矩形
	 * @param points
	 */
	public void writeRectangle(Rectangle rect);
	
	public void writeCircle(Rectangle rect);
	
	/**
	 * 输出标签
	 * @param label
	 */
	public void writeLabel(Label label);

	/**
	 * 输出文字
	 * @param txt
	 * @param point
	 */
	public void writeText(Point loc, String txt);
	
	/**
	 * 输出文字
	 * @param loc
	 * @param height
	 * @param txt
	 */
	public void writeText(Point loc, int height, String txt);
	
	public void writeText(Point loc, int height, float rotate, String txt);
	
	/**
	 * 输出线段
	 * @param start
	 * @param end
	 */
	public void writeLine(Point start, Point end);
	
	/**
	 * 输出线段
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public void writeLine(int startX, int startY, int endX, int endY);
	
	/**
	 * 输出连线
	 * @param conn
	 */
	public void writeConnection(Connection conn);
	
	/**
	 * 输出连线箭头
	 * @param fig
	 */
	public void writeLineDecoration(PolylineDecoration fig);
	
}
