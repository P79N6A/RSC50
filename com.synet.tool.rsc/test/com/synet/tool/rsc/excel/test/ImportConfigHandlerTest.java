package com.synet.tool.rsc.excel.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.excel.ImportConfig;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.excel.handler.ImportConfigHandler;
import com.synet.tool.rsc.util.ExcelReaderUtil;

public class ImportConfigHandlerTest {

	@Before
	public void before() {
		String dbName = "shangwu";
		ProjectManager instance = ProjectManager.getInstance();
//		instance.initDb(dbName);
		instance.openDb(dbName);
	}
	
	@Test
	public void testImportConfigHandler() {
		String xlspath = "./test/excel/caihong/物理连接图1106.xlsx";
		ImportConfig config = ImportConfigFactory.getFiberImportConfig();
		ImportConfigHandler handler = new ImportConfigHandler(1, config);
		List<?> result = ExcelReaderUtil.parseByHandler(xlspath, handler);
		assertTrue(result.size() > 1);
	}

}
