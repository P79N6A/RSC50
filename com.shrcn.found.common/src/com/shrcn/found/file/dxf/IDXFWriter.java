/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-6-29
 */
/**
 * $Log: IDXFWriter.java,v $
 * Revision 1.2  2012/07/31 09:29:42  cchun
 * Update:为writeText()增加旋转角度参数
 *
 * Revision 1.1  2012/07/02 02:17:21  cchun
 * Add:重构后的dxf输出class
 *
 */
public interface IDXFWriter {

	/**
	 * 文件结束
	 */
	public void writeEnd();

	/**
	 * 输出文件头
	 * @param comment
	 */
	public void writeHeader(String comment);

	/**
	 * 输出预设置表格
	 */
	public void writeTables();

	/**
	 * 输出图形块表格
	 */
	public void writeBlocks();

	/**
	 * 输出Object字典
	 */
	public void writeDictionary();

	/**
	 * 输出图形实体开始标签
	 */
	public void writeEntitiesBegin();

	/**
	 * 输出图形实体结束标签
	 */
	public void writeEntitiesEnd();

	/**
	 * 输出点坐标值
	 * @param v
	 * @param n
	 */
	public void writePoint(Vector v, int n);

	/**
	 * 输出二维坐标
	 * @param v
	 */
	public void writePoint2D(Vector v, int n);

	/**
	 * 输出文本
	 * @param loc
	 * @param height
	 * @param text
	 */
	public void writeText(Vector loc, int height, String text);
	
	public void writeText(Vector loc, int height, float rotate, String text);

	/**
	 * 输出线段
	 * @param points
	 * @param layer
	 */
	public void writePoint(Vector p);

	/**
	 * 输出连线
	 * @param points
	 * @param layer
	 */
	public void writePolyline(Vector[] points);

	/**
	 * 输出线段
	 * @param points
	 * @param layer
	 */
	public void writeLine(Vector start, Vector end);

	/**
	 * 输出矩形
	 * @param points
	 */
	public void writeRectangle(Vector[] points);
	
	/**
	 * 输出圆形
	 * @param loc
	 * @param radius
	 */
	public void writeCircle(Vector loc, double radius);

	/**
	 * 输出椭圆
	 * @param loc 中心
	 * @param endP 主轴原点与中心相对坐标
	 * @param ratio 长短轴比例
	 */
	public void writeEllipse(Vector loc, Vector endP, double ratio);

}