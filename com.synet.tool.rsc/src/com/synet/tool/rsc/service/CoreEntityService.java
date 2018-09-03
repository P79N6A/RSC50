package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1052CoreEntity;

public class CoreEntityService extends BaseService {

	public void saveBatch(List<Tb1052CoreEntity> list) {
		beanDao.saveBatch(list);
	}
}
