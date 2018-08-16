package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.util.DataUtils;

public class StatedataService extends BaseService {
	
	/**
	 * 根据stateDatCode查找
	 * @param dataCodes
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStatedataByDataCodes(List<String> dataCodes) {
		if(!DataUtils.notNull(dataCodes)) {
			return new ArrayList<>();
		}
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> statedataEntities = 
			(List<Tb1016StatedataEntity>) hqlDao.selectInObjects(
					Tb1016StatedataEntity.class, "f1016Code", dataCodes);
		return statedataEntities;
	}
	
	/**
	 * 根据互感器查找
	 * @param equEntities
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStateDataByEquips(List<Tb1043EquipmentEntity> equEntities) {
		if(!DataUtils.notNull(equEntities)) {
			return new ArrayList<>();
		}
		List<String> equitCodes = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : equEntities) {
			equitCodes.add(tb1043EquipmentEntity.getF1043Code());
		}
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> statedataEntities = 
		(List<Tb1016StatedataEntity>) hqlDao.selectInObjects(
				Tb1016StatedataEntity.class, "f1043Code", equitCodes);
		return statedataEntities;

	}

}
