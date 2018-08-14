/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.outtree;

import com.shrcn.business.scl.model.OutSingalFCDA;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-8
 */
/*
 * 修改历史
 * $Log: FCDAEntry.java,v $
 * Revision 1.1  2013/03/29 09:38:07  cchun
 * Add:创建
 *
 * Revision 1.3  2012/09/03 07:07:47  cchun
 * Update:为FCDAEntry增加序号属性
 *
 * Revision 1.2  2009/05/12 06:09:14  cchun
 * Update:添加节点描述
 *
 * Revision 1.1  2009/05/08 12:07:17  cchun
 * Update:完善外部、内部信号视图
 *
 */
public class FCDAEntry extends OutTreeEntry {

	private OutSingalFCDA fcda = null;
	//外部信号数据集中的FCDA序号，为排序时所用
	private int num;
	private boolean haveSglRef;
	
	public FCDAEntry(String name, String iconName, String toolTip, int entryType, 
			OutSingalFCDA fcda, int num) {
		super(name, iconName, toolTip, entryType);
		this.fcda = fcda;
		this.num = num;
	}

	public OutSingalFCDA getFcda() {
		return fcda;
	}

	public void setFcda(OutSingalFCDA fcda) {
		this.fcda = fcda;
	}

	@Override
	public String getDesc() {
		return fcda.getDesc();
	}

	@Override
	public void setDesc(String desc) {
		// TODO Auto-generated method stub
		
	}

	public int getNum() {
		return num;
	}

	@Override
	public boolean isHaveSglRef() {
		return haveSglRef;
	}

	@Override
	public void setHaveSglRef(boolean haveSglRef) {
		this.haveSglRef = haveSglRef;
	}

}
