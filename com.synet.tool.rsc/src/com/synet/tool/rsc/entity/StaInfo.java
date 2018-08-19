package com.synet.tool.rsc.entity;

/**
 * 
 * 监控信息点表
 *
 */
public class StaInfo {
	
	private String devName;		//装置Name
	private String devDesc;		//装置名称
	private String boardCode;	//板卡编号
	private String portCode;	//端口编号
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevDesc() {
		return devDesc;
	}
	public void setDevDesc(String devDesc) {
		this.devDesc = devDesc;
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

}
