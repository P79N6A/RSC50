/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-11
 */
public class UserGroupNode implements ITreeEntry{
	private String daName;
	private int level;
	protected ITreeEntry parent;
	protected List<ITreeEntry> children = new ArrayList<ITreeEntry>();
	
	public UserGroupNode(){
		
	}

	public String getDaName() {
		return daName;
	}

	public void setDaName(String daName) {
		this.daName = daName;
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
	public String getName() {
		// TODO Auto-generated method stub
		return daName;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.daName = name;
	}

	@Override
	public int getIndex() {
		// TODO Auto-generated method stub
		return level;
	}

	@Override
	public void setIndex(int index) {
		// TODO Auto-generated method stub
		this.level = index;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDesc(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITreeEntry copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
