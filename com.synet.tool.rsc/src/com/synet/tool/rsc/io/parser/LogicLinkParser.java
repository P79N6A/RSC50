/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shrcn.business.scl.check.CCDModelChecker;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class LogicLinkParser extends IedParserBase<Tb1065LogicallinkEntity> {

	public LogicLinkParser(Tb1046IedEntity ied) {
		super(ied);
	}

	@Override
	public void parse() {
		CCDModelChecker checker = new CCDModelChecker(iedName, CCDModelChecker.CHECK_CCD);
		boolean success = checker.doCheck();
		if (!success) {
			SCTLogger.error("装置 " + iedName + " 过程层通信配置有误，无法生成虚链路模型！");
			return;
		}
		Document ccdDoc = checker.createCcdDom();
		checker.clear();
		if (ccdDoc == null)
			return;
		Element root = ccdDoc.getRootElement();
		// GOOSE IN
		parseGooseIn(root);
		// SMV IN
		parseSmvIn(root);
		saveAll();
	}
	
	private void parseGooseIn(Element root) {
		List<Element> gseIns = DOM4JNodeHelper.selectNodes(root , "./GOOSESUB/GOCBref");
		for (Element gseIn : gseIns) {
			Tb1065LogicallinkEntity logiclink = new Tb1065LogicallinkEntity();
			items.add(logiclink);
			logiclink.setF1065Code(rscp.nextTbCode(DBConstants.PR_LOGICLINK));
			logiclink.setF1065Type(DBConstants.LINK_GOOSE);
			String appId = DOM4JNodeHelper.getAttributeValue(gseIn, "./GSEControl/@appID");
			Tb1055GcbEntity gcb = (Tb1055GcbEntity) beanDao.getObject(Tb1055GcbEntity.class, "cbId", appId);
			logiclink.setBaseCbByCdCode(gcb);
			logiclink.setF1046CodeIedRecv(ied.getF1046Code());
			String iedSendName = DOM4JNodeHelper.getAttributeValue(gseIn, "./ConnectedAP/@iedName");
			Tb1046IedEntity iedSend = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedSendName);
			logiclink.setF1046CodeIedSend(iedSend.getF1046Code());
			List<Element> fcdaList = DOM4JNodeHelper.selectNodes(gseIn, "./DataSet/FCDA");
			Set<Tb1063CircuitEntity> circuits = new HashSet<>();
			logiclink.setTb1063CircuitsByF1065Code(circuits);
			for (Element fcdaEl : fcdaList) {
				Element intAddrEl = fcdaEl.element("intAddr");
				if (intAddrEl == null)
					continue;
				String name = intAddrEl.attributeValue("name");
				if ("NULL".equals(name) || name==null)
					continue;
				circuits.add(createCircuit(logiclink, iedSend, fcdaEl));
			}
		}
	}

	private void parseSmvIn(Element root) {
		List<Element> smvIns = DOM4JNodeHelper.selectNodes(root , "./SVSUB/SMVCBref");
		for (Element smvIn : smvIns) {
			Tb1065LogicallinkEntity logiclink = new Tb1065LogicallinkEntity();
			items.add(logiclink);
			logiclink.setF1065Code(rscp.nextTbCode(DBConstants.PR_LOGICLINK));
			logiclink.setF1065Type(DBConstants.LINK_SMV);
			String cbId = DOM4JNodeHelper.getAttributeValue(smvIn, "./SampledValueControl/@smvID");
			Tb1056SvcbEntity gcb = (Tb1056SvcbEntity) beanDao.getObject(Tb1056SvcbEntity.class, "cbId", cbId);
			logiclink.setBaseCbByCdCode(gcb);
			logiclink.setF1046CodeIedRecv(ied.getF1046Code());
			String iedSendName = DOM4JNodeHelper.getAttributeValue(smvIn, "./ConnectedAP/@iedName");
			Tb1046IedEntity iedSend = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedSendName);
			logiclink.setF1046CodeIedSend(iedSend.getF1046Code());
			List<Element> fcdaList = DOM4JNodeHelper.selectNodes(smvIn, "./DataSet/FCDA");
			Set<Tb1063CircuitEntity> circuits = new HashSet<>();
			logiclink.setTb1063CircuitsByF1065Code(circuits);
			for (Element fcdaEl : fcdaList) {
				Element intAddrEl = fcdaEl.element("intAddr");
				if (intAddrEl == null)
					continue;
				String name = intAddrEl.attributeValue("name");
				if ("NULL".equals(name) || name==null)
					continue;
				circuits.add(createCircuit(logiclink, iedSend, fcdaEl));
			}
		}
	}
	
	private Tb1063CircuitEntity createCircuit(Tb1065LogicallinkEntity logiclink, Tb1046IedEntity iedSend, Element fcdaEl) {
		String doName = fcdaEl.attributeValue("doName");
		int circuitType = (DBConstants.LINK_SMV==logiclink.getF1065Type()) ? 
				getDoType(doName) : DBConstants.CIRCUIT_ST;
		Element intAddrEl = fcdaEl.element("intAddr");
		String name = intAddrEl.attributeValue("name");
		String desc = intAddrEl.attributeValue("desc");
		Tb1063CircuitEntity circuit = new Tb1063CircuitEntity();
		circuit.setF1063Code(rscp.nextTbCode(DBConstants.PR_CIRCUIT));
		circuit.setF1063Type(circuitType);
		circuit.setTb1065LogicallinkByF1065Code(logiclink);
		circuit.setTb1046IedByF1046CodeIedRecv(ied);
		circuit.setTb1046IedByF1046CodeIedSend(iedSend);
		String poutRefAddr = SclUtil.getFcdaRef(fcdaEl);
		// 发送
		Tb1061PoutEntity pout = (Tb1061PoutEntity) beanDao.getObject(Tb1061PoutEntity.class, "f1061RefAddr", poutRefAddr);
		circuit.setTb1061PoutByF1061CodePSend(pout);
		if (pout == null) {
			SCTLogger.error("找不到发送虚端子：" + poutRefAddr);
		}
		// 接收
		Tb1062PinEntity pin = new Tb1062PinEntity();
		circuit.setTb1062PinByF1062CodePRecv(pin);
		pin.setF1062Code(rscp.nextTbCode(DBConstants.PR_PIN));
		pin.setTb1046IedByF1046Code(ied);
		pin.setF1062Type(circuitType);
		String pinRefAddr = name;
		int p = pinRefAddr.indexOf(":");
		if (p > 0) {
			pinRefAddr = pinRefAddr.substring(p + 1);
		}
		pin.setF1062RefAddr(pinRefAddr);
		pin.setF1062Desc(desc);
		pin.setF1062IsUsed(1);
		beanDao.insert(pin);
		return circuit;
	}

	private int getDoType(String doName) {
		return doName.contains("Amp") ? DBConstants.CIRCUIT_I : 
			(doName.contains("Vol") ? DBConstants.CIRCUIT_U : DBConstants.CIRCUIT_OTHER);
	}
}
