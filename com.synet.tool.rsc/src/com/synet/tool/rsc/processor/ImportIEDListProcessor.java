package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM101IEDListEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportIEDListProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private IedEntityService iedEntityService = new IedEntityService();
//	private BayEntityService bayEntityService = new BayEntityService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM101IEDListEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM101IEDListEntity entity : list) {
//			try {
//				Tb1046IedEntity iedEntity = iedEntityService.getIedEntityByDevName(entity.getDevName());
//				if (iedEntity != null) {
//					Tb1042BayEntity bayEntity = bayEntityService.getBayEntityByName(entity.getBay());
//					if (bayEntity != null) {
//						iedEntity.setTb1042BaysByF1042Code(bayEntity);
//					}
//					iedEntity.setF1046Manufacturor(entity.getManufacturor());
//					iedEntity.setF1046version(entity.getDevVersion());
//					iedEntity.setF1046protectCategory(entity.getProtClassify());
//					iedEntity.setF1046protectType(entity.getProtType());
//					iedEntity.setF1046protectModel(entity.getProtModel());
//					iedEntity.setF1046OperateDate(entity.getDateService());
//					iedEntity.setF1046productDate(entity.getDateProduct());
//					iedEntity.setF1046productNo(entity.getProductCode());
//					iedEntity.setF1046dataGatType(entity.getDataCollectType());
//					iedEntity.setF1046OutType(entity.getOutType());
//					int num = 0;
//					try {
//						num = Integer.parseInt(entity.getBoardNum());
//					} catch(Exception e) {
//					}
//					iedEntity.setF1046boardNum(num);
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
