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
	public static String IEDACCOUNT_TABLE 					= "IEDAccountTable";
	public static String CTVT_POUT_TABLE 					= "CtvtPoutTable";
	public static String POUT_TABLE 						= "PoutTable";
	public static String ANALOG_TABLE 						= "AnalogTable";
	public static String PROT_ANALOG_TABLE 					= "ProtAnalogTable";
	public static String PROT_INTERVAL_TABLE 					= "ProtectIntervalTable";

	private static UIConfig uicfg = UIConfig.getInstance();
	
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
	public static DevKTable getIEDAccountTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(IEDACCOUNT_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}

}
