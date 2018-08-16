package com.synet.tool.rsc.ui.table;

import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.KTableDialogEditor;
import com.shrcn.found.ui.table.RKTableModel;
import com.synet.tool.rsc.dialog.CtvtChooseDialog;
import com.synet.tool.rsc.dialog.IedChooseDialog;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.ui.TableFactory;

import de.kupzog.ktable.KTableCellEditor;


public class DevKTableModel extends RKTableModel {
	
	private DefaultService defaultService;
	
	public DevKTableModel(DevKTable table, TableConfig config) {
		super(table, config);
		defaultService = new DefaultService();
	}
	
	@Override
	protected void saveCellValue(Object data, String property) {
	}


	protected int getSize(String editor) {
		int[] limit = NetPortUtil.getNetLimit(editor);
		return limit[0];
	}
	
	@Override
	public KTableCellEditor getCellEditor(int col, int row) {
		if (TableFactory.LINEPORTFIBER_TABLE.equals(tableName) 
				|| TableFactory.IOTERM_TABLE.equals(tableName) 
				|| TableFactory.POWERKK_TABLE.equals(tableName)) {
			if (col == 2 && row > 0) {
				KTableCellEditor editor = new KTableDialogEditor(IedChooseDialog.class);
				return editor;
			}
		} 
		if (TableFactory.VOLTAGEKK_TABLE.equals(tableName)) {
			if (col == 2 && row > 0) {
				KTableCellEditor editor = new KTableDialogEditor(CtvtChooseDialog.class);
				return editor;
			}
		}
		return super.getCellEditor(col, row);
	}
	
	@Override
	public void doSetContentAt(int col, int row, Object value) {
		super.doSetContentAt(col, row, value);
		Object obj = getItem(row);
		saveData(obj);
	}

	/**
	 * 实时保存更改的数据
	 * @param obj
	 */
	private void saveData(Object obj) {
		if (obj == null) return;
		if (TableFactory.LINEPORTFIBER_TABLE.equals(tableName) 
				|| TableFactory.IOTERM_TABLE.equals(tableName)
				|| TableFactory.POWERKK_TABLE.equals(tableName)
				|| TableFactory.VOLTAGEKK_TABLE.equals(tableName)) {
			defaultService.saveTableData(obj);
		}
	}
	
}
