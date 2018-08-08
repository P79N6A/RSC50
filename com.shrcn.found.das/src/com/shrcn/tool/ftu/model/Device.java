/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.ftu.model;

 /**
 * 
 * @author 李蒙龙(mailto:202137/siyuan.com)
 * @version 1.0, 2017-5-22
 */
public class Device {
	
	private int id;
	private String station;
	private String devname;
	private int synType;
	
	public Device() {
		super();
	}

	public Device(int id) {
		super();
		this.id = id;
	}
	
	public Device(String station, String devname, int synType) {
		super();
		this.station = station;
		this.devname = devname;
		this.synType = synType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getDevname() {
		return devname;
	}

	public void setDevname(String devname) {
		this.devname = devname;
	}

	public int getSynType() {
		return synType;
	}

	public void setSynType(int synType) {
		this.synType = synType;
	}
}

