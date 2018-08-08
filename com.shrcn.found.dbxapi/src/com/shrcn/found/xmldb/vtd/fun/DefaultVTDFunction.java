/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.xmldb.vtd.fun;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-8
 */
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.IFunction;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public abstract class DefaultVTDFunction implements IFunction {

	protected static final String charset = Constants.CHARSET_UTF8;
	
	protected AutoPilot ap;
	protected VTDNav vn;
	protected Map<String, Object> params;

	public DefaultVTDFunction(AutoPilot ap, VTDNav vn, Map<String, Object> params) {
		this.ap = ap;
		this.vn = vn;
		this.params = params;
	}

	/**
	 * 根据名称返回属性值
	 * @param attName
	 * @return
	 * @throws NavException
	 */
	protected String getAttVal(String attName) throws NavException {
		int attrIdx = vn.getAttrVal(attName);
		return attrIdx>-1 ? vn.toString(attrIdx) : "";
	}
	
	/**
	 * 执行查询
	 * @param select
	 * @throws XPathParseException
	 */
	protected void selectXPath(String select) throws XPathParseException {
		ap.selectXPath(select);
	}

	/**
	 * 查询单个节点
	 * @param select
	 * @return
	 */
	protected synchronized String selectSingleNode(String select) {
		String strResult = "";
		try {
			selectXPath(select);
			byte[] ba = vn.getXML().getBytes();
			if (ap.evalXPath() != -1) {
				long l = vn.getElementFragment();
	            int offset = (int)l;
	            int len = (int)(l >> 32);
	            byte[] ndData = new byte[len];
	            System.arraycopy(ba, offset, ndData, 0, len);
	            strResult += new String(ndData, charset);
			}
			ap.resetXPath();
		} catch (XPathParseException | UnsupportedEncodingException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return strResult;
	}
	
	/**
	 * 查询多个节点
	 * @param select
	 * @return
	 */
	protected synchronized List<String> selectNodes(String select) {
		List<String> result = new ArrayList<String>();
		try {
			selectXPath(select);
			byte[] ba = vn.getXML().getBytes();
			while ((ap.evalXPath()) != -1) {
				long l = vn.getElementFragment();
	            int offset = (int)l;
	            int len = (int)(l >> 32);
	            byte[] ndData = new byte[len];
	            System.arraycopy(ba, offset, ndData, 0, len);
	            result.add(new String(ndData, charset));
			}
			ap.resetXPath();
		} catch (XPathParseException | UnsupportedEncodingException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	protected synchronized List<String> selectNodesOnlyAtts(String select, String ndName) {
		List<String> result = new ArrayList<String>();
		try {
			selectXPath(select);
			int i = ap.evalXPath();
			while (i != -1) {
				String strResult = "<" + ndName;
				for (int j=0; j<vn.getAttrCount()*2; j+=2) {
					String attName = vn.toString(i + j + 1);
					strResult += " " + attName + "=\"" + vn.toString(vn.getAttrVal(attName)) + "\"";
				}
//				strResult += "/>";
				result.add(strResult);
				i = ap.evalXPath();
			}
			ap.resetXPath();
		} catch (XPathParseException | XPathEvalException | NavException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 清除命名空间
	 * @param content
	 * @param nsList
	 * @return
	 */
	protected String clearNamespaces(String content, Map<String, Namespace> nsMap) {
		Element iedNode = DOM4JNodeHelper.parseText2Node(content);
		iedNode.accept(new NSClearVisitor(nsMap));
		return iedNode.asXML();
	}
	
	/**
	 * 得到元素xml字符串
	 * @param ele
	 * @return
	 */
	protected String asXML(Element ele, Map<String, Namespace> nsMap) {
		StringBuilder txt = new StringBuilder(512);
		handle(ele, txt, nsMap);
		return txt.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void handle(Element parent, StringBuilder txt, Map<String, Namespace> nsMap) {
		String nodeName = parent.getName();
		Namespace ns = parent.getNamespace();
		String prefix = ns.getPrefix();
		if (ns != null && !StringUtil.isEmpty(prefix)) {
			nodeName = prefix + ":" + nodeName;
			if (!nsMap.containsKey(prefix))
				nsMap.put(prefix, ns);
		}
		txt.append("\t\t" + "<" + nodeName);
		// 按字母序处理属性，属性中空格需保留
		List<Attribute> atts = parent.attributes();
		for (Attribute att : atts) {
			String attName = att.getName();
			ns = att.getNamespace();
			prefix = ns.getPrefix();
			if (ns != null && !StringUtil.isEmpty(prefix)) {
				attName = prefix + ":" + attName;
				if (!nsMap.containsKey(prefix))
					nsMap.put(prefix, ns);
			}
			txt.append(" " + attName).append("=\"").append(att.getValue()).append("\"");
		}
		// 处理子节点
		String value = parent.getTextTrim();
		List<Element> children = parent.elements();
		if (StringUtil.isEmpty(value) && children.size() < 1) {
			txt.append("/>\r\n");
		} else {
			txt.append(">");
			if (!StringUtil.isEmpty(value)) {
				txt.append(value);
			} else if (children.size() > 0) {
				txt.append("\r\n");
				for (Element child : children) {
					txt.append("\t");
					handle(child, txt, nsMap);
				}
			}
			if (StringUtil.isEmpty(value))
				txt.append("\t\t");
			txt.append("</" + nodeName + ">\r\n");
		}
	}
	
	protected String getSubString(VTDNav vn, String attName) throws XPathParseException {
		AutoPilot ap = createAP(vn, attName);
		return ap.evalXPathToString();
	}
	
	protected double getSubNumber(VTDNav vn, String attName) throws XPathParseException {
		AutoPilot ap = createAP(vn, attName);
		return ap.evalXPathToNumber();
	}
	
	protected int getSubInt(VTDNav vn, String attName) throws XPathParseException {
		return (int) getSubNumber(vn, attName);
	}

	protected AutoPilot createAP(VTDNav vn, String attName)
			throws XPathParseException {
		AutoPilot ap = new AutoPilot();
		ap.bind(vn);
		ap.selectXPath(attName);
		return ap;
	}
}

class FindNamespaceHandler extends DefaultHandler {
	
	private Map<String, Namespace> nsMap;
	
	public FindNamespaceHandler(Map<String, Namespace> nsMap) {
		this.nsMap = nsMap;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		for (int i=0; i<atts.getLength(); i++) {
			String attName = atts.getQName(i);
			String attValue = atts.getValue(i);
			if (attName.startsWith("xmlns")) {
				int p = attName.indexOf(':');
				if (p > 0) {
					String prefix = attName.substring(p + 1);
					if (!nsMap.containsKey(prefix))
						nsMap.put(prefix, new Namespace(prefix, attValue));
				}
			}
		}
	}
}

class NSClearVisitor extends VisitorSupport {

	private Map<String, Namespace> nsMap;
	
	public NSClearVisitor(Map<String, Namespace> nsMap) {
		this.nsMap = nsMap;
	}

	public void visit(Element node) {
		Namespace ns = node.getNamespace();
		if ("".equals(ns.getPrefix()) && !StringUtil.isEmpty(ns.getURI())) {
			Namespace namespace = null;
			node.setQName(QName.get(node.getName(), namespace));
		}
		for (Object o : node.additionalNamespaces()) {
			Namespace nstemp = (Namespace) o;
			String prefix = nstemp.getPrefix();
			String uri = nstemp.getURI();
			if (!nsMap.containsKey(prefix)) {
				nsMap.put(prefix, new Namespace(prefix, uri));
			}
			node.remove(nstemp);
		}
	}
}
