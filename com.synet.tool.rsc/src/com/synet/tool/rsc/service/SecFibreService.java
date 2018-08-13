package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;

public class SecFibreService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1090LineprotfiberEntity> getLineList() {
		return (List<Tb1090LineprotfiberEntity>) beanDao.getAll(Tb1090LineprotfiberEntity.class);
	}

}
