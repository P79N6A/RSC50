/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-8
 */
/*
 * 修改历史 $Log: LnInstMap.java,v $
 * 修改历史 Revision 1.1  2013/03/29 09:36:55  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.2  2011/06/09 08:37:46  cchun
 * 修改历史 Update:添加nextLnInst()
 * 修改历史
 * 修改历史 Revision 1.1  2009/08/28 01:33:23  cchun
 * 修改历史 Refactor:重构包路径
 * 修改历史
 * 修改历史 Revision 1.2  2009/06/15 09:40:01  lj6061
 * 修改历史 更改代码中的对电压处理
 * 修改历史
 * 修改历史 Revision 1.1  2009/06/08 10:52:22  lj6061
 * 修改历史 根据Schema添加全局的Lninst
 * 修改历史
 */
public class LnInstMap {
	/**
	 * 单例对象
	 */
	private static volatile LnInstMap instance = new LnInstMap();
	private HashMap<String, Integer> maps = new HashMap<String, Integer>();
	/**
	 * 单例模式私有构造函数
	 */
	private LnInstMap() {
	}

	/**
	 * 获取单例对象
	 */
	public static LnInstMap getInstance() {
		if (null == instance) {
			synchronized (LnInstMap.class) {
				if (null == instance) {
					instance = new LnInstMap();
				}
			}
		}
		return instance;
	}

	public Map<String, Integer> getMaps() {
		return maps;
	}

	public void clearMap(){
		maps.clear();
	}
	
	/**
	 * 获取下一个LN inst值
	 * @param lnClass
	 * @return
	 */
	public int nextLnInst(String lnClass) {
		int max = getLnInst(lnClass);
		max++;
		setLnInst(lnClass, max);
		return max;
	}

	public int getLnInst(String key) {
		if (maps.containsKey(key)) {
			return maps.get(key);
		} else {
			return 0;
		}
	}

	public void setLnInst(String key, int lnInst) {
		if (maps.containsKey(key)) {
			maps.remove(key);
		}
		maps.put(key, lnInst);

	}
}
