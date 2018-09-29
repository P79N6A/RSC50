package com.synet.tool.rsc.io.scd.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.shrcn.found.common.dict.DictManager;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.das.ProjectManager;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.processor.ImportFibreListProcessor3;
import com.synet.tool.rsc.processor.LogcalAndPhyconnProcessor;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportFibreTest {

	private BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	private HqlDaoImpl hqlDao = HqlDaoImpl.getInstance();
//	private RSCProperties rscp = RSCProperties.getInstance();
	
	@Before
	public void before() {
		DictManager.getInstance().init(getClass(), RSCConstants.DICT_PATH);
		
//		String scdPath = "./test/sub_shangwu.scd";
//		XMLDBHelper.loadDocument("shangwu", scdPath);
		String prj = "p0928";
		ProjectManager prjmgr = ProjectManager.getInstance();
		if (!prjmgr.exists(prj)) {
			prjmgr.initDb(prj);
		} else {
			prjmgr.openDb(prj);
			BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
			beanDao.deleteAll(Tb1051CableEntity.class);
			beanDao.deleteAll(Tb1052CoreEntity.class);
			beanDao.deleteAll(Tb1053PhysconnEntity.class);
			beanDao.deleteAll(Tb1073LlinkphyrelationEntity.class);
		}
	}

	@Test
	public void testImport() {
		ImprotInfoService improtInfoService = new ImprotInfoService();
		List<IM100FileInfoEntity> fileInfoEntities = improtInfoService.getFileInfoEntityList(DBConstants.FILE_TYPE102);
		assertTrue(fileInfoEntities != null && fileInfoEntities.size() > 0);
		List<IM102FibreListEntity> temp = improtInfoService.getFibreListEntityList(fileInfoEntities.get(0));
		assertTrue(temp != null && temp.size() > 0);
		//导入数据
		new ImportFibreListProcessor3().importData(temp);
		int countPhy = hqlDao.getCount(Tb1053PhysconnEntity.class);
		assertTrue(countPhy == 48);
		int countCb = hqlDao.getCount(Tb1051CableEntity.class);
		assertTrue(countCb == 46);
		int countCr = hqlDao.getCount(Tb1052CoreEntity.class);
		assertTrue(countCr > 50);
		
//		//处理逻辑链路与物理回路关联(处理全部)
//		new LogcalAndPhyconnProcessor().analysis();
//		//确定TB1055_GCB和TB1056_SVCB表中F1071_CODE所代表的采集单元Code
//		new LogcalAndPhyconnProcessor().analysisGCBAndSVCB();
//		assertClassNum(Tb1073LlinkphyrelationEntity.class);
	}
	
	private void assertClassNum(Class<?> cls) {
		assertTrue(hqlDao.getCount(cls) > 0);
	}
}
