package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;

public class PhyscialAreaService extends BaseService {
	
	public List<Tb1050CubicleEntity> getCubicleList(Tb1049RegionEntity entity) {
		return getCubicleList(entity, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1050CubicleEntity> getCubicleList(Tb1049RegionEntity entity, String cubicleName, String cubicleDesc) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1049RegionByF1049Code", entity);
		if (cubicleName != null) {
			params.put("f1050Name", cubicleName);
		}
		if (cubicleDesc != null) {
			params.put("f1050Desc", cubicleDesc);
		}
		return (List<Tb1050CubicleEntity>) beanDao.getListByCriteria(Tb1050CubicleEntity.class, params);
	}
	
	public List<Tb1051CableEntity> getCableList(List<Tb1050CubicleEntity> cubicleEntities) {
		return getCableList(cubicleEntities, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1051CableEntity> getCableList(List<Tb1050CubicleEntity> cubicleEntities, String cableName, String cableDesc) {
		List<Tb1051CableEntity> result = new ArrayList<>();
		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
			List<Tb1051CableEntity> tempList = new ArrayList<>();
			for (Tb1050CubicleEntity entity : cubicleEntities) {
				Map<String, Object> paramsA = new HashMap<String, Object>();
				paramsA.put("tb1050CubicleByF1050CodeA", entity);
				if (cableName != null) {
					paramsA.put("f1051Name", cableName);
				}
				if (cableDesc != null) {
					paramsA.put("f1051Desc", cableDesc);
				}
				List<Tb1051CableEntity> list1 = (List<Tb1051CableEntity>) beanDao.getListByCriteria(Tb1051CableEntity.class, paramsA);
				if (list1 != null)
					tempList.addAll(list1);
				Map<String, Object> paramsB = new HashMap<String, Object>();
				paramsB.put("tb1050CubicleByF1050CodeB", entity);
				if (cableName != null) {
					paramsB.put("f1051Name", cableName);
				}
				if (cableDesc != null) {
					paramsB.put("f1051Desc", cableDesc);
				}
				List<Tb1051CableEntity> list2 = (List<Tb1051CableEntity>) beanDao.getListByCriteria(Tb1051CableEntity.class, paramsB);
				if (list2 != null)
					tempList.addAll(list2);

			}
			//去重处理
			if (!tempList.isEmpty()){
				for (Tb1051CableEntity temp : tempList) {
					boolean b = true;
					for (Tb1051CableEntity cableEntity : result) {
						if (cableEntity.getF1051Code().equals(temp.getF1051Code())) {
							b = false;
							break;
						}
					}
					if (b) {
						result.add(temp);
					}
				}
			}
		}
		return result;
	}
	
	public List<Tb1053PhysconnEntity> getPhysconnList(List<Tb1050CubicleEntity> cubicleEntities) {//, List<Tb1051CableEntity> cableEntities
		return getPhysconnList(cubicleEntities, null, null);
	}

	@SuppressWarnings("unchecked")
	public List<Tb1053PhysconnEntity> getPhysconnList(List<Tb1050CubicleEntity> cubicleEntities, String iedName, String iedDesc) {//, List<Tb1051CableEntity> cableEntities
		Set<Tb1053PhysconnEntity> temSet = new HashSet<>();
//		List<String> portCodes = new ArrayList<>();
//		List<Tb1052CoreEntity> tempCores = new ArrayList<>();
//		List<Tb1048PortEntity> tempPorts = new ArrayList<>();
//		//根据屏柜查找线芯
//		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
//			List<Tb1052CoreEntity> temp = (List<Tb1052CoreEntity>) hqlDao.selectInObjects(Tb1052CoreEntity.class, "tb1050CubicleByParentCode", cubicleEntities);
//			if (temp != null) {
//				tempCores.addAll(temp);
//			}
//		}
//		//根据光纤查找线芯
//		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
//			List<Tb1052CoreEntity> temp = (List<Tb1052CoreEntity>) hqlDao.selectInObjects(Tb1052CoreEntity.class, "tb1051CableByParentCode", cableEntities);
//			if (temp != null) {
//				tempCores.addAll(temp);
//			}
//		}
//		//根据线芯查找端口
//		if (!tempCores.isEmpty()){
//			for (Tb1052CoreEntity coreEntity : tempCores) {
//				String f1048CodeA = coreEntity.getF1048CodeA();
//				String f1048CodeB = coreEntity.getF1048CodeB();
//				if (f1048CodeA != null && !portCodes.contains(f1048CodeA)) {
//					portCodes.add(f1048CodeA);
//				}
//				if (f1048CodeB != null && !portCodes.contains(f1048CodeB)) {
//					portCodes.add(f1048CodeB);
//				}
//			}
//		}
		
		//根据屏柜查找装置
		List<Tb1046IedEntity> tempIeds =  (List<Tb1046IedEntity>) hqlDao.selectInObjects(Tb1046IedEntity.class, "tb1050CubicleEntity", cubicleEntities);
		if (tempIeds == null || tempIeds.size() <= 0) return null;
		//sql查询接口不满足，手动过滤
		List<Tb1046IedEntity> ieds = new ArrayList<>();
		for (Tb1046IedEntity ied : tempIeds) {
			if (iedName != null) {
				if (!iedName.equals(ied.getF1046Name())) {
					continue;
				}
			}
			if (iedDesc != null) {
				if (!iedDesc.equals(ied.getF1046Desc())) {
					continue;
				}
			}
			ieds.add(ied);
		}
		if (ieds == null || ieds.size() <= 0) return null;
		//根据装置查找板卡
		List<Tb1047BoardEntity> boards = (List<Tb1047BoardEntity>) hqlDao.selectInObjects(Tb1047BoardEntity.class, "tb1046IedByF1046Code", ieds);
		if (boards == null || boards.size() <= 0) return null;
		//根据板卡查找端口
		List<Tb1048PortEntity> ports = (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class, "tb1047BoardByF1047Code", boards);
		if (ports == null || ports.size() <= 0) return null;
//		//根据f1048Code查找端口
//		if (!portCodes.isEmpty()) {
//			tempPorts = (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class, "f1048Code", portCodes);
//		}
		
		//根据端口查找物理回路
		if (!ports.isEmpty()) {
			List<Tb1053PhysconnEntity> temp1 = (List<Tb1053PhysconnEntity>) hqlDao.selectInObjects(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", ports);
			if (temp1 != null) {
				temSet.addAll(temp1);
			}
			List<Tb1053PhysconnEntity> temp2 = (List<Tb1053PhysconnEntity>) hqlDao.selectInObjects(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", ports);
			if (temp2 != null) {
				temSet.addAll(temp2);
			}
		}
		if (temSet.isEmpty()) {
			return null;
		}
		List<Tb1053PhysconnEntity> result = new ArrayList<>();
		result.addAll(temSet);
		return result;
	}
}
