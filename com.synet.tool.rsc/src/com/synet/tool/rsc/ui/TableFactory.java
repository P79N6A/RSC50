package com.synet.tool.rsc.ui;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.TableBuilder;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.ui.table.IEDBoardTable;
import com.synet.tool.rsc.ui.table.RuleTable;


public class TableFactory {

	public static final String INTERVAL_MSG_TABLE  			= "IntervalMessageTable"; // 间隔
	public static final String TSF_SCD_TABLE  				= "TsfSecondaryTable"; // 次级
	public static final String PROTECT_SAM_TABLE  			= "ProtectSampleTalbe"; // 采样值
	public static final String SWITCH_STATUS_TABLE  		= "SwitchStatusTable"; // 开关状态-开关
	public static final String SLUICE_STATUS_TABLE  		= "SluiceStatusTable"; // 开关状态-状态
	public static final String REGION_LIST_TABLE  			= "RegionListTable"; // 区域
	public static final String CUBICLE_TABLE                = "CubicleTable"; 	// 屏柜
	public static final String CABLE_TABLE                  = "CableTable";		// 光缆
	public static final String PHYSCONNE_TABLE              = "PhysconnTable";	// 物理回路
	public static final String LINEPORTFIBER_TABLE			= "LineProtFiberTable";
	public static final String IOTERM_TABLE 				= "IOTermTable";
	public static final String POWERKK_TABLE 				= "PowerKKTable";
	public static final String VOLTAGEKK_TABLE 				= "VoltageKKTable";
	public static final String ICD_TABLE 					= "IcdTable";
	public static final String IEDACCOUNT_TABLE 			= "IEDAccountTable";
	public static final String CTVT_POUT_TABLE 				= "CtvtPoutTable";
	public static final String POUT_TABLE 					= "PoutTable";	// 发送虚端子
	public static final String ANALOG_TABLE 				= "AnalogTable"; // 测量量
	public static final String PROT_ANALOG_TABLE 			= "ProtAnalogTable"; // 保护测量量
	public static final String PROT_INTERVAL_TABLE 			= "ProtectIntervalTable"; // 装置列表
	public static final String IED_CHOOSE_TABLE             = "IedChooseTable";
	public static final String CTVT_CHOOSE_TABLE            = "CtvtChooseTable";
	public static final String GATHER_TABLE 				= "GatherTable";
	public static final String BOARD_PORT_TABLE 			= "BoardPortTable";
	public static final String PROTECT_PORT_TABLE 			= "ProtectValueTable"; // 保护定值
	public static final String PROTECT_PARAM_TABLE 			= "ProtectParamTable"; // 保护参数
	public static final String PROTECT_BOARD_TABLE 			= "ProtectBoardTable"; // 保护压板
	public static final String PROTECT_ACTION_TABLE 		= "ProtectActionTable"; // 保护动作
	public static final String PROTECT_QUANT_TABLE 			= "ProtectMeaQuantityTable"; // 保护测量量
	public static final String DEVICE_WARN_TABLE 			= "DeviceWarnTable";
	public static final String RUN_STATE_TABLE 				= "RunStateTable";
	public static final String VIRTER_OUT_TABLE 			= "VirtualTerminalOutTable";
	public static final String VIRTER_IN_TABLE				= "VirtualTerminalInTable";
	public static final String LOGICAL_LINK_TABLE 			= "LogicalLinkTable";	// 逻辑链路
	public static final String ANALOG_CHN_TABLE 			= "AnalogChnTable";
	public static final String CRITER_CHN_TABLE 			= "CriteriaChnTable";
	
	public static final String DEVICE_NAME_TABLE 			= "DeviceNameTable";
	public static final String BOARD_NAME_TABLE 			= "BoardNameTable";
	public static final String LOGLINK_NAME_TABLE 			= "LogicalLinkNameTable";
	
	public static String IED_LIST_TABLE                     = "IEDListTable";
	public static String IED_COMP_LIST_TABLE                = "IEDCompListTable";
	public static String BOARD_WARN_TABLE                   = "BoardWarnTableTable";
	public static String FIBRE_LIST_TABLE                   = "FibreListTable";
	public static String IED_BOARD_TABLE                    = "IEDBoardTable";
	public static String STATUS_IN_TABLE                    = "StatusInTable";
	public static String STA_INFO_TABLE                     = "StaInfoTable";
	public static String PORT_LIGHT_YABLE                   = "PortLightTable";
	public static String TER_STRAP_TABLE                    = "TerStrapTable";
	public static String BRK_CFM_TABLE                      = "BrkCfmTable";
	
	public static String SUBSTATION_TABLE			    	= "SubstationTable";
	public static String PORT_TABLE							= "PortTable";
	public static String MMS_FCDA_TABLE						= "MmsFcdaTable";
	public static final String PROBLEMS		  				= "problems";
	public static final String MODEL_RULE_TABLE		  		= "ModelRuleTable";
	
	private static UIConfig uicfg = UIConfig.getInstance();
	
	public static RuleTable getModelRuleTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(MODEL_RULE_TABLE);
		return (RuleTable) TableBuilder.createKTable(RuleTable.class, container, tableCfg);
	}
	
	public static DevKTable getDeviceNameTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(DEVICE_NAME_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getMmsFcdaTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(MMS_FCDA_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getBoardNameTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(BOARD_NAME_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getLogicalLinkNameTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(LOGLINK_NAME_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getDeviceWarnTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(DEVICE_WARN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getRunStateTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(RUN_STATE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getVirtualTerminalOutTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(VIRTER_OUT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getVirtualTerminalInTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(VIRTER_IN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getLogicalLinkTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(LOGICAL_LINK_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getAnalogChnTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(ANALOG_CHN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getCriteriaChnTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(CRITER_CHN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	public static DevKTable getProtectValueTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_PORT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectParamTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_PARAM_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectBoardTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_BOARD_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectActionTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_ACTION_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectMeaQuantityTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_QUANT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getBoardPortTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(BOARD_PORT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getGatherTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(GATHER_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectIntervalTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROT_INTERVAL_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getAnalogTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(ANALOG_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtAnalogTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROT_ANALOG_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getCtvtPoutTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(CTVT_POUT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getPoutTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(POUT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getTsfSecondaryTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(TSF_SCD_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getProtectSampleTalbe(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PROTECT_SAM_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getSwitchStatusTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(SWITCH_STATUS_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getSluiceStatusTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(SLUICE_STATUS_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getAreaListTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(REGION_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getIntervalMsgTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(INTERVAL_MSG_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getCubicleTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(CUBICLE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getCableTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(CABLE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getPhysconnTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PHYSCONNE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}

	public static DevKTable getLineProtFiberTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(LINEPORTFIBER_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	public static DevKTable getIOTermTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IOTERM_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	public static DevKTable getPowerKKTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(POWERKK_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	public static DevKTable getVoltageKKTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(VOLTAGEKK_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	public static DevKTable getIcdTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(ICD_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}

	public static DevKTable getIEDChooseTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IED_CHOOSE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getCtvtChooseTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(CTVT_CHOOSE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getIEDListTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IED_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getIEDCompListTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IED_COMP_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getBoardWarnTableTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(BOARD_WARN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getFibreListTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(FIBRE_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getIEDBoardTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IED_BOARD_TABLE);
		return (DevKTable) TableBuilder.createKTable(IEDBoardTable.class, container, tableCfg);
	}
	
	public static DevKTable getStatusInTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(STATUS_IN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getStaInfoTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(STA_INFO_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getPortLightTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PORT_LIGHT_YABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getTerStrapTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(TER_STRAP_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getBrkCfmTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(BRK_CFM_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getSubstationTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(SUBSTATION_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getPortTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(PORT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
}
