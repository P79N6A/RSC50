package com.synet.tool.rsc.processor;


import java.util.ArrayList;
import java.util.List;

import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.valid.DataTypeChecker;
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
import com.synet.tool.rsc.util.ProblemManager;

public class ImportFibreListProcessor3 {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProblemManager pmgr = ProblemManager.getInstance();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private SubstationService substationService = new SubstationService();
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
	
	private Tb1041SubstationEntity substation;
	
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
		
		if (list == null || list.size() <= 0) 
			return;
		List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
		if (substationList == null || substationList.size() <= 0) 
			return;
		substation = substationList.get(0);
		//处理光缆、芯线、物理回路
		analysisCable(substation);
		//保存光缆、芯线、物理回路
		cableEntityService.saveBatch(cableEntitieList);
		coreEntityService.saveBatch(coreEntitieList);
		phyconnEntityService.saveBatch(physconnEntitieList);
		ConsoleManager console = ConsoleManager.getInstance();
		console.append("导入光缆数：" + cableEntitieList.size());
		console.append("导入芯线数：" + coreEntitieList.size());
		console.append("导入物理回路数：" + physconnEntitieList.size());
	}
	
	//处理物理回路
	private void addPhyconn(Tb1048PortEntity portEntityA, Tb1048PortEntity portEntityB) {
		Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
		physconnEntity.setF1041Code(substation.getF1041Code());
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

	//处理光缆数据
	private void analysisCable(Tb1041SubstationEntity substationEntity) {
		for (IM102FibreListEntity entity : fibreListEntitieList) {
			Tb1046IedEntity iedEntityA = iedEntityService.getIedEntityByDevName(entity.getDevNameA());
			Tb1046IedEntity iedEntityB = iedEntityService.getIedEntityByDevName(entity.getDevNameB());
			String cableCode = entity.getCableCode();
			if (iedEntityA == null || iedEntityB == null) {
				String msg = "光缆 " + cableCode + " 连接有误：";
				if (iedEntityA == null) {
					msg += "找不到装置A[" + entity.getDevNameA() + "] ";
				}
				if (iedEntityB == null) {
					msg += "找不到装置B[" + entity.getDevNameB() + "]";
				}
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "装置检查", "", msg));
				continue;
			}
			Tb1050CubicleEntity cubicleEntityA = iedEntityA.getTb1050CubicleEntity();
			Tb1050CubicleEntity cubicleEntityB = iedEntityB.getTb1050CubicleEntity();
			boolean existsCable = !StringUtil.isEmpty(cableCode);
			// 添加光缆和芯线
			if (existsCable) {
				addCable(cableCode, cubicleEntityA, cubicleEntityB);	// 1根光缆
			} else {
				String msg = entity.getDevNameA() + " -> " + entity.getDevNameB() + "之间无光缆！";
				SCTLogger.info(msg);
				pmgr.append(new Problem(0, LEVEL.WARNING, "导入光缆", "光缆检查", entity.getCableCode(), msg));
			}
			addCableCores(entity, cubicleEntityA, cubicleEntityB);		// 3根芯线：A、B端跳纤和1根光缆芯线
		}
	}
	
	private void addCable(String cableCode, Tb1050CubicleEntity cubicleEntityA, Tb1050CubicleEntity cubicleEntityB) {
		Tb1051CableEntity cableEntity = new Tb1051CableEntity();
		cableEntity.setF1051Name(cableCode);
		cableEntity.setF1051Type(DBConstants.CABLE_TYPE_GL);
		Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
		if (oldCableEntity == null) {// 该光缆还未处理
			cableEntity.setTb1041SubstationByF1041Code(substation);
			cableEntity.setTb1050CubicleByF1050CodeA(cubicleEntityA);
			cableEntity.setTb1050CubicleByF1050CodeB(cubicleEntityB);
			cableEntity.setF1051CoreNum(0);
			Tb1051CableEntity oldEntity = cableEntityService.existEntity(cableEntity);
			// 重复，则更新
			if (oldEntity != null) {
				cableEntity.setF1051Code(oldEntity.getF1051Code());
			} else {
				cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
			}
			cableEntitieList.add(cableEntity);
		}
	}
	
	/**
	 * 3根：A、B端跳纤和1根光缆芯线
	 * @param entity
	 * @param cubicleEntityA
	 * @param cubicleEntityB
	 */
	private void addCableCores(IM102FibreListEntity entity, Tb1050CubicleEntity cubicleEntityA, Tb1050CubicleEntity cubicleEntityB) {
		String devNameA = entity.getDevNameA();
		String devNameB = entity.getDevNameB();
		Tb1048PortEntity portEntityA = portEntityService.getPortEntity(devNameA, entity.getBoardCodeA(), entity.getPortCodeA());
		Tb1048PortEntity portEntityB = portEntityService.getPortEntity(devNameB, entity.getBoardCodeB(), entity.getPortCodeB());
		String portA = devNameA + "," + entity.getBoardCodeA() + "," + entity.getPortCodeA();
		String portB = devNameB + "," + entity.getBoardCodeB() + "," + entity.getPortCodeB();
		if (portEntityA == null || portEntityB == null) {
			String msg = "无法创建芯线和物理回路：";
			if (portEntityA == null) {
				msg += "找不到端口A[" + portA + "] ";
			}
			if (portEntityB == null) {
				msg += "找不到端口B[" + portB + "]";
			}
			SCTLogger.error(msg);
			pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线和回路", entity.getCableCode(), msg));
			return;
		}
		
		//处理屏柜A跳线
		Tb1048PortEntity portEntityAA = portEntityService.getPortEntity(entity.getDistribFrameCodeA(), "X1", entity.getDistribFramePortNoA());
		if (portEntityAA != null) {
			String coreCodeA = entity.getCoreCodeA();
			if (StringUtil.isEmpty(coreCodeA) || !DataTypeChecker.checkDigit(coreCodeA)) {
				String msg = "芯线编号为空或非数字，无法创建端口A[" + portA + "]跳纤。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", entity.getCableCode(), msg));
			} else {
				Tb1052CoreEntity coreEntityA = createCore(coreCodeA, portEntityA, portEntityAA, cubicleEntityA);
				if (coreEntityA != null) {
					coreEntitieList.add(coreEntityA);
				}
			}
		}
		
		//处理屏柜B跳线
		Tb1048PortEntity portEntityBB = portEntityService.getPortEntity(entity.getDistribFrameCodeA(), "X1", entity.getDistribFramePortNoA());
		if (portEntityBB != null) {
			String coreCodeB = entity.getCoreCodeB();
			if (StringUtil.isEmpty(coreCodeB) || !DataTypeChecker.checkDigit(coreCodeB)) {
				String msg = "芯线编号为空或非数字，无法创建端口B[" + portB + "]跳纤。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", entity.getCableCode(), msg));
			} else {
				Tb1052CoreEntity coreEntityB = createCore(coreCodeB, portEntityBB, portEntityB, cubicleEntityB);
				if (coreEntityB != null) {
					coreEntitieList.add(coreEntityB);
				}
			}
		}
		
		//处理光缆芯线
		String coreCode = entity.getCoreCode();
		if (StringUtil.isEmpty(coreCode) || !DataTypeChecker.checkDigit(coreCode)) {
			String msg = "芯线编号为空或非数字，无法创建端口[" + portA + "]和[" + portB + "]之间光缆芯线。";
			SCTLogger.error(msg);
			pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", devNameA + " -> " + devNameB, msg));
		} else {
			Tb1052CoreEntity coreEntity = createCore(entity, portEntityA, portEntityB);
			if (coreEntity == null) {
				coreEntity = createCore(coreCode, portEntityA, portEntityB, cubicleEntityA); // 直连
			}
			if (coreEntity != null) {
				coreEntitieList.add(coreEntity);
			} else {
				String msg = "端口[" + portA + "]和[" + portB + "]之间无芯线连接。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", devNameA + " -> " + devNameB, msg));
			}
		}
		
		//处理物理回路
		addPhyconn(portEntityA, portEntityB);
	}

	//创建芯线
	private Tb1052CoreEntity createCore(IM102FibreListEntity entity, Tb1048PortEntity portEntityA, 
			Tb1048PortEntity portEntityB) {
		Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
		coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
		int coreNo = Integer.valueOf(entity.getCoreCode());
		coreEntity.setF1052No(coreNo);
		//处理端口类型
		String portCodeA = entity.getBoardCodeA();
		if (portCodeA != null) {
			if (portCodeA.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeA.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {
				portEntityA.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		String portCodeB = entity.getBoardCodeB();
		if (portCodeB != null) {
			if (portCodeB.contains(ExcelConstants.PORT_TYPE_TX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_TX);
			} else if (portCodeB.contains(ExcelConstants.PORT_TYPE_RX)) {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RX);
			} else {
				portEntityB.setF1048Direction(DBConstants.DIRECTION_RT);
			}
		}
		coreEntity.setTb1048PortByF1048CodeA(portEntityA);
		coreEntity.setTb1048PortByF1048CodeB(portEntityB);
		Tb1051CableEntity cableEntity = new Tb1051CableEntity();
		cableEntity.setF1051Name(entity.getCableCode());
		cableEntity.setF1051Type(DBConstants.CABLE_TYPE_GL);
		Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
		if (oldCableEntity == null) {
			return null;
		} else {
			int num = oldCableEntity.getF1051CoreNum();
			num++;
			oldCableEntity.setF1051CoreNum(num);
		}
		coreEntity.setParentCode(oldCableEntity.getF1051Code());
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
	private Tb1052CoreEntity createCore(String coreCode, 
			Tb1048PortEntity portEntityA, Tb1048PortEntity portEntityB, 
			Tb1050CubicleEntity cubicleEntity) {
		Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
		coreEntity.setF1052Type(DBConstants.CORE_TYPE_TX);
		int coreNo = Integer.valueOf(coreCode);
		coreEntity.setF1052No(coreNo);
		coreEntity.setTb1048PortByF1048CodeA(portEntityA);
		coreEntity.setTb1048PortByF1048CodeB(portEntityB);
		if (cubicleEntity != null) {
			coreEntity.setParentCode(cubicleEntity.getF1050Code());
		}
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
