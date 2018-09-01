package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1048PortEntity;

public class PortEntityService extends BaseService{

	@SuppressWarnings("unchecked")
	public Tb1048PortEntity existsEntity(Tb1048PortEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("", entity.getTb1047BoardByF1047Code());
		params.put("", entity.getF1048Direction());
		params.put("", entity.getF1048No());
		params.put("", entity.getF1048Plug());
		List<Tb1048PortEntity> list = (List<Tb1048PortEntity>) beanDao.getListByCriteria(Tb1048PortEntity.class, params);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
