/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public interface DBConstants {

	int CODE_STEP = 20;
	
	String PR_State 		= "State";
	String PR_Analog 		= "Analog";
	String PR_String 		= "String";
	String PR_IED 			= "IED";
	String PR_MMSSvr 		= "MMSSvr";
	String PR_DAU	 		= "DAU";
	String PR_GCB 			= "GCB";
	String PR_SVCB 			= "SVCB";
	String PR_POUT 			= "POUT";
	String PR_RCB 			= "RCB";
	String PR_SGCB 			= "SGCB";
	String PR_SG 			= "SG";
	String PR_SP 			= "SP";
	String PR_LOGICLINK 	= "LOGICLINK";
	String PR_CIRCUIT 		= "CIRCUIT";
	String PR_PIN 			= "PIN";
	String PR_STA 			= "Substation";
	String PR_BAY 			= "Bay";
	String PR_EQP 			= "Equipment";
	String PR_CNode 		= "CNode";
	String PR_Term 			= "Term";
	String PR_SEC 			= "CVTSnd";
	String PR_MMXU 			= "Meas";
	String PR_STRAP 		= "Strap";
	String PR_LINEPF        = "Lineprotfiber";
	String PR_IOTERM        = "Ioterm";
	String PR_POWERKK       = "Powerkk";
	String PR_VOLTAGEKK     = "Voltagekk";
	String PR_REGION        = "Region";
	String PR_CUBICLE       = "Cubicle";
	String PR_CABLE         = "Cable";
	String PR_CORE		    = "Core";
	String PR_PHYSCONN      = "Physconn";
	String PR_BOARD			= "Board";
	String PR_PORT			= "Port";
	String PR_SVCVR			= "SVCVR";
	String PR_LPRELATION    = "LPRelation";
	String PR_FUNCLASS   	= "FuncClass";
	String PR_FUN    		= "ProtFunc";
	String PR_FUNDEF    	= "DefectFuncR";
	//导入信息表主键
	String PR_IEDBOARD		= "IEDBoard";
	String PR_FILEINFO	    = "fileInfo";
	String PR_BOARDWARN		= "BoardWarn";
	String PR_STATUSIN		= "StatusIn";
	String PR_IEDLIST		= "IEDList";
	String PR_FIBRELIST		= "FibreList";
	String PR_STAINFO		= "StaInfo";
	String PR_LINKW			= "LinkWarn";
	String PR_BRKCFM		= "BrkCfm";
	String PR_TERSTRAP		= "TerStrap";
	String PR_PORTLIGHT		= "PortLight";

	String BAY_OTHER		= "其它间隔";	// 未知间隔
	String BAY_MOT 			= "测控装置";
	String BAY_PUB 			= "公共间隔"; // 交换机、采集器、配线架
	String BAY_ALL 			= "全部";
	
	// A、B套
	int SUITE_A = 1;
	int SUITE_B = 2;
	// POUT类型
	int DATA_ST = 1;
	int DATA_MX = 2;
	int DATA_STR = 3;

	// 虚端子数据类型
	int DAT_TYP_INT = 1;
	int DAT_TYP_FLOAT = 2;
	
	// 逻辑链路类型
	int LINK_GOOSE = 1;
	int LINK_SMV = 2;
	
	// 装置类型
	int IED_PROT 	= 00; 	//通用保护装置
	int IED_TERM 	= 20; 	//通用智能终端
	int IED_MU 		= 30; 	//通用合并单元
	int IED_MT 		= 40; 	//通用合并智能终端
	int IED_MONI 	= 50; 	//测控装置
	int IED_JHJ 	= 60; 	//交换机
	int IED_GP 		= 61; 	//光配单元
	int IED_CJQ		= 62; 	//采集器

	int Equipment_BRK = 01; //断路器
	int Equipment_DIS = 02; //刀闸
	int Equipment_BUS = 03; //母线
	int Equipment_PTR = 04; //变压器
	int Equipment_LINE = 05; //线路
	int Equipment_CBR = 06; //电流互感器
	int Equipment_VTR = 07; //电压互感器
	int Equipment_REA = 8; //电抗器
	int Equipment_CAP = 9; //电容器
	int Equipment_GPTR = 10; //站用变、接地变
	int Equipment_SHORT_L = 11; //短引线
	
	int MMXU_3I = 1; //三相电流
	int MMXU_I = 2; //零序或单相电流
	int MMXU_3U = 3; //三相电压
	int MMXU_U = 4; //零序和单相电压

	int STRAP_FUN = 1; //保护功能软压板
	int STRAP_IO = 2; //输入输出软压板
	int STRAP_CHECK = 3; //检修硬压板
	int STRAP_REMOTE = 4; //远方操作硬压板
	int STRAP_BRK = 5; //跳合闸出口硬压板
	
	//区域类型
	int IN_DOOR = 0; //户内
	int OUT_DOOR = 1; //户外
	
	int MATCHED_OK = 1;//匹配（是）
	int MATCHED_NO = 2;//未匹配（否）
	
	int CABLE_TYPE_GL = 1;	//光缆
	int CABLE_TYPE_WL = 2;	//尾缆
	int CABLE_TYPE_TL = 3;	//跳缆
	int CABLE_TYPE_SJX = 4;	//双绞线
	
	int FILE_TYPE101 = 101;//设备台账
	int FILE_TYPE102 = 102;//光缆清册
	int FILE_TYPE103 = 103;//装置板卡端口描述
	int FILE_TYPE104 = 104;//开入信号映射表
	int FILE_TYPE105 = 105;//告警与板卡关联表
	int FILE_TYPE106 = 106;//光强与端口关联表
	int FILE_TYPE107 = 107;//压板与虚端子关联表
	int FILE_TYPE108 = 108;//跳合闸反校关联表
	int FILE_TYPE109 = 109;//监控信息点表
	int FILE_TYPE110 = 110;//告警与链路关联表
	
	int DIRECTION_TX = 1; //表示发送
	int DIRECTION_RX = 2; //表示接收
	int DIRECTION_RT = 3; //表示收发
	
	int PLUG_LC = 1;
	int PLUG_ST = 2;
	int PLUG_SC = 3;
	int PLUG_FC = 4;
	int PLUG_RJ45 = 5;
	
	int CORE_TYPE_XX = 1;//芯线
	int CORE_TYPE_TX = 2;//跳纤
	int CORE_TYPE_SJX = 3;//双绞线
	
	int YES = 1; 	// 是
	int NO = 2; 	// 否

	int DTYPE_ST 	= 1; 	//状态 
	int DTYPE_ALG 	= 2; 	//模拟
	int DTYPE_STRAP = 3; 	//压板
	int DTYPE_POUT 	= 4; 	//输出虚端子
	int DTYPE_PIN 	= 5; 	//输入虚端子

	String sqlPath = "com/synet/tool/rsc/das/rsc-oracle.sql";
	String patchSqlPath = "com/synet/tool/rsc/das/patch.sql";
//	String sqlPath = "com/synet/tool/rsc/das/ora_scada_script.sql";
//	String sqlPath = "com/synet/tool/rsc/das/ora_scada_script.txt";
}

