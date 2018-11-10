package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1071DauEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.service.BoardEntityService;
import com.synet.tool.rsc.service.EnumIedType;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.LLinkPhyRelationService;
import com.synet.tool.rsc.service.LogicallinkEntityService;
import com.synet.tool.rsc.service.PhyconnEntityService;
import com.synet.tool.rsc.service.PortEntityService;
import com.synet.tool.rsc.util.ProblemManager;

/**
 * 逻辑链路与物理链路关联处理器
 */
public class LogcalAndPhyconnProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProblemManager pmgr = ProblemManager.getInstance();
	private LogicallinkEntityService logicallinkEntityService = new LogicallinkEntityService();
	private LLinkPhyRelationService lLinkPhyRelationService = new LLinkPhyRelationService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
	private BoardEntityService boardEntityService = new BoardEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private IedEntityService iedEntityService = new IedEntityService();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();

	private Map<String, List<Tb1053PhysconnEntity>> phyconnCache = new HashMap<>();
	private Map<Tb1046IedEntity, List<BaseCbEntity>> cbCache = new HashMap<>();
	
	/**
	 * 递归查找
	 * @param sendIedName
	 * @param recvIed
	 * @param phyconnList
	 * @return
	 */
	private boolean findSendPhysConns(String sendIedName, Tb1046IedEntity recvIed, List<Tb1053PhysconnEntity> phyconnList, List<Tb1053PhysconnEntity> phyconnListVisited) {
		String key = sendIedName + "->" + recvIed.getF1046Name();
		List<Tb1053PhysconnEntity> temp = phyconnCache.get(key);
		if (temp != null) {
			phyconnList.addAll(temp);
			return true;
		}
		// 接收装置物理回路map（key：物理回路，value：发送装置）
		Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(recvIed);
		if (phyconnMap == null) 
			return false;
		for (Tb1053PhysconnEntity physconnEntity : phyconnMap.keySet()) {
			Tb1046IedEntity sendIed = phyconnMap.get(physconnEntity);
			if (sendIed.getF1046Name().equals(sendIedName)) { //与逻辑链路发送端装置一致
				phyconnList.add(physconnEntity);
				return true;
			//判断是否是交换机
			} else if (isSwcDevice(sendIed)) {				  //与逻辑链路发送端装置不一致
				if (!hasVisited(sendIed, phyconnListVisited)) {
					phyconnList.add(physconnEntity);
					phyconnListVisited.add(physconnEntity);
					if (findSendPhysConns(sendIedName, sendIed, phyconnList, phyconnListVisited)) {
						return true;
					} else {
						phyconnList.remove(physconnEntity);
					}
				}
			}
		}
		return false;
	}
	
	private Map<Tb1046IedEntity, Tb1053PhysconnEntity> findSwcDevice(Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap) {
		Map<Tb1046IedEntity, Tb1053PhysconnEntity> swcDevs = new HashMap<>();
		if (phyconnMap == null)
			return swcDevs;
		for (Tb1053PhysconnEntity physconnEntity : phyconnMap.keySet()) {
			Tb1046IedEntity sendIed = phyconnMap.get(physconnEntity);
			if (isSwcDevice(sendIed)) {				  //与逻辑链路发送端装置不一致
				swcDevs.put(sendIed, physconnEntity);
			}
		}
		return swcDevs;
	}
	
	private boolean hasVisited(Tb1046IedEntity sendIed, List<Tb1053PhysconnEntity> phyconnListVisited) {
		boolean inPath = false; // 避免死循环
		for (Tb1053PhysconnEntity phyconn : phyconnListVisited) {
			String phyConSendIed = phyconn.getTb1048PortByF1048CodeA().getTb1047BoardByF1047Code().getTb1046IedByF1046Code().getF1046Name();
			if (sendIed.getF1046Name().equals(phyConSendIed)) {
				inPath = true;
				break;
			}
		}
		return inPath;
	}
	
	public void analysis() {
		ConsoleManager console = ConsoleManager.getInstance();
		// 获取所有的逻辑链路
		List<Tb1065LogicallinkEntity> logicallinkEntities = logicallinkEntityService.getAll();
		// 查找逻辑链路物理回路
		for (Tb1065LogicallinkEntity logicallinkEntity : logicallinkEntities) {
			Tb1046IedEntity recvIed = logicallinkEntity.getTb1046IedByF1046CodeIedRecv();
			Tb1046IedEntity sendIed = logicallinkEntity.getTb1046IedByF1046CodeIedSend();
			String appid = sendIed.getF1046Name() + "->" + recvIed.getF1046Name() + " [" + logicallinkEntity.getBaseCbByCdCode().getCbId() + "] ";
			if (sendIed == null || recvIed == null) {
				String msg = "找不到逻辑链路" + appid + "的";
				if (sendIed == null) {
					msg += "发送装置";
				}
				if (recvIed == null) {
					if (sendIed == null) {
						msg += "和";
					}
					msg += "接收装置";
				}
				msg += "。";
				SCTLogger.error(msg);
				pmgr.append(new Problem(0, LEVEL.ERROR, "分析回路", "装置检查", "", msg));
				continue;
			}
			List<Tb1053PhysconnEntity> phyconnList = new ArrayList<>();
			List<Tb1053PhysconnEntity> phyconnListVisited = new ArrayList<>();
			String sendIedName = sendIed.getF1046Name();
			// 直连
			Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(recvIed);
			if (phyconnMap != null) {
				for (Tb1053PhysconnEntity physconnEntity : phyconnMap.keySet()) {
					Tb1046IedEntity sendIed1 = phyconnMap.get(physconnEntity);
					if (sendIed1.getF1046Name().equals(sendIedName)) { //与逻辑链路发送端装置一致
						phyconnList.add(physconnEntity);
						break;
					}
				}
			}
			Map<Tb1046IedEntity, Tb1053PhysconnEntity> swcDevices = findSwcDevice(phyconnMap);
			// 通过交换机连接
			for (Tb1046IedEntity swcDevice : swcDevices.keySet()) {
				Tb1053PhysconnEntity physconnEntity = swcDevices.get(swcDevice);
				phyconnList.add(physconnEntity);
				phyconnListVisited.add(physconnEntity);
				if (!findSendPhysConns(sendIedName, swcDevice, phyconnList, phyconnListVisited)) {
					phyconnList.remove(physconnEntity);
				}
			}
			if (phyconnList.size()>0) {//有与逻辑链路匹配的物理回路
				List<Tb1073LlinkphyrelationEntity> relations = new ArrayList<>();
				for (Tb1053PhysconnEntity physconnEntity : phyconnList) {
					Tb1073LlinkphyrelationEntity llinkphyrelationEntity = ParserUtil.createLLinkRelation(logicallinkEntity, physconnEntity);
					relations.add(llinkphyrelationEntity);
				}
				beanDao.insertBatch(relations);
				console.append("逻辑链路 " + appid + " 关联了" + phyconnList.size() + "条物理回路。");
			}
		}
		console.append("物理回路与逻辑链路联关系分析完毕！");
	}
	
	//判断是否是交换机
	private boolean isSwcDevice(Tb1046IedEntity ied) {
		boolean b = false;
		int iedType = ied.getF1046Type();
		for (int type : EnumIedType.SWC_DEVICE.getTypes()) {
			if (iedType == type) {
				b = true;
				break;
			}
		}
		return b;
	}
	
	/**
	 * 根据接收装置查找物理回路
	 * @param recvIed
	 * @return 
	 */
	private Map<Tb1053PhysconnEntity, Tb1046IedEntity> getPhysconnEntitiesByPortB(Tb1046IedEntity recvIed) {
		Map<Tb1053PhysconnEntity, Tb1046IedEntity> map = new HashMap<>();
		//获取逻辑链路接收装置的所有板卡
		List<Tb1047BoardEntity> boardEntities = boardEntityService.getByIed(recvIed);
		if (boardEntities != null && boardEntities.size() > 0) {
			//获取逻辑链路接收装置的所有端口
			List<Tb1048PortEntity> portEntities = portEntityService.getByBoardList(boardEntities);
			if (portEntities != null && portEntities.size() > 0) {
				for (Tb1048PortEntity portEntity : portEntities) {
					int direction = portEntity.getF1048Direction();
					//接收端口只能为接收或收发
					if (direction == DBConstants.DIRECTION_RX || direction == DBConstants.DIRECTION_RT) {
						//根据接收端口查找发送端口，一个端口只能连一根线（已确认）
						Tb1053PhysconnEntity physconnEntity = phyconnEntityService.getByTb1048PortByF1048CodeB(portEntity); 
						if (physconnEntity == null) 
							continue;
						Tb1048PortEntity sendPortEntity = physconnEntity.getTb1048PortByF1048CodeA();
						if (sendPortEntity == null) 
							continue;
						//根据发送端口找到发动装置
						if (sendPortEntity.getTb1047BoardByF1047Code() == null) 
							continue;
						Tb1046IedEntity sendIed = sendPortEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code();
						if (sendIed == null) 
							continue;
						map.put(physconnEntity, sendIed);
					}
				}
			}		
		}
		if (map.size() <= 0) 
			return null;
		return map;
	}
	
	/**
	 * 根据装置查找所有端口相关的物理回路
	 * @param recvIed
	 * @return 
	 */
	private List<Tb1053PhysconnEntity> getPhysconnEntitiesByPortAll(Tb1046IedEntity recvIed) {
		List<Tb1053PhysconnEntity> list = new ArrayList<>();
		//获取逻辑链路接收装置的所有板卡
		List<Tb1047BoardEntity> boardEntities = boardEntityService.getByIed(recvIed);
		if (boardEntities != null && boardEntities.size() > 0) {
			//获取逻辑链路接收装置的所有端口
			List<Tb1048PortEntity> portEntities = portEntityService.getByBoardList(boardEntities);
			if (portEntities != null && portEntities.size() > 0) {
				for (Tb1048PortEntity portEntity : portEntities) {
					//根据接收端口查找发送端口，一个端口只能连一根线
					Tb1053PhysconnEntity physconnEntity = phyconnEntityService.getByTb1048PortByF1048CodeB(portEntity); 
					if (physconnEntity == null) {
						//根据发送端口查找接收端口，一个端口只能连一根线
						physconnEntity = phyconnEntityService.getByTb1048PortByF1048CodeA(portEntity);
					}
					if (physconnEntity == null) continue;
					Tb1048PortEntity sendPortEntity = physconnEntity.getTb1048PortByF1048CodeA();
					if (sendPortEntity == null) continue;
					list.add(physconnEntity);
				}
			}
					
		}
		if (list.size() <= 0) return null;
		return list;
	}
	
	//分析确定TB1055_GCB和TB1056_SVCB表中F1071_CODE所代表的采集单元Code
	public void analysisGCBAndSVCB() {
		//采集器
		ConsoleManager console = ConsoleManager.getInstance();
		List<Tb1046IedEntity> odfDeviceList = iedEntityService.getIedEntityByTypes(EnumIedType.ODF_DEVICE.getTypes());
		if (odfDeviceList == null || odfDeviceList.size() <= 0) {
			console.append("当前工程没有采集器，GOOSE/SMV与采集器关联关系分析完毕！");
			return;//采集器为空，退出
		}
		List<BaseCbEntity> cbsAll = new ArrayList<>();
		for (Tb1046IedEntity odf : odfDeviceList) {
			Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(odf);
			if (phyconnMap == null) 
				continue;
			Tb1071DauEntity dauEntity = (Tb1071DauEntity) beanDao.getObject(Tb1071DauEntity.class, "tb1046IedByF1046Code", odf);
			if (dauEntity == null) {
				continue;
			}
			this.currDau = odf;
			List<BaseCbEntity> cbsIED = new ArrayList<>();
			List<Tb1053PhysconnEntity> phyconnListVisited = new ArrayList<>();
			for (Entry<Tb1053PhysconnEntity, Tb1046IedEntity> entry : phyconnMap.entrySet()) {
				Tb1053PhysconnEntity phyconn = entry.getKey();
				Tb1046IedEntity iedSwc = entry.getValue();
				if (isSwcDevice(iedSwc)) {//是交换机，则视为交换机汇集口
					dauPhyConns.clear();
					dauPhyConns.add(phyconn);
					//搜索端口B相关的物理回路
					findCBs(iedSwc, cbsIED, phyconnListVisited);
				}
			}
			for (BaseCbEntity cb : cbsIED) {
				if (!cbsAll.contains(cb)) {
					cb.setF1071Code(dauEntity.getF1071Code());
					cbsAll.add(cb);
				} else {
//					System.out.println("cb加过了");
				}
			}
		}
		beanDao.updateBatch(cbsAll);
		console.append("一共更新 " + cbsAll.size() + " 个GOOSE/SMV的采集器编号。");
		console.append("GOOSE/SMV与采集器关联关系分析完毕！");
	}
	
	private void findCBs(Tb1046IedEntity iedSwc, List<BaseCbEntity> cbsAll, List<Tb1053PhysconnEntity> phyconnListVisited) {
		Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMapSwc = getPhysconnEntitiesByPortB(iedSwc);
		if (phyconnMapSwc == null) 
			return;
		for (Tb1053PhysconnEntity physconnEntity : phyconnMapSwc.keySet()) {
			Tb1046IedEntity sendIed = phyconnMapSwc.get(physconnEntity);
			if (isSwcDevice(sendIed)) {
				if (!phyconnListVisited.contains(physconnEntity)) {
					dauPhyConns.add(physconnEntity);
					phyconnListVisited.add(physconnEntity);
					findCBs(sendIed, cbsAll, phyconnListVisited);
				} else {
//					System.out.println("找过了");
				}
			} else {
				if (!cbCache.containsKey(sendIed)) {
					List<BaseCbEntity> cbs = (List<BaseCbEntity>) beanDao.getListByCriteria(BaseCbEntity.class, "tb1046IedByF1046Code", sendIed);
					if (cbs != null && cbs.size()>0) {
						cbsAll.addAll(cbs);
						cbCache.put(sendIed, cbs);
						dauPhyConns.add(physconnEntity);
						// 创建逻辑链路
						List<Tb1065LogicallinkEntity> links = new ArrayList<>();
						List<Tb1073LlinkphyrelationEntity> relations = new ArrayList<>();
						for (BaseCbEntity cb : cbs) {
							Tb1065LogicallinkEntity logicLink = ParserUtil.createLogicLink(currDau, cb);
							links.add(logicLink);
							// 创建逻辑链路与物理回路间管理关系
							for (Tb1053PhysconnEntity physconn : dauPhyConns) {
								Tb1073LlinkphyrelationEntity llinkphyrelationEntity = ParserUtil.createLLinkRelation(logicLink, physconn);
								relations.add(llinkphyrelationEntity);
							}
						}
						beanDao.insertBatch(links);
						beanDao.insertBatch(relations);
						// 清理交换机之后的回路，为下一个装置做准备
						Tb1053PhysconnEntity firstPhysconn = dauPhyConns.get(0);
						dauPhyConns.clear();
						dauPhyConns.add(firstPhysconn);
					}
				}
			}
		}
	}
	
	private List<Tb1053PhysconnEntity> dauPhyConns = new ArrayList<>();
	private Tb1046IedEntity currDau;
}