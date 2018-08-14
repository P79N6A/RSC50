/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.BayEntry;
import com.shrcn.business.scl.model.navgtree.ConductingEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.ConnectivityNodeEntry;
import com.shrcn.business.scl.model.navgtree.FunctionEntry;
import com.shrcn.business.scl.model.navgtree.GeneralEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNodeEntry;
import com.shrcn.business.scl.model.navgtree.PowerTransformerEntry;
import com.shrcn.business.scl.model.navgtree.SubEquipmentEntry;
import com.shrcn.business.scl.model.navgtree.SubFunctionEntry;
import com.shrcn.business.scl.model.navgtree.SubstationEntry;
import com.shrcn.business.scl.model.navgtree.TerminalEntry;
import com.shrcn.business.scl.model.navgtree.VoltageLevelEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-12
 */
/*
 * 修改历史
 * $Log: PrimaryUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:29  cchun
 * Add:创建
 *
 * Revision 1.23  2012/01/17 08:50:31  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.22  2012/01/13 08:39:56  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.21  2011/09/08 09:13:09  cchun
 * Fix Bug:修复PrimaryUtil.updateTerminal()在间隔与设备同名情况下可能导致bayName错误的bug
 *
 * Revision 1.20  2011/08/11 05:41:33  cchun
 * Fix Bug:修复replaceConnectivityNode()数组越界导致设备重命名失败的bug
 *
 * Revision 1.19  2011/05/06 09:36:41  cchun
 * Update:简化markParentUpdate()逻辑
 *
 * Revision 1.18  2010/12/20 02:39:33  cchun
 * Refactor:删除数据库异常处理
 *
 * Revision 1.17  2010/09/08 08:28:03  cchun
 * Refactor:规范异常捕捉
 *
 * Revision 1.16  2010/05/06 02:25:27  cchun
 * Fix Bug:修复一次设备插入顺序错误
 *
 * Revision 1.15  2010/04/23 03:22:26  cchun
 * Update:历史记录接口添加oid参数
 *
 * Revision 1.14  2010/04/12 01:39:33  cchun
 * Update:修改节点插入逻辑，避免<Private/>被放到最后
 *
 * Revision 1.13  2010/03/01 07:58:55  lj6061
 * 修改替换节点方法
 *
 * Revision 1.12  2009/12/11 08:40:14  lj6061
 * Add:1次设备操作添加历史增量标记
 *
 * Revision 1.11  2009/12/09 08:56:16  lj6061
 * 添加：变电站根节点重命名
 *
 * Revision 1.10  2009/12/01 09:12:08  lj6061
 * 修正匹配字符串Bug
 *
 * Revision 1.9  2009/09/28 02:36:40  wyh
 * 添加功能：间隔设备重命名时对设备名仅有"@"的处理
 *
 * Revision 1.8  2009/09/28 00:46:19  wyh
 * 添加功能：间隔设备重命名
 *
 * Revision 1.7  2009/09/18 09:05:06  lj6061
 * 修改：粘贴后对Terminal的分别处理
 *
 * Revision 1.6  2009/09/18 05:18:36  lj6061
 * 修正替换Xpath的Bug
 *
 * Revision 1.5  2009/09/14 01:34:18  lj6061
 * update:导出或粘贴文件删除所有的连接点信息，保留Terminal为grounded节点
 *
 * Revision 1.4  2009/09/10 03:36:53  lj6061
 * 删除无用方法
 *
 * Revision 1.3  2009/09/09 01:36:33  lj6061
 * 添加导入导出典型间隔
 *
 * Revision 1.2  2009/09/03 08:38:20  lj6061
 * 添加清理拓扑
 *
 * Revision 1.1  2009/08/28 01:33:24  cchun
 * Refactor:重构包路径
 *
 * Revision 1.7  2009/08/27 02:26:20  cchun
 * Refactor:重构导航树模型包路径
 *
 * Revision 1.6  2009/08/18 09:40:31  cchun
 * Update:合并代码
 *
 * Revision 1.4.2.2  2009/08/17 07:38:51  lj6061
 * 修改关联复制到BUG
 *
 * Revision 1.4.2.1  2009/07/24 07:11:17  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.4  2009/07/17 01:32:13  lj6061
 * 修改更新拓扑的方法
 *
 * Revision 1.3  2009/06/17 12:19:19  lj6061
 * 添加线程，修正Bug
 *
 * Revision 1.1.2.3  2009/06/17 12:08:31  lj6061
 * 添加线程，修正Bug
 *
 * Revision 1.1.2.2  2009/06/16 08:42:49  lj6061
 * 对空异常处理
 *
 * Revision 1.2  2009/06/16 08:33:02  lj6061
 * 对空异常处理
 *
 * Revision 1.1  2009/06/15 09:39:58  lj6061
 * 更改代码中的对电压处理
 *
 */
public class PrimaryUtil {
	
	/**
	 * 更新被修改节点相关属性
	 * @param target
	 * @param subName
	 * @param volName
	 * @param bayName
	 * @param isClear 是否需要清理cNodeName属性，在粘帖间隔时需要清理，重命名不需要
	 */
	public static void updateTerminal(Element target, String subName, String volName, String bayName, boolean isClear) {
		if(target == null)
			return;
		String xpath = ".//*[name()='Terminal']";
		List<Element> termEleList = DOM4JNodeHelper.selectNodes(target, xpath);
		for (Element element : termEleList) {
			if (subName != null)
				element.addAttribute("substationName", subName);
			if (volName != null)
				element.addAttribute("voltageLevelName", volName);
			if (bayName != null)
				element.addAttribute("bayName", bayName);
			String cNodeName = element.attributeValue("cNodeName");
			if (isClear) {
				if (!SCL.GROUNDED.equals(cNodeName))
					element.addAttribute("cNodeName", "null");
				element.addAttribute("connectivityNode", "null");
			} else {
				element.addAttribute("connectivityNode", 
						element.attributeValue("substationName") + "/" +
						element.attributeValue("voltageLevelName") + "/" +
						element.attributeValue("bayName") + "/" +
						cNodeName
						);
			}
		}
	}
	
	/**
	 * 更新连接点值
	 * @param copyEle
	 * @param subName
	 * @param volName
	 * @param bayName
	 */
	public static void updateConNode(Element copyEle, String subName, String volName, String bayName) {
		if(copyEle == null)
			return;
		String xpath = ".//*[name()='ConnectivityNode']";
		List<Element> termEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element element : termEleList) {
			String pathName = element.attributeValue("pathName");
			String[] name = pathName.split("/");
			if (name.length < 4)
				continue;
			if (subName != null)
				name[0] = subName;
			if (volName != null)
				name[1] = volName;
			if (bayName != null)
				name[2] = bayName;
			String newPathName = name[0] + "/" + name[1] + "/" + name[2] + "/"+ name[3];
			element.addAttribute("pathName", newPathName);
		}
	}
			
	/**
	 * 由于父节点名称改变，Xpath改变 重命名
	 * @param entry 
	 * @param newPath
	 * @param oldPath 新的节点路径
	 */
	public static void renameXpath(INaviTreeEntry entry,String newPath,String oldPath){
		List<ITreeEntry> children = entry.getChildren();
		for (ITreeEntry child : children) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) child;
			String xpath = treeEntry.getXPath();
			if(xpath.contains(oldPath)){
				String newXpath = xpath.replace(oldPath, newPath);
				treeEntry.setXpath(newXpath);
				treeEntry.setParent(entry);
				renameXpath(treeEntry,newPath,oldPath);
			}				
		}
	}
	
	/**
	 * 对于导入典型间隔需要设置所有的路径
	 * 例如 解析的间隔Xpath = /scl:bay,添加上部分电压等级的路径 
	 * @param entry 
	 * @param newPath
	 * @param oldPath 新的节点路径
	 */
	public static void resetXpath(INaviTreeEntry entry, String newPath) {
		List<ITreeEntry> children = entry.getChildren();
		for (ITreeEntry child : children) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) child;
			
			String xpath = treeEntry.getXPath();
			xpath = newPath + xpath;
			treeEntry.setXpath(xpath);
			resetXpath(treeEntry, newPath);
		}
	}
	
	/**
	 * 由于树上顺序不是按照文件顺序排列！需要对其排序
	 * 
	 * @throws XmlException
	 */
	public List<INaviTreeEntry> getSortList(List<INaviTreeEntry> treeList){
		for (int i = 0; i < treeList.size(); i++) {
			for (int j = i + 1; j < treeList.size(); j++) {
				if (treeList.get(i).getPriority() > treeList.get(j).getPriority()) {
					Collections.swap(treeList, i, j);
				}
			}	
		}
		return treeList;
	}
	
	/**
	 * 插入节点按照节点类型 取得末尾节点
	 * @param goin
	 */
	public static void insertInDB(Element insertEle, INaviTreeEntry treeEntry, INaviTreeEntry parentEntry) {
		if (treeEntry == null || insertEle == null)
			return;
		String select = parentEntry.getXPath();
		String sortPath = getSelect(treeEntry);
		if (sortPath == null)
			return;
		List<Element> targetNode = getTargetNode(select, sortPath);
		if (targetNode.isEmpty()) {
			XMLDBHelper.insertAsFirst(select, insertEle);
		} else {
			String nodeName = targetNode.get(0).getName();
			String targetPath = select + "/scl:" + nodeName + "[last()]";
			XMLDBHelper.insertAfter(targetPath, insertEle);
		}
	}

	public static List<Element> getTargetNode(String select, String sortPath) {
		List<Element> targetNode = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String targetPath = "let $ln:=" + XMLDBHelper.getDocXPath() + select
					+ ", $children:=(" + sortPath + ") "
					+ "return $children[last()]";
			targetNode = XMLDBHelper.queryNodes(targetPath);
		} else {
			String[] strs = sortPath.split(",");
			for(String sort : strs){
				List<Element> selectNodes = XMLDBHelper.selectNodes(select + "/*[name()='"+sort+"']");
				int size = selectNodes.size();
				if (size > 0) {
					targetNode.add(selectNodes.get(size - 1));
					break;
				}
			}
		}
		return targetNode;
	}

	/**
	 * 按照插入节点获得优先级顺序 
	 * @param goin
	 */
	public static String getSelect(INaviTreeEntry treeEntry) {
		String sortPath = null;
		if(Constants.XQUERY){
			if(treeEntry instanceof PowerTransformerEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:PowerTransformer";
			else if(treeEntry instanceof ConductingEquipmentEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment,$ln/scl:ConductingEquipment";
			else if(treeEntry instanceof GeneralEquipmentEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment";
			else if(treeEntry instanceof BayEntry)
				sortPath = "$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment,$ln/scl:Voltage,$ln/scl:Bay";
			else if(treeEntry instanceof VoltageLevelEntry)
				sortPath = "$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment,$ln/scl:VoltageLevel";
			else if(treeEntry instanceof FunctionEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment,$ln/scl:ConductingEquipment,$ln/scl:ConnectivityNode,$ln/scl:VoltageLevel,$ln/scl:Function";
			else if(treeEntry instanceof LNodeEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode";
			else if(treeEntry instanceof SubEquipmentEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:Terminal,$ln/scl:SubEquipment";
			else if(treeEntry instanceof SubFunctionEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:SubFunction";
			else if(treeEntry instanceof ConnectivityNodeEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:PowerTransformer,$ln/scl:GeneralEquipment,$ln/scl:ConductingEquipment,$ln/scl:ConnectivityNode";
			else if(treeEntry instanceof TerminalEntry)
				sortPath = "$ln/scl:Private,$ln/scl:LNode,$ln/scl:Terminal";
		} else {
			if(treeEntry instanceof PowerTransformerEntry)
				sortPath = "PowerTransformer,LNode,Private";
			else if(treeEntry instanceof ConductingEquipmentEntry)
				sortPath = "ConductingEquipment,GeneralEquipment,PowerTransformer,LNode,Private";
			else if(treeEntry instanceof GeneralEquipmentEntry)
				sortPath = "GeneralEquipment,PowerTransformer,LNode,Private";
			else if(treeEntry instanceof BayEntry)
//				sortPath = "Bay,Voltage,GeneralEquipment,PowerTransformer,LNode";
				sortPath = "Bay,Voltage,ConductingEquipment,GeneralEquipment,PowerTransformer,LNode,Private";
			else if(treeEntry instanceof VoltageLevelEntry)
				sortPath = "VoltageLevel,GeneralEquipment,PowerTransformer,LNode";
			else if(treeEntry instanceof FunctionEntry)
				sortPath = "Function,VoltageLevel,ConnectivityNode,ConductingEquipment,GeneralEquipment,PowerTransformer,LNode,Private";
			else if(treeEntry instanceof LNodeEntry)
				sortPath = "LNode,Private";
			else if(treeEntry instanceof SubEquipmentEntry)
				sortPath = "SubEquipment,Terminal,LNode,Private";
			else if(treeEntry instanceof SubFunctionEntry)
				sortPath = "SubFunction,LNode,Private";
			else if(treeEntry instanceof ConnectivityNodeEntry)
				sortPath = "ConnectivityNode,ConductingEquipment,GeneralEquipment,PowerTransformer,LNode,Private";
			else if(treeEntry instanceof TerminalEntry)
				sortPath = "Terminal,LNode,Private";
		}
		return sortPath;
	}
	
	/**
	 * 删除连接点信息
	 * @param entry
	 * @param newName 新的节点名称
	 */
	public static void removeConNode(Element copyEle){
		if(copyEle == null)
			return;
		String xpath = ".//*[name()='ConnectivityNode']";
		List<Element> connEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element connEle : connEleList) {
			connEle.getParent().remove(connEle);
		}
	}
	
	/**
	 * 修改由于父节点名称改变，Terminal部分节点引用改变
	 * 
	 * @param entry
	 * @param newName 新的节点名称
	 */
	public static void clearTerminal(Element copyEle){
		if(copyEle == null )return;
		String xpath = ".//*[name()='Terminal']";
		List<Element> TerEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element element : TerEleList) {
			element.addAttribute("connectivityNode", "null");
			element.addAttribute("voltageLevelName", "null");
			element.addAttribute("bayName", "null");
			element.addAttribute("substationName", "null");
			if (!element.attributeValue("cNodeName").equals("grounded")) // 是否清楚拓扑结构
				element.addAttribute("cNodeName", "null");
		}
	}
	
	/**
	 * 修改由于父节点名称改变，含有connectivityNode部分节点引用改变
	 * 
	 * @param entry
	 * @param newName 新的节点名称
	 */
	public static void resetConNode(Element copyEle,String pathName){
		if(copyEle == null )return;
		String xpath = ".//*[name()='ConnectivityNode']";
		List<Element> TerEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element element : TerEleList) {
			String name = element.attributeValue("name");
			element.addAttribute("pathName", pathName + name);
		}
	}
	
	/**
	 * 修改由于父节点名称改变，Terminal部分节点引用改变
	 * 
	 * @param entry
	 * @param newName 新的节点名称
	 */
	public static void resetTerminal(Element copyEle,String substation,String vol,String bayName){
		String xpath = ".//*[name()='Terminal']";
		List<Element> TerEleList = DOM4JNodeHelper.selectNodes(copyEle, xpath);
		for (Element element : TerEleList) {
			element.addAttribute("voltageLevelName", vol);
			element.addAttribute("bayName", bayName);
			element.addAttribute("substationName", substation);
			element.addAttribute("connectivityNode", substation + "/"+ vol + "/" + bayName + "/null");
		}
	}

	/**
	 * 如果设备的名称为以"@"开始，则替换它
	 * 只对间隔起作用
	 * @param element
	 * @param newName
	 */
	public static void renameATcharacter(Element oldBayelement, String newBayXpath, String newBayName){
		// 处理变压器
		List<?> powers = oldBayelement.elements("PowerTransformer");
		for(Object obj : powers){
//			String temp = "/scl:PowerTransformer";
			Attribute att = ((Element) obj).attribute("name");
			String poweroldName = att.getValue();
//			temp += "[@name='" + poweroldName + "']";
			if (poweroldName.contains("@")) {
				String powernewName = poweroldName.replace("@", newBayName);
				att.setValue(powernewName);
			}
		}
		// 处理普通设备
		List<?> conducts = oldBayelement.elements("ConductingEquipment");
		for (Object obj : conducts) {
//			String temp = "/scl:ConductingEquipment";
			Attribute att = ((Element) obj).attribute("name");
			String conductoldName = att.getValue();
//			temp += "[@name='" + conductoldName + "']";
			if (conductoldName.contains("@")) {
				String conductnewName = conductoldName.replace("@", newBayName);
				att.setValue(conductnewName);
			}
		}
	}

	/**
	 * 间隔设备重命名的处理
	 * @param entry
	 * @param newPath
	 * @param oldPath
	 */
	public static void renameBayXpath(INaviTreeEntry newBayentry,String newBayPath,String oldBayPath){
		String entryName = newBayentry.getName();
		List<ITreeEntry> oldchildren = newBayentry.getChildren();
		for (ITreeEntry entry : oldchildren) {
			INaviTreeEntry treeEntry = (INaviTreeEntry) entry;
			String treeEntryName = treeEntry.getName();
			String xpath = treeEntry.getXPath();
			if(treeEntryName.contains("@")){
				String tempName = treeEntryName.replace("@", entryName);
				treeEntry.setName(tempName);
				xpath = xpath.replace("'"+treeEntryName, "'"+tempName);
			}
			if(xpath.contains(oldBayPath)){
				String newXpath = xpath.replace(oldBayPath, newBayPath);
				treeEntry.setXpath(newXpath);
				treeEntry.setParent(newBayentry);
				renameXpath(treeEntry,newBayPath,oldBayPath);
			}				
		}
	}
	
	/**
	 * 记录修改历史
	 * @param parent
	 * @param name
	 */
	public static void markUpdateHistory(ITreeEntry parent, String name){
		if (parent instanceof SubstationEntry) {
			markUpdate(name, DevType.STA);
		} else if (parent instanceof VoltageLevelEntry) {
			markUpdate(name, DevType.VOL);
		} else if (parent instanceof BayEntry) {
			markUpdate(name, DevType.BAY);
		} else{
			markParentUpdate(parent, name);
		}
	}
	
	private static void markParentUpdate(ITreeEntry entry, String name) {
		if (entry.getParent() == null)
			return;
		ITreeEntry parent = entry.getParent();
		markUpdateHistory(parent, name);
	}
	
	public static void markRenameHistory(INaviTreeEntry parent,String oldName,String newName){
		if (parent instanceof SubstationEntry) {
			markRename(oldName, newName, DevType.STA);
		} else if (parent instanceof VoltageLevelEntry) {
			markRename(oldName, newName, DevType.VOL);
		} else if (parent instanceof BayEntry) {
			markRename(oldName, newName, DevType.BAY);
		} else{
			markParentUpdate(parent,newName);
		}
	}
	
	public static void markDeleteHistory(INaviTreeEntry parent,String name){
		if (parent instanceof SubstationEntry) {
			markDelete(name, DevType.STA);
		} else if (parent instanceof VoltageLevelEntry) {
			markDelete(name, DevType.VOL);
		} else if (parent instanceof BayEntry) {
			markDelete(name, DevType.BAY);
		} else{
			markParentUpdate(parent,name);
		}
	}
	
	/**
	 * 添加历史增量标记
	 * 
	 * @param oldName
	 * @param newName
	 */
	private static void markUpdate(String name,DevType type){
		HistoryManager manager = HistoryManager.getInstance();
		manager.markDevChanged(type, OperType.UPDATE, name, null);
	}
	
	/**
	 * 添加历史重命名增量标记
	 * @param oldName
	 * @param newName
	 */
	private static void markRename(String oldName, String newName, DevType type) {
		HistoryManager manager = HistoryManager.getInstance();
		manager.markRename(type, oldName, newName, null);
	}
	
	/**
	 * 添加历史删除增量标记
	 * @param oldName
	 * @param newName
	 */
	private static void markDelete(String name, DevType type) {
		HistoryManager manager = HistoryManager.getInstance();
		manager.markDevChanged(type, OperType.DELETE, name, null);
	}
}
