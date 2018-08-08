/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.shrcn.found.common.util.TimeCounter;
import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-9-29
 */
public class VtdTest1 {

	@Test
	public void testPerformance() {
		TimeCounter.begin();

//		String scdfile = "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-1)_1000个ied.scd";
		 String scdfile = "E:/work/检测/六统一/测试数据/SCD1-1xin-1.scd";
		// "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-2)_错误xml.scd";
		VTDGen vg = new VTDGen();
		vg.parseFile(scdfile, false);

		TimeCounter.end("SCD解析完毕", true);
		
		try {
			XMLModifier xm = new XMLModifier(vg.getNav());
			xm.output("E:/ied1000.scd");
		} catch (ModifyException | TranscodeException | IOException e) {
			e.printStackTrace();
		}
		
		TimeCounter.end("SCD保存完毕", true);
	}

	public static void main(String[] args) {
		try {
			// open a file and read the content into a byte array
			VTDGen vg = new VTDGen();
			String path = VtdTest1.class.getResource("").getPath();
			System.out.println(path);

			if (vg.parseFile(path + "oldpo.xml", false)) {
				VTDNav vn = vg.getNav();
				File fo = new File("./newpo.xml");
				FileOutputStream fos = new FileOutputStream(fo);
				AutoPilot ap = new AutoPilot(vn);
				XMLModifier xm = new XMLModifier(vn);
				ap.selectXPath("/purchaseOrder/items/item[@partNum='872-AA']");
				int i = -1;
//				while ((i = ap.evalXPath()) != -1) {
//					xm.remove();
//					xm.insertBeforeElement("<something/>\n");
//				}
//				int i = ap.evalXPath();
//				if (i != -1) {
//					vn.recoverNode(i);
//					vn.overWrite(i, "<something/>\n".getBytes());
//				}
				
				ap.selectXPath("/purchaseOrder/items/item/USPrice[.<40]/text()");
				while ((i = ap.evalXPath()) != -1) {
					// printElement(vn, i);
					xm.updateToken(i, "200");
				}
				xm.output(fos);
				
//				ap.selectXPath("/purchaseOrder/items/item[@partNum='872-AA']");
//				i = -1;
//				while ((i = ap.evalXPath()) != -1) {
//					 System.out.println("cc ==> " +
//					 vn.toString(vn.getAttrVal("aa:cc")));
//				}
				vn.dumpXML(fos);
			}
		} catch (NavException e) {
			System.out.println(" Exception during navigation " + e);
		} catch (ModifyException e) {
			System.out.println(" Modify exception occurred " + e);
		} catch (Exception e) {
		}
	}

	private static void printElement(VTDNav vn, int i) throws NavException {
		// System.out.println(vn.toString(i+2));
		System.out.print("Element name ==> " + vn.toString(i));
		int t = vn.getText(); // get the index of the text (char data or CDATA)
		if (t != -1)
			System.out.println(" Text  ==> " + vn.toNormalizedString(t));
		System.out.println("\n ============================== ");
	}

	private static void printAttr(VTDNav vn, int i) throws NavException {
		// int index = i+1;
		// while(vn.getAttrCount())
		// vn.get
	}
}
