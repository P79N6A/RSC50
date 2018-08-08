/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-2
 */
/**
 * $Log: ITreeEntry.java,v $
 * Revision 1.6  2013/06/06 03:07:28  cxc
 * Update：设置desc属性的方法
 *
 * Revision 1.5  2013/05/17 08:33:05  cchun
 * Update:添加节点排序
 *
 * Revision 1.4  2013/05/14 07:28:01  cchun
 * Update:添加removeChild()
 *
 * Revision 1.3  2013/05/07 01:34:00  cchun
 * Update:增加setName()
 *
 * Revision 1.2  2013/04/07 12:27:36  cchun
 * Update:完成基础界面框架
 *
 * Revision 1.1  2013/04/06 05:33:41  cchun
 * Add:导航树基础类
 *
 * Revision 1.1  2013/04/03 00:34:38  cchun
 * Update:转移tree控件至found工程
 *
 * Revision 1.1  2013/04/02 13:26:44  cchun
 * Add:界面类
 *
 */
public interface ITreeEntry {
	
	ITreeEntry getParent();
	
	void setParent(ITreeEntry parent);
	
	List<ITreeEntry> getChildren();
	
	void setChildren(List<ITreeEntry> children);
	
	void addChild(ITreeEntry child);

	void addChildAll(List<ITreeEntry> children);
	
	void removeChild(ITreeEntry child);
	
	String getName();
	
	void setName(String name);
	
	int getIndex();
	
	void setIndex(int index);
	
	String getDesc();
	
	void setDesc(String name);

	String getIcon();
	
	ITreeEntry copy();
}
