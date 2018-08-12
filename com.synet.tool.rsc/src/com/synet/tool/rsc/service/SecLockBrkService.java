package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1091IotermEntity;

public class SecLockBrkService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermList() {
		return (List<Tb1091IotermEntity>) beanDao.getAll(Tb1091IotermEntity.class);
	}

}
