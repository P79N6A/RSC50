/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf;

import java.io.PrintWriter;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-6-15
 */
/**
 * $Log: DXFWriterNew.java,v $
 * Revision 1.2  2012/07/25 02:38:55  cchun
 * Update:修改$HANDSEED值为301
 *
 * Revision 1.1  2012/06/18 09:38:16  cchun
 * Refactor:转移项目
 *
 * Revision 1.2  2011/06/24 08:14:21  cchun
 * Update:整理代码添加注释
 *
 * Revision 1.1  2011/06/17 06:32:53  cchun
 * Add:dxf导出API
 *
 */
final public class DXFWriterNew {
	
	private PrintWriter w;
	private int handle = 120;
	private static final String geomLayer = "0";
	private static final String textLayer = "1";
	
	/**
	 * 构造函数
	 * @param w
	 */
	public DXFWriterNew(PrintWriter w) {
		this.w = w;
	}

	/**
	 * 输出编码和值
	 * @param code
	 * @param val
	 */
	private void wg(int code, String val) {
		w.println("" + code);
		w.println(val);
	}
	
	/**
	 * 块开始
	 */
	private void beginSection() {
		wg(0, "SECTION");
	}
	
	/**
	 * 块结束
	 */
	private void endSection() {
		wg(0, "ENDSEC");
	}
	
	/**
	 * 文件结束
	 */
	public void writeEnd() {
		wg(0, "EOF");
	}

	/**
	 * 输出文件头
	 * @param comment
	 */
	public void writeHeader(String comment) {
		wg(999, comment);
		beginSection();
		wg(2, "HEADER");
		wg(9, "$ACADVER");
		wg(1, "AC1014");
		wg(9, "$HANDSEED");
		wg(5, "301");
		wg(9, "$INSUNITS");
		wg(70, "4");
		wg(9, "$LIMMIN");
		wg(10, "50.0");
		wg(20, "-180.0");
		wg(9, "$LIMMAX");
		wg(10, "130.0");
		wg(20, "-120.0");
		endSection();
	}
	
	/**
	 * 输出预设置表格
	 */
	public void writeTables() {
		beginSection();
		writeLineTable();
		writeLayerTable();
		writeStyleTable();
		w.println("  0\nTABLE\n  2\nVIEW\n  5\n  7\n100\nAcDbSymbolTable\n  0\nENDTAB\n  0\nTABLE\n  2\nUCS\n  5\n  8\n100\nAcDbSymbolTable\n  0\nENDTAB\n  0\nTABLE\n  2\nAPPID\n  5\n  9\n100\nAcDbSymbolTable\n  0\nAPPID\n  5\n  A\n100\nAcDbSymbolTableRecord\n100\nAcDbRegAppTableRecord\n  2\nACAD\n 70\n0\n  0\nENDTAB\n  0\nTABLE\n  2\nDIMSTYLE\n  5\n  B\n100\nAcDbSymbolTable\n  0\nENDTAB\n  0\nTABLE\n  2\nBLOCK_RECORD\n  5\n  C\n100\nAcDbSymbolTable\n  0\nBLOCK_RECORD\n  5\n  D\n100\nAcDbSymbolTableRecord\n100\nAcDbBlockTableRecord\n  2\n*MODEL_SPACE\n  0\nBLOCK_RECORD\n  5\n  E\n100\nAcDbSymbolTableRecord\n100\nAcDbBlockTableRecord\n  2\n*PAPER_SPACE\n  0\nENDTAB");
		endSection();
	}
	
	/**
	 * 输出线型参数表格
	 */
	private void writeLineTable() {
		w.println("  2\nTABLES\n  0\nTABLE\n  2\nVPORT\n  5\n  1\n100\nAcDbSymbolTable\n  0\nENDTAB\n  0\nTABLE\n  2\nLTYPE\n  5\n  2\n100\nAcDbSymbolTable\n  0\nLTYPE\n  5\n  3\n100\nAcDbSymbolTableRecord\n100\nAcDbLinetypeTableRecord\n  2\nBYBLOCK\n 70\n     0\n  0\nLTYPE\n  5\n  4\n100\nAcDbSymbolTableRecord\n100\nAcDbLinetypeTableRecord\n  2\nBYLAYER\n 70\n     0\n  0\nENDTAB");
	}
	
	/**
	 * 输出层定义表格
	 */
	private void writeLayerTable() {
		w.println("  0\nTABLE\n  2\nLAYER\n  5\n 1A\n330\n  0\n100\nAcDbSymbolTable\n0\nLAYER\n5\n24\n330\n 1A\n100\nAcDbSymbolTableRecord\n100\nAcDbLayerTableRecord\n2\n" +
				geomLayer +
				"\n70\n0\n62\n7\n6\nContinuous\n0\nLAYER\n5\n25\n330\n 1A\n100\nAcDbSymbolTableRecord\n100\nAcDbLayerTableRecord\n2\n" +
				textLayer +
				"\n70\n0\n62\n7\n6\nContinuous\n  0\nENDTAB");
	}
	
	/**
	 * 输出样式表格
	 */
	private void writeStyleTable() {
		w.println("  0\nTABLE\n  2\nSTYLE\n  5\n  5\n100\nAcDbSymbolTable\n  0\nSTYLE\n  5\n  6\n100\nAcDbSymbolTableRecord\n100\nAcDbTextStyleTableRecord\n  2\nSTANDARD\n 70\n     0\n 40\n0.0\n 41\n1.0\n 50\n0.0\n 71\n     0\n 42\n10.0\n  3\ntxt\n  4\ngbcbig.shx\n  0\nENDTAB");
	}
	
	/**
	 * 输出图形块表格
	 */
	public void writeBlocks() {
		wg(0, "SECTION");
		w.println("  2\nBLOCKS\n  0\nBLOCK\n  5\n  F\n330\n  D\n100\nAcDbEntity\n  8\n0\n100\nAcDbBlockBegin\n  2\n*MODEL_SPACE\n 70\n     0\n  0\nENDBLK\n  5\n 10\n100\nAcDbEntity\n  8\n0\n100\nAcDbBlockEnd\n  0\nBLOCK\n  5\n 11\n330\n  E\n100\nAcDbEntity\n  8\n0\n100\nAcDbBlockBegin\n  2\n*PAPER_SPACE\n 70\n     0\n  0\nENDBLK\n  5\n 12\n100\nAcDbEntity\n  8\n0\n100\nAcDbBlockEnd");
		wg(0, "ENDSEC");
	}

	/**
	 * 输出Object字典
	 */
	public void writeDictionary() {
		w.println(" 0\nSECTION\n  2\nOBJECTS\n  0\nDICTIONARY\n  5\n13\n100\nAcDbDictionary\n  3\nACAD_GROUP\n350\n14\n  3\nACAD_MLINESTYLE\n350\n15\n  0\nDICTIONARY\n  5\n14\n102\n{ACAD_REACTORS\n330\n13\n102\n}\n100\nAcDbDictionary\n  0\nDICTIONARY\n  5\n15\n102\n{ACAD_REACTORS\n330\n13\n102\n}\n100\nAcDbDictionary\n 3\nSTANDARD\n350\n16\n  0\nMLINESTYLE\n  5\n16\n102\n{ACAD_REACTORS\n330\n15\n102\n}\n100\nAcDbMlineStyle\n  2\nSTANDARD\n 70\n     0\n  3\n \n 62\n   256\n 51\n90.0\n 52\n90.0\n 71\n     2\n 49\n0.5\n 62\n   256\n  6\nBYLAYER\n 49\n-0.5\n 62\n   256\n  6\nBYLAYER\n  0\nENDSEC");
	}

	/**
	 * 输出图形实体开始标签
	 */
	public void writeEntitiesBegin() {
		beginSection();
		wg(2, "ENTITIES");
	}

	/**
	 * 输出图形实体结束标签
	 */
	public void writeEntitiesEnd() {
		endSection();
	}
	
	/**
	 * 得到对象handle值（十六进制）
	 * @return
	 */
	private void writeHandle() {
		wg(5, Integer.toHexString(handle++));
	}
	
	/**
	 * 输出点坐标值
	 * @param v
	 * @param n
	 */
	public void writePoint(Vector v, int n) {
		wg(10 + n, "" + v.getX());
		wg(20 + n, "" + v.getY());
		wg(30 + n, "" + v.getZ());
	}
	
	/**
	 * 输出二维坐标
	 * @param v
	 */
	public void writePoint2D(Vector v, int n) {
		wg(10 + n, "" + v.getX());
		wg(20 + n, "" + v.getY());
	}
	
	/**
	 * 输出文本
	 * @param loc
	 * @param height
	 * @param text
	 */
	public void writeText(Vector loc, int height, String text) {
		wg(0, "TEXT");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, textLayer);
		wg(100, "AcDbText");
//		wg(62, "6");		// 颜色
		writePoint2D(loc, 0);
		wg(40, "" + height);
//		wg(41, "0.1875");	// 比例
		wg(1, text);
		wg(100, "AcDbText");
	}

	/**
	 * 输出线段
	 * @param points
	 * @param layer
	 */
	public void writePoint(Vector p) {
		wg(0, "POINT");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbPoint");
		writePoint2D(p, 0);
	}
	
	/**
	 * 输出连线
	 * @param points
	 * @param layer
	 */
	public void writePolyline(Vector[] points) {
		if (points.length < 2)
			return;
		wg(0, "LWPOLYLINE");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbPolyline");
		wg(90, "" + points.length);
		wg(70, "0");
		for(Vector p : points) {
			writePoint2D(p, 0);
		}
	}
	
	/**
	 * 输出线段
	 * @param points
	 * @param layer
	 */
	public void writeLine(Vector start, Vector end) {
		wg(0, "LINE");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbLine");
		writePoint2D(start, 0);
		writePoint2D(end, 1);
	}
	
	/**
	 * 输出矩形
	 * @param points
	 * @param layer
	 */
	public void writeRectangle(Vector[] points) {
		if (points.length != 4)
			return;
		wg(0, "LWPOLYLINE");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbPolyline");
		wg(90, "4");
		wg(70, "0");
		for(Vector p : points) {
			writePoint2D(p, 0);
		}
		writePoint2D(points[0], 0);
	}
	
	/**
	 * 输出圆形
	 * @param loc
	 * @param radius
	 */
	public void writeCircle(Vector loc, double radius) {
		wg(0, "CIRCLE");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbCircle");
		writePoint(loc, 0);
		wg(40, "" + radius);
	}
	
	/**
	 * 输出椭圆
	 * @param loc 中心
	 * @param endP 主轴原点与中心相对坐标
	 * @param ratio 长短轴比例
	 */
	public void writeEllipse(Vector loc, Vector endP, double ratio) {
		wg(0, "ELLIPSE");
		writeHandle();
		wg(100, "AcDbEntity");
		wg(8, geomLayer);
		wg(100, "AcDbEllipse");
		writePoint2D(loc, 0);
		writePoint2D(endP, 1);
		wg(40, "" + ratio);
	}
}