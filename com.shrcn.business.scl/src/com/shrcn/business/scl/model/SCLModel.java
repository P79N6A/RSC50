/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import static com.shrcn.business.scl.model.SCL.EXTREF_DANAME;
import static com.shrcn.business.scl.model.SCL.EXTREF_DONAME;
import static com.shrcn.business.scl.model.SCL.EXTREF_IEDNAME;
import static com.shrcn.business.scl.model.SCL.EXTREF_INTADDR;
import static com.shrcn.business.scl.model.SCL.EXTREF_LDINST;
import static com.shrcn.business.scl.model.SCL.EXTREF_LNCALSS;
import static com.shrcn.business.scl.model.SCL.EXTREF_LNINST;
import static com.shrcn.business.scl.model.SCL.EXTREF_LNPREFIX;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史
 * $Log: SCLModel.java,v $
 * Revision 1.1  2013/03/29 09:36:36  cchun
 * Add:创建
 *
 * Revision 1.5  2011/11/30 11:11:10  cchun
 * Update:简化getLnNodePath()
 *
 * Revision 1.4  2010/11/08 07:13:54  cchun
 * Update:清理引用
 *
 * Revision 1.3  2010/07/29 09:56:41  cchun
 * Update:去掉不必要的daName属性
 *
 * Revision 1.2  2009/05/18 12:31:12  cchun
 * Update:增加getLnName()
 *
 * Revision 1.1  2009/05/18 05:57:20  cchun
 * Add:SCL节点操作公共类
 *
 */
public class SCLModel {

	/**
	 * 创建ExtRef空节点
	 * @return
	 */
	public static Element createExtRef() {
		Element data = DOM4JNodeHelper.createSCLNode("ExtRef");
		data.addAttribute(EXTREF_IEDNAME, "");
		data.addAttribute(EXTREF_LDINST, "");
		data.addAttribute(EXTREF_LNPREFIX, "");
		data.addAttribute(EXTREF_LNCALSS, "");
		data.addAttribute(EXTREF_LNINST, "");
		data.addAttribute(EXTREF_DONAME, "");
//		data.addAttribute(EXTREF_DANAME, "");
		data.addAttribute(EXTREF_INTADDR, "");
		return data;
	}
	
	/**
	 * 添加关联信息
	 * @param parent
	 * @param fcdaEntry
	 * @param intAddr
	 * @return
	 */
	public static Element addExtRef(Element parent, FCDAEntry fcdaEntry, String intAddr) {
		Element data = parent.addElement("ExtRef");
		data.addAttribute(EXTREF_IEDNAME, fcdaEntry.getIedName());
		data.addAttribute(EXTREF_LDINST, fcdaEntry.getLdInst());
		data.addAttribute(EXTREF_LNPREFIX, fcdaEntry.getPrefix());
		data.addAttribute(EXTREF_LNCALSS, fcdaEntry.getLnClass());
		data.addAttribute(EXTREF_LNINST, fcdaEntry.getLnInst());
		data.addAttribute(EXTREF_DONAME, fcdaEntry.getDoName());
		data.addAttribute(EXTREF_DANAME, fcdaEntry.getDaName());
		data.addAttribute(EXTREF_INTADDR, intAddr);
		return data;
	}
	
	/**
	 * 根据LN节点获取对应的xpath路径
	 * @param ln
	 * @return
	 */
	public static String getLnNodePath(Element ln) {
		String prefix = ln.attributeValue("prefix");
		String lnClass = ln.attributeValue("lnClass");
		String lnInst = ln.attributeValue("inst");
		return SCL.getLNXPath(prefix, lnClass, lnInst);
	}
	
	/**
	 * 根据LN节点获取其名称
	 * @param ln
	 * @return
	 */
	public static String getLnName(Element ln) {
		String prefix = ln.attributeValue("prefix", "");
		String lnClass = ln.attributeValue("lnClass");
		String lnInst = ln.attributeValue("inst", "");
		return prefix + lnClass + lnInst;
	}
}
