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
public class IedInfoDao {

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
		List<Element> cbEls = IedInfoDao.querySmvConfigPub(iedName);
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
				Element addEl = commEl.element("Address");
				el.addAttribute("appID", CommunicationDAO.getTypeValue(addEl, "APPID"));
				el.addAttribute("mac", CommunicationDAO.getTypeValue(addEl, "MAC-Address"));
				el.addAttribute("vlanID", CommunicationDAO.getTypeValue(addEl, "VLAN-ID"));
				el.addAttribute("priority", CommunicationDAO.getTypeValue(addEl, "VLAN-PRIORITY"));
				if (cbId.equals("appID")) {
					Element maxEl = commEl.element("MaxTime");
					Element minEl = commEl.element("MinTime");
					el.addAttribute("T0", (maxEl==null) ? "0" : StringUtil.nullToEmpty(maxEl.getText()));
					el.addAttribute("T1", (minEl==null) ? "0" : StringUtil.nullToEmpty(minEl.getText()));
				} else {
					el.addAttribute("asduNum", cbEl.attributeValue("nofASDU"));
				}
				el.addAttribute("confRev", cbEl.attributeValue("confRev"));
				if (dsEl == null)
					continue;
				el.addAttribute("dsName", dsEl.attributeValue("name")); 
				el.addAttribute("dsDesc", dsEl.attributeValue("desc")); 
				List<Element> fcdaEls = dsEl.elements("FCDA");
				int i = 0;
				for (Element fcdaEl : fcdaEls) {
					Element fcda = fcdaEl.createCopy();
					fcda.setName("fcda");
					String lnType = ExportUtil.getLnType(fcda.attributeValue("prefix"), fcda.attributeValue("lnClass"), fcda.attributeValue("lnInst"), ldEl);
					fcda.addAttribute("lnType", lnType);
					fcda.addAttribute("ref", SclUtil.getFcdaRef(fcda));
					fcda.addAttribute("idx", String.valueOf(i++));
					el.add(fcda);
				}
				el.addAttribute("entryNum", String.valueOf(fcdaEls.size()));
				
				list.add(el);
			}
		}
		return list;
	}

	/**
	 * 查询当前装置下所有报告控制块的FCDA
	 * @param iedName
	 * @return
	 */
	public static List<Element> getReportControlFCDA(String iedName) {
		if (Constants.XQUERY) {
			String xquery = "let $ied:=" + XMLDBHelper.getDocXPath() +
			        "/SCL/IED[@name='" + iedName + "'], $lds:=$ied/AccessPoint/Server/LDevice"+
					" return for $ld in $lds" +
					" return for $ln in $ld/*" +
					" return for $rcb in $ln/ReportControl" +
					" return <RCB ldName='{concat('" + iedName + "', $ld/@inst/string())}'" +
							" name='{$rcb/@name/string()}' " +
							" datSet='{$rcb/@datSet/string()}' " +
					"lnprefix='{$ln/@prefix/string()}' " +
					"lnclass ='{$ln/@lnClass/string()}' " +
					"lninst ='{$ln/@inst/string()}' " +
					"buffered='{$rcb/@buffered/string()}' " +
					"indexed='{$rcb/@indexed/string()}' " +
					"max='{$rcb/RptEnabled/@max/string()}'> {" +
					"for $fcda in $ln/DataSet[@name=$rcb/@datSet]/FCDA return $fcda" +
					"} </RCB>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> rcbList = new ArrayList<>();
			String ldXpath = "/SCL/IED[@name='" + iedName + "']/AccessPoint/Server/LDevice";
			List<Element> elLds = XMLDBHelper.selectNodes(ldXpath);
			for (Element elLd : elLds) {
				List<Element> elLNs = elLd.elements("LN0");
				for (Element elLN : elLNs) {
					List<Element> elRcbs = elLN.elements("ReportControl");
				}
			}
			return rcbList;
		}
	}
	
//	/**
//	 * 获取指定报告控制块参引。
//	 * @param rcb
//	 * @return
//	 */
//	public static String getRCBRef(Element rcb) {
//		String fc = isBrcb(rcb) ? ITagSG.FC_BRCB : ITagSG.FC_URCB;
//		String ln = rcb.attributeValue("lnprefix") + rcb.attributeValue("lnclass")
//				+ rcb.attributeValue("lninst");
//		return rcb.attributeValue("ldName") + "/" + ln +"$" + fc + "$" + rcb.attributeValue("name");
//	}
	
	/**
	 * 报告是否缓存
	 * @param rcb
	 * @return
	 */
	public static boolean isBrcb(Element rcb) {
		String buffered = rcb.attributeValue("buffered");
		if (StringUtil.isEmpty(buffered)) {
			return false;
		}
		return Boolean.parseBoolean(buffered);
	}
	
	/**
	 * 查询装置IP
	 * @param iedName
	 * @return
	 */
	public static String[] getIPs(String iedName) {
		List<String> ips = new ArrayList<>();
		String xpath = "/scl:SCL/scl:Communication/scl:SubNetwork/scl:ConnectedAP[@iedName='"
				+ iedName + "']/scl:Address/scl:P[@type='IP']";
		List<Element> elIPs = XMLDBHelper.selectNodes(xpath);
		for (Element elIP : elIPs) {
			String ip = elIP.getTextTrim();
			ips.add(ip);
		}
		return ips.toArray(new String[elIPs.size()]);
	}

	/**
	 * 获取报告类型
	 * 0-Din：遥信
	 * 1-Ain：遥测
	 * 2-Alarm：装置故障
	 * 3-Warning：装置告警
	 * 4-Trip：保护动作
	 * 5-Enable：压板
	 * 6-其他（类型枚举值可继续补充）
	 * @param datSet
	 * @return
	 */
	public static int getRcbType(String datSet) {
		EnumRcbType rcbType = null;
		for (EnumRcbType type : EnumRcbType.values()) {
			String name = type.name();
			if (datSet.indexOf(name) > 0) {
				rcbType = type;
				break;
			}
		}
		return (rcbType == null) ? EnumRcbType.Unknow.getCbtype() : rcbType.getCbtype();
	}
}

