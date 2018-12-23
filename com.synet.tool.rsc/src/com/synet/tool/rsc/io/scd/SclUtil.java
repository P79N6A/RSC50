/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.io.scd;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.synet.tool.rsc.DBConstants;

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
		if (!StringUtil.isEmpty(fc)) {
			sb.append("$" + fc);
		}
		if (!StringUtil.isEmpty(doName)) {
			doName = doName.replace('.', '$');
			sb.append("$" + doName);
		}
		if (!StringUtil.isEmpty(daName)) {
			daName = daName.replace('.', '$');
			sb.append("$" + daName);
		}
		return sb.toString();
	}
	
	/**
	 * 将intAddr格式参引改成mms格式
	 * @param intAddr
	 * @param fc
	 * @return
	 */
	public static String getFcdaRef(String intAddr, String fc) {
		String[] temp = intAddr.split("\\.");
		String ref = temp[0] + "$" + fc;
		for (int i=1; i<temp.length; i++) {
			ref += "$" + temp[i];
		}
		return ref;
	}

	public static String getDoRef(Element fcdaEl, String daName) {
		String ref = fcdaEl.attributeValue("ref");
		boolean empty = StringUtil.isEmpty(daName);
		int index = ref.lastIndexOf(daName);
		return (empty || index < 1) ? ref : ref.substring(0, index - 1);
	}
	
	public static String getFC(String f1058RefAddr) {
		char tag = ' ';
		if(f1058RefAddr.contains("$")) {
			tag = '$';
		} 
		String temp = f1058RefAddr.substring(f1058RefAddr.indexOf(tag) + 1, f1058RefAddr.length());
		int p = temp.indexOf(tag);
		return p>0 ? temp.substring(0, p) : null;
	}
	
	public static String getDoName(String f1058RefAddr) {
		char tag = '.';
		if(f1058RefAddr.contains("$")) {
			tag = '$';
		} 
		String temp = f1058RefAddr.substring(f1058RefAddr.indexOf(tag) + 1, f1058RefAddr.length());
		String doName = "";
		if ('$' == tag) {
			doName = temp.substring(temp.indexOf(tag) + 1, temp.length());
			int p = doName.indexOf(tag);
			if (p > 0) {
				doName = doName.substring(0, p);
			}
		} else {
			if (temp.indexOf(tag) > -1) {
				doName = temp.substring(0, temp.indexOf(tag));
			} else {
				doName = temp;
			}
		}
		return doName;
	}

	public static String getLnName(String f1058RefAddr) {
		char tag = '.';
		if(f1058RefAddr.contains("$")) {
			tag = '$';
		} 
		String temp = f1058RefAddr.substring(0, f1058RefAddr.indexOf(tag));
//		return getLnClass(temp);
		return temp.split("/")[1];
	}
	
	public static String getLnClass(String lnName) {
		String temp = lnName.replaceAll("\\d+","");
		return temp.substring(temp.length() - 4);
	}
	
	public static boolean isParam(String datSet) {
		return datSet.startsWith("dsParameter");
	}
	
	public static boolean isSetting(String datSet) {
		return datSet.startsWith("dsSetting");
	}
	
	public static boolean isWarning(String datSet) {
		return datSet.startsWith("dsWarning");
	}

	public static boolean isStrap(String datSet) {
		return isType(datSet, "DS_STRAP");
	}

	public static boolean isAIN(String datSet) {
		return isType(datSet, "DS_AIN");
	}

	public static boolean isDIN(String datSet) {
		return isType(datSet, "DS_DIN");
	}

	public static boolean isState(String datSet) {
		return datSet.startsWith("dsCommState");
	}

	private static boolean isType(String datSet, String dictType) {
		String[] names = DictManager.getInstance().getDictNames(dictType);
		for (String name : names) {
			if (datSet.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getFCDADesc(Element elLd, Element fcdaEl) {
		String prefix = fcdaEl.attributeValue("prefix");
		String lnClass = fcdaEl.attributeValue("lnClass");
		String lnInst = fcdaEl.attributeValue("lnInst");
		String doName = fcdaEl.attributeValue("doName");
		return SclUtil.getFCDADesc(elLd, prefix, lnClass, lnInst, doName);
	}
	
	public static String getFCDADesc(Element ld,
			String prefix, String lnClass, String lnInst, String doName) {
		if(null == ld)
			return "";
		String ln = SCL.getLNXPath(prefix, lnClass, lnInst);
		String doXPath = SCL.getDOXPath(doName);
		String desc = DOM4JNodeHelper.getAttributeByXPath(ld, 
					"." + ln + doXPath + "/@desc");
		if (StringUtil.isEmpty(desc)) {
			final Element dUNode = DOM4JNodeHelper.selectSingleNode(ld,
					"." + ln + doXPath + "/*[name()='DAI'][@name='dU']/scl:Val");
			if (dUNode != null) {
				desc = dUNode.getStringValue();
			} else {
				String lnDesc = DOM4JNodeHelper.getAttributeByXPath(ld, "." + ln + "/@desc");
				if (!StringUtil.isEmpty(lnDesc)) {
					desc = lnDesc;
				}
			}
		}
		desc = StringUtil.toXMLChars(desc == null ? "" : desc);
		return desc;
	}
	
	public static boolean isStData(String dataCode) {
		return dataCode.startsWith(DBConstants.PR_State);
	}
	
	public static boolean isAlgData(String dataCode) {
		return dataCode.startsWith(DBConstants.PR_Analog);
	}

	public static boolean isStrapData(String parentCode) {
		return parentCode.startsWith(DBConstants.PR_STRAP);
	}
}
