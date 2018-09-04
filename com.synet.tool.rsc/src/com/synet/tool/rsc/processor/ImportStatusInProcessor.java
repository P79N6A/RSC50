package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM104StatusInEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.MmsfcdaService;
import com.synet.tool.rsc.service.PinEntityService;

public class ImportStatusInProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private PinEntityService pinEntityService = new PinEntityService();
	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM104StatusInEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM104StatusInEntity entity : list) {
			try {
				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
				if (pinEntity != null) {
					pinEntity.setF1062Desc(entity.getPinDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
				}
				Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getMmsRefAddr());
				if (mmsfcdaEntity != null) {
					mmsfcdaEntity.setF1058Desc(entity.getMmsDesc());
					entity.setMatched(DBConstants.MATCHED_OK);
				}
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			entity.setFileInfoEntity(fileInfoEntity);
			improtInfoService.save(entity);
		}
		return true;
	}

}
