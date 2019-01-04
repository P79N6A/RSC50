package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;

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
	
	/**
	 * 根据Pin查询虚回路
	 * @param tb1062PinByF1062CodePRecv
	 * @return
	 */
	public Tb1063CircuitEntity getCircuitEntity(Tb1062PinEntity tb1062PinByF1062CodePRecv) {
		return (Tb1063CircuitEntity) beanDao.getObject(Tb1063CircuitEntity.class, "tb1062PinByF1062CodePRecv", tb1062PinByF1062CodePRecv);
	}

	/**
	 * 添加虚回路
	 * @param pin
	 * @param pout
	 * @return
	 */
	public void addCircuitEntity(Tb1062PinEntity pin, Tb1061PoutEntity pout) {
		BaseCbEntity cbEntity = pout.getCbEntity();
		Tb1065LogicallinkEntity logiclink = (Tb1065LogicallinkEntity) beanDao.getObject(Tb1065LogicallinkEntity.class, "baseCbByCdCode", cbEntity);
		Tb1063CircuitEntity circuit = getCircuitEntity(pin);
		if (circuit == null) {
			createCircuit(logiclink, pin, pout);
			beanDao.insert(circuit);
		} else {
			beanDao.markRecovered(circuit);
		}
	}

	/**
	 * 创建虚回路
	 * @param logiclink
	 * @param pin
	 * @param pout
	 * @return
	 */
	public Tb1063CircuitEntity createCircuit(Tb1065LogicallinkEntity logiclink, Tb1062PinEntity pin,
			Tb1061PoutEntity pout) {
		Tb1046IedEntity iedResv = logiclink.getTb1046IedByF1046CodeIedRecv(); 
		Tb1046IedEntity iedSend = logiclink.getTb1046IedByF1046CodeIedSend(); 
		Tb1063CircuitEntity circuit = new Tb1063CircuitEntity();
		circuit.setF1063Code(rscp.nextTbCode(DBConstants.PR_CIRCUIT));
		circuit.setTb1065LogicallinkByF1065Code(logiclink);
		circuit.setTb1046IedByF1046CodeIedRecv(iedResv);
		circuit.setTb1046IedByF1046CodeIedSend(iedSend);
		circuit.setTb1061PoutByF1061CodePSend(pout);
		circuit.setTb1062PinByF1062CodePRecv(pin);
		return circuit;
	}
	
	/**
	 * 更新虚回路
	 * @param pin
	 * @param pout
	 */
	public void updateCircuitEntity(Tb1062PinEntity pin, Tb1061PoutEntity pout) {
		Tb1063CircuitEntity circuit = getCircuitEntity(pin);
		if (circuit != null) {
			circuit.setTb1061PoutByF1061CodePSend(pout);
			beanDao.update(circuit);
		}
	}
	
	/**
	 * 删除虚回路
	 * @param pin
	 */
	public void deleteCircuitEntity(Tb1062PinEntity pin) {
		Tb1063CircuitEntity circuit = getCircuitEntity(pin);
		if (circuit != null) {
			beanDao.markDeleted(circuit);
		}
	}
}
