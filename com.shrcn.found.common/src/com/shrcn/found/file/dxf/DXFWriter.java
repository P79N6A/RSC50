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
 * $Log: DXFWriter.java,v $
 * Revision 1.2  2012/07/25 02:38:55  cchun
 * Update:修改$HANDSEED值为301
 *
 * Revision 1.1  2012/06/18 09:38:15  cchun
 * Refactor:转移项目
 *
 * Revision 1.1  2011/06/17 06:32:54  cchun
 * Add:dxf导出API
 *
 */
final public class DXFWriter {
	
	private PrintWriter w;
	private int handle = 123;
	private static final String geomLayer = "geom";
	private static final String textLayer = "text";
	
	public DXFWriter(PrintWriter w) {
		this.w = w;
	}

	public void writeHeader(String comment, Vector minPoint, Vector maxPoint) {
		wg(999, comment);
		wg(0, "SECTION");
		wg(2, "HEADER");
		wg(9, "$ACADVER");
		wg(1, "AC1009");
		wg(9, "$INSBASE");
		writePoint(new Vector(), 0);
		wg(9, "$EXTMIN");
		writePoint(minPoint, 0);
		wg(9, "$EXTMAX");
		writePoint(maxPoint, 0);
		wg(9, "$LIMMIN");
		wg(10, "0.0");
		wg(20, "0.0");
		wg(9, "$LIMMAX");
		wg(10, "420.0");
		wg(20, "297.0");
		wg(9, "$ORTHOMODE");
		wg(70, "0");
		wg(9, "$REGENMODE");
		wg(70, "1");
		wg(9, "$FILLMODE");
		wg(70, "1");
		wg(9, "$QTEXTMODE");
		wg(70, "0");
		wg(9, "$MIRRTEXT");
		wg(70, "0");
		
		wg(9, "$MIRRTEXT");
		wg(70, "0");
		wg(9, "$DRAGMODE");
		wg(70, "2");
		wg(9, "$LTSCALE");
		wg(40, "1.0");
		wg(9, "$OSMODE");
		wg(70, "37");
		wg(9, "$ATTMODE");
		wg(70, "1");
		wg(9, "$TEXTSIZE");
		wg(40, "2.5");
		wg(9, "$TRACEWID");
		wg(40, "1.0");
		wg(9, "$TEXTSTYLE");
		wg(7, "STANDARD");
		
		w.println("9\n$CLAYER\n8\n0\n9\n$CELTYPE\n6\nBYLAYER\n9\n$CECOLOR\n62\n256\n9\n$DIMSCALE\n40\n1.0\n9\n$DIMASZ\n40\n2.5\n9\n$DIMEXO\n40\n0.625\n9\n$DIMDLI\n40\n3.75\n9\n$DIMRND\n40\n0.0\n9\n$DIMDLE\n40\n0.0\n9\n$DIMEXE\n40\n1.25\n9\n$DIMTP\n40\n0.0\n9\n$DIMTM\n40\n0.0\n9\n$DIMTXT\n40\n2.5\n9\n$DIMCEN\n40\n2.5\n9\n$DIMTSZ\n40\n0.0\n9\n$DIMTOL\n70\n0\n9\n$DIMLIM\n70\n0\n9\n$DIMTIH\n70\n0\n9\n$DIMTOH\n70\n0\n9\n$DIMSE1\n70\n0\n9\n$DIMSE2\n70\n0\n9\n$DIMTAD\n70\n1\n9\n$DIMZIN\n70\n8\n9\n$DIMBLK\n1\n\n9\n$DIMASO\n70\n1\n9\n$DIMSHO\n70\n1\n9\n$DIMPOST\n1\n\n9\n$DIMAPOST\n1\n\n9\n$DIMALT\n70\n0\n9\n$DIMALTD\n70\n3\n9\n$DIMALTF\n40\n0.03937007874016\n9\n$DIMLFAC\n40\n1.0\n9\n$DIMTOFL\n70\n1\n9\n$DIMTVP\n40\n0.0\n9\n$DIMTIX\n70\n0\n9\n$DIMSOXD\n70\n0\n9\n$DIMSAH\n70\n0\n9\n$DIMBLK1\n1\n\n9\n$DIMBLK2\n1\n\n9\n$DIMSTYLE\n2\nISO-25\n9\n$DIMCLRD\n70\n0\n9\n$DIMCLRE\n70\n0\n9\n$DIMCLRT\n70\n0\n9\n$DIMTFAC\n40\n1.0\n9\n$DIMGAP\n40\n0.625\n9\n$LUNITS\n70\n2\n9\n$LUPREC\n70\n4\n9\n$SKETCHINC\n40\n1.0\n9\n$FILLETRAD\n40\n0.0\n9\n$AUNITS\n70\n0\n9\n$AUPREC\n70\n0\n9\n$MENU\n1\n.\n9\n$ELEVATION\n40\n0.0\n9\n$PELEVATION\n40\n0.0\n9\n$THICKNESS\n40\n0.0\n9\n$LIMCHECK\n70\n0\n9\n$BLIPMODE\n70\n0\n9\n$CHAMFERA\n40\n0.0\n9\n$CHAMFERB\n40\n0.0\n9\n$SKPOLY\n70\n0\n9\n$TDCREATE\n40\n2455727.6272694208\n9\n$TDUPDATE\n40\n2455727.6275298381\n9\n$TDINDWG\n40\n0.0002607755\n9\n$TDUSRTIMER\n40\n0.0002607755\n9\n$USRTIMER\n70\n1\n9\n$ANGBASE\n50\n0.0\n9\n$ANGDIR\n70\n0\n9\n$PDMODE\n70\n0\n9\n$PDSIZE\n40\n0.0\n9\n$PLINEWID\n40\n0.0\n9\n$COORDS\n70\n1\n9\n$SPLFRAME\n70\n0\n9\n$SPLINETYPE\n70\n6\n9\n$SPLINESEGS\n70\n8\n9\n$ATTDIA\n70\n0\n9\n$ATTREQ\n70\n1\n9\n$HANDLING\n70\n1\n9\n$HANDSEED\n5\n301\n9\n$SURFTAB1\n70\n6\n9\n$SURFTAB2\n70\n6\n9\n$SURFTYPE\n70\n6\n9\n$SURFU\n70\n6\n9\n$SURFV\n70\n6\n9\n$UCSNAME\n2\n\n9\n$UCSORG\n10\n0.0\n20\n0.0\n30\n0.0\n9\n$UCSXDIR\n10\n1.0\n20\n0.0\n30\n0.0\n9\n$UCSYDIR\n10\n0.0\n20\n1.0\n30\n0.0\n9\n$PUCSNAME\n2\n\n9\n$PUCSORG\n10\n0.0\n20\n0.0\n30\n0.0\n9\n$PUCSXDIR\n10\n1.0\n20\n0.0\n30\n0.0\n9\n$PUCSYDIR\n10\n0.0\n20\n1.0\n30\n0.0\n9\n$USERI1\n70\n0\n9\n$USERI2\n70\n0\n9\n$USERI3\n70\n0\n9\n$USERI4\n70\n0\n9\n$USERI5\n70\n0\n9\n$USERR1\n40\n0.0\n9\n$USERR2\n40\n0.0\n9\n$USERR3\n40\n0.0\n9\n$USERR4\n40\n0.0\n9\n$USERR5\n40\n0.0\n9\n$WORLDVIEW\n70\n1\n9\n$SHADEDGE\n70\n3\n9\n$SHADEDIF\n70\n70\n9\n$TILEMODE\n70\n1\n9\n$MAXACTVP\n70\n64\n9\n$PLIMCHECK\n70\n0\n9\n$PEXTMIN\n10\n0.0\n20\n0.0\n30\n0.0\n9\n$PEXTMAX\n10\n0.0\n20\n0.0\n30\n0.0\n9\n$PLIMMIN\n10\n0.0\n20\n0.0\n9\n$PLIMMAX\n10\n12.0\n20\n9.0\n9\n$UNITMODE\n70\n0\n9\n$VISRETAIN\n70\n1\n9\n$PLINEGEN\n70\n0\n9\n$PSLTSCALE\n70\n1");
		
		wg(0, "ENDSEC");
	}
	
	public void writeTables() {
		wg(0,"SECTION");
		wg(2,"TABLES");
		writeVPortTable();
		writeLineTable();
		writeLayerTable();
		writeStyleTable();
		w.println("0\nTABLE\n2\nVIEW\n70\n0\n0\nENDTAB");
		w.println("0\nTABLE\n2\nUCS\n70\n0\n0\nENDTAB");
		w.println("0\nTABLE\n2\nAPPID\n70\n2\n0\nAPPID\n2\nACAD\n70\n0\n0\nAPPID\n2\nACAD_PSEXT\n70\n0\n0\nENDTAB");
		w.println("0\nTABLE\n2\nDIMSTYLE\n70\n2\n0\nDIMSTYLE\n2\nISO-25\n70\n0\n3\n\n4\n\n5\n\n6\n\n7\n\n40\n1.0\n41\n2.5\n42\n0.625\n43\n3.75\n44\n1.25\n45\n0.0\n46\n0.0\n47\n0.0\n48\n0.0\n140\n2.5\n141\n2.5\n142\n0.0\n143\n0.03937007874016\n144\n1.0\n145\n0.0\n146\n1.0\n147\n0.625\n71\n0\n72\n0\n73\n0\n74\n0\n75\n0\n76\n0\n77\n1\n78\n8\n170\n0\n171\n3\n172\n1\n173\n0\n174\n0\n175\n0\n176\n0\n177\n0\n178\n0\n0\nENDTAB");
		wg(0,"ENDSEC");
	}
	
	public void writeVPortTable() {
		w.println("0\nTABLE\n2\nVPORT\n70\n1\n0\nVPORT\n2\n*ACTIVE\n70\n0\n10\n0.0\n20\n0.0\n11\n1.0\n21\n1.0\n12\n1467.933104234965\n22\n570.93021642829774\n13\n0.0\n23\n0.0\n14\n10.0\n24\n10.0\n15\n10.0\n25\n10.0\n16\n0.0\n26\n0.0\n36\n1.0\n17\n0.0\n27\n0.0\n37\n0.0\n40\n1141.860432856595\n41\n2.571125265392781\n42\n50.0\n43\n0.0\n44\n0.0\n50\n0.0\n51\n0.0\n71\n0\n72\n1000\n73\n1\n74\n3\n75\n0\n76\n0\n77\n0\n78\n0\n0\nENDTAB");
	}
	
	public void writeLineTable() {
		wg(0, "TABLE");
		wg(2, "LTYPE");
		wg(70, "1"); // standard flag values
		wg(0, "LTYPE");
		wg(2, "CONTINUOUS"); // ltype name
		// wg(70,"64"); // standard flag values
		wg(70, "0");
		wg(3, "Solid line"); // ltype text
		wg(72, "65"); // alignment code 'A'
		wg(73, "0"); // dash length items
		wg(40, "0.000000"); // total pattern length
		wg(0, "ENDTAB");
	}
	
	public void writeLayerTable() {
		wg(0, "TABLE");
		wg(2, "LAYER");
		// wg(70,"6"); // standard flag values
		wg(70, "1");
		// LAYER geom
		wg(0, "LAYER");
		wg(2, geomLayer); // layer name
		wg(70, "0"); // standard flag values
		wg(62, "7"); // color number
		wg(6, "CONTINUOUS"); // line type name
		// LAYER text
		wg(0, "LAYER");
		wg(2, textLayer); // layer name
		wg(70, "0"); // standard flag values
		wg(62, "7"); // color number
		wg(6, "CONTINUOUS"); // line type name
		wg(0, "ENDTAB");
	}
	
	public void writeStyleTable() {
		w.println("0\nTABLE\n2\nSTYLE\n70\n1\n0\nSTYLE\n2\nSTANDARD\n70\n0\n40\n0.0\n41\n1.0\n50\n0.0\n71\n0\n42\n2.5\n3\ntxt\n4\ngbcbig.shx\n0\nENDTAB");
	}
	
	public void writeBlocks() {
		w.println("0\nSECTION\n2\nBLOCKS\n0\nBLOCK\n8\n0\n2\n$MODEL_SPACE\n70\n0\n10\n0.0\n20\n0.0\n30\n0.0\n3\n$MODEL_SPACE\n1\n\n0\nENDBLK\n5\n21\n8\n0\n0\nBLOCK\n67\n1\n8\n0\n2\n$PAPER_SPACE\n70\n0\n10\n0.0\n20\n0.0\n30\n0.0\n3\n$PAPER_SPACE\n1\n\n0\nENDBLK\n5\nD5\n67\n1\n8\n0\n0\nENDSEC");
	}

	public void writeEntitiesBegin() {
		wg(0, "SECTION");
		wg(2, "ENTITIES");
	}

	public void writeEntitiesEnd() {
		wg(0, "ENDSEC");
	}
	
	public void writeEnd() {
		wg(0, "EOF");
	}

	void wg(int code, String val) {
		w.println("" + code);
		w.println(val);
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
	 * 输出文本
	 * @param loc
	 * @param height
	 * @param text
	 */
	public void writeText(Vector loc, int height, String text) {
		wg(0, "TEXT");
		writeHandle();
		wg(8, textLayer);
		writePoint(loc, 0);
		wg(40, "" + height);
		wg(1, text);
	}
	
	/**
	 * 输出矩形
	 * @param points
	 * @param layer
	 */
	public void writePolylineRect(Vector[] points) {
		if (points.length != 4)
			return;
		wg(0, "POLYLINE");
		writeHandle();
		wg(8, geomLayer);
		wg(66, "1");
		writePoint(new Vector(), 0);
		wg(70, "1");
		for(Vector p : points) {
			wg(0, "VERTEX");
			writeHandle();
			wg(8, geomLayer);
			writePoint(p, 0);
		}
		wg(0, "SEQEND");
		writeHandle();
		wg(8, geomLayer);
	}
	
	/**
	 * 输出圆形
	 * @param loc
	 * @param radius
	 */
	public void writeCircle(Vector loc, double radius) {
		wg(0, "CIRCLE");
		writeHandle();
		wg(8, geomLayer);
		writePoint(loc, 0);
		wg(40, "" + radius);
	}
}