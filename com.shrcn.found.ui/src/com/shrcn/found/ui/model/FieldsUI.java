/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;



/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2014-1-19
 */
public class FieldsUI {
	
	protected String name;
	protected String desc;
	protected String className;
	protected String tClassName;
	public String gettClassName() {
		return tClassName;
	}

	public void settClassName(String tClassName) {
		this.tClassName = tClassName;
	}

	protected boolean hasExd;
	protected IField[] fields;

	public FieldsUI() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public IField getField(String name) {
		for (IField field : getFields()) {
			if (field.getName().equals(name)) {
				return field;
			}
		}
		return null;
	}

	public IField[] getFields() {
		return fields;
	}

	public void setFields(IField[] fields) {
		this.fields = fields;
	}

	public boolean isHasExd() {
		return hasExd;
	}

	public void setHasExd(boolean hasExd) {
		this.hasExd = hasExd;
	}
}