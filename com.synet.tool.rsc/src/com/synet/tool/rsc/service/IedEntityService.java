package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

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
	public List<Tb1046IedEntity> getIedEntityByBay(String bayEntityCode) {
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, "f1042Code", bayEntityCode);
	}

}
