package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;

public class LLinkPhyRelationService extends BaseService {
	
	public Tb1073LlinkphyrelationEntity getLlinkphyrelationEntityByLogical(Tb1065LogicallinkEntity logicallinkEntity) {
		return (Tb1073LlinkphyrelationEntity) beanDao.getObject(Tb1073LlinkphyrelationEntity.class, "Tb1065LogicallinkEntity", logicallinkEntity);
	}

	//检查关联关系是否存在
	public Tb1073LlinkphyrelationEntity existEntity(Tb1073LlinkphyrelationEntity llinkphyrelationEntity) {
		if (llinkphyrelationEntity == null) return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1065LogicallinkByF1065Code", llinkphyrelationEntity.getTb1065LogicallinkByF1065Code());
		params.put("tb1053PhysconnByF1053Code", llinkphyrelationEntity.getTb1053PhysconnByF1053Code());
		return (Tb1073LlinkphyrelationEntity) beanDao.getObject(Tb1073LlinkphyrelationEntity.class, params);
	}
	
	//根据物理回路，查找关联关系
	@SuppressWarnings("unchecked")
	public List<Tb1073LlinkphyrelationEntity> getByTb1053PhysconnByF1053Code(Tb1053PhysconnEntity tb1053PhysconnByF1053Code) {
		if (tb1053PhysconnByF1053Code == null) return null;
		return (List<Tb1073LlinkphyrelationEntity>) beanDao.getListByCriteria(Tb1073LlinkphyrelationEntity.class, "tb1053PhysconnByF1053Code", tb1053PhysconnByF1053Code);
	}
}
