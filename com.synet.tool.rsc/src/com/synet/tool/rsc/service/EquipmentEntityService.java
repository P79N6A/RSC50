package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.EnumEquipmentType;
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
		types.add(EnumEquipmentType.CTR.getCode());
		types.add(EnumEquipmentType.VTR.getCode());
		List<Tb1043EquipmentEntity> temp = (List<Tb1043EquipmentEntity>) hqlDao.selectInObjects(Tb1043EquipmentEntity.class, "f1043Type", types);
		List<String> result = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : temp) {
			result.add(tb1043EquipmentEntity.getF1043Name());
		}
		return result;
	}
	
	/**
	 * 获取模拟量通道互感器 06 07
	 * @return
	 */
	public List<Tb1043EquipmentEntity> getEquByTypeAndBay(Tb1042BayEntity bayEntity) {
		List<Tb1043EquipmentEntity> equipmentEntitys = getEquipmentEntitysByBayEntity(bayEntity);
		List<Integer> types = new ArrayList<>();
		types.add(EnumEquipmentType.CTR.getCode());
		types.add(EnumEquipmentType.VTR.getCode());
		List<Tb1043EquipmentEntity> result = new ArrayList<>();
		for (Tb1043EquipmentEntity tb1043EquipmentEntity : equipmentEntitys) {
			if(types.contains(tb1043EquipmentEntity.getF1043Type())) {
				result.add(tb1043EquipmentEntity);
			}
		}
		return result;
	}
	
	/**
	 * 增量添加设备
	 * @param bay
	 * @param nodeName
	 * @param eqpName
	 * @param disInfo
	 */
	public void addEquip(Tb1042BayEntity bay, String nodeName, String eqpName, Map<String, String> disInfo) {
		String stype = disInfo.get("type");
		EnumEquipmentType type = null;
		switch(nodeName) {
		case "PowerTransformer":
			type = EnumEquipmentType.PTR;
			break;
		case "ConductingEquipment":
			type = EnumEquipmentType.getType(stype);
			break;
		default:
			break;
		}
		Tb1043EquipmentEntity equipment = new Tb1043EquipmentEntity();
		String eqpCode = rscp.nextTbCode(DBConstants.PR_EQP);
		equipment.setF1043Code(eqpCode);
		equipment.setTb1042BayByF1042Code(bay);
		equipment.setF1043Name(eqpName);
		equipment.setF1043Desc(disInfo.get("desc"));
		String virtual = disInfo.get("virtual");
		boolean isv = false;
		if (!StringUtil.isEmpty(virtual)) {
			isv = Boolean.parseBoolean(virtual);
		}
		equipment.setF1043IsVirtual(isv ? 1 : 0);
		equipment.setF1043Type(type.getCode());
		beanDao.insert(equipment);
	}
	
	public void deleteEquip(Tb1042BayEntity bay, String eqpName) {
		String hql = "update " + Tb1043EquipmentEntity.class.getName() +
				" set deleted=1 where tb1042BayByF1042Code=:bay and f1043Name=:eqpName";
		Map<String, Object> params = new HashMap<>();
		params.put("bay", bay);
		params.put("eqpName", eqpName);
		hqlDao.updateByHql(hql, params);
	}
	
	public Tb1043EquipmentEntity getEquipment(Tb1042BayEntity bay, String eqpName) {
		Map<String, Object> params = new HashMap<>();
		params.put("tb1042BayByF1042Code", bay);
		params.put("f1043Name", eqpName);
		return (Tb1043EquipmentEntity) beanDao.getObject(Tb1043EquipmentEntity.class, params);
	}
	
	public void udpateEquip(Tb1042BayEntity bay, String nodeName, String eqpName, Map<String, String> disInfo) {
		Tb1043EquipmentEntity equipment = getEquipment(bay, eqpName);
		if (disInfo.get("type") != null) {
		String stype = disInfo.get("type");
			EnumEquipmentType type = null;
			switch(nodeName) {
			case "PowerTransformer":
				type = EnumEquipmentType.PTR;
				break;
			case "ConductingEquipment":
				type = EnumEquipmentType.getType(stype);
				break;
			default:
				break;
			}
			equipment.setF1043Type(type.getCode());
		}
		if (disInfo.get("desc") != null) {
			equipment.setF1043Desc(disInfo.get("desc"));
		}
		if (disInfo.get("newName") != null) {
			equipment.setF1043Name(disInfo.get("newName"));
			equipment.setF1043Desc(disInfo.get("newDesc"));
		}
		if (disInfo.get("virtual") != null) {
			String virtual = disInfo.get("virtual");
			boolean isv = false;
			if (!StringUtil.isEmpty(virtual)) {
				isv = Boolean.parseBoolean(virtual);
			}
			equipment.setF1043IsVirtual(isv ? 1 : 0);
		}
		beanDao.update(equipment);
	}
}
