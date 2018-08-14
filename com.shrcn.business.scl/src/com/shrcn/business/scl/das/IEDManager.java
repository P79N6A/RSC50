/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.enums.EnumCommType;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.CommunicationUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-4-19
 */
/**
 * $Log: IEDManager.java,v $
 * Revision 1.8  2013/11/26 01:12:54  zq
 * Update: 修改增加开关选项判断
 *
 * Revision 1.7  2013/11/25 08:56:35  zq
 * Update: 替换appID和smvID的信息
 *
 * Revision 1.6  2013/11/25 07:05:16  cchun
 * Fix Bug:修复synControlsID() xpath错误
 *
 * Revision 1.5  2013/11/12 05:59:15  zq
 * Update: 增加替换IED名称的方法
 *
 * Revision 1.4  2012/01/17 08:50:30  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.3  2012/01/13 08:40:03  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.2  2011/12/02 08:52:12  cchun
 * Refactor:移动IED复制方法到IEDManager
 *
 * Revision 1.1  2010/04/19 05:50:59  cchun
 * Add: 添加
 *
 */
public class IEDManager {

	/**
	 * 改名时，同步修改appID和smvID
	 * @param oldIEDName
	 * @param newIEDName
	 */
	public static void synControlsID(String oldIEDName, String newIEDName) {
		if (!SCTProperties.getInstance().getPrefixAutoMode()) {
			return;
		}
		List<Element> lds = getLds(newIEDName);
		for(Element ld : lds) {
			String ldInst = ld.attributeValue("inst");
			List<?> controls = DOM4JNodeHelper.selectNodes(ld, "./*/*");
			for(Object obj : controls) {
				Element control = (Element) obj;
				String cbName = control.attributeValue("name");
				String type = control.getName();
				String xpath = null;
				if("GSEControl".equals(type)) {
					String appID = control.attributeValue("appID");
					String newAppID = IEDDAO.getNewAttr(newIEDName, oldIEDName, appID);
					control.addAttribute("appID", newAppID);
					xpath = "/SCL/IED[@name='" + newIEDName + "']/AccessPoint/Server/LDevice[@inst='" + ldInst +
							"']/LN0/GSEControl[@name='" + cbName +	"']";
				}
				if("SampledValueControl".equals(type)) {
					String smvID = control.attributeValue("smvID");
					String newSmvID = IEDDAO.getNewAttr(newIEDName, oldIEDName, smvID);
					control.addAttribute("smvID", newSmvID);
					xpath = "/SCL/IED[@name='" + newIEDName + "']/AccessPoint/Server/LDevice[@inst='" + ldInst +
							"']/LN0/SampledValueControl[@name='" + cbName +	"']";
				}
				if("ReportControl".equals(type)) {
					String rptID = control.attributeValue("rptID");
					String newRptID = rptID.replace(oldIEDName+"/", newIEDName+"/");
					control.addAttribute("rptID", newRptID);
					xpath = "/SCL/IED[@name='" + newIEDName + "']/AccessPoint/Server/LDevice[@inst='" + ldInst +
							"']/LN0/ReportControl[@name='" + cbName +	"']";
				}
				if (xpath != null)
					XMLDBHelper.replaceNode(xpath, control);
			}
		}
		
	}
	public static List<Element> getLds(String iedName){
		List<Element> lds = null;
		if (Constants.XQUERY){
			String xquery = "let $doc:=" + XMLDBHelper.getDocXPath() + ", $lds:=$doc/SCL/IED[@name='" + iedName +
					"']/AccessPoint/Server/LDevice return for $ld in $lds return <LDevice inst='{$ld/@inst}'><LN>{" +
					"$ld/LN0/*[name() eq 'GSEControl' or name() eq 'SampledValueControl' or name() eq 'ReportControl'  ]}</LN></LDevice>";
			lds = XMLDBHelper.queryNodes(xquery);
		}else {
			String xPath = "/SCL/IED[@name='" + iedName +
					"']/AccessPoint/Server/LDevice[count(./*/GSEControl)>0 or count(./*/SampledValueControl)>0 or count(./*/ReportControl)>0]";
			lds = XMLDBHelper.selectNodes(xPath);
		}
		return lds;
	}
	/**
	 * 获取Inputs信号中的IED名称列表
	 * 
	 * @param iedName IED名称
	 * @return
	 */
	public static List<String> getInputsIEDNames(String iedName) {
		List<String> iedNameList = new ArrayList<String>();
		Set<String> iedNameSet = new LinkedHashSet<String>();
		
		Element ied = XMLDBHelper.selectSingleNode(SCL.getIEDXPath(iedName));
		
		List<?> inputsList = DOM4JNodeHelper.selectNodes(ied, "./AccessPoint/Server/LDevice/*/Inputs/ExtRef']");
		
		for(Object obj : inputsList) {
			Element inputEl = (Element) obj;
			
			iedNameSet.add(inputEl.attributeValue("iedName"));
		}
		
		iedNameList.addAll(iedNameSet);
		
		return iedNameList;
	}
	
	public static void copyIED2SCD(Element oldIED, Map<String, List<?>> map, String name, String newName, boolean includeInputs, 
			Map<String, String> iedMap) {
		insertIED(oldIED, name, newName, includeInputs, iedMap);// 复制并插入IED
		insertCommunication(map, name,newName);// 复制并插入通信
		XMLDBHelper.forceCommit();
	}
	
	//复制并插入IED
	public static void insertIED(Element oldIED, String name, String newName, boolean includeInputs, 
			Map<String, String> iedMap) {
		Element newIED = oldIED.createCopy();
		if (includeInputs) {
			// 替换信号关联中输入IED名称
			replaceInputIed(newIED, iedMap);
		} else {
			// 清除inputs部分
			clearInputs(newIED);
		}
		(newIED.attribute("name")).setValue(newName); //$NON-NLS-1$
		XMLDBHelper.insertAfter(SCL.getIEDXPath(name), newIED);
	}
	
	/**
	 * 更新Inputs输入信号的IED名称
	 * 
	 * @param ied 装置IED节点
	 * @param iedMap IED名称对应关系
	 */
	private static void replaceInputIed(Element ied,
			Map<String, String> iedMap) {
		List<Element> inputsList = DOM4JNodeHelper.selectNodes(ied, "./AccessPoint/Server/LDevice/*/Inputs/ExtRef");
		
		for (Element el : inputsList) {
			String iedName = el.attribute("iedName").getValue();
			if (iedMap.containsKey(iedName)) {
				String newIedName = iedMap.get(iedName);
				
				if (newIedName != null && !"".equals(newIedName)) {
					el.attribute("iedName").setValue(newIedName);
				}
			}
		}
	}
	
	/**
	 * 复制并插入通信
	 */
	public static void insertCommunication(Map<String, List<?>> map, String name,String newName){
		for(String subNetXpath : map.keySet()){
			List<?> oldConAPList = map.get(subNetXpath);
 			if (oldConAPList.size() != 0) {
				Iterator<?> it = oldConAPList.iterator();
				while (it.hasNext()) {
					Element oldConnectedAP = (Element) it.next();
					if (oldConnectedAP == null)
						continue;
					Element newConnectedAP = oldConnectedAP.createCopy();
					newConnectedAP.addAttribute("iedName", newName);
					List<?> comCfs = newConnectedAP.elements();
					Iterator<?> comIt = comCfs.iterator();
					while (comIt.hasNext()) {
						Element comCf = (Element) comIt.next();
						String ndName = comCf.getName();
				    		EnumCommType commType = EnumCommType.getType(ndName); 
				    	if(commType!=null){
							switch (commType) {
								case MMS:
									Element ipNd = DOM4JNodeHelper.selectSingleNode(comCf, "./P[@type='IP']");
									String mmsIP = SCTProperties.getInstance().nextMmsIP();
									ipNd.setText(mmsIP);
									break;
								case GSE:
								case SMV:
									Element macNd = DOM4JNodeHelper.selectSingleNode(comCf, "./Address/P[@type='MAC-Address']");
									Element appIdNd = DOM4JNodeHelper.selectSingleNode(comCf, "./Address/P[@type='APPID']");
									String[] cfg = CommunicationUtil.getNextMACCfg(commType);
									macNd.setText(cfg[0]);
									appIdNd.setText(cfg[1]);
									break;
							}
				    	}
					}
					XMLDBHelper.insertAsLast(subNetXpath, newConnectedAP);
				}
			}
		}
	}
	
	public static Map<String, List<?>> getSubNetMap(String name){
		List<?> listofSubNet = XMLDBHelper.selectNodes(SCL.XPATH_SUBNETWORK); //$NON-NLS-1$
		Map<String, List<?>> map = new LinkedHashMap<>();
		if (listofSubNet != null) {
			for (Object obj : listofSubNet) {
				Element subNet = (Element) obj;
				String subNetName = subNet.attributeValue("name"); //$NON-NLS-1$
				String subNetXpath = SCL.XPATH_SUBNETWORK + "[@name='" + subNetName + "']";
				List<?> els = DOM4JNodeHelper.selectNodes(subNet, "./scl:ConnectedAP[@iedName='" + name + "']");
				map.put(subNetXpath, els);
			}
		}
		return map;
	}

	/**
	 *  清楚<IED>的inputs部分
	 * @param ied <IED>节点 
	 */
	private static void clearInputs(Element ied) {
		List<?> inputsList = DOM4JNodeHelper.selectNodes(ied, "./AccessPoint/Server/LDevice/*/Inputs");
		for (Object obj : inputsList) {
			Element inputs = (Element) obj;
			(inputs.getParent()).remove(inputs);
		}
	}

}
