package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1074SVCTVTRelationEntity;

public class SVCTVTRelationEntityService extends BaseService{

	/**
	 * 检查关联关系是否存在，存在返回true,不存在返回false
	 * @param secCtvtsecondaryEntity
	 * @param poutEntity
	 * @return
	 */
	public boolean relationExistCheck(Tb1067CtvtsecondaryEntity secCtvtsecondaryEntity, Tb1061PoutEntity poutEntity) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("tb1067CtvtsecondaryByF1067Code", secCtvtsecondaryEntity);
		params.put("f1061Code", poutEntity);
		Object object = beanDao.getObject(Tb1074SVCTVTRelationEntity.class, params);
		return object == null ? false : true;
	}
	
	public List<Tb1074SVCTVTRelationEntity> queryRelationsBySecd(Tb1067CtvtsecondaryEntity secCtvtsecondaryEntity) {
		return (List<Tb1074SVCTVTRelationEntity>) 
				beanDao.getListByCriteria(Tb1074SVCTVTRelationEntity.class, "tb1067CtvtsecondaryByF1067Code", secCtvtsecondaryEntity);
	}

	public void savePinOuts(Tb1067CtvtsecondaryEntity secCtvtsecondaryEntity, List<Tb1061PoutEntity> poutList) {
		beanDao.deleteAll(Tb1074SVCTVTRelationEntity.class, "tb1067CtvtsecondaryByF1067Code", secCtvtsecondaryEntity);
		List<Tb1074SVCTVTRelationEntity> relations = new ArrayList<>();
		for (Tb1061PoutEntity tb1061PoutEntity : poutList) {
			Tb1074SVCTVTRelationEntity relationEntity = new Tb1074SVCTVTRelationEntity(secCtvtsecondaryEntity, tb1061PoutEntity);
			relationEntity.setF1074Code(rscp.nextTbCode(DBConstants.PR_SVCVR));
			relations.add(relationEntity);
		}
		beanDao.insertBatch(relations);
		List<Tb1074SVCTVTRelationEntity> relSet = new ArrayList<>();
		relSet.addAll(relations);
		secCtvtsecondaryEntity.setSvRelations(relSet);
	}
}
