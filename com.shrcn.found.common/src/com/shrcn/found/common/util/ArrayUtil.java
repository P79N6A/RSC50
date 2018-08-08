/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-8-18
 */
/**
 * $Log: ArrayUtil.java,v $
 * Revision 1.3  2013/05/16 00:39:43  cchun
 * Update:添加合并单元板卡参数处理方法
 *
 * Revision 1.2  2013/05/07 01:30:54  cchun
 * Update:添加mergeStrings()
 *
 * Revision 1.1  2013/03/29 09:36:31  cchun
 * Add:创建
 *
 * Revision 1.8  2011/12/02 03:43:09  cchun
 * Update:添加isEmpty(),sortDsc(),reserve()
 *
 * Revision 1.7  2011/11/22 09:00:48  cchun
 * Update:添加containsEmpty()
 *
 * Revision 1.6  2011/01/24 07:01:21  cchun
 * Update:字符包含不区分大小写
 *
 * Revision 1.5  2010/12/24 06:39:06  cchun
 * Update:增加contains()方法
 *
 * Revision 1.4  2010/12/21 07:31:32  cchun
 * Update:添加contains()
 *
 * Revision 1.3  2010/08/18 10:04:04  cchun
 * Update:修改方法名
 *
 * Revision 1.2  2010/08/18 08:30:44  cchun
 * Update:增加isEqual()
 *
 */
public class ArrayUtil {
	
	/**
	 * 将字符串数组合并成一个字符串
	 * @param strs
	 * @return
	 */
	public static String mergeStrings(String[] strs) {
		StringBuffer buff = new StringBuffer();
		for (String str : strs) {
			buff.append(str);
		}
		return buff.toString();
	}
	
	/**
	 * 将字符串合并到原数组
	 * @param src
	 * @param str
	 * @return
	 */
	public static String[] append(String[] src, String str) {
		int len = src.length;
		String[] dest = new String[len + 1];
		System.arraycopy(src, 0, dest, 0, len);
		dest[len] = str;
		return dest;
	}
	
	/**
	 * 判断两个字符串数组是否不一致
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	public static boolean notEqual(String[] arr1, String[] arr2) {
		int length = arr1.length;
		if (length != arr2.length)
			return true;
		for (int i=0; i<length; i++) {
			if(!arr1[i].equals(arr2[i]))
				return true;
		}
		return false;
	}
	
	/**
	 * 替换旧字符串值
	 * @param src
	 * @param old
	 * @param value
	 */
	public static void replace(String[] src, String old, String value) {
		for(int i=0; i<src.length; i++) {
			String temp = src[i];
			if(old.equals(temp)) {
				src[i] = value;
				break;
			}
		}
	}
	
	/**
	 * 判断字符串中是否包含指定的字符串序列
	 * @param str
	 * @param strs
	 * @return
	 */
	public static boolean contains(String str, String[] strs) {
		for (String temp : strs) {
			if (containsNoCase(str, temp))
				return true;
		}
		return false;
	}
	
	/**
	 * 不区分大小写，判断字符串是否包含指定的子字符串。
	 * @param str
	 * @param strSub
	 * @return
	 */
	public static boolean containsNoCase(String str, String strSub) {
		str = str.toLowerCase();
		strSub = strSub.toLowerCase();
		return str.contains(strSub);
	}
	
	/**
	 * 判断数组中是否包含某个对象
	 * @param objs
	 * @param obj
	 * @return
	 */
	public static boolean contains(Object[] objs, Object obj) {
		for (Object temp : objs) {
			if (temp == obj)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断一个数组中是否存在空
	 * @param strParame String类型的数组
	 * @return
	 */
	public static boolean containsEmpty(String[] strParame) {
		for (int i = 0; i < strParame.length; i++) {
			if (strParame[i].equals(""))
				return true;
		}
		return false;
	}
	
	/**
	 * 颠倒顺序
	 * @param arr
	 */
	public static void reserve(int[] arr) {
		int len = arr.length;
		int[] tempArr = new int[len];
		System.arraycopy(arr, 0, tempArr, 0, len);
		for (int i=0; i<len; i++) {
			arr[i] = tempArr[len - 1 - i];
		}
	}
	
	/**
	 * 降序排列
	 * @param arr
	 */
	public static void sortDsc(int[] arr) {
		java.util.Arrays.sort(arr);
		reserve(arr);
	}
	
	/**
	 * 判断是否为空
	 * @param arr
	 * @return
	 */
	public static boolean isEmpty(int[] arr) {
		return arr == null || arr.length == 0;
	}
	
	public static byte[] getLefted(byte[] buf, int offset) {
		int len = buf.length - offset;
		byte[] res = new byte[len];
		System.arraycopy(buf, offset, res, 0, len);
		return res;
	}
	
	/**
	 * 数组排序
	 * @param arr
	 */
	public static void sort(int[] arr) {
		quick_sort(arr, 0, arr.length-1);
	}
	
	//快速排序
	private static void quick_sort(int s[], int l, int r)
	{
	    if (l < r)
	    {
			//Swap(s[l], s[(l + r) / 2]); //将中间的这个数和第一个数交换 参见注1
	        int i = l, j = r, x = s[l];
	        while (i < j)
	        {
	            while(i < j && s[j] >= x) // 从右向左找第一个小于x的数
					j--;  
	            if(i < j) 
					s[i++] = s[j];
				
	            while(i < j && s[i] < x) // 从左向右找第一个大于等于x的数
					i++;  
	            if(i < j) 
					s[j--] = s[i];
	        }
	        s[i] = x;
	        quick_sort(s, l, i - 1); // 递归调用 
	        quick_sort(s, i + 1, r);
	    }
	}
}
