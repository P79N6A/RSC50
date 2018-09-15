package com.synet.tool.rsc.model;

/**
 * 
 * 开入信号映射
 *
 */
public class IM104StatusInEntity {
	
	private String im104Code;
	private String devName;		//装置Name
	private String pinRefAddr;	//开入虚端子参引
	private String pinDesc;		//端子描述
	private String mmsRefAddr;//MMS信号参引
	private String mmsDesc;	//信号描述
	private Integer matched;	//是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm104Code() {
		return im104Code;
	}
	public void setIm104Code(String im104Code) {
		this.im104Code = im104Code;
	}
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
	public String getMmsRefAddr() {
		return mmsRefAddr;
	}
	public void setMmsRefAddr(String mmsRefAddr) {
		this.mmsRefAddr = mmsRefAddr;
	}
	public String getMmsDesc() {
		return mmsDesc;
	}
	public void setMmsDesc(String mmsDesc) {
		this.mmsDesc = mmsDesc;
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
