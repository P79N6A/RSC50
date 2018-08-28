/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import sun.security.pkcs11.Secmod.DbMode;

import com.shrcn.business.scl.check.InstResolver;
import com.shrcn.business.scl.check.Problem;
import com.shrcn.business.scl.das.FcdaDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.parser.DsParameterParser;
import com.synet.tool.rsc.io.parser.DsSettingParser;
import com.synet.tool.rsc.io.parser.IIedParser;
import com.synet.tool.rsc.io.parser.LogicLinkParser;
import com.synet.tool.rsc.io.parser.RcbParser;
import com.synet.tool.rsc.io.parser.GooseParser;
import com.synet.tool.rsc.io.parser.SmvParser;
import com.synet.tool.rsc.io.parser.SubstationParser;
import com.synet.tool.rsc.io.scd.IedInfoDao;
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
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-14
 */
public class SCDImporter implements IImporter {

	private String scdPath;
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	private Map<String, Tb1042BayEntity> bayCache = new HashMap<>();
	
	public SCDImporter(String scdPath) {
		this.scdPath = scdPath;
		clearHistory();
	}

	private void clearHistory() {
		FcdaDAO.getInstance().clear();
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
			List<Tb1041SubstationEntity> staList = (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
			if (staList != null && staList.size() > 0) {
				bay.setTb1041SubstationByF1041Code(staList.get(0));
			}
			beanDao.insert(bay);
		}
		return bay;
	}

	@Override
	public void execute() {
		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdPath);
		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, scdPath);
		// 一次部分
		SubstationParser sp = new SubstationParser();
		sp.parse();
		// 二次部分
		List<Element> iedNds = IEDDAO.getAllIEDWithCRC();
		if (iedNds == null || iedNds.size() < 1) {
			return;
		}
		Element dtTypeNd = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		for (Element iedNd : iedNds) {
			Tb1046IedEntity ied = new Tb1046IedEntity();
			String iedName = iedNd.attributeValue("name");
			ied.setF1046Name(iedName);
			ied.setF1046Desc(iedNd.attributeValue("desc"));
			ied.setF1046Model(iedNd.attributeValue("type"));
			ied.setF1046Manufacturor(iedNd.attributeValue("manufacturer"));
			ied.setF1046ConfigVersion(iedNd.attributeValue("configVersion"));
			String vtcrc = iedNd.attributeValue("crc");
			ied.setF1046Crc(vtcrc);
			InstResolver irs = new InstResolver(dtTypeNd, iedName, new ArrayList<Problem>());
			// code
			String iedCode = rscp.nextTbCode(DBConstants.PR_IED);
			ied.setF1046Code(iedCode);
			// A/B
			int aOrb = iedName.endsWith("B") ? 2 : 1;
			ied.setF1046AorB(aOrb);
			ied.setF1046IsVirtual(0);
			beanDao.insert(ied);
			
			Tb1026StringdataEntity strData = new Tb1026StringdataEntity();
			strData.setF1026Code(rscp.nextTbCode(DBConstants.PR_String));
			strData.setF1026Desc(vtcrc);
			strData.setParentCode(iedCode);
			beanDao.insert(strData);
			
			Map<String, IIedParser> pmap = new HashMap<String, IIedParser>();
			pmap.put("goose", new GooseParser(ied));
			pmap.put("smv", new SmvParser(ied));
			pmap.put("rcb", new RcbParser(ied));
			pmap.put("set", new DsSettingParser(ied, irs.getLnTypeMap()));
			pmap.put("param", new DsParameterParser(ied, irs.getLnTypeMap()));
			for (IIedParser parser : pmap.values()) {
				parser.parse();
			}
			// 根据解析结果修改类型
			if (pmap.get("rcb").getItems().size() > 0) {
				String ldXpath = SCL.getLDXPath(iedName, "PROT");
				int type = XMLDBHelper.existsNode(ldXpath) ?
						DBConstants.IED_PROT : DBConstants.IED_MONI;
				ied.setF1046Type(type);
				if (type == DBConstants.IED_PROT) {
					Tb1042BayEntity bay = getBayByName(DBConstants.BAY_PROT);
					ied.setTb1042BaysByF1042Code(bay);
				} else {
					Tb1042BayEntity bay = getBayByName(DBConstants.BAY_PUB);
					ied.setTb1042BaysByF1042Code(bay);
				}
				Tb1070MmsserverEntity mmsServer = new Tb1070MmsserverEntity();
				mmsServer.setF1070Code(rscp.nextTbCode(DBConstants.PR_MMSSvr));
				mmsServer.setTb1046IedByF1046Code(ied);
				String[] ips = IedInfoDao.getIPs(iedName);
				if (ips.length > 0) {
					mmsServer.setF1070IpA(ips[0]);
					if (ips.length > 1) {
						mmsServer.setF1070IpB(ips[1]);
					}
				} else {
					mmsServer.setF1070IpA("");
				}
				beanDao.insert(mmsServer);
			} else {
				if (pmap.get("smv").getItems().size() > 0) {
					ied.setF1046Type(DBConstants.IED_MU);
				} else {
					ied.setF1046Type(DBConstants.IED_TERM);
				}
				Tb1042BayEntity bay = getBayByName(DBConstants.BAY_PROT);
				ied.setTb1042BaysByF1042Code(bay);
			}
			beanDao.update(ied);
		}
		for (Element iedNd : iedNds) {
			String iedName = iedNd.attributeValue("name");
			Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedName);
			new LogicLinkParser(ied).parse();
		}
	}

}

