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
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ChanelConnectDialog;
import com.synet.tool.rsc.dialog.SampleConnectDialog;
import com.synet.tool.rsc.dialog.SelectEquiDialog;
import com.synet.tool.rsc.io.scd.EnumEquipmentType;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.service.CtvtsecondaryService;
import com.synet.tool.rsc.service.EnumIedType;
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
	private IedEntityService iedService;
	private Button btnAddTsf;
	private Button btnDelTsf;
	private CTabFolder tabFolder;
	private CtvtsecondaryService ctvtsecondaryService;
	private EquipmentEntityService equipmentEntityService;
	private ProtmmxuService protmmxuService;
	private List<Tb1066ProtmmxuEntity> protmmxuEntities;
	private List<Tb1043EquipmentEntity> statedataEntities;
	private List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities;
	private List<Tb1043EquipmentEntity> entities;
	private List<Tb1046IedEntity> comboDvData;
	
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
		tabFolder = SwtUtil.createTab(comp, gridData, tabNames);
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//互感器次级
		Composite compTsf = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		compTsf.setLayout(SwtUtil.getGridLayout(4));
		GridData gdlb = new GridData(200,25);
		String tsfLbName = curEntryName + "互感器次级配置";
		SwtUtil.createLabel(compTsf, tsfLbName, gdlb);
		btnChanelConnect = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "通道关联");
		btnAddTsf = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "添加次级");
		btnDelTsf = SwtUtil.createButton(compTsf, SwtUtil.bt_gd, SWT.BUTTON1, "删除次级");
		SwtUtil.createLabel(compTsf, "			", new GridData(SWT.DEFAULT,10));
		GridData gdSpan_3 = new GridData(GridData.FILL_BOTH);
		gdSpan_3.horizontalSpan = 3;
		
		GridData gdSpan_4 = new GridData(GridData.FILL_BOTH);
		gdSpan_4.horizontalSpan = 4;
		tableCtvtsecondary = TableFactory.getTsfSecondaryTable(compTsf);
		tableCtvtsecondary.getTable().setLayoutData(gdSpan_4);
		//保护采样值
		Composite compProtect = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		compProtect.setLayout(SwtUtil.getGridLayout(2));
		String protLbName = curEntryName + "保护采样值配置";
		SwtUtil.createLabel(compProtect, protLbName, gdlb);
		btnSampleConnect = SwtUtil.createButton(compProtect, SwtUtil.bt_gd, SWT.BUTTON1, "采样关联");
		SwtUtil.createLabel(compProtect, "			", new GridData(SWT.DEFAULT,10));
		tableProtectSample = TableFactory.getProtectSampleTalbe(compProtect);
		tableProtectSample.getTable().setLayoutData(gdSpan_3);
		//开关刀闸状态
		Composite compSwitch = SwtUtil.createComposite((Composite) controls[2], gridData, 1);
		compSwitch.setLayout(SwtUtil.getGridLayout(2));
		//开关刀闸状态-左侧
		GridData leftdata = new GridData(GridData.FILL_VERTICAL);
		leftdata.widthHint = 640;
		Composite comLeft = SwtUtil.createComposite(compSwitch, leftdata, 1);
		comLeft.setLayout(SwtUtil.getGridLayout(2));
		GridData gdlb_2 = new GridData(200,25);
		gdlb_2.horizontalSpan = 2;
		String switchLbName = "开关刀闸状态配置";
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
		
		tableSluiceStatus = TableFactory.getSluiceStatusTable(comRight);
		tableSluiceStatus.getTable().setLayoutData(gdSpan_3);
	}
	
	@Override
	public void init() {
		ctvtsecondaryService = new CtvtsecondaryService();
        equipmentEntityService = new EquipmentEntityService();
		protmmxuService = new ProtmmxuService();
		statedataService = new StatedataService();
		poutEntityService = new PoutEntityService();
		
		EditorConfigData data = (EditorConfigData)super.getInput().getData();
		this.curEntryName = data.getIedName();
		this.bayEntity = (Tb1042BayEntity) data.getData();
		this.iedService = new IedEntityService();
		if(bayEntity == null) {
			this.iedEntities = iedService.getIedList();
		} else {
			this.iedEntities = iedService.getIedEntityByBay(bayEntity);
		}
		
		if(iedEntities.size() < 1) {
			comboItems = new String[]{"装置为空"};
		} else {
			comboDvData = filterIedByTypes(iedEntities);
			List<String> lstComboDevItem = new ArrayList<>();
			for (Tb1046IedEntity tb1046IedEntity : comboDvData) {
				lstComboDevItem.add(tb1046IedEntity.getF1046Name());
			}
			comboItems = new String[lstComboDevItem.size()];
			comboItems = lstComboDevItem.toArray(comboItems);
		}
		super.init();
	}
	
	private List<Tb1046IedEntity> filterIedByTypes(
			List<Tb1046IedEntity> ied) {
		int[] terminal = EnumIedType.TERMINAL_DEVICE.getTypes();
		
		int[] unit = EnumIedType.UNIT_DEVICE.getTypes();
		
		List<Integer> types = new ArrayList<>();
		for (int i : unit) {
			types.add(i);
			}
		for (int i : terminal) {
			types.add(i);	
			}
		List<Tb1046IedEntity> filterResult = new ArrayList<>();
		for (Tb1046IedEntity tb1046IedEntity : ied) {
			if(types.contains(tb1046IedEntity.getF1046Type())) {
				filterResult.add(tb1046IedEntity);
			}
		}
		return filterResult;
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
							curEntryName, ctvtsecondaryEntity);
					chnDialog.open();
				} else if(object == btnSampleConnect) {
					Object obj = tableProtectSample.getSelection();
					if(obj == null) {
						return;
					}
					Tb1066ProtmmxuEntity protmmxuEntity = (Tb1066ProtmmxuEntity) obj;
					SampleConnectDialog sampleDialog = new SampleConnectDialog(SwtUtil.getDefaultShell(),
							curEntryName, protmmxuEntity, iedEntities);
					sampleDialog.open();
				} else if(object == btnAdd) {
					Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) tableSluiceStatus.getSelection();
					Tb1043EquipmentEntity equipmentEntity = (Tb1043EquipmentEntity) tableSwitchStatus.getSelection();
					equipmentEntity.setTb1016StatedataEntity(statedataEntity);	
					equipmentEntity.setF1016Code(statedataEntity.getF1016Code());
					statedataEntity.setParentCode(equipmentEntity.getF1043Code());
					statedataEntity.setF1011No(equipmentEntity.getF1043Type());
					BeanDaoImpl.getInstance().update(statedataEntity);
					tableSwitchStatus.refresh();
					tableSwitchStatus.getTable().layout();
				} else if(object == btnSearch) {
					if(tableSluiceStatuData == null) {
						return;
					}
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
					initTableSluiceStatus(curComboSelIdx);
					tableSluiceStatus.getTable().layout();
				} else if(object == btnAddTsf) {
					Tb1043EquipmentEntity select = null;
					SelectEquiDialog dialog = new SelectEquiDialog(getShell(), bayEntity);
					if(dialog.open() == IDialogConstants.OK_ID) {
						select = dialog.getSelect();
					}
					if(select == null) {
						 return;
					}
					Tb1067CtvtsecondaryEntity defaultRow = (Tb1067CtvtsecondaryEntity) tableCtvtsecondary.getDefaultRow();
					ctvtsecondaryService.addCtvtsecondary(select, defaultRow);
					tableCtvtsecondary.addRow(defaultRow);
					tableCtvtsecondary.refresh();
					
				} else if(object == btnDelTsf) {
					ctvtsecondaryService.delCtvtsecondary((Tb1067CtvtsecondaryEntity) 
							tableCtvtsecondary.getSelection());
					tableCtvtsecondary.removeSelected();
					tableCtvtsecondary.refresh();
					
				} else if(object == tabFolder) {
					loadDataByTbItem(tabFolder.getSelectionIndex());
				}
			}
		};
		tabFolder.addSelectionListener(sleListener);
		btnDelTsf.addSelectionListener(sleListener);
		btnAddTsf.addSelectionListener(sleListener);
		btnChanelConnect.addSelectionListener(sleListener);
		btnSampleConnect.addSelectionListener(sleListener);
		btnAdd.addSelectionListener(sleListener);
		btnSearch.addSelectionListener(sleListener);
		comboDevice.addSelectionListener(sleListener);
	}

	private void initTableSluiceStatus(int curComboSelIdx) {
		if(DataUtils.listNotNull(comboDvData)) {
			Tb1046IedEntity iedEntity = comboDvData.get(curComboSelIdx);
			tableSluiceStatuData = getStateDataByIed(iedEntity);
			tableSluiceStatus.setInput(tableSluiceStatuData);
		}
	}
	
	private void loadDataByTbItem(int idx) {
		if(this.bayEntity == null) {
			switch (idx) {
			case 0:
				//查询所有间隔下所有互感器
				if(!DataUtils.listNotNull(entities)) {
					entities = equipmentEntityService.getEquipmentList();
				}
				
				//查询互感器集合下关联的所有互感器次级
				if(!DataUtils.listNotNull(ctvtsecondaryEntities)) {
					ctvtsecondaryEntities = 
							ctvtsecondaryService.getCtvtsecondaryEntitiesByEquEntity(entities);
				}
				break;
			case 1:
				//查找互感器集合下关联的所有保护采样
				if(!DataUtils.listNotNull(protmmxuEntities)) {
					protmmxuEntities = protmmxuService.getProtmmxuByCtvtsecondary(ctvtsecondaryEntities);
				}
				break;
			case 2:
				//初始化开关刀闸状态左表
				if(!DataUtils.listNotNull(statedataEntities)) {
					statedataEntities = filterEquByTypes();
				}
				//初始化开关刀闸状态右表
				initTableSluiceStatus(0);
				break;
			default:
				break;
			}
		} else {
			switch (idx) {
			case 0:
				//根据当前节点：间隔，查询间隔下所有互感器
				if(!DataUtils.listNotNull(entities)) {
					entities = equipmentEntityService.getEquipmentEntitysByBayEntity(bayEntity);
				}
				//查询互感器集合下关联的所有互感器次级
				if(!DataUtils.listNotNull(ctvtsecondaryEntities)) {
				ctvtsecondaryEntities = 
						ctvtsecondaryService.getCtvtsecondaryEntitiesByEquEntity(entities);
					}
				break;
			case 1:
				//查找互感器集合下关联的所有保护采样
				if(!DataUtils.listNotNull(protmmxuEntities)) {
					protmmxuEntities = protmmxuService.getProtmmxuByCtvtsecondary(ctvtsecondaryEntities);
				}
				break;
			case 2:
				//初始化开关刀闸状态左表
				if(!DataUtils.listNotNull(statedataEntities)) {
					statedataEntities = filterEquByTypes();
				}
				//初始化开关刀闸状态右表
				initTableSluiceStatus(0);
				break;
			default:
				break;
			}
		}
		tableCtvtsecondary.setInput(ctvtsecondaryEntities);
		tableProtectSample.setInput(protmmxuEntities);
		tableSwitchStatus.setInput(statedataEntities);
	}

	private List<Tb1043EquipmentEntity> filterEquByTypes() {
		List<Tb1043EquipmentEntity> temp = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : entities) {
			if(tb1043EquipmentEntity.getF1043Type() == EnumEquipmentType.DIS.getCode()
					|| tb1043EquipmentEntity.getF1043Type() == EnumEquipmentType.CBR.getCode()) {
				temp.add(tb1043EquipmentEntity);
			}
		}
		return temp;
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
	
	@Override
	public void initData() {
		if(this.bayEntity == null) {
			//查询所有间隔下所有互感器
			entities = equipmentEntityService.getEquipmentList();
			//查询互感器集合下关联的所有互感器次级
			ctvtsecondaryEntities = 
					ctvtsecondaryService.getCtvtsecondaryEntitiesByEquEntity(entities);
		} else {
			//根据当前节点：间隔，查询间隔下所有互感器
			entities = equipmentEntityService.getEquipmentEntitysByBayEntity(bayEntity);
			//查询互感器集合下关联的所有互感器次级
			ctvtsecondaryEntities = 
					ctvtsecondaryService.getCtvtsecondaryEntitiesByEquEntity(entities);
		}
		tableCtvtsecondary.setInput(ctvtsecondaryEntities);
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
