package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;

public class BoardEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1047BoardEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
	
}
