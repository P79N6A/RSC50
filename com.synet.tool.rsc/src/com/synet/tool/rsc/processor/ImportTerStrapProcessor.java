package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM107TerStrapEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportTerStrapProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private PinEntityService pinEntityService = new PinEntityService();
//	private PoutEntityService poutEntityService = new PoutEntityService();
//	private MmsfcdaService mmsfcdaService = new MmsfcdaService();
//	private StatedataService statedataService = new StatedataService();
//	private StrapEntityService strapEntityService = new StrapEntityService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM107TerStrapEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM107TerStrapEntity entity : list) {
//			try {
//				String vpType = entity.getVpType();
//				if ("开入".equals(vpType)) {
//					Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getVpRefAddr());
//					if (pinEntity != null) {
//						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getStrapRefAddr());
//						if (mmsfcdaEntity != null) {
//							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
//									mmsfcdaEntity.getDataCode());
//							if (statedataEntity != null) {
//								if (statedataEntity.getParentCode() != null) {
//									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
//											statedataEntity.getParentCode());
//									if (strapEntity != null) {
//										pinEntity.setTb1064StrapByF1064Code(strapEntity);
//										pinEntityService.save(pinEntity);
//										entity.setMatched(DBConstants.MATCHED_OK);
//									}
//								}
//							}
//						}
//					}
//				} else if ("开出".equals(vpType)){
//					Tb1061PoutEntity poutEntity = poutEntityService.getPoutEntity(entity.getDevName(), entity.getVpRefAddr());
//					if (poutEntity != null) {
//						Tb1058MmsfcdaEntity mmsfcdaEntity = mmsfcdaService.getMmsfcdaByF1058RedAddr(entity.getDevName(), entity.getStrapRefAddr());
//						if (mmsfcdaEntity != null) {
//							Tb1016StatedataEntity statedataEntity = (Tb1016StatedataEntity) statedataService.getById(Tb1016StatedataEntity.class,
//									mmsfcdaEntity.getDataCode());
//							if (statedataEntity != null) {
//								if (statedataEntity.getParentCode() != null) {
//									Tb1064StrapEntity strapEntity = (Tb1064StrapEntity) strapEntityService.getById(Tb1062PinEntity.class, 
//											statedataEntity.getParentCode());
//									if (strapEntity != null) {
//										poutEntity.setTb1064StrapByF1064Code(strapEntity);
//										poutEntityService.save(poutEntity);
//										entity.setMatched(DBConstants.MATCHED_OK);
//									}
//								}
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
