package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1042BayEntity;

public class BayEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1042BayEntity> getBayEntryList() {
		return (List<Tb1042BayEntity>) beanDao.getListByCriteria(Tb1042BayEntity.class, "deleted", 0);
	}
	
	public Tb1042BayEntity getBayEntityByName(String f1042Name) {
		return (Tb1042BayEntity) beanDao.getObject(Tb1042BayEntity.class, "f1042Name", f1042Name);
	}

}
