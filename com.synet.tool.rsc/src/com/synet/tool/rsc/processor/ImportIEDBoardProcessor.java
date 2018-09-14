package com.synet.tool.rsc.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM103IEDBoardEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.util.RscObjectUtils;

public class ImportIEDBoardProcessor {
	
	private IedEntityService iedEntityService = new IedEntityService();
	private BoardEntityService boardEntityService = new BoardEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM103IEDBoardEntity> list){
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		try {
			Map<String, List<Tb1046IedEntity>> iedMap = new HashMap<String, List<Tb1046IedEntity>>();
			for (IM103IEDBoardEntity entity : list) {
				IM103IEDBoardEntity tempIEDBoard = improtInfoService.existsEntity(entity);
				if (tempIEDBoard != null){
					continue;
				}
				String portNumStr = entity.getPortNum();
				int portNum = 0;
				if (portNumStr != null) {
					try {
						portNum = Integer.valueOf(portNumStr);
					} catch(Exception e) {
					}
				}
				String key = entity.getManufacturor() + entity.getDevName() + entity.getConfigVersion();
				List<Tb1046IedEntity> ieds = iedMap.get(key);
				if (ieds == null) {
					ieds = iedEntityService.getIedByIM103IEDBoard(entity);
					iedMap.put(key, ieds);
				}
				for (Tb1046IedEntity ied : ieds) {
					Tb1047BoardEntity boardEntity = RscObjectUtils.createBoardEntity();
					boardEntity.setTb1046IedByF1046Code(ied);
					boardEntity.setF1047Slot(entity.getBoardIndex());
					boardEntity.setF1047Desc(entity.getBoardModel());
					boardEntity.setF1047Type(entity.getBoardType());
					Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(boardEntity);
					boolean exists = (tempBoard !=  null);
					if (!exists){
						if (portNum > 0) {
							entity.setMatched(DBConstants.MATCHED_OK);
							boardEntityService.insert(boardEntity);
						} else {
							continue;
						}
					} else {
						boardEntity = tempBoard;
					}
					if (exists) {
						boardEntityService.clearBoardPorts(boardEntity);
					}
					if (portNum > 0) {
						char A = 'A';
						for (int i = 0; i < portNum; i++) {
							char c = (char) (A + i);
							Tb1048PortEntity portEntity = RscObjectUtils.createPortEntity();
							portEntity.setTb1047BoardByF1047Code(boardEntity);
							portEntity.setF1048No("" + c);
							portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
							portEntity.setF1048Plug(DBConstants.PLUG_FC);
							portEntityService.insert(portEntity);
						}
					} else if(exists) {
						boardEntityService.delete(boardEntity);
					}
				}
				entity.setFileInfoEntity(fileInfoEntity);
				improtInfoService.insert(entity);
			}
			// 更新板卡数量
			for (List<Tb1046IedEntity> ieds : iedMap.values()) {
				for (Tb1046IedEntity ied : ieds) {
					iedEntityService.updateBoardNum(ied);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
