/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-6-15
 */
/**
 * $Log: CoordTransformer.java,v $
 * Revision 1.1  2012/06/18 09:38:15  cchun
 * Refactor:转移项目
 *
 * Revision 1.1  2011/06/17 06:32:53  cchun
 * Add:dxf导出API
 *
 */
public class CoordTransformer {
	
	private int canvasHeight;
	
	public CoordTransformer(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}
	
	/**
	 * 将draw2d坐标换成dxf坐标
	 * @param p
	 * @return
	 */
	public Vector getVector(Point p) {
		return new Vector(p.x, canvasHeight - p.y);
	}
	
	/**
	 * 根据draw2d矩形左上角坐标和大小值获取dxf矩形坐标
	 * @param loc
	 * @param size
	 * @return
	 */
	public Vector[] getPolylineRect(Point loc, Dimension size) {
		Vector vLoc = getVector(loc);
		return new Vector[] {
				vLoc,
				new Vector(vLoc.getX() + size.width, vLoc.getY()),
				new Vector(vLoc.getX(), vLoc.getY() - size.height),
				new Vector(vLoc.getX() + size.width, vLoc.getY() - size.height),
		};
	}
}
