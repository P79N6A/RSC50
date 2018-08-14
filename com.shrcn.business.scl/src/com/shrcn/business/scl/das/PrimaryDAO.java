/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNodeEntry;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.ImageConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 该类封装了对一次设备的数据操作的通用算法。
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-9-21
 */
/**
 * $Log: PrimaryDAO.java,v $
 * Revision 1.15  2013/09/13 05:59:48  cchun
 * Update:还原CVS版本标签
 *
 * Revision 1.13  2013/08/15 09:23:55  cxc
 * update:修改
 *
 * Revision 1.12  2012/11/08 12:28:01  cchun
 * Update:将间隔操作方法移动到本类中
 *
 * Revision 1.11  2012/03/22 03:02:50  cchun
 * Refactor:统一LNodeEntry初始化方法
 *
 * Revision 1.10  2012/03/09 07:35:51  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.9  2011/06/09 08:38:17  cchun
 * Update:去掉queryLNode()
 *
 * Revision 1.8  2011/01/07 02:20:20  cchun
 * Update:添加addLNode()和updateLNode()
 *
 * Revision 1.7  2010/10/25 01:27:08  cchun
 * Fix Bug:修复错误的静态变量用法
 *
 * Revision 1.6  2010/10/12 01:42:19  cchun
 * Update:去掉不必要的封装
 *
 * Revision 1.5  2010/09/14 08:08:26  cchun
 * Update:添加对LNode操作的方法
 *
 * Revision 1.4  2009/10/14 10:38:27  wyh
 * 处理hasSameName方法中xpath参数为空的情况
 *
 * Revision 1.3  2009/10/12 02:15:42  cchun
 * Update:去掉注释
 *
 * Revision 1.2  2009/09/22 09:30:32  cchun
 * Update:添加设备名称获取方法
 *
 * Revision 1.1  2009/09/21 01:32:10  cchun
 * Fix Bug:对图形重命名添加重名判断处理
 *
 */
public class PrimaryDAO {
	
	/**
	 * 判断是否存在同名设备
	 * @param xpath
	 * @param newName
	 * @return 存在返回true，否则返回false
	 */
	public static boolean hasSameName(String xpath, String newName) {
		if(null == xpath || xpath.equals("")) return false;
		String newXPath = xpath.substring(0, xpath.lastIndexOf("["))
			+ "[@name='" + newName + "']";
		return XMLDBHelper.existsNode(newXPath);
	}
	
	/**
	 * 获取新增设备名称
	 * @param parentXPath
	 * @param defaultName
	 * @return
	 */
	public static String getEqpName(String parentXPath, String defaultName) {
		List<String> siblings = XMLDBHelper.getAttributeValues(parentXPath + "/*/@name");
		int maxID = 0;
		for(String name : siblings) {
			if(name.indexOf(defaultName)==0) {
				String strID = name.substring(defaultName.length());
				int currID = -1;
				try {
					currID = Integer.parseInt(strID);
				} catch(Exception e) {}
				if(currID > maxID)
					maxID = currID;
			}
		}
		return defaultName + (maxID + 1); 
	}
	
	/**
	 * 向指定设备下添加逻辑节点。
	 * @param parent
	 * @param lnInfo
	 */
	public static void addLNode(INaviTreeEntry parent, String[] lnInfo) {
		String iedName = lnInfo[0];
		String ldInst = lnInfo[1];
		String prefix = lnInfo[2];
		String lnClass = lnInfo[3];
		String lnInst = lnInfo[4];
		String lntype = lnInfo[5];
		
		String name = iedName + "/" + ldInst + "." + prefix + lnClass + lnInst + ":" + lntype; //$NON-NLS-1$ //$NON-NLS-2$
		String parentXpath = parent.getXPath();
		String twPath = parentXpath + "/scl:LNode[@iedName='" + iedName + "'][@ldInst='" + ldInst +
				"']" + SCL.getLNAtts(prefix, lnClass, lnInst);
		LNodeEntry lnEntry = new LNodeEntry(name, twPath, ImageConstants.LNODE, DefaultInfo.SUBS_LNODE);
		lnEntry.setValues(iedName, ldInst, prefix, lnClass, lnInst, lntype);
		int idx = getLastLNodeIndex(parent);
		parent.getChildren().add(idx, lnEntry);
		lnEntry.setParent(parent);
		
		Element lnEle = DOM4JNodeHelper.createSCLNode("LNode");
		lnEle.addAttribute("iedName", iedName); //$NON-NLS-1$
		lnEle.addAttribute("ldInst", ldInst); //$NON-NLS-1$
		lnEle.addAttribute("lnClass", lnClass); //$NON-NLS-1$
		lnEle.addAttribute("lnType", lntype); //$NON-NLS-1$
		lnEle.addAttribute("lnInst", lnInst); //$NON-NLS-1$
		lnEle.addAttribute("prefix", prefix); //$NON-NLS-1$
		
		if (!XMLDBHelper.existsNode(parentXpath + "/scl:LNode")) {
			XMLDBHelper.insertAsFirst(parentXpath, lnEle);
		} else {
			XMLDBHelper.insertAfter(parentXpath + "/scl:LNode[last()]", lnEle);
		}
		
		LNodeEntry romoveEntry = null;
		for (ITreeEntry entry : parent.getChildren()) {
			if (entry instanceof LNodeEntry) {
				LNodeEntry tempEntry = (LNodeEntry) entry;
				if (tempEntry.getLnClass().equals(lnClass) && 
						tempEntry.getIedName().equals(DefaultInfo.IED_NAME)) {
					romoveEntry = tempEntry;
					break;
				}
			}
		}
		if (romoveEntry != null) {
			parent.removeChild(romoveEntry);
			XMLDBHelper.removeNodes(romoveEntry.getXpath());
		}
	}
	
	/**
	 * 得到最后一个LNode节点位置
	 * @param parent
	 * @return
	 */
	private static int getLastLNodeIndex(INaviTreeEntry parent) {
		int index = 0;
		for (ITreeEntry child : parent.getChildren()) {
			if (child instanceof LNodeEntry) {
				index++;
			}
		}
		return index;
	}
	
	/**
	 * 更新设备LNode关联信息
	 * @param entry
	 * @param lnInfo
	 */
	public static void updateLNode(LNodeEntry entry, String[] lnInfo) {
		String iedName = lnInfo[0];
		String ldInst = lnInfo[1];
		String prefix = lnInfo[2];
		String lnClass = lnInfo[3];
		String lnInst = lnInfo[4];
		String lntype = lnInfo[5];
		String parentPath = ((INaviTreeEntry)entry.getParent()).getXPath();
		String lnodePath = "/scl:LNode[@iedName='" + iedName //$NON-NLS-1$
				+ "'][@ldInst='" + ldInst + "']" + SCL.getLNAtts(prefix, lnClass, lnInst);
		lnodePath = parentPath + lnodePath;
	
		// 判断该设备下是否已经存在该逻辑节点的关联
		boolean isExist = XMLDBHelper.existsNode(lnodePath);
		if (isExist) {
			DialogHelper.showError(Messages.getString("LNTabPage.related.dev.exist")); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		
		String xPath = entry.getXpath();
		Element lnodeEle = XMLDBHelper.selectSingleNode(xPath);
		lnodeEle.addAttribute("iedName", iedName);
		lnodeEle.addAttribute("ldInst", ldInst);
		lnodeEle.addAttribute("lnClass", lnClass);
		lnodeEle.addAttribute("lnInst", lnInst);
		lnodeEle.addAttribute("prefix", prefix);
		lnodeEle.addAttribute("lnType", lntype);
		XMLDBHelper.replaceNode(xPath, lnodeEle);
		
		entry.setName(iedName + "/" + ldInst + "." + prefix //$NON-NLS-1$ //$NON-NLS-2$
				+ lnClass + lnInst + ":" + lntype); //$NON-NLS-1$
		entry.setPrefix(prefix);
		entry.setLnClass(lnClass);
		entry.setLnInst(lnInst);
		entry.setXpath(lnodePath);// 更新后设置节点新的xpath
	}

	/**
	 * 查询所有间隔信息(包括母线)
	 * @return
	 */
	public static List<Element> getBayInfos() {
		if (Constants.DEFAULT_PRJECT_NAME == null || Constants.DEFAULT_PRJECT_NAME == "")
			return new ArrayList<Element>();
		if (Constants.XQUERY) {
			String xquery = "for $bay in " + XMLDBHelper.getDocXPath() +
					"/SCL/Substation/VoltageLevel/Bay " +
//			"where count($bay/Private[@type='Busbar'])=0 " +
					"return <Bay oid='{$bay/Private[@type='oid']/text()}' name='{$bay/@name}'></Bay>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			String bayXPath = SCL.XPATH_ROOT + "/scl:Substation/scl:VoltageLevel/scl:Bay";
			List<Element> bayEls = XMLDBHelper.selectNodes(bayXPath);
			List<Element> bays = new ArrayList<Element>();
			for (Element bayEl : bayEls) {
				Element bay = DOM4JNodeHelper.createSCLNode("Bay");
				String oid = DOM4JNodeHelper.getNodeValueByXPath(bayEl, "./scl:Private[@type='oid']");
				bay.addAttribute("oid", oid);
				bay.addAttribute("name", bayEl.attributeValue("name"));
				bays.add(bay);
			}
			return bays;
		}
	}

	/**
	 * 根据间隔名得到oid
	 * @param bayName
	 * @param bays
	 * @return
	 */
	public static String getBayOid(String bayName, List<Element> bays) {
		if (bays==null || bays.size()==0)
			return "";
		for (Element bay : bays) {
			if (bay.attributeValue("name").equals(bayName))
				return bay.attributeValue("oid");
		}
		return bayName;
	}

	/**
	 * 根据间隔oid得到名称
	 * @param bayOid
	 * @param bays
	 * @return
	 */
	public static String getBayName(String bayOid, List<Element> bays) {
		if (bays==null || bays.size()==0)
			return "";
		for (Element bay : bays) {
			if (bay.attributeValue("oid").equals(bayOid))
				return bay.attributeValue("name");
		}
		return bayOid;
	}

	/**
	 * 得到间隔名称
	 * @param bays
	 * @return
	 */
	public static String[] getBayNames(List<Element> bays) {
		if (bays==null || bays.size()==0)
			return new String[0];
		String[] bayNames = new String[bays.size()];
		for (int i=0; i<bays.size(); i++) {
			Element bay = bays.get(i);
			bayNames[i] = bay.attributeValue("name");
		}
		return bayNames;
	}
}
