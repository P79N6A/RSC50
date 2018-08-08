/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司 . All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.editor;

import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
/**
 * $Log: IConfigEditor.java,v $
 * Revision 1.8  2013/11/11 07:31:27  cchun
 * Update:增加refresh()
 *
 * Revision 1.7  2013/08/01 06:20:23  cchun
 * Update:增加disposed属性
 *
 * Revision 1.6  2013/04/18 13:39:09  zsy
 * Update:增加setDirty方法
 *
 * Revision 1.5  2013/04/17 07:31:19  scy
 * Update：增加界面数据初始化方法
 *
 * Revision 1.4  2013/04/07 12:27:33  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.3  2013/04/07 02:31:04  cchun
 * Refactor:修改getEditorInput()为getInput()
 *
 * Revision 1.2  2013/04/06 11:58:06  cchun
 * Update:添加dispose()
 *
 * Revision 1.1  2013/04/06 05:33:25  cchun
 * Add:editor基础类
 *
 */
public interface IConfigEditor {

	public void init();
	
	public void initData();
	
	public void buildUI(Composite container);
	
	public void dispose();

	public void refresh();
	
	public void refresh(IEditorInput input);

	public void setInput(IEditorInput input);
	
	public IEditorInput getInput();
	
	public boolean isDirty();

	public boolean doSave();
	
	public void setDirty(boolean isDirty);
	
	public boolean isDisposed();

	public void setDisposed(boolean disposed);
}
