/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model.navgtree;

import java.util.ArrayList;
import java.util.List;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.model.ITreeEntry;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-2
 */
/**
 * $Log: EntryUtil.java,v $
 * Revision 1.1  2013/03/29 09:35:20  cchun
 * Add:创建
 *
 * Revision 1.11  2012/03/22 03:05:47  cchun
 * Update:添加changeParentXPath()
 *
 * Revision 1.10  2011/12/13 07:20:22  cchun
 * Update:修复设备节点定位bug
 *
 * Revision 1.9  2011/08/24 08:16:08  cchun
 * Update:添加findEntryByName()
 *
 * Revision 1.8  2011/05/06 09:34:45  cchun
 * Fix Bug:修复isInTargetTypes()逻辑错误；
 * Update:增加checkConflict()方法
 *
 * Revision 1.7  2011/01/07 02:20:40  cchun
 * Update:添加getNoneEqpAndFunType()
 *
 * Revision 1.6  2011/01/06 08:53:46  cchun
 * Update:增加从PrimaryTree提取的方法和isNotEqpOrFun()
 *
 * Revision 1.5  2010/11/08 08:32:53  cchun
 * Update: 清除没有用到的代码
 *
 * Revision 1.4  2010/09/14 08:23:17  cchun
 * Update:添加pri2Tag(),xpath2TreeItemNames()
 *
 * Revision 1.3  2010/09/14 08:16:46  cchun
 * Update:添加modifyNameFromXPath()
 *
 * Revision 1.2  2010/05/19 09:39:35  cchun
 * Update:添加updateSubXPath()
 *
 * Revision 1.1  2010/03/02 03:00:38  cchun
 * Add:添加树模型工具类
 *
 */
public class EntryUtil {
	/* 将拖拽分类 */
	public static final int DRAG_LNODE = 0; // 拖拽节点有且仅有一个LNode
	public static final int DRAG_EQUIP = 1; // 拖拽节点全为变压器或导电设备
	public static final int DRAG_TRANS = 2; // 拖拽节点全为变压器
	public static final int DRAG_BAY   = 3; // 拖拽节点全为间隔
	
	/**
	 * 得到拖拽类型
	 */
	public static int getDragType(List<INaviTreeEntry> entries) {
		int size = entries.size();
		if(size < 1)
			return -1;
		ITreeEntry first = entries.get(0);
		if(size == 1 && first instanceof LNodeEntry)
			return DRAG_LNODE;
		if(isInTargetTypes(entries, PowerTransformerEntry.class))
			return DRAG_TRANS;
		if(isInTargetTypes(entries, BayEntry.class))
			return DRAG_BAY;
		if(isInTargetTypes(entries, PowerTransformerEntry.class, ConductingEquipmentEntry.class, FunctionEntry.class))
			return DRAG_EQUIP;
		return -1;
	}
	
	/**
	 * 得到目标节点类型名
	 * @param entry
	 * @return
	 */
	public static String getNoneEqpAndFunType(ITreeEntry entry) {
		Class<?> entryClass = entry.getClass();
		String type = null;
		if (entryClass == SubstationEntry.class)
			type = DefaultInfo.SUB_NAME;
		else if (entryClass == VoltageLevelEntry.class)
			type = DefaultInfo.VOLTAGELEVEL_NAME;
		else if (entryClass == BayEntry.class) {
			if (((BayEntry)entry).isBusbar())
				type = DefaultInfo.BUSBAR_NAME;
			else
				type = DefaultInfo.BAY_NAME;
		}
		return type;
	}
	
	/**
	 * 判断节点集合是否为指定的一种或多种类型
	 * @param entries
	 * @param types
	 * @return
	 */
	private static boolean isInTargetTypes(List<INaviTreeEntry> entries, Class<?>... types) {
		if (types.length == 1) {
			for(INaviTreeEntry entry : entries) {
				Class<?> entryType = entry.getClass();
				if (entryType != types[0])
					return false;
			}
		} else {
			for(INaviTreeEntry entry : entries) {
				Class<?> entryType = entry.getClass();
				boolean isIn = false;
				for (Class<?> type : types) {
					if (entryType == type) {
						isIn = true;
						break;
					}
				}
				if (!isIn)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据xpath查找树节点
	 * @param data
	 * @param xpath
	 * @return
	 */
	public static INaviTreeEntry findEntryByXPath(List<ITreeEntry> data, String xpath) {
		for(ITreeEntry child : data) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) child;
			if(treeEntry.getXPath().equals(xpath)) {
				return treeEntry;
			} else if(child.getChildren().size() > 0) {
				INaviTreeEntry result = findEntryByXPath(child.getChildren(), xpath);
				if(null != result)
					return result;
				else
					continue;
			}
		}
		return null;
	}
	
	/**
	 * 更新父节点xpath。
	 * @param entry
	 * @param newParentXPath
	 */
	public static void changeParentXPath(INaviTreeEntry entry, String newParentXPath) {
		String xpath = entry.getXPath();
		String oldParentXPath = SCL.getParentXPath(xpath);
		xpath = xpath.replace(oldParentXPath, newParentXPath);
		entry.setXpath(xpath);
		List<ITreeEntry> children = entry.getChildren();
		if(children.size() > 0) {
			for(ITreeEntry child : children) {
				INaviTreeEntry treeEntry = (INaviTreeEntry) child;
				
				changeParentXPath(treeEntry, entry.getXPath());
			}
		}
	}
	
	/**
	 * 根据装置名称找到对应的树节点
	 * @param data
	 * @param name
	 * @return
	 */
	public static INaviTreeEntry findEntryByName(List<ITreeEntry> data, String name) {
		for(ITreeEntry child : data) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) child;
			if (StringUtil.getName(child.getName()).equals(name)) {
				return treeEntry;
			} else if(child.getChildren().size() > 0) {
				INaviTreeEntry result = findEntryByName(child.getChildren(), name);
				if(null != result)
					return result;
				else
					continue;
			}
		}
		return null;
	}
	
	/**
	 * 更新子节点xpath
	 * @param target
	 * @param oldXPath
	 * @param newXPath
	 */
	public static void updateSubXPath(INaviTreeEntry target, String oldXPath, String newXPath) {
		List<ITreeEntry> children = target.getChildren();
		if(children==null || children.size() == 0) {
			INaviTreeEntry targetEntry = (INaviTreeEntry) target;
			String subFunXpath = targetEntry.getXPath();
			if(subFunXpath!=null) {
				subFunXpath = subFunXpath.replace(oldXPath,
						newXPath);
				targetEntry.setXpath(subFunXpath);
			}
		} else {
			for (ITreeEntry e : target.getChildren()) {
				updateSubXPath((INaviTreeEntry)e, oldXPath, newXPath);
			}
		}
	}
	
	/**
	 * 根据结点类型在xpath上修改对应节点的名称
	 * @param oldXPath
	 * @param oldName
	 * @param newName
	 * @param curNodeType
	 * @return
	 */
	public static String modifyNameFromXPath(String oldXPath, String oldName,
			String newName, String curNodeType) {
		String name = "[@name='";
		String pre = "";
		if (curNodeType != null) {
			pre = curNodeType + name;
		}
		return oldXPath.replace(pre + oldName, pre + newName);
	}
	
	/**
	 * 优先级别转化为对应的tag名称
	 * @param pri
	 * @return
	 */
	public static String pri2Tag(int pri) {
		switch (pri) {
		case DefaultInfo.SUBS_ROOT:
			return "Substation";
		case DefaultInfo.SUBS_VOLTAGEL:
			return "VoltageLevel";
		case DefaultInfo.SUBS_BAY:
			return "Bay";
		case DefaultInfo.SUBS_LNODE:
			return "";
		}
		return "";
	}
	
	/**
	 * 将xpath分解成按级别先后组成的name数组
	 * 
	 * @param xpath
	 * @return
	 */
	public static String[] xpath2TreeItemNames(String xpath) {
		String name="@name='";
		String[] childXP = xpath.split("/");
		int length = childXP.length;
		int index = 0;
		List<String> lst=new ArrayList<String>();
		for (int i = 1; i < length; i++) {
			index = childXP[i].indexOf(name);
			if(index == -1 || childXP[i].indexOf(SCL.NODE_FUNLIST) > -1)
				continue;
			lst.add(childXP[i].substring(index + name.length(),childXP[i].length() - 2));
		}
		return lst.toArray(new String[lst.size()]);
	}
	
	/**
	 * 检查当前节点是否已存在于集合中
	 * @param source
	 * @param targets
	 * @return
	 */
	public static boolean checkConflict(INaviTreeEntry source, List<INaviTreeEntry> targets) {
		for (ITreeEntry entry : targets) {
			if (source.getName().equals(entry.getName()))
				return true;
		}
		return false;
	}
	
	/**
	 * 检查两个集合是否存在同名节点
	 * @param sources
	 * @param targets
	 * @return
	 */
	public static boolean checkConflict(List<INaviTreeEntry> sources, List<INaviTreeEntry> targets) {
		for (INaviTreeEntry entry : sources) {
			if (checkConflict(entry, targets))
				return true;
		}
		return false;	
	}
	
	public static List<INaviTreeEntry> getNaviChildren(List<ITreeEntry> list){
		List<INaviTreeEntry> childs = new ArrayList<INaviTreeEntry>();
		for(ITreeEntry ent : list){
			childs.add((INaviTreeEntry)ent);
		}
		return childs;
	}
}
