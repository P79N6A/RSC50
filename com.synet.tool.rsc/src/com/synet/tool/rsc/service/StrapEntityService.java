package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
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
	public Tb1064StrapEntity addStrap(Tb1016StatedataEntity statedata) {
		boolean replaceMode = RSCProperties.getInstance().isReplaceMode();
		Tb1064StrapEntity strap = null;
		boolean recover = false;
		if (replaceMode) {
			strap = getStrapEntity(statedata);
		}
		if (strap == null) {
			String strapCode = rscp.nextTbCode(DBConstants.PR_STRAP);
			strap = new Tb1064StrapEntity();
			strap.setF1064Code(strapCode);
			statedata.setParentCode(strapCode);
		} else {
			recover = true;
		}
		int typeId = statedata.getF1011No();
		String iedCode = statedata.getTb1046IedByF1046Code().getF1046Code();
		strap.setF1046Code(iedCode);
		strap.setF1064Type(typeId);
		strap.setF1064Desc(statedata.getF1016Desc());
		statedata.setF1011No(typeId);
		if (recover) {
			beanDao.markRecovered(strap);
		} else {
			beanDao.insert(strap);
		}
		beanDao.update(statedata);
		return strap;
	}
	
	/**
	 * 查询压板
	 * @param statedata
	 * @return
	 */
	public Tb1064StrapEntity getStrapEntity(Tb1016StatedataEntity statedata) {
		return (Tb1064StrapEntity) beanDao.getObject(
			Tb1064StrapEntity.class, "f1064Code", statedata.getParentCode());
	}
	
	/**
	 * 删除压板
	 * @param statedata
	 */
	public void removeStrap(Tb1016StatedataEntity statedata) {
		boolean replaceMode = RSCProperties.getInstance().isReplaceMode();
		if (replaceMode) {
			beanDao.markDeleted(Tb1064StrapEntity.class, "f1064Code", statedata.getParentCode());
		} else {
			beanDao.deleteAll(Tb1064StrapEntity.class, "f1064Code", statedata.getParentCode());
		}
		String iedCode = statedata.getTb1046IedByF1046Code().getF1046Code();
		statedata.setParentCode(iedCode);
		beanDao.update(statedata);
	}
}
