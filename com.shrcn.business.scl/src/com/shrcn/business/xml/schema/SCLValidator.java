/*
 * Validate.java
 */

package com.shrcn.business.xml.schema;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.xml.sax.SAXException;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.found.file.xml.ReportBean;
import com.shrcn.found.file.xml.SCLErrorHandler;


public class SCLValidator {
	
	/**
	 * 单例对象
	 */
	private static volatile SCLValidator instance = new SCLValidator();
	private boolean mustCheck = true;
	
	/**
	 * 单例模式私有构造函数
	 */
	private SCLValidator(){
	}

	/**
	 * 获取单例对象
	 */
	public static SCLValidator getInstance(){
		if(null == instance) {
			synchronized (SCLValidator.class) {
				if(null == instance) {
					instance = new SCLValidator();
				}
			}
		}
		return instance;
	}
	
	public void check(String file, ReportBean report,IProgressMonitor monitor) {
		if(!mustCheck)
			return;
		Schema schema = getSchema();
		checkSchema(file, report, monitor,schema);
	}
	
	private Schema getSchema() {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			SCTProperties property = SCTProperties.getInstance();
			File fXsd = new File(property.getSchemaPath() + File.separator + "SCL.xsd");
			if(fXsd.exists()) {
				schema = factory.newSchema(fXsd);
			} else {
				String ver = property.getSchemaVersion();
				ver = "v" + ver.replace(".", "_");
				schema = factory.newSchema(SCLValidator.class
					.getResource("xsd/" + ver + "/SCL.xsd"));
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return schema;
	}

	private void checkSchema(String file, ReportBean report,
			IProgressMonitor monitor,Schema schema) {
		try {
			if(monitor != null)
				monitor.beginTask("开始执行Schema检查...", 3);
			if(monitor != null)
				monitor.worked(1);
			Validator validator = schema.newValidator();
			SCLErrorHandler errorHandler = new SCLErrorHandler(report);
			errorHandler.setMonitor(monitor);
			validator.setErrorHandler(errorHandler);
			if(monitor != null)
				monitor.worked(1);
			validator.validate(new StreamSource(new File(file)));
			if(monitor != null)
				monitor.worked(1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
     
	public void checkSingle(String file, ReportBean report,IProgressMonitor monitor,Schema schema) {
		if(!mustCheck)
			return;
		checkSchema(file, report, monitor,schema);
	}
}
