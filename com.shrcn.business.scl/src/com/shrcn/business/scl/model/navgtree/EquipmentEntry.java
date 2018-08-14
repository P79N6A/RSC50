/**
 * Copyright (c) 2007, 2016 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.model.navgtree;



/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2016-2-25
 */
public class EquipmentEntry extends TreeEntryImpl  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String message;
	
	protected boolean isVirtual = false;
	
	public EquipmentEntry(){}

	public EquipmentEntry(String name, String xpath, String iconName,int priority) {
		super(name, xpath, iconName, priority);
	}

	public EquipmentEntry(String name, String desc, String xpath, String iconName,int priority) {
		super(name, desc, xpath, iconName, priority);
	}
	
	public EquipmentEntry(String name, String desc, String type, String xpath, String iconName,int priority) {
		super(name, desc, type, xpath, iconName, priority);
	}
	
    /** 自定义节点方法*/

	public String getMessage() {
		return message;
	}

	public boolean isVirtual() {
		return isVirtual;
	}

	public void setVirtual(boolean isVirtual) {
		this.isVirtual = isVirtual;
	}
	
	public INaviTreeEntry copy(INaviTreeEntry parent) {
		EquipmentEntry newEntry = (EquipmentEntry)super.copy(parent);
		newEntry.setVirtual(isVirtual);
		return newEntry;
	}
	
	@Override
	public INaviTreeEntry copy() {
		EquipmentEntry newEntry = (EquipmentEntry)super.copy();
		newEntry.setVirtual(isVirtual);
		return newEntry;
	}
}
