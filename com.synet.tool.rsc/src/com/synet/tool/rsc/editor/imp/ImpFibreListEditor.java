/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.editor.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.editor.IEditorInput;
import com.shrcn.found.ui.util.SwtUtil;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.processor.ImportFibreListProcessor3;
import com.synet.tool.rsc.processor.LogcalAndPhyconnProcessor;
import com.synet.tool.rsc.service.CubicleEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.ui.TableFactory;

/**
 * 导入信息->光缆清册树菜单编辑器。
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2013-4-3
 */
public class ImpFibreListEditor extends ExcelImportEditor {
	
	private SubstationService substationService;
	private CubicleEntityService cubicleService;
	private PortEntityService portEntityService;
	private Button btAnalysis;
	
	public ImpFibreListEditor(Composite container, IEditorInput input) {
		super(container, input);
	}
	
	@Override
	public void init() {
		improtInfoService = new ImprotInfoService();
		map = new HashMap<String, IM100FileInfoEntity>();
		substationService = new SubstationService();
		cubicleService = new CubicleEntityService();
		portEntityService = new PortEntityService();
		super.init();
	}

	@Override
	public void buildUI(Composite container) {
		super.buildUI(container);
		container.setLayout(SwtUtil.getGridLayout(2));
		
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 150;
		titleList = SwtUtil.createList(container, gridData);
		
		int cols = 3;
		Composite cmpRight = SwtUtil.createComposite(container, new GridData(GridData.FILL_BOTH), cols);
		SwtUtil.createLabel(cmpRight, "", new GridData(GridData.FILL_HORIZONTAL));
		GridData btData = new GridData();
		btData.horizontalAlignment = SWT.RIGHT;
		
		Composite btComp = SwtUtil.createComposite(cmpRight, btData, 6);
		btAdd = SwtUtil.createPushButton(btComp, "添加", new GridData());
		btDelete = SwtUtil.createPushButton(btComp, "删除", new GridData());
		btCheck = SwtUtil.createPushButton(btComp, "冲突检查", new GridData());
		btImport = SwtUtil.createPushButton(btComp, "导入光缆", new GridData());
//		btExport = SwtUtil.createPushButton(btComp, "导出原始数据", new GridData());
		btExportCfgData  = SwtUtil.createPushButton(btComp, "导出配置数据", new GridData());
		btAnalysis = SwtUtil.createPushButton(btComp, "分析回路", new GridData());
		
		table = TableFactory.getFibreListTable(cmpRight);
		GridData tbData = new GridData(GridData.FILL_BOTH);
		tbData.horizontalSpan = cols;
		table.getTable().setLayoutData(tbData);
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object obj = e.getSource();
				if (obj == btAnalysis) {
					String[] selects = titleList.getSelection();
					if (selects != null && selects.length > 0) {
						doAnalysis();
					}
				}
			}
		};
		btAnalysis.addSelectionListener(listener);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doImport(IProgressMonitor monitor) {
		List<IM102FibreListEntity> temp = new ArrayList<>();
		List<IM102FibreListEntity> list = (List<IM102FibreListEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		for (IM102FibreListEntity entity : list) {
			if (entity.isOverwrite()) {
				temp.add(entity);
			}
		}
		//导入数据
		if (temp.size() > 0) {
			new ImportFibreListProcessor3().importData(temp, monitor);
		}
	}
	
	private void doAnalysis() {
		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		beanDao.deleteAll(Tb1073LlinkphyrelationEntity.class);
		//处理逻辑链路与物理回路关联(处理全部)
		new LogcalAndPhyconnProcessor().analysis();
		//确定TB1055_GCB和TB1056_SVCB表中F1071_CODE所代表的采集单元Code
		new LogcalAndPhyconnProcessor().analysisGCBAndSVCB();
	}

	//光缆清册为导入添加数据，只要检查是否添加过和必要参数是否存在即可,数据重复时，导入数据为更新操作
	@SuppressWarnings("unchecked")
	protected void checkData() {
		List<IM102FibreListEntity> list = (List<IM102FibreListEntity>) table.getInput();
		if (list == null || list.size() <= 0) return;
		List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
		boolean notStation = (substationList == null || substationList.size() <= 0);
		for (IM102FibreListEntity entity : list) {
			if (entity.getMatched() == DBConstants.MATCHED_OK) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			if (notStation) {
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) {//光缆编号不存在，不处理
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			
			if (entity.getCoreCode() == null || "".equals(entity.getCoreCode())) {//纤芯编号不存在，不处理
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue;
			}
			Tb1048PortEntity portEntityA = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
			Tb1048PortEntity portEntityB = portEntityService.getPortEntity(entity.getDevNameB(), entity.getBoardCodeB(), entity.getPortCodeB());
			if (portEntityA == null || portEntityB == null) {//端口都不能为空
				entity.setConflict(DBConstants.YES);
				entity.setOverwrite(false);
				continue; 
			}
			entity.setConflict(DBConstants.NO);
			entity.setOverwrite(true);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object locate(Problem problem) {
		List<IM102FibreListEntity> list = (List<IM102FibreListEntity>) table.getInput();
		if (list == null || list.size() <= 0) 
			return null;
		String ref = problem.getRef();
		String desc = problem.getDesc();
		if (ref.indexOf("->") > 0) {
			String[] temp = ref.split("->");
			String iedA = temp[0].trim();
			String iedB = temp[1].trim();
			for (IM102FibreListEntity entity : list) {
				if (iedA.equals(entity.getDevNameA()) && iedB.equals(entity.getDevNameB())) {
					return entity;
				}
			}
		} else {
			if (desc.indexOf("重复") > 0 && desc.indexOf('[')>0) {
				desc = desc.substring(desc.indexOf('[') + 1, desc.indexOf(']'));
				String[] temp = desc.split(",");
				if (temp.length == 3) {
					for (IM102FibreListEntity entity : list) {
						if ((entity.getDevNameA().equals(temp[0]) && 
								entity.getBoardCodeA().equals(temp[1]) && 
								entity.getPortCodeA().equals(temp[2])) ||
								(entity.getDevNameB().equals(temp[0]) && 
										entity.getBoardCodeB().equals(temp[1]) && 
										entity.getPortCodeB().equals(temp[2]))) {
							return entity;
						}
					}
				} else {
					for (IM102FibreListEntity entity : list) {
						String coreCode = entity.getCoreCode();
						String cableCode = entity.getCableCode();
						if ((!StringUtil.isEmpty(coreCode) && coreCode.equals(temp[0]))
								&& (!StringUtil.isEmpty(cableCode) && cableCode.equals(ref))) {
							return entity;
						}
					}
				}
			} else {
				for (IM102FibreListEntity entity : list) {
					if (!StringUtil.isEmpty(ref) && ref.equals(entity.getCableCode())) {
						return entity;
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void exportExcel() {
		table.exportExcel2007();
	}
	
}
