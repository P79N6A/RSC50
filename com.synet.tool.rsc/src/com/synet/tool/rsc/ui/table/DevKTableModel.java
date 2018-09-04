package com.synet.tool.rsc.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.KTableDialogEditor;
import com.shrcn.found.ui.table.RKTableModel;
import com.synet.tool.rsc.dialog.CableByCubicleADialog;
import com.synet.tool.rsc.dialog.CableByCubicleBDialog;
import com.synet.tool.rsc.dialog.CtvtChooseDialog;
import com.synet.tool.rsc.dialog.IedChooseDialog;
import com.synet.tool.rsc.dialog.PhyConnByPortADialog;
import com.synet.tool.rsc.dialog.PhyConnByPortBDialog;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.DataUtils;

import de.kupzog.ktable.KTableCellEditor;


public class DevKTableModel extends RKTableModel {
	
	private DefaultService defaultService;
	private Map<String, List<String>> dictMap;
	
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
		if (row > 0) {
			if (TableFactory.LINEPORTFIBER_TABLE.equals(tableName) 
					|| TableFactory.IOTERM_TABLE.equals(tableName) 
					|| TableFactory.POWERKK_TABLE.equals(tableName)) {
				if (col == 2) {
					KTableCellEditor editor = new KTableDialogEditor(IedChooseDialog.class);
					return editor;
				}
			} 
			if (TableFactory.VOLTAGEKK_TABLE.equals(tableName)) {
				if (col == 2) {
					KTableCellEditor editor = new KTableDialogEditor(CtvtChooseDialog.class);
					return editor;
				}
			}
			if (TableFactory.CABLE_TABLE.equals(tableName)) {
				if (col == 6) {
					KTableCellEditor editor = new KTableDialogEditor(CableByCubicleADialog.class);
					return editor;
				} else if (col == 8) {
					KTableCellEditor editor = new KTableDialogEditor(CableByCubicleBDialog.class);
					return editor;
				}
			}
			if (TableFactory.PHYSCONNE_TABLE.equals(tableName)) {
				if (col == 5) {
					KTableCellEditor editor = new KTableDialogEditor(PhyConnByPortADialog.class);
					return editor;
				} else if (col == 9) {
					KTableCellEditor editor = new KTableDialogEditor(PhyConnByPortBDialog.class);
					return editor;
				}
			}
			if(TableFactory.INTERVAL_MSG_TABLE.equals(tableName)) {
				initTableDict(row);
			}
		}
		return super.getCellEditor(col, row);
	}

	private void initTableDict(int row) {
		if(dictMap == null) {
			dictMap = new HashMap<>();
		}
		Tb1063CircuitEntity circuit = (Tb1063CircuitEntity) getItem(row);
		Tb1046IedEntity iedSend = circuit.getTb1046IedByF1046CodeIedSend();
		if(!dictMap.containsKey(iedSend.getF1046Name())) {
			PoutEntityService service = new PoutEntityService();
			List<Tb1061PoutEntity> poutEntitys = service.getPoutEntityByProperties(iedSend, null);
			List<String> poutDescs = new ArrayList<>();
			for (Tb1061PoutEntity entity : poutEntitys) {
				poutDescs.add(entity.getF1061Desc());
			}
			dictMap.put(iedSend.getF1046Name(), poutDescs);
			DictManager dict = DictManager.getInstance();
			dict.removeDict("CONVCHK");
			dict.addDict("CONVCHK", "CONVCHK", DataUtils.createDictItems(poutDescs));
		}
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
				|| TableFactory.VOLTAGEKK_TABLE.equals(tableName) 
				|| TableFactory.REGION_LIST_TABLE.equals(tableName) 
				|| TableFactory.CUBICLE_TABLE.equals(tableName) 
				|| TableFactory.CABLE_TABLE.equals(tableName) 
				|| TableFactory.PHYSCONNE_TABLE.equals(tableName)) {
			defaultService.saveTableData(obj);
		}
	}
	
	
	
}
