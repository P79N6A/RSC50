/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;

import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-9-25
 */
/**
 * $Log: ControlBlockCheck.java,v $
 * Revision 1.1  2013/03/29 09:36:38  cchun
 * Add:创建
 *
 * Revision 1.2  2011/11/03 02:34:23  cchun
 * Update:指定精确的节点名称，避免因为icd文件中含有<Private>导致程序出错
 *
 * Revision 1.1  2011/09/15 08:39:30  cchun
 * Refactor:将模型检查移动到common插件
 *
 * Revision 1.7  2011/07/26 05:37:26  cchun
 * Refactor:使用统一的控制块枚举
 *
 * Revision 1.6  2011/01/10 09:24:00  cchun
 * Update:修改注释
 *
 * Revision 1.5  2010/12/31 05:22:03  cchun
 * Update:增加短地址比较处理
 *
 * Revision 1.4  2010/11/23 06:47:29  cchun
 * Update:修改控制块检查
 *
 * Revision 1.3  2010/11/04 09:30:32  cchun
 * Update:完善控制块检查
 *
 * Revision 1.2  2010/10/12 01:54:45  cchun
 * Update:完善控制块比较功能
 *
 * Revision 1.1  2010/09/26 09:19:11  cchun
 * Add:smv,goose,rcb,LNodeType的检查处理类
 *
 */
/**
 * 用于控制块部分的检查,需要检查的内容有:
 * 1、新的RCB、GSE、SMV引用的数据集是否存在;
 * 2、在名称、LDevice一致的情况下，新旧RCB/GSE/SMV引用的数据集名称是否一致;
 * 3、判断原来的RCB、GSE、SMV在新的icd文件中是否存在;
 * 4、判断原来的RCB、GSE、SMV引用的数据集是否存在;
 * 5、判断原来的RCB、GSE、SMV引用的数据集的FCDA条目和内容是否一致;
 */
public class ControlBlockCheck {
	
	interface Status {
		String err = "\t\n错误：";
		String waring = "\t\n警告：";
	}
	
	/**
	 * 检查新旧两文件中的LDevice下的控制块部分和短地址
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @return
	 */
	public static List<String> checkControl(List<?> lstNewLDevice,
			List<Element> lstOldLDevice) {
		List<String> lstStatus = new ArrayList<String>();
		checkReportControl(lstNewLDevice, lstOldLDevice, lstStatus);
		checkGooseControl(lstNewLDevice, lstOldLDevice, lstStatus);
		checkSMVControl(lstNewLDevice, lstOldLDevice, lstStatus);
		checkSAddrInfo(lstNewLDevice, lstOldLDevice, lstStatus);
		return lstStatus;
	}

	/**
	 * 比较报告控制块
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @param lstStatus
	 */
	private static void checkReportControl(List<?> lstNewLDevice,
			List<Element> lstOldLDevice, List<String> lstStatus) {
		lstStatus.add("\t\n------报告控制块比较开始------");
		List<String> lstRe = check(lstNewLDevice, lstOldLDevice,
				EnumCtrlBlock.ReportControl.name());
		if (lstRe.size() > 0)
			lstStatus.addAll(lstRe);
		else
			lstStatus.add("\n报告控制块没有差异");
		lstStatus.add("\t\n------报告控制块比较结束------\n");
	}
	
	/**
	 * 比较GOOSE控制块
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @param lstStatus
	 */
	private static void checkGooseControl(List<?> lstNewLDevice,
			List<Element> lstOldLDevice, List<String> lstStatus) {
		lstStatus.add("\t\n------GOOSE比较开始------");
		List<String> lstRe = check(lstNewLDevice, lstOldLDevice,
				EnumCtrlBlock.GSEControl.name());
		if (lstRe.size() > 0)
			lstStatus.addAll(lstRe);
		else
			lstStatus.add("\nGOOSE没有差异");
		lstStatus.add("\t\n------GOOSE比较结束------\n");
	}
	
	/**
	 * 比较SMV控制块
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @param lstStatus
	 */
	private static void checkSMVControl(List<?> lstNewLDevice,
			List<Element> lstOldLDevice, List<String> lstStatus) {
		lstStatus.add("\t\n------SMV比较开始------");
		List<String> lstRe = check(lstNewLDevice, lstOldLDevice,
				EnumCtrlBlock.SampledValueControl.name());
		if (lstRe.size() > 0)
			lstStatus.addAll(lstRe);
		else
			lstStatus.add("\nSMV没有差异");
		lstStatus.add("\t\n------SMV比较结束------\n");
	}
	
	/**
	 * 比较短地址信息
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @param lstStatus
	 */
	private static void checkSAddrInfo(List<?> lstNewLDevice,
			List<Element> lstOldLDevice, List<String> lstStatus) {
		lstStatus.add("\t\n------短地址比较开始------");
		List<String> lstWarning = new ArrayList<String>();
		List<String> lstAddedLost = new ArrayList<String>();
		Map<String, String[]> sAddrMapOld = new HashMap<String, String[]>();
		if(lstOldLDevice!=null){
		for (Element ld : lstOldLDevice) {
			String ldInst = ld.attributeValue("inst");
			String ldDesc = ld.attributeValue("desc");
			List<?> sAddrDAIs = ld.selectNodes(".//*[name()='DAI'][@sAddr]");
			for (Object obj : sAddrDAIs) {
				Element sAddrDAI = (Element) obj;
				String sAddr = sAddrDAI.attributeValue("sAddr", "");
				String strRef = new String("");
				String strDesc = new String("");
				Element parent = sAddrDAI;
				 while (parent != null && !parent.getName().equals("LDevice")) {
					String name = null;
					String desc = parent.attributeValue("desc");
					String ndType = parent.getName();
					if (ndType.startsWith("LN")) {
						name = SCL.getLnName(parent);
					} else {
						name = parent.attributeValue("name");
					}
					if (strRef.equals(""))
						strRef = name;
					else
						strRef = name + "." + strRef;
					if (desc != null)
						strDesc = desc + " " + strDesc;
					parent = parent.getParent();
				};
				strRef = ldInst + "/" + strRef;
				if (ldDesc != null)
					strDesc = ldDesc + " " + strDesc;
				sAddrMapOld.put(strRef, new String[] {sAddr, strDesc});
			}
		}
		}
		if(lstNewLDevice!=null){
		for (Object obj : lstNewLDevice) {
			Element ld = (Element) obj;
			String ldInst = ld.attributeValue("inst");
			String ldDesc = ld.attributeValue("desc");
			List<?> sAddrDAIs = ld.selectNodes(".//*[name()='DAI'][@sAddr]");
			for (Object obj1 : sAddrDAIs) {
				Element sAddrDAI = (Element) obj1;
				String sAddr = sAddrDAI.attributeValue("sAddr", "");
				String strRef = new String("");
				String strDesc = new String("");
				Element parent = sAddrDAI;
				 while (parent != null && !parent.getName().equals("LDevice")) {
					String name = null;
					String desc = parent.attributeValue("desc");
					String ndType = parent.getName();
					if (ndType.startsWith("LN")) {
						name = SCL.getLnName(parent);
					} else {
						name = parent.attributeValue("name");
					}
					if (strRef.equals(""))
						strRef = name;
					else
						strRef = name + "." + strRef;
					if (desc != null)
						strDesc = desc + " " + strDesc;
					parent = parent.getParent();
				};
				strRef = ldInst + "/" + strRef;
				if (ldDesc != null)
					strDesc = ldDesc + " " + strDesc;
				// 比较
				if (sAddrMapOld.containsKey(strRef)) {
					String[] oldSAddr = sAddrMapOld.get(strRef);
					if (!sAddr.equals(oldSAddr[0])) {
						lstWarning.add(Status.waring + strRef + "(" + oldSAddr[1].trim() + ")的短地址已经改成[" + sAddr +
								"]，原值为[" + oldSAddr[0] + "]。");
					}
					sAddrMapOld.remove(strRef);
				} else {
					lstAddedLost.add(Status.waring + "新的ICD文件增加了短地址" + strRef + "[" + sAddr + "](" + strDesc + ")");
				}
			}
		}
		}
		
		for (Entry<String, String[]> entry : sAddrMapOld.entrySet()) {
			String strRef = entry.getKey();
			String[] oldSAddr = entry.getValue();
			lstAddedLost.add(Status.waring + "新的ICD文件减少了短地址" + strRef + "[" + oldSAddr[0] + "](" + oldSAddr[1] + ")");
		}
		
		if (lstWarning.size() > 0 || lstAddedLost.size() > 0) {
			lstStatus.addAll(lstWarning);
			lstStatus.addAll(lstAddedLost);
		} else {
			lstStatus.add("\n短地址没有差异");
		}
		lstStatus.add("\t\n------短地址比较结束------\n");
	}
	
	/**
	 * 比较新、旧LD控制块差异
	 * @param lstNewLDevice
	 * @param lstOldLDevice
	 * @param controltype
	 * @return
	 */
	private static List<String> check(List<?> lstNewLDevice,
			List<Element> lstOldLDevice, String controltype) {
		List<String> lstStatus = new ArrayList<String>();
		// 得到控制块信息
		List<Element> lstNewControl = new ArrayList<Element>();
		List<Element> lstOldControl = new ArrayList<Element>();
		findControl(lstNewLDevice, lstNewControl, controltype);
		findControl(lstOldLDevice, lstOldControl, controltype);
		// 得到数据集信息
		Map<String, Element> hshNewDataSet = new HashMap<String, Element>();
		Map<String, Element> hshOldDataSet = new HashMap<String, Element>();
		findDataSet(lstNewLDevice, hshNewDataSet);
		findDataSet(lstOldLDevice, hshOldDataSet);

		Map<String, Element> hshControl = new HashMap<String, Element>();
		List<String> lstDataSetName = new ArrayList<String>();// 存储已经检查过的数据集
		for (Element oldControl : lstOldControl) {
			String tagName = oldControl.getName();
			String controlName = oldControl.attributeValue("name");
			String iedName = oldControl.attributeValue("iedName", "");
			String ldInst = oldControl.attributeValue("ldInst", "");
			String lnType = oldControl.attributeValue("lnType", "LN");
			Element newControlElement = null;
			String key = tagName + Constants.DOLLAR + ldInst + Constants.DOLLAR
					+ controlName;

			String buffered = getBufferedValue(oldControl);
			String errTip = EnumCtrlBlock.valueOf(tagName).getDesc()
					+ iedName + ldInst + "/" + lnType + "/" + buffered
					+ Constants.DOLLAR + controlName;
			if (hshControl.get(key) != null) {
				newControlElement = hshControl.get(key);
			} else {
				newControlElement = existWithinNewICD(lstNewControl,
						hshControl, tagName, controlName, ldInst);
			}
			if (newControlElement == null) {// 判断原来的RCB、GSE、SMV在新的icd文件中是否存在;
				lstStatus.add(Status.waring + "icd文件不存在 " + errTip);
			} else {
				String dataset = oldControl.attributeValue("datSet");
				String newDataSet = newControlElement.attributeValue("datSet", "");
				String newLDInst = newControlElement.attributeValue("ldInst", "");

				if (lstDataSetName.contains(dataset)) {// 已经检查过的数据集不再检查
					continue;
				}
				if (!dataset.equals(newDataSet)) {// 在名称、LDevice一致的情况下，新旧RCB/GSE/SMV引用的数据集名称是否一致

					lstStatus.add(Status.waring + errTip + " 原来引用的数据集名为 "
							+ dataset + ",已更新为" + newDataSet);
					continue;
				}
				lstDataSetName.add(dataset);

				String oldkey = oldControl.attributeValue("iedName", "")
						+ Constants.DOLLAR + ldInst + Constants.DOLLAR
						+ dataset;
				String newKey = newControlElement.attributeValue("iedName", "")
						+ Constants.DOLLAR + newLDInst + Constants.DOLLAR
						+ newDataSet;

				Element newDS = hshNewDataSet.get(newKey);
				Element oldDS = hshOldDataSet.get(oldkey);

				if (newDS == null) {// 新的RCB、GSE、SMV引用的数据集是否存在
					lstStatus.add(Status.err + errTip + " 引用的数据集 " + newDataSet
							+ " 在icd文件中不存在");
				} else {
					if (oldDS == null) {
						String noKey = newControlElement.attributeValue(
								"iedName", "")
								+ Constants.DOLLAR
								+ ldInst
								+ Constants.DOLLAR
								+ dataset;
						if (hshNewDataSet.get(noKey) == null) {// 判断原来的RCB、GSE、SMV引用的数据集是否存在
							lstStatus.add(Status.err + errTip + " 引用的数据集"
									+ newDataSet + " 在icd文件中不存在");
						}
						continue;
					}
					List<String> re = compareDataSet(newDS, oldDS);// 判断原来的RCB、GSE、SMV引用的数据集的FCDA条目和内容是否一致
					if (re != null && re.size() > 0) {
						for (String status : re) {
							lstStatus.add(Status.waring + "数据集" + ldInst + "/"
									+ lnType + "/" + dataset + " " + status);
						}
					}
				}
			}
		}
		removeAttr(lstOldControl);
		removeAttr(lstNewControl);
		return lstStatus;
	}
	
	/**
	 * 根据control 名称和tag在集合中查找对应的Element
	 * @param name
	 * @param curTagName
	 * @param lstNewControl
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Element findControlByNameType(String name,
			String curTagName, List<Element> lstNewControl) {
		Element reElement = null;
		if (lstNewControl == null || lstNewControl.size() == 0) {
			return reElement;
		}
		for (Element newControl : lstNewControl) {
			String tagName = newControl.getName();
			String controlName = newControl.attributeValue("name", "");
			if (curTagName.equals(tagName) && controlName.equals(name)) {
				reElement = newControl;
				break;
			}
		}
		return reElement;
	}
	
	/**
	 * 比较新旧两数据集的内容以及其里面的FCDA个数
	 * 
	 * @param newDS
	 * @param oldDS
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<String> compareDataSet(Element newDS, Element oldDS) {
		List<String> lstStatus = new ArrayList<String>();
		List<Element> lstNewFCDA = newDS.elements("FCDA");
		List<Element> lstOldFCDA = oldDS.elements("FCDA");
		if (lstNewFCDA.size() != lstOldFCDA.size()) {
			lstStatus.add(" FCDA的数目在icd文件中与现有装置不一致");
		}

		fetchDifference(lstStatus, lstNewFCDA, lstOldFCDA);
		return lstStatus;
	}
	
	/**
	 * 获取两集合差异化的内容
	 * @param lstStatus
	 * @param lstNewFCDA
	 * @param lstOldFCDA
	 * @param addOrReduce
	 */
	private static void fetchDifference(List<String> lstStatus,
			List<Element> lstNewFCDA, List<Element> lstOldFCDA) {
		if (lstOldFCDA == null || lstNewFCDA == null) {
			return;
		}
		String addOrReduce="增加了";
		List<String> lstDataSetRef=new ArrayList<String>();
		for (Element oldFCDA : lstOldFCDA) {
			String oldDataSetRef=getDataSetRef(oldFCDA);
			lstDataSetRef.add(oldDataSetRef.toString());		
		}
		
		for (Element newFCDA : lstNewFCDA) {
			String newDataSetRef=getDataSetRef(newFCDA);
			if (lstDataSetRef.contains(newDataSetRef)) {
				lstDataSetRef.remove(newDataSetRef);
				continue;
			}else{
				lstStatus.add(" FCDA" + addOrReduce + " " + newDataSetRef);
			}
		}
		addOrReduce = "减少了";
		for(String ref : lstDataSetRef){
			lstStatus.add(" FCDA" + addOrReduce + " " + ref);
		}
	}
	
	/**
	 * 获取DataSet的参引
	 * @param fcda
	 * @return
	 */
	private static String getDataSetRef(Element fcda){
		String doName = getAttr(fcda, "doName");
		String daName = getAttr(fcda, "daName");
		String ldInst = getAttr(fcda, "ldInst");
		String lnClass = getAttr(fcda, "lnClass");
		String lnInst = getAttr(fcda, "lnInst");
		String prefix = getAttr(fcda, "prefix");
		String fc = getAttr(fcda, "fc");
		
		StringBuffer sbDataSetRef = new StringBuffer(ldInst + "/");
		sbDataSetRef.append(prefix + lnClass + lnInst);
		sbDataSetRef.append(Constants.DOT + fc);
		sbDataSetRef.append(Constants.DOT + doName);

		if (!StringUtil.isEmpty(daName)) {
			sbDataSetRef.append(Constants.DOT + daName);
		}
		return sbDataSetRef.toString();
	}
	
	/**获取元素的属性值
	 * @param element
	 * @param attr
	 * @return
	 */
	private static String getAttr(Element element, String attr) {
		return element.attributeValue(attr, "");
	}

	/**
	 * 原文件中的控制块是否在新文件中
	 * 
	 * @param lstNewControl
	 * @param hshControl
	 * @param tagName
	 * @param controlName
	 * @return
	 */
	private static Element existWithinNewICD(List<Element> lstNewControl,
			Map<String, Element> hshControl, String tagName, String controlName,String ldInst) {
		Element re = null;
		if (lstNewControl == null) {
			return re;
		}
		for (Element newControl : lstNewControl) {
			String newTagName = newControl.getName();
			String newControlName = newControl.attributeValue("name");
			String newldInst = newControl.attributeValue("ldInst", "");
			String key = newTagName + Constants.DOLLAR + newldInst
					+ Constants.DOLLAR + newControlName;

			hshControl.put(key, newControl);

			if (newTagName.equals(tagName)
					&& newControlName.equals(controlName)
					&& newldInst.equals(ldInst)) {
				re = newControl;
			}
		}
		return re;
	}

	/**
	 * 根据xpath在LDevice中获得数据集,并把找到的数据集放入缓存
	 * 
	 * @param lst
	 * @param hshDataset
	 */
	private static void findDataSet(List<?> lst,
			Map<String, Element> hshDataset) {
		if (lst == null || lst.size() == 0) {
			return;
		}
		String xpath = "./*[name()='LN0' or name()='LN']/*[name()='DataSet']";
		for (Object obj : lst) {
			if (obj == null)
				continue;
			Element lDevice = (Element) obj;
			List<?> lstChildDataSet = lDevice.selectNodes(xpath);
			if (lstChildDataSet == null) {
				continue;
			}
			String iedName=lDevice.attributeValue("iedName", "");
			String ldInst=lDevice.attributeValue("inst", "");
			for (Object obj1 : lstChildDataSet) {
				Element ds = (Element) obj1;
				String name = ds.attributeValue("name");
				hshDataset.put(iedName+Constants.DOLLAR+ldInst+Constants.DOLLAR+name, ds);
			}
		}
	}

	/**
	 * 根据xpath在LDevice中获得控制块,并把找到的控制块放入缓存
	 * 
	 * @param lst
	 * @param lstControl
	 */
	private static void findControl(List<?> lst,
			List<Element> lstControl, String type) {
		if (lst == null || lst.size() == 0)
			return;
		if (StringUtil.isEmpty(type)) {
			type = "name()='ReportControl' or name()='GSEControl' or name()='SampledValueControl'";
		} else {
			type = "name()='" + type + "' ";
		}
		String xpath = "./*[name()='LN0' or name()='LN']/*[" + type + "]";
		for (Object obj : lst) {
			if (obj == null)
				continue;
			Element lDevice = (Element) obj;
			List<?> lstChildControl = lDevice.selectNodes(xpath);
			for (Object obj1 : lstChildControl) {
				Element control = (Element) obj1;
				control.addAttribute("iedName", lDevice.attributeValue(
						"iedName", ""));
				control.addAttribute("ldInst", lDevice.attributeValue("inst",
						""));
				if (control.getParent() != null)
					control.addAttribute("lnType", control.getParent()
							.getName());
				lstControl.add(control);
			}
		}
	}
	
	/**
	 * 获取各个control的buffered值
	 * @param control
	 * @return
	 */
	private static String getBufferedValue(Element control) {
		String tagName = control.getName();
		String buffered = "";
		if ("GSEControl".equals(tagName)) {
			buffered = "GO";
		} else if ("ReportControl".equals(tagName)) {
			buffered = "true".equals(control.attributeValue("buffered")) ? "BR"
					: "RP";
		} else if ("SampledValueControl".equals(tagName)) {
			buffered = "SV";
		}
		return buffered;
	} 
	
	/**
	 * 删除前面增加的属性
	 * @param lstControl
	 */
	private static void removeAttr(List<Element> lstControl) {
		if (lstControl == null)
			return;
		for (Element control : lstControl) {
			if (control.attributeValue("iedName") != null) {
				control.addAttribute("iedName", null);
			}
			if (control.attributeValue("ldInst") != null) {
				control.addAttribute("ldInst", null);
			}
			if (control.attributeValue("lnType") != null) {
				control.addAttribute("lnType", null);
			}
		}
	}
}
