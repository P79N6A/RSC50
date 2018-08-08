/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.tool.rtu.model;

/**
 * 增加福建类型定值区设置对象
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2014-5-5
 */
/**
 * $Log$
 */
public class TFjsppoint extends BaseDpaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String aetype;
	private String ontime;
	private String offtime;
	private String counttimes;
	private String fun;
	private String inf;
	private int deviceid;
	private String threshold;
	
	public String getFun() {
		return fun;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public String getInf() {
		return inf;
	}

	public void setInf(String inf) {
		this.inf = inf;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	@Override
	public void setInfoType(int infoType) {
		
	}

	@Override
	public void setPointNum(int pointNum) {

	}

	public String getAetype() {
		return aetype;
	}

	public void setAetype(String aetype) {
		this.aetype = aetype;
	}

	public int getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}

	public String getOntime() {
		return ontime;
	}

	public void setOntime(String ontime) {
		this.ontime = ontime;
	}

	public String getOfftime() {
		return offtime;
	}

	public void setOfftime(String offtime) {
		this.offtime = offtime;
	}

	public String getCounttimes() {
		return counttimes;
	}

	public void setCounttimes(String counttimes) {
		this.counttimes = counttimes;
	}

}
