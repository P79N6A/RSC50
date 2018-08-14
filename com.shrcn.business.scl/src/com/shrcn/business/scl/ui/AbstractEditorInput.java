/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-8-10
 */
/**
 * $Log: AbstractEditorInput.java,v $
 * Revision 1.2  2010/12/29 07:43:55  cchun
 * Update:添加setName(),setEditorId()
 *
 * Revision 1.1  2010/08/10 06:48:58  cchun
 * Refactor:修改editor input类包路径，及继承关系
 *
 */
public abstract class AbstractEditorInput implements IEditorInput {
	
	private String name;
	private String editorId;

	public AbstractEditorInput(String name, String editorId) {
		this.name = name;
		this.editorId = editorId;
	}
	
	/**
	 * 返回浮动提示文本
	 */
	@Override
	public String getToolTipText() {
		return name;
	}
	
	/**
	 * 根据class返回适配器对象
	 * @param adapter
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	/**
	 * 返回图标
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	
	/**
	 * 返回true, 则打开该编辑器后它会出现在Eclipse主菜单"文件"最下部的最近打开的文档中.
	 * 返回false则不出现在其中
	 */
	@Override
	public boolean exists() {
		return false;
	}
	
	/**
	 * 返回一个可以用作保存本编辑输入数据的对象
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * 返回标题
	 */
	@Override
	public String getName() {
		return name;
	}

	public String getEditorId() {
		return editorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}
}
