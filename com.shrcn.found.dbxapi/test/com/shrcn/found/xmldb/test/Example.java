package com.shrcn.found.xmldb.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.shrcn.found.common.util.TimeCounter;

public class Example {
	private static final String uri = "http://www.iec.ch/61850/2003/SCL";
	
	public static void main(final String[] args) {
		String scdfile = "C:\\Documents and Settings\\chenchun\\桌面\\test1.scd";
//		String scdfile = "C:\\Documents and Settings\\chenchun\\桌面\\ZJHHB201507221630.scd";
		try  {
			TimeCounter.begin();
//			FileReader reader = new FileReader(scdfile);
			FileInputStream is = new FileInputStream(scdfile);
			XMLInputFactory factory = XMLInputFactory.newFactory();
			factory.setProperty("javax.xml.stream.isCoalescing", Boolean.TRUE);
			XMLEventReader xereader = factory.createXMLEventReader(is, "UTF-8");
			List<String> iednames = new ArrayList<String>();
			while (xereader.hasNext()) {
				XMLEvent e = xereader.nextEvent();
//				if (e.isCharacters()) {
//					Characters cdata = e.asCharacters();
//					if (cdata.isWhiteSpace())
//						continue;
//					System.out.println(cdata.getData());
//				}
				if (e.isStartElement()) {
					StartElement sel = e.asStartElement();
					QName qname = sel.getName();
					if ("IED".equals(qname.getLocalPart())) {
//						String name = sel.getAttributeByName(new QName(uri, "name")).getValue();
//						String desc = sel.getAttributeByName(new QName(uri, "desc")).getValue();
//						System.out.println("装置：" + name + "，" + desc);
						Iterator attributes = sel.getAttributes();
						Object o = null;
						String name = null;
						String desc = null;
						for (;attributes.hasNext();) {
							o = attributes.next();
							Attribute att = (Attribute) o;
							String attName = att.getName().getLocalPart();
							if (attName.equals("name"))
								name = att.getValue();
							else if (attName.equals("desc"))
								desc = att.getValue();
						}
//						System.out.println("装置：" + name + "，" + desc);
						iednames.add("装置：" + name + "，" + desc);
					}
				}
			}
			xereader.close();
			TimeCounter.end("SCD解析完毕");
			System.out.println("装置台数：" + iednames.size());
//			XMLStreamReader xsreader = factory.createXMLStreamReader(reader);
//			while(xsreader.hasNext()) {
//				 int eventType = xsreader.next();
//				 if (eventType == XMLStreamConstants.START_ELEMENT) {
//					 if ("IED".equals(xsreader.getLocalName())) {
//						String name = xsreader.getAttributeValue(uri, "name");
//						String desc = xsreader.getAttributeValue(uri, "desc");
//						System.out.println("装置：" + name + "，" + desc);
//					 }
//				 }
//			}
//			xsreader.close();
		} catch (XMLStreamException | IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
