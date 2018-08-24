package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;

public class LogicallinkEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1065LogicallinkEntity> getAll() {
		return (List<Tb1065LogicallinkEntity>) beanDao.getAll(Tb1065LogicallinkEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1065LogicallinkEntity> getByRecvIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1065LogicallinkEntity>) beanDao.getListByCriteria(Tb1065LogicallinkEntity.class, "tb1046IedByF1046CodeIedRecv", iedEntity);
	}
	
}
