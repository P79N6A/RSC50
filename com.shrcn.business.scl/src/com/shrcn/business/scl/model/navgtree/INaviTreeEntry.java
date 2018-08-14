/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import com.shrcn.business.scl.ui.SCTEditorInput;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-9
 */
/*
 * 修改历史 $Log: ITreeEntry.java,v $ Revision 1.1 2013/03/29 09:35:21 cchun Add:创建
 * 
 * Revision 1.12 2010/12/29 07:43:03 cchun Update:添加setEditorInput()
 * 
 * Revision 1.11 2010/12/29 06:44:32 cchun Update:添加构造方法
 * 
 * Revision 1.10 2010/11/08 07:14:15 cchun Update:清理引用
 * 
 * Revision 1.9 2010/09/03 02:43:06 cchun Update:子节点增加按xpath map存储
 * 
 * Revision 1.8 2010/08/24 01:23:20 cchun Refactor:修改包路径
 * 
 * Revision 1.7 2010/08/10 06:48:32 cchun Refactor:修改editor input类包路径
 * 
 * Revision 1.6 2010/02/08 10:41:04 cchun Refactor:完成第一阶段重构
 * 
 * Revision 1.5 2010/02/03 08:32:34 hqh 删除布尔属性
 * 
 * Revision 1.4 2010/02/03 07:35:33 hqh 添加属性变量
 * 
 * Revision 1.3 2009/09/22 05:33:05 hqh 方法移动到逻辑节点
 * 
 * Revision 1.2 2009/09/14 10:28:07 hqh 添加set/get
 * 
 * Revision 1.1 2009/08/27 02:22:43 cchun Refactor:重构导航树模型包路径
 * 
 * Revision 1.13 2009/06/01 00:47:11 pht 浅拷贝，改成深拷贝。
 * 
 * Revision 1.12 2009/05/27 04:46:27 lj6061 添加一次信息图片
 * 
 * Revision 1.11 2009/05/26 06:17:36 lj6061 默认菜单新建SSD
 * 
 * Revision 1.10 2009/05/25 12:32:16 pht 根据xpath识别结点。因为害怕不同父结点下，会有同名结点。
 * 
 * Revision 1.9 2009/05/23 12:24:09 pht 接口加setName方法
 * 
 * Revision 1.8 2009/05/22 03:03:57 lj6061 修改节点属性添加优先级
 * 
 * Revision 1.7 2009/05/18 07:08:11 lj6061 导入1次信息
 * 
 * Revision 1.6 2009/04/17 07:16:48 cchun Refactor:清理无效代码
 * 
 * Revision 1.2 2009/04/17 06:07:13 cchun Refactor:重构导航视图
 * 
 * Revision 1.1 2009/04/10 00:41:53 cchun Add:重构树节点
 */
public interface INaviTreeEntry extends ITreeEntry {

	/**
	 * 得到当前节点的xpath
	 * 
	 * @return
	 */
	public String getXPath();

	/**
	 * 得到树结点的提示信息
	 * 
	 * @return
	 */
	public String getToolTip();

	public void setXpath(String xpath);
	
	/**
	 * 得到EditorInput
	 * 
	 * @return
	 */
	public SCTEditorInput getEditorInput();

	/**
	 * 设置EditorInput
	 * 
	 * @param editorInput
	 */
	public void setEditorInput(SCTEditorInput editorInput);

	/** 设置节点类型 */
	public String getType();

	public void setType(String nodetype);

	/** 设置节点优先级 */
	public int getPriority();

	public void setPriority(int priority);

	public INaviTreeEntry clone();
}
