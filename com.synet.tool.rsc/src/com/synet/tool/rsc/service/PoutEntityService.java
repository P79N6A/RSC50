package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;

public class PoutEntityService extends BaseService{

	/**
	 * 根据条件查找虚端子
	 * @param iedEntity
	 * @param svcbEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1061PoutEntity> getPoutEntityByProperties(Tb1046IedEntity iedEntity, Tb1056SvcbEntity svcbEntity) {
		Map<String, Object> params = new HashMap<>();
		if(iedEntity !=null) {
			params.put("tb1046IedByF1046Code", iedEntity);
		}
		if(svcbEntity != null) {
			params.put("tb1056SvcbByCbCode", svcbEntity);
		}
		if(params.isEmpty()) {
			return new ArrayList<>();
		}
		
		return (List<Tb1061PoutEntity>) beanDao.getListByCriteria(Tb1061PoutEntity.class, params);

	}
	
	public Tb1061PoutEntity getPoutEntity(String devName, String f1061RefAddr) {
		Tb1046IedEntity iedEntity = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "devDesc", devName);
		if (iedEntity != null) {
			Map<String, Object> params = new HashMap<>();
			params.put("tb1046IedByF1046Code", iedEntity);
			params.put("f1061RefAddr", f1061RefAddr);
			return (Tb1061PoutEntity) beanDao.getObject(Tb1061PoutEntity.class, params);
		}
		return null;
	}
}
