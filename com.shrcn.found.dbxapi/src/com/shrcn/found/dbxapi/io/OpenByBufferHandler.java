package com.shrcn.found.dbxapi.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ByteBufferUtil;
import com.shrcn.found.file.util.FileManipulate;

public class OpenByBufferHandler implements ContentHandler {
	
	private static final String handleDir = Constants.tempDir + "scdTemp";
	
	private List<String[]> iednames = new ArrayList<String[]>();
	private List<String> subNetwork = new ArrayList<String>();
	
	private enum Section {History, Substation, Communication, IED, DataTypeTemplates};
	private Section currSec = null;
	private String currIedName = null;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1014 * 5);
//	private char[] buffer = new char[1024 * 1014 * 5];
//	private int size = 0;
//	private StringBuilder doc = new StringBuilder(1024 * 1014 * 5);

	public List<String[]> getIeds() {
		return iednames;
	}

	public List<String> getSubNets() {
		return subNetwork;
	}
	
	public OpenByBufferHandler() {
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

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		for (Section s : Section.values()) {
			if (s.name().equals(qName)) {
				this.currSec = s;
				buffer.clear();
//				size = 0;
//				doc.delete(0, doc.length());
//				doc = new StringBuilder(1024 * 1014 * 5);
				break;
			}
		}
		if ("IED".equals(qName)) {
			String name = atts.getValue("name");
			String desc = atts.getValue("desc");
			String type = atts.getValue("type");
			String manufacturer = atts.getValue("manufacturer");
			String[] ied = new String[] {name, desc, type, manufacturer};
			iednames.add(ied);
			currIedName = name;
		} else if ("SubNetwork".equals(qName)) {
			String sub = atts.getValue("name");
			subNetwork.add(sub);
		}
		if (currSec != null) {
			StringBuilder startEl = new StringBuilder();
			startEl.append("<" + qName);
			for (int i=0; i<atts.getLength(); i++) {
				startEl.append(" " + atts.getQName(i) + "=\"" + atts.getValue(i) + "\"");
			}
			startEl.append(">");
			buffer.put(startEl.toString().getBytes());
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (currSec != null) {
//			doc.append("</" + qName + ">");
			String endEl = "</" + qName + ">";
			buffer.put(endEl.getBytes());
		}
		if (currSec != null && currSec.name().equals(qName) && buffer.position()>0) {
			String fname = null;
			if (currSec != Section.IED) {
				fname = currSec.name();
			} else {
				fname = "IED_" + currIedName;
			}
//			try {
//				String path = handleDir + File.separator + fname;
//				String content = doc.toString();
//				FileManager.saveTextFile(content, path);
//			} catch (UnsupportedEncodingException e) {
//				SCTLogger.error("SCD临时文件输出错误", e);
//			}
			FileOutputStream os = null;
			try {
				os = new FileOutputStream(handleDir + File.separator + fname);
				os.write(ByteBufferUtil.getData(buffer));
//				os.getChannel().write(buffer);
			} catch (IOException e) {
				SCTLogger.error("SCD临时文件输出错误", e);
			} finally {
				try {
					buffer.clear();
					if (os != null)
						os.close();
				} catch (IOException e) {
					SCTLogger.error("SCD临时文件输出错误", e);
				}
			}
			this.currSec = null;
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
//		if (currSec != null) {
//			for (int i=start; i<length; i++) {
////				buffer.putChar(ch[i]);
//				buffer[size] = ch[i];
//				size++;
//			}
//		}
//		if (currSec==Section.Communication) {
//			System.out.println(new String(ch, start, length));
//		}
//		doc.append(new String(ch, start, length));
		buffer.put(new String(ch, start, length).getBytes());
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
