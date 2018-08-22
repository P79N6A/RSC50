package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1069RcdchannelaEntity;

public class RcdchannelaEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1069RcdchannelaEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1069RcdchannelaEntity>) beanDao.getListByCriteria(Tb1069RcdchannelaEntity.class, "tb1046IedByIedCode", iedEntity);
	}

}
