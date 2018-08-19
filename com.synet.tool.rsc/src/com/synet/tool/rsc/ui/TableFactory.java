package com.synet.tool.rsc.ui;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.TableBuilder;
import com.synet.tool.rsc.ui.table.DevKTable;


public class TableFactory {

	public static final String REGION_LIST_TABLE  			= "RegionListTable";
	public static final String INTERVAL_MSG_TABLE  			= "IntervalMessageTable";
	public static final String TSF_SCD_TABLE  				= "TsfSecondaryTable";
	public static final String PROTECT_SAM_TABLE  			= "ProtectSampleTalbe";
	public static final String SWITCH_STATUS_TABLE  		= "SwitchStatusTable";
	public static final String SLUICE_STATUS_TABLE  		= "SluiceStatusTable";
	public static final String CUBICLE_TABLE                = "CubicleTable";
	public static final String CABLE_TABLE                  = "CableTable";
	public static final String PHYSCONNE_TABLE              = "PhysconnTable";		
	public static String LINEPORTFIBER_TABLE				= "LineProtFiberTable";
	public static String IOTERM_TABLE 						= "IOTermTable";
	public static String POWERKK_TABLE 						= "PowerKKTable";
	public static String VOLTAGEKK_TABLE 					= "VoltageKKTable";
	public static String ICD_TABLE 							= "IcdTable";
	public static String POUT_TABLE 						= "PoutTable";
	public static String ANALOG_TABLE 						= "AnalogTable";
	public static String PROT_ANALOG_TABLE 					= "ProtAnalogTable";
	public static String PROT_INTERVAL_TABLE 				= "ProtectIntervalTable";
	public static String IED_CHOOSE_TABLE                   = "IedChooseTable";
	public static String CTVT_CHOOSE_TABLE              	= "CtvtChooseTable";
	public static String GATHER_TABLE 						= "GatherTable";
	public static String BOARD_PORT_TABLE 					= "BoardPortTable";
	public static String PROTECT_PORT_TABLE 				= "ProtectValueTable";
	public static String PROTECT_PARAM_TABLE 				= "ProtectParamTable";
	public static String PROTECT_BOARD_TABLE 				= "ProtectBoardTable";
	public static String PROTECT_ACTION_TABLE 				= "ProtectActionTable";
	public static String PROTECT_QUANT_TABLE 				= "ProtectMeaQuantityTable";
	public static String DEVICE_WARN_TABLE 					= "DeviceWarnTable";
	public static String RUN_STATE_TABLE 					= "RunStateTable";
	public static String VIRTER_IN_TABLE 					= "VirtualTerminalOutTable";
	public static String VIRTER_OUT_TABLE 					= "VirtualTerminalInTable";
	public static String LOGICAL_LINK_TABLE 				= "LogicalLinkTable";
	public static String ANALOG_CHN_TABLE 					= "AnalogChnTable";
	public static String CRITER_CHN_TABLE 					= "CriteriaChnTable";
	public static String CTVT_POUT_TABLE 					= "CtvtPoutTable";
	private static String IED_LIST_TABLE                    = "IEDListTable";
	private static String BOARD_WARN_TABLE                  = "BoardWarnTableTable";
	private static String FIBRE_LIST_TABLE                  = "FibreListTable";
	private static String IED_BOARD_TABLE                   = "IEDBoardTable";
	private static String STATUS_IN_TABLE                   = "StatusInTable";
	private static String STA_INFO_TABLE                    = "StaInfoTable";
	private static String PORT_LIGHT_YABLE                  = "PortLightTable";
	private static String TER_STRAP_TABLE                   = "TerStrapTable";
	private static String BRK_CFM_TABLE                     = "BrkCfmTable";
	
	private static UIConfig uicfg = UIConfig.getInstance();
	
	
	public static DevKTable getDeviceWarnTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(DEVICE_WARN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getRunStateTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(RUN_STATE_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getVirtualTerminalOutTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(VIRTER_IN_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getVirtualTerminalInTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(VIRTER_OUT_TABLE);
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
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
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
}
