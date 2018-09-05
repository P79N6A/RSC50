package com.synet.tool.rsc.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;

import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.service.PhyscialAreaService;

public class ImportInfoTest {
	private BeanDaoImpl beandao;
	private PhyscialAreaService service;
	
//	@Before
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
		System.out.println(parser.getHandler().getErrorMsg());
	}
}
