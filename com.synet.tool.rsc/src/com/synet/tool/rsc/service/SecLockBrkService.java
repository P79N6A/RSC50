package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;

public class SecLockBrkService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermList() {
		return (List<Tb1091IotermEntity>) beanDao.getAll(Tb1091IotermEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermListByIedParams(String f1046Type, String f1046Name){
		List<Tb1091IotermEntity> result= new ArrayList<>();
		if (f1046Type == null && f1046Name == null){
			result = (List<Tb1091IotermEntity>) beanDao.getAll(Tb1091IotermEntity.class);
			return result;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (f1046Type != null) {
			params.put("f1046Type", Integer.parseInt(f1046Type));
		}
		if (f1046Name != null) {
			params.put("f1046Name", f1046Name);
		}
		List<Tb1046IedEntity> temp = (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, params);
		if (temp == null || temp.isEmpty()){
			return result;
		}
		result = (List<Tb1091IotermEntity>) hqlDao.selectInObjects(Tb1091IotermEntity.class,
				"tb1046IedByF1046Code", temp);
		return result;
	}
	
}
