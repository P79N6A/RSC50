/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.io.scd;

import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2016-12-27
 */
public class SclUtil {

	/**
	 * iedName ldInst / prefix lnClass lnInst $ fc doName $ daName
	 * @param iedName
	 * @param el
	 * @return
	 */
	public static String getCfgRef(String iedName, Element el) {
		return iedName + getFcdaRef(el);
	}
	
	public static String getFcdaRef(Element el) {
		String ldInst = el.attributeValue("ldInst");
		String prefix = el.attributeValue("prefix");
		String lnClass = el.attributeValue("lnClass");
		String lnInst = el.attributeValue("lnInst");
		String doName = el.attributeValue("doName");
		String daName = el.attributeValue("daName");
		String fc = el.attributeValue("fc");
		StringBuffer sb = new StringBuffer();
		sb.append(ldInst + "/");
		if (!StringUtil.isEmpty(prefix))
			sb.append(prefix);
		sb.append(lnClass);
		if (!StringUtil.isEmpty(lnInst))
			sb.append(lnInst);
		sb.append("$");
		if (!StringUtil.isEmpty(fc))
			sb.append(fc);
		sb.append("$" + doName);
		if (daName != null && !"MX".equals(fc))
			sb.append("$" + daName);
		return sb.toString();
	}

	public static String getDoRef(Element fcdaEl, String daName) {
		String ref = fcdaEl.attributeValue("ref");
		boolean empty = StringUtil.isEmpty(daName);
		int index = ref.lastIndexOf(daName);
		return (empty || index < 1) ? ref : ref.substring(0, index - 1);
	}
	
	/**
	 * IEDName $ LDName $ LNClass + LNInst $ FC $ doName $ daName
	 * 
	 * @param el
	 * @return
	 */
	public static String getRef(String iedName, Element el) {
		String ldInst = el.attributeValue("ldInst");
		String prefix = el.attributeValue("prefix");
		String lnClass = el.attributeValue("lnClass");
		String lnInst = el.attributeValue("lnInst");
		String doName = el.attributeValue("doName");
		String daName = el.attributeValue("daName");
		String fc = el.attributeValue("fc");
		StringBuffer sb = new StringBuffer();
		sb.append(iedName + "$" + ldInst + "$");
		if (prefix != null)
			sb.append(prefix);
		sb.append(lnClass);
		if (lnInst != null)
			sb.append(lnInst);
		sb.append("$" + fc + "$" + doName);
		if (daName != null)
			sb.append("$" + daName);
		return sb.toString();
	}
	
	public static void addSAddr(Element fcdaEl, String sAddr) {
		List<String> addrs = ExportUtil.getSaddrs(sAddr);
		for (String addr : addrs) {
			Element item = fcdaEl.addElement("Item");
			DOM4JNodeHelper.addAttribute(item, "addr", addr);
		}
	}
}

