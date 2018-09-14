package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;

public class BoardEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1047BoardEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
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
			params.put("f047Slot", slot);
			return (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
		}
		return null;
	}
	
	public void clearBoardPorts(Tb1047BoardEntity entity) {
		beanDao.deleteAll(Tb1048PortEntity.class, "tb1047BoardByF1047Code", entity);
	}
}
