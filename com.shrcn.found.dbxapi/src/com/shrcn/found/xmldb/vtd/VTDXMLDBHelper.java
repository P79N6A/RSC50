/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.AbstractDBBroker;
import com.shrcn.found.xmldb.IFunction;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.ximpleware.AutoPilot;
import com.ximpleware.FastLongBuffer;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-9-30
 */
public class VTDXMLDBHelper extends AbstractDBBroker {
	
	private static final String charset = Constants.CHARSET_UTF8;
	private VTDGen vg;
	private VTDNav vn;
	private AutoPilot ap;
	private XMLModifier xm;
	private boolean autoCommit = false;
	private boolean fixingModify = false;

	@Override
	public synchronized void close() {
		if (vg != null) {
			vg.clear();
			System.gc();
			System.gc();
		}
		if (xm != null)
			xm.reset();
		vg = null;
		vn = null;
	}

	@Override
	public synchronized String getDocXPath() {
		return null;
	}

	@Override
	public synchronized boolean existsDocument(String docName) {
		return false;
	}
	
	private void parse(byte[] b) throws ParseException {
		if (b == null || b.length < 1) {
			return;
		}
		if (vg != null) {
			vg.clear();
			System.gc();
			System.gc();
		}
		this.vg = new VTDGen();
		vg.setDoc_BR(b);
		vg.parse(false);
		this.vn = vg.getNav();
		this.ap = new AutoPilot(vn);
		this.xm = null;
	}
	
	private XMLModifier getXMLModifier() throws ModifyException {
		if (xm == null)
			this.xm = new XMLModifier(this.vn);
		return this.xm;
	}
	
	@Override
	public synchronized void loadDocument(String docName, String filePath) {
		File f = new File(filePath);
		if (!f.exists() || f.length()<1)
			throw new RuntimeException(filePath + "内容为空！");
		
		if (vg != null) {
			vg.clear();
			System.gc();
			System.gc();
		}
		this.vg = new VTDGen();
		vg.parseFile(filePath, false);
		this.vn = vg.getNav();
		this.ap = new AutoPilot(vn);
		this.xm = null;
	}

	@Override
	public synchronized void loadElementAsDocument(String docName, Element node) {
		loadDocFromString(docName, node.asXML());
	}

	@Override
	public synchronized void loadDocFromString(String docName, String xmlStr) {
		try {
			parse(xmlStr.getBytes());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void removeDocument(String docName, String colName) {
	}

	@Override
	public synchronized void exportFormatedDoc(String projectName, String docName,
			String outPath) {
		FileOutputStream fos = null;
		try {
			if (hasModified()) {
				fos = new FileOutputStream(outPath);
				xm.output(fos);
				if (xm != null)
					xm.reset();
			} else {
				if (vn != null)
					vn.dumpXML(outPath);
			}
		} catch (IOException | ModifyException | TranscodeException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (fos!=null)
					fos.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void exportFormatedDoc(String outPath) {
		exportFormatedDoc(null, null, outPath);
	}
	
	private void selectXPath(String select) throws XPathParseException {
		select = DOM4JNodeHelper.clearPrefix(select);
		ap.selectXPath(select);
	}

	@Override
	public synchronized List<Element> selectNodes(String select) {
		List<Element> result = new ArrayList<Element>();
		try {
			selectXPath(select);
			byte[] ba = vn.getXML().getBytes();
			String strResult = "";
			while ((ap.evalXPath()) != -1) {
				long l = vn.getElementFragment();
	            int offset = (int)l;
	            int len = (int)(l >> 32);
	            byte[] ndData = new byte[len];
	            System.arraycopy(ba, offset, ndData, 0, len);
	            strResult += new String(ndData, charset);
			}
			ap.resetXPath();
			if (null != strResult && !"".equals(strResult))
				result = XMLDBHelper.getNodesFromResult(strResult);
		} catch (XPathParseException | UnsupportedEncodingException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Override
	public synchronized List<Element> selectNodesOnlyAtts(String select, String ndName) {
		List<Element> result = new ArrayList<Element>();
		try {
			selectXPath(select);
			String strResult = "";
			int i = ap.evalXPath();
			while (i != -1) {
				strResult += "<" + ndName;
				for (int j=0; j<vn.getAttrCount()*2; j+=2) {
					String attName = vn.toString(i + j + 1);
					strResult += " " + attName + "=\"" + vn.toString(vn.getAttrVal(attName)) + "\"";
				}
				strResult += "/>";
				i = ap.evalXPath();
			}
			ap.resetXPath();
			if (null != strResult && !"".equals(strResult))
				result = XMLDBHelper.getNodesFromResult(strResult);
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public synchronized List<Element> selectNodes(String select, int offset, int number) {
		List<Element> resultAll = selectNodes(select);
		return resultAll.subList(offset, offset + number);
	}

	@Override
	public synchronized Element selectSingleNode(String select) {
		List<Element> result = new ArrayList<Element>();
		try {
			selectXPath(select);
			byte[] ba = vn.getXML().getBytes();
			String strResult = "";
			if (ap.evalXPath() != -1) {
				long l = vn.getElementFragment();
	            int offset = (int)l;
	            int len = (int)(l >> 32);
	            byte[] ndData = new byte[len];
	            System.arraycopy(ba, offset, ndData, 0, len);
	            strResult += new String(ndData, charset);
			}
			ap.resetXPath();
			if (null != strResult && !"".equals(strResult))
				result = XMLDBHelper.getNodesFromResult(strResult);
		} catch (XPathParseException | UnsupportedEncodingException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return result.size()>0 ? result.get(0) : null;
	}

	@Override
	public synchronized boolean existsNode(String select) {
		return countNodes(select)>0;
	}

	@Override
	public synchronized int countNodes(String select) {
		int count = 0;
		try {
			selectXPath(select);
			while ((ap.evalXPath()) != -1) {
				count++;
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return count;
	}

	@Override
	public synchronized void insertBefore(String select, String content) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.insertBeforeElement(content + "\n");
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				insertBefore(select, content);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void insertAfter(String select, String content) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.insertAfterElement(content + "\n");
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				insertAfter(select, content);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}
	
	@Override
	public synchronized void insertAfterType(String parentXPath, String[] types, String node) {
		try {
			XMLModifier xm = getXMLModifier();
			if (types == null || types.length == 0) {
				selectXPath(parentXPath);
				insertAsLast(parentXPath, node);
			} else {
				boolean success = false;
				for (int i=types.length-1; i>-1; i--) {
					selectXPath(parentXPath + "/" + types[i] + "[last()]");
					if (ap.evalXPath() != -1) {
						xm.insertAfterElement(node + "\n");
						if (isAutoCommit())
							forceCommit();
						success = true;
						break;
					}
				}
				if (!success)
					insertAsLast(parentXPath, node);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				insertAfterType(parentXPath, types, node);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void insertAsFirst(String select, String content) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.insertAfterHead("\n" + content);
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				insertAsFirst(select, content);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void insertAsLast(String select, String content) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.insertBeforeTail(content + "\n");
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				insertAsLast(select, content);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void moveDown(String select, int curRow) {
		moveTo(select, curRow, curRow + 1);
	}

	@Override
	public synchronized void moveUp(String select, int curRow) {
		moveTo(select, curRow, curRow - 1);
	}
	
	@Override
	public synchronized void moveTo(String select, int curRow, int newRow) {
		if (curRow == newRow)
			return;
		select = DOM4JNodeHelper.clearPrefix(select);
		String xqDelete = select + "[" + curRow + "]"; 
		String xqInsert = select + "[" + (newRow - 1) + "]";
		try {
			Element curNode = selectSingleNode(xqDelete);
			if(null == curNode)
				return;
			deleteNodes(xqDelete);
			if (newRow == 1)
				insertBefore(select + "[1]", curNode);
			else
				insertAfter(xqInsert, curNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized void insert(String select, String content) {
		insertAsLast(select, content);
	}
	
	@Override
	public synchronized void replaceNode(String select, String node) {
		int count = countNodes(select);
		if (count < 1) {
			throw new RuntimeException(select + "对应的目标节点不存在！");
		} else if (count > 1) {
			throw new RuntimeException(select + "同时存在多个目标节点！");
		}
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				xm.remove();
				xm.insertBeforeElement(node);
				if (isAutoCommit())
					forceCommit();
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				replaceNode(select, node);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void deleteNodes(String select) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select);
			int rms = 0;
			while (ap.evalXPath() != -1) {
				xm.remove();
				rms++;
			}
			if (isAutoCommit() && rms>0)
				forceCommit();
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				deleteNodes(select);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void saveAttribute(String ndXpath, String attName, String value) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(ndXpath);
			if (ap.evalXPath() != -1) {
				xm.updateToken(vn.getAttrVal(attName), value);
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + ndXpath);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				saveAttribute(ndXpath, attName, value);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized String getAttributeValue(String select) {
		try {
			selectXPath(select);
			if (ap.evalXPath() != -1) {
				String attName = select.substring(select.lastIndexOf("@") + 1);
				return vn.toString(vn.getAttrVal(attName));
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public synchronized List<String> getAttributeValues(String select) {
		List<String> values = new ArrayList<>();
		try {
			selectXPath(select);
			String attName = select.substring(select.lastIndexOf("@") + 1);
			while (ap.evalXPath() != -1) {
				values.add(vn.toString(vn.getAttrVal(attName)));
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return values;
	}

	@Override
	public synchronized String getNodeValue(String select) {
		try {
			selectXPath(select + "/text()");
			int i = ap.evalXPath();
			if (i != -1) {
				return vn.toString(i);
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public synchronized void appendAttribute(String ndXpath, String attName, String value) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(ndXpath);
			if (ap.evalXPath() != -1) {
				xm.insertAttribute(" " + attName + "=\"" + value + "\"");
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + ndXpath);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				appendAttribute(ndXpath, attName, value);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void update(String select, String value) {
		try {
			XMLModifier xm = getXMLModifier();
			selectXPath(select + "/text()");
			int i = ap.evalXPath();
			if (i < 0) {
				selectXPath(select);
				i = ap.evalXPath();
			}
			if (i != -1) {
				xm.updateToken(i, value);
				if (isAutoCommit())
					forceCommit();
			} else {
				throw new RuntimeException("找不到目标节点" + select);
			}
			ap.resetXPath();
		} catch (ModifyException | XPathParseException | XPathEvalException | NavException | UnsupportedEncodingException e) {
			if ((e instanceof ModifyException) && !fixingModify) {
				fixingModify = true;
				forceCommit();
				update(select, value);
				fixingModify = false;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void insertNodeByDoc(Element element, String docName, String select) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized void replaceNodeByDoc(Element element, String docName, String select) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized List<String> getAttributeStrings(String xquery) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized List<Element> queryNodes(String xquery) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized List<String> queryAttributes(String xquery) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized String queryAttribute(String xquery) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized int executeUpdate(String xupdate) {
		throw new UnsupportedOperationException("VTD-XML不支持XQuery！");
	}

	@Override
	public synchronized boolean isAutoCommit() {
		return this.autoCommit;
	}

	@Override
	public synchronized void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	@Override
	public synchronized void forceBegin() {
		throw new UnsupportedOperationException("VTD-XML不支持事务开始！");
	}
	
	private boolean hasModified() {
		if (xm == null)
			return false;
		FastLongBuffer flb = (FastLongBuffer) ObjectUtil.getFieldValue(xm, "flb");
		return flb.size() > 0;
	}
	
	private boolean isBig() throws ModifyException, TranscodeException {
		int max = 1024 * 1024 * 100;
		return xm!=null && (xm.getUpdatedDocumentSize() > max);
	}

	@Override
	public synchronized void forceCommit() {
		if (xm == null || !hasModified())
			return;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
//			if (isBig())
//				return;
			if (xm != null)
				xm.output(os);
			parse(os.toByteArray());
			if (xm != null)
				xm.reset();
		} catch (ModifyException | TranscodeException | IOException | ParseException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void forceRollback() {
		throw new UnsupportedOperationException("VTD-XML不支持事务回滚！");
	}

	@Override
	public IFunction createFunction(Class<?> funClass,
			Map<String, Object> params) {
		return (IFunction) ObjectUtil.newInstance(funClass, 
				new Class<?>[]{AutoPilot.class, VTDNav.class, Map.class}, 
				ap, vn, params);
	}

	@Override
	public IFunction createUpdateFunction(Class<?> funClass,
			Map<String, Object> params) {
		IFunction fun = null;
		try {
			fun = (IFunction) ObjectUtil.newInstance(funClass, 
					new Class<?>[]{AutoPilot.class, VTDNav.class, XMLModifier.class, Map.class}, 
					ap, vn, getXMLModifier(), params);
		} catch (ModifyException e) {
			throw new RuntimeException(e);
		}
		return fun;
	}

	@Override
	public void insertBefore(String select, Element node) {
		insertBefore(select, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void insertAfter(String select, Element node) {
		insertAfter(select, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void insertAfterType(String parentXPath, String[] types, Element node) {
		insertAfterType(parentXPath, types, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void insertAfterType(String parentXPath, String[] types, List<Element> nodes) {
		for (Element node : nodes) {
			insertAfterType(parentXPath, types, DOM4JNodeHelper.asXML(node));
		}
	}

	@Override
	public void insertAsFirst(String select, Element node) {
		insertAsFirst(select, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void insertAsLast(String select, Element node) {
		insertAsLast(select, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void insert(String select, Element node) {
		insert(select, DOM4JNodeHelper.asXML(node));
	}

	@Override
	public void replaceNode(String select, Element node) {
		replaceNode(select, DOM4JNodeHelper.asXML(node));
	}
}
