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
	@SuppressWarnings("unchecked")
	public List<String> getStateDataByIed(Tb1046IedEntity iedEntity) {
		String parentCode = iedEntity.getF1046Code();
		List<Tb1016StatedataEntity> temp = (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, "parentCode", parentCode);
		List<String> result = new ArrayList<>();
		for (Tb1016StatedataEntity tb1016StatedataEntity : temp) {
			result.add(tb1016StatedataEntity.getF1016Byname());
		}
		return result;
	}

}
