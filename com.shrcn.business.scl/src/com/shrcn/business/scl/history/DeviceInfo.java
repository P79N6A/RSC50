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
 * $Log: DeviceInfo.java,v $
 * Revision 1.1  2013/03/29 09:38:25  cchun
 * Add:创建
 *
 * Revision 1.3  2010/04/23 03:22:25  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.2  2009/12/09 08:21:49  cchun
 * Update:将int改成enum
 *
 * Revision 1.1  2009/12/09 07:16:12  cchun
 * Add:添加历史记录工具类
 *
 */
public class DeviceInfo extends MarkInfo {

	private String name;		// 名称
	private String newName;		// 新名称
	private String oid;			// oid
	
	/**
	 * 构造方法
	 * @param type 对象类型
	 * @param opType 操作类型Add,Delete,Update,Rename
	 * @param name 对象名
	 */
	public DeviceInfo(DevType type, OperType opType, String name, String oid) {
		super(type, opType);
		this.name = name;
		this.oid = oid;
	}
	
	/**
	 * 构造方法
	 * @param type 对象类型
	 * @param opType 操作类型
	 * @param newName 新名称
	 * @param name 对象名
	 */
	public DeviceInfo(DevType type, OperType opType, String name, String newName, String oid) {
		this(type, opType, name, oid);
		this.newName = newName;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@Override
	public String getContent() {
		String content = "";
		if(null == newName)
			content = type.toString() + SEP + getOpType(opType) + SEP + name;
		else
			content = type.toString() + SEP + getOpType(OperType.RENAME) + SEP + name + SEP + newName;
		return content.trim();
	}
}
