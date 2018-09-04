package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1053PhysconnEntity;

public class PhyconnEntityService extends BaseService {

	public void saveBatch(List<Tb1053PhysconnEntity> list) {
		beanDao.saveBatch(list);
	}
}
