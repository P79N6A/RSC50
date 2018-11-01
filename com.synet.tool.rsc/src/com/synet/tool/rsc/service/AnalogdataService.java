package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.RuleType;

public class AnalogdataService extends BaseService {
	
	/**
	 * 根据codes查找
	 * @param codes
	 * @return
	 */
	public Tb1006AnalogdataEntity getAnologByCodes(String code) {
		return (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", code);
	}
	
	public List<Tb1006AnalogdataEntity> getMeasDataByIed(Tb1046IedEntity iedEntity) {
		String hql = "from " + Tb1006AnalogdataEntity.class.getName() + " where tb1046IedByF1046Code=:ied and f1011No between :min and :max";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ied", iedEntity);
		params.put("min", RuleType.PROT_MS.getMin());
		params.put("max", RuleType.PROT_MS.getMax());
		return (List<Tb1006AnalogdataEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public List<Tb1006AnalogdataEntity> getAnologByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1006AnalogdataEntity>) beanDao.getListByCriteria(Tb1006AnalogdataEntity.class, "tb1046IedByF1046Code", iedEntity);
	}
	
	/**
	 * 根据IED获取
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAnologNameByIed(Tb1046IedEntity iedEntity) {
		List<String> result = new ArrayList<>();
		List<Tb1006AnalogdataEntity> temp = getAnologByIed(iedEntity);
		for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : temp) {
			result.add(tb1006AnalogdataEntity.getF1006Byname());
		}
		return result;
	}
	

}
