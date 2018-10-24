package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.DataUtils;

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
		List<Tb1016StatedataEntity> temp = getStateDataByParentCode(iedEntity.getF1046Code());
		List<String> result = new ArrayList<>();
		for (Tb1016StatedataEntity tb1016StatedataEntity : temp) {
			result.add(tb1016StatedataEntity.getF1016Byname());
		}
		return result;
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

}
