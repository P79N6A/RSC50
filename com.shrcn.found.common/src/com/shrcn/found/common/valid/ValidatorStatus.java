/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.found.common.valid;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-8-12
 */
/**
 * $Log: ValidatorStatus.java,v $
 * Revision 1.1  2013/03/29 09:37:49  cchun
 * Add:创建
 *
 * Revision 1.2  2010/11/03 08:29:01  cchun
 * Refactor:规范代码
 *
 * Revision 1.1  2010/08/17 03:09:57  cchun
 * Update:校验状态类
 *
 */
public class ValidatorStatus {
	
	private String title;
	private String message;

	public ValidatorStatus(String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
