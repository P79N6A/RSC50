package com.synet.tool.rsc.das.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.Tb1022FaultconfigEntity;


public class TestMain {

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

}
