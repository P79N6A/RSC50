/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.enums;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-17
 */
/**
 * $Log: EnumCtrlBlock.java,v $
 * Revision 1.1  2013/03/29 09:36:41  cchun
 * Add:创建
 *
 * Revision 1.3  2011/08/03 09:00:57  cchun
 * Update:增加isCtrlBlock()
 *
 * Revision 1.2  2011/08/01 08:17:51  cchun
 * Update:添加相关控制块配置节点名
 *
 * Revision 1.1  2010/12/20 02:37:31  cchun
 * Add:控制块枚举类
 *
 */
public enum EnumCtrlBlock {
	
	ReportControl("报告控制块", "RPT", "RP"),
	LogControl("日志控制块", "LOG", "LG"),
	GSEControl("GSE控制块", "GSE", "GO"),
	SampledValueControl("采样值控制块", "SMV", "MS"),
	SettingControl("定值控制块", "SET", "SE");
	
	private String desc;
	private String cbName;
	private String fc;
	
	EnumCtrlBlock(String desc, String cbName, String fc) {
		this.desc = desc;
		this.cbName = cbName;
		this.fc = fc;
	}
	
	public String getDesc() {
		return this.desc;
	}

	public String getCbName() {
		return cbName;
	}
	
	public String getFc() {
		return fc;
	}

	public static boolean isCtrlBlock(String desc) {
		boolean is = false;
		for (EnumCtrlBlock cb : values()) {
			if (cb.getDesc().equals(desc)) {
				is = true;
				break;
			}
		}
		return is;
	}
	
	public static EnumCtrlBlock getByCbName(String cbName) {
		if (GSEControl.getCbName().equals(cbName))
			return GSEControl;
		if (SampledValueControl.getCbName().equals(cbName))
			return SampledValueControl;
		return null;
	}
}
