/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-6-1
 */
/**
 * $Log: DoNode.java,v $
 * Revision 1.1  2013/03/29 09:35:21  cchun
 * Add:创建
 *
 * Revision 1.4  2011/08/10 06:31:06  cchun
 * Update:去掉不必要的属性和方法
 *
 * Revision 1.3  2010/11/08 07:14:15  cchun
 * Update:清理引用
 *
 * Revision 1.2  2010/09/03 02:42:03  cchun
 * Update:使用getter,setter
 *
 * Revision 1.1  2010/06/18 09:49:55  cchun
 * Add:添加描述导出、导入功能
 *
 */
public class DoNode {
	private String name;
	private String desc;
	private List<String> otherDAName = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getOtherDAName() {
		return otherDAName;
	}
	public void setOtherDAName(List<String> otherDAName) {
		this.otherDAName = otherDAName;
	}
	
}
