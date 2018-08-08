/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.enums;

/**
 * 数据类型枚举类
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-4-2
 */
/**
 * $Log: EnumDataType.java,v $
 * Revision 1.1  2013/03/29 09:38:04  cchun
 * Add:创建
 *
 * Revision 1.1  2011/05/13 07:01:00  cchun
 * Refactor:修改class所在项目包
 *
 * Revision 1.3  2010/12/06 08:52:20  cchun
 * Update:添加int64类型
 *
 * Revision 1.2  2010/04/16 06:48:09  cchun
 * Update:添加INT128
 *
 * Revision 1.1  2010/04/13 03:24:52  cchun
 * Refactor:移动枚举类到enums包
 *
 * Revision 1.2  2010/04/06 01:34:47  cchun
 * Update
 *
 */
public enum EnumDataType {
	BOOLEAN("1"),
	INT8("2"), Enum("2"), 
	INT16("3"),
	INT32("4"), INT128("4"),
	INT64("5"),
	INT8U("6"), 
	INT16U("7"),
	INT32U("8"),
	INT64U("9"),
	FLOAT32("10"), 
	DOUBLE("11"), 
	Dbpos("12"), Check("12"), Tcmd("12"), Quality("12");
	
	private String id;
	
	EnumDataType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	/**
	 * 根据id得到类型名称
	 * @param id
	 * @return
	 */
	public static String getTypeById(String id) {
		EnumDataType datType = null;
		for(EnumDataType type : values()) {
			if(type.getId().equals(id)) {
				datType = type;
				break;
			}
		}
		return (datType == null)?id:datType.name();
	}
}
