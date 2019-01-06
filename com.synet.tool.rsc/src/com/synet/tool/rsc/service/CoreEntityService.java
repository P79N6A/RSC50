package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;

public class CoreEntityService extends BaseService {

	public void saveBatch(List<Tb1052CoreEntity> list) {
		beanDao.insertBatch(list);
	}

	public Tb1052CoreEntity existEntity(Tb1052CoreEntity coreEntity) {
		return getCoreEntity(coreEntity.getTb1051CableByParentCode(), coreEntity.getF1052No());
	}
	
	public Tb1052CoreEntity getCoreEntity(Tb1051CableEntity cable, int f1052No) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1051CableByParentCode", cable);
		params.put("f1052No", f1052No);
		return (Tb1052CoreEntity) beanDao.getObject(Tb1052CoreEntity.class, params);
	}
}
