package com.synet.tool.rsc.excel.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.synet.tool.rsc.excel.ImportConfig;
import com.synet.tool.rsc.excel.ImportConfigFactory;
import com.synet.tool.rsc.excel.ImportField;

public class ImportConfigFactoryTest {

	@Test
	public void testGetFiberImportConfig() {
		ImportConfig fiberImportConfig = ImportConfigFactory.getFiberImportConfig();
		ImportField importField = fiberImportConfig.getEntityPropties().get(Integer.MAX_VALUE);
		assertNotNull(importField);
		String[] excelFields = fiberImportConfig.getExcelFields();
		for (String f : excelFields) {
			System.out.println(f);
		}
	}

}
