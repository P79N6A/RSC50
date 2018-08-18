package com.synet.tool.rsc.das.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.Tb1022FaultconfigEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.PhyscialAreaService;


public class TestMain {
	
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
	public void listTest() {
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
		//创建变电站
		Tb1041SubstationEntity substationEntity1 = new Tb1041SubstationEntity();
		substationEntity1.setF1041Code("substation1");
		substationEntity1.setF1041Company("思源电气股份有限公司");
		substationEntity1.setF1041Desc("变电站1");
		substationEntity1.setF1041Dqdesc("变电站1");
		substationEntity1.setF1041DqName("变电站1");
		substationEntity1.setF1041Name("变电站1");
		substationEntity1.setF1042VoltageH(1);
		substationEntity1.setF1042VoltageM(2);
		substationEntity1.setF1042VoltageL(3);
		beandao.insert(substationEntity1);
		//间隔1
		Tb1042BayEntity bayEntity1 = new Tb1042BayEntity();
		bayEntity1.setF1042Code(" bay1");
		bayEntity1.setF1041Code(substationEntity1.getF1041Code());
		bayEntity1.setF1042Name("间隔1");
		bayEntity1.setF1042Desc("间隔1");
		bayEntity1.setF1042DevType(1);
		bayEntity1.setF1042Voltage(2);
		bayEntity1.setF1042ConnType(3);
		bayEntity1.setTb1041SubstationByF1041Code(substationEntity1);
		beandao.insert(bayEntity1);
		//互感器1
		Tb1043EquipmentEntity equipmentEntity1 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment1");
		equipmentEntity1.setF1042Code(bayEntity1.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity1);
		equipmentEntity1.setTb1016StatedataEntity(null);
		
		//互感器次级
		Tb1067CtvtsecondaryEntity ctvtsecondaryEntity1  = new Tb1067CtvtsecondaryEntity();
		ctvtsecondaryEntity1.setF1067Code("Ctvtsecondary1");
		ctvtsecondaryEntity1.setF1067Desc("ctvtsecondary1");
		ctvtsecondaryEntity1.setF1043Code(equipmentEntity1.getF1043Code());
		ctvtsecondaryEntity1.setTb1043EquipmentByF1043Code(equipmentEntity1);
		
		//虚端子1
		//虚端子2
		//虚端子3
		//虚端子4
		//虚端子5
		//虚端子6
		
		
		//互感器2
		Tb1043EquipmentEntity equipmentEntity2 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment2");
		equipmentEntity1.setF1042Code(bayEntity1.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity1);
		equipmentEntity1.setTb1016StatedataEntity(null);
		
		//互感器3
		Tb1043EquipmentEntity equipmentEntity3 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment3");
		equipmentEntity1.setF1042Code(bayEntity1.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity1);
		equipmentEntity1.setTb1016StatedataEntity(null);
		//间隔2
		Tb1042BayEntity bayEntity2 = new Tb1042BayEntity();
		bayEntity2.setF1042Code(" bay2");
		bayEntity2.setF1041Code(substationEntity1.getF1041Code());
		bayEntity2.setF1042Name("间隔2");
		bayEntity2.setF1042Desc("间隔2");
		bayEntity2.setF1042DevType(1);
		bayEntity2.setF1042Voltage(2);
		bayEntity2.setF1042ConnType(3);
		bayEntity2.setTb1041SubstationByF1041Code(substationEntity1);
		beandao.insert(bayEntity2);
		//互感器4
		Tb1043EquipmentEntity equipmentEntity4 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment4");
		equipmentEntity1.setF1042Code(bayEntity2.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity2);
		equipmentEntity1.setTb1016StatedataEntity(null);
		
		//互感器5
		Tb1043EquipmentEntity equipmentEntity5 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment5");
		equipmentEntity1.setF1042Code(bayEntity2.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity2);
		equipmentEntity1.setTb1016StatedataEntity(null);
		
		//互感器6
		Tb1043EquipmentEntity equipmentEntity6 = new Tb1043EquipmentEntity();
		equipmentEntity1.setF1043Code("Equipment6");
		equipmentEntity1.setF1042Code(bayEntity2.getF1042Code());
		equipmentEntity1.setTb1042BayByF1042Code(bayEntity2);
		equipmentEntity1.setTb1016StatedataEntity(null);
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
		List<Tb1050CubicleEntity> list = 
		(List<Tb1050CubicleEntity>) beandao.getListByCriteria(Tb1050CubicleEntity.class,
				"tb1049RegionByF1049Code", entity);
		System.out.println(list);
		List<Tb1051CableEntity> result = service.getCableList(list);
		System.out.println(result);
	}
	
	@Test
	public void test1090() {
		List<Tb1090LineprotfiberEntity> list = (List<Tb1090LineprotfiberEntity>) beandao.getAll(Tb1090LineprotfiberEntity.class);
		System.out.println(list);
	}
	

}
