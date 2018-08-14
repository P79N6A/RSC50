/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */

package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.beans.BeanFactory;
import com.shrcn.business.scl.beans.GSEConfigModel;
import com.shrcn.business.scl.beans.MMSConfigModel;
import com.shrcn.business.scl.beans.PhysConn;
import com.shrcn.business.scl.beans.SMVConfigModel;
import com.shrcn.business.scl.beans.SubNetModel;
import com.shrcn.business.scl.enums.EnumCommType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.CommunicationUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.dbxapi.io.ScdSaxParser;
import com.shrcn.found.dbxapi.io.ScdSection;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * Add one sentence class summary here. Add class description here.
 * 
 * @author 黄钦辉
 * @version 1.0, 2009-4-22
 */
public class CommunicationDAO {
	/* 缺省值 */
	private static final SCTProperties property = SCTProperties.getInstance();
	private static final String qualifer = "33"; //$NON-NLS-1$
//	private static final String psel = "00 00 00 01"; //$NON-NLS-1$
	private static final String ssel = "0001"; //$NON-NLS-1$
//	private static final String tsel = "00 01"; //$NON-NLS-1$
	private static final String osiAp = "1,3,9999,33"; //$NON-NLS-1$
	/* 缺省值 结束 */

	private List<?> items = new LinkedList<Object>();
	private String subNetworkName;
	protected Map<String, List<String[]>> iedNameGSEMap = new LinkedHashMap<String, List<String[]>>();
	protected Map<String, List<String[]>> iedNameSMVMap = new LinkedHashMap<String, List<String[]>>();
	protected Map<String, List<String[]>> iedNameMMSMap = new LinkedHashMap<String, List<String[]>>();
	protected List<String> gseIEDs = new ArrayList<String>();
	protected List<String> gseList = new ArrayList<String>();
	protected List<String> smvIEDs = new ArrayList<String>();
	protected List<String> smvList = new ArrayList<String>();
	protected List<String> mmsIEDs = new ArrayList<String>();
	protected List<String> mmsList = new ArrayList<String>();
	public static boolean isCancled = false;
	public static final String GOOSETYPE = "GOOSE";
	public static final String SMVTYPE = "SMV";
	public static final String MMSTYPE = "MMS";
	public static final String DBMMSTYPE = "MMS S2";
	private String iedName, apName;
	private String apXPath;
	private Element subNetWork;
	
	/**
	 * 构造函数
	 * @param name
	 */
	public CommunicationDAO(String subNetworkName) {
		this.subNetworkName = subNetworkName;
	}

	/**
	 * 刷新IED及其访问点
	 * @param iedName
	 * @param apName
	 */
	public void refreshIEDAp(String iedName, String apName) {
		this.iedName = iedName;
		this.apName = apName;
		apXPath = "/scl:SCL/scl:Communication/scl:SubNetwork[@name='"
				+ subNetworkName + "']/scl:ConnectedAP[@iedName='" + iedName
				+ "'][@apName='" + apName + "']";
	}
	
	/**
	 * 更新MMS地址参数
	 * @param type
	 * @param newValue
	 */
	public void updateAddr(String type, String newValue) {
		String addrXPath = apXPath + "/scl:Address"; //$NON-NLS-1$
		String updatepath = addrXPath + "/scl:P[@type='" + type + "']"; //$NON-NLS-1$
		boolean exist = XMLDBHelper.existsNode(updatepath);
		if (exist) {
			XMLDBHelper.update(updatepath, newValue);
		} else {
			Element root = createP(type, newValue);
			XMLDBHelper.insert(addrXPath, root);
		}
	}

	/**
	 * 保存goose配置
	 * @param type
	 * @param newValue
	 * @param attrName
	 * @param cbName
	 * @param ldInst
	 */
	public void updateGseOrSMV(String type, String newValue, String attrName,
			String cbName, String ldInst) {
		String addrXPath = apXPath + "/scl:" + attrName + "[@cbName='" + cbName
				+ "'][@ldInst='" + ldInst + "']/scl:Address";
		String updatepath = addrXPath + "/scl:P[@type='" + type + "']";

		boolean exist = XMLDBHelper.existsNode(updatepath);

		if (exist) {
			XMLDBHelper.update(updatepath, newValue);
		} else {
			Element root = createP(type, newValue);
			XMLDBHelper.insert(addrXPath, root);
		}
	}

	/**
	 * 创建<P>节点
	 * @param type
	 * @return
	 */
	private Element createP(String type, String value) {
		Element root = DOM4JNodeHelper.createSCLNode("P"); //$NON-NLS-1$
		root.setText(value);
		return root.addAttribute("type", type); //$NON-NLS-1$
	}

	/**
	 * 加载数据
	 * @param name
	 * @return
	 */
	public List<?> loadData(String name) {
		items.clear();
		items = loadComms(name);
		return items;
	}
	
	/**
	 * 加载通信配置信息
	 * @param name
	 * @return
	 */
	public static List<Element> loadComms(String name) {
		String path = "/scl:SCL/scl:Communication/scl:SubNetwork[@name='"
			+ name + "']/scl:ConnectedAP";
		return XMLDBHelper.selectNodes(path);
	}

	/**
	 * 得到当前连接点通信配置信息
	 * @return
	 */
	public List<?> getItems() {
		if (items.size() == 0) {
			// 重新导入数据
			if (Constants.FINISH_FLAG) {
				return loadData(subNetworkName);
			} else {
				return DOM4JNodeHelper.selectSingleNode(ScdSaxParser.getInstance().getPart(ScdSection.Communication), 
						"./*[@name='" + subNetworkName + "']").elements();
			}
		}
		return items;
	}
	
	/**
	 * 重新加载通信配置
	 * @return
	 */
	public List<?> reload() {
		loadData(subNetworkName);
		return items;
	}

	/**
	 * 得到goose配置信息
	 * @return
	 */
	public List<String> getGSEItems() {
		gseList.clear();
		List<Element> gseAlls = getGSEControls();
		for (Element gse : gseAlls) {
			String iedName = (String) gse.attributeValue("iedName");
			String apName = (String) gse.attributeValue("apName");
			String ldInst = (String) gse.attributeValue("ldInst");
			String gseName = (String) gse.attributeValue("name");
			gseList.add(iedName + "." + apName + "." + gseName + "." + ldInst);
		}
		getDelopyGse(gseList, subNetworkName);// 过滤
		
		return gseList;
	}
	
	/**
	 * 得到goose配置信息
	 * @return
	 */
	public List<String> getGSECfgItems() {
		List<Element> gseAlls = getGSEControls();
		for (Element gse : gseAlls) {
			String iedName = (String) gse.attributeValue("iedName");
			String apName = (String) gse.attributeValue("apName");
			String appID = (String) gse.attributeValue("appID");
			String gseName = (String) gse.attributeValue("name");
			gseList.add(iedName + "." + apName + "." + gseName + "." + appID);
		}
		
		return gseList;
	}

	/**
	 * 得到goose IED名称信息
	 * @param list
	 * @return
	 */
	public List<String> getGseIEDNames(List<String> list) {
		iedNameGSEMap.clear();
		gseIEDs.clear();
		List<String[]> ls = null;

		for (String name : list) {
			String[] split = name.split("\\.");
			String iedName = split[0];
			if (!iedNameGSEMap.containsKey(iedName)) {
				ls = new ArrayList<String[]>();
				iedNameGSEMap.put(iedName, ls);
			}
			ls.add(split);
			if (!gseIEDs.contains(iedName)) {
				gseIEDs.add(iedName);
			}
		}
		return gseIEDs;
	}

	/**
	 * 过滤已经配置的控制块
	 * @param list
	 * @param subnetName
	 */
	private void getDelopyGse(List<String> list, String subNetworkName) {
		getDelopyCB(list, subNetworkName, "GSE");
	}
	
	/**
	 * 过滤控制块
	 * 
	 * @param list
	 * @param subNetworkName
	 * @param conCbName
	 */
	private void getDelopyCB(List<String> list, String subNetworkName, String conCbName){
		String subnetPath = SCL.XPATH_SUBNETWORK + "/@name";
		List<String> subnetNames = XMLDBHelper.getAttributeValues(subnetPath);
		for (String subnetName : subnetNames) {
			String subNetPa = SCL.XPATH_SUBNETWORK + "[@name='" + subnetName + "']";
			List<Element> selectNodes = XMLDBHelper.selectNodes(subNetPa);
			for (Element e : selectNodes) {
				List<?> elements = e.elements("ConnectedAP");
				for (Object obj : elements) {
					Element ele = (Element) obj;
					String name = ele.attributeValue("iedName");
					String appName = ele.attributeValue("apName");
					List<?> gse = ele.elements(conCbName);// 此装置下的goose控制块已经配置，则过滤
					for (Object obj1 : gse) {
						Element gseE = (Element) obj1;
						String cbName = gseE.attributeValue("cbName");
						String ldInst = gseE.attributeValue("ldInst");
						String allName = name + "." + appName + "." + cbName + "." + ldInst;
						if (list.contains(allName)) {
							list.remove(allName);
						}
					}
					if (!subnetName.equals(subNetworkName)) {// 其他子网已经配置了某装置goose控制块，则此子网不能配置此装置的控制块，过滤
						String gsePath = "./scl:ConnectedAP[@iedName='"
								+ name + "'][count(./scl:" + conCbName + ")>0]/@apName";
						List<String> apNames = DOM4JNodeHelper.getAttributeValues(e, gsePath);
						if (apNames.size() < 1)
							continue;
						for (int i = list.size() - 1; i >= 0; i--) {
							String string = list.get(i);
							String[] split = string.split("\\.");
							if (split[0].equals(name) && apNames.contains(split[1])) {
								list.remove(string);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 查询当前SCD下所有装置的访问点信息
	 * @return
	 */
	public List<Element> getMMSControls() {
		if (Constants.XQUERY) {
			String xquery = "let $ieds:=" + XMLDBHelper.getDocXPath() + 
				"/SCL/IED for $ied in $ieds for $ap in $ied/AccessPoint where(exists($ap/scl:Server/scl:LDevice/*/scl:ReportControl) or $ap/@name='S1' or $ap/@name='S2') " +
				"return <IED iedName='{$ied/@name}' apName='{$ap/@name}' />";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> iedAps = new ArrayList<>();
			List<Element> ieds = XMLDBHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
			for (Element ied : ieds) {
				String iedName = ied.attributeValue("name");
				List<Element> aps = XMLDBHelper.selectNodesOnlyAtts("/SCL/IED[@name='" + iedName +
						"']/AccessPoint[count(./scl:Server/scl:LDevice/*/scl:ReportControl)>0 or @name='S1' or @name='S2']", "AccessPoint");
				for (Element ap : aps) {
					Element iedAp = DOM4JNodeHelper.createSCLNode("IED");
					iedAp.addAttribute("iedName", iedName);
					iedAp.addAttribute("apName", ap.attributeValue("name"));
					iedAps.add(iedAp);
				}
			}
			return iedAps;
		}
	}

	/**
	 * 查询GSEControl标签
	 * @param name
	 * @return
	 */
	public List<Element> getGSEControls() {
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED return "
					+ "for $ap in $ied/scl:AccessPoint return for $ld in $ap/scl:Server/scl:LDevice "
					+ "return for $gse in $ld/scl:LN0/scl:GSEControl "
					+ "return <GSEControl iedName='{$ied/@name}' apName='{$ap/@name}' ldInst='{$ld/@inst}' name='{$gse/@name}' appID='{$gse/@appID}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> iedGses = new ArrayList<>();
			List<Element> ieds = XMLDBHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
			for (Element ied : ieds) {
				String iedName = ied.attributeValue("name");
				List<Element> aps = XMLDBHelper.selectNodes("/SCL/IED[@name='" + iedName + "']/AccessPoint");
				for (Element ap : aps) {
					List<Element> lds = DOM4JNodeHelper.selectNodes(ap, "./scl:Server/scl:LDevice");
					for (Element ld : lds) {
						String ldInst = ld.attributeValue("inst");
						List<Element> gses = DOM4JNodeHelper.selectNodes(ld, "./scl:LN0/scl:GSEControl");
						for (Element gse : gses) {
							String gseName = gse.attributeValue("name");
							String appID = gse.attributeValue("appID");
							Element iedGse = DOM4JNodeHelper.createSCLNode("GSEControl");
							iedGse.addAttribute("iedName", iedName);
							iedGse.addAttribute("apName", ap.attributeValue("name"));
							iedGse.addAttribute("ldInst", ldInst);
							iedGse.addAttribute("name", gseName);
							iedGse.addAttribute("appID", appID);
							iedGses.add(iedGse);
						}
					}
				}
			}
			return iedGses;
		}
	}

	/**
	 * 得到<SampledValueControl>信息
	 * @param name
	 * @return
	 */
	private List<Element> getSMVControls() {
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED return "
					+ "for $ap in $ied/scl:AccessPoint return for $ld in $ap/scl:Server/scl:LDevice "
					+ "return for $smv in $ld/scl:LN0/scl:SampledValueControl "
					+ "return <SampledValueControl iedName='{$ied/@name}' apName='{$ap/@name}' ldInst='{$ld/@inst}' name='{$smv/@name}' smvID='{$smv/@smvID}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> iedSmvs = new ArrayList<>();
			List<Element> ieds = XMLDBHelper.selectNodesOnlyAtts("/SCL/IED", "IED");
			for (Element ied : ieds) {
				String iedName = ied.attributeValue("name");
				List<Element> aps = XMLDBHelper.selectNodes("/SCL/IED[@name='" + iedName + "']/AccessPoint");
				for (Element ap : aps) {
					List<Element> lds = DOM4JNodeHelper.selectNodes(ap, "./scl:Server/scl:LDevice");
					for (Element ld : lds) {
						String ldInst = ld.attributeValue("inst");
						List<Element> smvs = DOM4JNodeHelper.selectNodes(ld, "./scl:LN0/scl:SampledValueControl");
						for (Element smv : smvs) {
							String smvName = smv.attributeValue("name");
							String smvID = smv.attributeValue("smvID");
							Element iedSmv = DOM4JNodeHelper.createSCLNode("SampledValueControl");
							iedSmv.addAttribute("iedName", iedName);
							iedSmv.addAttribute("apName", ap.attributeValue("name"));
							iedSmv.addAttribute("ldInst", ldInst);
							iedSmv.addAttribute("name", smvName);
							iedSmv.addAttribute("smvID", smvID);
							iedSmvs.add(iedSmv);
						}
					}
				}
			}
			return iedSmvs;
		}
	}

	/**
	 * 得到smv信息
	 * @return
	 */
	public List<String> getSMVSItems() {
		smvList.clear();
		List<Element> gseAlls = getSMVControls();
		for (Element gse : gseAlls) {
			String iedName = (String) gse.attributeValue("iedName");
			String apName = (String) gse.attributeValue("apName");
			String ldInst = (String) gse.attributeValue("ldInst");
			String gseName = (String) gse.attributeValue("name");
			smvList.add(iedName + "." + apName + "." + gseName + "." + ldInst);

		}
		getDelopySMV(smvList, subNetworkName);// 过滤装置
		return smvList;
	}
	
	/**
	 * 得到smv信息
	 * @return
	 */
	public List<String> getSMVCfgItems() {
		List<String> smvList = new ArrayList<String>();
		List<Element> gseAlls = getSMVControls();
		for (Element gse : gseAlls) {
			String iedName = (String) gse.attributeValue("iedName");
			String apName = (String) gse.attributeValue("apName");
			String smvID = (String) gse.attributeValue("smvID");
			String gseName = (String) gse.attributeValue("name");
			smvList.add(iedName + "." + apName + "." + gseName + "." + smvID);
			
		}
		return smvList;
	}

	/**
	 * 过滤已经配置的smv控制块
	 * @param list
	 * @param subnetName
	 */
	private void getDelopySMV(List<String> list, String subNetworkName) {
		getDelopyCB(list, subNetworkName, "SMV");
	}
	
	/**
	 * 得到smv IED名称信息
	 * @param list
	 * @return
	 */
	public List<String> getSmvsIEDNames(List<String> list) {
		iedNameSMVMap.clear();
		smvIEDs.clear();
		List<String[]> ls = null;
		for (String name : list) {
			String[] split = name.split("\\.");
			String iedName = split[0];
			if (!iedNameSMVMap.containsKey(iedName)) {
				ls = new ArrayList<String[]>();
				iedNameSMVMap.put(iedName, ls);
			}
			ls.add(split);
			if (!smvIEDs.contains(iedName)) {
				smvIEDs.add(iedName);
			}
		}
		return smvIEDs;
	}

	/**
	 * 得到MMS配置信息（如果一个IED的访问点下存在报告控制块，则此访问点才能配置mms）。
	 * @return
	 */
	public List<String> getMMSItems() {
		mmsList.clear();
		List<Element> mmsControns = getMMSControls();
		String select = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[count(./scl:Address)>0]/@iedName";
		List<String> deployedIEDs = XMLDBHelper.getAttributeValues(select);
		for (Element mms : mmsControns) {
			String iedName = (String) mms.attributeValue("iedName");
			boolean existsNode = deployedIEDs.contains(iedName);
			if (!existsNode) {
				String apName = (String) mms.attributeValue("apName");
				mmsList.add(iedName + "." + apName);
			}
		}
		return mmsList;
	}
	
	public List<String> getDbNetMMSItems(){
		List<Element> mmsControls = getMMSControls();
		mmsList.clear();
		for (Element mms : mmsControls) {
			String iedName = (String) mms.attributeValue("iedName");
			mmsList.add(iedName + "." + "S2");
		}
		
		List<Element> mmsAPList = getMmsAPList();
		Set<String> s1Ieds = new HashSet<String>();
		Set<String> s2Ieds = new HashSet<String>();
		
		for (Element el : mmsAPList) {
			String iedName = el.attributeValue("iedName");
			String apName = el.attributeValue("apName");
			
			if ("S1".equals(apName)) {
				s1Ieds.add(iedName);
			} else if ("S2".equals(apName)) {
				s2Ieds.add(iedName);
			}
		}
		
		// 获取配置了S1、没有配置S2的IED
		for (int i = mmsList.size() - 1; i >= 0; i--) {
			String key = mmsList.get(i);
			String[] split = key.split("\\.");
			String iedName = split[0]; 
			
			if (!s1Ieds.contains(iedName)) {
				mmsList.remove(i);
			} else {
				if (s2Ieds.contains(iedName)) {
					mmsList.remove(i);
				}
			}
		}
		
		return mmsList;
	}
	/**
	 * 过滤mms
	 * 
	 * @param list
	 */
	private List<Element> getMmsAPList() {
		List<Element> mmsApList = new ArrayList<Element>();
		Element elComms = XMLDBHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		
		List<?> subnetWorks = elComms.elements("SubNetwork");
		for (Object subNetWork : subnetWorks) {
			List<?> apEls = ((Element)subNetWork).elements("ConnectedAP");
			for (Object obj : apEls) {
				Element apEl = (Element) obj;
				Element addressEl = apEl.element("Address");
				if (addressEl != null) {
					mmsApList.add(apEl);
				}
			}
		}
		return mmsApList;
	}

	/**
	 * 得到MMS的所有IED名称信息
	 * @param list
	 * @return
	 */
	public List<String> getMmsIEDNames(List<String> list) {
		iedNameMMSMap.clear();
		mmsIEDs.clear();
		List<String[]> ls = null;
		for (String name : list) {
			String[] split = name.split("\\.");
			String iedName = split[0];
			if (!iedNameMMSMap.containsKey(iedName)) {
				ls = new ArrayList<String[]>();
				iedNameMMSMap.put(iedName, ls);
			}
			ls.add(split);
			if (!mmsIEDs.contains(iedName)) {
				mmsIEDs.add(iedName);
			}
		}
		return mmsIEDs;
	}

	public Map<String, List<String[]>> getIedNameGSEMap() {
		return iedNameGSEMap;
	}

	public Map<String, List<String[]>> getIedNameSMVMap() {
		return iedNameSMVMap;
	}

	public Map<String, List<String[]>> getIedNameMMSMap() {
		return iedNameMMSMap;
	}

	public List<String> getGseList() {
		return gseList;
	}

	public List<String> getSmvList() {
		return smvList;
	}

	public List<String> getMmsList() {
		return mmsList;
	}

	/**
	 * 判断当前连接点下是否存在<Address>
	 * @return 存在返回true，否则返回false
	 */
	public boolean existAddress() {
		String addrXPath = "./scl:ConnectedAP[@iedName='" + iedName
				+ "'][@apName='" + apName + "']/scl:Address";
		return DOM4JNodeHelper.existsNode(subNetWork, addrXPath);
	}

	/**
	 * 判断当前连接点是否存在
	 * @return 存在返回true，否则返回false
	 */
	public boolean existConnectedAP() {
		String apXPath = "./scl:ConnectedAP[@iedName='" + iedName
			+ "'][@apName='" + apName + "']";
		return DOM4JNodeHelper.existsNode(subNetWork, apXPath);
	}

	/**
	 * 插入连接点<ConnectedAP>
	 */
	public void insertConnectedAP() {
		Element ap = DOM4JNodeHelper.createSCLNode("ConnectedAP"); //$NON-NLS-1$
		ap.addAttribute("iedName", iedName); //$NON-NLS-1$
		ap.addAttribute("apName", apName); //$NON-NLS-1$
		subNetWork.add(ap);
	}

	/**
	 * 向当前连接点下添加<Address>
	 * @param ip
	 * @param sub
	 */
	@SuppressWarnings("unchecked")
	public void insertAddr(String ip, String sub) {
		Element mmsAdress = DOM4JNodeHelper.createSCLNode("Address");
		initAddressInfo(ip, sub, mmsAdress);
		String path = "./*[name()='ConnectedAP'][@iedName='" + iedName
		+ "'][@apName='" + apName + "']";
	    Element connectedAP = DOM4JNodeHelper.selectSingleNode(subNetWork, path);
	    List<Element> childs = connectedAP.elements();
	    childs.add(0, mmsAdress);
	}

	/**
	 * 为MMS设置默认值
	 * @param ip
	 * @param sub
	 * @param addressE
	 * @return
	 */
	private static void initAddressInfo(String ip, String sub, Element addressE) {
		String gateway = ip.substring(0, ip.lastIndexOf('.')) + ".1";
		addressE.addElement("P").addAttribute("type", "OSI-AP-Title").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				osiAp);
		addressE.addElement("P").addAttribute("type", "OSI-AE-Qualifier") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				.setText(qualifer);
		addressE.addElement("P").addAttribute("type", "OSI-PSEL").setText(property.getMmsPSel()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addressE.addElement("P").addAttribute("type", "OSI-SSEL").setText(ssel); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addressE.addElement("P").addAttribute("type", "OSI-TSEL").setText(property.getMmsTSel()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addressE.addElement("P").addAttribute("type", "IP").setText(ip); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addressE.addElement("P").addAttribute("type", "IP-SUBNET").setText(sub); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		addressE.addElement("P").addAttribute("type", "IP-GATEWAY").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				gateway);
	}

	/**
	 * 保存goose配置中MaxTime,MinTime配置参数
	 * @param cbName
	 * @param ldInst
	 * @param timeType
	 * @param newValue
	 */
	public void updateGooseTime(String cbName, String ldInst, String timeType,
			String newValue) {
		String updatepath = apXPath
				+ "/scl:GSE[@cbName='" + cbName + "'][@ldInst='" + ldInst //$NON-NLS-1$ //$NON-NLS-2$
				+ "']/scl:" + timeType; //$NON-NLS-1$
		if (XMLDBHelper.existsNode(updatepath)) {
			XMLDBHelper.update(updatepath, newValue);
		} else {
			String insertPath = apXPath + "/scl:GSE[@cbName='" + cbName
					+ "'][@ldInst='" + ldInst + "']";
			Element root = DOM4JNodeHelper.createSCLNode(timeType);
			root.setText(newValue);
			XMLDBHelper.insertAsLast(insertPath, root);
		}
	}
	
	/**
	 * 清除goose，smv配置信息
	 * @param attrName
	 */
	public void clearGooseOrSVM(String attrName) {
		String gsePath = "./*[name()='ConnectedAP']/*[name()='"+ attrName +"']";
		DOM4JNodeHelper.deleteNodes(subNetWork, gsePath);
	}

	/**
	 * 保存goose，smv配置信息
	 * @param attrName
	 * @param goose
	 */
	@SuppressWarnings("unchecked")
	public void addGooseOrSVM(String attrName, Goose goose) {
		Element gseEle = createElement(attrName, goose);
		String path = "./*[name()='ConnectedAP'][@iedName='" + iedName
			+ "'][@apName='" + apName + "']";
		Element connectedAP = DOM4JNodeHelper.selectSingleNode(subNetWork, path);
		if (connectedAP == null) {
			insertConnectedAP();
			connectedAP = DOM4JNodeHelper.selectSingleNode(subNetWork, path);
		}
		String smvPath = path + "/*[name()='"+ SMVTYPE +"']";
		boolean smvExist = DOM4JNodeHelper.existsNode(subNetWork, smvPath);
		if (smvExist && "GSE".equals(attrName)) { // add goose
			List<Element> childs = connectedAP.elements();
			int num = 0;
			for (int i = 0; i < childs.size(); i++) {
				String name = childs.get(i).getName();
				if (name != null && name.equals(SMVTYPE)) {
					num = i;
					break;
				}
			}
			childs.add(num, gseEle);
		} else {								// add smv
			connectedAP.add(gseEle);
		}
	}

	/**
	 * 创建goose XML节点
	 * @param attributeName
	 * @param goose
	 * @return
	 */
	private Element createElement(String attributeName, Goose goose) {
		Element root = DOM4JNodeHelper.createSCLNode(attributeName);
		root.addAttribute("cbName", goose.getCbName()); //$NON-NLS-1$
		root.addAttribute("ldInst", goose.getLdInst()); //$NON-NLS-1$
		Element addressE = root.addElement("Address"); //$NON-NLS-1$
		addressE.addElement("P").addAttribute("type", "MAC-Address").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				goose.getMac());
		addressE.addElement("P").addAttribute("type", "VLAN-ID").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				goose.getVlanId());
		addressE.addElement("P").addAttribute("type", "VLAN-PRIORITY").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				goose.getVlanP());
		addressE.addElement("P").addAttribute("type", "APPID").setText( //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				goose.getAppId());
		if ("GSE".equals(attributeName)) { //$NON-NLS-1$
			Element minEle = root.addElement("MinTime"); //$NON-NLS-1$
			minEle.addAttribute("unit", "s"); //$NON-NLS-1$ //$NON-NLS-2$
			minEle.addAttribute("multiplier", "m"); //$NON-NLS-1$ //$NON-NLS-2$
			minEle.setText(goose.getMin());
			Element maxEle = root.addElement("MaxTime"); //$NON-NLS-1$
			maxEle.addAttribute("unit", "s"); //$NON-NLS-1$ //$NON-NLS-2$
			maxEle.addAttribute("multiplier", "m"); //$NON-NLS-1$ //$NON-NLS-2$
			maxEle.setText(goose.getMax());
		}
		return root;
	}

	/**
	 * 删除goose或者smv
	 * @param attributeName
	 * @param goose
	 */
	public void delGooseOrSMV(String attributeName, Goose goose) {
		String comPath = apXPath + "/*";
		int comNum = XMLDBHelper.countNodes(comPath);
		String deletePath = apXPath + "/scl:" + attributeName + "[@cbName='"
				+ goose.getCbName() + "'][@ldInst='" + goose.getLdInst() + "']";
		if (comNum == 1 && XMLDBHelper.existsNode(deletePath)) {	// 此ConnectedAP下只有一个GSE或者SMV
			deletePath = apXPath;		// 直接删除ConnectedAP标签
		}
		deleteDB(deletePath);
//		markDeleteHistory(iedName, apName);
	}
	
    /**
     * 删除所有的SMV或GSE或IP
     * @param attributeName
     */
    public void delAll(String attributeName){
    	String connectedPath = SCL.XPATH_SUBNETWORK + "[@name='" + subNetworkName
			+ "']/*[name()='ConnectedAP']";
    	//MMS通信配置节点名称为"Address",单独处理
    	attributeName = attributeName.equals("MMS")? "Address" : attributeName;
    	XMLDBHelper.removeNodes(connectedPath + "/*[name()='" + attributeName  +"']");
    	//若ConnectedAP节点下不存在子节点，删除该节点
    	XMLDBHelper.removeNodes(connectedPath + "[not(node())]");
    }	
    
	/**
	 * 删除数据库
	 * @param deletePath
	 */
	public synchronized void deleteDB(final String deletePath) {
		try {
			XMLDBHelper.removeNodes(deletePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有子网名称
	 * @return
	 */
	public static List<String> getSubNets() {
		return XMLDBHelper.getAttributeValues(SCL.XPATH_SUBNETWORK + "/@name");
	}
	
	/**
	 * 查询变电站通信配置。
	 * @return
	 */
	public static List<SubNetModel> getCommInfo() {
		List<SubNetModel> subNets = new ArrayList<SubNetModel>();
		List<String> subNetNames = CommunicationDAO.getSubNets();
		Element commEl = XMLDBHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		// 分别导出每个子网的MMS,GSE,SMV配置
		for (String subNetName : subNetNames) {
			SubNetModel subNet = new SubNetModel(subNetName);
			subNets.add(subNet);
			List<?> connAps = DOM4JNodeHelper.selectNodes(commEl, "./scl:SubNetwork[@name='"
					+ subNetName + "']/scl:ConnectedAP");
			for (Object obj : connAps) {
				Element cpEle = (Element) obj;
				Element addrEle = cpEle.element("Address");
				List<?> gseEles = cpEle.elements("GSE");
				List<?> smvEles = cpEle.elements("SMV");
				if (cpEle != null && addrEle != null) {
					MMSConfigModel datasetMMS = BeanFactory.createMMS(cpEle);
					subNet.getMmsCMS().add(datasetMMS);
				}

				if (cpEle != null && gseEles != null) {
					String iedName = cpEle.attributeValue("iedName");
					String apName = cpEle.attributeValue("apName");
					for (Object gseEle : gseEles) {
						GSEConfigModel datasetGSE = BeanFactory.createGSE(
								(Element) gseEle, iedName, apName);
						subNet.getGseCMs().add(datasetGSE);
					}
				}
				if (cpEle != null && smvEles != null) {
					String iedName = cpEle.attributeValue("iedName");
					String apName = cpEle.attributeValue("apName");
					for (Object smvEle : smvEles) {
						SMVConfigModel datasetSMV = BeanFactory.createSMV(
								(Element) smvEle, iedName, apName);
						subNet.getSmvCMs().add(datasetSMV);
					}
				}
			}
		}
		return subNets;
	}
	
	/**
	 * 查询所有子网名称数组
	 * @return
	 */
	public static String[] getSubNetsArray() {
		List<String> subNets = getSubNets();
		return subNets.toArray(new String[subNets.size()]);
	}

	public String getSubNetworkName() {
		return subNetworkName;
	}
	
	/**
	 * 获取类型值
	 * @param addrEle
	 * @param type
	 * @return
	 */
	public static String getTypeValue(Element addrEle, String type) {
		Element ipEle = null;
		if (addrEle != null) {
			ipEle = (Element) addrEle
				.selectSingleNode("./*[name()='P'][@type='" + type + "']");
		}
		return (null != ipEle) ? ipEle.getText() : "";
	}
	
	/**
	 * 创建MMS行数据
	 * @param cpEle
	 * @return
	 */
	private String[] createMMSRowData(Element cpEle, int columnCount, String[] properties) {
		Element addrEle = cpEle.element("Address");
		if (cpEle != null && addrEle != null) {
			String[] row = new String[columnCount];
			row[0] = cpEle.attributeValue("iedName");
			row[1] = cpEle.attributeValue("apName");
			row[2] = getTypeValue(addrEle, "IP");
			row[3] = getTypeValue(addrEle, "IP-SUBNET");
			for (int i = 4; i < columnCount; i++) {
				row[i] = getTypeValue(addrEle, properties[i + 1]);
			}
			return row;
		}
		return null;
	}

	/**
	 * 读xml内容为设置列内容做准备
	 * 
	 * @param iedName
	 * @param appName
	 * @param gse
	 * @return
	 */
	private String[] getCbValues(String iedName, String appName, Element gse,
			EnumCommType commType) {
		if (gse == null)
			return null;
		List<String> lstGse = new ArrayList<String>();
		String min = ""; //$NON-NLS-1$
		String max = ""; //$NON-NLS-1$
		String mac = ""; //$NON-NLS-1$
		String vlanId = ""; //$NON-NLS-1$
		String vanPriority = ""; //$NON-NLS-1$
		String aPpID = ""; //$NON-NLS-1$
		String cbName = gse.attributeValue("cbName"); //$NON-NLS-1$
		String ldInst = gse.attributeValue("ldInst"); //$NON-NLS-1$

		Element addrEle = gse.element("Address");
		if (addrEle != null) {
			mac = getTypeValue(addrEle, "MAC-Address");
			vlanId = getTypeValue(addrEle, "VLAN-ID");
			vanPriority = getTypeValue(addrEle, "VLAN-PRIORITY");
			aPpID = getTypeValue(addrEle, "APPID");
		}

		Element minEle = gse.element("MinTime");
		Element maxEle = gse.element("MaxTime");
		if (minEle != null) {
			min = minEle.getText();
		}
		if (maxEle != null) {
			max = maxEle.getText();
		}

		lstGse.add(iedName);
		lstGse.add(appName);
		lstGse.add(cbName);
		lstGse.add(ldInst);
		lstGse.add(mac);
		lstGse.add(vlanId);
		lstGse.add(vanPriority);
		lstGse.add(aPpID);
		if (EnumCommType.GSE == commType) {
			lstGse.add(min);
			lstGse.add(max);
		}

		return lstGse.toArray(new String[lstGse.size()]);
	}
	
	/**
	 * 获取指定类型的通信数据
	 * @param commType
	 * @param columnCount
	 * @param properties
	 * @return
	 */
	private List<String[]> getCbInfo(EnumCommType commType) {
		String attributeName = commType.name();
		List<String[]> data = new ArrayList<String[]>();
		for (Object obj : items) {
			Element conn = (Element) obj;
			String iedName = conn.attributeValue("iedName"); //$NON-NLS-1$
			String apName = conn.attributeValue("apName"); //$NON-NLS-1$
			List<?> gseEles = conn.elements(attributeName);
			if (gseEles != null) {
				for (Object gse : gseEles) {
					String[] row = getCbValues(iedName, apName, (Element) gse, commType);
					if (row != null)
						data.add(row);
				}
			}
		}
		return data;
	}
	
	/**
	 * 获取当前子网所有访问点的MMS配置
	 * @param columnCount
	 * @param properties
	 * @return
	 */
	private List<String[]> getMMSInfo(int columnCount, String[] properties) {
		List<String[]> data = new ArrayList<String[]>();
		for (Object obj : items) {
			Element cpEle = (Element) obj;
			String[] row = createMMSRowData(cpEle, columnCount, properties);
			if (row != null)
				data.add(row);
		}
		return data;
	}
	
	/**
	 * 获取当前子网所有访问点的指定类型的通信配置
	 * @param commType
	 * @param columCount
	 * @param properties
	 * @return
	 */
	public List<String[]> getCommInfo(EnumCommType commType, int columnCount, String[] properties) {
		switch (commType) {
			case MMS:
				return getMMSInfo(columnCount, properties);
			case GSE:
			case SMV:
				return getCbInfo(commType);
			default:
				break;
		}
		return null;
	}
	
	/**
	 * 保存MMS信息到xmldb
	 * @param subNet
	 * @param iedName
	 * @param apName
	 */
	public static void insertMMS(String subNet, String iedName, String apName) {
		Element connectedAP = DOM4JNodeHelper.createSCLNode("ConnectedAP"); //$NON-NLS-1$
		connectedAP.addAttribute("iedName", iedName); //$NON-NLS-1$
		connectedAP.addAttribute("apName", apName); //$NON-NLS-1$
		String mmsIP = SCTProperties.getInstance().nextMmsIP();
		Element Address = connectedAP.addElement("Address"); //$NON-NLS-1$
		initAddressInfo(mmsIP, SCTProperties.getInstance().getSubnetMask(), Address);
//		initPhsConn(connectedAP.addElement("PhysConn"));
		//将该MMS信息插入相应的SubNetwork下
		XMLDBHelper.insertAsLast("/scl:SCL/scl:Communication/scl:SubNetwork[@name='" + subNet + "']", connectedAP); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@SuppressWarnings("unchecked")
	public static Element insertCommunication(String subNet, String iedName,
			String apName, Element root) {
		Element connectedAP = DOM4JNodeHelper.createSCLNode("ConnectedAP");
		connectedAP.addAttribute("iedName", iedName); 
		connectedAP.addAttribute("apName", apName); 
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String name = ap.attributeValue("name");
			if (name==null || !name.equals(apName)){
				continue;
			}
			List<Element> reports = ap.selectNodes(".//*[name()='ReportControl']");
			if (reports!=null && !reports.isEmpty()){
				insertRpts(connectedAP);
			}
			
			List<Element> gses = ap.selectNodes(".//*[name()='GSEControl']");
			if (gses!=null && !gses.isEmpty()){
				insertGses(gses,connectedAP);
			}

			List<Element> smvs = ap.selectNodes(".//*[name()='SampledValueControl']");
			if (smvs!=null && !smvs.isEmpty()){
				insertSmvs(smvs,connectedAP);
			}
			
			// <PhysConn>
			List<Element> consEle = getConnectApEle(root,apName);
			boolean flag = false;
			for (Element element1 : consEle) {
				List<Element> physEle = element1.elements("PhysConn");
				for (Element phyEle : physEle) {
					if (!flag){
						insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"Connection");
						flag = true;
					} else {
						insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"RedConn");
					}
				}
			}
		}
		return connectedAP;
	}
	
	public static Element insertComm(String subNet, String iedName,
			String apName, Element root) {
		Element connectedAP = DOM4JNodeHelper.createSCLNode("ConnectedAP");
		connectedAP.addAttribute("iedName", iedName); 
		connectedAP.addAttribute("apName", apName); 
		Element communicationEle = root.element("IED");
		List<?> aps = communicationEle.elements("AccessPoint");
		for (Object obj : aps) {
			Element ap = (Element) obj;
			String name = ap.attributeValue("name");
			if (name==null || !name.equals(apName)){
				continue;
			}
			insertRpts(connectedAP);

			// <PhysConn>
			List<Element> consEle = getConnectApEle(root, apName);
			boolean flag = false;
			for (Element element1 : consEle) {
				List<Element> physEle = element1.elements("PhysConn");
				for (Element phyEle : physEle) {
					if (!flag){
						insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"Connection");
						flag = true;
					} else {
						insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"RedConn");
					}
				}
			}
		}
		return connectedAP;
	}

	private static void insertSmvs(List<Element> smvs, Element connectedAP) {
		for (Element element : smvs) {
			Element gse = connectedAP.addElement("SMV");
			gse.addAttribute("cbName", element.attributeValue("name"));
			String ldinst = element.getParent().getParent().attributeValue("inst");
			gse.addAttribute("ldInst", ldinst);
			Element addr = gse.addElement("Address");
			Element p1 = addr.addElement("P");
			p1.addAttribute("type", "MAC-Address");
			String mac = SCTProperties.getInstance().nextSmvMac();
			p1.setText(mac);
			Element p2 = addr.addElement("P");
			p2.addAttribute("type", "VLAN-ID");
			p2.setText("000");
			Element p3 = addr.addElement("P");
			p3.addAttribute("type", "VLAN-PRIORITY");
			p3.setText("4");
			Element p4 = addr.addElement("P");
			p4.addAttribute("type", "APPID");
			p4.setText(CommunicationUtil.getAppId(mac));
		}
	}

	private static void insertGses(List<Element> gses, Element connectedAP) {
		for (Element element : gses) {
			Element gse = connectedAP.addElement("GSE");
			gse.addAttribute("cbName", element.attributeValue("name"));
			String ldinst = element.getParent().getParent().attributeValue("inst");
			gse.addAttribute("ldInst", ldinst);
			Element addr = gse.addElement("Address");
			Element p1 = addr.addElement("P");
			p1.addAttribute("type", "MAC-Address");
			String mac = SCTProperties.getInstance().nextGseMac();
			p1.setText(mac);
			Element p2 = addr.addElement("P");
			p2.addAttribute("type", "VLAN-ID");
			p2.setText("000");
			Element p3 = addr.addElement("P");
			p3.addAttribute("type", "VLAN-PRIORITY");
			p3.setText("4");
			Element p4 = addr.addElement("P");
			p4.addAttribute("type", "APPID");
			p4.setText(CommunicationUtil.getAppId(mac));
			Element minTime = gse.addElement("MinTime");
			minTime.addAttribute("unit", "s");
			minTime.addAttribute("multiplier", "m");
			minTime.setText("2");
			Element maxTime = gse.addElement("MaxTime");
			maxTime.addAttribute("unit", "s");
			maxTime.addAttribute("multiplier", "m");
			maxTime.setText("5000");
		}
	}

	private static void insertRpts(Element connectedAP) {
		String mmsIP = SCTProperties.getInstance().nextMmsIP();
		Element address = connectedAP.addElement("Address"); 
		initAddressInfo(mmsIP, SCTProperties.getInstance().getSubnetMask(), address);
	}

	@SuppressWarnings("unchecked")
	private static void insertPysConn(Element phyEle, Element addElement,
			String type) {
		List<Element> subsEle = phyEle.elements("P");
		addElement.addAttribute("type", type);
		for (Element element : subsEle) {
			Element addSubEle = addElement.addElement("P");
			addSubEle.addAttribute("type", element.attributeValue("type"));
			addSubEle.addText(element.getText());
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Element> getConnectApEle(Element root, String apName) {
		List<Element> relList = new ArrayList<Element>();
		Element commuEle = root.element("Communication");
		if (commuEle!=null){
			List<Element> subsEle = commuEle.elements("SubNetwork");
			if (subsEle!=null && !subsEle.isEmpty()){
				for (Element element : subsEle) {
					List<Element> apsEle = element.elements("ConnectedAP");
					for (Element element2 : apsEle) {
						String ap = element2.attributeValue("apName");
						if (ap!=null && ap.equals(apName)){
							relList.add(element2);
						}
					}
					
				}
			}
		} 
		return relList;
	}

	/**
	 * 保存双网访问点S2到xmldb中
	 */
	public static void insertServerAt(String path){
		Element ndS2 = DOM4JNodeHelper.createSCLNode("AccessPoint");
    	ndS2.addAttribute("name", "S2");
    	ndS2.addElement("ServerAt").addAttribute("apName", "S1");
    	
		XMLDBHelper.insertAfter(path, ndS2);
	}
	
	/**
	 * 得到数据库中的通信配置
	 */
	public void getDBSubNet(){
		subNetWork = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBNETWORK + "[@name='" + subNetworkName+ "']");
	}
   
	public Element getSubNetWork() {
		return subNetWork;
	}

	public static Map<String, List<String>> getSubNet(String iedName) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<Element> subnetEls = XMLDBHelper.selectNodes(SCL.XPATH_SUBNETWORK);
		for (Element subnetEl : subnetEls) {
			String subNet = subnetEl.attributeValue("name");
			List<Element> cbEls = DOM4JNodeHelper.selectNodes(subnetEl, "./scl:ConnectedAP[@iedName='" + iedName + "']/*[name()='GSE' or name()='SMV']");
			for(Element el : cbEls){
				String apName = el.getParent().attributeValue("apName");
				String ldInst = el.attributeValue("ldInst");
				String cbName = el.attributeValue("cbName");
				String key = subNet + Constants.COLON + apName;
				if (map.get(key) == null) {
					map.put(key, new ArrayList<String>());
				}
				map.get(key).add(ldInst + Constants.COLON + cbName);
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static void updateCommunication(String subNet, String iedName,
			String apName, Element root, Map<String, List<String>> map) {
		String conAPPath = SCL.XPATH_SUBNETWORK + "[@name='" + subNet + "']/scl:ConnectedAP[@iedName='" + iedName + "'][@apName='"+ apName+"']"; //$NON-NLS-1$
		List<String> list = map.get(subNet + Constants.COLON + apName);
		Element newNode = XMLDBHelper.selectSingleNode(conAPPath);
		Element connectedAP = newNode.createCopy();
		DOM4JNodeHelper.deleteNodes(connectedAP, "./*[name()='PhysConn']");
		
		
		Element ap = DOM4JNodeHelper.selectSingleNode(root, "./scl:IED/scl:AccessPoint[@name='"+apName+"']");
		List<Element> gses = ap.selectNodes(".//*[name()='GSEControl']");
		if (gses != null && !gses.isEmpty()) {
			insertGses(getNewEls(list, gses), connectedAP);
		}
		
		List<Element> smvs = ap.selectNodes(".//*[name()='SampledValueControl']");
		if (smvs != null && !smvs.isEmpty()) {
			insertSmvs(getNewEls(list, smvs), connectedAP);
		}
		
		// 移除不再使用的部分。
		for (String key : list) {
			String[] cons = key.split(Constants.COLON);
			DOM4JNodeHelper.deleteNodes(connectedAP, "./*[@ldInst='" + cons[0] + "'][@cbName='" + cons[1] + "']");
		}

		// <PhysConn>
		List<Element> consEle = getConnectApEle(root, apName);
		boolean flag = false;
		for (Element element1 : consEle) {
			List<Element> physEle = element1.elements("PhysConn");
			for (Element phyEle : physEle) {
				if (!flag){
					insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"Connection");
					flag = true;
				} else {
					insertPysConn(phyEle, connectedAP.addElement("PhysConn"),"RedConn");
				}
			}
		}
		XMLDBHelper.replaceNode(conAPPath, connectedAP);
	}

	/**
	 * 添加新增通信部分
	 * 
	 * @param list
	 * @param cbs
	 * @return
	 */
	private static List<Element> getNewEls(List<String> list, List<Element> cbs) {
		List<Element> newGses = new ArrayList<Element>();
		if(list!=null){
			for (Element gse : cbs) {
				String cbName = gse.attributeValue("name");
				String ldInst = gse.getParent().getParent().attributeValue("inst");
				String key = ldInst + Constants.COLON + cbName;
				if (list.contains(key)) {
					list.remove(key);// 已存在
				} else {
					newGses.add(gse);// 新增
				}
			}
		}
		return newGses;
	}
	
	/**
	 * 查询物理端口信息
	 * @return
	 */
	public List<PhysConn> getPhysConn() {
		List<PhysConn> physConnList = new ArrayList<>();
		Element apNode = XMLDBHelper.selectSingleNode(apXPath);
		List<Element> pcNodes = apNode.elements("PhysConn");
		for (Element pcNode : pcNodes) {
			String port = getPAttr(pcNode, "Port");
			String plug = getPAttr(pcNode, "Plug");
			String type = getPAttr(pcNode, "Type");
			physConnList.add(new PhysConn(port, plug, type));
		}
		return physConnList;
	}
	
	private String getPAttr(Element node, String name) {
		Element pNode = DOM4JNodeHelper.selectSingleNode(node, "./scl:P[@type='" + name + "']");
		return pNode.getTextTrim();
	}
	
	/**
	 * 保存物理端口信息
	 * @param physConnList
	 */
	public void savePhysConn(List<PhysConn> physConnList) {
		Element apNode = XMLDBHelper.selectSingleNode(apXPath);
		DOM4JNodeHelper.deleteNodes(apNode, "./scl:PhysConn");
		for (int i=0; i<physConnList.size(); i++) {
			PhysConn physConn = physConnList.get(i);
			Element pcNode = apNode.addElement("PhysConn");
			if (i==0) {
				pcNode.addAttribute("type", "Connection");
			} else {
				pcNode.addAttribute("type", "RedConn");
			}
			addPAttr(pcNode, "Port", physConn.getPort());
			addPAttr(pcNode, "Plug", physConn.getPlug());
			addPAttr(pcNode, "Type", physConn.getType());
		}
		XMLDBHelper.replaceNode(apXPath, apNode);
	}
	
	private void addPAttr(Element node, String name, String value) {
		Element pNode = node.addElement("P");
		pNode.addAttribute("type", name);
		pNode.setText(value);
	}
}
