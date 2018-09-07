package com.synet.tool.rsc.das.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.Tb1022FaultconfigEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.util.RscObjectUtils;


public class TestMain {
	
	private BeanDaoImpl beandao;
	private PhyscialAreaService service;
	
	@Before
	public void before() {
		String dbName = "shangwu";
		ProjectManager instance = ProjectManager.getInstance();
//		instance.initDb(dbName);
		instance.openDb(dbName);
		beandao = BeanDaoImpl.getInstance();
		service = new PhyscialAreaService();
	}
	
	@Test
	public void listTest() {
		@SuppressWarnings("unchecked")
		List<Tb1049RegionEntity> list = (List<Tb1049RegionEntity>) beandao.getAll(Tb1049RegionEntity.class);
		System.out.println(list);
//		System.out.println(list.get(0).getF1041Code());
		System.out.println(list.get(0).getTb1041SubstationByF1041Code().getF1041Code());
		Tb1049RegionEntity entity = new Tb1049RegionEntity();
		entity.setF1049Code("104903");
//		entity.setF1041Code("104101");
		entity.setF1049Name("SX");
		entity.setF1049Desc("山西");
		entity.setF1049Area(0);
//		Tb1041SubstationEntity entity2 = new Tb1041SubstationEntity();
//		entity.setF1041Code("104101");
		entity.setTb1041SubstationByF1041Code(list.get(0).getTb1041SubstationByF1041Code());
		beandao.insert(entity);
	}
	
	
	@Test
	public void testInsertBayEntry() {
	}
	
	@Test
	public void testGetBayEntryList() {
		List<?> res = beandao.getAll(Tb1042BayEntity.class);
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() > 0);
	}
	
	@Test
	public void testBayEntryService() {
		BayEntityService service = new BayEntityService();
		List<?> res = service.getBayEntryList();
		Assert.assertNotNull(res);
		Assert.assertTrue(res.size() > 0);
	}

	@Test
	public void testInsert() {
		String dbName = "RscData";
		ProjectManager instance = ProjectManager.getInstance();
		instance.initDb(dbName);
		instance.openDb(dbName);
		
		Tb1022FaultconfigEntity fault = new Tb1022FaultconfigEntity();
		fault.setF1022Code("aaa");
		fault.setF1011No(0);
		fault.setF1022Faultlevel(0);
		fault.setF1022K(11);
		fault.setF1022T1(11);
		fault.setF1022T2(11);
		BeanDaoImpl beandao = BeanDaoImpl.getInstance();
		beandao.deleteAll(Tb1022FaultconfigEntity.class);
		beandao.insert(fault);
		List<?> testItem = beandao.getAll(Tb1022FaultconfigEntity.class);
//				.getListLike(PlinTest.class, "name",name);
		Tb1022FaultconfigEntity entity = (Tb1022FaultconfigEntity) testItem.get(0);
		System.out.println(entity.getF1022Code());
		assertTrue(testItem.size() > 0);
				
//		//测试正常,注意,TestCase不能打印Plintest,否则死循环
//		PlinTest plinTest = new PlinTest();
//		String name="XXXXXXX";
//		plinTest.setName(name);
//		List<TestCase> cases=new ArrayList<>();
//		TestCase case1=new TestCase();
//		case1.setName("name1");
//		TestCase case2=new TestCase();
//		case2.setName("name2");
//		TestCase case3=new TestCase();
//		case3.setName("case3");
//		TestCase case4=new TestCase();
//		case4.setName("case4");
//		TestCase case5=new TestCase();
//		case5.setName("case5");
//		TestCase case6=new TestCase();
//		case6.setName("case6");
//		cases.add(case1);
//		cases.add(case2);
//		cases.add(case3);
//		cases.add(case4);
//		cases.add(case5);
//		cases.add(case6);
//		plinTest.setTestCases(cases);
//		BeanDaoImpl.getInstance().insert(plinTest);
//		List<?> testItem=BeanDaoImpl.getInstance()
//				.getListLike(PlinTest.class, "name",name);
//		assertTrue(testItem.size() > 0);
//		PlinTest plinTest2 = (PlinTest)(testItem.get(0));
//		System.out.print(plinTest2);
//		assertTrue(plinTest2.getTestCases().size() > 0);
	}
	
	@Test
	public void test1050() {
//		List<Tb1050CubicleEntity> list = (List<Tb1050CubicleEntity>) beandao.getAll(Tb1050CubicleEntity.class);
		Tb1049RegionEntity entity = new Tb1049RegionEntity();
		entity.setF1049Code("104901");
		@SuppressWarnings("unchecked")
		List<Tb1050CubicleEntity> list = 
		(List<Tb1050CubicleEntity>) beandao.getListByCriteria(Tb1050CubicleEntity.class,
				"tb1049RegionByF1049Code", entity);
		System.out.println(list);
		List<Tb1051CableEntity> result = service.getCableList(list);
		System.out.println(result);
	}
	
	@Test
	public void test1090() {
		@SuppressWarnings("unchecked")
		List<Tb1090LineprotfiberEntity> list = (List<Tb1090LineprotfiberEntity>) beandao.getAll(Tb1090LineprotfiberEntity.class);
		System.out.println(list);
	}
	
	@Test
	public void testTb1047BoardEntity() {
		Tb1046IedEntity ied = (Tb1046IedEntity) beandao.getObject(Tb1046IedEntity.class, "f1046Name", "PC101");
//		Tb1047BoardEntity boardEntity = RscObjectUtils.createBoardEntity();
//		boardEntity.setTb1046IedByF1046Code(ied);
//		boardEntity.setF1047Slot("5");
//		boardEntity.setF1047Desc("sy8201");
//		boardEntity.setF1047Type("cpu");
//		beandao.insert(boardEntity);
		List<Tb1047BoardEntity> list = (List<Tb1047BoardEntity>) beandao.getAll(Tb1047BoardEntity.class);
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
