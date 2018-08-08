/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. 
 * This program is an arithmetic for taxis of figure.
 */

package com.shrcn.tool.rtu.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 *
 * @author 孙春颖
 * @version 1.0, 2014-4-9
 */
/**
 * $Log$
 */
public class TDcaiedcdt extends BaseDcaied implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String portName;
	private String baudRate;
	private String dataBit;
	private String stopBit;
	private String parity;
	private String tmTran;
	private String tmRecv;
	private String bufferSize;
	private String remoteAddr;
	private String iedAddr;
	private String localAddr;
	private String cmdSport;
	private String cmdTimeout;
	private String setTime;
	private String frozenTime;

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

	@Override
	public BaseDcaied copy() {
		TDcaiedcdt ied = (TDcaiedcdt) super.copy();
		ied.setPsMx(null);
		ied.setPsSt(null);
		ied.setPsCo(null);
		ied.setPsSp(null);
		ied.setPsSg(null);
		ied.setPsSe(null);
		return ied;
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

	public String getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getIedAddr() {
		return iedAddr;
	}

	public void setIedAddr(String iedAddr) {
		this.iedAddr = iedAddr;
	}

	public String getCmdSport() {
		return cmdSport;
	}

	public void setCmdSport(String cmdSport) {
		this.cmdSport = cmdSport;
	}

	public String getCmdTimeout() {
		return cmdTimeout;
	}

	public void setCmdTimeout(String cmdTimeout) {
		this.cmdTimeout = cmdTimeout;
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

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public String getSetTime() {
		return setTime;
	}

	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}

	public String getFrozenTime() {
		return frozenTime;
	}

	public void setFrozenTime(String frozenTime) {
		this.frozenTime = frozenTime;
	}

}
