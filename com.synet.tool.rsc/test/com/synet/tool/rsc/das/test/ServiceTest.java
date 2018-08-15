package com.synet.tool.rsc.das.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.SecFibreService;

public class ServiceTest {
	
	private SecFibreService secFibreService;
	
	@Before
	public void init() {
		String dbName = "RscData";
		ProjectManager instance = ProjectManager.getInstance();
		instance.initDb(dbName);
		instance.openDb(dbName);
		secFibreService = new SecFibreService();
	}
	
	@Test
	public void testQuery(){
		String f1046Name = "TEST_IED1";
		String f1046Model = "保护装置";
		List<Tb1090LineprotfiberEntity> list = secFibreService.getLineListByIedParams(f1046Model, f1046Name);
		System.out.println(list);
	}

}
