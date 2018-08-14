package com.shrcn.business.scl.beans;;

public class GSEConfigModel {
	private String iedName;
	private String apName;
	private String controlBk;
	private String ldInst;
	private String macAddress;
	private String vlanId;
	private String vlanP;
	private String appId;
	private String minTime;
	private String maxTime;
	public GSEConfigModel(String iedName,String apName,String controlBk,String ldInst){
		this.iedName=iedName;
		this.apName=apName;
		this.controlBk=controlBk;
		this.ldInst=ldInst;
		}
	public GSEConfigModel(String iedName,String apName,String controlBk,String ldInst,String macAddress,String vlanId,String vlanP,String appId,String minTime,String maxTime){
		this.iedName=iedName;
		this.apName=apName;
		this.controlBk=controlBk;
		this.ldInst=ldInst;
		this.macAddress=macAddress;
		this.vlanId=vlanId;
		this.vlanP=vlanP;
		this.appId=appId;
		this.minTime=minTime;
		this.maxTime=maxTime;
	}
	public String getIedName() {
		return iedName;
	}
	public void setIedName(String iedName) {
		this.iedName = iedName;
	}
	public String getApName() {
		return apName;
	}
	public void setApName(String apName) {
		this.apName = apName;
	}
	public String getControlBk() {
		return controlBk;
	}
	public void setControlBk(String controlBk) {
		this.controlBk = controlBk;
	}
	public String getLdInst() {
		return ldInst;
	}
	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getVlanId() {
		return vlanId;
	}
	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
	}
	public String getVlanP() {
		return vlanP;
	}
	public void setVlanP(String vlanP) {
		this.vlanP = vlanP;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMinTime() {
		return minTime;
	}
	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}
	public String getMaxTime() {
		return maxTime;
	}
	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}
	
}
