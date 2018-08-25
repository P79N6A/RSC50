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
import static com.synet.tool.rsc.RSCConstants.ET_IMP_IED;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_PORT;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_ST;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STA;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_STRAP;
import static com.synet.tool.rsc.RSCConstants.ET_IMP_WRN;
import static com.synet.tool.rsc.RSCConstants.ET_PR_BAY;
import static com.synet.tool.rsc.RSCConstants.ET_PR_MDL;
import static com.synet.tool.rsc.RSCConstants.ET_PT_BAY;
import static com.synet.tool.rsc.RSCConstants.ET_PT_IED;
import static com.synet.tool.rsc.RSCConstants.ET_PT_PBAY;
import static com.synet.tool.rsc.RSCConstants.ET_PY_AREA;
import static com.synet.tool.rsc.RSCConstants.ET_PY_MDL;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_FIB;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_LCK;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_PRO;
import static com.synet.tool.rsc.RSCConstants.ET_SEC_PWR;

import java.util.List;
import java.util.Set;

import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.model.ProjectEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;
import com.shrcn.found.ui.view.ANavgTreeFactory;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.IedEntityService;

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
//		treeBuilder = TreeViewerBuilder.create(XMLFileManager.loadXMLFile(getClass(), Constants.CFGURL));
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
		ConfigTreeEntry protectEntry = createConfigEntry(projectEntry, "保护信息模型", "column.gif", "", 2);
		ConfigTreeEntry physicalEntry = createConfigEntry(projectEntry, "物理信息模型", "column.gif", ET_PY_MDL, 3);
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
			Tb1042BayEntity pbayEntity = null;
			for (int i = 0; i < bayEntityList.size(); i++) {
				Tb1042BayEntity bayEntity = bayEntityList.get(i);
				String bayName = bayEntity.getF1042Name();
				List<Tb1046IedEntity> iedEntities = iedService.getIedEntityByBay(bayEntity);
				if (iedEntities != null && iedEntities.size() > 0) {
					if (!DBConstants.BAY_PUB.equals(bayName)) {
						ConfigTreeEntry bayEntry = createConfigEntry(protectEntry, bayEntity.getF1042Name(), "bay.gif", ET_PT_BAY, i+1);
						for (Tb1046IedEntity iedEntity : iedEntities) {
							ConfigTreeEntry proEntry = createConfigEntry(bayEntry, iedEntity.getF1046Name(), "device.png", ET_PT_IED, 1);
							proEntry.setData(iedEntity);
						}
					} else {
						createConfigEntry(protectEntry, bayEntity.getF1042Name(), "bay.gif", ET_PT_PBAY, bayEntityList.size()+1);
					}
				}
			}
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
				ConfigTreeEntry areaEntry = createConfigEntry(physicalEntry, entity.getF1049Desc(), "bay.gif", ET_PY_AREA, index++);
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
		ConfigTreeEntry iedEntry = createConfigEntry(importEntry, "设备台账", "bay.gif", ET_IMP_IED, 1);
		ConfigTreeEntry ftListEntry = createConfigEntry(importEntry, "光缆清册", "bay.gif", ET_IMP_FIB, 2);
		ConfigTreeEntry boardPortEntry = createConfigEntry(importEntry, "装置板卡端口描述", "bay.gif", ET_IMP_BRD, 3);
		ConfigTreeEntry stInEntry = createConfigEntry(importEntry, "开入信号映射表", "bay.gif", ET_IMP_ST, 4);
		ConfigTreeEntry wmBoardEntry = createConfigEntry(importEntry, "告警与板卡关联表", "bay.gif", ET_IMP_WRN, 5);
		ConfigTreeEntry lpPortEntry = createConfigEntry(importEntry, "光强与端口关联表", "bay.gif", ET_IMP_PORT, 6);
		ConfigTreeEntry strapTermEntry = createConfigEntry(importEntry, "压板与虚端子关联表", "bay.gif", ET_IMP_STRAP, 7);
		ConfigTreeEntry brkCfmEntry = createConfigEntry(importEntry, "跳合闸反校关联表", "bay.gif", ET_IMP_BRK, 8);
		ConfigTreeEntry staInfoEntry = createConfigEntry(importEntry, "监控信息点表", "bay.gif", ET_IMP_STA, 9);
	}
	
	private BayIEDEntry createParentEntry(ITreeEntry parent, String name, String icon, int index) {
		BayIEDEntry bayIEDEntry = new BayIEDEntry(parent, name, "", icon);
		bayIEDEntry.setIndex(index);
		return bayIEDEntry;
	}
	
	private ConfigTreeEntry createConfigEntry(ITreeEntry parent, String name, String icon, String editorId, int index) {
		ConfigTreeEntry configTreeEntry = new ConfigTreeEntry(parent, name, "", icon, editorId);
		configTreeEntry.setIndex(index);
		return configTreeEntry;
	}
	
}
