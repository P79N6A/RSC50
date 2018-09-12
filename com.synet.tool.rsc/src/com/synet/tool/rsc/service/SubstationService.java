package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1041SubstationEntity;

public class SubstationService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1041SubstationEntity> getAllSubstation() {
		return (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public Tb1041SubstationEntity getCurrSubstation() {
		List<Tb1041SubstationEntity> staList = (List<Tb1041SubstationEntity>) beanDao.getAll(Tb1041SubstationEntity.class);
		if (staList != null && staList.size() > 0) {
			return staList.get(0);
		}
		return null;
	}
}
