/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import com.shrcn.business.scl.check.CCDDomBuilder;
import com.shrcn.business.scl.check.CCDModelChecker;
import com.shrcn.business.scl.check.CCDTextBuilder;
import com.shrcn.business.scl.check.InstResolver;
import com.shrcn.business.scl.check.Problem;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.ui.ProblemManager;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.ViewManager;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2013-1-29
 */
/**
 * $Log: CRCInfoDao.java,v $
 * Revision 1.2  2013/08/13 12:45:23  cchun
 * Udpate:为getInputs增加ap信息
 *
 * Revision 1.1  2013/03/29 09:36:21  cchun
 * Add:创建
 *
 * Revision 1.6  2013/02/05 03:48:41  cchun
 * Refactor:封装FileManipulate.getCRC32Clean()
 *
 * Revision 1.5  2013/02/05 03:35:43  cchun
 * Update:1、改进LN定位条件；2、算CRC时去掉空格
 *
 * Revision 1.4  2013/02/01 09:48:30  cchun
 * Update:增加数据项LLN0查询容错处理
 *
 * Revision 1.3  2013/02/01 08:38:50  cchun
 * Update:增加clearCRCCode()
 *
 * Revision 1.2  2013/01/30 02:50:19  cchun
 * Update:添加addCRCCode(),calcCRCCode(),createCRCDoc()
 *
 * Revision 1.1  2013/01/29 11:08:26  cchun
 * Add:CRC信息查询类
 *
 */
public class CRCInfoDao {
	
	/**
	 * 得到当前装置的关联装置
	 * @param iedName
	 * @return
	 */
	public static List<String> getInputIED(String iedName) {
		List<String> iedSet = new ArrayList<String>();
		String xpath = "/scl:SCL/scl:IED[@name='" + iedName + "']/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef/@iedName";
		List<String> names = XMLDBHelper.getAttributeValues(xpath);
		for (String name : names) {
			if (!iedSet.contains(name))
				iedSet.add(name);
		}
		return iedSet;
	}
	
	public static void clearDesc(Element doda) {
		Attribute elDesc = doda.attribute("desc");
		if (elDesc != null)
			doda.remove(elDesc);
		@SuppressWarnings("unchecked")
		List<Element> subs = doda.elements();
		if (subs!=null && subs.size()>0) {
			for (Element sub : subs) {
				clearDesc(sub);
				if ("dU".equals(sub.attributeValue("name"))) {
					doda.remove(sub);
				}
			}
		}
	}
	
	/**
	 * 查询IED关联信息（仅IEDConfigTool调用）
	 * @param iedName
	 * @return
	 */
	public static Element getInputs(String iedName) {
		List<Element> nds = new ArrayList<Element>();
		if (Constants.XQUERY) {
			String xquery = "let $ied:=" + XMLDBHelper.getDocXPath() +
					"/scl:SCL/scl:IED[@name='" + iedName + "'] return element Inputs {" +
					"for $ap in $ied/scl:AccessPoint return for $extref in $ap/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef " +
					"return element ExtRef {$extref/@*, attribute AP {$ap/@name}}}";
			nds = XMLDBHelper.queryNodes(xquery);
			if (nds.size() > 0)
				return nds.get(0);
		} else {
			List<Element> extRefEls = XMLDBHelper.selectNodes(SCL.getIEDXPath(iedName) + "/scl:AccessPoint");
			Element inputEl = DOM4JNodeHelper.createSCLNode("Inputs");
			for (Element el : extRefEls) {
				String apName = el.attributeValue("name");
				List<Element> list = DOM4JNodeHelper.selectNodes(el, "./scl:Server/scl:LDevice/scl:LN0/scl:Inputs/scl:ExtRef");
				if (list == null || list.size() == 0)
					continue;
				for (Element extEl : list) {
					Element cpEl = extEl.createCopy();
					cpEl.addAttribute("AP", apName);
					inputEl.add(cpEl);
				}
			}
			return inputEl;
		}
		return null;
	}
	
	public static Document createCRCDocNew(String iedName) {
		CCDModelChecker checker = new CCDModelChecker(iedName, CCDModelChecker.CHECK_CCD);
		boolean pass = false;
		try {
			pass = checker.doCheck();
		} catch (RuntimeException e) {
			pass = false;
			checker.getProblems().add(
				Problem.createError(iedName, "XML语法", "", e.getMessage()));
		}
		List<Problem> problems = checker.getProblems();
		if (!pass) {
			ProblemManager.getInstance().append(problems);
			return null;
		}
		ProblemManager.getInstance().append(problems); // 可能还有警告
		Document ccdDoc = checker.createCcdDom();
		if (ccdDoc == null)
			return null;
		checker.clear();
		return ccdDoc;
	}
	
	public static Document createCRCDoc(String iedName, Map<String, Map<String, Object[]>> lnTypeMap, Map<String, Element> map, List<String> subCbs){
		CCDModelDao ccdModelDao = new CCDModelDao(iedName, lnTypeMap, map, subCbs);
		Document ccdDoc = ccdModelDao.createCcdDom();
		if (subCbs != null)
			subCbs.remove(iedName);
		if (ccdDoc == null)
			return null;
		return ccdDoc;
	}
	
	/**
	 * 创建虚端子配置文件
	 * @param iedName
	 * @param path
	 */
	public static String createCRCFile(String iedName, String path) {
		Document ccdDoc = createCRCDocNew(iedName);
		if (ccdDoc == null)
			return null;
		
		String ccdText = CCDTextBuilder.getCCDText(ccdDoc);
		String ccdTextPath = Constants.crc32Dir + File.separator + ccdDoc.getRootElement().attributeValue("name") + ".txt";
		FileManager.saveTextFile(ccdText, ccdTextPath);
		String crcCode = FileManipulate.getCRC32(ccdText);
		createCRC(ccdDoc.getRootElement(), crcCode);
		FileManager.saveTextFile(path, CCDDomBuilder.getCCDDocument(ccdDoc), Constants.CHARSET_UTF8);
		return crcCode;
	}

	private static void createCRC(Element ied, String crc) {
		Element el = ied.addElement("CRC");
		el.addAttribute("id", crc);
		el.addAttribute("timestamp", new SimpleDateFormat(Constants.STD_TIME_FORMAT).format(new Date()));
	}
	
	/**
	 * 计算SCD文件CRC32代码
	 */
	public static String calcCRCCode(IProgressMonitor monitor) {
		TimeCounter.begin();
		String crcDir = Constants.crc32Dir;
		FileManipulate.deleteDir(crcDir);
		FileManipulate.initDir(crcDir);
		List<Element> ieds = new ArrayList<Element>(); // 转储避免排序错误
		for (Element ied : IEDDAO.getALLIED()) {
			ieds.add(ied);
		}
		if (monitor != null)
			monitor.beginTask("装置共计:" + ieds.size() + "个, 开始计算装置CRC...",
					ieds.size() + 1);
		
		// 升序排列
		Comparator<Element> c = new Comparator<Element>() {
			@Override
			public int compare(Element t1, Element t2) {
				return t1.attributeValue("name").compareTo(t2.attributeValue("name"));
			}};
		java.util.Collections.sort(ieds, c);
		
		String scdCRC = "";
		String msg = "";
		for (Element ied : ieds) {
			String iedName = ied.attributeValue("name");
			monitor.setTaskName("正在计算 " + iedName + " 的CRC");
			Document ccdDoc = createCRCDocNew(iedName);
			String crcCode = "";
			if (ccdDoc == null) {
				if (!"".equals(msg))
					msg += "\n";
				msg += "警告：装置 " + iedName + " 虚端子CRC计算失败！";
			} else {
				String ccdText = CCDTextBuilder.getCCDText(ccdDoc);
				crcCode = FileManipulate.getCRC32(ccdText);
			}
			addCRCCode(SCL.getIEDXPath(iedName), Constants.IED_CRC_DESC, crcCode);
			scdCRC += crcCode;
			if (monitor != null)
				monitor.worked(1);
		}
		String scdCRCCode =  FileManipulate.getCRC32(scdCRC);
		addCRCCode("/scl:SCL", Constants.SCD_CRC_DESC, scdCRCCode);
		XMLDBHelper.forceCommit();
		if (monitor != null)
			monitor.worked(1);
		
		if (!"".equals(msg))
			msg += "\n";
		msg += TimeCounter.end("CRC计算");
		ConsoleManager.getInstance().append(msg, false);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ViewManager.findView(UIConstants.View_Console_ID);
			}
		});
		return scdCRCCode;
				
	}

	public static Map<String, Map<String, Object[]>> collectLnType() {
		Element dataTplNode = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		// 缓存模板
		Map<String, Map<String, Object[]>> lnTypeMap = new InstResolver(dataTplNode, "", new ArrayList<Problem>()).getLnTypeMap();
		return lnTypeMap;
	}
	
	@SuppressWarnings("unchecked")
	public static void collectCbApCache(Map<String, Element> map, Map<String, List<String>> subCbs) {
		List<Element> els = XMLDBHelper.selectNodes(SCL.XPATH_SUBNETWORK + "/scl:ConnectedAP");
		for (Element commEl : els) {
			String curIedName = commEl.attributeValue("iedName");
			List<Element> pEls = commEl.elements("Private");
			List<Element> phyEls = commEl.elements("PhysConn");
		
			List<Element> commEls = DOM4JNodeHelper.selectNodes(commEl, "./*[name()='GSE' or name()='SMV']"); 
			for(Element el : commEls){
				String ldInst = el.attributeValue("ldInst");
				String cbName = el.attributeValue("cbName");
				String name = el.getName();
				EnumCtrlBlock block = name.equals("GSE") ?  EnumCtrlBlock.GSEControl : EnumCtrlBlock.SampledValueControl;
				String cbRef = SCL.getCbRef(curIedName, ldInst, cbName, block);

				Element cbAp = DOM4JNodeHelper.createSCLNode("ConnectedAP");
				DOM4JNodeHelper.copyAttributes(commEl, cbAp);

				if (subCbs.get(curIedName) == null)
					subCbs.put(curIedName, new ArrayList<String>());
				subCbs.get(curIedName).add(cbRef);
				
				map.put(cbRef, cbAp);
				for (Element pEL : pEls) {
					cbAp.add(pEL.createCopy()); // 可能存在多个CB公用一个ConnectedAP的<Private>
				}
				cbAp.add(el.createCopy());
				for (Element phyEl : phyEls) {
					cbAp.add(phyEl.createCopy()); // 可能存在多个CB公用一个ConnectedAP的<PhysConn>
				}
			}
		}
	}
	
	/**
	 * 保存CRC代码
	 * @param xpath
	 * @param desc
	 * @param value
	 */
	private static void addCRCCode(String xpath, String desc, String value) {
		String crcXpath = "/Private[@type='" + desc + "']";
		List<Element> els = XMLDBHelper.selectNodes(xpath + crcXpath);
		if (els.size() > 1) {
			XMLDBHelper.removeNodes(xpath + crcXpath);
			els = null;
		}
		if (els == null || els.size() < 1) { // 不存在
			Element crcNode = DOM4JNodeHelper.createSCLNode("Private");
			crcNode.addAttribute("type", desc);
			crcNode.setText(value);
			XMLDBHelper.insertAsFirst(xpath, crcNode);
		} else {
			XMLDBHelper.updateOnce(xpath + crcXpath, value);
		}
	}
	
	/**
	 * 计算单个Ied的Crc码
	 * @param iedName
	 * 结果现在跑起来很慢，多数情况下ccdDoc==null
	 */
	public static String calcIedCrc(String iedName)
	{
		Document ccdDoc = createCRCDocNew(iedName);
		String crcCode = "";
		if (ccdDoc == null) {
			ConsoleManager.getInstance().append("警告：装置 " + iedName + " 虚端子CRC计算失败！", false);
		} else {
			String ccdText = CCDTextBuilder.getCCDText(ccdDoc);
			crcCode = FileManipulate.getCRC32(ccdText);
		}
		addCRCCode(SCL.getIEDXPath(iedName), Constants.IED_CRC_DESC, crcCode);
		return crcCode;
	}
	
}
