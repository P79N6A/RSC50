/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.outtree;

import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-8
 */
/*
 * 修改历史
 * $Log: ITreeEntry.java,v $
 * Revision 1.1  2013/03/29 09:38:06  cchun
 * Add:创建
 *
 * Revision 1.4  2012/09/03 07:18:17  cchun
 * Update:增加isQt()
 *
 * Revision 1.3  2010/12/21 07:29:35  cchun
 * Update:增加AP类型
 *
 * Revision 1.2  2009/05/12 06:09:14  cchun
 * Update:添加节点描述
 *
 * Revision 1.1  2009/05/08 12:07:17  cchun
 * Update:完善外部、内部信号视图
 *
 */
public interface IOutTreeEntry extends ITreeEntry {

	public static final int IED_ENTRY = 0;
	public static final int AP_ENTRY = 1;
	public static final int LD_ENTRY = 2;
	public static final int DAT_ENTRY = 3;
	public static final int FCDA_ENTRY = 4;
	
	/**
	 * 得到树节点类型
	 * 
	 * @return
	 */
	public int getEntryType();
	
	
	/**
	 * 得到树结点的提示信息
	 * 
	 * @return
	 */
	public String getToolTip();
	
	public boolean isQt();
	
	public boolean isHaveSglRef();
	
	public void setHaveSglRef (boolean haveSglRef);
}
