/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-7-30
 */
/**
 * $Log: AbstractBrush.java,v $
 * Revision 1.1  2012/07/31 09:29:58  cchun
 * Add:增加dxf笔刷
 *
 */
public abstract class AbstractBrush {

	
	public void drawLine(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		
	}

	public void drawRectangle(Rectangle rect) {
		
	}
	
	public void drawOval(Rectangle rect) {
		
	}
	
	public void drawRectangle(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	public void drawString(String s, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	
	public void setBackgroundColor(Color rgb) {
		// TODO Auto-generated method stub
		
	}

	
	public void setFont(Font f) {
		// TODO Auto-generated method stub
		
	}
	
	public void setLineWidth(int width) {
		// TODO Auto-generated method stub
		
	}

	public void setXORMode(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setForegroundColor(Color rgb) {
		// TODO Auto-generated method stub
		
	}

	
	public void popState() {
		// TODO Auto-generated method stub
		
	}

	
	public void pushState() {
		// TODO Auto-generated method stub
		
	}

}
