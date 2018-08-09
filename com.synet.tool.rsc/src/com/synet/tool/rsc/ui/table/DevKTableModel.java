package com.synet.tool.rsc.ui.table;

import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.RKTableModel;


public class DevKTableModel extends RKTableModel {
	
	
	public DevKTableModel(DevKTable table, TableConfig config) {
		super(table, config);
	}
	
	@Override
	protected void saveCellValue(Object data, String property) {
	}


	protected int getSize(String editor) {
		int[] limit = NetPortUtil.getNetLimit(editor);
		return limit[0];
	}
	
}
