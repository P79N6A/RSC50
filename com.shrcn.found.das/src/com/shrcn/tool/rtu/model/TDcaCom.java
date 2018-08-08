/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 7 15
 */
/**
 * $Log: TDcaCom.java,v $
 * Revision 1.6  2013/09/02 00:55:13  cxc
 * update:修改copy方法
 *
 * Revision 1.5  2013/07/23 10:29:27  cxc
 * Update：修改
 *
 * Revision 1.4  2013/07/19 08:56:24  scy
 * Update：更新数据库结构
 *
 * Revision 1.3  2013/07/18 03:03:26  scy
 * Update：增加属性localAddr
 *
 * Revision 1.2  2013/07/17 09:19:08  scy
 * Update：调整103DCA数据结构
 *
 * Revision 1.1  2013/07/16 06:36:21  scy
 * Add：创建
 *
 */
public class TDcaCom extends TDcaChannel {

	private static final long serialVersionUID = -5661313755705755019L;
	private String comType;
	private String procType;
	private String localAddr;
	private String portName;
	private String baudRate;
	private String dataBit;
	private String stopBit;
	private String parity;
	private String tmTran;
	private String tmRecv;

	public TDcaCom() {
	}
	
	public TDcaCom(int id) {
		setId(id);
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public String getProcType() {
		return procType;
	}

	public void setProcType(String procType) {
		this.procType = procType;
	}

	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(String baudRate) {
		this.baudRate = baudRate;
	}

	public String getDataBit() {
		return dataBit;
	}

	public void setDataBit(String dataBit) {
		this.dataBit = dataBit;
	}

	public String getStopBit() {
		return stopBit;
	}

	public void setStopBit(String stopBit) {
		this.stopBit = stopBit;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getTmTran() {
		return tmTran;
	}

	public void setTmTran(String tmTran) {
		this.tmTran = tmTran;
	}

	public String getTmRecv() {
		return tmRecv;
	}

	public void setTmRecv(String tmRecv) {
		this.tmRecv = tmRecv;
	}

	public TDcaChannel copy() {
		TDcaCom com = new TDcaCom();
		com.setBaudRate(getBaudRate());
		com.setBufferSize(getBufferSize());
		com.setComType(getComType());
		com.setDataBit(getDataBit());
		com.setIedOffset(getIedOffset());
		com.setParity(getParity());
		com.setPortName(getPortName());
		com.setProcType(getProcType());
		com.setStopBit(getStopBit());
		com.setTmRecv(getTmRecv());
		com.setTmTran(getTmTran());
		com.setIeds(getIeds());
		com.setDca(getDca());
		com.setLocalAddr(getLocalAddr());
		com.setName(getName());
		com.setRespTimeout(getRespTimeout());
		//Modbus
		com.setEndian(getEndian());
		com.setRequestIntervel(getRequestIntervel());
		return com;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

}
