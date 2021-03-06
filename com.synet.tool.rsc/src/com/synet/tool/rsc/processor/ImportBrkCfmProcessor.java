package com.synet.tool.rsc.processor;

import java.util.List;

import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM108BrkCfmEntity;
import com.synet.tool.rsc.service.ImprotInfoService;

public class ImportBrkCfmProcessor {
	
	private ImprotInfoService improtInfoService = new ImprotInfoService();
//	private PinEntityService pinEntityService = new PinEntityService();
//	private PoutEntityService poutEntityService = new PoutEntityService();
//	private CircuitEntityService circuitEntityService = new CircuitEntityService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM108BrkCfmEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		for (IM108BrkCfmEntity entity : list) {
//			try {
//				Tb1062PinEntity pinEntity = pinEntityService.getPinEntity(entity.getDevName(), entity.getPinRefAddr());
//				if (pinEntity != null) {
//					Tb1063CircuitEntity circuitEntity = circuitEntityService.getCircuitEntity(pinEntity);
//					if (circuitEntity != null) {
//						Tb1061PoutEntity poutEntity1 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdAckVpRefAddr());
//						if (poutEntity1 != null) {
//							circuitEntity.setTb1061PoutByF1061CodeConvChk1(poutEntity1);
//							entity.setMatched(DBConstants.MATCHED_OK);
//						}
//						Tb1061PoutEntity poutEntity2 = poutEntityService.getPoutEntity(entity.getDevName(), entity.getCmdOutVpRefAddr());
//						if (poutEntity2 != null) {
//							circuitEntity.setTb1061PoutByF1061CodeConvChk2(poutEntity1);
//							entity.setMatched(DBConstants.MATCHED_OK);
//						}
//						circuitEntityService.save(circuitEntity);
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
