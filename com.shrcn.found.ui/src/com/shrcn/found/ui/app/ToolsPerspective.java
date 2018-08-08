/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.app;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.shrcn.found.ui.UIConstants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ToolsPerspective.java,v $
 * Revision 1.3  2013/04/12 05:15:37  cchun
 * Update:信息窗统一使用ConsoleView
 *
 * Revision 1.2  2013/04/07 12:27:34  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.1  2013/04/06 05:18:45  cchun
 * Refactor:切换工程
 *
 * Revision 1.1  2013/04/02 13:26:33  cchun
 * Add:界面类
 *
 */
public class ToolsPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(UIConstants.View_Navg_ID, false, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addStandaloneView(UIConstants.View_Content_ID, false, IPageLayout.TOP, 0.8f, editorArea);
		layout.addStandaloneView(UIConstants.View_Console_ID, true, IPageLayout.BOTTOM, 0.15f, editorArea);
	}


}
