/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.synet.tool.rsc.io.scd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.das.CommunicationDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2016-12-27
 */
public class IedSubInfoDao {

//	/**
//	 * 查询IED类型
//	 * @param iedInScd
//	 * @return
//	 */
//	public static String getIEDType(String iedInScd) {
//		return XMLDBHelper.getAttributeValue(SCL.getIEDXPath(iedInScd) + "/@type");
//	}
//
//	/**
//	 * 读取界面GOOSE数据.
//	 * 
//	 * @param iedName
//	 * @return
//	 */
//	public static List<Element> getGooseCfgPub(String iedName) {
//		if (Constants.XQUERY) {
//			String xquery = "let $root:="
//					+ XMLDBHelper.getDocXPath()
//					+ "/scl:SCL, $ied:=$root/scl:IED[@name='"
//					+ iedName
//					+ "'] return for $ap in $ied/scl:AccessPoint return for $LD in "
//					+ "$ap/scl:Server/scl:LDevice return for $gse in $LD/scl:LN0/scl:GSEControl return "
//					+ "let $ds:=$LD/*/scl:DataSet[@name=$gse/@datSet] return element cb {attribute cbName{$gse/@name}, attribute cbId{$gse/@appID}, attribute ldInst{$LD/@inst}, "
//					+ "for $fcda in $ds/scl:FCDA return let $ln:=$LD/*"
//					+ "[((@prefix=$fcda/@prefix or ((not(@prefix) or @prefix='') and (not($fcda/@prefix) or $fcda/@prefix=''))) and (@lnClass=$fcda/@lnClass)"
//					+ "and (@inst=$fcda/@lnInst or ((not(@inst) or @inst='') and (not($fcda/@lnInst) or $fcda/@lnInst='')))) or (@lnClass=$fcda/@lnClass and @lnClass='LLN0')]"
//					+ " return element fcda {$fcda/@*}}";
//			return XMLDBHelper.queryNodes(xquery);
//		} 
//		return getCfgPub(iedName, "scl:GSEControl");
//	}
//
//	/**
//	 * 读取界面SMV数据.
//	 * 
//	 * @param iedName
//	 * @return
//	 */
//	public static List<Element> getSmvCfgPub(String iedName) {
//		if (Constants.XQUERY) {
//			String xquery = "let $root:="
//					+ XMLDBHelper.getDocXPath()
//					+ "/scl:SCL, $ied:=$root/scl:IED[@name='"
//					+ iedName
//					+ "'] return for $ap in $ied/scl:AccessPoint return for $LD in "
//					+ "$ap/scl:Server/scl:LDevice return for $smv in $LD/scl:LN0/scl:SampledValueControl return "
//					+ "let $ds:=$LD/*/scl:DataSet[@name=$smv/@datSet] return element cb {attribute cbName{$smv/@name}, attribute cbId{$smv/@smvID}, attribute ldInst{$LD/@inst}, "
//					+ "for $fcda at $i in $ds/scl:FCDA return let $ln:=$LD/*"
//					+ "[((@prefix=$fcda/@prefix or ((not(@prefix) or @prefix='') and (not($fcda/@prefix) or $fcda/@prefix=''))) and (@lnClass=$fcda/@lnClass)"
//					+ "and (@inst=$fcda/@lnInst or ((not(@inst) or @inst='') and (not($fcda/@lnInst) or $fcda/@lnInst='')))) or (@lnClass=$fcda/@lnClass and @lnClass='LLN0')] "
//					+ "return element fcda {$fcda/@*}}";
//			return XMLDBHelper.queryNodes(xquery);
//		} 
//		return getCfgPub(iedName, "scl:SampledValueControl");
//	}
//	
//	private static List<Element> getCfgPub(String iedName, String blockName) {
//		List<Element> list = new ArrayList<Element>();
//		List<Element> ldEls = XMLDBHelper.selectNodes(SCL.XPATH_IED + "[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[count(./scl:LN0/"+blockName+")>0]");
//		for (Element ldEl : ldEls) {
//			String ldInst = ldEl.attributeValue("inst");
//			List<Element> cbEls = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/" + blockName + "");
//			for (Element cbEl : cbEls) {
//				Element el = DOM4JNodeHelper.createSCLNode("cb");
//				String cbName = cbEl.attributeValue("name");
//				String dsName = cbEl.attributeValue("datSet");
//				String cbId = "GSEControl".equals(cbEl.getName()) ? cbEl.attributeValue("appID") : cbEl.attributeValue("smvID");
//				el.addAttribute("cbName", cbName);
//				el.addAttribute("cbId", cbId);
//				el.addAttribute("ldInst", ldInst);
//				List<Element> fcdaEls = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/scl:DataSet[@name='"+ dsName +"']/scl:FCDA");
//				for (Element fcdaEl : fcdaEls) {
//					Element createCopy = fcdaEl.createCopy();
//					createCopy.setName("fcda");
//					el.add(createCopy);
//				}
//				list.add(el);
//			}
//		}
//		return list;
//	}

	/**
	 * 读取界面GOOSE数据.
	 * 
	 * @param iedName
	 * @return
	 */
	public static List<Element> getGooseConfigPub(String iedName) {
		if (Constants.XQUERY) {
			String xquery = "let $root:=" + XMLDBHelper.getDocXPath()
					+ "/scl:SCL, $ied:=$root/scl:IED[@name='" + iedName
					+ "'] return for $ap in $ied/scl:AccessPoint return for $LD in "
					+ "$ap/scl:Server/scl:LDevice return "
					+ "for $gse in $LD/scl:LN0/scl:GSEControl return "
					+ "let $cp:=$root/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName
					+ "' and exists(./scl:GSE[@cbName=$gse/@name and @ldInst=$LD/@inst])], "
					+ "$cpGse:=$cp/scl:GSE[@cbName=$gse/@name and @ldInst=$LD/@inst], $addr:=$cpGse/scl:Address, "
					+ "$ds:=$LD/*/scl:DataSet[@name=$gse/@datSet] "
					+ " where exists($cp/scl:GSE[@cbName=$gse/@name]) return element cb {attribute iedName {$ied/@name}, attribute iedDesc {$ied/@desc}, attribute AP {$ap/@name}, "
					+ "attribute cbRef {concat('" + iedName
					+ "', $LD/@inst, '/LLN0$GO$', $gse/@name)}, attribute cbName {$gse/@name}, attribute cbId {$gse/@appID}, "
					+ "attribute dsRef {concat('" + iedName + "', $LD/@inst, '/LLN0$', $ds/@name)}, attribute dsName {$ds/@name}, attribute dsDesc {$ds/@desc}, "
					+ "attribute appID {$addr/scl:P[@type='APPID']/text()}, attribute mac {$addr/scl:P[@type='MAC-Address']/text()}, "
					+ "attribute vlanID {$addr/scl:P[@type='VLAN-ID']/text()}, attribute priority {$addr/scl:P[@type='VLAN-PRIORITY']/text()}, "
					+ "attribute T0 {$cpGse/scl:MaxTime/text()}, attribute T1 {$cpGse/scl:MinTime/text()}, attribute confRev {$gse/@confRev}, attribute entryNum {count($ds/scl:FCDA)}, "
					+ "for $fcda at $i in $ds/scl:FCDA return let $ln:=$LD/*"
					+ "[((@prefix=$fcda/@prefix or ((not(@prefix) or @prefix='') and (not($fcda/@prefix) or $fcda/@prefix=''))) and (@lnClass=$fcda/@lnClass)"
					+ "and (@inst=$fcda/@lnInst or ((not(@inst) or @inst='') and (not($fcda/@lnInst) or $fcda/@lnInst='')))) or (@lnClass=$fcda/@lnClass and @lnClass='LLN0')]"
					+ " return element fcda {$fcda/@*, attribute lnType {$ln/@lnType}, attribute ref{concat('" + iedName
					+ "', $LD/@inst, '/', $fcda/@prefix, $fcda/@lnClass, $fcda/@lnInst, '$', $fcda/@fc, '$', $fcda/@doName, if(exists($fcda/@daName)) then concat('$', $fcda/@daName) else '')}, attribute idx {$i - 1}}"
					+ "}";
			return XMLDBHelper.queryNodes(xquery);
		}
		return getConfigPub(iedName, "scl:GSEControl", "scl:GSE", "appID", "GO");
	}
	
	/**
	 * 通过装置名称获取采样值控制块信息。 注：smpRate取思源弘瑞装置的默认值80，而不是发送装置配置的值。
	 * 
	 * @param iedName
	 * @return
	 */
	public static List<Element> getSmvConfigPub(String iedName) {
		List<Element> cbEls = IedSubInfoDao.querySmvConfigPub(iedName);
		// 更正通道号
		for (Element cbEl : cbEls) {
			boolean existQ = DOM4JNodeHelper.existsNode(cbEl,
					"./scl:fcda[@daName='q']");
			if (!existQ)
				continue;
			List<?> fcdaEls = cbEl.elements();
			Map<String, Integer> chnIdx = new HashMap<String, Integer>();
			int cur = 0;
			for (Object obj : fcdaEls) {
				Element fcdaEl = (Element) obj;
				String daName = fcdaEl.attributeValue("daName");
				if (!"q".equals(daName)) {
					fcdaEl.addAttribute("idx", cur + "");
					String doRef = SclUtil.getDoRef(fcdaEl, daName);
					chnIdx.put(doRef, cur);
					cur++;
				}
			}
			cbEl.addAttribute(ITagSG.ATTR_ENTRY_NUM, cur + "");
			for (Object obj : fcdaEls) {
				Element fcdaEl = (Element) obj;
				String daName = fcdaEl.attributeValue("daName");
				if ("q".equals(daName)) {
					String doRef = SclUtil.getDoRef(fcdaEl, daName);
					Integer idx = chnIdx.get(doRef);
					idx = idx == null ? 0 : idx;
					fcdaEl.addAttribute("idx", idx + "");
				}
			}
		}
		return cbEls;
	}
	
	/**
	 * 读取界面SMV数据.
	 * 
	 * @param iedName
	 * @return
	 */
	public static List<Element> querySmvConfigPub(String iedName) {
		if (Constants.XQUERY) {
			String xquery = "let $root:="
					+ XMLDBHelper.getDocXPath()
					+ "/scl:SCL, $ied:=$root/scl:IED[@name='" + iedName
					+ "'] return for $ap in $ied/scl:AccessPoint return for $LD in "
					+ "$ap/scl:Server/scl:LDevice return "
					+ "for $smv in $LD/scl:LN0/scl:SampledValueControl return "
					+ "let $cp:=$root/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName
					+ "' and exists(./scl:SMV[@cbName=$smv/@name and @ldInst=$LD/@inst])], "
					+ "$cpSmv:=$cp/scl:SMV[@cbName=$smv/@name and @ldInst=$LD/@inst], $addr:=$cpSmv/scl:Address, "
					+ "$ds:=$LD/*/scl:DataSet[@name=$smv/@datSet]"
					+ " where exists($cp/scl:SMV[@cbName=$smv/@name]) return element cb {attribute iedName {$ied/@name}, attribute iedDesc {$ied/@desc}, attribute AP {$ap/@name}, "
					+ "attribute cbRef {concat('" + iedName + "', $LD/@inst, '/LLN0$MS$', $smv/@name)}, attribute cbName {$smv/@name}, attribute cbId {$smv/@smvID}, "
					+ "attribute dsRef {concat('" + iedName + "', $LD/@inst, '/LLN0$', $ds/@name)}, attribute dsName {$ds/@name}, attribute dsDesc {$ds/@desc}, "
					+ "attribute appID {$addr/scl:P[@type='APPID']/text()}, attribute mac {$addr/scl:P[@type='MAC-Address']/text()}, "
					+ "attribute vlanID {$addr/scl:P[@type='VLAN-ID']/text()}, attribute priority {$addr/scl:P[@type='VLAN-PRIORITY']/text()}, "
					+ "attribute confRev {$smv/@confRev}, attribute asduNum {$smv/@nofASDU}, attribute entryNum {count($ds/scl:FCDA)}, "
					+ "for $fcda at $i in $ds/scl:FCDA return let $ln:=$LD/*"
					+ "[((@prefix=$fcda/@prefix or ((not(@prefix) or @prefix='') and (not($fcda/@prefix) or $fcda/@prefix=''))) and (@lnClass=$fcda/@lnClass)"
					+ "and (@inst=$fcda/@lnInst or ((not(@inst) or @inst='') and (not($fcda/@lnInst) or $fcda/@lnInst='')))) or (@lnClass=$fcda/@lnClass and @lnClass='LLN0')] "
					+ "return element fcda {$fcda/@*, attribute lnType {$ln/@lnType}, "
					+ "attribute ref {concat('" + iedName + "', $LD/@inst, '/', $fcda/@prefix, $fcda/@lnClass, $fcda/@lnInst, '$', $fcda/@fc, '$', $fcda/@doName, if(exists($fcda/@daName)) then concat('$', $fcda/@daName) else '')}, "
					+ "attribute idx {$i - 1}}" + "}";
			return XMLDBHelper.queryNodes(xquery);
		}
		return getConfigPub(iedName, "scl:SampledValueControl", "scl:SMV", "smvID", "MS");
	}

	private static List<Element> getConfigPub(String iedName, String blockName, String block, String cbId, String fc) {
		List<Element> list = new ArrayList<Element>();
		List<Element> commEls = XMLDBHelper.selectNodes(SCL.XPATH_COMMUNICATION + "/scl:SubNetwork/scl:ConnectedAP[@iedName='" + iedName+"']/" + block);
		Map<String, Element> map = new HashMap<>(); 
		for (Element el : commEls) {
			String cbName = el.attributeValue("cbName");
			String ldInst = el.attributeValue("ldInst");
			map.put(ldInst + ":" + cbName, el);
		}
		Element iedEl = IEDDAO.getIEDNode(iedName);
		String iedDesc = iedEl.attributeValue("desc");
		List<Element> ldEls = DOM4JNodeHelper.selectNodes(iedEl,"./scl:AccessPoint/scl:Server/scl:LDevice[count(./scl:LN0/"+blockName+")>0]");
		for (Element ldEl : ldEls) {
			String ldInst = ldEl.attributeValue("inst");
			List<Element> cbEls = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/"+blockName);
			for (Element cbEl : cbEls) {
				Element el = DOM4JNodeHelper.createSCLNode("cb");
				String cbName = cbEl.attributeValue("name");
				String dsName = cbEl.attributeValue("datSet");
				
				Element dsEl = DOM4JNodeHelper.selectSingleNode(ldEl, "./*/scl:DataSet[@name='"+ dsName +"']");
				Element commEl = map.get(ldInst + ":" + cbName);
				if (commEl == null)
					continue;
				el.addAttribute("iedName", iedName);
				el.addAttribute("iedDesc", iedDesc);
				el.addAttribute("AP", ldEl.getParent().getParent().attributeValue("name"));
				el.addAttribute("cbRef", iedName + ldInst+"/LLN0$"+fc+"$"+ cbName);
				el.addAttribute("cbName", cbName);
				el.addAttribute("cbId", cbEl.attributeValue(cbId));
				el.addAttribute("dsRef", iedName + ldInst+ "/LLN0$"+ dsName);
				el.addAttribute("dsName", dsEl.attributeValue("name")); 
				el.addAttribute("dsDesc", dsEl.attributeValue("desc")); 
				Element addEl = commEl.element("Address");
				el.addAttribute("appID", CommunicationDAO.getTypeValue(addEl, "APPID"));
				el.addAttribute("mac", CommunicationDAO.getTypeValue(addEl, "MAC-Address"));
				el.addAttribute("vlanID", CommunicationDAO.getTypeValue(addEl, "VLAN-ID"));
				el.addAttribute("priority", CommunicationDAO.getTypeValue(addEl, "VLAN-PRIORITY"));
				if (cbId.equals("appID")) {
					Element maxEl = commEl.element("MaxTime");
					Element minEl = commEl.element("MinTime");
					el.addAttribute("T0", StringUtil.nullToEmpty(maxEl.getText()));
					el.addAttribute("T1", StringUtil.nullToEmpty(minEl.getText()));
				} else {
					el.addAttribute("asduNum", cbEl.attributeValue("nofASDU"));
				}
				el.addAttribute("confRev", cbEl.attributeValue("confRev"));
				List<Element> fcdaEls = dsEl.elements("FCDA");
				int i = 0;
				for (Element fcdaEl : fcdaEls) {
					Element fcda = fcdaEl.createCopy();
					fcda.setName("fcda");
					String lnType = ExportUtil.getLnType(fcda.attributeValue("prefix"), fcda.attributeValue("lnClass"), fcda.attributeValue("lnInst"), ldEl);
					fcda.addAttribute("lnType", lnType);
					fcda.addAttribute("ref", SclUtil.getCfgRef(iedName, fcda));
					fcda.addAttribute("idx", String.valueOf(i++));
					el.add(fcda);
				}
				el.addAttribute("entryNum", String.valueOf(fcdaEls.size()));
				
				list.add(el);
			}
		}
		return list;
	}

//	/**
//	 * @param saddrA
//	 * @param infoA
//	 * @return
//	 */
//	public static String getDesc(String saddr, Map<String, DSInfo> addrMap) {
//		if (saddr.endsWith("DataPP.bit_zero")) {
//			return "0";
//		} else if (saddr.endsWith("DataPP.bit_one")) {
//			return "1";
//		} else if (saddr.equals("NULL")) {
//			return "NULL";
//		}
//		if (addrMap.get(saddr) == null) {
//			console.append("警告：CID模型的dsDin数据集中找不到单点信号" + saddr + "。");
//			return saddr;
//		}
//		String nullToEmpty = StringUtil.nullToEmpty(addrMap.get(saddr).getDesc());
//		return StringUtil.isEmpty(nullToEmpty) ? StringUtil.nullToEmpty(saddr) : nullToEmpty;
//	}

	/**
	 * 查询装置IP
	 * @param iedName
	 * @return
	 */
	public static String getIPs(String iedName) {
		String xpath = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='"
				+ iedName + "']/scl:Address/scl:P[@type='IP']";
		if (Constants.XQUERY) {
			return XMLDBHelper.getAttributeValue(xpath + "/text()");
		} else {
			return XMLDBHelper.getNodeValue(xpath);
		}
	}

	/**
	 * 检查IP地址.
	 * 
	 * @param iedName
	 * @return
	 */
	public static boolean checkIP(String iedName) {
		return StringUtil.isEmpty(getIPs(iedName));
	}
	
	/**
	 * 查询CB MAC地址
	 * @param type
	 * @param iedName
	 * @param ldInst
	 * @param cbName
	 * @return
	 */
	public static String getMac(String type, String iedName, String ldInst, String cbName) {
		String xpath = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='"
						+ iedName + "']/scl:" + type + "[@ldInst='" + ldInst + "'][@cbName='" + cbName
						+ "']/scl:Address/scl:P[@type='MAC-Address']";
		if (Constants.XQUERY) {
			return XMLDBHelper.getAttributeValue(xpath + "/text()");
		} else {
			return XMLDBHelper.getNodeValue(xpath);
		}
	}
	
}

