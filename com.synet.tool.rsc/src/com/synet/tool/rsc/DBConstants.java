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
	String PR_GCB 			= "GCB";
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
	String PR_PHYSCONN      = "Physconn";
	String PR_BOARD			= "Board";
	String PR_PORT			= "Port";
	String PR_IEDBOARD		= "IEDBoard";
	String PR_FILEINFO	    = "fileInfo";

	String BAY_PROT			= "保护间隔";
	String BAY_PUB 			= "公共间隔";
	
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

	// 状态量功能类型
	int DAT_BRK	 	= 01; 	//开关状态
	int DAT_DIS	 	= 05; 	//刀闸状态
	int DAT_COM	 	= 15;	//通讯状态
	int DAT_CK_STRAP	 	= 31;	//检修压板
	int DAT_GOUT_STRAP	 	= 32;	//GOOSE输出压板
	int DAT_GIN_STRAP	 	= 33;	//GOOSE输入压板
	int DAT_SVIN_STRAP	 	= 34;	//SV输入压板
	int DAT_TM_STRAP	 	= 35;	//智能终端出口硬压板
	int DAT_IED_LIGHT	 	= 51;	//装置运行状态灯
	int DAT_IED_W_HW	 	= 52;	//装置告警-硬件异常
	int DAT_IED_W_SW	 	= 53;	//装置告警-软件异常
	int DAT_IED_W_T	 	= 54;	//装置告警-CT/PT断线
	int DAT_IED_I	 	= 55;	//装置告警-差流越限
	int DAT_IED_IN	 	= 56;	//装置告警-开入异常
	int DAT_TIME	 	= 57;	//对时异常
	int DAT_W_SV	 	= 58;	//SV告警（危急）
	int DAT_E_SV	 	= 59;	//SV异常（严重）
	int DAT_W_GO	 	= 60;	//GOOSE告警（危急）
	int DAT_E_GO	 	= 61;	//GOOSE异常（严重）
	int DAT_IED_W	 	= 100;	//装置一般告警
	int DAT_PROT_MX	 	= 101;	//保护测量值
	int DAT_MS_VALUE	= 102;	//模拟量采样值
	int DAT_TEMP	 	= 103;	//装置监测温度
	int DAT_WK_VOL	 	= 104;	//装置工作电压
	int DAT_PORT_POWER	 	= 105;	//光口光强

	// 虚回路类型
	int CIRCUIT_OPEN = 01;	//跳闸回路
	int CIRCUIT_CLOSE = 02;	//重合闸回路
	int CIRCUIT_FIAL_START = 03;	//启动失灵回路
	int CIRCUIT_LBRK = 04;	//联跳回路
	int CIRCUIT_LOCK = 05;	//闭锁回路
	int CIRCUIT_I = 06;	//电流采样回路
	int CIRCUIT_U = 07;	//电压采样回路
	int CIRCUIT_ST = 8;	//遥信开入
	int CIRCUIT_OTHER = 9;	//其它

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
	
	int MATCHED_OK = 0;//匹配
	int MATCHED_NO = 1;//未匹配
	
	int FILE_TYPE101 = 101;//设备台账
	int FILE_TYPE102 = 102;//光缆清册
	int FILE_TYPE103 = 103;//装置板卡端口描述
	int FILE_TYPE104 = 104;//开入信号映射表
	int FILE_TYPE105 = 105;//告警与板卡关联表
	int FILE_TYPE106 = 106;//光强与端口关联表
	int FILE_TYPE107 = 107;//压板与虚端子关联表
	int FILE_TYPE108 = 108;//跳合闸反校关联表
	int FILE_TYPE109 = 109;//监控信息点表
}

