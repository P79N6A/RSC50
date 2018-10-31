package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.RuleType;

public class MmsfcdaService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public List<Tb1058MmsfcdaEntity> getMmsfcdaByIed(Tb1046IedEntity iedEntitie) {
		return (List<Tb1058MmsfcdaEntity>) beanDao.getListByCriteria(Tb1058MmsfcdaEntity.class, "tb1046IedByF1046Code", iedEntitie);
	}
	
	/**
	 * 根据所属数据集名称获取
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Tb1058MmsfcdaEntity> getMmsdcdaByDataSet(Tb1046IedEntity iedEntity, String dataSet) {
		if((iedEntity == null) || !DataUtils.strNotNull(dataSet)) {
			return new ArrayList<>();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("tb1046IedByF1046Code", iedEntity);
		params.put("f1054Dataset", dataSet);
		List<Tb1054RcbEntity> rcbEntities = (List<Tb1054RcbEntity>) beanDao.getListByCriteria(Tb1054RcbEntity.class, params);
		return (List<Tb1058MmsfcdaEntity>) hqlDao.selectInObjects(Tb1058MmsfcdaEntity.class, "tb1054RcbByF1054Code", rcbEntities);
	}
	
	/**
	 * 根据所属多个数据集名称获取
	 * @param iedEntity
	 * @param dataSets
	 * @return
	 */
	public List<Tb1058MmsfcdaEntity> getMmsdcdaByDataSet(Tb1046IedEntity iedEntity, String[] dataSets) {
		List<Tb1058MmsfcdaEntity> fcdas = new ArrayList<>();
		for (String dataSet : dataSets) {
			fcdas.addAll(getMmsdcdaByDataSet(iedEntity, dataSet));
		}
		return fcdas;
	}
	
	public Tb1058MmsfcdaEntity getMmsfcdaByF1058RedAddr(String f1058RefAddr) {
		return (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, "f1058RefAddr", f1058RefAddr);
	}

	public Tb1058MmsfcdaEntity getMmsfcdaByF1058RedAddr(String devName, String f1058RefAddr) {
		Tb1046IedEntity iedEntity = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", devName);
		if (iedEntity != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code", iedEntity);
			params.put("f1058RefAddr", f1058RefAddr);
			return (Tb1058MmsfcdaEntity) beanDao.getObject(Tb1058MmsfcdaEntity.class, params);
		}
		return null;
	}
	
	
	public List<Tb1058MmsfcdaEntity> getByPort(String portCode) {
		@SuppressWarnings("unchecked")
		List<Tb1006AnalogdataEntity> listByAnalogdata = (List<Tb1006AnalogdataEntity>) beanDao.getListByCriteria(Tb1006AnalogdataEntity.class, "parentCode", portCode);
		List<String> analogCodeList = new ArrayList<>();
		for (Tb1006AnalogdataEntity analog : listByAnalogdata) {
			analogCodeList.add(analog.getF1006Code());
		}
		@SuppressWarnings("unchecked")
		List<Tb1058MmsfcdaEntity> result = (List<Tb1058MmsfcdaEntity>) hqlDao.selectInObjects(Tb1058MmsfcdaEntity.class, "dataCode", analogCodeList);
		return result;
	}
	
	public List<Tb1058MmsfcdaEntity> getByDataType(Tb1046IedEntity iedEntity, RuleType...types) {
		List<Tb1058MmsfcdaEntity> mmsList = new ArrayList<>();
		for (RuleType rtyp : types) {
			String hql = "from " + Tb1058MmsfcdaEntity.class.getName() + " where tb1046IedByF1046Code=:ied " +
					"and f1058Type between :min and :max";
			Map<String, Object> params = new HashMap<>();
			params.put("ied", iedEntity);
			params.put("min", rtyp.getMin());
			params.put("max", rtyp.getMax());
			List<Tb1058MmsfcdaEntity> list = (List<Tb1058MmsfcdaEntity>) hqlDao.getListByHql(hql, params);
			mmsList.addAll(list);
		}
		return mmsList;
	}
	
	public List<Tb1058MmsfcdaEntity> getEventData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.PROT_EVENT);
	}
	
	public List<Tb1058MmsfcdaEntity> getDinData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.IED_YX);
	}
	
	public List<Tb1058MmsfcdaEntity> getAinData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.PROT_MS);
	}
	
	public List<Tb1058MmsfcdaEntity> getStrapData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.STRAP);
	}
	
	public List<Tb1058MmsfcdaEntity> getWarningData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.IED_WARN);
	}
	
	public List<Tb1058MmsfcdaEntity> getStateData(Tb1046IedEntity iedEntity) {
		return getByDataType(iedEntity, RuleType.IED_STATE);
	}
	
	public void updateStateF1011No(String dataCode, int typeId) {
		String hql = "update " + Tb1016StatedataEntity.class.getName() + " set f1011No=:f1011No where f1016Code=:f1016Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1011No", typeId);
		params.put("f1016Code", dataCode);
		hqlDao.updateByHql(hql, params);
	}
	
	public void updateStrapStateF1011No(String strapCode, int typeId) {
		String hql = "update " + Tb1016StatedataEntity.class.getName() + " set f1011No=:f1011No where parentCode=:strapCode";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1011No", typeId);
		params.put("strapCode", strapCode);
		hqlDao.updateByHql(hql, params);
	}

	public void updateAnalogF1011No(String dataCode, int typeId) {
		String hql = "update " + Tb1006AnalogdataEntity.class.getName() + " set f1011No=:f1011No where f1006Code=:f1006Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1011No", typeId);
		params.put("f1006Code", dataCode);
		hqlDao.updateByHql(hql, params);
	}
	
	public void updateStrapF1011No(String dataCode, int typeId) {
		Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", dataCode);
		if (typeId == statedataEntity.getF1011No()) {
			return;
		}
		String hql = "update " + Tb1064StrapEntity.class.getName() + " set f1064Type=:f1064Type where f1064Code=:f1064Code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("f1064Type", typeId);
		params.put("f1064Code", statedataEntity.getParentCode());
		hqlDao.updateByHql(hql, params);
	}
	
	
}
