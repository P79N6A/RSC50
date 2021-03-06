package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportBoardWarnProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private BoardEntityService boardEntityService = new BoardEntityService();
//	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
//	private StatedataService statedataService = new StatedataService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM105BoardWarnEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.insert(fileInfoEntity);
		for (IM105BoardWarnEntity entity : list) {
//			try {
//				Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(entity.getDevName(), entity.getBoardCode());
//				if (tempBoard != null) {
//					Tb1058MmsfcdaEntity tempMmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getAlarmRefAddr());
//					if (tempMmsfcdaEntity != null) {
//						String dataCode = tempMmsfcdaEntity.getDataCode();
//						if (dataCode != null) {
//							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class, dataCode);
//							if (statedataEntity != null) {
//								statedataEntity.setParentCode(tempBoard.getF1047Code());
//								statedataService.save(statedataEntity);
//								entity.setMatched(DBConstants.MATCHED_OK);
//							}
//						}
//					}
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
