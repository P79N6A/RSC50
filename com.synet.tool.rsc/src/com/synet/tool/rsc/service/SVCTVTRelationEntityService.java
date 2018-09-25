package com.synet.tool.rsc.service;

import java.util.HashMap;

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
	
}
