package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class SecProBrkService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1093VoltagekkEntity> getProBrkList() {
		return (List<Tb1093VoltagekkEntity>) beanDao.getAll(Tb1093VoltagekkEntity.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1093VoltagekkEntity> getIotermListByIedParams(String f1046Model, String f1046Name){
		List<Tb1093VoltagekkEntity> result= new ArrayList<>();
		if (f1046Model == null && f1046Name == null){
			result = (List<Tb1093VoltagekkEntity>) beanDao.getAll(Tb1093VoltagekkEntity.class);
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
		List<Tb1061PoutEntity> temp2 = (List<Tb1061PoutEntity>) hqlDao.selectInObjects(Tb1061PoutEntity.class,
				"tb1046IedByF1046Code", temp);
		if (temp2 == null || temp2.isEmpty()) {
			return result;
		}
		Set<Tb1067CtvtsecondaryEntity> tempSet = new HashSet<>();
		List<Tb1067CtvtsecondaryEntity> ctvtsecondaryList = new ArrayList<>();
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeA1", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeA2", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeB1", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeB2", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeC1", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		ctvtsecondaryList = (List<Tb1067CtvtsecondaryEntity>) hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class,
				"tb1061PoutByF1061CodeC2", temp2);
		if (ctvtsecondaryList != null){
			tempSet.addAll(ctvtsecondaryList);
		}
		
		if (tempSet.isEmpty()) {
			return result;
		}
		List<Tb1067CtvtsecondaryEntity> temp3 = new ArrayList<>();
		temp3.addAll(tempSet);
		
		result = (List<Tb1093VoltagekkEntity>) hqlDao.selectInObjects(Tb1093VoltagekkEntity.class, 
				"tb1067CtvtsecondaryByF1067Code", temp3);

		return result;
	}

}
