package com.synet.tool.rsc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;

public class ImprotInfoService extends BaseService {
	
	@SuppressWarnings("unchecked")
	public IM100FileInfoEntity existsEntity(IM100FileInfoEntity entity) {
		List<IM100FileInfoEntity> list = (List<IM100FileInfoEntity>) beanDao.getListByCriteria(IM100FileInfoEntity.class, "filePath", entity.getFilePath());
		if (list != null && list.size() > 0) 
			return list.get(0);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public IM103IEDBoardEntity existsEntity(IM103IEDBoardEntity entity) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fileInfoEntity", entity.getFileInfoEntity());
		params.put("boardIndex", entity.getBoardIndex());
		params.put("boardType", entity.getBoardType());
		params.put("boardCode", entity.getBoardCode());
		List<IM103IEDBoardEntity> list = (List<IM103IEDBoardEntity>) beanDao.getListByCriteria(IM103IEDBoardEntity.class, params);
		if (list != null && list.size() > 0) 
			return list.get(0);
		return null;
	}
}
