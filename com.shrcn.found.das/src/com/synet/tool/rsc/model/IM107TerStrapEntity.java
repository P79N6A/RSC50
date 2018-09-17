package com.synet.tool.rsc.model;

/**
 * 
 * 压板与虚端子
 *
 */
public class IM107TerStrapEntity {
	
	private String im107Code;
	private String devName;			//装置Name
	private String devDesc;			//装置名称
	private String strapRefAddr;	//压板参引
	private String strapDesc;		//压板描述
	private String strapType;		//压板类型
	private String vpRefAddr;		//虚端子参引
	private String vpDesc;			//虚端子描述
	private String vpType;			//虚端子类型（开入、开出）
	private Integer matched;		//是否匹配
	private IM100FileInfoEntity fileInfoEntity;
	
	private int conflict; // 是否冲突：1-是，2-否
	private boolean overwrite; // 是否覆盖
	
	public String getIm107Code() {
		return im107Code;
	}
	public void setIm107Code(String im107Code) {
		this.im107Code = im107Code;
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
	public String getStrapRefAddr() {
		return strapRefAddr;
	}
	public void setStrapRefAddr(String strapRefAddr) {
		this.strapRefAddr = strapRefAddr;
	}
	public String getStrapDesc() {
		return strapDesc;
	}
	public void setStrapDesc(String strapDesc) {
		this.strapDesc = strapDesc;
	}
	public String getStrapType() {
		return strapType;
	}
	public void setStrapType(String strapType) {
		this.strapType = strapType;
	}
	public String getVpRefAddr() {
		return vpRefAddr;
	}
	public void setVpRefAddr(String vpRefAddr) {
		this.vpRefAddr = vpRefAddr;
	}
	public String getVpDesc() {
		return vpDesc;
	}
	public void setVpDesc(String vpDesc) {
		this.vpDesc = vpDesc;
	}
	public String getVpType() {
		return vpType;
	}
	public void setVpType(String vpType) {
		this.vpType = vpType;
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
