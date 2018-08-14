/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.beans.BayLNodeModel;
import com.shrcn.business.scl.beans.ExtRefModel;
import com.shrcn.business.scl.beans.FCDAModel;
import com.shrcn.business.scl.beans.IEDInputsModel;
import com.shrcn.business.scl.beans.IEDModel;
import com.shrcn.business.scl.beans.LDeviceModel;
import com.shrcn.business.scl.beans.LNodeModel;
import com.shrcn.business.scl.beans.LdDataSetModel;
import com.shrcn.business.scl.beans.LdLnModel;
import com.shrcn.business.scl.model.EquipmentConfig;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.XQueryUtil;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-18
 */
/**
 * $Log: ExcelReportDao.java,v $
 * Revision 1.1  2013/03/29 09:36:19  cchun
 * Add:创建
 *
 * Revision 1.9  2012/03/14 02:14:51  cchun
 * Fix Bug:避免导出的daName为null
 *
 * Revision 1.8  2012/03/09 07:35:49  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.7  2012/01/13 08:40:03  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.6  2011/11/03 02:24:46  cchun
 * Update:修改getIEDWithInputs()使用精确xpath
 *
 * Revision 1.5  2011/08/11 05:46:49  cchun
 * Update:清理注释
 *
 * Revision 1.4  2011/08/10 06:29:17  cchun
 * Update:修改描述获取方式
 *
 * Revision 1.3  2011/08/01 08:15:05  cchun
 * Refactor:提取getApStr()
 *
 * Revision 1.2  2011/07/22 03:12:50  cchun
 * Update:修改getIEDInputs()入参
 *
 * Revision 1.1  2011/07/20 05:53:20  cchun
 * Update:改名，并增加信号关联和一二次关联查询方法
 *
 * Revision 1.1  2011/07/19 05:43:44  cchun
 * Add:IED数据导出查询类
 *
 */
public class ExcelReportDao {
	
	private static final int DATASET_INFO 	= 0;
	private static final int LNODE_INFO 	= 1;
	private static final int RC_INFO 	= 2;//报告控制块
	private static final int LC_INFO 	= 3;//日志控制块
	private ExcelReportDao() {
	}
	
	private static List<IEDModel> getInfoBySelected(List<String[]> selectedItems, int type) {
		List<IEDModel> ieds = new ArrayList<IEDModel>();
		for (String[] item : selectedItems) {
			if (item.length < 1)
				continue;
			String iedName = item[0];
			int apLen = item.length - 1;
			String[] aps = new String[apLen];
			System.arraycopy(item, 1, aps, 0, apLen);
			if (type == DATASET_INFO)
				ieds.add(getIedWithDataSets(iedName, aps));
			else if (type == LNODE_INFO)
				ieds.add(getIedWithLNs(iedName, aps));
			
		}
		return ieds;
	}
	
	/**
	 * 获取所选IED的指定访问点下数据集信息
	 * @param selectedItems
	 * @return
	 */
	public static List<IEDModel> getDataSetsInfo(List<String[]> selectedItems) {
		return getInfoBySelected(selectedItems, DATASET_INFO);
	}

	/**
	 * 获取所选IED指定AP下的LN信息
	 * @param selectedItems
	 * @return
	 */
	public static List<IEDModel> getLNsInfo(List<String[]> selectedItems) {
		return getInfoBySelected(selectedItems, LNODE_INFO);
	}
	
	/**
	 * 获取当前IED的指定访问点下数据集信息
	 * @param iedName
	 * @param aps
	 * @return
	 */
	private static IEDModel getIedWithDataSets(String iedName, String[] aps) {
		if (aps.length < 1)
			return null;
		List<Element> ndAps = new ArrayList<>();
		if (Constants.XQUERY) {
			String xquery = XQueryUtil.DECLARE_ADD_ATT_FUN +
					"let $ied:=" + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED[@name='" + iedName +
					"'], $aps:=" + XQueryUtil.getGroupString(aps) +
					" return for $ap in $ied/scl:AccessPoint " +
					"where exists(index-of($aps, $ap/@name)) return " +
					"<AccessPoint name='{$ap/@name}' iedName='{$ied/@name}' iedDesc='{$ied/@desc}' iedType='{$ied/@type}'>{" +
					"for $ld in $ap/scl:Server/scl:LDevice return " +
					"<LDevice inst='{$ld/@inst}' desc='{$ld/@desc}'>{" +
					"for $dat in $ld/*/scl:DataSet return $dat" +
					"}</LDevice>" +
					"}</AccessPoint>";
			ndAps = XMLDBHelper.queryNodes(xquery);
		} else {
			Element iedNode = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
			String apAtt = "";
			for (String ap : aps) {
				if ("".equals(apAtt))
					apAtt += "[@name='" + ap + "'";
				else
					apAtt += " or @name='" + ap + "'";
			}
			apAtt += "]";
			List<Element> apEls = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint" + apAtt);
			String iedDesc = iedNode.attributeValue("desc");
			String iedType = iedNode.attributeValue("type");
			for (Element apEl : apEls) {
				String apName = apEl.attributeValue("name");
				Element ndAp = DOM4JNodeHelper.createSCLNode("AccessPoint");
				ndAps.add(ndAp);
				ndAp.addAttribute("name", apName);
				ndAp.addAttribute("iedName", iedName);
				ndAp.addAttribute("iedDesc", iedDesc);
				ndAp.addAttribute("iedType", iedType);
				for (Element ldEl : DOM4JNodeHelper.selectNodes(apEl, "./scl:Server/scl:LDevice")) {
					String ldInst = ldEl.attributeValue("inst");
					String ldDesc = ldEl.attributeValue("desc");
					Element ndLd = ndAp.addElement("LDevice");
					ndLd.addAttribute("inst", ldInst);
					ndLd.addAttribute("desc", ldDesc);
					for (Element datEl : DOM4JNodeHelper.selectNodes(ldEl, "./*/scl:DataSet")) {
						ndLd.add(datEl.createCopy());
					}
				}
			}
		}
		IEDModel ied = new IEDModel(iedName);
		FcdaDAO fcdaDAO = FcdaDAO.getInstance();
		for (Element ndAp : ndAps) {
			String apName = ndAp.attributeValue("name");
			String iedDesc = ndAp.attributeValue("iedDesc");
			String iedType = ndAp.attributeValue("iedType");
			ied.setDesc(iedDesc);
			ied.setType(iedType);
			List<?> ndLds = ndAp.elements();
			for (Object obj : ndLds) {
				Element ndLd = (Element) obj;
				String ldInst = ndLd.attributeValue("inst");
				List<?> dats = ndLd.elements();
				for (Object obj1 : dats) {
					Element ndDat = (Element) obj1;
					String datName = ndDat.attributeValue("name");
					String datDesc = ndDat.attributeValue("desc");
					LdDataSetModel dataset = new LdDataSetModel(apName, ldInst, datName, datDesc);
					ied.getLds().add(dataset);
					List<?> fcdaEls = ndDat.elements();
					for (Object obj2 : fcdaEls) {
						Element ndFcda = (Element) obj2;
						String prefix = DOM4JNodeHelper.getAttribute(ndFcda, "prefix");
						String lnClass = DOM4JNodeHelper.getAttribute(ndFcda, "lnClass");
						String lnInst = DOM4JNodeHelper.getAttribute(ndFcda, "lnInst");
						String doName = DOM4JNodeHelper.getAttribute(ndFcda, "doName");
						String daName = DOM4JNodeHelper.getAttribute(ndFcda, "daName");
						String desc = fcdaDAO.getFCDADescOnly(iedName, ldInst, prefix, lnClass, lnInst, doName, daName);
						FCDAModel fcda = new FCDAModel(ldInst + "$" + prefix + lnClass + lnInst + "$" + doName,
								daName, desc);
						dataset.getFcdas().add(fcda);
					}
				}
			}
		}
		return ied;
	}
	
	private static IEDModel getIedWithLNs(String iedName, String[] aps) {
		if (aps.length < 1)
			return null;
		List<Element> ndAps = new ArrayList<>();
		if (Constants.XQUERY) {
			String xquery = "let $ied:=" + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED[@name='" + iedName +
					"'], $aps:=" + XQueryUtil.getGroupString(aps) +
					" return for $ap in $ied/scl:AccessPoint " +
					"where exists(index-of($aps, $ap/@name)) return " +
					"<AccessPoint name='{$ap/@name}' iedName='{$ied/@name}' iedDesc='{$ied/@desc}' iedType='{$ied/@type}'>{" +
					"for $ld in $ap/scl:Server/scl:LDevice return " +
					"<LDevice inst='{$ld/@inst}' desc='{$ld/@desc}'>{" +
					"for $ln in $ld/*[name()='LN' or name()='LN0'] return " +
					"<LN prefix='{$ln/@prefix}' lnClass='{$ln/@lnClass}' inst='{$ln/@inst}' desc='{$ln/@desc}'/>" +
					"}</LDevice>" +
					"}</AccessPoint>";
			ndAps = XMLDBHelper.queryNodes(xquery);
		} else {
			Element iedNode = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
			String apAtt = "";
			for (String ap : aps) {
				if ("".equals(apAtt))
					apAtt += "[@name='" + ap + "'";
				else
					apAtt += " or @name='" + ap + "'";
			}
			apAtt += "]";
			List<Element> apEls = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint" + apAtt);
			String iedDesc = iedNode.attributeValue("desc");
			String iedType = iedNode.attributeValue("type");
			for (Element apEl : apEls) {
				String apName = apEl.attributeValue("name");
				Element ndAp = DOM4JNodeHelper.createSCLNode("AccessPoint");
				ndAps.add(ndAp);
				ndAp.addAttribute("name", apName);
				ndAp.addAttribute("iedName", iedName);
				ndAp.addAttribute("iedDesc", iedDesc);
				ndAp.addAttribute("iedType", iedType);
				for (Element ldEl : DOM4JNodeHelper.selectNodes(apEl, "./scl:Server/scl:LDevice")) {
					String ldInst = ldEl.attributeValue("inst");
					String ldDesc = ldEl.attributeValue("desc");
					Element ndLd = ndAp.addElement("LDevice");
					ndLd.addAttribute("inst", ldInst);
					ndLd.addAttribute("desc", ldDesc);
					for (Element lnEl : DOM4JNodeHelper.selectNodes(ldEl, "./*[name()='LN' or name()='LN0']")) {
						Element ndLn = ndLd.addElement("LN");
						String[] atts = new String[] {"prefix", "lnClass", "inst", "desc"};
						for (String att : atts) {
							String val = lnEl.attributeValue(att, "");
							ndLn.addAttribute(att, val);
						}
					}
				}
			}
		}
		IEDModel ied = new IEDModel(iedName);
		for (Element ndAp : ndAps) {
			String apName = ndAp.attributeValue("name");
			String iedDesc = ndAp.attributeValue("iedDesc");
			String iedType = ndAp.attributeValue("iedType");
			ied.setDesc(iedDesc);
			ied.setType(iedType);
			List<?> ndLdEls = ndAp.elements();
			for (Object obj : ndLdEls) {
				Element ndLd = (Element) obj;
				String ldInst = ndLd.attributeValue("inst");
				LDeviceModel ld = new LDeviceModel(apName, ldInst);
				ied.getLds().add(ld);
				List<?> ndLns = ndLd.elements();
				for (Object obj1 : ndLns) {
					Element ndLn = (Element) obj1;
					String prefix = ndLn.attributeValue("prefix");
					String lnClass = ndLn.attributeValue("lnClass");
					String lnInst = ndLn.attributeValue("inst");
					String lnDesc = ndLn.attributeValue("desc");
					ld.getLns().add(new LdLnModel(prefix + lnClass + lnInst, lnDesc));
				}
			}
		}
		return ied;
	}
	
	/**
	 * 查询指定IED的信号关联信息
	 * @param selection
	 * @return
	 */
	public static List<IEDInputsModel> getIEDInputs(List<String> selection) {
		List<IEDInputsModel> ieds = new ArrayList<IEDInputsModel>();
		for (String iedName : selection) {
			ieds.add(getIEDWithInputs(iedName));
		}
		return ieds;
	}
	
	private static IEDInputsModel getIEDWithInputs(String iedName) {
		Element ndIed = IEDDAO.getIEDNode(iedName);
		if (ndIed == null)
			return null;
		List<Element> refEls = DOM4JNodeHelper.selectNodes(ndIed, ".//scl:Inputs/scl:ExtRef");
		IEDInputsModel iedModel = new IEDInputsModel(ndIed.attributeValue("name"), 
				ndIed.attributeValue("desc"), ndIed.attributeValue("type"));
		Map<String, Element> ldCache = new HashMap<String, Element>();
		FcdaDAO fcdaDAO = FcdaDAO.getInstance();
		for (Object obj : refEls) {
			Element extref = (Element) obj;
			String intAddr = extref.attributeValue("intAddr");
			String[] infos = SCL.getIntAddrInfo(intAddr);
			String intDesc = "";
			int p = intAddr.indexOf('/');
			if (p > 0) {
				String ldInst = infos[0];
				Element ldData = ldCache.get(ldInst);
				if (ldData == null) {
					ldData = DOM4JNodeHelper.selectSingleNode(ndIed, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']");
					ldCache.put(ldInst, ldData);
				}
				intDesc = GOOSEInputDAO.getInnerDesc(extref, ldData);
			} else {
				intAddr = StringUtil.nullToEmpty(intAddr);
			}
			String extIedName = extref.attributeValue("iedName");
			String extLdInst = extref.attributeValue("ldInst");
			String prefix = extref.attributeValue("prefix");
			String lnClass = extref.attributeValue("lnClass");
			String lnInst = extref.attributeValue("lnInst");
			String doName = extref.attributeValue("doName");
			String daName = extref.attributeValue("daName");
			String extDesc = fcdaDAO.getFCDADescOnly(extIedName, extLdInst, prefix, lnClass, lnInst, doName, daName);
			String daAddr = StringUtil.isEmpty(daName) ? "" : "." + daName;
			String extAddr = extLdInst + "/" + prefix + lnClass + lnInst + "." + doName + daAddr;
			ExtRefModel extModel = new ExtRefModel(extIedName, extAddr, extDesc, intAddr, intDesc);
			iedModel.getInputs().add(extModel);
		}
		return iedModel;
	}
	
	/**
	 * 查询当前变电站所有一二次关联关系信息
	 * @return
	 */
	public static List<BayLNodeModel> getBayLNodes() {
		List<Element> ndBays = new ArrayList<>();
		if (Constants.XQUERY) {
			String xquery = XQueryUtil.DECLARE_ADD_ATT_FUN + "let $doc:=" + XMLDBHelper.getDocXPath() + "/scl:SCL return " +
				"for $vol in $doc/scl:Substation/scl:VoltageLevel return " +
				"for $bay in $vol/scl:Bay return " +
				"<Bay name='{$bay/@name}' vol='{$vol/@name}'>{" +
				"for $eqp in $bay/* " +
				"where exists($eqp/@type) or $eqp/name()='Function' return " + 
				"<Eqp name='{$eqp/@name}' type='{$eqp/@type}' cate='{$eqp/name()}'>{" +
				"for $lnode in $eqp//scl:LNode return " +
				"cchun:add-or-update-attributes($lnode, xs:QName('desc'), " +
				"$doc/scl:IED[@name=$lnode/@iedName]/scl:AccessPoint/scl:Server/scl:LDevice[@inst=$lnode/@ldInst]/*" +
				"[not(@prefix) or (not(@prefix) and $lnode/@prefix='') or (exists($lnode/@prefix) and @prefix=$lnode/@prefix)][@lnClass=$lnode/@lnClass][@inst=$lnode/@lnInst]/@desc)" +
				"}</Eqp>}</Bay>";
			ndBays = XMLDBHelper.queryNodes(xquery);
		} else {
			Element subEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:Substation");
			List<Element> volEls = subEl.elements("VoltageLevel");
			for (Element volEl : volEls) { // 电压等级
				String volName = volEl.attributeValue("name");
				List<Element> bayEls = volEl.elements("Bay");
				for (Element bayEl : bayEls) { // 间隔
					Element ndBay = DOM4JNodeHelper.createSCLNode("Bay");
					ndBays.add(ndBay);
					ndBay.addAttribute("name", bayEl.attributeValue("name"));
					ndBay.addAttribute("vol", volName);
					List<Element> eqpEls = bayEl.elements();
					for (Element eqpEl : eqpEls) { // 设备
						String cate = eqpEl.getName();
						if (eqpEl.attribute("type")!=null || "Function".equals(cate)) {
							Element ndEqp = ndBay.addElement("Eqp");
							ndEqp.addAttribute("name", eqpEl.attributeValue("name"));
							ndEqp.addAttribute("type", eqpEl.attributeValue("type"));
							ndEqp.addAttribute("cate", cate);
							List<Element> lnodeEls = DOM4JNodeHelper.selectNodes(eqpEl, ".//scl:LNode");
							for (Element lnodeEl : lnodeEls) { // LNode
								String iedName = lnodeEl.attributeValue("iedName");
								String ldInst = lnodeEl.attributeValue("ldInst");
								String prefix = lnodeEl.attributeValue("prefix");
								String lnClass = lnodeEl.attributeValue("lnClass");
								String lnInst = lnodeEl.attributeValue("lnInst");
								String xpath = "/scl:SCL/scl:IED[@name='" + iedName +
										"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
										"']/*" + SCL.getLNAtts(prefix, lnClass, lnInst);
								String lnDesc = XMLDBHelper.getAttributeValue(xpath );
								lnodeEl.addAttribute("desc", lnDesc);
								ndEqp.add(lnodeEl.createCopy());
							}
						}
					}
				}
			}
		}
		List<BayLNodeModel> bays = new ArrayList<BayLNodeModel>();
		for (Element ndBay : ndBays) {
			String name = ndBay.attributeValue("name");
			String vol = ndBay.attributeValue("vol");
			BayLNodeModel bay = new BayLNodeModel(name, vol);
			List<?> ndEqpEls = ndBay.elements();
			for (Object obj1 : ndEqpEls) {
				Element ndEqp = (Element) obj1;
				String eqpName = ndEqp.attributeValue("name");
				String eqpType = ndEqp.attributeValue("type");
				String cate = ndEqp.attributeValue("cate");
				String eqpTypeName = "Function".equals(cate) ? "功能" : EquipmentConfig.getInstance().getDesc(eqpType);
				String eqp = StringUtil.isEmpty(eqpTypeName) ? eqpName : eqpTypeName + "-" + eqpName;
				List<?> ndLNEls = ndEqp.elements();
				for (Object obj2 : ndLNEls) {
					Element ndLNode = (Element) obj2;
					String iedName = ndLNode.attributeValue("iedName");
					String ldInst = ndLNode.attributeValue("ldInst");
					String prefix = DOM4JNodeHelper.getAttribute(ndLNode, "prefix");
					String lnClass = ndLNode.attributeValue("lnClass");
					String lnInst = ndLNode.attributeValue("lnInst");
					String lnDesc = ndLNode.attributeValue("desc");
					LNodeModel lnModel = new LNodeModel(eqp, iedName, ldInst, prefix + lnClass + lnInst, lnDesc);
					bay.getLnodes().add(lnModel);
				}
			}
			if (bay.getLnodes().size() > 0)
				bays.add(bay);
		}
		return bays;
	}

	
}
