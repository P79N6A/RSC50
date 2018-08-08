/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-6-13
 */
/**
 * $Log: TableManager.java,v $
 * Revision 1.2  2013/07/22 05:47:04  cchun
 * Update:增加对comment属性的支持
 *
 * Revision 1.1  2013/06/14 01:42:46  cchun
 * Refactor:提取 TableManager
 *
 */
public class TableManager {
	
	private String uiCfgPath;
	private ResourceBundle uicfg;
	
	private Map<String, Form> formCache = new HashMap<String, Form>();
	private Map<String, TableConfig> tableCache = new HashMap<String, TableConfig>();
	private Map<String, List<TableConfig>> tabCache = new HashMap<String, List<TableConfig>>();
	
	private Class<?> loaderClass;
	
	public TableManager(Class<?> loaderClass) {
		this(loaderClass, null);
	}
	
	public TableManager(Class<?> loaderClass, String uiCfgPath) {
		this.loaderClass = loaderClass;
		if (uiCfgPath != null) {
			this.uiCfgPath = uiCfgPath;
			uicfg = ResourceBundle.getBundle(uiCfgPath, Locale.getDefault(), loaderClass.getClassLoader());
		}
	}
	
	public String getUIDefine(String key) {
		return uicfg.getString(key);
	}
	
	public String getUiCfgPath() {
		return uiCfgPath;
	}

	/**
	 * 获取表单配置信息。 --------------------add
	 * @param panelConfig
	 * @return
	 */
	public Form getForm(String panelConfig){
		Form form = formCache.get(panelConfig);
		if (form == null) {
			form = ConfigParser.parse(loaderClass, panelConfig);
			if (!Constants.DEBUG)
				formCache.put(panelConfig, form);
		}
		return form;
	}
	
	public TableConfig getTableConfig(String configPath) {
		if (configPath == null)
			return null;
		TableConfig tableconfig = tableCache.get(configPath);
		if (tableconfig == null) {
			Element tableEle = getDocElement(configPath);
			tableconfig = parseTableConfig(tableEle);
			if (!Constants.DEBUG)
				tableCache.put(configPath, tableconfig);
		}
		return tableconfig;
	}
	
	/**
	 * 得到指定表格配置。        ----------------------add
	 * @param panelKey
	 * @return
	 */
	public TableConfig getDefinedTable(String panelKey) {
		String configPath = null;
		if (uicfg.containsKey(panelKey))
			configPath = getUIDefine(panelKey);
		return getTableConfig(configPath);
	}
	
	/**
	 * 得到指定表单配置。        ----------------------add
	 * @param panelKey
	 * @return
	 */
	public Form getDefinedForm(String panelKey) {
		String configPath = null;
		if (uicfg.containsKey(panelKey))
			configPath = getUIDefine(panelKey);
		return getForm(configPath);
	}

	/**
	 * 获取配置文件根元素。
	 * @param configPath
	 * @return
	 */
	private Element getDocElement(String configPath) {
		Document doc = XMLFileManager.loadXMLFile(loaderClass, configPath);
		return doc.getRootElement();
	}
	
	/**
	 * 获取TabFolder配置信息。 ----------------------------add
	 * @param configPath
	 * @return
	 */
	public List<TableConfig> getTabFolderConfig(String configPath) {
		if (configPath == null)
			return null;
		List<TableConfig> tabconfig = tabCache.get(configPath);
		if (tabconfig == null) {
			tabconfig = new ArrayList<TableConfig>();
			Element tableEle = getDocElement(configPath);
			List<?> tables = tableEle.elements();
			for (Object table : tables) {
				tabconfig.add(parseTableConfig((Element) table));
			}
			if (!Constants.DEBUG)
				tabCache.put(configPath, tabconfig);
		}
		return tabconfig;
	}
	
	/**
	 * 根据单个表格，生成TableConfig
	 * @param tableEle
	 * @return
	 */
	public TableConfig parseTableConfig(Element tableEle) {
		String tableName = tableEle.attributeValue("name");
		String tableDesc = tableEle.attributeValue("desc");
		String className = tableEle.attributeValue("class");
		String tClassName = tableEle.attributeValue("tableClass");
		List<?> eleFields = tableEle.elements();
		IField[] fields = new FieldBase[eleFields.size()];
		int i = 0;
		for (Object obj : eleFields) {
			Element eleField = (Element) obj;
			String fieldName = eleField.attributeValue("name");
			String title = eleField.attributeValue("desc");
			String strWidth = eleField.attributeValue("width");
			int width = 50;
			if (null != strWidth)
				width = Integer.parseInt(strWidth);
			String strIndex = eleField.attributeValue("index");
			int index = 0;
			if (null != strIndex)
				index = Integer.parseInt(strIndex);
			String defaultValue = eleField.attributeValue("default");
			String editor = eleField.attributeValue("editor");
			String regex = eleField.attributeValue("regex");
			String render = eleField.attributeValue("render");
			String comment = eleField.attributeValue("comment");
			String remark = eleField.attributeValue("remark");
			String datatype = eleField.attributeValue("datType");
			String visible = eleField.attributeValue("visible");
			String length = eleField.attributeValue("length");
			String readonly = eleField.attributeValue("readonly");
			String fixed = eleField.attributeValue("fixed");
			String exd = eleField.attributeValue("exd");
			IField field = new FieldBase(fieldName, title, comment, defaultValue);
			field.setDictType(eleField.attributeValue("dict"));
			field.setWidth(width);
			field.setIndex(index);
			field.setRemark(remark);
			if (null != editor)
				field.setEditor(editor);
			if (null != regex)
				field.setRegex(regex);
			if (null != render)
				field.setRender(render);
			if (null != datatype)
				field.setDatatype(datatype);
			if (null != visible)
				field.setVisible(Boolean.parseBoolean(visible));
			if (null != length)
				field.setLength(length);
			if (null != readonly)
				field.setReadonly(Boolean.parseBoolean(readonly));
			if (null != fixed)
				field.setFixed(Boolean.parseBoolean(fixed));
			if (null != exd)
				field.setExd(Boolean.parseBoolean(exd));
			field.setEmpty(getBooleanValue(eleField, "empty", true));
			fields[i] = field;
			i++;
		}
		TableConfig tc = new TableConfig(tableName, tableDesc, className, tClassName);
		tc.setFields(fields);
		return tc;
	}
	
	/**
	 * 得到布尔属性值。
	 * @param fieldEle
	 * @param att
	 * @return
	 */
	private static boolean getBooleanValue(Element fieldEle, String att, boolean defaultV) {
		String strv = fieldEle.attributeValue(att);
		if (StringUtil.isEmpty(strv))
			return defaultV;
		return Boolean.valueOf(strv);
	}
	
	private static int getIntegerValue(Element fieldEle, String att) {
		String strv = fieldEle.attributeValue(att);
		if (StringUtil.isEmpty(strv))
			return -1;
		return Integer.valueOf(strv);
	}
	
}
