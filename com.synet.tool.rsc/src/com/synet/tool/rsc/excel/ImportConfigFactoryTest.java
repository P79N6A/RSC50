package com.synet.tool.rsc.excel;

import static org.junit.Assert.*;

import org.junit.Test;

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
