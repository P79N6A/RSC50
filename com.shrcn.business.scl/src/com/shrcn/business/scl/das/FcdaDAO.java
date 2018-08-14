/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-10-29
 */
/**
 * $Log: FcdaDAO.java,v $
 * Revision 1.2  2013/05/27 03:05:46  cchun
 * Update:添加clear()
 *
 * Revision 1.1  2013/03/29 09:36:23  cchun
 * Add:创建
 *
 * Revision 1.11  2012/10/23 03:58:14  cchun
 * Update:DO描述为空时去掉尾部的'/'
 *
 * Revision 1.10  2012/10/22 07:23:17  cchun
 * Refactor:拆分getFCDADesc()
 *
 * Revision 1.9  2012/01/17 08:50:29  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.8  2011/12/02 03:38:29  cchun
 * Refactor:修改函数名queryLDevice()
 *
 * Revision 1.7  2011/11/30 11:00:17  cchun
 * Update:增加getFCDADesc()
 *
 * Revision 1.6  2011/11/22 08:33:03  cchun
 * Update:修改getFCDADesc()调整DO描述格式
 *
 * Revision 1.5  2011/08/11 05:47:02  cchun
 * Refactor:统一使用SCL.getDOIDesc()
 *
 * Revision 1.4  2011/08/10 06:29:42  cchun
 * Update:添加getFCDADescOnly()
 *
 * Revision 1.3  2011/07/20 05:53:56  cchun
 * Refactor:优化代码逻辑
 *
 * Revision 1.2  2009/11/13 07:18:10  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.1  2009/10/29 09:08:03  cchun
 * Refactor:统一fcda描述查询
 *
 */
public class FcdaDAO {
	/**
	 * 单例对象
	 */
	private static volatile FcdaDAO instance = new FcdaDAO();
	private static Map<String, Element> ldCache = null;
	
	/**
	 * 单例模式私有构造函数
	 */
	private FcdaDAO(){
		init();
	}

	/**
	 * 获取单例对象
	 */
	public static FcdaDAO getInstance() {
		if(null == instance) {
			synchronized (FcdaDAO.class) {
				if(null == instance) {
					instance = new FcdaDAO();
				}
			}
		} else {
			init();
		}
		return instance;
	}
	
	private static void init() {
		if(null == ldCache)
			ldCache = new HashMap<String, Element>();
		else
			ldCache.clear();
	}
	
	public void clear() {
		ldCache.clear();
	}
	
	/**
	 * 获取fcda描述信息
	 * @param iedName
	 * @param fcda
	 * @return
	 */
	public String getFCDADesc(String iedName, Element fcda) {
		String ldInst = fcda.attributeValue("ldInst");
		String prefix = fcda.attributeValue("prefix");
		String lnClass = fcda.attributeValue("lnClass");
		String lnInst = fcda.attributeValue("lnInst");
		String doName = fcda.attributeValue("doName");
		return getFCDADesc(iedName, ldInst, prefix, lnClass, lnInst, doName);
	}
	
	/**
	 * 获取fcda描述信息
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param doName
	 * @param daName
	 * @return
	 */
	public String getFCDADesc(String iedName, String ldInst,
			String prefix, String lnClass, String lnInst, String doName) {
		Element ld = queryLDevice(iedName, ldInst);
		return getFCDADesc(ld, prefix, lnClass, lnInst, doName);
	}
	
	/**
	 * 获取fcda描述信息。
	 * @param ld
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param doName
	 * @return
	 */
	public static String getFCDADesc(Element ld,
			String prefix, String lnClass, String lnInst, String doName) {
		if(null == ld)
			return "";
		String ln = SCL.getLNXPath(prefix, lnClass, lnInst);
		String doXPath = SCL.getDOXPath(doName);
		final Element dUNode = DOM4JNodeHelper.selectSingleNode(ld,
				"." + ln + doXPath + "/*[name()='DAI'][@name='dU']/scl:Val");
		String lnDesc = DOM4JNodeHelper.getAttributeByXPath(ld, "." + ln + "/@desc");
		String doDesc = null;
		if(null != dUNode)
			doDesc = dUNode.getStringValue();
		else
			doDesc = DOM4JNodeHelper.getAttributeByXPath(ld, 
					"." + ln + doXPath + "/@desc");
		String desc = null;
		if (!StringUtil.isEmpty(doDesc)) {
			desc = doDesc;
		} else if (!StringUtil.isEmpty(lnDesc)) {
			desc = lnDesc;
		}
		desc = StringUtil.toXMLChars(desc == null ? "" : desc);
		return desc;
	}
	
	/**
	 * 获取do描述
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param doName
	 * @return
	 */
	public String getFCDADescOnly(String iedName, String ldInst,
			String prefix, String lnClass, String lnInst, String doName, String daName) {
		Element ld = queryLDevice(iedName, ldInst);
		if(null == ld)
			return null;
		String ln = SCL.getLNXPath(prefix, lnClass, lnInst);
		String doXPath = SCL.getDOXPath(doName);
		final Element doNode = DOM4JNodeHelper.selectSingleNode(ld, "." + ln + doXPath);
		if (doNode == null)
			return null;
		return SCL.getDOIDesc(doNode, daName);
	}
	
	/**
	 * 根据参引获取描述信息
	 * @param iedName
	 * @param intAddr
	 * @return
	 */
	public String getInnerDesc(String iedName, String intAddr) {
		if(StringUtil.isEmpty(intAddr))
			return "";
		String[] daAddr = intAddr.split("/|\\.");
		String doName = daAddr[2];
		String[] lnInfo = SCLUtil.getLNInfo(daAddr[1]);
		String prefix = lnInfo[0];
		String lnClass = lnInfo[1];
		String lnInst = lnInfo[2];
		if(intAddr.lastIndexOf(".q")>0 || intAddr.lastIndexOf(".t")>0)
			return "";
		return getFCDADesc(iedName, daAddr[0], prefix, lnClass, lnInst, doName);
	}
	
	/**
	 * 获取LD信息
	 * @param iedName
	 * @param ldInst
	 * @return
	 */
	private Element queryLDevice(String iedName, String ldInst) {
		String key = iedName + "$" + ldInst;
		Element ld = ldCache.get(key);
		if(ld != null)
			return ld;
		String xquery = "/scl:SCL/scl:IED[@name='" + iedName + 
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		ld = XMLDBHelper.selectSingleNode(xquery);
		ldCache.put(key, ld);
		return ld;
	}
}
