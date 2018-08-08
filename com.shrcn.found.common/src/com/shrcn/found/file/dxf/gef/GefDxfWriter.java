/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf.gef;

import java.io.PrintWriter;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.shrcn.found.file.dxf.DefaultDXFWriter;
import com.shrcn.found.file.dxf.Vector;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-6-29
 */
/**
 * $Log: GefDxfWriter.java,v $
 * Revision 1.2  2012/07/31 09:31:22  cchun
 * Update:1、为writeText()增加旋转角度参数；2、增加writeCircle()
 *
 * Revision 1.1  2012/07/02 02:17:22  cchun
 * Add:重构后的dxf输出class
 *
 */
public class GefDxfWriter extends DefaultDXFWriter implements IGefDxfWriter {

	public GefDxfWriter(PrintWriter w) {
		super(w);
	}
	/**
	 * 得到DXF坐标
	 * @param p
	 * @return
	 */
	private Vector getVector(Point p) {
		return new Vector(p.x, -p.y);
	}

	@Override
	public void writeRectangle(Rectangle rect) {
		Point loc = rect.getLocation();
		int w = rect.width;
		int h = rect.height;
		Vector[] points = new Vector[] {
			getVector(loc),
			getVector(new Point(loc.x + w, loc.y)),
			getVector(new Point(loc.x + w, loc.y + h)),
			getVector(new Point(loc.x, loc.y + h))
		};
		writeRectangle(points);
	}
	
	/**
	 * 输出文字标签
	 * @param label
	 */
	@Override
	public void writeLabel(Label label) {
		Point loc = label.getLocation();
		writeText(getVector(new Point(loc.x, loc.y + 15)), 
				FONT_HEIGHT, label.getText());
	}

	@Override
	public void writeLine(Point start, Point end) {
		writeLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void writeLine(int startX, int startY, int endX, int endY) {
		writeLine(getVector(new Point(startX, startY))
				, getVector(new Point(endX, endY)));
	}
	
	@Override
	public void writeLineDecoration(PolylineDecoration fig) {
    	PointList pointList= fig.getPoints();
    	Point middle = pointList.getPoint(1);
    	Point start = pointList.getPoint(0);
		Point end = pointList.getPoint(2);
		writeLine(start.x , start.y , middle.x, middle.y);
		writeLine(end.x, end.y, middle.x , middle.y);
    }
	
	/**
	 * 输出装置间连线(draw project)
	 * @param connf
	 */
	@Override
	public void writeConnection(Connection conn) {
		PointList points = conn.getPoints();
		int size = points.size();
		for (int i=0; i<size-1; i++) {
			Point p1 = points.getPoint(i);
			Point p2 = points.getPoint(i+1);
			writeLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	@Override
	public void writeText(Point loc, String txt) {
		writeText(loc, FONT_HEIGHT12, txt);
	}
	
	@Override
	public void writeText(Point loc, int height, String txt) {
		writeText(loc, height, 0, txt);
	}
	
	@Override
	public void writeText(Point loc, int height, float rotate, String txt) {
		writeText(getVector(new Point(loc.x, loc.y)), height, rotate, txt);
	}
	
	@Override
	public void writeCircle(Rectangle rect) {
		Point loc = rect.getLocation();
		int r = rect.width / 2;
		Vector locP = getVector(new Point(loc.x+r, loc.y+r));
		writeCircle(locP, r);
	}
}
