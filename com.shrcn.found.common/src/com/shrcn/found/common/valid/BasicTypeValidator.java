/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.valid;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-4-13
 */
/*
 * 修改历史
 * $Log: BasicTypeValidator.java,v $
 * Revision 1.1  2013/03/29 09:37:48  cchun
 * Add:创建
 *
 * Revision 1.5  2010/08/24 01:25:39  cchun
 * Update:增加保留字符
 *
 * Revision 1.4  2009/05/15 02:15:40  hqh
 * 修改数字校验
 *
 * Revision 1.3  2009/05/06 10:50:35  lj6061
 * updata判断重复
 *
 * Revision 1.2  2009/04/29 09:41:32  lj6061
 * 添加GSE功能
 *
 * Revision 1.1  2009/04/23 11:21:27  wyh
 * Schema基本类型校验
 *
 */
public class BasicTypeValidator {

	//布尔值的校验
	public static boolean checkBooleanValid(String newText) {
		Pattern pattern = Pattern.compile("[01]|false|true");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	//无符号整型的校验
	public static boolean checkUnsignedIntValid(String newText) {
		Pattern pattern = Pattern.compile("0|[1-9][0-9]*");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	//整型的校验
	public static boolean checkIntegerValid(String newText) {
		Pattern pattern = Pattern.compile("-?(0|[0-9][0-9]*)");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	//xs:string、xs:normalizedString、xs:token的校验
	public static boolean checkStringValid(String newText) {
		Pattern pattern = Pattern.compile("(\\s|.)*");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	//xs:decimal的校验
	public static boolean checkDecimalValid(String newText) {
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	//xs:anyURI的校验
	private static final String MARK_CHARACTERS = "-_.!#~*()";
	  private static boolean isDigit1(char p_char)
	  {
	    return p_char >= '0' && p_char <= '9';
	  }
	  private static boolean isAlpha(char p_char)
	  {
	    return ((p_char >= 'a' && p_char <= 'z')
	            || (p_char >= 'A' && p_char <= 'Z'));
	  }
	  private static boolean isAlphanum(char p_char)
	  {
	    return (isAlpha(p_char) || isDigit1(p_char));
	  }
	  private static boolean isUnreservedCharacter(char p_char)
	  {
	    return (isAlphanum(p_char) || MARK_CHARACTERS.indexOf(p_char) != -1);
	  }
	  private static final String RESERVED_CHARACTERS = ";/?:@&=+$, '";
	  private static boolean isReservedCharacter(char p_char)
	  {
	    return RESERVED_CHARACTERS.indexOf(p_char) != -1;
	  }	
	  private static boolean isDigit(char p_char)
	  {
	    return p_char >= '0' && p_char <= '9';
	  }	
	  private static boolean isHex(char p_char)
	  {
	    return (isDigit(p_char) || (p_char >= 'a' && p_char <= 'f')
	            || (p_char >= 'A' && p_char <= 'F'));
	  }
	  
		private static boolean isURIString(String p_uric)
		  {

		    if (p_uric == null)
		    {
		      return false;
		    }

		    int end = p_uric.length();
		    char testChar = '\0';

		    for (int i = 0; i < end; i++)
		    {
		      testChar = p_uric.charAt(i);

		      if (testChar == '%')
		      {
		        if (i + 2 >= end ||!isHex(p_uric.charAt(i + 1))
		                ||!isHex(p_uric.charAt(i + 2)))
		        {
		          return false;
		        }
		        else
		        {
		          i += 2;

		          continue;
		        }
		      }

		      if (isReservedCharacter(testChar) || isUnreservedCharacter(testChar))
		      {
		        continue;
		      }
		      else
		      {
		        return false;
		      }
		    }
		    return true;
		  }	
		
	public static boolean checkanyURIValid(String newText) {
		Pattern pattern = Pattern.compile("^(\\s)*.+(\\s)+.+$");
		Matcher matcher = pattern.matcher(newText);	
		if (isURIString(newText) && !matcher.matches()){
			return true;
		}
		else return false;
	}
	
	//ip地址的校验
	public static boolean checkIPValid(String newText){
		Pattern pattern = Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$");
		Matcher matcher = pattern.matcher(newText);		
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	
	// 判断是否为空
	public static boolean checkEmpty(String newText){
		boolean isValid = true;
		if (newText.equals("")) {
			isValid = !isValid;
		}
		return isValid;
	}
	
	// 判断是否重复
	public static boolean checkSameText(String newText, List<String> txtList) {
		boolean isValid = true;
		if (txtList.contains(newText) && txtList.size() != 0
				&& !newText.equals("")) {
			isValid = !isValid;
		}
		return isValid;
	}
}
