package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.RuleType;

public class StatedataService extends BaseService {
	
	/**
	 * 根据stateDatCode查找
	 * @param dataCodes
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStatedataByDataCodes(List<String> dataCodes) {
		if(!DataUtils.listNotNull(dataCodes)) {
			return new ArrayList<>();
		}
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> statedataEntities = 
			(List<Tb1016StatedataEntity>) hqlDao.selectInObjects(
					Tb1016StatedataEntity.class, "f1016Code", dataCodes);
		return statedataEntities;
	}
	

	
	/**
	 * 根据IED获取
	 * @param iedEntity
	 * @return
	 */
	public List<String> getStateDataByIed(Tb1046IedEntity iedEntity) {
		List<Tb1016StatedataEntity> temp = getStatesByIed(iedEntity);
		List<String> result = new ArrayList<>();
		for (Tb1016StatedataEntity tb1016StatedataEntity : temp) {
			result.add(tb1016StatedataEntity.getF1016Byname());
		}
		return result;
	}
	
	/**
	 * 根据ied查询状态量
	 * @param iedEntity
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStatesByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, "tb1046IedByF1046Code", iedEntity);
	}

	/**
	 * 根据父对象Code获取
	 * @param parentCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1016StatedataEntity> getStateDataByParentCode(String parentCode) {
		return (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, 
				"parentCode", parentCode);
	}
	
	/**
	 * getByCode
	 * @param code
	 * @return
	 */
	public Tb1016StatedataEntity getStateDataByCode(String code) {
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> listByCriteria = (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, "f1016Code", code);
		if(DataUtils.listNotNull(listByCriteria)) {
			return listByCriteria.get(0);
		}
		return null;

	}

	/**
	 * 获取一次设备状态
	 * @param iedEntity
	 * @return
	 */
	public List<Tb1016StatedataEntity> getEqpStateByIed(Tb1046IedEntity iedEntity) {
		String hql = "from " + Tb1016StatedataEntity.class.getName() + " where tb1046IedByF1046Code=:ied and f1011No between :min and :max";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ied", iedEntity);
		params.put("min", RuleType.EQP_STATE.getMin());
		params.put("max", RuleType.EQP_STATE.getMax());
		return (List<Tb1016StatedataEntity>) hqlDao.getListByHql(hql, params);
	}
}
