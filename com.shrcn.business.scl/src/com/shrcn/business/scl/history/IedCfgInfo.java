/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.history;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-12-8
 */
/**
 * $Log: ComInfo.java,v $
 * Revision 1.1  2013/03/29 09:38:27  cchun
 * Add:创建
 *
 * Revision 1.2  2009/12/09 08:21:49  cchun
 * Update:将int改成enum
 *
 * Revision 1.1  2009/12/09 07:16:12  cchun
 * Add:添加历史记录工具类
 *
 */
public class IedCfgInfo extends MarkInfo {
	
	private IedCfg cfgType;
	private String cbRef;
	private String attrName;
	private String oldValue;
	private String newValue;
	
	/**
	 * 构造方法
	 * @param opType 操作类型Add,Delete,Update
	 * @param subNet 子网
	 * @param iedName IED
	 * @param apName 访问点
	 */
	public IedCfgInfo(IedCfg cfgType, OperType opType, String cbRef) {
		super(DevType.IED, opType);
		this.cfgType = cfgType;
		this.cbRef = cbRef;
		
		this.attrName = "";
		this.oldValue = "";
		this.newValue = "";
	}
	
	public IedCfgInfo(IedCfg cfgType, String cbRef, 
			String attrName, String oldValue, String newValue) {
		this(cfgType, OperType.UPDATE, cbRef);
		this.attrName = attrName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public String getContent() {
		String content = cfgType.toString() + SEP + getOpType(opType) + 
			SEP + cbRef + SEP + attrName + SEP + oldValue + SEP + newValue;
		return content.trim();
	}
}
