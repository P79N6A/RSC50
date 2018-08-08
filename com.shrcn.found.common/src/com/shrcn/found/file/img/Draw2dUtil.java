/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.img;

import java.io.ByteArrayOutputStream;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-1-25
 */
/**
 * $Log: Draw2dUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:47  cchun
 * Add:创建
 *
 * Revision 1.1  2011/01/25 07:03:21  cchun
 * Add:DRAW2D工具类
 *
 */
public class Draw2dUtil {

	private Draw2dUtil() {}
	
	/**
	 * 创建图形数据
	 * @param figure
	 * @param format
	 * @return
	 */
	public static byte[] createImage(IFigure figure, int format) {
		Rectangle r = figure.getBounds();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		Image image = null;
		GC gc = null;
		Graphics g = null;
		try {
			image = new Image(null, r.width, r.height);
			gc = new GC(image);
			g = new SWTGraphics(gc);
			g.translate(r.x * -1, r.y * -1);
			figure.paint(g);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { image.getImageData() };
			imageLoader.save(result, format);
		} finally {
			if (g != null) {
				g.dispose();
			}
			if (gc != null) {
				gc.dispose();
			}
			if (image != null) {
				image.dispose();
			}
		}
		return result.toByteArray();
	}
}
