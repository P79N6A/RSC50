package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1072RcdchanneldEntity;

public class RcdchanneldEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1072RcdchanneldEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1072RcdchanneldEntity>) beanDao.getListByCriteria(Tb1072RcdchanneldEntity.class, "tb1046IedByIedCode", iedEntity);
	}

}
