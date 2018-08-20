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
		List<Tb1067CtvtsecondaryEntity> res = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : equipmentEntities) {
//			List<?> its = beanDao.getListByCriteria(Tb1067CtvtsecondaryEntity.class, "tb1043EquipmentByF1043Code", tb1043EquipmentEntity);
//			List<?> its = beanDao.getAll(Tb1067CtvtsecondaryEntity.class);
			res.addAll(tb1043EquipmentEntity.getTb1067SecondarysByF1043Code());
		}
		return res;
	}

}
