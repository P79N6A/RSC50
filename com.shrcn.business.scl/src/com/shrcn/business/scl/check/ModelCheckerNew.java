/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.check;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.CheckUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.valid.DataTypeChecker;
import com.shrcn.found.common.valid.ValidatorStatus;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-23
 */

public class ModelCheckerNew {
	
	/**
	 * 判断CB通信参数是否存在/正确
	 * @param cbEl
	 * @param cbName
	 * @param block
	 * @param msg
	 */
	public static void checkCbProps(String iedName, Element cbEl, LEVEL level, List<Problem> problems) {
		EnumCtrlBlock block = EnumCtrlBlock.GSEControl.getCbName().equals(cbEl.getName()) ?
				EnumCtrlBlock.GSEControl : EnumCtrlBlock.SampledValueControl;
		String cbName = cbEl.attributeValue("cbName");
		String[] props = {"MAC-Address", "APPID", "VLAN-ID", "VLAN-PRIORITY"};
		String[] props1 = {"MAC", "APPID", "VLANID", "VLANP"};
		String[] warnAttrs = {"VLAN-ID", "VLAN-PRIORITY"};
		int i = 0;
		String ldInst = cbEl.attributeValue("ldInst");
		for (String prop : props) {
			String xpath = "./scl:Address/scl:P[@type='" + prop + "']";
			String value = DOM4JNodeHelper.getNodeValueByXPath(cbEl, xpath);
			String cbRef = SCL.getCbRef(ldInst, cbName, block);
			if (StringUtil.isEmpty(value)) {
				boolean isWarn = (Arrays.asList(warnAttrs).indexOf(prop) > -1) && level == LEVEL.WARNING;
				problems.add(Problem.create(isWarn  ? LEVEL.WARNING : LEVEL.ERROR, iedName, block.getDesc(), cbRef, "属性 \"" + prop + "\"" + checkProp(value) + "。"));
			} else {
				checkAttr(iedName, ldInst, cbName, block, prop, props1, i, value, level, problems);
			}
			i++;
		}
		// 检查GSE的MinTime、MaxTime
		if (EnumCtrlBlock.GSEControl == block) {
			checkAttr(cbEl, iedName, block, level, problems);
		}
	}

	/**
	 * 
	 * @param cbName
	 * @param block
	 * @param msg
	 * @param props1
	 * @param i
	 * @param value
	 */
	private static void checkAttr(String iedName, String ldInst, String cbName, 
			EnumCtrlBlock block, String prop, String[] props1, int i, String value, LEVEL level, List<Problem> problems) {
		String p = props1[i];
		List<String> list = new ArrayList<String>();
		Class<?>[] paramTypes = null;
		Object[] paramObj = null;
		if (i < 2) {
			paramTypes = new Class[] { String.class, String.class, List.class };
			paramObj = new Object[] { value, block.getCbName(), list };
		} else {
			paramTypes = new Class[] { String.class };
			paramObj = new Object[] { value };
		}
		try {
			String validateMethod = "check" + p;
			Method method = CheckUtil.class.getMethod(validateMethod, paramTypes);
			ValidatorStatus vs = (ValidatorStatus) method.invoke(CheckUtil.class.newInstance(), paramObj);
			if (vs != null) {
				String[] warnAttrs = {"VLANID", "VLANP"};
				String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
				boolean isWarn = (Arrays.asList(warnAttrs).indexOf(p) > -1) && level ==LEVEL.WARNING;
				problems.add(Problem.create(isWarn  ? LEVEL.WARNING : LEVEL.ERROR, iedName, block.getDesc(), cbRef, prop + "(" + value +
						 ")" + vs.getMessage()));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查缺少属性信息
	 * 
	 * @param iedName
	 * @param el
	 * @param str
	 * @return
	 */
	private static void checkAttr(Element el, String iedName, EnumCtrlBlock block, LEVEL level, List<Problem> problems) {
		String ldInst = el.attributeValue("ldInst");
		String cbName = el.attributeValue("cbName");
		String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
		String[] xpaths = {"MinTime", "MaxTime"};
		int minTime = -1, maxTime = -1;
		for (String xpath : xpaths) {
			Element tNd = el.element(xpath);
			if (tNd == null) {
				problems.add(Problem.create(level, iedName, block.getDesc(), cbRef, "属性 " + xpath + " 缺失。"));
				continue;
			}
			String time = tNd.getText();
			if (StringUtil.isEmpty(time.trim())) {
				problems.add(Problem.create(level, iedName, block.getDesc(), cbRef, "属性 " + xpath + " 为空。"));
				continue;
			}
			if (DataTypeChecker.checkDigit(time)) {
				if ("MinTime".equals(xpath)) {
					minTime = Integer.parseInt(time);
				} else {
					maxTime = Integer.parseInt(time);
				}
			} else {
				problems.add(Problem.create(level, iedName, block.getDesc(), cbRef, "属性 " + xpath + " 不是整数。"));
			}
		}
		if (minTime > 0 && maxTime > 0) {
			if (minTime >= maxTime) {
				problems.add(Problem.create(level, iedName, block.getDesc(), cbRef, "MinTime属性值 " + minTime +
						" 不比MaxTime属性值 " + maxTime + " 小。"));
			}
		}
	}
	
	private static void addError(List<Problem> problems, String iedName, String subType, String ref, String desc) {
		problems.add(Problem.createError(iedName, subType, ref, desc));
	}
	
	private static void addWarning(List<Problem> problems, String iedName, String subType, String ref, String desc) {
		problems.add(Problem.createWarning(iedName, subType, ref, desc));
	}
	
	private static void addProblem(LEVEL level, List<Problem> problems, String iedName, String subType, String ref, String desc) {
		if (LEVEL.ERROR == level) {
			addError(problems, iedName, subType, ref, desc);
		} else {
			addWarning(problems, iedName, subType, ref, desc);
		}
	}

	/**
	 * IED内部四种控制块属性值为空/缺失/重复检查。
	 * @param iedNode
	 * @param level
	 */
	public static Map<String, Map<String, Object[]>> checkInnerCtrl(Element iedNode, Element commNode, List<Problem> problems) {
		String iedName = iedNode.attributeValue("name");
		// 控制块需要检查的属性映射（判断是否为空/缺失）
		Map<EnumCtrlBlock, String[]> map = new HashMap<EnumCtrlBlock, String[]>();
		map.put(EnumCtrlBlock.ReportControl, new String[]{"name", "confRev"});
		map.put(EnumCtrlBlock.LogControl,  new String[]{"name", "logName", "intgPd"});
		map.put(EnumCtrlBlock.GSEControl,  new String[]{"name", "appID", "confRev"});
		map.put(EnumCtrlBlock.SampledValueControl,  new String[]{"name", "smvID", "confRev", "smpRate", "nofASDU"});
		map.put(EnumCtrlBlock.SettingControl,  new String[]{"numOfSGs"});
		String maxAttr = DOM4JNodeHelper.getAttributeValue(iedNode, "./scl:Services/scl:ConfDataSet/@maxAttributes");
		int fcdaMax = -1;
		if (StringUtil.isEmpty(maxAttr)) {
			addWarning(problems, iedName, "数据集成员最大数", "", "属性" + checkProp(maxAttr));
		} else {
			if (StringUtil.isNumeric(maxAttr)) {
				fcdaMax = Integer.parseInt(maxAttr);
			} else {
				addError(problems, iedName, "数据集成员最大数", "", "属性为非整数值 " + maxAttr);
			}
		}
		String xpath = "./scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0/*[name()='ReportControl' or name()='LogControl' or name()='GSEControl' " +
				"or name()='SampledValueControl' or name()='SettingControl']";
		List<?> els = DOM4JNodeHelper.selectNodes(iedNode, xpath);
		List<String> listNames = new ArrayList<String>();
		List<String> appIDNames = new ArrayList<String>();
		Map<String, Map<String, Object[]>> pubMap = new HashMap<>();
		int cbIndex = 0;
		for (Object obj : els) {
			Element elCB = (Element) obj;
			Element elLN = elCB.getParent();
			Element elLD = elLN.getParent();
			String ldInst = elLD.attributeValue("inst");
			String cbType = elCB.getName(); 					// 控制块类型
			String dsName = elCB.attributeValue("datSet");		// 控制块数据集名称
			String cbName = elCB.attributeValue("name"); 		// 控制块名称
			String desc = DOM4JNodeHelper.getAttribute(elCB, "desc");// 控制块描述
			EnumCtrlBlock block = EnumCtrlBlock.valueOf(cbType);
			Element dsEl = null;
			String cbRef = (EnumCtrlBlock.SettingControl != block) ? SCL.getCbRef(ldInst, cbName, block) : "";
//			boolean isNull = cbName!=null && "NULL".equals(cbName.toUpperCase());
			// 检查控制块名称是否为空/缺失/重复
			if ("".equals(checkProp(cbName))) {
				String key = cbType + "." + ldInst + "." + cbName;
				if(listNames.contains(key)) {
					addError(problems, iedName, block.getDesc(), cbRef, "名称 " + cbName + " 被重复使用。");
				} else {
					listNames.add(key);
				}
			}
			// 检查控制块数据集是否存在
			if (EnumCtrlBlock.SettingControl != block) {
				String attrMsg = checkProp(dsName);
				if (!"".equals(attrMsg)) { // 为空或缺失
					addError(problems, iedName, block.getDesc(), cbRef, "属性 datSet " + attrMsg + "。");
				} else {
					String dsXpath = "./scl:DataSet[@name='" + dsName + "']";
					dsEl = DOM4JNodeHelper.selectSingleNode(elLN, dsXpath);
					if (dsEl == null) {
						addError(problems, iedName, block.getDesc(), cbRef, "对应的数据集 " + dsName +  " 不存在。");
					} else {
						List<Element> elFCDAs = dsEl.elements("FCDA");
						int fsize = elFCDAs.size();
						if (fcdaMax > -1 && fsize > fcdaMax) {
							addError(problems, iedName, "数据集成员最大数", dsName, "实际为 " + fsize + " 超过预设范围 " + fcdaMax);
						}
					}
				}
			}
			// 检查控制块属性是否为空/缺失
			String[] warnAttrs = {"smpRate", "nofASDU", "intgPd"};// 告警级别的错误。
			String[] attrs = map.get(block);
			boolean hasName = true;
			for (String attr : attrs) {
				String value = elCB.attributeValue(attr);
				String attrMsg = checkProp(value);
				boolean isEmpty = !"".equals(attrMsg);
				if (isEmpty) { // 为空或缺失
					if ("name".equals(attr)) {
						hasName = false;
						String loc = StringUtil.isEmpty(desc) ? cbRef : desc;
						addError(problems, iedName, block.getDesc(), loc, "属性 " + attr +  " " + attrMsg + "。");
					} else {
						if (Arrays.asList(warnAttrs).indexOf(attr) > -1) {
							if ((!"intgPd".equals(attr)||"".equals(value)))
								addWarning(problems, iedName, block.getDesc(), cbRef, "属性 " + attr +  " " + attrMsg + "。");
						} else {
							addError(problems, iedName, block.getDesc(), cbRef, "属性 " + attr +  " " + attrMsg + "。");
						}
					}
				} else  {
					if ("logName".equals(attr)) {
						if (!DOM4JNodeHelper.existsNode(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + value + "']")) {
							addError(problems, iedName, block.getDesc(), cbRef, "属性 " + attr +  
									" 值为 " + value + " ， 属性值错误，不存在此逻辑设备。");
						}
					}
					else if ("confRev".equals(attr) || "intgPd".equals(attr) || "numOfSGs".equals(attr)) {
						LEVEL level = Arrays.asList(warnAttrs).indexOf(attr) > -1 ? LEVEL.WARNING : LEVEL.ERROR;
						if (!DataTypeChecker.checkInteger(value)) {
							addProblem(level, problems, iedName, block.getDesc(), cbRef, "属性 " + attr +  " 必须为整数，实际为 " + value + " 。");
						} else {
							int iv = Integer.parseInt(value);
							if (iv < 0)
								addProblem(level, problems, iedName, block.getDesc(), cbRef, "属性 " + attr +  " 必须大于等于零，实际为 " + value + " 。");
						}
					}
					else if (attr.equals("appID") || attr.equals("smvID")) { //  || attr.equals("rptID"
						String key1 = ldInst + "." + value;
						if (appIDNames.contains(key1)) {
							addError(problems, iedName, block.getDesc(), cbRef, "属性 " +  attr +  " 的值 " + value + " 被重复使用。");
						} else { 
							appIDNames.add(key1);
						}
					}
				}
			}
			// 缓存cb信息
			if (hasName) {
				if (EnumCtrlBlock.GSEControl==block || EnumCtrlBlock.SampledValueControl==block) {
					// 检查通信参数是否存在
					String cbApXpath = "./scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName +
							"']/scl:" + block.getCbName() + "[@cbName='" + cbName + "'][@ldInst='" + ldInst + "']";
					Element cbAp = DOM4JNodeHelper.selectSingleNode(commNode, cbApXpath);
					if (cbAp == null) {
						addError(problems, iedName, block.getDesc(), cbRef, "未设置通信参数");
					}
					cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
					Map<String, Object[]> iedCbs = pubMap.get(iedName);
					if (iedCbs == null) {
						iedCbs = new HashMap<String, Object[]>();
						pubMap.put(iedName, iedCbs);
					}
					iedCbs.put(cbRef, new Object[]{elCB, dsEl, cbIndex});
					cbIndex++;
				}
			}
		}
		return pubMap;
	}
	
	/**
	 * 控制块属性缺失、为空检查
	 * @param value
	 * @return
	 */
	public static String checkProp(String value) {
		if (value == null)
			return "缺失";
		else if (value.equals("") || "NULL".equals(value.toUpperCase()))
			return "为空";
		return "";
	}
}
