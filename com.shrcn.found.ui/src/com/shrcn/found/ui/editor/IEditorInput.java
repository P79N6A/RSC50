/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司 All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.editor;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
public interface IEditorInput {

	public abstract String getEditorId();

	public abstract void setEditorId(String editorId);
	
	public abstract String getEditorName();

	public abstract void setEditorName(String editorName);

	public abstract String getIcon();

	public abstract void setIcon(String icon);
	
	public abstract Object getData();

	public abstract void setData(Object data);

}