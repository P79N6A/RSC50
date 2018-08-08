/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.business.ui;

import com.shrcn.found.common.util.StringUtil;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-7-14
 */
/**
 * $Log: NetPortUtil.java,v $
 * Revision 1.4  2013/08/06 07:37:49  scy
 * Fix Bug:修复越界异常。
 *
 * Revision 1.3  2013/08/05 04:50:32  scy
 * Update：增加A、B网不可同时为一个网口的校验
 *
 * Revision 1.2  2013/07/22 05:47:30  cchun
 * Update:增加getNetLimit(),EDITOR
 *
 * Revision 1.1  2013/07/18 13:38:48  cchun
 * Udpate:增加panel编辑方式
 *
 */
public class NetPortUtil {
	
	public static final String NET_SELECTOR = "netSelector";

	public static int[] getNetLimit(String editor) {
		String[] split = editor.split("_");
		int num = Integer.parseInt(split[1]);
		int count = Integer.parseInt(split[2]);
		int netCol = -1;
		if (split.length > 3) {
			if (!" ".equals(split[3])) {
				netCol = Integer.parseInt(split[3]);
			}
		}
		
		//int netCol = split.length == 3 ? -1 : Integer.parseInt(split[3]);
		String[] sp = new String[2];
		if (split.length > 4) {
			sp = split[4].split(",");
		}
		return new int[] {num, count, netCol, StringUtil.nullToInt(sp[0], 0), StringUtil.nullToInt(sp[1], 0)};
	}

	/**
	 * 通过网口名字获取网口对应的数据值.
	 * 位0至位7分别表示A网的网口1至网口8
	 * 
	 * @param netName
	 * @param size 网口个数.
	 * @return
	 */
	public static String getNetValue(String netName, int size) {
		if (StringUtil.isEmpty(netName))
			return "";
		if (netName.length() == size && !netName.contains("F"))// TODO 可用正则表达式代替
			return netName;
		netName = netName.replace("F", "").replace(",", "");
		return getValue(netName, size);
	}
	
	public static String getValue(String netName, int size) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= size; i++) 
			sb.append(netName.indexOf(String.valueOf(i)) > -1 ? 1 : 0);
		return sb.toString();
	}

	/**
	 *  通过网口数据值获取网口名字.
	 * 位0至位7分别表示A网的网口1至网口8
	 * 
	 * @param netValue
	 * @param size
	 * @return
	 */
	public static String getNetName(String netValue, int size) {
		if (StringUtil.isEmpty(netValue))
			return "";
		boolean isFirst = true;
		char[] charArray = netValue.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= size; i++) {
			if ((i - 1 < charArray.length) && charArray[i - 1] == '1') {
				sb.append(isFirst ? "" : ",");
				sb.append("F" + i);
				isFirst = false;
			}
		}
		return sb.toString();
	}
}
