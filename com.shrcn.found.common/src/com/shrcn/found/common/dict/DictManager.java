/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.dict;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.util.PropertyFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-12-10
 */
/**
 * $Log: DictManager.java,v $
 * Revision 1.4  2013/09/26 12:47:19  cchun
 * Update:去掉getId()
 *
 * Revision 1.3  2013/09/17 12:53:09  cchun
 * Update:增加整数值
 *
 * Revision 1.2  2013/08/13 12:45:35  cchun
 * Udpate:增加addDict()
 *
 * Revision 1.1  2013/03/29 09:38:04  cchun
 * Add:创建
 *
 * Revision 1.3  2011/12/13 02:35:42  cchun
 * Update:将字典和枚举分离
 *
 * Revision 1.2  2011/11/23 09:29:11  cchun
 * Update:通过包装使其支持简单字典类型
 *
 * Revision 1.1  2011/01/04 09:26:02  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.2  2010/12/16 02:03:38  cchun
 * Update:增加hasValue()，修改方法名
 *
 * Revision 1.1  2010/12/13 02:11:49  cchun
 * Update:数据字典改用xml格式
 *
 */
public class DictManager {

	private static DictManager instance = new DictManager();

	private Map<String, DictItem> dictCache = new Hashtable<String, DictItem>();
	
	private DictManager() {
		
	}
	
	public static DictManager getInstance() {
		if(null == instance) {
			instance = new DictManager();
		}
		return instance;
	}
	
	/**
	 * 初始化字典信息
	 */
	public void init(Class<?> baseClass, String cfgPath) {
		init(baseClass, cfgPath, false);
	}
	
	public void init(Class<?> baseClass, String cfgPath, boolean fromFile) {
		String dictFileName = cfgPath.substring(cfgPath.lastIndexOf("/")+1);
		String userDictPath = Constants.cfgDir + File.separator + dictFileName;
		Properties dictData = new Properties();
		if (fromFile)
			PropertyFileManager.initFile(dictData, userDictPath, cfgPath, baseClass);
		else
			PropertyFileManager.loadRes(dictData, cfgPath, baseClass);
		Enumeration<Object> keys = dictData.keys();
		while (keys.hasMoreElements()) {
			String typeid = (String) keys.nextElement();
			DictItem dictType = new DictItem(typeid, null);
			String[] items = dictData.get(typeid).toString().split(",");
			DictItem dictItem = null;
			for (String item : items) {
				int p = item.indexOf(':');
				if (p > 0) {
					String id = item.substring(0, p);
					String name = item.substring(p + 1);
					dictItem = new DictItem(id, name);
				} else {
					dictItem = new DictItem(item, item);
				}
				if (dictItem != null) {
					dictType.addItem(dictItem);
					dictType.setType((p > 0) ? DictItem.DICT_TYPE : DictItem.ENUM_TYPE);
				}
			}
			dictCache.put(typeid, dictType);
		}
	}
	
	public void addDict(String typeid, String typename, String[][] items) {
		DictItem dictType = new DictItem(typeid, typename);
		for (String[] item : items) {
			String id = item[0];
			String name = item[1];
			DictItem dictItem = new DictItem(id, name);
			dictType.addItem(dictItem);
			dictType.setType((id.equals(name)) ? DictItem.ENUM_TYPE : DictItem.DICT_TYPE);
		}
		if(items.length==0){
			return;
		}
		dictCache.put(typeid, dictType);
	}
	
	public void removeDict(String typeid) {
		dictCache.remove(typeid);
	}
	/**
	 * 根据类型获取名称集合
	 * @param type
	 * @return
	 */
	public String[] getDictIds(String type) {
		DictItem dictType = dictCache.get(type);
		if (dictType != null) {
			List<String> names = new ArrayList<String>();
			for (DictItem item : dictType.getItems()) {
				names.add(item.getId());
			}
			return names.toArray(new String[names.size()]);
		} else {
			return new String[0];
		}
	}
	
	/**
	 * 根据类型获取名称集合
	 * @param type
	 * @return
	 */
	public String[] getDictNames(String type) {
		DictItem dictType = dictCache.get(type);
		if (dictType != null) {
			List<String> names = new ArrayList<String>();
			for (DictItem item : dictType.getItems()) {
				names.add(item.getName());
			}
			return names.toArray(new String[names.size()]);
		} else {
			return new String[0];
		}
	}
	
	/**
	 * 根据名称和类型获取字典id
	 * @param type
	 * @param name
	 * @return
	 */
	public String getIdByName(String type, String name) {
		DictItem dictType = dictCache.get(type);
		if (dictType != null) {
			for (DictItem item : dictType.getItems()) {
				if (name.equals(item.getName()))
					return item.getId();
			}
			return name;
		} else {
			return "";
		}
	}
	
	public boolean getBoolIdByName(String type, String name) {
		return Boolean.valueOf(getIdByName(type, name));
	}
	
	/**
	 * 根据id和类型获取字典名称
	 * @param type
	 * @param id
	 * @return
	 */
	public String getNameById(String type, String id) {
		DictItem dictType = dictCache.get(type);
		if (dictType != null) {
			for (DictItem item : dictType.getItems()) {
				if (id.equals(item.getId()))
					return item.getName();
			}
			return id;
		} else {
			int index = -1;
			try {
				index = Integer.valueOf(id);
			} catch (NumberFormatException e) {
			}
			return index == -1 ? id : "";
		}
	}
	
	public String getNameById(String type, boolean id) {
		return getNameById(type, id+"");
	}
	
	public String getNameById(String type, int id) {
		return getNameById(type, id+"");
	}
	
	public boolean isEnum(String type) {
		if (type == null) {
			return false;
		}
		DictItem dictType = dictCache.get(type);
		if (dictType != null) {
			return dictType.getType().equals(DictItem.ENUM_TYPE);
		}
		return false;
	}
	
	/**
	 * 清空字典
	 */
	public void clearDict() {
		dictCache.clear();
	}
	
	/**
	 * 根据字典类型添加一个DictItem
	 */
	public void addItemByType(String dictType, String itemName) {
		DictItem item = dictCache.get(dictType);
		createItem(item, itemName);
	}
	
	/**
	 * 在指定字典中添加一个item
	 * @param item
	 * @param itemName
	 */
	public void createItem(DictItem item, String itemName) {
		int p = itemName.indexOf(':');
		DictItem dictItem = null;
		if (p > 0) {
			String id = itemName.substring(0, p);
			String name = itemName.substring(p + 1);
			dictItem = new DictItem(id, name);
		} else {
			dictItem = new DictItem(itemName, itemName);
		}
		if (dictItem != null) {
			item.addItem(dictItem);
			item.setType((p > 0) ? DictItem.DICT_TYPE : DictItem.ENUM_TYPE);
		}
	}
	
	public Map<String, DictItem> getDictCache() {
		return dictCache;
	}
}
