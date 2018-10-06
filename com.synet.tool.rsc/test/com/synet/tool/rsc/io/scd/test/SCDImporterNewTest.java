/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.scd.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.SCDImporterNew;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-29
 */
public class SCDImporterNewTest {

	private BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	
	@Before
	public void before() {
		String prj = "bbb1";
		ProjectManager prjmgr = ProjectManager.getInstance();
		if (!prjmgr.exists(prj)) {
			prjmgr.initDb(prj);
		} else {
			prjmgr.openDb(prj);
		}
	}
	@Test
	public void testExecute() {
		String scdPath = "./test/bbb1.scd";
		TimeCounter.begin();
		new SCDImporterNew(scdPath).execute(null);
		TimeCounter.end("New导入SCD");
		assertLogiclinks();
		assertEquipments();
	}

	private void assertLogiclinks() {
		List<Tb1065LogicallinkEntity> cbs = (List<Tb1065LogicallinkEntity>) beanDao.getAll(Tb1065LogicallinkEntity.class);
		assertTrue(cbs.size() > 0);
		List<Tb1063CircuitEntity> cts = (List<Tb1063CircuitEntity>) beanDao.getAll(Tb1063CircuitEntity.class);
		assertTrue(cts.size() > 0);
		List<Tb1062PinEntity> pins = (List<Tb1062PinEntity>) beanDao.getAll(Tb1062PinEntity.class);
		assertTrue(pins.size() > 0);
	}
	
	private void assertEquipments() {
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
//		List<Tb1066ProtmmxuEntity> mms = (List<Tb1066ProtmmxuEntity>) beanDao.getAll(Tb1066ProtmmxuEntity.class);
//		assertTrue(mms.size() > 0);
	}
}

