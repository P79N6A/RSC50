/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.scd.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import com.shrcn.business.scl.check.InstResolver;
import com.shrcn.business.scl.check.Problem;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.parser.DsParameterParser;
import com.synet.tool.rsc.io.parser.DsSettingParser;
import com.synet.tool.rsc.io.parser.GooseParser;
import com.synet.tool.rsc.io.parser.RcbParser;
import com.synet.tool.rsc.io.parser.SmvParser;
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
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class IedParserTest {
	
	private BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	private RSCProperties rscp = RSCProperties.getInstance();
	
	@Before
	public void before() {
		DictManager.getInstance().init(getClass(), RSCConstants.DICT_PATH);
		
		String scdPath = "./test/sub_shangwu.scd";
		XMLDBHelper.loadDocument("shangwu", scdPath);
		String prj = "shangwu";
		ProjectManager prjmgr = ProjectManager.getInstance();
		if (!prjmgr.exists(prj)) {
			prjmgr.initDb(prj);
		} else {
			prjmgr.openDb(prj);
			BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
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
		
//		new SCDImporter(scdPath).execute();
	}

	@Test
	public void testGooseParser() {
//		List<Element> iedNds = IEDDAO.getAllIEDWithCRC();
//		assertNotNull(iedNds);
//		assertTrue(iedNds.size() > 1);

		Tb1046IedEntity ied = new Tb1046IedEntity();
		ied.setF1046Name("IL1101");
		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
		beanDao.insert(ied);
		GooseParser iedSubParser = new GooseParser(ied);
		iedSubParser.parse();
		List<Tb1055GcbEntity> items = iedSubParser.getItems();
		assertTrue(items.size() > 0);
		List<Tb1055GcbEntity> cbs = (List<Tb1055GcbEntity>) beanDao.getAll(Tb1055GcbEntity.class);
		assertTrue(items.size() == cbs.size());
		List<Tb1061PoutEntity> pouts = (List<Tb1061PoutEntity>) beanDao.getAll(Tb1061PoutEntity.class);
		assertTrue(pouts.size() > 0);
		List<?> sts = beanDao.getAll(Tb1016StatedataEntity.class);
		assertTrue(sts.size() > 0);
	}

	@Test
	public void testSmvParser() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		ied.setF1046Name("PE101_DPU08");
		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
		beanDao.insert(ied);
		SmvParser iedSubParser = new SmvParser(ied);
		iedSubParser.parse();
		List<Tb1056SvcbEntity> items = iedSubParser.getItems();
		assertTrue(items.size() > 0);
		List<Tb1056SvcbEntity> cbs = (List<Tb1056SvcbEntity>) beanDao.getAll(Tb1056SvcbEntity.class);
		assertTrue(items.size() == cbs.size());
		List<Tb1061PoutEntity> pouts = (List<Tb1061PoutEntity>) beanDao.getAll(Tb1061PoutEntity.class);
		assertTrue(pouts.size() > 0);
		List<?> agls = beanDao.getAll(Tb1006AnalogdataEntity.class);
		assertTrue(agls.size() > 0);
	}
	
	@Test
	public void testRcbParser() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		ied.setF1046Name("CL1101");
		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
		beanDao.insert(ied);
		RcbParser iedSubParser = new RcbParser(ied);
		iedSubParser.parse();
		List<Tb1054RcbEntity> items = iedSubParser.getItems();
		assertTrue(items.size() > 0);
		List<Tb1054RcbEntity> cbs = (List<Tb1054RcbEntity>) beanDao.getAll(Tb1054RcbEntity.class);
		assertTrue(items.size() == cbs.size());
//		String sql = "select a.*, c.Parent_Code " +
//				" from tb1058_mmsfcda a," +
//				" tb1054_rcb b," +
//				" tb1016_statedata c" +
//				" where a.F1054_CODE=b.F1054_CODE" +
//				" and a.DATA_CODE=c.F1016_CODE" +
//				" and b.F1046_CODE=:iedCode" +
//				" and b.F1054_Dataset=:datSet";
//		Map<String, Object> params = new HashMap<>();
//		params.put("iedCode", ied.getF1046Code());
//		params.put("datSet", "dsWarning");
//		List<?> result = HqlDaoImpl.getInstance().queryBySql(sql, Tb1058MmsfcdaEntity.class, params);
//		assertTrue(result.size() > 0);
		List<Tb1058MmsfcdaEntity> fcdas = (List<Tb1058MmsfcdaEntity>) beanDao.getAll(Tb1058MmsfcdaEntity.class);
		assertTrue(fcdas.size() > 0);
		checkDatas();
	}
	
	private void checkDatas() {
		List<?> sts = beanDao.getAll(Tb1016StatedataEntity.class);
		List<?> traps = beanDao.getAll(Tb1064StrapEntity.class);
		assertTrue(traps.size() > 0);
		List<?> agls = beanDao.getAll(Tb1006AnalogdataEntity.class);
		List<?> strs = beanDao.getAll(Tb1026StringdataEntity.class);
		assertTrue(sts.size() + agls.size() + strs.size() > 0);
	}
	
	@Test
	public void testSgcbParser() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		String f1046Name = "SY_XHK1";
		ied.setF1046Name(f1046Name);
		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
		beanDao.insert(ied);
		Element dtTypeNd = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		InstResolver irs = new InstResolver(dtTypeNd, f1046Name, new ArrayList<Problem>());
		DsSettingParser iedSubParser = new DsSettingParser(ied, irs.getLnTypeMap());
		iedSubParser.parse();
		List<Tb1057SgcbEntity> items = iedSubParser.getItems();
		assertTrue(items.size() > 0);
		List<Tb1057SgcbEntity> cbs = (List<Tb1057SgcbEntity>) beanDao.getAll(Tb1057SgcbEntity.class);
		assertTrue(items.size() == cbs.size());
		List<Tb1059SgfcdaEntity> pouts = (List<Tb1059SgfcdaEntity>) beanDao.getAll(Tb1059SgfcdaEntity.class);
		assertTrue(pouts.size() > 0);
	}
	
	@Test
	public void testSpParser() {
		Tb1046IedEntity ied = new Tb1046IedEntity();
		String f1046Name = "PZ101";
		ied.setF1046Name(f1046Name);
		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
		beanDao.insert(ied);
		Element dtTypeNd = XMLDBHelper.selectSingleNode(SCL.XPATH_DATATYPETEMPLATES);
		InstResolver irs = new InstResolver(dtTypeNd, f1046Name, new ArrayList<Problem>());
		DsParameterParser iedSubParser = new DsParameterParser(ied, irs.getLnTypeMap());
		iedSubParser.parse();
		List<Tb1060SpfcdaEntity> items = iedSubParser.getItems();
		assertTrue(items.size() > 0);
		List<Tb1060SpfcdaEntity> sps = (List<Tb1060SpfcdaEntity>) beanDao.getListByCriteria(Tb1060SpfcdaEntity.class, "tb1046IedByF1046Code", ied);
		assertTrue(items.size() == sps.size());
	}
	
	@Test
	public void testLogicLinkParser() {
//		Tb1046IedEntity ied = new Tb1046IedEntity();
//		ied.setF1046Name("PT1101A");
//		ied.setF1046Code(rscp.nextTbCode(DBConstants.PR_IED));
//		beanDao.insert(ied);
//		LogicLinkParser iedSubParser = new LogicLinkParser(ied);
//		iedSubParser.parse();
//		List<Tb1065LogicallinkEntity> items = iedSubParser.getItems();
//		assertTrue(items.size() > 0);
		List<Tb1065LogicallinkEntity> cbs = (List<Tb1065LogicallinkEntity>) beanDao.getAll(Tb1065LogicallinkEntity.class);
		assertTrue(cbs.size() > 0);
		List<Tb1063CircuitEntity> cts = (List<Tb1063CircuitEntity>) beanDao.getAll(Tb1063CircuitEntity.class);
		assertTrue(cts.size() > 0);
		List<Tb1062PinEntity> pins = (List<Tb1062PinEntity>) beanDao.getAll(Tb1062PinEntity.class);
		assertTrue(pins.size() > 0);
	}
	
	@Test
	public void testSubstationParser() {
		SubstationParser sp = new SubstationParser();
		sp.init();
		sp.parse();
		List<Tb1041SubstationEntity> sts = (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
		assertTrue(sts.size() > 0);
		List<Tb1042BayEntity> bas = (List<Tb1042BayEntity>) beanDao.getAll(Tb1042BayEntity.class);
		assertTrue(bas.size() > 0);
		List<Tb1043EquipmentEntity> eqs = (List<Tb1043EquipmentEntity>) beanDao.getAll(Tb1043EquipmentEntity.class);
		assertTrue(eqs.size() > 0);
		List<Tb1044TerminalEntity> tts = (List<Tb1044TerminalEntity>) beanDao.getAll(Tb1044TerminalEntity.class);
		assertTrue(tts.size() > 0);
		List<Tb1045ConnectivitynodeEntity> ccs = (List<Tb1045ConnectivitynodeEntity>) beanDao.getAll(Tb1045ConnectivitynodeEntity.class);
		assertTrue(ccs.size() > 0);
		List<Tb1067CtvtsecondaryEntity> secs = (List<Tb1067CtvtsecondaryEntity>) beanDao.getAll(Tb1067CtvtsecondaryEntity.class);
		assertTrue(secs.size() > 0);
		List<Tb1066ProtmmxuEntity> mms = (List<Tb1066ProtmmxuEntity>) beanDao.getAll(Tb1066ProtmmxuEntity.class);
		assertTrue(mms.size() > 0);
	}
	
}

