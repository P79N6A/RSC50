/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.check;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.common.DefaultInfo;
import com.shrcn.business.scl.das.DataTemplateDao;
import com.shrcn.business.scl.das.DataTypeTemplateDao;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.navgtree.LNInfo;
import com.shrcn.business.scl.util.CheckUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ListUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.valid.DataTypeChecker;
import com.shrcn.found.common.valid.ValidatorStatus;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 吴云华(mailto:wyh@shrcn.com)
 * @version 1.0, 2009-7-9
 */

public class ModelChecker {
	
	/**
	 * 装置外部对当前装置某信号是否存在关联引用；用于检查FCDA删除后对当前模型的影响。
	 * @param iedName
	 * @return
	 */
	public static boolean existOuterRef(String iedName, Element fcda) {
		String ldInst = fcda.attributeValue("ldInst", "");
		String prefix = fcda.attributeValue("prefix", "");
		String lnClass = fcda.attributeValue("lnClass", "");
		String lnInst = fcda.attributeValue("lnInst", "");
		String doName = fcda.attributeValue("doName", "");
		String xpath = "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/*[@iedName='" + iedName +
				"'][@ldInst='" + ldInst +
				"']" + SCL.getLNAtts(prefix, lnClass, lnInst) +
				"[@doName='" + doName + "']";
		return XMLDBHelper.existsNode(xpath);
	}
	
	/**
	 * 装置内部对信号的引用，包括数据集FCDA、信号关联intAddr和LN实例化引用；用于检查
	 * 数据模板变化对当前模型的影响。
	 * @param iedName
	 * @return
	 */
	public static String checkInnerRef(String lnId, String doName) {
		List<Element> refIEDs = getRefIEDs(lnId, doName);
		StringBuilder sbRefs = new StringBuilder();
		for (Element ied : refIEDs) {
			String iedName = ied.attributeValue("name", "");
			List<?> lds = ied.elements();
			for (Object ld : lds) {
				List<?> lns = ((Element) ld).elements();
				for (Object obj : lns) {
					Element ln = (Element) obj;
					String doRef = iedName + ln.attributeValue("doRef", "");
					boolean intRef = Boolean.valueOf(ln.attributeValue("intRef", ""));
					boolean outRef = Boolean.valueOf(ln.attributeValue("outRef", ""));
					sbRefs.append("已实例化为 " + doRef + " ");
					if (intRef)
						sbRefs.append("，且被用作开入虚端子");
					if (outRef)
						sbRefs.append("，又被数据集引用");
				}
			}
		}
		return sbRefs.toString();
	}

	public static List<Element> getRefIEDs(String lnId, String doName) {
		List<Element> refIEDs = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED" +
					" where exists($ied/scl:AccessPoint/scl:Server/scl:LDevice/*[@lnType='" + lnId + "'][exists(./*[@name='" + doName + "'])])" +
					" return <IED name='{$ied/@name}'>{" +
					" for $ld in $ied/scl:AccessPoint/scl:Server/scl:LDevice " +
					" where exists($ld/*[@lnType='" + lnId + "'][exists(./*[@name='" + doName + "'])])" +
					" return <LDevice inst='{$ld/@inst}'>{" +
					" for $ln in $ld/*[@lnType='" + lnId + "'][exists(./*[@name='" + doName + "'])] return " +
					" let $doRef:=concat($ld/@inst, '/', $ln/@prefix, $ln/@lnClass, $ln/@inst, '.', '" + doName + "')" +
					" return element LN {" +
					"$ln/@*, " +
					"attribute doRef { $doRef }, " +
					"attribute intRef { exists($ied/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/*[contains(@intAddr, $doRef)]) }, " +
					"attribute outRef { exists($ied/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:DataSet" +
					"/*[@ldInst=$ld/@inst]" +
					"[not(@prefix) or (not(@prefix) and $ln/@prefix='') or (exists($ln/@prefix) and @prefix=$ln/@prefix)]" +
					"[@lnClass=$ln/@lnClass][@lnInst=$ln/@inst]" +
					"[@doName='" + doName + "']) }}" +
					"}</LDevice>" +
					"}</IED>";
			refIEDs = XMLDBHelper.queryNodes(xquery);
		} else { 
			List<Element> iedList = XMLDBHelper.selectNodesOnlyAtts(SCL.XPATH_IED, "IED");
			for (Element iedEl : iedList) {
				Element iedCpEl = iedEl.createCopy();
				String name = iedEl.attributeValue("name");
				String ldXPath = SCL.XPATH_IED + "[@name='"+ name +"']/scl:AccessPoint/scl:Server/scl:LDevice";
				List<Element> ldEls = XMLDBHelper.selectNodes(ldXPath);
				List<String> intAddrs = XMLDBHelper.getAttributeValues(ldXPath + "/*/scl:Inputs/@intAddr");
				for (Element ldEl : ldEls) {
					String ldInst = ldEl.attributeValue("inst");
					List<Element> lnEls = DOM4JNodeHelper.selectNodes(ldEl, "./*[@lnType='" + lnId + "'][count(./*[@name='" + doName + "'])>0]");
					for (Element lnEl : lnEls) {
						Element ldCpEl = DOM4JNodeHelper.selectSingleNode(iedCpEl, "./scl:LDevice");
						if (ldCpEl == null) {
							ldCpEl = iedCpEl.addElement("LDevice");
							ldCpEl.addAttribute("desc", ldEl.attributeValue("desc"));
							ldCpEl.addAttribute("inst", ldInst);
						}
						
						// 创建LN节点
						Element lnCpEl = ldCpEl.addElement("LN");
						DOM4JNodeHelper.copyAttributes(lnEl, lnCpEl);
						String prefix = lnEl.attributeValue("prefix");
						String lnClass = lnEl.attributeValue("lnClass");
						String lnInst = lnEl.attributeValue("inst");
						String doRef = ldInst + "/" + prefix + lnClass + lnInst + "." + doName;
						lnCpEl.addAttribute("doRef", doRef);
						String hasIntAddr = "false";
						for (String intAddr : intAddrs) {
							if (intAddr.contains(doRef)) {
								hasIntAddr = "true";
								break;
							}
						}
						lnCpEl.addAttribute("intRef", hasIntAddr);
						boolean existsNode = DOM4JNodeHelper.existsNode(ldEl, "./*/scl:DataSet/*[@ldInst='" + ldInst
								+ "']" + SCL.getLNAtts(prefix, lnClass, lnInst) + "[@doName='" + doName + "']");
						lnCpEl.addAttribute("outRef", String.valueOf(existsNode));
						if (!refIEDs.contains(iedCpEl))
							refIEDs.add(iedCpEl);
					}
				}
			}
		}
		return refIEDs;
	}
	
	/**
	 * 检查IP配置。
	 * 
	 * @param items
	 * @param map
	 */
	public static String checkMMS(String iedName, Element elAddr, LEVEL level) {
		String msg = "";
		String ip = DOM4JNodeHelper.getNodeValueByXPath(elAddr, "./scl:P[@type='IP']");
		if (StringUtil.isEmpty(ip)) {
			msg += level.getPrefix() + "装置 " + iedName + " 的IP地址未设置。";
		} else {
			if (!DataTypeChecker.checkIP(ip)) {
				msg += level.getPrefix() + "装置 " + iedName + " 的IP地址 " + ip + " 无效。";
			}
		}
		return msg;
	}
	
	/**
	 * 判断属性是否存在、属性是否正确
	 * @param cbEl
	 * @param cbName
	 * @param block
	 * @param msg
	 */
	public static String checkCbProps(String iedName, Element cbEl, LEVEL level) {
		EnumCtrlBlock block = EnumCtrlBlock.GSEControl.getCbName().equals(cbEl.getName()) ?
				EnumCtrlBlock.GSEControl : EnumCtrlBlock.SampledValueControl;
		StringBuilder msg = new StringBuilder(); 
		String cbName = cbEl.attributeValue("cbName");
		String[] props = {"MAC-Address", "APPID", "VLAN-ID", "VLAN-PRIORITY"};
		String[] props1 = {"MAC", "APPID", "VLANID", "VLANP"};
		int i = 0;
		String ldInst = cbEl.attributeValue("ldInst");
		for (String prop : props) {
			String xpath = "./scl:Address/scl:P[@type='" + prop + "']";
			String value = DOM4JNodeHelper.getNodeValueByXPath(cbEl, xpath);
			if (StringUtil.isEmpty(value)) {
				String cbRef = SCL.getCbRef(ldInst, cbName, block);
				msg.append(level.getPrefix() + "装置 " + iedName + " 的" + block.getDesc() + " " + cbRef + " 缺少属性 \"" + prop + "\"。");
			} else {
				msg.append(checkAttr(iedName, ldInst, cbName, block, prop, props1, i, value, level));
			}
			i++;
		}
		// 检查GSE的MinTime、MaxTime
		if (EnumCtrlBlock.GSEControl == block) {
			msg.append(checkAttr(cbEl, iedName, block, level));
		}
		return msg.toString();
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
	private static String checkAttr(String iedName, String ldInst, String cbName, 
			EnumCtrlBlock block, String prop, String[] props1, int i, String value, LEVEL level) {
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
				String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
				return level.getPrefix() + block.getDesc() + " " + cbRef + " 的 " + prop + " 属性值 " + value +
						" 错误，" + vs.getMessage() + "。"; 
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
		return "";
	}

	/**
	 * 检查缺少属性信息
	 * 
	 * @param iedName
	 * @param el
	 * @param str
	 * @return
	 */
	private static String checkAttr(Element el, String iedName, EnumCtrlBlock block, LEVEL level) {
		StringBuilder msg = new StringBuilder();
		String ldInst = el.attributeValue("ldInst");
		String cbName = el.attributeValue("cbName");
		String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
		String[] xpaths = {"MinTime", "MaxTime"};
		int minTime = -1, maxTime = -1;
		for (String xpath : xpaths) {
			String time = DOM4JNodeHelper.getNodeValueByXPath(el, "./scl:" + xpath);
			if (StringUtil.isEmpty(time)) {
				msg.append(level.getPrefix() + block.getDesc() + " " + cbRef + " 的属性 " + xpath + " 不存在。");
			} else {
				if (DataTypeChecker.checkDigit(time)) {
					if ("MinTime".equals(xpath)) {
						minTime = Integer.parseInt(time);
					} else {
						maxTime = Integer.parseInt(time);
					}
				} else {
					msg.append(level.getPrefix() + block.getDesc() + " " + cbRef + " 的属性 " + xpath + " 不是整数。");
				}
			}
		}
		if (minTime > 0 && maxTime > 0) {
			if (minTime >= maxTime) {
				msg.append(level.getPrefix() + block.getDesc() + " " + cbRef + " 的MinTime属性值 " + minTime +
						" 不比MaxTime属性值 " + maxTime + " 小。");
			}
		}
		return msg.toString();
	}
	
	/**
	 * 检查<ConnectedAP>中指定装置的指定控制块是否存在，并返回提示信息。
	 * @param iedName
	 * @param level
	 * @return 错误提示
	 */
	public static String checkCbInIed(String iedName, LEVEL level) {
		StringBuilder msg = new StringBuilder();
		String prefix = level.getPrefix();
		List<Element> subNets = XMLDBHelper.selectNodes(SCL.XPATH_SUBNETWORK);
		Map<String, String> map = new HashMap<String, String>();
		for (Element subNet : subNets) {
			String netName = subNet.attributeValue("name");
			List<Element> cbs = DOM4JNodeHelper.selectNodes(subNet, 
					"./*[name()='ConnectedAP'][@iedName='" + iedName + "']/*[name()='GSE' or name()='SMV']");
			for (Element cb : cbs) {
				String ndName = cb.getName();
				String cbName = cb.attributeValue("cbName"); //$NON-NLS-1$
				String ldInst = cb.attributeValue("ldInst"); //$NON-NLS-1$
				EnumCtrlBlock block = EnumCtrlBlock.GSEControl.getCbName().equals(ndName)
						? EnumCtrlBlock.GSEControl : EnumCtrlBlock.SampledValueControl;

				String xpath = "/scl:SCL/scl:IED[@name='" + iedName
						+ "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst
						+ "']/*/scl:" + block.name() + "[@name='" + cbName + "']";
				String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
				if (!XMLDBHelper.existsNode(xpath)) {
					msg.append(prefix + "子网 " + netName + " 的" + block.getDesc() + " " + cbRef + " 在装置模型中不存在。");
				} else {
					// 检查通信参数合法性
					msg.append(checkCbProps(iedName, cb, level));
					if (map.containsKey(cbRef)) {
						String net = map.get(cbRef);
						String pre = LEVEL.WARNING.getPrefix();
						if (net.equals(netName)) {
							msg.append(pre + "" + block.getDesc() + " " + cbRef + " 在子网 " + net + " 中存在重复配置。");
						} else {
							if (!net.equals(netName)) {
								msg.append(pre + "" + block.getDesc() + " " + cbRef + " 同时出现在 " + net + " 和 " + netName + " 两个子网中。");
							} else {
								msg.append(pre + "" + block.getDesc() + " " + cbRef + " 在子网 " + net + " 中存在重复配置。");
							}
						}
					} else {
						map.put(cbRef, netName);
					}
				}
			}
		}
		return msg.toString();
	}
	
	/**
	 * 检查关联信号开出虚端子是否存在
	 * @param iedName
	 * @param extRef
	 */
	public static String checkOuterSignal(String iedName, Element extRef) {
		LEVEL level = LEVEL.WARNING;
		String exiedName = DOM4JNodeHelper.getAttribute(extRef, "iedName");
		String exldInst = DOM4JNodeHelper.getAttribute(extRef, "ldInst"); 
		String prefix = DOM4JNodeHelper.getAttribute(extRef, "prefix"); 
		String exlnClass = DOM4JNodeHelper.getAttribute(extRef, "lnClass"); 
		String exlnInst = DOM4JNodeHelper.getAttribute(extRef, "lnInst"); 
		String exdoName = DOM4JNodeHelper.getAttribute(extRef, "doName");
		if (!checkIsComplete(exiedName, exldInst, exlnClass, exdoName)) {
			return LEVEL.ERROR.getPrefix() + "装置 " + iedName + " 的外部虚端子不完整 " + extRef.asXML() + " 。";
		} else if (!XMLDBHelper.existsNode("/scl:SCL/scl:IED[@name='" + exiedName + "']")) {
			return ""; // ModelVerifier.inputsCheck()统一处理
		}
		String exdaName = DOM4JNodeHelper.getAttribute(extRef, "daName");
		String daAtt = SCL.getDaAtts(exdaName);
		String fcdaXpath = "/scl:SCL/scl:IED[@name='" + exiedName
				+ "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:DataSet/scl:FCDA"
				+ SCL.getLNAtts(prefix, exlnClass, exlnInst)
				+ "[@ldInst='" + exldInst + "'][@doName='" + exdoName + "']"
				+ daAtt;
		if (!XMLDBHelper.existsNode(fcdaXpath)) {
			return level.getPrefix() + "装置 " + iedName + " 关联的外部虚端子 " + SCL.getNodeFullRef(extRef) +
					" 在装置 " + exiedName + " 的数据集中不存在。";
		}
		return "";
	}
	
	private static boolean checkIsComplete(String...atts) {
		for (String att : atts) {
			if (StringUtil.isEmpty(att))
				return false;
		}
		return true;
	}
	
	/**
	 * 检查关联信号开入虚端子是否存在
	 * @param iedName
	 * @param extRef
	 */
	public static String checkInnerSignal(String iedName, Element extRef, 
			Element dataTypeTemplates, Map<String, List<String>> lnTypeCache) {
		String intAddr = DOM4JNodeHelper.getAttribute(extRef, "intAddr");
		if (StringUtil.isEmpty(intAddr)) {
			return LEVEL.ERROR.getPrefix() + "装置 " + iedName + " 的内部虚端子不完整 " + extRef.asXML() + " 。";
		}
		String msg = "";
		String[] split = intAddr.split(":");
		if(split != null && split.length == 2) {
			intAddr = split[1];
		}
		String lnType = DOM4JNodeHelper.getAttribute(extRef, "lnType");
		String outChkMsg = checkOuterSignal(iedName, extRef);
		msg += outChkMsg;
		String intChkMsg = checkIntAddr(iedName, intAddr, lnType, dataTypeTemplates, lnTypeCache);
		msg += intChkMsg;
		// 虚端子类型不匹配（DO/DA、bType for GOOSE、CDC for SV）
		if ("".equals(outChkMsg) && "".equals(intChkMsg)) {
			if (intAddr.indexOf(":")>-1) {
				intAddr = intAddr.split(":")[1];
			}
			String[] innerInfo = intAddr.split("\\.");
			if (innerInfo.length > 1) {
				String outDaName = extRef.attributeValue("daName");
				boolean chkCDC = (innerInfo.length==2) || (outDaName==null);
				String exiedName = extRef.attributeValue("iedName");
				String exldInst = extRef.attributeValue("ldInst");
				String outLnType = XMLDBHelper.getAttributeValue(SCL.getLDXPath(exiedName, exldInst) 
						+ SCL.getFcdaLNXPath(extRef) + "/@lnType");
				String doName = innerInfo[1];
				if (chkCDC) {
					String inCDC = DataTypeTemplateDao.getCDC(dataTypeTemplates, lnType, doName);
					String outCDC = DataTypeTemplateDao.getCDC(dataTypeTemplates, outLnType, extRef.attributeValue("doName"));
					if (checkIsComplete(inCDC, outCDC) && !inCDC.equals(outCDC)) {
						msg += "\n错误：装置 " + iedName + " 的虚端子关联 cdc 类型不匹配，内部 " + intAddr +
								" 为 " + inCDC + " 外部 " + SCL.getNodeFullRef(extRef) + " 为 " + outCDC + " 。";
					}
				} else {
					String inBType = DataTypeTemplateDao.getBType(dataTypeTemplates, lnType, doName, innerInfo[2]);
					String outBType = DataTypeTemplateDao.getBType(dataTypeTemplates, outLnType, extRef.attributeValue("doName"), outDaName);
					if (checkIsComplete(inBType, outBType) && !inBType.equals(outBType)) {
						msg += "\n错误：装置 " + iedName + " 的虚端子关联 bType 类型不匹配，内部 " + intAddr +
								" 为 " + inBType + " 外部 " + SCL.getNodeFullRef(extRef) + " 为 " + outBType + " 。";
					}
				}
			}
		}
		return msg;
	}
	
	public static String checkIntAddr(String iedName, String intAddr, String lnType,
			Element dataTypeTemplates, Map<String, List<String>> lnTypeCache) {
		if (intAddr.indexOf(":")>-1) {
			intAddr = intAddr.split(":")[1];
		}
		int posDot = intAddr.indexOf('.');
		if (posDot < 0) {
			return "\n错误：装置 " + iedName + " 的内部虚端子 " + intAddr + " 引用不完整。";
		}
		String lnName = intAddr.substring(0, posDot);
		String ref = intAddr.substring(posDot + 1);
		
		List<String> lnTypeRefs = lnTypeCache.get(lnType);
		if (lnTypeRefs == null) {
			lnTypeRefs = DataTemplateDao.getLnTypeSubRef(lnType, dataTypeTemplates);
			if (lnTypeRefs == null) {
				if (StringUtil.isEmpty(lnType)) {
					return "\n错误：装置 " + iedName + " 的内部虚端子 " + intAddr + " 对应的LN " + lnName + "不存在。";
				} else {
					// 日志：lnType不存在
					return "\n错误：逻辑节点 " + iedName + lnName + " 的类型 " + lnType + " 在数据模板中未定义。";
				}
			}
			if (lnTypeRefs != null)
				lnTypeCache.put(lnType, lnTypeRefs);
		}
		boolean contains = ListUtil.search(lnTypeRefs, ref) > -1;
		if (!contains) {
			// 日志：DO,DA不存在
			return "\n错误：装置 " + iedName + " 的内部虚端子 " + intAddr + " 不存在。";
		}
		return "";
	}
	
	/**
	 * 检查当前SCD的一二次关联是否合理
	 * @return
	 */
	public static List<String> checkLNodesRelated() {
		List<String> msgs = new ArrayList<String>();
		Element subStationElement = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		if (subStationElement == null)
			return msgs;
		List<Element> lstLNode = DOM4JNodeHelper.selectNodes(subStationElement, ".//*[name()='LNode']");
		if (lstLNode == null || lstLNode.size() == 0)
			return msgs;
		for (Element lnode : lstLNode) {
			String iedName = lnode.attributeValue("iedName");
			if (DefaultInfo.IED_NAME.equals(iedName))
				continue;
			String ldInst = lnode.attributeValue("ldInst");
			String lnClass = lnode.attributeValue("lnClass");
			String lnInst = lnode.attributeValue("lnInst");
			String prefix = lnode.attributeValue("prefix");
			String oldLNType = lnode.attributeValue("lnType");
			LNInfo lnInfo = new LNInfo(prefix, lnClass, lnInst);
			String lnTypePath = iedName + "/" + ldInst + "." + lnInfo.toString() + ":" + oldLNType;
			String eqpPath = SCL.getEqpPath(lnode);
			String iedXpath = SCL.getIEDXPath(iedName);
			if (!XMLDBHelper.existsNode(iedXpath)) {
				msgs.add("错误：关联IED已不存在，设备[" + eqpPath + "]，LNode["+ lnTypePath + "]。\n");
				continue;
			}
			String lnXpath = iedXpath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
					"']" + SCL.getLNXPath(prefix, lnClass, lnInst);
			String lnType = XMLDBHelper.getAttributeValue(lnXpath + "/@lnType");
			if (StringUtil.isEmpty(lnType)) {
				msgs.add("错误：关联LN已不存在，设备[" + eqpPath + "]，LNode["+ lnTypePath + "]。\n");
			} else if (!oldLNType.equals(lnType)) {
				msgs.add("错误：lnType属于与实际值[" + lnType + "]不匹配，设备[" + eqpPath + "]，LNode["+ lnTypePath + "]。\n");
			}
		}
		Collections.sort(msgs);
		return msgs;
	}

	/**
	 * IED内部四种控制块属性值为空/缺失/重复检查。
	 * @param iedNode
	 * @param level
	 */
	public static String checkInnerCtrl(Element iedNode, LEVEL level) {
		String msg = "", prefix = level.getPrefix();
		String iedName = iedNode.attributeValue("name");
		// 控制块需要检查的属性映射（判断是否为空/缺失）
		Map<EnumCtrlBlock, String[]> map = new HashMap<EnumCtrlBlock, String[]>();
		map.put(EnumCtrlBlock.ReportControl, new String[]{"name", "rptID", "confRev"});
		map.put(EnumCtrlBlock.LogControl,  new String[]{"name", "logName"});
		map.put(EnumCtrlBlock.GSEControl,  new String[]{"name", "appID", "confRev"});
		map.put(EnumCtrlBlock.SampledValueControl,  new String[]{"name", "smvID", "confRev"});
		map.put(EnumCtrlBlock.SettingControl,  new String[]{"numOfSGs"});
		
		String xpath = "./scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0/*[name()='ReportControl' or name()='LogControl' or name()='GSEControl' " +
				"or name()='SampledValueControl' or name()='SettingControl']";
		List<?> els = DOM4JNodeHelper.selectNodes(iedNode, xpath);
		List<String> listNames = new ArrayList<String>();
		List<String> appIDNames = new ArrayList<String>();
		for (Object obj : els) {
			Element elCB = (Element) obj;
			Element elLD = elCB.getParent().getParent();
			String ldInst = elLD.attributeValue("inst");
			String cbType = elCB.getName(); 					// 控制块类型
			String dsName = elCB.attributeValue("datSet");		// 控制块数据集名称
			String cbName = elCB.attributeValue("name"); 		// 控制块名称
			String desc = DOM4JNodeHelper.getAttribute(elCB, "desc");// 控制块描述
			EnumCtrlBlock block = EnumCtrlBlock.valueOf(cbType);
			String cbRef = (EnumCtrlBlock.SettingControl != block) ? SCL.getCbRef(ldInst, cbName, block) : "";
			// 检查控制块名称是否为空/缺失/重复
			if (!StringUtil.isEmpty(cbName)) {
				String key = cbType + "." + ldInst + "." + cbName;
				if(listNames.contains(key)) {
					msg += prefix + "装置 " + iedName + " 的逻辑设备 " + ldInst + " 中存在名称同为 " + cbName +  " 的" + block.getDesc() + "。";
				} else {
					listNames.add(key);
				}
			}
			// 检查控制块数据集是否存在
			if (EnumCtrlBlock.SettingControl != block) {
				String attrMsg = checkProp(dsName);
				if (!"".equals(attrMsg)) { // 为空或缺失
					msg += prefix + "装置 " + iedName + " 的" + block.getDesc() + " " + cbRef + " 的属性 datSet " + attrMsg + "。";
				} else {
					String dsXpath = "./*/scl:DataSet[@name='" + dsName + "']";
					if (!DOM4JNodeHelper.existsNode(elLD, dsXpath)) {
						msg += prefix + "装置 " + iedName + " 的" + block.getDesc() + " " + cbRef + " 对应的数据集 " + dsName +  " 不存在。";
					}
				}
			}
			// 检查控制块属性是否为空/缺失
			String[] attrs = map.get(block);
			for (String attr : attrs) {
				String value = elCB.attributeValue(attr);
				String attrMsg = checkProp(value);
				if (!"".equals(attrMsg)) { // 为空或缺失
					if ("name".equals(attr)) {
						msg += prefix + "装置 " + iedName + " 的逻辑设备 " + ldInst +
								" 下的" + block.getDesc() + " " + desc + " 的属性 " + attr +  " " + attrMsg + "。";
					} else {
						msg += prefix + "装置 " + iedName + " 的" + block.getDesc() + " " + cbRef + " 的属性 " + attr +  " " + attrMsg + "。";
					}
				} else if (EnumCtrlBlock.LogControl == block && "logName".equals(attr)) {
					if (!DOM4JNodeHelper.existsNode(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + value + "']")) {
						msg += prefix + "装置 " + iedName + " 的" + block.getDesc() + " " + cbRef + " 的属性 " + attr +  
								" 值为 " + value + " ， 属性值错误，不存在此逻辑设备。";
					}
				}
				if ((attr.equals("appID") || attr.equals("smvID")) && StringUtil.isEmpty(attrMsg)){
					String key1 = ldInst + "." + value;
					if (appIDNames.contains(key1)) {
						msg += prefix + "装置 " + iedName + " 的逻辑设备 " + ldInst + " 中存在 " +  attr +  " 属性值同为" + value + " 的" + block.getDesc() + "。";
					} else { 
						appIDNames.add(key1);
					}
				}
			}
		}
		return msg;
	}
	
	/**
	 * 控制块属性缺失、为空检查
	 * @param value
	 * @return
	 */
	public static String checkProp(String value) {
		if (value == null)
			return "缺失";
		else if (value.equals(""))
			return "为空";
		return "";
	}
}
