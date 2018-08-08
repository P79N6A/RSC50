/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.action;

import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-3
 */
/**
 * $Log: ConfigAction.java,v $
 * Revision 1.2  2013/04/12 09:05:35  scy
 * Update：继承MenuAction，修改构造函数
 *
 * Revision 1.1  2013/04/06 05:31:57  cchun
 * Add:配置action
 *
 */
public class ConfigAction extends MenuAction {

	protected ITreeEntry selEntry;
	
	public ConfigAction(String title) {
		super(title);
	}

	public ITreeEntry getSelEntry() {
		return selEntry;
	}

	public void setSelEntry(ITreeEntry selEntry) {
		this.selEntry = selEntry;
	}
	
}
