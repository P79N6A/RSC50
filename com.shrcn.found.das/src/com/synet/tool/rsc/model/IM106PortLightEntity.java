package com.synet.tool.rsc.model;

/**
 * 
 * 光强与端口
 *
 */
public class IM106PortLightEntity {
	
	private String im106Code;
	private String devName;			//装置Name
	private String devDesc;			//装置名称
	private String opticalRefAddr;	//光强信息参引
	private String opticalDesc;		//光强信息描述
	private String boardCode;		//板卡编号
	private String portCode;		//端口编号
	private Integer matched;		//是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict = 2; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm106Code() {
		return im106Code;
	}
	public void setIm106Code(String im106Code) {
		this.im106Code = im106Code;
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
	public String getOpticalRefAddr() {
		return opticalRefAddr;
	}
	public void setOpticalRefAddr(String opticalRefAddr) {
		this.opticalRefAddr = opticalRefAddr;
	}
	public String getOpticalDesc() {
		return opticalDesc;
	}
	public void setOpticalDesc(String opticalDesc) {
		this.opticalDesc = opticalDesc;
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
}
