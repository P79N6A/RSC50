/**
 * Copyright (c) 2007-2015 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.das.GOOSEInputDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.FCDAEntry;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-9-20
 */
public class RelationsConfig {
	
	private static final String cfgPath = Constants.cfgDir + File.separator + "Relations.xml";
	private static final int typeLen = 5;
	private Element relationsRoot;

	private static RelationsConfig instance = new RelationsConfig();
	
	private RelationsConfig() {
		try {
			this.relationsRoot = XMLFileManager.loadXMLFile(cfgPath).getRootElement();
		} catch (Exception e) {
			SCTLogger.error("", e);
		}
	}
	
	public static RelationsConfig getInstance() {
		if (instance==null) {
			instance = new RelationsConfig();
		}
		return instance;
	}
	
	/**
	 * 检查关联是否合法
	 * @param iedName
	 * @param outIed
	 * @param num
	 * @return
	 */
	public String checkRelation(String iedName, String outIed, int num) {
		String msg = null;
		Element iedCfg = getIEDCfg(iedName);
		if (iedCfg == null) {
			msg = "规范中未找到装置 " + iedName + " 的虚端子定义！";
		} else {
			Element inputsCfg = getInputsCfg(iedCfg, outIed);
			if (inputsCfg == null) {
				msg = "根据规范，无需和装置 " + outIed + " 关联！";
			} else {
				int maxNum = getMaxNumber(inputsCfg);
				if (maxNum > 0 && num > maxNum) {
					msg = "根据规范，最多和 " + maxNum + " 台 " + outIed + " XX类型装置关联！";
				}
			}
		}
		return msg;
	}
	
	public String getIedType(String iedName) {
		return iedName.substring(0, typeLen);
	}
	
	private int getNumber(String num) {
		if (!StringUtil.isEmpty(num) && StringUtil.isNumeric(num)) {
			return Integer.parseInt(num);
		}
		return -1;
	}
	
	/**
	 * 得到段、位串等编号
	 * @param iedName
	 * @return
	 */
	public int getIedNumber(String iedName) {
		if (iedName.length() >= (typeLen + 2)) {
			String num = iedName.substring(typeLen, typeLen + 2);
			return getNumber(num);
		}
		return -1;
	}
	
	/**
	 * 得到子机编号
	 * @param iedName
	 * @return
	 */
	public int getIedSubNumber(String iedName) {
		if (iedName.length() > (typeLen + 4)) {
			String num = "" + iedName.charAt(iedName.length() - 1);
			return getNumber(num);
		}
		return -1;
	}
	
	/**
	 * 得到可接入最大间隔数
	 * @param inputsCfg
	 * @return
	 */
	public int getMaxNumber(Element inputsCfg) {
		String strMax = inputsCfg.attributeValue("maxN");
		return getNumber(strMax);
	}
	
	/**
	 * 获取IED虚端子配置
	 * @param iedName
	 * @return
	 */
	public Element getIEDCfg(String iedName) {
		String iedType = getIedType(iedName);
		return DOM4JNodeHelper.selectSingleNode(relationsRoot, "./IED[@type='" + iedType + "']");
	}
	
	/**
	 * 获取关联装置配置
	 * @param iedCfg
	 * @param outIed
	 * @return
	 */
	public Element getInputsCfg(Element iedCfg, String outIed) {
		String iedType = getIedType(outIed);
		return DOM4JNodeHelper.selectSingleNode(iedCfg, "./Inputs[@iedType='" + iedType + "']");
	}
	
	/**
	 * 自动关联虚端子
	 * @return
	 */
	public boolean autoRelateAll(IProgressMonitor monitor) {
		List<String> allIeds = IEDDAO.getIEDNames();
		if (monitor != null)
			monitor.beginTask("开始自动关联虚端子", allIeds.size()+1);
		boolean success = true;
		Map<String, Map<String, FCDAEntry>> gooseCache = new HashMap<>();
		ConsoleManager console = ConsoleManager.getInstance();
		for (String iedName : allIeds) {
			if (monitor != null)
				monitor.setTaskName("正在处理装置 " + iedName);
			StringBuilder sbErrs = new StringBuilder();
			String[] outIeds = findOutIedsByCfg(iedName, allIeds, sbErrs);
			if (sbErrs.length() > 0) {
				String msg = "错误：装置 " + iedName + " 缺少如下类型关联装置：" + sbErrs.toString();
				SCTLogger.warn(msg);
				console.append(msg);
				success = false;
			}
			String ldInst = "PIGO";
			Element lnData = XMLDBHelper.selectSingleNode(SCL.getLDXPath(iedName, ldInst) + "/scl:LN0");
			GOOSEInputDAO dao = new GOOSEInputDAO(iedName, ldInst, lnData, gooseCache);
			dao.autoRelate(outIeds);
			if (monitor != null)
				monitor.worked(1);
		}
		return success;
	}
	
	/**
	 * 从SCD中查找可以关联的外部装置
	 * @param iedName
	 * @param allIeds
	 * @param sbErrs
	 * @return
	 */
	private String[] findOutIedsByCfg(String iedName, List<String> allIeds, StringBuilder sbErrs) {
		List<String> outIeds = new ArrayList<>();
		Element iedCfg = getIEDCfg(iedName);
		if (iedCfg != null) {
			String groupMax = iedCfg.attributeValue("groupMax");
			int iedNum = getIedNumber(iedName);
			Integer min = null;
			Integer max = null;
			if (groupMax != null) {
				int gMax = getNumber(groupMax);
				int iedSubNum = getIedSubNumber(iedName);
				if (gMax > 0 && iedSubNum > 0) {
					min = gMax * (iedSubNum-1);
					max = gMax * iedSubNum;
				}
			}
			List<Element> inputs = iedCfg.elements();
			for (Element input : inputs) {
				String outType = input.attributeValue("iedType");
				String groupMaxOut = input.attributeValue("groupMax");
				int num = getMaxNumber(input);
				num = num > 0 ? num : 1;
				List<String> outTypeIeds = new ArrayList<>();
				if (groupMaxOut == null) {
					if (min != null && max != null) {
						outTypeIeds = findOutIedsByType(iedName, allIeds, outType, num, min, max);
					} else {
						outTypeIeds = findOutIedsByType(iedName, allIeds, outType, num);
					}
				} else {
					outTypeIeds = findOutIedsByType(iedName, allIeds, outType, Integer.MAX_VALUE);
				}
				int outSize = outTypeIeds.size();
				if (outSize > 0) {
					if (groupMaxOut != null) {
						int gMaxOut = getNumber(groupMaxOut);
						if (gMaxOut > 0) {
							int outNum = (iedNum%gMaxOut > 0) ? (iedNum/gMaxOut) + 1 : (iedNum/gMaxOut);
							for (String outTypeIed : outTypeIeds) {
								if (outNum == getIedSubNumber(outTypeIed)) {
									outIeds.add(outTypeIed);
									break;
								}
							}
						}
					} else {
						outIeds.addAll(outTypeIeds);
					}
				} else {
					boolean isPM = outType.startsWith("PM");
					boolean exists = false;
					for (String outIed : outIeds) {
						if (isPM && outIed.startsWith("PM")) {
							exists = true;
							break;
						}
					}
					if (!exists) {
						if (sbErrs.length() > 0) {
							sbErrs.append(",");
						}
						sbErrs.append(outType);
					}
				}
			}
		}
		return outIeds.toArray(new String[outIeds.size()]);
	}
	
	/**
	 * 根据接入装置类型和最大间隔数查找外部关联装置
	 * @param iedName
	 * @param allIeds
	 * @param outType
	 * @param num
	 * @return
	 */
	private List<String> findOutIedsByType(String iedName, List<String> allIeds, String outType, int num) {
		int i=0;
		List<String> outIeds = new ArrayList<>();
		for (String ied : allIeds) {
			if (!iedName.equals(ied) && ied.startsWith(outType) && i<num) {
				outIeds.add(ied);
				i++;
			}
		}
		return outIeds;
	}
	
	private List<String> findOutIedsByType(String iedName, List<String> allIeds, String outType, int num, Integer min, Integer max) {
		int i=0;
		int gLen = typeLen + 2;
		List<String> outIeds = new ArrayList<>();
		for (String ied : allIeds) {
			boolean isSameGroup = iedName.substring(0, gLen).equals(ied.substring(0, 7));
			if (!iedName.equals(ied) && ied.startsWith(outType) && i<num && !isSameGroup) {
				int iedNum = getIedNumber(ied);
				if (iedNum > min && iedNum <= max) {
					outIeds.add(ied);
					i++;
				}
			}
		}
		return outIeds;
	}
	
}
