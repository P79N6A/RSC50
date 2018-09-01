package com.synet.tool.rsc;

public interface ExcelConstants {

	String IED_LIST = "设备台账";
	String FIBRE_LIST ="光缆清册";
	String IED_BOARD = "装置板卡端口描述";
	String STATUS_IN = "开入信号映射表";
	String BOARD_WARN = "告警与板卡关联表";
	String PORT_LIGHT = "光强与端口关联表";
	String TER_STRAP = "压板与虚端子关联表";
	String BRK_CFM = "跳合闸反校关联表";
	String STA_INFO = "监控信息点表";
	
	String[] IED_LIST_FIELDS = {"装置Name","装置名称","所属间隔","所属屏柜","生产厂家","装置型号","装置版本号","A/B套",
			"保护分类(国产、进口)","保护型号","保护类型"};
	String[] FIBRE_LIST_FIELDS = {"光缆编号","纤芯编号","端口A装置代码","端口A装置Name","端口A装置名称","端口A板卡编号",
			"端口A端口编号","端口A屏柜代码","端口A屏柜名称","端口A芯线编号","端口A光配架端口编号","端口B装置代码",
			"端口B装置Name","端口B装置名称","端口B板卡编号","端口B端口编号","端口B屏柜代码","端口B屏柜名称",
			"端口B芯线编号","端口B光配架端口编号"};
	String[] IED_BOARD_FIELDS = {"装置Name","装置名称","板卡编号","端口编号"};
	String[] STATUS_IN_FIELDS = {"装置Name","开入虚端子参引","端子描述","MMS信号参引","信号描述"};
	String[] BOARD_WARN_FIELDS = {"装置Name","告警信息参引","告警信息描述","板卡编号"};;
	String[] PORT_LIGHT_FIELDS = {"装置Name","光强信息参引","光强信息描述","板卡编号","端口编号"};
	String[] TER_STRAP_FIELDS = {"装置Name","压板参引","压板描述","压板类型","虚端子参引","虚端子描述","虚端子类型(开入、开出)"};
	String[] BRK_CFM_FIELDS = {"装置Name","开入虚端子参引","开入虚端子描述","命令确认虚端子参引","命令确认虚端子描述",
			"命令出口虚端子参引","命令出口虚端子描述"};
	String[] STA_INFO_FIELDS = {"装置Name","装置名称","板卡编号","端口编号"};
}
