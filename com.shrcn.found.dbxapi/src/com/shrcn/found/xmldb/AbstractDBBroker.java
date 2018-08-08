/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb;

import java.util.List;
import java.util.Map;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-1-13
 */
/**
 * $Log: AbstractDBBroker.java,v $
 * Revision 1.1  2013/03/29 09:37:02  cchun
 * Add:创建
 *
 * Revision 1.4  2012/11/26 08:38:17  cchun
 * Update:增加setProject()
 *
 * Revision 1.3  2012/08/21 03:33:34  cchun
 * Update:使getNamespace()不返回null
 *
 * Revision 1.2  2012/05/18 07:09:00  cchun
 * Update:整理注释
 *
 * Revision 1.1  2012/01/13 08:39:51  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 */
public abstract class AbstractDBBroker implements DBBroker {

	protected String document;
	protected String project;
	protected String namespace;
	
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public String getNamespace() {
		return StringUtil.nullToEmpty(namespace);
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getProject() {
		if (project == null)
			return Constants.DEFAULT_PRJECT_NAME;
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	
	@Override
	public void updateOnce(String select, String value) {
		update(select, value);
	}
	
	public void saveAttributeValue(String attXpath, String value) {
		String ndXPath = attXpath.substring(0, attXpath.lastIndexOf('/'));
		String attName = attXpath.substring(attXpath.lastIndexOf('@') + 1);
		saveAttribute(ndXPath, attName, value);		
	}
	
	public String getAttributeString(String xquery) {
		List<String> attVals = getAttributeStrings(xquery);
		if (attVals.size() > 0)
			return attVals.get(0);
		return null;
	}
	
	@Override
	public IFunction createFunction(Class<?> funClass,
			Map<String, Object> params) {
		return null;
	}
	
	@Override
	public IFunction createUpdateFunction(Class<?> funClass,
			Map<String, Object> params) {
		return null;
	}
	
	@Override
	public boolean isInCache(String select) {
		return false;
	}
}
