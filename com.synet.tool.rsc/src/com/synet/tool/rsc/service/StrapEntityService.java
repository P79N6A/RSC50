package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
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
	public void addStrap(Tb1016StatedataEntity statedata) {
		Tb1064StrapEntity strap = new Tb1064StrapEntity();
		String strapCode = rscp.nextTbCode(DBConstants.PR_STRAP);
		strap.setF1064Code(strapCode);
		String iedCode = statedata.getParentCode();
		statedata.setParentCode(strapCode);
		strap.setF1046Code(iedCode);
		strap.setF1064Type(statedata.getF1011No());
		strap.setF1064Desc(statedata.getF1016Desc());
		beanDao.insert(strap);
	}
	
	/**
	 * 删除压板
	 * @param statedata
	 */
	public void removeStrap(Tb1061PoutEntity poutEntity) {
		Tb1016StatedataEntity statedata = (Tb1016StatedataEntity) 
				beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", poutEntity.getDataCode());
		if (statedata == null)
			return;
		beanDao.deleteAll(Tb1064StrapEntity.class, "f1064Code", statedata.getParentCode());
		String f1046Code = statedata.getTb1046IedByF1046Code().getF1046Code();
		poutEntity.setParentCode(f1046Code);
		statedata.setParentCode(f1046Code);
		beanDao.update(poutEntity);
		beanDao.update(statedata);
	}
}
