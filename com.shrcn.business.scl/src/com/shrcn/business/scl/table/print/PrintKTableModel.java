/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.table.print;

import de.kupzog.ktable.KTableModel;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-17
 */
/**
 * $Log: PrintKTableModel.java,v $
 * Revision 1.1  2010/03/02 07:49:14  cchun
 * Add:添加重构代码
 *
 * Revision 1.1  2009/11/19 08:28:52  cchun
 * Update:完成信号关联打印功能
 *
 */
public interface PrintKTableModel extends KTableModel {
	int getRowHeight();
	int getFirstRowHeight();
	int getFixedColumnCount();
	int getFixedRowCount();
}
