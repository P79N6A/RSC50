/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-7-30
 */
/**
 * $Log: DxfBrush.java,v $
 * Revision 1.1  2012/07/31 09:29:59  cchun
 * Add:增加dxf笔刷
 *
 */
public class DxfBrush extends AbstractBrush {

	private IGefDxfWriter w;
	private Font font;
	
	public DxfBrush(IGefDxfWriter writer) {
		this.w = writer;
	}
	
	@Override
	public void setFont(Font f) {
		this.font = f;
	}
	
	@Override
	public void drawLine(int x1, int y1, int x2, int y2) {
		w.writeLine(x1, y1, x2, y2);
	}

	public void drawRectangle(Rectangle rect) {
		w.writeRectangle(rect);
	}
	
	public void drawOval(Rectangle rect) {
		w.writeCircle(rect);
	}
	
	@Override
	public void drawRectangle(int x, int y, int width, int height) {
		w.writeRectangle(new Rectangle(x, y, width, height));
	}

	@Override
	public void drawString(String s, int x, int y) {
		int h = 6;
		if (font != null) {
			FontData fd = font.getFontData()[0];
			h = (int) fd.height;
		}
		h += StringUtil.hasChinese(s) ? 4 : 0;
		w.writeText(new Point(x, y + 10), h, s);
	}

}
