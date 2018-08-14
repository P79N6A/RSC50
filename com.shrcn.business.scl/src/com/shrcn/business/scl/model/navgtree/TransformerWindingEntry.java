/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

/**
 * 变压器绕组
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-13
 */
/*
 * 修改历史
 * $Log: TransformerWindingEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:22  cchun
 * Add:创建
 *
 * Revision 1.1  2009/08/27 02:22:31  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.4  2009/06/25 07:33:13  lj6061
 * 整理AppID相关配置
 *
 * Revision 1.3  2009/05/22 03:03:58  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.2  2009/05/18 07:08:12  lj6061
 * 导入1次信息
 *
 * Revision 1.1  2009/05/13 07:40:48  hqh
 * 增加实体类
 *
 */
public class TransformerWindingEntry extends EquipmentEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TransformerWindingEntry(){}
	
	public TransformerWindingEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}

	public TransformerWindingEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	
	public TransformerWindingEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
	}
}
