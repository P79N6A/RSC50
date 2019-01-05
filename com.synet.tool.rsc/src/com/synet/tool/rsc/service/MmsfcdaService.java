package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.ObjectUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleType;

public class MmsfcdaService extends BaseService {
	
	private StrapEntityService strapServ = new StrapEntityService();
	
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
	
	public Tb1058MmsfcdaEntity getMmsfcdaByF1058RedAddr(String devName, String f1058RefAddr) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("devName", devName);
		params.put("f1058RefAddr", f1058RefAddr);
		String hql = "from " + Tb1058MmsfcdaEntity.class.getName() + " where tb1046IedByF1046Code.f1046Name=:devName and f1058RefAddr=:f1058RefAddr";
		List<?> list = hqlDao.getListByHql(hql, params);
		return (Tb1058MmsfcdaEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
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

	public List<Tb1058MmsfcdaEntity> getOtherData(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("tb1046IedByF1046Code", iedEntity);
		params.put("f1058Type", RSCConstants.OTHERS_ID);
		return (List<Tb1058MmsfcdaEntity>) beanDao.getListByCriteria(Tb1058MmsfcdaEntity.class, params);
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
	
	/**
	 * 添加压板
	 * @param statedata
	 */
	public void addStrap(Tb1058MmsfcdaEntity mmsFcda) {
		Tb1016StatedataEntity statedata = (Tb1016StatedataEntity) 
				beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", mmsFcda.getDataCode());
		if (statedata == null)
			return;
		statedata.setF1011No(mmsFcda.getF1058Type());
		Tb1064StrapEntity strap = strapServ.addStrap(statedata);
		mmsFcda.setParentCode(strap.getF1064Code());
		beanDao.update(mmsFcda);
	}
	
	/**
	 * 删除压板
	 * @param statedata
	 */
	public void removeStrap(Tb1058MmsfcdaEntity mmsFcda) {
		Tb1016StatedataEntity statedata = (Tb1016StatedataEntity) 
				beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", mmsFcda.getDataCode());
		if (statedata == null)
			return;
		statedata.setF1011No(mmsFcda.getF1058Type());
		strapServ.removeStrap(statedata);
		String iedCode = statedata.getTb1046IedByF1046Code().getF1046Code();
		mmsFcda.setParentCode(iedCode);
		beanDao.update(mmsFcda);
	}
	
	public void updateWarnParent(Object objMms, String parendCode) {
		String dataCode = (String) ObjectUtil.getProperty(objMms, "dataCode");
		if (SclUtil.isStData(dataCode)) {
			StatedataService statedataService = new StatedataService();
			Tb1016StatedataEntity stateData = statedataService.getStateDataByCode(dataCode);
			stateData.setParentCode(parendCode);
			beanDao.update(stateData);
		} else {
			AnalogdataService analogdataService = new AnalogdataService();
			Tb1006AnalogdataEntity anolog = analogdataService.getAnologByCodes(dataCode);
			anolog.setParentCode(parendCode);
			beanDao.update(anolog);
		}
		ObjectUtil.setProperty(objMms, "parentCode", parendCode);
		beanDao.update(objMms);
	}

	/**
	 * 增量添加mms、st、ang
	 * @param rcb
	 * @param fcdaRef
	 * @param disInfo
	 */
	public void addFcda(Tb1054RcbEntity rcb, String fcdaRef, Map<String, String> disInfo) {
		String fcdaDesc = disInfo.get("desc");
		int i = Integer.parseInt(disInfo.get("index"));
		Tb1046IedEntity ied = rcb.getTb1046IedByF1046Code();

		Tb1058MmsfcdaEntity mmsFcda = new Tb1058MmsfcdaEntity();
		mmsFcda.setF1058Code(rscp.nextTbCode(DBConstants.PR_FCDA));
		mmsFcda.setTb1046IedByF1046Code(ied);
		mmsFcda.setTb1054RcbByF1054Code(rcb);
		mmsFcda.setF1058Index(i);
		mmsFcda.setF1058Desc(fcdaDesc);
		mmsFcda.setF1058RefAddr(fcdaRef);
		String fc = disInfo.get("fc");
		Rule type = F1011_NO.OTHERS;
		mmsFcda.setF1058Type(type.getId());
		if ("ST".equals(fc)) {
			mmsFcda.setF1058DataType(DBConstants.DATA_ST);
			Tb1016StatedataEntity statedata = addStatedata(ied, fcdaRef, fcdaDesc, type.getId());
			mmsFcda.setDataCode(statedata.getF1016Code());
			mmsFcda.setParentCode(statedata.getParentCode());
		} else {
			mmsFcda.setF1058DataType(DBConstants.DATA_MX);
			Tb1006AnalogdataEntity algdata = addAlgdata(ied, fcdaRef, fcdaDesc, type.getId());
			mmsFcda.setDataCode(algdata.getF1006Code());
			mmsFcda.setParentCode(algdata.getParentCode());
		}
		beanDao.insert(mmsFcda);
	}
	
	/**
	 * 增量删除mms、st、ang
	 * @param rcb
	 * @param fcdaRef
	 */
	public void deleteFcda(Tb1054RcbEntity rcb, String fcdaRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("devName", rcb.getTb1046IedByF1046Code().getF1046Name());
		params.put("f1058RefAddr", fcdaRef);
		String hql = "update " + Tb1058MmsfcdaEntity.class.getName() + 
				" set deleted=1 where tb1046IedByF1046Code.f1046Name=:devName and f1058RefAddr=:f1058RefAddr";
		hqlDao.updateByHql(hql, params);
	}

	/**
	 * 增量修改mms、st、ang
	 * @param rcb
	 * @param fcdaRef
	 * @param upInfo
	 */
	public void updateFcda(Tb1054RcbEntity rcb, String fcdaRef, Map<String, String> upInfo) {
		Tb1046IedEntity ied = rcb.getTb1046IedByF1046Code();
		Tb1058MmsfcdaEntity mmsFcda = getMmsfcdaByF1058RedAddr(ied.getF1046Name(), fcdaRef);
		if (mmsFcda != null) {
			if (upInfo.get("desc") != null) {
				mmsFcda.setF1058Desc(upInfo.get("desc"));
			}
			if (upInfo.get("index") != null) {
				int i = Integer.parseInt(upInfo.get("index"));
				mmsFcda.setF1058Index(i);
			}
			beanDao.update(mmsFcda);
		}
	}
	
}
