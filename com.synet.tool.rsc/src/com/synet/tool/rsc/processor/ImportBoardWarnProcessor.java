package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM105BoardWarnEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.StatedataService;

public class ImportBoardWarnProcessor {
	
	private BoardEntityService boardEntityService = new BoardEntityService();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
	private StatedataService statedataService = new StatedataService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM105BoardWarnEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		try {
			improtInfoService.insert(fileInfoEntity);
			for (IM105BoardWarnEntity entity : list) {
				Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(entity.getDevName(), entity.getBoardCode());
				if (tempBoard != null) {
					Tb1058MmsfcdaEntity tempMmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getAlarmRefAddr());
					if (tempMmsfcdaEntity != null) {
						String dataCode = tempMmsfcdaEntity.getDataCode();
						if (dataCode != null) {
							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class, dataCode);
							if (statedataEntity != null) {
								statedataEntity.setParentCode(tempBoard.getF1047Code());
								statedataService.save(statedataEntity);
								entity.setMatched(DBConstants.MATCHED_OK);
							}
						}
					}
				}
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
