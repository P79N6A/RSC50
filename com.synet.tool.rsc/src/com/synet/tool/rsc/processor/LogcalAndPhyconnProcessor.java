package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.ui.view.LEVEL;
import com.shrcn.found.ui.view.Problem;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
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

	/**
	 * 递归查找
	 * @param sendIedName
	 * @param recvIed
	 * @param phyconnList
	 * @return
	 */
	private boolean findSendPhysConns(String sendIedName, Tb1046IedEntity recvIed, List<Tb1053PhysconnEntity> phyconnList) {
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
				phyconnList.add(physconnEntity);
				return findSendPhysConns(sendIedName, recvIed, phyconnList);
			}
		}
		return false;
	}
	
	public void analysis() {
		ConsoleManager console = ConsoleManager.getInstance();
		// 获取所有的逻辑链路
		List<Tb1065LogicallinkEntity> logicallinkEntities = logicallinkEntityService.getAll();
		// 查找逻辑链路物理回路
		for (Tb1065LogicallinkEntity logicallinkEntity : logicallinkEntities) {
			Tb1046IedEntity recvIed = logicallinkEntity.getTb1046IedByF1046CodeIedRecv();
			Tb1046IedEntity sendIed = logicallinkEntity.getTb1046IedByF1046CodeIedSend();
			String appid = recvIed.getF1046Name() + " [" + logicallinkEntity.getBaseCbByCdCode().getCbId() + "] ";
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
			String sendIedName = sendIed.getF1046Name();
			boolean success = findSendPhysConns(sendIedName, recvIed, phyconnList);
			if (success) {//有与逻辑链路匹配的物理回路
				for (Tb1053PhysconnEntity physconnEntity : phyconnList) {
					Tb1073LlinkphyrelationEntity llinkphyrelationEntity = new Tb1073LlinkphyrelationEntity();
					llinkphyrelationEntity.setTb1065LogicallinkByF1065Code(logicallinkEntity);
					llinkphyrelationEntity.setTb1053PhysconnByF1053Code(physconnEntity);
					//关联关系已存在
					if (lLinkPhyRelationService.existEntity(llinkphyrelationEntity) != null) {
						continue;
					}
					llinkphyrelationEntity.setF1073Code(rscp.nextTbCode(DBConstants.PR_LPRELATION));
					lLinkPhyRelationService.save(llinkphyrelationEntity);
				}
				console.append("逻辑链路" + appid + "与" + phyconnList.size() + "条物理回路有关。");
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
		BeanDaoImpl beanDao = BeanDaoImpl.getInstance();
		int cbNum = 0;
		for (Tb1046IedEntity odf : odfDeviceList) {
			Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(odf);
			if (phyconnMap == null) 
				continue;
			for (Tb1046IedEntity ied : phyconnMap.values()) {
				if (isSwcDevice(ied)) {//是交换机，则视为交换机汇集口
					//搜索端口B相关的物理回路
					Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMapSwc = getPhysconnEntitiesByPortB(ied);
					if (phyconnMapSwc == null) 
						continue;
					for (Tb1053PhysconnEntity physconnEntity : phyconnMapSwc.keySet()) {
						Tb1046IedEntity sendIed = phyconnMapSwc.get(physconnEntity);
						List<BaseCbEntity> cbs = (List<BaseCbEntity>) beanDao.getListByCriteria(BaseCbEntity.class, "tb1046IedByF1046Code", sendIed);
						if (cbs != null && cbs.size()>0) {
							for (BaseCbEntity cb : cbs) {
								cb.setF1071Code(odf.getF1046Code());
							}
							beanDao.updateBatch(cbs);
							cbNum += cbs.size();
						}
					}
				}
			}
		}
		console.append("一共更新 " + cbNum + " 个GOOSE/SMV的采集器编号。");
		console.append("GOOSE/SMV与采集器关联关系分析完毕！");
	}
}