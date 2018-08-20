package com.synet.tool.rsc.entity;

/**
 * 
 * 开入信号映射
 *
 */
public class StatusIn {
	
	private String devName;		//装置Name
	private String pinRefAddr;	//开入虚端子参引
	private String pinDesc;		//端子描述
	private String mmsSignal;	//MMS信号参引
	private String signalDesc;	//信号描述
	private boolean matched;	//是否匹配
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getPinRefAddr() {
		return pinRefAddr;
	}
	public void setPinRefAddr(String pinRefAddr) {
		this.pinRefAddr = pinRefAddr;
	}
	public String getPinDesc() {
		return pinDesc;
	}
	public void setPinDesc(String pinDesc) {
		this.pinDesc = pinDesc;
	}
	public String getMmsSignal() {
		return mmsSignal;
	}
	public void setMmsSignal(String mmsSignal) {
		this.mmsSignal = mmsSignal;
	}
	public String getSignalDesc() {
		return signalDesc;
	}
	public void setSignalDesc(String signalDesc) {
		this.signalDesc = signalDesc;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}
