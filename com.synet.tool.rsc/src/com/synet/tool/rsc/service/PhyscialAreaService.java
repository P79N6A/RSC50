package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;

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

}
