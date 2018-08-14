/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.OutSingalFCDA;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.OuterDataSetFunction;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-5-25
 */
/*
 * 修改历史
 * $Log: SignalDAO.java,v $
 * Revision 1.1  2013/03/29 09:36:18  cchun
 * Add:创建
 *
 * Revision 1.14  2012/04/19 10:24:30  cchun
 * Refactor:清除引用
 *
 * Revision 1.13  2012/01/13 08:40:01  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.12  2011/05/06 09:33:48  cchun
 * Update:修改queryOuterDataSets(),getFCDAs()增加LD层次
 *
 * Revision 1.11  2010/12/21 07:29:18  cchun
 * Update:将外部信号的LD改成AP
 *
 * Revision 1.10  2010/11/08 08:32:22  cchun
 * Update: 清除没有用到的代码
 *
 * Revision 1.9  2010/11/08 02:39:08  cchun
 * Update:去掉未使用变量
 *
 * Revision 1.8  2010/09/27 07:08:13  cchun
 * Update:将描述查询对象放到循环外面，提高性能
 *
 * Revision 1.7  2009/11/13 07:18:11  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.6  2009/10/29 09:08:04  cchun
 * Refactor:统一fcda描述查询
 *
 * Revision 1.5  2009/10/23 07:21:44  cchun
 * Update:修改信号描述查询使其适应跨LD的情况，同时优化查询效率
 *
 * Revision 1.4  2009/09/03 06:14:37  cchun
 * Update:为外部信号添加LN描述
 *
 * Revision 1.3  2009/08/18 09:36:09  cchun
 * Update:合并代码
 *
 * Revision 1.2.2.1  2009/07/24 07:10:40  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.2  2009/06/12 03:18:28  cchun
 * 修改信号描述为dU优先，desc次之
 *
 * Revision 1.1.2.1  2009/06/12 03:17:54  cchun
 * 修改信号描述为dU优先，desc次之
 *
 * Revision 1.1  2009/05/25 08:23:37  cchun
 * 添加采样值关联拖拽
 *
 */
public class CopyOfSignalDAO implements SCLDAO {
	
	private CopyOfSignalDAO() {}
	
	/**
	 * 查询指定IED外部信号数据集信息
	 * @param iedName
	 * @param signalType
	 * @return
	 */
	public static List<Element> queryOuterDataSets(String iedName, EnumCtrlBlock signalType) {
		String ndName = signalType.name();
		if (Constants.XQUERY) {
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED where $ied/@name != '" + iedName +
					"' return <IED name='{$ied/@name}' desc='{$ied/@desc}'>{" +
					"for $ap in $ied/scl:AccessPoint " +
					"return <AP name='{$ap/@name}' desc='{$ap/@desc}'> {" +
					"for $ld in $ap/scl:Server/scl:LDevice " +
					"where exists($ld/*/scl:" + ndName + ") " +
					"return " +
					"<LDevice inst='{$ld/@inst}' desc='{$ld/@desc}'> {" +
					"for $cb in $ld/*/scl:" + ndName + " return " +
					"<DataSet cbName='{$cb/@name}' cbDesc='{$cb/@desc}' datName='{$cb/@datSet}' datDesc='{$ld/*/scl:DataSet[@name=$cb/@datSet]/@desc}'/>" +
					"}</LDevice>" +
					"}</AP>" +
					"}</IED>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
//			List<Element> list = new ArrayList<Element>();
//			String iedXPath = SCL.XPATH_IED + "[@name!='" + iedName + "']";
//			List<Element> iedEls =  XMLDBHelper.selectNodesOnlyAtts(iedXPath, "IED");
//			long t = System.currentTimeMillis();
//			for (Element iedEl : iedEls) {
//				String name = iedEl.attributeValue("name");
//				Element iedCpEl = iedEl.createCopy();
//				list.add(iedCpEl);
//				Element iedNode = IEDDAO.getIEDNode(name);
//				List<Element> cbEls = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice/*/scl:" + ndName);
//				for (Element cbEl : cbEls) {
//					Element ldEl = cbEl.getParent().getParent();
//					Element apEl = ldEl.getParent().getParent();
//					
//					String apName = apEl.attributeValue("name");
//					Element apCpEl = DOM4JNodeHelper.selectSingleNode(iedCpEl, "./scl:AccessPoint[@name='" + apName + "']");
//					if (apCpEl == null) {
//						apCpEl = iedCpEl.addElement("AP");
//						apCpEl.addAttribute("name", apName);
//						apCpEl.addAttribute("desc", apEl.attributeValue("desc", ""));
//					}
//					
//					String ldInst = ldEl.attributeValue("inst");
//					Element ldCpEl = DOM4JNodeHelper.selectSingleNode(apCpEl, "./scl:Server/scl:LDevice[@inst='" + ldInst + "']");
//					if (ldCpEl == null) {
//						ldCpEl = apCpEl.addElement("LDevice");
//						DOM4JNodeHelper.copyAttributes(ldEl, ldCpEl);
//					}
//					Element cbNewEl = ldCpEl.addElement("DataSet");
//					String datName = cbEl.attributeValue("datSet");
//					cbNewEl.addAttribute("cbName", cbEl.attributeValue("name", ""));
//					cbNewEl.addAttribute("cbDesc", cbEl.attributeValue("desc", ""));
//					cbNewEl.addAttribute("datName", datName);
//					String datDesc = DOM4JNodeHelper.getAttributeValue(ldEl, "./*/scl:DataSet[@name='" + datName + "']/@desc");
//					cbNewEl.addAttribute("datDesc", datDesc);
//				}
//			}
//			System.out.println("加载外部信号耗时：" + (System.currentTimeMillis()-t)/1000);
//			return list;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("iedName", iedName);
			return (List<Element>) XMLDBHelper.callFunction(OuterDataSetFunction.class, params);
		}
	}
	
	/**
	 * 获取当前IED下所有LD信息
	 * @return 形如<LDevice inst='xx' desc='xx'/>的节点List
	 * @deprecated
	 */
	public static List<Element> getLDs(String iedName) {
		if (Constants.XQUERY) {
			String xquery = "for $LD in " + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + 
					"']/scl:AccessPoint/scl:Server/scl:LDevice return <LDevice inst='{$LD/@inst}' desc='{$LD/@desc}'/>";
			return XMLDBHelper.queryNodes(xquery);
		}
		String ldXpath = SCL.XPATH_IED + "[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice";
		return XMLDBHelper.selectNodesOnlyAtts(ldXpath, "LDevice");
	}
	
	/**
	 * 获取指定LD下指定DataSet的FCDA集合
	 * @param ldInst
	 * @param datName
	 * @return
	 */
	public static List<OutSingalFCDA> getFCDAs(String iedName, String ldInst, String datName) {
		String xpath = "/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']/*/scl:DataSet[@name='" 
				+ datName + "']/scl:FCDA";
		List<OutSingalFCDA> values = new ArrayList<OutSingalFCDA>();
		List<Element> fcdas = XMLDBHelper.selectNodes(xpath);
		//封装成OutSingalFCDA对象，并查询相应的desc属性值
		FcdaDAO fcdaDao = FcdaDAO.getInstance();
		for(Element fcda:fcdas) {
			OutSingalFCDA outFCDA = new OutSingalFCDA(fcda);
			String prefix = outFCDA.getPrefix();
			String lnClass = outFCDA.getLnClass();
			String lnInst = outFCDA.getLnInst();
			String doName = outFCDA.getDoName();
			String daName = outFCDA.getDaName();
			//添加对信号不在同一个LDevice下的处理
			String sgLdInst = outFCDA.getLdInst();
			String desc = fcdaDao.getFCDADesc(iedName, sgLdInst, 
					prefix, lnClass, lnInst, doName);
			if(null == desc)
				outFCDA.setDesc("");
			else if(!SignalsChecker.T.equals(daName) && !SignalsChecker.Q.equals(daName))
				outFCDA.setDesc(desc);
			values.add(outFCDA);
		}
		return values;
	}
	
	/**
	 * 查询数据集信息
	 * @param iedName
	 * @param ldInst
	 * @param signalType
	 * @return
	 * @deprecated
	 */
	public static List<Element> getDataSets(String iedName, String ldInst, EnumCtrlBlock signalType) {
		if (signalType == EnumCtrlBlock.GSEControl)
			return getGooseDataSets(iedName, ldInst);
		else
			return getSmvDataSets(iedName, ldInst);
	}
	
	/**
	 * 查询当前IED下指定LD的GSEControl对应的数据集。
	 * @param iedName
	 * @param ldInst
	 * @return
	 */
	private static List<Element> getGooseDataSets(String iedName, String ldInst) {
		if(Constants.XQUERY){
			String xquery = "let $LD:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']" +
					", $gses:=$LD/scl:LN0/scl:GSEControl" +
					", $dats:=$LD/*/scl:DataSet " +
					"for $gse in $gses, $dat in $dats " + 
					"where $gse/@datSet eq $dat/@name " +
					"return <DataSet cbName='{$gse/@name}' cbDesc='{$gse/@desc}' datName='{$dat/@name}' datDesc='{$dat/@desc}'/>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			Element ldEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']");
			List<Element> gses = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/scl:GSEControl");
			List<Element> gseDats = new ArrayList<>();
			for (Element gse : gses) {
				String datName = gse.attributeValue("datSet");
				Element datEl = DOM4JNodeHelper.selectSingleNode(ldEl, "./*/scl:DataSet[@name='" + datName + "']");
				Document document = DocumentHelper.createDocument();
				Element datEl1 = document.addElement("DataSet");
				datEl1.addAttribute("cbName", gse.attributeValue("name"));
				datEl1.addAttribute("cbDesc", gse.attributeValue("desc"));
				datEl1.addAttribute("datName", datName);
				datEl1.addAttribute("datDesc", datEl.attributeValue("desc"));
				gseDats.add(datEl1);
			}
			return gseDats;
		}
	}
	
	/**
	 * 查询当前IED下指定LD的SampledValueControl对应的数据集。
	 * @param iedName
	 * @param ldInst
	 * @return
	 */
	private static List<Element> getSmvDataSets(String iedName, String ldInst) {
		if(Constants.XQUERY){
			String xquery = "let $LD:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']" +
					", $smvs:=$LD/scl:LN0/scl:SampledValueControl" +
					", $dats:=$LD/*/scl:DataSet " +
					"for $smv in $smvs, $dat in $dats " +
					"where $smv/@datSet eq $dat/@name " +
					"return <DataSet cbName='{$smv/@name}' cbDesc='{$smv/@desc}' datName='{$dat/@name}' datDesc='{$dat/@desc}'/>";
			return XMLDBHelper.queryNodes(xquery);
		}else{
			Element ldEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']");
			List<Element> smvs = DOM4JNodeHelper.selectNodes(ldEl, "./scl:LN0/scl:SampledValueControl");
			List<Element> smvDats = new ArrayList<>();
			for (Element smv : smvs) {
				String datName = smv.attributeValue("datSet");
				Element datEl = DOM4JNodeHelper.selectSingleNode(ldEl, "./*/scl:DataSet[@name='" + datName + "']");
				Document document = DocumentHelper.createDocument();
				Element datEl1 = document.addElement("DataSet");
				datEl1.addAttribute("cbName", smv.attributeValue("name"));
				datEl1.addAttribute("cbDesc", smv.attributeValue("desc"));
				datEl1.addAttribute("datName", datName);
				datEl1.addAttribute("datDesc", datEl.attributeValue("desc"));
				smvDats.add(datEl1);
			}
			return smvDats;
		}
		
	}
}
