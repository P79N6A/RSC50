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
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

/**
 * 采样关联
 * @author Administrator
 *
 */
public class SampleConnectDialog extends WrappedDialog {

	private String curEntryName;
	private DevKTable tableProtctSample;
	private Button btnAdd;
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
	private List<Tb1006AnalogdataEntity> portAnalogTableData;
	
	public SampleConnectDialog(Shell parentShell) {
		super(parentShell);
	}

	public SampleConnectDialog(Shell defaultShell, String curEntryName, 
			Tb1066ProtmmxuEntity protmmxuEntity, List<Tb1046IedEntity> iedEntities) {
		super(defaultShell);
		this.curEntryName = curEntryName;
		this.curSel = protmmxuEntity;
		this.iedEntities = iedEntities;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
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
		
		GridData gridBtnCom = new GridData(41, SWT.DEFAULT);
		Composite comBtn = SwtUtil.createComposite(comLeft, gridBtnCom, 1);
		comBtn.setLayout(SwtUtil.getGridLayout(1));
		
		btnAdd = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		
		textDesc = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		textDesc.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comRight, new GridData(50, 25), SWT.BUTTON1, "查询");
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableSample = TableFactory.getAnalogTable(comRight);
		tableSample.getTable().setLayoutData(gdSpan_3);
		addListeners();
		initTableData();
		initComboData();
		return composite;
	}
	
	private void initComboData() {
		if(!DataUtils.listNotNull(iedEntities)) {
			comboItems = new String[]{"装置为空"};
		} else {
			List<String> lstIedName = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : iedEntities) {
				lstIedName.add(tb1046IedEntity.getF1046Name());
			}
			comboItems = new String[lstIedName.size()];
			comboItems = lstIedName.toArray(comboItems);
			Tb1046IedEntity defSel = iedEntities.get(0);
			mmsfcdaService = new MmsfcdaService();
			analogdataEntities = getAnalogByIed(defSel);
			tableSample.setInput(analogdataEntities);
		}
		comboDevice.setItems(comboItems);
		comboDevice.select(0);
	}

	private List<Tb1006AnalogdataEntity> getAnalogByIed(Tb1046IedEntity defSel) {
		List<Tb1058MmsfcdaEntity> mmsfcdaEntities = mmsfcdaService.getMmsfcdaByIed(defSel);
		if(!DataUtils.listNotNull(mmsfcdaEntities)) {
			return new ArrayList<>();
		}
		List<String> lstAnalogDataCodes = new ArrayList<>();
		for (Tb1058MmsfcdaEntity tb1058MmsfcdaEntity : mmsfcdaEntities) {
			lstAnalogDataCodes.add(tb1058MmsfcdaEntity.getDataCode());
		}
		if(!DataUtils.listNotNull(lstAnalogDataCodes)) {
			return new ArrayList<>();
		}
		analogdataService = new AnalogdataService();
		List<Tb1006AnalogdataEntity> analogdataEntities = analogdataService.getAnologByCodes(lstAnalogDataCodes);
		return analogdataEntities;
	}
	
	private void initTableData() {
		portAnalogTableData = new ArrayList<>();
		Tb1006AnalogdataEntity f1006CodeA = curSel.getF1006CodeA();
		if(f1006CodeA == null) {
			f1006CodeA = new Tb1006AnalogdataEntity("采样值数据A相");
		} else {
			f1006CodeA.setF1006Byname("采样值数据A相");
		}
		portAnalogTableData.add(f1006CodeA);
		Tb1006AnalogdataEntity f1006CodeB = curSel.getF1006CodeB();
		
		if(f1006CodeB == null) {
			f1006CodeB = new Tb1006AnalogdataEntity("采样值数据B相");
		} else {
			f1006CodeB.setF1006Byname("采样值数据B相");
		}
		portAnalogTableData.add(f1006CodeB);
		Tb1006AnalogdataEntity f1006CodeC = curSel.getF1006CodeC();
		
		if(f1006CodeC == null) {
			f1006CodeC = new Tb1006AnalogdataEntity("采样值数据C相");
		} else {
			f1006CodeC.setF1006Byname("采样值数据C相");
		}
		portAnalogTableData.add(f1006CodeC);
		tableProtctSample.setInput(portAnalogTableData);
		tableProtctSample.getTable().layout();
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
				} else if(obj == btnAdd) {
					configData();
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
		btnAdd.addSelectionListener(selectionListener);
		btnSearch.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
		
	}
	
	private void configData() {
		Tb1006AnalogdataEntity analogLeft = (Tb1006AnalogdataEntity) tableProtctSample.getSelection();
		if(analogLeft == null) {
			return;
		}
		Tb1006AnalogdataEntity analogRight = (Tb1006AnalogdataEntity) tableSample.getSelection();
		if(analogRight == null) {
			return;
		}
		analogLeft.setF1006Desc(analogRight.getF1006Desc());
		analogRight.setF1006Byname(analogLeft.getF1006Byname());
//		analogLeft = analogRight;
		int selectRowNum = tableProtctSample.getSelectRowNum();
		switch (selectRowNum) {
		case 1:
			curSel.setF1006CodeA(analogRight);
			break;
		case 2:
			curSel.setF1006CodeB(analogRight);
			break;
		case 3:
			curSel.setF1006CodeC(analogRight);
		default:
			break;
		}
		tableProtctSample.setInput(portAnalogTableData);
		tableProtctSample.getTable().layout();
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
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID) {
			if(curSel.getF1006CodeA() == null 
				|| curSel.getF1006CodeB() == null
					|| curSel.getF1006CodeC() == null) {
				DialogHelper.showInformation("请关联3个模拟量对象");
				 return;
			}
		}
		super.buttonPressed(buttonId);
	}
}
