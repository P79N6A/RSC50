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

import com.shrcn.found.ui.model.ConfigTreeEntry;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.model.ProjectEntry;
import com.shrcn.found.ui.tree.TreeViewerBuilder;
import com.shrcn.found.ui.view.ANavgTreeFactory;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.service.BayEntityService;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
public class NavgTreeFactory extends ANavgTreeFactory {
	
	private static BeanDaoImpl beanDao = BeanDaoImpl.getInstance();

	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();

	private static volatile NavgTreeFactory factory;
	
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
//		ProjectEntry projectEntry = new ProjectEntry(prjFileMgr.getProjectName(), "project.gif", 1);
		ProjectEntry projectEntry = new ProjectEntry("变电站", "", "project.gif");
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
//		// 加载装置
//		for (DeviceData data : prjFileMgr.getDevices()) {
//			BayIEDEntry devEntry = newDevice(data.getName(), data.getIedInScd());
//			if (data.getType().equals(DevConstants.DEV_BUS)) {
//				busRoot.addChild(devEntry);
//			} else if (data.getType().equals(DevConstants.DEV_BAY)) {
//				bayRoot.addChild(devEntry);
//			}
//		}
		loadPrimary(primaryEntry);
		loadProtect(protectEntry);
		loadPhysical(physicalEntry);
		loadSecurity(securityEntry);
		loadIcd(icdEntry);
		loadImport(importEntry);
	}
	
	private void loadPrimary(ITreeEntry primaryEntry) {
		BayEntityService service = new BayEntityService();
		List<Tb1042BayEntity> bayEntityList = service.getBayEntryList();
		if(DataUtils.notNull(bayEntityList)){
			for (int i = 0; i < bayEntityList.size(); i++) {
				createConfigEntry(primaryEntry, bayEntityList.get(i).getF1042Desc(), "bay.gif", ET_PR_BAY, i+1);
			}
		}
	}
	
	private void loadProtect(ITreeEntry protectEntry) {
		/** 动态加载-begin  */
		ConfigTreeEntry bayEntry = createConfigEntry(protectEntry, "间隔1", "bay.gif", ET_PT_BAY, 1);
		ConfigTreeEntry proEntry = createConfigEntry(bayEntry, "保护", "device.png", ET_PT_IED, 1);
		ConfigTreeEntry muEntry = createConfigEntry(bayEntry, "合并单元", "device.png", ET_PT_IED, 2);
		ConfigTreeEntry tmEntry = createConfigEntry(bayEntry, "智能终端", "device.png", ET_PT_IED, 3);
		/** 动态加载-end  */
		
		ConfigTreeEntry bayPubEntry = createConfigEntry(protectEntry, "公用间隔", "bay.gif", ET_PT_PBAY, 2);
	}
	
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
	
	private void loadSecurity(ITreeEntry securityEntry) {
		ConfigTreeEntry ftEntry = createConfigEntry(securityEntry, "保护纵联光纤", "bay.gif", ET_SEC_FIB, 1);
		ConfigTreeEntry lockEntry = createConfigEntry(securityEntry, "重合回路闭锁", "bay.gif", ET_SEC_LCK, 2);
		ConfigTreeEntry pwBrkEntry = createConfigEntry(securityEntry, "装置电源空开", "bay.gif", ET_SEC_PWR, 3);
		ConfigTreeEntry rtBrkEntry = createConfigEntry(securityEntry, "保护电压回路空开", "bay.gif", ET_SEC_PRO, 4);
	}
	
	private void loadIcd(ITreeEntry icdEntry) {
		// 暂无
	}
	
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
	
//	/**
//	 * 创建装置节点.
//	 * 
//	 * @param prjEntry
//	 *            工程节点.
//	 * @param iedName
//	 *            装置名称.
//	 */
//	public BayIEDEntry createDevice(String iedName, String type){
//		if (primaryEntry == null || protectEntry == null)
//			return null;
//		BayIEDEntry device = newDevice(iedName, null);
//		if (type.equals(DevConstants.DEV_BUS)) {
//			primaryEntry.addChild(device);
//		} else if (type.equals(DevConstants.DEV_BAY)) {
//			protectEntry.addChild(device);
//		}
//		return device;
//	}
//	
//	/**
//	 * 装置重命名
//	 * @param oldName
//	 * @param newName
//	 */
//	public ITreeEntry renameDevice(String oldName, String newName) {
//		if (primaryEntry == null || protectEntry == null)
//			return null;
//		ITreeEntry iedEntry = findDevice(oldName);
//		iedEntry.setName(newName);
//		return iedEntry;
//	}
//	
//	/**
//	 * 删除装置
//	 * @param devName
//	 */
//	public void removeDevice(String devName) {
//		if (primaryEntry == null || protectEntry == null)
//			return;
//		ITreeEntry iedEntry = findDevice(devName);
//		iedEntry.getParent().removeChild(iedEntry);
//	}
//
//	public ITreeEntry findDevice(String devName) {
//		ITreeEntry iedEntry = null;
//		List<ITreeEntry> children = new ArrayList<ITreeEntry>();
//		children.addAll(protectEntry.getChildren());
//		children.addAll(primaryEntry.getChildren());
//		for (ITreeEntry entry : children) {
//			if (entry.getName().equals(devName)) {
//				iedEntry = entry;
//				break;
//			}
//		}
//		return iedEntry;
//	}
//
//	private BayIEDEntry newDevice(String iedName, String iedInScd) {
//		BayIEDEntry bayIEDEntry = createParentEntry(null, iedName, "device.png", 1);
//		bayIEDEntry.setSyned(iedInScd != null);
//		return bayIEDEntry;
//	}
}
