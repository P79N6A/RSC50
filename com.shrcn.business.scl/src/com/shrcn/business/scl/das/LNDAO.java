/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.SCLModel;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-20
 */
/*
 * 修改历史
 * Revision 1.18.4.1  2009/07/24 07:10:41  cchun
 * Update:切换数据库Sedna
 *
 * Revision 1.18  2009/06/02 08:31:12  lj6061
 * LN对应的级联删除
 *
 * Revision 1.17  2009/05/21 05:37:27  hqh
 * 扩大方法访问
 *
 * Revision 1.16  2009/05/19 09:24:15  cchun
 * Update:修改LN查询，使其更严谨
 *
 * Revision 1.15  2009/05/18 12:30:49  cchun
 * Update:优化LN查询效率
 *
 * Revision 1.14  2009/05/06 12:08:59  lj6061
 * Ln去除删除功能，改用下标修改
 *
 * Revision 1.13  2009/04/29 02:23:34  cchun
 * 统一数据操作对象接口
 *
 * Revision 1.12  2009/04/28 07:50:52  lj6061
 * 添加GSE功能
 *
 * Revision 1.11  2009/04/27 09:45:57  lj6061
 * 添加GSE控制页面
 *
 * Revision 1.10  2009/04/27 03:17:59  lj6061
 * update:创建Name=pos的节点
 *
 * Revision 1.9  2009/04/23 11:51:01  lj6061
 * 添加LN的删除和刷新
 *
 * Revision 1.8  2009/04/22 08:17:08  cchun
 * Update:修改ReportControl按LN过滤
 *
 * Revision 1.7  2009/04/22 01:27:21  lj6061
 * 重构并添加ControlDAO
 *
 * Revision 1.6  2009/04/21 08:04:13  cchun
 * Update:去掉缓存
 *
 * Revision 1.5  2009/04/21 06:18:35  lj6061
 * 修改表记录分离到该类
 *
 * Revision 1.4  2009/04/21 06:02:49  lj6061
 * Update:添加get,set
 *
 * Revision 1.3  2009/04/21 05:57:03  lj6061
 * Update:为LNDAO添加属性
 *
 * Revision 1.2  2009/04/20 09:29:06  cchun
 * Update:修改getLNNodes()访问权限
 *
 * Revision 1.1  2009/04/20 09:03:34  cchun
 * Update:添加LNDAO，ReportControlDAO
 *
 */
public class LNDAO extends IEDEditDAO {

	private Map<String, String[]> lnMap = new HashMap<String, String[]>();
	private List<Element> data = null;
	private String[] names = null;

	public LNDAO(String iedName, String ldInst) {
		super(iedName, ldInst);
		reLoad();
	}

	public void reLoad() {
		// 加载LN
		this.data = queryLNs();
		// 初始化LN names
		List<String> lNames = new ArrayList<String>();
		if (data == null)
			return;
		for (Element ln : data) {
			String lnName = SCLModel.getLnName(ln);
			String prefix = ln.attributeValue("prefix");
			String lnClass = ln.attributeValue("lnClass");
			String inst = ln.attributeValue("inst");
			lNames.add(lnName);
			lnMap.put(lnName, new String[] { prefix, lnClass, inst });
		}
		this.names = lNames.toArray(new String[lNames.size()]);
	}

	/**
	 * 获取当前AP,LD对应的LN name
	 * 
	 * @param iedXPath
	 * @return LD inst值数组
	 */
	public String[] getLNNames() {
		return this.names;
	}

	/**
	 * 获取当前AP,LD对应的LN
	 * 
	 * @param iedXpath
	 * @return LN Element List
	 */
	public List<Element> getLNNodes() {
		return this.data;
	}
	
	/**
	 * 获取指定LN下所有DOI信息
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @return
	 */
	public static List<Element> getLnDois(String iedName, String ldInst, 
			String prefix, String lnClass, String lnInst) {
		String lnXpath = SCL.getIEDXPath(iedName) + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
				"']" + SCL.getLNXPath(prefix, lnClass, lnInst);
		Element ln = XMLDBHelper.selectSingleNode(lnXpath);
		List<?> doiEls =  ln.elements("DOI");
		String lnXPath = SCL.getIEDXPath(iedName) + "/scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0";
		List<Element> dois = new ArrayList<Element>();
		for (Object obj : doiEls) {
			Element doi = (Element) obj;
			String doName = doi.attributeValue("name");
			String datNameXpath = lnXPath + "/scl:DataSet[count(./*[@ldInst='" + ldInst + "']" + SCL.getLNAtts(prefix, lnClass, lnInst) + "[@doName='" + doName + "'])>0]/@name";
			String datName = XMLDBHelper.getAttributeValue(datNameXpath);
			if (!StringUtil.isEmpty(datName)) {
				doi.addAttribute("datSet", datName);
			}
			dois.add(doi);
		}
		return dois;
	}
	
	/**
	 * 根据LN name获取LN信息
	 * @param lnName LN名称(prefix + lnClass + inst)
	 * @return LN信息节点
	 */
	public Element getLNByName(String lnName) {
		String[] constraint = lnMap.get(lnName);
		String xpath = getLdXpath() + 
			SCL.getLNXPath(constraint[0], constraint[1], constraint[2]);
		return XMLDBHelper.selectSingleNode(xpath);
	}

	/**
	 * 查询ln信息
	 * @return
	 */
	private List<Element> queryLNs() {
		return XMLDBHelper.selectNodes(getLnXpath());
	}

	/**
	 * 获得LD的路径
	 * @return
	 */
	public String getLdXpath() {
		return iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']";
	}
	
	/**
	 * 获取ln路径
	 * @return
	 */
	public String getLnXpath() {
		return getLdXpath() + "/*[name()='LN0' or name()='LN']";
	}
	
	public String getLnRef(int row) {
		return iedName + ldInst + "/" + SCL.getLnName(data.get(row-1));
	}

	/**
	 * 获得唯一确定的LN
	 */
	private String getSelect(int row) {
		return getLnXpath() + "[" + row + "]";
	}

	/**
	 * 获得当前LN在的数据集下的FCDA路径
	 */
	private String getFCDASelect(String prefix, String lnClass, String inst) {
		return getLdXpath() + "//scl:FCDA" + SCL.getLNAtts(prefix, lnClass, inst);
	}

	/**
	 * 修改指定LN的属性值
	 * @param row 行号
	 * @param attName 属性名
	 * @param value 值
	 */
	public void updateLnAtt(int row, String attName, String value) {
		XMLDBHelper.saveAttribute(getSelect(row), attName, value);
	}

	/**
	 * 删除表格数据
	 * 
	 * @param prefix
	 * @param lnClass
	 * @param inst
	 * 
	 */
	public void deleteLNList(int row) {
		XMLDBHelper.removeNodes(getSelect(row));
	}

	/**
	 * 删除LN对应FCDA, 2中方案:
	 * 1.对应的去删除库里的FCDA，
	 * 2.取出整个LD后删除FCDA，插入对应的位置
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 */
	public void deleteFCDA(String prefix, String lnClass, String lnInst) {
		String select = getFCDASelect(prefix, lnClass, lnInst);
		XMLDBHelper.removeNodes(select);
	}

	/**
	 * 删除引用LndeInput部分
	 */
	public void deleteInput(String prefix, String lnClass, String lnInst) {
		String lnName = prefix + lnClass + lnInst;
		String select = getInputSelect(prefix, lnClass, lnInst);
		List<Element> extRef = XMLDBHelper.selectNodes(select);
		for (Element element : extRef) {
			String intAddr = element.attributeValue("intAddr");
			if (intAddr.contains(lnName)) {
				XMLDBHelper.removeNodes(getLdXpath()
								+ "//scl:Inputs/scl:ExtRef[@intAddr='"
								+ intAddr + "']");
			}
		}
	}

	private String getInputSelect(String prefix, String lnClass, String lnInst) {
		String select = getLdXpath() + "//scl:Inputs/scl:ExtRef";
		return select;
	}

	public void setLdInst(String ldInst) {
		this.ldInst = ldInst;
	}

	/**
	 * 查询指定IED下按AP,LD,LN层次关系排列的所有LN信息
	 * @param iedName
	 * @return
	 */
	public static List<Element> queryAllLNodesByIEDName(String iedName) {
		if (Constants.XQUERY) {
			StringBuilder query = new StringBuilder("let $ied:=" + XMLDBHelper.getDocXPath());
			query.append("/scl:SCL/scl:IED[@name='" + iedName + "'] ");
			query.append(" return <IED name='{$ied/@name}' desc='{$ied/@desc}'>{");
			query.append(" for $ap in $ied/scl:AccessPoint ");
			query.append(" return <AccessPoint name='{$ap/@name}'>{");
			query.append(" for $ldevice in $ap/scl:Server/scl:LDevice ");
			query.append(" return <LDevice desc='{$ldevice/@desc}' inst='{$ldevice/@inst}'> {");
			query.append(" for $ln in $ldevice/*[name()='LN' or name()='LN0'] ");
			query.append(" return <LN lnClass='{$ln/@lnClass}' inst='{$ln/@inst}' prefix='{$ln/@prefix}'></LN>");
			query.append(" }</LDevice> ");
			query.append(" }</AccessPoint>");
			query.append(" }</IED>");
			return XMLDBHelper.queryNodes(query.toString());
		}
		List<Element> list = new ArrayList<Element>();
		String iedXPath = SCL.XPATH_IED + "[@name='" + iedName + "']";
		List<Element> iedEls = XMLDBHelper.selectNodesOnlyAtts(iedXPath, "IED");
		if (iedEls.size()==0)
			return list;
		Element iedCpEl = iedEls.get(0).createCopy();
		list.add(iedCpEl);
		List<Element> apEls = XMLDBHelper.selectNodes(iedXPath + "/scl:AccessPoint");
		for (Element apEl : apEls) {
			Element apCpEl = iedCpEl.addElement("AccessPoint");
			apCpEl.addAttribute("name", apEl.attributeValue("name"));
			List<Element> els = DOM4JNodeHelper.selectNodes(apEl, "./scl:Server/scl:LDevice/*[name()='LN' or name()='LN0']");
			for (Element el : els) {
				Element ldEl = el.getParent();
				String inst = ldEl.attributeValue("inst");
				String desc = ldEl.attributeValue("desc");
				Element ldCpEl = DOM4JNodeHelper.selectSingleNode(apCpEl, "./scl:LDevice[@inst='" + inst + "']");
				if (ldCpEl == null) {
					ldCpEl = apCpEl.addElement("LDevice");
					ldCpEl.addAttribute("desc", desc);
					ldCpEl.addAttribute("inst", inst);
				}
				
				Element elCp = el.createCopy();
				elCp.setName("LN");
				ldCpEl.add(elCp);
			}
		}
		return list;
	}
	
	/**
	 * 获得单个LN
	 * @param iedName
	 * @param ldInst
	 * @param lnInst
	 * @param lnClass
	 * @param lnPrefix
	 * @return
	 */
	public static List<Element> getSingleLN(String iedName, String ldInst,
			String lnInst, String lnClass, String lnPrefix) {
		if (Constants.XQUERY) {
			StringBuffer xquery = new StringBuffer("for $ln in " + XMLDBHelper.getDocXPath());
			xquery.append(SCL.getIEDXPath(iedName) + "/scl:AccessPoint");
			xquery.append("/scl:Server/scl:LDevice[@inst='" + ldInst + "']");
			xquery.append(SCL.getLNXPath(lnPrefix, lnClass, lnInst));
			xquery.append(" return $ln");
			return XMLDBHelper.queryNodes(xquery.toString());
		}
		return XMLDBHelper.selectNodes(SCL.getIEDXPath(iedName) + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']" + SCL.getLNXPath(lnPrefix, lnClass, lnInst));
	}
	
	/**
	 * 更新数据集
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param prefixNew
	 */
	public void updateFCDAPrefix(String prefix, String lnClass, String lnInst, String prefixNew) {
		String xpath = getFCDASelect(prefix, lnClass, lnInst);
		XMLDBHelper.saveAttribute(xpath, "prefix", prefixNew);
	}
	
	/**
	 * 更新一二次关联
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param prefixNew
	 */
	public void updateLNodePrefix(String prefix, String lnClass, String lnInst, String prefixNew) {
		String xpath = "/scl:SCL/scl:Substation[count(./scl:VoltageLevel//scl:LNode"
					+ "[@iedName='" + iedName +
					"'][@ldInst='" + ldInst +
					"']" + SCL.getLNAtts(prefix, lnClass, lnInst) + ")>0]";

		List<Element> subEls = XMLDBHelper.selectNodesOnlyAtts(xpath, "Sub");
		for (Element el : subEls) {
			String currIedName = el.attributeValue("name");
			xpath = "/scl:SCL/scl:Substation[@name='" + currIedName
					+ "']/scl:VoltageLevel//scl:LNode" + "[@iedName='"
					+ iedName + "'][@ldInst='" + ldInst + "']"
					+ SCL.getLNAtts(prefix, lnClass, lnInst);
			XMLDBHelper.saveAttribute(xpath, "prefix", prefixNew);
		}
	}
	
	/**
	 * 更新内外部信号关联
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param prefixNew
	 */
	public void updateInputsPrefix(String prefix, String lnClass, String lnInst, String prefixNew) {
		// 虚开入
		String xpath = "/scl:SCL/scl:IED[@name='" + iedName +
					"']/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst +
					"']/*/scl:Inputs/scl:ExtRef";
		String lnPart = ldInst + "/" + (prefix==null?"":prefix) + lnClass + lnInst + ".";
		String lnPartNew = ldInst + "/" + (prefixNew==null?"":prefixNew) + lnClass + lnInst + ".";
		List<Element> extRefs = XMLDBHelper.selectNodes(xpath);
		for (Element extRef : extRefs) {
			String intAddr = extRef.attributeValue("intAddr");
			if (StringUtil.isEmpty(intAddr))
				continue;
			if (intAddr.startsWith(lnPart)) {
				String intAddrNew = intAddr.replace(lnPart, lnPartNew);
				XMLDBHelper.saveAttribute(xpath + "[@intAddr='" + intAddr + "']", "intAddr", intAddrNew);
			}
		}
		// 虚开出
		xpath = "/scl:SCL/scl:IED[count(./scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef"
			+ SCL.getLNAtts(prefix, lnClass, lnInst) + "[@iedName='" + iedName + "'][@ldInst='" + ldInst + "'])>0]";
		List<Element> iedAttr = XMLDBHelper.selectNodesOnlyAtts(xpath, "IED");
		for (Element el : iedAttr) {
			String currIedName = el.attributeValue("name");
			xpath = "/scl:SCL/scl:IED[@name='" + currIedName + "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef"
					+ SCL.getLNAtts(prefix, lnClass, lnInst) + "[@iedName='" + iedName + "'][@ldInst='" + ldInst + "']";
			XMLDBHelper.saveAttribute(xpath, "prefix", prefixNew);
		}
	}
	
	public static Element getCbByLD(Element ldEl, Element data){
		String prefix = data.attributeValue("prefix");
		String lnClass = data.attributeValue("lnClass");
		String lnInst = data.attributeValue("lnInst");
		String doName = data.attributeValue("doName");
		String dodaXpath = "[@doName='" + doName + "']";
		String xpath = null;
		boolean haveprefix = !StringUtil.isEmpty(prefix);
		boolean havalnInst = !StringUtil.isEmpty(lnInst);
		if (haveprefix && havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[@prefix='" + prefix + "'][@lnClass='" + lnClass + "'][@lnInst='" + lnInst + "']" + dodaXpath;
		} else if (haveprefix && !havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[((@prefix='" + prefix + "'][@lnClass='" + lnClass + "']" + dodaXpath;
		} else if (!haveprefix && havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[@lnClass='" + lnClass + "'][@lnInst='" + lnInst + "']" + dodaXpath;
		} else {
			xpath = "./*/scl:DataSet/scl:FCDA[@lnClass='" + lnClass + "']" + dodaXpath;
		}
		if (ldEl == null)
			return null;
		List<Element> fcdaEls = DOM4JNodeHelper.selectNodes(ldEl, xpath);
		if (fcdaEls == null)
			return null;
		for(Element fcdaEl : fcdaEls){
			Element dsEl = fcdaEl.getParent();
			String dsName = dsEl.attributeValue("name");
			Element	cbEl = DOM4JNodeHelper.selectSingleNode(ldEl, "./scl:LN0/*[name()='GSEControl' or name()='SampledValueControl'][@datSet='" + dsName + "']");
			if (cbEl != null){
				return cbEl;
			}
		}
		return null;
	}

	public static List<String> getDsNameByLD(Element ldEl, Element data){
		String prefix = data.attributeValue("prefix");
		String lnClass = data.attributeValue("lnClass");
		String lnInst = data.attributeValue("lnInst");
		String doName = data.attributeValue("doName");
		String dodaXpath = "[@doName='" + doName + "']";
		String xpath = null;
		boolean haveprefix = !StringUtil.isEmpty(prefix);
		boolean havalnInst = !StringUtil.isEmpty(lnInst);
		if (haveprefix && havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[@prefix='" + prefix + "'][@lnClass='" + lnClass + "'][@lnInst='" + lnInst + "']" + dodaXpath;
		} else if (haveprefix && !havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[((@prefix='" + prefix + "'][@lnClass='" + lnClass + "']" + dodaXpath;
		} else if (!haveprefix && havalnInst) {
			xpath = "./*/scl:DataSet/scl:FCDA[@lnClass='" + lnClass + "'][@lnInst='" + lnInst + "']" + dodaXpath;
		} else {
			xpath = "./*/scl:DataSet/scl:FCDA[@lnClass='" + lnClass + "']" + dodaXpath;
		}
		if (ldEl == null)
			return null;
		List<Element> fcdaEls = DOM4JNodeHelper.selectNodes(ldEl, xpath);
		if (fcdaEls == null)
			return null;
		List<String> list = new ArrayList<String>();
		for(Element fcdaEl : fcdaEls){
			Element dsEl = fcdaEl.getParent();
			list.add(dsEl.attributeValue("name"));
		}
		return list;
	}
}
