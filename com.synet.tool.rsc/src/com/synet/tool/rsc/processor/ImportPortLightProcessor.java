package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM106PortLightEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportPortLightProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private PortEntityService portEntityService = new PortEntityService();
//	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
//	private StatedataService statedataService = new StatedataService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM106PortLightEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM106PortLightEntity entity : list) {
//			try {
//				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevName(), 
//						entity.getBoardCode(), entity.getPortCode());
//				if (portEntity != null) {
//					Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getOpticalRefAddr());
//					if (mmsfcdaEntity != null) {
//						Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
//								mmsfcdaEntity.getDataCode());
//						if (statedataEntity != null) {
//							statedataEntity.setParentCode(portEntity.getF1048Code());
//							entity.setMatched(DBConstants.MATCHED_OK);
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
