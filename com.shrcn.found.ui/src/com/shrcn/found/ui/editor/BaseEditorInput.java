/**
 * Copyright (c) 2007-2013 上海杏仁软件技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.editor;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public class BaseEditorInput implements IEditorInput {
	
	private String editorName;
	private String icon;
	private String editorId;
	private Object data;
    
	public BaseEditorInput(String editorName, String icon, String editorId, Object data) {
		this.editorName = editorName;
		this.icon = icon;
		this.editorId = editorId;
		this.data = data;
	}

	public BaseEditorInput(String editorName, String icon, String editorId) {
		this(editorName, icon, editorId, null);
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getEditorId() {
		return editorId;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
