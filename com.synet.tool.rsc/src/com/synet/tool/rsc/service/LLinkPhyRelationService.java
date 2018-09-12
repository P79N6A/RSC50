package com.synet.tool.rsc.service;

import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;

public class LLinkPhyRelationService extends BaseService {
	
	public Tb1073LlinkphyrelationEntity getLlinkphyrelationEntityByLogical(Tb1065LogicallinkEntity logicallinkEntity) {
		return (Tb1073LlinkphyrelationEntity) beanDao.getObject(Tb1073LlinkphyrelationEntity.class, "Tb1065LogicallinkEntity", logicallinkEntity);
	}

}
