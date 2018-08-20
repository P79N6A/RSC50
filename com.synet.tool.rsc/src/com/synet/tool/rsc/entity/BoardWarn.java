package com.synet.tool.rsc.entity;

/**
 * 
 * 告警与板卡关联
 *
 */
public class BoardWarn {
	
	private String devName;			//装置Name
	private String alarmRefAddr;	//告警信息参引
	private String alarmDesc;		//告警信息描述
	private String boardCode;		//板卡编号
	private boolean matched;		//是否匹配
	
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getAlarmRefAddr() {
		return alarmRefAddr;
	}
	public void setAlarmRefAddr(String alarmRefAddr) {
		this.alarmRefAddr = alarmRefAddr;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}
