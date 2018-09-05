package com.synet.tool.rsc.processor;

import java.util.List;

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
		for (IM103IEDBoardEntity entity : list) {
			try {
				IM103IEDBoardEntity tempIEDBoard = improtInfoService.existsEntity(entity);
				if (tempIEDBoard != null){
					continue;
				} 
				Tb1046IedEntity ied = iedEntityService.getIedByIM103IEDBoard(entity);
				if (ied != null) {
					String portNumStr = entity.getPortNum();
					Tb1047BoardEntity boardEntity = RscObjectUtils.createBoardEntity();
					boardEntity.setTb1046IedByF1046Code(ied);
					boardEntity.setF1047Slot(entity.getBoardIndex());
					boardEntity.setF1047Desc(entity.getBoardModel());
					boardEntity.setF1047Type(entity.getBoardType());
					Tb1047BoardEntity tempBoard = boardEntityService.existsEntity(boardEntity);
					if (tempBoard ==  null){
						boardEntityService.insert(boardEntity);
						entity.setMatched(DBConstants.MATCHED_OK);
						System.out.println("添加板卡成功");
					} else {
						boardEntity = tempBoard;
					}
					if (portNumStr != null) {
						int portNum = Integer.valueOf(portNumStr);
						if (portNum > 0) {
							char A = 'A';
							for (int i = 0; i < portNum; i++) {
								char c = (char) (A + i);
								Tb1048PortEntity portEntity = RscObjectUtils.createPortEntity();
								portEntity.setTb1047BoardByF1047Code(boardEntity);
								portEntity.setF1048No("" + c);
								portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
								portEntity.setF1048Plug(DBConstants.PLUG_FC);
								if (portEntityService.existsEntity(portEntity) == null) {
									portEntityService.insert(portEntity);
									System.out.println("添加端口成功");
									entity.setMatched(DBConstants.MATCHED_OK);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			entity.setFileInfoEntity(fileInfoEntity);
			improtInfoService.insert(entity);
		}
		return true;
	}

}