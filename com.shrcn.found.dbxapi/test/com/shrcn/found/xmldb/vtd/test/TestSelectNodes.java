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
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-9-29
 */
public class TestSelectNodes {

	@Test
	public void testPerformance() {
		TimeCounter.begin();

		String scdfile = "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-1)_1000个ied.scd";
		// String scdfile =
		// "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-2)_错误xml.scd";
		VTDGen vg = new VTDGen();
		vg.parseFile(scdfile, false);

		TimeCounter.end("SCD解析完毕", true);
		
//		try {
//			XMLModifier xm = new XMLModifier(vg.getNav());
//			xm.output("E:/ied1000.scd");
//		} catch (ModifyException | TranscodeException | IOException e) {
//			e.printStackTrace();
//		}
		try {
			vg.getNav().dumpXML("E:/ied1000.scd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TimeCounter.end("SCD保存完毕", true);
	}

	public static void main(String[] args) {
		try {
			// open a file and read the content into a byte array
			VTDGen vg = new VTDGen();
			String path = TestSelectNodes.class.getResource("").getPath();
			System.out.println(path);

			if (vg.parseFile(path + "oldpo.xml", false)) {
				VTDNav vn = vg.getNav();
				File fo = new File("./newpo.xml");
				FileOutputStream fos = new FileOutputStream(fo);
				AutoPilot ap = new AutoPilot(vn);
				
				ap.selectXPath("/purchaseOrder/items/item");
				int i = -1;
				byte[] ba = vn.getXML().getBytes();
				while ((i = ap.evalXPath()) != -1) {
//					 System.out.println("partNum ==> " +
//					 vn.toString(vn.getAttrVal("partNum")));
					
					long l = vn.getElementFragment();
                    int offset = (int)l;
                    int len = (int)(l >> 32);
                    byte[] ndData = new byte[len];
                    System.arraycopy(ba, offset, ndData, 0, len);
                    System.out.println(new String(ndData, "UTF-8"));
				}
			}
		} catch (NavException e) {
			System.out.println(" Exception during navigation " + e);
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
