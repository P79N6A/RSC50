/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;


/**
 * 间隔
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-5-13
 */
/*
 * 修改历史 $Log: BayEntry.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:35:20  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.3  2011/01/06 08:54:09  cchun
 * 修改历史 Update:增加缺省构造函数
 * 修改历史
 * 修改历史 Revision 1.2  2010/03/29 02:44:44  cchun
 * 修改历史 Update:提交
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/27 02:22:45  cchun
 * 修改历史 Refactor:重构导航树模型包路径
 * 修改历史
 * 修改历史 Revision 1.4  2009/07/16 06:34:09  lj6061
 * 修改历史 整理代码
 * 修改历史 1.删除未被引用的对象和方法
 * 修改历史 2 修正空指针的异常
 * 修改历史
 * 修改历史 Revision 1.3  2009/05/22 03:03:57  lj6061
 * 修改历史 修改节点属性添加优先级
 * 修改历史
 * 修改历史 Revision 1.2  2009/05/18 07:08:11  lj6061
 * 修改历史 导入1次信息
 * 修改历史 Revision 1.1 2009/05/13 07:40:48 hqh 增加实体类
 * 
 */
public class BayEntry extends TreeEntryImpl {

	private static final long serialVersionUID = 1L;
	private boolean isBusbar = false;
	
	public BayEntry() {}
	
	public BayEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}
	
	public BayEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}

	public BayEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
	}
	
	public boolean isBusbar() {
		return isBusbar;
	}

	public void setBusbar(boolean isBusbar) {
		this.isBusbar = isBusbar;
	}
}
