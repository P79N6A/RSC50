/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.ximpleware.AutoPilot;
import com.ximpleware.VTDNav;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-8
 */
public class ExportCidFunction extends QueryVTDFunction {

	private String rootPart = "";
	private String crcPart = "";
	private String headerPart = "";
	private String commPart = "";
	private String iedPart = "";
	private String dtpPart = "";
	
	private Map<String, Namespace> nsMap = new HashMap<String, Namespace>();
	private String iedName;
	private IProgressMonitor monitor;
	
	public ExportCidFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		super(ap, vn, params);
		this.iedName = (String) params.get("iedName");
		this.monitor = (IProgressMonitor) params.get("monitor");
	}
	
	private void updateTask(String msg) {
		if (monitor != null) {
			monitor.setTaskName(msg);
		}
	}

	@Override
	public Object exec() {
		addRoot();
		addScdCrc();
		addHeader();
		updateTask("完成文件名为:" + iedName + ".cid文件的Header标签");
		addCommunication();// 包括虚拟IED通信设置
		updateTask("完成文件名为:" + iedName + ".cid文件的Communication标签");
		addIED();
		updateTask("完成文件名为:" + iedName + ".cid文件的IED标签");
		addDataTemplate();
		updateTask("完成文件名为:" + iedName + ".cid文件的DataTypeTemplates标签");
		
//		StringBuilder content = new StringBuilder(1024 * 5);\
		String content = "";
		content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		finishRoot();
		content += rootPart;
		content += crcPart;
		content += headerPart;
		content += commPart;
		content += iedPart;
		content += dtpPart;
		content += "</SCL>";
		return content.toString();
	}
	
	private void finishRoot() {
		for (Namespace ns : nsMap.values()) {
			String attName = " xmlns:" + ns.getPrefix() + "=\"";
			if (rootPart.indexOf(attName)<0)
				rootPart += attName + ns.getURI() + "\"";
		}
		rootPart += ">\r\n";
	}

	/**
	 * 要考虑保留私有命名空间
	 */
	private void addRoot() {
		rootPart += selectNodesOnlyAtts("/SCL", "SCL").get(0);
	}

	/**
	 * SCD CRC
	 */
	private void addScdCrc() {
		crcPart += "\t" + selectSingleNode("/SCL/Private[@type='"+ Constants.SCD_CRC_DESC +"']") + "\r\n";
	}

	/**
	 * 历史版本信息
	 */
	private void addHeader() {
		headerPart += "\t" + selectSingleNode("/SCL/Header") + "\r\n";
	}

	/**
	 * 通信配置（包括虚拟IED）
	 */
	private void addCommunication() {
		commPart += "\t<Communication>\r\n";
		
		String ieds = "@iedName='" + iedName + "'";
		List<String> clientIEDs = new ArrayList<String>();
		clientIEDs.addAll(XMLDBHelper.getAttributeValues(getIEDXpath() + "/AccessPoint/Server/LDevice/*/*[name()='ReportControl']/*[name()='RptEnabled']/*[name()='ClientLN']/@iedName"));
		clientIEDs.addAll(XMLDBHelper.getAttributeValues(getIEDXpath() + "/AccessPoint/Server/LDevice/*/*[name()='SampledValueControl']/*[name()='IEDName']"));
		for (String clientIED : clientIEDs) {
			ieds += " or @iedName='" + clientIED + "'";
		}
		
		List<String> subNets = selectNodesOnlyAtts("/SCL/Communication/SubNetwork[count(./ConnectedAP[" + ieds + "])>0]", "SubNetwork");
		for (String subNet : subNets) {
			commPart += "\t\t" + subNet + ">\r\n";
			int p = subNet.indexOf("name=");
			String netName = subNet.substring(p + 6);
			netName = netName.substring(0, netName.indexOf('"'));
			List<String> netEles = new ArrayList<>();
			netEles.addAll(selectNodes("/SCL/Communication/SubNetwork[@name='" + netName + "']/BitRate"));
			netEles.addAll(selectNodes("/SCL/Communication/SubNetwork[@name='" + netName + "']/ConnectedAP[" + ieds + "]"));
			for (String netEle : netEles) {
				commPart += "\t\t\t" + netEle + "\r\n";
			}
			commPart += "\t\t</SubNetwork>\r\n";
		}
		commPart += "\t</Communication>\r\n";
	}

	/**
	 * IED
	 */
	private void addIED() {
		String iedNode = selectSingleNode(getIEDXpath());
		iedNode = clearNamespaces(iedNode, nsMap);
		iedPart += "\t" + iedNode + "\r\n";
	}

	private String getIEDXpath() {
		return "/SCL/IED[@name='" + iedName + "']";
	}

	/**
	 * 数据模板
	 */
	private void addDataTemplate() {
		dtpPart += "\t<DataTypeTemplates>\r\n";
		
		Element dataTempEle = XMLDBHelper.selectSingleNode("/SCL/DataTypeTemplates");
		List<String> lnTypes = XMLDBHelper.getAttributeValues(getIEDXpath() 
				+ "/AccessPoint/Server/LDevice/*/@lnType");
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
		// 添加<DataTypeTemplates>
		for (Element type : types) {
			String id = type.attributeValue("id");
			subTypes = subTypesMap.get(type.getName());
			if (subTypes.size()>0 && subTypes.contains(id)) {
				dtpPart += asXML(typeMap.get(id), nsMap);
				subTypes.remove(id);
			}
		}
		dtpPart += "\t</DataTypeTemplates>\r\n";
	}
	
	private void findSubTypes(String type, Map<String, List<String>> subTypesMap, Map<String, Element> typeMap) {
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

