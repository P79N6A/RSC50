package com.synet.tool.rsc.excel.test;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.excel.ImportInfoParser;
import com.synet.tool.rsc.excel.TableHeadParser;
import com.synet.tool.rsc.excel.handler.RegionHandler;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;
import com.synet.tool.rsc.util.ExcelReaderUtil;

public class ImportInfoTest {
	private BeanDaoImpl beandao;
	private PhyscialAreaService service;
	
	@Before
	public void before() {
		String prj = "shangwu";
		ProjectManager prjmgr = ProjectManager.getInstance();
		if (!prjmgr.exists(prj)) {
			prjmgr.initDb(prj);
		} else {
			prjmgr.openDb(prj);
		}
		beandao = BeanDaoImpl.getInstance();
		service = new PhyscialAreaService();
		DictManager dictmgr = DictManager.getInstance();
		dictmgr.init(getClass(), RSCConstants.DICT_PATH, true);
	}
	
//	@Test
//	public void testRead1090() {
//		ImportInfoParser parser = new ImportInfoParser();
//		List<Tb1090LineprotfiberEntity> result = parser.getLineprotfiberList("C:\\Users\\36576\\Desktop\\sub_cyb20180306\\test.xlsx");
//		System.out.println(result);
//		System.out.println(parser.getErrorMsg());
//	}
	
	@Test
	public void testTableHead() {
		TableHeadParser parser = new TableHeadParser();
		Map<Integer, String> temp = parser.getTableHeadInfo("C:\\Users\\36576\\Desktop\\光缆清册2.xlsx", 2);
		System.out.println(temp);
	}
	
	@Test
	public void testRegionImport() {
		String xlspath = "F:/工程问题/rsc/基础数据整理(1).xlsx";
		SheetsHandler handler = new RegionHandler();
		List<?> result = ExcelReaderUtil.parseByHandler(xlspath, handler);
		assertTrue(result.size() > 0);
	}
}
