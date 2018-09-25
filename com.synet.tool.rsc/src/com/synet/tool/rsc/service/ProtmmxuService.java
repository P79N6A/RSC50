package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.util.DataUtils;

public class ProtmmxuService extends BaseService {
	
	/**
	 * 查询关系是否存在，存在返回true,不存在返回false
	 * @param tb1067CtvtsecondaryByF1067Code
	 * @param f1006Code
	 * @return
	 */
	public boolean relationExistCheck(Tb1067CtvtsecondaryEntity tb1067CtvtsecondaryByF1067Code,
			Tb1006AnalogdataEntity f1006Code) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("tb1067CtvtsecondaryByF1067Code", tb1067CtvtsecondaryByF1067Code);
		params.put("f1006Code", f1006Code);
		Object object = beanDao.getObject(Tb1066ProtmmxuEntity.class, params);
		return object == null ? false : true;
	}
	
	/**
	 * 根据互感器查找保护采样
	 * @param equipmentEntities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Tb1066ProtmmxuEntity> getProtmmxuByCtvtsecondary(List<Tb1067CtvtsecondaryEntity> ctvtsecondaryEntities) {
		if(!DataUtils.listNotNull(ctvtsecondaryEntities)) {
			return new ArrayList<>();
		}
		return (List<Tb1066ProtmmxuEntity>) hqlDao.selectInObjects(Tb1066ProtmmxuEntity.class, "tb1067CtvtsecondaryByF1067Code", ctvtsecondaryEntities);
	}

}
