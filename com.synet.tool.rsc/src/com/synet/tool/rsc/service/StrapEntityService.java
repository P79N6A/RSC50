package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
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
	
	/**
	 * 添加压板
	 * @param statedata
	 * @param desc
	 */
	public void addStrap(Tb1016StatedataEntity statedata, String desc) {
		Tb1064StrapEntity strap = new Tb1064StrapEntity();
		String strapCode = rscp.nextTbCode(DBConstants.PR_STRAP);
		strap.setF1064Code(strapCode);
		String iedCode = statedata.getParentCode();
		statedata.setParentCode(strapCode);
		strap.setF1046Code(iedCode);
		strap.setF1064Type(statedata.getF1011No());
		strap.setF1064Desc(desc);
		beanDao.insert(strap);
	}
}
