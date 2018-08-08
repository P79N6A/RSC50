/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-5
 */
/**
 * $Log: RowData.java,v $ Revision 1.2 2012/11/13 11:22:05 cchun
 * Refactor:统一序号属性名为seqnum
 * 
 * Revision 1.1 2012/11/05 07:53:25 cchun Update:添加rownum和fc
 * 
 */
public abstract class RowData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int seqnum;

	public int getSeqnum() {
		return seqnum;
	}

	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}

}
