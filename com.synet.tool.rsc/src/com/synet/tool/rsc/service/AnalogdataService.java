package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.DataUtils;

public class AnalogdataService extends BaseService {
	
	/**
	 * 根据codes查找
	 * @param codes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1006AnalogdataEntity> getAnologByCodes(List<String> codes) {
		if(!DataUtils.listNotNull(codes)) {
			return new ArrayList<>();
		}
		return (List<Tb1006AnalogdataEntity>) hqlDao.selectInObjects(Tb1006AnalogdataEntity.class, "f1006Code", codes);
	}
	
	/**
	 * 根据codes查找
	 * @param codes
	 * @return
	 */
	public Tb1006AnalogdataEntity getAnologByCodes(String code) {
		return (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", code);
	}
	
	/**
	 * 根据IED获取
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAnologByIed(Tb1046IedEntity iedEntity) {
		String parentCode = iedEntity.getF1046Code();
		List<String> result = new ArrayList<>();
		List<Tb1006AnalogdataEntity> temp = (List<Tb1006AnalogdataEntity>) beanDao.getListByCriteria(Tb1006AnalogdataEntity.class, "parentCode", parentCode);
		for (Tb1006AnalogdataEntity tb1006AnalogdataEntity : temp) {
			result.add(tb1006AnalogdataEntity.getF1006Byname());
		}
		return result;
	}
	

}
