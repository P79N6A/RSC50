package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;

public class StrapEntityService extends BaseService {

	/**根据IED获取保护压板集合
	 * 
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1064StrapEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1064StrapEntity>) beanDao.getListByCriteria(Tb1064StrapEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
}
