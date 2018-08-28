package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.util.DataUtils;

public class EquipmentEntityService extends BaseService{
	
	/**
	 * 根据间隔，查询间隔下所有互感器
	 * @param bayEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1043EquipmentEntity> getEquipmentEntitysByBayEntity(Tb1042BayEntity bayEntity) {
		if(bayEntity == null) {
			return new ArrayList<>();
		}
		return (List<Tb1043EquipmentEntity>) beanDao.getListByCriteria(Tb1043EquipmentEntity.class, "tb1042BayByF1042Code", bayEntity);
	}

	/**
	 * 根据开关刀闸状态，查找互感器
	 * @param statedataEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Tb1043EquipmentEntity getEquipmentByStateData(Tb1016StatedataEntity statedataEntity) {
		String code = statedataEntity.getParentCode();
		List<Tb1043EquipmentEntity> res = (List<Tb1043EquipmentEntity>) beanDao.getListByCriteria(Tb1043EquipmentEntity.class, "f1043Code", code);
		if(DataUtils.listNotNull(res)) {
			return res.get(0);
		}
		return null;
	}
	
	/**
	 * 获取模拟量通道互感器 06 07
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEquipmentByType() {
		List<Integer> types = new ArrayList<>();
		types.add(6);
		types.add(7);
		List<Tb1043EquipmentEntity> temp = (List<Tb1043EquipmentEntity>) hqlDao.selectInObjects(Tb1043EquipmentEntity.class, "f1043Type", types);
		List<String> result = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : temp) {
			result.add(tb1043EquipmentEntity.getF1043Name());
		}
		return result;
	}
}
