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
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ProtmmxuService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

import de.kupzog.ktable.KTableCellSelectionListener;

/**
 * 采样关联
 * @author Administrator
 *
 */
public class SampleConnectDialog extends WrappedDialog {

	private Combo comboDevice;
	private Text textDesc;
	private Button btFilter;
	private Button btnSearch;
	private DevKTable tableProtctSample;
	private DevKTable tableSample;
	
	private ProtmmxuService protmmxuService;
	private AnalogdataService analogdataService;
	private IedEntityService iedEntityService;
	
	private int preComboDevSel = 0;
	private String[] comboItems;
	private Tb1042BayEntity bayEntity;
	private List<Tb1046IedEntity> iedEntities;
	private List<Tb1006AnalogdataEntity> analogdataEntities;
	private List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities;
	private List<Tb1006AnalogdataEntity> selectedAnalog;
	private List<Tb1066ProtmmxuEntity> protmmxuEntityList;
	
	public SampleConnectDialog(Shell defaultShell, Tb1042BayEntity bayEntity, List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities) {
		super(defaultShell);
		this.bayEntity = bayEntity;
		this.ctvtsecondaryEntities = ctvtsecondaryEntities;
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
		String switchLbName = bayEntity.getF1042Name() + "互感器保护采样值：";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		
		tableProtctSample = TableFactory.getProtAnalogTable(comLeft);
		tableProtctSample.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//右侧
		Composite comRight = SwtUtil.createComposite(composite, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(4));
		GridData textGridData = new GridData();
		textGridData.widthHint = 150;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		
		textDesc = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		textDesc.setMessage("描述");		
		btFilter = SwtUtil.createCheckBox(comRight, "当前间隔", null);
		btnSearch = SwtUtil.createButton(comRight, new GridData(50, 25), SWT.BUTTON1, RSCConstants.SEARCH);
		
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 4;
		tableSample = TableFactory.getAnalogTable(comRight);
		tableSample.getTable().setLayoutData(gdSpan_3);
		initData();
		addListeners();
		return composite;
	}
	
	private void loadIEDList() {
		boolean curBay = btFilter.getSelection();
		if (curBay) {
			iedEntities = iedEntityService.getIedByTypesAndBay(EnumIedType.PROTECT_DEVICE.getTypes(), bayEntity);
		} else {
			iedEntities = iedEntityService.getIedByTypesAndBay(EnumIedType.PROTECT_DEVICE.getTypes(), null);
		}
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
			loadAnalogByIed(defSel);
		}
		comboDevice.setItems(comboItems);
		comboDevice.select(0);
	}

	private void initData() {
		protmmxuService = new ProtmmxuService();	
		analogdataService = new AnalogdataService();
		iedEntityService = new IedEntityService();
		tableProtctSample.setInput(ctvtsecondaryEntities);
		btFilter.setSelection(true);
		loadIEDList();
	}

	private void loadAnalogByIed(Tb1046IedEntity defSel) {
		analogdataEntities = analogdataService.getMeasDataByIed(defSel);
		tableSample.setInput(analogdataEntities);
		selectSecondAnalog();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("保护采样值关联");
	}
	
	private void selectSecondAnalog() {
		List<Tb1006AnalogdataEntity> searchRes = (List<Tb1006AnalogdataEntity>) tableSample.getInput();
		for (Tb1006AnalogdataEntity searchRe : searchRes) {
			searchRe.setSelected(false);
		}
		List<Object> secds = tableProtctSample.getSelections();
		if (secds==null || secds.size()<1) {
			return;
		}
		List<Tb1067CtvtsecondaryEntity> selections = new ArrayList<>();
		for (Object o : secds) {
			Tb1067CtvtsecondaryEntity sec = (Tb1067CtvtsecondaryEntity) o;
			selections.add(sec);
		}
		List<Tb1066ProtmmxuEntity> mmxus = protmmxuService.getProtmmxuByCtvtsecondary(selections);
		List<Tb1006AnalogdataEntity> algs = new ArrayList<>();
		for (Tb1066ProtmmxuEntity mmxu : mmxus) {
			algs.add(mmxu.getF1006Code());
		}
		for (Tb1006AnalogdataEntity searchRe : searchRes) {
			if (algs.contains(searchRe)) {
				searchRe.setSelected(true);
			}
		}
		tableSample.refresh();
	}
	
	private void addListeners() {
		tableProtctSample.getTable().addCellSelectionListener(new KTableCellSelectionListener() {
			@Override
			public void fixedCellSelected(int col, int row, int statemask) {
			}
			
			@Override
			public void cellSelected(int col, int row, int statemask) {
				if (row < 1) {
					return;
				}
				selectSecondAnalog();
			}
		});
		
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
					loadAnalogByIed(curSelIed);
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
				} else if(obj == btFilter) {
					loadIEDList();
				}
			}
		};
		btnSearch.addSelectionListener(selectionListener);
		comboDevice.addSelectionListener(selectionListener);
		btFilter.addSelectionListener(selectionListener);
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
			selectedAnalog = new ArrayList<>();
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
