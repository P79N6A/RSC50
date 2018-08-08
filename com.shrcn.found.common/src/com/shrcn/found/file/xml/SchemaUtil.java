/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.xml;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dom4j.Element;
import org.dom4j.Node;
import org.xml.sax.SAXException;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2010-11-2
 */
/**
 * $Log: SchemaUtil.java,v $
 * Revision 1.1  2013/03/29 09:38:02  cchun
 * Add:创建
 *
 * Revision 1.2  2011/09/21 02:24:10  cchun
 * Update:修改needsVersions()，增加getSchemaPath()
 *
 * Revision 1.1  2010/11/02 06:10:36  cchun
 * Add:schema工具类
 *
 */
public class SchemaUtil {
	
	private SchemaUtil() {}
	
	/**
	 * 判断当前schema是否需要version,revision属性
	 * @param xsdDir
	 * @return
	 */
	public static boolean needsVersions(String xsdDir, String ver) {
		String fPath = xsdDir + File.separator + "SCL.xsd";
		File fXsd = new File(fPath);
		Element root = null;
		if(fXsd.exists()) {
			root = XMLFileManager.loadXMLFile(fPath).getRootElement();
		} else {
			root = XMLFileManager.loadXMLFile(SchemaUtil.class, getSchemaPath(ver)).getRootElement();
		}
		if (root != null) {
			Node vsNd = root.selectSingleNode("//xs:extension[@base='tBaseElement']/xs:attribute[@name='version']");
			return vsNd != null;
		}
		return false;
	}
	
	/**
	 * 根据版本获取schema路径
	 * @return
	 */
	public static String getSchemaPath(String ver) {
//		String ver = SCTProperties.getInstance().getSchemaVersion();
		ver = "v" + ver.replace(".", "_");
		return "/com/shrcn/business/xml/schema/xsd/" + ver + "/SCL.xsd";
	}
	
	public static String checkSchema(String filePath, String schemaPath) {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			File fXsd = new File(schemaPath);
			if(fXsd.exists()) {
				schema = factory.newSchema(fXsd);
			} else {
				return "无法校验，找不到Schema文件" + schemaPath + "！";
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}
		ReportBean report = new ReportBean();
		try {
			Validator validator = schema.newValidator();
			SCLErrorHandler errorHandler = new SCLErrorHandler(report);
			validator.setErrorHandler(errorHandler);
			validator.validate(new StreamSource(new File(filePath)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (report.getCount() > 0)
			return report.getMessage().toString();
		return null;
	}
}
