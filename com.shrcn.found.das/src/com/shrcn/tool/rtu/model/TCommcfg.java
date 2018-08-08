/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 3 26
 */
/**
 * $Log: TCommcfg.java,v $
 * Revision 1.2  2013/12/11 02:15:09  cxc
 * update:在TCommCfg中增加ipbAddr字段
 *
 * Revision 1.1  2013/04/03 03:16:23  scy
 * Add：创建通信TCommcfg对象
 *
 */
public class TCommcfg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6850937638545680657L;
	
	private int id;
	private String apname;
	private String ipaddr;
	private String ipbaddr;
	private String port;
	private String doublenet;
	private String aptitle;
	private String aequalifier;
	private String psel;
	private String ssel;
	private String tsel;
	private String cremode;
	
	private TDcaied ied;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getApname() {
		return apname;
	}

	public void setApname(String apname) {
		this.apname = apname;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getIpbaddr() {
		return ipbaddr;
	}

	public void setIpbaddr(String ipbaddr) {
		this.ipbaddr = ipbaddr;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDoublenet() {
		return doublenet;
	}

	public void setDoublenet(String doublenet) {
		this.doublenet = doublenet;
	}

	public String getAptitle() {
		return aptitle;
	}

	public void setAptitle(String aptitle) {
		this.aptitle = aptitle;
	}

	public String getAequalifier() {
		return aequalifier;
	}

	public void setAequalifier(String aequalifier) {
		this.aequalifier = aequalifier;
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

	public String getTsel() {
		return tsel;
	}

	public void setTsel(String tsel) {
		this.tsel = tsel;
	}

	public String getCremode() {
		return cremode;
	}

	public void setCremode(String cremode) {
		this.cremode = cremode;
	}
	
	public TDcaied getIed() {
		return ied;
	}
	
	public void setIed(TDcaied ied) {
		this.ied = ied;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String getIedname(){
		return ied.getIedname();
	}
}
