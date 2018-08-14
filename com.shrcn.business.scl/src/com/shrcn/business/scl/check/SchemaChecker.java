/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.xml.sax.SAXException;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.xml.schema.SCLValidator;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.util.URLUtil;
import com.shrcn.found.file.xml.ReportBean;
import com.shrcn.found.ui.util.DialogHelper;
import com.shrcn.found.ui.util.TaskManager;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-12
 */
public class SchemaChecker {
	
	public static void check() {
		final String scdName = Constants.DEFAULT_PRJECT_NAME;
		final String checkPath = Constants.tempDir + scdName;
		final Schema chechSchema = getVerSchema(SCTProperties.getInstance().getSchemaVersion());
		check(scdName, checkPath, chechSchema, true);
	}
	
	public static void check(String path, String name) {
		final Schema chechSchema = getVerSchema(SCTProperties.getInstance().getSchemaVersion());
		check(name, path, chechSchema, false);
	}

	public static void check(final String scdName, final String checkPath, final Schema chechSchema, final boolean needTemp) {
		TaskManager.addTask(new Job("Schema检查") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (needTemp) {
//					XMLDBHelper.exportFormatedDoc(URLUtil.string2URL(scdName),
					XMLDBHelper.exportFormatedDoc(StringUtil.string2Unicode(scdName),
							Constants.DEFAULT_SCD_DOC_NAME, checkPath);
				}
				checkSchemaSingle(checkPath, chechSchema);
				return Status.OK_STATUS;
			}
		});
	}
	
	/**
	 * 根据用户选择的路径得到Schema
	 * @param path
	 * @return
	 */
	public static Schema getPathSchema(String path) {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			File fXsd = new File(path + File.separator + "SCL.xsd");
			if (fXsd.exists()) {
				schema = factory.newSchema(fXsd);
			} else {
				DialogHelper.showWarning("你选择的Schema路径下无SCL.xsd文件！");
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return schema;
	}
    
	/**
	 * 根据用户选择的Schema版本得到Schema
	 * @param version
	 * @return
	 */
	public static Schema getVerSchema(String version) {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			String ver = version;
			ver = "v" + ver.replace(".", "_");
			schema = factory.newSchema(SCLValidator.class.getResource("xsd/"
					+ ver + "/SCL.xsd"));
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return schema;
	}
	
	/**
	 * schema校验
	 * @param scdfile
	 * @param force
	 * @return
	 */
	public static boolean checkSchema(String scdfile, boolean force) {
		ReportBean report = new ReportBean();
		SCTProperties properties = SCTProperties.getInstance();
		if (properties.needsXSDCheck() || force) {
			SCLValidator.getInstance().check(scdfile, report, null);
		}
		if (report.getCount() > 0) {
			exportWarnMessages(report);
			return false;
		} else if (properties.needsXSDCheck()) {
			ConsoleManager.getInstance().append("SCL Schema 校验通过。", false);
		}
		return true;
	}
	
	public static boolean checkSchemaSingle(String scdfile,Schema schema) {
		ReportBean report = new ReportBean();
		SCLValidator.getInstance().checkSingle(scdfile, report, null, schema);
		if (report.getCount() > 0) {
			exportWarnMessages(report);
			return false;
		} else {
			ConsoleManager.getInstance().append("SCL Schema 校验通过。", false);
		}
		return true;
	}

	/**
	 * 向输出视图添加schema校验信息
	 * 
	 * @param report
	 */
	private static void exportWarnMessages(ReportBean report) {
		String content = "-------SCL Schema校验开始-------\n\n"; //$NON-NLS-1$
		content += report.getMessage().toString();
		content += "\n共" + report.getCount() + "处错误。"; //$NON-NLS-1$ //$NON-NLS-2$
		content += "\n\n-------SCL Schema校验结束-------"; //$NON-NLS-1$
		// 输出到视图
		ConsoleManager.getInstance().append(content, false);
		// 输出到schema校验日志文件
		FileWriter writer = null;
		try {
			writer = new FileWriter(Constants.schemaLog);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
		} finally {
			try {
				if (null != writer)
					writer.close();
			} catch (IOException e) {
			}
		}
	}
}

