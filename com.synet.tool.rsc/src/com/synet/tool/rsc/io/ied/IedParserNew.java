package com.synet.tool.rsc.io.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.check.ModelCheckerNew;
import com.shrcn.business.scl.das.CIDDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.parser.IedParserBase;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.io.scd.IedInfoDao;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class IedParserNew {

	private Element iedNd;
	private Context context;
	private IProgressMonitor monitor;
	private String iedName;
	private Tb1046IedEntity ied;
	private List<Tb1054RcbEntity> rcbs = new ArrayList<>();
	private List<Tb1055GcbEntity> gses = new ArrayList<>();
	private List<Tb1056SvcbEntity> smvs = new ArrayList<>();
	private List<Tb1006AnalogdataEntity> agls = new ArrayList<>();
	private List<Tb1016StatedataEntity> sts = new ArrayList<>();
	private List<Tb1062PinEntity> pins = new ArrayList<>();
	// LN 映射
	private Map<String, Element> iedLNMap = new HashMap<String, Element>();
	// extref 缓存
	private List<Element> elExtRefAll = new ArrayList<>();
	// 间隔缓存
	private Map<String, Tb1042BayEntity> bayCache = new HashMap<>();
		
	private RSCProperties rscp = RSCProperties.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private IedEntityService iedServ = new IedEntityService();
	private SubstationService staServ = new SubstationService();
	
	public IedParserNew(Element iedNd, Context context, IProgressMonitor monitor) {
		super();
		this.iedNd = iedNd;
		this.context = context;
		this.monitor = monitor;
		this.iedName = iedNd.attributeValue("name");
	}
	
	private void createIed() {
		this.ied = new Tb1046IedEntity();
		String iedName = iedNd.attributeValue("name");
		ied.setF1046Name(iedName);
		ied.setF1046Desc(iedNd.attributeValue("desc"));
		ied.setF1046Model(iedNd.attributeValue("type"));
		ied.setF1046Manufacturor(iedNd.attributeValue("manufacturer"));
		ied.setF1046ConfigVersion(iedNd.attributeValue("configVersion"));
		String vtcrc = DOM4JNodeHelper.getNodeValueByXPath(iedNd, "./Private[@type='IED virtual terminal conection CRC']");
		ied.setF1046Crc(vtcrc);
		// code
		String iedCode = rscp.nextTbCode(DBConstants.PR_IED);
		ied.setF1046Code(iedCode);
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
		beanDao.insert(ied);
		iedServ.addCommState(ied);
		iedServ.addCRCData(ied);
	}
	
	public void parse() {
		createIed();
		if (monitor != null) {
			monitor.setTaskName("正在导入装置" + iedName);
		}
		Integer f1046Type = ied.getF1046Type();
		boolean isMoni = (f1046Type != null && DBConstants.IED_MONI == f1046Type);
		String ldXpath = "./AccessPoint/Server/LDevice";
		List<Element> elLDs = DOM4JNodeHelper.selectNodes(iedNd, ldXpath);
		for (Element elLD : elLDs) {
			String ldInst = elLD.attributeValue("inst");
			List<Element> elLNs = elLD.elements();
			for (Element elLN : elLNs) {
				if (!isMoni) {
					parsePins(ldInst, elLN);
				}
				String ndName = elLN.getName();
				if ("LN0".equals(ndName)) {
					Element elLN0 = elLN;
					List<Element> elDatSets = elLN0.elements("DataSet");
					for (Element elDatSet : elDatSets) {
						String datSet = elDatSet.attributeValue("name");
						if (SclUtil.isSetting(datSet)) {
							createSgcb(datSet, elDatSet, elLD);
						} else if (SclUtil.isParam(datSet)) {
							createSpcb(datSet, elDatSet, elLD);
						} else {
							Element elRcb = DOM4JNodeHelper.selectSingleNode(elLN0, "./ReportControl[@datSet='" + datSet + "']");
							Element elGoose = DOM4JNodeHelper.selectSingleNode(elLN0, "./GSEControl[@datSet='" + datSet + "']");
							Element elSmv = DOM4JNodeHelper.selectSingleNode(elLN0, "./SampledValueControl[@datSet='" + datSet + "']");
							if (elRcb != null) {
								createRcb(datSet, elDatSet, elRcb, elLD);
							} else if (elGoose != null && !isMoni) {
								createGoose(datSet, elDatSet, elGoose, elLD);
							} else if (elSmv != null && !isMoni) {
								createSMV(datSet, elDatSet, elSmv, elLD);
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
		beanDao.insertBatch(agls);
		beanDao.insertBatch(sts);
		beanDao.insertBatch(pins);
		// 分析虚端子
		parseInputs();
		// 根据解析结果修改装置类型和间隔类型，增加保护测控IP设置
		Tb1042BayEntity bay = null;
		if (getRcbs().size() > 0) {		// 保护测控
			int type = ied.getF1046Type();
			if (type == DBConstants.IED_MONI) {
				bay = getBayByName(DBConstants.BAY_MOT);
			}
			NetConfig netConfig = context.getNetConfig(iedName + ".MMS");
			if (netConfig != null) {
				ied.setF1046aNetIp(netConfig.getIpA());
				ied.setF1046bNetIp(netConfig.getIpB());
				iedServ.addMMSServer(ied);
			}
			String scddir = ProjectManager.getInstance().getProjectCidPath();
			CIDDAO.export(null, iedName, scddir , ".cid");
		} else {
			if (getSmvs().size() > 0) {			// 合并单元
				ied.setF1046Type(DBConstants.IED_MU);
			} else {										// 智能终端
				ied.setF1046Type(DBConstants.IED_TERM);
			}
		}
		bay = (bay==null) ? getBayByName(DBConstants.BAY_OTHER) : bay;
		ied.setF1042Code(bay.getF1042Code());
		beanDao.update(ied);
		
		if (monitor != null) {
			monitor.worked(1);
		}
	}

	private Tb1042BayEntity getBayByName(String bayName) {
		if (bayCache.containsKey(bayName)) {
			return bayCache.get(bayName);
		}
		Tb1042BayEntity bay = (Tb1042BayEntity) beanDao.getObject(Tb1042BayEntity.class, "f1042Name", bayName);
		if (bay == null) {
			bay = new Tb1042BayEntity();
			bay.setF1042Code(rscp.nextTbCode(DBConstants.PR_BAY));
			bay.setF1042Name(bayName);
			Tb1041SubstationEntity station = staServ.getCurrSubstation();
			if (station != null) {
				bay.setTb1041SubstationByF1041Code(station);
			}
			beanDao.insert(bay);
		}
		bayCache.put(bayName, bay);
		return bay;
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
		String pinAddr = ldInst + "/" + lnName + "." + doName;
		if ("ST".equals(fc)) {
			pinRef += (doName.startsWith("AnIn") ? "$mag$f" : "$stVal");
			pinAddr += (doName.startsWith("AnIn") ? ".mag.f" : ".stVal");
		}
//		Rule type = F1011_NO.getType("", lnName, doName, doDesc, fc);
		Rule type = F1011_NO.OTHERS;
		Tb1062PinEntity pin = ParserUtil.createPin(ied, pinRef, doDesc, type.getId(), 0);
		context.cachePin(iedName + pinAddr, pin);
		pins.add(pin);
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
							context.addVTLink(iedName, indesc + "," + intAddr, extIedName + "," + fcdaRef);
						}
					}
				}
			}
		}
	}
	
	private void createSMV(String datSet, Element elDat, Element cbNd, Element elLd) {
		Tb1056SvcbEntity smv = new Tb1056SvcbEntity();
		String cbCode = rscp.nextTbCode(DBConstants.PR_SVCB);
		String cbName = cbNd.attributeValue("name");
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ParserUtil.getCbRef(iedName, ldInst, cbName, "MS");
		String cbId = ParserUtil.getCbId(ldInst, cbName, "MS");
		smv.setCbCode(cbCode);
		smv.setTb1046IedByF1046Code(ied);
		smv.setCbName(cbName);
		smv.setCbId(cbId);
		smv.setDataset(elDat.attributeValue("name"));
		smv.setDsDesc(elDat.attributeValue("desc"));
		NetConfig netCfg = context.getNetConfig(cbRef);
		if (netCfg != null) {
			smv.setMacAddr(netCfg.getMACAddr());
			smv.setVlanid(netCfg.getVLANID());
			smv.setVlanPriority(netCfg.getVLANPriority());
			smv.setAppid(netCfg.getAPPID());
		} else {
			context.addProblem(new Problem(0, LEVEL.ERROR, iedName, "SMV", cbRef, "通信参数未定义。"));
		}
		beanDao.insert(smv);
		smvs.add(smv);
		// 状态点
		Tb1016StatedataEntity st = IedParserBase.createStatedata(cbNd.attributeValue("cbRef")+"状态", cbCode, F1011_NO.IED_WRN_SV.getId());
		st.setTb1046IedByF1046Code(ied);
		beanDao.insert(st);
		// 发送虚端子
		parsePOuts(elLd, cbNd, elDat, smv);
	}
	
	private void createGoose(String datSet, Element elDat, Element cbNd, Element elLd) {
		Tb1055GcbEntity gcb = new Tb1055GcbEntity();
		String cbCode = rscp.nextTbCode(DBConstants.PR_GCB);
		String cbName = cbNd.attributeValue("name");
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ParserUtil.getCbRef(iedName, ldInst, cbName, "GO");
		String cbId = ParserUtil.getCbId(ldInst, cbName, "GO");
		gcb.setCbCode(cbCode);
		gcb.setCbName(cbName);
		gcb.setTb1046IedByF1046Code(ied);
		gcb.setCbId(cbId);
		gcb.setDataset(elDat.attributeValue("name"));
		gcb.setDsDesc(elDat.attributeValue("desc"));
		NetConfig netCfg = context.getNetConfig(cbRef);
		if (netCfg != null) {
			gcb.setMacAddr(netCfg.getMACAddr());
			gcb.setVlanid(netCfg.getVLANID());
			gcb.setVlanPriority(netCfg.getVLANPriority());
			gcb.setAppid(netCfg.getAPPID());
		} else {
			context.addError(iedName, "GOOSE", cbRef, "通信参数未定义。");
		}
		beanDao.insert(gcb);
		gses.add(gcb);
		// 状态点
		Tb1016StatedataEntity st = ParserUtil.createStatedata(cbNd.attributeValue("cbRef")+"状态", "", cbCode, ied, F1011_NO.IED_WRN_GOOSE.getId());
		sts.add(st);
		// 发送虚端子
		parsePOuts(elLd, cbNd, elDat, gcb);
	}
	
	private void parsePOuts(Element ldNd, Element cbNd, Element elDat, BaseCbEntity cb) {
//		String datSet = elDat.attributeValue("name");
		List<Element> fcdaEls = elDat.elements("FCDA");
		List<Tb1061PoutEntity> pouts = new ArrayList<>();
		int i = 0;
		for (Element fcdaEl : fcdaEls) {
			Tb1061PoutEntity pout = new Tb1061PoutEntity();
			pout.setF1061Code(rscp.nextTbCode(DBConstants.PR_POUT));
			pout.setTb1046IedByF1046Code(ied);
			pout.setCbEntity(cb);
			pout.setCbCode(cb.getCbCode());
			String ref = SclUtil.getFcdaRef(fcdaEl);
			pout.setF1061RefAddr(ref);
			pout.setF1061Index(i);
			i++;
			String fcdaDesc = getFCDADesc(ldNd, fcdaEl);
			pout.setF1061Desc(fcdaDesc);
			String fc = fcdaEl.attributeValue("fc");
//			String lnName = fcdaEl.attributeValue("lnClass");
//			String doName = fcdaEl.attributeValue("doName");
			String daName = fcdaEl.attributeValue("daName");
			if ("q".equals(daName) || "t".equals(daName)) {
				continue;
			}
//			Rule type = F1011_NO.getType(datSet, lnName, doName, fcdaDesc, fc);
			Rule type = F1011_NO.OTHERS;
			pout.setF1061Type(type.getId());
			if ("ST".equals(fc)) {
				Tb1016StatedataEntity statedata = addStatedata(ref, fcdaDesc, type.getId());
				pout.setDataCode(statedata.getF1016Code());
				pout.setParentCode(statedata.getParentCode());
			} else {
				Tb1006AnalogdataEntity algdata = addAlgdata(ref, fcdaDesc, type.getId());
				String algcode = algdata.getF1006Code();
				pout.setDataCode(algcode);
				pout.setParentCode(algdata.getParentCode());
			}
			context.cachePout(iedName + SCL.getNodeRef(fcdaEl), pout);
			pouts.add(pout);
		}
		beanDao.insertBatch(pouts);
//		for (Tb1061PoutEntity pout : pouts) {
//			try {
//				beanDao.insert(pout);
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//		}
	}
	
	private void createRcb(String datSet, Element elDat, Element elRcb, Element elLd) {
		Tb1054RcbEntity rcb = new Tb1054RcbEntity();
		rcb.setF1054Code(rscp.nextTbCode(DBConstants.PR_RCB));
		rcb.setTb1046IedByF1046Code(ied);
		rcb.setF1054Dataset(datSet);
		rcb.setF1054DsDesc(elDat.attributeValue("desc"));
		boolean isBrcb = IedInfoDao.isBrcb(elRcb);
		int brcb = isBrcb ? 1 : 0;
		rcb.setF1054IsBrcb(brcb);
		String ldInst = elLd.attributeValue("inst");
		String cbName = elRcb.attributeValue("name");
		String cbId = ParserUtil.getCbId(ldInst, cbName, isBrcb ? "BR" : "RP");
		rcb.setF1054Rptid(cbId);
		rcb.setF1054CbType(IedInfoDao.getRcbType(datSet));
		beanDao.insert(rcb);
		rcbs.add(rcb);
		List<Element> elFcdas = elDat.elements("FCDA");
		List<Tb1058MmsfcdaEntity> mmsFcdaList = new ArrayList<>();
		rcb.setTb1058MmsfcdasByF1054Code(mmsFcdaList);
		int i = 0;
		for (Element fcdaEl : elFcdas) {
			String fcdaDesc = getFCDADesc(elLd, fcdaEl);
			Tb1058MmsfcdaEntity mmsFcda = new Tb1058MmsfcdaEntity();
			mmsFcdaList.add(mmsFcda);
			mmsFcda.setF1058Code(rscp.nextTbCode(DBConstants.PR_FCDA));
			mmsFcda.setTb1046IedByF1046Code(ied);
			mmsFcda.setTb1054RcbByF1054Code(rcb);
			mmsFcda.setF1058Index(i);
			mmsFcda.setF1058Desc(fcdaDesc);
			String ref = SclUtil.getFcdaRef(fcdaEl);
			mmsFcda.setF1058RefAddr(ref);
			String fc = fcdaEl.attributeValue("fc");
//			String lnName = fcdaEl.attributeValue("lnClass");
//			String doName = fcdaEl.attributeValue("doName");
//			Rule type = F1011_NO.getType(datSet, lnName, doName, fcdaDesc, fc);
			Rule type = F1011_NO.OTHERS;
			mmsFcda.setF1058Type(type.getId());
			if ("ST".equals(fc)) {
				mmsFcda.setF1058DataType(DBConstants.DATA_ST);
				Tb1016StatedataEntity statedata = addStatedata(ref, fcdaDesc, type.getId());
				mmsFcda.setDataCode(statedata.getF1016Code());
				mmsFcda.setParentCode(statedata.getParentCode());
			} else {
				mmsFcda.setF1058DataType(DBConstants.DATA_MX);
				Tb1006AnalogdataEntity algdata = addAlgdata(ref, fcdaDesc, type.getId());
				mmsFcda.setDataCode(algdata.getF1006Code());
				mmsFcda.setParentCode(algdata.getParentCode());
			}
			i++;
		}
		beanDao.insertBatch(mmsFcdaList);
	}
	
	private void createSgcb(String datSet, Element elDat, Element elLd) {
		Tb1057SgcbEntity sgcb = new Tb1057SgcbEntity();
		sgcb.setF1057Code(rscp.nextTbCode(DBConstants.PR_SGCB));
		sgcb.setTb1046IedByF1046Code(ied);
		String cbName = "SGCB";
		String ldInst = elLd.attributeValue("inst");
		String cbRef = ldInst + "/LLN0$SP$SGCB";
		sgcb.setF1057CbName(cbName);
		sgcb.setF1057CbRef(cbRef);
		sgcb.setF1057Dataset(datSet);
		sgcb.setF1057DsDesc(elDat.attributeValue("desc"));
		beanDao.insert(sgcb);
		List<Element> elFcdas = elDat.elements("FCDA");
		List<Tb1059SgfcdaEntity> sgFcdaList = new ArrayList<>();
		sgcb.setTb1059SgfcdasByF1057Code(sgFcdaList);
		int i = 0;
		for (Element fcdaEl : elFcdas) {
			String fcdaDesc = getFCDADesc(elLd, fcdaEl);
			Tb1059SgfcdaEntity sgFcda = new Tb1059SgfcdaEntity();
			sgFcdaList.add(sgFcda);
			sgFcda.setF1059Code(rscp.nextTbCode(DBConstants.PR_SG));
			sgFcda.setTb1057SgcbByF1057Code(sgcb);
			sgFcda.setF1059Index(i);
			sgFcda.setF1059Desc(fcdaDesc);
			sgFcda.setF1059RefAddr(SclUtil.getFcdaRef(fcdaEl));
			sgFcda.setF1059DataType(context.getBType(elLd, fcdaEl));
			i++;
		}
		beanDao.insertBatch(sgFcdaList);
	}
	
	private void createSpcb(String datSet, Element elDat, Element elLd) {
		List<Tb1060SpfcdaEntity> spFcdaList = new ArrayList<>();
		List<Element> elFcdas = elDat.elements("FCDA");
		int i = 0;
		for (Element fcdaEl : elFcdas) {
			String fcdaDesc = getFCDADesc(elLd, fcdaEl);
			Tb1060SpfcdaEntity sgFcda = new Tb1060SpfcdaEntity();
			sgFcda.setF1060Code(rscp.nextTbCode(DBConstants.PR_SP));
			sgFcda.setTb1046IedByF1046Code(ied);
			sgFcda.setF1060Index(i);
			sgFcda.setF1060Desc(fcdaDesc);
			sgFcda.setF1060RefAddr(SclUtil.getFcdaRef(fcdaEl));
			sgFcda.setF1060DataType(context.getBType(elLd, fcdaEl));
			spFcdaList.add(sgFcda);
			i++;
		}
		beanDao.insertBatch(spFcdaList);
	}
	
	/**
	 * 添加状态量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	protected Tb1016StatedataEntity addStatedata(String ref, String fcdaDesc, int f1011No) {
		Tb1016StatedataEntity statedata = ParserUtil.createStatedata(fcdaDesc, ref,
				ied.getF1046Code(), ied, f1011No);
		sts.add(statedata);
		return statedata;
	}
	
	/**
	 * 添加模拟量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	protected Tb1006AnalogdataEntity addAlgdata(String ref, String fcdaDesc, int f1011No) {
		Tb1006AnalogdataEntity algdata = ParserUtil.createAlgdata(ref, fcdaDesc, ied, f1011No);
		agls.add(algdata);
		return algdata;
	}
	
	private String getFCDADesc(Element elLd, Element fcdaEl) {
		String prefix = fcdaEl.attributeValue("prefix");
		String lnClass = fcdaEl.attributeValue("lnClass");
		String lnInst = fcdaEl.attributeValue("lnInst");
		String doName = fcdaEl.attributeValue("doName");
		return SclUtil.getFCDADesc(elLd, prefix, lnClass, lnInst, doName);
	}
	
	private void addError(String iedName, String subType, String ref, String desc) {
		context.addError(iedName, subType, ref, desc);
	}
	
	private void addWarning(String iedName, String subType, String ref, String desc) {
		context.addWarning(iedName, subType, ref, desc);
	}

	public Tb1046IedEntity getIed() {
		return ied;
	}

	public List<Tb1054RcbEntity> getRcbs() {
		return rcbs;
	}

	public List<Tb1055GcbEntity> getGses() {
		return gses;
	}

	public List<Tb1056SvcbEntity> getSmvs() {
		return smvs;
	}
	
}
