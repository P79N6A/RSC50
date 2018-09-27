package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;

public class LLinkPhyRelationService extends BaseService {
	
	public Tb1073LlinkphyrelationEntity getLlinkphyrelationEntityByLogical(Tb1065LogicallinkEntity logicallinkEntity) {
		return (Tb1073LlinkphyrelationEntity) beanDao.getObject(Tb1073LlinkphyrelationEntity.class, "Tb1065LogicallinkEntity", logicallinkEntity);
	}

	public Tb1073LlinkphyrelationEntity existEntity(Tb1073LlinkphyrelationEntity llinkphyrelationEntity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1065LogicallinkByF1065Code", llinkphyrelationEntity.getTb1065LogicallinkByF1065Code());
		params.put("tb1053PhysconnByF1053Code", llinkphyrelationEntity.getTb1053PhysconnByF1053Code());
		return (Tb1073LlinkphyrelationEntity) beanDao.getObject(Tb1073LlinkphyrelationEntity.class, params);
	}
}
