package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;

public class TerminalEntityService extends BaseService {
	
	private CNodeEntityService cnodeServ = new CNodeEntityService();

	public void addTerminal(Tb1043EquipmentEntity equipment, String tmName, Map<String, String> disInfo) {
		Tb1044TerminalEntity tm = new Tb1044TerminalEntity();
		tm.setF1044Code(rscp.nextTbCode(DBConstants.PR_Term));
		tm.setF1044Name(tmName);
		tm.setF1044Desc(disInfo.get("connectivityNode"));
		tm.setTb1043EquipmentByF1043Code(equipment);
		String cnode = disInfo.get("cNodeName");
		Tb1045ConnectivitynodeEntity node = cnodeServ.getCNodeEntity(cnode);
		tm.setTb1045ConnectivitynodeByF1045Code(node);
		beanDao.insert(tm);
	}
	
	public void deleteTerminal(Tb1043EquipmentEntity equipment, String tmName) {
		String hql = "update " + Tb1044TerminalEntity.class.getName() +
				" set deleted=1 where tb1043EquipmentByF1043Code=:eqp and f1044Name=:tmName";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eqp", equipment);
		params.put("tmName", tmName);
		hqlDao.updateByHql(hql, params);
	}
	
	public Tb1044TerminalEntity getTerminalEntity(Tb1043EquipmentEntity equipment, String tmName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1043EquipmentByF1043Code", equipment);
		params.put("f1044Name", tmName);
//		System.out.println(beanDao.getAll(Tb1044TerminalEntity.class));
		return (Tb1044TerminalEntity) beanDao.getObject(Tb1044TerminalEntity.class, params);
	}

	public void updateTerminal(Tb1043EquipmentEntity equipment, String tmName, Map<String, String> disInfo) {
		Tb1044TerminalEntity tm = getTerminalEntity(equipment, tmName);
		if (disInfo.get("connectivityNode") != null) {
			tm.setF1044Desc(disInfo.get("connectivityNode"));
		}
		if (disInfo.get("cNodeName") != null) {
			String cnode = disInfo.get("cNodeName");
			Tb1045ConnectivitynodeEntity node = cnodeServ.getCNodeEntity(cnode);
			tm.setTb1045ConnectivitynodeByF1045Code(node);
		}
		beanDao.update(tm);
	}
}
