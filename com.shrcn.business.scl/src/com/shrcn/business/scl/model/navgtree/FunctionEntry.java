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
 * 功能
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-15
 */
/*
 * 修改历史 $Log: FunctionEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:35:25  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.3  2010/01/19 09:02:32  lj6061
 * 修改历史 add:统一国际化工程
 * 修改历史
 * 修改历史 Revision 1.2  2009/09/10 11:30:40  lj6061
 * 修改历史 为属性窗口的添加功能和设备不得重名判断
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/27 02:22:37  cchun
 * 修改历史 Refactor:重构导航树模型包路径
 * 修改历史
 * 修改历史 Revision 1.2  2009/05/22 03:03:57  lj6061
 * 修改历史 修改节点属性添加优先级
 * 修改历史
 * 修改历史 Revision 1.1  2009/05/18 07:08:11  lj6061
 * 修改历史 导入1次信息
 * 修改历史
 */
public class FunctionEntry extends TreeEntryImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public FunctionEntry(){}

	public FunctionEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}
	
	public FunctionEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	

	public FunctionEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
	}
	
	@Override
	public void setName(String name) {
		if(name ==null||name.equals("")){ //$NON-NLS-1$
			message = Messages.getString("FunctionEntry.name_null_message"); //$NON-NLS-1$
			return;
		}
		List<String> namelist = new ArrayList<String>();
		ITreeEntry entry = getParent();
		if(entry == null) return;
		List<ITreeEntry> list = entry.getChildren();
		for (ITreeEntry treeEntry : list) {
			if(treeEntry instanceof FunctionEntry)
			namelist.add(treeEntry.getName());
		}
		if(!namelist.contains(name)){
			this.name = name;
		}else{
			message = Messages.getString("FunctionEntry.same_type_function_message") + name + Messages.getString("FunctionEntry.same_subNode_message"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
    /** 自定义节点方法*/

	public String getMessage() {
		return message;
	}
}
