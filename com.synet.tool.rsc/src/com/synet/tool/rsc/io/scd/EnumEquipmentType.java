/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.scd;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-20
 */
public enum EnumEquipmentType {

	CBR(1, "断路器"),
	DIS(2, "刀闸"),
	BUS(3, "母线"),
	PTR(4, "变压器"),
	IFL(5, "线路"),
	CTR(6, "电流互感器"),
	VTR(7, "电压互感器"),
	REA(8, "电抗器"),
	CAP(9, "电容器"),
	GPTR(10, "站用变/接地变"),
	SHORT_L(11, "短引线")
	;
	
	private int code;
	private String desc;
	
	private EnumEquipmentType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static EnumEquipmentType getType(String stype) {
		EnumEquipmentType t = null;
		for (EnumEquipmentType tmp : values()) {
			if (tmp.name().equals(stype)) {
				t = tmp;
				break;
			}
		}
		return t;
	}
}

