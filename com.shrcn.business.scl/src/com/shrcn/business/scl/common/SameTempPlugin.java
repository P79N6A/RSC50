/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-7
 */
/*
 * 修改历史 $Log: SameTempPlugin.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:53  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.2  2009/12/02 03:36:12  lj6061
 * 修改历史 Fix：多个节点删除Bug
 * 修改历史 add:删除后界面中文本框自动清空
 * 修改历史
 * 修改历史 Revision 1.1  2009/12/01 07:48:54  lj6061
 * 修改历史 添加重复模板处理
 * 修改历史
 * 修改历史 Revision 1.2  2009/05/12 06:11:01  cchun
 * 修改历史 Update:添加editor view事件类型参数
 * 修改历史
 * 修改历史 Revision 1.1  2009/05/07 06:38:01  cchun
 * 修改历史 Add:修改懒加载实现
 * 修改历史
 */
public class SameTempPlugin {
	
	/**
	 * 单例对象
	 */
	private static volatile SameTempPlugin secNavgPlugin = new SameTempPlugin();

	/**
	 * 单例模式私有构造函数
	 */
	private SameTempPlugin(){
	}

	/**
	 * 获取单例对象
	 */
	public static SameTempPlugin getInstance(){
		if(null == secNavgPlugin) {
			synchronized (SameTempPlugin.class) {
				if(null == secNavgPlugin) {
					secNavgPlugin = new SameTempPlugin();
				}
			}
		}
		return secNavgPlugin;
	}

	ArrayList<LoadSameTempListener> myListeners = new ArrayList<LoadSameTempListener>();

	// A public method that allows listener registration
	public void addListener(LoadSameTempListener listener) {
		if (!myListeners.contains(listener))
			myListeners.add(listener);
	}

	// A public method that allows listener registration
	public void removeListener(LoadSameTempListener listener) {
		myListeners.remove(listener);
	}

	public void invoke(List<?> list,List<?> nameList) {
		for (Object obj : myListeners) {
			LoadSameTempListener element = (LoadSameTempListener) obj;
			element.loadCompareText(list,nameList);
		}
	}
	
	public void invoke(String name) {
		for (Object obj : myListeners) {
			LoadSameTempListener element = (LoadSameTempListener) obj;
			element.selectCompareText(name);
		}
	}
	
	public void invoke() {
		for (Object obj : myListeners) {
			LoadSameTempListener element = (LoadSameTempListener) obj;
			element.openCompareDialog();
		}
	}

	public void clearText(String name) {
		for (Object obj : myListeners) {
			LoadSameTempListener element = (LoadSameTempListener) obj;
			element.removeTemp(name);
		}
	}
}
