package com.synet.tool.rsc.das.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.util.RscObjectUtils;

//connect 'jdbc:derby:D:/develop/eclipse-rcp-indigo-SR2-win32/eclipse/RscData/ppp;create=true;user=rscapp;password=123456';

public class IEDServiceTest {

	private BeanDaoImpl beandao;
	
	@Before
	public void before() {
//		String dbName = "shangwu";
		String dbName = "ppp";
		ProjectManager instance = ProjectManager.getInstance();
//		instance.initDb(dbName);
		instance.openDb(dbName);
		beandao = BeanDaoImpl.getInstance();
	}

	@Test
	public void testTb1047BoardEntity() {
		Tb1046IedEntity ied = (Tb1046IedEntity) beandao.getObject(Tb1046IedEntity.class, "f1046Name", "PC101");
		List<Tb1047BoardEntity> list = (List<Tb1047BoardEntity>) beandao.getAll(Tb1047BoardEntity.class);
		if (list.size() < 1) {
			Tb1047BoardEntity boardEntity = RscObjectUtils.createBoardEntity();
			boardEntity.setTb1046IedByF1046Code(ied);
			boardEntity.setF1047Slot("5");
			boardEntity.setF1047Desc("sy8201");
			boardEntity.setF1047Type("cpu");
			beandao.insert(boardEntity);
			list = (List<Tb1047BoardEntity>) beandao.getAll(Tb1047BoardEntity.class);
		}
		Assert.assertTrue(list.size() > 0);
		Tb1047BoardEntity boardEntity = list.get(0);
		
		Tb1048PortEntity portEntity = RscObjectUtils.createPortEntity();
		portEntity.setTb1047BoardByF1047Code(boardEntity);
		portEntity.setF1048No("1");
		portEntity.setF1048Direction(3);
		portEntity.setF1048Plug(4);
		beandao.insert(portEntity);
		List<Tb1048PortEntity> list1 = (List<Tb1048PortEntity>) beandao.getAll(Tb1048PortEntity.class);
		Assert.assertTrue(list1.size() > 0);
	}
}
