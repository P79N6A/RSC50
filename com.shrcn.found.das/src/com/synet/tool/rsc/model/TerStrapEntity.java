package com.synet.tool.rsc.model;

/**
 * 
 * 压板与虚端子
 *
 */
public class TerStrapEntity {
	
	private String im107Code;
	private String devName;			//装置Name
	private String strapRefAddr;	//压板参引
	private String strapDesc;		//压板描述
	private String strapType;		//压板类型
	private String vpRefAddr;		//虚端子参引
	private String vpDesc;			//虚端子描述
	private String vpType;			//虚端子类型（开入、开出）
	private Integer matched;		//是否匹配
	private FileInfoEntity fileInfoEntity;
	
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
	public FileInfoEntity getFileInfoEntity() {
		return fileInfoEntity;
	}
	public void setFileInfoEntity(FileInfoEntity fileInfoEntity) {
		this.fileInfoEntity = fileInfoEntity;
	}
}
