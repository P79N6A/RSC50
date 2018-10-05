package com.synet.tool.rsc.io.ied;

public class NetConfig {
	private String ipA;
	private String ipB;
	private String CBNAME;
	private String CBID;
	private String MACAddr;
	private String VLANID;
	private String VLANPriority;
	private String APPID;
	
	public NetConfig(String cBID, String ipA) {
		this.CBID = cBID;
		this.ipA = ipA;
	}
	
	public NetConfig(String cBID, String cBNAME, String mACAddr,
			String vLANID, String vLANPriority, String aPPID) {
		this.CBID = cBID;
		this.CBNAME = cBNAME;
		this.MACAddr = mACAddr;
		this.VLANID = vLANID;
		this.VLANPriority = vLANPriority;
		this.APPID = aPPID;
	}
	
	public String getIpA() {
		return ipA;
	}
	public void setIpA(String ipA) {
		this.ipA = ipA;
	}
	public String getIpB() {
		return ipB;
	}
	public void setIpB(String ipB) {
		this.ipB = ipB;
	}
	public String getCBNAME() {
		return CBNAME;
	}
	public void setCBNAME(String cBNAME) {
		CBNAME = cBNAME;
	}
	public String getCBID() {
		return CBID;
	}
	public void setCBID(String cBID) {
		CBID = cBID;
	}
	public String getMACAddr() {
		return MACAddr;
	}
	public void setMACAddr(String mACAddr) {
		MACAddr = mACAddr;
	}
	public String getVLANID() {
		return VLANID;
	}
	public void setVLANID(String vLANID) {
		VLANID = vLANID;
	}
	public String getVLANPriority() {
		return VLANPriority;
	}
	public void setVLANPriority(String vLANPriority) {
		VLANPriority = vLANPriority;
	}
	public String getAPPID() {
		return APPID;
	}
	public void setAPPID(String aPPID) {
		APPID = aPPID;
	}

}
