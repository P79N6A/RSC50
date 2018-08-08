/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import org.eclipse.jface.wizard.WizardPage;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-25
 */
/**
 * $Log: ContextPage.java,v $
 * Revision 1.1  2013/03/29 09:36:47  cchun
 * Add:创建
 *
 * Revision 1.3  2012/01/13 08:39:55  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.2  2011/08/01 08:19:06  cchun
 * Update:为向导对话框添加校验处理
 *
 * Revision 1.1  2011/07/27 07:34:39  cchun
 * Add:向导基础类
 *
 */
public abstract class ContextPage extends WizardPage {
	
	protected WizardContext context;
	
	protected ContextPage(String name, WizardContext context) {
		super(name);
		this.context = context;
		setPageComplete(false);
	}

	public WizardContext getContext() {
		return context;
	}
	
	public abstract boolean nextButtonClick();
	public abstract boolean checkInput();
}