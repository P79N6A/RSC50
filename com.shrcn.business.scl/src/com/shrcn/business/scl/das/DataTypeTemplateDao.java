/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-8-25
 */
/**
 * $Log: DataTypeTemplateDao.java,v $
 * Revision 1.1  2013/03/29 09:36:22  cchun
 * Add:创建
 *
 * Revision 1.8  2013/01/31 02:56:26  cchun
 * Update:添加clearTemplates()
 *
 * Revision 1.7  2013/01/29 11:08:06  cchun
 * Refactor:提取getSubType()
 *
 * Revision 1.6  2011/11/03 02:34:23  cchun
 * Update:指定精确的节点名称，避免因为icd文件中含有<Private>导致程序出错
 *
 * Revision 1.5  2011/01/20 03:13:57  cchun
 * Update:聂国勇提交,修改实例化不显示未实例化的bug
 *
 * Revision 1.4  2011/01/10 09:14:57  cchun
 * Update:添加数据类型比较方法
 *
 * Revision 1.3  2010/11/04 08:31:06  cchun
 * Fix Bug:修改为静态调用，避免数据不同步错误
 *
 * Revision 1.2  2010/09/07 09:59:32  cchun
 * Update:更换过时接口
 *
 * Revision 1.1  2010/09/03 02:37:59  cchun
 * Update:修改数据项描述时按doName进行修改
 *
 */
public class DataTypeTemplateDao {

	private DataTypeTemplateDao() {}
	
	/**
	 * 查询数目模板
	 * @return
	 */
	public static Element getDataTypeTemplates() {
		return XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
	}

	/**
	 * 获取指定LN下所有DOI信息
	 * @param currLN
	 * @return
	 */
	public static List<Element> getDataTypeTemplatesDOI(Element currLN) {
		if (currLN == null)
			return null;
		String lnType = currLN.attributeValue("lnType"); //$NON-NLS-1$
		String xpath = "/scl:SCL/scl:DataTypeTemplates/scl:LNodeType[@id='" + lnType + "']/scl:DO"; //$NON-NLS-1$ //$NON-NLS-2$
		return XMLDBHelper.selectNodes(xpath);
	}

	/**
	 * 比较两种类型是否相等
	 * @param imported
	 * @param db
	 * @return
	 */
	public static boolean isSameType(Element imported, Element db) {
		List<String> importedInfo = DataTypeTemplateDao.getTypeInfos(imported);
		List<String> dbInfo = DataTypeTemplateDao.getTypeInfos(db);
		if (importedInfo.size() != dbInfo.size())
			return false;
		for (String info : importedInfo) {
			if (dbInfo.contains(info)) {
				dbInfo.remove(info);
			} else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取指定数据模板类型的字符串形式内容
	 * @param type
	 * @return
	 */
	private static List<String> getTypeInfos(Element type) {
		List<String> infos = new ArrayList<String>();
		List<?> els = type.elements();
		for (Object child : els) {
			String typeInfo = getTypeInfo((Element) child);
			if (typeInfo != null)
				infos.add(typeInfo);
		}
		return infos;
	}

	/**
	 * 将数据类型转换成字符串，以便比较
	 * @param child
	 * @return
	 */
	private static String getTypeInfo(Element child) {
		String type = child.getName();
		String name = child.attributeValue("name");
		if (StringUtil.isEmpty(name))
			return null;
		StringBuilder sbInfo = new StringBuilder(name);
		if ("DO".equals(type)) {
			appendAtts(sbInfo, child, new String[]{"type"});
		} else if ("SDO".equals(type)) {
			appendAtts(sbInfo, child, new String[]{"type"});
		} else if ("DA".equals(type)) {
			appendAtts(sbInfo, child, new String[]{"fc", "bType", "type", "sAddr", "valKind", "count"
					, "dchg", "qchg", "dupd"});
			appendVals(sbInfo, child);
		} else if ("BDA".equals(type)) {
			appendAtts(sbInfo, child, new String[]{"bType", "type", "sAddr", "valKind", "count"});
			appendVals(sbInfo, child);
		} else if ("EnumVal".equals(type)) {
			String ord = child.attributeValue("ord");
			sbInfo = new StringBuilder(ord);
			sbInfo.append("$").append(child.getTextTrim());
		}
		return sbInfo.toString();
	}
	
	/**
	 * 添加属性信息
	 * @param sbInfo
	 * @param ele
	 * @param atts
	 */
	private static void appendAtts(StringBuilder sbInfo, Element ele, String[] atts) {
		for (String att : atts) {
			String value = ele.attributeValue(att);
			if (!StringUtil.isEmpty(value))
				sbInfo.append("$").append(value);
		}
	}
	
	/**
	 * 添加Val信息
	 * @param sbInfo
	 * @param ele
	 */
	private static void appendVals(StringBuilder sbInfo, Element ele) {
		List<?> vals = ele.elements("Val");
		if (vals == null)
			return;
		List<String> values = new ArrayList<String>();
		for (Object obj : vals) {
			Element val = (Element) obj;
			values.add(val.getTextTrim());
		}
		java.util.Collections.sort(values);
		for (String value : values) {
			sbInfo.append("@").append(value);
		}
	}
	
	/**
	 * 得到下级数据类型
	 * @param parentId
	 * @param names
	 * @param dataTemplate
	 * @return
	 */
	public static String getSubType(String parentId, String names, Element dataTemplate) {
		Element parentType = DOM4JNodeHelper.selectSingleNode(dataTemplate, "./*[@id='" + parentId + "']");
		if (parentType == null) {
			SCTLogger.warn("数据类型模板缺少对\"" + parentId + "\"的定义！");
			return null;
		}
		int pos = names.indexOf('.');
		if (pos < 0) {
			Element subType = DOM4JNodeHelper.selectSingleNode(parentType, "./*[@name='" + names + "']");
			String type = subType.attributeValue("type");
			String bType = subType.attributeValue("bType");
			return StringUtil.isEmpty(bType) ? type : bType;
		} else {
			String currName = names.substring(0, pos);
			String subNames = names.substring(pos + 1);
			Element subType = DOM4JNodeHelper.selectSingleNode(parentType, "./*[@name='" + currName + "']");
			String type = subType.attributeValue("type");
			return getSubType(type, subNames, dataTemplate);
		}
	}
	
	/**
	 * 获取DO cdc属性值
	 * 
	 * @param dataTypeTemplates
	 * @param lnType
	 * @param doName
	 * @return
	 */
	public static String getCDC(Element dataTypeTemplates, String lnType, String doName) { 
		String inDoType = getInDoType(dataTypeTemplates, lnType, doName);
		return DOM4JNodeHelper.getAttributeByXPath(dataTypeTemplates, 
				"./scl:DOType[@id='" + inDoType + "']/@cdc");
	}

	/**
	 * 获得DO的ID（支持复杂的SDO结构）
	 * 
	 * @param dataTypeTemplates
	 * @param lnType
	 * @param doName
	 * @return
	 */
	private static String getInDoType(Element dataTypeTemplates, String lnType, String doName) {
		String[] dodaName = doName.split("\\.");
		String id = lnType;
		String inDoType = "";
		for (String name : dodaName) {// 支持SDO类别的解析
			inDoType = DOM4JNodeHelper.getAttributeByXPath(dataTypeTemplates, 
					"./*[@id='" + id + "']/*[@name='" + name + "']/@type");
			id = inDoType;
		}
		return inDoType;
	}
	
	/**
	 * 获取DA bType属性值
	 * @param dataTypeTemplates
	 * @param lnType
	 * @param doName
	 * @param daName
	 * @return
	 */
	public static String getBType(Element dataTypeTemplates, String lnType, String doName, String daName) {
		String doType = DataTypeTemplateDao.getSubType(lnType, doName, dataTypeTemplates);
		return DataTypeTemplateDao.getSubType(doType, daName, dataTypeTemplates);
	}
	
	/**
	 * 得到指定属性集合
	 * @param xpath
	 * @return
	 */
	private static Set<String> getTypeSet(String xpath) {
		List<String> types = XMLDBHelper.getAttributeValues(xpath);
		Set<String> typeSet = new HashSet<String>();
		typeSet.addAll(types);
		return typeSet;
	}
	
	/**
	 * 清除未使用类型
	 * @param refxpath
	 * @param typexpath
	 */
	private static void clearUnusedTypes(String refxpath, String typexpath) {
		// 查询引用
		Set<String> refTypeSet = getTypeSet(refxpath);
		Set<String> defTypeSet = getTypeSet(typexpath);
		for (String defType : defTypeSet) {
			if (!refTypeSet.contains(defType)) {
				Set<String> subTypeSet = getTypeSet("/scl:SCL/scl:DataTypeTemplates/*[@id='" + defType + "']/*/@type");
				for (String subType : subTypeSet) {
					if (!XMLDBHelper.existsNode("/scl:SCL/scl:DataTypeTemplates/*[not(@id='" + defType + "')]/*[@type='" + subType + "']")) {
						XMLDBHelper.removeNodes("/scl:SCL/scl:DataTypeTemplates/*[@id='" + subType + "']");
					}
				}
				XMLDBHelper.removeNodes("/scl:SCL/scl:DataTypeTemplates/*[@id='" + defType + "']");
			}
		}
	}
	
	/**
	 * 清除数据模板
	 */
	public static void clearTemplates() {
		String[][] xpathes = new String[][] {
				new String[]{"/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/*/@lnType", 
						"/scl:SCL/scl:DataTypeTemplates/scl:LNodeType/@id"},
				new String[]{"/scl:SCL/scl:DataTypeTemplates/*[name()='LNodeType' or name()='DOType']/*/@type", 
						"/scl:SCL/scl:DataTypeTemplates/scl:DOType/@id"},
				new String[]{"/scl:SCL/scl:DataTypeTemplates/*[name()='DOType' or name()='DAType']/*/@type", 
						"/scl:SCL/scl:DataTypeTemplates/scl:DAType/@id"},
				new String[]{"/scl:SCL/scl:DataTypeTemplates/*[name()='DOType' or name()='DAType']/*/@type", 
						"/scl:SCL/scl:DataTypeTemplates/scl:EnumType/@id"}
				};
		for (int i=0; i<xpathes.length; i++) {
			clearUnusedTypes(xpathes[i][0], xpathes[i][1]);
		}
	}
}
