package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportIEDListProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM101IEDListEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		try {
			improtInfoService.save(fileInfoEntity);
			for (IM101IEDListEntity entity : list) {
				entity.setFileInfoEntity(fileInfoEntity);
				improtInfoService.save(entity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
