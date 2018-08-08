/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-17
 */
/**
 * $Log: ListUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:26  cchun
 * Add:创建
 *
 * Revision 1.2  2011/08/03 09:01:08  cchun
 * Update:增加isEmpty()
 *
 * Revision 1.1  2010/12/20 02:38:33  cchun
 * Add:List操作类
 *
 */
public class ListUtil {
	
	private ListUtil() {}
	
	/**
	 * 将集合中指定元素移动到指定位置
	 * 
	 * @param curIdx
	 * @param newIdx
	 * @param data
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String moveTo(int curIdx, int newIdx, List data) {
		if (curIdx < 0 || curIdx == newIdx)
			return null;
		int size = data.size();
		if (newIdx < 0 || newIdx > size - 1) {
			return "行号错误，未能正确移动！";
		}
		Object curObj = data.get(curIdx);
		data.remove(curIdx);
		if (newIdx == 0)
			data.add(0, curObj);
		else
			data.add(newIdx, curObj);
		return null;
	}

	/**
	 * 交换列表中的两个元素
	 * 
	 * @param list
	 * @param i
	 * @param j
	 */
	public static void swap(List<Object> list, int i, int j) {
		Object temp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temp);
	}
	
	/**
	 * 是否为空
	 * @param ls
	 * @return
	 */
	public static boolean isEmpty(List<?> ls) {
		return ls == null || ls.size() == 0;
	}
	
	/**
	 * 字符串列表排序。
	 * @param list
	 */
	public static void sortStr(List<String> list) {
		java.util.Collections.sort(list, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}});
	}
	
	/**
	 * 二分法查找
	 * @param list
	 * @param value
	 * @return
	 */
	public static int search(List<String> list, String value) {
		int start = 0;
		int end = list.size() - 1;
		while (start <= end) {
			int middle = (start + end) / 2;
			String midValue = list.get(middle);
			if (midValue.indexOf(value)==0) {
				return middle;
			}
			if (value.compareTo(midValue) < 0) {
				end = middle - 1;
			} else if (value.compareTo(midValue) > 0) {
				start = middle + 1;
			}
		}
		return -1;
	}
	
	/**
	 * 排序
	 * @param list
	 * @param c
	 */
	public static <T> void sort(List<T> list, Comparator<? super T> c) {
		if (list!=null && list.size()>1)
			quick_sort(list, 0, list.size()-1, c);
	}
	
	//快速排序
	private static <T> void quick_sort(List<T> s, int l, int r, Comparator<? super T> c)
	{
	    if (l < r)
	    {
	        int i = l, j = r;
	        T x = s.get(l);
	        while (i < j)
	        {
	            while(i < j && c.compare(s.get(j), x) >= 0) // 从右向左找第一个小于x的数
					j--;  
	            if(i < j) { 
	            	s.set(i++, s.get(j));
	            }
	            while(i < j && c.compare(s.get(i), x) < 0) // 从左向右找第一个大于等于x的数
					i++;  
	            if(i < j) {
	            	s.set(j--, s.get(i));
	            }
	        }
	        s.set(i, x);
	        quick_sort(s, l, i - 1, c); // 递归调用 
	        quick_sort(s, i + 1, r, c);
	    }
	}
}
