package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class IedEntityService extends BaseService {
	
	/**
	 * 根据设备类型查找设备
	 * @param types
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByTypes(int[] types) {
		if(types.length < 1) {
			return new ArrayList<>();
		}
		List<Integer> lstType = new ArrayList<>();
		for (int i : types) {
			lstType.add(i);
		}
		return (List<Tb1046IedEntity>) hqlDao.selectInObjects(Tb1046IedEntity.class, "f1046Type", lstType);
	}
	
	/**
	 * 根据所属间隔查询IED
	 * @param bayEntityCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByBay(Tb1042BayEntity bayEntity) {
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, "tb1042BaysByF1042Code", bayEntity);
	}
	
	/**
	 * 更新间隔编号
	 * @param ied
	 */
	public void updateIEDBayCode(Tb1046IedEntity ied) {
		String hql = "update " + ied.getClass().getName() +
				" set f1042Code=:f1042Code" + 
				" where f1046Code=:f1046Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1042Code", ied.getF1042Code());
		params.put("f1046Code", ied.getF1046Code());
		HqlDaoImpl.getInstance().updateByHql(hql, params);
	}
	
	@SuppressWarnings("unchecked")
	public Tb1046IedEntity getIedByIM103IEDBoard(IM103IEDBoardEntity entity) {
		Map<String, Object> params = new HashMap<>();
		params.put("f1046Manufacturor", entity.getManufacturor());
		params.put("f1046Model", entity.getDevName());
		params.put("f1046ConfigVersion", entity.getConfigVersion());
		List<Tb1046IedEntity> list = (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, params);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

}
