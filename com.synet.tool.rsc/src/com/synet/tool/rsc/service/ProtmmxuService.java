package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.util.DataUtils;

public class ProtmmxuService extends BaseService {
	
	/**
	 * 根据互感器查找保护采样
	 * @param equipmentEntities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1066ProtmmxuEntity> getProtmmxuByEquEntity(List<Tb1043EquipmentEntity> equipmentEntities) {
		if(!DataUtils.notNull(equipmentEntities)) {
			return new ArrayList<>();
		}
		List<String> lstF1043Code = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : equipmentEntities) {
			lstF1043Code.add(tb1043EquipmentEntity.getF1043Code());
		}
		return (List<Tb1066ProtmmxuEntity>) hqlDao.selectInObjects(Tb1066ProtmmxuEntity.class, "f1043Code", lstF1043Code);
	}

}
