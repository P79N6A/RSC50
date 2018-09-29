package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.List;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.service.CableEntityService;
import com.synet.tool.rsc.service.CoreEntityService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.PhyconnEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.SubstationService;

public class ImportFibreListProcessor2 {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private SubstationService substationService = new SubstationService();
//	private CubicleEntityService cubicleService = new CubicleEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private CableEntityService cableEntityService = new CableEntityService();
	private CoreEntityService coreEntityService = new CoreEntityService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
	private IedEntityService iedEntityService = new IedEntityService();
	private List<IM102FibreListEntity> fibreListEntitieList = new ArrayList<>();
	//存放解析出来的光缆（去重）
	private List<Tb1051CableEntity> cableEntitieList = new ArrayList<>();
	private List<Tb1052CoreEntity> coreEntitieList = new ArrayList<>();//光缆芯线
	private List<Tb1053PhysconnEntity> physconnEntitieList = new ArrayList<>();
	
	public boolean processor(IM100FileInfoEntity fileInfoEntity, List<IM102FibreListEntity> list){
		cableEntitieList.clear();
		coreEntitieList.clear();
		physconnEntitieList.clear();
		if (list == null || list.size() <= 0)
			return false;
		improtInfoService.save(fileInfoEntity);
		
		//保存导入信息
		save(fileInfoEntity, list);
		return true;
	}
	
	public void importData(List<IM102FibreListEntity> list) {
		cableEntitieList.clear();
		coreEntitieList.clear();
		physconnEntitieList.clear();
		fibreListEntitieList.clear();
		fibreListEntitieList.addAll(list);
		
		if (list == null || list.size() <= 0) return;
		List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
		//处理光缆信息
		if (substationList == null || substationList.size() <= 0) return;
		analysisCable(substationList.get(0));
		//处理芯线信息
		analysisCore();
		//处理物理回路
		analysisPhyconn(substationList.get(0));
		//保存光缆、芯线、物理回路
		cableEntityService.saveBatch(cableEntitieList);
		coreEntityService.saveBatch(coreEntitieList);
		phyconnEntityService.saveBatch(physconnEntitieList);
	}
	
	//处理物理回路
	private void analysisPhyconn(Tb1041SubstationEntity substationEntity) {
		for (IM102FibreListEntity entity : fibreListEntitieList) {
			Tb1048PortEntity portEntityA = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
			Tb1048PortEntity portEntityB = portEntityService.getPortEntity(entity.getDevNameB(), entity.getBoardCodeB(), entity.getPortCodeB());
			if (portEntityA == null || portEntityB == null) continue;
			Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
			physconnEntity.setF1041Code(substationEntity.getF1041Code());
			physconnEntity.setTb1048PortByF1048CodeA(portEntityA);
			physconnEntity.setTb1048PortByF1048CodeB(portEntityB);
			Tb1053PhysconnEntity oldPhysconnEntity = phyconnEntityService.existEntity(physconnEntity);
			//重复则更新
			if (oldPhysconnEntity != null) {
				physconnEntity.setF1053Code(oldPhysconnEntity.getF1053Code());
			} else {
				physconnEntity.setF1053Code(rscp.nextTbCode(DBConstants.PR_PHYSCONN));
			}
			physconnEntitieList.add(physconnEntity);
		}
	}

	//处理光缆数据
	private void analysisCable(Tb1041SubstationEntity substationEntity) {
		for (IM102FibreListEntity entity : fibreListEntitieList) {
			Tb1046IedEntity iedEntityA = iedEntityService.getIedEntityByDevName(entity.getDevNameA());
			Tb1046IedEntity iedEntityB = iedEntityService.getIedEntityByDevName(entity.getDevNameB());
			if (iedEntityA == null || iedEntityB == null) continue;
			Tb1050CubicleEntity cubicleEntityA = iedEntityA.getTb1050CubicleEntity();
			Tb1050CubicleEntity cubicleEntityB = iedEntityB.getTb1050CubicleEntity();
			if (cubicleEntityA == null || cubicleEntityB == null) continue;//屏柜都不能为空
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			Tb1051CableEntity cableEntity = new Tb1051CableEntity();
			cableEntity.setF1051Name(entity.getCableCode());
			cableEntity.setF1051Type(DBConstants.CABLE_TYPE_WL);//现默认为“尾缆”
			Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
			if (oldCableEntity == null) {//该光缆还未处理
				cableEntity.setTb1041SubstationByF1041Code(substationEntity);
				cableEntity.setTb1050CubicleByF1050CodeA(cubicleEntityA);
				cableEntity.setTb1050CubicleByF1050CodeB(cubicleEntityB);
				Tb1051CableEntity oldEntity = cableEntityService.existEntity(cableEntity);
				//重复，则更新
				if (oldEntity != null) {
					cableEntity.setF1051Code(oldEntity.getF1051Code());
				} else {
					cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
				}
				cableEntitieList.add(cableEntity);
			}
		}
	}
	
	//处理芯线数据
	private void analysisCore() {
		for (IM102FibreListEntity entity : fibreListEntitieList) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			Tb1048PortEntity portEntityA = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
			Tb1048PortEntity portEntityB = portEntityService.getPortEntity(entity.getDevNameB(), entity.getBoardCodeB(), entity.getPortCodeB());
			if (portEntityA == null || portEntityB == null) continue; //端口都不能为空
			
			//处理屏柜A跳线
			Tb1046IedEntity iedEntityA = iedEntityService.getIedEntityByDevName(entity.getDevNameA());
			if (iedEntityA == null ) continue;
			Tb1050CubicleEntity cubicleEntityA = iedEntityA.getTb1050CubicleEntity();
			if (cubicleEntityA != null) {
				Tb1048PortEntity portEntityAA = portEntityService.getPortEntity(entity.getDistribFrameCodeA(), "X1", entity.getDistribFramePortNoA());
				if (portEntityAA != null) {
					Tb1052CoreEntity coreEntity1 = createCore(entity, portEntityA, portEntityAA, cubicleEntityA);
					coreEntitieList.add(coreEntity1);
				}
			}
			
			//处理屏柜B跳线
			Tb1046IedEntity iedEntityB = iedEntityService.getIedEntityByDevName(entity.getDevNameB());
			if (iedEntityB == null ) continue;
			Tb1050CubicleEntity cubicleEntityB = iedEntityB.getTb1050CubicleEntity();
			if (cubicleEntityB != null) {
				Tb1048PortEntity portEntityBB = portEntityService.getPortEntity(entity.getDistribFrameCodeA(), "X1", entity.getDistribFramePortNoA());
				if (portEntityBB != null) {
					Tb1052CoreEntity coreEntity2 = createCore(entity, portEntityBB, portEntityB, cubicleEntityB);
					coreEntitieList.add(coreEntity2);
				}
			}
			
			//处理光缆芯线
			Tb1052CoreEntity coreEntity = createCore(entity, portEntityA, portEntityB);
			coreEntitieList.add(coreEntity);
			
		}
	}
	
	//创建芯线
	private Tb1052CoreEntity createCore(IM102FibreListEntity entity, Tb1048PortEntity portEntityA, 
			Tb1048PortEntity portEntityB) {
		Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
		coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
		if (entity.getCoreCode() == null) return null;//纤芯编号不存在，不处理
		int coreNo = Integer.valueOf(entity.getCoreCode());
		coreEntity.setF1052No(coreNo);
		//处理端口类型
		String portCodeA = entity.getBoardCodeA();
		if (portCodeA != null) {
			if (portCodeA.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeA.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {// (portCodeA.contains(ExcelConstants.PORT_TYPE_RT)) 不是收或者发的，都算收发
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		String portCodeB = entity.getBoardCodeB();
		if (portCodeB != null) {
			if (portCodeB.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeB.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {// (portCodeB.contains(ExcelConstants.PORT_TYPE_RT)) 不是收或者发的，都算收发
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		coreEntity.setF1048CodeA(portEntityA.getF1048Code());
		coreEntity.setF1048CodeB(portEntityB.getF1048Code());
		Tb1051CableEntity cableEntity = new Tb1051CableEntity();
		cableEntity.setF1051Name(entity.getCableCode());
		cableEntity.setF1051Type(DBConstants.CABLE_TYPE_WL);//现默认为“尾缆”
		Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
		if (oldCableEntity == null) return null;
		coreEntity.setParentCode(oldCableEntity.getF1051Code());
//		coreEntity.setTb1051CableByParentCode(oldCableEntity);
		Tb1052CoreEntity oldCoreEntity = coreEntityService.existEntity(coreEntity);
		//去重，存在则更新
		if (oldCoreEntity != null) {
			coreEntity.setF1052Code(oldCoreEntity.getF1052Code());
		} else {
			coreEntity.setF1052Code(rscp.nextTbCode(DBConstants.PR_CORE));
		}
		return coreEntity;
	}
	
	//创建跳线
	private Tb1052CoreEntity createCore(IM102FibreListEntity entity, Tb1048PortEntity portEntityA, 
			Tb1048PortEntity portEntityB, Tb1050CubicleEntity cubicleEntity) {
		Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
		coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
		if (entity.getCoreCode() == null) return null;//纤芯编号不存在，不处理
		int coreNo = Integer.valueOf(entity.getCoreCode());
		coreEntity.setF1052No(coreNo);
		//处理端口类型
		String portCodeA = entity.getBoardCodeA();
		if (portCodeA != null) {
			if (portCodeA.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeA.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {// (portCodeA.contains(ExcelConstants.PORT_TYPE_RT)) 不是收或者发的，都算收发
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		String portCodeB = entity.getBoardCodeB();
		if (portCodeB != null) {
			if (portCodeB.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeB.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {// (portCodeB.contains(ExcelConstants.PORT_TYPE_RT)) 不是收或者发的，都算收发
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		coreEntity.setF1048CodeA(portEntityA.getF1048Code());
		coreEntity.setF1048CodeB(portEntityB.getF1048Code());
		if (cubicleEntity == null) return null;
		coreEntity.setParentCode(cubicleEntity.getF1050Code());
//			coreEntity.setTb1051CableByParentCode(oldCableEntity);
		Tb1052CoreEntity oldCoreEntity = coreEntityService.existEntity(coreEntity);
		//去重，存在则更新
		if (oldCoreEntity != null) {
			coreEntity.setF1052Code(oldCoreEntity.getF1052Code());
		} else {
			coreEntity.setF1052Code(rscp.nextTbCode(DBConstants.PR_CORE));
		}
		return coreEntity;
	}
	
	@SuppressWarnings("unused")
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
			if (entity.getF1051Name().equals(cableEntity.getF1051Name()) 
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
