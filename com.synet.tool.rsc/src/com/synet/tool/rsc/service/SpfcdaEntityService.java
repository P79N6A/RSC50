package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;

public class SpfcdaEntityService extends BaseService {

	/**
	 * 根据IED获取保护参数集合
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1060SpfcdaEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1060SpfcdaEntity>) beanDao.getListByCriteria(Tb1060SpfcdaEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
}
