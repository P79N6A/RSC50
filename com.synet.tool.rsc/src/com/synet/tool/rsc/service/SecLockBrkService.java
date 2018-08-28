package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.excel.ImportInfoParser;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;

public class SecLockBrkService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermList() {
		return (List<Tb1091IotermEntity>) beanDao.getAll(Tb1091IotermEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1091IotermEntity> getIotermListByIedParams(String f1046Model, String f1046Name){
		List<Tb1091IotermEntity> result= new ArrayList<>();
		if (f1046Model == null && f1046Name == null){
			result = (List<Tb1091IotermEntity>) beanDao.getAll(Tb1091IotermEntity.class);
			return result;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		if (f1046Model != null) {
			params.put("f1046Model", f1046Model);
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
	
	public List<Tb1091IotermEntity> importData(String filePath) {
		List<Tb1091IotermEntity> result = new ArrayList<>();
		result = new ImportInfoParser().getIotermList(filePath);
		return result;
		
	}
	
	public void exportData(List<Tb1091IotermEntity> list, String filePath) {
//		ExcelUtils.e
	}

}
