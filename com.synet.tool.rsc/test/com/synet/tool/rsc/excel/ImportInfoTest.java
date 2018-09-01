package com.synet.tool.rsc.excel;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;

public class ImportInfoTest {
	private BeanDaoImpl beandao;
	private PhyscialAreaService service;
	
	@Before
	public void before() {
		String dbName = "RscData";
		ProjectManager instance = ProjectManager.getInstance();
		instance.initDb(dbName);
		instance.openDb(dbName);
		beandao = BeanDaoImpl.getInstance();
		service = new PhyscialAreaService();
	}
	
	@Test
	public void testRead1090() {
		ImportInfoParser parser = new ImportInfoParser();
		List<Tb1090LineprotfiberEntity> result = parser.getLineprotfiberList("C:\\Users\\36576\\Desktop\\sub_cyb20180306\\test.xlsx");
		System.out.println(result);
		System.out.println(parser.getHandler().getErrorMsg());
	}
	
	@Test
	public void testImpFibreList() {
		ImportInfoParser parser = new ImportInfoParser();
		Map<String, List<IM102FibreListEntity>> result = parser.getFibreList("C:\\Users\\36576\\Desktop\\光缆清册test.xlsx");
		System.out.println(result);
		System.out.println(parser.getHandler().getErrorMsg());
	}
}
