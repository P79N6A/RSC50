package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.Map;

import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;

public class PortEntityService extends BaseService{

	public Tb1048PortEntity existsEntity(Tb1048PortEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("", entity.getTb1047BoardByF1047Code());
		params.put("", entity.getF1048Direction());
		params.put("", entity.getF1048No());
		params.put("", entity.getF1048Plug());
		return (Tb1048PortEntity) beanDao.getObject(Tb1048PortEntity.class, params);
	}
	
	/**
	 * 根据装置Name，板卡编号，端口编号查询端口信息
	 * @param devName
	 * @param slot
	 * @param f1048No
	 * @return
	 */
	public Tb1048PortEntity getPortEntity(String devName, String slot,String f1048No) {
		//TODO devName是英文名，还是中文名待确认?
		Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Desc", devName);
		if (ied != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("tb1046IedByF1046Code",ied);
			params.put("f047Slot", slot);
			Tb1047BoardEntity boardEntity = (Tb1047BoardEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
			if (boardEntity != null) {
				params.clear();
				params.put("tb1047BoardByF1047Code",boardEntity);
				params.put("f1048No", f1048No);
				return (Tb1048PortEntity) beanDao.getObject(Tb1047BoardEntity.class, params);
			}
		}
		return null;
	}
}
