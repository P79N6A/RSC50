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
import org.eclipse.core.runtime.IProgressMonitor;

import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.ied.Context;
import com.synet.tool.rsc.io.scd.EnumEquipmentType;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.service.BayEntityService;
import com.synet.tool.rsc.service.CtvtsecondaryService;
import com.synet.tool.rsc.service.IedEntityService;
import com.synet.tool.rsc.service.LNodeEntityService;
import com.synet.tool.rsc.service.StatedataService;
import com.synet.tool.rsc.service.SubstationService;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class OnlySubstationParser extends IedParserBase<Tb1042BayEntity> {

	private Map<String, Tb1045ConnectivitynodeEntity> connMap = new HashMap<>();
	private CtvtsecondaryService secService = new CtvtsecondaryService();
	private SubstationService staServ = new SubstationService();
	private BayEntityService bayServ = new BayEntityService();
	private IedEntityService iedServ = new IedEntityService();
	private LNodeEntityService lnodeServ = new LNodeEntityService();
	private StatedataService statedataService = new StatedataService();
	private Context context;
	private IProgressMonitor monitor;
	
	public OnlySubstationParser(Context context, IProgressMonitor monitor) {
		super(null);
		this.context = context;
		this.monitor = monitor;
	}

	@Override
	public void parse() {
		Element staEl = XMLDBHelper.selectSingleNode("/SCL/Substation");
		if (staEl == null) {
			return;
		}
		// 更新变电站信息
		Tb1041SubstationEntity station = staServ.getCurrSubstation();
		if (station == null) {
			return;
		}
		station.setF1041Name(staEl.attributeValue("name"));
		station.setF1041Desc(staEl.attributeValue("desc"));
		beanDao.update(station);
		int bayCount = XMLDBHelper.countNodes("/SCL/Substation/VoltageLevel/Bay");
		if (monitor != null) {
			monitor.beginTask("开始导入SSD信息", bayCount + 2);
		}
		int bayNum = 0, eqpNum = 0, conNum = 0;
		// 连接点
		if (monitor != null) {
			monitor.setTaskName("正在导入设备拓扑连接关系");
		}
		List<Tb1045ConnectivitynodeEntity> conns = new ArrayList<>();
		List<Element> connEls = DOM4JNodeHelper.selectNodes(staEl, "./VoltageLevel/Bay/ConnectivityNode");
		for (Element connEl : connEls) {
			String pathName = connEl.attributeValue("pathName");
			String nodeName = connEl.attributeValue("name");
			if ("grounded".equals(nodeName.toLowerCase())) {
				continue;
			}
			if (connMap.containsKey(nodeName)) {
				SCTLogger.warn("已存在：" + nodeName);
				context.addWarning("一次模型", "连接点", nodeName, "拓扑模型有误，存在同名的连接点。");
				continue;
			}
			addConnNode(nodeName, pathName, conns);
			conNum++;
		}
		addConnNode("grounded", "grounded", conns);
		beanDao.insertBatch(conns);
		if (monitor != null) {
			monitor.worked(1);
		}
		// 一次设备
		List<Tb1043EquipmentEntity> eqpList = new ArrayList<>();
		List<Element> volEls = staEl.elements("VoltageLevel");
		for (Element volEl : volEls) {
			String vol = DOM4JNodeHelper.getNodeValue(volEl, "./Voltage");
			int ivol = 0;
			if (!StringUtil.isEmpty(vol)) {
				ivol = Integer.parseInt(vol);
			}
			List<Element> bayEls = volEl.elements("Bay");
			bayNum += bayEls.size();
			for (Element bayEl : bayEls) {
				List<Element> eqpEls = bayEl.elements();
				String bayName = bayEl.attributeValue("name");
				String bayDesc = bayEl.attributeValue("desc");
				if (monitor != null) {
					monitor.setTaskName("正在导入间隔" + bayName);
				}
				Tb1042BayEntity bay = bayServ.addBay(station, ivol, bayName, bayDesc);
				for (Element eqpEl : eqpEls) {
					String stype = eqpEl.attributeValue("type");
					EnumEquipmentType type = null;
					String nodeName = eqpEl.getName();
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
						eqpList.add(equipment);
						String eqpCode = rscp.nextTbCode(DBConstants.PR_EQP);
						equipment.setF1043Code(eqpCode);
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
								addTerminals(equipment, twEl); // 连接端子
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
							lnodeServ.updateIEDBayInfo(lnodeEl.attributeValue("iedName"), bay);
							// 判断是否接地
							List<Element> terminals = eqpEl.elements("Terminal");
							boolean isGround = false;
							for (Element terminal : terminals) {
								String cNodeName = terminal.attributeValue("cNodeName");
								if ("grounded".equals(cNodeName)) {
									isGround = true;
									break;
								}
							}
							String lnClass = lnodeEl.attributeValue("lnClass");
							String ldInst = lnodeEl.attributeValue("ldInst");
							String prefix = lnodeEl.attributeValue("prefix");
							String lnInst = lnodeEl.attributeValue("lnInst");
							String iedName = lnodeEl.attributeValue("iedName");
							lnodeServ.linkStaPoint(eqpCode, iedName, ldInst, prefix, lnClass, lnInst, isGround);
						}
						eqpNum++;
					}
				}
				if (monitor != null) {
					monitor.worked(1);
				}
			}
		}
		beanDao.insertBatch(eqpList);
		if (monitor != null) {
			monitor.setTaskName("正在添加公共间隔");
		}
		bayServ.addBay(station, 0, DBConstants.BAY_PUB, DBConstants.BAY_PUB); // 补充公共间隔用于存放交换机、采集器、配线架
		bayNum++;
		ConsoleManager console = ConsoleManager.getInstance();
		console.append("一共导入 " + bayNum + " 个间隔， " + eqpNum + " 台设备， " + conNum + " 个连接点。");
		if (monitor != null) {
			monitor.done();
		}
	}
	
	private void updatePosType(String iedName, String dataRef, Rule rule) {
		statedataService.updateStateF1011No(iedName, dataRef, rule.getId());
	}

	private void addConnNode(String nodeName,
			String pathName, List<Tb1045ConnectivitynodeEntity> conns) {
		Tb1045ConnectivitynodeEntity conn = new Tb1045ConnectivitynodeEntity();
		conn.setF1045Code(rscp.nextTbCode(DBConstants.PR_CNode));
		conn.setF1045Name(nodeName);
		conn.setF1045Desc(pathName);
		conns.add(conn);
		connMap.put(nodeName, conn);
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
			String tmName = tmEl.attributeValue("name");
			Tb1044TerminalEntity tm = new Tb1044TerminalEntity();
			terminals.add(tm);
			tm.setF1044Code(rscp.nextTbCode(DBConstants.PR_Term));
			tm.setF1044Name(tmName);
			tm.setF1044Desc(tmEl.attributeValue("connectivityNode"));
			tm.setTb1043EquipmentByF1043Code(equipment);
			String cnode = tmEl.attributeValue("cNodeName");
			boolean isNull = StringUtil.isEmpty(cnode) || "null".equals(cnode.toLowerCase());
			String eqp = equipment.getTb1042BayByF1042Code().getF1042Name() + "/" + equipment.getF1043Name();
			if (isNull) {
				context.addWarning("一次模型", "一次设备", eqp, "拓扑模型有误，端子[ " + tmName + " ]的连接点为空。");
			} else {
				Tb1045ConnectivitynodeEntity node = connMap.get(cnode);
				if (node == null) {
					context.addWarning("一次模型", "一次设备", eqp, "拓扑模型有误，找不到端子[ " + tmName + " ]的连接点 " + cnode + " 。");
				} else {
					tm.setTb1045ConnectivitynodeByF1045Code(node);
				}
			}
			terminals.add(tm);
		}
		equipment.setTb1044TerminalsByF1043Code(terminals);
	}

}
