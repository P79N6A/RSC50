/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.business.scl.beans.SubPointBean;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-11-28
 */
/**
 * $Log: SubPointsDao.java,v $
 * Revision 1.1  2013/03/29 09:36:22  cchun
 * Add:创建
 *
 * Revision 1.4  2012/03/09 07:35:50  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.3  2012/01/13 08:40:02  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.2  2011/12/02 03:39:52  cchun
 * Update:添加queryRelations()
 *
 * Revision 1.1  2011/11/30 11:01:42  cchun
 * Add:工程点表数据访问类
 *
 */
public class SubPointsDao {

	private SubPointsDao() {}
	
	/**
	 * 查询除被GSE、SMV控制块引用之外的数据集信息。
	 * @param iedName
	 * @return
	 */
	public static List<Element> getDataSets(String iedName) {
		if(Constants.XQUERY){
			String xq = "let $ied:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "']" +
					" return for $ld in $ied/scl:AccessPoint/scl:Server/scl:LDevice" +
					" return let $cbDs:=$ld/*/*[name()='GSEControl' or name()='SampledValueControl']/@datSet/string()" +
					" return for $dataset in $ld/*/scl:DataSet" +
					" where not(exists(index-of($cbDs, $dataset/@name)))" +
					" return element DataSet{$dataset/@*, attribute ldInst {$ld/@inst}}";
				return XMLDBHelper.queryNodes(xq);
		}else{
			Element ldEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
			List<Element> ld = DOM4JNodeHelper.selectNodes(ldEl, "./scl:AccessPoint/scl:Server/scl:LDevice");
			List<String> cbDs =  new ArrayList<>();
			List<Element> dSets =  new ArrayList<>();
			for(Element lDevice:ld){
				cbDs = DOM4JNodeHelper.getAttributeValues(lDevice,"./*/*[name()='GSEControl' or name()='SampledValueControl']/@datSet");	
				
				List<Element> datasets = DOM4JNodeHelper.selectNodes(lDevice,"./*/scl:DataSet");		
				for(Element dataset:datasets){
					boolean exit = false;
					for(String cbD:cbDs){
						if(cbD.equals(dataset.attributeValue("name"))){
							exit = true;
							break;
						}
					}
					if(!exit){
						dataset.addAttribute("ldInst", lDevice.attributeValue("inst"));
						dSets.add(dataset);
					}
				}
			}
			return dSets;
		}
	}
	
	/**
	 * 查询指定数据集信息
	 * @param iedName
	 * @param ldInst
	 * @param datName
	 * @return
	 */
	public static List<SubPointBean> queryPoints(String iedName, String ldInst, String datName, String type) {
		// 查询与一次设备存在关联的LN信息
		List<Element> lnodes = new ArrayList<>();
		if(Constants.XQUERY){
			String xq = XMLDBHelper.getDocXPath() + "/scl:SCL/scl:Substation//scl:LNode[@iedName='" + iedName + "']";
			lnodes = XMLDBHelper.queryNodes(xq);
		}else{
			lnodes = XMLDBHelper.selectNodesOnlyAtts("/scl:SCL/scl:Substation//scl:LNode[@iedName='" + iedName + "']","LNode");
		}
		List<String> lns = new ArrayList<String>();
		for (Element lnode : lnodes) {
			lns.add(getLnName(lnode));
		}
		// 查询数据集FCDA信息
		List<Element> fcdas = new ArrayList<>();
		if(Constants.XQUERY){
			String xq = "let $ied:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "'] return " +
					"for $fcda in $ied/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']/*/scl:DataSet[@name='" + datName + "']/scl:FCDA " +
					"return element FCDA {$fcda/@*, attribute desc " +
					"{$ied/scl:AccessPoint/scl:Server/scl:LDevice[@inst=$fcda/@ldInst]" +
					"/*[not(@prefix) or (not(@prefix) and $fcda/@prefix='') or (exists($fcda/@prefix) and @prefix=$fcda/@prefix)]" +
					"[@lnClass=$fcda/@lnClass][@inst=$fcda/@lnInst]" +
					"/*[@name=$fcda/@doName]/@desc" +
					"}}";
			fcdas = XMLDBHelper.queryNodes(xq);
		}else{
			Element ldEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:IED[@name='" + iedName + "']");
			List<Element> fcdaEls = DOM4JNodeHelper.selectNodes(ldEl, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']/*/scl:DataSet[@name='" + datName + "']/scl:FCDA");
			for(Element fcdaEl:fcdaEls){
				String fcdaLdInst = fcdaEl.attributeValue("ldInst");
				String fcdaPrefix = fcdaEl.attributeValue("prefix");
				String PrefixXpath = "/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']/*/scl:DataSet[@name='" + datName + "']/scl:FCDA/@prefix";
				String fcdaLnClass = fcdaEl.attributeValue("lnClass");
				String fcdaLnInst = fcdaEl.attributeValue("lnInst");
				String fcdaDoName = fcdaEl.attributeValue("doName");
				String descEl = XMLDBHelper.getAttributeValue("/scl:SCL/scl:IED[@name='" + iedName + "']" + 
						"/scl:AccessPoint/scl:Server/scl:LDevice[@inst='"+ fcdaLdInst +"']" +
						"/*[not('@prefix') or (not('@prefix') and "+fcdaPrefix+"='') or (count("+PrefixXpath+")>0 and @prefix='"+fcdaPrefix+"')]" +
						"[@lnClass='"+ fcdaLnClass +"'][@inst='"+fcdaLnInst+"']" +
						"/*[@name='"+ fcdaDoName +"']/@desc");
				fcdaEl.addAttribute("desc", String.valueOf(descEl));
				fcdas.add(fcdaEl);
			}
		}
		
		FcdaDAO fcdaDAO = FcdaDAO.getInstance();
		List<SubPointBean> points = new ArrayList<SubPointBean>();
		for (Element fcda : fcdas) {
			String lnName = getLnName(fcda);
			if (!lns.contains(lnName)) // 过滤
				continue;
			String desc = fcda.attributeValue("desc", "");
			if (StringUtil.isEmpty(desc))
				desc = fcdaDAO.getFCDADesc(iedName, fcda);
			SubPointBean point = new SubPointBean();
			point.setType(type);
			point.setDesc(desc);
			point.setRef(SCL.getNodeRef(fcda));
			points.add(point);
		}
		return points;
	}
	
	/**
	 * 获取LN名称
	 * @param ele
	 * @return
	 */
	private static String getLnName(Element ele) {
		String prefix = ele.attributeValue("prefix");
		String lnClass = ele.attributeValue("lnClass");
		String inst = ele.attributeValue("lnInst");
		return SCL.getLnName(prefix, lnClass, inst);
	}
	
	/**
	 * 查询指定IED下全部关联信号
	 * @param iedName
	 * @return
	 */
	public static List<SubPointBean> queryRelations(String iedName) {
		List<Element> extrefs = XMLDBHelper.selectNodes("/scl:SCL/scl:IED[@name='" + iedName + "']" +
				"/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef");
		List<SubPointBean> points = new ArrayList<SubPointBean>();
		FcdaDAO fcdaDao = FcdaDAO.getInstance();
		for (Element extRef : extrefs) {
			String intAddr = extRef.attributeValue("intAddr");
			String outIEDName = extRef.attributeValue("iedName");
			String daName = extRef.attributeValue("daName");
			if (StringUtil.isEmpty(outIEDName) || SCL.isExcludeDa(daName))
				continue;
			String ldInst = extRef.attributeValue("ldInst");
			String prefix = extRef.attributeValue("prefix");
			String lnClass = extRef.attributeValue("lnClass");
			String lnInst = extRef.attributeValue("lnInst");
			String doName = extRef.attributeValue("doName");
			String outDesc = fcdaDao.getFCDADesc(outIEDName, ldInst, 
					prefix, lnClass, lnInst, doName);
			String intDesc = fcdaDao.getInnerDesc(iedName, intAddr);
			SubPointBean point = new SubPointBean();
			point.setResvRef(intAddr);
			point.setResvDesc(intDesc);
			point.setSendRef(SCL.getNodeRef(extRef));
			point.setSendDesc(outDesc);
			point.setFlow("←");
			point.setSendIED(outIEDName);
			points.add(point);
		}
		return points;
	}
}
