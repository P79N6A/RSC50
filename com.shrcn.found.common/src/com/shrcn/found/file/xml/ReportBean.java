/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.xml;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-26
 */
/*
 * 修改历史
 * $Log: ReportBean.java,v $
 * Revision 1.1  2013/03/29 09:38:02  cchun
 * Add:创建
 *
 * Revision 1.1  2009/05/27 07:37:00  cchun
 * Update:添加schema校验功能
 *
 */
public class ReportBean {
	
	private StringBuffer message;
	private int count = 0;
	
	public ReportBean() {
		this.message = new StringBuffer();
	}
	
	public void addMessage(String msg) {
		message.append(msg);
		count++;
	}

	public StringBuffer getMessage() {
		return message;
	}

	public int getCount() {
		return count;
	}
}
