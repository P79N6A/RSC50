package com.synet.tool.rsc.model;


/**
 * 
 * 监控信息点表
 *
 */
public class IM109StaInfoEntity extends ImportMmsEntity {
	
	private String im109Code;
	private String devName;		//装置Name
	private String devDesc;		//装置名称
	private String mmsRefAddr;	//MMS信号参引
	private String mmsDesc; 	//信号描述
	private Integer matched;    //是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict = 2; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm109Code() {
		return im109Code;
	}
	public void setIm109Code(String im109Code) {
		this.im109Code = im109Code;
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
	@Override
	public String toString() {
		return "devName=" + devName + ", devDesc="
				+ devDesc + ", mmsRefAddr=" + mmsRefAddr + ", mmsDesc="
				+ mmsDesc;
	}
	
}
