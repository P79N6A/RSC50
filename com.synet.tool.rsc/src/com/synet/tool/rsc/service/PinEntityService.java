package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;

public class PinEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1062PinEntity> getByIed(Tb1046IedEntity ied) {
		return (List<Tb1062PinEntity>) beanDao.getListByCriteria(Tb1062PinEntity.class, "tb1046IedByF1046Code", ied);
	}

}
