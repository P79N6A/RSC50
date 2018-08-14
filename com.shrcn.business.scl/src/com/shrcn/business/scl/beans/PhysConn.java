/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.beans;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-9-18
 */
public class PhysConn {
	private String port;
	private String plug;
	private String type;
	
	public PhysConn() {
		super();
	}
	
	public PhysConn(String port, String plug, String type) {
		super();
		this.port = port;
		this.plug = plug;
		this.type = type;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPlug() {
		return plug;
	}
	public void setPlug(String plug) {
		this.plug = plug;
	}
}
