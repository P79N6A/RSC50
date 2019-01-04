package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;


public class SgfcdaEntityService extends BaseService {

	/**
	 * 获取当前IED下所有保护定值
	 * @param iedEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1059SgfcdaEntity> getSgfcdaByIed(Tb1046IedEntity iedEntity) {
		List<Tb1057SgcbEntity> sgcbEntities = (List<Tb1057SgcbEntity>) beanDao.getListByCriteria(Tb1057SgcbEntity.class, "tb1046IedByF1046Code", iedEntity);
		return (List<Tb1059SgfcdaEntity>) hqlDao.selectInObjects(Tb1059SgfcdaEntity.class, "tb1057SgcbByF1057Code", sgcbEntities);
	}

	/**
	 * 增量添加sgfcda
	 * @param sgcb
	 * @param fcdaRef
	 * @param disInfo
	 */
	public void addSgfcda(Tb1057SgcbEntity sgcb, String fcdaRef, Map<String, String> disInfo) {
		String fcdaDesc = disInfo.get("desc");
		int i = Integer.parseInt(disInfo.get("index"));
		int dtype = Integer.parseInt(disInfo.get("datType"));
		Tb1059SgfcdaEntity sgFcda = new Tb1059SgfcdaEntity();
		sgFcda.setF1059Code(rscp.nextTbCode(DBConstants.PR_SG));
		sgFcda.setTb1057SgcbByF1057Code(sgcb);
		sgFcda.setF1059Index(i);
		sgFcda.setF1059Desc(fcdaDesc);
		sgFcda.setF1059RefAddr(fcdaRef);
		sgFcda.setF1059DataType(dtype);
		beanDao.insert(sgFcda);
	}

	/**
	 * 增量删除sgfcda
	 * @param sgcb
	 * @param fcdaRef
	 */
	public void deleteSgfcda(Tb1057SgcbEntity sgcb, String fcdaRef) {
		String hql = "update " + Tb1059SgfcdaEntity.class.getName() +
				" set deleted=1" +
				" where f1059RefAddr=:fcdaRef and tb1057SgcbByF1057Code=:sgcb";
		Map<String, Object> params = new HashMap<>();
		params.put("sgcb", sgcb);
		params.put("fcdaRef", fcdaRef);
		hqlDao.updateByHql(hql, params);
	}
	
	/**
	 * 查询sgfcda
	 * @param sgcb
	 * @param fcdaRef
	 * @return
	 */
	public Tb1059SgfcdaEntity getSgfcdaEntity(Tb1057SgcbEntity sgcb, String fcdaRef) {
		Map<String, Object> params = new HashMap<>();
		params.put("tb1057SgcbByF1057Code", sgcb);
		params.put("f1059RefAddr", fcdaRef);
		return (Tb1059SgfcdaEntity) beanDao.getObject(Tb1059SgfcdaEntity.class, params);
	}

	/**
	 * 增量更新sgfcda
	 * @param sgcb
	 * @param fcdaRef
	 * @param upInfo
	 */
	public void updateSgfcda(Tb1057SgcbEntity sgcb, String fcdaRef, Map<String, String> upInfo) {
		Tb1059SgfcdaEntity sgFcda = getSgfcdaEntity(sgcb, fcdaRef);
		if (sgFcda != null) {
			if (upInfo.get("desc") != null) {
				sgFcda.setF1059Desc(upInfo.get("desc"));
			}
			if (upInfo.get("index") != null) {
				int i = Integer.parseInt(upInfo.get("index"));
				sgFcda.setF1059Index(i);
			}
			if (upInfo.get("datType") != null) {
				int dtype = Integer.parseInt(upInfo.get("datType"));
				sgFcda.setF1059DataType(dtype);
			}
			beanDao.update(sgFcda);
		}
	}
	
}
