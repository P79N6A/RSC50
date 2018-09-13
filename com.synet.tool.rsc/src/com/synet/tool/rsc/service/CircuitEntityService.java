package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;

public class CircuitEntityService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1063CircuitEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1063CircuitEntity>) beanDao.getListByCriteria(Tb1063CircuitEntity.class, "tb1046IedByF1046CodeIedRecv", iedEntity);
	}
	
	/**
	 * 
	 * @param iedEntity
	 * @param isSv
	 * @return
	 */
	public List<String> getByIedAndTypes(Tb1046IedEntity iedEntity, boolean isSv) {
//		List<Integer> types = new ArrayList<>();
//		types.add(6);
//		types.add(7);
		List<Tb1063CircuitEntity> byIed = getByIed(iedEntity);
		List<Tb1063CircuitEntity> temp = new ArrayList<>();
//		if(isSv) {
//			for (Tb1063CircuitEntity tb1063CircuitEntity : byIed) {
//				if(types.contains(tb1063CircuitEntity.getF1063Type())) {
//					temp.add(tb1063CircuitEntity);
//				}
//			}
//		} else {
			temp.addAll(byIed);
//		}
		
		List<String> result = new ArrayList<>();
		for (Tb1063CircuitEntity tb1063CircuitEntity : temp) {
			result.add(tb1063CircuitEntity.getTb1061PoutByF1061CodePSend().getF1061Code());
		}
		return result;
	}
	
	public Tb1063CircuitEntity getCircuitEntity(Tb1062PinEntity tb1062PinByF1062CodePRecv) {
		return (Tb1063CircuitEntity) beanDao.getObject(Tb1063CircuitEntity.class, "tb1062PinByF1062CodePRecv", tb1062PinByF1062CodePRecv);
	}

}
