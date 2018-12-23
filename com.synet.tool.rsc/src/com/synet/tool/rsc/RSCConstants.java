package com.synet.tool.rsc;

import static java.io.File.separator;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.dict.DictManager;

public interface RSCConstants {
	
	String RSC_PRJ_NAME  	= "RscData";
	String USR_DIR 	 	    = System.getProperty("user.dir") + separator;
	String RULES_DIR	    = System.getProperty("user.dir") + separator + "rules" + separator;
	String RSC_HB_CFG   	= "com/synet/tool/rsc/das/hibernate.rsccfg.xml";
	String RSC_SQL      	= "com/synet/tool/rsc/das/rsc.sql";
	String EXCEL_COMM_EXPORT_2007  	= "com/synet/tool/rsc/common.xlsx";

	public static final String PLIN_CFG_FILE  = "com/sieyuan/tool/plin/plincfg.properties";
	
	//监听事件映射文件地址
	String EVENT_PATH		= "com/synet/tool/rsc/eventscfg.xml";
	String DICT_PATH 		= "com/synet/tool/rsc/rscdict.properties";
	
    String CFGURL 			= "com/synet/tool/rsc/util/rscnavg.xml";
    String RULEFILE			= "rules.xml";
    String RULEURL 			= "com/synet/tool/rsc/util/" + RULEFILE;
    String FUNCFILE			= Constants.cfgDir + separator + "func.xml";
    String CURR_SCD			= "current.scd";
    String CURR_SSD			= "current.ssd";

    String ET_PR_MDL		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.PrimaryModelEditor";
    String ET_PR_BAY		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.PrimaryBayEditor";
    
     String ET_PT_BAY		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.ProtectBaylEditor";
     String ET_PT_BAY_PUB	= "com.synet.tool.rsc/com.synet.tool.rsc.editor.ProtectBayPubEditor";
     String ET_PT_IED		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.ProtectIEDlEditor";
     String ET_PT_PBAY		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.ProtectPubBaylEditor";

     String ET_PY_MDL		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.PhysicalModelEditor";
     String ET_PY_AREA		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.PhysicalAreaEditor";

     String ET_ICD_MDL		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.ICDModelEditor";

     String ET_SEC_FIB		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.SecFibreEditor";
     String ET_SEC_LCK		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.SecLockEditor";
     String ET_SEC_PRO		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.SecProBrkEditor";
     String ET_SEC_PWR		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.SecPwrBrkEditor";

     String ET_IMP_IED		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpIEDListEditor";
     String ET_IMP_FIB		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpFibreListEditor";
     String ET_IMP_BRD		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpIEDBoardEditor";
     String ET_IMP_ST		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpStatusInEditor";
     String ET_IMP_WRN		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpBoardWarnEditor";
     String ET_IMP_PORT		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpPortLightEditor";
     String ET_IMP_STRAP	= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpTerStrapEditor";
     String ET_IMP_BRK		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpBrkCfmEditor";
     String ET_IMP_STA		= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpStaInfoEditor";
     String ET_IMP_LINKW	= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpLinkWarnEditor";
     String ET_IMP_FIBNew	= "com.synet.tool.rsc/com.synet.tool.rsc.editor.imp.ImpFibreNewListEditor";
	
     DictManager dictMgr = DictManager.getInstance();
     
     String DEV_TYPE_ALL		= "全部装置";
     
     String DEV_TYPE_PRO		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_PROT); //"保护装置";
     String DEV_TYPE_UNIT		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_MU); //"合并单元";
     String DEV_TYPE_TER		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_TERM); //"智能终端";
     //测控装置
     String DEV_TYPE_MOT		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_MONI); //"测控装置";
     //交换机，采集单元，光配架
     String DEV_TYPE_SWC		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_JHJ); //"交换机";
     String DEV_TYPE_GAT		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_CJQ); //"采集单元";
     String DEV_TYPE_ODF		= dictMgr.getNameById("IED_TYPE", DBConstants.IED_GP); //"光配架";
     
     String DEV_TYPE_UNIT_TER	= dictMgr.getNameById("IED_TYPE", DBConstants.IED_MT); //"合并智能终端";
     
     int OTHERS_ID				= 9999;
     String OTHERS_NAME			= "其它";
     String DICT_IED_EVT		= "DICT_IED_EVT";	//保护动作
     String DICT_IED_MEAS		= "DICT_IED_MEAS";	//保护测量量
     String DICT_IED_STRIP		= "DICT_IED_STRIP";	//保护压板
     String DICT_IED_WARN		= "DICT_IED_WARN";	//装置告警
     String DICT_IED_STATE		= "DICT_IED_STATE";	//运行工况
     String DICT_IED_YX			= "DICT_IED_YX";	//运行工况
     String DICT_IED_POUT		= "DICT_IED_POUT";	//开出虚端子
     String DICT_IED_PIN		= "DICT_IED_PIN";	//开入虚端子

     String SEARCH				= "查询";
     String DESCRIPTION			= "描述";
    
     String TSF_SCDRAY        	= "互感器次级";
     String PROTCT_SAMP       	= "保护采样值";
     String SWICH_STATES      	= "开关刀闸状态";
   
     String BOARD_PORT 			= "板卡端口";
     String PROTECT_MSG 		= "保护信息";
     String RUN_STATE 			= "运行工况";
     String DEV_YX		 		= "装置遥信";
     String DEV_QT 				= "其他信号";
     String DEV_WARNING 		= "装置告警";
     String CIRCUI_BOARD 		= "虚端子压板";
     String LOGICAL_LINK 		= "逻辑链路";
     String PROTECT_WAVE 		= "保护录波";

     String PROTECT_VALUE 		= "保护定值";
     String PROTECT_PARAM 		= "保护参数";
     String PROTECT_BOARD 		= "保护压板";
     String PROTECT_ACTION 		= "保护动作";
     String PROTECT_MEAQU 		= "保护测量量";
   
     String TAB_CUBICLE       	= "屏柜";
     String TAB_CABLE      		= "光缆";
     String TAB_PHYSCONN      	= "物理回路";
   
   //树节点名称
     String TREE_PHYAICAMODEL 	= "物理信息模型";
   
}
