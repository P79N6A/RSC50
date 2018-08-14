/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.io.scd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.das.DataTypeTemplateDao;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;


public class ExportUtil {

	public static boolean isT(String daName) {
		return SCL.ATT_T.equals(daName);
	}
	
	public static boolean isQT(String daName) {
		return SCL.ATT_T.equals(daName) || SCL.ATT_Q.equals(daName);
	}

	/**
	 * 解析短地址
	 * @addr
	 */
	public static List<String> getSaddrs(String addr) {
		List<String> addrs = new ArrayList<String>();
		String[] arr = addr.split(Constants.COLON);
		addrs.addAll(Arrays.asList(arr));
		return addrs;
	}

	public static String getDoXpath(String prefix, String lnClass,
			String lnInst, String doName) {
		return "." + getDoSubXpath(prefix, lnClass, lnInst, doName);
	}
	
	public static String getDoSubXpath(String prefix, String lnClass,
			String lnInst, String doName) {
		String ln = SCL.getLNXPath(prefix, lnClass, lnInst);
		return ln + getDoDaXpath(doName);
	}
	
	public static String getDoDaXpath(String dodaName) {
		String xpath = "";
		String[] dois = dodaName.split("\\.");
		for (String doi : dois) {
			xpath += "/*[@name='" + doi + "']";
		}
		return xpath;
	}
	
	public static String getLnType(String prefix, String lnClass,
			String lnInst, Element ldData) {
		String lnXpath = "." + SCL.getLNXPath(prefix, lnClass, lnInst);
		return DOM4JNodeHelper.getAttributeByXPath(ldData, lnXpath + "/@lnType");
	}
	
	public static String getDoName(String lnType, String doDaName, Element datTpl) {
		String parentType = lnType;
		String doName = "";
		String[] dois = doDaName.split("\\.");
		for (int i=0; i<dois.length; i++) {
			String doi = dois[i];
			Element parentDef = DOM4JNodeHelper.selectSingleNode(datTpl, "./*[@id='" + parentType + "']");
			Element typDef = DOM4JNodeHelper.selectSingleNode(parentDef, "./*[@name='" + doi + "']");
			String ndName = typDef.getName();
			if ("DO".equals(ndName) || "SDO".equals(ndName)) {
				if (!"".equals(doName))
					doName += ".";
				doName += doi;
				parentType = typDef.attributeValue("type");
			} else {
				break;
			}
		}
		return doName;
	}

	public static String getFc(String lnType, String doName, String daName, Element datTpl) {
		String doType = DataTypeTemplateDao.getSubType(lnType, doName, datTpl);
		daName = daName.split("\\.")[0];
		return DOM4JNodeHelper.getAttributeValue(datTpl, 
				"./*[@id='" + doType + "']/*[@name='" + daName + "']/@fc");
	}
	
	public static String getDaName(String lnType, String doName, String fc, Element datTpl) {
		List<String> daNames = getDaNames(lnType, doName, fc, datTpl);
		if (daNames.size() > 0)
			return daNames.get(0);
		return null;
	}
	
	public static List<String> getDaNames(String lnType, String doName, String fc, Element datTpl) {
		String parentType = lnType;
		String[] dois = doName.split("\\.");
		for (int i=0; i<dois.length; i++) {
			String doi = dois[i];
			Element parentDef = DOM4JNodeHelper.selectSingleNode(datTpl, "./*[@id='" + parentType + "']");
			Element typDef = DOM4JNodeHelper.selectSingleNode(parentDef, "./*[@name='" + doi + "']");
			parentType = typDef.attributeValue("type");
		}
		return findDaNames(parentType, fc, datTpl);
	}
	
	private static List<String> findDaNames(String doType, String fc, Element datTpl) {
		Element doDef = DOM4JNodeHelper.selectSingleNode(datTpl, "./*[@id='" + doType + "']");
		List<Element> das = DOM4JNodeHelper.selectNodes(doDef, "./*[@fc='" + fc + "']");
		List<String> daNames = new ArrayList<String>();
		for (Element da : das) {
			getDas(da, "", daNames, datTpl);
		}
		return daNames;
	}
	
	private static void getDas(Element daNd, String doName, List<String> daNames, Element datTpl) {
		String daName = daNd.attributeValue("name");
		if (SCL.isExcludeDa(daName))
			return;
		String datype = daNd.attributeValue("type");
		String btype = daNd.attributeValue("bType");
		if ("Struct".equals(btype)) {
			Element datypeNode = DOM4JNodeHelper.selectSingleNode(datTpl, "./*[@id='" + datype + "']");
			List<?> elements = datypeNode.elements();
			for (Object subda : elements) {
				getDas((Element) subda, doName + ("".equals(doName)?"":".") + daName, daNames, datTpl);
			}
		} else {
			daNames.add(doName + ("".equals(doName)?"":".") + daName);
		}
	}
	
	public static String getDoDesc(String doXpath, Element ldData) {
		Element el = DOM4JNodeHelper.selectSingleNode(ldData, doXpath + "/*[name()='DAI'][@name='dU']//*[name()='Val']");
		String desc = (el == null) ? null : el.getText();
		if (StringUtil.isEmpty(desc))
			desc = DOM4JNodeHelper.getAttributeByXPath(ldData, doXpath + "/@desc");
		return desc;
	}
	
	public static String[] getInnerAddr(String doXpath, String daName, Element ldData) {
		return getInnerAddr(doXpath, daName, ldData, true);
	}
	
	//获取 q 的 sAddr
	public static String[] getInnerAddrQ (String doXpath, String daName, Element ldData) {
		return getInnerAddr(doXpath, daName, ldData, false);
	}
	
	private static String[] getInnerAddr(String doXpath, String daName, Element ldData, boolean filter) {
		String desc = getDoDesc(doXpath, ldData);
		if (isQT(daName) && filter) {
			return new String[] {"", daName, desc};
		}
		
		if (StringUtil.isEmpty(daName)) {
			Element doEl = DOM4JNodeHelper.selectSingleNode(ldData, doXpath);
			Element daEl = (doEl == null) ? null : DOM4JNodeHelper.selectSingleNode(doEl, ".//*[@sAddr]");
			if (daEl == null)
				return new String[] {"", "", desc};
			String sAddrDaName = daEl.attributeValue("name");
			Element parent = daEl.getParent();
			while (parent != doEl) {
				sAddrDaName = parent.attributeValue("name") + "." + sAddrDaName;
				parent = parent.getParent();
			}
			String saddr = daEl.attributeValue("sAddr");
			return new String[] {saddr, sAddrDaName, desc};
		} else {
			String daXpath = doXpath;
			String[] das = daName.split("\\.");
			for (String da : das) {
				daXpath += "/*[@name='" + da + "']";
			}
			String saddr = DOM4JNodeHelper.getAttributeByXPath(ldData, daXpath + "/@sAddr");
			return new String[] {saddr, daName, desc};
		}
	}
	
	public static String getExtRefXPath(String extied, Element fcda) {
		String prefix = fcda.attributeValue("prefix");
		String lnClass = fcda.attributeValue("lnClass");
		String inst = fcda.attributeValue("lnInst");
		String xpath = "./scl:ExtRef[@iedName='" + extied + "'][@ldInst='" + fcda.attributeValue("ldInst") + "']"
				+ SCL.getLNAtts(prefix, lnClass, inst) + "[@doName='" + fcda.attributeValue("doName") + "']";
		String daName = fcda.attributeValue("daName");
		if (!StringUtil.isEmpty(daName)) {
			xpath += "[@daName='" + daName + "']";
		}
		return xpath;
	}

	public static String getDaName(Element doEl, Element daEl) {
		if (daEl == null)
			return "";
		String daName = daEl.attributeValue("name");
		Element parentEl = daEl.getParent();
		while(parentEl != null && parentEl != doEl) {
			daName = parentEl.attributeValue("name") + ITagSG.REF_SEP + daName;
			parentEl = parentEl.getParent();
		}
		return daName;
	}
	
	public static String getBoardId(String boardId) {
		return (boardId.length() == 1 ? "0" : "") + boardId;
	}
	
//	public static String getCommAddr(String boardId, CommStateInfo commState) {
//		String type = commState.isSv() ? ".sv" : ".goose";
//		return "B" + boardId + type + ".rx_link_all[" + commState.getNet() + "][" + commState.getCb() + "]";
//	}
//	
//	public static String getCommAddr(String boardId, int net, int cb) {
//		return "B" + boardId + ".goose" + ".rx_link_all[" + net + "][" + cb + "]";
//	}
	
	public static boolean isCommstate(String dsId) {
		return dsId.indexOf("dsCommState") > -1;
	}
}
