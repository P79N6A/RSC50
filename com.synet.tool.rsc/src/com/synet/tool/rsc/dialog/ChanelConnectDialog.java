package com.synet.tool.rsc.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1074SVCTVTRelationEntity;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.SVCTVTRelationEntityService;
import com.synet.tool.rsc.service.SvcbEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

/**
 * 通道关联
 * @author Administrator
 *
 */
public class ChanelConnectDialog extends WrappedDialog{

	private DevKTable tableState;
	private DevKTable tableChanel;
	private Button btnAdd;
	private String curEntryName;
	private String[] comboDevItems;
	private Tb1067CtvtsecondaryEntity curSel;
	private Combo comboDevice;
	private Button chBay;
	private int preComboDevSelIdx = 0;
	private List<Tb1046IedEntity> iedEntities;
	private List<Tb1056SvcbEntity> svcbEntities;
	private Composite comRight;
	private Button btnDel;
	private SvcbEntityService svcbService;
	private IedEntityService iedService;
	private PoutEntityService poutEntityService;
	private List<Tb1061PoutEntity> selectedPoutList;

	public ChanelConnectDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public ChanelConnectDialog(Shell parentShell, String curEntryName, 
			Tb1067CtvtsecondaryEntity ctvtsecondaryEntity) {
		super(parentShell);
		this.curEntryName = curEntryName;
		this.curSel = ctvtsecondaryEntity;
		
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent) {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		//左侧
		Composite comLeft = SwtUtil.createComposite(composite, gridData, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		GridData chlData = new GridData(SWT.CENTER, SWT.FILL, false, true);
		chlData.widthHint = 730;
		comLeft.setLayoutData(chlData);
		GridData gdlb_2 = new GridData(200,25);
		gdlb_2.horizontalSpan = 2;
		String switchLbName = curEntryName + "互感器次级：";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		tableChanel = TableFactory.getCtvtPoutTable(comLeft);
		tableChanel.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		GridData gridBtnCom = new GridData(41, SWT.DEFAULT);
		Composite comBtn = SwtUtil.createComposite(comLeft, gridBtnCom, 1);
		comBtn.setLayout(SwtUtil.getGridLayout(1));
		btnAdd = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		btnDel = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "->");
		//右侧
		comRight = SwtUtil.createComposite(composite, gridData, 2);
		comRight.setLayout(SwtUtil.getGridLayout(2));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 150;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		chBay = SwtUtil.createCheckBox(comRight, "当前间隔", null);
		
		GridData gdSpan_2 = new GridData(SWT.CENTER, SWT.FILL, false, true);
		gdSpan_2.widthHint = 450;
		gdSpan_2.horizontalSpan = 2;
		tableState = TableFactory.getPoutTable(comRight);
		tableState.getTable().setLayoutData(gdSpan_2);
		addListeners();
		initData();
		return composite;
	}
	
	private void initData() {
		selectedPoutList = new ArrayList<>();
		iedService = new IedEntityService();
		svcbService = new SvcbEntityService();
		poutEntityService = new PoutEntityService();
		
		for (Tb1074SVCTVTRelationEntity relation : curSel.getSvRelations()) {
			selectedPoutList.add(relation.getF1061Code());
		}
		tableChanel.setInput(selectedPoutList);
		
		chBay.setSelection(true);
		loadIeds(true);
	}
	
	private void loadIeds(boolean useBay) {
		int[] devTypes = EnumIedType.UNIT_DEVICE.getTypes();
		Tb1042BayEntity bay = useBay ? curSel.getTb1043EquipmentByF1043Code().getTb1042BayByF1042Code() : null;
		iedEntities = iedService.getIedByTypesAndBay(devTypes, bay);
		if(iedEntities.size() < 1) {
			comboDevItems = new String[]{"装置为空"};
		} else {
			List<String> lstComboDevItem = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : iedEntities) {
				lstComboDevItem.add(tb1046IedEntity.getF1046Name());
			}
			comboDevItems = new String[lstComboDevItem.size()];
			comboDevItems = lstComboDevItem.toArray(comboDevItems);
		}
		comboDevice.setItems(comboDevItems);
		comboDevice.select(0);
		initTableData();
	}


	/**
	 * 初始化表格数据
	 */
	private void initTableData() {
		if(DataUtils.listNotNull(iedEntities)) {
			List<Tb1061PoutEntity> leftTableData = (List<Tb1061PoutEntity>) tableChanel.getInput();
			Tb1046IedEntity iedEntity = null;
			if (leftTableData != null && leftTableData.size() > 0) {
				iedEntity = leftTableData.get(0).getTb1046IedByF1046Code();
			} else {
				iedEntity = iedEntities.get(0);
			}
			comboDevice.setText(iedEntity.getF1046Name());
			refreshTableState(iedEntity);
		}
	}
	

	private void refreshTableState(Tb1046IedEntity iedEntity) {
		svcbEntities = svcbService.getSvcbEntityByIedEntity(iedEntity);
		List<Tb1061PoutEntity> allPouts = new ArrayList<>();
		for (Tb1056SvcbEntity tb1056SvcbEntity : svcbEntities) {
			List<Tb1061PoutEntity> pouts = poutEntityService.getPoutEntityByCb(tb1056SvcbEntity);
			for (Tb1061PoutEntity pout : pouts) {
				List<Tb1061PoutEntity> leftTableData = (List<Tb1061PoutEntity>) tableChanel.getInput();
				if(leftTableData.contains(pout)) {
					pout.setSelected(true);
				}
				allPouts.add(pout);
			}
		}
		tableState.setInput(allPouts);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器通道关联");
	}
	
	private void addListeners() {
		chBay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadIeds(chBay.getSelection());
			}
		});
		SelectionListener selectionListener = new SelectionAdapter() {
			private Tb1046IedEntity curSelIedEntity;
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == btnAdd) {//效率较低
					selectedPoutList.clear();
					List<Tb1061PoutEntity> rigthTableData = (List<Tb1061PoutEntity>) tableState.getInput();
					for (Tb1061PoutEntity tb1061PoutEntity : rigthTableData) {
						if(tb1061PoutEntity.isSelected()) {
							selectedPoutList.add(tb1061PoutEntity);
						}
					}
					List<Tb1061PoutEntity> leftTableData = (List<Tb1061PoutEntity>) tableChanel.getInput();
					for (Tb1061PoutEntity tb1061PoutEntity : selectedPoutList) {
						if(!leftTableData.contains(tb1061PoutEntity)) {
							tableChanel.addRow(tb1061PoutEntity);
						}
					}
					tableChanel.refresh();
				} else if(obj == btnDel) {
					List<Object> selections = tableChanel.getSelections();
					List<Tb1061PoutEntity> rigthTableData = (List<Tb1061PoutEntity>) tableState.getInput();
					for (Tb1061PoutEntity tb1061PoutEntity : rigthTableData) {
						if(selections.contains(tb1061PoutEntity)) {
							tb1061PoutEntity.setSelected(false);
						}
					}
					tableChanel.removeSelected();
					tableChanel.refresh();
					tableState.refresh();
				} else if(obj == comboDevice) {
					int curComboDevSelIdx = comboDevice.getSelectionIndex();
					if(curComboDevSelIdx == preComboDevSelIdx) {
						return;
					}
					preComboDevSelIdx = curComboDevSelIdx;
					String selDevName = comboDevItems[curComboDevSelIdx];
					curSelIedEntity = getIedEntityByName(selDevName);
					refreshTableState(curSelIedEntity);
					comboDevice.redraw();
				}
			}

		};
		btnDel.addSelectionListener(selectionListener);
		btnAdd.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
	}
	
	private Tb1046IedEntity getIedEntityByName(String iedName) {
		for (Tb1046IedEntity iedEntity : iedEntities) {
			if(iedEntity.getF1046Name().equals(iedName)) {
				return iedEntity;
			}
		}
		return null;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", true);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(1200, 650);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID){
			SVCTVTRelationEntityService service = new SVCTVTRelationEntityService();
			List<Tb1061PoutEntity> input = (List<Tb1061PoutEntity>) tableChanel.getInput();
			service.savePinOuts(curSel, input);
		}
		super.buttonPressed(buttonId);
	}

}
