package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;


public class SgfcdaEntityService extends BaseService {

	/**
	 * 获取当前IED下所有保护定值
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1059SgfcdaEntity> getSgfcdaByIed(Tb1046IedEntity iedEntity) {
		List<Tb1057SgcbEntity> sgcbEntities = (List<Tb1057SgcbEntity>) beanDao.getListByCriteria(Tb1057SgcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		return (List<Tb1059SgfcdaEntity>) hqlDao.selectInObjects(Tb1059SgfcdaEntity.class, "tb1057SgcbByF1057Code", sgcbEntities);
	}
	
}
