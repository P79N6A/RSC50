package com.synet.tool.rsc.processor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.valid.DataTypeChecker;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM111FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.service.CableEntityService;
import com.synet.tool.rsc.service.CoreEntityService;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.PhyconnEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.util.ProblemManager;

public class ImportFibreNewListProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProblemManager pmgr = ProblemManager.getInstance();
	private SubstationService substationService = new SubstationService();
	private PortEntityService portEntityService = new PortEntityService();
	private CableEntityService cableEntityService = new CableEntityService();
	private CoreEntityService coreEntityService = new CoreEntityService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
	private IedEntityService iedEntityService = new IedEntityService();
	private BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
	//存放解析出来的光缆（去重）
	private List<IM111FibreListEntity> fibreListEntitieList = new ArrayList<>();
	private List<Tb1051CableEntity> cableEntitieList = new ArrayList<>();
	private List<Tb1052CoreEntity> coreEntitieList = new ArrayList<>();//光缆芯线
	private List<Tb1053PhysconnEntity> physconnEntitieList = new ArrayList<>();
	// 芯线连接关系 A@B
	private Map<String, Tb1052CoreEntity> coreMap = new HashMap<String, Tb1052CoreEntity>();
	
	//保存处理过的端口，用于验证端口重复
	private Map<String, Tb1048PortEntity> portAMap = new HashMap<String, Tb1048PortEntity>();
	private Map<String, Tb1048PortEntity> portBMap = new HashMap<String, Tb1048PortEntity>();
	//光缆芯线编号
	private List<String> cableCores = new ArrayList<>();
	
	private Tb1041SubstationEntity substation;
	
	public void importData(List<IM111FibreListEntity> list, IProgressMonitor monitor) {
		cableEntitieList.clear();
		coreEntitieList.clear();
		physconnEntitieList.clear();
		fibreListEntitieList.clear();
		fibreListEntitieList.addAll(list);
		coreMap.clear();
		portAMap.clear();
		portBMap.clear();
		
		if (list == null || list.size() <= 0) 
			return;
		monitor.beginTask("正在导入数据", 7);
		List<Tb1041SubstationEntity> substationList = substationService.getAllSubstation();
		if (substationList == null || substationList.size() <= 0) 
			return;
		substation = substationList.get(0);
		monitor.worked(1);
		if (monitor.isCanceled()) {
			return;
		}
		// 清除历史数据
		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		beanDao.deleteAll(Tb1073LlinkphyrelationEntity.class);
		beanDao.deleteAll(Tb1053PhysconnEntity.class);
		boolean replaceMode = RSCProperties.getInstance().isReplaceMode();
		if (replaceMode) {
			beanDao.markAllDeleted(Tb1052CoreEntity.class);
			beanDao.markAllDeleted(Tb1051CableEntity.class);
		} else {
			beanDao.deleteAll(Tb1052CoreEntity.class);
			beanDao.deleteAll(Tb1051CableEntity.class);
		}
		
		//处理光缆、芯线、物理回路
		analysisCable(substation);
		monitor.worked(2);
		if (monitor.isCanceled()) {
			return;
		}
		//保存光缆、芯线
		if (replaceMode) { // 增量
			//保存光缆
			Map<String, Tb1051CableEntity> cableMap = new HashMap<>();
			for (Tb1051CableEntity cable : cableEntitieList) {
				String cableName = cable.getF1051Name();
				Tb1051CableEntity existEntity = cableEntityService.getCableEntity(cableName);
				if (existEntity == null) {
					beanDao.insert(cable);
					cableMap.put(cableName, cable);
				} else {
					beanDao.markRecovered(existEntity);
					cableMap.put(cableName, existEntity);
				}
			}
			//保存芯线
			for (Tb1052CoreEntity core : coreEntitieList) {
				String cableName = core.getTb1051CableByParentCode().getF1051Name();
				Tb1051CableEntity cable = cableMap.get(cableName);
				if (cable == null) {
					SCTLogger.error("保存芯线出错，光缆" + cableName + "丢失！");
					continue;
				}
				core.setTb1051CableByParentCode(cable);
				Tb1052CoreEntity existEntity = coreEntityService.getCoreEntity(cable, core.getF1052No());
				if (existEntity == null) {
					beanDao.insert(core);
				} else {
					beanDao.markRecovered(existEntity);
				}
			}
		} else {	// 起始
			cableEntityService.saveBatch(cableEntitieList);
			monitor.worked(1);
			if (monitor.isCanceled()) {
				return;
			}
			coreEntityService.saveBatch(coreEntitieList);
			monitor.worked(1);
			if (monitor.isCanceled()) {
				return;
			}
		}
		//保存物理回路
		phyconnEntityService.saveBatch(physconnEntitieList);
		monitor.worked(1);
		if (monitor.isCanceled()) {
			return;
		}
		ConsoleManager console = ConsoleManager.getInstance();
		console.append("导入光缆数：" + cableEntitieList.size());
		console.append("导入芯线数：" + coreEntitieList.size());
		console.append("导入物理回路数：" + physconnEntitieList.size());
		monitor.done();
	}
	
	private boolean isEndConn(IM111FibreListEntity entryOut) {
		String portB = entryOut.getDevNameB() + "," + entryOut.getBoardCodeB() + "," + entryOut.getPortCodeB();
		Tb1048PortEntity portEntityB = portBMap.get(portB);
//		int f1048Direction = portEntityB.getF1048Direction();
//		return (DBConstants.DIRECTION_RX == f1048Direction 
//				|| DBConstants.DIRECTION_RT == f1048Direction);
		return isDevicePort(portEntityB);
	}
	
	private boolean isDevicePort(Tb1048PortEntity portEntity) {
		return (portEntity != null) && !EnumIedType.GAT_DEVICE.include(
				portEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getF1046Type());
	}
	
	private boolean isStartConn(IM111FibreListEntity entryOut) {
		String portA = entryOut.getDevNameA() + "," + entryOut.getBoardCodeA() + "," + entryOut.getPortCodeA();
		Tb1048PortEntity portEntityA = portAMap.get(portA);
		return isDevicePort(portEntityA);
	}
	
	private void analysisPhyconns() {
		for (IM111FibreListEntity entity : fibreListEntitieList) {
			entity.setUsed(false);
		}
		boolean notUsed = true;
		List<Tb1052CoreEntity> phyCores = new ArrayList<>();
		while(fibreListEntitieList.size()>0 && notUsed) {
			List<IM111FibreListEntity> phyConnList = new ArrayList<>();
			IM111FibreListEntity entryOut = null;
			do {
				entryOut = findfNextFibreOut(entryOut);
				if (entryOut != null) {
					phyConnList.add(entryOut);
					if (isEndConn(entryOut)) {
						break;
					}
				}
			} while (entryOut != null);
			int size = phyConnList.size();
			if (size > 0) {
				IM111FibreListEntity entryFirst = phyConnList.get(0);
				IM111FibreListEntity entryLast = phyConnList.get(size - 1);
				if (isEndConn(entryLast)) {
					for (IM111FibreListEntity entity : phyConnList) {
						String portA = entity.getDevNameA() + "," + entity.getBoardCodeA() + "," + entity.getPortCodeA();
						String portB = entity.getDevNameB() + "," + entity.getBoardCodeB() + "," + entity.getPortCodeB();
						String coreName = portA + "@" + portB;
						if (!coreMap.containsKey(coreName)) {
							continue;
						}
						phyCores.add(coreMap.get(coreName));
					}
					if (phyCores.size() > 0) {
						Tb1053PhysconnEntity phyconn = null;
						Tb1052CoreEntity firstCore = phyCores.get(0);
						Tb1052CoreEntity lastCore = phyCores.get(phyCores.size() - 1);
						phyconn = addPhyconn(firstCore.getTb1048PortByF1048CodeA(), lastCore.getTb1048PortByF1048CodeB());
						for (Tb1052CoreEntity core : phyCores) {
							core.setTb1053ByF1053Code(phyconn);
						}
					}
					phyCores.clear();
				} else {
					String portA = entryFirst.getDevNameA() + "," + entryFirst.getBoardCodeA() + "," + entryFirst.getPortCodeA();
					String portB = entryFirst.getDevNameB() + "," + entryFirst.getBoardCodeB() + "," + entryFirst.getPortCodeB();
					String msg = "未找到接收端口，无法创建物理回路。";
					SCTLogger.error(msg);
					pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "创建回路", portA + " -> " + portB, msg));
				}
			} else {
				System.out.println("分析完毕！");
				break;
			}
			notUsed = false;
			for (IM111FibreListEntity entity : fibreListEntitieList) {
				if (!entity.isUsed()) {
					notUsed = true;
					break;
				}
			}
		}
//		for (int i = 0; i<fibreListEntitieList.size(); i++) {
//			IM111FibreListEntity entity = fibreListEntitieList.get(i);
//			String portA = entity.getDevNameA() + "," + entity.getBoardCodeA() + "," + entity.getPortCodeA();
//			String portB = entity.getDevNameB() + "," + entity.getBoardCodeB() + "," + entity.getPortCodeB();
//			String coreName = portA + "@" + portB;
//			if (!coreMap.containsKey(coreName)) {
//				continue;
//			}
//			phyCores.add(coreMap.get(coreName));
//			coreMap.remove(coreName);
//			portAMap.remove(portA);
//			if (portAMap.containsKey(portB)) {
//				continue;
//			} else {
//				Tb1053PhysconnEntity phyconn = null;
//				Tb1052CoreEntity firstCore = phyCores.get(0);
//				Tb1052CoreEntity lastCore = phyCores.get(phyCores.size() - 1);
//				phyconn = addPhyconn(firstCore.getTb1048PortByF1048CodeA(), lastCore.getTb1048PortByF1048CodeB());
//				for (Tb1052CoreEntity core : phyCores) {
//					core.setTb1053ByF1053Code(phyconn);
//				}
//				phyCores.clear();
//			}
//		}
	}
	
	private IM111FibreListEntity findfNextFibreOut(IM111FibreListEntity entryOut) {
		if (entryOut == null) {
			for (IM111FibreListEntity entity : fibreListEntitieList) {
				if (isStartConn(entity) && !entity.isUsed()) {
					entryOut = entity;
					entryOut.setUsed(true);
					return entryOut;
				}
			}
		} else {
			String portB = entryOut.getDevNameB() + "," + entryOut.getBoardCodeB() + "," + entryOut.getPortCodeB();
			for (IM111FibreListEntity entity : fibreListEntitieList) {
				String portA = entity.getDevNameA() + "," + entity.getBoardCodeA() + "," + entity.getPortCodeA();
				if (portB.equals(portA) && !entity.isUsed()) {
					entity.setUsed(true);
					return entity;
				}
			}
		}
		return null;
	}
	
	//处理物理回路
	private Tb1053PhysconnEntity addPhyconn(Tb1048PortEntity portEntityA, Tb1048PortEntity portEntityB) {
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
		return physconnEntity;
	}

	//处理光缆数据
	private void analysisCable(Tb1041SubstationEntity substationEntity) {
		for (IM111FibreListEntity entity : fibreListEntitieList) {
			Tb1046IedEntity iedEntityA = iedEntityService.getIedEntityByDevName(entity.getDevNameA());
			Tb1046IedEntity iedEntityB = iedEntityService.getIedEntityByDevName(entity.getDevNameB());
			String cableCode = entity.getCableCode();
			if (iedEntityA == null || iedEntityB == null) {
				String msg = "光缆 " + cableCode + " 连接有误：";
				if (iedEntityA == null) {
					msg += "找不到装置A[" + entity.getDevNameA() + "]";
				}
				if (iedEntityB == null) {
					if (iedEntityA == null) {
						msg += "，";
					}
					msg += "找不到装置B[" + entity.getDevNameB() + "]";
				}
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "装置检查", cableCode, msg));
				continue;
			}
			Tb1050CubicleEntity cubicleEntityA = iedEntityA.getTb1050CubicleEntity();
			Tb1050CubicleEntity cubicleEntityB = iedEntityB.getTb1050CubicleEntity();
			if (cubicleEntityA == null || cubicleEntityB == null) {
				String msg = "光缆 " + cableCode + " 连接有误：";
				if (cubicleEntityA == null) {
					msg += "找不到装置[" + iedEntityA.getF1046Name() + "]所在屏柜A";
				}
				if (cubicleEntityB == null) {
					if (cubicleEntityA == null) {
						msg += "，";
					}
					msg += "找不到装置[" + iedEntityB.getF1046Name() + "]所在屏柜B";
				}
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "屏柜检查", cableCode, msg));
				continue;
			}
		
			boolean existsCable = !StringUtil.isEmpty(cableCode);
			// 添加光缆和芯线
			if (existsCable) {
				addCable(cableCode, cubicleEntityA, cubicleEntityB);	// 1根光缆
				entity.setMatched(DBConstants.MATCHED_OK);
			} else {
				String ref = entity.getDevNameA() + " -> " + entity.getDevNameB();
				String msg = ref + "之间无光缆！";
				SCTLogger.info(msg);
				pmgr.append(new Problem(0, LEVEL.WARNING, "导入光缆", "光缆检查", ref, msg));
			}
			addCableCores(entity, cubicleEntityA, cubicleEntityB);		// 3根芯线：A、B端跳纤和1根光缆芯线
		}
		analysisPhyconns();
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
			cableEntity.setF1051CoreNum(1);
			Tb1051CableEntity oldEntity = cableEntityService.existEntity(cableEntity);
			// 重复，则更新
			if (oldEntity != null) {
				cableEntityService.delete(oldEntity);
			}
			cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
			cableEntitieList.add(cableEntity);
		} else {
			int num = (oldCableEntity.getF1051CoreNum() == null) ? 
					0 : oldCableEntity.getF1051CoreNum();
			cableEntity.setF1051CoreNum(num + 1);
		}
	}
	
	private void clearPortA(Tb1048PortEntity portEntityA) {
		beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeA", portEntityA);
		beanDao.deleteAll(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeA", portEntityA);
	}
	
	private void clearPortB(Tb1048PortEntity portEntityB) {
		beanDao.deleteAll(Tb1052CoreEntity.class, "tb1048PortByF1048CodeB", portEntityB);
		beanDao.deleteAll(Tb1053PhysconnEntity.class, "tb1048PortByF1048CodeB", portEntityB);
	}
	
	private String getPortAName(IM111FibreListEntity entity) {
		return entity.getDevNameA() + "," + entity.getBoardCodeA() + "," + entity.getPortCodeA();
	}
	
	private String getPortBName(IM111FibreListEntity entity) {
		return entity.getDevNameB() + "," + entity.getBoardCodeB() + "," + entity.getPortCodeB();
	}
	
	/**
	 * 1根：A、B端跳纤或1根光缆芯线
	 * @param entity
	 * @param cubicleEntityA
	 * @param cubicleEntityB
	 */
	private void addCableCores(IM111FibreListEntity entity, Tb1050CubicleEntity cubicleEntityA, Tb1050CubicleEntity cubicleEntityB) {
		String devNameA = entity.getDevNameA();
		String devNameB = entity.getDevNameB();
		Tb1048PortEntity portEntityA = portEntityService.getPortEntity(devNameA, entity.getBoardCodeA(), entity.getPortCodeA());
		Tb1048PortEntity portEntityB = portEntityService.getPortEntity(devNameB, entity.getBoardCodeB(), entity.getPortCodeB());
		String portA = getPortAName(entity);
		String portB = getPortBName(entity);
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
		//检查端口是否重复，并记录非重复端口
		if (portAMap.containsKey(portA)) {
			String msg = "端口A重复[" + portA + "]" ;
			SCTLogger.error(msg);
			pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "端口重复检查", devNameA, msg));
			return;
		} else if (portBMap.containsKey(portB)) {
			String msg = "端口B重复[" + portB + "]" ;
			SCTLogger.error(msg);
			pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "端口重复检查", devNameB, msg));
			return;
		} else {
			portAMap.put(portA, portEntityA);
			portBMap.put(portB, portEntityB);
			// 清理历史导入的芯线和物理回路，避免重复
			clearPortA(portEntityA);
			clearPortB(portEntityB);
		}
		//端口检查通过则通过
		entity.setMatched(DBConstants.MATCHED_OK);

		//处理光缆芯线
		String cableCode = entity.getCableCode();
		String coreCode = entity.getCoreCode();
		Tb1052CoreEntity coreEntity = null;
		if (!StringUtil.isEmpty(cableCode) && !StringUtil.isEmpty(coreCode)) {
			if (DataTypeChecker.checkDigit(coreCode)) {
				if (portEntityA != null && portEntityB != null) {
					coreEntity = createCore(entity, portEntityA, portEntityB);					// 光缆连接
					String key  = cableCode + "." + coreCode;
					if (cableCores.contains(key)) {
						String msg = "光缆 " + cableCode + " 存在重复的芯线编号 [" + coreCode + "] 。";
						SCTLogger.error(msg);
						pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", cableCode, msg));
					} else {
						cableCores.add(key);
					}
				} else {
					if (portEntityA == null) {
						String msg = "端口[" + portA + "]不存在，无法创建端口[" + portA + "]和[" + portB + "]之间光缆芯线。";
						SCTLogger.error(msg);
						pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", cableCode, msg));
					}
					if (portEntityB == null) {
						String msg = "端口[" + portB + "]不存在，无法创建端口[" + portA + "]和[" + portB + "]之间光缆芯线。";
						SCTLogger.error(msg);
						pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", cableCode, msg));
					}
				}
			} else {
				String msg = "芯线编号[" + coreCode + "]不是数字，无法创建端口[" + portA + "]和[" + portB + "]之间光缆芯线。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", cableCode, msg));
			}
		} else {
			if (!StringUtil.isEmpty(coreCode)) {
				coreEntity = createCore(coreCode, portEntityA, portEntityB, cubicleEntityA); // 芯线直连
			} else {
				String msg = "芯线编号[" + coreCode + "]为空或者不是数字，无法创建端口[" + portA + "]和[" + portB + "]之间光缆芯线。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", cableCode, msg));
			}
		}
		if (coreEntity != null) {
			coreMap.put(portA + "@" + portB, coreEntity);
			coreEntitieList.add(coreEntity);
		} else {
//			String msg = "端口[" + portA + "]和[" + portB + "]之间无芯线连接。";
//			SCTLogger.error(msg);
//			pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", portA + " -> " + portB, msg));
		}
		
	}

	//创建芯线
	private Tb1052CoreEntity createCore(IM111FibreListEntity entity, Tb1048PortEntity portEntityA, 
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
			String cubicleA = portEntityA.getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getTb1050CubicleEntity().getF1050Name();
			String cubicleB = portEntityB.getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getTb1050CubicleEntity().getF1050Name();
			String portA = getPortAName(entity);
			String portB = getPortBName(entity);
			String cableCubicleA = oldCableEntity.getTb1050CubicleByF1050CodeA().getF1050Name();
			String cableCubicleB = oldCableEntity.getTb1050CubicleByF1050CodeB().getF1050Name();
			if (!cubicleA.equals(cableCubicleA) && !cubicleA.equals(cableCubicleB)) {
				String msg = "A端屏柜不同，芯线为[" + cubicleA + "]，光缆为[" + cableCubicleA + "，" + 
									cableCubicleB + "]。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", portA + " -> " + portB, msg));
				return null;
			}
			if (!cubicleB.equals(cableCubicleA) && !cubicleB.equals(cableCubicleB)) {
				String msg = "B端屏柜不同，芯线为[" + cubicleB + "]，光缆为[" + cableCubicleA + "，" + 
									cableCubicleB + "]。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", portA + " -> " + portB, msg));
				return null;
			}
			if ("WX".equals(entity.getConnType())) { // 同屏柜
				if (!cableCubicleA.equals(cableCubicleB)) {
					String msg = "芯线连接类型为WX，A、B端屏柜须相同，A为[" + cableCubicleA + "]，B为[" + cableCubicleB + "]。";
					SCTLogger.error(msg);
					pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", portA + " -> " + portB, msg));
					return null;
				}
			} else if ("WL".equals(entity.getConnType()) || "GL".equals(entity.getConnType())) { // 不同屏柜
				if (cableCubicleA.equals(cableCubicleB)) {
					String msg = "芯线连接类型为GL/WL，A、B端屏柜须不同，A、B同为[" + cableCubicleA + "]。";
					SCTLogger.error(msg);
					pmgr.append(new Problem(0, LEVEL.ERROR, "导入光缆", "导入芯线", portA + " -> " + portB, msg));
					return null;
				}
			}
			int num = (oldCableEntity.getF1051CoreNum() == null) ? 
					0 : oldCableEntity.getF1051CoreNum();
			oldCableEntity.setF1051CoreNum(num + 1);
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

}
