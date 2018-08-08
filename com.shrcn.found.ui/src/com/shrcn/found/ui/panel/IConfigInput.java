/**
 * Copyright (c) 2007-2010 上海思源弘瑞有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.panel;

import java.util.Map;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-9
 */
/**
 * $Log: IConfigInput.java,v $
 * Revision 1.1  2013/03/29 09:55:11  cchun
 * Add:创建
 *
 * Revision 1.1  2012/11/09 09:35:11  cchun
 * Refactor:添加IConfigInput接口
 *
 */
public interface IConfigInput {
	
	public void createContent();
	
	public void addListeners();
	
	public void setDefaults();

	public void setObject(Object value);
	
	public void initConfig();
	
	public Map<String, String> getMapValues();
	
}
