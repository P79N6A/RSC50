/**
 * Copyright (c) 2007-2013 上海思弘瑞电力控制技术有限公司. All rights reserved. 
 * This program is an eclipse Rich Client Application
 * designed for IED configuration and debuging.
 */
package com.shrcn.found.file.xml;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-4-12
 */
/**
 * $Log: JAXBUtil.java,v $
 * Revision 1.3  2013/04/18 06:03:03  scy
 * Fix Bug:修改文件生成错误
 *
 * Revision 1.2  2013/04/17 02:08:45  cchun
 * Fix Bug:修改文件解析错误
 *
 * Revision 1.1  2013/04/12 05:14:02  cchun
 * Add:JAXB基础类
 *
 */
public class JAXBUtil {

	private JAXBUtil() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 将XML转成对象。
	 * @param contextPath
	 * @param xmlStream
	 * @return
	 * @throws JAXBException
	 */
	public static Object unmarshal(String contextPath, ClassLoader classLoader, InputStream xmlStream)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(contextPath, classLoader);
		Unmarshaller u = jc.createUnmarshaller();
		return u.unmarshal(xmlStream);
	}

	/**
	 * 将对象转成XML。
	 * @param contextPath
	 * @param obj
	 * @param stream
	 * @throws JAXBException
	 */
	public static void marshal(String contextPath, ClassLoader classLoader, Object obj,
			OutputStream stream, String charset) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(contextPath, classLoader);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, charset);
		m.marshal(obj, stream);
	}
}
