/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.scd;

 /**
* 0-Din：遥信
 * 1-Ain：遥测
 * 2-Alarm：装置故障
 * 3-Warning：装置告警
 * 4-Trip：保护动作
 * 5-Enable：压板
 * 6-其他（类型枚举值可继续补充）
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-16
 */
public enum EnumRcbType {
	Din(0, "Din", "遥信"), 
	Soe(0, "Soe", "遥信"), 
	Ain(1, "Ain", "遥测"), 
	Alarm(2, "Alarm", "装置故障"), 
	Alm(2, "Alm", "装置故障"), 
	Warning(3, "Warning", "装置告警"), 
	Trip(4, "Trip", "保护动作"), 
	Enable(5, "Enable", "压板"), 
	Ena(5, "Ena", "压板"), 
	CommState(6, "CommState", "通信工况"), 
	Unknow(10, "Unknow", "未知");
	
	private int cbtype;
	private String suffix;
	private String desc;
	
	private EnumRcbType(int cbtype, String suffix, String desc) {
		this.cbtype = cbtype;
		this.suffix = suffix;
		this.desc = desc;
	}

	public int getCbtype() {
		return cbtype;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getDesc() {
		return desc;
	}
	
}

