/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Device Customization Platform System.
 */

package com.shrcn.found.common.valid;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;

/**
 * 名称校验.
 * 
 * @author 刘静
 * @date 2008-11-27
 */
public class NewNameValidator implements IInputValidator {
    /**用来保存已经存在的名称信息*/
	private List<String> itemList = null;

	public NewNameValidator(List<String> itemList) {
		this.itemList = itemList;
	}
    
	 /**
     * 重写父类方法，每次输入都去验证是否满足条件
     * @param newText 验证的名称的文本信息
     */
	@Override
	public String isValid(String newText) {
		String valid = isSameValue(newText);
		if (valid != null) {
			return valid;
		}
		String result = checkValueValid(newText);

		if (result != null) {
			return result;
		}
		return valid;
	}
    /**
     * 判断是否存在相同的名字
     * 
     * @param newText 验证的名称
     */
	private String isSameValue(String newText) {
		if (newText == null || newText.trim().equals("")) { //$NON-NLS-1$
			return "名称不能为空";
		}
		//寻找是否有相同的名称
		for (String name : itemList) {
			if (name.equals(newText)) {
				return "存在相同名称的节点";
			}
		}

		return null;
	}

	 /**
     * 判断条件的正则表达式
     * 
     * @param newText 验证的名称
     */
	public static String checkValueValid(String newText) {
		String firstChar = newText.substring(0, 1);
        //正则表达式，判断是否为数字
		Pattern pattern = Pattern.compile("[\\d]"); //$NON-NLS-1$
		Matcher matcher = pattern.matcher(firstChar);

		if (matcher.matches()) {
			return "首字母不能是数字";
		}
		if (NameValidUtil.haveInvalidChar(newText)) {
			return NameValidUtil.INVALID_CHARS + " 为非法字符";
		}

		return null;
	}
	
	/**
	 * 判断装置名称的接口。
	 * 
	 * @param newText 验证的名称
	 */
	public static String checkIEDNameValid(String newText) {
		String firstChar = newText.substring(0, 1);
        //正则表达式，判断是否为数字
		Pattern pattern = Pattern.compile("[\\d]"); //$NON-NLS-1$
		Matcher matcher = pattern.matcher(firstChar);

		if (matcher.matches()) {
			return "首字母不能是数字";
		}
		
		// 允许减号作为合法字符，后续会对兼容的字符（减号）进行警告提示。
		pattern = Pattern.compile("[A-Za-z0-9_-]+"); //$NON-NLS-1$
		matcher = pattern.matcher(newText);
		if (!matcher.matches()){
			return "请输入合法字符：英文字母大小写、数字以及下划线。";
		}

		return null;
	}
	
	/**
	 * 检查警告值，允许输入。
	 * 
	 * @param newText
	 * @return
	 */
	public static String checkIEDNameWarn(String newText) {
		if (hasMinus(newText)) {
			return "名称中有减号，可能会对通信产生影响，请谨慎使用！";
		}
		return null;
	}

	private static boolean hasMinus(String newText){
		return newText.contains("-");
	}
}
