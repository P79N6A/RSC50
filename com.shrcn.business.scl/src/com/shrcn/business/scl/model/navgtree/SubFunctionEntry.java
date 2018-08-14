/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.ui.model.ITreeEntry;


/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史
 * $Log: SubFunctionEntry.java,v $
 * Revision 1.1  2013/03/29 09:35:20  cchun
 * Add:创建
 *
 * Revision 1.3  2010/01/19 09:02:33  lj6061
 * add:统一国际化工程
 *
 * Revision 1.2  2009/09/10 11:30:41  lj6061
 * 为属性窗口的添加功能和设备不得重名判断
 *
 * Revision 1.1  2009/08/27 02:22:40  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.2  2009/05/22 03:03:57  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.1  2009/05/18 07:08:11  lj6061
 * 导入1次信息
 *
 */
public class SubFunctionEntry extends TreeEntryImpl {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public SubFunctionEntry(){}

	public SubFunctionEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}

	public SubFunctionEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	
	@Override
	public void setName(String name) {
		if(name ==null||name.equals("")){ //$NON-NLS-1$
			message = Messages.getString("SubFunctionEntry.name_null_message"); //$NON-NLS-1$
			return;
		}
		List<String> namelist = new ArrayList<String>();
		ITreeEntry entry = getParent();
		if(entry == null) return;
		List<ITreeEntry> list = entry.getChildren();
		for (ITreeEntry treeEntry : list) {
			if(treeEntry instanceof SubFunctionEntry)
			namelist.add(treeEntry.getName());
		}
		if(!namelist.contains(name)){
			this.name = name;
		}else{
			message = Messages.getString("SubFunctionEntry.same_name_message") + name + Messages.getString("SubFunctionEntry.same_subName_message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
    /** 自定义节点方法*/

	public String getMessage() {
		return message;
	}
}