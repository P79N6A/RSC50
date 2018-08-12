package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1092PowerkkEntity;

public class SecPwrBrkService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1092PowerkkEntity> getPowerkkList(){
		return (List<Tb1092PowerkkEntity>) beanDao.getAll(Tb1092PowerkkEntity.class);
	}
}
