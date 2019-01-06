package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;

public class RegionEntityService extends BaseService {
	
	public Tb1049RegionEntity getRegionEntity(String name) {
		return (Tb1049RegionEntity) beanDao.getObject(Tb1049RegionEntity.class, "f1049Name", name);
	}
	
	public void addRegion(Tb1049RegionEntity region) {
		region.setF1049Code(rscp.nextTbCode(DBConstants.PR_REGION));
		beanDao.insert(region);
	}
	
	public void deleteRegion(String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1049Name", name);
		String hql = "update " + Tb1049RegionEntity.class.getName() + 
				" set deleted=1 where f1049Name=:f1049Name";
		hqlDao.updateByHql(hql, params);
	}
	
	public void updateRegion(String name, Tb1049RegionEntity regionNew) {
		Tb1049RegionEntity regionOld = getRegionEntity(name);
		if (regionNew.getF1049Desc() != null) {
			regionOld.setF1049Desc(regionNew.getF1049Desc());
		}
		if (regionNew.getF1049Area() != null) {
			regionOld.setF1049Area(regionNew.getF1049Area());
		}
		beanDao.update(regionOld);
	}
}
