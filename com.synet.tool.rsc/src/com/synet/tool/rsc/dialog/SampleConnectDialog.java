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
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.ProtmmxuService;
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
	private Button btnSearch;
	private DevKTable tableSample;
	private String[] comboItems;
	private Combo comboDevice;
	private MmsfcdaService mmsfcdaService;
	private AnalogdataService analogdataService;
	private int preComboDevSel = 0;
	private List<Tb1046IedEntity> iedEntities;
	private List<Tb1006AnalogdataEntity> analogdataEntities;
	private Text textDesc;
	private List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities;
	private List<Tb1006AnalogdataEntity> selectedAnalog;
	private ProtmmxuService protmmxuService;
	private List<Tb1066ProtmmxuEntity> protmmxuEntityList;
	
	public SampleConnectDialog(Shell defaultShell, List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities, String curEntryName, 
			 List<Tb1046IedEntity> bayIeds) {
		super(defaultShell);
		this.curEntryName = curEntryName;
		iedEntities = new ArrayList<>();
		this.ctvtsecondaryEntities = ctvtsecondaryEntities;
		for (Tb1046IedEntity bayIed : bayIeds) {
			if (EnumIedType.PROTECT_DEVICE.include(bayIed.getF1046Type())) {
				iedEntities.add(bayIed);
			}
		}
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
		
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.widthHint = 150;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		
		textDesc = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		textDesc.setMessage("描述");
		
		btnSearch = SwtUtil.createButton(comRight, new GridData(50, 25), SWT.BUTTON1, RSCConstants.SEARCH);
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableSample = TableFactory.getAnalogTable(comRight);
		tableSample.getTable().setLayoutData(gdSpan_3);
		initData();
		addListeners();
		return composite;
	}
	

	private void initData() {
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
		
		tableProtctSample.setInput(ctvtsecondaryEntities);
		selectedAnalog = new ArrayList<>();
		
		protmmxuService = new ProtmmxuService();
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
	
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("保护采样值关联");
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
				} else if(obj == btnSearch) {
					String desc = textDesc.getText().trim();
					List<Tb1006AnalogdataEntity> searchRes = new ArrayList<>();
					if(desc.isEmpty()) {
						searchRes = analogdataEntities;
					} else {
						for (Tb1006AnalogdataEntity analogdataEntity : analogdataEntities) {
							if(analogdataEntity.getF1006Desc().contains(desc)) {
								searchRes.add(analogdataEntity);
							}
						}
					}
					tableSample.setInput(searchRes);
					tableSample.getTable().layout();
				}
			}

			
		};
		btnSearch.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
		
	}
	

	private Tb1046IedEntity getSelIedByName(String select) {
		for (Tb1046IedEntity ied : iedEntities) {
			if(ied.getF1046Name().equals(select)) {
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
		return new Point(1200, 650);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == IDialogConstants.OK_ID) {
			RSCProperties rscp = RSCProperties.getInstance();
			Object selection = tableProtctSample.getSelection();
			if(selection == null) {
				return;
			}
			protmmxuEntityList = new ArrayList<>();
			selectedAnalog.clear();
			Tb1067CtvtsecondaryEntity selectedCtvt = (Tb1067CtvtsecondaryEntity) selection;
			@SuppressWarnings("unchecked")
			List<Tb1006AnalogdataEntity> input = (List<Tb1006AnalogdataEntity>) tableSample.getInput();
			
			for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : input) {
				if(tb1006AnalogdataEntity.isSelected()) {
					selectedAnalog.add(tb1006AnalogdataEntity);
				}
			}
			
			for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : selectedAnalog) {
				boolean exist = protmmxuService.relationExistCheck(selectedCtvt, tb1006AnalogdataEntity);
				if(!exist) {
					Tb1066ProtmmxuEntity entity = new Tb1066ProtmmxuEntity(selectedCtvt, tb1006AnalogdataEntity);
					entity.setF1066Code(rscp.nextTbCode(DBConstants.PR_MMXU));
					protmmxuEntityList.add(entity);
					protmmxuService.insert(entity);
				}
			}
		}
		super.buttonPressed(buttonId);
	}
	
	public List<Tb1066ProtmmxuEntity> getProtmmxuEntityList() {
		return protmmxuEntityList;
	}
}
