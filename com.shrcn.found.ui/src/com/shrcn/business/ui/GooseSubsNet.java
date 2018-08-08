/**
 * Copyright (c) 2007-2013 上海思源弘瑞自动化有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

/**
 * @author 周思韵(mailto:zsy.14193@shrcn.com)
 * @version 1.0, 2013-4-25
 */
/**
 * $log$
 */
public class GooseSubsNet {
	
	private String index;
	private boolean select;
	
	public GooseSubsNet(String index, boolean select) {
		super();
		this.index = index;
		this.select = select;
	}
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public Boolean getSelect() {
		return select;
	}
	public void setSelect(Boolean select) {
		this.select = select;
	}

}
