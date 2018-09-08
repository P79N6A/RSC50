package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;

public class PinEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1062PinEntity> getByIed(Tb1046IedEntity ied) {
		return (List<Tb1062PinEntity>) beanDao.getListByCriteria(Tb1062PinEntity.class, "tb1046IedByF1046Code", ied);
	}

	public Tb1062PinEntity getPinEntity(String devName, String f1062RefAddr) {
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (ied != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code", ied);
			params.put("f1062RefAddr", f1062RefAddr);
			return (Tb1062PinEntity) beanDao.getObject(Tb1062PinEntity.class, params);
		}
		return null;
	}
	
	/**
	 * 根根据关联的保护压板查询
	 * @param straps
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1062PinEntity> getByStraps(List<Tb1064StrapEntity> straps) {
		return (List<Tb1062PinEntity>) hqlDao.selectInObjects(Tb1062PinEntity.class, "tb1064StrapByF1064Code", straps);
	}
}
