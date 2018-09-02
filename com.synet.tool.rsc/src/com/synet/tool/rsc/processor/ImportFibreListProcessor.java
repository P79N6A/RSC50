package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.CubicleEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.SubstationService;

public class ImportFibreListProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private SubstationService substationService = new SubstationService();
	private CubicleEntityService cubicleService = new CubicleEntityService();
	//存放解析出来的光缆（去重）
	private List<Tb1051CableEntity> cableEntitieList = new ArrayList<>();
	private List<Tb1052CoreEntity> coreEntitieList = new ArrayList<>();
	private List<Tb1053PhysconnEntity> physconnEntitieList = new ArrayList<>();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, Map<String, List<IM102FibreListEntity>> map){
		cableEntitieList.clear();
		coreEntitieList.clear();
		physconnEntitieList.clear();
		if (map == null || map.size() <= 0)
			return false;
		try {
			improtInfoService.save(fileInfoEntity);
			List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
			//map 的key值为屏柜的中文名称
			for (String cubicleDesc : map.keySet()) {
				Tb1050CubicleEntity cubicleEntity = cubicleService.getCubicleEntityByDesc(cubicleDesc);
				if (substationList != null && substationList.size() > 0 
						&& cubicleEntity != null) {
					List<IM102FibreListEntity> fibreListEntities = map.get(cubicleDesc);
					if (fibreListEntities != null && fibreListEntities.size() > 0) {
						analysisCable(substationList.get(0), cubicleEntity, fibreListEntities);
						save(fileInfoEntity, fibreListEntities);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void analysisCable(Tb1041SubstationEntity substationEntity, Tb1050CubicleEntity cubicleEntity, List<IM102FibreListEntity> list) {
		for (IM102FibreListEntity entity : list) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			Tb1051CableEntity cableEntity = new Tb1051CableEntity();
			cableEntity.setF1051Name(entity.getCableCode());
			cableEntity.setF1051Type(DBConstants.CABLE_TYPE_WL);//现默认为“尾缆”
			Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
			if (oldCableEntity == null) {
				cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
				cableEntity.setTb1041SubstationByF1041Code(substationEntity);
				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
				cableEntity.setTb1050CubicleByF1050CodeA(cubicleEntity);
				cableEntitieList.add(cableEntity);
				analysisCore(cubicleEntity, cableEntity, list);
			} else {
				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
				oldCableEntity.setTb1050CubicleByF1050CodeB(cubicleEntity);
				analysisCore(cubicleEntity, oldCableEntity, list);
			}
		}
	}
		
	private void analysisCore(Tb1050CubicleEntity cubicleEntity,
			Tb1051CableEntity cableEntity, List<IM102FibreListEntity> list) {
		for (IM102FibreListEntity entity : list) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			if (entity.getCableCode().equals(cableEntity.getF1051Name())) {
				Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
//				coreEntity.setF1052No(entity.getCoreCode());
				Tb1052CoreEntity oldCoreEntity = checkCoreEntity(coreEntity);
			}
		}
	}
	
	private Tb1052CoreEntity checkCoreEntity(Tb1052CoreEntity coreEntity) {
		for (Tb1052CoreEntity entity : coreEntitieList) {
			if (entity.getTb1051CableByParentCode() == coreEntity.getTb1051CableByParentCode() 
					&& entity.getF1052Type() == coreEntity.getF1052Type() 
					&& entity.getF1052No() == coreEntity.getF1052No()) {
				return entity;
			}
		}
		return null;
	}

	private Tb1051CableEntity checkCableEntity(Tb1051CableEntity cableEntity) {
		for (Tb1051CableEntity entity : cableEntitieList) {
			if (entity.getF1051Name().equals(entity.getF1051Name()) 
					&& entity.getF1051Type() == cableEntity.getF1051Type()) {
				return entity;
			}
		}
		return null;
	}

	private void save(IM100FileInfoEntity fileInfoEntity, List<IM102FibreListEntity> list) {
		for (IM102FibreListEntity entity : list) {
			entity.setFileInfoEntity(fileInfoEntity);
			improtInfoService.save(entity);
		}
	}

}
