/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import com.shrcn.found.file.xml.DOM4JNodeHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-11-23
 */
public class VTDXPathUtil {
	
	public static final int CACHE_P = 3;
	
//	/**
//	 * 得到xpath属性映射表
//	 * @param select
//	 * @return
//	 */
//	public static Map<String, String> getAttrMap(String select) {
//		Map<String, String> attrMap = new HashMap<String, String>();
//		String ndAtt = select.substring(select.lastIndexOf('/') + 1);
//		String[] parts = ndAtt.split("\\[|\\]");
//		if (parts.length < 2)
//			return null;
//		for (int i=1; i<parts.length; i++) {
//			String part = parts[i];
//			if ("".equals(part))
//				continue;
//			String name = part.substring(1, part.indexOf('='));
//			String value = part.split("'")[1];
//			attrMap.put(name, value);
//		}
//		return attrMap;
//	}
//	
//	/**
//	 * 检查节点属性值是否符合映射表条件
//	 * @param node
//	 * @param attrMap
//	 * @return
//	 */
//	public static boolean matchNode(Element node, Map<String, String> attrMap) {
//		if (attrMap==null || attrMap.size()<1)
//			return true;
//		boolean match = true;
//		for (Entry<String, String> att : attrMap.entrySet()) {
//			String attValue = att.getValue();
//			String ndValue = node.attributeValue(att.getKey());
//			if (ndValue != null) {
//				if (!ndValue.equals(attValue))
//					return false;
//			} else {
//				if (attValue != null)
//					return false;
//			}
//		}
//		return match;
//	}
//
//	/**
//	 * 去掉xpath属性信息
//	 * @param select
//	 * @return
//	 */
//	public static String getPartRootXPath(String select) {
////		String xpath = new String(select);
////		int p = select.indexOf('[');
////		if (p > 0)
////			xpath = select.substring(0, p);
////		return xpath;
//		select = DOM4JNodeHelper.clearPrefix(select);
//		String temp[] = VTDXPathUtil.clearFun(select).split("/");
//		String xpath = "";
//		if (temp != null && temp.length > VTDXPathUtil.CACHE_P ) {
//			for (int i=0; i<2; i++) {
//				select = select.substring(select.indexOf('/') + 1);
//				xpath += "/" + select.substring(0, select.indexOf('/'));
//			}
//			int p = xpath.indexOf('[');
//			if (p > 0)
//				xpath = select.substring(0, p);
//		}
//		return xpath;
//	}
	
	/**
	 * 清除xpath中count()函数
	 * @param select
	 * @return
	 */
	public static String clearFun(String select) {
		String xpath = "";
		int p = select.indexOf("[count(");
		if (p > 0) {
			while (p > 0) {
				xpath += select.substring(0, p);
				select = select.substring(getIdx(select, p) + 1);
				p = select.indexOf("[count(");
			}
			xpath += select;
		} else {
			xpath = select;
		}
		return xpath;
	}
	
	/**
	 * 递归解决count语句中包含括号的问题
	 * "/scl:SCL/scl:IED[count(./scl:AccessPoint/scl:Server/scl:LDevice//scl:Inputs/scl:ExtRef[@iedName='CM103'])>0]"
	 * 
	 * @param select
	 * @param p
	 * @return
	 */
	private static int getIdx(String select, int p) {
		int firstEndIdx = select.indexOf(']', p + 1);//排除本身
		int firstStartIdx = select.indexOf('[', p + 1);
		if (firstStartIdx > firstEndIdx || firstStartIdx < 0)
			return firstEndIdx;
		else
			return getIdx(select, firstEndIdx);
	}
	
	/**
	 * 判断是否存在count()函数
	 * @param select
	 * @return
	 */
	public static boolean existsFun(String select) {
		return select.indexOf("[count(") > 0;
	}
	
	/**
	 * 得到缓存xpath
	 * @param select
	 * @return
	 */
	public static String getCacheXPath(String select) {
		select = VTDXPathUtil.clearFun(select);
		select = DOM4JNodeHelper.clearPrefix(select);
		String temp[] = select.split("/");
		if (temp != null && temp.length >= VTDXPathUtil.CACHE_P ) {
			String partName = temp[2];
			int p = partName.indexOf('[');
			if (p > -1) {
				String ndName = partName.substring(0, p);
				String nameAtt = "";
				while (p > -1) {
					String att = partName.substring(p, partName.indexOf(']')+1);
					if (att.indexOf("@name=")>0) {
						nameAtt = att;
						break;
					}
					int i = att.indexOf("name()=");
					if (i > 0) {
						i = i + "name()=".length() + 2;
						ndName = partName.substring(i, partName.indexOf('\'', i));
					}
					partName = partName.substring(partName.indexOf(']')+1);
					p =  partName.indexOf('[');
				}
				partName = ndName  + nameAtt;
			}
			String rootName = temp[1];
			int i = rootName.indexOf("name()=");
			if (i > 0) {
				i = i + "name()=".length() + 1;
				rootName = rootName.substring(i, rootName.indexOf('\'', i));
			}
			return "/" + rootName + "/" + partName;
		}
		return null;
	}
	
	/**
	 * 得到缓存类型xpath
	 * @param select
	 * @return
	 */
	public static String getPartRootXPath(String select) {
		select = VTDXPathUtil.clearFun(select);
		select = DOM4JNodeHelper.clearPrefix(select);
		String temp[] = select.split("/");
		if (temp != null && temp.length >= VTDXPathUtil.CACHE_P ) {
			String partName = temp[2];
			int p = partName.indexOf('[');
			if (p > 0) {
				partName = partName.substring(0, p);
			}
			return "/" + temp[1] + "/" + partName;
		}
		return null;
	}
	
	/**
	 * 判断xpath是否遍历缓存
	 * @param select
	 * @return
	 */
	public static boolean isPartRoot(String select) {
		return getCacheXPath(select).equals(getPartRootXPath(select));
	}

	/**
	 * 得到子节点xpath
	 * @param select
	 * @return
	 */
	public static String getSubXPath(String select) {
		select = DOM4JNodeHelper.clearPrefix(select);
		String temp[] = VTDXPathUtil.clearFun(select).split("/");
		if (temp != null && temp.length >= VTDXPathUtil.CACHE_P) {
			for (int i=0; i<2; i++) {
				select = select.substring(select.indexOf('/') + 1);
			}
			int p1 = select.indexOf('[');
			int p2 = select.indexOf('/');
			if (p1 > -1 && p2 > -1) {
				int p = (p1<p2) ? p1 : p2;
				return "." + select.substring(p);
			} else if (p1 > -1) {
				return "." + select.substring(p1);
			} else if (p2 > -1) {
				return "." + select.substring(p2);
			} else {
				return ".";
			}
		}
		return ".";
	}
	
	public static boolean isCachePart(String select) {
		select = VTDXPathUtil.clearFun(select);
		String[] temp = select.split("/");
		return temp != null && temp.length == VTDXPathUtil.CACHE_P ;
	}
	
	public static boolean isRoot(String select) {
		select = VTDXPathUtil.clearFun(select);
		String[] temp = select.split("/");
		return temp != null && temp.length == (VTDXPathUtil.CACHE_P  - 1);
	}
	
}

