/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.business.scl.das.FcdaDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.ied.IedParserNew;
import com.synet.tool.rsc.io.ied.LogicLinkParserNew;
import com.synet.tool.rsc.io.ied.NetConfig;
import com.synet.tool.rsc.io.parser.SubstationParser;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.util.ProblemManager;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-14
 */
public class SCDImporterNew implements IImporter {

	private String scdPath;
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private SubstationService staServ = new SubstationService();
	private Map<String, Tb1042BayEntity> bayCache = new HashMap<>();
	
	public SCDImporterNew(String scdPath) {
		this.scdPath = scdPath;
		clearHistory();
	}

	private void clearHistory() {
		FcdaDAO.getInstance().clear();
		bayCache.clear();
		beanDao.deleteAll(Tb1006AnalogdataEntity.class);
		beanDao.deleteAll(Tb1016StatedataEntity.class);
		beanDao.deleteAll(Tb1026StringdataEntity.class);
		beanDao.deleteAll(Tb1046IedEntity.class);
		
		beanDao.deleteAll(Tb1055GcbEntity.class);
		beanDao.deleteAll(Tb1056SvcbEntity.class);
		beanDao.deleteAll(Tb1061PoutEntity.class);

		beanDao.deleteAll(Tb1054RcbEntity.class);
		beanDao.deleteAll(Tb1058MmsfcdaEntity.class);

		beanDao.deleteAll(Tb1057SgcbEntity.class);
		beanDao.deleteAll(Tb1059SgfcdaEntity.class);
		
		beanDao.deleteAll(Tb1065LogicallinkEntity.class);
		beanDao.deleteAll(Tb1063CircuitEntity.class);
		beanDao.deleteAll(Tb1062PinEntity.class);

		beanDao.deleteAll(Tb1041SubstationEntity.class);
		beanDao.deleteAll(Tb1042BayEntity.class);
		beanDao.deleteAll(Tb1043EquipmentEntity.class);
		beanDao.deleteAll(Tb1044TerminalEntity.class);
		beanDao.deleteAll(Tb1045ConnectivitynodeEntity.class);
		beanDao.deleteAll(Tb1067CtvtsecondaryEntity.class);
		beanDao.deleteAll(Tb1066ProtmmxuEntity.class);
		beanDao.deleteAll(Tb1064StrapEntity.class);
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

	@Override
	public void execute(IProgressMonitor monitor) {
		long begin = System.currentTimeMillis();
		ConsoleManager console = ConsoleManager.getInstance();
		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdPath);
		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, scdPath);
		Context context = new Context();
		SubstationParser sp = new SubstationParser(context);
		sp.init();
		// 二次部分
		List<Element> iedNds = IEDDAO.getAllIEDWithCRC();
		if (iedNds == null || iedNds.size() < 1) {
			return;
		}
		if (monitor != null) {
			monitor.beginTask("开始导入SCD", iedNds.size() + 2);
		}
		for (Element iedNd : iedNds) {
			String iedName = iedNd.attributeValue("name");
			iedNd = XMLDBHelper.selectSingleNode(SCL.getIEDXPath(iedName));
			IedParserNew iedParser = new IedParserNew(iedNd, context, monitor);
			iedParser.parse();
			Tb1046IedEntity ied = iedParser.getIed();
			// 根据解析结果修改装置类型和间隔类型
			Tb1042BayEntity bay = null;
			if (iedParser.getRcbs().size() > 0) {		// 保护测控
				String ldXpath = SCL.getLDXPath(iedName, "PROT");
				int type = XMLDBHelper.existsNode(ldXpath) ?
						DBConstants.IED_PROT : DBConstants.IED_MONI;
				ied.setF1046Type(type);
				if (type == DBConstants.IED_MONI) {
					bay = getBayByName(DBConstants.BAY_MOT);
				}
				NetConfig netConfig = context.getNetConfig(iedName + ".MMS");
				if (netConfig != null) {
					Tb1070MmsserverEntity mmsServer = new Tb1070MmsserverEntity();
					mmsServer.setF1070Code(rscp.nextTbCode(DBConstants.PR_MMSSvr));
					mmsServer.setTb1046IedByF1046Code(ied);
					mmsServer.setF1070IpA(netConfig.getIpA());
					mmsServer.setF1070IpB(netConfig.getIpB());
					String vtcrc = iedNd.attributeValue("crc");
					mmsServer.setF1070IedCrc(vtcrc);
					mmsServer.setF1070CrcPath("LD0/LPHD$SP$IEDPinCrc$setVal");
					beanDao.insert(mmsServer);
				}
			} else {
				if (iedParser.getSmvs().size() > 0) {	// 合并单元
					ied.setF1046Type(DBConstants.IED_MU);
				} else {										// 智能终端
					ied.setF1046Type(DBConstants.IED_TERM);
				}
			}
			bay = (bay==null) ? getBayByName(DBConstants.BAY_OTHER) : bay;
			ied.setF1042Code(bay.getF1042Code());
			beanDao.update(ied);
		}
		console.append("一共导入 " + iedNds.size() + " 台装置。");
		// 虚链路与虚回路
		if (monitor != null) {
			monitor.setTaskName("正在处理逻辑链路和虚回路");
		}
		new LogicLinkParserNew(context).parse();
		if (monitor != null) {
			monitor.worked(1);
		}
		// 一次部分
		if (monitor != null) {
			monitor.setTaskName("正在处理一次拓扑模型");
		}
		sp.parse();
		if (monitor != null) {
			monitor.done();
		}
		long t = (System.currentTimeMillis() - begin) / 1000;
		console.append("SCD导入耗时 " + t + " 秒。");
		ProblemManager.getInstance().append(context.getProblems());
	}

}

