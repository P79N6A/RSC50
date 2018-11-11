package com.synet.tool.rsc.excel;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.found.file.excel.SheetsHandler;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.excel.handler.RegionHandler;
import com.synet.tool.rsc.model.IM102FibreListEntity;
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
	
	@Test
	public void testRead1090() {
		ImportInfoParser parser = new ImportInfoParser();
		List<Tb1090LineprotfiberEntity> result = parser.getLineprotfiberList("C:\\Users\\36576\\Desktop\\sub_cyb20180306\\test.xlsx");
		System.out.println(result);
		System.out.println(parser.getErrorMsg());
	}
	
	@Test
	public void testImpFibreList() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(7, ExcelConstants.IM102_CABLE_CODE);
		map.put(6, ExcelConstants.IM102_CORE_CODE);
		map.put(0, ExcelConstants.IM102_BOARD_PORT_CODEA);
		map.put(1, ExcelConstants.IM102_DEV_NAMEA);
		ImportInfoParser parser = new ImportInfoParser();
		ImportResult result = parser.getFibreList("C:\\Users\\36576\\Desktop\\光缆清册.xlsx", 2, map);
		if (result != null) {
			Map<String, List<IM102FibreListEntity>> temp = (Map<String, List<IM102FibreListEntity>>) result.getResult();
			if (temp != null) {
				for (String key : temp.keySet()) {
					List<IM102FibreListEntity> list = temp.get(key);
					System.out.println("------------------" + key + "----------------------");
					System.out.println("板卡号，端口号，装置Name，纤芯编号，光纤编号");
					for (IM102FibreListEntity entity : list) {
						System.out.println(entity.getBoardCodeA() + "，" +
								entity.getPortCodeA() + "，" + entity.getDevNameA() + "，" 
								+ entity.getCoreCode() + "，" + entity.getCableCode());
					}
					break;
				}
			}
		}
		System.out.println(result.getResult());
		System.out.println(parser.getErrorMsg());
	}
	
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
