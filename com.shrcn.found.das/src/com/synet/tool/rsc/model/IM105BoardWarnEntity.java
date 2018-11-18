package com.synet.tool.rsc.model;

/**
 * 
 * 告警与板卡关联
 *
 */
public class IM105BoardWarnEntity {
	
	private String im105Code;
	private String devName;			//装置Name
	private String devDesc;			//装置名称
	private String alarmRefAddr;	//告警信息参引
	private String alarmDesc;		//告警信息描述
	private String boardCode;		//板卡编号
	private Integer matched;		//是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict = 2; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm105Code() {
		return im105Code;
	}
	public void setIm105Code(String im105Code) {
		this.im105Code = im105Code;
	}
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
	public Integer getMatched() {
		return matched;
	}
	public void setMatched(Integer matched) {
		this.matched = matched;
	}
	public IM100FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(IM100FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}
	public int getConflict() {
		return conflict;
	}
	public void setConflict(int conflict) {
		this.conflict = conflict;
	}
	public boolean isOverwrite() {
		return overwrite;
	}
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
	@Override
	public String toString() {
		return "devName=" + devName + ", devDesc="
				+ devDesc + ", alarmRefAddr=" + alarmRefAddr + ", alarmDesc="
				+ alarmDesc + ", boardCode=" + boardCode;
	}
	
}
