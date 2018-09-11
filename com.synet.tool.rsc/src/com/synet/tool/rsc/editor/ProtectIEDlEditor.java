/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.io.TemplateExport;
import com.synet.tool.rsc.io.TemplateImport;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1069RcdchannelaEntity;
import com.synet.tool.rsc.model.Tb1072RcdchanneldEntity;
import com.synet.tool.rsc.service.AnalogdataService;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.BoardPortService;
import com.synet.tool.rsc.service.CircuitEntityService;
import com.synet.tool.rsc.service.EquipmentEntityService;
import com.synet.tool.rsc.service.LogicallinkEntityService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.RcdchannelaEntityService;
import com.synet.tool.rsc.service.RcdchanneldEntityService;
import com.synet.tool.rsc.service.SgfcdaEntityService;
import com.synet.tool.rsc.service.SpfcdaEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.service.StrapEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;

/**
 * 保护信息模型->装置树菜单编辑器。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class ProtectIEDlEditor extends BaseConfigEditor {
	
	private Button btnTempCamp;
	private Button btnTempQuote;
	private Button btnTempSave;
	private GridData gridData;
	private DevKTable tableBoardPort;
	private DevKTable tableProtectValue;
	private DevKTable tableProtParam;
	private DevKTable tableProtectPlate;
	private DevKTable tableProtectAction;
	private DevKTable tableProtectMeaQuantity;
	private DevKTable tableDeviceWarning;
	private DevKTable tableRunState;
	private DevKTable tableLogicalLink;
	private DevKTable tableVirtualTerminalOut;
	private DevKTable tableVirtualTerminalIn;
	private DevKTable tableAnalogChn;
	private DevKTable tableCriteriaChn;
	private Button btnAdd;
	private DevKTable tableDeviceName;
	private DevKTable tableBoardName;
	private DevKTable tableLogLinkName;
	private Tb1046IedEntity iedEntity;
	private List<Tb1046IedEntity> iedEntityList;
	private CircuitEntityService circuitEntityService;
	private CTabFolder tabFolder;
	private MmsfcdaService mmsfcdaService;
	private BoardPortService portService;
	private SgfcdaEntityService sgfcdaEntityService;
	private SpfcdaEntityService spfcdaEntityService;
	private StrapEntityService strapEntityService;
	private BoardEntityService boardEntityService;
	private LogicallinkEntityService logicallinkEntityService;
	private RcdchannelaEntityService rcdChnlaService;
	private RcdchanneldEntityService rcdChnLdService;
	//
	private List<Tb1048PortEntity> portEntities;
	private List<Tb1059SgfcdaEntity> sgfcdaEntities;
	private List<Tb1060SpfcdaEntity> spfcdaEntities;
	private List<Tb1064StrapEntity> staEntities;
	private List<Tb1058MmsfcdaEntity> mmsfcdasProtcAction;
	private List<Tb1058MmsfcdaEntity> mmsfcdasProtcMeaQua;
	private List<Tb1058MmsfcdaEntity> mmsfcdaEntitiesRun;
	private List<Tb1058MmsfcdaEntity> mmsfcdaEntities;
	private List<Tb1047BoardEntity> boardEntities;
	private List<Tb1065LogicallinkEntity> logicallinkEntities;
	private List<Tb1063CircuitEntity> circuitEntities;
	private List<Tb1069RcdchannelaEntity> rcdchannelaEntities;
	private List<Tb1072RcdchanneldEntity> rcdchanneldEntities;
	private CTabFolder tabFProtect;
	private DictManager dic;
	private HashMap<Integer, DevKTable> tableMapper;
	private CTabFolder tabFdCfg;
	
	

	public ProtectIEDlEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		dic = DictManager.getInstance();
		iedEntityList = new ArrayList<>();
		mmsfcdaService = new MmsfcdaService();
		portService = new BoardPortService();
		sgfcdaEntityService = new SgfcdaEntityService();
		spfcdaEntityService = new SpfcdaEntityService();
		strapEntityService = new StrapEntityService();
		circuitEntityService = new CircuitEntityService();
		boardEntityService = new BoardEntityService();
		logicallinkEntityService = new LogicallinkEntityService();
		rcdChnlaService = new RcdchannelaEntityService();
		rcdChnLdService = new RcdchanneldEntityService();
		ConfigEditorInput input = (ConfigEditorInput) getInput();
		iedEntity = ((Tb1046IedEntity) ((EditorConfigData)input.getData()).getData());
		iedEntityList.add(iedEntity);
		gridData = new GridData(GridData.FILL_BOTH);
		tableMapper = new HashMap<Integer, DevKTable>();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		Composite comp = SwtUtil.createComposite(container, gridData, 1);
		comp.setLayout(SwtUtil.getGridLayout(4));
		SwtUtil.createLabel(comp, "", new GridData(780, SWT.DEFAULT));
		btnTempCamp = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "对比模版");
		btnTempQuote = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "引用模版");
		btnTempSave = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "保存模版");
		createCompByEntryName(comp);
	}
	
	private void createCompByEntryName(Composite comp) {
		GridData gdSpan_4 = new GridData(GridData.FILL_BOTH);
		gdSpan_4.horizontalSpan = 4;
		int type = iedEntity.getF1046Type();
		switch (type) {
		case DBConstants.IED_PROT:
			createProtectCmp(comp, gdSpan_4);
			break;
		case DBConstants.IED_MU:
		case DBConstants.IED_MT:
			createMergeUnitCmp(comp, gdSpan_4);
			break;
		case DBConstants.IED_TERM:
			createIedCmp(comp, gdSpan_4);
			break;
		default:
			break;
		}
		
	}

	/**
	 * 创建智能终端界面
	 * @param comp
	 * @param gdSpan_4
	 */
	private void createIedCmp(Composite comp, GridData gdSpan_4) {
		String[] tabNames = new String[]{RSCConstants.BOARD_PORT, RSCConstants.DEV_WARNING, RSCConstants.RUN_STATE, RSCConstants.CIRCUI_BOARD, RSCConstants.LOGICAL_LINK};
		Control[] controls = getControls(comp, gdSpan_4, tabNames);
		//板卡端口
		createBoardPortCmp((Composite) controls[0]);
		//装置告警
		createDeviceWarningCmp((Composite) controls[1]);
		//运行工况
		createRunStateCmp((Composite) controls[2]);
		//虚端子压板
		createVirtualTerminalPlateCmp((Composite) controls[3]);
		//逻辑链路
		createLogicalLinkCmp((Composite) controls[4]);
	}

	/**
	 * 创建合并单元界面
	 * @param comp
	 * @param gdSpan_4
	 */
	private void createMergeUnitCmp(Composite comp, GridData gdSpan_4) {
		String[] tabNames = new String[]{RSCConstants.BOARD_PORT, RSCConstants.DEV_WARNING, RSCConstants.RUN_STATE};
		Control[] controls = getControls(comp, gdSpan_4, tabNames);
		//板卡端口
		createBoardPortCmp((Composite) controls[0]);
		//装置告警
		createDeviceWarningCmp((Composite) controls[1]);
		//运行工况
		createRunStateCmp((Composite) controls[2]);
	}

	/**
	 * 创建保护界面
	 * @param comp
	 * @param gdSpan_4
	 */
	private void createProtectCmp(Composite comp, GridData gdSpan_4) {
		String[] tabNames = new String[]{RSCConstants.BOARD_PORT, RSCConstants.PROTECT_MSG,
				RSCConstants.DEV_WARNING, RSCConstants.RUN_STATE, RSCConstants.CIRCUI_BOARD, 
				RSCConstants.LOGICAL_LINK, RSCConstants.PROTECT_WAVE};
		Control[] controls = getControls(comp, gdSpan_4, tabNames);
		//板卡端口
		createBoardPortCmp((Composite) controls[0]);
		//保护信息
		createProtMsgCmp((Composite) controls[1]);
		//装置告警
		createDeviceWarningCmp((Composite) controls[2]);
		//运行工况
		createRunStateCmp((Composite) controls[3]);
		//虚端子压板
		createVirtualTerminalPlateCmp((Composite) controls[4]);
		//逻辑链路
		createLogicalLinkCmp((Composite) controls[5]);
		//保护录波
		createProtWaveCmp((Composite) controls[6]);
	}

	/**
	 * 创建TbFolder,返回子控件集合
	 * @param comp
	 * @param gdSpan_4
	 * @param tabNames
	 * @return
	 */
	private Control[] getControls(Composite comp, GridData gdSpan_4,
			String[] tabNames) {
		tabFolder = SwtUtil.createTab(comp, gdSpan_4, tabNames);
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		return controls;
	}

	protected void addListeners() {
		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == tabFolder) {
					initTbDataByTbName(tabFolder.getSelection().getText());
				} else if(obj == tabFProtect) {
					initProTbDataByTbName(tabFProtect.getSelection().getText());
				} else if(obj == btnAdd) {
					config();
				} else if(obj == btnTempSave) {
					ProgressManager.execute(new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							new TemplateExport(iedEntity).execute();
						}
					});
					DialogHelper.showAsynInformation("保存模版结束！");
				} else if(obj == btnTempQuote) {
					ProgressManager.execute(new IRunnableWithProgress() {
						@Override
						public void run(IProgressMonitor monitor) throws InvocationTargetException,
								InterruptedException {
							new TemplateImport(iedEntity).execute();
						}
					});
					DialogHelper.showAsynInformation("引入模版结束！");
					
					
				} else if(obj == btnTempCamp) {
					//TODO
				}
			}
		};
		if(tabFProtect != null) {
			tabFProtect.addSelectionListener(selectionListener);
		}
		tabFolder.addSelectionListener(selectionListener);
		btnAdd.addSelectionListener(selectionListener);
		btnTempCamp.addSelectionListener(selectionListener);
		btnTempQuote.addSelectionListener(selectionListener);
		btnTempSave.addSelectionListener(selectionListener);
	}
	
	/**
	 * 装置告警关联
	 */
	private void config() {
		Object selection = tableMapper.get(tabFdCfg.getSelectionIndex()).getSelection();
		if(selection == null) {
			 return;
		}
		String code = null;
		if(selection instanceof Tb1046IedEntity) {
			code = ((Tb1046IedEntity) selection).getF1046Code();
		} else if(selection instanceof Tb1047BoardEntity) {
			code = ((Tb1047BoardEntity) selection).getF1047Code();
		} else if(selection instanceof Tb1065LogicallinkEntity) {
			code = ((Tb1065LogicallinkEntity) selection).getF1065Code();
		} 
		if(code == null) {
			return;
		}
		Object objMms = tableDeviceWarning.getSelection();
		if(objMms == null) {
			return;
		}
		Tb1058MmsfcdaEntity mms = (Tb1058MmsfcdaEntity) objMms;
		StatedataService statedataService = new StatedataService();
		Tb1016StatedataEntity stateData = statedataService.getStateDataByCode(mms.getDataCode());
		stateData.setParentCode(code);
		statedataService.update(stateData);
		tableDeviceWarning.getTable().layout();
		ConsoleManager.getInstance().append("关联成功！");
	}

	/**
	 * 保护信息-根据tb标签名称加载数据
	 * @param text
	 */
	private void initProTbDataByTbName(String text) {
		switch (text) {
		case RSCConstants.PROTECT_VALUE:
			if(!DataUtils.listNotNull(sgfcdaEntities)) {
				//保护信息-保护定值
				sgfcdaEntities = sgfcdaEntityService.getSgfcdaByIed(iedEntity);
				tableProtectValue.setInput(sgfcdaEntities);
			}
			break;
		case RSCConstants.PROTECT_PARAM:
			if(!DataUtils.listNotNull(spfcdaEntities)) {
				//保护信息-保护参数
				spfcdaEntities = spfcdaEntityService.getByIed(iedEntity);
				tableProtParam.setInput(spfcdaEntities);
			}
			break;
		case RSCConstants.PROTECT_BOARD:
			if(!DataUtils.listNotNull(staEntities)) {
				//保护信息-保护压板
				staEntities = strapEntityService.getByIed(iedEntity);
				tableProtectPlate.setInput(staEntities);
			}
			break;
		case RSCConstants.PROTECT_ACTION:
			if(!DataUtils.listNotNull(mmsfcdasProtcAction)) {
				//保护信息-保护动作
				mmsfcdasProtcAction = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity.getF1046Name(), "dsDin", 1);
				tableProtectAction.setInput(mmsfcdasProtcAction);
			}
			break;
		case RSCConstants.PROTECT_MEAQU:
			if(!DataUtils.listNotNull(mmsfcdasProtcMeaQua)) {
				//保护信息-保护测量量
				mmsfcdasProtcMeaQua = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity.getF1046Name(), "dsAin", 2);
				tableProtectMeaQuantity.setInput(mmsfcdasProtcMeaQua);
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 根据tb标签名称加载数据
	 * @param tabName
	 */
	private void initTbDataByTbName(String tabName) {
		switch (tabName) {
		case RSCConstants.BOARD_PORT:
			if(!DataUtils.listNotNull(portEntities)) {
				portEntities = portService.getBoardPortByIed(iedEntity);
				tableBoardPort.setInput(portEntities);
			}
			break;
		case RSCConstants.PROTECT_MSG:
			if(!DataUtils.listNotNull(sgfcdaEntities)) {
				//保护信息-保护定值
				sgfcdaEntities = sgfcdaEntityService.getSgfcdaByIed(iedEntity);
				tableProtectValue.setInput(sgfcdaEntities);
			}
			break;
		case RSCConstants.RUN_STATE:
			if(!DataUtils.listNotNull(mmsfcdaEntitiesRun)) {
				mmsfcdaEntitiesRun = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity.getF1046Name(), "dsCommState");
					tableRunState.setInput(mmsfcdaEntitiesRun);	
			}
			
			break;
		case RSCConstants.DEV_WARNING:
			if(!DataUtils.listNotNull(mmsfcdaEntities)) {
				mmsfcdaEntities = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity.getF1046Name(), "dsWarning");
					tableDeviceWarning.setInput(mmsfcdaEntities);
					tableDeviceName.setInput(iedEntityList);
			}
			if(!DataUtils.listNotNull(boardEntities)) {
					boardEntities = boardEntityService.getByIed(iedEntity);
					tableBoardName.setInput(boardEntities);	
			}
			if(!DataUtils.listNotNull(logicallinkEntities)) {
				logicallinkEntities  = logicallinkEntityService.getByRecvIed(iedEntity);
			}
			tableLogLinkName.setInput(logicallinkEntities);
			break;
		case RSCConstants.CIRCUI_BOARD:
			if(!DataUtils.listNotNull(circuitEntities)) {
				//虚端子压板
				circuitEntities = circuitEntityService.getByIed(iedEntity);
//				for (Tb1063CircuitEntity circui : circuitEntities) {
//					dic.addItemByType("REV_CONVCHK", circui.get);
//					dic.addItemByType("SEND_CONVCHK", circui.get);
//				}
				//开出
				tableVirtualTerminalOut.setInput(circuitEntities);
				//开入
				tableVirtualTerminalIn.setInput(circuitEntities);
			}
			break;
		case RSCConstants.LOGICAL_LINK:
			if(!DataUtils.listNotNull(logicallinkEntities)) {
				//逻辑链路
				logicallinkEntities = logicallinkEntityService.getByRecvIed(iedEntity);
			}
			tableLogicalLink.setInput(logicallinkEntities);
			break;
		case RSCConstants.PROTECT_WAVE:
			if(!DataUtils.listNotNull(rcdchannelaEntities)) {
				//保护录波-模拟量通道
				rcdchannelaEntities = rcdChnlaService.getByIed(iedEntity);
				tableAnalogChn.setInput(rcdchannelaEntities);
			}
			if(!DataUtils.listNotNull(rcdchanneldEntities)) {
				//保护录波-状态量通道
				rcdchanneldEntities = rcdChnLdService.getByIed(iedEntity);
				tableCriteriaChn.setInput(rcdchanneldEntities);
			}
			break;
		default:
			break;
		}
	}
		
	@Override
	public void initData() {
		//板卡端口 
		List<Tb1048PortEntity> portEntities = portService.getBoardPortByIed(iedEntity);
		tableBoardPort.setInput(portEntities);
		super.initData();
	}
	
	private void initTableDict() {
		dic.removeDict("EQU_ANALOG");
		dic.removeDict("SV_ANALOG");
		dic.removeDict("MMS_ANALOG");
		dic.removeDict("GOOSE_CIRCUIT");
		dic.removeDict("MMS__CIRCUIT");
		EquipmentEntityService qEntityService = new EquipmentEntityService();
		//互感器字典
		List<String> equNames = qEntityService.getEquipmentByType();
		dic.addDict("EQU_ANALOG", "EQU_ANALOG", DataUtils.createDictItems(equNames));
		//SV虚端子字典
		List<String> svPoutNames = circuitEntityService.getByIedAndTypes(iedEntity, true);
		dic.addDict("SV_ANALOG", "SV_ANALOG", DataUtils.createDictItems(svPoutNames));
		//MMS模拟量字典
		AnalogdataService analogdataService = new AnalogdataService();
		List<String> mmsAnalog = analogdataService.getAnologByIed(iedEntity);
		dic.addDict("MMS_ANALOG", "MMS_ANALOG", DataUtils.createDictItems(mmsAnalog));
		//GOOSE虚端子字典
		List<String> goosePoutDescs = circuitEntityService.getByIedAndTypes(iedEntity, false);
		dic.addDict("GOOSE_CIRCUIT", "GOOSE_CIRCUIT", DataUtils.createDictItems(goosePoutDescs));
		//MMS状态量字典 
		StatedataService statedataService = new StatedataService();
		List<String> mmsCircuit = statedataService.getStateDataByIed(iedEntity);
		dic.addDict("MMS__CIRCUIT", "MMS__CIRCUIT", DataUtils.createDictItems(mmsCircuit));
		
	}

	/**
	 * 创建板卡端口界面
	 * @param com
	 * @return
	 */
	private Composite createBoardPortCmp(Composite com) {
		Composite cmpBoardPort = SwtUtil.createComposite(com, gridData, 1);
		cmpBoardPort.setLayout(SwtUtil.getGridLayout(1));
		tableBoardPort = TableFactory.getBoardPortTable(cmpBoardPort);
		tableBoardPort.getTable().setLayoutData(gridData);
		return cmpBoardPort;
	}
	
	/**
	 * 创建保护信息界面
	 * @param com
	 * @return
	 */
	private Composite createProtMsgCmp(Composite com) {
		Composite cmpProtMsg = SwtUtil.createComposite(com, gridData, 1);
		cmpProtMsg.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{RSCConstants.PROTECT_VALUE, 
				RSCConstants.PROTECT_PARAM, RSCConstants.PROTECT_BOARD,
				RSCConstants.PROTECT_ACTION, RSCConstants.PROTECT_MEAQU};
		
		tabFProtect = SwtUtil.createTab(cmpProtMsg, gridData, tabNames );
		tabFProtect.setSelection(0);
		Control[] controls = tabFProtect.getChildren();
		//保护定值
		Composite cmpProtValue = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		tableProtectValue = TableFactory.getProtectValueTable(cmpProtValue);
		tableProtectValue.getTable().setLayoutData(gridData);
		//保护参数
		Composite cmpProtParam = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		tableProtParam = TableFactory.getProtectParamTable(cmpProtParam);
		tableProtParam.getTable().setLayoutData(gridData);
		//保护压板
		Composite cmpProtPlate = SwtUtil.createComposite((Composite) controls[2], gridData, 1);
		tableProtectPlate = TableFactory.getProtectBoardTable(cmpProtPlate);
		tableProtectPlate.getTable().setLayoutData(gridData);
		//保护动作
		Composite cmpProtAction = SwtUtil.createComposite((Composite) controls[3], gridData, 1);
		tableProtectAction = TableFactory.getProtectActionTable(cmpProtAction);
		tableProtectAction.getTable().setLayoutData(gridData);
		//保护测量量
		Composite cmpProtMeaQuantity = SwtUtil.createComposite((Composite) controls[4], gridData, 1);
		tableProtectMeaQuantity = TableFactory.getProtectMeaQuantityTable(cmpProtMeaQuantity);
		tableProtectMeaQuantity.getTable().setLayoutData(gridData);
		
		return cmpProtMsg;
	}
	
	/**
	 * 创建装置告警界面
	 * @param com
	 * @return
	 */
	private Composite createDeviceWarningCmp(Composite com) {
		Composite cmpDeviceWarning = SwtUtil.createComposite(com, gridData, 1);
		cmpDeviceWarning.setLayout(SwtUtil.getGridLayout(3));
		
		Composite cmpLeft = SwtUtil.createComposite(cmpDeviceWarning, gridData, 1);
		cmpLeft.setLayout(SwtUtil.getGridLayout(1));
		
		tableDeviceWarning = TableFactory.getDeviceWarnTable(cmpLeft);
		tableDeviceWarning.getTable().setLayoutData(gridData);
		
		GridData gdCentor = new GridData(41, SWT.DEFAULT);
		Composite cmpCentor = SwtUtil.createComposite(cmpDeviceWarning, gdCentor, 1);
		cmpCentor.setLayout(SwtUtil.getGridLayout(1));
		btnAdd = SwtUtil.createButton(cmpCentor, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		
		GridData gdRight = new GridData(GridData.FILL_VERTICAL);
		gdRight.widthHint = 520;
		Composite cmpRight = SwtUtil.createComposite(cmpDeviceWarning, gdRight, 1);
		cmpRight.setLayout(SwtUtil.getGridLayout(1));
		
		String[] tabNames = new String[]{"装置", "板卡", RSCConstants.LOGICAL_LINK};
		tabFdCfg = SwtUtil.createTab(cmpRight, new GridData(GridData.FILL_BOTH), tabNames);
		tabFdCfg.setSelection(0);
		Control[] contros = tabFdCfg.getChildren();
		tableDeviceName = TableFactory.getDeviceNameTable((Composite) contros[0]);
		tableDeviceName.getTable().setLayoutData(gridData);
		
		tableBoardName = TableFactory.getBoardNameTable((Composite) contros[1]);
		tableBoardName.getTable().setLayoutData(gridData);
		
		tableLogLinkName = TableFactory.getLogicalLinkNameTable((Composite) contros[2]);
		tableLogLinkName.getTable().setLayoutData(gridData);
		
		tableMapper.put(0, tableDeviceName);
		tableMapper.put(1, tableBoardName);
		tableMapper.put(2, tableLogLinkName);
		return cmpDeviceWarning;
	}
	
	/**
	 * 创建运行工况界面
	 * @param com
	 * @return
	 */
	private Composite createRunStateCmp(Composite com) {
		Composite cmpRunState = SwtUtil.createComposite(com, gridData, 1);
		cmpRunState.setLayout(SwtUtil.getGridLayout(1));
		tableRunState = TableFactory.getRunStateTable(cmpRunState);
		tableRunState.getTable().setLayoutData(gridData);
		return cmpRunState;
	}
	
	/**
	 * 创建虚端子压板界面
	 * @param com
	 * @return
	 */
	private Composite createVirtualTerminalPlateCmp(Composite com) {
		Composite cmpVirtualTerminalPlate = SwtUtil.createComposite(com, gridData, 1);
		cmpVirtualTerminalPlate.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{"开出虚端子", "开入虚端子"};
		CTabFolder tabFolder = SwtUtil.createTab(cmpVirtualTerminalPlate, gridData, tabNames );
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//开出虚端子
		Composite cmpVirtualTerminalOut = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		tableVirtualTerminalOut = TableFactory.getVirtualTerminalOutTable(cmpVirtualTerminalOut);
		tableVirtualTerminalOut.getTable().setLayoutData(gridData);
		//开入虚端子
		Composite cmpVirtualTerminalIn = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		tableVirtualTerminalIn = TableFactory.getVirtualTerminalInTable(cmpVirtualTerminalIn);
		tableVirtualTerminalIn.getTable().setLayoutData(gridData);
		
		return cmpVirtualTerminalPlate;
	}
	
	/**
	 * 创建逻辑链路界面
	 * @param com
	 * @return
	 */
	private Composite createLogicalLinkCmp(Composite com) {
		Composite cmpLogicalLink = SwtUtil.createComposite(com, gridData, 1);
		cmpLogicalLink.setLayout(SwtUtil.getGridLayout(1));
		tableLogicalLink = TableFactory.getLogicalLinkTable(cmpLogicalLink);
		tableLogicalLink.getTable().setLayoutData(gridData);
		return cmpLogicalLink;
	}
	
	/**
	 * 创建保护录波界面
	 * @param com
	 * @return
	 */
	private Composite createProtWaveCmp(Composite com) {
		Composite cmpProtWave = SwtUtil.createComposite(com, gridData, 1);
		cmpProtWave.setLayout(SwtUtil.getGridLayout(1));
		String[] tabNames = new String[]{"模拟量通道", "状态量通道"};
		CTabFolder tabFolder = SwtUtil.createTab(cmpProtWave, gridData, tabNames );
		tabFolder.setSelection(0);
		Control[] controls = tabFolder.getChildren();
		//模拟量通道
		Composite cmpAnalogChn = SwtUtil.createComposite((Composite) controls[0], gridData, 1);
		tableAnalogChn = TableFactory.getAnalogChnTable(cmpAnalogChn);
		tableAnalogChn.getTable().setLayoutData(gridData);
		//状态量通道
		Composite cmpCriteriaChn = SwtUtil.createComposite((Composite) controls[1], gridData, 1);
		tableCriteriaChn = TableFactory.getCriteriaChnTable(cmpCriteriaChn);
		tableCriteriaChn.getTable().setLayoutData(gridData);
		
		return cmpProtWave;
	}
	
}
