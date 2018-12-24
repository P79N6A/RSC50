package com.synet.tool.rsc.compare.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.check.ModelCheckerNew;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.ied.NetConfig;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class IedCompareParser {

	private Element iedNd;
	private Context context;
	private String iedName;
	private Tb1046IedEntity ied;
	// LN 映射
	private Map<String, Element> iedLNMap = new HashMap<String, Element>();
	// extref 缓存
	private List<Element> elExtRefAll = new ArrayList<>();
		
	private IedDomHelper domHelper;
	private String path;
	
	public IedCompareParser(Element iedNd, Context context) {
		this.iedNd = iedNd;
		this.context = context;
		this.iedName = iedNd.attributeValue("name");
		this.domHelper = new IedDomHelper(iedName, context);
		if (iedName.endsWith("_old") || iedName.endsWith("_new")) {
			this.iedName = iedName.substring(0, iedName.length() - 4);
		}
	}
	
	private void createIed() {
		this.ied = new Tb1046IedEntity();
		ied.setF1046Name(iedName);
		ied.setF1046Desc(iedNd.attributeValue("desc"));
		ied.setF1046Model(iedNd.attributeValue("type"));
		ied.setF1046Manufacturor(iedNd.attributeValue("manufacturer"));
		ied.setF1046ConfigVersion(iedNd.attributeValue("configVersion"));
		String vtcrc = iedNd.attributeValue("crc");
		ied.setF1046Crc(vtcrc);
		// A/B
		int aOrb = iedName.endsWith("B") ? 2 : 1;
		ied.setF1046AorB(aOrb);
		ied.setF1046IsVirtual(0);
		ied.setF1046boardNum(0);
		
		boolean existsS1 = DOM4JNodeHelper.existsNode(iedNd, "./scl:AccessPoint[@name='S1']");
		boolean existsProt = DOM4JNodeHelper.existsNode(iedNd, "./scl:AccessPoint/scl:Server/scl:LDevice[@inst='PROT']");
		if (existsS1) {
			int type = existsProt ? DBConstants.IED_PROT : DBConstants.IED_MONI;
			ied.setF1046Type(type);
		}
		NetConfig netConfig = context.getNetConfig(iedName + ".MMS");
		if (netConfig != null) {
			ied.setF1046aNetIp(netConfig.getIpA());
			ied.setF1046bNetIp(netConfig.getIpB());
		}
	}
	
	public void parse() {
		createIed();
		Integer f1046Type = ied.getF1046Type();
		boolean isMoni = (f1046Type != null && DBConstants.IED_MONI == f1046Type);
		String ldXpath = "./AccessPoint/Server/LDevice";
		List<Element> elLDs = DOM4JNodeHelper.selectNodes(iedNd, ldXpath);
		for (Element elLd : elLDs) {
			String ldInst = elLd.attributeValue("inst");
			List<Element> elLNs = elLd.elements();
			for (Element elLN : elLNs) {
				if (!isMoni) {
					parsePins(ldInst, elLN);
				}
				String ndName = elLN.getName();
				if ("LN0".equals(ndName)) {
					Element elLN0 = elLN;
					List<Element> elDatSets = elLN0.elements("DataSet");
					for (Element elDat : elDatSets) {
						String datSet = elDat.attributeValue("name");
						if (SclUtil.isSetting(datSet)) {
							domHelper.saveDsSetting(elDat, elLd);
						} else if (SclUtil.isParam(datSet)) {
							domHelper.saveDsParams(elDat, elLd);
						} else {
							Element elRcb = DOM4JNodeHelper.selectSingleNode(elLN0, "./ReportControl[@datSet='" + datSet + "']");
							Element elGoose = DOM4JNodeHelper.selectSingleNode(elLN0, "./GSEControl[@datSet='" + datSet + "']");
							Element elSmv = DOM4JNodeHelper.selectSingleNode(elLN0, "./SampledValueControl[@datSet='" + datSet + "']");
							if (elRcb != null) {
								domHelper.saveRcbs(elDat, elRcb, elLd);
							} else if (elGoose != null && !isMoni) {
								domHelper.saveGoose(elDat, elGoose, elLd);
							} else if (elSmv != null && !isMoni) {
								domHelper.saveSmv(elDat, elSmv, elLd);
							} else {
								SCTLogger.info("装置 " + iedName + " 未被识别的数据集 " + datSet + " 。");
							}
						}
					}
					Element inputs = elLN0.element("Inputs");
					if (inputs != null) {
						List<Element> elExtRefs = inputs.elements("ExtRef");
						if (elExtRefs != null && elExtRefs.size()>0) {
							elExtRefAll.addAll(elExtRefs);
						}
					}
				}
				String lnName = ldInst + "/" + SCL.getLnName(elLN);
				iedLNMap.put(lnName, elLN);
			}
		}
		// 分析虚端子
		parseInputs();
		domHelper.saveIED(ied);
		this.path = domHelper.saveDom();
	}
	
	public String getDomPath() {
		return path;
	}
	
	public Element getIedNode() {
		return domHelper.getDomRoot();
	}
	
	private void parsePins(String ldInst, Element elLN) {
		String prefix = elLN.attributeValue("prefix");
		String lnName = SCL.getLnName(elLN);
		List<Element> dois = elLN.elements("DOI");
		if ("GOIN".equals(prefix)) {
			for (Element doi : dois) {
				addPin(ldInst, lnName, doi, "ST");
			}
		} else if ("SVIN".equals(prefix)) {
			for (Element doi : dois) {
				addPin(ldInst, lnName, doi, "MX");
			}
		}
	}
	
	private void addPin(String ldInst, String lnName, Element doi, String fc) {
		String doName = doi.attributeValue("name");
		if (isNullDOI(doName)) {
			return;
		}
		String doDesc = doi.attributeValue("desc");
		String pinRef = ldInst + "/" + lnName + "$" + fc + "$" + doName;
//		String pinAddr = ldInst + "/" + lnName + "." + doName;
		if ("ST".equals(fc)) {
			pinRef += (doName.startsWith("AnIn") ? "$mag$f" : "$stVal");
//			pinAddr += (doName.startsWith("AnIn") ? ".mag.f" : ".stVal");
		}
		domHelper.savePins(pinRef, doDesc);
	}
	
	private boolean isNullDOI(String doName) {
		return "Mod".equals(doName) || "Beh".equals(doName) || "Health".equals(doName) || "NamPlt".equals(doName);
	}

	private void parseInputs() {
		for (Element extref : elExtRefAll) { // 一个外部信号可被多个内部虚端子接收
			String intAddr = extref.attributeValue("intAddr");
			String extIedName = extref.attributeValue("iedName");
			String fcdaRef = SCL.getNodeRef(extref);
			String outFullRef = extIedName + fcdaRef;
			if (StringUtil.isEmpty(intAddr)) {
				addError(iedName, "虚端子关联", outFullRef , "内部虚端子" + ModelCheckerNew.checkProp(intAddr));
			} else {
				int pm = intAddr.indexOf(':'); // 内部虚端子前面可能有物理端口
				if (pm > 0)
					intAddr = intAddr.substring(pm + 1);
				String[] intInfo = intAddr.split("\\.");
				if (intInfo.length < 2) {
					addError(iedName, "虚端子关联", intAddr, "内部虚端子不完整");
				} else {
					int p = intAddr.indexOf('.');
					String inLnName = intInfo[0];
					String inDoName = intInfo[1];
					Element inLN = iedLNMap.get(inLnName);
					String inDodaName = intAddr.substring(p + 1);
					if (inLN == null) {
						addError(iedName, "虚端子关联", intAddr, "在SCD中找不到对应的逻辑节点");
					} else {
						// 类型匹配检查
						String inLnType = inLN.attributeValue("lnType");
						Map<String, Object[]> dodaTypeMap = context.getLnTypeMap().get(inLnType);
						String inDodaType = null;
						if (dodaTypeMap != null) {
							inDodaType = (String) (dodaTypeMap.get(inDodaName)==null ? null : dodaTypeMap.get(inDodaName)[0]);
							if (inDodaType == null) {
								addError(iedName, "虚端子关联", intAddr, "数据模板 " + inLnType + " 中不存在 " + inDodaName);
							} else {
//								Element dodaEl = DOM4JNodeHelper.selectSingleNode(inLN, "." + SCL.getDOXPath(inDodaName));
//								if (dodaEl == null) {
//									addWarning(iedName, "虚端子关联", intAddr, "内部虚端子未实例化");
//								}
							}
						} else {
							addError(iedName, "虚端子关联", intAddr, "无此逻辑节点类型 " + inLnType);
						}
					}
					// 添加intAddr<DOI>,<DAI>
					if (inLN != null) {
						Element indoEl = DOM4JNodeHelper.selectSingleNode(inLN, "./*[@name='" + inDoName + "']");
						if (indoEl != null) {
							String indesc = indoEl.attributeValue("desc");
//							context.addVTLink(iedName, indesc + "," + intAddr, extIedName + "," + fcdaRef);
							domHelper.saveExtRef(indesc, intAddr, extIedName, fcdaRef);
						}
					}
				}
			}
		}
	}
	
	private void addError(String iedName, String subType, String ref, String desc) {
		context.addError(iedName, subType, ref, desc);
	}
	
	public Tb1046IedEntity getIed() {
		return ied;
	}

}
