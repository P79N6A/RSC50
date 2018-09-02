package com.synet.tool.rsc.service;

import com.synet.tool.rsc.model.Tb1050CubicleEntity;

public class CubicleEntityService extends BaseService {

	public Tb1050CubicleEntity getCubicleEntityByDesc(String f1050Desc) {
		return (Tb1050CubicleEntity) beanDao.getObject(Tb1050CubicleEntity.class, "f1050Desc", f1050Desc);
	}
}
