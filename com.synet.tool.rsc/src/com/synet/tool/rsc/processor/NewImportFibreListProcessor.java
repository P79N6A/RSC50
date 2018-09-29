package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.ExcelConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.IM100FileInfoEntity;
import com.synet.tool.rsc.model.IM102FibreListEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.CableEntityService;
import com.synet.tool.rsc.service.CoreEntityService;
import com.synet.tool.rsc.service.CubicleEntityService;
import com.synet.tool.rsc.service.ImprotInfoService;
import com.synet.tool.rsc.service.LLinkPhyRelationService;
import com.synet.tool.rsc.service.LogicallinkEntityService;
import com.synet.tool.rsc.service.PhyconnEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.service.SubstationService;

public class NewImportFibreListProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ImprotInfoService improtInfoService = new ImprotInfoService();
	private SubstationService substationService = new SubstationService();
	private CubicleEntityService cubicleService = new CubicleEntityService();
	private BoardEntityService boardEntityService = new BoardEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private CableEntityService cableEntityService = new CableEntityService();
	private CoreEntityService coreEntityService = new CoreEntityService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
	private List<IM102FibreListEntity> fibreListEntitieList = new ArrayList<>();
	private LogicallinkEntityService logicallinkEntityService = new LogicallinkEntityService();
	private LLinkPhyRelationService lLinkPhyRelationService = new LLinkPhyRelationService();
	//存放解析出来的光缆（去重）
	private List<Tb1051CableEntity> cableEntitieList = new ArrayList<>();
	private List<Tb1052CoreEntity> coreEntitieList = new ArrayList<>();
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
		//处理光缆、芯线、物理回路重复数据
//		doRepeatingData();
		//处理逻辑链路与物理回路之间的关联关系(现只处理新增的芯线)
		analysisLogicalAndPhyonn();
		//保存光缆、芯线、物理回路
		cableEntityService.saveBatch(cableEntitieList);
		coreEntityService.saveBatch(coreEntitieList);
		phyconnEntityService.saveBatch(physconnEntitieList);
//		for (String cubicleDesc : map.keySet()) {
//			Tb1050CubicleEntity cubicleEntity = cubicleService.getCubicleEntityByDesc(cubicleDesc);
//			try {
//				if (substationList != null && substationList.size() > 0 &&
//						cubicleEntity != null) {
//					if (fibreListEntitieList != null && fibreListEntitieList.size() > 0) {
//						analysisCable(substationList.get(0),cubicleEntity, fibreListEntitieList);
//						
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return false;
//			}
//			cableEntityService.saveBatch(cableEntitieList);
//			coreEntityService.saveBatch(coreEntitieList);
//			phyconnEntityService.saveBatch(physconnEntitieList);
//			
//		}	
	}
	
	//重复数据做更新操作
	private void doRepeatingData() {
		for (Tb1051CableEntity cableEntity : cableEntitieList) {
			Tb1051CableEntity temp = cableEntityService.existEntity(cableEntity);
			if (temp != null) {
				cableEntity.setF1051Code(temp.getF1051Code());
			}
		}
		for (Tb1052CoreEntity coreEntity : coreEntitieList) {
			Tb1052CoreEntity temp = coreEntityService.existEntity(coreEntity);
			if (temp != null) {
				coreEntity.setF1052Code(temp.getF1052Code());
			}
		}
		for (Tb1053PhysconnEntity physconnEntity : physconnEntitieList) {
			Tb1053PhysconnEntity temp = phyconnEntityService.existEntity(physconnEntity);
			if (temp != null) {
				physconnEntity.setF1053Code(temp.getF1053Code());
			}
		}
	}
	
	//处理光芯收发端口顺序
	private void analysisPortSort() {
		if (fibreListEntitieList != null) {
			for (IM102FibreListEntity entity : fibreListEntitieList) {
				String portCodeA = entity.getPortCodeA();
				String portCodeB = entity.getPortCodeB();
				if (portCodeA.contains(ExcelConstants.PORT_TYPE_RX) //收发端口颠倒的，交换端口顺序
						&& portCodeB.contains(ExcelConstants.PORT_TYPE_TX)) {
					String boardCodeA = entity.getBoardCodeA();
					String devDescA = entity.getDevDescA();
					String cubicleDescA = entity.getCubicleDescA();
					
					String boardCodeB = entity.getBoardCodeB();
					String devDescB = entity.getDevDescB();
					String cubicleDescB = entity.getCubicleDescB();
					
					entity.setPortCodeA(portCodeB);
					entity.setBoardCodeA(boardCodeB);
					entity.setDevDescA(devDescB);
					entity.setCubicleDescA(cubicleDescB);
					
					entity.setPortCodeB(portCodeA);
					entity.setBoardCodeB(boardCodeA);
					entity.setDevDescB(devDescA);
					entity.setCubicleDescB(cubicleDescA);
				}
			}
		}
	}

	
	//处理物理回路
	private void analysisPhyconn(Tb1041SubstationEntity substationEntity) {
		for (Tb1052CoreEntity coreEntity : coreEntitieList) {
			//避免重复处理更新数据
//			if (coreEntity.getF1052Code() != null)
//				continue;
			Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
			physconnEntity.setF1041Code(substationEntity.getF1041Code());
			String portCodeA = coreEntity.getF1048CodeA();
			String portCodeB = coreEntity.getF1048CodeB();
			if (portCodeA != null) {
				Tb1048PortEntity portEntityA = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeA);
				physconnEntity.setTb1048PortByF1048CodeA(portEntityA);
			}
			if (portCodeB != null) {
				Tb1048PortEntity portEntityB = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeB);
				physconnEntity.setTb1048PortByF1048CodeB(portEntityB);
			}
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
			Tb1050CubicleEntity cubicleEntityA = cubicleService.getCubicleEntityByDesc(entity.getCubicleDescA());
			Tb1050CubicleEntity cubicleEntityB = cubicleService.getCubicleEntityByDesc(entity.getCubicleDescB());
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
//			if (oldCableEntity == null) {
//				cableEntity.setTb1041SubstationByF1041Code(substationEntity);
//				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
//				cableEntity.setTb1050CubicleByF1050CodeA(cubicleEntity);
//				cableEntity.setF1051Code(rscp.nextTbCode(DBConstants.PR_CABLE));
//				cableEntitieList.add(cableEntity);
//				analysisCore(cubicleEntity, cableEntity, list);
//			} else {
//				//TODO 现没有区分A端和B端，现赋值A端，在赋值B端
//				oldCableEntity.setTb1050CubicleByF1050CodeB(cubicleEntity);
//				analysisCore(cubicleEntity, oldCableEntity, list);
//			}
		}
		//处理F1051Code，数据库也存在，则使用原来的code（更新）
	}
	
	//处理光芯数据
	private void analysisCore() {//Tb1050CubicleEntity cubicleEntity,Tb1051CableEntity cableEntity, List<IM102FibreListEntity> list
		for (IM102FibreListEntity entity : fibreListEntitieList) {
			if (entity.getCableCode() == null || "".equals(entity.getCableCode())) continue;
			Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
			coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
			if (entity.getCoreCode() == null) continue;//纤芯编号不存在，不处理
			int coreNo = Integer.valueOf(entity.getCoreCode());
			coreEntity.setF1052No(coreNo);
			Tb1048PortEntity portEntityA = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
			Tb1048PortEntity portEntityB = portEntityService.getPortEntity(entity.getDevNameB(), entity.getBoardCodeB(), entity.getPortCodeB());
			if (portEntityA == null || portEntityB == null) continue; //端口都不能为空
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
			coreEntity.setTb1048PortByF1048CodeA(portEntityA);
			coreEntity.setTb1048PortByF1048CodeB(portEntityB);
			Tb1051CableEntity cableEntity = new Tb1051CableEntity();
			cableEntity.setF1051Name(entity.getCableCode());
			cableEntity.setF1051Type(DBConstants.CABLE_TYPE_WL);//现默认为“尾缆”
			Tb1051CableEntity oldCableEntity = checkCableEntity(cableEntity);
			if (oldCableEntity == null) continue;
			coreEntity.setParentCode(oldCableEntity.getF1051Code());
//			coreEntity.setTb1051CableByParentCode(oldCableEntity);
			Tb1052CoreEntity oldCoreEntity = coreEntityService.existEntity(coreEntity);
			//去重，存在则更新
			if (oldCoreEntity != null) {
				coreEntity.setF1052Code(oldCoreEntity.getF1052Code());
			} else {
				coreEntity.setF1052Code(rscp.nextTbCode(DBConstants.PR_CORE));
			}
			coreEntitieList.add(coreEntity);
			
//			if (entity.getCableCode().equals(cableEntity.getF1051Name())) {
//				Tb1052CoreEntity coreEntity = new Tb1052CoreEntity();
//				coreEntity.setF1052Type(DBConstants.CORE_TYPE_XX);
//				if (entity.getCoreCode() == null) continue;//纤芯编号不存在，不处理
//				int coreNo = Integer.valueOf(entity.getCoreCode());
//				coreEntity.setF1052No(coreNo);
//				Tb1048PortEntity portEntity = portEntityService.getPortEntity(entity.getDevNameA(), entity.getBoardCodeA(), entity.getPortCodeA());
//				if (portEntity != null) {
//					//处理端口类型
//					String portCode = entity.getBoardCodeA();
//					if (portCode != null) {
//						if (portCode.contains(ExcelConstants.PORT_TYPE_TX)) {
//							portEntity.setF1048Direction(DBConstants.DIRECTION_TX);
//						} else if (portCode.contains(ExcelConstants.PORT_TYPE_RX)) {
//							portEntity.setF1048Direction(DBConstants.DIRECTION_RX);
//						} else if (portCode.contains(ExcelConstants.PORT_TYPE_RT)) {
//							portEntity.setF1048Direction(DBConstants.DIRECTION_RT);
//						}
//					}
//					Tb1052CoreEntity oldCoreEntity = checkCoreEntity(coreEntity);
//					if (oldCoreEntity == null) {
//						if (portEntity != null) {
//							switch (portEntity.getF1048Direction()) {
//							case 1:
//							case 3:
//								coreEntity.setF1048CodeA(portEntity.getF1048Code());
//								break;
//							case 2:
//								coreEntity.setF1048CodeB(portEntity.getF1048Code());
//								break;
//							default:
//								break;
//							}
//							coreEntity.setF1052Code(rscp.nextTbCode(DBConstants.PR_CORE));
//							coreEntitieList.add(coreEntity);
//						}
//					} else {
//						if (portEntity != null) {
//							switch (portEntity.getF1048Direction()) {
//							case 1:
//							case 2:
//								oldCoreEntity.setF1048CodeB(portEntity.getF1048Code());
//								break;
//							case 3:
//								oldCoreEntity.setF1048CodeA(portEntity.getF1048Code());
//								break;
//							default:
//								break;
//							}
//						}
//					}
//				}
//			}
		}
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
	
	//处理逻辑链路与物理回路关联
	private void analysisLogicalAndPhyonn() {
		List<Tb1065LogicallinkEntity> logicallinkEntities = logicallinkEntityService.getAll();
		if (logicallinkEntities == null || logicallinkEntities.size() <= 0) return;
		for (Tb1065LogicallinkEntity logicallinkEntity : logicallinkEntities) {
			if (logicallinkEntity.getTb1046IedByF1046CodeIedSend() == null 
					|| logicallinkEntity.getTb1046IedByF1046CodeIedRecv() == null) {
				continue;
			}
			//key:接收端，value：发送端
			Map<String, String> resultMap = null;
			Tb1046IedEntity resvIed = logicallinkEntity.getTb1046IedByF1046CodeIedRecv();
			//获取逻辑链路接收装置的所有板卡
			List<Tb1047BoardEntity> boardEntities = boardEntityService.getByIed(resvIed);
			if (boardEntities != null && boardEntities.size() > 0) {
				//获取逻辑链路接收装置的所有端口
				List<Tb1048PortEntity> portEntities = portEntityService.getByBoardList(boardEntities);
				if (portEntities != null && portEntities.size() > 0) {
					//根据接收端口查找所有芯线
					Map<String, List<Tb1052CoreEntity>> result = getCoreEntityByPort(portEntities);
					if (result == null) continue;
					//检查芯线的发送端是否为逻辑链路的发送端(b)
					String logicalSendIedCode = logicallinkEntity.getTb1046IedByF1046CodeIedSend().getF1046Code();
					resultMap = checkSendPort(result, logicalSendIedCode);
					//找到物理回路
					if (resultMap != null && resultMap.size() > 0) {
						endHandler(logicallinkEntity, resultMap);
					} else {//未找到物理回路,找到的芯线的F1048_CODE_A作为芯线TB1052_Core的F1048_CODE_B再次找到所有的芯线，最多两次
						for (int i = 0; i < 2; i++) {
							result = getCoreEntityByCoere(result);
							if (result == null) break;//数据中断，跳出
							resultMap = checkSendPort(result, logicalSendIedCode);
							if (resultMap != null && resultMap.size() > 0) { //找到物理回路，跳出
								endHandler(logicallinkEntity, resultMap);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	//结束逻辑链路与物理链路关联处理
	private void endHandler(Tb1065LogicallinkEntity logicallinkEntity, Map<String, String> resultMap) {
		if (resultMap == null || resultMap.size() <= 0) return;
		for (String portCodeB : resultMap.keySet()) {
			String portCodeA = resultMap.get(portCodeB);
			Tb1048PortEntity sendPort = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeA);
			Tb1048PortEntity recvPort = (Tb1048PortEntity) portEntityService.getById(Tb1048PortEntity.class, portCodeA);
			//生成物理回路
			Tb1053PhysconnEntity physconnEntity = new Tb1053PhysconnEntity();
			physconnEntity.setTb1048PortByF1048CodeA(sendPort);
			physconnEntity.setTb1048PortByF1048CodeB(recvPort);
			Tb1053PhysconnEntity oldPhysconnEntity = phyconnEntityService.existEntity(physconnEntity);
			if (oldPhysconnEntity != null) {
				physconnEntity.setF1053Code(oldPhysconnEntity.getF1053Code());
			} else {
				physconnEntity.setF1053Code(rscp.nextTbCode(DBConstants.PR_PHYSCONN));
			}
			phyconnEntityService.save(physconnEntity);
			//生成逻辑链路与物理回路的关联关系
			Tb1073LlinkphyrelationEntity llinkphyrelationEntity = new Tb1073LlinkphyrelationEntity();
			llinkphyrelationEntity.setF1073Code(rscp.nextTbCode(DBConstants.PR_LPRELATION));
			llinkphyrelationEntity.setTb1065LogicallinkByF1065Code(logicallinkEntity);
			llinkphyrelationEntity.setTb1053PhysconnByF1053Code(physconnEntity);
			lLinkPhyRelationService.save(llinkphyrelationEntity);
		}
		
	}
	
	private Map<String, String> checkSendPort(Map<String, List<Tb1052CoreEntity>> map , String logicalSendIedCode) {
		Map<String, String> temp = new HashMap<>();
		for (String key : map.keySet()) { 
			List<Tb1052CoreEntity> coreEntities = map.get(key);
			for (Tb1052CoreEntity coreEntity : coreEntities) {
				if (coreEntity.getF1048CodeA() == null)
					continue;
				Tb1048PortEntity tempPort = (Tb1048PortEntity) portEntityService
						.getById(Tb1048PortEntity.class, coreEntity.getF1048CodeA());
				Tb1047BoardEntity tempBoard = tempPort.getTb1047BoardByF1047Code();
				if (tempBoard == null) 
					continue;
				Tb1046IedEntity tempIed = tempBoard.getTb1046IedByF1046Code();
				if (tempIed == null) 
					continue;
				if (logicalSendIedCode.equals(tempIed.getF1046Code())) {
					temp.put(key, coreEntity.getF1048CodeA());
				}
			}
		}
		return temp;
	}
	
	//根据芯线的A端口作为新的芯线芯线的B端口查找芯线（c）
	private  Map<String, List<Tb1052CoreEntity>> getCoreEntityByCoere(Map<String, List<Tb1052CoreEntity>> map) {
		Map<String, List<Tb1052CoreEntity>> result = new HashMap<>();
		for (String key : map.keySet()) { 
			List<Tb1052CoreEntity> coList = map.get(key);
			for (Tb1052CoreEntity coreEntity : coList) {
				if (coreEntity.getF1048CodeA() == null) continue;
				List<Tb1052CoreEntity> temp = matchingCoreByRx(coreEntity.getF1048CodeA());
				if (temp != null) {
					result.put(key, temp);
				}
			}
		}
		return result;
	}
	
	/**
	 * 根据端口查找芯线（a）
	 * @param portEntities
	 * @return key 发送端口
	 */
	private Map<String, List<Tb1052CoreEntity>> getCoreEntityByPort(List<Tb1048PortEntity> portEntities) {
		Map<String, List<Tb1052CoreEntity>> result = new HashMap<>();
		for (Tb1048PortEntity portEntity : portEntities) {
			int direction = portEntity.getF1048Direction();
			if (direction == DBConstants.DIRECTION_RX) {
				List<Tb1052CoreEntity> temp = matchingCoreByRx(portEntity);
				if (temp != null) {
					result.put(portEntity.getF1048Code(), temp);
				}
			}
		}
		return result;
	}
	
	private List<Tb1052CoreEntity> matchingCoreByRx(String portCodeB) {
		if (portCodeB == null) return null;
		List<Tb1052CoreEntity> list = new ArrayList<>();
		for (Tb1052CoreEntity coreEntity : coreEntitieList) {
			if (coreEntity.getF1048CodeB() != null 
					&& coreEntity.getF1048CodeB().equals(portCodeB)) {
				list.add(coreEntity);
			}
		}
		return list;
	}
	
	private List<Tb1052CoreEntity> matchingCoreByRx(Tb1048PortEntity portEntity) {
		if (portEntity == null) return null;
		List<Tb1052CoreEntity> list = new ArrayList<>();
		for (Tb1052CoreEntity coreEntity : coreEntitieList) {
			if (coreEntity.getF1048CodeB() != null 
					&& coreEntity.getF1048CodeB().equals(portEntity.getF1048Code())) {
				list.add(coreEntity);
			}
		}
		return list;
	}

}
