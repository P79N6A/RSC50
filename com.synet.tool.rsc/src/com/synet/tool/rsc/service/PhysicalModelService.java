package com.synet.tool.rsc.service;

import java.util.List;
import com.synet.tool.rsc.model.Tb1049RegionEntity;

/**
 * 
 * 用于树节点数据初始化
 *
 */
public class PhysicalModelService extends BaseService{
	
	@SuppressWarnings("unchecked")
	public List<Tb1049RegionEntity> getAllRegionList() {
		List<Tb1049RegionEntity> list = (List<Tb1049RegionEntity>) beanDao.getAll(Tb1049RegionEntity.class);
		return list;
	}
}
