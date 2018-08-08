/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.valid;

import java.util.List;

import org.eclipse.jface.dialogs.IInputValidator;

import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-3-18
 */
/**
 * $Log: RTUNameValidator.java,v $
 * Revision 1.3  2013/07/22 05:30:19  cchun
 * Update:名称重复不区分大小写
 *
 * Revision 1.2  2013/05/20 11:42:03  cchun
 * Update:修改提示
 *
 * Revision 1.1  2013/03/29 09:37:48  cchun
 * Add:创建
 *
 * Revision 1.2  2012/11/28 06:50:21  cchun
 * Update:为主站和logicied添加名称检查
 *
 * Revision 1.1  2011/03/18 08:39:35  cchun
 * Refactor:修改校验逻辑
 *
 */
public class NameValidator implements IInputValidator {

	private List<String> names;
	
	public NameValidator(List<String> names) {
		this.names = names;
	}
	
	@Override
	public String isValid(String newText) {
		if (!StringUtil.isValidName(newText))
			return "只允许输入中英文字符、数字和下划线，且数字不允许为第一个字符！";
		boolean exists = false;
		for (String name : names) {
			if (newText.equalsIgnoreCase(name)) {
				exists = true;
				break;
			}
		}
		if (exists)
			return "存在重复的名称（不分大小写）！";
		if (NameValidUtil.haveInvalidChar(newText))
			return "不允许含有下列字符：" + NameValidUtil.INVALID_CHARS + "！";
		return null;
	}
}
