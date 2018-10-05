package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.tool.found.das.impl.HqlDaoImpl;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;

public class IedEntityService extends BaseService {
	
	/**
	 * 根据类型和间隔获取IED
	 * @param types
	 * @param bayEntity
	 * @return
	 */
	public List<Tb1046IedEntity> getIedByTypesAndBay(int[] types, Tb1042BayEntity bayEntity) {
		//所有间隔下的合并单元
		List<Tb1046IedEntity> iedEntityByTypes = getIedEntityByTypes(types);
		if(bayEntity == null){
			return iedEntityByTypes;
		}
		List<Tb1046IedEntity> result = new ArrayList<>();
		for (Tb1046IedEntity tb1046IedEntity : iedEntityByTypes) {
			//匹配对应的间隔
			if(tb1046IedEntity.getTb1042BaysByF1042Code().getF1042Code().equals(bayEntity.getF1042Code())) {
				result.add(tb1046IedEntity);
			}
		}
		return result;
	}
	
	/**
	 * 删除IED及其关联对象（仅适用于交换机、光配单元、采集器）
	 * @param iedEntity
	 */
	public void deleteTb1046IedEntity(Tb1046IedEntity iedEntity) {
		deleteBoards(iedEntity);
		beanDao.delete(iedEntity);
	}

	/**
	 * 删除IED关联对象
	 * @param iedEntity
	 */
	public void deleteBoards(Tb1046IedEntity iedEntity) {
		List<Tb1047BoardEntity> boards = (List<Tb1047BoardEntity>) beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
		for (Tb1047BoardEntity board : boards) {
			List<Tb1048PortEntity> ports = (List<Tb1048PortEntity>) beanDao.getListByCriteria(Tb1048PortEntity.class, "tb1047BoardByF1047Code", board);
			for (Tb1048PortEntity port : ports) {
				beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeA", port);
				beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeB", port);
				List<Tb1053PhysconnEntity> physA = (List<Tb1053PhysconnEntity>) beanDao.getListByCriteria(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", port);
				List<Tb1053PhysconnEntity> physB = (List<Tb1053PhysconnEntity>) beanDao.getListByCriteria(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", port);
				if (physA == null) {
					physA = new ArrayList<>();
				}
				if (physB != null) {
					physA.addAll(physB);
				}
				for (Tb1053PhysconnEntity phy : physA) {
					beanDao.deleteAll(Tb1073LlinkphyrelationEntity.class, "tb1053PhysconnByF1053Code", phy);
				}
				beanDao.deleteBatch(physA);
			}
			beanDao.deleteBatch(ports);
		}
		beanDao.deleteBatch(boards);
	}
	
	/**
	 * 根据设备类型查找设备
	 * @param types
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByTypes(int[] types) {
		if(types.length < 1) {
			return new ArrayList<>();
		}
		List<Integer> lstType = new ArrayList<>();
		for (int i : types) {
			lstType.add(i);
		}
		return (List<Tb1046IedEntity>) hqlDao.selectInObjects(Tb1046IedEntity.class, "f1046Type", lstType);
	}
	
	/**
	 * 根据所属间隔查询IED
	 * @param bayEntityCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedEntityByBay(Tb1042BayEntity bayEntity) {
		if(bayEntity == null) {
			return (List<Tb1046IedEntity>) beanDao.getAll(Tb1046IedEntity.class);
		}
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, "tb1042BaysByF1042Code", bayEntity);
	}
	
	/**
	 * 更新间隔编号
	 * @param ied
	 */
	public void updateIEDBayCode(String f1046Code, String f1042Code) {
		String hql = "update " + Tb1046IedEntity.class.getName() +
				" set f1042Code=:f1042Code" + 
				" where f1046Code=:f1046Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1042Code", f1042Code);
		params.put("f1046Code", f1046Code);
		HqlDaoImpl.getInstance().updateByHql(hql, params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1046IedEntity> getIedByIM103IEDBoard(IM103IEDBoardEntity entity) {
		Map<String, Object> params = new HashMap<>();
		if (!StringUtil.isEmpty(entity.getManufacturor()))
			params.put("f1046Manufacturor", entity.getManufacturor());
		params.put("f1046Model", entity.getDevName());
		if (!StringUtil.isEmpty(entity.getConfigVersion()))
			params.put("f1046ConfigVersion", entity.getConfigVersion());
		return (List<Tb1046IedEntity>) beanDao.getListByCriteria(Tb1046IedEntity.class, params);
	}

	public Tb1046IedEntity getIedEntityByDevName(String devName) {
		if (devName == null)
			return null;
		Object object = beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		return (Tb1046IedEntity) object;
	}
	
	public void updateBoardNum(Tb1046IedEntity ied) {
		int num = hqlDao.getCount(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ied);
		ied.setF1046boardNum(num);
		beanDao.update(ied);
	}
}
