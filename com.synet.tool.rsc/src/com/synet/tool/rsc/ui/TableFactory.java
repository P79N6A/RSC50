package com.synet.tool.rsc.ui;

import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.TableBuilder;
import com.synet.tool.rsc.ui.table.DevKTable;


public class TableFactory {

	public static final String AREA_LIST_TABLE  			= "areaListTable";
	

	private static UIConfig uicfg = UIConfig.getInstance();

	public static DevKTable getAreaListTable(Composite container) {
		TableConfig tableCfg = uicfg.getDefinedTable(AREA_LIST_TABLE);
		return (DevKTable) TableBuilder.createKTable(DevKTable.class, container, tableCfg);
	}

}
