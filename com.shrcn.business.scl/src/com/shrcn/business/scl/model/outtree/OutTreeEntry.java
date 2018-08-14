/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.outtree;

import java.util.ArrayList;
import java.util.List;

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
 * @version 1.0, 2009-5-8
 */
/*
 * 修改历史
 * $Log: OutTreeEntry.java,v $
 * Revision 1.7  2013/11/13 00:21:18  cxc
 * update:添加设置是否关联过的方法
 *
 * Revision 1.6  2012/09/03 07:18:40  cchun
 * Update:增加isQt()
 *
 * Revision 1.5  2010/12/21 07:31:12  cchun
 * Update:添加addChildren(),copy(),getRootEntry()
 *
 * Revision 1.4  2010/11/12 08:56:32  cchun
 * Update:使用统一图标
 *
 * Revision 1.3  2010/10/19 07:08:42  cchun
 * Update:将外部信号视图数据集名改成对应的控制块名
 *
 * Revision 1.2  2009/05/12 06:09:14  cchun
 * Update:添加节点描述
 *
 * Revision 1.1  2009/05/08 12:07:18  cchun
 * Update:完善外部、内部信号视图
 *
 */
public class OutTreeEntry implements IOutTreeEntry, IDeferredWorkbenchAdapter {

	private String name = null;
	private String desc = null;
	private String datName = null;
	private String datDesc = null;
	private List<ITreeEntry> children = new ArrayList<ITreeEntry>();
	private ITreeEntry parent;
	private String iconName;
	private String toolTip;
	private int entryType = -1;
	
	public OutTreeEntry(String name, String iconName,
			String toolTip, int entryType) {
		this.name = name;
		this.iconName = iconName;
		this.toolTip = toolTip;
		this.entryType = entryType;
	}
	
	@Override
	public void addChild(ITreeEntry child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public void addChildAll(List<ITreeEntry> children) {
		for (ITreeEntry child : children) {
			addChild(child);
		}
	}

	@Override
	public List<ITreeEntry> getChildren() {
		return children;
	}

	@Override
	public int getEntryType() {
		return entryType;
	}

	@Override
	public String getIcon() {
		return iconName;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ITreeEntry getParent() {
		return parent;
	}

	@Override
	public String getToolTip() {
		return toolTip;
	}

	@Override
	public void removeChild(ITreeEntry child) {
		children.remove(child);
	}

	@Override
	public void setParent(ITreeEntry parent) {
		this.parent = parent;
	}

	@Override
	public void fetchDeferredChildren(Object object,
			IElementCollector collector, IProgressMonitor monitor) {
		if (!(object instanceof IOutTreeEntry))
			return;
		IOutTreeEntry entry = (IOutTreeEntry) object;
		if (entry.getEntryType() == IOutTreeEntry.IED_ENTRY) { // 加载装置下数据集信息
			OutTreeDataFactory factory = OutTreeDataFactory.getInstance();
			factory.fillOutIedEntry(entry, collector, monitor);
		} else if (entry.getEntryType() == IOutTreeEntry.DAT_ENTRY) { // 加载装置下数据集数据项
			OutTreeDataFactory factory = OutTreeDataFactory.getInstance();
			factory.fillDataSetEntry(entry, collector, monitor);
		} else {
			List<ITreeEntry> children = entry.getChildren();
			int size = children.size();
			monitor.beginTask("Loading", size);
			for (ITreeEntry ent : children) {
				collector.add(ent, monitor);
				monitor.worked(1);
				if (monitor.isCanceled()) {
					break;
				}
			}
			monitor.done();
		}
	}
	
	/**
	 * 得到根结点
	 * @return
	 */
	public ITreeEntry getRootEntry() {
		ITreeEntry parent = this;
		while (parent != null) {
			if (parent.getParent() == null)
				return parent;
			parent = parent.getParent();
		}
		return null;
	}

	@Override
	public ISchedulingRule getRule(Object object) {
		return null;
	}

	@Override
	public boolean isContainer() {
		if(entryType == IOutTreeEntry.IED_ENTRY || 
				entryType == IOutTreeEntry.DAT_ENTRY || children.size()>0)
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDatName() {
		return datName;
	}

	public void setDatName(String datName) {
		this.datName = datName;
	}

	public String getDatDesc() {
		return datDesc;
	}

	public void setDatDesc(String datDesc) {
		this.datDesc = datDesc;
	}

	public IOutTreeEntry copy() {
		OutTreeEntry entry = new OutTreeEntry(name, iconName, toolTip, entryType);
		entry.setDesc(desc);
		entry.setDatName(datName);
		entry.setDatDesc(datDesc);
		return entry;
	}

	@Override
	public boolean isQt() {
		String name = getName();
		return name.endsWith("$q") || name.endsWith("$t");
	}

	@Override
	public void setHaveSglRef(boolean haveSglRef) {
	}

	@Override
	public boolean isHaveSglRef() {
		return false;
	}

	@Override
	public void setChildren(List<ITreeEntry> children) {
		this.children = children;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public void setIndex(int index) {
	}
}
