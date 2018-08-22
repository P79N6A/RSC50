package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;

public class BoardPortService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1048PortEntity> getBoardPortByIed(Tb1046IedEntity iedEntity) {
		List<Tb1047BoardEntity> boardEntities = (List<Tb1047BoardEntity>) 
				beanDao.getListByCriteria(Tb1047BoardEntity.class, "tb1046IedByF1046Code", iedEntity);
		return (List<Tb1048PortEntity>) hqlDao.selectInObjects(Tb1048PortEntity.class, "tb1047BoardByF1047Code", boardEntities);
	}

}
