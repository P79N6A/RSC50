package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1041SubstationEntity;

public class SubstationService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1041SubstationEntity> getAllSubstation() {
		return (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
	}
}
