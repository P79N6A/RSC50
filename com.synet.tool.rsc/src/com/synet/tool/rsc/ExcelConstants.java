package com.synet.tool.rsc;

public interface ExcelConstants {

	//表类型
	String IM101_IED_LIST = "设备台账";
	String IM102_FIBRE_LIST ="光缆清册";
	String IM103_IED_BOARD = "装置板卡端口描述";
	String IM104_STATUS_IN = "开入信号映射表";
	String IM105_BOARD_WARN = "告警与板卡关联表";
	String IM106_PORT_LIGHT = "光强与端口关联表";
	String IM107_TER_STRAP = "压板与虚端子关联表";
	String IM108_BRK_CFM = "跳合闸反校关联表";
	String IM109_STA_INFO = "监控信息点表";
	//表字段信息
	String IM101_DEV_NAME  = "装置Name";
	String IM101_DEV_DESC  = "装置名称";
	String IM101_BAY  = "所属间隔";
	String IM101_CUBICLE  = "所属屏柜";
	String IM101_MANUFACTUROR = "生产厂家";
	String IM101_DEV_TYPE  = "装置型号";
	String IM101_DEV_VERSION  = "装置版本号";
	String IM101_A_OR_B  = "A/B套";
	String IM101_PROT_CLASSIFY  = "保护分类(国产、进口)";
	String IM101_PROT_MODEL  = "保护型号";
	String IM101_PROT_TYPE  = "保护类型";
	String IM101_NETA_IP  = "A网IP";
	String IM101_NETB_IP  = "B网IP";
	String IM101_DATE_SERVICE  = "投运日期";
	String IM101_DATE_PRODUCT  = "出厂日期";
	String IM101_PRODUCT_CODE  = "出厂编号";
	String IM101_DATA_COLLECT_TYPE  = "数据采集方式";
	String IM101_OUT_TYPE  = "出口方式";
	String IM101_BOARD_NUM  = "板卡数量";
	String IM101_MATCHED_IED_CODE  = "匹配装置代码";
	String[] IM101_IED_LIST_FIELDS = {
			IM101_DEV_NAME,
			IM101_DEV_DESC,
			IM101_BAY,
			IM101_CUBICLE,
			IM101_NETA_IP,
			IM101_NETB_IP,
//			IM101_MANUFACTUROR,
//			IM101_DEV_TYPE,
//			IM101_DEV_VERSION,
//			IM101_A_OR_B,
//			IM101_PROT_CLASSIFY,
//			IM101_PROT_MODEL,
//			IM101_PROT_TYPE,
			IM101_DATE_SERVICE,
			IM101_DATE_PRODUCT,
			IM101_PRODUCT_CODE,
			IM101_DATA_COLLECT_TYPE,
			IM101_OUT_TYPE,
//			IM101_BOARD_NUM,
//			IM101_MATCHED_IED_CODE
	};
	
	String IM102_CABLE_CODE = "光缆编号";
	String IM102_CORE_CODE = "纤芯编号";
	String IM102_DEV_CODEA = "端口A装置代码";
	String IM102_DEV_NAMEA = "端口A装置Name";
	String IM102_DEV_DESCA = "端口A装置名称";
	String IM102_BOARD_PORT_CODEA = "装置插件/端口号(端口A)";//现在不用
	String IM102_BOARD_CODEA = "端口A板卡编号";
	String IM102_PORT_CODEA = "端口A端口编号";
	String IM102_CUBICLE_CODEA = "端口A屏柜代码";
	String IM102_CUBICLE_DESCA = "端口A屏柜名称";
	String IM102_CORE_CODEA = "端口A芯线编号";
	String IM102_DISTRIB_FRAME_CODEA = "端口A光配架端口编号";
	String IM102_DEV_CODEB = "端口B装置代码";
	String IM102_DEV_NAMEB = "端口B装置Name";
	String IM102_DEV_DESCB = "端口B装置名称";
	String IM102_BOARD_PORT_CODEB = "装置插件/端口号(端口B)";//现在不用
	String IM102_BOARD_CODEB = "端口B板卡编号";
	String IM102_PORT_CODEB = "端口B端口编号";
	String IM102_CUBICLE_CODEB = "端口B屏柜代码";
	String IM102_CUBICLE_DESCB = "端口B屏柜名称";
	String IM102_CORE_CODEB = "端口B芯线编号";
	String IM102_DISTRIB_FRAME_CODEB = "端口B光配架端口编号";
	String[] IM102_FIBRE_LIST_FIELDS = {
			IM102_CABLE_CODE,
			IM102_CORE_CODE,
			IM102_DEV_CODEA,
			IM102_DEV_NAMEA,
			IM102_DEV_DESCA,
//			IM102_BOARD_PORT_CODEA,
			IM102_BOARD_CODEA,
			IM102_PORT_CODEA,
			IM102_CUBICLE_CODEA,
			IM102_CUBICLE_DESCA,
			IM102_CORE_CODEA,
			IM102_DISTRIB_FRAME_CODEA,
			IM102_DEV_CODEB,
			IM102_DEV_NAMEB,
			IM102_DEV_DESCB,
//			IM102_BOARD_PORT_CODEB,
			IM102_BOARD_CODEB,
			IM102_PORT_CODEB,
			IM102_CUBICLE_CODEB,
			IM102_CUBICLE_DESCB,
			IM102_CORE_CODEB,
			IM102_DISTRIB_FRAME_CODEB
	};
	
	String IM103_DEV_TYPE = "保护型号";
	String IM103_MANUFACTUROR = "制造厂家";
	String IM103_DEV_MODEL = "保护型号";
	String IM103_CONFIG_VERSION= "ICD文件版本";
	String IM103_BOARD_CODE = "板卡编号";
	String IM103_BOARD_INDEX = "板卡序号";
	String IM103_BOARD_MODEL = "板卡型号";
	String IM103_BOARD_TYPE = "板卡类别/用途";
	String IM103_PORT_NUM = "端口数";
	String[] IM103_IED_BOARD_FIELDS = {
			IM103_DEV_TYPE,
			IM103_MANUFACTUROR,
			IM103_CONFIG_VERSION,
			IM103_BOARD_INDEX,
			IM103_BOARD_CODE,
			IM103_BOARD_MODEL,
			IM103_BOARD_TYPE,
			IM103_PORT_NUM,
	};
	
	String IM104_DEV_NAME = "装置Name";
	String IM104_DEV_DESC = "装置名称";
	String IM104_PIN_REF_ADDR = "开入虚端子参引";
	String IM104_PIN_DESC = "端子描述";
	String IM104_MMS_SIGNAL = "MMS信号参引";
	String IM104_SIGNAL_DESC = "信号描述";
	String[] IM104_STATUS_IN_FIELDS = {
			IM104_DEV_NAME,
			IM104_DEV_DESC,
			IM104_PIN_REF_ADDR,
			IM104_PIN_DESC,
			IM104_MMS_SIGNAL,
			IM104_SIGNAL_DESC
	};
	
	String IM105_DEV_NAME = "装置Name";
	String IM105_DEV_DESC = "装置名称";
	String IM105_ALARM_REF_ADDR = "告警信息参引";
	String IM105_ALARM_DESC = "告警信息描述";
	String IM105_BOARD_CODE = "板卡编号";
	String[] IM105_BOARD_WARN_FIELDS = {
			IM105_DEV_NAME,
			IM105_DEV_DESC,
			IM105_ALARM_REF_ADDR,
			IM105_ALARM_DESC,
			IM105_BOARD_CODE
	};
	
	String IM106_DEV_NAME = "装置Name";
	String IM106_DEV_DESC = "装置名称";
	String IM106_OPTICAL_REF_ADDR = "光强信息参引";
	String IM106_OPTICAL_DESC = "光强信息描述";
	String IM106_BOARD_CODE = "板卡编号";
	String IM106_PORT_CODE = "端口编号";
	String[] IM106_PORT_LIGHT_FIELDS = {
			IM106_DEV_NAME,
			IM106_DEV_DESC,
			IM106_OPTICAL_REF_ADDR,
			IM106_OPTICAL_DESC,
			IM106_BOARD_CODE,
			IM106_PORT_CODE
	};
	
	String IM107_DEV_NAME = "装置Name";
	String IM107_DEV_DESC = "装置名称";
	String IM107_STRAP_REF_ADDR = "压板参引";
	String IM107_STRAP_DESC = "压板描述";
	String IM107_STRAP_TYPE = "压板类型";
	String IM107_VP_REF_ADDR = "虚端子参引";
	String IM107_VP_DESC = "虚端子描述";
	String IM107_VP_TYPE = "虚端子类型(开入、开出)";
	String[] IM107_TER_STRAP_FIELDS = {
			IM107_DEV_NAME,
			IM107_DEV_DESC,
			IM107_STRAP_REF_ADDR,
			IM107_STRAP_DESC,
			IM107_STRAP_TYPE,
			IM107_VP_REF_ADDR,
			IM107_VP_DESC,
			IM107_VP_TYPE
	};
	
	String IM108_DEV_NAME = "装置Name";
	String IM108_DEV_DESC = "装置名称";
	String IM108_PIN_REF_ADDR = "开入虚端子参引";
	String IM108_PIN_DESC = "开入虚端子描述";
	String IM108_CMDACKVP_REF_ADDR = "命令确认虚端子参引";
	String IM108_CMDACKVP_DESC = "命令确认虚端子描述";
	String IM108_CMDOUTVP_REF_ADDR = "命令出口虚端子参引";
	String IM108_CMDOUTVP_DESC = "命令出口虚端子描述";
	String[] IM108_BRK_CFM_FIELDS = {
			IM108_DEV_NAME,
			IM108_DEV_DESC,
			IM108_PIN_REF_ADDR,
			IM108_PIN_DESC,
			IM108_CMDACKVP_REF_ADDR,
			IM108_CMDACKVP_DESC,
			IM108_CMDOUTVP_REF_ADDR,
			IM108_CMDOUTVP_DESC
	};

	String IM109_DEV_NAME = "装置Name";
	String IM109_DEV_DESC  = "装置名称";
	String IM109_REF_ADDR = "MMS参引";
	String IM109_DESCRIPTION = "MMS描述";
	String[] IM109_STA_INFO_FIELDS = {
			IM109_DEV_NAME,
			IM109_DEV_DESC,
			IM109_REF_ADDR,
			IM109_DESCRIPTION,
	};
	
	String PORT_TYPE_RX = "TX";//表示发送
	String PORT_TYPE_TX = "RX";//表示接收
	String PORT_TYPE_RT = "RT";//表示收发
}
