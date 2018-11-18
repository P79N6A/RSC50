package com.synet.tool.rsc.model;


/**
 * 
 * 告警与链路关联表
 *
 */
public class IM110LinkWarnEntity {
	
	private String im110Code;
	private String devName;		//装置Name
	private String devDesc;		//装置名称
	private String mmsRefAddr;	//告警信息参引
	private String mmsDesc; 	//告警信息描述
	private String sendDevName;	//发送装置Name
	private String cbRef;		//逻辑链路Ref
	
	private Integer matched;    //是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict = 2; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm110Code() {
		return im110Code;
	}
	public void setIm110Code(String im110Code) {
		this.im110Code = im110Code;
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
	public String getMmsDesc() {
		return mmsDesc;
	}
	public void setMmsDesc(String mmsDesc) {
		this.mmsDesc = mmsDesc;
	}
	public String getMmsRefAddr() {
		return mmsRefAddr;
	}
	public void setMmsRefAddr(String mmsRefAddr) {
		this.mmsRefAddr = mmsRefAddr;
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
	public String getSendDevName() {
		return sendDevName;
	}
	public void setSendDevName(String sendDevName) {
		this.sendDevName = sendDevName;
	}
	public String getCbRef() {
		return cbRef;
	}
	public void setCbRef(String cbRef) {
		this.cbRef = cbRef;
	}
	@Override
	public String toString() {
		return "devName=" + devName + ", devDesc="
				+ devDesc + ", mmsRefAddr=" + mmsRefAddr + ", mmsDesc="
				+ mmsDesc + ", sendDevName=" + sendDevName + ", cbRef=" + cbRef;
	}
	
}
