package com.synet.tool.rsc.processor;

import java.util.List;

import com.shrcn.found.common.util.ObjectUtil;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM110LinkWarnEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class DefaultImportProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<?> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.insert(fileInfoEntity);
		for (Object entity : list) {
			ObjectUtil.setProperty(entity, "fileInfoEntity", fileInfoEntity);
		}
		improtInfoService.insertBatch(list);
		return true;
	}

}
