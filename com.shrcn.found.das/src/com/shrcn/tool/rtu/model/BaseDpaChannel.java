/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-9
 */
/**
 * $Log: BaseDpaChannel.java,v $
 * Revision 1.2  2012/11/19 07:23:19  cchun
 * Update:添加iednumber，iedoffset
 *
 * Revision 1.1  2012/11/09 09:35:38  cchun
 * Update:完成dpa模型sql及其映射
 *
 */
public class BaseDpaChannel {

	private int id;
	protected int iednumber;
	protected int iedoffset;
    private TDpamaster master;
    private TMasterApp tMasterApp;
    
    public BaseDpaChannel() {
    }

	
    public BaseDpaChannel(int id) {
        this.id = id;
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public int getIednumber() {
		return iednumber;
	}


	public void setIednumber(int iednumber) {
		this.iednumber = iednumber;
	}


	public int getIedoffset() {
		return iedoffset;
	}


	public void setIedoffset(int iedoffset) {
		this.iedoffset = iedoffset;
	}


	public TDpamaster getMaster() {
		return master;
	}


	public void setMaster(TDpamaster master) {
		this.master = master;
	}


	public TMasterApp gettMasterApp() {
		return tMasterApp;
	}


	public void settMasterApp(TMasterApp tMasterApp) {
		this.tMasterApp = tMasterApp;
	}
	
}
