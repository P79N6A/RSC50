/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.model;

import java.util.List;


/**
 * 
 * @author 周思韵(mailto:zsy.14193@shrcn.com)
 * @version 1.0, 2013-4-8
 */
/**
 * $Log: UIConfig.java,v $
 * Revision 1.1  2013/07/05 08:11:45  cxc
 * add：创建FPK对话框表格
 *
 * Revision 1.2  2013/06/14 01:37:43  cchun
 * Refactor:提取 TableManager
 *
 * Revision 1.1  2013/05/17 06:53:07  cxc
 * Refactor：修改UIConfig
 *
 * Revision 1.2  2013/04/12 09:12:41  scy
 * Update：删除无用属性
 *
 * Revision 1.1  2013/04/10 06:21:09  zsy
 * Add:创建表格和表单
 *
 */
public abstract class AUIConfig {
	
	protected TableManager tableManager;
	
	public String getUIDefine(String key) {
		return tableManager.getUIDefine(key);
	}
	
	/**
	 * 获取表单配置信息。 --------------------add
	 * @param panelConfig
	 * @return
	 */
	public Form getForm(String panelConfig){
		return tableManager.getForm(panelConfig);
	}
	
	public TableConfig getTableConfig(String configPath) {
		return tableManager.getTableConfig(configPath);
	}
	
	/**
	 * 得到指定表格配置。        ----------------------add
	 * @param panelKey
	 * @return
	 */
	public TableConfig getDefinedTable(String panelKey) {
		return tableManager.getDefinedTable(panelKey);
	}
	
	/**
	 * 得到指定表单配置。        ----------------------add
	 * @param panelKey
	 * @return
	 */
	public Form getDefinedForm(String panelKey) {
		return tableManager.getDefinedForm(panelKey);
	}

	/**
	 * 获取TabFolder配置信息。 ----------------------------add
	 * @param configPath
	 * @return
	 */
	public List<TableConfig> getTabFolderConfig(String configPath) {
		return tableManager.getTabFolderConfig(configPath);
	}
}
