package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;

public class SvcbEntityService extends BaseService {
	
	/**
	 * 根据设备查找关联SV控制块
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1056SvcbEntity> getSvcbEntityByIedEntity(Tb1046IedEntity iedEntity) {
		return (List<Tb1056SvcbEntity>) beanDao.getListByCriteria(Tb1056SvcbEntity.class, "tb1046IedByF1046Code", iedEntity);
	}

}
