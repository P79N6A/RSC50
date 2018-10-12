package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;

public class PhyconnEntityService extends BaseService {

	public void saveBatch(List<Tb1053PhysconnEntity> list) {
		beanDao.insertBatch(list);
	}

	public Tb1053PhysconnEntity existEntity(Tb1053PhysconnEntity physconnEntity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1048PortByF1048CodeA", physconnEntity.getTb1048PortByF1048CodeA());
		params.put("tb1048PortByF1048CodeB", physconnEntity.getTb1048PortByF1048CodeB());
		return (Tb1053PhysconnEntity) beanDao.getObject(Tb1053PhysconnEntity.class, params);
	}
	
	//根据接收端端口查找物理回路
	public Tb1053PhysconnEntity getByTb1048PortByF1048CodeB(Tb1048PortEntity tb1048PortByF1048CodeB) {
		if (tb1048PortByF1048CodeB == null) return null;
		return (Tb1053PhysconnEntity) beanDao.getObject(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", tb1048PortByF1048CodeB);
	}
	
	//根据发送端端口查找物理回路
	public Tb1053PhysconnEntity getByTb1048PortByF1048CodeA(Tb1048PortEntity tb1048PortByF1048CodeA) {
		if (tb1048PortByF1048CodeA == null) return null;
		return (Tb1053PhysconnEntity) beanDao.getObject(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", tb1048PortByF1048CodeA);
	}
}
