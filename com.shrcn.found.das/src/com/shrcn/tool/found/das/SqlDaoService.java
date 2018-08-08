/**
 * Copyright (c) 2007-2014 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das;

import java.sql.SQLException;
import java.util.List;


/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-12-17
 */
public interface SqlDaoService {
	/**
	 * 根据新旧上一级ID号，在当前表格进行拷贝
	 * 
	 * @param clazz
	 * @param parentStr
	 * @param parentId
	 * @param copyId
	 * @throws SQLException
	 */
	public void copyCurrTable(Class<?> clazz, String parentStr, int parentId, int copyId);
	
	/**
	 * 根据上一级ID获取，当前表格ID列表.
	 * 
	 * @param clazz
	 * @param parentStr
	 * @param parentId
	 * @return
	 */
	public List<Integer> getIds(Class<?> clazz, String parentStr, int parentId);
	
}
