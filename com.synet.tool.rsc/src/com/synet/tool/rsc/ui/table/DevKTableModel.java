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
import com.synet.tool.rsc.dialog.CircuitsViewDialog;
import com.synet.tool.rsc.dialog.ConvChk1Dialog;
import com.synet.tool.rsc.dialog.ConvChk2Dialog;
import com.synet.tool.rsc.dialog.CtvtChooseDialog;
import com.synet.tool.rsc.dialog.CubicleSelectDialog;
import com.synet.tool.rsc.dialog.IedChooseDialog;
import com.synet.tool.rsc.dialog.LightRefAddrDialog;
import com.synet.tool.rsc.dialog.OnlyPinBaordEdtDialog;
import com.synet.tool.rsc.dialog.PhyConnByPortADialog;
import com.synet.tool.rsc.dialog.PhyConnByPortBDialog;
import com.synet.tool.rsc.dialog.PhyConnsViewDialog;
import com.synet.tool.rsc.dialog.PinBaordEdtDialog;
import com.synet.tool.rsc.dialog.PoutBaordEdtDialog;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.service.DefaultService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.util.RuleType;

import de.kupzog.ktable.KTableCellEditor;


public class DevKTableModel extends RKTableModel {
	
	private DefaultService defaultService;
	private MmsfcdaService mmsService;
	private PoutEntityService poutServ;
	
	public DevKTableModel(DevKTable table, TableConfig config) {
		super(table, config);
		defaultService = new DefaultService();
		mmsService = new MmsfcdaService();
		poutServ = new PoutEntityService();
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
		} else if("OnlyPinBoardEditor".equals(editor)) {
			return new KTableDialogEditor(OnlyPinBaordEdtDialog.class);
		} else if("PoutBoardEditor".equals(editor)) {
			return new KTableDialogEditor(PoutBaordEdtDialog.class);
		} else if("ConvChk1Editor".equals(editor)) {
			return new KTableDialogEditor(ConvChk1Dialog.class);
		} else if("ConvChk2Editor".equals(editor)) {
			return new KTableDialogEditor(ConvChk2Dialog.class);
		} else if("CircuitsViewEditor".equals(editor)) {
			return new KTableDialogEditor(CircuitsViewDialog.class);
		} else if("PhyConnsViewEditor".equals(editor)) {
			return new KTableDialogEditor(PhyConnsViewDialog.class);
		}
		return super.getCustomEditor(editor);
	}

	@Override
	protected void saveCellValue(Object data, String property) {
		if (!"overwrite".equals(property)) { // 更新
			saveData(data);
		}
		if (TableFactory.REGION_LIST_TABLE.equals(tableName)
				&& "f1049Name".equals(property)) { // 区域名称
			Tb1049RegionEntity regionEntity = (Tb1049RegionEntity) data;
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
	
	private boolean isStrapType(int type) {
		return RuleType.STRAP.include(type);
	}
	
	@Override
	protected void updateRelations(Object data, String property,
			Object oldValue, String newValue) {
		// 处理数据类型修改
		if (data instanceof Tb1058MmsfcdaEntity) {
			if ("f1058Type".equals(property)) {
				Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) data;
				int type = mmsfcdaEntity.getF1058Type();
				String dataCode = mmsfcdaEntity.getDataCode();
				int oldType = Integer.parseInt(oldValue.toString());
				if (isStrapType(type)) {
					mmsService.addStrap(mmsfcdaEntity);
				} else if (isStrapType(oldType)) {
					mmsService.removeStrap(mmsfcdaEntity);
				}
				if (SclUtil.isStData(dataCode)) {
					mmsService.updateStateF1011No(dataCode, type);
				} else if (SclUtil.isAlgData(dataCode)) {
					mmsService.updateAnalogF1011No(dataCode, type);
				}
			}
		} else if (data instanceof Tb1061PoutEntity) {
			if ("f1061Type".equals(property)) {
				Tb1061PoutEntity poutEntity = (Tb1061PoutEntity) data;
				String dataCode = poutEntity.getDataCode();
				int f1061Type = poutEntity.getF1061Type();
				if (SclUtil.isStData(dataCode)) {
					mmsService.updateStateF1011No(dataCode, f1061Type);
				} else if (SclUtil.isAlgData(dataCode)) {
					mmsService.updateAnalogF1011No(dataCode, f1061Type);
				}
				int oldType = Integer.parseInt(oldValue.toString());
				if (isStrapType(f1061Type)) {
					poutServ.addStrap(poutEntity);
				} else if (isStrapType(oldType)) {
					poutServ.removeStrap(poutEntity);
				}
			}
		} else if (data instanceof Tb1063CircuitEntity) {
			if ("tb1062PinByF1062CodePRecv.f1011No".equals(property)) {
				Tb1063CircuitEntity circuit = (Tb1063CircuitEntity) data;
				saveData(circuit.getTb1062PinByF1062CodePRecv());
			}
		}
	}
	
	/**
	 * 实时保存更改的数据
	 * @param obj
	 */
	private void saveData(Object obj) {
		if (obj != null) {
			defaultService.saveTableData(obj);
		}
	}
	
	private void reloadPrj() {
		if (TableFactory.REGION_LIST_TABLE.equals(tableName)) {
			EventManager.getDefault().notify(EventConstants.PROJECT_RELOAD, null);
		}
	}
	
}
