package com.shrcn.found.xmldb.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.shrcn.found.common.util.TimeCounter;

public class OpenProjectHandler implements ContentHandler {
	
	private static final String uri = "http://www.iec.ch/61850/2003/SCL";
	private List<String> iednames = new ArrayList<String>();

	@Override
	public void setDocumentLocator(Locator locator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if ("IED".equals(qName)) {
			String name = atts.getValue("name");
			String desc = atts.getValue("desc");
//			System.out.println("装置：" + name + "，" + desc);
			iednames.add("装置：" + name + "，" + desc);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	public List<String> getIednames() {
		return iednames;
	}

	public static void main(String[] args) {
//		String scdfile = "C:\\Documents and Settings\\chenchun\\桌面\\test1.scd";
		String scdfile = "E:/work/检测/六统一/测试数据/SCD-电科院0/测试用SCD-3/scd(1-1)_1000个ied.scd";
//		String scdfile = "C:\\Documents and Settings\\chenchun\\桌面\\ZJHHB201507221630.scd";
//		try {
//			ContentHandler handler = new OpenProjectHandler();
//	      SAXParser parser = new SAXParserImpl(); 
//	      parser.setAttribute(SAXParser.STANDALONE, Boolean.valueOf(true)); 
//	      parser.setValidationMode(SAXParser.NONVALIDATING); 
//	      parser.setContentHandler(handler); 
////	      this.saxProcessor.setParserActive(); 
//	      parser.parse(new FileInputStream(scdfile)); 
//
//	    } catch (ProcessingCompleteException pce) { 
//	      pce.printStackTrace(); 
//	    } catch (Exception e) { 
//	      e.printStackTrace(); 
//	    } 
		
		try {
			TimeCounter.begin();
			OpenProjectHandler handler = new OpenProjectHandler();
			// 1、创建工厂  
	        SAXParserFactory factory = SAXParserFactory.newInstance();  
	        // 2、得到解析器  
	        SAXParser saxParser = factory.newSAXParser();  
	        // 3、得到读取器  
	        XMLReader reader = saxParser.getXMLReader();  
	        reader.setContentHandler(handler);
//	        reader.parse(scdfile);
	        FileInputStream fis = new FileInputStream(scdfile);
			InputSource is = new InputSource(fis);
	        is.setEncoding("UTF-8");
	        reader.parse(is);
	        fis.close();
	        TimeCounter.end("SCD解析完毕");
	        System.out.println("装置台数：" + handler.getIednames().size());
		} catch (Exception e) { 
	      e.printStackTrace(); 
	    } 
	}
}
