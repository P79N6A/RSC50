package com.synet.tool.rsc.ui.table;

import org.eclipse.swt.widgets.Display;

import com.shrcn.business.ui.NetPortUtil;
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.model.TableConfig;
import com.shrcn.found.ui.table.KTableDialogEditor;
import com.shrcn.found.ui.table.RKTableModel;
import com.synet.tool.rsc.dialog.BaySelectDialog;
import com.synet.tool.rsc.dialog.CableByCubicleADialog;
import com.synet.tool.rsc.dialog.CableByCubicleBDialog;
import com.synet.tool.rsc.dialog.ConvChk1Dialog;
import com.synet.tool.rsc.dialog.ConvChk2Dialog;
import com.synet.tool.rsc.dialog.CtvtChooseDialog;
import com.synet.tool.rsc.dialog.CubicleSelectDialog;
import com.synet.tool.rsc.dialog.IedChooseDialog;
import com.synet.tool.rsc.dialog.LightRefAddrDialog;
import com.synet.tool.rsc.dialog.PhyConnByPortADialog;
import com.synet.tool.rsc.dialog.PhyConnByPortBDialog;
import com.synet.tool.rsc.dialog.PinBaordEdtDialog;
import com.synet.tool.rsc.dialog.PoutBaordEdtDialog;
import com.synet.tool.rsc.dialog.RefAddrSelectDialog;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
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
			if(TableFactory.BOARD_PORT_TABLE.equals(tableName)) {
				if(col == 6) {
					KTableCellEditor editor = new KTableDialogEditor(RefAddrSelectDialog.class);
					return editor;
				}
			}
		}
		return super.getCellEditor(col, row);
	}
	
	@Override
	protected KTableCellEditor getCustomEditor(String editor) {
		
		if("BaySelectEditor".equals(editor)) {
			return new KTableDialogEditor(BaySelectDialog.class);
		} else if("CubicleSelectEditor".equals(editor)) {
			return new KTableDialogEditor(CubicleSelectDialog.class);
		} else if("lightRefAddrEditor".equals(editor)) {
			return new KTableDialogEditor(LightRefAddrDialog.class);
		} else if("PinBoardEditor".equals(editor)) {
			return new KTableDialogEditor(PinBaordEdtDialog.class);
		} else if("PoutBoardEditor".equals(editor)) {
			return new KTableDialogEditor(PoutBaordEdtDialog.class);
		} else if("ConvChk1Editor".equals(editor)) {
			return new KTableDialogEditor(ConvChk1Dialog.class);
		} else if("ConvChk2Editor".equals(editor)) {
			return new KTableDialogEditor(ConvChk2Dialog.class);
		}
		return super.getCustomEditor(editor);
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
		if (obj == null)
			return;
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
		if (TableFactory.REGION_LIST_TABLE.equals(tableName)) {
			Tb1049RegionEntity regionEntity = (Tb1049RegionEntity) obj;
			if (regionEntity.getF1049Name() != null) {
				Display.getCurrent().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						reloadPrj();
					}
				});
			}
		}
	}
	
	private void reloadPrj() {
		if (TableFactory.REGION_LIST_TABLE.equals(tableName)) {
			EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
		}
	}
	
}
