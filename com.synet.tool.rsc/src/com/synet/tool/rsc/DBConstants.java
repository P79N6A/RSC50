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
	
	// A、B套
	int SUITE_A = 1;
	int SUITE_B = 2;
	// POUT类型
	int DATA_ST = 1;
	int DATA_MX = 2;
	int DATA_STR = 3;
	
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

}

