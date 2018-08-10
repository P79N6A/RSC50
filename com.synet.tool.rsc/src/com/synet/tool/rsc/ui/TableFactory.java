package com.synet.tool.rsc.ui;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.TableBuilder;
import com.synet.tool.rsc.ui.table.DevKTable;


public class TableFactory {

	public static final String AREA_LIST_TABLE  			= "areaListTable";
	public static final String INTERVAL_MSG_TABLE  			= "intervalMessageTable";
	public static final String TSF_SCD_TABLE  				= "tsfSecondaryTable";
	public static final String PROTECT_SAM_TABLE  			= "protectSampleTalbe";
	public static final String SWITCH_STATUS_TABLE  		= "switchStatusTable";
	public static final String SLUICE_STATUS_TABLE  		= "sluiceStatusTable";
	

	private static UIConfig uicfg = UIConfig.getInstance();
	
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
		TableConfig tableCfg = uicfg.getDefinedTable(AREA_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}
	
	public static DevKTable getIntervalMsgTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(INTERVAL_MSG_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}

}
