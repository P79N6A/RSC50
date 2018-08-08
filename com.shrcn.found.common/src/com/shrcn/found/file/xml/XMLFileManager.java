/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.util.Messages;

/**
 * XML文件读写管理.
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-5-20
 */
public class XMLFileManager {

	/**
	 * 读取xml文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 * @throws DocumentException
	 */
	public static Document readXml(String filePath) {
		if (filePath != null) {
			File file = new File(filePath);
			Document doc = null;
			if (!file.exists()) {/* 目标文件不存在返回NULL */
				return null;
			} else {
				SAXReader reader = new SAXReader();/* 读文件并返回Document对象 */
				try {
					doc = reader.read(file);
				} catch (DocumentException e) {
					e.printStackTrace();
				}
			}
			return doc;
		}
		return null;
	}
	
	/**
	 * 根据指定字符集读取XML文件。
	 * @param filePath
	 * @param charSet
	 * @return
	 */
	public static Document readXml(String filePath, String charSet) {
		File file = new File(filePath);
		Document doc = null;
		if (!file.exists()) {/* 目标文件不存在返回NULL */
			return null;
		} else {
			try {
				InputStream is = new FileInputStream(filePath);
				InputStreamReader isr = new InputStreamReader(is, charSet);
				SAXReader reader = new SAXReader();/* 读文件并返回Document对象 */
				doc = reader.read(isr);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}
	
	/**
	 * 保存xml文件
	 * 
	 * @param doc
	 * @param fileName
	 */
	public static void writeDoc(Document doc, String fileName) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(Constants.CHARSET_UTF8);
		XMLWriter writer = null;
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(fileName);
			writer = new XMLWriter(os, format);
			writer.write(doc);
			os.flush();
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (writer != null) {
					writer.close();
					writer = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 格式化doc
	 * 
	 * @param document
	 * @return
	 */
	public static String formatDocument(Document document) {
		StringWriter stringWriter = new StringWriter();
		OutputFormat opf = OutputFormat.createPrettyPrint();
		opf.setEncoding(Constants.CHARSET_UTF8); //$NON-NLS-1$
		String s = null;
		XMLWriter xmlWriter = new XMLWriter(stringWriter, opf);
		try {
			xmlWriter.write(document);
			s = stringWriter.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				xmlWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}

	/**
	 * 创建Document对象doc,把文件内容写到doc中
	 * 
	 * @return 返回doc
	 */
	public static Document createDocument(File f) throws IllegalArgumentException {
		Document doc = null;
		SAXReader read = new SAXReader();
		try {
			doc = read.read(f);
		} catch (DocumentException e) {
			throw new IllegalArgumentException(Messages.getString("FileManager.file_format_wrong") //$NON-NLS-1$
					+ e.getLocalizedMessage());
		}
		return doc;
	}
	
	/**
	 * 创建W3C, Document对象doc,把文件内容写到doc中
	 * 
	 * @param fileName
	 * @return
	 */
	public static org.w3c.dom.Document createW3CDocument(InputStream inputStream) {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		org.w3c.dom.Document document = null;
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			document = builder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * 从xml文件创建document
	 * @param file
	 * @return
	 */
	public static Document createDocument(String file) {
		SAXReader reader = new SAXReader();
		Document result = null;
		try {
			result = reader.read(new File(file));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 将xml文件转成Document
	 * @param clazz
	 * @param path 为包路径形式的资源定位串，例如:com/shrcn/sct/rtu/das/default.cfg
	 * @return
	 */
	public static Document loadXMLFile(Class<?> clazz, String path) {
		if (StringUtil.isEmpty(path))
			return null;
		InputStream is = null;
		XMLWriter writer = null;
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		try {
			is = clazz.getClassLoader().getResourceAsStream(path);
			doc = saxReader.read(is);
		} catch (DocumentException e) {
			SCTLogger.error("文件异常：" + e.getMessage());
		} finally {
			try {
				if(null != writer)
					writer.close();
				if(null != is)
					is.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
		return doc;
	}
	
	/**
	 * 读取指定位置的xml文件
	 * @param path
	 * @return DOM
	 * @throws IOException 
	 */
	public static Document loadXMLFile(String path){
		try {
			return loadXMLFile(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取指定位置的xml文件
	 * @param path
	 * @return DOM
	 * @throws IOException 
	 */
	public static Document loadXMLFileWithExp(String path) throws IOException,DocumentException,FileNotFoundException {
		return loadXMLFile(new File(path));
	}
	
	/**
	 * 读取指定位置的xml文件
	 * @param file
	 * @return
	 */
	public static Document loadXMLFile(File file) throws IOException,DocumentException,FileNotFoundException{
		if (!file.exists())
			return null;
		InputStream is = null;
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		String path = file.getAbsolutePath();
		try {
			is = new FileInputStream(file);
			doc = saxReader.read(is);
		} catch (DocumentException e) {
			SCTLogger.error(path + "文件异常：" + e.getMessage());
			throw e;
		} catch (FileNotFoundException e) {
			SCTLogger.error(path + "未找到文件异常：" + e.getMessage());
			throw e;
		} finally {
			try {
				if(null != is)
					is.close();
			} catch (IOException e) {
				SCTLogger.error(path + "IO处理异常：" + e.getMessage());
				throw e;
			}
		}
		return doc;
	}
	
	/**
	 * 保存XML Document（指定字符集）
	 * @param doc
	 * @param outPath
	 * @param charSet
	 */
	public static void saveDocument(Document doc, String outPath, String charSet) {
		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(charSet);
			writer = new XMLWriter(new FileOutputStream(new File(outPath)), format);
			writer.write(doc);
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("编码异常：" + e.getMessage());
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} finally {
			try {
				if(null != writer)
					writer.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}
	
	/**
	 * 保存Document至文件（UTF-8编码）
	 * @param doc
	 * @param outPath
	 */
	public static void saveUTF8Document(Document doc, String outPath) {
		saveDocument(doc, outPath, Constants.CHARSET_UTF8);
	}
	
	/**
	 * 保存Document至文件（GBK编码）
	 * @param doc
	 * @param outPath
	 */
	public static void saveGB2312Document(Document doc, String outPath) {
		saveDocument(doc, outPath, Constants.CHARSET_GB2312);
	}
	
	/**
	 * 保存字符串至UTF-8文件
	 * @param content
	 * @param target
	 */
	public static void saveUTF8File(String content, File target) {
		XMLWriter writer = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding(Constants.CHARSET_UTF8);
			saxReader.setFeature("http://xml.org/sax/features/namespaces", false);
			Document doc = saxReader.read(new ByteArrayInputStream(content.getBytes("UTF-8")));
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Constants.CHARSET_UTF8);
			writer = new XMLWriter(new FileOutputStream(target), format);
			writer.write(doc);
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("编码异常：" + e.getMessage());
		} catch (FileNotFoundException e) {
			SCTLogger.error("未找到文件异常：" + e.getMessage());
		} catch (IOException e) {
			SCTLogger.error("IO处理异常：" + e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error("文件异常：" + e.getMessage());
		} catch (SAXException e) {
			SCTLogger.error("解析异常：" + e.getMessage());
		} finally {
			try {
				if(null != writer)
					writer.close();
			} catch (IOException e) {
				SCTLogger.error("IO处理异常：" + e.getMessage());
			}
		}
	}
	
	/**
	 * 保存字符串至UTF-8文件
	 * @param content
	 * @param target
	 */
	public static void saveUTF8File(String content, String outPath) {
		String charset = Constants.CHARSET_UTF8;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding(charset);
			saxReader.setFeature("http://xml.org/sax/features/namespaces", false);
			Document doc = saxReader.read(new ByteArrayInputStream(content.getBytes(charset)));
			XMLFileManager.saveUTF8Document(doc, outPath);
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("编码异常：" + e.getMessage());
		} catch (DocumentException e) {
			SCTLogger.error("文件异常：" + e.getMessage());
		} catch (SAXException e) {
			SCTLogger.error("解析异常：" + e.getMessage());
		}
	}
	
	/**
	 * 利用sax解析xml文件（UTF-8）
	 * @param filePath
	 * @param handler
	 * @return
	 */
	public static String parseUTF8BySax(String filePath, ContentHandler handler) {
		return parseBySax(filePath, handler, Constants.CHARSET_UTF8);
	}
	
	/**
	 * 利用sax解析xml文件
	 * @param filePath
	 * @param handler
	 * @return
	 */
	public static String parseBySax(String filePath, ContentHandler handler, String charset) {
		String msg = null;
		FileInputStream fis = null;
		try {
	        fis = new FileInputStream(new File(filePath));
	        msg = parseBySax(fis, handler, charset);
		} catch (IOException e) {
			msg = e.getMessage();
			SCTLogger.error(msg, e);
		}
		return msg;
	}
	
	public static String parseUTF8BySax(Class<?> clazz, String filePath, ContentHandler handler) {
		return parseBySax(clazz, filePath, handler, Constants.CHARSET_UTF8); 
	}
	
	public static String parseBySax(Class<?> clazz, String filePath, ContentHandler handler, String charset) {
		InputStream fis = clazz.getClassLoader().getResourceAsStream(filePath);
		return parseBySax(fis, handler, charset);
	}
	
	public static String parseUTF8BySax(InputStream fis, ContentHandler handler) {
		return parseBySax(fis, handler, Constants.CHARSET_UTF8);
	}
	
	public static String parseBySax(InputStream fis, ContentHandler handler, String charset) {
		String msg = null;
		try {
			// 1、创建工厂  
	        SAXParserFactory factory = SAXParserFactory.newInstance();  
	        // 2、得到解析器  
	        SAXParser saxParser = factory.newSAXParser();  
	        // 3、得到读取器  
	        XMLReader reader = saxParser.getXMLReader();  
	        reader.setContentHandler(handler);
			InputSource is = new InputSource(fis);
	        is.setEncoding(charset);
	        reader.parse(is);
		} catch (SAXParseException exception) {
			msg = "行" + exception.getLineNumber() + "，列" + exception.getColumnNumber() + "："
					+ exception.getMessage();
			SCTLogger.error(msg, exception);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			msg = e.getMessage();
			SCTLogger.error(msg, e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				msg = e.getMessage();
				SCTLogger.error(msg, e);
			}
		}
		return msg;
	}
}
