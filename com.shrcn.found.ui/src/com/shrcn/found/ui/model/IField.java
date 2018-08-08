/**
 * Copyright (c) 2007-2009 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.model;

import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-3-4
 */
/**
 * $Log: IField.java,v $
 * Revision 1.2  2011/09/02 07:10:42  cchun
 * Update:添加排序相关方法
 *
 * Revision 1.1  2011/01/04 09:25:59  cchun
 * Refactor:将table框架移动到common项目中
 *
 * Revision 1.2  2010/12/10 02:03:15  cchun
 * Update:增加editor属性
 *
 * Revision 1.1  2010/03/09 07:37:49  cchun
 * Add:添加远动配置插件
 *
 */
public interface IField {
	
	public String getName();

	public void setName(String name);

	public String getTitle();

	public void setTitle(String title);
	
	public String getRemark();
	
	public void setRemark(String remark);

	public String getComment();

	public void setComment(String comment);

	public String getEditor();

	public void setEditor(String editor);
	
	public String getEditorClass();
	
	public void setEditorClass(String editorClass);
	
	public String getDictType();

	public void setDictType(String dictType);

	public String getRender();

	public void setRender(String render);
	
	public String getRegex();

	public void setRegex(String regex);
	
	public String getDatatype();

	public void setDatatype(String datatype);

	public String getDefaultValue();

	public void setDefaultValue(String defaultValue);

	public int getWidth();

	public void setWidth(int width);

	public int getIndex();

	public void setIndex(int index);

	public boolean isVisible();

	public void setVisible(boolean visible);
	
	public String getLength();

	public void setLength(String length);
	
	/**
	 * 判断是否可编辑
	 * @return
	 */
	public boolean isEditAble();
	
	public boolean isReadonly();

	public void setReadonly(boolean readonly);

	public boolean isDict();

	public boolean isEnum();
	
	public boolean isExd();

	public void setExd(boolean exd);

	public boolean isFixed();

	public void setFixed(boolean fixed);
	
	/**
	 * 这个属性是否能为空
	 * @return 是否可为空
	 */
	public boolean isEmpty();

	public void setEmpty(boolean empty);
	
	/**
	 * 返回字段图标
	 * @param element
	 * @return
	 */
	public Image getImageValue(Object element);
	
	/**
	 * 返回字段值
	 * @param element
	 * @return
	 */
	public String getTextValue(Object element);
	
	/**
     * @param element1
     * @param element2
     * @return Either:
     * 	<li>负数，如果element1的值小于element2；
     *  <li>零，如果element1的值小于element2；
     *  <li>正数，如果element1的值大于element2。
     */
	public int compare(Object element1, Object element2);
    
}
