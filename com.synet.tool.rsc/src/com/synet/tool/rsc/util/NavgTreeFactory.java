/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.synet.tool.rsc.util;

import static com.synet.tool.rsc.RSCConstants.ET_ICD_MDL;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRD;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_BRK;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_FIB;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_FIBNew;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_IED;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_PORT;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_ST;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STA;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_LINKW;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STRAP;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_WRN;
import static com.synet.tool.rsc.RSCConstants.ET_PR_BAY;
import static com.synet.tool.rsc.RSCConstants.ET_PT_BAY;
import static com.synet.tool.rsc.RSCConstants.ET_PT_BAY_PUB;
import static com.synet.tool.rsc.RSCConstants.ET_PT_IED;
import static com.synet.tool.rsc.RSCConstants.ET_PY_AREA;
import static com.synet.tool.rsc.RSCConstants.ET_PY_MDL;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_FIB;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_LCK;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_PRO;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_PWR;
import static com.synet.tool.rsc.RSCConstants.ET_PR_MDL;

import java.util.List;
import java.util.Set;

import com.shrcn.found.ui.editor.BaseEditorInput;
import com.shrcn.found.ui.editor.ConfigEditorInput;
import com.shrcn.found.ui.editor.EditorConfigData;
import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.IEDEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.model.ProjectEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;
import com.shrcn.found.ui.view.ANavgTreeFactory;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.ui.PhysicInfoEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
public class NavgTreeFactory extends ANavgTreeFactory {
	
	private static BeanDaoImpl beanDao = BeanDaoImpl.getInstance();

	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();

	private static volatile NavgTreeFactory factory;
	
	private IedEntityService iedService = new IedEntityService();
	
	private NavgTreeFactory() {
		treeBuilder = TreeViewerBuilder.create(null);
	}
	
	public static NavgTreeFactory getInstance() {
		if (factory == null) {
			synchronized (NavgTreeFactory.class) {
				factory = new NavgTreeFactory();
			}
		}
		return factory;
	}
	
	@Override
	public TreeViewerBuilder getTreeBuilder() {
		treeBuilder = TreeViewerBuilder.create(getClass(), RSCConstants.CFGURL);
		return super.getTreeBuilder();
	}

	/**
	 * 打开工程节点.
	 */
	public void loadProject() {
		super.loadProject();
		
		if (prjFileMgr.isClosed())
			return;
		List<Tb1041SubstationEntity> staList = (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
		String staName = null;
		if (staList!=null && staList.size()>0) {
			staName = staList.get(0).getF1041Name();
		}
		if (staName == null) {
			staName = "变电站";
		}
		ProjectEntry projectEntry = new ProjectEntry(staName, "", "project.gif");
		data.add(projectEntry);
		ConfigTreeEntry primaryEntry = createConfigEntry(projectEntry, "一次拓扑模型", "column.gif", ET_PR_MDL, 1);
		ConfigTreeEntry protectEntry = createConfigEntry(projectEntry, "保护信息模型", "column.gif", null, 2);
		ConfigTreeEntry physicalEntry = new PhysicInfoEntry(projectEntry, "物理信息模型", "", "column.gif", ET_PY_MDL);
		physicalEntry.setIndex(3);
		ConfigTreeEntry securityEntry = createConfigEntry(projectEntry, "安措配置", "column.gif", "", 4);
		ConfigTreeEntry icdEntry = createConfigEntry(projectEntry, "系统ICD", "column.gif", ET_ICD_MDL, 5);
		ConfigTreeEntry importEntry = createConfigEntry(projectEntry, "导入信息", "column.gif", "", 6);
		projectEntry.addChild(primaryEntry);
		projectEntry.addChild(protectEntry);
		projectEntry.addChild(physicalEntry);
		projectEntry.addChild(securityEntry);
		projectEntry.addChild(icdEntry);
		projectEntry.addChild(importEntry);
		
		BayEntityService service = new BayEntityService();
		List<Tb1042BayEntity> bayEntityList = service.getBayEntryList();
		loadPrimary(primaryEntry, bayEntityList);
		loadProtect(protectEntry, bayEntityList);
		loadPhysical(physicalEntry);
		loadSecurity(securityEntry);
		loadIcd(icdEntry);
		loadImport(importEntry);
	}
	
	/**
	 * 加载一次拓扑模型
	 * @param primaryEntry
	 * @param bayEntityList
	 */
	private void loadPrimary(ITreeEntry primaryEntry, List<Tb1042BayEntity> bayEntityList) {
		if(DataUtils.listNotNull(bayEntityList)){
			for (int i = 0; i < bayEntityList.size(); i++) {
				Tb1042BayEntity tb1042BayEntity = bayEntityList.get(i);
				Set<Tb1043EquipmentEntity> equipments = tb1042BayEntity.getTb1043EquipmentsByF1042Code();
				if (equipments!= null && equipments.size()>0) {
					createConfigEntry(primaryEntry, tb1042BayEntity.getF1042Name(), "bay.gif", ET_PR_BAY, i+1).setData(tb1042BayEntity);
				}
			}
			createConfigEntry(primaryEntry, DBConstants.BAY_ALL, "bay.gif", ET_PR_BAY, bayEntityList.size()+1).setData(null);
		}
	}
	
	/**
	 * 加载保护信息模型
	 * @param protectEntry
	 * @param bayEntityList
	 */
	private void loadProtect(ITreeEntry protectEntry, List<Tb1042BayEntity> bayEntityList) {
		/** 动态加载-begin  */
		if(DataUtils.listNotNull(bayEntityList)){
			ConfigTreeEntry bayOther = null;
			ConfigTreeEntry bayMot = null;
			ConfigTreeEntry bayPub = null;
			int bayIndex = 0;
			for (int i = 0; i < bayEntityList.size(); i++) {
				Tb1042BayEntity bayEntity = bayEntityList.get(i);
				List<Tb1046IedEntity> iedEntities = iedService.getIedEntityByBay(bayEntity);
				String bayName = bayEntity.getF1042Name();
				boolean isPubBay = DBConstants.BAY_PUB.equals(bayName);
				if (isPubBay || (iedEntities != null && iedEntities.size() > 0)) {
					bayIndex = i + 1;
					String editorId = isPubBay ? ET_PT_BAY_PUB : ET_PT_BAY;
					ConfigTreeEntry bayEntry = createConfigEntry(protectEntry, bayName, "bay.gif", editorId, bayIndex);
					bayEntry.setData(bayEntity);
					int iedIndex = 0;
					for (Tb1046IedEntity iedEntity : iedEntities) {
						String name = iedEntity.getF1046Name();
						String desc = iedEntity.getF1046Desc();
						ConfigTreeEntry proEntry = createConfigEntry(bayEntry, name, "device.png", ET_PT_IED, ++iedIndex);
						proEntry.setDesc(desc);
						proEntry.setData(iedEntity);
					}
					if (DBConstants.BAY_OTHER.equals(bayName)) {
						bayOther = bayEntry;
					} else if (DBConstants.BAY_MOT.equals(bayName)) {
						bayMot = bayEntry;
					} else if (isPubBay) {
						bayPub = bayEntry;
					}
				}
			}
			if (bayOther != null)
				bayOther.setIndex(++bayIndex);
			if (bayMot != null)
				bayMot.setIndex(++bayIndex);
			if (bayPub != null)
				bayPub.setIndex(++bayIndex);
			createConfigEntry(protectEntry, DBConstants.BAY_ALL, "bay.gif", ET_PT_BAY, ++bayIndex);
		}
		/** 动态加载-end  */
	}
	
	/**
	 * 加载物理信息模型
	 * @param physicalEntry
	 */
	@SuppressWarnings("unchecked")
	private void loadPhysical(ITreeEntry physicalEntry) {
		/** 动态加载-begin  */
		List<Tb1049RegionEntity> list = (List<Tb1049RegionEntity>) beanDao.getAll(Tb1049RegionEntity.class);
		if (list != null && !list.isEmpty()) {
			int index = 1;
			for(Tb1049RegionEntity entity : list) {
				ConfigTreeEntry areaEntry = createConfigEntry(physicalEntry, entity.getF1049Name(), "bay.gif", ET_PY_AREA, index++);
				areaEntry.setData(entity);
			}
		}
		/** 动态加载-end  */
	}
	
	/**
	 * 加载安措配置
	 * @param securityEntry
	 */
	private void loadSecurity(ITreeEntry securityEntry) {
		ConfigTreeEntry ftEntry = createConfigEntry(securityEntry, "保护纵联光纤", "bay.gif", ET_SEC_FIB, 1);
		ConfigTreeEntry lockEntry = createConfigEntry(securityEntry, "重合回路闭锁", "bay.gif", ET_SEC_LCK, 2);
		ConfigTreeEntry pwBrkEntry = createConfigEntry(securityEntry, "装置电源空开", "bay.gif", ET_SEC_PWR, 3);
		ConfigTreeEntry rtBrkEntry = createConfigEntry(securityEntry, "保护电压回路空开", "bay.gif", ET_SEC_PRO, 4);
	}
	
	/**
	 * 加载系统ICD
	 * @param icdEntry
	 */
	private void loadIcd(ITreeEntry icdEntry) {
		// 暂无
	}
	
	/**
	 * 加载导入信息
	 * @param importEntry
	 */
	private void loadImport(ITreeEntry importEntry) {
		int i = 1;
		ConfigTreeEntry iedEntry = createConfigEntry(importEntry, "设备台账", "bay.gif", ET_IMP_IED, i++);
		ConfigTreeEntry boardPortEntry = createConfigEntry(importEntry, "装置板卡端口描述", "bay.gif", ET_IMP_BRD, i++);
		ConfigTreeEntry ftListEntry = createConfigEntry(importEntry, "光缆清册", "bay.gif", ET_IMP_FIB, i++);
		ConfigTreeEntry ftListNewEntry = createConfigEntry(importEntry, "新光缆清册", "bay.gif", ET_IMP_FIBNew, i++);
		ConfigTreeEntry stInEntry = createConfigEntry(importEntry, "开入信号映射表", "bay.gif", ET_IMP_ST, i++);
		ConfigTreeEntry wmBoardEntry = createConfigEntry(importEntry, "告警与板卡关联表", "bay.gif", ET_IMP_WRN, i++);
		ConfigTreeEntry lpPortEntry = createConfigEntry(importEntry, "光强与端口关联表", "bay.gif", ET_IMP_PORT, i++);
		ConfigTreeEntry strapTermEntry = createConfigEntry(importEntry, "压板与虚端子关联表", "bay.gif", ET_IMP_STRAP, i++);
		ConfigTreeEntry brkCfmEntry = createConfigEntry(importEntry, "跳合闸反校关联表", "bay.gif", ET_IMP_BRK, i++);
		ConfigTreeEntry staInfoEntry = createConfigEntry(importEntry, "监控信息点表", "bay.gif", ET_IMP_STA, i++);
		ConfigTreeEntry linkWarnEntry = createConfigEntry(importEntry, "告警与链路关联表", "bay.gif", ET_IMP_LINKW, i++);
	}
	
	private ConfigTreeEntry createConfigEntry(ITreeEntry parent, String name, String icon, String editorId, int index) {
		ConfigTreeEntry configTreeEntry = new ConfigTreeEntry(parent, name, "", icon, editorId);
		configTreeEntry.setIndex(index);
		return configTreeEntry;
	}
	
	public static ConfigEditorInput getInput(ConfigTreeEntry configEntry, IEDEntry iedEntry, String editorId) {
		EditorConfigData data = new EditorConfigData(configEntry.getName(), null, 0, configEntry.getName());
		data.setData(configEntry.getData());
		return new ConfigEditorInput(configEntry.getName(), configEntry.getIcon(), editorId, data);
	}
	
	public static BaseEditorInput createEditInput(String name, String icon, String editorId, Object editdata) {
		return new BaseEditorInput(name, icon, editorId, editdata);
	}
	
}
