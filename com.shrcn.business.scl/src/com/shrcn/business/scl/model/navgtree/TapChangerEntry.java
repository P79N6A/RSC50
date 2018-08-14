/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史 $Log: TapChangerEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:35:24  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/27 02:22:40  cchun
 * 修改历史 Refactor:重构导航树模型包路径
 * 修改历史
 * 修改历史 Revision 1.3  2009/06/25 07:33:06  lj6061
 * 修改历史 整理AppID配置
 * 修改历史
 * 修改历史 Revision 1.2  2009/05/22 03:03:58  lj6061
 * 修改历史 修改节点属性添加优先级
 * 修改历史
 * 修改历史 Revision 1.1  2009/05/18 07:08:12  lj6061
 * 修改历史 导入1次信息
 * 修改历史
 */
public class TapChangerEntry extends EquipmentEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TapChangerEntry(){}

	public TapChangerEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}

	public TapChangerEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	
	public TapChangerEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
	}
}
