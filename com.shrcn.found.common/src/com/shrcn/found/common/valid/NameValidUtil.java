/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.valid;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-3-18
 */
/**
 * $Log: NameValidUtil.java,v $
 * Revision 1.1  2013/03/29 09:37:49  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/28 06:50:21  cchun
 * Update:为主站和logicied添加名称检查
 *
 * Revision 1.3  2012/02/23 09:31:39  cchun
 * Update:去掉名称中不能有#号限制
 *
 * Revision 1.2  2011/05/30 09:50:11  cchun
 * Update:清理引用
 *
 * Revision 1.1  2011/03/18 08:39:35  cchun
 * Refactor:修改校验逻辑
 *
 */
public class NameValidUtil {
	
	public static final String INVALID_CHARS = "?, \\, /, :, \", <, >, &&, \'";
	/** 不合法字符列表. */
	public static final String[] INVALID_NAMES = { "?", "\\", //$NON-NLS-1$ //$NON-NLS-2$
            "/", ":", "\"", "<", ">", "&", "\'" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	private NameValidUtil() {}
	
	/**
	 * 判断是否含有非法字符
	 * @param text
	 * @return
	 */
	public static boolean haveInvalidChar(String text) {
		if (text == null) {
			return false;
		}
		for (int i = 0; i < INVALID_NAMES.length; i++) {
			if (text.indexOf(INVALID_NAMES[i]) != -1) {
				return true;
			}
		}
		return false;
	}
}
