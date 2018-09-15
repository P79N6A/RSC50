package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1052CoreEntity;

public class CoreEntityService extends BaseService {

	public void saveBatch(List<Tb1052CoreEntity> list) {
		beanDao.saveBatch(list);
	}

	public Tb1052CoreEntity existEntity(Tb1052CoreEntity coreEntity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1051CableByParentCode", coreEntity.getTb1051CableByParentCode());
		params.put("f1052No", coreEntity.getF1052No());
		return (Tb1052CoreEntity) beanDao.getObject(Tb1052CoreEntity.class, params);
	}
}
