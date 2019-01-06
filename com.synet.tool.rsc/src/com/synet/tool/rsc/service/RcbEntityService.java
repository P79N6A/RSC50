package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;

public class RcbEntityService extends BaseService {
	
	public Tb1054RcbEntity getRcbEntity(Tb1046IedEntity ied, String cbRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("f1054Rptid", cbRef);
		String hql = "from " + Tb1054RcbEntity.class.getName() + " where tb1046IedByF1046Code=:tb1046IedByF1046Code and f1054Rptid=:f1054Rptid";
		List<?> list = hqlDao.getListByHql(hql, params);
		return (Tb1054RcbEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
	}
	
	public void addCbEntity(Tb1046IedEntity ied, String cbRef, Map<String, String> disInfo) {
		Tb1054RcbEntity rcb = new Tb1054RcbEntity();
		rcb.setF1054Code(rscp.nextTbCode(DBConstants.PR_RCB));
		rcb.setTb1046IedByF1046Code(ied);
		rcb.setF1054Dataset(disInfo.get("dsName"));
		rcb.setF1054DsDesc(disInfo.get("dsDesc"));
		int brcb = Integer.parseInt(disInfo.get("brcb"));
		rcb.setF1054IsBrcb(brcb);
		rcb.setF1054Rptid(cbRef);
		int cbType = Integer.parseInt(disInfo.get("cbType"));
		rcb.setF1054CbType(cbType);
		beanDao.insert(rcb);
	}
	
	public void deleteCbEntity(Tb1046IedEntity ied, String cbRef) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("f1054Rptid", cbRef);
		String hql = "update " + Tb1054RcbEntity.class.getName() + 
				" set deleted=1 where tb1046IedByF1046Code=:tb1046IedByF1046Code and f1054Rptid=:f1054Rptid";
		hqlDao.updateByHql(hql, params);
	}

	public void updateCbEntity(Tb1046IedEntity ied, String cbRef, Map<String, String> disInfo) {
		Tb1054RcbEntity rcb = getRcbEntity(ied, cbRef);
		if (rcb == null)
			return;
		if (disInfo.get("dsName") != null)
			rcb.setF1054Dataset(disInfo.get("dsName"));
		if (disInfo.get("dsDesc") != null)
			rcb.setF1054DsDesc(disInfo.get("dsDesc"));
		if (disInfo.get("brcb") != null) {
			int brcb = Integer.parseInt(disInfo.get("brcb"));
			rcb.setF1054IsBrcb(brcb);
		}
		if (disInfo.get("cbType") != null) {
			int cbType = Integer.parseInt(disInfo.get("cbType"));
			rcb.setF1054CbType(cbType);
		}
		beanDao.update(rcb);
	}

}
