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
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1074SVCTVTRelationEntity;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
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
	private int preComboDevSelIdx = 0;
	private List<Tb1046IedEntity> iedEntities;
	private SvcbEntityService svcbService;
	private List<Tb1056SvcbEntity> svcbEntities;
	private Composite comRight;
	private List<Tb1061PoutEntity> chanelTableData;
	private IedEntityService iedService;
	private Button btnDel;
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
		initData();
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
		btnDel = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "->");
		//右侧
		comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(2));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 150;
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
	
	private void initData() {
		selectedPoutList = new ArrayList<>();
		
		iedService = new IedEntityService();
		svcbService = new SvcbEntityService();
		int[] devTypes = EnumIedType.UNIT_DEVICE.getTypes();
		iedEntities = iedService.getIedByTypesAndBay(devTypes, null);
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
	}


	/**
	 * 初始化表格数据
	 */
	private void initTableData() {
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
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == btnAdd) {//效率较低
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
					tableChanel.removeSelected();
					tableChanel.refresh();
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
		return new Point(1000, 650);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID){
			SVCTVTRelationEntityService service = new SVCTVTRelationEntityService();
			List<Tb1061PoutEntity> input = (List<Tb1061PoutEntity>) tableChanel.getInput();
			setChanelTableData(input);
			for (Tb1061PoutEntity tb1061PoutEntity : input) {
				boolean exist = service.relationExistCheck(curSel, tb1061PoutEntity);
				if(!exist) {
					Tb1074SVCTVTRelationEntity relationEntity = new Tb1074SVCTVTRelationEntity(curSel, tb1061PoutEntity);
					service.insert(relationEntity);
				}
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
