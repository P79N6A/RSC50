/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.das;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.business.scl.util.SCLUtil;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2016-1-5
 */
public class CCDModelDao {

	private Element iedNode;

	/** pubs导出顺序，subs导出顺序由inputs决定 */
	private List<String> pubCbs = new ArrayList<>();
	// key: cbRef, value: cbAp
	private Map<String, Element> cbApCache = new HashMap<>();
	
	private Map<String, Map<String, Object[]>> pubsCache = new HashMap<>();
	// <Inputs/ExtRef>
	private List<Element> inputs = new ArrayList<Element>();
	// key: outFullRef, value: cbRef
	private Map<String, String> inputsCbCache = new HashMap<>();
	
	private Map<String, Object[]> apMap = new HashMap<>();
	
	private Map<String, List<String>> outRefs = new HashMap<>();
	private Map<String, Map<String, Object[]>> lnTypeMap = new HashMap<>();
	
	private String iedName;
	private boolean isCheck = true;
	
	public CCDModelDao(String iedName, Map<String, Map<String, Object[]>> lnTypeMap, Map<String, Element> cbApCache, List<String> pubCbs) {
		this.iedName = iedName;
		this.iedNode = IEDDAO.getIEDNode(iedName);
		this.lnTypeMap = lnTypeMap;
		this.cbApCache = cbApCache;
		this.pubCbs = pubCbs;
		if (iedNode == null)
			return;
		this.inputs = DOM4JNodeHelper.selectNodes(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0/scl:Inputs/scl:ExtRef");
	}

	private void collectApMap() {
		String xpath = "./scl:AccessPoint/scl:Server/scl:LDevice/scl:LN0/*[name()='GSEControl' or name()='SampledValueControl']";
		List<?> els = DOM4JNodeHelper.selectNodes(iedNode, xpath);
		int cbIndex = 0;
		for (Object obj : els) {
			Element elCB = (Element) obj;
			Element elLD = elCB.getParent().getParent();
			String ldInst = elLD.attributeValue("inst");
			String dsName = elCB.attributeValue("datSet");		// 控制块数据集名称

			String cbName = elCB.attributeValue("name");
			EnumCtrlBlock block = EnumCtrlBlock.valueOf(elCB.getName());
			String cbRef = SCL.getCbRef(iedName, ldInst, cbName, block);
			
			prepareApMap(iedName, cbIndex, elCB, ldInst, dsName, cbRef);
			cbIndex++;
		}
		for (Element extref : inputs) {
			String outFullRef = SCL.getNodeFullRef(extref);
			List<String> intAddrs = outRefs.get(outFullRef);
			if (intAddrs == null) {
				intAddrs = new ArrayList<>();
				outRefs.put(outFullRef, intAddrs);
			}
			intAddrs.add(extref.attributeValue("intAddr"));
		}
	}

	private void prepareApMap(String iedName, int cbIndex, Element elCB, String ldInst, String dsName, String cbRef) {
		Element elLN = elCB.getParent();
		Element elLD = elLN.getParent();
		String dsXpath = "./scl:DataSet[@name='" + dsName + "']";
		
		Element dsEl = DOM4JNodeHelper.selectSingleNode(elLN, dsXpath).createCopy();
		List<?> fcdaEls = dsEl.elements();
		for (Object obj1 : fcdaEls) {
			Element fcdaEl = (Element) obj1;
			String prefix = fcdaEl.attributeValue("prefix");
			String lnClass = fcdaEl.attributeValue("lnClass");
			String lnInst = fcdaEl.attributeValue("lnInst");
			String doName = fcdaEl.attributeValue("doName");
			String daName = fcdaEl.attributeValue("daName");
			Element fcdaLn = DOM4JNodeHelper.selectSingleNode(elLD, "." + SCL.getLNXPath(prefix, lnClass, lnInst));
			if (fcdaLn == null)
				continue;
			String doxpath = "." + SCL.getDOXPath(doName);
			Element doEl = DOM4JNodeHelper.selectSingleNode(fcdaLn, doxpath); 
			// 添加bType、desc
			addAttr(fcdaEl, fcdaLn, doName, daName, doEl);
			
			if (this.iedName.equals(iedName)) { // 发布
				if (doEl != null) {
					if (StringUtil.isEmpty(daName)) {
						Element doi = doEl.createCopy();
						CRCInfoDao.clearDesc(doi);
						fcdaEl.add(doi);
					} else {
						Element daEl = DOM4JNodeHelper.selectSingleNode(doEl, "./" + SCL.getDOXPath(daName));
						if (daEl != null) { // 有些装置的q、t不做实例化
							Element dai = daEl.createCopy();
							CRCInfoDao.clearDesc(dai);
							fcdaEl.add(dai);
						}
					}
				}
			} else {// 订阅
				collectSubInfo(iedName, fcdaEl);
			}
		}
		this.apMap.put(cbRef, new Object[]{elCB, dsEl, cbIndex});
	}
	
	/**
	 * 为FCDA添加bType、desc属性
	 * 
	 * @param fcdaEl
	 * @param fcdaLn
	 * @param isSV
	 * @param doName
	 * @param daName
	 * @param doEl
	 */
	private void addAttr(Element fcdaEl, Element fcdaLn, String doName, String daName, Element doEl){
		String lnType = fcdaLn.attributeValue("lnType");
		Map<String, Object[]> outDodaTypeMap = lnTypeMap.get(lnType);
		String outDodaName = doName + "." + daName;
		if (outDodaTypeMap != null) {
			Object[] dodatype = outDodaTypeMap.get(outDodaName);
			if (dodatype != null && dodatype.length > 0) {
				String outDodaType = (String) dodatype[0];
				if (!StringUtil.isEmpty(outDodaType) && !"Struct".equals(outDodaType)) {
					fcdaEl.addAttribute("bType", outDodaType);
				}
			}
		}
		// 为fcda添加desc
		String desc = (doEl==null) ? "" : doEl.attributeValue("desc", "");
		fcdaEl.addAttribute("desc", desc);
	}

	/**
	 * 采集订阅信息（仅用于自组数据条件分支）
	 * 
	 * @param iedName
	 * @param fcdaEl
	 */
	private void collectSubInfo(String iedName, Element fcdaEl) {
		String nodeRef = iedName + SCL.getNodeRef(fcdaEl);
		List<String> intAddrs = outRefs.get(nodeRef);
		if (intAddrs==null || intAddrs.size()<1) {
			Element intAddrEl = fcdaEl.addElement("intAddr");
			intAddrEl.addAttribute("desc", "");
			intAddrEl.addAttribute("name", "NULL");
		} else {
			outRefs.remove(nodeRef);
			for (String intAddr : intAddrs) {
				Element intAddrEl = fcdaEl.addElement("intAddr");
				int pm = intAddr.indexOf(':'); // 内部虚端子前面可能有物理端口
				String newIntAddr = intAddr;
				if (pm > 0)
					newIntAddr = intAddr.substring(pm + 1);
				String[] intInfo = newIntAddr.split("/|\\.");
				if (intInfo.length < 3)
					continue;
				String ldInst = intInfo[0];
				String inDoName = intInfo[2];
				String[] lnInfo = SCLUtil.getLNInfo(intInfo[1]);
				String inLnprefix = lnInfo[0];
				String inLnClass = lnInfo[1];
				String inLnInst = lnInfo[2];
	
				Element inLN = DOM4JNodeHelper.selectSingleNode(iedNode, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst + "']" + SCL.getLNXPath(inLnprefix, inLnClass, inLnInst));
	
				// 添加intAddr<DOI>,<DAI>
				if (inLN != null) {
					Element indoEl = DOM4JNodeHelper.selectSingleNode(inLN, "./*[@name='" + inDoName + "']");
					if (indoEl != null) {
						String indesc = indoEl.attributeValue("desc");
						intAddrEl.addAttribute("desc", indesc);
						intAddrEl.addAttribute("name", intAddr);
						if (intInfo.length == 2) {
							Element doi = indoEl.createCopy();
							CRCInfoDao.clearDesc(doi);
							intAddrEl.add(doi);
						} else {
							int p = intAddr.indexOf('.');
							String inDodaName = intAddr.substring(p + 1);
							Element daEl = DOM4JNodeHelper.selectSingleNode(inLN, "." + SCL.getDOXPath(inDodaName));
							if (daEl != null) { // 有些装置的q、t不做实例化
								Element dai = daEl.createCopy();
								CRCInfoDao.clearDesc(dai);
								intAddrEl.add(dai);
							}
						}
					}
				}
			}
		}
	}

	public Document createCcdDom() {
		try {
			collectApMap();
		} catch (NullPointerException e) {
			return null;
		}
		if (iedNode == null)
			return null;
		Document ccdDom = DOM4JNodeHelper.createSCLNode("IED").getDocument();
		Element ccdRoot = ccdDom.getRootElement();
		DOM4JNodeHelper.copyAttributes(iedNode, ccdRoot);
		Element gpRoot = ccdRoot.addElement("GOOSEPUB");
		Element gsRoot = ccdRoot.addElement("GOOSESUB");
		Element spRoot = ccdRoot.addElement("SVPUB");
		Element ssRoot = ccdRoot.addElement("SVSUB");
		// GOOSE PUB
		// SMV PUB
		fillPubs(gpRoot, spRoot);
		// GOOSE SUB
		// SMV SUB
		fillSubs(gsRoot, ssRoot);

		Element[] parts = new Element[] { gpRoot, spRoot, gsRoot, ssRoot };
		for (Element part : parts) {
			if (part.elements().size() < 1)
				ccdRoot.remove(part);
		}

		return ccdDom;
	}

	private void fillPubs(Element gpRoot, Element spRoot) { 
		if(pubCbs == null)
			return;
		for (String cbRef : pubCbs) {
			Element cbAp = cbApCache.get(cbRef);
			if (cbAp == null)
				continue;
			boolean isGse = cbAp.element("GSE") != null;
			Element elPub = isGse ? gpRoot : spRoot;
			String ndName = getCBNodeName(isGse);
			fillCbDs(iedName, cbRef, elPub, ndName, false);
		}
		// 按照IED中控制块顺序排列
		sortPubs(gpRoot);
		sortPubs(spRoot);
	}

	@SuppressWarnings("unchecked")
	private void sortPubs(Element elPub) {
		List<Element> pubList = new ArrayList<>();
		pubList.addAll(elPub.elements());
		Comparator<Element> comp = new Comparator<Element>() {
			@Override
			public int compare(Element o1, Element o2) {
				String cbRef1 = o1.attributeValue("name");
				String cbRef2 = o2.attributeValue("name");
				if (pubsCache.size() > 0)
					apMap = pubsCache.get(iedName);
				Integer cbIdx1 = (Integer) apMap.get(cbRef1)[2];
				Integer cbIdx2 = (Integer) apMap.get(cbRef2)[2];
				return cbIdx1.compareTo(cbIdx2);
			}
		};
		java.util.Collections.sort(pubList, comp);
		elPub.clearContent();
		for (Element pub : pubList) {
			elPub.add(pub.createCopy());
		}
	}

	private void fillSubs(Element gsRoot, Element ssRoot) {
		Map<String, Element> ldMap = collectLdMap();
		Map<String, Element> map = new HashMap<String, Element>();
		int idx = 0;
		for (Element extref : inputs) {
			String outFullRef = SCL.getNodeFullRef(extref);
			String cbRef = inputsCbCache.get(outFullRef);
			if (isCheck && StringUtil.isEmpty(inputsCbCache.get(outFullRef))) {
				String extIedName = extref.attributeValue("iedName");
				String ldInst = extref.attributeValue("ldInst");
				String ldXpath = SCL.getLDXPath(extIedName, ldInst);
		    	Element ldEl = ldMap.get(ldXpath);
				Element cbEl = LNDAO.getCbByLD(ldEl, extref);
				if (cbEl == null)
					continue;
				String dsName = cbEl.attributeValue("datSet");
				String cbName = cbEl.attributeValue("name");
				EnumCtrlBlock block = EnumCtrlBlock.valueOf(cbEl.getName());
				cbRef = SCL.getCbRef(extIedName, ldInst, cbName, block);
				
				inputsCbCache.put(outFullRef, cbRef);// 避免重复查询
				prepareApMap(extIedName, idx, cbEl, ldInst, dsName, cbRef);
				idx++;
			}
			Element cbAp = cbApCache.get(cbRef);
			if (cbAp == null)
				continue;
			String iedName = cbAp.attributeValue("iedName");
			// 去重
			if (map.containsKey(cbRef)) {
				continue;
			} else {
				map.put(cbRef, cbAp);
			}
			if (cbAp != null) {
				boolean isGse = cbAp.element("GSE") != null;
				Element elSub = isGse ? gsRoot : ssRoot;
				String ndName = getCBNodeName(isGse);
				fillCbDs(iedName, cbRef, elSub, ndName, true);
			}
		}
	}

	private Map<String, Element> collectLdMap() {
		Map<String, Element> ldMap = new HashMap<String, Element>();
		if (isCheck) {
			for (Element extref : inputs) {
				String extIedName = extref.attributeValue("iedName");
				String ldInst = extref.attributeValue("ldInst");
				String ldXpath = SCL.getLDXPath(extIedName, ldInst);
				if (ldMap.get(ldXpath) == null) {
					Element ldEl = XMLDBHelper.selectSingleNode(ldXpath);
					ldMap.put(ldXpath, ldEl);
				}
			}
		}
		return ldMap;
	}

	private String getCBNodeName(boolean isGse) {
		return isGse ? "GOCBref" : "SMVCBref";
	}

	private void fillCbDs(String iedName, String cbRef, Element elPubSub,
			String ndName, boolean isSub) {
		Element elCBref = elPubSub.addElement(ndName);
		elCBref.addAttribute("name", cbRef);
		if (pubsCache.size() > 0)
			apMap = pubsCache.get(iedName);
		boolean exists = (apMap != null);
		Object[] cbDs = exists ? apMap.get(cbRef) : null;
		if (cbDs == null)
			return;
		Element cbEl = (Element) ((Element) cbDs[0]).createCopy();
		Element capEl = (Element) cbApCache.get(cbRef).createCopy();
		Element dsEl = (Element) ((Element) cbDs[1]).createCopy();
		clearNotNeed(isSub, cbEl, capEl, dsEl);
		elCBref.add(cbEl); // 控制块
		elCBref.add(capEl); // 通信参数
		elCBref.add(dsEl); // 数据集
	}

	private void clearNotNeed(boolean isSub, Element... els) {
		for (Element el : els) {
			if (isSub) {
				clearSubNodes(el, "Private");
				clearSubNodes(el, "PhysConn");
			}
			Attribute attdesc = el.attribute("desc");
			if (attdesc != null)
				el.remove(attdesc);
		}
	}

	@SuppressWarnings("unchecked")
	private void clearSubNodes(Element el, String subName) {
		List<Element> elphs = el.elements(subName);
		for (Element elph : elphs) {
			if (elph != null)
				el.remove(elph);
		}
	}

	public String getIEDString() {
		return iedNode.asXML();
	}
}
