/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.xml.schema;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-4-30
 */
/**
 * $Log: EnumLNUtil.java,v $
 * Revision 1.1  2013/03/29 09:38:02  cchun
 * Add:创建
 *
 * Revision 1.5  2011/04/12 09:18:06  cchun
 * Fix Bug:不应该覆盖用户修改过的lnClass.xml文件
 *
 * Revision 1.4  2011/01/25 02:06:30  cchun
 * Add:聂国勇增加，增加同时添加多个逻辑节点功能
 *
 * Revision 1.3  2010/05/26 01:30:01  cchun
 * Update:添加lnode菜单配置文件功能
 *
 * Revision 1.2  2010/04/30 06:29:59  cchun
 * Update:整理代码
 *
 * Revision 1.1  2010/04/30 06:28:40  cchun
 * Add:lnClass直接从schema读取
 *
 */
public class EnumLNUtil {
	
	private static final String pack = "com/shrcn/business/xml/schema/";
	public static final String sclEnums = pack + "xsd/SCL_Enums.xsd";
	public static final String lnClass = pack + "LnClass.xml";
	public static final String fLnClass = Constants.cfgDir + File.separator + "LnClass.xml";
	
	static {
		InputStream is = (InputStream) EnumLNUtil.class.getClassLoader().getResourceAsStream(EnumLNUtil.lnClass);
		File lnFile = new File(fLnClass);
		if (!lnFile.exists())
			FileManager.saveInputStream(is, lnFile);
	}

	/**
	 * 查询LN集合
	 * @return
	 */
	public static List<LnClass> getDomainLNs() {
		Document doc = XMLFileManager.loadXMLFile(EnumLNUtil.class, sclEnums);
		Element root = doc.getRootElement();
		String tLNDomainsXPath = "/xs:schema/xs:simpleType[@name='tDomainLNEnum']/xs:union/@memberTypes";
		String[] tLNDomains = DOM4JNodeHelper.getAttributeValue(root, tLNDomainsXPath).split(" ");
		int preLen = "tDomainLNGroup".length();
		List<LnClass> groups = new ArrayList<LnClass>();
		for(String tLNDomain : tLNDomains) {
			String lnGroup = tLNDomain.substring(preLen, preLen + 1) + "xxx";
			String lnGroupDesc = Messages.getString(lnGroup);
			LnClass lnClass = new LnClass(lnGroup, lnGroupDesc);
			String lnGroupXPath = "/xs:schema/xs:simpleType[@name='" + tLNDomain +
					"']/xs:restriction/xs:enumeration/@value";
			List<String> lns = DOM4JNodeHelper.getAttributeValues(root, lnGroupXPath);
			for(String ln : lns) {
				lnClass.addChild(new LnClass(ln, Messages.getString(ln)));
			}
			groups.add(lnClass);
		}
		return groups;
	}
	
	/**
	 * 读取lnClass配置文件得到lnClass信息，以便自动生成lnode添加菜单项。
	 * 一层循环得到根元素下的子元素LNTYPE
	 * 取得子元素LNTYPE下的属性NAME与DESC
	 * 二层循环得到LNTYPE元素下的子元素LNCLASS
	 * 取得子元素LNCLASS元素下的属性VALUE与DESC
	 * @return
	 */
	public static List<LnClass> getXMLLNs() {
		File lnClassFile = new File(fLnClass);
		Document doc = null;
		if (lnClassFile.exists()) {
			doc = XMLFileManager.loadXMLFile(fLnClass);
		} else {
			doc = XMLFileManager.loadXMLFile(EnumLNUtil.class, lnClass);
		}
		Element root = doc.getRootElement();
		List<LnClass> groups = new ArrayList<LnClass>();
		// 两层循环遍历document
		List<?> lsType = root.elements();
		for (int i = 0; i < lsType.size(); i++) {
			Element type = (Element) lsType.get(i);
			String tName = type.attributeValue("name");
			String tDesc = type.attributeValue("desc");
			LnClass lnClass = new LnClass(tName, tDesc);
			List<?> lsClass = type.elements();
			for (int j = 0; j < lsClass.size(); j++) {
				Element tClass = (Element) lsClass.get(j);
				String cName = tClass.attributeValue("value");
				String cDesc = tClass.attributeValue("desc");
				LnClass child =new LnClass(cName, cDesc);
				lnClass.addChild(child);
				child.setParent(lnClass);
			}
			groups.add(lnClass);
		}
		return groups;
	}
	
}