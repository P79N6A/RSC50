/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.intree;

import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-5
 */
/*
 * 修改历史
 * $Log: ITreeEntry.java,v $
 * Revision 1.1  2013/03/29 09:38:18  cchun
 * Add:创建
 *
 * Revision 1.21  2012/09/10 02:17:40  cchun
 * Update:添加tm.compare.editor
 *
 * Revision 1.20  2012/09/03 07:07:03  cchun
 * Update:增加APP_476_ENTRY
 *
 * Revision 1.19  2011/12/13 08:02:02  cchun
 * Update:添加101规约类型号
 *
 * Revision 1.18  2011/09/14 03:37:01  cchun
 * Update:添加打印服务器进程
 *
 * Revision 1.17  2011/08/17 05:17:51  cchun
 * Update:添加103规约相关参数
 *
 * Revision 1.16  2011/07/04 09:15:32  cchun
 * Update:添加CDT进程
 *
 * Revision 1.15  2011/03/11 01:20:22  cchun
 * Update:添加id属性
 *
 * Revision 1.14  2010/12/09 01:54:47  cchun
 * Update:添加61850dpa类型
 *
 * Revision 1.13  2010/11/30 11:42:48  cchun
 * Update:添加五防
 *
 * Revision 1.12  2010/11/30 01:06:47  cchun
 * Update:添加节点类型
 *
 * Revision 1.11  2010/11/15 06:36:43  cchun
 * Update:添加节点类型
 *
 * Revision 1.10  2010/09/25 04:38:42  cchun
 * Update:添加节点类型
 *
 * Revision 1.9  2010/04/14 06:12:15  cchun
 * Update:添加setName()
 *
 * Revision 1.8  2010/03/29 02:44:41  cchun
 * Update:提交
 *
 * Revision 1.7  2010/03/16 12:16:49  cchun
 * Update: 更新
 *
 * Revision 1.6  2010/03/09 08:42:12  cchun
 * Update:添加DCA类型id
 *
 * Revision 1.5  2009/11/13 07:18:11  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.4  2009/08/17 06:16:36  cchun
 * Update:添加对Struct类型DA的处理
 *
 * Revision 1.3  2009/05/12 06:09:04  cchun
 * Update:添加节点描述，修改DO信息为IED实例化后的数据
 *
 * Revision 1.2  2009/05/06 11:33:08  cchun
 * Update:优化LN加载效率
 *
 * Revision 1.1  2009/05/06 06:39:03  cchun
 * Add:添加内部信号树节点对象
 *
 */
public interface IInnerTreeEntry extends ITreeEntry {
	
	public static final int RTU_ENTRY = 500;
	
	public static final int DCA_ENTRY = 17;

	public static final int MASTER_ENTRY = 20;
	public static final int LIED_ENTRY = 201;
	
	public static final int APP_61850_ENTRY 	= 301;
	public static final int APP_CALC_ENTRY 		= 302;
	public static final int APP_GUI_ENTRY 		= 303;
	public static final int APP_SYNTIME_ENTRY 	= 304;
	public static final int APP_SWITCHER_ENTRY 	= 305;
	public static final int APP_104_ENTRY 		= 306;
	public static final int APP_DISA_ENTRY 		= 307;
	public static final int APP_dlt645_ENTRY 	= 308;
	public static final int APP_WF_ENTRY 		= 309;
	public static final int APP_61850DPA_ENTRY 	= 310;
	public static final int APP_CDT_ENTRY 		= 311;
	public static final int APP_103_ENTRY 		= 312;
	public static final int APP_PRTSVR_ENTRY 	= 313;
	public static final int APP_101_ENTRY       = 314;
	public static final int APP_476_ENTRY       = 315;
	public static final int APP_103COM_ENTRY    = 316;
	
	public static final int IED_ENTRY 			= 16;
	public static final int RCB_GROUP_ENTRY 	= 1601;
	public static final int SP_GROUP_ENTRY 		= 1602;
	public static final int CO_GROUP_ENTRY 		= 1603;
	public static final int RCB_ENTRY 			= 160101;
	public static final int LD_ENTRY 			= 15;
	public static final int LN_ENTRY 			= 14;
	public static final int LN_FC_ENTRY 		= 13;
	//"CO", "CF", "DC", "EX", "MX", "SE", "SG", "SP", "ST", "SV"
//	public static final int LN_ST_ENTRY = 201;
//	public static final int LN_MX_ENTRY = 202;
//	public static final int LN_CO_ENTRY = 203;
//	public static final int LN_SP_ENTRY = 204;
//	public static final int LN_SG_ENTRY = 205;
//	public static final int LN_SE_ENTRY = 206;
//	public static final int LN_SV_ENTRY = 207;
//	public static final int LN_CF_ENTRY = 208;
//	public static final int LN_DC_ENTRY = 209;
//	public static final int LN_EX_ENTRY = 210;
	public static final int DO_ENTRY = 12;
	public static final int SDO_ENTRY = 11;
	public static final int STRUCT_ENTRY = 11;
	public static final int DA_ENTRY = 10;
	
	//"CO", "CF", "DC", "EX", "MX", "SE", "SG", "SP", "ST", "SV"
	public static enum DA_FC {CO, CF, DC, EX, MX, SE, SG, SP, ST, SV};
	
//	public static final String LD_IEDNAME = "IED name";
//	public static final String LN_LDINST  = "lnInst";
	public static final String LN_PRIFIX  = "prefix";
	public static final String LN_CLASS   = "lnClass";
	public static final String LN_INST    = "inst";
	public static final String LN_TYPE    = "lnType";
	public static final String FC    	  = "fc";
	
	/**
	 * 得到树节点类型
	 * 
	 * @return
	 */
	public int getEntryType();
	
	/**
	 * 得到树节点数据类型
	 * 
	 * @return
	 */
	public String getDataType();
	
	/**
	 * 得到树节点数据类型
	 * 
	 */
	public void setDataType(String dataType);
	
	/**
	 * 得到当前节点的xpath
	 * 
	 * @return
	 */
	public String getXPath(); 
	
	public String getId();
	
	public void setId(String id);
	
	/**
	 * 得到树结点的提示信息
	 * 
	 * @return
	 */
	public String getToolTip();
	
	/**
	 * 设置扩展属性
	 * @param attName
	 * @return
	 */
	public String getAttribute(String attName);
	
	/**
	 * 设置扩展属性
	 * @param attName 属性名
	 * @param value
	 */
	public void setAttribute(String attName, String value);
	
	/**
	 * 是否为叶子节点
	 * @return
	 */
	public boolean isLeaf();

	public boolean isHavaSglRef();

	public void setHaveSglRef(boolean haveSglRef);
}