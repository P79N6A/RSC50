package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;

public class LogicallinkEntityService extends BaseService {

	@SuppressWarnings("unchecked")
	public List<Tb1065LogicallinkEntity> getAll() {
		return (List<Tb1065LogicallinkEntity>) beanDao.getAll(Tb1065LogicallinkEntity.class);
	}
	
	/**
	 * 根据当前装置查询
	 * 逻辑链路的接收装置为当前装置
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1065LogicallinkEntity> getByRecvIed(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity.getF1046Code());
		String hql = "from " + Tb1065LogicallinkEntity.class.getName() + 
				" where f1046CodeIedRecv=:ied and deleted=0";
		List<Tb1065LogicallinkEntity> linkList = (List<Tb1065LogicallinkEntity>) hqlDao.getListByHql(hql, params);
		for (Tb1065LogicallinkEntity link : linkList) {
			link.setCircuits("查看");
			link.setPhyconns("查看");
		}
		return linkList;
	}
	
	/**
	 * 根据装置名称和cbRef查询
	 * @param sendDevName
	 * @param cbRef
	 * @return
	 */
	public Tb1065LogicallinkEntity getBySendIedAndRef(String recvDevName, String sendDevName, String cbRef) {
		Map<String, Object> params = new HashMap<>();
		params.put("devName", sendDevName);
		params.put("cbId", cbRef);
		String hql = "from " + BaseCbEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:devName and cbId=:cbId";
		List<?> list = hqlDao.getListByHql(hql, params);
		BaseCbEntity cb = (BaseCbEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
		if (cb != null) {
//			return (Tb1065LogicallinkEntity) beanDao.getObject(Tb1065LogicallinkEntity.class, "baseCbByCdCode", cb);
			params.clear();
			params.put("baseCbByCdCode", cb);
			params.put("devName", recvDevName);
			hql = "from " + Tb1065LogicallinkEntity.class.getName() + " where tb1046IedByF1046CodeIedRecv.f1046Name=:devName and baseCbByCdCode=:baseCbByCdCode";
			list = hqlDao.getListByHql(hql, params);
			return (Tb1065LogicallinkEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
		}
		return null;
	}
}
