package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;

public class BoardPortService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1048PortEntity> getBoardPortByIed(Tb1046IedEntity iedEntity) {
		List<Tb1047BoardEntity> boardEntities = (List<Tb1047BoardEntity>) 
				beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
		List<Tb1048PortEntity> portList = (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class, "tb1047BoardByF1047Code", boardEntities);
		List<Tb1006AnalogdataEntity> angList = (List<Tb1006AnalogdataEntity>) beanDao.getListByCriteria(Tb1006AnalogdataEntity.class, "tb1046IedByF1046Code", iedEntity);
		Map<String, Tb1006AnalogdataEntity> angMap = new HashMap<>();
		for (Tb1006AnalogdataEntity ang : angList) {
			String parentCode = ang.getParentCode();
			if (!StringUtil.isEmpty(parentCode)) {
				angMap.put(parentCode, ang);
			}
		}
		for (Tb1048PortEntity port : portList) {
			String f1048Code = port.getF1048Code();
			Tb1006AnalogdataEntity ang = angMap.get(f1048Code);
			port.setTb1006AnalogdataByF1048Code(ang);
		}
		return portList;
	}

}
