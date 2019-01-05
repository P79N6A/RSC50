package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;

public class SpfcdaEntityService extends BaseService {

	/**
	 * 根据IED获取保护参数集合
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1060SpfcdaEntity> getByIed(Tb1046IedEntity iedEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("ied", iedEntity);
		String hql = "from " + Tb1060SpfcdaEntity.class.getName() + 
				" where tb1046IedByF1046Code=:ied and deleted=0";
		return (List<Tb1060SpfcdaEntity>) hqlDao.getListByHql(hql, params);
	}
	
	/**
	 * 查询spfcda
	 * @param iedEntity
	 * @param fcdaRef
	 * @return
	 */
	public Tb1060SpfcdaEntity getSpfcdaEntity(Tb1046IedEntity iedEntity, String fcdaRef) {
		Map<String, Object> params = new HashMap<>();
		params.put("tb1046IedByF1046Code", iedEntity);
		params.put("f1060RefAddr", fcdaRef);
		return (Tb1060SpfcdaEntity) beanDao.getObject(Tb1060SpfcdaEntity.class, params);
	}
	
	/**
	 * 增量添加fcda
	 * @param iedEntity
	 * @param fcdaRef
	 * @param disInfo
	 */
	public void addSpfcda(Tb1046IedEntity iedEntity, String fcdaRef, Map<String, String> disInfo) {
		String fcdaDesc = disInfo.get("desc");
		int i = Integer.parseInt(disInfo.get("index"));
		int dtype = Integer.parseInt(disInfo.get("datType"));
		Tb1060SpfcdaEntity spFcda = new Tb1060SpfcdaEntity();
		spFcda.setF1060Code(rscp.nextTbCode(DBConstants.PR_SP));
		spFcda.setTb1046IedByF1046Code(iedEntity);
		spFcda.setF1060Index(i);
		spFcda.setF1060Desc(fcdaDesc);
		spFcda.setF1060RefAddr(fcdaRef);
		spFcda.setF1060DataType(dtype);
		beanDao.insert(spFcda);
	}

	/**
	 * 删除参数fcda
	 * @param iedEntity
	 * @param fcdaRef
	 */
	public void deleteSpfcda(Tb1046IedEntity iedEntity, String fcdaRef) {
		String hql = "update " + Tb1060SpfcdaEntity.class.getName() +
				" set deleted=1" +
				" where f1060RefAddr=:fcdaRef and tb1046IedByF1046Code=:ied";
		Map<String, Object> params = new HashMap<>();
		params.put("fcdaRef", fcdaRef);
		params.put("ied", iedEntity);
		hqlDao.updateByHql(hql, params);
	}
	
	/**
	 * 增量修改fcda
	 * @param iedEntity
	 * @param fcdaRef
	 * @param upInfo
	 */
	public void updateSpfcda(Tb1046IedEntity iedEntity, String fcdaRef, Map<String, String> upInfo) {
		Tb1060SpfcdaEntity spFcda = getSpfcdaEntity(iedEntity, fcdaRef);
		if (spFcda != null) {
			if (upInfo.get("desc") != null) {
				spFcda.setF1060Desc(upInfo.get("desc"));
			}
			if (upInfo.get("index") != null) {
				int i = Integer.parseInt(upInfo.get("index"));
				spFcda.setF1060Index(i);
			}
			if (upInfo.get("datType") != null) {
				int dtype = Integer.parseInt(upInfo.get("datType"));
				spFcda.setF1060DataType(dtype);
			}
			beanDao.update(spFcda);
		}
	}
}
