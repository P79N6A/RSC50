package com.synet.tool.rsc.excel.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.excel.ExcelImporter;
import com.synet.tool.rsc.excel.TableHeadParser;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;

public class ExcelImporterTest {
	
	private static final String dir = "./test/excel/caihong/";
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
	public void testImportExcelIEDBoard() {
//		String path = dir + "装置板卡端口.xlsx";
		String path = dir + "装置板卡与端口整理09201523(1).xlsx";
		int excelHeadRow = 0;
		Map<Integer, String> excelColInfo = new TableHeadParser().getTableHeadInfo(path, excelHeadRow);
		assertTrue(ExcelImporter.importExcelData(ExcelConstants.IM103_IED_BOARD, path, excelHeadRow, excelColInfo));
		List<Tb1046IedEntity> ieds = (List<Tb1046IedEntity>) 
				beandao.getListByCriteria(Tb1046IedEntity.class, "f1046Model", "UDC_501A_0001");
		assertTrue(ieds.size()>0);
		Tb1046IedEntity ied = ieds.get(0);
		assertTrue(ied.getF1046boardNum()>0);
		
		List<Tb1047BoardEntity> bds = (List<Tb1047BoardEntity>) 
				beandao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ied);
		assertTrue(bds.size()>0);
		assertTrue(ied.getF1046boardNum()==bds.size());
		
		Tb1047BoardEntity bd = bds.get(0);
		List<Tb1048PortEntity> pts = (List<Tb1048PortEntity>) 
				beandao.getListByCriteria(Tb1048PortEntity.class, "tb1047BoardByF1047Code", bd);
		assertTrue(pts.size()>0);
	}

}
