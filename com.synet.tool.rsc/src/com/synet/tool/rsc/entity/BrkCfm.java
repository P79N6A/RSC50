package com.synet.tool.rsc.entity;

/**
 * 
 * 跳合闸反校关联
 *
 */
public class BrkCfm {
	
	private String devName;				//装置Name
	private String pinRefAddr;			//开入虚端子参引
	private String pinDesc;				//开入虚端子描述
	private String cmdAckVpRefAddr;		//命令确认虚端子参引
	private String cmdAckVpDesc;		//命令确认虚端子描述
	private String cmdoutVpRefAddr;		//命令出口虚端子参引
	private String cmdouVptDesc;		//命令出口虚端子描述
	private boolean matched;			//是否匹配
	
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
	public String getCmdAckVpRefAddr() {
		return cmdAckVpRefAddr;
	}
	public void setCmdAckVpRefAddr(String cmdAckVpRefAddr) {
		this.cmdAckVpRefAddr = cmdAckVpRefAddr;
	}
	public String getCmdAckVpDesc() {
		return cmdAckVpDesc;
	}
	public void setCmdAckVpDesc(String cmdAckVpDesc) {
		this.cmdAckVpDesc = cmdAckVpDesc;
	}
	public String getCmdoutVpRefAddr() {
		return cmdoutVpRefAddr;
	}
	public void setCmdoutVpRefAddr(String cmdoutVpRefAddr) {
		this.cmdoutVpRefAddr = cmdoutVpRefAddr;
	}
	public String getCmdouVptDesc() {
		return cmdouVptDesc;
	}
	public void setCmdouVptDesc(String cmdouVptDesc) {
		this.cmdouVptDesc = cmdouVptDesc;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}
