package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;

public class BayEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1042BayEntity> getBayEntryList() {
		return (List<Tb1042BayEntity>) beanDao.getListByCriteria(Tb1042BayEntity.class, "deleted", 0);
	}
	
	/**
	 * 按名称查询Bay
	 * @param f1042Name
	 * @return
	 */
	public Tb1042BayEntity getBayEntityByName(String f1042Name) {
		return (Tb1042BayEntity) beanDao.getObject(Tb1042BayEntity.class, "f1042Name", f1042Name);
	}

	/**
	 * 添加Bay（增量/非增量）
	 * @param station
	 * @param ivol
	 * @param bayName
	 * @param bayDesc
	 * @return
	 */
	public Tb1042BayEntity addBay(Tb1041SubstationEntity station, int ivol,
			String bayName, String bayDesc) {
		Tb1042BayEntity bay = new Tb1042BayEntity();
		bay.setF1042Code(rscp.nextTbCode(DBConstants.PR_BAY));
		bay.setF1042Name(bayName);
		bay.setF1042Desc(bayDesc);
		bay.setF1042Voltage(ivol);
		bay.setTb1041SubstationByF1041Code(station);
		beanDao.insert(bay);	// 避免IED更新bay信息后查询出错
		return bay;
	}
	
	/**
	 * 增量删除Bay
	 * @param bayName
	 */
	public void deleteBay(String bayName) {
		String hql = "update " + Tb1042BayEntity.class.getName() + 
				" set deleted=1 where f1042Name=:f1042Name";
		Map<String, Object> params = new HashMap<>();
		params.put("f1042Name", bayName);
		hqlDao.updateByHql(hql, params);
	}

	/**
	 * 增量修改Bay
	 * @param bayName
	 * @param disInfo
	 */
	public void updateBay(String bayName, Map<String, String> disInfo) {
		if (disInfo.get("desc") != null) {
			Tb1042BayEntity bayEntity = getBayEntityByName(bayName);
			bayEntity.setF1042Desc(disInfo.get("desc"));
			beanDao.update(bayEntity);
		}
	}
}
