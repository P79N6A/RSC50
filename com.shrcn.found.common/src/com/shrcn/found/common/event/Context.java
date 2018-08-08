/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.event;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
/**
 * $Log: Context.java,v $
 * Revision 1.1  2013/04/06 11:58:34  cchun
 * Update:完成界面事件框架
 *
 */
public class Context {
	private String eventName;
	private Object source;
	private Object data;

	public Context(String eventName, Object source, Object data) {
		super();
		this.eventName = eventName;
		this.source = source;
		this.data = data;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
