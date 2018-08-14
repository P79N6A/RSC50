/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ListUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-25
 */
/**
 * $Log: DataTemplateDao.java,v $
 * Revision 1.1  2013/03/29 09:36:22  cchun
 * Add:创建
 *
 * Revision 1.2  2011/12/08 03:03:07  cchun
 * Update:数据类型改成替换处理模式
 *
 * Revision 1.1  2011/07/27 07:23:38  cchun
 * Add:数据模板操作类
 *
 */
public class DataTemplateDao {

	private DataTemplateDao() {
	}
	
	/**
	 * 导入数据模板
	 * @param datatypeTemplates
	 */
	public static void importDataTypes(Element datatypeTemplates) {
		if(XMLDBHelper.countNodes("/scl:SCL/scl:DataTypeTemplates/*")==0) { //$NON-NLS-1$
			XMLDBHelper.replaceNode("/scl:SCL/scl:DataTypeTemplates", datatypeTemplates); //$NON-NLS-1$
			return;
		}
		List<Element> alltypes = new ArrayList<>();
		List<Element> enumtypes = datatypeTemplates.elements("EnumType"); //$NON-NLS-1$
		List<Element> datypes = datatypeTemplates.elements("DAType"); //$NON-NLS-1$
		List<Element> dotypes = datatypeTemplates.elements("DOType"); //$NON-NLS-1$
		List<Element> lnodetypes = datatypeTemplates.elements("LNodeType"); //$NON-NLS-1$
		alltypes.addAll(enumtypes);
		alltypes.addAll(datypes);
		alltypes.addAll(dotypes);
		alltypes.addAll(lnodetypes);
		deleteOnExists(alltypes);
		
		XMLDBHelper.insertAfterType("/scl:SCL/scl:DataTypeTemplates", new String[] {"LNodeType"}, 
				lnodetypes);
		XMLDBHelper.insertAfterType("/scl:SCL/scl:DataTypeTemplates", new String[] {"LNodeType", "DOType"}, 
				dotypes);
		XMLDBHelper.insertAfterType("/scl:SCL/scl:DataTypeTemplates", new String[] {"LNodeType", "DOType", "DAType"}, 
				datypes);
		XMLDBHelper.insertAfterType("/scl:SCL/scl:DataTypeTemplates", new String[] {"LNodeType", "DOType", "DAType", "EnumType"}, 
				enumtypes);
	}
	
	/**
	 * 如果类型存在则删除
	 * @param ele
	 */
	private static void deleteOnExists(List<Element> eles) {
		if (eles == null || eles.size() == 0)
			return;
		String ids = "";
		int idNum = 0;
		for (Element ele : eles) {
			String id = ele.attributeValue("id");
			if (!"".equals(ids)) {
				ids += " or ";
			}
			ids += "@id='" + id + "'";
			idNum += 1;
			if (idNum > 49) {		// 避免xquery语句超长
				String xpath = "/scl:SCL/scl:DataTypeTemplates/*[" + ids + "]";
				XMLDBHelper.removeNodes(xpath);
				ids = "";
				idNum = 0;
			}
		}
		if (!"".equals(ids)) {
			String xpath = "/scl:SCL/scl:DataTypeTemplates/*[" + ids + "]";
			XMLDBHelper.removeNodes(xpath);
		}
	}
	
	/**
	 * 当选择“统一加前缀”时插入数据模板
	 */
	public static void insertTypesWithPrefix(Element root, String prefix) {
		Element dttElement = root.element("DataTypeTemplates");
		addEnumTypePrefix(dttElement, prefix);
		addLNodeTypePrefix(dttElement, prefix);
		addDOTypePrefix(dttElement, prefix);
		addDATypePrefix(dttElement, prefix);
		DataTemplateDao.importDataTypes(dttElement);
	}
	
	//当选择“统一加前缀”时插入枚举部分
	private static void addEnumTypePrefix(Element dttElement, String prefix){
		List<?> listofEnumType = dttElement.selectNodes("./*[name()='EnumType']"); //$NON-NLS-1$
		Iterator<?> itEnumType = listofEnumType.iterator();
		while (itEnumType.hasNext()) {
			Element element = (Element) itEnumType.next();
			Attribute attnodeType = element.attribute("id"); //$NON-NLS-1$
			String attValue = attnodeType.getValue();
			attnodeType.setValue(prefix + attValue);
		}
	}
	
	//当选择“统一加前缀”时插入LNodeType部分
	private static void addLNodeTypePrefix(Element dttElement, String prefix){
		List<?> listofLNodeType = dttElement.selectNodes("./*[name()='LNodeType']"); //$NON-NLS-1$
		Iterator<?> itnodeType = listofLNodeType.iterator();
		while (itnodeType.hasNext()) {
			Element element = (Element) itnodeType.next();
			Attribute attnodeType = element.attribute("id"); //$NON-NLS-1$
			String attValue = attnodeType.getValue();
			attnodeType.setValue(prefix + attValue);
			//将该LNodeType下所有的type属性加前缀prefix
			List<?> subElements = element.elements();
			Iterator<?> it = subElements.iterator();
			while (it.hasNext()){
				Element e = (Element)it.next();
				Attribute att = e.attribute("type"); //$NON-NLS-1$
				if(null != att) {
					String oldTypeValue = att.getValue();
					att.setValue(prefix+oldTypeValue);
				}
			}
		}
	}
	
	//当选择“统一加前缀”时插入DOType部分
	private static void addDOTypePrefix(Element dttElement, String prefix) {
		List<?> listofDOType = dttElement.selectNodes("./*[name()='DOType']"); //$NON-NLS-1$
		Iterator<?> itDOType = listofDOType.iterator();
		while (itDOType.hasNext()) {
			Element ndDOType = (Element) itDOType.next();
			Attribute attnodeType = ndDOType.attribute("id"); //$NON-NLS-1$
			String attValue = attnodeType.getValue();
			attnodeType.setValue(prefix + attValue);
			//将该DOType下所有的type属性加前缀prefix
			List<?> subElements = ndDOType.elements();
			Iterator<?> it = subElements.iterator();
			while (it.hasNext()){
				Element e = (Element)it.next();
				Attribute att = e.attribute("type"); //$NON-NLS-1$
				if (att != null){
					String oldTypeValue = att.getValue();
					att.setValue(prefix+oldTypeValue);
				}
			}
		}
	}
	
	//当选择“统一加前缀”时插入DAType部分
	private static void addDATypePrefix(Element dttElement, String prefix){
		List<?> listofDAType = dttElement.selectNodes("./*[name()='DAType']"); //$NON-NLS-1$
		Iterator<?> itDAType = listofDAType.iterator();
		while (itDAType.hasNext()) {
			Element ndDAType = (Element) itDAType.next();
			Attribute attnodeType = ndDAType.attribute("id"); //$NON-NLS-1$
			String attValue = attnodeType.getValue();
			attnodeType.setValue(prefix + attValue);
			//将该DAType下所有的type属性加前缀prefix
			List<?> subElements = ndDAType.elements("BDA"); //$NON-NLS-1$
			Iterator<?> it = subElements.iterator();
			while (it.hasNext()){
				Element e = (Element)it.next();
				Attribute att = e.attribute("type"); //$NON-NLS-1$
			if (att != null){
				String oldTypeValue = att.getValue();
				att.setValue(prefix+oldTypeValue);
				}
			}
		}
	}
	
	/**
	 * 遍历指定LNodeType下所有DO,DA，并返回所有DA的参引。
	 * @param id
	 * @return
	 */
	public static List<String> getLnTypeSubRef(String id, Element dataTypeTemplates) {
		List<String> subRefs = new ArrayList<String>();
		Element lnodeType = (Element) dataTypeTemplates.selectSingleNode("./*[name()='LNodeType'][@id='" + id + "']");
		if (lnodeType == null)
			return null;
		List<?> doList = lnodeType.elements("DO");
		for (Object obj : doList) {
			Element doNd = (Element) obj;
			String name = doNd.attributeValue("name");
			String type = doNd.attributeValue("type");
			findDOSubRefs(name, type, subRefs, dataTypeTemplates);
		}
		ListUtil.sortStr(subRefs);
		return subRefs;
	}

	/**
	 * 得到DO下所有SDO,DA(Struct)的参引
	 * @param id
	 * @return
	 */
	private static void findDOSubRefs(String parentName, String id, List<String> subRefs, Element dataTypeTemplates) {
		Element doType = DOM4JNodeHelper.selectSingleNode(dataTypeTemplates, "./*[@id='" + id + "']");
		if (doType == null) {
			subRefs.add(parentName);
			return;
		}
		List<?> children = doType.elements();
		for (Object obj : children) {
			Element child = (Element) obj;
			String nodeName = child.getName();
			String name = child.attributeValue("name"); 
			String type = child.attributeValue("type");
			String bType = child.attributeValue("bType");
			if ("SDO".equals(nodeName)) {		// SDO
				findDOSubRefs(parentName + Constants.DOT + name, type, subRefs, dataTypeTemplates);
			} else if ("DA".equals(nodeName) || "BDA".equals(nodeName)) {
				if (bType.equals("Struct")) {	// Struct型DA
					findDOSubRefs(parentName + Constants.DOT + name, type, subRefs, dataTypeTemplates);
				} else {						// 普通DA, BDA
					subRefs.add(parentName + Constants.DOT + name);
				}
			}
		}
	}
	
}
