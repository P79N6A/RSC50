/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-7-30
 */
/**
 * $Log: GraphicsBrush.java,v $
 * Revision 1.1  2012/07/31 09:29:59  cchun
 * Add:增加dxf笔刷
 *
 */
public class GraphicsBrush extends AbstractBrush {

	private Graphics g;
	
	public GraphicsBrush(Graphics g) {
		this.g = g;
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		
		g.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		
		g.drawRectangle(x, y, width, height);
	}

	@Override
	public void drawString(String s, int x, int y) {
		
		g.drawString(s, x, y);
	}

	@Override
	public void setBackgroundColor(Color rgb) {
		
		g.setBackgroundColor(rgb);
	}

	@Override
	public void setFont(Font f) {
		g.setFont(f);
	}

	@Override
	public void setForegroundColor(Color rgb) {
		g.setForegroundColor(rgb);
	}

	@Override
	public void setLineWidth(int width) {
		
		g.setLineWidth(width);
	}

	@Override
	public void setXORMode(boolean b) {
		
		g.setXORMode(b);
	}

	@Override
	public void popState() {
		
		g.popState();
	}

	@Override
	public void pushState() {
		
		g.pushState();
	}

	@Override
	public void drawRectangle(Rectangle rect) {
		
		g.drawRectangle(rect);
	}

	@Override
	public void drawOval(Rectangle rect) {
		
		g.drawOval(rect);
	}

}
