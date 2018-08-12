package com.synet.tool.rsc.service;

import java.util.List;

public class PhyscialAreaService extends BaseService {
	
	public List<?> getListByCriteriaRegionCode(Class<?> clazz, String property, String param) {
		return beanDao.getListLike(clazz, property, param);

	}

}
