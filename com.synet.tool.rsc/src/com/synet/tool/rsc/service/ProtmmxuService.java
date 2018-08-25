package com.synet.tool.rsc.service;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.util.DataUtils;

public class ProtmmxuService extends BaseService {
	
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
		List<String> lstF1067Code = new ArrayList<>();
		for (Tb1067CtvtsecondaryEntity ctvtsecondaryEntity : ctvtsecondaryEntities) {
			lstF1067Code.add(ctvtsecondaryEntity.getF1067Code());
		}
		//TODO
		return (List<Tb1066ProtmmxuEntity>) hqlDao.selectInObjects(Tb1066ProtmmxuEntity.class, "f1067Code", lstF1067Code);
	}

}
