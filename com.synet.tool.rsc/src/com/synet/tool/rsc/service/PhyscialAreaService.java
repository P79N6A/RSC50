package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;

public class PhyscialAreaService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1050CubicleEntity> getCubicleList(Tb1049RegionEntity entity) {
		return (List<Tb1050CubicleEntity>) beanDao.getListByCriteria(Tb1050CubicleEntity.class, "tb1049RegionByF1049Code", entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tb1051CableEntity> getCableList(List<Tb1050CubicleEntity> cubicleEntities) {
		List<Tb1051CableEntity> result = new ArrayList<>();
		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
			List<Tb1051CableEntity> tempList = new ArrayList<>();
			for (Tb1050CubicleEntity entity : cubicleEntities) {
				List<Tb1051CableEntity> list1 = (List<Tb1051CableEntity>) beanDao.getListByCriteria(Tb1051CableEntity.class, "tb1050CubicleByF1050CodeA", entity);
				if (list1 != null)
					tempList.addAll(list1);
				List<Tb1051CableEntity> list2 = (List<Tb1051CableEntity>) beanDao.getListByCriteria(Tb1051CableEntity.class, "tb1050CubicleByF1050CodeB", entity);
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

	@SuppressWarnings("unchecked")
	public List<Tb1053PhysconnEntity> getPhysconnList(List<Tb1050CubicleEntity> cubicleEntities, List<Tb1051CableEntity> cableEntities) {
		Set<Tb1053PhysconnEntity> temSet = new HashSet<>();
		List<String> portCodes = new ArrayList<>();
		List<Tb1052CoreEntity> tempCores = new ArrayList<>();
		List<Tb1048PortEntity> tempPorts = new ArrayList<>();
		//根据屏柜查找线芯
		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
			List<Tb1052CoreEntity> temp = (List<Tb1052CoreEntity>) hqlDao.selectInObjects(Tb1052CoreEntity.class, "tb1050CubicleByParentCode", cubicleEntities);
			if (temp != null) {
				tempCores.addAll(temp);
			}
		}
		//根据光纤查找线芯
		if (cubicleEntities != null && !cubicleEntities.isEmpty()) {
			List<Tb1052CoreEntity> temp = (List<Tb1052CoreEntity>) hqlDao.selectInObjects(Tb1052CoreEntity.class, "tb1051CableByParentCode", cableEntities);
			if (temp != null) {
				tempCores.addAll(temp);
			}
		}
		//根据线芯查找端口
		if (!tempCores.isEmpty()){
			for (Tb1052CoreEntity coreEntity : tempCores) {
				String f1048CodeA = coreEntity.getF1048CodeA();
				String f1048CodeB = coreEntity.getF1048CodeB();
				if (f1048CodeA != null && !portCodes.contains(f1048CodeA)) {
					portCodes.add(f1048CodeA);
				}
				if (f1048CodeB != null && !portCodes.contains(f1048CodeB)) {
					portCodes.add(f1048CodeB);
				}
			}
		}
		
		//根据f1048Code查找端口
		if (!portCodes.isEmpty()) {
			tempPorts = (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class, "f1048Code", portCodes);
		}
		
		//根据端口查找物理回路
		if (!tempPorts.isEmpty()) {
			List<Tb1053PhysconnEntity> temp1 = (List<Tb1053PhysconnEntity>) hqlDao.selectInObjects(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", tempPorts);
			if (temp1 != null) {
				temSet.addAll(temp1);
			}
			List<Tb1053PhysconnEntity> temp2 = (List<Tb1053PhysconnEntity>) hqlDao.selectInObjects(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", tempPorts);
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
