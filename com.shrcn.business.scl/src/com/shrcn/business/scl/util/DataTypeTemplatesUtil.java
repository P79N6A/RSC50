/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.das.DataTypeTemplateDao;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-6-26
 */
/*
 * 修改历史
 * $Log: DataTypeTemplatesUtil.java,v $
 * Revision 1.1  2013/03/29 09:36:28  cchun
 * Add:创建
 *
 * Revision 1.19  2012/01/17 08:50:30  cchun
 * Update:使用更加安全的xpath形式
 *
 * Revision 1.18  2010/11/08 07:14:32  cchun
 * Update:清理引用
 *
 * Revision 1.17  2010/11/04 08:31:38  cchun
 * Fix Bug:修改DataTypeTemplateDao为静态调用，避免数据不同步错误
 *
 * Revision 1.16  2010/09/03 02:44:24  cchun
 * Refactor:使用xpath常量和公用查询方法
 *
 * Revision 1.15  2010/07/29 09:56:59  cchun
 * Update:修改方法调用
 *
 * Revision 1.14  2009/12/04 09:42:55  lj6061
 * 清理模板时检测相同内容的模板清理时BUg
 *
 * Revision 1.13  2009/12/04 07:10:10  lj6061
 * 清理模板时检测相同内容的模板任意清理一个
 *
 * Revision 1.12  2009/12/02 09:05:22  lj6061
 * Fix 模板列表
 *
 * Revision 1.11  2009/12/02 03:33:55  lj6061
 * Fix DOType名称错误
 *
 * Revision 1.10  2009/12/01 07:48:55  lj6061
 * 添加重复模板处理
 *
 * Revision 1.9  2009/10/29 05:41:12  lj6061
 * 修正清理模板时对LN0的单独处理
 *
 * Revision 1.8  2009/10/14 09:16:16  lj6061
 * updata:删除IED时候不清理模板，保存文件时候清理LnodeType
 *
 * Revision 1.7  2009/08/18 09:36:08  cchun
 * Update:合并代码
 *
 * Revision 1.6.2.2  2009/08/05 12:54:53  cchun
 * Update:修改大节点插入或替换处理逻辑，解决namespace不符合要求的bug
 *
 * Revision 1.6.2.1  2009/07/31 09:37:13  cchun
 * Update:修改命名控件处理方式
 *
 * Revision 1.6  2009/07/17 02:23:01  lj6061
 * 修正空指针异常
 *
 * Revision 1.5  2009/07/14 07:57:52  lj6061
 * 添加测试用例
 *
 * Revision 1.4  2009/07/09 05:36:24  lj6061
 * 修改替换节点方法
 *
 * Revision 1.3  2009/07/07 00:35:34  lj6061
 * 修正Null异常
 *
 * Revision 1.2  2009/07/06 08:49:16  lj6061
 * 修正Null异常
 *
 * Revision 1.1  2009/07/06 07:35:14  lj6061
 * 添加配置文件统一建模选项
 * 改变配置文件工程的位置
 *
 * Revision 1.4  2009/07/03 05:43:06  lj6061
 * 清理模板信息类
 *
 * Revision 1.3  2009/07/03 05:31:23  lj6061
 * 清理模板信息类
 *
 * Revision 1.2  2009/07/01 10:02:22  lj6061
 * 合并主干和分支代码
 *
 * Revision 1.1  2009/06/26 09:05:48  lj6061
 * 重新整理类所在的包
 *
 */
public class DataTypeTemplatesUtil {
	
	/** 数据模板 */
	private static Element templateNode = null;
	
	/**
	 * 替换数据模板，清理多余的DODA
	 */
	public static void clearTemplates() {
		getTemplates();
		if (templateNode != null) {
			if (isClearTemplates()) {
				replaceTemplate();
			}
		}
	}
	
	/**
	 * 获取相同的模板节点数据
	 * 
	 * @return 返回map key 类型是LnodeType还是DoType等，
	 */
	public static Map<String, Map<String, List<Element>>> getSameTemplates() {
		if (templateNode == null)
			getTemplates();
		if (templateNode != null)
			return fillSameTemplates(templateNode);
		return null;
	}
	
	/**
	 * 判断是否需要替换模板 当DODA有一方需要清理时，替换整个模板
	 * 
	 * @return true 需要替换模板 ，false 不需要替换模板
	 */
	private static boolean isClearTemplates() {
		boolean isln = getClearLnNodeType();
		boolean isDo = getClearDOType();
		boolean isDa = getClearDAType();
		boolean isEnmu = getClearEnumType();
		if (!isDo && !isDa && !isln && !isEnmu)
			return false;
		else
			return true;
	}
	
	/**
	 *  获取库里数据模板
	 */
	private static void getTemplates() {
		templateNode  = DataTypeTemplateDao.getDataTypeTemplates();;
	}

	/**
	 * 删除所有的未引用的LNodeType
	 */
	private static boolean getClearLnNodeType() {
		List<Element> lnList = new LinkedList<Element>();	
		List<Element> typeList = getTypeList("LNodeType");
		
		List<String> refLnType = getLnodeType();
		for (Element element : typeList) {
			String lnId = element.attributeValue("id");
			if(lnId == null) continue;
			if(!refLnType.contains(lnId)){
				lnList.add(element);
			}
		}
		return isReplaceTemp(isClear(lnList), removeSameNode(typeList));
	}
	
	/**
	 * 获得模板中的LNodeType集合
	 * @return
	 */
	private static List<String> getLnodeType(){
		String lnPath = "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/scl:LN/@lnType";
		String ln0Path = "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0/@lnType";
		// 将所有IED部分出现的LnTYPE添加到集合
		List<String> ln0 = XMLDBHelper.getAttributeValues(ln0Path);
		List<String> lnodeList = XMLDBHelper.getAttributeValues(lnPath);
		if (ln0 != null)
			lnodeList.addAll(ln0);
		return lnodeList;
	}
	
	/***
	 * 从数据模板中获取清理DO,并清理 完全相同的数据模板
	 * 
	 * @return 是否需要清理
	 */
	private static boolean getClearDOType() {
		List<Element> sdoList = new LinkedList<Element>();	
		List<Element> unRefDo = new LinkedList<Element>();
		List<Element> typeList = getTypeList("DOType");
		
		getUnRefLNDoType(unRefDo, sdoList,typeList);
		unRefDo = getUnRefSDO(unRefDo, sdoList);

		return isReplaceTemp(isClear(unRefDo), removeSameNode(typeList));
	}
	
	/***
	 * 从数据模板中清理DA，并清理 完全相同的数据模板
	 * 
	 * @return 是否需要清理
	 */
	private static boolean getClearDAType() {
		List<Element> bdaList = new LinkedList<Element>();	
		List<Element> unRefDa = new LinkedList<Element>();
		
	    List<Element> typeList = getTypeList("DAType");
	    
		getUnRefDAType(unRefDa, bdaList,typeList);
		unRefDa = getUnRefBDA(unRefDa, bdaList);
		
		return isReplaceTemp(isClear(unRefDa), removeSameNode(typeList));
	}
	
	/**
	 * 从数据模板中清理未引用的模板，并清理 完全相同的数据模板
	 * @return
	 */
	private static boolean getClearEnumType() {
		List<Element> unRefEnum = new LinkedList<Element>();
		List<Element> typeList = getTypeList("EnumType");
		
		getUnRefEnumType(unRefEnum,typeList);
		return isReplaceTemp(isClear(unRefEnum), removeSameNode(typeList));
	}

	/**
	 * 判断是否需要替换节点
	 * @param isUnRef
	 * @param isSame
	 * @return
	 */
	private static boolean isReplaceTemp(boolean isUnRef, boolean isSame) {
		return isUnRef || isSame;
	}
	/***
	 * 根据清理的列表的大小 判断是否需要清理模板
	 * 
	 * @return 是否需要清理
	 */
	public static boolean isClear(List<Element> unRef){
		if (unRef.size() > 0) {
			for (Element element : unRef) {
				templateNode.remove(element);
			}
			return true;
		} else
			return false;
	}
	
	/**
	 * 获取未被LNNodeType中的DO引用下的DOType的集合 并获得被引用的DoType下含有SDO的
	 * 
	 * @param unRefDo
	 *            未被引用的D
	 * @param sdoList
	 *            被引用的SDO
	 */
	@SuppressWarnings("unchecked")
	private static void getUnRefLNDoType(List<Element> unRefDo,List<Element> sdoList,List<Element> typeList) {
		List<Element> doList = DOM4JNodeHelper.selectNodes(templateNode, ".//*[name()='DO']");
		
		for (Element doEle : typeList) {
			unRefDo.add(doEle);
			String doRef = (doEle).attributeValue("id");
			for (Element lnDoEle : doList) {
				String lnDoType = lnDoEle.attributeValue("type");
				if(lnDoType == null) return ;
				if (lnDoType.equals(doRef) && lnDoType != null) {
					if (unRefDo.contains(doEle)) {
						sdoList.addAll(doEle.elements("SDO"));
						unRefDo.remove(doEle);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 获取未被LNNodeType中的DO引用下的DOType中筛选是否被其他SDO引用
	 * 
	 * @param unRefDo
	 *            未被引用的D
	 * @param sdoList
	 *            被引用的SDO
	 * @return unRefSDO 最终模板中未使用DO
	 */
	private static List<Element> getUnRefSDO(List<Element> unRefDo,List<Element> sdoList){
		List<Element> unRefSDO = new LinkedList<Element>();
		for (Element refDo : unRefDo) {
			unRefSDO.add(refDo);
			String doRef = (refDo).attributeValue("id");
			for (Element sdoEle : sdoList) {
				String sdoType = sdoEle.attributeValue("type");
				if(sdoType == null) return unRefSDO;
				if (sdoType.equals(doRef)){
					if (unRefDo.contains(refDo)) {
						unRefSDO.remove(refDo);
						break;
					}
				}
			}
		}
		return unRefSDO;
	}
		
	/**
	 * 获取未被LNNodeType中的DO引用下的DOType的集合 并获得被引用的DoType下含有SDO的
	 * 
	 * @param unRefDo
	 *            未被引用的D
	 * @param sdoList
	 *            被引用的SDO
	 */
	private static void getUnRefDAType(List<Element> unRefDa,List<Element> bdaList,List<Element> typeList) {
		List<Element> daList = DOM4JNodeHelper.selectNodes(templateNode,"./*[name()='DOType']/*[name()='DA'][@bType='Struct']");
		
		for (Element daEle : typeList) {
			unRefDa.add(daEle);
			String daRef = (daEle).attributeValue("id");
			for (Element doDaEle : daList) {
				String daType = doDaEle.attributeValue("type");
				if(daType == null) return;
				if (daType.equals(daRef)) {
					if (unRefDa.contains(daEle)) {
						List<Element> strList = DOM4JNodeHelper.selectNodes(daEle, ".//*[name()='BDA'][@bType='Struct']");
						bdaList.addAll(strList);
						unRefDa.remove(daEle);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 获取未被LNNodeType中的DO引用下的DOType中筛选是否被其他SDO引用
	 * 
	 * @param unRefDo
	 *            未被引用的D
	 * @param sdoList
	 *            被引用的SDO
	 * @return unRefSDO 最终模板中未使用DO
	 */
	private static List<Element> getUnRefBDA(List<Element> unRefDa,List<Element> bdaList){
		List<Element> unRefBDA = new LinkedList<Element>();
		for (Element refDa : unRefDa) {
			unRefBDA.add(refDa);
			String doRef = (refDa).attributeValue("id");
			for (Element sdoEle : bdaList) {
				String bdaType = sdoEle.attributeValue("type");
				if(bdaType == null) return unRefBDA;
				if (bdaType.equals(doRef)){
					if (unRefDa.contains(refDa)) {
						unRefBDA.remove(refDa);
						break;
					}
				}
			}
		}
		return unRefBDA;
	}
	
	private static void getUnRefEnumType(List<Element> unRefEnmu,List<Element> typeList) {
		List<Element> daList = DOM4JNodeHelper.selectNodes(templateNode,"./*[name()='DOType']/*[name()='DA'][@bType='Enum']");
		List<Element> bdaList = DOM4JNodeHelper.selectNodes(templateNode,"./*[name()='DAType']/*[name()='BDA'][@bType='Enum']");
		daList.addAll(bdaList); //将DA和DATYPE下的BDA类型为Enum存在一个集合，取其属性type；
		
		for (Element enumEle : typeList) {
			unRefEnmu.add(enumEle);
			String enumRef = (enumEle).attributeValue("id");
			for (Element doDaEle : daList) {
				String enmuType = doDaEle.attributeValue("type");
				if(enmuType == null) return;
				if (enmuType.equals(enumRef)) {
					if (unRefEnmu.contains(enumEle)) {
						unRefEnmu.remove(enumEle);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 替换模板
	 * @param iedName
	 */
	private static void replaceTemplate(){
		if (templateNode != null) {
			XMLDBHelper.replaceNode(SCL.XPATH_DATATYPETEMPLATES, templateNode);
		}
	}
	
	/**
	 * 返回节点集合
	 * @param eleName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Element> getTypeList(String eleName){
		return templateNode.elements(eleName);
	}
	
	/**
	 * 获取模板中所有的重复的类型
	 * @param templateNode
	 * @return
	 */
	private static Map<String, Map<String, List<Element>>> fillSameTemplates(Element templateNode){
		String tmpPath = ".";
		// 按类型定义相同节点集合
		Map<String, Map<String, List<Element>>> samaMap = new LinkedHashMap<String, Map<String, List<Element>>>();
		Map<String, List<Element>> lnSamaMap = fillSameTemp("LNodeType",templateNode, tmpPath + "/*[name()='LNodeType']");
		Map<String, List<Element>> doSamaMap = fillSameTemp("DOType",templateNode, tmpPath + "/*[name()='DOType']");
		Map<String, List<Element>> daSamaMap = fillSameTemp("DAType",templateNode, tmpPath + "/*[name()='DAType']");
		Map<String, List<Element>> enmuXamaMap = fillSameTemp("EnumType",templateNode, tmpPath + "/*[name()='EnumType']");
		if (lnSamaMap.size() > 0)
			samaMap.put("LNodeType", lnSamaMap);
		if (doSamaMap.size() > 0)
			samaMap.put("DOType", doSamaMap);
		if (daSamaMap.size() > 0)
			samaMap.put("DAType", daSamaMap);
		if (enmuXamaMap.size() > 0)
			samaMap.put("EnumType", enmuXamaMap);
		return samaMap;
	}
	
	/**
	 * 获取eleName模板类型的id重复节点.
	 * @param eleName
	 * @param templateNode
	 * @param xpath
	 * @return 返回id 所对应的重复节点类型
	 */
	private static Map<String, List<Element>> fillSameTemp(String eleName,Element templateNode,String xpath){
		Map<String, List<Element>> samaMap = new LinkedHashMap<String, List<Element>>();
		Set<String> lnSet= new LinkedHashSet<String>();
		List<Element> lNodeList = getTypeList(eleName);
		for (Element element : lNodeList) {
			lnSet.add(element.attributeValue("id"));
		}
		for (String lnodeName : lnSet) {
			List<Element> eleList = DOM4JNodeHelper.selectNodes(templateNode,xpath + "[@id='" + lnodeName + "']");
			if (eleList != null && eleList.size() > 1) {
				samaMap.put(lnodeName, eleList);
			}
		}
		return samaMap;
	}
	
	/**
	 * 存在重复模板完全一样的情况下 删除其他的 保留一个，用户无需处理
	 * @param eleList
	 */
	private static boolean removeSameNode(List<Element> eleList){
		List<Element> removeList = new LinkedList<Element>();
		for (int i = 0; i < eleList.size(); i++) {
			String asXml = eleList.get(i).asXML();
			for (int j = 1; j < eleList.size() && j != i; j++) {
				if(asXml.equals(eleList.get(j).asXML())){
					removeList.add(eleList.get(j));
				}
			}
		}
		return isClear(removeList);
	}
	
	public static void setTemplateNode(Element templateNode) {
		DataTypeTemplatesUtil.templateNode = templateNode;
	}
	
}
