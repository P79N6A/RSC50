package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.RuleType;

public class PoutEntityService extends BaseService{
	
	@SuppressWarnings("unchecked")
	public List<Tb1061PoutEntity> getByIed(Tb1046IedEntity ied) {
		return (List<Tb1061PoutEntity>) beanDao.getListByCriteria(Tb1061PoutEntity.class, "tb1046IedByF1046Code", ied);
	}
	
	public List<Tb1061PoutEntity> getWarningData(Tb1046IedEntity ied) {
		return getByDataType(ied, RuleType.IED_WARN);
	}
	
	public List<Tb1061PoutEntity> getStateData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.IED_STATE);
	}
	
	public List<Tb1061PoutEntity> getDinData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.IED_YX);
	}
	
	public List<Tb1061PoutEntity> getStrapData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.STRAP);
	}
	
	public List<Tb1061PoutEntity> getByDataType(Tb1046IedEntity ied, RuleType rtyp) {
		String hql = "from " + Tb1061PoutEntity.class.getName() + " where tb1046IedByF1046Code=:ied " +
				"and f1061Type between :min and :max";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		params.put("min", rtyp.getMin());
		params.put("max", rtyp.getMax());
		return (List<Tb1061PoutEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public List<Tb1061PoutEntity> getOtherData(Tb1046IedEntity ied) {
		String hql = "from " + Tb1061PoutEntity.class.getName() + " where tb1046IedByF1046Code=:ied " +
				"and f1061Type=:type";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		params.put("type", RSCConstants.OTHERS_ID);
		return (List<Tb1061PoutEntity>) hqlDao.getListByHql(hql, params);
	}
	
	/**
	 * 根据条件查找虚端子
	 * @param iedEntity
	 * @param svcbEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1061PoutEntity> getPoutEntityByProperties(Tb1046IedEntity iedEntity, Tb1056SvcbEntity svcbEntity) {
		Map<String, Object> params = new HashMap<>();
		if(iedEntity !=null) {
			params.put("tb1046IedByF1046Code", iedEntity);
		}
		if(svcbEntity != null) {
			params.put("tb1056SvcbByCbCode", svcbEntity);
		}
		if(params.isEmpty()) {
			return new ArrayList<>();
		}
		return (List<Tb1061PoutEntity>) beanDao.getListByCriteria(Tb1061PoutEntity.class, params);
	}
	
	public Tb1061PoutEntity getPoutEntity(String devName, String f1061RefAddr) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("devName", devName);
		params.put("f1061RefAddr", f1061RefAddr);
		String hql = "from " + Tb1061PoutEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:devName and f1061RefAddr=:f1061RefAddr";
		List<?> list = hqlDao.getListByHql(hql, params);
		return (Tb1061PoutEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
	}
	
	/**
	 * 根据关联的保护压板查找开出虚端子
	 * @param straps
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1061PoutEntity> getByStraps(List<Tb1064StrapEntity> straps) {
		return (List<Tb1061PoutEntity>) hqlDao.selectInObjects(Tb1061PoutEntity.class, "tb1064StrapByF1064Code", straps);
	}
	
}
