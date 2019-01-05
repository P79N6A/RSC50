package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1072RcdchanneldEntity;

public class RcdchanneldEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1072RcdchanneldEntity> getByIed(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity);
		String hql = "from " + Tb1072RcdchanneldEntity.class.getName() + 
				" where tb1046IedByIedCode=:ied and deleted=0";
		return (List<Tb1072RcdchanneldEntity>) hqlDao.getListByHql(hql, params);
	}

}
