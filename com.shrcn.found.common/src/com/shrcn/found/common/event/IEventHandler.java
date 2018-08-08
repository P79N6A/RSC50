/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.common.event;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-6
 */
/**
 * $Log: IEventHandler.java,v $
 * Revision 1.1  2013/04/06 11:58:34  cchun
 * Update:完成界面事件框架
 *
 */
public interface IEventHandler {

	void execute(Context context);
}
