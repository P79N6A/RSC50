package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;

public class SgcbEntityService extends BaseService {
	
	public Tb1057SgcbEntity getSgcbEntity(Tb1046IedEntity ied, String cbRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("f1057CbRef", cbRef);
		return (Tb1057SgcbEntity) beanDao.getObject(Tb1057SgcbEntity.class, params);
	}
	

	public void addCbEntity(Tb1046IedEntity ied, String cbRef, Map<String, String> disInfo) {
		Tb1057SgcbEntity sgcb = new Tb1057SgcbEntity();
		sgcb.setF1057Code(rscp.nextTbCode(DBConstants.PR_SGCB));
		sgcb.setTb1046IedByF1046Code(ied);
		sgcb.setF1057CbName(disInfo.get("cbName"));
		sgcb.setF1057CbRef(disInfo.get("cbRef"));
		sgcb.setF1057Dataset(disInfo.get("dsName"));
		sgcb.setF1057DsDesc(disInfo.get("dsDesc"));
		beanDao.insert(sgcb);
	}
	
	public void deleteCbEntity(Tb1046IedEntity ied, String cbRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("f1057CbName", cbRef);
		String hql = "update " + Tb1057SgcbEntity.class.getName() + 
				" set deleted=1 where tb1046IedByF1046Code=:tb1046IedByF1046Code and f1057CbName=:f1057CbName";
		hqlDao.updateByHql(hql, params);
	}

	public void updateCbEntity(Tb1046IedEntity ied, String cbRef, Map<String, String> disInfo) {
		Tb1057SgcbEntity sgcb = getSgcbEntity(ied, cbRef);
		if (sgcb == null) {
			return;
		}
		String cbName = disInfo.get("cbName");
		String dsName = disInfo.get("dsName");
		String dsDesc = disInfo.get("dsDesc");
		if (cbName != null)
			sgcb.setF1057CbName(disInfo.get("cbName"));
		if (dsName != null)
			sgcb.setF1057Dataset(disInfo.get("dsName"));
		if (dsDesc != null)
			sgcb.setF1057DsDesc(disInfo.get("dsDesc"));
		beanDao.update(sgcb);
	}

}
