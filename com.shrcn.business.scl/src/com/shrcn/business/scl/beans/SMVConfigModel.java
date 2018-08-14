package com.shrcn.business.scl.beans;

public class SMVConfigModel {
	private String iedName;
	private String apName;
	private String controlBk;
	private String ldInst;
	private String macAddress;
	private String vlanId;
	private String vlanP;
	private String appId;
	public SMVConfigModel(String iedName,String apName,String controlBk,String ldInst){
		this.iedName=iedName;
		this.apName=apName;
		this.controlBk=controlBk;
		this.ldInst=ldInst;
		
	}
	public SMVConfigModel(String iedName,String apName,String controlBk,String ldInst,String macAddress,String vlanId,String vlanP,String appId){
		this.iedName=iedName;
		this.apName=apName;
		this.controlBk=controlBk;
		this.ldInst=ldInst;
		this.macAddress=macAddress;
		this.vlanId=vlanId;
		this.vlanP=vlanP;
		this.appId=appId;
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
	
}