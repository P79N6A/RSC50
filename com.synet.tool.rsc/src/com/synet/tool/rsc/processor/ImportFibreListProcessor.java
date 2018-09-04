package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.CableEntityService;
import com.synet.tool.rsc.service.CoreEntityService;
import com.synet.tool.rsc.service.CubicleEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PhyconnEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.SubstationService;

public class ImportFibreListProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private SubstationService substationService = new SubstationService();
	private CubicleEntityService cubicleService = new CubicleEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private CableEntityService cableEntityService = new CableEntityService();
	private CoreEntityService coreEntityService = new CoreEntityService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
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
		improtInfoService.save(fileInfoEntity);
		List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
		//map 的key值为屏柜的中文名称
		for (String cubicleDesc : map.keySet()) {
			List<IM102FibreListEntity> fibreListEntities = map.get(cubicleDesc);
			try {
				Tb1050CubicleEntity cubicleEntity = cubicleService.getCubicleEntityByDesc(cubicleDesc);
				if (substationList != null && substationList.size() > 0 
						&& cubicleEntity != null) {
					if (fibreListEntities != null && fibreListEntities.size() > 0) {
						analysisCable(substationList.get(0), cubicleEntity, fibreListEntities);
						analysisPhyconn(substationList.get(0));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			cableEntityService.saveBatch(cableEntitieList);
			coreEntityService.saveBatch(coreEntitieList);
			phyconnEntityService.saveBatch(physconnEntitieList);
			save(fileInfoEntity, fibreListEntities);
		}
		return true;
	}
	
	//处理物理连接
	private void analysisPhyconn(Tb1041SubstationEntity substationEntity) {
		for (Tb1052CoreEntity coreEntity : coreEntitieList) {
			Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
			physconnEntity.setF1053Code(rscp.nextTbCode(DBConstants.PR_PHYSCONN));
			physconnEntity.setF1041Code(substationEntity.getF1041Code());
			String portCodeA = coreEntity.getF1048CodeA();
			String portCodeB = coreEntity.getF1048CodeB();
			if (portCodeA != null) {
				Tb1048PortEntity portEntityA = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeA);
				physconnEntity.setTb1048PortByF1048CodeA(portEntityA);
			}
			if (portCodeB != null) {
				Tb1048PortEntity portEntityB = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeB);
				physconnEntity.setTb1048PortByF1048CodeA(portEntityB);
			}
			physconnEntitieList.add(physconnEntity);
		}
	}

	//处理光缆数据
	private void analysisCable(Tb1041SubstationEntity substationEntity, Tb1050CubicleEntity cubicleEntity, List<IM102FibreListEntity> list) {
		for (IM102FibreListEntity entity : list) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			Tb1051CableEntity cableEntity = new Tb1051CableEntity();
			cableEntity.setF1051Name(entity.getCableCode());
			cableEntity.setF1051Type(DBConstants.CABLE_TYPE_WL);//现默认为“尾缆”
			Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
			if (oldCableEntity == null) {
				cableEntity.setTb1041SubstationByF1041Code(substationEntity);
				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
				cableEntity.setTb1050CubicleByF1050CodeA(cubicleEntity);
				cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
				cableEntitieList.add(cableEntity);
				analysisCore(cubicleEntity, cableEntity, list);
			} else {
				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
				oldCableEntity.setTb1050CubicleByF1050CodeB(cubicleEntity);
				analysisCore(cubicleEntity, oldCableEntity, list);
			}
		}
	}
	
	//处理光芯数据
	private void analysisCore(Tb1050CubicleEntity cubicleEntity,
			Tb1051CableEntity cableEntity, List<IM102FibreListEntity> list) {
		for (IM102FibreListEntity entity : list) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			if (entity.getCableCode().equals(cableEntity.getF1051Name())) {
				Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
				coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
				if (entity.getCoreCode() == null) continue;//纤芯编号不存在，不处理
				int coreNo = Integer.valueOf(entity.getCoreCode());
				coreEntity.setF1052No(coreNo);
				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
				if (portEntity != null) {
					//处理端口类型
					String portCode = entity.getBoardCodeA();
					if (portCode != null) {
						if (portCode.contains(ExcelConstants.PORT_TYPE_TX)) {
							portEntity.setF1048Direction(DBConstants.DIRECTION_TX);
						} else if (portCode.contains(ExcelConstants.PORT_TYPE_RX)) {
							portEntity.setF1048Direction(DBConstants.DIRECTION_RX);
						} else if (portCode.contains(ExcelConstants.PORT_TYPE_RT)) {
							portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
						}
					}
					Tb1052CoreEntity oldCoreEntity = checkCoreEntity(coreEntity);
					if (oldCoreEntity == null) {
						if (portEntity != null) {
							switch (portEntity.getF1048Direction()) {
							case 1:
							case 3:
								coreEntity.setF1048CodeA(portEntity.getF1048Code());
								break;
							case 2:
								coreEntity.setF1048CodeB(portEntity.getF1048Code());
								break;
							default:
								break;
							}
							coreEntity.setF1052Code(rscp.nextTbCode(DBConstants.PR_CORE));
							coreEntitieList.add(coreEntity);
						}
					} else {
						if (portEntity != null) {
							switch (portEntity.getF1048Direction()) {
							case 1:
							case 2:
								oldCoreEntity.setF1048CodeB(portEntity.getF1048Code());
								break;
							case 3:
								oldCoreEntity.setF1048CodeA(portEntity.getF1048Code());
								break;
							default:
								break;
							}
						}
					}
				}
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
