/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.ExportCidFunction;

/**
 * 
 * @author 黄钦辉(mailto:huangqinhui@shrcn.com)
 * @version 1.0, 2009-4-27
 */
/*
 * 修改历史 $Log: CIDDAO.java,v $
 * 修改历史 Revision 1.2  2013/10/24 01:31:37  cchun
 * 修改历史 Refactor:提取createCidNode()
 * 修改历史
 * 修改历史 Revision 1.1  2013/03/29 09:36:19  cchun
 * 修改历史 Add:创建
 * 修改历史
 * 修改历史 Revision 1.30  2013/02/22 08:31:08  cchun
 * 修改历史 Fix Bug:修复monitor == null缺陷
 * 修改历史
 * 修改历史 Revision 1.29  2013/02/01 08:38:26  cchun
 * 修改历史 Update:export()增加monitor.done()
 * 修改历史
 * 修改历史 Revision 1.28  2012/01/17 08:50:29  cchun
 * 修改历史 Update:使用更加安全的xpath形式
 * 修改历史
 * 修改历史 Revision 1.27  2012/01/13 08:30:19  cchun
 * 修改历史 Fix Bug:修复export()中root.add(ied)错误
 * 修改历史
 * 修改历史 Revision 1.26  2011/10/09 06:45:51  cchun
 * 修改历史 Update:为导出cid添加警告信息
 * 修改历史
 * 修改历史 Revision 1.25  2011/09/02 08:59:31  cchun
 * 修改历史 Fix Bug:修复client IED子网没合并的问题
 * 修改历史
 * 修改历史 Revision 1.24  2011/06/21 09:12:43  cchun
 * 修改历史 Fix Bug:去掉不需要的SubnetWork
 * 修改历史
 * 修改历史 Revision 1.23  2011/04/06 08:21:16  cchun
 * 修改历史 Update:添加export()方法
 * 修改历史
 * 修改历史 Revision 1.22  2011/01/07 09:42:21  cchun
 * 修改历史 Fix Bug:为getCommunication()增加子网判断
 * 修改历史
 * 修改历史 Revision 1.21  2010/11/08 07:13:31  cchun
 * 修改历史 Update:清理引用
 * 修改历史
 * 修改历史 Revision 1.20  2010/09/07 09:59:33  cchun
 * 修改历史 Update:更换过时接口
 * 修改历史
 * 修改历史 Revision 1.19  2010/06/17 03:08:00  cchun
 * 修改历史 Fix Bug:修复重复导出DA的bug
 * 修改历史
 * 修改历史 Revision 1.18  2010/06/07 06:36:06  cchun
 * 修改历史 Fix Bug:修复枚举类型导出不完整的bug
 * 修改历史
 * 修改历史 Revision 1.17  2010/05/20 06:01:12  cchun
 * 修改历史 Fix Bug:修复getCommunication()中xpath错误
 * 修改历史
 * 修改历史 Revision 1.16  2010/01/19 09:02:30  lj6061
 * 修改历史 add:统一国际化工程
 * 修改历史
 * 修改历史 Revision 1.15  2009/11/27 02:53:45  wyh
 * 修改历史 当SubNetwork节点下不存在指定IED的ConnectedAP时不进行添加
 * 修改历史
 * 修改历史 Revision 1.14  2009/11/27 02:47:34  wyh
 * 修改历史 修改导出CID通信部分缺失的bug
 * 修改历史
 * 修改历史 Revision 1.13  2009/11/10 07:44:38  cchun
 * 修改历史 Fix Bug:修改单词拼写错误
 * 修改历史
 * 修改历史 Revision 1.12  2009/08/11 08:44:57  lj6061
 * 修改历史 合并主干分支代码
 * 修改历史
 * 修改历史 Revision 1.11.2.2  2009/08/10 08:15:41  hqh
 * 修改历史 添加单例模式
 * 修改历史 修改历史 Revision 1.11.2.1 2009/07/31 09:37:12 cchun
 * 修改历史 Update:修改命名控件处理方式 修改历史 修改历史 Revision 1.11 2009/07/17 02:37:55 hqh 修改历史
 * add comment 修改历史 修改历史 Revision 1.10 2009/07/17 02:30:27 hqh 修改历史 修改空指针 修改历史
 * 修改历史 Revision 1.9 2009/07/08 02:08:19 hqh 修改历史 add comment 修改历史 修改历史 Revision
 * 1.8 2009/07/08 01:48:01 hqh 修改历史 修改导数据模板 修改历史 修改历史 Revision 1.7 2009/07/08
 * 01:42:24 hqh 修改历史 修改导数据模板 修改历史 修改历史 Revision 1.6 2009/06/04 05:18:43 hqh 修改历史
 * 导出cid的传数据模板参数 修改历史 修改历史 Revision 1.5 2009/06/04 03:23:45 hqh 修改历史 修改导出cid文件
 * 修改历史 修改历史 Revision 1.4 2009/06/03 12:29:49 hqh 修改历史 修改导出cid 修改历史 修改历史
 * Revision 1.3 2009/05/26 05:34:29 hqh 修改历史 添加导出cid DAO 修改历史 Revision 1.2
 * 2009/04/29 02:23:34 cchun 统一数据操作对象接口
 * 
 * Revision 1.1 2009/04/28 00:35:54 hqh 添加蹈入cid模型
 * 
 */
public class CIDDAO implements SCLDAO {

	private CIDDAO() {
	}

	/**
	 * 导通讯
	 * 
	 * @param name
	 * @param comm
	 */
	private static void getCommunication(String name, Element comm) {
		List<Element> subNetworkElements = getSubNetworks(name);
		for (Element subNetworkElement : subNetworkElements) {
			comm.add(subNetworkElement.createCopy());
		}
	}
	
	/**
	 * 导虚拟IED通讯配置
	 * 
	 * @param name
	 * @param subnets
	 * @param comm
	 */
	private static void appendClientIEDComm(String name, List<?> subnets, Element comm) {
		List<Element> subNetworkElements = getSubNetworks(name);
		for (Element subNetworkElement : subNetworkElements) {
			boolean added = false;
			for (Object obj : subnets) {
				Element subnet = (Element) obj;
				String subnetName = subnet.attributeValue("name");
				String clientSubNetName = subNetworkElement.attributeValue("name");
				if (subnetName.trim().equals(clientSubNetName.trim())) {
					List<?> connectedAPs = subNetworkElement.elements("ConnectedAP");
					for (Object connectedAP : connectedAPs) {
						subnet.add(((Element) connectedAP).createCopy());
						added = true;
					}
				}
			}
			if (!added)
				comm.add(subNetworkElement.createCopy());
		}
	}
	
	public static List<Element> getSubNetworks(String name) {
		if (Constants.XQUERY) {
			String xquery = "for $subnet in " + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:Communication/scl:SubNetwork" +
					" where exists($subnet/scl:ConnectedAP[@iedName='" + name + "'])" +
					" return element SubNetwork {$subnet/@*" +
					", $subnet/scl:BitRate, $subnet/scl:ConnectedAP[@iedName='" + name + "']" +
					"}";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			String select = "/scl:SCL/scl:Communication/scl:SubNetwork[count(./scl:ConnectedAP[@iedName='" + name + "'])>0]";
			List<Element> subNetworkElements = XMLDBHelper.selectNodesOnlyAtts(select, "SubNetwork");
			for (Element subNetworkElement : subNetworkElements) {
				String netName = subNetworkElement.attributeValue("name");
				List<Element> subNds = new ArrayList<>();
				subNds.addAll(XMLDBHelper.selectNodes("/scl:SCL/scl:Communication/scl:SubNetwork[@name='" + netName + "']/scl:BitRate"));
				subNds.addAll(XMLDBHelper.selectNodes("/scl:SCL/scl:Communication/scl:SubNetwork[@name='" + netName + "']/scl:ConnectedAP[@iedName='" + name + "']"));
				for (Element subNd : subNds) {
					subNetworkElement.add(subNd.createCopy());
				}
			}
			return subNetworkElements;
		}
	}

	/**
	 * 导出指定IED对应的cid文件
	 * @param monitor
	 * @param name
	 * @param path
	 * @return 出错信息
	 */
	public static void export(IProgressMonitor monitor, String iedName, String path, String suffix) {
		if (Constants.XQUERY || XMLDBHelper.isInCache(SCL.getIEDXPath(iedName))) {
			String fileName = path + File.separator + iedName + suffix;
			Element root = createCidNode(monitor, iedName);
			if (root == null)
				return;
			XMLFileManager.writeDoc(root.getDocument(), fileName);
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("iedName", iedName);
			params.put("monitor", monitor);
			String cid = (String) XMLDBHelper.callFunction(ExportCidFunction.class, params);
			FileManager.saveTextFile(path + File.separator + iedName + ".cid", cid, "UTF-8");
		}
		if (monitor != null) {
			monitor.setTaskName("完成文件名为:" + iedName + suffix + "文件");// 提示信息
		}
	}
	
	public static void export(IProgressMonitor monitor, String iedName, String path) {
		export(monitor, iedName, path, ".cid");
	}
	
	private static void addSubElement(Element cidRoot, Element node) {
		cidRoot.add(node.createCopy());
	}
	
	/**
	 * 查询scd虚端子crc。
	 * @return
	 */
	public static String getScdCrc() {
		Element scdcrc = XMLDBHelper.selectSingleNode("/scl:SCL/scl:Private[@type='"+ Constants.SCD_CRC_DESC +"']"); //$NON-NLS-1$
		return (scdcrc==null) ? "" : scdcrc.getTextTrim();
	}

	/**
	 * 创建IED节点
	 * @param monitor
	 * @param iedName
	 * @return
	 */
	public static Element createCidNode(IProgressMonitor monitor, String iedName) {
		Element cidRoot = DOM4JNodeHelper.createSCLNode("SCL"); //$NON-NLS-1$
		if(SCTProperties.getInstance().needsVersions()) {
			cidRoot.addAttribute("revision", "A");
			cidRoot.addAttribute("version", "2007");
		}
		
		Element scdcrc = XMLDBHelper.selectSingleNode("/scl:SCL/scl:Private[@type='"+ Constants.SCD_CRC_DESC +"']"); //$NON-NLS-1$
		if (scdcrc != null) {
			addSubElement(cidRoot, scdcrc);
		}
		Element head = XMLDBHelper.selectSingleNode("/scl:SCL/scl:Header"); //$NON-NLS-1$
		if (head != null) {
			addSubElement(cidRoot, head);
		}
		
		if (monitor != null) {
			monitor.setTaskName("完成文件名为:" + iedName + ".cid文件的Header标签");
			monitor.worked(1);
		}
		Element comm = cidRoot.addElement("Communication");//$NON-NLS-1$
		getCommunication(iedName, comm);
		if (monitor != null) {
			monitor.setTaskName("完成文件名为:" + iedName + ".cid文件的Communication标签");
			monitor.worked(1);
		}
		List<?> subnets = comm.elements("SubNetwork");
		Element iedNode = IEDDAO.getIEDNode(iedName);
		if (iedNode == null)
			return null;
		List<String> clientIEDs = DOM4JNodeHelper.getAttributeValues(iedNode, ".//*[name()='ReportControl']/*[name()='RptEnabled']/*[name()='ClientLN']/@iedName");
		clientIEDs.addAll(DOM4JNodeHelper.getNodeValuesByXPath(iedNode, ".//*[name()='SampledValueControl']/*[name()='IEDName']"));
		for (String clientIED : clientIEDs) {
			appendClientIEDComm(clientIED, subnets, comm);
		}
		// ~
		addSubElement(cidRoot, iedNode);
		if (monitor != null) {
			monitor.setTaskName("完成文件名为:" + iedName + ".cid文件的IED标签");
			monitor.worked(1);
		}
		
		appendDataTemp(cidRoot);
		if (monitor != null) {
			monitor.setTaskName("完成文件名为:" + iedName + ".cid文件的DataTypeTemplates标签");
			monitor.worked(1);
		}
		SCLUtil.updateRootNS(cidRoot);
		return cidRoot;
	}
	
	private static void appendDataTemp(Element cidRoot) {
		List<String> lnTypes = DOM4JNodeHelper.getAttributeValues(cidRoot, SCL.XPATH_IED 
				+ "/scl:AccessPoint/scl:Server/scl:LDevice/*[name()='LN0' or name()='LN']/@lnType");
		cidRoot.add(getIEDDatTemplates(lnTypes).createCopy());
	}
	
	public static Element getIEDDatTemplates(List<String> lnTypes) {
		Element dataTempEle = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		// 索引
		Map<String, Element> typeMap = new HashMap<String, Element>();
		List<Element> types = dataTempEle.elements();
		for (Element type : types) {
			String id = type.attributeValue("id");
			if ("LNodeType".equals(type.getName())) {
				if (lnTypes.contains(id))
					typeMap.put(id, type);
			} else {
				typeMap.put(id, type);
			}
		}
		// 查找类型，这里将type分类存放可提高10倍性能
		Map<String, List<String>> subTypesMap = new HashMap<String, List<String>>();
		subTypesMap.put("LNodeType", new ArrayList<String>(100));
		subTypesMap.put("DOType", new ArrayList<String>(100));
		subTypesMap.put("DAType", new ArrayList<String>(100));
		subTypesMap.put("EnumType", new ArrayList<String>(100));
		List<String> subTypes = subTypesMap.get("LNodeType");
		for (String lnType : lnTypes) {
			subTypes.add(lnType);
			findSubTypes(lnType, subTypesMap, typeMap);
		}
		// 添加<scl:DataTypeTemplates>
		Element datNode = DOM4JNodeHelper.createSCLNode("DataTypeTemplates");
//				cidRoot.addElement("DataTypeTemplates");
		for (Element type : types) {
			String id = type.attributeValue("id");
			subTypes = subTypesMap.get(type.getName());
			if (subTypes.size()>0 && subTypes.contains(id)) {
				datNode.add(typeMap.get(id).createCopy());
				subTypes.remove(id);
			}
		}
		return datNode;
	}
	
	/**
	 * 查找子类型，包括：<DO>,<SDO>,以及bType为Struct或Enum的<DA>,<BDA>。
	 * @param type
	 * @param subTypesMap
	 * @param typeMap
	 */
	private static void findSubTypes(String type, Map<String, List<String>> subTypesMap, Map<String, Element> typeMap) {
		Element ndType = typeMap.get(type);
		if (ndType == null) {
			SCTLogger.error("找不到类型 " + type);
			return;
		}
		List<Element> ndSubs = ndType.elements();
		for (Element ndSub : ndSubs) {
			String ndName = ndSub.getName();
			String subType = ndSub.attributeValue("type");
			boolean hasSubs = "DO".equals(ndName) || "SDO".equals(ndName);
			if (!hasSubs) {
				if ("DA".equals(ndName) || "BDA".equals(ndName)) {
					String bType = ndSub.attributeValue("bType");
					hasSubs = "Struct".equals(bType) || "Enum".equals(bType);
				}
			}
			List<String> subTypes = null;
			if ("DO".equals(ndName) || "SDO".equals(ndName)) {
				subTypes = subTypesMap.get("DOType");
			} else if ("DA".equals(ndName) || "BDA".equals(ndName)) {
				String bType = ndSub.attributeValue("bType");
				if ("Struct".equals(bType)) {
					subTypes = subTypesMap.get("DAType");
				} else if ("Enum".equals(bType)) {
					subTypes = subTypesMap.get("EnumType");
				}
			}
			if (subTypes!=null && !subTypes.contains(subType)) { // 除重
				subTypes.add(subType);
				if (hasSubs) {
					findSubTypes(subType, subTypesMap, typeMap);
				}
			}
		}
	}
}
