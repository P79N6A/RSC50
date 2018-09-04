package com.synet.tool.rsc.model;

/**
 * 
 * 监控信息点表
 *
 */
public class IM109StaInfoEntity {
	
	private String im109Code;
	private String devName;		//装置Name
	private String mmsDesc; 	//信号描述
	private String mmsRefAddr;	//MMS信号参引
	private Integer matched;    //是否匹配
	private IM100FileInfoEntity fileInfoEntity;
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
	
}
