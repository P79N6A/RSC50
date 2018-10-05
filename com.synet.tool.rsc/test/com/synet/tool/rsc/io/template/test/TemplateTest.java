package com.synet.tool.rsc.io.template.test;

import org.junit.Before;
import org.junit.Test;

import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.io.TemplateExport;
import com.synet.tool.rsc.io.TemplateImport;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class TemplateTest {
	
	private Tb1046IedEntity iedEntity;

	@Before
	public void before() {
		String prj = "shangwu";
		ProjectManager prjmgr = ProjectManager.getInstance();
		if (!prjmgr.exists(prj)) {
			prjmgr.initDb(prj);
		} else {
			prjmgr.openDb(prj);
		}
		iedEntity = new Tb1046IedEntity("111", "222", "333");
	}
	
	
	@Test
	public void testTemplateExport() {
		TemplateExport templateExport = new TemplateExport(iedEntity);
		templateExport.execute();
	}
	
	@Test
	public void testTemplateImport() {
		TemplateImport templateImport = new TemplateImport(iedEntity);
		templateImport.execute(null);
	}

}
