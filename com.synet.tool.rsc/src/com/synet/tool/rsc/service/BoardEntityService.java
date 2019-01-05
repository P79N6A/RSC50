package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;

public class BoardEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1047BoardEntity> getByIed(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity);
		String hql = "from " + Tb1047BoardEntity.class.getName() + 
				" where tb1046IedByF1046Code=:ied and deleted=0";
		return (List<Tb1047BoardEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public Tb1047BoardEntity existsEntity(Tb1047BoardEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", entity.getTb1046IedByF1046Code());
		params.put("f1047Slot", entity.getF1047Slot());
		return (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
		
	}
	
	public Tb1047BoardEntity existsEntity(String devName, String slot) {
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (ied != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code",ied);
			params.put("f1047Slot", slot);
			return (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
		}
		return null;
	}
	
}
