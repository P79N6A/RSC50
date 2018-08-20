package com.synet.tool.rsc.entity;

/**
 * 
 * 压板与虚端子
 *
 */
public class TerStrap {
	
	private String devName;			//装置Name
	private String strapRefAddr;	//压板参引
	private String strapDesc;		//压板描述
	private String strapType;		//压板类型
	private String vpRefAddr;		//虚端子参引
	private String vpDesc;			//虚端子描述
	private String vpType;			//虚端子类型（开入、开出）
	private boolean matched;		//是否匹配
	
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
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
}
