/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.history;


 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-4
 */
public class DescInfo extends MarkInfo {
	
	private String ref;
	private String desc;
	private String descNew;

	public DescInfo(String ref, String desc, String descNew) {
		super(DevType.IED, OperType.UP_DESC);
		this.ref = ref;
		this.desc = desc;
		this.descNew = descNew;
	}

	@Override
	public String getContent() {
		String content = type.toString() + SEP + getOpType(opType) + SEP + ref + SEP + desc + SEP + descNew;
		return content.trim();
	}

}

