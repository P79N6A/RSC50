/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.check;

import static com.shrcn.business.scl.model.SCL.EXCLUDED_DOS;

import java.util.Arrays;
import java.util.StringTokenizer;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-8-5
 */

public class SignalsChecker {
	
	public static final String T = SCL.ATT_T;
	public static final String Q = SCL.ATT_Q;
	
	/**
	 * 根据DO名称判断当前DO是否为无效DO。根据IED实现原理，部分DO是不可以
	 * 用来做信号关联的。
	 * @param doName
	 * @return
	 */
	public static boolean isExcludedDO(String doName) {
		return Arrays.binarySearch(EXCLUDED_DOS, doName) > -1;
	}
	
	/**
	 * 判断当前关联信号是否匹配（只针对是否同为t，或同为q）
	 * @param da1 
	 * @param da2 
	 * @return
	 */
	public static boolean isMatchingDA(String da1, String da2) {
		if(null == da1 || null == da2
				|| "".equals(da1) || "".equals(da2))
			return true;
		if(da1.indexOf('.')>0)
			da1 = da1.substring(da1.lastIndexOf('.') + 1);
		if(da2.indexOf('.')>0)
			da2 = da2.substring(da2.lastIndexOf('.') + 1);
		
		if(!da1.equals(T) && !da1.equals(Q) &&
				!da2.equals(T) && !da2.equals(Q))
			return true;
		if((da1.equals(T) && da2.equals(T)) ||
				(da1.equals(Q) && da2.equals(Q)))
			return true;
		return false;
	}
	
	/**
	 * 根据相关属性查询FC类型
	 * @param iedName 装置名
	 * @param ldInst LD inst
	 * @param prefix LN前缀
	 * @param lnClass LN类型
	 * @param lnInst LN inst
	 * @param do_da DO、DA串，格式为DO.[SDO].DA
	 * @return
	 */
	public static String getFCType(String iedName, String ldInst, String prefix,
			String lnClass, String lnInst, String do_da) {
		String ldPath = "/scl:SCL/scl:IED[@name='" + iedName +
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		String lnPath = ldPath + SCL.getLNXPath(prefix, lnClass, lnInst);
		String lnType = XMLDBHelper.getAttributeValue(lnPath + "/@lnType");
		StringTokenizer token = new StringTokenizer(do_da, ".");
		if(!token.hasMoreElements())
			return null;
		String doN = token.nextToken();
		String doID = XMLDBHelper.getAttributeValue("/scl:SCL/scl:DataTypeTemplates" +
				"/scl:LNodeType[@id='" + lnType + "']/scl:DO[@name='" + doN + "']/@type");
		String fc = null;
		String previous = null;
		while(token.hasMoreTokens()) {
			previous = doID;
			doN = token.nextToken();
			if(token.hasMoreTokens()) {
				//处理SDO
				doID = XMLDBHelper.getAttributeValue("/scl:SCL/scl:DataTypeTemplates" +
						"/scl:DOType[@id='" + doID + "']/scl:SDO[@name='" + doN + "']/@type");
				//处理Struct属性
				if(StringUtil.isEmpty(doID)) {
					fc = XMLDBHelper.getAttributeValue("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id='" + previous + "']" +
						"/scl:DA[@name='" + doN + "']/@fc");
					if(null != fc) return fc;
				}
			} else {
				String daN = doN;
				fc = XMLDBHelper.getAttributeValue("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id='" + doID + "']" +
						"/scl:DA[@name='" + daN + "']/@fc");
			}
		}
		return fc;
	}
	
	/**
	 * 查找指定DO下是否存在目标fc类型的DA
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param doName
	 * @param fc
	 * @return
	 */
	public static boolean findSameFCDa(String iedName, String ldInst, String prefix,
			String lnClass, String lnInst, String doName, String fc) {
		String ldPath = "/scl:SCL/scl:IED[@name='" + iedName +
				"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
		String lnPath = ldPath + SCL.getLNXPath(prefix, lnClass, lnInst);
		String lnType = XMLDBHelper.getAttributeValue(lnPath + "/@lnType");
		String doID = XMLDBHelper.getAttributeValue("/scl:SCL/scl:DataTypeTemplates" +
				"/scl:LNodeType[@id='" + lnType + "']/scl:DO[@name='" + doName + "']/@type");
		return XMLDBHelper.existsNode("/scl:SCL/scl:DataTypeTemplates/scl:DOType[@id='" + doID + "']" +
				"/scl:DA[@fc='" + fc + "']");
	}
}
