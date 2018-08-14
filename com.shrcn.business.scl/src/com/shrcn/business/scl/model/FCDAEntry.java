/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

import static com.shrcn.business.scl.model.SCL.FCDA_DANAME;
import static com.shrcn.business.scl.model.SCL.FCDA_DONAME;
import static com.shrcn.business.scl.model.SCL.FCDA_FC;
import static com.shrcn.business.scl.model.SCL.FCDA_LDINST;
import static com.shrcn.business.scl.model.SCL.FCDA_LNCLASS;
import static com.shrcn.business.scl.model.SCL.FCDA_LNINST;
import static com.shrcn.business.scl.model.SCL.FCDA_PREFIX;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * 
 * @author 普洪涛(mailto:pht@shrcn.com)
 * @version 1.0, 2009-4-13
 */
/*
 * 修改历史 $Log: FCDAEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:36  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.3  2011/08/11 05:47:16  cchun
 * 修改历史 Update:整理格式
 * 修改历史
 * 修改历史 Revision 1.2  2010/09/26 08:28:51  cchun
 * 修改历史 Update:实现比较接口
 * 修改历史 Revision 1.1 2009/05/18 05:53:10 cchun
 * Refactor:移动位置
 * 
 * Revision 1.2 2009/04/27 05:47:09 pht Add:private String prefix; private
 * String lnClass; private String lnInst; private String doName; private String
 * ldInst;
 * 
 * Revision 1.1 2009/04/23 08:29:36 cchun Refactor:转移类位置
 * 
 * Revision 1.1 2009/04/16 02:02:56 pht 数据集对象
 * 
 */
public class FCDAEntry implements Comparable<Object> {

	private String lNDoName; 	// 数据引用名的格式是：LDevice/LN$DO
	private String daName; 		// daName：对应FCDA中的daName属性
	private String desc; 		// desc：对应于DOI的desc属性
	private String du; 			// du：对应于DOI的名为dU的DAI值
	private String sAddr; 		// 如果FCDA中的do有相应的短地址对应，
	private String prefix;
	private String lnClass;
	private String lnInst;
	private String doName;
	private String ldInst;
	private String iedName;
	private String fc;

	public FCDAEntry() {
	}

	/**
	 * XML元素转FCDA对象
	 * @param fcdaEle
	 */
	public FCDAEntry(Element fcdaEle) {
		ldInst = getValue(fcdaEle, FCDA_LDINST);
		prefix = getValue(fcdaEle, FCDA_PREFIX);
		lnClass = getValue(fcdaEle, FCDA_LNCLASS);
		lnInst = getValue(fcdaEle, FCDA_LNINST);
		doName = getValue(fcdaEle, FCDA_DONAME);
		daName = getValue(fcdaEle, FCDA_DANAME);
		fc = getValue(fcdaEle, FCDA_FC);
	}

	private String getValue(Element ele, String attr) {
		return ele.attributeValue(attr);
	}

	public Element toElemnt() {
		Element data = DOM4JNodeHelper.createSCLNode("FCDA");
		data.addAttribute(FCDA_LDINST, ldInst);
		data.addAttribute(FCDA_PREFIX, prefix);
		data.addAttribute(FCDA_LNCLASS, lnClass);
		data.addAttribute(FCDA_LNINST, lnInst);
		data.addAttribute(FCDA_DONAME, doName);
		if (null != daName)
			data.addAttribute(FCDA_DANAME, daName);
		data.addAttribute(FCDA_FC, fc);
		return data;
	}

	public String getLNDoName() {
		return lNDoName;
	}

	public void setLNDoName(String doName) {
		lNDoName = doName;
	}

	public String getDaName() {
		return daName;
	}

	public void setDaName(String daName) {
		this.daName = daName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDu() {
		return du;
	}

	public void setDu(String du) {
		this.du = du;
	}

	public String getSAddr() {
		return sAddr;
	}

	public void setSAddr(String addr) {
		sAddr = addr;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getLnClass() {
		return lnClass;
	}

	public void setLnClass(String lnClass) {
		this.lnClass = lnClass;
	}

	public String getLnInst() {
		return lnInst;
	}

	public void setLnInst(String lnInst) {
		this.lnInst = lnInst;
	}

	public String getDoName() {
		return doName;
	}

	public void setDoName(String doName) {
		this.doName = doName;
	}

	public String getLdInst() {
		return ldInst;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	public String getFc() {
		return fc;
	}

	public void setFc(String fc) {
		this.fc = fc;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object) 比较两FCDA是否相等
	 */
	@Override
	public int compareTo(Object o) {
		if (!(o instanceof FCDAEntry))
			return -1;
		FCDAEntry fcda = (FCDAEntry) o;
		int re = 0;
		re += getCompareValue(fcda.daName, daName);
		re += getCompareValue(fcda.doName, doName);
		re += getCompareValue(fcda.fc, fc);
		re += getCompareValue(fcda.ldInst, ldInst);
		re += getCompareValue(fcda.lnClass, lnClass);
		re += getCompareValue(fcda.fc, fc);
		re += getCompareValue(fcda.prefix, prefix);
		return re;
	}

	/**
	 * 比较两个字符串
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 */
	private int getCompareValue(String value1, String value2) {
		int re = 0;
		if (value1 != null) {
			re += value1.compareTo(value2);
		} else if (value2 != null) {
			re += value2.compareTo(value1);
		}
		return re;
	}

	public String getRef() {
		return ldInst + "/" + StringUtil.nullToEmpty(prefix) + lnClass
				+ StringUtil.nullToEmpty(lnInst) + "$" + doName;
	}

}
