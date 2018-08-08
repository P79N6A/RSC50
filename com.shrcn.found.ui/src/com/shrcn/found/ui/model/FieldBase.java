/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.model;

import java.util.Map;
import java.util.Set;

import org.dom4j.Element;
import org.eclipse.swt.graphics.Image;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.ui.enums.EnumCellEditor;
import com.shrcn.found.ui.util.IconsManager;
import com.shrcn.found.ui.util.ImageConstants;

/**
 * 
 * @author 李培宜(mailto:lpy14137@shrcn.com)
 * @version 1.0, 2012-10-8
 */
/**
 * $Log: FormField.java,v $ Revision 1.1 2013/03/29 09:55:14 cchun Add:创建
 * 
 * Revision 1.2 2013/03/04 05:11:29 scy Add：增加exd属性，是否默认不显示。
 * 
 * Revision 1.1 2012/10/08 09:18:44 cchun Add:表单界面相关类
 * 
 */
public class FieldBase implements IField {
	
	private String name;
	private String title;
	private String comment;
	private String remark;
	private String editor;
	private String editorClass;
	private String dictType;
	private String render;
	private String regex;
	private String datatype;
	private String defaultValue;
	private int width;
	private String length;
	private int index;
	private boolean visible = true;
	private boolean readonly;
	private boolean exd;
	private boolean fixed;
	/**
	 * 此属性是否能为空，默认是可以为空的
	 */
	private boolean empty = true;
	
	public FieldBase() {}
	
	public FieldBase(String title) {
		
		this.title = title;
	}

	public FieldBase(String title, int width) {
		
		this.width = width;
		this.title = title;
	}
	
	public FieldBase(String name, String title) {
		
		this.name = name;
		this.title = title;
	}
	
	public FieldBase(String name, String title, int width) {
		this(name, title);
		this.width = width;
	}
	
	public FieldBase(String name, String title, String comment, String defaultValue) {
		
		this.name = name;
		this.title = title;
		this.comment = comment;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int compare(Object element1, Object element2) {
		if (element1 == null || element2 == null) {
			return 0;
		} else {
			return StringUtil.compare(getTextValue(element1), getTextValue(element2));
		}
	}

	@Override
	public Image getImageValue(Object element) {
		if (EnumCellEditor.CHECK.getType().equals(editor)) {
			String value = getValue(element);
			IconsManager iconMgr = IconsManager.getInstance();
			return Boolean.valueOf(value) ? iconMgr.getImage(ImageConstants.CHECKED)
					: iconMgr.getImage(ImageConstants.UNCHECKED);
		}
		return null;
	}

	@Override
	public String getTextValue(Object element) {
		if (EnumCellEditor.CHECK.getType().equals(editor))
			return "";
		String value = getValue(element);
		return StringUtil.nullToEmpty(value);
	}
	
	/**
	 * 处理任意对象以及Dom4j数据.
	 * 
	 * @param element
	 * @return
	 */
	private String getValue(Object element) {
		String value = "";
		if (element instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,String> temp = (Map<String, String>) ((Map<?, ?>)element);
			Set<String> set = temp.keySet();
			if(!set.contains(name)){
				value = "";
			}else {
				value = temp.get(name);
				if(value == null) {
					value = "";
				}else {
					value = value.toString();
				}
			}
		} if (element instanceof Element) {
			value = ((Element)element).attributeValue(name);
		} else {
			if (name!= null && (name.indexOf(".")>0 || ObjectUtil.existProperty(element, name))) {
				Object pv = ObjectUtil.getProperty(element, name);
				if (pv != null) {
					value = pv.toString();
				}
			}
		}
		return value;
	}

	@Override
	public boolean isEditAble() {
		return !EnumCellEditor.NONE.getType().equals(editor);
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isExd() {
		return exd;
	}

	public void setExd(boolean exd) {
		this.exd = exd;
	}

	public boolean isDict() {
		return !StringUtil.isEmpty(dictType);
	}
	
	public boolean isEnum() {
		return DictManager.getInstance().isEnum(dictType);
	}
	
	public boolean isFixed() {
		return fixed;
	}

	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String getRender() {
		return render;
	}

	@Override
	public void setRender(String render) {
		this.render = render;
	}

	@Override
	public String getRemark() {
		return remark;
	}

	@Override
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEditorClass() {
		return editorClass;
	}

	public void setEditorClass(String editorClass) {
		this.editorClass = editorClass;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	
	
}
