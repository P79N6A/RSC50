package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.util.DataUtils;


public class CtvtsecondaryService extends BaseService{
	
	/**
	 * 根据互感器查找互感器次级
	 * @return
	 */
	public List<Tb1067CtvtsecondaryEntity> getCtvtsecondaryEntitiesByEquEntity(List<Tb1043EquipmentEntity> equipmentEntities) {
		if(!DataUtils.listNotNull(equipmentEntities)) {
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

	/**
	 * 删除互感器次级
	 * @param ctvtsecondaryEntity
	 */
	public void delCtvtsecondary(Tb1067CtvtsecondaryEntity ctvtsecondaryEntity) {
		Tb1043EquipmentEntity equipmentEntity = ctvtsecondaryEntity.getTb1043EquipmentByF1043Code();
		Set<Tb1067CtvtsecondaryEntity> setCtvtsecondaryEntities = equipmentEntity.getTb1067SecondarysByF1043Code();
		setCtvtsecondaryEntities.remove(ctvtsecondaryEntity);
		beanDao.update(equipmentEntity);
		beanDao.delete(ctvtsecondaryEntity);
		Object portmmsuEntity = beanDao.getObject(Tb1066ProtmmxuEntity.class, "tb1067CtvtsecondaryByF1067Code", ctvtsecondaryEntity);
		if(portmmsuEntity != null) {
			beanDao.delete(portmmsuEntity);
		}
	}
	
	/**
	 * 添加互感器次级
	 * @param equipment
	 */
	public void addCtvtsecondary(Tb1043EquipmentEntity equipment, Tb1067CtvtsecondaryEntity sec) {
		Set<Tb1067CtvtsecondaryEntity> secs = equipment.getTb1067SecondarysByF1043Code();
		if(secs == null) {
			secs = new HashSet<>();
			equipment.setTb1067SecondarysByF1043Code(secs);
		}
		boolean isNull = (sec == null);
		if(isNull) {
			sec = new Tb1067CtvtsecondaryEntity();
			sec.setF1067Index(1);
		} else {
			sec.setF1067Index(secs.size() + 1);
		}
		secs.add(sec);
		sec.setF1067Code(rscp.nextTbCode(DBConstants.PR_SEC));
		sec.setTb1043EquipmentByF1043Code(equipment);
		sec.setF1067Name(null);
		sec.setF1067Type(null);
		sec.setF1067TermNo(null);
		sec.setF1067CircNo(null);
		sec.setF1067Desc(null);
		if(!isNull) {
			beanDao.insert(sec);
		}
//		addProtMMXU(sec);
		secs.add(sec);
		equipment.setTb1067SecondarysByF1043Code(secs);
	}
	
//	private void addProtMMXU(Tb1067CtvtsecondaryEntity ctvtsecondary) {
//		Tb1066ProtmmxuEntity protmmxu = new Tb1066ProtmmxuEntity();
//		protmmxu.setF1066Code(rscp.nextTbCode(DBConstants.PR_MMXU));
//		protmmxu.setF1067Code(ctvtsecondary.getF1067Code());
//		protmmxu.setF1066Type(DBConstants.MMXU_3I);
//		beanDao.insert(protmmxu);
//	}
	
	public Tb1067CtvtsecondaryEntity getCtvtsecondaryEntitiesByIndex(Integer f1067Index) {
		return (Tb1067CtvtsecondaryEntity) beanDao.getObject(Tb1067CtvtsecondaryEntity.class, "f1067Index", f1067Index);
	}
}
