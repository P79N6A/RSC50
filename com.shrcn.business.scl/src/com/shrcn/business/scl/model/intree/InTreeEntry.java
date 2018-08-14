/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.intree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.progress.IDeferredWorkbenchAdapter;
import org.eclipse.ui.progress.IElementCollector;

import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-5
 */
/*
 * 修改历史
 * $Log: InTreeEntry.java,v $
 * Revision 1.11  2013/11/13 00:18:41  cxc
 * update:添加设置是否关联过的方法
 *
 * Revision 1.10  2012/09/03 07:07:15  cchun
 * Update:为InTreeEntry增加序号属性
 *
 * Revision 1.9  2011/03/11 01:20:22  cchun
 * Update:添加id属性
 *
 * Revision 1.8  2010/11/12 08:55:27  cchun
 * Update:使用统一图标
 *
 * Revision 1.7  2010/03/09 08:42:25  cchun
 * Update:添加构造函数
 *
 * Revision 1.6  2009/11/13 07:18:12  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.5  2009/11/06 07:22:55  cchun
 * Update:删除子节点时，同时去掉对父节点的引用
 *
 * Revision 1.4  2009/08/17 07:24:28  cchun
 * Update:修改对Struct属性数据的处理，将树层次拉直为一级(合并)
 *
 * Revision 1.3  2009/05/12 06:09:04  cchun
 * Update:添加节点描述，修改DO信息为IED实例化后的数据
 *
 * Revision 1.2  2009/05/07 03:03:12  cchun
 * Update:修改懒加载实现
 *
 * Revision 1.1  2009/05/06 06:39:03  cchun
 * Add:添加内部信号树节点对象
 *
 */
public class InTreeEntry implements IInnerTreeEntry, IDeferredWorkbenchAdapter {

	private String id;
	private String name;
	private String desc;
	private String xpath;
	private String dataType;
	private List<ITreeEntry> children = new ArrayList<ITreeEntry>();
	private ITreeEntry parent;
	private String iconName;
	private String toolTip;
	private boolean haveSglRef;
	private int entryType = -1;
	private Map<String, String> attributes = new HashMap<String, String>();
	//LN 序号排序用 只有LN节点赋值了
	private int num;
	
	/**
	 * 构造函数
	 * @param name
	 * @param iconName
	 * @param toolTip
	 * @param entryType
	 */
	public InTreeEntry(String name, String iconName,
			String toolTip, int entryType) {
		this.name = name;
		this.iconName = iconName;
		this.toolTip = toolTip;
		this.entryType = entryType;
	}
	
	/**
	 * 创建导航树节点模型对象
	 * @param name
	 * @param xpath
	 * @param iconName
	 * @param ediorID
	 */
	public InTreeEntry(String name, String xpath, String iconName,
			String toolTip, int entryType) {
		this(name, iconName, toolTip, entryType);
		this.xpath = xpath;
	}
	
	/**
	 * 内部信号视图节点
	 * @param name
	 * @param xpath
	 * @param iconName
	 * @param toolTip
	 * @param entryType
	 * @param num  排序用的序号
	 */
	public InTreeEntry(String name, String xpath, String iconName,
			String toolTip, int entryType, int num) {
		this(name, iconName, toolTip, entryType);
		this.xpath = xpath;
		this.num = num;
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
		child.setParent(null);
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
	public int getEntryType() {
		return entryType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		if (!(object instanceof IInnerTreeEntry))
			return;
		IInnerTreeEntry entry = (IInnerTreeEntry) object;
		if (entry.getEntryType() != IInnerTreeEntry.LN_ENTRY) {
			List<ITreeEntry> children = entry.getChildren();
			int size = children.size();
			monitor.beginTask("Loading", size);
			for(ITreeEntry ent:children) {
				collector.add(ent, monitor);
				monitor.worked(1);
				if (monitor.isCanceled()) {
					break;
				}
//				pause(10);
			}
			monitor.done();
		} else if (entry.getParent() != null) {
			InTreeDataFactory factory = InTreeDataFactory.getInstance();
			String ldInst = entry.getParent().getName();
			factory.fillLNTreeData(ldInst, entry, collector, monitor);
		}
	}

	@Override
	public ISchedulingRule getRule(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isContainer() {
		if(entryType == IInnerTreeEntry.LN_ENTRY || children.size()>0)
			return true;
		return false;
	}

	@Override
	public Object[] getChildren(Object o) {
		return children.toArray();
	}

	@Override
	public ImageDescriptor getImageDescriptor(Object object) {
		String icon = isContainer() ? ImageConstants.FOLDER :
			ImageConstants.FILE;
		return IconsManager.getInstance().getImageDescriptor(icon);
	}

	@Override
	public String getLabel(Object o) {
		return name;
	}

	@Override
	public Object getParent(Object o) {
		return parent;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String getAttribute(String attName) {
		return attributes.get(attName);
	}

	@Override
	public void setAttribute(String attName, String value) {
		attributes.put(attName, value);
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	@Override
	public boolean isLeaf() {
		return children.size() == 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	@Override
	public boolean isHavaSglRef() {
		return haveSglRef;
	}

	@Override
	public void setHaveSglRef(boolean haveSglRef) {
		this.haveSglRef = haveSglRef;
	}

	@Override
	public void setChildren(List<ITreeEntry> children) {
		this.children = children;
	}

	@Override
	public void addChildAll(List<ITreeEntry> children) {
		this.children.addAll(children);
	}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public void setIndex(int index) {
	}

	@Override
	public ITreeEntry copy() {
		return null;
	}
}
