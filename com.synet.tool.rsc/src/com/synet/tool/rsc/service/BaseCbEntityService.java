package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.util.F1011_NO;

public class BaseCbEntityService extends BaseService {
	
	private static final String T_GCB = "Goose";
	private static final String T_SV = "Smv";
	
	public BaseCbEntity getCbEntity(Tb1046IedEntity ied, String cbId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("cbId", cbId);
		String hql = "from " + BaseCbEntity.class.getName() + " where tb1046IedByF1046Code=:tb1046IedByF1046Code and cbId=:cbId";
		List<?> list = hqlDao.getListByHql(hql, params);
		return (BaseCbEntity) ((list!=null && list.size()>0) ? list.get(0) : null);
	}
	
	public void addCbEntity(Tb1046IedEntity ied, String cbId, Map<String, String> disInfo, String type) {
		boolean isGcb = T_GCB.equals(type);
		BaseCbEntity gcb = isGcb ? new Tb1055GcbEntity() : new Tb1056SvcbEntity();
		String prefix = isGcb ? DBConstants.PR_GCB : DBConstants.PR_SVCB;
		String cbCode = rscp.nextTbCode(prefix);
		String cbName = disInfo.get("cbName");
		gcb.setCbCode(cbCode);
		gcb.setCbName(cbName);
		gcb.setTb1046IedByF1046Code(ied);
		gcb.setCbId(cbId);
		gcb.setDataset(disInfo.get("dsName"));
		gcb.setDsDesc(disInfo.get("dsDesc"));
		gcb.setMacAddr(disInfo.get("macAddr"));
		gcb.setVlanid(disInfo.get("vlanid"));
		gcb.setVlanPriority(disInfo.get("vlanPriority"));
		gcb.setAppid(disInfo.get("appid"));
		beanDao.insert(gcb);
		// 状态点
		Tb1016StatedataEntity st = ParserUtil.createStatedata(ied.getF1046Name() + cbId + "状态", "", cbCode, ied, F1011_NO.IED_WRN_GOOSE.getId());
		beanDao.insert(st);
	}
	
	public void deleteCbEntity(Tb1046IedEntity ied, String cbId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tb1046IedByF1046Code", ied);
		params.put("cbId", cbId);
		String hql = "update " + BaseCbEntity.class.getName() + 
				" set deleted=1 where tb1046IedByF1046Code=:tb1046IedByF1046Code and cbId=:cbId";
		hqlDao.updateByHql(hql, params);
	}

	public void updateCbEntity(Tb1046IedEntity ied, String cbId, Map<String, String> disInfo, String type) {
		BaseCbEntity gcb = getCbEntity(ied, cbId);
		if (gcb == null)
			return;
		String cbName = disInfo.get("cbName");
		String dsName = disInfo.get("dsName");
		String dsDesc = disInfo.get("dsDesc");
		String macAddr = disInfo.get("macAddr");
		String vlanid = disInfo.get("vlanid");
		String vlanPriority = disInfo.get("vlanPriority");
		String appid = disInfo.get("appid");
		if (cbName != null)
			gcb.setCbName(disInfo.get("cbName"));
		if (dsName != null)
			gcb.setDataset(disInfo.get("dsName"));
		if (dsDesc != null)
			gcb.setDsDesc(disInfo.get("dsDesc"));
		if (macAddr != null)
			gcb.setMacAddr(disInfo.get("macAddr"));
		if (vlanid != null)
			gcb.setVlanid(disInfo.get("vlanid"));
		if (vlanPriority != null)
			gcb.setVlanPriority(disInfo.get("vlanPriority"));
		if (appid != null)
			gcb.setAppid(disInfo.get("appid"));
		beanDao.update(gcb);
	}
	
}
