/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.history.HistoryManager;
import com.shrcn.business.scl.history.MarkInfo.DevType;
import com.shrcn.business.scl.history.MarkInfo.OperType;
import com.shrcn.business.scl.model.IED;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-15
 */

public class IEDDAO implements SCLDAO {

	private List<String> allNameDesc = null;
	private List<String> allName = null;
	private String iedName = null;
	private String iedXPath = null;
	
	/**
	 * @param iedName 当前的IEDName
	 * @param needLoad 是否需要重新加载当前SCD中所有的IED名称和描述
	 */
	public IEDDAO(String iedName, boolean needLoad) {
		if(!StringUtil.isEmpty(iedName)){
			this.iedName = iedName;
			this.iedXPath = "/scl:SCL/scl:IED[@name='" + iedName + "']";
		}
		
		if(needLoad)
			load();
	}
	
	public IEDDAO() {
		this(null,true);
	}
	
	private void load() {
		allNameDesc = new ArrayList<String>();
		allName = new ArrayList<String>();
		List<Element> listElement = getALLIED();
		for (Element e : listElement) {
			String name = e.attributeValue("name", "");
			String desc = e.attributeValue("desc", "");
			allNameDesc.add(name + 
					("".equals(desc)?"":Constants.COLON) + desc);
			allName.add(name);
		}
	}
	
	/**
	 * 得到当前系统下所有带有报告控制快的IED装置名和描述
	 * @return
	 */
	public static List<String> getSubstationIEDs() {
		List<Element> ieds =  getSubstationIEDInfos();
		List<String> iedDesc = new ArrayList<String>();
		for (Element ied : ieds) {
			String name = ied.attributeValue("name");
			String desc = ied.attributeValue("desc");
			name = StringUtil.getLabel(name, desc);
			iedDesc.add(name);
		}
		return iedDesc;
	}
	
	/**
	 * 得到当前系统下所有站控层IED装置。
	 * @return
	 */
	public static List<Element> getSubstationIEDInfos() {
		if (Constants.XQUERY){
			String xquery = "let $ieds:=" + XMLDBHelper.getDocXPath() +
					"/SCL/IED return for $ied in $ieds where count($ied/AccessPoint/Server/LDevice/*/ReportControl) > 0 " +
					"return <IED name='{$ied/@name}' desc='{$ied/@desc}' type='{$ied/@type}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			String xPath = "/SCL/IED[count(./AccessPoint/Server/LDevice/*/ReportControl)>0]";
			return XMLDBHelper.selectNodesOnlyAtts(xPath, "IED");
		}
	}
	
	/**
	 * 得到当前系统下所有IED装置。
	 * @return
	 */
	public static List<Element> getProcessIEDInfos() {
		if (Constants.XQUERY){
			String xquery = "let $ieds:=" + XMLDBHelper.getDocXPath() +
			"/SCL/IED return for $ied in $ieds where count($ied/AccessPoint/Server/LDevice/*/GSEControl) > 0 " +
			"or count($ied/AccessPoint/Server/LDevice/*/SampledValueControl) > 0 return <IED name='{$ied/@name}' desc='{$ied/@desc}' type='{$ied/@type}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			String xPath = "/SCL/IED[count(./AccessPoint/Server/LDevice/*/GSEControl)>0 or count(./AccessPoint/Server/LDevice/*/SampledValueControl)>0]";
			return XMLDBHelper.selectNodesOnlyAtts(xPath, "IED");
		}
	}
	
	/**
	 * 得到当前系统下所有合并单元装置。
	 * @return
	 */
	public static List<Element> getUDMIEDInfos() {
		if (Constants.XQUERY){
			String xquery = "let $ieds:=" + XMLDBHelper.getDocXPath() +
			"/SCL/IED return for $ied in $ieds where count($ied/AccessPoint/Server/LDevice/*/SampledValueControl) > 0 " +
			"return <IED name='{$ied/@name}' desc='{$ied/@desc}' type='{$ied/@type}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			String xPath = "/SCL/IED[count(./AccessPoint/Server/LDevice/*/SampledValueControl)>0]";
			return XMLDBHelper.selectNodesOnlyAtts(xPath, "IED");
		}
	}
	
	/**
	 * 获取所有IED的名称和描述
	 * @param type
	 * @return
	 */
	public static List<String> getAllIEDNameDesc() {
		return getIEDNameDescByType(null);
	}
	
	/**
	 * 获取所有IED的名称和描述Map
	 * @return
	 */
	public static Map<String, String> getAllIEDNameDescMap() {
		java.util.List<String> allNameDesc = getAllIEDNameDesc();
		Map<String, String> iedMap = new HashMap<>();
		for (String nameDesc : allNameDesc) {
			String ied = nameDesc.split(Constants.COLON)[0];
			iedMap.put(ied, nameDesc);
		}
		return iedMap;
	}
	
	/**
	 * 根据IED类型获取IED的名称和描述
	 * 
	 * @param type 装置类型
	 * @return
	 */
	public static List<String> getIEDNameDescByType(String type) {
		ArrayList<String> allNameDesc = new ArrayList<String>();
		List<Element> listElement = getIEDByType(type);
		if (listElement == null)
			return allNameDesc;
		for (Element e : listElement) {
			String name = e.attributeValue("name", "");
			String desc = e.attributeValue("desc", "");
			allNameDesc.add(name + 
					("".equals(desc)?"":Constants.COLON) + desc);
		}
		return allNameDesc;
	}
	
	/**
	 * 根据IED的装置类型进行查找IED
	 * 
	 * @param type 装置类型
	 * @return
	 */
	public static List<Element> getIEDByType(String type){
		if (Constants.XQUERY){
			String fnQuery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED "
				+ (StringUtil.isEmpty(type) ? "" : " where $ied/@type='" + type + "' ")
				+ " return element IED {$ied/@*}";
			return XMLDBHelper.queryNodes(fnQuery);
		} else {
			if (StringUtil.isEmpty(type)) {
				return getALLIED();
			} else {
				String xPath = "/SCL/IED[@type='" + type + "']";
				return XMLDBHelper.selectNodesOnlyAtts(xPath, "IED");
			}
		}
	}
	
	/**
	 * 查询所有的IED的所有属性
	 * 
	 * @return 返回所有IED的所有属性
	 */
	public static List<Element> getALLIED() {
		return queryIEDs(false);
	}
	
	/**
	 * 查询所有的IED的所有属性（带虚端子CRC）
	 * @return
	 */
	public static List<Element> getAllIEDWithCRC() {
		List<Element> ieds = queryIEDs(true);
		List<Element> sortList = new ArrayList<>();
		for (Element ied : ieds) {
			sortList.add((Element) ied.detach());
		}
		Collections.sort(sortList, new Comparator<Element>() {
			@Override
			public int compare(final Element o1, final Element o2) {
				final Element ied1 = (Element) o1;
				final Element ied2 = (Element) o2;
				return ied1.attributeValue("name").compareTo(ied2.attributeValue("name"));
			}
		});
		return sortList;
	}
	
	/**
	 * 查询装置信息
	 * @param withCRC 是否带CRC
	 * @return
	 */
	private static List<Element> queryIEDs(boolean withCRC) {
		List<Element> ieds = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String fnQuery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED "
					+ " return element IED {$ied/@*" +
					(withCRC ? ", attribute crc{$ied/scl:Private[@type='" + Constants.IED_CRC_DESC + "']}" : "") +
					"}";
			ieds =  XMLDBHelper.queryNodes(fnQuery);
		} else {
			ieds =  XMLDBHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
			if (withCRC) {
				for (Element ied : ieds) {
					String iedName = ied.attributeValue("name");
					String crc = XMLDBHelper.getNodeValue("/scl:SCL/scl:IED[@name='" + iedName  +
							"']/scl:Private[@type='" + Constants.IED_CRC_DESC + "']");
					ied.addAttribute("crc", StringUtil.nullToEmpty(crc));
				}
			}
		}
		return ieds;
	}
	
	/**
	 * 返回所有IED的名称和描述,每个字符串格式:名称+(":"+描述)
	 * 
	 * @return
	 */
	public List<String> getAllNameDescs() {
		return allNameDesc;
	}

	/**
	 * 按IED装置类型对IED进行排序
	 * 
	 * @return
	 */
	public static List<IED> getAllNameDescsByTypeOrder() {
		List<IED> lstIED = new ArrayList<IED>();
		List<Element> listElement = getAllIEDWithCRC();
		for (Element e : listElement) {
			lstIED.add(createIED(e));
		}
		return lstIED;
	}
	
	/**
	 * 将XML节点转为Java对象
	 * @param e
	 * @return
	 */
	public static IED createIED(Element e) {
		String name = e.attributeValue("name", "");
		String desc = e.attributeValue("desc", "");
		String type = e.attributeValue("type", "");
		String manufacturer = e.attributeValue("manufacturer", "");
		String configVersion = e.attributeValue("configVersion", "");
		String crc = e.attributeValue("crc", "");
		IED ied = new IED(name, desc, type, manufacturer, configVersion);
		ied.setVtCRC(crc);
		return ied;
	}
	
	/**
	 * 查询IED信息
	 * @param name
	 * @return
	 */
	public static IED queryIED(String name) {
		if (Constants.XQUERY){
			String xq = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED "
				+ " where $ied/@name='" + name + "' "
				+ " return element IED {$ied/@*}";
			List<Element> list = XMLDBHelper.queryNodes(xq);
			if (list == null || list.isEmpty())
				return null;
			return createIED(list.get(0));
		} else {
			String xPath = "/SCL/IED[@name='" + name + "']";
			List<Element> list = XMLDBHelper.selectNodesOnlyAtts(xPath, "IED");
			for (Element element : list) {
				String temp = element.attributeValue("name");
				if (temp!=null && temp.equals(name)){
					return createIED(element);
				}
			}
			return null;
		}
	}
	
	/**
	 * 获取当前IED下所有的LD的inst属性
	 * @return
	 */
	public static String[] getLDInsts(String iedName) {
		String iedXPath = "/scl:SCL/scl:IED[@name='" + iedName + "']";
		String ldInstXPath = iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice/@inst";
		List<String> insts = XMLDBHelper.getAttributeValues(ldInstXPath);
		return insts.toArray(new String[insts.size()]);
	}
	
	/**
	 * 获取当前IED对应的访问点下的所有LD的inst
	 * 
	 * @param apName
	 * @return LD inst值数组
	 */
	public static String[] getLDInsts(String iedName, String apName) {
		String iedXPath = "/scl:SCL/scl:IED[@name='" + iedName + "']";
		String ldInstXPath = iedXPath + "/scl:AccessPoint[@name='" + apName + "']/scl:Server/scl:LDevice/@inst";
		List<String> insts = XMLDBHelper.getAttributeValues(ldInstXPath);
		String[] lds = insts.toArray(new String[insts.size()]);
		return lds;
	}

	/**获取当前IED下所有的AP的名称
	 * @return
	 */
	public static String[] getAPNames(String iedName) {
		List<String> names = XMLDBHelper.getAttributeValues("/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/@name");
		return names.toArray(new String[names.size()]);
	}

	public static IEDInfo getIedInfo(String iedName) {
		Element ied = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
		return ied ==null ? null : new IEDInfo(iedName, ied.attributeValue("desc", ""), ied.attributeValue("type", "")
				, ied.attributeValue("configVersion", ""), ied.attributeValue("manufacturer", ""));
	}
	
	/**
	 * 获取指定IED之外的IED name数组
	 * @param iedName
	 * @return
	 */
	public String[] getOtherNames() {
		List<String> destIEDs = new ArrayList<String>();
		destIEDs.addAll(allName);
		destIEDs.remove(iedName);
		return destIEDs.toArray(new String[destIEDs.size()]);
	}
	
	public static List<String> getIEDNames() {
		List<String> list = new ArrayList<String>();
		List<Element> allied = getALLIED();
		for (Element iedEl : allied) {
			String iedName = iedEl.attributeValue("name");
			if (!StringUtil.isEmpty(iedName))
				list.add(iedName);
		}
		return list;
	}
	
	/**
	 * 查询指定IED描述信息
	 * @param iedName
	 * @return
	 */
	public static String getDesc(String iedName) {
		return XMLDBHelper.getAttributeValue("/scl:SCL/scl:IED[@name='" + iedName + "']/@desc");
	}
	
	/**
	 * 从名称描述组合字符串分解出IED名称
	 * @param nameDesc
	 * @return
	 */
	public static String getName(String nameDesc){
		int seperator = nameDesc.indexOf(Constants.COLON);
		if (seperator > 0) {
			return nameDesc.substring(0, seperator);
		} else {
			return nameDesc;
		}
	}

	public String getIedName() {
		return iedName;
	}

	public void setIedName(String iedName) {
		this.iedName = iedName;
	}

	public static boolean isProcessAP(String iedName, String apName) {
		return XMLDBHelper.existsNode("/scl:SCL/scl:IED[@name='" + iedName + 
				"']/scl:AccessPoint[@name='" + apName + "']//scl:GSEControl");
	}
	
	public void updateDesc(String newDesc){
		XMLDBHelper.saveAttribute(iedXPath, "desc", newDesc); //$NON-NLS-1$
	}
	
	
	/**
	 * 得到数据库中指定IED的LDevice信息
	 * @param iedName
	 * @return
	 */
	public static List<Element> queryLDevices(String iedName) {
		if (Constants.XQUERY){
			String xquery = "for $ap in " + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint return " +
					"for $ld in $ap/scl:Server/scl:LDevice return element LDevice {$ld/@*, attribute apName {" +
					"$ap/@name}, attribute iedName {'" + iedName + "'}, $ld/*}";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			Element iedNode = getIEDNode(iedName);
			List<Element> lds = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice");
			for (Element ld : lds) {
				String apName = ld.getParent().getParent().attributeValue("name", "");
				ld.addAttribute("apName", apName);
				ld.addAttribute("iedName", iedName);
			}
			return lds;
		}
	}

	/**
	 * 得到icd文件中IED节点的LDevice信息
	 * @param root
	 * @return
	 */
	public static List<Element> getLDevicesFromDoc(Element root) {
		Element ied = root.element("IED");
		List<Element> ldevices = new ArrayList<Element>();
		if (ied == null)
			return null;
		List<?> aps = ied.elements("AccessPoint"); //$NON-NLS-1$
		for (Object obj : aps) {
			Element ap = (Element) obj;
			List<?> ldevices1 = ap.element("Server").elements("LDevice"); //$NON-NLS-1$ //$NON-NLS-2$
			for (Object ld : ldevices1) {
				ldevices.add((Element) ld);
			}
		}
		return ldevices;
	}
	
	/**
	 * 查询指定IED节点
	 * @param iedName
	 * @return
	 */
	public static Element getIEDNode(String iedName) {
		return XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
	}
	
	/**
	 * 从根节点得到IED节点
	 * @param root
	 * @return
	 */
	public static Element getIEDNode(Element root) {
		return root.element("IED");
	}
	
	/**
	 * 得到IED节点中ExtRef信息
	 * @param iedNode
	 * @return
	 */
	public static List<Element> getExtRefs(Element iedNode) {
		List<Element> extRefs = DOM4JNodeHelper.selectNodes(iedNode, ".//*[name()='ExtRef']");
		Map<String, String> lnTypeCache = new HashMap<String, String>();
		for (Element extRef : extRefs) {
			fixExtRef(extRef, iedNode, lnTypeCache);
		}
		return extRefs;
	}
	
	/**
	 * 为<ExtRef>添加lnType属性。
	 * @param extRef
	 * @param iedNode
	 * @param lnTypeCache
	 */
	public static void fixExtRef(Element extRef, Element iedNode, Map<String, String> lnTypeCache) {
		String intAddr = extRef.attributeValue("intAddr");
		if (intAddr == null)
			return;
		if (intAddr.indexOf(":")>-1)
			intAddr = intAddr.split(":")[1];
		int slashPos = intAddr.indexOf("/");
		if (slashPos < 1)
			return;
		String ldInst = intAddr.substring(0, slashPos);
		int dotPos = intAddr.indexOf(".", slashPos);
		if (dotPos < 0)
			return;
		String lnName = intAddr.substring(slashPos + 1, dotPos);
		String key = ldInst + "/" + lnName;
		String lnType = lnTypeCache.get(key);
		if (lnType == null) {
			String[] lnInfo = SCLUtil.getLNInfo(lnName);
			String lnXPath = "./*[name()='AccessPoint']/*[name()='Server']/*[name()='LDevice'][@inst='" + ldInst + "']"
				+ SCL.getLNXPath(lnInfo[0], lnInfo[1], lnInfo[2]);
			Element ln = DOM4JNodeHelper.selectSingleNode(iedNode, lnXPath);
			if (ln == null)
				return;
			lnType = ln.attributeValue("lnType");
			lnTypeCache.put(key, lnType);
		}
		extRef.addAttribute("lnType", lnType);
	}
	
	/**
	 * 为外部<ExtRef>添加lnType属性。
	 * @param extRef
	 * @param iedNode
	 * @param lnTypeCache
	 */
	public static void fixOutExtRef(Element extRef, Element iedNode, Map<String, String> lnTypeCache) {
		String ldInst = extRef.attributeValue("ldInst");
		String prefix = extRef.attributeValue("prefix");
		String lnClass = extRef.attributeValue("lnClass");
		String lnInst = extRef.attributeValue("lnInst");
		String doName = extRef.attributeValue("doName");
		String daName = extRef.attributeValue("daName");
		String lnName = (prefix==null?"":prefix) + lnClass + lnInst;
		String lnType = lnTypeCache.get(lnName);
		if (lnType == null) {
			String lnXPath = "./*[name()='AccessPoint']/*[name()='Server']/*[name()='LDevice'][@inst='" + ldInst + "']"
					+ SCL.getLNXPath(prefix, lnClass, lnInst);
			Element ln = DOM4JNodeHelper.selectSingleNode(iedNode, lnXPath);
			if (ln == null)
				return;
			lnType = ln.attributeValue("lnType");
			lnTypeCache.put(lnName, lnType);
		}
		extRef.addAttribute("lnType", lnType);
		String outAddr = ldInst + "/" + lnName + "." + doName + 
			(StringUtil.isEmpty(daName) ? "" : "." + daName);
		extRef.addAttribute("outAddr", outAddr);
	}

	/**
	 * 得到IED节点中ExtRef信息
	 * @param iedName IED名称
	 * @return
	 */
	public static List<Element> getExtRefs(String iedName) {
		return getExtRefs(getIEDNode(iedName));
	}
	
	/**
	 * 得到IED节点中FCDA信息
	 * @param iedNode
	 * @return
	 */
	public static Map<String, List<String>> getFCDARefs(Element iedNode) {
		Map<String, List<String>> subRefMap = new HashMap<String, List<String>>();
		Map<String, String> lnTypeCache = new HashMap<String, String>();
		List<Element> datSets = DOM4JNodeHelper.selectNodes(iedNode, ".//*[name()='DataSet']");
		for (Element datSet : datSets) {
			String datName = datSet.attributeValue("name");
			List<?> fcdas = datSet.elements("FCDA");
			for (Object obj : fcdas) {
				Element fcda = (Element) obj;
				String prefix = DOM4JNodeHelper.getAttribute(fcda, "prefix");
				String lnClass = DOM4JNodeHelper.getAttribute(fcda, "lnClass");
				String lnInst = DOM4JNodeHelper.getAttribute(fcda, "lnInst");
				String ldInst = DOM4JNodeHelper.getAttribute(fcda, "ldInst");
				String doName = DOM4JNodeHelper.getAttribute(fcda, "doName");
				String daName = DOM4JNodeHelper.getAttribute(fcda, "daName");
				String lnName = ldInst + "/" + prefix + lnClass + lnInst;
				String lnType = null;
				if (lnTypeCache.containsKey(lnName)) {
					lnType = lnTypeCache.get(lnName);
				} else {
					String lnXPath = "./*[name()='AccessPoint']/*[name()='Server']/*[name()='LDevice'][@inst='" + ldInst + "']"
							+ SCL.getLNXPath(prefix, lnClass, lnInst);
					Element lnNode = DOM4JNodeHelper.selectSingleNode(iedNode, lnXPath);
					if (lnNode != null)
						lnType = lnNode.attributeValue("lnType");
					lnTypeCache.put(lnName, lnType);
				}
				String key = lnName + ":" + lnType + "@" + datName;
				List<String> refs = subRefMap.get(key);
				if (refs == null) {
					refs = new ArrayList<String>();
					subRefMap.put(key, refs);
				}
				refs.add(doName + (daName.length() > 0 ? "." + daName : ""));
			}
		}
		return subRefMap;
	}
	
	/**
	 * 查询装置所有的AccessPoint节点
	 * @param iedName
	 * @return
	 */
	public static List<Element> queryAPs(String iedName){
		if (Constants.XQUERY){
			String xQuery = "for $aps in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED[@name='"+iedName+"']/scl:AccessPoint return $aps "; //$NON-NLS-1$ //$NON-NLS-2$
			return XMLDBHelper.queryNodes(xQuery);
		} else {
			String xPath = "/SCL/IED[@name='" + iedName + "']/AccessPoint";
			return XMLDBHelper.selectNodes(xPath);
		}
	}
	
	/**
	 * 得到LD集合下所有逻辑节点的lnType与其子节点参引集合的映射。
	 * @return
	 */
	public static Map<String, List<String>> getLnSubRef(List<Element> ldList) {
		Map<String, List<String>> subRefMap = new HashMap<String, List<String>>();
		for (Element ldNode : ldList) {
			List<?> lnList = ldNode.selectNodes("./*[name()='LN0' or name()='LN']");
			String ldInst = ldNode.attributeValue("inst");
			for (Object obj : lnList) {
				Element lnNode = (Element) obj;
				String lnName = SCL.getLnName(lnNode);
				String lnType = lnNode.attributeValue("lnType");
				List<String> subRefs = new ArrayList<String>();
				subRefMap.put(ldInst + "/" + lnName + ":" + lnType, subRefs);
				List<?> doiList = lnNode.elements("DOI");
				for (Object obj1 : doiList) {
					Element doi = (Element) obj1;
					String name = doi.attributeValue("name");
					addSubRefs(name, doi, subRefs);
				}
			}
		}
		return subRefMap;
	}
	
	/**
	 * 添加DO及其子节点参引。
	 * @param parentName
	 * @param parentNode
	 * @param subRefs
	 */
	public static void addSubRefs(String parentName, Element parentNode, List<String> subRefs) {
		List<Element> children = DOM4JNodeHelper.selectNodes(parentNode, "./*[name()='SDI' or name()='DAI']");
		if (children.size() == 0) {
			subRefs.add(parentName);
		} else {
			for (Element child : children) {
				String nodeName = child.getName();
				String name = child.attributeValue("name");
				if (nodeName.equals("DAI")) {
					subRefs.add(parentName + Constants.DOT + name);
				} else {
					addSubRefs(parentName + Constants.DOT + name, child, subRefs);
				}
			}
		}
	}
	
	/**
	 * 删除IED
	 * @param name
	 */
	public static void deleteIED(String name) {
		// 删除IED部分
		String IEDPath = "/scl:SCL/scl:IED[@name='" + name + "']"; //$NON-NLS-1$ //$NON-NLS-2$
		XMLDBHelper.removeNodes(IEDPath);
		// 删除数据模板部分,仅处理模板中一个IED的，需要删除整个模板信息
		List<Element> list = XMLDBHelper.selectNodesOnlyAtts(SCL.XPATH_IED, "IED");
		if (list.size() == 0) {// 说明只导入了一个IED此时删除数据模板下的所有元素
			XMLDBHelper.removeNodes("/scl:SCL/scl:DataTypeTemplates/*"); //$NON-NLS-1$
		}
		HistoryManager manager = HistoryManager.getInstance();
		manager.markDevChanged(DevType.IED, OperType.DELETE, name, null);
	}
	
	/**
	 * 删除Inputs及可能存在的<IEDName/>ClientLN/>标签
	 * @param name
	 */
	public static void deleteInputs(String name) {
		List<Element> allied = getALLIED();
		for (Element iedEl : allied) {
			String iedName = iedEl.attributeValue("name");
			String inputPath = "/scl:SCL/scl:IED[@name='" //$NON-NLS-1$
					+ iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice//scl:ExtRef[@iedName='" //$NON-NLS-1$
					+ name + "']"; //$NON-NLS-1$
			String reportPath = "/scl:SCL/scl:IED[@name='" //$NON-NLS-1$
					+ iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:ReportControl/scl:RptEnabled/scl:ClientLN[@iedName='" //$NON-NLS-1$
					+ name + "']"; //$NON-NLS-1$
			String sampPath = "/scl:SCL/scl:IED[@name='" //$NON-NLS-1$
					+ iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:SampledValueControl[IEDName='" //$NON-NLS-1$
					+ name + "']"; //$NON-NLS-1$

			XMLDBHelper.removeNodes(inputPath);
			XMLDBHelper.removeNodes(reportPath);
			Element selectNodes = XMLDBHelper.selectSingleNode(sampPath);
			if (selectNodes != null) {
				List<?> elements = selectNodes.elements("IEDName"); //$NON-NLS-1$
				for (int i = elements.size() - 1; i > -1; i--) {
					Element element = (Element) elements.get(i);
					String text = element.getText();
					if (text.equals(name)) {
						elements.remove(element);
					}
				}
				XMLDBHelper.replaceNode(sampPath, selectNodes);	// 删除可能存在的
			}
		}
	}
	
	public static void deleteInputs(String name, List<String> inputsIeds, List<String> reportIeds, List<String> sampIeds) {
		for (String iedName : inputsIeds) {
			String inputPath = "/scl:SCL/scl:IED[@name='" + iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice//scl:ExtRef[@iedName='" + name + "']";
			XMLDBHelper.removeNodes(inputPath);
		}
		for (String iedName : reportIeds) {
			String reportPath = "/scl:SCL/scl:IED[@name='" + iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:ReportControl/scl:RptEnabled/scl:ClientLN[@iedName='" //$NON-NLS-1$
					+ name + "']"; //$NON-NLS-1$
			XMLDBHelper.removeNodes(reportPath);
		}
		for (String iedName : sampIeds) {
			String sampPath = "/scl:SCL/scl:IED[@name='" + iedName
					+ "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:SampledValueControl[IEDName='" //$NON-NLS-1$
					+ name + "']"; //$NON-NLS-1$
			Element selectNodes = XMLDBHelper.selectSingleNode(sampPath);
			if (selectNodes != null) {
				List<?> elements = selectNodes.elements("IEDName"); //$NON-NLS-1$
				for (int i = elements.size() - 1; i > -1; i--) {
					Element element = (Element) elements.get(i);
					String text = element.getText();
					if (text.equals(name)) {
						elements.remove(element);
					}
				}
				XMLDBHelper.replaceNode(sampPath, selectNodes);	// 删除可能存在的
			}
		}
	}

	/**
	 * 删除通讯（替换装置时，不能删除物理端口信息）
	 * @param name
	 * @param keepStub
	 */
	public static void deleteComm(String name, boolean keepStub) {
		// 删除通信部分
		String conAPPath = SCL.XPATH_SUBNETWORK + "/scl:ConnectedAP[@iedName='" + name + "']"; //$NON-NLS-1$
		if (keepStub) {
			conAPPath += "/*[name()='Address' or name()='GSE' or name()='SMV']"; //$NON-NLS-1$
		}
		XMLDBHelper.removeNodes(conAPPath);
	}
	
	/**
	 * 获取IED更新属性
	 * 
	 * @param newIEDName 新的IED名称
	 * @param oldIEDName 旧的IED名称
	 * @param attr 属性
	 * @return
	 */
	public static String getNewAttr(String newIEDName, String oldIEDName, String attr) {
		if (attr.startsWith(oldIEDName) && !(attr.startsWith(newIEDName) && newIEDName.startsWith(oldIEDName))) {
			return newIEDName + attr.substring(oldIEDName.length());
		} else if (attr.startsWith(newIEDName)) {
			return attr;
		} else {
			return newIEDName + attr;
		}
	}
}