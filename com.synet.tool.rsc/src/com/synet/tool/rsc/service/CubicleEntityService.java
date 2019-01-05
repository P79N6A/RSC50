package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.util.ProblemManager;

public class CubicleEntityService extends BaseService {
	
	private ProblemManager pmgr = ProblemManager.getInstance();

	public Tb1050CubicleEntity getCubicleEntityByDesc(String f1050Desc) {
		return (Tb1050CubicleEntity) beanDao.getObject(Tb1050CubicleEntity.class, "f1050Desc", f1050Desc);
	}
	
	public void doImport(List<Tb1049RegionEntity> regions) {
		Map<String, Tb1050CubicleEntity> cbNewCache = new HashMap<>();
		List<Tb1050CubicleEntity> cubicles = new ArrayList<>();
		for (Tb1049RegionEntity reg : regions) {
			cubicles.addAll(reg.getTb1050CubiclesByF1049Code());
		}
		for (Tb1050CubicleEntity cubicle : cubicles) {
			cbNewCache.put(cubicle.getF1050Name(), cubicle);
		}
		// 备份
		List<?> iedList = beanDao.getAll(Tb1046IedEntity.class);
		// 清除IED屏柜关联
		String hql = "update " + Tb1046IedEntity.class.getName() + " set tb1050CubicleEntity=:cb";
		Map<String, Object> params = new HashMap<>();
		params.put("cb", null);
		hqlDao.updateByHql(hql, params);
		// 清除区域屏柜
		beanDao.deleteAll(Tb1050CubicleEntity.class);
		beanDao.deleteAll(Tb1049RegionEntity.class);
		beanDao.insertBatch(regions);
		beanDao.insertBatch(cubicles);
		// 恢复
		for (Object o : iedList) {
			Tb1046IedEntity ied = (Tb1046IedEntity) o;
			Tb1050CubicleEntity cbOld = ied.getTb1050CubicleEntity();
			Tb1050CubicleEntity cbNew = null;
			if (cbOld != null) {
				cbNew = cbNewCache.get(cbOld.getF1050Name());
			}
			ied.setTb1050CubicleEntity(cbNew);
			if (cbNew==null && cbOld!=null) {
				pmgr.appendWarning("装置屏柜导入", "恢复装置屏柜", ied.getF1046Name(), 
						"原屏柜 " + cbOld.getF1050Name() + " 已不存在，请重新指定装置屏柜。");
			}
		}
		beanDao.updateBatch(iedList);
	}
	
	public void doReplace(List<Tb1049RegionEntity> regions) {
		Map<String, Tb1050CubicleEntity> cbNewCache = new HashMap<>();
		List<Tb1050CubicleEntity> cubicles = new ArrayList<>();
		for (Tb1049RegionEntity reg : regions) {
			cubicles.addAll(reg.getTb1050CubiclesByF1049Code());
		}
		for (Tb1050CubicleEntity cubicle : cubicles) {
			cbNewCache.put(cubicle.getF1050Name(), cubicle);
		}
		// 备份
		List<?> iedList = beanDao.getAll(Tb1046IedEntity.class);
		// 清除IED屏柜关联
		String hql = "update " + Tb1046IedEntity.class.getName() + " set tb1050CubicleEntity=:cb";
		Map<String, Object> params = new HashMap<>();
		params.put("cb", null);
		hqlDao.updateByHql(hql, params);
		// 清除区域屏柜
		beanDao.deleteAll(Tb1050CubicleEntity.class);
		beanDao.deleteAll(Tb1049RegionEntity.class);
		beanDao.insertBatch(regions);
		beanDao.insertBatch(cubicles);
		// 恢复
		for (Object o : iedList) {
			Tb1046IedEntity ied = (Tb1046IedEntity) o;
			Tb1050CubicleEntity cbOld = ied.getTb1050CubicleEntity();
			Tb1050CubicleEntity cbNew = null;
			if (cbOld != null) {
				cbNew = cbNewCache.get(cbOld.getF1050Name());
			}
			ied.setTb1050CubicleEntity(cbNew);
			if (cbNew==null && cbOld!=null) {
				pmgr.appendWarning("装置屏柜导入", "恢复装置屏柜", ied.getF1046Name(), 
						"原屏柜 " + cbOld.getF1050Name() + " 已不存在，请重新指定装置屏柜。");
			}
		}
		beanDao.updateBatch(iedList);
	}
}
