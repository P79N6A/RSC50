/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: TreeNode.java,v $
 * Revision 1.1  2013/03/29 09:35:22  cchun
 * Add:创建
 *
 * Revision 1.3  2011/03/11 01:20:23  cchun
 * Update:添加id属性
 *
 * Revision 1.2  2010/08/12 07:29:14  cchun
 * Update:添加构造函数
 *
 * Revision 1.1  2010/03/09 08:43:05  cchun
 * Add:通用树节点对象
 *
 */
public class TreeNode implements ITreeNode {

	private static final long serialVersionUID = -1159328029658021173L;
	
	protected String id;
	protected String name;
	protected String toolTip;
	protected String iconName;
	protected ITreeNode parent;
	protected List<ITreeNode> children = new ArrayList<ITreeNode>();
	
	public TreeNode(String name) {
		this(name, name, null);
	}
	
	public TreeNode(String name, String toolTip, String iconName) {
		this.name = name;
		this.toolTip = toolTip;
		this.iconName = iconName;
	}

	@Override
	public void addChild(ITreeNode child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public List<ITreeNode> getChildren() {
		return this.children;
	}

	@Override
	public String getToolTip() {
		return this.toolTip;
	}
	
	@Override
	public String getIconName() {
		return this.iconName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ITreeNode getParent() {
		return this.parent;
	}

	@Override
	public void removeChild(ITreeNode child) {
		children.remove(child);
	}

	@Override
	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}
	
	@Override
	public void setChildren(List<ITreeNode> children) {
		this.children =children;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isLeaf() {
		return (null == children) || (children.size() == 0);
	}

	@Override
	public boolean isRoot() {
		return parent == null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
