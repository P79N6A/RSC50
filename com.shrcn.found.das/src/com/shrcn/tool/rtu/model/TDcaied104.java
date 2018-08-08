/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;
/**
 * 
 * @author 曹兴春(mailto:cxc14375@shrcn.com)
 * @version 1.0, 2013-9-17
 */
/**
 * $Log: TDcaied104.java,v $
 * Revision 1.4  2013/12/10 00:44:05  cxc
 * update:增加按照点号排序
 *
 * Revision 1.2  2013/11/04 01:54:18  cxc
 * update:添加判断是否为单点遥控属性
 *
 * Revision 1.1  2013/10/08 06:32:57  cxc
 * Add:新建
 *
 */
public class TDcaied104 extends BaseDcaied implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ipA;
	private String ipB;
	private String iedAddr;
	private String tcpPort;
	private String totalTime;
	private String timeInterval;
	private String asduAddress;
	private String w_value;
	private String confirmTime;
	private String stime;
	private String idleTime;
	private String infoType13;
	private String cmdAckTimeout;
	private String ciScanInterval;
	private int diNumber;
	private int doNumber;
	private int aiNumber;
	private int aoNumber;
	private int ciNumber;
	private int diOffset;
	private int doOffset;
	private int aiOffset;
	private int aoOffset;
	private int ciOffset;

	private Set<TDcaMx> psMx = new HashSet<TDcaMx>();
	private Set<TDcaSt> psSt = new HashSet<TDcaSt>();
	private Set<TDcaCo> psCo = new HashSet<TDcaCo>();
	private Set<TDcaSp> psSp = new HashSet<TDcaSp>();
	private Set<TDcaSg> psSg = new HashSet<TDcaSg>();
	private Set<TDcaSe> psSe = new HashSet<TDcaSe>();

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

	public String getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getAsduAddress() {
		return asduAddress;
	}

	public void setAsduAddress(String asduAddress) {
		this.asduAddress = asduAddress;
	}

	public String getW_value() {
		return w_value;
	}

	public void setW_value(String w_value) {
		this.w_value = w_value;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(String idleTime) {
		this.idleTime = idleTime;
	}

	public String getInfoType13() {
		return infoType13;
	}

	public void setInfoType13(String infoType13) {
		this.infoType13 = infoType13;
	}

	public String getCmdAckTimeout() {
		return cmdAckTimeout;
	}

	public void setCmdAckTimeout(String cmdAckTimeout) {
		this.cmdAckTimeout = cmdAckTimeout;
	}

	public String getCiScanInterval() {
		return ciScanInterval;
	}

	public void setCiScanInterval(String ciScanInterval) {
		this.ciScanInterval = ciScanInterval;
	}

	public int getDiNumber() {
		return diNumber;
	}

	public void setDiNumber(int diNumber) {
		this.diNumber = diNumber;
	}

	public int getDoNumber() {
		return doNumber;
	}

	public void setDoNumber(int doNumber) {
		this.doNumber = doNumber;
	}

	public int getAiNumber() {
		return aiNumber;
	}

	public void setAiNumber(int aiNumber) {
		this.aiNumber = aiNumber;
	}

	public int getAoNumber() {
		return aoNumber;
	}

	public void setAoNumber(int aoNumber) {
		this.aoNumber = aoNumber;
	}

	public int getCiNumber() {
		return ciNumber;
	}

	public void setCiNumber(int ciNumber) {
		this.ciNumber = ciNumber;
	}

	public int getDiOffset() {
		return diOffset;
	}

	public void setDiOffset(int diOffset) {
		this.diOffset = diOffset;
	}

	public int getDoOffset() {
		return doOffset;
	}

	public void setDoOffset(int doOffset) {
		this.doOffset = doOffset;
	}

	public int getAiOffset() {
		return aiOffset;
	}

	public void setAiOffset(int aiOffset) {
		this.aiOffset = aiOffset;
	}

	public int getAoOffset() {
		return aoOffset;
	}

	public void setAoOffset(int aoOffset) {
		this.aoOffset = aoOffset;
	}

	public int getCiOffset() {
		return ciOffset;
	}

	public void setCiOffset(int ciOffset) {
		this.ciOffset = ciOffset;
	}

	@Override
	public BaseDcaied copy() {
		TDcaied104 ied = (TDcaied104) super.copy();
		ied.setPsMx(null);
		ied.setPsSt(null);
		ied.setPsCo(null);
		ied.setPsSp(null);
		ied.setPsSg(null);
		ied.setPsSe(null);
		return ied;
	}

	public Set<TDcaMx> getPsMx() {
		return psMx;
	}

	public void setPsMx(Set<TDcaMx> psMx) {
		this.psMx = psMx;
	}

	public Set<TDcaSt> getPsSt() {
		return psSt;
	}

	public void setPsSt(Set<TDcaSt> psSt) {
		this.psSt = psSt;
	}

	public Set<TDcaCo> getPsCo() {
		return psCo;
	}

	public void setPsCo(Set<TDcaCo> psCo) {
		this.psCo = psCo;
	}

	public Set<TDcaSp> getPsSp() {
		return psSp;
	}

	public void setPsSp(Set<TDcaSp> psSp) {
		this.psSp = psSp;
	}

	public Set<TDcaSg> getPsSg() {
		return psSg;
	}

	public void setPsSg(Set<TDcaSg> psSg) {
		this.psSg = psSg;
	}

	public Set<TDcaSe> getPsSe() {
		return psSe;
	}

	public void setPsSe(Set<TDcaSe> psSe) {
		this.psSe = psSe;
	}
	
	public String getIedAddr() {
		return iedAddr;
	}

	public void setIedAddr(String iedAddr) {
		this.iedAddr = iedAddr;
	}
}
