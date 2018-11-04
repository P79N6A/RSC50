package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportLinkWarnProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM110LinkWarnEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM110LinkWarnEntity entity : list) {
			entity.setFileInfoEntity(fileInfoEntity);
			improtInfoService.save(entity);
		}
		return true;
	}

}
