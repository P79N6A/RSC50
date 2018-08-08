/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.found.file.xml;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.Visitor;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-2
 */
public class DOM4JNodeHelper {
	
	/**
	 * 按指定节点名创建xml节点
	 * @param name
	 * @return
	 */
	public static Element createSCLNode(String name) {
		Document doc = DocumentHelper.createDocument();
		QName qn = Constants.XQUERY 
				? new QName(name, new Namespace("", Constants.uri)) 
				: new QName(name);
		return doc.addElement(qn);
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
	 * 获取指定节点属性
	 * @param node
	 * @param property
	 * @return
	 */
	public static String getAttribute(Element node, String property) {
		Attribute att = node.attribute(property);
		return (null == att) ? "" : att.getValue();
	}

	public static int getAttributeInt(Element node, String property) {
		String v = getAttribute(node, property);
		return "".equals(v) ? 0 : Integer.parseInt(v);
	}

	public static int getAttributeInt16(Element node, String property) {
		String v = getAttribute(node, property);
		return "".equals(v) ? 0 : Integer.parseInt(v, 16);
	}

	public static float getAttributeFloat(Element node, String property) {
		String v = getAttribute(node, property);
		return "".equals(v) ? 0 : Float.parseFloat(v);
	}

	public static boolean getAttributeBoolean(Element node, String property) {
		String v = getAttribute(node, property);
		return "True".equals(v);
	}
	
	/**
	 * 清除节点指定属性
	 * 
	 * @param nd
	 * @param atts
	 */
	public static void clearAttributes(Element nd, String... atts) {
		for (String att : atts) {
			Attribute attr = nd.attribute(att);
			if (attr != null)
				nd.remove(attr);
		}
	}

	/**
	 * 复制属性
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyAttributes(Element source, Element target) {
		List<?> atts = source.attributes();
		for (Object obj : atts) {
			Attribute att = (Attribute) obj;
			target.addAttribute(att.getName(), att.getValue());
		}
	}
	
	/**
	 * 复制属性
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyAttributes(Element source, Object target) {
		List<?> atts = source.attributes();
		for (Object obj : atts) {
			Attribute att = (Attribute) obj;
			ObjectUtil.setProperty(target, att.getName(), att.getValue());
		}
	}

	/**
	 * 按xpath获取单一属性值
	 * 
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static String getAttributeValue(Element root, String xpath) {
		Node att = getSingleNode(root, xpath);
		if (null != att)
			return ((Attribute) att).getValue();
		return null;
	}

	/**
	 * 按xpath获取所有属性值
	 * 
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static List<String> getAttributeValues(Element root, String xpath) {
		synchronized (root) {
			boolean hasNs = hasNamespace(root);
			xpath = clearNamespace(root, xpath);
			List<?> atts = root.selectNodes(xpath);
			if (hasNs)
				fixNamespace(root);
			List<String> values = new ArrayList<String>();
			for(Object obj : atts) {
				Node att = (Node) obj;
				String value = ((Attribute)att).getValue();
				if (value != null)
					values.add(value);
			}
			return values;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Attribute> getAttributes(Element root, String xpath) {
		synchronized (root) {
			boolean hasNs = hasNamespace(root);
			xpath = clearNamespace(root, xpath);
			List<Attribute> atts = root.selectNodes(xpath);
			if (hasNs)
				fixNamespace(root);
			return atts;
		}
	}

	/**
	 * 创建一个RTU xml节点
	 * 
	 * @param name
	 * @return
	 */
	public static Element createRTUNode(String name) {
		Document doc = DocumentHelper.createDocument();
		QName qn = new QName(name);
		Element ele = doc.addElement(qn);
		return ele;
	}
//	
//	/**
//	 * 按指定节点名创建xml节点,存在命名空间
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public static Element createSCLNamespaceNode(String name) {
//		Document doc = DocumentHelper.createDocument();
//		QName qn = new QName(name, new Namespace("", Constants.uri));
//		return doc.addElement(qn);
//	}

	/**
	 * 将字符串转成xml节点
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static Element parseText2Node(String xmlStr) {
		Element node = null;
		try {
			Document document = DocumentHelper.parseText(xmlStr);
			node = document.getRootElement();
		} catch (DocumentException e) {
			SCTLogger.error("", e);
		}
		return node;
	}
	
	/**
	 * 将节点转成紧凑结构的字符串
	 * 
	 * @param node
	 * @return
	 */
	public static String node2String(Node node) {
		String xmlStr = null;
		XMLWriter writer = null;
		try {
			StringWriter sw = new StringWriter(); 
			OutputFormat format = OutputFormat.createCompactFormat();
			writer = new XMLWriter(format);
			writer.setWriter(sw); 
			writer.write(node);
			xmlStr = sw.toString();
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("", e);
		} catch (IOException e) {
			SCTLogger.error("", e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				SCTLogger.error("", e);
			}
		}
		return xmlStr;
	}

	/**
	 * 将节点转成树形结构的字符串
	 * 
	 * @param node
	 * @return
	 */
	public static String node2StringWithFormat(Node node) {
		String xmlStr = null;
		XMLWriter writer = null;
		try {
			StringWriter sw = new StringWriter(); 
			OutputFormat format = OutputFormat.createPrettyPrint();
			writer = new XMLWriter(format);
			writer.setWriter(sw); 
			writer.write(node);
			xmlStr = sw.toString();
		} catch (UnsupportedEncodingException e) {
			SCTLogger.error("", e);
		} catch (IOException e) {
			SCTLogger.error("", e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				SCTLogger.error("", e);
			}
		}
		return xmlStr;
	}
	
	/**
	 * 查询属性值(字符串)
	 * 
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static String getAttributeByXPath(Element root, String xpath) {
		synchronized (root) {
			boolean hasNs = hasNamespace(root);
			xpath = clearNamespace(root, xpath);
			Node att = root.selectSingleNode(xpath);
			if (hasNs)
				fixNamespace(root);
			if (null != att)
				return ((Attribute) att).getValue();
		}
		return null;
	}
	
	/**
	 * 查询指定节点值(字符串)
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static String getNodeValueByXPath(Element root, String xpath) {
		Element node = selectSingleNode(root, xpath);
		if (null != node)
			return node.getStringValue();
		return null;
	}
	
	/**
	 * 查询指定节点值(字符串集合)
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static List<String> getNodeValuesByXPath(Element root, String xpath) {
		List<Element> nodes = selectNodes(root, xpath);
		List<String> values = new ArrayList<String>();
		for (Element nd : nodes) {
			values.add(nd.getTextTrim());
		}
		return values;
	}
	
	private static Node getSingleNode(Element root, String xpath) {
		synchronized (root) {
			boolean hasNs = hasNamespace(root);
			xpath = clearNamespace(root, xpath);
			Node node = root.selectSingleNode(xpath);
			if (hasNs)
				fixNamespace(root);
			return node;
		}
	}
	
	/**
	 * 按xpath查询，返回第一个目标节点。
	 * @param root 被查节点
	 * @param xpath
	 * @return
	 */
	public static Element selectSingleNode(Element root, String xpath) {
		Node result = getSingleNode(root, xpath);
		return (null == result) ? null : (Element)result;
	}
	
	/**
	 * 判断节点是否存在。
	 * @param root
	 * @param xpath
	 * @return
	 */
	public static boolean existsNode(Element root, String xpath){
	    return selectSingleNode(root, xpath) != null;
	}
	
	/**
	 * 按xpath查询，返回所有目标节点。
	 * 
	 * @param root
	 * @param xpath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> selectNodes(Element root, String xpath) {
		synchronized (root) {
			boolean hasNs = hasNamespace(root);
			xpath = clearNamespace(root, xpath);
			List<Element> result = new ArrayList<>();
			List<Element> nds = root.selectNodes(xpath);
			if (nds != null && nds.size() > 0)
				result.addAll(nds);
			if (hasNs)
				fixNamespace(root);
			return result;
		}
	}
	
	public static boolean hasNamespace(Element root) {
		Namespace ns = root.getNamespaceForPrefix("");
		return (null != ns && !"".equals(ns.getURI()));
	}
	
	private static void fixNamespace(Element root) {
		Namespace ns = new Namespace("", Constants.uri);
		root.add(ns);
		root.setQName(QName.get(root.getName(), ns));
		
	}
	
	/**
	 * 必须和clearNamespace()成对调用，避免引入多余的Namespace
	 * @param root
	 * @param xpath
	 * @return
	 */
	private static String clearNamespace(Element root, String xpath) {
		Namespace ns = root.getNamespaceForPrefix("");
		if (null != ns) {
			root.remove(ns);
			if (hasNamespace(root))
				root.accept(new DefaultNSClearVisitor());
		}
		if (xpath.contains(Constants.prefix + ":")) {
			xpath = clearPrefix(xpath);
		}
		return xpath;
	}
	
	/**
	 * 给xpath添加前缀
	 * @param xpath
	 * @return
	 */
	public static String appendPrefix(String xpath) {
		String newXpath = "";
		String prefix = Constants.prefix + ":";
		int p = xpath.indexOf('/');
		if (p > -1) {
			newXpath = xpath.substring(0, p);
			while (p > -1) {
				xpath = xpath.substring(p + 1);
				if (!xpath.startsWith(prefix) && !xpath.startsWith("*") && !xpath.startsWith("@")) {
					xpath = prefix + xpath;
				}
				p = xpath.indexOf('/');
				if (p > -1) {
					String part = xpath.substring(0, p);
					if (prefix.equals(part))	// 避免双斜杠查询错误
						part = "";
					newXpath += "/" + part;
				} else {
					newXpath += "/" + xpath;
				}
			}
		} else {
			newXpath = xpath;
			if (!newXpath.startsWith(prefix)) {
				newXpath = prefix + newXpath;
			}
		}
		return newXpath;
	}
	
	/**
	 * 将dom4j节点转成w3c document。
	 * @param root
	 * @return
	 * @throws DocumentException
	 */
	public static org.w3c.dom.Document asDOMDocument(Element root) throws DocumentException {
		Document doc = DocumentHelper.createDocument(root);
		DOMWriter writer = new DOMWriter();
		return writer.write(doc);
	}
	
	/**
	 * 清除xpath中的scl:
	 * @param xpath
	 * @return
	 */
	public static String clearPrefix(String xpath) {
		return xpath.replaceAll("scl:", "");
	}
	
	/**
	 * 清除节点默认namespace
	 * @param element
	 */
	public static void clearDefaultNS(Element element) {
		element.accept(new NamespaceAdjustmentVisitor(null));
	}
	
	/**
	 * 检查xml文件格式是否正确
	 * @param file
	 * @return
	 */
	public static String checkStyle(String file) {
		String msg = null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(false);
		try {
			SAXParser parser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler();
			parser.parse(new File(file), handler);
		} catch (SAXParseException exception) {
			msg = "行" + exception.getLineNumber() + "，列" + exception.getColumnNumber() + "："
					+ exception.getMessage();
		} catch (ParserConfigurationException e) {
			msg = e.getMessage();
		} catch (SAXException e) {
			msg = e.getMessage();
		} catch (IOException e) {
			msg = e.getMessage();
		}
		return msg;
	}
	
	/**
	 * 获取指定SCL文件根结点
	 * @param sclFile
	 * @return
	 */
	public static Element getImportElement(String sclFile) {
		Document doc = XMLFileManager.createDocument(new File(sclFile));
		Element root = doc.getRootElement();
		Namespace oldNs = root.getNamespace();
		Namespace newNs = Namespace.get(Constants.uri);
		Visitor visitor = new NamesapceChangingVisitor(oldNs, newNs);
		doc.accept(visitor);
		return root;
	}
	
	/**
	 * 向目标节点添加子节点
	 * @param parent
	 * @param children
	 */
	public static void addChildren(Element parent, List<Element> children) {
		for(Element child : children) {
			parent.add(child.detach());
		}
	}
	
	/**
	 * 添加文本属性节点
	 * @param node
	 * @param name
	 * @param value
	 */
	public static void addText(Element node, String name, String value) {
		Element typeEl = node.addElement(name);
		typeEl.addText(StringUtil.nullToEmpty(value));
	}
	
	/**
	 * 获取文本属性节点值
	 * @param node
	 * @param name
	 * @return
	 */
	public static String getText(Element node, String name) {
		return node.selectSingleNode("./" + name).getText();
	}

	/**
	 * 从Java对象获取XML节点属性
	 * @param node
	 * @param obj
	 * @param atts
	 */
	public static void addAttributes(Element nd, Object obj) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
				String name = p.getName();
				Object value = ObjectUtil.getProperty(obj, name);
				if (value instanceof String) {
					nd.addAttribute(name, (String) value);
				}
			}
		} catch (IntrospectionException e) {
			SCTLogger.error("", e);
		} 
	}

	/**
	 * 从Java对象获取XML节点属性
	 * （注：IEDConfigTool单独使用）
	 * @param node
	 * @param obj
	 * @param atts
	 */
	public static void addAttributes2(Element nd, Object obj) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			for (PropertyDescriptor p : beanInfo.getPropertyDescriptors()) {
				String name = p.getName();
				Object value = ObjectUtil.getProperty(obj, name);
				//值为空时不创建
				if (value instanceof String && !"".equals(value)) {
					nd.addAttribute(name, (String) value);
				}
			}
		} catch (IntrospectionException e) {
			SCTLogger.error("", e);
		} 
	}
	
	/**
	 * 从Java对象获取XML节点指定属性
	 * @param nd
	 * @param obj
	 * @param attNames
	 */
	public static void addAttributes(Element nd, Object obj, String...attNames) {
		for (String attName : attNames) {
			Object value = ObjectUtil.getProperty(obj, attName);
			if (value instanceof String) {
				nd.addAttribute(attName, (String) value);
			} else {
				nd.addAttribute(attName, ""+value);
			}
		}
	}
	
	/**
	 * 从XML节点赋值。
	 * @param obj
	 * @param nd
	 */
	public static void assignAttValues(Object obj, Element nd) {
		List<?> atts = nd.attributes();
		for (Object obj1 : atts) {
			Attribute att = (Attribute) obj1;
			String attName = att.getName();
			String value = att.getValue();
			if (value != null && ObjectUtil.existProperty(obj, attName))
				ObjectUtil.setProperty(obj, attName, value);
		}
	}
	
	public static void addAttribute(Element addrEl, String name, String value) {
		if (!StringUtil.isEmpty(value))
			addrEl.addAttribute(name, value);
	}
	
	
	/**
	 * 获取配置文件根元素。
	 * @param configPath
	 * @return
	 */
	public static Element getRootElement(String configPath) {
		Document doc = XMLFileManager.loadXMLFile(configPath);
		return doc.getRootElement();
	}
	
	/**
	 * 获取配置文件根元素。
	 * @param configPath
	 * @return
	 */
	public static Element getRootElement(Class<?> clazz, String configPath) {
		Document doc = XMLFileManager.loadXMLFile(clazz, configPath);
		return doc.getRootElement();
	}
	
	/**
	 * 得到元素去掉命名空间并增加打印格式的xml字符串。
	 * @param ele
	 * @return
	 */
	public static String asXML(Element ele) {
		StringBuilder txt = new StringBuilder(512);
		handle(ele, txt);
		return txt.toString();
	}
	
	private static boolean hasNs(Namespace ns) {
		return ns != null && !StringUtil.isEmpty(ns.getPrefix());
	}
	
	@SuppressWarnings("unchecked")
	private static void handle(Element parent, StringBuilder txt) {
		String nodeName = parent.getName();
		Namespace ns = parent.getNamespace();
		if (hasNs(ns)) {
			nodeName = ns.getPrefix() + ":" + nodeName;
		}
		String tabs = getTabs(parent);
		txt.append(tabs);
		txt.append("<" + nodeName);
		// 处理命名空间
		List<Namespace> nsList = parent.additionalNamespaces();
		for (Namespace ns1 : nsList) {
			txt.append(" xmlns:" + ns1.getPrefix()).append("=\"").append(ns1.getURI()).append("\"");
		}
		// 处理属性，属性中空格需保留
		List<Attribute> atts = parent.attributes();
		for (Attribute att : atts) {
			String attName = att.getName();
			ns = att.getNamespace();
			if (hasNs(ns)) {
				attName = ns.getPrefix() + ":" + attName;
			}
			txt.append(" " + attName).append("=\"").append(StringUtil.toXMLChars(att.getValue())).append("\"");
		}
		// 处理子节点
		String value = StringUtil.toXMLChars(parent.getTextTrim());
		List<Element> children = parent.elements();
		if (StringUtil.isEmpty(value) && children.size() < 1) {
			txt.append("/>\n");
		} else {
			txt.append(">");
			if (!StringUtil.isEmpty(value)) {
				txt.append(value);
			} else if (children.size() > 0) {
				txt.append("\n");
				for (Element child : children) {
					handle(child, txt);
				}
			}
			if (StringUtil.isEmpty(value))
				txt.append(tabs);
			txt.append("</" + nodeName + ">\n");
		}
	}
	
	private static String getTabs(Element ele) {
		String tabs = "";
		Element parent = ele.getParent();
		while(parent != null) {
			tabs += "\t";
			parent = parent.getParent();
		}
		return tabs;
	}
	
	public static String getNodesContent(List<Element> eles) {
		String content = "";
		for (Element ele : eles) {
			content += asXML(ele);
		}
		return content;
	}
	
	public static void main(String[] args) {
		String xml = "<PowerTransformer xmlns='http://www.iec.ch/61850/2003/SCL' xmlns:ext='http://www.shrcn.com' name='2卷轴变压器1(复制)1' type='PTR'>" +
			 "<ext:test name='变压器绕组相2' type='PTW'/>" +
			 "<LNode iedName='None' ldInst='' lnClass='YPTR' lnType='null' lnInst='2' prefix=''/>" +
			 "<LNode iedName='None' ldInst='' lnClass='YEFN' lnType='null' lnInst='2' prefix=''/>" +
			 "<LNode iedName='None' ldInst='' lnClass='YLTC' lnType='null' lnInst='2' prefix=''/>" +
			 "<LNode iedName='None' ldInst='' lnClass='YPSN' lnType='null' lnInst='2' prefix=''/>" +
			 "<TransformerWinding name='变压器绕组相1' type='PTW' ext:aa='111'>" +
			  "<Terminal name='Term1' connectivityNode='变电站/电压等级1/间隔1/null' substationName='变电站' voltageLevelName='电压等级1' bayName='间隔1' cNodeName='null'/>" +
			 "</TransformerWinding>" +
			 "<TransformerWinding name='变压器绕组相2' type='PTW'>" +
			  "<Terminal name='Term1' connectivityNode='变电站/电压等级1/间隔1/null' substationName='变电站' voltageLevelName='电压等级1' bayName='间隔1' cNodeName='null'/>" +
			 "</TransformerWinding>" +
			"</PowerTransformer>";
		Node nd = parseText2Node(xml);
//		clearNS2((Element) nd);
//		printNS((Element) nd);
//		clearDefaultNS((Element) nd);
//		printNS((Element) nd);
//		System.out.println(node2StringWithFormat(nd));
		
		System.out.println(asXML((Element) nd));
	}

	@SuppressWarnings("unchecked")
	public static void  insertBefore(Element root,String select, Element srcEle) {
		Node temp = getSingleNode(root, select);
		if (temp == null)
			return;
		Element parent = temp.getParent();
		List list = parent.elements();
		int index = list.indexOf(temp);
		if (srcEle == null)
			return;
		list.add(index, srcEle.detach());
	}

	@SuppressWarnings("unchecked")
	public static void insertAfter(Element root, String select, Element srcEle) {
		Node temp = getSingleNode(root, select);
		if (temp == null || srcEle == null)
			return;
		Element parent = temp.getParent();
		List list = parent.elements();
		int index = list.indexOf(temp) + 1;
		if (index < list.size())
			list.add(index, srcEle.detach());
		else
			list.add(srcEle.detach());
	}

	@SuppressWarnings("unchecked")
	public static void insertAsFirst(Element root, String select, Element srcEle) {
		Element temp = selectSingleNode(root, select);
		if (temp == null || srcEle == null)
			return;
		List list = temp.elements();
		if (list.size() > 0)
			list.add(0, srcEle.detach());
		else
			list.add(srcEle.detach());
	}

	public static void insertAsLast(Element root, String select, Element srcEle) {
		Element temp = selectSingleNode(root, select);
		if (temp == null || srcEle == null)
			return;
		temp.add(srcEle.detach());
	}

	public static void replaceNode(Element root, String select, Element srcEle) {
		Node temp = getSingleNode(root, select);
		if (temp == null || srcEle == null)
			return;
		List<Element> parent = temp.getParent().elements();
		parent.set(parent.indexOf(temp), (Element) srcEle.detach()); 
	}

	public static void deleteNodes(Element root, String select) {
		List<Element> nds = selectNodes(root, select);
		if (nds==null || nds.size()<1)
			return;
		for (Element nd : nds) {
			nd.getParent().remove(nd);
		}
	}

	public static void saveAttribute(Element root, String select,
			String attName, String value) {
		List<Element> temp = selectNodes(root, select);
		if (temp == null)
			return;
		for (Element el : temp) {
			el.addAttribute(attName, value);
		}
	}

	public static String getAttribute(Element root, String select,
			String attName) {
		Element temp = selectSingleNode(root, select);
		return (temp==null) ? null : temp.attributeValue(attName, "");
	}

	@SuppressWarnings("unchecked")
	public static List<String> getAttributes(Element root, String select,
			String attName) {
		List<String> rels = new ArrayList<String>();
		List<Element> nds = selectNodes(root, select);
		for (Element nd : nds) {
			rels.add(nd.attributeValue(attName, ""));
		}
		return rels;
	}

	public static String getNodeValue(Element root, String select) {
		Element temp = selectSingleNode(root, select);
		if (temp == null)
			return "";
		return temp.getText();
	}

	public static void update(Element root, String select, String value) {
		Element temp = selectSingleNode(root, select);
		if (temp == null)
			return;
		temp.setText(value);
	}

	@SuppressWarnings("unchecked")
	public static List<Element> selectNodesOnlyAtts(Element root,
			String xpath, String ndName) {
		synchronized (root) {
			List<Element> nds = selectNodes(root, xpath);
			List<Element> list = new ArrayList<Element>();
			for (Element nd : nds) {
				Element newNd = nd.createCopy();
				newNd.clearContent();
				newNd.setName(ndName);
				list.add(newNd);
			}
			return list;
		}
	}

	public static int countNodes(Element root, String select) {
		List<Element> sels = selectNodes(root, select);
		if (sels == null) {
			return 0;
		} else {
			return sels.size();
		}
	}
}
