/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.common;

import java.util.List;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-11-30
 */
/*
 * 修改历史
 * $Log: LoadSameTempListener.java,v $
 * Revision 1.1  2013/03/29 09:36:55  cchun
 * Add:创建
 *
 * Revision 1.2  2009/12/02 03:36:12  lj6061
 * Fix：多个节点删除Bug
 * add:删除后界面中文本框自动清空
 *
 * Revision 1.1  2009/12/01 07:48:54  lj6061
 * 添加重复模板处理
 *
 */
public interface LoadSameTempListener {
	public void loadCompareText(List<?> list,List<?> nameList);
	public void selectCompareText(String name);
	public void openCompareDialog();
	public void removeTemp(String name);
}
