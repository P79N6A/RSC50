package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;

public class BoardEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1047BoardEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
	
	@SuppressWarnings("unchecked")
	public Tb1047BoardEntity existsEntity(Tb1047BoardEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", entity.getTb1046IedByF1046Code());
		params.put("f047Slot", entity.getF1047Slot());
		params.put("f047Desc", entity.getF1047Desc());
		params.put("f1047Type", entity.getF1047Type());
		List<Tb1047BoardEntity> list = (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}
