package com.synet.tool.rsc.service;

import java.util.List;

import com.synet.tool.rsc.model.TB1084FuncClassEntity;
import com.synet.tool.rsc.model.TB1085ProtFuncEntity;
import com.synet.tool.rsc.model.TB1086DefectFuncREntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;

public class TB1085ProtFuncService extends BaseService {

	public List<TB1085ProtFuncEntity> getByIed(Tb1046IedEntity iedEntity) {
		return (List<TB1085ProtFuncEntity>) beanDao.getListByCriteria(TB1085ProtFuncEntity.class, "tb1046ByF1046CODE", iedEntity);
	}
	
	public List<TB1086DefectFuncREntity> getFunDefByFunc(TB1085ProtFuncEntity funcEntity) {
		return (List<TB1086DefectFuncREntity>) beanDao.getListByCriteria(TB1086DefectFuncREntity.class, "tb1085ByF1085CODE", funcEntity);
	}
	
	public List<TB1084FuncClassEntity> getFuncClassAll() {
		return (List<TB1084FuncClassEntity>) beanDao.getAll(TB1084FuncClassEntity.class);
	}
}
