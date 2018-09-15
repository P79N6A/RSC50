package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1051CableEntity;

public class CableEntityService extends BaseService{

	public void saveBatch(List<Tb1051CableEntity> list) {
		beanDao.saveBatch(list);
	}
	
	public Tb1051CableEntity existEntity(Tb1051CableEntity cableEntity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1051Name", cableEntity.getF1051Name());
		params.put("f1051CoreNum", cableEntity.getF1051CoreNum());
		return (Tb1051CableEntity) beanDao.getObject(Tb1051CableEntity.class, params);
	}
}
