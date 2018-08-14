/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl;


/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-23
 */
/*
 * 修改历史 $Log: PropertConstants.java,v $
 * 修改历史 Revision 1.3  2013/08/14 11:58:24  cchun
 * 修改历史 Update:添加BUILD_ID
 * 修改历史
 * 修改历史 Revision 1.2  2013/08/13 12:44:36  cchun
 * 修改历史 Udpate:增加SUBSTATION_AP、SG_BOARD
 * 修改历史
 * 修改历史 Revision 1.1  2013/03/29 09:36:56  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.18  2013/01/28 03:17:55  cchun
 * 修改历史 Update:添加goose最大、最小时间配置
 * 修改历史
 * 修改历史 Revision 1.17  2011/08/24 08:15:16  cchun
 * 修改历史 Update:按虚端子类型过滤
 * 修改历史
 * 修改历史 Revision 1.16  2011/08/11 10:12:01  cchun
 * 修改历史 Update:扩充可配置参数
 * 修改历史
 * 修改历史 Revision 1.15  2011/07/11 08:52:01  cchun
 * 修改历史 Refactor:清理不必要的常量定义
 * 修改历史
 * 修改历史 Revision 1.14  2010/11/08 02:29:58  cchun
 * 修改历史 Update:添加schema校验参数
 * 修改历史
 * 修改历史 Revision 1.13  2010/11/03 08:26:14  cchun
 * 修改历史 Update:添加常量
 * 修改历史
 * 修改历史 Revision 1.12  2010/11/03 03:01:39  cchun
 * 修改历史 Update:添加配置参数名称常量
 * 修改历史
 * 修改历史 Revision 1.11  2010/09/09 08:14:00  cchun
 * 修改历史 Update:整理字符常量定义
 * 修改历史
 * 修改历史 Revision 1.10  2010/06/28 02:45:25  cchun
 * 修改历史 Update:添加oid配置项
 * 修改历史
 * 修改历史 Revision 1.9  2010/05/27 05:39:56  cchun
 * 修改历史 Update:添加version配置项
 * 修改历史
 * 修改历史 Revision 1.8  2010/01/19 09:02:37  lj6061
 * 修改历史 add:统一国际化工程
 * 修改历史
 * 修改历史 Revision 1.7  2009/12/01 03:44:41  hqh
 * 修改历史 添加网络模式常量
 * 修改历史
 * 修改历史 Revision 1.6  2009/11/30 05:20:50  hqh
 * 修改历史 添加ip最大值标签
 * 修改历史
 * 修改历史 Revision 1.5  2009/11/30 04:45:42  hqh
 * 修改历史 添加ip配置常量
 * 修改历史
 * 修改历史 Revision 1.4  2009/11/02 10:32:16  hqh
 * 修改历史 添加SMV_APPID key
 * 修改历史
 * 修改历史 Revision 1.3  2009/08/31 06:23:55  lj6061
 * 修改历史 修改配置文件的操作
 * 修改历史
 * 修改历史 Revision 1.2  2009/07/15 02:58:34  cchun
 * 修改历史 添加信号关联调整功能
 * 修改历史
 * 修改历史 Revision 1.1  2009/07/06 07:35:14  lj6061
 * 修改历史 添加配置文件统一建模选项
 * 修改历史 改变配置文件工程的位置
 * 修改历史
 * 修改历史 Revision 1.2  2009/06/25 07:25:55  lj6061
 * 修改历史 整理配置文件定值
 * 修改历史
 * 修改历史 Revision 1.1  2009/06/23 02:36:04  lj6061
 * 修改历史 提取配置常量
 * 修改历史
 */
public interface IPropertTag {

	/** 属性键名 */
	String LANGUAGE 			= "LANGUAGE"; //$NON-NLS-1$
	String CRC_CHECK 			= "CRC_CHECK"; //$NON-NLS-1$
	String XSD_CHECK 			= "XSD_CHECK"; //$NON-NLS-1$
	// Schema路径 
	String SCL_SCHEMA  			= "SCL_SCHEMA"; //$NON-NLS-1$
	String SCHEMA_VER 			= "SCHEMA_VER"; //$NON-NLS-1$
    String SCL_VERSION 			= "SCL_VERSION"; //$NON-NLS-1$
    String SCL_PRIMARY_OID 		= "SCL_Primary_OID";
    String PRITEMPLATE 			= "PRITEMPLATE"; //$NON-NLS-1$
    String HISTORY				= "HISTORY"; //$NON-NLS-1$
    // 虚端子lnClass
    public final static String VT_LNCLASS	 		= "VT_LNCLASS"; //$NON-NLS-1$
    // 建模方式 统一建模。非统一建模，默认为非
	public final static String UNIFIED_MODEL 		= "UNIFIED_MODEL"; //$NON-NLS-1$
	// 网络模式
	public final static String NETWORK_MODEL 		= "NETWORK_MODEL"; //$NON-NLS-1$
    String MMS_IP_TITLE 		= "IP"; //$NON-NLS-1$
    String MMS_OSI_PSEL 		= "OSI_PSEL"; //$NON-NLS-1$
    String MMS_OSI_TSEL 		= "OSI_TSEL"; //$NON-NLS-1$
    String GSE_MAC_TITLE 		= "GSE_MAC"; //$NON-NLS-1$
	public final static String GSE_MIN_TIME			= "GSE_MIN_TIME"; //$NON-NLS-1$
	public final static String GSE_MAX_TIME			= "GSE_MAX_TIME"; //$NON-NLS-1$
    String SMV_MAC_TITLE 		= "SMV_MAC"; //$NON-NLS-1$
    String GSE_APPID_TITLE 		= "GSE_APPID"; //$NON-NLS-1$
    String SMV_APPID_TITLE 		= "SMV_APPID"; //$NON-NLS-1$
    String SUBNET_MASK 			= "SUBNET_MASK"; //$NON-NLS-1$
    
	public final static String PREFIX_AUTO_MODE		= "PREFIX_AUTO_MODE"; //$NON-NLS-1$
	public final static String RELATION_CHECK_MODE	= "RELATION_CHECK_MODE"; //$NON-NLS-1$
	public final static String RELATION_NORM_MODE   = "RELATION_NORM_MODE";
	public final static String RELATION_DA_VALUE    = "RELATION_DA_VALUE";
}
