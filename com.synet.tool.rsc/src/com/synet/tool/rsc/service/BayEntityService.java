package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1042BayEntity;

public class BayEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1042BayEntity> getBayEntryList() {
		return (List<Tb1042BayEntity>) beanDao.getAll(Tb1042BayEntity.class);
	}

}
