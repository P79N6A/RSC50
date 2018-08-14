/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 导入ied操作类
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-26
 */
/*
 * 修改历史 $Log: ImportIEDDAO.java,v $
 * 修改历史 Revision 1.24  2013/12/06 01:04:28  scy
 * 修改历史 Fix Bug：修改可能出现的空指针异常。
 * 修改历史
 * 修改历史 Revision 1.23  2013/11/27 10:54:33  cxc
 * 修改历史 update:修改
 * 修改历史
 * 修改历史 Revision 1.22  2013/11/27 10:29:05  cxc
 * 修改历史 update:替换时保留S2访问点
 * 修改历史
 * 修改历史 Revision 1.21  2013/11/11 06:24:57  zq
 * 修改历史 Update: 替换ICD时，同步更新goid和svid
 * 修改历史
 * 修改历史 Revision 1.20  2012/01/17 08:50:29  cchun
 * 修改历史 Update:使用更加安全的xpath形式
 * 修改历史
 * 修改历史 Revision 1.19  2011/11/22 08:36:02  cchun
 * 修改历史 Update:添加existsIED()
 * 修改历史
 * 修改历史 Revision 1.18  2011/11/21 08:30:15  cchun
 * 修改历史 Update:增加IED默认名称处理
 * 修改历史
 * 修改历史 Revision 1.17  2011/11/03 02:30:26  cchun
 * 修改历史 Update:修改addInputsToLn()，增加对<Private>节点的判断
 * 修改历史
 * 修改历史 Revision 1.16  2011/07/27 07:27:45  cchun
 * 修改历史 Refactor:修改replaceIED()参数，移动importDataTypes()到DataTypeTemplateDao
 * 修改历史
 * 修改历史 Revision 1.15  2011/06/27 09:08:11  cchun
 * 修改历史 Update:修改getApsWithFlag()为静态调用
 * 修改历史
 * 修改历史 Revision 1.14  2011/01/10 09:16:23  cchun
 * 修改历史 Refactor:统一数据模板导入逻辑，并增加重复检查
 * 修改历史
 * 修改历史 Revision 1.13  2011/01/07 09:44:39  cchun
 * 修改历史 Update:去掉getSubNetworkNames()
 * 修改历史
 * 修改历史 Revision 1.12  2010/11/08 02:37:28  cchun
 * 修改历史 Add:添加replaceIED()
 * 修改历史
 * 修改历史 Revision 1.11  2010/11/02 06:09:16  cchun
 * 修改历史 Refactor:调整getImportElement()到xml操作类
 * 修改历史
 * 修改历史 Revision 1.10  2010/09/07 09:59:33  cchun
 * 修改历史 Update:更换过时接口
 * 修改历史
 * 修改历史 Revision 1.9  2010/09/03 02:37:12  cchun
 * 修改历史 Update:增加查询IED的type信息
 * 修改历史
 * 修改历史 Revision 1.8  2010/05/31 02:50:59  cchun
 * 修改历史 Update:导入icd,iid时，统一转换namespace
 * 修改历史
 * 修改历史 Revision 1.7  2010/05/19 11:35:50  cchun
 * 修改历史 Fix Bug:修复替换IED后Inputs丢失的bug
 * 修改历史
 * 修改历史 Revision 1.6  2010/05/06 02:21:48  cchun
 * 修改历史 Update:IED替换时，inputs放到gsecontrol之前
 * 修改历史
 * 修改历史 Revision 1.5  2009/12/30 03:12:11  wyh
 * 修改历史 修复添加元素的bug
 * 修改历史
 * 修改历史 Revision 1.4  2009/12/14 10:19:59  wyh
 * 修改历史 增加批量替换IED相关方法
 * 修改历史
 * 修改历史 Revision 1.3  2009/12/09 05:57:02  wyh
 * 修改历史 增加方法：delANDreplaceInputsElement
 * 修改历史
 * 修改历史 Revision 1.2  2009/11/27 01:28:55  wyh
 * 修改历史 添加：ICD文件访问点下若有报告控制块则给出提示
 * 修改历史
 * 修改历史 Revision 1.1  2009/05/26 05:34:52  hqh
 * 修改历史 添加导入ied DAO
 * 修改历史
 */
public class ImportIEDDAO {
	
	/**
	 * 连接点列表
	 * @param root
	 * @return
	 */
	public static List<String> getConnApNames(Element root) {
		Element communicationEle = root.element("IED");
		List<?> subNetworks = communicationEle.elements("AccessPoint");
		List<String> appNames = new LinkedList<String>();
		for (Object obj : subNetworks) {
			Element AP = (Element) obj;
			appNames.add((AP.attributeValue("name")));
		}
		return appNames;
	}
	
	/**
	 * 如果访问点下有报告控制块，则给出相应的提示
	 * @param root
	 * @return
	 */
	public static List<String> getApsWithFlag(Element root) {
		List<String> list = new ArrayList<String>();
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name");
			Element report = (Element) ap.selectSingleNode(".//*[name()='ReportControl']"); 
			if (report != null) {
				apName += "$";
				list.add(apName);
				continue;
			} else {
				list.add(apName);
				continue;
			}
		}
		return list;
	}
	/**
	 * 有报告控制块的访问点
	 * @param root
	 * @return
	 */
	public static List<String> getRpcAps(Element root) {
		List<String> list = new ArrayList<String>();
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name");
			Element report = (Element) ap.selectSingleNode(".//*[name()='ReportControl']"); 
			if (report != null) {
				list.add(apName);
				continue;
			}
		}
		return list;
	}
	/**
	 * 有GSEControl的访问点
	 * @param root
	 * @return
	 */
	public static List<String> getGseAps(Element root) {
		List<String> list = new ArrayList<String>();
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name");
			Element report = (Element) ap.selectSingleNode(".//*[name()='GSEControl']"); 
			if (report != null) {
				list.add(apName);
				continue;
			}
		}
		return list;
	}
	/**
	 * 有SampledValueControl的访问点
	 * @param root
	 * @return
	 */
	public static List<String> getSmvAps(Element root) {
		List<String> list = new ArrayList<String>();
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String apName = ap.attributeValue("name");
			Element report = (Element) ap.selectSingleNode(".//*[name()='SampledValueControl']"); 
			if (report != null) {
				list.add(apName);
				continue;
			}
		}
		return list;
	}
	
	/**
	 * 返回root中的IED名称
	 * @param root
	 * @return
	 */
	public static String getIEDDefaultName(Element root, String fileName){
		Element iedEle = root.element("IED");
		if (iedEle == null) {
			return "";
		}
		String iedName = iedEle.attributeValue("name");
		if ("TEMPLATE".equalsIgnoreCase(iedName)) {
			int pos = fileName.lastIndexOf('.');
			if (pos > -1)
				fileName = fileName.substring(0, pos);
			return fileName;
		} else {
			return iedName;
		}
	}
	
	/**
	 * 返回root中的IED描述
	 * @param root
	 * @return
	 */
	public static String getIEDDefaultDesc(Element root){
		return root.element("IED").attributeValue("name", "");
	}
	
	/**
	 * 设置IED描述
	 * @param root
	 * @param desc
	 */
	public static void setIEDDesc(Element root, String desc){
		root.element("IED").addAttribute("desc", desc);
	}
	
	/**
	 * 判断name是否与SCD中的IED的名称有重复
	 * @param name
	 * @return
	 */
	public static boolean existsIED(String name) {
		String iedXpath = "/scl:SCL/scl:IED[@name='" + name + "']";
		return XMLDBHelper.existsNode(iedXpath);
	}
}
