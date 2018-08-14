/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.swt.graphics.Color;

import com.shrcn.business.scl.common.EnumEquipType;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-9-8
 */
/**
 * $Log: RelatedLNodeService.java,v $
 * Revision 1.1  2013/03/29 09:36:20  cchun
 * Add:创建
 *
 * Revision 1.12  2012/01/17 08:50:29  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.11  2012/01/13 08:40:03  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.10  2011/12/19 02:49:50  cchun
 * Fix Bug:修复一次设备关联状态遗漏的bug
 *
 * Revision 1.9  2011/12/06 08:05:55  cchun
 * Update:优化程序逻辑
 *
 * Revision 1.8  2011/05/30 09:50:13  cchun
 * Update:清理引用
 *
 * Revision 1.7  2011/05/27 09:03:01  cchun
 * Fix Bug:LNode没有按ied过滤
 *
 * Revision 1.6  2011/03/25 09:55:49  cchun
 * Refactor:重命名
 *
 * Revision 1.5  2011/03/07 08:16:56  cchun
 * Update:聂国勇修改，修改关联红色为黄色
 *
 * Revision 1.4  2011/01/05 07:37:38  cchun
 * Update:将LN关联改用图标提示
 *
 * Revision 1.3  2011/01/04 09:28:43  cchun
 * Update:去掉不必要的变量
 *
 * Revision 1.2  2010/10/14 03:50:02  cchun
 * Fix Bug:修复缓存未清除的bug
 *
 * Revision 1.1  2010/09/14 08:08:51  cchun
 * Add:LN关联工具类
 *
 */
/**
 *获取Substation tag下所有的LNode
 */
public class RelatedLNodeService {

	// key:iedName$ldInst.prefix+lnClass+lnInst, color:red
	private Map<String, Color> hshRelatedLNode = Collections.synchronizedMap(new HashMap<String, Color>());
	private Map<String, String[]> relatedLNodeInfo = Collections.synchronizedMap(new HashMap<String, String[]>());
	private static RelatedLNodeService service = null;
	private String nullKey = null;

	private RelatedLNodeService() {
	}

	public static RelatedLNodeService newInstance() {
		if (service == null) {
			synchronized (RelatedLNodeService.class) {
				if (service == null) {
					service = new RelatedLNodeService();
				}
			}
		}
		return service;
	}

	/**
	 * 查询多个IED关联的LNode
	 * @param lstIedName
	 */
	public void queryRelatedLNode(List<String> lstIedName) {
		for (String iedName : lstIedName) {
			queryRelatedLNode(iedName);
		}
	}

	/**
	 * 查询指定IED关联的LNode
	 * @param iedName
	 */
	public void queryRelatedLNode(String iedName) {
		clearRelatedCache(iedName, relatedLNodeInfo);
		clearRelatedCache(iedName, hshRelatedLNode);
		List<Element> lst = getLNode(iedName);
		
		for (Element ele : lst) {
			String name = ele.attributeValue("name");
			String type = ele.attributeValue("type", "");
			String eqType = ele.getName();
			if ("PowerTransformer".equals(eqType)) {
				List<Element> windings = DOM4JNodeHelper.selectNodes(ele, "./*[name()='TransformerWinding']");
				if (windings.size() == 3)
					type = EnumEquipType.PTR3;// 区分图片给定类型标识
				else if (windings.size() == 2)
					type = EnumEquipType.PTR2;// 区分图片给定类型标识
			} else if ("Function".equals(eqType)) {
				type = EnumEquipType.FUNCTION;
			}
			if (EnumEquipType.DIS.equals(type) &&
					DOM4JNodeHelper.selectNodes(ele, "./*[name()='Terminal'][@cNodeName='grounded']").size() > 0) {
				type = EnumEquipType.DDIS;
			}
			String[] info = new String[] {type, name};
			List<Element> lnodes = DOM4JNodeHelper.selectNodes(ele, ".//*[name()='LNode'][@iedName='" + iedName + "']");
			if (lnodes == null)
				continue;
			for (Element lnode : lnodes) {
				String ldInst = DOM4JNodeHelper.getAttribute(lnode, "ldInst");
				String pre = DOM4JNodeHelper.getAttribute(lnode, "prefix");
				String lC = DOM4JNodeHelper.getAttribute(lnode, "lnClass");
				String in = DOM4JNodeHelper.getAttribute(lnode, "lnInst");
				StringBuffer key = new StringBuffer(iedName + Constants.DOLLAR + ldInst + Constants.DOT);
				key.append(pre);
				key.append(lC);
				key.append(in);
				relatedLNodeInfo.put(key.toString(), info);
				hshRelatedLNode.put(key.toString(), UIConstants.YELLOW);
			}
		}
	}

	public List<Element> getLNode(String iedName) {
		List<Element> lst = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String xquery = "let $types:=('PowerTransformer', 'GeneralEquipment', 'ConductingEquipment', 'Function')" +
					" return for $eqp in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:Substation//*" +
					" where exists(index-of($types, $eqp/name())) and count($eqp//scl:LNode[@iedName='" + iedName + "'])>0" +
					" return $eqp";
			lst = XMLDBHelper.queryNodes(xquery);
		} else {
			lst = XMLDBHelper.selectNodes("/scl:SCL/scl:Substation//*[(name()='PowerTransformer' or name()='GeneralEquipment' or name()='ConductingEquipment' or name()='Function') and count(.//scl:LNode[@iedName='" + iedName + "'])> 0]");
		}
		return lst;
	}
	
	/**
	 * 清除相关IED的缓存
	 * @param iedName
	 * @param cache
	 */
	private void clearRelatedCache(String iedName, Map<String, ?> cache) {
		for (Iterator<String> iter = cache.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			if (key != null) {
				int dollarPos = key.indexOf("$");
				if (dollarPos < 0)
					return;
				String ky = key.substring(0, dollarPos);
				if (ky.equals(iedName))
					iter.remove();
			}
		}
	}

	/**
	 * 清除缓存
	 */
	public void clearPreRelated() {
		relatedLNodeInfo.clear();
		hshRelatedLNode.clear();
	}
	
	/**
	 * 得到关联LNode颜色映射表
	 * @return
	 */
	public Map<String, String[]> getRelatedLNodeInfo() {
		return relatedLNodeInfo;
	}
	
	/**
	 * 得到关联LNode颜色映射表
	 * @return
	 */
	public Map<String, Color> getRelatedLNodeMap() {
		if (!hshRelatedLNode.containsKey(nullKey)) {
			hshRelatedLNode.put(nullKey, UIConstants.WHITE);
		}
		return hshRelatedLNode;
	}
}
