/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.SCLConstants;
import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.common.LnInstMap;
import com.shrcn.business.scl.model.navgtree.INaviTreeEntry;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.business.scl.model.navgtree.TreeEntryImpl;
import com.shrcn.found.common.event.EventManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.model.ITreeEntry;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-9-27
 */
/**
 * $Log: LNTypeInSubstationCheck.java,v $
 * Revision 1.1  2013/03/29 09:36:36  cchun
 * Add:创建
 *
 * Revision 1.3  2012/03/09 07:35:55  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.2  2012/01/17 08:50:27  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.1  2011/09/15 08:39:30  cchun
 * Refactor:将模型检查移动到common插件
 *
 * Revision 1.4  2011/08/01 08:25:55  cchun
 * Refactor:修复方法为private，直接调用静态变量
 *
 * Revision 1.3  2011/06/09 08:45:56  cchun
 * Update:将检查和校正提示信息分开，增加对LN不存在的校正
 *
 * Revision 1.2  2010/11/04 09:32:19  cchun
 * Update:修改警告信息
 *
 * Revision 1.1  2010/10/12 01:56:12  cchun
 * Add:添加lnType检查类
 *
 */
/**
 * 当替换ICD时,需要检查与一次设备里面每个设备下LNode节点相关联的LN的lnType是否改变以及该LN是否存在
 */
public class LNTypeInSubstationCheck {
	
	// 待替换的lnType值和对应的xpath
	private static Map<String, Set<String[]>> hshLNTypeXPath = new HashMap<String, Set<String[]>>();
	// 待解除关联的LNode（数组中三个元素依次为原xpath、新xpath、lnClass）
	private static List<String[]> lstReleaseLNodes = new ArrayList<String[]>();
	// 数据更新提示信息
	private static List<String> updateInfo = new ArrayList<String>();
	private static LNTypeInSubstationCheck lnTypeCheck = new LNTypeInSubstationCheck();

	private LNTypeInSubstationCheck() {
	}

	public static LNTypeInSubstationCheck newInstance() {
		if (lnTypeCheck == null) {
			synchronized (LNTypeInSubstationCheck.class) {
				if (lnTypeCheck == null) {
					lnTypeCheck = new LNTypeInSubstationCheck();
				}
			}
		}
		return lnTypeCheck;
	}

	/**
	 * 开始对比替换文件(是root部分)与原文件(是lstIedName部分)进行检查
	 * @param root
	 * @param lstIedName
	 * @return
	 */
	public List<String> check(Element root, List<String> lstIedName) {
		final List<String> lstStatus = new ArrayList<String>();
		if (lstIedName == null || lstIedName.size() == 0)
			return lstStatus;
		hshLNTypeXPath.clear();
		lstReleaseLNodes.clear();
		updateInfo.clear();
		Element subStationElement = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		for (String iedName : lstIedName) {
			List<Element> lstLNode = DOM4JNodeHelper.selectNodes(subStationElement, 
					".//*[name()='LNode'][@iedName='" + iedName + "']"); //PrimaryDAO.queryLNode(iedName);
			if (lstLNode == null)
				continue;
			for (Element lnode : lstLNode) {
				List<String> lstRe = match(lnode, root, subStationElement);
				if (lstRe != null && lstRe.size() > 0) {
					lstStatus.addAll(lstRe);
				}
			}
		}
		return lstStatus;
	}

	/**
	 * 开始对替换文件和原文件的LNode与LN进行比较,不存在LN则返回错误信息,否则去比较lntype是否相同,不相同则出现提示信息
	 * @param iedName 原装置IED名
	 * @param lnode   原装置对应的LNode
	 * @param root    新装置<IED>节点
	 * @param subStationElement SCD中<Substation>节点
	 * @return
	 */
	private List<String> match(Element lnode, Element root, Element subStationElement) {
		String status = null;
		List<String> lstStatus = new ArrayList<String>();
		String iedName = lnode.attributeValue("iedName");
		String ldInst = lnode.attributeValue("ldInst");
		String lnClass = lnode.attributeValue("lnClass");
		String lnInst = lnode.attributeValue("lnInst");
		String prefix = lnode.attributeValue("prefix");
		String oldLNType = lnode.attributeValue("lnType");
		LNInfo lnInfo = new LNInfo(prefix, lnClass, lnInst);
		// 根据lnInst和LN信息在新ICD中查找与之对应的LN
		Element lnElement = findLN(root, ldInst, lnInfo);
		// 未找到，则证明该LNode关联的LN已不存在
		if (lnElement == null) {
			status = "\n警告：在icd文件中与一次设备关联的装置 " + iedName + " 下名为 " + ldInst + "/"
					+ lnInfo.toString() +("LLN0".equals(lnClass)?" 的LN0":" 的LN") + "不存在";
			lstStatus.add(status);
			status = "\n警告：与装置 " + iedName + " 下名为 " + ldInst + "/"
				+ lnInfo.toString() +("LLN0".equals(lnClass)?" 的LN0":" 的LN") + "的关联关系已自动解除";
			updateInfo.add(status);
			String parentXpath = getParentXpath(lnode);
			String oldLnodeXpath = parentXpath + "scl:" + lnode.getName() + "[@iedName='" //$NON-NLS-1$
					+ iedName + "'][@ldInst='" + ldInst //$NON-NLS-1$
					+ "']" + SCL.getLNAtts(prefix, lnClass, lnInst);
			final int lnst = LnInstMap.getInstance().nextLnInst(lnClass);
			String newLnodeXpath = parentXpath + "scl:" + lnode.getName() + "[@iedName='" //$NON-NLS-1$
					+ DefaultInfo.IED_NAME + "'][@ldInst=''][@prefix=''][@lnClass='" + lnClass //$NON-NLS-1$ //$NON-NLS-2$
					+ "'][@lnInst='" + String.valueOf(lnst) //$NON-NLS-1$
					+ "']";
			lstReleaseLNodes.add(new String[]{oldLnodeXpath, newLnodeXpath, lnClass, String.valueOf(lnst)});
			return lstStatus;
		}
		String newLNType = lnElement.attributeValue("lnType");
		// 如果对应的LN存在，但lnType属性已经改变了，则提示自动纠正
		if (!newLNType.equals(oldLNType)) {
			Set<String[]> xpathSet = hshLNTypeXPath.get(newLNType);
			if (xpathSet==null) {
				xpathSet = new HashSet<String[]>();
				hshLNTypeXPath.put(newLNType, xpathSet);
			}
			xpathSet.add(new String[]{oldLNType, iedName});
			
			String lnTypePath = iedName + "/" + ldInst + "."
					+ lnInfo.toString() + ":" + oldLNType;
			// 构造如此复杂一棵树结构只为了获取path信息，实为不可取
			List<INaviTreeEntry> lstTree = queryLNodePath(subStationElement, null,
					".//*[name()='LNode'][@lnType='" + oldLNType + "' and @iedName='" + iedName + "']");
			for (INaviTreeEntry entry : lstTree) {
				String eqpPath = getLNodePath(entry);
				// check info
				status = "\n警告：一次设备\"" + eqpPath + "\"关联LNode["
					+ lnTypePath + "]的lnType将更新为[" + newLNType + "]";
				lstStatus.add(status);
				// update info
				status = "\n警告：一次设备\"" + eqpPath + "\"关联LNode["
					+ lnTypePath + "]的lnType已自动校正为[" + newLNType + "]";
				updateInfo.add(status);
			}
		}
		return lstStatus;
	}

	/**
	 * 获取树节点名称path
	 * @param tree
	 * @return
	 */
	private String getLNodePath(INaviTreeEntry tree) {
		String lnodePath = tree.getName();
		ITreeEntry parent = tree.getParent();
		while(parent != null) {
			lnodePath = parent.getName() + "/" + lnodePath;
			parent = parent.getParent();
		}
		return lnodePath;
	}
	
	/**
	 * 获取LNode父节点xpath
	 * @param lnode
	 * @return
	 */
	private String getParentXpath(Element lnode) {
		Element parent = lnode.getParent();
		String parentXpath = "";
		while(parent != null) {
			parentXpath = "scl:" + parent.getName() + "[@name='" + 
				parent.attributeValue("name") + "']" + "/" + parentXpath; 
			parent = parent.getParent();
		}
		return "/scl:SCL/" + parentXpath;
	}

	/**
	 * 查询LNode的树
	 * @param element
	 * @param tree
	 * @param select
	 * @return
	 */
	private List<INaviTreeEntry> queryLNodePath(Element element,
			TreeEntryImpl tree, String select) {
		select += "/..";
		List<?> lst = element.selectNodes(select);
		List<INaviTreeEntry> lstTree = new ArrayList<INaviTreeEntry>();
		if (lst == null)
			return lstTree;
		for (Object obj : lst) {
			Element ele = (Element) obj;
			TreeEntryImpl parent = new TreeEntryImpl(ele.attributeValue("name"));
			lstTree.add(parent);
			if (tree != null) {
				tree.setParent(parent);
			}
			if (!"Substation".equals(ele.getName())) {
				queryLNodePath(element, parent, select);
			}
		}
		return lstTree;
	}

	/**
	 * 从替换文件中获取在ied下满足特定条件的LN以及LN0
	 * 
	 * @param scl
	 * @param ldInst
	 * @param iedName
	 * @param ln
	 * @return
	 */
	private Element findLN(Element scl, String ldInst, LNInfo ln) {
		StringBuffer sbXPath = new StringBuffer(
				"./*[name()='IED']/*[name()='AccessPoint']/*[name()='Server']");
		sbXPath.append("/*[name()='LDevice'][@inst='" + ldInst + "']");
		sbXPath.append(SCL.getLNXPath(ln.getPrefix(), ln.getLnClass(), ln.getInst()));
		return DOM4JNodeHelper.selectSingleNode(scl, sbXPath.toString());
	}

	/**
	 * 更新LNode的lnType属性值
	 */
	public List<String> updateLNType() {
		java.util.Collections.sort(updateInfo);
		if (hshLNTypeXPath.size() == 0)
			return updateInfo;
		Iterator<String> iter = hshLNTypeXPath.keySet().iterator();
		while (iter.hasNext()) {
			String lntype = iter.next();
			Set<String[]> lnTypeSet = hshLNTypeXPath.get(lntype);
			for (String[] lnType : lnTypeSet) {
				String xpath = SCL.XPATH_SUBSTATION + "//scl:LNode[@lnType='"
					+ lnType[0] + "' and @iedName='" + lnType[1] + "']";
				XMLDBHelper.saveAttribute(xpath, "lnType", lntype);
			}
		}
		for (String[] lnode : lstReleaseLNodes) {
			// 触发树更新节点名称
			Object[] value = new String[] {lnode[0], lnode[1],  DefaultInfo.IED_NAME, "", "", lnode[2],
					lnode[3], DefaultInfo.LNTYPE};
			EventManager.getDefault().notify(
					SCLConstants.EQUIP_GRAPH_RELEATE_LNODE, value);
		}
		return updateInfo;
	}
}
