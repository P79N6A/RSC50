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
 * $Log: TDcaNet.java,v $
 * Revision 1.9  2013/12/20 04:34:21  cxc
 * Update:修改
 *
 * Revision 1.8  2013/12/19 01:02:11  cxc
 * Update:增加heartbeat属性
 *
 * Revision 1.7  2013/09/02 00:56:40  cxc
 * update:修改copy方法
 *
 * Revision 1.6  2013/07/26 00:30:46  cxc
 * Update：修改UdpSC为String类型
 *
 * Revision 1.5  2013/07/23 10:29:27  cxc
 * Update：修改
 *
 * Revision 1.4  2013/07/19 08:56:23  scy
 * Update：更新数据库结构
 *
 * Revision 1.3  2013/07/18 03:04:29  scy
 * Update：增加localAddr、localIP、udpRemotePort、udpLocalPort，删除udpPort、bcPort
 *
 * Revision 1.2  2013/07/17 09:19:07  scy
 * Update：调整103DCA数据结构
 *
 * Revision 1.1  2013/07/16 06:36:21  scy
 * Add：创建
 *
 */
public class TDcaNet extends TDcaChannel {

	private static final long serialVersionUID = -4172012658528570310L;
	
	private int netType;
	private String localAddr;
	private String procType;
	private String ipA;
	private String ipB;
	private String localIP;
	private String tcpSC;
	private String udpSC;
	private String tcpPort;
	private String udpRemotePort;
	private String udpLocalPort;
	private String tmTran;
	private String tmRecv;
	private String heartbeat;
	
	// Modbus
	private String serverIP;
	private int serverPort;

	public TDcaNet() {
	}

	public TDcaNet(int id) {
		setId(id);
	}

	public int getNetType() {
		return netType;
	}

	public void setNetType(int netType) {
		this.netType = netType;
	}

	public String getProcType() {
		return procType;
	}

	public void setProcType(String procType) {
		this.procType = procType;
	}

	public String getIpA() {
		return ipA;
	}

	public void setIpA(String ipA) {
		this.ipA = ipA;
	}

	public String getIpB() {
		return ipB;
	}

	public void setIpB(String ipB) {
		this.ipB = ipB;
	}

	public String getTcpSC() {
		return tcpSC;
	}

	public void setTcpSC(String tcpSC) {
		this.tcpSC = tcpSC;
	}

	public String getUdpSC() {
		return udpSC;
	}

	public void setUdpSC(String udpSC) {
		this.udpSC = udpSC;
	}

	public String getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public String getLocalIP() {
		return localIP;
	}

	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

	public String getUdpRemotePort() {
		return udpRemotePort;
	}

	public void setUdpRemotePort(String udpRemotePort) {
		this.udpRemotePort = udpRemotePort;
	}

	public String getUdpLocalPort() {
		return udpLocalPort;
	}

	public void setUdpLocalPort(String udpLocalPort) {
		this.udpLocalPort = udpLocalPort;
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
		TDcaNet net = new TDcaNet();
		net.setBufferSize(getBufferSize());
		net.setIedNumber(getIedNumber());
		net.setIedOffset(getIedOffset());
		net.setIpA(getIpA());
		net.setIpB(getIpB());
		net.setNetType(getNetType());
		net.setProcType(getProcType());
		net.setTcpPort(getTcpPort());
		net.setTcpSC(getTcpSC());
		net.setTmRecv(getTmRecv());
		net.setTmTran(getTmTran());
		net.setUdpLocalPort(getUdpLocalPort());
		net.setUdpRemotePort(getUdpRemotePort());
		net.setLocalAddr(getLocalAddr());
		net.setLocalIP(getLocalIP());
		net.setUdpSC(getUdpSC());
		net.setDca(getDca());
		net.setIeds(getIeds());
		net.setName(getName());
		net.setRespTimeout(getRespTimeout());
		net.setHeartbeat(getHeartbeat());
		// Modbus
		net.setServerIP(getServerIP());
		net.setServerPort(getServerPort());
		net.setEndian(getEndian());
		net.setRequestIntervel(getRequestIntervel());
		return net;
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
}
