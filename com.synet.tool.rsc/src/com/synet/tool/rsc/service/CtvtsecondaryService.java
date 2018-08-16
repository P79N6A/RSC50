package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.util.DataUtils;


public class CtvtsecondaryService extends BaseService{
	
	/**
	 * 根据互感器查找互感器次级
	 * @return
	 */
	public List<Tb1067CtvtsecondaryEntity> getCtvtsecondaryEntitiesByEquEntity(List<Tb1043EquipmentEntity> equipmentEntities) {
		if(!DataUtils.notNull(equipmentEntities)) {
			return new ArrayList<>();
		}
		List<String> lstF1043Code = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : equipmentEntities) {
			lstF1043Code.add(tb1043EquipmentEntity.getF1043Code());
		}
		@SuppressWarnings("unchecked")
		List<Tb1067CtvtsecondaryEntity> res = (List<Tb1067CtvtsecondaryEntity>)hqlDao.selectInObjects(Tb1067CtvtsecondaryEntity.class, "f1043Code", lstF1043Code);
		return res;
	}

}
