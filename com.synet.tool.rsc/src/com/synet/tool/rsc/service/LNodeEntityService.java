package com.synet.tool.rsc.service;

import java.util.List;
import java.util.Map;

import com.shrcn.found.common.util.StringUtil;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class LNodeEntityService extends BaseService {
	
	private IedEntityService iedServ = new IedEntityService();
	private StatedataService statedataService = new StatedataService();

	/**
	 * 更新IED所属间隔
	 * @param iedName
	 * @param bay
	 */
	public void updateIEDBayInfo(String iedName, Tb1042BayEntity bay) {
		if (!StringUtil.isEmpty(iedName) && !"None".equalsIgnoreCase(iedName) && !"null".equalsIgnoreCase(iedName)) {
			Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedName);
			if (ied == null) {
				return;
			}
			String bayName = ied.getTb1042BaysByF1042Code().getF1042Name();
			if (DBConstants.BAY_OTHER.equals(bayName)) {
				String bayCode = bay.getF1042Code();
				ied.setF1042Code(bayCode);
				String f1046Code = ied.getF1046Code();
				iedServ.updateIEDBayCode(f1046Code, bayCode);
//				List<Tb1046IedEntity> bayIeds = new ArrayList<>();
//				List<Tb1065LogicallinkEntity> logiclinkIns = (List<Tb1065LogicallinkEntity>) beanDao.getListByCriteria(
//						Tb1065LogicallinkEntity.class, "f1046CodeIedRecv", f1046Code);
//				for (Tb1065LogicallinkEntity logiclink : logiclinkIns) {
//					Tb1046IedEntity iedSend = logiclink.getTb1046IedByF1046CodeIedSend();
//					iedSend.setF1042Code(bayCode);
//					bayIeds.add(iedSend);
//				}
//				List<Tb1065LogicallinkEntity> logiclinkOuts = (List<Tb1065LogicallinkEntity>) beanDao.getListByCriteria(
//						Tb1065LogicallinkEntity.class, "f1046CodeIedSend", f1046Code);
//				for (Tb1065LogicallinkEntity logiclink : logiclinkOuts) {
//					Tb1046IedEntity iedResv = logiclink.getTb1046IedByF1046CodeIedRecv();
//					iedResv.setF1042Code(bayCode);
//					bayIeds.add(iedResv);
//				}
//				beanDao.updateBatch(bayIeds);
			}
		}
	}
	
	/**
	 * 关联一次设备状态点及其数据类型
	 * @param eqpCode
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 * @param isGround
	 */
	public void linkStaPoint(String eqpCode, String iedName, String ldInst, String prefix, String lnClass, String lnInst, boolean isGround) {
		// 更新开关道闸信号数据类型
		if ("XSWI".equals(lnClass)) { 
			Rule rule = isGround ? F1011_NO.ST_GDIS : F1011_NO.ST_DIS;
			String dataRef = ldInst + "/" + prefix + lnClass + lnInst + "$ST$Pos$stVal";
			Tb1016StatedataEntity stateData = statedataService.getStateByIedRef(iedName, dataRef);
			if (stateData != null) {
				updatePosType(iedName, dataRef, rule);
				stateData.setF1011No(rule.getId());
				stateData.setParentCode(eqpCode);
				beanDao.update(stateData);
			}
		} else if ("XCBR".equals(lnClass)) {
			String dataRef = ldInst + "/" + prefix + lnClass + lnInst + "$ST$Pos$stVal";
			Tb1016StatedataEntity stateData = statedataService.getStateByIedRef(iedName, dataRef);
			if (stateData != null) {
				Rule rule = F1011_NO.ST_BRK;
				String desc = stateData.getF1016Desc();
				if (!StringUtil.isEmpty(desc)) {
					if (desc.contains("A")) {
						rule = F1011_NO.ST_BRK_A;
					} else if (desc.contains("B")) {
						rule = F1011_NO.ST_BRK_B;
					} else if (desc.contains("C")) {
						rule = F1011_NO.ST_BRK_C;
					}
				}
				updatePosType(iedName, dataRef, rule);
				stateData.setF1011No(rule.getId());
				stateData.setParentCode(eqpCode);
				beanDao.update(stateData);
			}
		}
	}

	/**
	 * 解除关联一次设备状态点及其数据类型
	 * @param iedName
	 * @param ldInst
	 * @param prefix
	 * @param lnClass
	 * @param lnInst
	 */
	public void disLinkStaPoint(String iedName, String ldInst, String prefix, String lnClass, String lnInst) {
		Tb1046IedEntity ied = iedServ.getIedEntityByDevName(iedName);
		// 更新开关道闸信号数据类型
		Rule rule = F1011_NO.OTHERS;
		if ("XSWI".equals(lnClass)) { 
			String dataRef = ldInst + "/" + prefix + lnClass + lnInst + "$ST$Pos$stVal";
			Tb1016StatedataEntity stateData = statedataService.getStateByIedRef(iedName, dataRef);
			if (stateData != null) {
				updatePosType(iedName, dataRef, rule);
				stateData.setF1011No(rule.getId());
				stateData.setParentCode(ied.getF1046Code());
				beanDao.update(stateData);
			}
		} else if ("XCBR".equals(lnClass)) {
			String dataRef = ldInst + "/" + prefix + lnClass + lnInst + "$ST$Pos$stVal";
			Tb1016StatedataEntity stateData = statedataService.getStateByIedRef(iedName, dataRef);
			if (stateData != null) {
				updatePosType(iedName, dataRef, rule);
				stateData.setF1011No(rule.getId());
				stateData.setParentCode(ied.getF1046Code());
				beanDao.update(stateData);
			}
		}
//		// 改变IED所属间隔
//		String bayName = null;
//		if (DBConstants.IED_MONI == ied.getF1046Type()) {
//			bayName = DBConstants.BAY_MOT;
//		} else {
//			bayName = DBConstants.BAY_PUB;
//		}
//		Tb1042BayEntity bay = new BayEntityService().getBayEntityByName(bayName);
//		ied.setF1042Code(bay.getF1042Code());
//		beanDao.update(ied);
	}
	
	private void updatePosType(String iedName, String dataRef, Rule rule) {
		statedataService.updateStateF1011No(iedName, dataRef, rule.getId());
	}
	
	private boolean isGround(Tb1043EquipmentEntity equipment) {
		List<?> terminals = beanDao.getListByCriteria(Tb1044TerminalEntity.class, "tb1043EquipmentByF1043Code", equipment);
		boolean isGround = false;
		for (Object o : terminals) {
			Tb1044TerminalEntity terminal = (Tb1044TerminalEntity) o;
			String cNodeName = terminal.getF1044Desc();
			if (cNodeName!=null && cNodeName.endsWith("grounded")) {
				isGround = true;
				break;
			}
		}
		return isGround;
	}
	
	public void addLNode(Tb1043EquipmentEntity equipment, Map<String, String> disInfo) {
		String lnClass = disInfo.get("lnClass");
		String ldInst = disInfo.get("ldInst");
		String prefix = disInfo.get("prefix");
		String lnInst = disInfo.get("lnInst");
		String iedName = disInfo.get("iedName");
		Tb1042BayEntity bay = equipment.getTb1042BayByF1042Code();
		updateIEDBayInfo(iedName, bay);
		linkStaPoint(equipment.getF1043Code(), iedName, ldInst, prefix, lnClass, lnInst, isGround(equipment));
	}
	
	public void deleteLNode(Map<String, String> disInfo) {
		String lnClass = disInfo.get("lnClass");
		String ldInst = disInfo.get("ldInst");
		String prefix = disInfo.get("prefix");
		String lnInst = disInfo.get("lnInst");
		String iedName = disInfo.get("iedName");
		disLinkStaPoint(iedName, ldInst, prefix, lnClass, lnInst);
	}
}
