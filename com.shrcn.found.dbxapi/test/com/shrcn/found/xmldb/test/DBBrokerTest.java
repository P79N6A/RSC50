package com.shrcn.found.xmldb.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.TimeCounter;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.xmldb.ProjectManager;
import com.shrcn.found.xmldb.XMLDBHelper;

public class DBBrokerTest {

	@Test
	public void testClose() {
		fail("尚未实现");
	}

	@Test
	public void testGetNamespace() {
		fail("尚未实现");
	}

	@Test
	public void testSetNamespace() {
		fail("尚未实现");
	}

	@Test
	public void testGetDocument() {
		fail("尚未实现");
	}

	@Test
	public void testSetDocument() {
		fail("尚未实现");
	}

	@Test
	public void testGetProject() {
		fail("尚未实现");
	}

	@Test
	public void testSetProject() {
		fail("尚未实现");
	}

	@Test
	public void testGetDocXPath() {
		fail("尚未实现");
	}

	@Test
	public void testExistsDocument() {
		fail("尚未实现");
	}

	@Test
	public void testLoadDocument() {
//		Constants.DEFAULT_PRJECT_NAME = "Sub_JinNan_New";
//		DBAdminBroker admin = new XMLDBAdminImpl();
//		admin.getCollection();
//		DBBroker dbHelper = new XMLDBHelperImpl();
//		assertNotNull(dbHelper.selectSingleNode("/SCL/IED"));
		
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
				
				String prjName = "test1";
				Constants.DEFAULT_PRJECT_NAME = prjName;
				String scdfile = "C:\\Documents and Settings\\chenchun\\桌面\\test1.scd";
				FileManipulate.clearDir(".\\data");
				
				TimeCounter.begin();
				if (!ProjectManager.existProject(prjName))
					ProjectManager.initPrjectDB(prjName);
				try {
					XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdfile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				TimeCounter.end("SCD加载");
//			}});
//		t.start();
//		try {
//			Thread.sleep(15000);
//			if (t.isAlive())
//				t.stop();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Test
	public void testLoadElementAsDocument() {
		fail("尚未实现");
	}

	@Test
	public void testLoadDocFromString() {
		fail("尚未实现");
	}

	@Test
	public void testRemoveDocument() {
		fail("尚未实现");
	}

	@Test
	public void testExportFormatedDocStringStringString() {
		fail("尚未实现");
	}

	@Test
	public void testExportFormatedDocString() {
		fail("尚未实现");
	}

	@Test
	public void testSelectNodesString() {
		fail("尚未实现");
	}

	@Test
	public void testSelectNodesStringIntInt() {
		fail("尚未实现");
	}

	@Test
	public void testSelectSingleNode() {
		fail("尚未实现");
	}

	@Test
	public void testExistsNode() {
		fail("尚未实现");
	}

	@Test
	public void testCountNodes() {
		fail("尚未实现");
	}

	@Test
	public void testInsertBeforeStringElement() {
		fail("尚未实现");
	}

	@Test
	public void testInsertBeforeStringString() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAfterStringElement() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAfterStringString() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAfterType() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsFirstStringElement() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsFirstStringString() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsLastStringElement() {
		fail("尚未实现");
	}

	@Test
	public void testInsertAsLastStringString() {
		fail("尚未实现");
	}

	@Test
	public void testMoveUp() {
		fail("尚未实现");
	}

	@Test
	public void testMoveDown() {
		fail("尚未实现");
	}

	@Test
	public void testMoveTo() {
		fail("尚未实现");
	}

	@Test
	public void testInsertStringElement() {
		fail("尚未实现");
	}

	@Test
	public void testInsertStringString() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceNode() {
		fail("尚未实现");
	}

	@Test
	public void testDeleteNodes() {
		fail("尚未实现");
	}

	@Test
	public void testSaveAttribute() {
		fail("尚未实现");
	}

	@Test
	public void testSaveAttributeValue() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeValue() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeValues() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeStrings() {
		fail("尚未实现");
	}

	@Test
	public void testGetAttributeString() {
		fail("尚未实现");
	}

	@Test
	public void testGetNodeValue() {
		fail("尚未实现");
	}

	@Test
	public void testAppendAttribute() {
		fail("尚未实现");
	}

	@Test
	public void testQueryNodes() {
		fail("尚未实现");
	}

	@Test
	public void testQueryAttributes() {
		fail("尚未实现");
	}

	@Test
	public void testQueryAttribute() {
		fail("尚未实现");
	}

	@Test
	public void testExecuteUpdate() {
		fail("尚未实现");
	}

	@Test
	public void testUpdate() {
		fail("尚未实现");
	}

	@Test
	public void testIsAutoCommit() {
		fail("尚未实现");
	}

	@Test
	public void testSetAutoCommit() {
		fail("尚未实现");
	}

	@Test
	public void testForceBegin() {
		fail("尚未实现");
	}

	@Test
	public void testForceCommit() {
		fail("尚未实现");
	}

	@Test
	public void testForceRollback() {
		fail("尚未实现");
	}

	@Test
	public void testInsertNodeByDoc() {
		fail("尚未实现");
	}

	@Test
	public void testReplaceNodeByDoc() {
		fail("尚未实现");
	}

}
