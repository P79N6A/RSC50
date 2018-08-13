/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ConfigTreeEntry.java,v $
 * Revision 1.4  2013/05/30 11:02:33  cchun
 * Fix Bug:修复导航树乱序bug
 *
 * Revision 1.3  2013/05/30 08:07:55  cxc
 * Update：增加gin属性
 *
 * Revision 1.2  2013/04/06 11:58:23  cchun
 * Fix Bug:去掉parent
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
public class ConfigTreeEntry extends ContainerEntry {

	protected String iedName;
	protected String editorId;
	private int gin;
	private Object data;
	
	public ConfigTreeEntry(String name, String desc, String icon, 
			String editorId) {
		this(null, name, desc, icon, editorId);
	}
	
	public ConfigTreeEntry(ITreeEntry parent, String name, String desc, String icon, 
			String editorId) {
		super(parent, name, desc, icon);
		this.editorId = editorId;
	}

	public String getEditorId() {
		return this.editorId;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public int getGin() {
		return gin;
	}

	public void setGin(int gin) {
		this.gin = gin;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public ITreeEntry copy() {
		ConfigTreeEntry configTreeEntry = new ConfigTreeEntry(name, desc, icon, editorId);
		configTreeEntry.setIndex(index);
		return configTreeEntry;
	}

	@Override
	public boolean isContainer() {
		return children.size() > 0;
	}
}
