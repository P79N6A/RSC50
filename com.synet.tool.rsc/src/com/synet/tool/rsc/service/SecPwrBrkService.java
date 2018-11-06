package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;

public class SecPwrBrkService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1092PowerkkEntity> getPowerkkList(){
		return (List<Tb1092PowerkkEntity>) beanDao.getAll(Tb1092PowerkkEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1092PowerkkEntity> getIotermListByIedParams(String f1046Type, String f1046Name){
		List<Tb1092PowerkkEntity> result= new ArrayList<>();
		if (f1046Type == null && f1046Name == null){
			return result;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (f1046Type != null) {
			params.put("f1046Type", Integer.parseInt(f1046Type));
		}
		if (f1046Name != null) {
			params.put("f1046Name", f1046Name);
		}
		List<Tb1046IedEntity> temp = (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1092PowerkkEntity.class, params);
		if (temp == null || temp.isEmpty()){
			return result;
		}
		result = (List<Tb1092PowerkkEntity>) hqlDao.selectInObjects(Tb1092PowerkkEntity.class,
				"tb1046IedByF1046Code", temp);
		return result;
	}
	
}
