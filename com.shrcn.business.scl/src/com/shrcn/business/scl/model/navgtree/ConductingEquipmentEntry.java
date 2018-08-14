/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-13
 */
/*
 * 修改历史
 * $Log: ConductingEquipmentEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:24  cchun
 * Add:创建
 *
 * Revision 1.7  2010/11/08 07:14:15  cchun
 * Update:清理引用
 *
 * Revision 1.6  2010/07/08 03:32:59  cchun
 * Update:修改图标读取方式
 *
 * Revision 1.5  2010/06/28 02:04:56  cchun
 * Update:添加电容器、电抗器设备图标
 *
 * Revision 1.4  2010/01/19 09:02:32  lj6061
 * add:统一国际化工程
 *
 * Revision 1.3  2009/09/10 11:30:40  lj6061
 * 为属性窗口的添加功能和设备不得重名判断
 *
 * Revision 1.2  2009/08/27 02:40:35  lj6061
 * 修改设备类型文件位置
 *
 * Revision 1.4  2009/05/27 08:01:04  lj6061
 * 添加重命名
 *
 * Revision 1.3  2009/05/27 04:46:28  lj6061
 * 添加一次信息图片
 *
 * Revision 1.2  2009/05/22 03:03:59  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.1  2009/05/13 07:40:48  hqh
 * 增加实体类
 *
 */
public class ConductingEquipmentEntry extends EquipmentEntry {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConductingEquipmentEntry(){}

	public ConductingEquipmentEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);	
	}
	
	public ConductingEquipmentEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);	
	}
	
	public ConductingEquipmentEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
		setType(type);
	}
	
	/** 设置节点类型 */
	public void setType(String nodetype){
		super.setType(nodetype);
		
		String iconPath=Constants.ICONS_DIR+File.separator+nodetype+".gif";
		File iconFile=new File(iconPath);
		if(!iconFile.exists())
			iconName="icons"+File.separator+"folder.gif";
		else
			iconName="icons"+File.separator+nodetype+".gif";
	}

	@Override
	public void setName(String name) {
		if(name ==null||name.equals("")){ //$NON-NLS-1$
			message = Messages.getString("ConductingEquipmentEntry.name_null_message"); //$NON-NLS-1$
			return;
		}
		List<String> namelist = new ArrayList<String>();
		ITreeEntry entry = getParent();
		if(entry == null) return;
		List<ITreeEntry> list = entry.getChildren();
		for (ITreeEntry treeEntry : list) {
			if(treeEntry instanceof ConductingEquipmentEntry)
			namelist.add(treeEntry.getName());
		}
		if(!namelist.contains(name)){
			this.name = name;
		}else{
			message = Messages.getString("ConductingEquipmentEntry.sama_name_message") + name + Messages.getString("ConductingEquipmentEntry.same_node_message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
}
