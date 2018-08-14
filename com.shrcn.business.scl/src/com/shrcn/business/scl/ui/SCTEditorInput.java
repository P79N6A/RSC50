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
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-10
 */
/*
 * 修改历史 $Log: SCTEditorInput.java,v $
 * 修改历史 Revision 1.3  2011/12/02 03:43:59  cchun
 * 修改历史 Update:添加构造函数
 * 修改历史
 * 修改历史 Revision 1.2  2010/12/29 07:44:18  cchun
 * 修改历史 Update:添加setXpath
 * 修改历史
 * 修改历史 Revision 1.1  2010/08/10 06:48:58  cchun
 * 修改历史 Refactor:修改editor input类包路径，及继承关系
 * 修改历史
 * 修改历史 Revision 1.3  2009/11/13 07:18:17  cchun
 * 修改历史 Update:完善关联表格功能
 * 修改历史
 * 修改历史 Revision 1.2  2009/10/13 05:42:07  hqh
 * 修改历史 修改编辑输入
 * 修改历史 Revision 1.1 2009/08/27 02:22:44 cchun
 * Refactor:重构导航树模型包路径
 * 
 * Revision 1.13 2009/05/23 10:21:07 cchun Update:重构IEDDAO
 * 
 * Revision 1.12 2009/05/19 11:09:04 pht IED名称改为：name+":"+desc
 * 
 * Revision 1.11 2009/04/22 10:52:48 pht 名称改为SCTEditorInput
 * 
 * Revision 1.9 2009/04/17 07:32:22 hqh 修改模型导入数据
 * 
 * Revision 1.5 2009/04/17 07:15:55 cchun Refactor:清理无效代码
 * 
 * Revision 1.1 2009/04/10 00:45:09 cchun Add:添加editorinput
 * 
 */
public class SCTEditorInput extends AbstractEditorInput {

	private String xpath;
	private String toolTip="";
	
	/**
	 * 构造函数
	 * @param name
	 * @param editorId
	 */
	public SCTEditorInput(String name, String editorId) {
		super(name, editorId);
	}
	
	/**
	 * 构造函数
	 * @param entry
	 * @param editorId
	 */
	public SCTEditorInput(INaviTreeEntry entry, String editorId) {
		this(IEDDAO.getName(entry.getName()), entry.getXPath(), editorId);
		this.toolTip = entry.getToolTip();
	}

	/**
	 * 构造函数
	 * @param name
	 * @param xpath
	 * @param editorId
	 */
	public SCTEditorInput(String name, String xpath, String editorId) {
		super(name, editorId);
		this.xpath = xpath;
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

	public SCTEditorInput clone() {
		SCTEditorInput input = new SCTEditorInput(getName(), this.xpath, getEditorId());
		return input;
	}
	
	public String getToolTipText(){
		return toolTip;
	}
}