package com.synet.tool.rsc.entity;

/**
 * 
 * 光强与端口
 *
 */
public class PortLight {
	
	private String devName;			//装置Name
	private String opticalRefAddr;	//光强信息参引
	private String opticalDedc;		//光强信息描述
	private String boardCode;		//板卡编号
	private String portCode;		//端口编号
	private boolean matched;		//是否匹配
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getOpticalRefAddr() {
		return opticalRefAddr;
	}
	public void setOpticalRefAddr(String opticalRefAddr) {
		this.opticalRefAddr = opticalRefAddr;
	}
	public String getOpticalDedc() {
		return opticalDedc;
	}
	public void setOpticalDedc(String opticalDedc) {
		this.opticalDedc = opticalDedc;
	}
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public String getPortCode() {
		return portCode;
	}
	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}
