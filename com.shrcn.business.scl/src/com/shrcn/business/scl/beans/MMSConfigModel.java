package com.shrcn.business.scl.beans;;

public class MMSConfigModel {
	private String iedName;
	private String apName;
	private String ip;
	private String sub;
	private String getWay;
	private String aeInvoke;
	private String aeQualifier;
	private String apInvoke;
	private String apTitle;
	private String nasp;
	private String tsel;
	private String psel;
	private String ssel;
	
	public MMSConfigModel(String iedName, String apName, String ip, String sub,
			String getWay, String aeInvoke, String aeQualifier,String apInvoke,
		    String apTitle,String nasp,String tsel,String psel,String ssel) {
		this.iedName = iedName;
		this.apName = apName;
		this.ip = ip;
		this.sub = sub;
		this.getWay=getWay;
		this.aeInvoke=aeInvoke;
		this.aeQualifier=aeQualifier;
		this.apInvoke=apInvoke;
		this.apTitle=apTitle;
		this.nasp=nasp;
		this.tsel=tsel;
		this.psel=psel;
		this.ssel=ssel;
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getGetWay() {
		return getWay;
	}
	public void setGetWay(String getWay) {
		this.getWay = getWay;
	}
	public String getAeInvoke() {
		return aeInvoke;
	}
	public void setAeInvoke(String aeInvoke) {
		this.aeInvoke = aeInvoke;
	}
	public String getAeQualifier() {
		return aeQualifier;
	}
	public void setAeQualifier(String aeQualifier) {
		this.aeQualifier = aeQualifier;
	}
	public String getApInvoke() {
		return apInvoke;
	}
	public void setApInvoke(String apInvoke) {
		this.apInvoke = apInvoke;
	}
	public String getApTitle() {
		return apTitle;
	}
	public void setApTitle(String apTitle) {
		this.apTitle = apTitle;
	}
	public String getNasp() {
		return nasp;
	}
	public void setNasp(String nasp) {
		this.nasp = nasp;
	}
	public String getTsel() {
		return tsel;
	}
	public void setTsel(String tsel) {
		this.tsel = tsel;
	}
	public String getPsel() {
		return psel;
	}
	public void setPsel(String psel) {
		this.psel = psel;
	}
	public String getSsel() {
		return ssel;
	}
	public void setSsel(String ssel) {
		this.ssel = ssel;
	}
	
	}

