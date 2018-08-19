package com.synet.tool.rsc.entity;


/** 
 * 
 * 装置板卡端口描述
 *
 */
public class IEDBoard {

	private String devName; 	//装置Name
	private String iedDesc;		//装置名称
	private String boardCode;	//板卡编号
	private String portCode;	//端口编号
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getIedDesc() {
		return iedDesc;
	}
	public void setIedDesc(String iedDesc) {
		this.iedDesc = iedDesc;
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
