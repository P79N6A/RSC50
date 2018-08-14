/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.SCTProperties;
import com.shrcn.business.scl.das.CRCInfoDao;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-22
 */
public class CCDModelChecker {

	public static final int CHECK_CCD = 0;
	public static final int CHECK_CID = 1;
	
	private int checkType = CHECK_CID;
	
	private String iedName;
	private Element commNode;
	private Element iedNode;
	private Element dataTplNode;
	private List<Problem> problems = new ArrayList<Problem>();
	// key: iedName, value: iedNode
	private Map<String, Element> iedCache = new HashMap<>();
	// key: iedName, value: (key:ldInst+"/"+lnName, value:<LN>)
	private Map<String, Map<String, Element>> lnMap = new HashMap<String, Map<String, Element>>();
	// <Inputs/ExtRef>
	private List<Element> inputs = new ArrayList<Element>();
	// key: outFullRef, value: List<ExtRef>
	private Map<String, List<Element>> inputsCache = new HashMap<>(); // 可能一对多，处理完即移除，最后遗留项即为找不到外部虚端子的关联
	// key: outFullRef, value: cbRef
	private Map<String, String> inputsCbCache = new HashMap<>();
	// key:iedName value:(key: cbRef, value: {elCB, dsEl})
	private Map<String, Map<String, Object[]>> pubsCache = new HashMap<>();
	// key: cbRef, value: cbAp
	private Map<String, Element> cbApCache = new HashMap<>();
	// key: lnType, value: (key:dodaName, value:bType/cdc)
	private Map<String, Map<String, Object[]>> lnTypeMap = new HashMap<String, Map<String, Object[]>>();
	
	/** pubs导出顺序，subs导出顺序由inputs决定 */
	List<String> pubCbs = new ArrayList<>();
	
	private InstResolver ires;

	public CCDModelChecker(String iedName, int checkType) {
		this.iedName = iedName;
		this.checkType = checkType;
	}
	
	/**
	 * 清除缓存
	 */
	public void clear() {
		problems.clear();
		iedCache.clear();
		lnMap.clear();
		inputs.clear();
		inputsCache.clear();
		inputsCbCache.clear();
		pubsCache.clear();
		cbApCache.clear();
		lnTypeMap.clear();
		pubCbs.clear();
	}
	
	private void addError(String iedName, String subType, String ref, String desc) {
		problems.add(Problem.createError(iedName, subType, ref, desc));
	}
	
	private void addWarning(String iedName, String subType, String ref, String desc) {
		problems.add(Problem.createWarning(iedName, subType, ref, desc));
	}

	private void addWarning(String iedName, String subType, String ref, String desc, String detailInfo) {
		problems.add(Problem.createWarning(iedName, subType, ref, desc, detailInfo));
	}
	
	public boolean doCheck() {
		this.iedNode = IEDDAO.getIEDNode(iedName);
		if (iedNode==null) {
			addError(iedName, "装置查询", "", "装置不存在");
			return false;
		}
		iedCache.put(iedName, iedNode);
		this.commNode = XMLDBHelper.selectSingleNode(SCL.XPATH_COMMUNICATION);
		this.dataTplNode = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		// 缓存模板
		this.ires = new InstResolver(dataTplNode, iedName, problems);
		lnTypeMap = ires.getLnTypeMap();
				
		cacheIedParts(iedNode);
		checkCBDefines();
		if (inputs.size()<1 && pubsCache.size()<1) {
			boolean hasErr = false;
			for (Problem p : problems) {
				if (p.getLevel() == LEVEL.ERROR) {
					hasErr = true;
					break;
				}
			}
			if (!hasErr && (checkType==CHECK_CCD)) {
				addError(iedName, "通信检查", "", "无过程层通信");
				return false;
			}
			return !hasErr;
		}
		
		checkCBComms();
		
		// 检查未配置通信参数的控制块
		for (String apIedName : pubsCache.keySet()) {
			Map<String, Object[]> cbRefMap = pubsCache.get(apIedName);
			if (cbRefMap != null) {
				Set<String> cbRefs = cbRefMap.keySet();
				for (String cbRef : cbRefs) {
					if (apIedName.equals(iedName) && !pubCbs.contains(cbRef)) {
						Object[] cbDs = cbRefMap.get(cbRef);
						Element cbAp = (Element) cbDs[0];
						boolean isGse = cbAp.element("GSE")!=null;
						EnumCtrlBlock block = isGse ? EnumCtrlBlock.GSEControl : EnumCtrlBlock.SampledValueControl;
						checkDataSet(apIedName, block, cbRef, false);
					}
				}
			}
		}
		
		// 检查找不到外部虚端子的关联
		if (inputsCache.size() > 0) {
			for (Entry<String, List<Element>> entry : inputsCache.entrySet()) {
				String outFullRef = entry.getKey();
				List<Element> extRefs = entry.getValue();
				if (extRefs!=null && extRefs.size() > 0) {
					Element extRef = extRefs.get(0);
					String extIedName = extRef.attributeValue("iedName");
					Element iedNode = iedCache.get(extIedName);
					boolean exists = false;
					if (iedNode != null) {
						String prefix = DOM4JNodeHelper.getAttribute(extRef, "prefix"); 
						String exlnClass = DOM4JNodeHelper.getAttribute(extRef, "lnClass"); 
						String exlnInst = DOM4JNodeHelper.getAttribute(extRef, "lnInst"); 
						String atts = SCL.getLNAtts(prefix, exlnClass, exlnInst) + "[@doName='" + extRef.attributeValue("doName") +
								"']" + SCL.getDaAtts(extRef.attributeValue("daName"));
						exists = DOM4JNodeHelper.existsNode(iedNode, "./AccessPoint/Server/LDevice/*/DataSet/FCDA" + atts);
					}
					String msg = exists ? "控制块配置错误" : "外部虚端子不存在";
					addError(iedName, "虚端子关联", outFullRef, "无法接收，" + msg);
				} 
			}
		}
		
		boolean success = true;
		for (Problem p : problems) {
			if (p.getLevel() == LEVEL.ERROR) {
				success = false;
				break;
			}
		}
		return success;
	}
	
	/**
	 * 缓存IED中LN实例和当前装置Inputs，为实例化检查做准备
	 * @param iedNode
	 */
	@SuppressWarnings("unchecked")
	private void cacheIedParts(Element iedNode) {
		String cacheIed = iedNode.attributeValue("name");
		List<Element> elLDs = DOM4JNodeHelper.selectNodes(iedNode, "./AccessPoint/Server/LDevice");
		Map<String, Element> intAddrMap = new HashMap<String, Element>();
		for (Element elLD : elLDs) {
			String ldInst = elLD.attributeValue("inst");
			List<Element> elLNs = DOM4JNodeHelper.selectNodes(elLD, "./*[name()='LN0' or name()='LN']");
			String modelDI = new String();// 记录模型一致性检查
			for (Element elLN : elLNs) {
				String lntype = elLN.attributeValue("lnType");
				String lnName = ldInst + "/" + SCL.getLnName(elLN);
				Map<String, Element> iedLNMap = lnMap.get(cacheIed);
				if (iedLNMap == null) {
					iedLNMap = new HashMap<String, Element>();
					lnMap.put(cacheIed, iedLNMap);
				}
				iedLNMap.put(lnName, elLN); // 暂不考虑重复的LN
				if (!iedName.equals(cacheIed))	// 非当前装置，直接跳过
					continue;
				
				if (!lnTypeMap.containsKey(lntype)) {
					addError(cacheIed, "LN实例化", lnName, "数据模板中未定义其类型 " + lntype);
				} else {
					// LN子元素实例化检查
					DOIInstChecker checker = new DOIInstChecker(iedName, lnName, lntype, lnTypeMap.get(lntype), problems);
					checker.checkSubRefs("", elLN);
					modelDI += checker.getErrRefs();
				}
				// 缓存当前装置Inputs
				if ("LN0".equals(elLN.getName())) {
					Element elInputs = elLN.element("Inputs");
					if (elInputs != null) {
						List<Element> extrefs = elInputs.elements("ExtRef");
						inputs.addAll(extrefs);
						for (Element extref : extrefs) {
							String intAddr = extref.attributeValue("intAddr");
							String empMsg = ModelCheckerNew.checkProp(intAddr);
							if (!StringUtil.isEmpty(empMsg)) {	// 检查是否为空
								addError(cacheIed, "虚端子关联", SCL.getNodeFullRef(extref), "内部虚端子" + empMsg);
							} else {
								// 检查重复关联
								if (intAddrMap.containsKey(intAddr)) {
									addError(cacheIed, "虚端子关联", intAddr, "存在重复关联");
								} else {
									if (!intAddr.contains(":")) {
										addWarning(cacheIed, "虚端子关联", intAddr, "物理端口号未配置");
									}
									intAddrMap.put(intAddr, extref);
								}
								// 缓存关联关系
								String nodeFullRef = SCL.getNodeFullRef(extref);
								List<Element> intAddrs = inputsCache.get(nodeFullRef);
								if (intAddrs == null) {
									intAddrs = new ArrayList<>();
									inputsCache.put(nodeFullRef, intAddrs);
								}
								intAddrs.add(extref);
							}
						}
					}
				}
			}
			if (modelDI.length() > 0)
				addWarning(cacheIed, "LN实例化顺序", ldInst, "LN实例化排序与模型排序不一致。 ", modelDI);
		}
	}
	
	/**
	 * 检查控制块属性为空/缺失/重复/dataSet引用，并返回控制块定义节点
	 */
	private void checkCBDefines() {
		// 检查当前装置CB定义
		pubsCache.putAll(ModelCheckerNew.checkInnerCtrl(iedNode, commNode, problems));
		for (Element extref : inputs) {
			String extied = extref.attributeValue("iedName");
			if (iedCache.containsKey(extied))
				continue;
			String intAddr = extref.attributeValue("intAddr");
			if (StringUtil.isEmpty(extied)) {
				addError(iedName, "虚端子关联", intAddr, "外部虚端子未配置");
			} else {
				Element extiedNode = IEDDAO.getIEDNode(extied);
				if (extiedNode != null) {	// IED不存在时不增加错误，避免后续处理重复
					iedCache.put(extied, extiedNode);
					// 缓存LN及类型
					cacheIedParts(extiedNode);
					// 检查其它装置CB定义
					pubsCache.putAll(ModelCheckerNew.checkInnerCtrl(extiedNode, commNode, problems));
				}
			}
		}
	}
	
	/**
	 * 检查控制块通信参数设置及重复情况（包括当前及其关联装置）
	 */
	@SuppressWarnings("unchecked")
	private void checkCBComms() {
		Map<String, String> cbRefMap = new HashMap<>();
		List<Element> subNets = commNode.elements("SubNetwork");
		for (Element subNet : subNets) {
			String netName = subNet.attributeValue("name");
			List<Element> aps = subNet.elements("ConnectedAP");
			for (Element ap : aps) {
				String apIedName = ap.attributeValue("iedName");
				if (!iedCache.containsKey(apIedName))
					continue;
				List<Element> cbs = ap.elements();
				for (Element cb : cbs) {
					String ldInst = cb.attributeValue("ldInst");
					String cbName = cb.attributeValue("cbName");
					EnumCtrlBlock block = EnumCtrlBlock.getByCbName(cb.getName());
					if (block == null)
						continue;
					String cbRef = SCL.getCbRef(apIedName, ldInst, cbName, block);
					// 检查通信参数正确性
					boolean isCurrentIed = iedName.equals(apIedName);
					ModelCheckerNew.checkCbProps(apIedName, cb, isCurrentIed ? LEVEL.ERROR : LEVEL.WARNING, problems);
					// 检查控制块定义是否存在
					if (!checkCommCb(apIedName, block, cbRef))
						continue;
					if (!checkDataSet(apIedName, block, cbRef, true))
						continue;
					if (!inputsCbCache.values().contains(cbRef) && !isCurrentIed)
						continue;
					// 检查CB是否重复配置
					String netName1 = cbRefMap.get(cbRef);
					if (netName1 != null) {
						String msg = netName1.equals(netName) ? 
								"在子网中 " + netName + " 被重复定义" 
								: "在子网 " + netName + " 和 " + netName1 + " 中被重复定义";
						addError(apIedName, block.getDesc(), cbRef, msg);
						continue;
					} else {
						cbRefMap.put(cbRef, netName);
					}
					// 缓存通信配置
					List<Element> pEls = ap.elements("Private");
					List<Element> phyEls = ap.elements("PhysConn");
					Element cbAp = DOM4JNodeHelper.createSCLNode("ConnectedAP");
					DOM4JNodeHelper.copyAttributes(ap, cbAp);
					cbApCache.put(cbRef, cbAp);
					for (Element pEL : pEls) {
						cbAp.add(pEL.createCopy()); // 可能存在多个CB公用一个ConnectedAP的<Private>
					}
					cbAp.add(cb.createCopy());
					for (Element phyEl : phyEls) {
						cbAp.add(phyEl.createCopy()); // 可能存在多个CB公用一个ConnectedAP的<PhysConn>
					}
				} // end of cb
			} // end of ap
		} // end of net
	}
	
	// 检查控制块定义是否存在
	private boolean checkCommCb(String apIedName, EnumCtrlBlock block, String cbRef) {
		Map<String, Object[]> apMap = pubsCache.get(apIedName);
		Object[] cbds = (apMap!=null) ? apMap.get(cbRef) : null;
		if (cbds == null) {
			addError(apIedName, block.getDesc(), cbRef.substring(apIedName.length()), "在SCD中找不到对应的控制块");
			return false;
		}
		
		// pub cbs 存根
		if (apIedName.equals(iedName) && !pubCbs.contains(cbRef)) {
			pubCbs.add(cbRef);
		}
		
		return true;
	}

	/**
	 * 检查控制块数据集
	 * @param apIedName
	 * @param block
	 * @param cbRef
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean checkDataSet(String apIedName, EnumCtrlBlock block, String cbRef, boolean hasComm) {
		Map<String, Object[]> apMap = pubsCache.get(apIedName);
		Object[] cbds = (apMap!=null) ? apMap.get(cbRef) : null;
		if (cbds == null) {
			return false;
		}
		// 检查数据集及数据项是否实例化，并缓存外部fcda
		Element dsEl = (Element) cbds[1];
		if (dsEl == null)
			return false;
		String dsName = dsEl.attributeValue("name");
		List<Element> fcdas = dsEl.elements("FCDA");
		for (Element fcda : fcdas) {
			String doName = fcda.attributeValue(SCL.FCDA_DONAME);
			String daName = fcda.attributeValue(SCL.FCDA_DANAME);
			String fcdaRef = SCL.getNodeRef(fcda);
			String lnName = fcda.attributeValue("ldInst") + "/" + SCL.getLnNameInFCDA(fcda);
			Element fcdaLN = lnMap.get(apIedName).get(lnName);
			String lntype = (fcdaLN==null) ? null : fcdaLN.attributeValue("lnType");
			boolean isSV = StringUtil.isEmpty(daName);
			if (lntype == null) {
				addError(apIedName, "数据集" + dsName, fcdaRef, "在SCD中找不到对应的逻辑节点");
			} else {
				// 为fcda添加bType
				Map<String, Object[]> outDodaTypeMap = lnTypeMap.get(lntype);
				String outDodaName = isSV ? doName : (doName + "." + daName);
				String outDodaType = null;
				if (outDodaTypeMap != null) {
					Object[] dodatype = outDodaTypeMap.get(outDodaName);
					if (dodatype == null || dodatype.length == 0) {
						addError(apIedName, "数据集" + dsName, fcdaRef, "数据模板 " + lntype + " 中不存在 " + outDodaName);
					} else {
						outDodaType = (String) dodatype[0];
						if (outDodaType == null) {
							addError(apIedName, "数据集" + dsName, fcdaRef, "数据模板 " + lntype + " 中不存在 " + outDodaName);
						} else {
							if ("Struct".equals(outDodaType)) {
								addWarning(apIedName, "数据集" + dsName, fcdaRef, "数据集 " + dsName + " 中结构体不应作为<DAI>，类型 " + lntype);
							} else if (!isSV) {
								fcda.addAttribute("bType", outDodaType);
							}
						}
					}
				} else {
					addError(apIedName, "数据集" + dsName, fcdaRef, "无此逻辑节点类型 " + lntype);
				}
				String doxpath = "." + SCL.getDOXPath(doName);
				Element doEl = DOM4JNodeHelper.selectSingleNode(fcdaLN, doxpath);
				// 为fcda添加desc
				String desc = (doEl==null) ? "" : doEl.attributeValue("desc", "");
				fcda.addAttribute("desc", desc);
				// 为fcda添加<DOI>,<DAI>,<intAddr>
				if (apIedName.equals(iedName)) {	// 发布
					if (isSV) {
						if (doEl != null) {
							Element doi = doEl.createCopy();
							CRCInfoDao.clearDesc(doi);
							fcda.add(doi);
						} else {
							addWarning(apIedName, "数据集" + dsName, fcdaRef, "数据集 " + dsName + " 中使用了，但未实例化");
						}
					} else {
						Element daEl = DOM4JNodeHelper.selectSingleNode(fcdaLN, doxpath + SCL.getDOXPath(daName));
						if (daEl != null) { // 有些装置的q、t不做实例化
							Element dai = daEl.createCopy();
							CRCInfoDao.clearDesc(dai);
							fcda.add(dai);
						} else {
							addWarning(apIedName, "数据集" + dsName, fcdaRef, "数据集 " + dsName + " 中使用了，但未实例化");
						}
					}
				} else {							// 订阅
					String outFullRef = apIedName + fcdaRef;
					List<Element> extrefs = inputsCache.get(outFullRef);
					if (extrefs != null && extrefs.size()>0) { // 存在关联
						inputsCache.remove(outFullRef);
						inputsCbCache.put(outFullRef, cbRef);
						if (!hasComm) {
							addError(iedName, "虚端子关联", outFullRef, "无法接收，通信参数未设置 " + cbRef);
						} else {
							for (Element extref : extrefs) { // 一个外部信号可被多个内部虚端子接收
								Element intAddrEl = fcda.addElement("intAddr");
								String intAddr = extref.attributeValue("intAddr");
								if (StringUtil.isEmpty(intAddr)) {
									addError(iedName, "虚端子关联", outFullRef, "内部虚端子" + ModelCheckerNew.checkProp(intAddr));
								} else {
									int pm = intAddr.indexOf(':'); // 内部虚端子前面可能有物理端口
									if (pm > 0)
										intAddr = intAddr.substring(pm + 1);
									String[] intInfo = intAddr.split("\\.");
									if (intInfo.length < 2) {
										addError(iedName, "虚端子关联", intAddr, "内部虚端子不完整");
									} else {
										boolean misMatch = false;
										if (isSV) {
											if (intInfo.length > 2) { // 外DO，内DA
												misMatch = true;
												addError(iedName, "虚端子关联", outFullRef, "外部的DO不能关联到内部的DA虚端子上");
											} else if (intInfo.length < 3 && EnumCtrlBlock.GSEControl==block) { // Goose信号，外DO，内DO
												misMatch = true;
												addError(iedName, "虚端子关联", outFullRef, block.getDesc() + "必须关联到DA虚端子上");
											}
										} else if (intInfo.length < 3) {	// 外DA，内DO
											misMatch = true;
											addError(iedName, "虚端子关联", outFullRef, "外部的DA不能关联到内部的DO虚端子上");
										}
										// 类型检查
										if (!misMatch) {
											int p = intAddr.indexOf('.');
											String inLnName = intInfo[0];
											String inDoName = intInfo[1];
											Element inLN = lnMap.get(iedName).get(inLnName);
											String inDodaName = intAddr.substring(p + 1);
											if (inLN == null) {
												addError(iedName, "虚端子关联", intAddr, "在SCD中找不到对应的逻辑节点");
											} else {
												// 类型匹配检查
												String inLnType = inLN.attributeValue("lnType");
												Map<String, Object[]> dodaTypeMap = lnTypeMap.get(inLnType);
												String inDodaType = null;
												if (dodaTypeMap != null) {
													inDodaType = (String) (dodaTypeMap.get(inDodaName)==null ? null : dodaTypeMap.get(inDodaName)[0]);
													if (inDodaType == null) {
														addError(iedName, "虚端子关联", intAddr, "数据模板 " + inLnType + " 中不存在 " + inDodaName);
													} else {
//														if (outDodaType!= null && !outDodaType.equals(inDodaType)) {
														if (!SCTProperties.getInstance().isSameType(inDodaType, outDodaType)) {
															misMatch = true;
															addError(iedName, "虚端子关联", intAddr, "外部虚端子 " + outDodaType + 
																	" 与内部虚端子 " + inDodaType + " 类型不一致");
														}
														Element dodaEl = DOM4JNodeHelper.selectSingleNode(inLN, "." + SCL.getDOXPath(inDodaName));
														if (dodaEl == null) {
															addWarning(iedName, "虚端子关联", intAddr, "内部虚端子未实例化");
														}
													}
												} else {
													addError(iedName, "虚端子关联", intAddr, "无此逻辑节点类型 " + inLnType);
												}
											}
											// 添加intAddr<DOI>,<DAI>
											if (!misMatch && inLN != null) {
												Element indoEl = DOM4JNodeHelper.selectSingleNode(inLN, "./*[@name='" + inDoName + "']");
												if (indoEl != null) {
													String indesc = indoEl.attributeValue("desc");
													intAddrEl.addAttribute("desc", indesc);
													intAddrEl.addAttribute("name", extref.attributeValue("intAddr"));
													if (intInfo.length == 2) {
														Element doi = indoEl.createCopy();
														CRCInfoDao.clearDesc(doi);
														intAddrEl.add(doi);
													} else {
														Element daEl = DOM4JNodeHelper.selectSingleNode(inLN, "." + SCL.getDOXPath(inDodaName));
														if (daEl != null) { // 有些装置的q、t不做实例化
															Element dai = daEl.createCopy();
															CRCInfoDao.clearDesc(dai);
															intAddrEl.add(dai);
														}
													}
												}
											}
										} // 类型检查，添加intAddr<DOI>,<DAI>结束
									}
								}
							}
						}
					} else { // 没有关联
						if (hasComm) {
							Element intAddrEl = fcda.addElement("intAddr");
							intAddrEl.addAttribute("desc", "");
							intAddrEl.addAttribute("name", "NULL");
						}
					}
				}
			}
		}
		return true;
	}
	
	public Document createCcdDom() {
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
		
		Element[] parts = new Element[] {gpRoot, spRoot, gsRoot, ssRoot};
		for (Element part : parts) {
			if (part.elements().size()<1)
				ccdRoot.remove(part);
		}
		
		return ccdDom;
	}

	private void fillPubs(Element gpRoot, Element spRoot) {
		for (String cbRef : pubCbs) {
			Element cbAp = cbApCache.get(cbRef);
			if (cbAp == null)
				continue;
			String iedName = cbAp.attributeValue("iedName");
			boolean isGse = cbAp.element("GSE")!=null;
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
		final Map<String, Object[]> apMap = pubsCache.get(iedName);
		Comparator<Element> comp = new Comparator<Element>() {
			@Override
			public int compare(Element o1, Element o2) {
				String cbRef1 = o1.attributeValue("name");
				String cbRef2 = o2.attributeValue("name");
				Integer cbIdx1 = (Integer) apMap.get(cbRef1)[2];
				Integer cbIdx2 = (Integer) apMap.get(cbRef2)[2];
				return cbIdx1.compareTo(cbIdx2);
			}};
		java.util.Collections.sort(pubList, comp);
		elPub.clearContent();
		for (Element pub : pubList) {
			elPub.add(pub.createCopy());
		}
	}

	private void fillSubs(Element gsRoot, Element ssRoot) {
		Map<String, Element> map = new HashMap<String, Element>();
		for (Element extref : inputs) {
			String outFullRef = SCL.getNodeFullRef(extref);
			String cbRef = inputsCbCache.get(outFullRef);
			Element cbAp = cbApCache.get(cbRef);
			String iedName = cbAp.attributeValue("iedName");
			// 去重
			if (map.containsKey(cbRef)) {
				continue;
			} else {
				map.put(cbRef, cbAp);
			}
			if (cbAp!=null) {
				boolean isGse = cbAp.element("GSE")!=null;
				Element elSub = isGse ? gsRoot : ssRoot;
				String ndName = getCBNodeName(isGse);
				fillCbDs(iedName, cbRef, elSub, ndName, true);
			}
		}
	}

	private String getCBNodeName(boolean isGse) {
		return isGse ? "GOCBref" : "SMVCBref";
	}

	private void fillCbDs(String iedName, String cbRef, Element elPubSub, String ndName, boolean isSub) {
		Element elCBref = elPubSub.addElement(ndName);
		elCBref.addAttribute("name", cbRef);
		Map<String, Object[]> apMap = pubsCache.get(iedName);
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
	
	private void clearNotNeed(boolean isSub, Element...els) {
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

	public List<Problem> getProblems() {
		java.util.Collections.sort(problems, new Comparator<Problem>(){
			@Override
			public int compare(Problem p1, Problem p2) {
				if (p1.getLevel() == p2.getLevel()) {
					return p1.getSubType().compareTo(p2.getSubType());
				} else {
					return (p1.getLevel()==LEVEL.ERROR) ? -1 : 1;
				}
			}});
		return problems;
	}
	
}
