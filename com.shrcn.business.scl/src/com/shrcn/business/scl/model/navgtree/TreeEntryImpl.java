/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.ui.SCTEditorInput;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-9
 */
/*
 * 修改历史
 * $Log: TreeEntryImpl.java,v $
 * Revision 1.1  2013/03/29 09:35:22  cchun
 * Add:创建
 *
 * Revision 1.10  2010/12/29 07:43:03  cchun
 * Update:添加setEditorInput()
 *
 * Revision 1.9  2010/12/29 06:44:51  cchun
 * Update:添加copy()
 *
 * Revision 1.8  2010/09/03 02:43:07  cchun
 * Update:子节点增加按xpath map存储
 *
 * Revision 1.7  2010/08/10 06:48:41  cchun
 * Refactor:修改editor input类包路径
 *
 * Revision 1.6  2010/02/08 10:41:04  cchun
 * Refactor:完成第一阶段重构
 *
 * Revision 1.5  2010/02/03 08:32:36  hqh
 * 删除布尔属性
 *
 * Revision 1.4  2010/02/03 07:35:34  hqh
 * 添加属性变量
 *
 * Revision 1.3  2009/09/22 05:33:05  hqh
 * 方法移动到逻辑节点
 *
 * Revision 1.2  2009/09/14 10:28:07  hqh
 * 添加set/get
 *
 * Revision 1.1  2009/08/27 02:22:38  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.11  2009/08/10 02:22:19  cchun
 * Update添加主接线图模工具
 *
 * Revision 1.10  2009/07/16 06:21:44  pht
 * 添加getValue和setValue方法，DOI实例用。
 *
 * Revision 1.9  2009/06/04 08:22:17  lj6061
 * 添加配置文件
 *
 * Revision 1.8  2009/06/01 00:47:25  pht
 * 浅拷贝，改成深拷贝。
 *
 * Revision 1.6  2009/05/27 04:46:28  lj6061
 * 添加一次信息图片
 *
 * Revision 1.5  2009/05/26 06:17:36  lj6061
 * 默认菜单新建SSD
 *
 * Revision 1.4  2009/05/22 03:03:59  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.3  2009/05/18 07:08:12  lj6061
 * 导入1次信息
 *
 * Revision 1.2  2009/04/23 07:59:38  hqh
 * 添加节点实例子
 *
 * Revision 1.1  2009/04/17 07:16:49  cchun
 * Refactor:清理无效代码
 *
 * Revision 1.2  2009/04/17 06:07:13  cchun
 * Refactor:重构导航视图
 *
 * Revision 1.1  2009/04/10 00:41:55  cchun
 * Add:重构树节点
 *
 */
public class TreeEntryImpl implements INaviTreeEntry, Cloneable, Serializable {
	
	private static final long serialVersionUID = -3621583754420654913L;
	protected String name = null;
	protected String xpath = null;
	protected List<ITreeEntry> children = new ArrayList<ITreeEntry>();
	transient protected SCTEditorInput editorInput = null;;
	protected ITreeEntry parent = null;
	protected String iconName = null;;
	protected String toolTip = null;;
	protected String nodeType = null;;
	protected int priority;
	protected String value;
	protected  boolean isReleated=false;
//	protected Map<String,ITreeEntry> hshXPathEntry=new HashMap<String,ITreeEntry>();
	protected String desc = null;
//	protected String virType = null;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public TreeEntryImpl() {
	}

	/**
	 * 创建导航树节点模型对象（二次设备）
	 * @param name
	 * @param xpath
	 * @param iconName
	 * @param ediorID
	 */
	public TreeEntryImpl(String name, String xpath, String iconName,
			String toolTip, String editorID) {
		this.name = name;
		this.xpath = xpath;
		this.iconName = iconName;
		this.toolTip = toolTip;
		if(null != editorID && !"".equals(editorID)) {
			this.editorInput = new SCTEditorInput(this, editorID);
		}
	}
	
	/**
	 * 创建导航树节点模型对象（一次设备）
	 * @param name
	 * @param xpath
	 * @param iconName
	 * @param priority
	 */
	public TreeEntryImpl(String name, String xpath, String iconName,int priority) {
		this.name = name;
		this.xpath = xpath;
		this.iconName = iconName;
		this.priority = priority;
	}

	/**
	 * 创建导航树节点模型对象（一次设备）
	 * 
	 * @param name
	 * @param desc
	 * @param xpath
	 * @param iconName
	 * @param priority
	 */
	public TreeEntryImpl(String name, String desc, String xpath, String iconName,int priority) {
		this.name = name;
		this.desc = desc;
		this.xpath = xpath;
		this.iconName = iconName;
		this.priority = priority;
	}
	

	/**
	 * 创建导航树节点模型对象（一次设备）
	 * 
	 * @param name
	 * @param desc
	 * @param virtual
	 * @param xpath
	 * @param iconName
	 * @param priority
	 */
	public TreeEntryImpl(String name, String desc, String type, String xpath, String iconName,int priority) {
		this.name = name;
		this.desc = desc;
		this.nodeType = type;
		this.xpath = xpath;
		this.iconName = iconName;
		this.priority = priority;
	}
	
	
	public TreeEntryImpl(String name) {
		this.name = name;
	}
	 
	@Override
	public void addChild(ITreeEntry child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public List<ITreeEntry> getChildren() {
		return this.children;
	}

	@Override
	public SCTEditorInput getEditorInput() {
		return this.editorInput;
	}

	@Override
	public String getToolTip() {
		return this.toolTip;
	}
	
	@Override
	public String getIcon() {
		return this.iconName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ITreeEntry getParent() {
		return this.parent;
	}

	@Override
	public String getXPath() {
		return this.xpath;
	}

	@Override
	public void removeChild(ITreeEntry child) {
		children.remove(child);
	}

	@Override
	public void setParent(ITreeEntry parent) {
		this.parent = parent;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return nodeType;
	}

	@Override
	public void setType(String nodetype) {
		this.nodeType = nodetype;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public void setChildren(List<ITreeEntry> children) {
		this.children =children;
	}

	@SuppressWarnings("unchecked")
	public INaviTreeEntry clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		TreeEntryImpl treeEntryImpl = (TreeEntryImpl) o;
		treeEntryImpl.children = new ArrayList<>();
		ArrayList<ITreeEntry> childrenTree = (ArrayList<ITreeEntry>) children;
		ArrayList<ITreeEntry> childrenTreeClone = (ArrayList<ITreeEntry>) childrenTree.clone();
		for (int i = 0; i < children.size(); i++) {
			TreeEntryImpl copyEntry = (TreeEntryImpl) children.get(i);
			childrenTreeClone.set(i, copyEntry.copy(treeEntryImpl));
		}
		treeEntryImpl.children = childrenTreeClone;
		return treeEntryImpl;
	}

	public INaviTreeEntry copy(INaviTreeEntry parent) {
		TreeEntryImpl newEntry = newEntry();
		newEntry.setParent(parent);
		newEntry.setName(name);
		newEntry.setDesc(StringUtil.nullToEmpty(desc));
		newEntry.setType(nodeType);
		newEntry.setXpath(xpath);
		newEntry.iconName = iconName;
		newEntry.toolTip = toolTip;
		newEntry.editorInput = editorInput;
		newEntry.setPriority(this.priority);
		return newEntry;
	}

	private TreeEntryImpl newEntry() {
		TreeEntryImpl newEntry = null;
		try {
			newEntry = (TreeEntryImpl) this.getClass().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return newEntry;
	}
	

	@Override
	public ITreeEntry copy() {
		TreeEntryImpl newEntry = newEntry();
		newEntry.setName(name);
		newEntry.setDesc(StringUtil.nullToEmpty(desc));
		newEntry.setType(nodeType);
		newEntry.setXpath(xpath);
		newEntry.iconName = iconName;
		newEntry.toolTip = toolTip;
		newEntry.editorInput = editorInput;
		newEntry.setPriority(this.priority);
		return newEntry;
	}

	@Override
	public void setEditorInput(SCTEditorInput editorInput) {
		this.editorInput = editorInput;
	}

	@Override
	public void addChildAll(List<ITreeEntry> children) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
