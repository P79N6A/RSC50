/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.das.CommunicationDAO;
import com.shrcn.business.scl.enums.EnumCommType;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-11-18
 */
/**
 * $Log: CommunicationChecker.java,v $
 * Revision 1.2  2013/04/07 12:25:00  cchun
 * Refactor:清理引用
 *
 * Revision 1.1  2013/03/29 09:37:58  cchun
 * Add:创建
 *
 * Revision 1.3  2012/08/28 03:51:05  cchun
 * Update:清理引用
 *
 * Revision 1.2  2012/01/17 08:50:31  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.1  2011/11/18 09:13:50  cchun
 * Add:添加通信配置校验工具类
 *
 */
public class CommunicationChecker {

	private static Map<String, List<CommObj>> commCfgCache;
	
	private CommunicationChecker() {}
	
	/**
	 * 校验通信配置
	 * @param subNet
	 * @return
	 */
	public static String check(String subNet) {
		initCache(subNet);
		StringBuilder sbMsg = new StringBuilder("\n-------通信配置校验开始-------\n");
		int errNum = 0;
		Set<Entry<String, List<CommObj>>> entries = commCfgCache.entrySet();
		for(Entry<String, List<CommObj>> entry : entries) {
			List<CommObj> commObjs = entry.getValue();
			int size = commObjs.size();
			if (size > 1) {
				errNum++;
				String addr = entry.getKey();
				StringBuilder error = new StringBuilder("错误：地址[" + addr +
						"]被" + size +
						"个装置重复使用——");
				int i = 1;
				for (CommObj commObj : commObjs) {
					error.append(commObj.toString());
					if (i < commObjs.size())
						error.append("，");
					i++;
				}
				error.append("。\n");
				sbMsg.append(error);
			}
		}
		sbMsg.append("共计" + errNum + "处错误。\n");
		sbMsg.append("-------通信配置校验结束-------\n");
		return sbMsg.toString();
	}
	
	/**
	 * 初始化通信配置
	 * @param subNet
	 */
	private static void initCache(String subNet) {
		if (commCfgCache == null) {
			commCfgCache = new HashMap<String, List<CommObj>>();
		} else {
			commCfgCache.clear();
		}
		List<Element> comms = CommunicationDAO.loadComms(subNet);
		for (Element comm : comms) {
			String iedName = comm.attributeValue("iedName");
			String apName = comm.attributeValue("apName");
			List<?> cfgs = comm.elements();
			for (Object obj : cfgs) {
				Element cfg = (Element) obj;
				String ndName = cfg.getName();
				EnumCommType commType = EnumCommType.getType(ndName);
				String addrXPath = null;
				String addr = null;
				switch (commType) {
					case MMS:
						addrXPath = "./*[name()='P'][@type='IP']";
						addr = DOM4JNodeHelper.selectSingleNode(cfg, addrXPath).getTextTrim();
						putConfig(addr, iedName, apName, null, null);
						break;
					case GSE:
					case SMV:
						addrXPath = "./*[name()='Address']/*[name()='P'][@type='MAC-Address']";
						addr = DOM4JNodeHelper.selectSingleNode(cfg, addrXPath).getTextTrim();
						String cbName = cfg.attributeValue("cbName");
						String ldInst = cfg.attributeValue("ldInst");
						putConfig(addr, iedName, apName, ldInst, cbName);
						break;
					default:
						break;
				}
			}
		}
	}
	
	private static void putConfig(String addr, String iedName, String apName,
			String ldInst, String cbName) {
		List<CommObj> commObjs = commCfgCache.get(addr);
		if (commObjs == null) {
			commObjs = new ArrayList<CommObj>();
			commCfgCache.put(addr, commObjs);
		}
		commObjs.add(new CommObj(iedName, apName, ldInst, cbName));
	}
}

class CommObj {
	String iedName;
	String apName;
	String ldInst;
	String cbName;
	
	public CommObj(String iedName, String apName, String ldInst, String cbName) {
		this.iedName = iedName;
		this.apName = apName;
		this.ldInst = ldInst;
		this.cbName = cbName;
	}

	@Override
	public String toString() {
		return "装置" + iedName + "（" +
			"访问点[" + apName + "]" +
			(ldInst==null ? "" : "逻辑设备[" + ldInst + "]") +
			(cbName==null ? "" : "控制块[" + cbName + "]）");
	}
}	
