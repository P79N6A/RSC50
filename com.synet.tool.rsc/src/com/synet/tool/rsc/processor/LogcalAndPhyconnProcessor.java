package com.synet.tool.rsc.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * 逻辑链路与物理链路关联处理器
 */
public class LogcalAndPhyconnProcessor {
	
	private RSCProperties rscp = RSCProperties.getInstance();
	private LogicallinkEntityService logicallinkEntityService = new LogicallinkEntityService();
	private LLinkPhyRelationService lLinkPhyRelationService = new LLinkPhyRelationService();
	private PhyconnEntityService phyconnEntityService = new PhyconnEntityService();
	private BoardEntityService boardEntityService = new BoardEntityService();
	private PortEntityService portEntityService = new PortEntityService();
	private IedEntityService iedEntityService = new IedEntityService();

	public void analysis() {
		//获取所有的逻辑链路
		List<Tb1065LogicallinkEntity> logicallinkEntities = logicallinkEntityService.getAll();
		
		for (Tb1065LogicallinkEntity logicallinkEntity : logicallinkEntities) {
			if (logicallinkEntity.getTb1046IedByF1046CodeIedSend() == null 
					|| logicallinkEntity.getTb1046IedByF1046CodeIedRecv() == null) {
				continue;
			}
			Tb1046IedEntity recvIed = logicallinkEntity.getTb1046IedByF1046CodeIedRecv();
			Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(recvIed);
			if (phyconnMap == null) continue;
			List<Tb1053PhysconnEntity> phyconnList = new ArrayList<>();
			for (Tb1053PhysconnEntity physconnEntity : phyconnMap.keySet()) {
				Tb1046IedEntity sendIed = phyconnMap.get(physconnEntity);
				if (sendIed.getF1046Name().equals(logicallinkEntity.getTb1046IedByF1046CodeIedSend().getF1046Name())) {
					//与逻辑链路发送端装置一致
					phyconnList.add(physconnEntity);
				} else {
					//与逻辑链路发送端装置不一致
					//判断是否是交换机
					if (sendIed.getF1046Desc() == null) continue;
					if (isSwcDevice(sendIed)) {
						//将交换作为接收装置查找物理链路
						Map<Tb1053PhysconnEntity, Tb1046IedEntity> tempPhyconnMap = getPhysconnEntitiesByPortB(sendIed);
						for (Tb1053PhysconnEntity tempPhysconnEntity : tempPhyconnMap.keySet()) {
							Tb1046IedEntity tempSendIed = tempPhyconnMap.get(tempPhysconnEntity);
							if (tempSendIed.getF1046Name().equals(logicallinkEntity.getTb1046IedByF1046CodeIedSend().getF1046Name())) {
								//与逻辑链路发送端装置一致
								phyconnList.add(physconnEntity);
							}
						}
					} 
				}
			}
			if (phyconnList.size() > 0) {//有与逻辑链路匹配的物理回路
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
			}
		}
		
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
						//根据接收端口查找发送端口，一个端口只能连一根线（带确认）
						Tb1053PhysconnEntity physconnEntity = phyconnEntityService.getByTb1048PortByF1048CodeB(portEntity); 
						if (physconnEntity == null) continue;
						Tb1048PortEntity sendPortEntity = physconnEntity.getTb1048PortByF1048CodeA();
						if (sendPortEntity == null) continue;
						//根据发送端口找到发动装置
						if (sendPortEntity.getTb1047BoardByF1047Code() == null) continue;
						Tb1046IedEntity sendIed = sendPortEntity.getTb1047BoardByF1047Code().getTb1046IedByF1046Code();
						if (sendIed == null) continue;
						map.put(physconnEntity, sendIed);
					}
				}
			}		
		}
		if (map.size() <= 0) return null;
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
		List<Tb1046IedEntity> odfDeviceList = iedEntityService.getIedEntityByTypes(EnumIedType.ODF_DEVICE.getTypes());
		if (odfDeviceList == null || odfDeviceList.size() <= 0) return;//采集器为空，退出
		for (Tb1046IedEntity odf : odfDeviceList) {
			Map<Tb1053PhysconnEntity, Tb1046IedEntity> phyconnMap = getPhysconnEntitiesByPortB(odf);
			if (phyconnMap == null) continue;
			for (Tb1046IedEntity ied : phyconnMap.values()) {
				if (isSwcDevice(ied)) {//是交换机，则视为交换机汇集口
					//搜索所有端口相关的物理回路
					List<Tb1053PhysconnEntity> list = getPhysconnEntitiesByPortAll(ied);
					if (list == null) continue;
					for (Tb1053PhysconnEntity physconnEntity : list) {
						//根据物理回路查找与之关联的逻辑链路
						List<Tb1073LlinkphyrelationEntity> llEntities = lLinkPhyRelationService.getByTb1053PhysconnByF1053Code(physconnEntity);
						if (llEntities == null || llEntities.size() <= 0) continue;
						for (Tb1073LlinkphyrelationEntity llEntity : llEntities) {
							Tb1065LogicallinkEntity logicallinkEntity = llEntity.getTb1065LogicallinkByF1065Code();
							//把逻辑链路对应的GCB、SVCB的F1071_CODE为当前采集器的Code
							BaseCbEntity baseCbByCdCode = logicallinkEntity.getBaseCbByCdCode();
							if (baseCbByCdCode != null) {
								baseCbByCdCode.setF1071Code(odf.getF1046Code());
							}
						}
					}
				}
			}
		}
	}
}