/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl;


/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-6-23
 */
public interface PropertConstants {
    /** ~属性键名结束~ */
	String SCHEMA_VERSION 		= "2003"; //$NON-NLS-1$
	String SCL_VERSION_TITLE 	= "SCL带有revision、version属性。"; //$NON-NLS-1$
	String INTERNA 				= Messages.getString("PropertConstants.chinese"); //$NON-NLS-1$
	
	String SCL_VERSION_DEFAULT 	= "true"; //$NON-NLS-1$
	String MMS_IP_TEXT 			= "10.10.5.0"; //$NON-NLS-1$
	String MMS_IP_MAX 			= "255.255.255.255"; //$NON-NLS-1$
	String LABLE_MMSIP 			= Messages.getString("PropertConstants.lable_max")+MMS_IP_MAX; //$NON-NLS-1$

	String VLAN_ID 			= "000"; //$NON-NLS-1$
	String VLAN_PTY			= "4"; //$NON-NLS-1$
	
	String GSE_MAC_TEXT 		= "01-0C-CD-01-00-00"; //$NON-NLS-1$
	String GSE_MAC_PREFIX 		= "01-0C-CD-01-"; //$NON-NLS-1$
	String GSE_APPID 			= "0000"; //$NON-NLS-1$
	String GSE_MAC_MAX 			= "01-0C-CD-01-FF-FF"; //$NON-NLS-1$
	String LABLE_GSEMAC 		= Messages.getString("PropertConstants.lable_gsemax"); //$NON-NLS-1$
	
	String SMV_MAC_TEXT 		= "01-0C-CD-04-00-00"; //$NON-NLS-1$
	String SMV_MAC_PREFIX 		= "01-0C-CD-04-"; //$NON-NLS-1$
	String SMV_APPID 			= "0000"; //$NON-NLS-1$
	String SMV_MAC_MAX 			= "01-0C-CD-04-FF-FF"; //$NON-NLS-1$
	String LABLE_SMVMAC 		= Messages.getString("PropertConstants.lable_smvmax"); //$NON-NLS-1$
	String GSE_MIN_APPID		= "0x0000";
	String GSE_MAX_APPID		= "0x3FFF";
	String SMV_MIN_APPID 		= "0x4000"; //$NON-NLS-1$
	String SMV_MAX_APPID 		= "0x7FFF"; //$NON-NLS-1$
	String LABLE_SMVAPPID 		= Messages.getString("PropertConstants.lable_min") + SMV_MIN_APPID +
												Messages.getString("PropertConstants.max") + SMV_MAX_APPID; //$NON-NLS-1$ //$NON-NLS-2$
	
	/** 网络模式*/
	String TWOLAYER_NETWORK_MODEL 	= Messages.getString("PropertConstants.two_layer_network"); //$NON-NLS-1$
	String THREELAYER_NETWORK_MODEL = Messages.getString("PropertConstants.three_layer_network"); //$NON-NLS-1$
	
	// 自动选项配置
	String AUTO_OPT 				= Messages.getString("PropertConstants.autoOpt"); //$NON-NLS-1$
	String PREFIX_ADD_IEDNAME 		= Messages.getString("PropertConstants.prefixAddIedname"); //$NON-NLS-1$
	String SIGNAL_RELATION_INSPECT  = Messages.getString("PropertConstants.signalRelationInspect");
	String SIGNAL_RELATION_NORM     = Messages.getString("PropertConstants.signalRelationNorm");
}
