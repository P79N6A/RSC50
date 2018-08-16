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
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.app.WrappedDialog;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

public class SampleConnectDialog extends WrappedDialog {

	private String curEntryName;
	private DevKTable tableProtctSample;
	private Button btnMove;
	private Button btnSearch;
	private DevKTable tableSample;
	private String[] comboItems;
	private Tb1066ProtmmxuEntity curSel;
	private Combo comboDevice;
	private MmsfcdaService mmsfcdaService;
	private AnalogdataService analogdataService;
	private int preComboDevSel = 0;
	private List<Tb1046IedEntity> iedEntities;
	private List<Tb1006AnalogdataEntity> analogdataEntities;
	private Text textDesc;
	private List<Tb1006AnalogdataEntity> tableData;
	
	public SampleConnectDialog(Shell parentShell) {
		super(parentShell);
	}

	public SampleConnectDialog(Shell defaultShell, String curEntryName, Tb1066ProtmmxuEntity protmmxuEntity) {
		super(defaultShell);
		this.curEntryName = curEntryName;
		this.curSel = protmmxuEntity;
		comboItems = new String[]{"智能终端1"};
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
		String switchLbName = curEntryName + "互感器保护采样值：";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		
		tableProtctSample = TableFactory.getProtAnalogTable(comLeft);
		tableProtctSample.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		btnMove = SwtUtil.createButton(comLeft, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		comboDevice.setItems(comboItems);
		comboDevice.select(0);
		
		textDesc = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		textDesc.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comRight, new GridData(50, 25), SWT.BUTTON1, "查询");
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableSample = TableFactory.getAnalogTable(comRight);
		tableSample.getTable().setLayoutData(gdSpan_3);
		addListeners();
		initTableData();
		return composite;
	}
	
	private void initComboData() {
		IedEntityService iedEntityService  = new IedEntityService();
		int[] types = EnumIedType.UNIT_DEVICE.getTypes();
		iedEntities = iedEntityService.getIedEntityByTypes(types);
		if(!DataUtils.notNull(iedEntities)) {
			comboItems = new String[]{"装置为空"};
		} else {
			List<String> lstIedName = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : iedEntities) {
				lstIedName.add(tb1046IedEntity.getF1046Desc());
			}
			comboItems = new String[lstIedName.size()];
			comboItems = lstIedName.toArray(comboItems);
			Tb1046IedEntity defSel = iedEntities.get(0);
			mmsfcdaService = new MmsfcdaService();
			analogdataEntities = getAnalogByIed(defSel);
			tableSample.setInput(analogdataEntities);
		}

	}

	private List<Tb1006AnalogdataEntity> getAnalogByIed(Tb1046IedEntity defSel) {
		List<Tb1058MmsfcdaEntity> mmsfcdaEntities = mmsfcdaService.getMmsfcdaByIed(defSel);
		if(!DataUtils.notNull(mmsfcdaEntities)) {
			return new ArrayList<>();
		}
		List<String> lstAnalogDataCodes = new ArrayList<>();
		for (Tb1058MmsfcdaEntity tb1058MmsfcdaEntity : mmsfcdaEntities) {
			lstAnalogDataCodes.add(tb1058MmsfcdaEntity.getDataCode());
		}
		if(!DataUtils.notNull(lstAnalogDataCodes)) {
			return new ArrayList<>();
		}
		analogdataService = new AnalogdataService();
		List<Tb1006AnalogdataEntity> analogdataEntities = analogdataService.getAnologByCodes(lstAnalogDataCodes);
		return analogdataEntities;
	}
	
	private void initTableData() {
		List<String> f1066codes = new ArrayList<>();
		String f1006CodeA = curSel.getF1006CodeA();
		String f1006CodeB = curSel.getF1006CodeB();
		String f1006CodeC = curSel.getF1006CodeC();
		String[] temp = new String[]{f1006CodeA, f1006CodeB, f1006CodeC};
		for (String code : temp) {
			if(!code.isEmpty()) {
				f1066codes.add(code);
			}
		}
		//根据1006codes,查找1006
		List<Tb1006AnalogdataEntity> analogdataEntities = analogdataService.getAnologByCodes(f1066codes);
		tableProtctSample.setInput(analogdataEntities);
	}
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("互感器通道关联");
	}
	
	private void addListeners() {
		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == comboDevice) {
					int curComboSel = comboDevice.getSelectionIndex();
					if(preComboDevSel == curComboSel) {
						return;
					}
					preComboDevSel = curComboSel;
					Tb1046IedEntity curSelIed = getSelIedByName(comboDevice.getItem(curComboSel));
					analogdataEntities = getAnalogByIed(curSelIed);
					tableSample.setInput(analogdataEntities);
					tableSample.getTable().layout();
				} else if(obj == btnMove) {
					Object tableData = tableSample.getSelection();
					if(tableData != null) {
						tableProtctSample.addRow(tableData);
					}
					tableProtctSample.getTable().layout();
				} else if(obj == btnSearch) {
					String desc = textDesc.getText().trim();
					List<Tb1006AnalogdataEntity> searchRes = new ArrayList<>();
					for (Tb1006AnalogdataEntity analogdataEntity : analogdataEntities) {
						if(analogdataEntity.getF1006Desc().contains(desc)) {
							searchRes.add(analogdataEntity);
						}
					}
					tableSample.setInput(searchRes);
					tableSample.getTable().layout();
				}
			}

			
		};
		btnMove.addSelectionListener(selectionListener);
		btnSearch.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
		
	}

	private Tb1046IedEntity getSelIedByName(String select) {
		for (Tb1046IedEntity ied : iedEntities) {
			if(ied.getF1046Desc().equals(select)) {
				return ied;
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
		if(buttonId == IDialogConstants.OK_ID) {
			setTableData((List<Tb1006AnalogdataEntity>) tableProtctSample.getInput());
			
		}
		super.buttonPressed(buttonId);
	}

	public List<Tb1006AnalogdataEntity> getTableData() {
		return tableData;
	}

	private void setTableData(List<Tb1006AnalogdataEntity> tableData) {
		this.tableData = tableData;
	}

}
