/**
 * Copyright (c) 2007-2010 chenchun.
 * All rights reserved. This program is an application based on tcp/ip.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2012-2-9
 */
/**
 * $Log: TLocalip.java,v $
 * Revision 1.2  2012/11/14 11:49:57  cchun
 * Update:修改localPort字段类型
 *
 * Revision 1.1  2012/10/25 11:25:47  cchun
 * Refactor:方便hibernate初始化，移动位置
 *
 * Revision 1.2  2012/10/22 07:43:27  cchun
 * Update:根据应用修改实体Bean
 *
 * Revision 1.1  2012/10/17 08:09:22  cchun
 * Add:映射实体类
 *
 */
public class TLocalip extends RowData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String localaddress;
	private String localport;
	private String localport_W;
	private String localport_G;
	private String standbycloseport;
	
	private TApplication app;
	private TMasterApp masterapp;
	public TLocalip() {
	}
	
	
	
	public TLocalip(String localport_W, String localport_G) {
		super();
		this.localport_W = localport_W;
		this.localport_G = localport_G;
	}



	public TLocalip(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getLocaladdress() {
		return localaddress;
	}
	public void setLocaladdress(String localaddress) {
		this.localaddress = localaddress;
	}
	public String getStandbycloseport() {
		return standbycloseport;
	}
	public void setStandbycloseport(String standbycloseport) {
		this.standbycloseport = standbycloseport;
	}

	public String getLocalport() {
		return localport;
	}

	public void setLocalport(String localport) {
		this.localport = localport;
	}

	public TApplication getApp() {
		return app;
	}

	public void setApp(TApplication app) {
		this.app = app;
	}

	public TMasterApp getMasterapp() {
		return masterapp;
	}

	public void setMasterapp(TMasterApp masterapp) {
		this.masterapp = masterapp;
	}

	public String getLocalport_W() {
		return localport_W;
	}

	public void setLocalport_W(String localport_W) {
		this.localport_W = localport_W;
	}

	public String getLocalport_G() {
		return localport_G;
	}

	public void setLocalport_G(String localport_G) {
		this.localport_G = localport_G;
	}
	
	
}
