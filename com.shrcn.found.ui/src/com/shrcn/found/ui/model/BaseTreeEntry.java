/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: BaseTreeEntry.java,v $
 * Revision 1.6  2013/06/06 03:07:28  cxc
 * Update：设置desc属性的方法
 *
 * Revision 1.5  2013/05/17 08:33:05  cchun
 * Update:添加节点排序
 *
 * Revision 1.4  2013/05/14 07:28:00  cchun
 * Update:添加removeChild()
 *
 * Revision 1.3  2013/05/07 01:34:00  cchun
 * Update:增加setName()
 *
 * Revision 1.2  2013/04/07 12:27:36  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.1  2013/04/06 05:33:40  cchun
 * Add:导航树基础类
 *
 * Revision 1.1  2013/04/03 00:34:38  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:27:05  cchun
 * Add:界面类
 *
 */
public class BaseTreeEntry implements ITreeEntry {

	protected String name;
	protected int index;
	protected String desc;
	protected String icon;
	
	protected ITreeEntry parent;
	protected List<ITreeEntry> children = new ArrayList<ITreeEntry>(0);

	public BaseTreeEntry(String name, String desc, String icon) {
		this(null, name, desc, icon);
	}
	
	public BaseTreeEntry(ITreeEntry parent, String name, String desc, String icon) {
		this.name = name;
		this.desc = desc;
		this.icon = icon;
		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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
	public String getIcon() {
		return this.icon;
	}

	@Override
	public void setParent(ITreeEntry parent) {
		this.parent = parent;
	}

	@Override
	public ITreeEntry getParent() {
		return this.parent;
	}

	@Override
	public List<ITreeEntry> getChildren() {
		return children;
	}
	
	@Override
	public void addChild(ITreeEntry child) {
		child.setParent(this);
		children.add(child);
	}

	@Override
	public void addChildAll(List<ITreeEntry> children) {
		for (ITreeEntry child : children) {
			addChild(child);
		}
	}

	@Override
	public void removeChild(ITreeEntry child) {
		children.remove(child);
		child.setParent(null);
	}

	@Override
	public void setChildren(List<ITreeEntry> children) {
		this.children = children;
	}

	@Override
	public ITreeEntry copy() {
		return new BaseTreeEntry(name, desc, icon);
	}

}
