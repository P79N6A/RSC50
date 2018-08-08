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
public class ConfigEditorInput extends BaseEditorInput {
	
	public ConfigEditorInput(String editorName, String icon, String editorId,
			EditorConfigData data) {
		super(editorName, icon, editorId, data);
	}

	public String getIedName() {
		return getConfigData().getIedName();
	}

	public String getIedType() {
		return getConfigData().getIedType();
	}

	public int getGin() {
		return getConfigData().getGin();
	}

	public String getType() {
		return getConfigData().getType();
	}

	private EditorConfigData getConfigData() {
		return (EditorConfigData) getData();
	}
}
