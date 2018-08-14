/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-22
 */
/*
 * 修改历史
 * $Log: EmReportAttribute.java,v $
 * Revision 1.1  2013/03/29 09:36:39  cchun
 * Add:创建
 *
 * Revision 1.3  2012/01/17 08:50:27  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.2  2009/04/23 07:38:42  cchun
 * Update:补充属性
 *
 * Revision 1.1  2009/04/22 12:20:20  cchun
 * Update:添加报告属性保存方法
 *
 */
public enum EmReportAttribute {
	
	//base attributes
	NAME("name", ""),
	DESC("desc", ""),
	DATSET("datSet", ""),
	INTGPD("intgPd", ""),
	RPTID("rptID", ""),
	CONFREV("confRev", ""),
	BUFFERED("buffered", ""),
	BUFTIME("bufTime", ""),
	//TrgOps attributes
	TRGOPS_DCHG("dchg", "/*[name()='TrgOps']"),
	TRGOPS_QCHG("qchg", "/*[name()='TrgOps']"),
	TRGOPS_DUPD("dupd", "/*[name()='TrgOps']"),
	TRGOPS_PERIOD("period", "/*[name()='TrgOps']"),
	//OptFields attributes
	OPTFIELDS_DATAREF("dataRef", "/*[name()='OptFields']"),
	OPTFIELDS_REASONCODE("reasonCode", "/*[name()='OptFields']"),
	OPTFIELDS_CONFIGREF("configRef", "/*[name()='OptFields']"),
	OPTFIELDS_DATASET("dataSet", "/*[name()='OptFields']"),
	OPTFIELDS_SEQNUM("seqNum", "/*[name()='OptFields']"),
	OPTFIELDS_ENTRYID("entryID", "/*[name()='OptFields']"),
	OPTFIELDS_TIMESTAMP("timeStamp", "/*[name()='OptFields']"),
	OPTFIELDS_BUFOVFL("bufOvfl", "/*[name()='OptFields']"),
	OPTFIELDS_SEGMENTATION("segmentation", "/*[name()='OptFields']"),
	//RptEnabled attributes
	RPTENABLED_MAX("max", "/*[name()='RptEnabled']"),
	RPTENABLED_DESC("desc", "/*[name()='RptEnabled']");
	
	private final String attName;
	private final String subPath;
	
	private EmReportAttribute(String attName, String subPath) {
		this.attName = attName;
		this.subPath = subPath;
	}
	
	public String getAttName() {
		return this.attName;
	}

	public String getSubPath() {
		return subPath;
	}
}