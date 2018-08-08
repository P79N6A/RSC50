/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.action;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-7-10
 */
/**
 * $Log$
 */
public class GroupAction extends MenuAction {

	private List<MenuAction> subactions;
	
	public GroupAction(String text) {
		super(text);
	}

	public List<MenuAction> getSubactions() {
		return subactions;
	}

	public void setSubactions(List<MenuAction> subactions) {
		this.subactions = subactions;
	}
	
	public void addSubaction(MenuAction subaction) {
		if (subactions==null)
			this.subactions = new ArrayList<MenuAction>();
		subactions.add(subaction);
	}
}
