/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.test;

import java.io.File;

import org.dom4j.Document;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.dbxapi.util.TestUtil;
import com.shrcn.found.file.xml.XMLFileManager;
import com.ximpleware.VTDGen;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2016-1-13
 */
public class PerformanceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VtdLoader vtdLoader = new VtdLoader();
		DomLoader domLoader = new DomLoader();
		XmlDbLoader xmlDbLoader = new XmlDbLoader();
		ILoader[] loaders = new ILoader[] {
//					vtdLoader, 
//					domLoader, 
					xmlDbLoader
				};
		
		for (ILoader loader : loaders) {
			for (int i=0; i<5; i++) {
				loader.load();
			}
		}
	}
	
}

class VtdLoader extends ScdLoader {
	VTDGen vg;
	@Override
	protected void doLoad() {
		vg = new VTDGen();
		vg.parseFile(scdfile, false);
	}
	@Override
	protected void free() {
		vg.clear();
	}
}

class DomLoader extends ScdLoader {
	Document dom;
	@Override
	protected void doLoad() {
		dom = XMLFileManager.loadXMLFile(scdfile);
	}
	@Override
	protected void free() {
		dom.clearContent();
		dom = null;
	}
}

class XmlDbLoader extends ScdLoader {
	@Override
	protected void doLoad() {
		Constants.XQUERY = true;
		TestUtil.init(scdfile, true);
	}

	@Override
	protected void free() {
	}
}

interface ILoader {
	void load();
}

abstract class ScdLoader implements ILoader {
	
	protected static final String scdfile = 
//			"E:/work/检测/六统一/测试数据/SCD1-1xin-1.scd";
			"E:/work/检测/六统一/测试数据/SCD-电科院/测试用SCD-5/典型500kV智能变电站SCD1-1文件/SCD1-1.scd";
//			"E:/work/检测/六统一/测试数据/SCD-电科院/测试用SCD-3/测试SCD（导出CCD用）.scd";
	
	@Override
	public void load() {
		TimeCounter.begin();
		doLoad();
		TimeCounter.end(getClassName() + "SCD解析完毕", true);
//		showMemory();
//		try {
//			Thread.sleep(30000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		free();
		System.gc();
		System.gc();
	}
	
	abstract protected void doLoad();
	abstract protected void free();

	private static final int mb = 1024*1024;
	
	protected void showMemory() {
		long usage = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
//		double times = usage / new File(scdfile).length();
		System.out.println(getClassName() + "占用内存：" + (usage/mb) + "mb字节");
		System.out.println(getClassName() + "空闲内存：" + (free/mb) + "mb字节");
//		System.out.println(getClassName() + "占用内存倍数：" + times);
	}

	protected String getClassName() {
		return getClass().getSimpleName();
	}
}

