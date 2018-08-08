package com.shrcn.found.dbxapi.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManipulate;

public class OpenByStringHandler implements ContentHandler {
	
	private static final String handleDir = Constants.tempDir + "scdTemp";

	private String rootNode = null;
	private ScdSection currSec = null;
	private String currIedName = null;
	
	private List<String[]> iednames = new ArrayList<String[]>();
	private List<String> subNetwork = new ArrayList<String>();
	private StringBuilder doc = new StringBuilder(1024 * 1014 * 5);
	private Map<String, String> docCache = new HashMap<>();

	public List<String[]> getIeds() {
		return iednames;
	}

	public List<String> getSubNets() {
		return subNetwork;
	}
	
	public String getPart(String fname) {
		return docCache.get(fname);
	}
	
	public OpenByStringHandler() {
		init();
	}

	private void init() {
		File dir = new File(handleDir);
		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File f : files) {
				f.delete();
			}
		} else {
			FileManipulate.initDir(handleDir);
		}
	}
	
	public void reset() {
		rootNode = null;
		currSec = null;
		currIedName = null;
		iednames.clear();
		subNetwork.clear();
		doc.delete(0, doc.length());
		docCache.clear();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		if ("SCL".equals(qName)) {
			rootNode = "<SCL";
			for (int i=0; i<atts.getLength(); i++) {
				rootNode += 
						" " + atts.getQName(i) + "=\"" + atts.getValue(i) + "\"";
			}
			rootNode += ">";
		}
		for (ScdSection s : ScdSection.values()) {
			if (s.name().equals(qName)) {
				this.currSec = s;
				doc.delete(0, doc.length());
				if (rootNode != null)
					doc.append(rootNode);
				break;
			}
		}
		if ("IED".equals(qName)) {
			String name = atts.getValue("name");
			String desc = atts.getValue("desc");
			String type = atts.getValue("type");
			String manufacturer = atts.getValue("manufacturer");
			String configVersion = atts.getValue("configVersion");
			String[] ied = new String[] {name, desc, type, manufacturer, configVersion};
			iednames.add(ied);
			currIedName = name;
		} else if ("SubNetwork".equals(qName)) {
			String sub = atts.getValue("name");
			subNetwork.add(sub);
		}
		if (currSec != null) {
			doc.append("<" + qName);
			for (int i=0; i<atts.getLength(); i++) {
				doc.append(" " + atts.getQName(i) + "=\"" + atts.getValue(i) + "\"");
			}
			doc.append(">");
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (currSec != null) {
			doc.append("</" + qName + ">");
		}
		if (currSec != null && currSec.name().equals(qName) && doc.length()>0) {
			if (rootNode != null)
				doc.append("</SCL>");
			String fname = null;
			if (currSec != ScdSection.IED) {
				fname = currSec.name();
			} else {
				fname = "IED_" + currIedName;
			}
			docCache.put(fname, doc.toString());
			this.currSec = null;
			if (Constants.DEBUG) {
				fname = handleDir + File.separator + fname;
				String content = doc.toString();
	//			FileManager.saveTextFile(fname, content, Constants.CHARSET_UTF8);
				try {
					FileOutputStream os = new FileOutputStream(fname);
					os.write(content.getBytes());
				} catch (IOException e) {
					SCTLogger.error("SCD临时文件输出错误", e);
				}
			}
		}
	}
	
	@Override
	public void setDocumentLocator(Locator locator) {
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		doc.append(new String(ch, start, length));
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}
}
