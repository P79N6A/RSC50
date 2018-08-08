/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

/**
 * 
 * 数据选择器接口
 * 
 * @author 周泉(mailto:zq@shrcn.com)
 * @version 1.0, 2014-8-21
 */
/**
 * $Log$
 */
public interface IValueSelector {
	
	Object getValue();
	
	void setValue(Object value);
}
