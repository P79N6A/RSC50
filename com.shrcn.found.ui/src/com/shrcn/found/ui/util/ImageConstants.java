/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.ui.util;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-6
 */
/*
 * 修改历史 $Log: ImageConstants.java,v $ Revision 1.13 2014/01/08 06:17:09 cchun
 * update:修改
 * 
 * Revision 1.12 2013/11/12 05:47:08 cchun Update:添加小图标
 * 
 * Revision 1.11 2013/11/08 11:20:11 cchun Update:定义图标常量
 * 
 * Revision 1.10 2013/09/26 08:39:36 cchun Update:增加WAVE
 * 
 * Revision 1.9 2013/09/25 01:19:38 cchun Update:增加图标
 * 
 * Revision 1.8 2013/07/26 06:20:10 cchun Update:添加图片
 * 
 * Revision 1.7 2013/07/09 05:35:54 cchun Update:增加CLEAR_CO
 * 
 * Revision 1.6 2013/07/09 03:46:23 cchun Refactor:统一图片常量
 * 
 * Revision 1.5 2013/06/28 09:20:32 cxc Update：增加图片
 * 
 * Revision 1.4 2013/06/26 00:21:52 cxc Update：定义图片常量
 * 
 * Revision 1.3 2013/05/16 13:54:45 cchun Update:添加关于对话框
 * 
 * Revision 1.2 2013/04/07 02:24:33 cchun Update:修改图片路径
 * 
 * Revision 1.1 2013/03/29 09:36:56 cchun Add:创建
 * 
 * Revision 1.34 2013/03/08 08:24:32 cchun Update:修改EXCEL
 * 
 * Revision 1.33 2012/11/22 07:56:41 cchun Update:添加ABOUT_RTU30
 * 
 * Revision 1.32 2012/09/13 11:35:35 cchun Update:增加EXCEL
 * 
 * Revision 1.31 2012/09/03 07:00:12 cchun Update:增加SORT
 * 
 * Revision 1.30 2012/04/11 08:15:27 cchun Update:增加图标COMPARE
 * 
 * Revision 1.29 2011/09/06 08:26:20 cchun Update:修改按钮图标
 * 
 * Revision 1.28 2011/07/27 07:18:18 cchun Update:添加图片
 * 
 * Revision 1.27 2011/06/27 03:10:09 cchun Update:添加远动“关于”菜单
 * 
 * Revision 1.26 2011/03/18 08:38:38 cchun Update:添加图标
 * 
 * Revision 1.25 2011/03/02 08:28:52 cchun Add:添加图标detail.gif
 * 
 * Revision 1.24 2011/02/25 07:38:02 cchun Update:添加图标
 * 
 * Revision 1.23 2011/02/22 08:01:10 cchun Update:添加CONNECTION
 * 
 * Revision 1.22 2011/01/10 09:13:43 cchun Update:统一图标常量
 * 
 * Revision 1.21 2011/01/05 07:37:39 cchun Update:将LN关联改用图标提示
 * 
 * Revision 1.20 2010/12/29 06:41:41 cchun Update:添加图片
 * 
 * Revision 1.19 2010/12/24 06:38:49 cchun Update:修改访问点图标
 * 
 * Revision 1.18 2010/12/24 03:46:27 cchun Update:添加数据模板图标
 * 
 * Revision 1.17 2010/12/21 07:28:03 cchun Update:添加图片
 * 
 * Revision 1.16 2010/12/15 01:00:53 cchun Update:修改图片路径
 * 
 * Revision 1.15 2010/12/13 02:09:55 cchun Add:checkbox图片
 * 
 * Revision 1.14 2010/11/30 01:03:13 cchun Update:添加图标
 * 
 * Revision 1.13 2010/11/12 09:32:23 cchun Update:添加远动图标常量
 * 
 * Revision 1.12 2010/11/12 08:55:38 cchun Update:增加图标常量
 * 
 * Revision 1.11 2010/09/25 04:37:50 cchun Upate:添加图片
 * 
 * Revision 1.10 2010/09/14 03:30:07 cchun Update:添加常量
 * 
 * Revision 1.9 2010/06/28 02:04:57 cchun Update:添加电容器、电抗器设备图标
 * 
 * Revision 1.8 2009/08/27 02:40:35 lj6061 修改设备类型文件位置
 * 
 * Revision 1.7 2009/06/18 08:06:36 lj6061 内部外部信号图标
 * 
 * Revision 1.6 2009/06/11 07:46:40 cchun 添加可拖拽图标
 * 
 * Revision 1.5 2009/05/31 10:56:52 lj6061 添加图标和定值
 * 
 * Revision 1.4 2009/05/27 08:01:01 lj6061 添加重命名
 * 
 * Revision 1.3 2009/05/27 04:46:26 lj6061 添加一次信息图片
 * 
 * Revision 1.2 2009/05/23 11:31:15 lj6061 添加2次图片
 * 
 * Revision 1.1 2009/05/06 11:33:28 cchun ADD:添加图片常量类
 */
public class ImageConstants {

	private static String BUNDLE_NAME = ImageConstants.class.getName();

	private ImageConstants() {
	}

	static {
		NLS.initializeMessages(BUNDLE_NAME, ImageConstants.class);
	}

	public static String FIX;
	public static String CHECKED;
	public static String UNCHECKED;
	public static String DRAGABLE;
	public static String IMPORT;
	public static String EXPORT;
	public static String SUBNETWORK;
	public static String HISTORY;
	public static String PARENT;
	public static String DETAIL;
	// 一次设备图片
	public static String SUBSTATION;
	public static String TRANSFORMER2;
	public static String TRANSFORMER3;
	public static String CBR;
	public static String CTR;
	public static String VTR;
	public static String LNODE;
	public static String VOLTAGE;
	public static String FUNCTION;
	public static String SUB_FUNCTION;
	public static String SUB_EQUP;
	public static String BAY;
	public static String AXN;
	public static String BAT;
	public static String MOT;
	public static String SAR;
	public static String TRANSFORM;
	public static String IFL;
	public static String CAP;
	public static String REA;

	// 刀闸和接地
	public static String DIS;
	public static String DDIS;

	// 内部外部信号图标
	public static String AP;
	public static String DO;
	public static String FC;
	public static String INPUT_SIG;
	public static String LDEVICE;
	public static String DATASET;
	public static String OUTPUT_SIG;
	// 数据模板
	public static String LN_TXT;
	public static String DOI_TXT;
	public static String DO_TXT;
	public static String DAI_TXT;
	public static String DA_TXT;
	public static String BDA_TXT;
	public static String E_TXT;
	public static String V_TXT;

	public static String PULL_DOWN;

	public static String PULL_UP;

	public static String FRAME_LOGO;

	public static String STEP_ROOT;

	public static String STEP_ADD;

	public static String HAVE_SGLREF;

	public static String STEP_MINUS;

	public static String STEP_TABLE;

	public static String STEP_COLUMN;

	public static String STEP_EXPAND;

	public static String FILTER;
	public static String FOLDER;
	public static String FILE;
	public static String SAVEAS_PRJ;
	public static String SCL;
	public static String SINGLE;
	public static String LIB;
	public static String COLLAPSE;
	public static String OPEN_HIS;
	public static String COPY;
	public static String PASTE;
	public static String SORT;
	public static String UNSORT;
	// IED配置器按钮图片
	public static String EDIT_NEW;
	public static String NET_MAP;
	public static String EDIT_DELETE;
	public static String BAR_SAVE;
	public static String EDIT_SAVE;
	public static String REFRESH;
	public static String REPLACE;
	public static String COMPARE;
	// FCDA移动按钮图片
	public static String MOVE_DOWN;
	public static String MOVE_UP;
	public static String RENAME;
	public static String REMOVE;
	// 子网图片
	public static String COL_SHOW;
	public static String CHECK;

	public static String SCHEMA_CHECK;

	// 关于对话框图片
	public static String ABOUT;
	public static String ABOUT_RTU;
	public static String ABOUT_RTU30;

	// ied装置图片
	public static String IED;
	public static String IED_V;
	// ied装置图片
	public static String IEDBig;
	public static String EXPANDALL;

	// 解除关联图片
	public static String RELEASE;
	public static String BLANK;
	public static String CONNECTTOOL;

	// 打印
	public static String PRINT;
	public static String FIRST;
	public static String PREVIOUS;
	public static String NEXT;
	public static String LAST;
	public static String WARN;
	public static String ERROR;

	public static String SYNCED;
	public static String SYNCED8;
	public static String FIND;
	public static String NET_PORT;
	public static String NET_ADDR;

	// /////////////////// 远动
	public static String IMG_RTU;
	public static String IMG_DCA;
	public static String IMG_MASTER;
	public static String IMG_APP;
	public static String IMG_OBJ;
	public static String IMG_OUT;
	public static String EXCEL;
	public static String logo;
	public static String SCD;
	public static String SAVE_RTU;
	public static String REPLACE_SCD;
	public static String TABLE;
	public static String CONNECTION;
	public static String PROPERTY;
	public static String PRIMARY_REFRESH_ICON;

	// /////////////////// 配置调试工具
	public static String EDIT_EXPORT;
	public static String CLEAR_CO;
	public static String CONNECT;
	public static String DISCONNECT;
	public static String PROJECT;
	public static String VBI;
	public static String SETTING;
	public static String PARAMETER;
	public static String DEVICE;
	public static String BREAK_OPEN_OP;
	public static String BREAK_CLOSE_OP;
	public static String REFRESH1;
	public static String MOVE_OUT_OP;
	public static String VBI_OP;
	public static String BASE_INFO;
	public static String MOVE_OUT;
	public static String LIB_MGR;
	public static String MEMORY;
	public static String EDIT24;
	public static String RELOAD24;
	public static String IMPORT24;
	public static String EXPORT24;

	
	// /////////////////// 波形演示工具
	public static String PLAY;
	public static String PAUSE;
	public static String STOP;
	public static String WAVE;
	public static String PLAYSET;

	// ////////////////// 配置工具
	public static String ABOUT_TITLE;
	public static String CONFIG_ICD_16;
	public static String PROJECT_16;
	public static String SYSTEM_CONFIG_16;
	public static String ICD_CONFIG_16;
	public static String DEV_ATTR_16;
	public static String TASK_16;
	public static String RACK_16;
	public static String MARCRO_16;
	public static String MARCRO_UNIT_16;
	public static String MARCRO_PARAM_16;
	public static String SIGNAL_DESC_16;
	public static String SIGNAL_LINK_16;
	public static String REF_TABLE_16;
	public static String PARA_GROUP_16;
	public static String HMI_16;
	public static String LCD_16;
	public static String GOOSE_CFG_16;
	public static String SV_CFG_16;
	public static String SV_OUT_16;
	public static String SV_IN_16;
	public static String IN_16;
	public static String OUT_16;
	public static String GOOSE_OUT_16;
	public static String GOOSE_IN_16;
	public static String REF_ITEM_16;
	public static String REF_ITEM_DIS_16;
	public static String PARA_GROUP_ITEM_16;
	public static String REF_GROUP_ITEM_16;
	public static String HEADER;
	public static String ICD_FOLDER;
	public static String ICD_ITEM;
	public static String COMMUNICATION;
	public static String IED_16;
	public static String ACCESS_POINT;
	public static String SERVER;
	public static String LD;
	public static String LN0;
	public static String LN;
	public static String DATA_SET_ITEM;
	public static String DATA_SET;
	public static String SETTING_CONTROL;
	public static String SETTING_ITEM;
	public static String GSE_CONTROL;
	public static String GSE_ITEM;
	public static String SV_CONTROL;
	public static String SV_ITEM;
	public static String LOG_CONTROL;
	public static String LOG_ITEM;
	public static String INPUTS;
	public static String LN_TYPE;
	public static String DO_TYPE;
	public static String DA_TYPE;
	public static String ENUM_TYPE;
	public static String TEMPLATE_ITEM;
	public static String SERVICES;
	public static String REPORT_CONTROL;
	public static String TEMPLATE;
	public static String LN_TYPE_ITEM;
	public static String DO_TYPE_ITEM;
	public static String DA_TYPE_ITEM;
	public static String ENUM_TYPE_ITEM;
	public static String PARAM;
	public static String IN;
	public static String OUT;
	public static String ITEM1;
	public static String ITEM2;
	public static String ITEM3;
	public static String ITEM4;
	public static String ITEM5;
	public static String REPORT_ITEM;
	public static String ERROR_NODE;
	public static String WARNING_NODE;
	public static String MENU_16;
	public static String NSG_16;
	public static String NSG_INFO_16;
	public static String NSG_GOOSE_IN_16;
	public static String NSG_GOOSE_OUT_16;
	public static String NSG_SV_IN_16;
	public static String NSG_SV_OUT_16;
	public static String NSG_IEEE1588_16;
	public static String NSG_GMRP_16;
	public static String ADC_16;

	//////////////////// 调试工具
	public static String DETAIL_16;
	public static String STOP_REFRESH_24;
	public static String REFRESH_24;
	public static String DEVICE_DISCONNECT;
	public static String DEVICE_CONNECT32;
	public static String CONNECT16;
	public static String NEWCONNECT16;
	public static String DISCONNECT16;
	public static String RECONNECT16;
	public static String NEW_DEVICE16;
	public static String DELETE_DEVICE16;
	public static String RENAME_DEVICE16;
	public static String ADDOFFSET24;
	public static String ADD24;
	public static String EDIT_24;
	public static String DELETE24;
	public static String DELETEAll24;
	public static String RELOAD_24;
	public static String IMPORT_24;
	public static String EXPORT_24;
	public static String IMPORT_EXCEL;
	public static String EXPORT_EXCEL;
	public static String EXPORT_DESC;
	public static String SAVE24;
	public static String TRANSMIT_OFF;
	public static String TRANSMIT;
	public static String YES;
	public static String FILE_DOWNLOAD;
	public static String FILE_UPLOAD;
	public static String SWITCH_ON;
	public static String SWITCH_OFF;
	public static String WAVE_24;
	public static String WAVE_ALL;
	public static String HISTORY_REPORT;
	public static String CLEAR_CHAN;
	public static String STATION;
	public static String STATUS_OFFLINE;
	public static String STATUS_ONLINE;
	public static String GROUP_CTRL;
	public static String GROUP_DG;
	public static String GROUP_OTHER;
	public static String GROUP_PARAM;
	public static String GROUP_SETTING;
	public static String GROUP_ST;
	public static String GROUP_SV;
	public static String GROUP_REPORT;
	public static String ENTRY_CTRL;
	public static String ENTRY_DG;
	public static String ENTRY_OTHERS;
	public static String ENTRY_REPORT;
	public static String ENTRY_PARAM;
	public static String ENTRY_SETTING;
	public static String ENTRY_ST;
	public static String ENTRY_SV;
	public static String DEBUG_MASTER;
	
	
	//////////////////// 表格图片
	public static String TABLE_ADD;
	public static String TABLE_INSERT;
	public static String TABLE_DELETE;
	public static String TABLE_UP;
	public static String TABLE_DOWN;
	public static String TABLE_SEARCH;
	public static String TABLE_EXPORT_EXCEL;
	public static String TABLE_IMPORT_EXCEL;
	public static String TABLE_SETTING;
	public static String TABLE_DIALOG;
	public static String TABLE_RADIO_CHECKED;
	public static String TABLE_RADIO_UNCHECKED;
	
	//LCD仿真
	public static String LIGHT_RED;
	public static String LIGHT_GREEN;
	public static String LIGHT_YELLOW;
	public static String LIGHT;
	public static String LIGHT_REGBRIGHT;
	public static String LIGHT_GREENBRIGHT;
	public static String LIGHT_YELLOWBRIGHT;
	
	// OCT工具
	public static String LIGHT_1;
	public static String LIGHT_RED1;
	
	// IMC工具
	public static String USER;
	public static String MODIFY_PASS;
}
