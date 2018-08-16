/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.scd.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.parser.GooseParser;
import com.synet.tool.rsc.io.parser.SmvParser;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class IedParserTest {
	
	@Before
	public void before() {
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
		}
	}

	@Test
	public void testGooseParser() {
//		List<Element> iedNds = IEDDAO.getAllIEDWithCRC();
//		assertNotNull(iedNds);
//		assertTrue(iedNds.size() > 1);

		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		RSCProperties rscp = RSCProperties.getInstance();

		Tb1046IedEntity ied = new Tb1046IedEntity();
		ied.setF1046Name("PT1101A");
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
	}

	@Test
	public void testSmvParser() {

		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		RSCProperties rscp = RSCProperties.getInstance();

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
	}
}

