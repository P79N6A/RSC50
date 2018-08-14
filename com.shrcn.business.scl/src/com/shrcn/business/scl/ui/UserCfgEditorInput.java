/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.ui;

import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2015-12-23
 */
public class UserCfgEditorInput extends SCTEditorInput {

	private String xpath;
	private String iedName;

	/**
	 * 构造函数
	 * @param name
	 * @param editorId
	 */
	public UserCfgEditorInput(String name, String editorId, String iedName) {
		super(name, editorId);
	}
	
	/**
	 * 构造函数
	 * @param entry
	 * @param editorId
	 */
	public UserCfgEditorInput(INaviTreeEntry entry, String editorId, String iedName) {
		this(IEDDAO.getName(entry.getName()), entry.getXPath(), editorId, iedName);
	}

	/**
	 * 构造函数
	 * @param name
	 * @param xpath
	 * @param editorId
	 */
	public UserCfgEditorInput(String name, String xpath, String editorId, String iedName) {
		super(name, editorId);
		this.xpath = xpath;
		this.iedName = iedName;
	}

	/**
	 * 返回true, 则打开该编辑器后它会出现在Eclipse主菜单"文件"最下部的最近打开的文档中. 返回false则不出现在其中
	 */
	public boolean exists() {
		return false;
	}

	public String getXpath() {
		return xpath;
	}
    
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public UserCfgEditorInput clone() {
		UserCfgEditorInput input = new UserCfgEditorInput(getName(), this.xpath, getEditorId(), this.iedName);
		return input;
	}

}
