package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCConstants;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;
import com.synet.tool.rsc.util.RuleType;

public class PoutEntityService extends BaseService {
	
	private StrapEntityService strapServ = new StrapEntityService();
	
	@SuppressWarnings("unchecked")
	public List<Tb1061PoutEntity> getByIed(Tb1046IedEntity ied) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		String hql = "from " + Tb1061PoutEntity.class.getName() + 
				" where tb1046IedByF1046Code=:ied and deleted=0";
		return (List<Tb1061PoutEntity>) hqlDao.getListByHql(hql, params);
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
				"and deleted=0 and f1061Type between :min and :max";
		Map<String, Object> params = new HashMap<>();
		params.put("ied", ied);
		params.put("min", rtyp.getMin());
		params.put("max", rtyp.getMax());
		return (List<Tb1061PoutEntity>) hqlDao.getListByHql(hql, params);
	}
	
	public List<Tb1061PoutEntity> getOtherData(Tb1046IedEntity ied) {
		String hql = "from " + Tb1061PoutEntity.class.getName() + " where tb1046IedByF1046Code=:ied " +
				"and deleted=0 and f1061Type=:type";
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
	public List<Tb1061PoutEntity> getPoutEntityByCb(BaseCbEntity cbEntity) {
		return (List<Tb1061PoutEntity>) beanDao.getListByCriteria(Tb1061PoutEntity.class, "cbEntity", cbEntity);
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

	/**
	 * 增量添加输出虚端子
	 * @param cb
	 * @param fcdaRef
	 * @param disInfo
	 */
	public void addFcda(BaseCbEntity cb, String ref, Map<String, String> disInfo) {
		String fcdaDesc = disInfo.get("desc");
		String fc = disInfo.get("fc");
		int i = Integer.parseInt(disInfo.get("index"));
//		int dtype = Integer.parseInt(disInfo.get("datType"));
		Tb1046IedEntity ied = cb.getTb1046IedByF1046Code();
		
		Tb1061PoutEntity pout = new Tb1061PoutEntity();
		pout.setF1061Code(rscp.nextTbCode(DBConstants.PR_POUT));
		pout.setTb1046IedByF1046Code(ied);
		pout.setCbEntity(cb);
		pout.setCbCode(cb.getCbCode());
		pout.setF1061RefAddr(ref);
		pout.setF1061Index(i);
		pout.setF1061Desc(fcdaDesc);
//		Rule type = F1011_NO.getType(datSet, lnName, doName, fcdaDesc, fc);
		Rule type = F1011_NO.OTHERS;
		pout.setF1061Type(type.getId());
		if ("ST".equals(fc)) {
			Tb1016StatedataEntity statedata = addStatedata(ied, ref, fcdaDesc, type.getId());
			pout.setDataCode(statedata.getF1016Code());
			pout.setParentCode(statedata.getParentCode());
		} else {
			Tb1006AnalogdataEntity algdata = addAlgdata(ied, ref, fcdaDesc, type.getId());
			String algcode = algdata.getF1006Code();
			pout.setDataCode(algcode);
			pout.setParentCode(algdata.getParentCode());
		}
		beanDao.insert(pout);
	}

	/**
	 * 增量删除输出虚端子
	 * @param cb
	 * @param fcdaRef
	 */
	public void deleteFcda(BaseCbEntity cb, String fcdaRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cbEntity", cb);
		params.put("f1061RefAddr", fcdaRef);
		String hql = "update " + Tb1061PoutEntity.class.getName() + 
				" set deleted=1 where cbEntity=:cbEntity and f1061RefAddr=:f1061RefAddr";
		hqlDao.updateByHql(hql, params);   
//		System.out.println(beanDao.getListByCriteria(Tb1061PoutEntity.class, params));
	}

	/**
	 * 增量修改输出虚端子
	 * @param cb
	 * @param fcdaRef
	 * @param upInfo
	 */
	public void updateFcda(BaseCbEntity cb, String fcdaRef, Map<String, String> upInfo) {
		Tb1046IedEntity ied = cb.getTb1046IedByF1046Code();
		Tb1061PoutEntity pout = getPoutEntity(ied.getF1046Name(), fcdaRef);
		if (pout != null) {
			if (upInfo.get("desc") != null) {
				pout.setF1061Desc(upInfo.get("desc"));
			}
			if (upInfo.get("index") != null) {
				int i = Integer.parseInt(upInfo.get("index"));
				pout.setF1061Index(i);
			}
			beanDao.update(pout);
		}
	}

	/**
	 * 添加压板
	 * @param statedata
	 */
	public void addStrap(Tb1061PoutEntity poutEntity) {
		Tb1016StatedataEntity statedata = (Tb1016StatedataEntity) 
				beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", poutEntity.getDataCode());
		if (statedata == null)
			return;
		statedata.setF1011No(poutEntity.getF1061Type());
		Tb1064StrapEntity strap = strapServ.addStrap(statedata);
		poutEntity.setParentCode(strap.getF1064Code());
		beanDao.update(poutEntity);
	}
	
	/**
	 * 删除压板
	 * @param statedata
	 */
	public void removeStrap(Tb1061PoutEntity poutEntity) {
		Tb1016StatedataEntity statedata = (Tb1016StatedataEntity) 
				beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", poutEntity.getDataCode());
		if (statedata == null)
			return;
		statedata.setF1011No(poutEntity.getF1061Type());
		strapServ.removeStrap(statedata);
		String iedCode = statedata.getTb1046IedByF1046Code().getF1046Code();
		poutEntity.setParentCode(iedCode);
		beanDao.update(poutEntity);
	}
	
}
