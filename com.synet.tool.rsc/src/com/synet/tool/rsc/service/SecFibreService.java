package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.excel.ImportInfoParser;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;

public class SecFibreService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1090LineprotfiberEntity> getLineList() {
		return (List<Tb1090LineprotfiberEntity>) beanDao.getAll(Tb1090LineprotfiberEntity.class);
	}

	
	@SuppressWarnings("unchecked")
	public List<Tb1090LineprotfiberEntity> getLineListByIedParams(String f1046Model, String f1046Name){
		List<Tb1090LineprotfiberEntity> result= new ArrayList<>();
		if (f1046Model == null && f1046Name == null){
			result = (List<Tb1090LineprotfiberEntity>) beanDao.getAll(Tb1090LineprotfiberEntity.class);
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
		result = (List<Tb1090LineprotfiberEntity>) hqlDao.selectInObjects(Tb1090LineprotfiberEntity.class,
				"tb1046IedByF1046Code", temp);
		return result;
	}
	
	public List<Tb1090LineprotfiberEntity> importData(String filePath) {
		List<Tb1090LineprotfiberEntity> result = new ArrayList<>();
		result = new ImportInfoParser().getLineprotfiberList(filePath);
		beanDao.insertBatch(result);
		return result;
		
	}
	
	public void exportData(List<Tb1090LineprotfiberEntity> list, String filePath) {
//		ExcelUtils.e
	}
	

}
