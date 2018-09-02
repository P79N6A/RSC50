package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.util.DataUtils;

public class MmsfcdaService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1058MmsfcdaEntity> getMmsfcdaByIed(Tb1046IedEntity iedEntitie) {
		return (List<Tb1058MmsfcdaEntity>) beanDao.getListByCriteria(Tb1058MmsfcdaEntity.class, "tb1046IedByF1046Code", iedEntitie);
	}
	
	/**
	 * 根据所属数据集名称获取
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1058MmsfcdaEntity> getMmsdcdaByDataSet(String iedName, String dataSet) {
		if(!DataUtils.strNotNull(iedName) || !DataUtils.strNotNull(dataSet)) {
			return new ArrayList<>();
		}
		List<Tb1054RcbEntity> rcbEntities = (List<Tb1054RcbEntity>) beanDao.getListByCriteria(Tb1054RcbEntity.class, "f1054Dataset", dataSet);
		List<Tb1058MmsfcdaEntity> result = (List<Tb1058MmsfcdaEntity>) hqlDao.selectInObjects(Tb1058MmsfcdaEntity.class, "tb1054RcbByF1054Code", rcbEntities);
		List<Tb1058MmsfcdaEntity> temp = new ArrayList<>();
		for (Tb1058MmsfcdaEntity tb1058MmsfcdaEntity : result) {
			if(iedName.equals(tb1058MmsfcdaEntity.getTb1046IedByF1046Code().getF1046Name())) {
				temp.add(tb1058MmsfcdaEntity);
			}
		}
		return temp;
	}
	
	/**
	 * 根据所属数据集名称和数据类型获取
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1058MmsfcdaEntity> getMmsdcdaByDataSet(String iedName, String dataSet, int dataType) {
		if(!DataUtils.strNotNull(iedName) || !DataUtils.strNotNull(dataSet)) {
			return new ArrayList<>();
		}
		List<Tb1054RcbEntity> rcbEntities = (List<Tb1054RcbEntity>) beanDao.getListByCriteria(Tb1054RcbEntity.class, "f1054Dataset", dataSet);
		List<Tb1058MmsfcdaEntity> result = (List<Tb1058MmsfcdaEntity>) hqlDao.selectInObjects(Tb1058MmsfcdaEntity.class, 
				"tb1054RcbByF1054Code", rcbEntities);
		
		List<Tb1058MmsfcdaEntity> temp = new ArrayList<>();
		for (Tb1058MmsfcdaEntity tb1058MmsfcdaEntity : result) {
			if(iedName.equals(tb1058MmsfcdaEntity.getTb1046IedByF1046Code().getF1046Name())
					&& tb1058MmsfcdaEntity.getF1058DataType() == dataType) {
				temp.add(tb1058MmsfcdaEntity);
			}
		}
		return temp;
	}
	
	public Tb1058MmsfcdaEntity getMmsfcdaByF1058RedAddr(String f1058RefAddr) {
		return (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, "f1058RefAddr", f1058RefAddr);
	}

	public Tb1058MmsfcdaEntity getMmsfcdaByF1058RedAddr(String devName, String f1058RefAddr) {
		Tb1046IedEntity iedEntity = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "devDesc", devName);
		if (iedEntity != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code", iedEntity);
			params.put("f1058RefAddr", f1058RefAddr);
			return (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, params);
		}
		return null;
	}
}
