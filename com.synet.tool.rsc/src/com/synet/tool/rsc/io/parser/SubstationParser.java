/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.EnumEquipmentType;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.service.CtvtsecondaryService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.SubstationService;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class SubstationParser extends IedParserBase<Tb1042BayEntity> {

	private Map<String, Tb1045ConnectivitynodeEntity> connMap = new HashMap<>();
	private CtvtsecondaryService secService = new CtvtsecondaryService();
	private SubstationService staServ = new SubstationService();
	private IedEntityService iedServ = new IedEntityService();
	
	public SubstationParser() {
		super(null);
	}

	@Override
	public void parse() {
		Element staEl = XMLDBHelper.selectSingleNode("/SCL/Substation");
		if (staEl == null) {
			return;
		}
		Tb1041SubstationEntity station = staServ.getCurrSubstation();
		// 连接点
		List<Element> connEls = DOM4JNodeHelper.selectNodes(staEl, "./VoltageLevel/Bay/ConnectivityNode");
		for (Element connEl : connEls) {
			String cnode = connEl.attributeValue("name");
			if (connMap.containsKey(cnode)) {
				SCTLogger.error("已存在：" + cnode);
				continue;
			}
			Tb1045ConnectivitynodeEntity conn = new Tb1045ConnectivitynodeEntity();
			conn.setF1045Code(rscp.nextTbCode(DBConstants.PR_CNode));
			conn.setF1045Name(cnode);
			conn.setF1045Desc(connEl.attributeValue("pathName"));
			beanDao.insert(conn);
			connMap.put(cnode, conn);
		}
		List<Element> volEls = staEl.elements("VoltageLevel");
		for (Element volEl : volEls) {
			String vol = DOM4JNodeHelper.getNodeValue(volEl, "./Voltage");
			int ivol = 0;
			if (!StringUtil.isEmpty(vol)) {
				ivol = Integer.parseInt(vol);
			}
			List<Element> bayEls = volEl.elements("Bay");
			for (Element bayEl : bayEls) {
				List<Element> eqpEls = bayEl.elements();
				Set<Tb1043EquipmentEntity> equipments = new HashSet<>();
				String bayName = bayEl.attributeValue("name");
				String bayDesc = bayEl.attributeValue("desc");
				Tb1042BayEntity bay = addBay(station, ivol, bayName, bayDesc, equipments);
				for (Element eqpEl : eqpEls) {
					String stype = eqpEl.attributeValue("type");
					EnumEquipmentType type = null;
					String nodeName = eqpEl.getName();
					List<Tb1044TerminalEntity> terminals = new ArrayList<>();
					switch(nodeName) {
					case "PowerTransformer":
						type = EnumEquipmentType.PTR;
						break;
					case "ConductingEquipment":
						type = EnumEquipmentType.getType(stype);
						break;
					default:
						break;
					}
					if (type != null) {
						Tb1043EquipmentEntity equipment = new Tb1043EquipmentEntity();
						equipments.add(equipment);
						equipment.setF1043Code(rscp.nextTbCode(DBConstants.PR_EQP));
						equipment.setTb1042BayByF1042Code(bay);
						equipment.setF1043Name(eqpEl.attributeValue("name"));
						equipment.setF1043Desc(eqpEl.attributeValue("desc"));
						String virtual = eqpEl.attributeValue("virtual");
						boolean isv = false;
						if (!StringUtil.isEmpty(virtual)) {
							isv = Boolean.parseBoolean(virtual);
						}
						equipment.setF1043IsVirtual(isv ? 1 : 0);
						equipment.setF1043Type(type.getCode());
						if (EnumEquipmentType.PTR == type) {	// 变压器
							List<Element> twEls = eqpEl.elements("TransformerWinding");
							for (Element twEl : twEls) {
								addTerminals(equipment, eqpEl); // 连接端子
							}
						} else {								// 其它设备
							addTerminals(equipment, eqpEl); // 连接端子
							if (EnumEquipmentType.VTR == type ||
									EnumEquipmentType.CTR == type) {
								secService.addCtvtsecondary(equipment, null); // 互感器次级
							}
						}
						// 一二次关联信息
						String lnodeXpath = null;
						if (EnumEquipmentType.VTR == type ||
								EnumEquipmentType.CTR == type) {
							lnodeXpath = "./SubEquipment/LNode";
						} else {
							lnodeXpath = "./LNode";
						}
						List<Element> lnodeEls = DOM4JNodeHelper.selectNodes(eqpEl, lnodeXpath);
						for (Element lnodeEl : lnodeEls) {
							updateIEDBayInfo(lnodeEl, bay);
						}
					}
				}
				beanDao.update(bay);
			}
		}
		addBay(station, 0, DBConstants.BAY_PUB, DBConstants.BAY_PUB, null); // 补充公共间隔用于存放交换机、采集器、配线架
	}

	private Tb1042BayEntity addBay(Tb1041SubstationEntity station, int ivol,
			String bayName, String bayDesc, Set<Tb1043EquipmentEntity> equipments) {
		Tb1042BayEntity bay = new Tb1042BayEntity();
		bay.setF1042Code(rscp.nextTbCode(DBConstants.PR_BAY));
		bay.setF1042Name(bayName);
		bay.setF1042Desc(bayDesc);
		bay.setF1042Voltage(ivol);
		bay.setTb1041SubstationByF1041Code(station);
		bay.setTb1043EquipmentsByF1042Code(equipments);
		beanDao.insert(bay);	// 避免IED更新bay信息后查询出错
		return bay;
	}
	
	/**
	 * 解析连接端子
	 * @param equipment
	 * @param eqpEl
	 */
	@SuppressWarnings("unchecked")
	private void addTerminals(Tb1043EquipmentEntity equipment, Element eqpEl) {
		Set<Tb1044TerminalEntity> terminals = new HashSet<>();
		List<Element> tmEls = eqpEl.elements("Terminal");
		if (tmEls == null || tmEls.size() < 1)
			return;
		for (Element tmEl : tmEls) {
			Tb1044TerminalEntity tm = new Tb1044TerminalEntity();
			terminals.add(tm);
			tm.setF1044Code(rscp.nextTbCode(DBConstants.PR_Term));
			tm.setF1044Name(tmEl.attributeValue("name"));
			tm.setF1044Desc(tmEl.attributeValue("connectivityNode"));
			tm.setTb1043EquipmentByF1043Code(equipment);
			String cnode = tmEl.attributeValue("cNodeName");
			Tb1045ConnectivitynodeEntity node = connMap.get(cnode);
			if (node == null) {
				String eqp = equipment.getTb1042BayByF1042Code().getF1042Name() + "/" + equipment.getF1043Name();
				SCTLogger.error("拓扑模型有误，找不到设备[ " + eqp + " ]连接点" + cnode);
			}
			tm.setTb1045ConnectivitynodeByF1045Code(node);
			terminals.add(tm);
		}
		equipment.setTb1044TerminalsByF1043Code(terminals);
	}

	private void updateIEDBayInfo(Element lnodeEl, Tb1042BayEntity bay) {
		String iedName = lnodeEl.attributeValue("iedName");
		if (!StringUtil.isEmpty(iedName) && !"None".equalsIgnoreCase(iedName) && !"null".equalsIgnoreCase(iedName)) {
			Tb1046IedEntity ied = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedName);
			String bayName = ied.getTb1042BaysByF1042Code().getF1042Name();
			if (DBConstants.BAY_OTHER.equals(bayName)) {
				String bayCode = bay.getF1042Code();
				ied.setF1042Code(bayCode);
				String f1046Code = ied.getF1046Code();
				iedServ.updateIEDBayCode(f1046Code, bayCode);
				List<Tb1065LogicallinkEntity> logiclinkIns = (List<Tb1065LogicallinkEntity>) beanDao.getListByCriteria(
						Tb1065LogicallinkEntity.class, "f1046CodeIedRecv", f1046Code);
				for (Tb1065LogicallinkEntity logiclink : logiclinkIns) {
//					ied = logiclink.getTb1046IedByF1046CodeIedSend();
//					ied.setF1042Code(bayCode);
//					iedServ.updateIEDBayCode(ied);
					iedServ.updateIEDBayCode(logiclink.getF1046CodeIedSend(), bayCode);
				}
				List<Tb1065LogicallinkEntity> logiclinkOuts = (List<Tb1065LogicallinkEntity>) beanDao.getListByCriteria(
						Tb1065LogicallinkEntity.class, "f1046CodeIedSend", f1046Code);
				for (Tb1065LogicallinkEntity logiclink : logiclinkOuts) {
//					ied = logiclink.getTb1046IedByF1046CodeIedRecv();
//					ied.setF1042Code(bayCode);
//					iedServ.updateIEDBayCode(ied);
					iedServ.updateIEDBayCode(logiclink.getF1046CodeIedRecv(), bayCode);
				}
			}
		}
	}

	public void init() {
		Element staEl = XMLDBHelper.selectSingleNode("/SCL/Substation");
		// 变电站
		Tb1041SubstationEntity station = new Tb1041SubstationEntity();
		station.setF1041Code(rscp.nextTbCode(DBConstants.PR_STA));
		if (staEl == null) {
			station.setF1041Name(Constants.CURRENT_PRJ_NAME);
			station.setF1041Desc(Constants.CURRENT_PRJ_NAME);
		} else {
			station.setF1041Name(staEl.attributeValue("name"));
			station.setF1041Desc(staEl.attributeValue("desc"));
		}
		beanDao.insert(station);		
	}
}
