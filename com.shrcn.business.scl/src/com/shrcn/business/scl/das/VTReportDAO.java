/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.model.SignalRelation;
import com.shrcn.business.scl.model.intree.InTreeDataFactory;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-11-2
 */
/**
 * $Log: VTReportDAO.java,v $
 * Revision 1.18  2013/12/03 07:38:35  cxc
 * fix bug：修复关联信息查询时没有da的情况
 *
 * Revision 1.17  2013/08/05 07:59:23  cchun
 * Fix Bug:增加对daName为空的判断
 *
 * Revision 1.16  2013/08/02 06:07:51  cchun
 * Update:扩充GGIO前缀
 *
 * Revision 1.15  2012/03/09 07:35:51  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.14  2012/01/13 08:40:01  cchun
 * Refactor:为方便数据库切换改用新接口
 *
 * Revision 1.13  2011/11/03 02:33:00  cchun
 * Update:指定精确的节点名称，避免因为icd文件中含有<Private>导致程序出错
 *
 * Revision 1.12  2011/09/23 06:24:50  cchun
 * Fix Bug:修复prefix为空找不到关联信号，以及描述查询错误
 *
 * Revision 1.11  2011/09/13 08:13:01  cchun
 * Fix Bug:修复数据集没有区分LD的bug
 *
 * Revision 1.10  2011/09/06 08:27:45  cchun
 * Update:优化输出虚端子查询XQuery
 *
 * Revision 1.9  2011/01/18 09:44:09  cchun
 * Refactor:将xQueryofOutput()从IEDConnect中移过来
 *
 * Revision 1.8  2010/09/27 07:12:04  cchun
 * Update:1、统一开入、开出信号关联对象；2、修复描述信息不准确的bug
 *
 * Revision 1.7  2010/04/19 09:09:31  cchun
 * Update:清理注释，避免空指针
 *
 * Revision 1.6  2009/12/21 06:36:24  cchun
 * Update:整理代码，纠正elements()用法错误
 *
 * Revision 1.5  2009/11/17 05:59:41  cchun
 * Update:修复xpath静态变量的错误用法
 *
 * Revision 1.4  2009/11/13 09:38:41  cchun
 * Update:增加开入端子描述
 *
 * Revision 1.3  2009/11/13 08:04:18  cchun
 * Fix Bug:1、添加t,q过滤；2、数据集查询错误；3、xquery缺少prefix属性条件的bug
 *
 * Revision 1.2  2009/11/13 07:18:11  cchun
 * Update:完善关联表格功能
 *
 * Revision 1.1  2009/11/04 02:21:13  cchun
 * Add:虚端子查看表格数据逻辑
 *
 */
public class VTReportDAO implements SCLDAO {

	public static final String desc_seperate = "^";
	public static final String SV_IN_PREFIX = "SVIN|AISVIN";
	public static final String GSE_IN_PREFIX = "GOIN|BINGOIN|NIGOIN";
	public static final String INPUT_PREFIX = SV_IN_PREFIX + "|" + GSE_IN_PREFIX;
	public static final String[] INPUT_PREFIXS = INPUT_PREFIX.split("\\|");
	public static enum SignalType {ALL, ST, MX};
	private SignalType sgType = SignalType.ALL;
	private static InTreeDataFactory inTreeFactory = null;
	private static Map<String, String> descCache = new HashMap<String, String>(); 
	
	private static Map<String, Element> doTypeCache = new HashMap<String, Element>();
	private static Map<String, Element> daTypeCache = new HashMap<String, Element>();

	private static Map<String, Element> ldCache = new HashMap<String, Element>();
	
	private static FcdaDAO fcdaDao;
	private IProgressMonitor monitor;
	
	private static Element dataTypeEl;
	
	private static VTReportDAO instance = new VTReportDAO();
	
	private VTReportDAO() {
	}

	public static VTReportDAO getInstance() {
		if (null == instance) {
			synchronized (VTReportDAO.class) {
				if (null == instance) {
					instance = new VTReportDAO();
					init();
				}
			}
		} else {
			init();
		}
		return instance;
	}
	
	private static void init() {
		descCache.clear();
		doTypeCache.clear();
		daTypeCache.clear();
		ldCache.clear();
		inTreeFactory = InTreeDataFactory.getInstance();
		inTreeFactory.setOnlyGGIO(true);
		inTreeFactory.setLoadAll(false);
		fcdaDao = FcdaDAO.getInstance();
		dataTypeEl = XMLDBHelper.selectSingleNode("/scl:SCL/scl:DataTypeTemplates");
	}
	
	/**
	 * 查询开入逻辑节点(prefix属性为SVIN,AISVIN或含有GOIN,BINGOIN,NIGOIN)
	 * @param iedName
	 * @return
	 */
	private List<Element> getInputLNs(String iedName) {
		List<Element> inputs = new ArrayList<>();
		if(Constants.XQUERY){
			String xquery = "let $root:=" + XMLDBHelper.getDocXPath() + "/scl:SCL return " + 
					"for $ld in $root/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice return " +
					"for $ln in $ld/*[name()='LN0' or name()='LN'] " +
						"where matches($ln/@prefix, '" + INPUT_PREFIX + "') return " + 
						"<LN iedName='" + iedName + "' ldInst='{$ld/@inst}' prefix='{$ln/@prefix}' lnClass='{$ln/@lnClass}' lnInst='{$ln/@inst}' lnType='{$ln/@lnType}'> " +
						"	{for $doi in $ln/scl:DOI return " +
						"		<DOI name='{$doi/@name}' doDesc='{$doi/@desc}' dU='{$doi/scl:DAI[@name='dU']/scl:Val/text()}' type='{$root/scl:DataTypeTemplates/scl:LNodeType[@id=$ln/@lnType]/scl:DO[@name=$doi/@name]/@type}'/>} " +
						"</LN>";
			return XMLDBHelper.queryNodes(xquery);
		} else {
			Map<String, Element> map = new HashMap<String, Element>();
			List<Element> lds = XMLDBHelper.selectNodes("/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice");
			for (Element ld : lds) {
				List<Element> lns = DOM4JNodeHelper.selectNodes(ld, "./*[name()='LN0' or name()='LN']");
				for (Element ln : lns) {
					String prefix = ln.attributeValue("prefix");
					if (StringUtil.isEmpty(prefix))
						continue;
					if (Arrays.asList(INPUT_PREFIXS).indexOf(prefix) > -1) {
						Document document = DocumentHelper.createDocument();
						Element ln1 = document.addElement("LN");
						ln1.addAttribute("iedName", iedName);
						ln1.addAttribute("ldInst", ld.attributeValue("inst"));
						ln1.addAttribute("prefix", ln.attributeValue("prefix"));
						ln1.addAttribute("lnClass", ln.attributeValue("lnClass"));
						ln1.addAttribute("lnInst", ln.attributeValue("inst"));
						String lnType = ln.attributeValue("lnType");
						ln1.addAttribute("lnType", lnType);
						
						Element lNodeTypeEl = map.get(lnType);
						if (lNodeTypeEl == null) {
							lNodeTypeEl = DOM4JNodeHelper.selectSingleNode(dataTypeEl, "./scl:LNodeType[@id='" + lnType + "']");
							map.put(lnType, lNodeTypeEl);
						}
						List<Element> dois = DOM4JNodeHelper.selectNodes(ln, "./scl:DOI");
						
						for(Element doi:dois){
							Element Val =  DOM4JNodeHelper.selectSingleNode(doi, "./scl:DAI[@name='dU']/scl:Val");
							String dU = (Val == null ? "" : Val.getText());
							String type = DOM4JNodeHelper.getAttributeValue(lNodeTypeEl, "./scl:DO[@name='"+ doi.attributeValue("name") +"']/@type");
							Element doi1 = ln1.addElement("DOI");
							doi1.addAttribute("name", doi.attributeValue("name"));
							doi1.addAttribute("doDesc", doi.attributeValue("desc"));
							doi1.addAttribute("dU", String.valueOf(dU));
							doi1.addAttribute("type", String.valueOf(type));
						}							
						inputs.add(ln1);
					}
				}
			}
			return inputs;
		}
	}
	
	/**
	 * 针对Struct类型DA，获取下级属性
	 * 
	 * @param daNd DA对象
	 */
	private List<DANode> getSubStructDANds(DANode daNd) {
		String daType = daNd.getDaType();
		Element struct = daTypeCache.get(daType);
		if (struct == null) {
			struct = DOM4JNodeHelper.selectSingleNode(dataTypeEl, "./scl:DAType[@id='" + daType + "']");
			daTypeCache.put(daType, struct);
		}
		List<?> das = struct.elements("BDA");
		List<DANode> children = new ArrayList<DANode>();
		for (Object obj : das) {
			Element da = (Element) obj;
			String name = da.attributeValue("name");
			String type = da.attributeValue("type");
			String bType = da.attributeValue("bType");
			children.add(new DANode(name, type, bType));
		}
		return children;
	}
	
	/**
	 * 针对SDO类型DA，获取下级属性
	 * 
	 * @param daNd DA对象
	 */
	private List<DANode> getSubSDODANds(DANode daNd) {
		String doType = daNd.getDaType();
		Element struct = doTypeCache.get(doType);
		if (struct == null) {
			struct = DOM4JNodeHelper.selectSingleNode(dataTypeEl, "./scl:DOType[@id='" + doType + "']");
			doTypeCache.put(doType, struct);
		}
		List<DANode> children = new ArrayList<DANode>();
		if (null != struct) {
			List<?> das = struct.selectNodes("./*[name()='SDO' or name()='DA']");
			for (Object obj : das) {
				Element da = (Element) obj;
				String ndName = da.getName();
				String name = da.attributeValue("name");
				String type = da.attributeValue("type");
				String fc = da.attributeValue("fc");
				if (null != fc && !isNeededFC(fc))
					continue;
				String bType = null;
				if (ndName.equals("SDO"))
					bType = "SDO";
				else
					bType = da.attributeValue("bType");
				children.add(new DANode(name, type, bType, fc));
			}
		}
		return children;
	}
	
	/**
	 * 是否为关联DA
	 * @param fc
	 * @return
	 */
	private boolean isNeededFC(String fc) {
		if(sgType == SignalType.ST) {
			return fc.equals("ST");
		} else if(sgType == SignalType.MX) {
			return fc.equals("MX");
		} else {
			return fc.equals("ST") || fc.equals("MX");
		}
	}
	
	/**
	 * 是否为IO类信号
	 */
	private boolean isNeeededDA(DANode daNd) {
		return isNeededFC(daNd.getFc());
	}
	
	/**
	 * 是否为无效属性
	 * @param name
	 * @return 是为true，否为false
	 */
	private boolean isInvalidAttr(String name) {
		return "t".equals(name) || "q".equals(name);
	}
	
	/**
	 * 获取DO下所有属性地址
	 * @param daNode
	 * @return
	 */
	private List<String> getDODAAddrs(Element daNode) {
		String daName = daNode.attributeValue("name");
		String daType = daNode.attributeValue("type");
		DANode root = new DANode(daName, daType, "SDO");
		Stack<DANode> stack = new Stack<DANode>();
		stack.push(root);
		DANode curDa = null;
		List<String> daAddrs = new ArrayList<String>();
		while(true) {
			if(curDa != null) {
				if("SDO".equals(curDa.getBType())) {
					List<DANode> children = getSubSDODANds(curDa);
					for(DANode nd : children) {
						curDa.addChild(nd);
						nd.setParent(curDa);
						stack.add(nd);
					}
				} else {
					DANode parent = curDa.getParent();
					String addr = curDa.getDaName();
					while(null != parent) {
						addr = parent.getDaName() + "." + addr;
						parent = parent.getParent();
					}
					if(!isInvalidAttr(curDa.getDaName()) && isNeeededDA(curDa)) {
						if("Struct".equals(curDa.getBType())) {
							List<String> attrs = getStructDAAddrs(curDa);
							for(String attr : attrs) {
								daAddrs.add(attr);
							}
						} else {
							daAddrs.add(addr);
						}
					}
				}
				curDa = null;
			} else {
				if(!stack.isEmpty()) {
					curDa = stack.pop();
				} else {
					break;
				}
			}
		}
		return daAddrs;
	}
	
	/**
	 * 得到DO下Struct类型的DA的所有属性路径
	 * @param daNode
	 * @return
	 */
	private List<String> getStructDAAddrs(DANode root) {
		Stack<DANode> stack = new Stack<DANode>();
		stack.push(root);
		DANode curDa = null;
		List<String> daAddrs = new ArrayList<String>();
		while(true) {
			if(curDa != null) {
				if("Struct".equals(curDa.getBType())) {
					List<DANode> children = getSubStructDANds(curDa);
					for(DANode nd : children) {
						curDa.addChild(nd);
						nd.setParent(curDa);
						stack.add(nd);
					}
				} else {
					DANode parent = curDa.getParent();
					String addr = curDa.getDaName();
					while(null != parent) {
						addr = parent.getDaName() + "." + addr;
						parent = parent.getParent();
					}
					daAddrs.add(addr);
				}
				curDa = null;
			} else {
				if(!stack.isEmpty()) {
					curDa = stack.pop();
				} else {
					break;
				}
			}
		}
		return daAddrs;
	}
	
	/**
	 * 根据查询获取输入端子及其外部关联端子信息
	 * @param iedName
	 * @return 返回的关联信息以LD/LN分组
	 */
	public Map<String, List<SignalRelation>> getInputRelations(String iedName) {
		Map<String, List<SignalRelation>> inRelations = new HashMap<String, List<SignalRelation>>();
		List<Element> lns = getInputLNs(iedName);
		if (monitor != null) {
			monitor.beginTask("查询获取输入端子及其外部关联端子信息......", lns.size());
		}
		Map<String, SignalRelation> inputs = getInputs(iedName);
		for(Element ln : lns) {
			String ldInst = ln.attributeValue("ldInst");
			String prefix = ln.attributeValue("prefix");
			String lnClass = ln.attributeValue("lnClass");
			String lnInst = ln.attributeValue("lnInst");
			String ldLnTag = ldInst + "/" + prefix + lnClass + lnInst;
			List<SignalRelation> relations = new ArrayList<SignalRelation>();
			List<?> DOs = ln.elements();
			for(Object obj : DOs) {
				Element DO = (Element) obj;
				String doName = DO.attributeValue("name");
				String doDesc = getDODesc(DO);
				if(SignalsChecker.isExcludedDO(doName))
					continue;
				if (SV_IN_PREFIX.indexOf(prefix)>-1) {
					String intAddr = ldLnTag + "." + doName;
					SignalRelation relation = getInputRelation(inputs, intAddr);
					relation.setIntAddrDesc(doDesc);
					relations.add(relation);
					continue;
				}
				List<String> doAddrs = getDODAAddrs(DO);
				for(String addr : doAddrs) {
					String intAddr = ldLnTag + "." + addr;
					SignalRelation relation = getInputRelation(inputs, intAddr);
					relation.setIntAddrDesc(doDesc);
					relations.add(relation);
				}
			}
			inRelations.put(ldLnTag, relations);
			if (monitor != null)
				monitor.worked(1);
		}
		return inRelations;
	}

	private SignalRelation getInputRelation(Map<String, SignalRelation> inputs,
			String intAddr) {
		SignalRelation relation = null;
		for (Entry<String, SignalRelation> input : inputs.entrySet()) {
			// 根据396规范SV虚端子只需到DO，为了兼容到DA的情况改成字段匹配判断
			String key = input.getKey();
			if (key.equals(intAddr) || (key.indexOf(intAddr + ".") > -1)) {
				relation = input.getValue();
				break;
			}
		}
		if (relation == null) {
			relation = new SignalRelation();
			relation.setIntAddr(intAddr);
		}
		return relation;
	}
	
	private String getDODesc(Element doEle) {
		String doDesc = doEle.attributeValue("doDesc");
		String dU = doEle.attributeValue("dU");
		return StringUtil.isEmpty(dU)?doDesc:dU;
	}
	
	/**
	 * 查询开出关联信息
	 * @param iedName
	 * @return 返回信息以DataSet分组
	 */
	public Map<String, List<SignalRelation>> getOutputRelations(String iedName) {
		Map<String, List<SignalRelation>> outRelations = new HashMap<String, List<SignalRelation>>();
		List<Element> dataSets = getOutputDataSets(iedName);
		if (monitor != null) {
			int i = 0;
			for (Element el : dataSets) {
				i += el.elements("FCDA").size();
			}
			monitor.beginTask("查询开出关联信息......", i);
			System.err.println(i);
		}
		for (Element dataSet : dataSets) {
			String dataLdInst = dataSet.attributeValue("ldInst");
			String datSetName = dataSet.attributeValue("name");
			List<?> fcdas = dataSet.elements("FCDA");
			List<SignalRelation> relations = new ArrayList<SignalRelation>(); 
			for (Object obj : fcdas) {
				Element fcda = (Element) obj;
				String ldInst = fcda.attributeValue("ldInst");
				String prefix = fcda.attributeValue("prefix", "");
				String lnClass = fcda.attributeValue("lnClass");
				String lnInst = fcda.attributeValue("lnInst");
				String doName = fcda.attributeValue("doName");
				String daName = fcda.attributeValue("daName");
				if(isInvalidAttr(daName))
					continue;
				lnInst = StringUtil.nullToEmpty(lnInst);
				daName = StringUtil.nullToEmpty(daName);
				String desc = getFCDADesc(iedName, ldInst, prefix, lnClass, lnInst, doName, iedName);
				String extRef = ldInst + "/" + prefix + "$" + lnClass + "$" + lnInst + "$" + doName + "$" + daName;
				List<Element> outputs = xQueryofOutput(iedName, extRef);
				extRef = ldInst + "/" + prefix + lnClass + lnInst + "." + doName + 
					(StringUtil.isEmpty(daName) ? "" : ("." + daName));
				
				if (outputs.size() > 0) {
					for (Element output : outputs) {
						SignalRelation relation = createRelation(ldInst, prefix, lnClass, lnInst, doName, daName);
						relation.setExtDODesc(desc);
						relation.setExtIEDName(iedName);
						relation.setIntAddr(output.attributeValue("intAddr"));
						relation.setInIEDName(output.attributeValue("iedName"));
						relation.setInIEDDesc(output.attributeValue("iedDesc"));
						relation.setIntAddrDesc(getDODesc(output));
						relations.add(relation);
					}
				} else {
					SignalRelation relation = createRelation(ldInst, prefix, lnClass, lnInst, doName, daName);
					relation.setExtDODesc(desc);
					relation.setExtIEDName(iedName);
					relations.add(relation);
				}
				if (monitor != null)
					monitor.worked(1);
			}
			outRelations.put(dataLdInst + "." + datSetName, relations);
		}
		return outRelations;
	}
	
	private SignalRelation createRelation(String ldInst, String prefix, String lnClass, 
			String lnInst, String doName, String daName) {
		SignalRelation relation = new SignalRelation();
		relation.setExtLdInst(ldInst);	
		relation.setExtPrefix(prefix);
		relation.setExtLnClass(lnClass);
		relation.setExtLnInst(lnInst);
		relation.setExtDoName(doName);
		relation.setExtDaName(daName);
		return relation;
	}
	
	/**
	 * 查询某装置下所有外部开入信号信息
	 * @param iedName
	 * @return
	 */
	private Map<String, SignalRelation> getInputs(String iedName) {
		List<Element> extRefs = new ArrayList<>();
		if (Constants.XQUERY) {
			String xquery = "let $root:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL, $inputs:=$root/scl:IED[@name='" + iedName +
					"']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs return for $extRef in $inputs/* return " +
					"element ExtRef {$extRef/@*, attribute iedDesc {$root/IED[@name=$extRef/@iedName]/@desc}}";
			extRefs = XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> extRefEls = XMLDBHelper.selectNodes(SCL.getIEDXPath(iedName) +"/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/*");
			for (Element extRefEl : extRefEls) {
				String currIed = extRefEl.attributeValue("iedName");
				String desc = descCache.get(currIed);
				if (desc == null) {
					desc = XMLDBHelper.getAttributeValue(SCL.getIEDXPath(currIed) + "/@desc");
					descCache.put(currIed, desc);
				}
				extRefEl.addAttribute("iedDesc", desc);
				extRefs.add(extRefEl);
			}
		}
		
		Map<String, SignalRelation> inputs = new HashMap<String, SignalRelation>();
		for (Element extRef : extRefs) {
			String intAddr = extRef.attributeValue("intAddr");
			String ldInst = extRef.attributeValue("ldInst");
			String prefix = extRef.attributeValue("prefix");
			String lnClass = extRef.attributeValue("lnClass");
			String lnInst = extRef.attributeValue("lnInst");
			String doName = extRef.attributeValue("doName");
			String daName = extRef.attributeValue("daName");
			String extIEDName = extRef.attributeValue("iedName");
			
			SignalRelation relation = new SignalRelation();
			relation.setIntAddr(intAddr);
			relation.setExtIEDName(extIEDName);
			relation.setExtLdInst(ldInst);	
			relation.setExtPrefix(prefix);
			relation.setExtLnClass(lnClass);
			relation.setExtLnInst(lnInst);
			relation.setExtDoName(doName);
			relation.setExtDaName(daName);
			String iedDesc = extRef.attributeValue("iedDesc");
			relation.setExtIEDDesc(iedDesc);
			
			String desc = getFCDADesc(iedName, ldInst, prefix, lnClass, lnInst, doName, extIEDName);
			relation.setExtDODesc(desc);
			inputs.put(intAddr, relation);
		}
		return inputs;
	}

	private String getFCDADesc(String iedName, String ldInst, String prefix,
			String lnClass, String lnInst, String doName, String extIEDName) {
		String key = extIEDName + "$" + ldInst + "$" + prefix + "$" + lnClass + "$" + lnInst + "$" + doName;
		String desc = descCache.get(key);
		if (desc == null) {
			desc = fcdaDao.getFCDADesc(iedName, ldInst, prefix, lnClass, lnInst, doName);
			descCache.put(key, desc);
		}
		return desc;
	}
	
	/**
	 * 查询所有开出数据集
	 * @param iedName
	 * @return
	 */
	private List<Element> getOutputDataSets(String iedName) {
		List<Element> dataSets = new ArrayList<>();
		List<Element> cbELs = new ArrayList<>();
		if(Constants.XQUERY){
			String xquery = "let $ied:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "']" +
					" return for $ld in $ied/scl:AccessPoint/scl:Server/scl:LDevice" +
					" return for $cb in $ld/*/*[name()='GSEControl' or name()='SampledValueControl']" +
					" return let $dataset:=$ld/*/scl:DataSet[@name=$cb/@datSet]" +
					" return element DataSet {$dataset/@*, attribute ldInst {$ld/@inst}, $dataset/*}";
			return XMLDBHelper.queryNodes(xquery);
		}else{
			Element iedEl = XMLDBHelper.selectSingleNode(SCL.getIEDXPath(iedName));
			if (iedEl != null) {
				List<Element> ldELs = DOM4JNodeHelper.selectNodes(iedEl,"./scl:AccessPoint/scl:Server/scl:LDevice");
				for (Element ldEL : ldELs) {
					String ldInst = ldEL.attributeValue("inst");
					cbELs = DOM4JNodeHelper.selectNodes(ldEL, "./*/*[name()='GSEControl' or name()='SampledValueControl']");
					for (Element cbEL : cbELs) {
						Element dataset = DOM4JNodeHelper.selectSingleNode(ldEL, "./*/scl:DataSet[@name='"+ cbEL.attributeValue("datSet") +"']");
						dataset.addAttribute("ldInst", ldInst);
						dataSets.add(dataset);
					}
				}
			} else {
				ConsoleManager.getInstance().append("当前SCD文件中没有此装置，请重新选择SCD文件。");
			}
			return dataSets;
		}
	}
	/**
	 * 查找IED输出管脚所关联的信息
	 * 平均耗时700ms
	 * 返回的是一个List<Element>
	 * 每个Element含有四个属性，分别是:iedName,iedDesc,intAddr和DO描述
	 * 关联包括LN0和LN下的Inputs
	 * @param  iedName
	 *         目标装置名
	 * @param  doInfo
	 * 		   该输出管脚的DO信息
	 * 		   格式：ldInst/prefix$lnClass$lnInst$doName$daName   
	 * @return 与该管脚所有关联IED(iedName, iedDesc)及DO(intAddr, desc)信息
	 *
	 */
	public static List<Element> xQueryofOutput(String iedName, String doInfo){
		int dot1 = doInfo.indexOf('$');
		int dot2 = doInfo.indexOf('$', dot1+1);
		int dot3 = doInfo.indexOf('$', dot2+1);
		int dot4 = doInfo.indexOf('$', dot3+1);
		if(dot4<0){
			dot4 = doInfo.length();
		}
		String ldInst = doInfo.substring(0, doInfo.indexOf('/'));
		String prefix = doInfo.substring(doInfo.indexOf('/')+1, doInfo.indexOf('$'));
		String lnClass = doInfo.substring(dot1+1, dot2);
		String lnInst = doInfo.substring(dot2+1, dot3);
		String doName = doInfo.substring(dot3+1, dot4);
		String daName = "";
		if (dot4 != doInfo.length()) {
			daName = doInfo.substring(dot4 + 1);
		}
		String lnAtt = SCL.getLNAtts(prefix, lnClass, lnInst);
		String daAtt = SCL.getDaAtts(daName);
		List<Element> list = new ArrayList<>();
		if(Constants.XQUERY){
			String xquery = "for $ied in " + XMLDBHelper.getDocXPath() + "/scl:SCL/scl:IED " + //$NON-NLS-1$
					"return for $ld in $ied/scl:AccessPoint/scl:Server/scl:LDevice "+ //$NON-NLS-1$ //$NON-NLS-2$
					"return for $extRef in $ld/*/scl:Inputs/scl:ExtRef[@iedName='" + iedName + "'][@ldInst='" + ldInst +
					"']" + lnAtt + "[@doName='" + doName + "']" + daAtt +
					" return <Ref iedName='{$ied/@name}' iedDesc='{$ied/@desc}' ldInst='{$ld/@inst}' intAddr='{$extRef/@intAddr}'/>";
			list = XMLDBHelper.queryNodes(xquery);
		} else {
			List<Element> iedEls = IEDDAO.getALLIED();
			for (Element ied : iedEls) {
				List<Element> extRefEls = XMLDBHelper.selectNodes(SCL.getIEDXPath(ied.attributeValue("name")) + "/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef[@iedName='" + iedName + "'][@ldInst='" + ldInst +
							"']" + lnAtt + "[@doName='" + doName + "']" + daAtt);
				for (Element extRefEl : extRefEls) {
					Document document = DocumentHelper.createDocument();
					Element RefEl = document.addElement("Ref");
					RefEl.addAttribute("iedName", ied.attributeValue("name"));
					RefEl.addAttribute("iedDesc", ied.attributeValue("desc"));
					RefEl.addAttribute("ldInst", ldInst);//extRefEl.getParent().getParent().getParent().attributeValue("inst"));
					RefEl.addAttribute("intAddr", extRefEl.attributeValue("intAddr"));
					list.add(RefEl);
				}
			}	
		}
		
		if (list == null)
			return null;
		List<Element> listElement = new ArrayList<Element>();
		for (Element element : list) {
			String relativeIEDName = element.attributeValue("iedName"); //$NON-NLS-1$
			String relativeLdInst = element.attributeValue("ldInst"); //$NON-NLS-1$
			String ldXpath = SCL.getLDXPath(relativeIEDName, relativeLdInst);
			Element ldEl = ldCache.get(ldXpath);
			if (ldEl == null) {
				ldEl = XMLDBHelper.selectSingleNode(ldXpath);
				ldCache.put(ldXpath, ldEl);
			}
			String addr = element.attributeValue("intAddr");
			String[] addrInfo = SCL.getIntAddrInfo(addr);
			String key = relativeIEDName + "$" + relativeLdInst + "$" + addrInfo[1] + "$" + addrInfo[2] + "$" + addrInfo[3] + "$" + addrInfo[4];
			String dodesc = descCache.get(key);
			if(dodesc == null){
				if (addrInfo.length < 5 || ldEl == null){
					dodesc = "";
					descCache.put(key, dodesc);
				} else{
					dodesc = GOOSEInputDAO.getInnerDesc(element, ldEl);
					descCache.put(key, dodesc);
				}
			}
			
			element.addAttribute("doDesc", dodesc); //$NON-NLS-1$
			listElement.add(element);
		}
		return listElement;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}
}
