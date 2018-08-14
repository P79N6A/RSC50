/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.common.EnumEquipType;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.ImageConstants;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-13
 */
/*
 * 修改历史 $Log: PowerTransformerEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:35:24  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.4  2010/01/19 09:02:33  lj6061
 * 修改历史 add:统一国际化工程
 * 修改历史
 * 修改历史 Revision 1.3  2009/09/10 11:30:41  lj6061
 * 修改历史 为属性窗口的添加功能和设备不得重名判断
 * 修改历史
 * 修改历史 Revision 1.2  2009/08/27 02:40:35  lj6061
 * 修改历史 修改设备类型文件位置
 * 修改历史
 * 修改历史 Revision 1.6  2009/08/26 09:30:54  cchun
 * 修改历史 Update:添加树节点选择联动处理逻辑
 * 修改历史
 * 修改历史 Revision 1.5  2009/06/25 05:41:41  lj6061
 * 修改历史 修改为NUll异常
 * 修改历史
 * 修改历史 Revision 1.4  2009/05/27 04:46:28  lj6061
 * 修改历史 添加一次信息图片
 * 修改历史
 * 修改历史 Revision 1.3  2009/05/22 03:03:57  lj6061
 * 修改历史 修改节点属性添加优先级
 * 修改历史
 * 修改历史 Revision 1.2  2009/05/18 07:08:11  lj6061
 * 修改历史 导入1次信息
 * 修改历史 Revision 1.1 2009/05/13 07:40:48
 * hqh 增加实体类
 * 
 */
public class PowerTransformerEntry extends EquipmentEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PowerTransformerEntry(){}

	public PowerTransformerEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}
	
	public PowerTransformerEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	
	public PowerTransformerEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
		setType(type);
	}

	/** 设置节点类型 */
	public void setType(String nodetype){
		if(nodetype == null)return;
		super.setType(nodetype);
		if (nodetype.equals(EnumEquipType.PTR2)) {
			iconName = ImageConstants.TRANSFORMER2;
		} else if (nodetype.equals(EnumEquipType.PTR3)) {
			iconName = ImageConstants.TRANSFORMER3;
		}
	}
	
	@Override
	public void setName(String name) {
		if(name ==null||name.equals("")){ //$NON-NLS-1$
			message = Messages.getString("PowerTransformerEntry.name_null_message"); //$NON-NLS-1$
			return;
		}
		List<String> namelist = new ArrayList<String>();
		ITreeEntry entry = getParent();
		if(entry == null) return;
		List<ITreeEntry> list = entry.getChildren();
		for (ITreeEntry treeEntry : list) {
			if(treeEntry instanceof PowerTransformerEntry)
			namelist.add(treeEntry.getName());
		}
		if(!namelist.contains(name)){
			this.name = name;
		}else{
			message = Messages.getString("PowerTransformerEntry.same_name_message") + name + Messages.getString("PowerTransformerEntry.same_subName_message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
