package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1051CableEntity;

public class CableEntityService extends BaseService{

	public void saveBatch(List<Tb1051CableEntity> list) {
		beanDao.saveBatch(list);
	}
}
