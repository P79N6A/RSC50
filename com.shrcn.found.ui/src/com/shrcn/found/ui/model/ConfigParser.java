/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.model;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.XMLFileManager;

/**
 * 
 * @author 李培宜(mailto:lpy14137@shrcn.com)
 * @version 1.0, 2012-10-8
 */
/**
 * $Log: ConfigParser.java,v $
 * Revision 1.1  2013/03/29 09:55:11  cchun
 * Add:创建
 *
 * Revision 1.3  2013/03/04 05:12:42  scy
 * Update：创建控件时判定是否不显示。
 *
 * Revision 1.2  2012/10/29 01:56:25  cchun
 * Update:添加注释
 *
 * Revision 1.1  2012/10/08 09:18:44  cchun
 * Add:表单界面相关类
 *
 */
public class ConfigParser {

	/**
	 * 解析表单定义文件。
	 * @param context
	 * @param cfgPath
	 * @return
	 */
	public static Form parse(Class<?> context, String cfgPath) {
		Document doc = XMLFileManager.loadXMLFile(context, cfgPath);
		Element formEle  = doc.getRootElement();
		
		int column = Integer.valueOf(formEle.attributeValue("columns"));
		Form form = new Form(formEle.attributeValue("desc"),
				column,
				formEle.attributeValue("class"),
				formEle.attributeValue("tableClass"));
		List<?> elements = formEle.elements();
		IField[] fields = new FieldBase[elements.size()];
		int i = 0;
		for (Object obj : elements) {
			Element fieldEle = (Element) obj;
			String name = fieldEle.attributeValue("name");
			String title = fieldEle.attributeValue("desc");
			String comment = fieldEle.attributeValue("comment");
			String defaultValue = fieldEle.attributeValue("default");
			IField field = new FieldBase(name, title, comment, defaultValue);
			fields[i] = field;
			field.setRemark(fieldEle.attributeValue("remark"));
			field.setDictType(fieldEle.attributeValue("dict"));
			field.setDatatype(fieldEle.attributeValue("datType"));
			field.setRegex(fieldEle.attributeValue("regex"));
			field.setRender(fieldEle.attributeValue("render"));
			String input = fieldEle.attributeValue("editor");
			field.setEditor(input);
			field.setEditorClass(fieldEle.attributeValue("editorClass"));
			field.setVisible(getBooleanValue(fieldEle, "visible", true));
			field.setReadonly(getBooleanValue(fieldEle, "readonly", false));
			field.setExd(getBooleanValue(fieldEle, "exd", false));
			field.setFixed(getBooleanValue(fieldEle, "fixed", false));
			field.setEmpty(getBooleanValue(fieldEle, "empty", true));
			field.setWidth(getIntegerValue(fieldEle, "width"));
			field.setIndex(getIntegerValue(fieldEle, "index"));
			if (!form.isHasExd() && field.isExd())
				form.setHasExd(true);
			// 标题
			// 输入控件
			i++;
		}
		form.setFields(fields);
		return form;
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
