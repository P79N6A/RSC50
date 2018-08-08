/**
 * Copyright (c) 2007-2013 上海思源弘瑞有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.editor;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-29
 */
public class EditorConfigData {

	private String iedName;
	private String iedType;
	private int gin;
	private String type;

	public EditorConfigData() {
	}

	public EditorConfigData(String iedName){
		this.iedName = iedName;
	}
	
	public EditorConfigData(String iedName, String iedType) {
		this.iedName = iedName;
		this.iedType = iedType;
	}

	public EditorConfigData(String iedName, String iedType, int gin, String type) {
		this.iedName = iedName;
		this.iedType = iedType;
		this.gin = gin;
		this.type = type;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public String getIedType() {
		return iedType;
	}

	public void setIedType(String iedType) {
		this.iedType = iedType;
	}

	public int getGin() {
		return gin;
	}

	public void setGin(int gin) {
		this.gin = gin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
