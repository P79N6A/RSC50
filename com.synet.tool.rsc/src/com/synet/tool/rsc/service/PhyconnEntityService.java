package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1053PhysconnEntity;

public class PhyconnEntityService extends BaseService {

	public void saveBatch(List<Tb1053PhysconnEntity> list) {
		beanDao.saveBatch(list);
	}

	public Tb1053PhysconnEntity existEntity(Tb1053PhysconnEntity physconnEntity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1048PortByF1048CodeA", physconnEntity.getTb1048PortByF1048CodeA());
		params.put("tb1048PortByF1048CodeB", physconnEntity.getTb1048PortByF1048CodeB());
		return (Tb1053PhysconnEntity) beanDao.getObject(Tb1053PhysconnEntity.class, params);
	}
}
