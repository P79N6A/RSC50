package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM109StaInfoEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportStaInfoProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM109StaInfoEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM109StaInfoEntity entity : list) {
//			try {
//				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
//				if (mmsfcdaEntity != null) {
//					mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
//					entity.setMatched(DBConstants.MATCHED_OK);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
			entity.setFileInfoEntity(fileInfoEntity);
			improtInfoService.save(entity);
		}
		return true;
	}

}
