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
import org.eclipse.jface.dialogs.IDialogConstants;
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
import com.shrcn.found.common.event.EventConstants;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ProgressManager;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.dialog.ModelCompareDialog;
import com.synet.tool.rsc.dialog.SelectRuleDialog;
import com.synet.tool.rsc.io.TemplateExport;
import com.synet.tool.rsc.io.TemplateImport;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
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
import com.synet.tool.rsc.service.PoutEntityService;
import com.synet.tool.rsc.service.RcdchannelaEntityService;
import com.synet.tool.rsc.service.RcdchanneldEntityService;
import com.synet.tool.rsc.service.SgfcdaEntityService;
import com.synet.tool.rsc.service.SpfcdaEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.service.StrapEntityService;
import com.synet.tool.rsc.ui.TableFactory;
import com.synet.tool.rsc.ui.table.DevKTable;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

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
	private Button btnAddWarn;
	private Button btnDelWarn;
	private DevKTable tableDeviceName;
	private DevKTable tableBoardName;
	private DevKTable tableLogLinkName;
	private Tb1046IedEntity iedEntity;
	private List<Tb1046IedEntity> iedEntityList;
	private PoutEntityService poutEntityService;
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
	private List<Tb1061PoutEntity> poutEntities;
	private List<Tb1069RcdchannelaEntity> rcdchannelaEntities;
	private List<Tb1072RcdchanneldEntity> rcdchanneldEntities;
	private CTabFolder tabFProtect;
	private DictManager dic;
	private HashMap<Integer, DevKTable> tableMapper;
	private CTabFolder tabFdCfg;
	private Button btnApplyRule;
	private StatedataService statedataService;
	private AnalogdataService analogdataService;
	
	

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
		poutEntityService = new PoutEntityService();
		circuitEntityService = new CircuitEntityService();
		boardEntityService = new BoardEntityService();
		logicallinkEntityService = new LogicallinkEntityService();
		rcdChnlaService = new RcdchannelaEntityService();
		rcdChnLdService = new RcdchanneldEntityService();
		statedataService = new StatedataService();
		analogdataService = new AnalogdataService();
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
		comp.setLayout(SwtUtil.getGridLayout(5));
		if (!needConfig()) {
			SwtUtil.createLabel(comp, "当前装置无需配置！", new GridData(780, SWT.DEFAULT));
		} else {
			SwtUtil.createLabel(comp, "", new GridData(700, SWT.DEFAULT));
			btnApplyRule = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "应用规则");
			btnTempCamp = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "对比模版");
			btnTempQuote = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "引用模版");
			btnTempSave = SwtUtil.createButton(comp, SwtUtil.bt_gd, SWT.BUTTON1, "保存模版");
			createCompByEntryName(comp);
		}
	}
	
	private boolean needConfig() {
		int type = iedEntity.getF1046Type();
		switch (type) {
		case DBConstants.IED_PROT:
		case DBConstants.IED_MU:
		case DBConstants.IED_MT:
		case DBConstants.IED_TERM:
			return true;
		}
		return false;
	}
	
	private void createCompByEntryName(Composite comp) {
		GridData gdSpan_5 = new GridData(GridData.FILL_BOTH);
		gdSpan_5.horizontalSpan = 5;
		int type = iedEntity.getF1046Type();
		switch (type) {
		case DBConstants.IED_PROT:
			createProtectCmp(comp, gdSpan_5);
			break;
		case DBConstants.IED_MU:
		case DBConstants.IED_MT:
			createMergeUnitCmp(comp, gdSpan_5);
			break;
		case DBConstants.IED_TERM:
			createIedCmp(comp, gdSpan_5);
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
		if (!needConfig()) {
			return;
		}
		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if(obj == tabFolder) {
					initTbDataByTbName(tabFolder.getSelection().getText());
				} else if(obj == tabFProtect) {
					initProTbDataByTbName(tabFProtect.getSelection().getText());
				} else if(obj == btnAddWarn) {
					config();
				} else if(obj == btnDelWarn) {
					delConfig();
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
							new TemplateImport(iedEntity).execute(monitor);
							EventManager.getDefault().notify(EventConstants.REFRESH_EIDTOR, null);
						}
					});
					DialogHelper.showAsynInformation("引入模版结束！");
					
					
				} else if(obj == btnApplyRule) {
					SelectRuleDialog selectRuleDialog = new SelectRuleDialog(getShell());
					if(selectRuleDialog.open() == IDialogConstants.OK_ID) {
						List<Rule> rulesSelect = selectRuleDialog.getRulesSelect();
						List<Integer> rulesId = new ArrayList<>();
						for (Rule rule : rulesSelect) {
							rulesId.add(rule.getId());
						}
						applyRule(rulesId);
					}
				} else if(obj == btnTempCamp) {
					ModelCompareDialog dialog = new ModelCompareDialog(getShell(), iedEntity);
					dialog.open();
				}
			}
			
		};
		if(tabFProtect != null) {
			tabFProtect.addSelectionListener(selectionListener);
		}
		tabFolder.addSelectionListener(selectionListener);
		btnAddWarn.addSelectionListener(selectionListener);
		btnDelWarn.addSelectionListener(selectionListener);
		btnTempCamp.addSelectionListener(selectionListener);
		btnTempQuote.addSelectionListener(selectionListener);
		btnTempSave.addSelectionListener(selectionListener);
		btnApplyRule.addSelectionListener(selectionListener);
	}
	
	/**
	 * 规则应用
	 * @param rulesSelect 
	 */
	private void applyRule(List<Integer> rulesId) {
		applyProtectStrapRule(rulesId);
		applyMmsfcdaRule(rulesId);
		applyPoutRule(rulesId);
	}

	private void applyPoutRule(List<Integer> rulesId) {
		PoutEntityService poutEntityService = new PoutEntityService();
		List<Tb1061PoutEntity> poutEntities = poutEntityService.getByIed(iedEntity);
		if(!DataUtils.listNotNull(poutEntities)) {
			return;
		}
		boolean flag = false;
		for (Tb1061PoutEntity tb1061PoutEntity : poutEntities) {
			flag = false;
			String datSet = tb1061PoutEntity.getCbEntity().getDataset();
			String f1061RefAddr = tb1061PoutEntity.getF1061RefAddr();
			String lnName = getLnName(f1061RefAddr);
			String doName = getDoName(f1061RefAddr);
			String doDesc = tb1061PoutEntity.getF1061Desc();
			Rule f1011no = F1011_NO.getType(datSet, lnName, doName, doDesc, null);
			if(f1011no == null || !rulesId.contains(f1011no.getId())) {
				continue;
			}
			int newTypeId = f1011no.getId();
			Tb1006AnalogdataEntity algdata = tb1061PoutEntity.getAlgdata();
			
			if(algdata != null) {
				if(algdata.getF1011No() != newTypeId) {
					algdata.setF1011No(newTypeId);
					poutEntityService.update(algdata);
					flag = true;
				}
			}
			Tb1016StatedataEntity stdata = tb1061PoutEntity.getStdata();
			if(stdata != null) {
				if(stdata.getF1011No() != newTypeId) {
					stdata.setF1011No(newTypeId);
					poutEntityService.update(stdata);
					flag = true;
				}
			}
			if(flag) {
				console.append("开出虚端子\"" + tb1061PoutEntity.getF1061RefAddr() + "(" + tb1061PoutEntity.getF1061Desc() +
						")\" 关联的数据集类型已改变，新类型为 \"" + F1011_NO.getNameById(newTypeId) +
						"\" 。");
			}
		}
		
		if(tableVirtualTerminalOut != null) {
			tableVirtualTerminalOut.setInput(poutEntities);
		}
		console.append("规则已应用于开出虚端子！");
	}

	private void applyMmsfcdaRule(List<Integer> rulesId) {
		List<Tb1058MmsfcdaEntity> temp = new ArrayList<>();
		if(!DataUtils.listNotNull(mmsfcdasProtcAction)) {
			//保护信息-保护动作
			String[] names = DictManager.getInstance().getDictNames("DS_DIN");
			mmsfcdasProtcAction = 
					mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
		}
		if(!DataUtils.listNotNull(mmsfcdasProtcMeaQua)) {
			//保护信息-保护测量量
			String[] names = DictManager.getInstance().getDictNames("DS_AIN");
			mmsfcdasProtcMeaQua = 
					mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
		}
		temp.addAll(mmsfcdasProtcMeaQua);
		temp.addAll(mmsfcdasProtcAction);
		if(!DataUtils.listNotNull(temp)) {
			return;
		}
		for (Tb1058MmsfcdaEntity mmsfcdaEntity : temp) {
			String datSet = mmsfcdaEntity.getTb1054RcbByF1054Code().getF1054Dataset();
			String lnName = getLnName(mmsfcdaEntity.getF1058RefAddr());
			String doName = getDoName(mmsfcdaEntity.getF1058RefAddr());
			String doDesc = mmsfcdaEntity.getF1058Desc();
			int f1058DataType = mmsfcdaEntity.getF1058DataType();
			Rule f1011no = F1011_NO.getType(datSet, lnName, doName, doDesc, null);
			if(f1011no == null || !rulesId.contains(f1011no.getId())) {
				continue;
			}
			
			Tb1006AnalogdataEntity analogdataEntity = analogdataService.
					getAnologByCodes(mmsfcdaEntity.getDataCode());
			Tb1016StatedataEntity statedataEntity = statedataService.
					getStateDataByCode(mmsfcdaEntity.getDataCode());
			int newTypeId = f1011no.getId();
			
			if(analogdataEntity!= null) {
				if (f1058DataType != newTypeId ) {
					mmsfcdaEntity.setF1058DataType(newTypeId);
					analogdataEntity.setF1011No(newTypeId);
					mmsfcdaService.save(analogdataEntity);
					mmsfcdaService.save(mmsfcdaEntity);
					console.append("保护测量量\"" + mmsfcdaEntity.getF1058RefAddr() + "(" + mmsfcdaEntity.getF1058Desc() +
							")\" 类型已改变：原类型为 \"" + F1011_NO.getNameById(f1058DataType) +
							"\" ，新类型为 \"" + F1011_NO.getNameById(newTypeId) +
							"\" 。");
				}
			}
			
			if(statedataEntity != null) {
				if (f1058DataType != newTypeId ) {
					mmsfcdaEntity.setF1058DataType(newTypeId);
					statedataEntity.setF1011No(newTypeId);
					mmsfcdaService.save(statedataEntity);
					mmsfcdaService.save(mmsfcdaEntity);
					console.append("保护动作\"" + mmsfcdaEntity.getF1058RefAddr() + "(" + mmsfcdaEntity.getF1058Desc() +
							")\" 类型已改变：原类型为 \"" + F1011_NO.getNameById(f1058DataType) +
							"\" ，新类型为 \"" + F1011_NO.getNameById(newTypeId) +
							"\" 。");
				}
			}
		}
		tableProtectAction.setInput(mmsfcdasProtcAction);
		tableProtectMeaQuantity.setInput(mmsfcdasProtcMeaQua);
		console.append("规则已应用于报告FCDA点类型！");
	}

	private void applyProtectStrapRule(List<Integer> rulesId) {
		//保护压板-规则应用
		if(!DataUtils.listNotNull(staEntities)) {
			return;
		}
		for (Tb1064StrapEntity tb1064StrapEntity : staEntities) {
			Tb1016StatedataEntity statedata = tb1064StrapEntity.getStatedata();
			Tb1058MmsfcdaEntity mmsfcdaEntity = statedata.getTb1058FcdaByF1058Code();
			String datSet = mmsfcdaEntity.getTb1054RcbByF1054Code().getF1054Dataset();
			String lnName = getLnName(mmsfcdaEntity.getF1058RefAddr());
			String doName = getDoName(mmsfcdaEntity.getF1058RefAddr());
			String doDesc = mmsfcdaEntity.getF1058Desc();
			int f1064Type = tb1064StrapEntity.getF1064Type();
			Rule f1011no = F1011_NO.getType(datSet, lnName, doName, doDesc, null);
			if(f1011no == null || !rulesId.contains(f1011no.getId())) {
				continue;
			}
			int newTypeId = f1011no.getId();
			if (f1064Type != newTypeId) {
				tb1064StrapEntity.setF1064Type(newTypeId);
				statedata.setF1011No(newTypeId);
				mmsfcdaService.save(statedata);
				mmsfcdaService.save(tb1064StrapEntity);
				console.append("压板 \"" + mmsfcdaEntity.getF1058RefAddr() + "(" + mmsfcdaEntity.getF1058Desc() +
						")\" 类型已改变：原类型为 \"" + F1011_NO.getNameById(f1064Type) +
						"\" ，新类型为 \"" + F1011_NO.getNameById(newTypeId) +
						"\" 。");
			}
		}
		tableProtectPlate.setInput(staEntities);
		console.append("规则已应用于压板类型！");
	}

	
	private String getDoName(String f1058RefAddr) {
		char tag = '.';
		if(f1058RefAddr.contains("$")) {
			tag = '$';
		} 
		String temp = f1058RefAddr.substring(f1058RefAddr.indexOf(tag) + 1, f1058RefAddr.length());
		String doName = temp.substring(temp.indexOf(tag) + 1, temp.length());
		int p = doName.indexOf(tag);
		if (p > 0) {
			doName = doName.substring(0, p);
		}
		return doName;
	}

	private String getLnName(String f1058RefAddr) {
		char tag = '.';
		if(f1058RefAddr.contains("$")) {
			tag = '$';
		} 
		String temp = f1058RefAddr.substring(0, f1058RefAddr.indexOf(tag));
		return DataUtils.getLnClass(temp);
	}

	/**
	 * 删除装置告警关联
	 */
	private void delConfig() {
		String code = iedEntity.getF1046Code();
		if(code == null) {
			return;
		}
		for (Object objMms : tableDeviceWarning.getSelections()) {
			if(objMms == null) {
				return;
			}
			Tb1058MmsfcdaEntity mms = (Tb1058MmsfcdaEntity) objMms;
			StatedataService statedataService = new StatedataService();
			Tb1016StatedataEntity stateData = statedataService.getStateDataByCode(mms.getDataCode());
			if(stateData == null) {
				ConsoleManager.getInstance().append("关联失败！没有找到状态量数据对象");
				return;
			}
			stateData.setParentCode(code);
			mms.setParentCode(code);
			beandao.update(mms);
			statedataService.update(stateData);
		}
		tableDeviceWarning.refresh();
		ConsoleManager.getInstance().append("关联已取消。");
	}
	/**
	 * 添加装置告警关联
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
		int index = tableDeviceWarning.getSelectRowNums()[0];
		Tb1058MmsfcdaEntity mms = (Tb1058MmsfcdaEntity) objMms;
		StatedataService statedataService = new StatedataService();
		Tb1016StatedataEntity stateData = statedataService.getStateDataByCode(mms.getDataCode());
		if(stateData == null) {
			ConsoleManager.getInstance().append("关联失败！没有找到状态量数据对象");
			return;
		}
		stateData.setParentCode(code);
		mms.setParentCode(code);
		String mmsSql = "update tb1058_mmsfcda set Parent_CODE='" + code + "'" +
				" where F1058_CODE='" + mms.getF1058Code() + "'";
		String stSql = "update tb1016_statedata set Parent_CODE='" + code + "'" +
				" where F1016_CODE='" + stateData.getF1016Code() + "'";
		hqldao.updateBySql(mmsSql);
		hqldao.updateBySql(stSql);
		tableDeviceWarning.refresh();
		tableDeviceWarning.setSelection(index);
		ConsoleManager.getInstance().append("关联成功。");
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
				String[] names = DictManager.getInstance().getDictNames("DS_DIN");
				mmsfcdasProtcAction = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
				tableProtectAction.setInput(mmsfcdasProtcAction);
			}
			break;
		case RSCConstants.PROTECT_MEAQU:
			if(!DataUtils.listNotNull(mmsfcdasProtcMeaQua)) {
				//保护信息-保护测量量
				String[] names = DictManager.getInstance().getDictNames("DS_AIN");
				mmsfcdasProtcMeaQua = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
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
				String[] names = DictManager.getInstance().getDictNames("DS_STATE");
				mmsfcdaEntitiesRun = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
					tableRunState.setInput(mmsfcdaEntitiesRun);	
			}
			
			break;
		case RSCConstants.DEV_WARNING:
			if(!DataUtils.listNotNull(mmsfcdaEntities)) {
				String[] names = DictManager.getInstance().getDictNames("DS_WARN");
				mmsfcdaEntities = mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
				// 补充运行工况
				names = DictManager.getInstance().getDictNames("DS_STATE");
				List<Tb1058MmsfcdaEntity> mmsfcdaEntitiesRun = 
						mmsfcdaService.getMmsdcdaByDataSet(iedEntity, names);
				if (mmsfcdaEntitiesRun!=null && mmsfcdaEntitiesRun.size()>0) {
					mmsfcdaEntities.addAll(mmsfcdaEntitiesRun);
				}
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
				//开出虚端子
				poutEntities = poutEntityService.getPoutEntityByProperties(iedEntity, null);
				//虚端子压板
				circuitEntities = circuitEntityService.getByIed(iedEntity);
				//开出
				tableVirtualTerminalOut.setInput(poutEntities);
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
		if (!needConfig()) {
			return;
		}
		//板卡端口 
		List<Tb1048PortEntity> portEntities = portService.getBoardPortByIed(iedEntity);
		tableBoardPort.setInput(portEntities);
		super.initData();
	}
	
	@SuppressWarnings("unused")
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
		List<String> mmsAnalog = analogdataService.getAnologNameByIed(iedEntity);
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
		btnAddWarn = SwtUtil.createButton(cmpCentor, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "<-");
		btnDelWarn = SwtUtil.createButton(cmpCentor, new GridData(40, SWT.DEFAULT), SWT.BUTTON1, "->");
		
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
