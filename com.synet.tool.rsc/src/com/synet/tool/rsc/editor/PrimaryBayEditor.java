/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ChanelConnectDialog;
import com.synet.tool.rsc.dialog.SampleConnectDialog;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.CtvtsecondaryService;
import com.synet.tool.rsc.service.EquipmentEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.ProtmmxuService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

/**
 * 一次拓扑模型树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class PrimaryBayEditor extends BaseConfigEditor {
	
	private Button btnChanelConnect;
	private Button btnSampleConnect;
	private String curEntryName;
	private Button btnAdd;
	private String[] comboItems;
	private Button btnSearch;
	private DevKTable tableCtvtsecondary;
	private Tb1042BayEntity bayEntity;
	private DevKTable tableProtectSample;
	private DevKTable tableSwitchStatus;
	private DevKTable tableSluiceStatus;
	private List<Tb1046IedEntity> iedEntities;
	private PoutEntityService poutEntityService;
	private Combo comboDevice;
	private int preComboSelIdx = 0;
	private StatedataService statedataService;
	private List<Tb1016StatedataEntity> tableSluiceStatuData;
	private Text textDesc;
	private Button btnDel;
	
	private IedEntityService iedService;
	
	public PrimaryBayEditor(Composite container, IEditorInput input) {
		super(container, input);
	}

	
	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{RSCConstants.TSF_SCDRAY, RSCConstants.PROTCT_SAMP, RSCConstants.SWICH_STATES};
		CTabFolder tabFolder = SwtUtil.createTab(comp, gridData, tabNames);
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//互感器次级
		Composite compTsf = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		compTsf.setLayout(SwtUtil.getGridLayout(2));
		GridData gdlb = new GridData(200,25);
		String tsfLbName = curEntryName + "互感器次级配置";
		SwtUtil.createLabel(compTsf, tsfLbName, gdlb);
		btnChanelConnect = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "通道关联");
		SwtUtil.createLabel(compTsf, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_2 = new GridData(GridData.FILL_BOTH);
		gdSpan_2.horizontalSpan = 2;
		tableCtvtsecondary = TableFactory.getTsfSecondaryTable(compTsf);
		tableCtvtsecondary.getTable().setLayoutData(gdSpan_2);
		//保护采样值
		Composite compProtect = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		compProtect.setLayout(SwtUtil.getGridLayout(2));
		String protLbName = curEntryName + "保护采样值配置";
		SwtUtil.createLabel(compProtect, protLbName, gdlb);
		btnSampleConnect = SwtUtil.createButton(compProtect, SwtUtil.bt_gd, SWT.BUTTON1, "采样关联");
		SwtUtil.createLabel(compProtect, "			", new GridData(SWT.DEFAULT,10));
		tableProtectSample = TableFactory.getProtectSampleTalbe(compProtect);
		tableProtectSample.getTable().setLayoutData(gdSpan_2);
		//开关刀闸状态
		Composite compSwitch = SwtUtil.createComposite((Composite) controls[2], gridData, 1);
		compSwitch.setLayout(SwtUtil.getGridLayout(2));
		//开关刀闸状态-左侧
		Composite comLeft = SwtUtil.createComposite(compSwitch, new GridData(640,405), 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		GridData gdlb_2 = new GridData(200,25);
		gdlb_2.horizontalSpan = 2;
		String switchLbName = curEntryName + "开关刀闸状态配置";
		SwtUtil.createLabel(comLeft, switchLbName, gdlb_2);
		GridData gdlbSpace_2 = new GridData(SWT.DEFAULT,10);
		gdlbSpace_2.horizontalSpan = 2;
		SwtUtil.createLabel(comLeft, "			", gdlbSpace_2);
		tableSwitchStatus = TableFactory.getSwitchStatusTable(comLeft);
		tableSwitchStatus.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		GridData gridBtnCom = new GridData(41, SWT.DEFAULT);
		Composite comBtn = SwtUtil.createComposite(comLeft, gridBtnCom, 1);
		comBtn.setLayout(SwtUtil.getGridLayout(1));
		btnAdd = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		btnDel = SwtUtil.createButton(comBtn, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "->");
		
		//开关刀闸状态-右侧
		Composite comRight = SwtUtil.createComposite(compSwitch, gridData, 1);
		comRight.setLayout(SwtUtil.getGridLayout(3));
		GridData textGridData = new GridData();
		textGridData.heightHint = 25;
		textGridData.widthHint = 80;
		comboDevice = SwtUtil.createCombo(comRight, textGridData, true);
		comboDevice.setItems(comboItems);
		comboDevice.select(0);
		textDesc = SwtUtil.createText(comRight, SwtUtil.bt_hd);
		textDesc.setMessage("描述");
		btnSearch = SwtUtil.createButton(comRight, SwtUtil.bt_gd, SWT.BUTTON1, "查询");
		SwtUtil.createLabel(comRight, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		tableSluiceStatus = TableFactory.getSluiceStatusTable(comRight);
		tableSluiceStatus.getTable().setLayoutData(gdSpan_3);
	}
	
	@Override
	public void init() {
		EditorConfigData data = (EditorConfigData)super.getInput().getData();
		this.curEntryName = data.getIedName();
		this.bayEntity = (Tb1042BayEntity) data.getData();
		this.iedService = new IedEntityService();
		this.iedEntities = iedService.getIedEntityByBay(bayEntity);
		if(iedEntities.size() < 1) {
			comboItems = new String[]{"装置为空"};
		} else {
			List<String> lstComboDevItem = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : iedEntities) {
				lstComboDevItem.add(tb1046IedEntity.getF1046Desc());
			}
			comboItems = new String[lstComboDevItem.size()];
			comboItems = lstComboDevItem.toArray(comboItems);
		}
		super.init();
	}
	
	protected void addListeners() {
		SelectionListener sleListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object object = e.getSource();
				if(object == btnChanelConnect) {
					Object obj = tableCtvtsecondary.getSelection();
					if(obj == null) {
						return;
					}
					Tb1067CtvtsecondaryEntity ctvtsecondaryEntity = (Tb1067CtvtsecondaryEntity) obj;
					ChanelConnectDialog chnDialog = new ChanelConnectDialog(SwtUtil.getDefaultShell(), 
							curEntryName, ctvtsecondaryEntity, iedEntities);
					if(chnDialog.open() == IDialogConstants.OK_ID) {
						//TODO 设置虚端子
					}
				} else if(object == btnSampleConnect) {
					Object obj = tableProtectSample.getSelection();
					if(obj == null) {
						return;
					}
					Tb1066ProtmmxuEntity protmmxuEntity = (Tb1066ProtmmxuEntity) obj;
					SampleConnectDialog sampleDialog = new SampleConnectDialog(SwtUtil.getDefaultShell(),
							curEntryName, protmmxuEntity, iedEntities);
					if(sampleDialog.open() == IDialogConstants.OK_ID) {
					//TODO 设置模拟量	
					}
				} else if(object == btnAdd) {
					Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) tableSluiceStatus.getSelection();
					if(statedataEntity != null) {
						//找到1043
						EquipmentEntityService equipmentService = new EquipmentEntityService();
						Tb1043EquipmentEntity res = equipmentService.getEquipmentByStateData(statedataEntity);
						if(res != null) {
							tableSwitchStatus.addRow(res);
							tableSluiceStatus.removeSelected();
							tableSluiceStatus.refresh();
							tableSwitchStatus.refresh();
						}
					}
					tableSwitchStatus.getTable().layout();
				} else if(object == btnSearch) {
					String desc = textDesc.getText().trim();
					List<Tb1016StatedataEntity> searchResult = searchByDesc(desc);
					tableSluiceStatus.setInput(searchResult);
					tableSluiceStatus.getTable().layout();
				} else if(object == comboDevice) {
					int curComboSelIdx = comboDevice.getSelectionIndex();
					if(preComboSelIdx == curComboSelIdx) {
						return;
					}
					preComboSelIdx = curComboSelIdx;
					Tb1046IedEntity iedEntity = getIedEntityByName(comboDevice.getItem(curComboSelIdx));
					tableSluiceStatuData = getStateDataByIed(iedEntity);
					tableSluiceStatus.setInput(tableSluiceStatuData);
					tableSluiceStatus.getTable().layout();
				} else if(object == btnDel) {
					Tb1043EquipmentEntity equipmentEntity = (Tb1043EquipmentEntity) tableSwitchStatus.getSelection();
					if(equipmentEntity == null) {
						return;
					}
					Tb1016StatedataEntity statedataEntity = equipmentEntity.getTb1016StatedataEntity();
					tableSluiceStatus.addRow(statedataEntity);
					tableSwitchStatus.removeSelected();
					tableSwitchStatus.refresh();
					tableSluiceStatus.refresh();
				}
			}
		};
		btnDel.addSelectionListener(sleListener);
		btnChanelConnect.addSelectionListener(sleListener);
		btnSampleConnect.addSelectionListener(sleListener);
		btnAdd.addSelectionListener(sleListener);
		btnSearch.addSelectionListener(sleListener);
		comboDevice.addSelectionListener(sleListener);
	}

	private List<Tb1016StatedataEntity> searchByDesc(String desc) {
		List<Tb1016StatedataEntity> res = new ArrayList<>();
		for (Tb1016StatedataEntity statedataEntity : tableSluiceStatuData) {
			if (statedataEntity.getF1016Desc().contains(desc)) {
				res.add(statedataEntity);
			}
		}
		return res;
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
	public void initData() {
		EquipmentEntityService equipmentEntityService = new EquipmentEntityService();
		//根据当前节点：间隔，查询间隔下所有互感器
		List<Tb1043EquipmentEntity> entities = equipmentEntityService.getEquipmentEntitysByBayEntity(bayEntity);
		CtvtsecondaryService ctvtsecondaryService = new CtvtsecondaryService();
		//查询互感器集合下关联的所有互感器次级
		List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities = ctvtsecondaryService.getCtvtsecondaryEntitiesByEquEntity(entities);
		tableCtvtsecondary.setInput(ctvtsecondaryEntities);
		ProtmmxuService protmmxuService = new ProtmmxuService();
		//查找互感器集合下关联的所有保护采样
		List<Tb1066ProtmmxuEntity> protmmxuEntities = protmmxuService.getProtmmxuByCtvtsecondary(ctvtsecondaryEntities);
		tableProtectSample.setInput(protmmxuEntities);
		//初始化开关刀闸状态右表
		statedataService = new StatedataService();
		if(DataUtils.listNotNull(iedEntities)) {
			Tb1046IedEntity iedEntity = iedEntities.get(0);
			poutEntityService = new PoutEntityService();
			tableSluiceStatuData = getStateDataByIed(iedEntity);
			tableSluiceStatus.setInput(tableSluiceStatuData);
		}
		//初始化开关刀闸状态左表
		List<Tb1016StatedataEntity> statedataEntities = statedataService.getStateDataByEquips(entities);
		tableSwitchStatus.setInput(statedataEntities);
		super.initData();
	}


	private List<Tb1016StatedataEntity> getStateDataByIed(
			Tb1046IedEntity iedEntity) {
		List<Tb1016StatedataEntity> statedataEntities = null;
		List<Tb1061PoutEntity> poutEntities = poutEntityService.getPoutEntityByProperties(iedEntity, null);
		if(DataUtils.listNotNull(poutEntities)) {
			List<String> stateDataCodes = new ArrayList<>();
			for (Tb1061PoutEntity tb1061PoutEntity : poutEntities) {
				stateDataCodes.add(tb1061PoutEntity.getDataCode());
			}
			
			statedataEntities = statedataService.getStatedataByDataCodes(stateDataCodes);
		}
		return statedataEntities;
	}
}
