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
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
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
	private int preComboDevSelIdx = 0;
	private List<Tb1046IedEntity> iedEntities;
	private SvcbEntityService svcbService;
	private List<Tb1056SvcbEntity> svcbEntities;
	private Composite comRight;
	private List<Tb1061PoutEntity> chanelTableData;
	private IedEntityService iedService;
	private ArrayList<Object> tableChnData;

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
		initComboData();
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite composite = SwtUtil.createComposite(parent, gridData, 1);
		composite.setLayout(SwtUtil.getGridLayout(2));
		//左侧
		Composite comLeft = SwtUtil.createComposite(composite, gridData, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
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
		//右侧
		comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(2));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		comboDevice.setItems(comboDevItems);
		comboDevice.select(0);
		
		GridData gdSpan_2 = new GridData(GridData.FILL_BOTH);
		gdSpan_2.horizontalSpan = 2;
		tableState = TableFactory.getPoutTable(comRight);
		tableState.getTable().setLayoutData(gdSpan_2);
		addListeners();
		initTableData();
		return composite;
	}
	
	private void initComboData() {
		iedService = new IedEntityService();
		svcbService = new SvcbEntityService();
		int[] devTypes = EnumIedType.values()[2].getTypes();
		Tb1042BayEntity bay = curSel.getTb1043EquipmentByF1043Code().getTb1042BayByF1042Code();
		iedEntities = iedService.getIedByTypesAndBay(devTypes, bay);
		if(iedEntities.size() < 1) {
			comboDevItems = new String[]{"装置为空"};
		} else {
			List<String> lstComboDevItem = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : iedEntities) {
				lstComboDevItem.add(tb1046IedEntity.getF1046Desc());
			}
			comboDevItems = new String[lstComboDevItem.size()];
			comboDevItems = lstComboDevItem.toArray(comboDevItems);
		}
	}


	/**
	 * 初始化表格数据
	 */
	private void initTableData() {
		tableChnData = new ArrayList<>();
		Tb1061PoutEntity a1 = curSel.getTb1061PoutByF1061CodeA1();
		if(a1 == null) {
			a1 = new Tb1061PoutEntity("A相通道1虚端子");
		} else {
			a1.setF1061Desc("A相通道1虚端子");
		}
			tableChnData.add(a1);
			
		Tb1061PoutEntity a2 = curSel.getTb1061PoutByF1061CodeA2();
		if(a2 == null) {
			a2 = new Tb1061PoutEntity("A相通道2虚端子");
		} else {
			a2.setF1061Desc("A相通道2虚端子");
		}
			tableChnData.add(a2);
		Tb1061PoutEntity b1 = curSel.getTb1061PoutByF1061CodeB1();
		if(b1 == null) {
			b1 = new Tb1061PoutEntity("B相通道1虚端子");
		} else {
			b1.setF1061Desc("B相通道1虚端子");
		}
			tableChnData.add(b1);
		Tb1061PoutEntity b2 = curSel.getTb1061PoutByF1061CodeB2();
		if(b2 == null) {
			b2 = new Tb1061PoutEntity("B相通道2虚端子");
		} else {
			b2.setF1061Desc("B相通道2虚端子");
		}
			tableChnData.add(b2);
		Tb1061PoutEntity c1 = curSel.getTb1061PoutByF1061CodeC1();
		if(c1 == null) {
			c1 = new Tb1061PoutEntity("C相通道1虚端子");
		} else {
			c1.setF1061Desc("C相通道1虚端子");
		}
		tableChnData.add(c1);
		Tb1061PoutEntity c2 = curSel.getTb1061PoutByF1061CodeC2();
		if(c2 == null) {
			c2 = new Tb1061PoutEntity("C相通道2虚端子");
		} else {
			c2.setF1061Desc("C相通道2虚端子");
		}
		tableChnData.add(c2);
		tableChanel.setInput(tableChnData);
		tableChanel.getTable().layout();
		
		if(DataUtils.listNotNull(iedEntities)) {
			refreshTableState(iedEntities.get(0));
		}
		
	}
	

	private void refreshTableState(Tb1046IedEntity iedEntity) {
		svcbEntities = svcbService.getSvcbEntityByIedEntity(iedEntity);
		List<Tb1061PoutEntity> allPouts = new ArrayList<>();
		for (Tb1056SvcbEntity tb1056SvcbEntity : svcbEntities) {
			allPouts.addAll(tb1056SvcbEntity.getTb1061PoutsByCbCode());
		}
		tableState.setInput(allPouts);
		tableState.getTable().layout();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器通道关联");
	}
	
	private void addListeners() {
		SelectionListener selectionListener = new SelectionAdapter() {
			private Tb1046IedEntity curSelIedEntity;

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == btnAdd) {
					Tb1061PoutEntity rightSel = (Tb1061PoutEntity) tableState.getSelection();
					if(rightSel == null) {
						return;
					}
					Tb1061PoutEntity leftSel = (Tb1061PoutEntity) tableChanel.getSelection();
					if(leftSel == null) {
						return;
					}
					rightSel.setF1061Desc(leftSel.getF1061Desc());
//					leftSel = rightSel;
					tableChnData.set(tableChnData.indexOf(leftSel), rightSel);
					tableChanel.setInput(tableChnData);
				} else if(obj == comboDevice) {
					int curComboDevSelIdx = comboDevice.getSelectionIndex();
					if(curComboDevSelIdx == preComboDevSelIdx) {
						return;
					}
					preComboDevSelIdx = curComboDevSelIdx;
					String selDevName = comboDevItems[curComboDevSelIdx];
					curSelIedEntity = getIedEntityByName(selDevName);
					refreshTableState(curSelIedEntity);
//					comRight.layout();
					comboDevice.redraw();
				} 
//				else if(obj == btnDel) {
//					Tb1061PoutEntity poutEntity = (Tb1061PoutEntity) tableChanel.getSelection();
//					if(poutEntity == null) {
//						return;
//					}
//					tableState.addRow(poutEntity);
//					tableChanel.removeSelected();
//					tableChanel.refresh();
//					tableState.refresh();
//				}
			}

		};
//		btnDel.addSelectionListener(selectionListener);
		btnAdd.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
	}
	
	private Tb1046IedEntity getIedEntityByName(String iedName) {
		for (Tb1046IedEntity iedEntity : iedEntities) {
			if(iedEntity.getF1046Desc().equals(iedName)) {
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
		return new Point(800, 550);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID){
			if(tableChanel.getItemCount() == 6) {
				List<Tb1061PoutEntity> input = (List<Tb1061PoutEntity>) tableChanel.getInput();
				setChanelTableData(input);
				curSel.setTb1061PoutByF1061CodeA1(input.get(0));
				curSel.setTb1061PoutByF1061CodeA2(input.get(1));
				curSel.setTb1061PoutByF1061CodeB1(input.get(2));
				curSel.setTb1061PoutByF1061CodeA2(input.get(3));
				curSel.setTb1061PoutByF1061CodeC1(input.get(4));
				curSel.setTb1061PoutByF1061CodeC2(input.get(5));
			} else {
				DialogHelper.showInformation("请关联6个虚端子对象");
				return;
			}
		}
		super.buttonPressed(buttonId);
	}

	public List<Tb1061PoutEntity> getChanelTableData() {
		return chanelTableData;
	}

	private void setChanelTableData(List<Tb1061PoutEntity> chanelTableData) {
		this.chanelTableData = chanelTableData;
	}
}
