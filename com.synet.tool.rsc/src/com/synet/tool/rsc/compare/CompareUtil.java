package com.synet.tool.rsc.compare;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.shrcn.found.common.util.ArrayUtil;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.FileManipulate;
import com.synet.tool.rsc.util.IndexedJSONUtil;

public class CompareUtil {

	/**
	 * 比较两个xml节点文本md5码是否一致
	 * @param ndSrc
	 * @param ndDest
	 * @return
	 */
	public static boolean matchMd5(Element ndSrc, Element ndDest) {
		return matchMd5(ndSrc.asXML(), ndDest.asXML());
	}
	
	/**
	 * 比较两个obj节点json文本md5码是否一致
	 * @param objSrc
	 * @param objDest
	 * @return
	 */
	public static boolean matchMd5(Object objSrc, Object objDest) {
		return matchMd5(IndexedJSONUtil.getJson(objSrc), IndexedJSONUtil.getJson(objDest));
	}
	
	/**
	 * 比较两个字符串md5码是否一致
	 * @param objSrc
	 * @param objDest
	 * @return
	 */
	public static boolean matchMd5(String objSrc, String objDest) {
		String srcMd5 = FileManipulate.getMD5CodeForStr(objSrc);
		String destMd5 = FileManipulate.getMD5CodeForStr(objDest);
		if (srcMd5.equals(destMd5)) {
			return true;
		}
		return false;
	}

	/**
	 * 比较两个xml节点属性并返回差异信息
	 * @param ndSrc
	 * @param ndDest
	 * @return
	 */
	public static String compare(Element ndSrc, Element ndDest) {
		return compare(ndSrc, ndDest, null);
	}
	
	/**
	 * 比较两个xml节点属性并返回差异信息
	 * @param ndSrc
	 * @param ndDest
	 * @param atts
	 * @return
	 */
	public static String compare(Element ndSrc, Element ndDest, String...atts) {
		String msg = "";
		Iterator<Attribute> iterator = ndSrc.attributeIterator();
		while (iterator.hasNext()) {
			Attribute att = iterator.next();
			String name = att.getName();
			if ("md5".equals(name) || 
					!(atts==null || ArrayUtil.contains(name, atts))) {
				continue;
			} else {
				String valueSrc = att.getValue();
				String valueDest = ndDest.attributeValue(name);
				if (!valueSrc.equals(valueDest)) {
					if (!"".equals(msg)) {
						msg += ",";
					}
					msg += name + ":" + valueSrc + "->" + valueDest;
				}
			}
		}
		return msg;
	}
	
	/**
	 * 比较json对象属性
	 * @param jsSrc
	 * @param jsDest
	 * @return
	 */
	public static String compare(JSONObject jsSrc, JSONObject jsDest) {
		String msg = "";
		Iterator<String> iterator = jsSrc.keySet().iterator();
		while (iterator.hasNext()) {
			String fname = iterator.next();
			String valueSrc = jsSrc.getString(fname);
			String valueDest = jsDest.getString(fname);
			if (!valueSrc.equals(valueDest)) {
				if (!"".equals(msg)) {
					msg += ",";
				}
				msg += fname + ":" + valueSrc + "->" + valueDest;
			}
		}
		return msg;
	}
	
	/**
	 * 比较json字符串
	 * @param jsSrc
	 * @param jsDest
	 * @return
	 */
	public static String compare(String jsSrc, String jsDest) {
		return compare(JSONObject.parseObject(jsSrc), JSONObject.parseObject(jsDest));
	}
	
	/**
	 * 以指定属性为主键将子节点转成Map，便于查找
	 * @param ndParent
	 * @param att
	 * @return
	 */
	public static Map<String, Element> getChildrenMapByAtt(Element ndParent, String att) {
		Map<String, Element> map = new HashMap<>();
		Iterator<Element> iterator = ndParent.elementIterator();
		while (iterator.hasNext()) {
			Element nd = iterator.next();
			String ref = nd.attributeValue(att);
			if (ref != null) {
				map.put(ref, nd);
			}
		}
		return map;
	}
	
	/**
	 * 以指定属性为主键将子节点转成Map，便于查找
	 * @param objSet
	 * @param att
	 * @return
	 */
	public static Map<String, Object> getChildrenMapByAtt(Collection<?> objSet, String att) {
		Map<String, Object> map = new HashMap<>();
		Iterator<?> iterator = objSet.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			String ref = (String) ObjectUtil.getProperty(obj, att);
			if (ref != null) {
				map.put(ref, obj);
			}
		}
		return map;
	}
	
	/**
	 * 获取任意节点除指定属性名之外的属性值
	 * @param fcda
	 * @param except
	 * @return
	 */
	public static String getAttsMsg(Element fcda, String except) {
		String msg = "";
		Iterator<Attribute> attributeIterator = fcda.attributeIterator();
		while(attributeIterator.hasNext()) {
			Attribute att = attributeIterator.next();
			String name = att.getName();
			if (!except.equals(name) && !"md5".equals(name)) {
				if (!"".equals(msg)) {
					msg += ",";
				}
				msg += name + ":" + att.getValue();
			}
		}
		return msg;
	}
	
	/**
	 * 根据xml节点属性创建diff(add,delete)
	 * @param diffParent
	 * @param ndSub
	 * @param attName
	 * @param op
	 * @return
	 */
	public static Difference addDiffByAttName(Difference diffParent, Element ndSub, String attName, OP op) {
		String ndSubName = ndSub.getName();
		String subName = ndSub.attributeValue(attName);
		subName = StringUtil.nullToEmpty(subName);
		Difference diff = new Difference(diffParent, ndSubName, subName, CompareUtil.getAttsMsg(ndSub, attName), op);
		setDesc(diff, ndSub);
		return diff;
	}
	
	/**
	 * 根据object属性创建diff(add,delete)
	 * @param diffParent
	 * @param type
	 * @param obj
	 * @param attName
	 * @param attDesc
	 * @param op
	 * @return
	 */
	public static Difference addDiffByAttName(Difference diffParent, String type, Object obj, String attName, String attDesc, OP op) {
		String subName = (String) ObjectUtil.getProperty(obj, attName);
		String msg = IndexedJSONUtil.getJson(obj);
		Difference diff = new Difference(diffParent, type, subName, msg, op);
		diff.setDesc((String) ObjectUtil.getProperty(obj, attDesc));
		return diff;
	}
	
	/**
	 * 根据xml节点属性创建diff(update)
	 * @param diffParent
	 * @param ndSrc
	 * @param ndDest
	 */
	public static Difference addUpdateDiff(Difference diffParent, Element ndSrc, Element ndDest) {
		String msg = CompareUtil.compare(ndSrc, ndDest);
		Difference diff = new Difference(diffParent, ndSrc.getName(), "", msg, OP.UPDATE);
		setNameDesc(diff, ndSrc, ndDest);
		return diff;
	}
	
	/**
	 * 根据json数据创建diff(update)
	 * @param diffParent
	 * @param type
	 * @param name
	 * @param jsSrc
	 * @param jsDest
	 * @return
	 */
	public static Difference addUpdateDiff(Difference diffParent, String type, String name, String jsSrc, String jsDest) {
		String msg = CompareUtil.compare(jsSrc, jsDest);
		return new Difference(diffParent, type, name, msg, OP.UPDATE);
	}
	
	/**
	 * 设置名称、描述
	 * @param diff
	 * @param ndSrc
	 * @param ndDest
	 */
	public static void setNameDesc(Difference diff, Element ndSrc, Element ndDest) {
		String name = CompareUtil.getAttribute(ndSrc, "name");
		diff.setName(name);
		setDesc(diff, ndSrc);
		diff.setNewName(name);
		diff.setNewDesc(CompareUtil.getAttribute(ndDest, "desc"));
	}
	
	/**
	 * 子节点排序
	 * @param diff
	 */
	public static void sortChildren(Difference diff) {
		List<Difference> subDiffs = diff.getChildren();
		sortDiffs(subDiffs);
	}

	/**
	 * 差异排序
	 * @param subDiffs
	 */
	public static void sortDiffs(List<Difference> subDiffs) {
		Collections.sort(subDiffs, new Comparator<Difference> () {
			@Override
			public int compare(Difference diff1, Difference diff2) {
				String type1 = diff1.getType() + diff1.getName();
				String type2 = diff2.getType() + diff2.getName();
				return type1.compareTo(type2);
			}});
	}
	
	/**
	 * 获取xml节点属性
	 * @param nd
	 * @param att
	 * @return
	 */
	public static String getAttribute(Element nd, String att) {
		return StringUtil.nullToEmpty(nd.attributeValue(att));
	}
	
	/**
	 * 设置描述
	 * @param diff
	 * @param nd
	 */
	public static void setDesc(Difference diff, Element nd) {
		diff.setDesc(getAttribute(nd, "desc"));
	}

	/**
	 * 获取更新信息
	 * @param msg
	 * @return
	 */
	public static Map<String, String> getUpdateInfo(String msg) {
		Map<String, String> updateInfo = new HashMap<>();
		if (!StringUtil.isEmpty(msg)) {
			String[] atts = msg.split(",");
			for (String att : atts) {
				String[] temp = att.split(":");
				String[] change = temp[1].split("->");
				String newValue = change.length > 1 ? change[1] : "";
				updateInfo.put(temp[0], newValue);
			}
		}
		return updateInfo;
	}
	
	/**
	 * 获取新增信息
	 * @param msg
	 * @return
	 */
	public static Map<String, String> getDisInfo(String msg) {
		Map<String, String> updateInfo = new HashMap<>();
		if (!StringUtil.isEmpty(msg)) {
			String[] atts = msg.split(",");
			for (String att : atts) {
				String[] temp = att.split(":");
				updateInfo.put(temp[0], temp[1]);
			}
		}
		return updateInfo;
	}
}
