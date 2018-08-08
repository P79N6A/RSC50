/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 7 17
 */
/**
 * $Log: BaseDcaied.java,v $
 * Revision 1.4  2013/09/03 03:17:58  cxc
 * update:修改
 *
 * Revision 1.3  2013/09/02 00:54:17  cxc
 * update:修改
 *
 * Revision 1.2  2013/07/24 12:08:09  cxc
 * Update：修改
 *
 * Revision 1.1  2013/07/17 09:17:34  scy
 * Add：创建装置
 *
 */
public abstract class BaseDcaied implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private int id;
    private String iedname;
    private String iedtype;
    private String description;
    private int statuspoint;

    private TDca dca;
    private TDcaChannel chn;
    
	public BaseDcaied() {
    }
	
    public BaseDcaied(int id) {
        this.id = id;
    }

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

    public TDca getDca() {
		return dca;
	}

	public void setDca(TDca dca) {
		this.dca = dca;
	}

	public String getIedname() {
		return iedname;
	}

	public void setIedname(String iedname) {
		this.iedname = iedname;
	}


	public String getIedtype() {
		return iedtype;
	}


	public void setIedtype(String iedtype) {
		this.iedtype = iedtype;
	}


	public int getStatuspoint() {
		return statuspoint;
	}


	public void setStatuspoint(int statuspoint) {
		this.statuspoint = statuspoint;
	}
	  
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public BaseDcaied copy() {
		return (BaseDcaied) ObjectUtil.duplicate(this);
	}

	public TDcaChannel getChn() {
		return chn;
	}
	
	public void setChn(TDcaChannel chn) {
		this.chn = chn;
		if (chn != null)
			setDca(chn.getDca());
	}
}
