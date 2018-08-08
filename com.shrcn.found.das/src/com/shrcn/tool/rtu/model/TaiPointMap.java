/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import com.shrcn.found.common.Constants;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-7
 */
/**
 * $Log: TaiPointMap.java,v $ Revision 1.2 2013/04/01 03:00:50 cchun
 * Update:添加copy()
 * 
 * Revision 1.1 2012/11/07 12:00:38 cchun Update:修改计算器表达式和变量表
 * 
 */
public class TaiPointMap extends BaseCalcPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TaiPointMap() {
	}

	public TaiPointMap(int id, int varnum, String varname, TDcaMx mx,
			TDcaSt st, TDcaCo co) {
		super(id, varnum, varname, mx, st, co);
	}

	/**
	 * 获取变量前缀。
	 * 
	 * @return
	 */
	public static String getVarPrefix() {
		return "AI";
	}

	@Override
	public BaseCalcPoint copy() {
		TaiPointMap p = new TaiPointMap();
		assignBaseValues(p);
		return p;
	}
}
