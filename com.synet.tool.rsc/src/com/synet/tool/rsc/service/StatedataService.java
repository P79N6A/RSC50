package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.RuleType;

public class StatedataService extends BaseService {
	
	/**
	 * 根据stateDatCode查找
	 * @param dataCodes
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStatedataByDataCodes(List<String> dataCodes) {
		if(!DataUtils.listNotNull(dataCodes)) {
			return new ArrayList<>();
		}
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> statedataEntities = 
			(List<Tb1016StatedataEntity>) hqlDao.selectInObjects(
					Tb1016StatedataEntity.class, "f1016Code", dataCodes);
		return statedataEntities;
	}
	

	
	/**
	 * 根据IED获取
	 * @param iedEntity
	 * @return
	 */
	public List<String> getStateDataByIed(Tb1046IedEntity iedEntity) {
		List<Tb1016StatedataEntity> temp = getStatesByIed(iedEntity);
		List<String> result = new ArrayList<>();
		for (Tb1016StatedataEntity tb1016StatedataEntity : temp) {
			result.add(tb1016StatedataEntity.getF1016Byname());
		}
		return result;
	}
	
	/**
	 * 根据ied查询状态量
	 * @param iedEntity
	 * @return
	 */
	public List<Tb1016StatedataEntity> getStatesByIed(Tb1046IedEntity iedEntity) {
		return (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, "tb1046IedByF1046Code", iedEntity);
	}

	/**
	 * 根据父对象Code获取
	 * @param parentCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1016StatedataEntity> getStateDataByParentCode(String parentCode) {
		return (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, 
				"parentCode", parentCode);
	}
	
	/**
	 * 更新状态点父对象
	 * @param iedEntity
	 * @param addRef
	 * @param parentCode
	 */
	public void updateStateParentCode(Tb1046IedEntity iedEntity, String addRef, String parentCode) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity);
		params.put("addRef", addRef);
		params.put("parentCode", parentCode);
		String hql = "update " + Tb1016StatedataEntity.class.getName() + " set parentCode=:parentCode " +
				"where tb1046IedByF1046Code=:ied and f1016AddRef=:addRef";
		hqlDao.updateByHql(hql, params);
		hql = "update " + Tb1058MmsfcdaEntity.class.getName() + " set parentCode=:parentCode " +
				"where tb1046IedByF1046Code=:ied and f1058RefAddr=:addRef";
		hqlDao.updateByHql(hql, params);
		hql = "update " + Tb1061PoutEntity.class.getName() + " set parentCode=:parentCode " +
				"where tb1046IedByF1046Code=:ied and f1061RefAddr=:addRef";
		hqlDao.updateByHql(hql, params);
	}
	
	/**
	 * getByCode
	 * @param code
	 * @return
	 */
	public Tb1016StatedataEntity getStateDataByCode(String code) {
		@SuppressWarnings("unchecked")
		List<Tb1016StatedataEntity> listByCriteria = (List<Tb1016StatedataEntity>) beanDao.getListByCriteria(Tb1016StatedataEntity.class, "f1016Code", code);
		if(DataUtils.listNotNull(listByCriteria)) {
			return listByCriteria.get(0);
		}
		return null;
	}

	/**
	 * 获取一次设备状态
	 * @param iedEntity
	 * @return
	 */
	public List<Tb1016StatedataEntity> getEqpStateByIed(Tb1046IedEntity iedEntity) {
		String hql = "from " + Tb1016StatedataEntity.class.getName() + " where tb1046IedByF1046Code=:ied and f1011No between :min and :max";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ied", iedEntity);
		params.put("min", RuleType.EQP_STATE.getMin());
		params.put("max", RuleType.EQP_STATE.getMax());
		return (List<Tb1016StatedataEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public Tb1016StatedataEntity getStateByIedRef(String iedName, String dataRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ied", iedName);
		params.put("ref", dataRef);
		List<?> result = hqlDao.getListByHql("from " + Tb1016StatedataEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:ied and f1016AddRef=:ref", params);
		if (result != null && result.size() > 0) {
			return (Tb1016StatedataEntity) result.get(0);
		}
		return null;
	}
	
	public void updateStateF1011No(String iedName, String dataRef, int typeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ied", iedName);
		params.put("ref", dataRef);
		List<?> result = hqlDao.getListByHql("from " + Tb1016StatedataEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:ied and f1016AddRef=:ref", params);
		if (result != null && result.size() > 0) {
			Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) result.get(0);
			statedataEntity.setF1011No(typeId);
			beanDao.update(statedataEntity);
		}
		result = hqlDao.getListByHql("from " + Tb1058MmsfcdaEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:ied and f1058RefAddr=:ref", params);
		if (result != null && result.size() > 0) {
			Tb1058MmsfcdaEntity mmsfcdaEntity = (Tb1058MmsfcdaEntity) result.get(0);
			mmsfcdaEntity.setF1058Type(typeId);
			beanDao.update(mmsfcdaEntity);
		} else {
			result = hqlDao.getListByHql("from " + Tb1061PoutEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:ied and f1061RefAddr=:ref", params);
			if (result != null && result.size() > 0) {
				Tb1061PoutEntity poutEntity = (Tb1061PoutEntity) result.get(0);
				poutEntity.setF1061Type(typeId);
				beanDao.update(poutEntity);
			}
		}
	}
}
