package com.synet.tool.rsc.io.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shrcn.found.ui.view.ConsoleManager;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.parser.ParserUtil;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class LogicLinkParserNew {
	
	private Context context;
	private RSCProperties rscp = RSCProperties.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();

	public LogicLinkParserNew(Context context) {
		this.context = context;
	}
	
	private boolean isMoni(Tb1046IedEntity ied) {
		Integer f1046Type = ied.getF1046Type();
		return (f1046Type != null && DBConstants.IED_MONI == f1046Type);
	}

	// 虚链路与虚回路
	public void parse() {
		Map<String, Tb1065LogicallinkEntity> linkCache = new HashMap<>();
		Map<String, Map<String, String>> vtLinkMap = context.getVtLinkMap();
		List<Tb1063CircuitEntity> circuits = new ArrayList<>();
		for (String iedName : vtLinkMap.keySet()) {
			Map<String, String> links = vtLinkMap.get(iedName);
			for (String key : links.keySet()) {
				String value = links.get(key);
				if (!(key.indexOf(",")>0) || !(value.indexOf(",")>0)) {
					continue;
				}
				String[] temp = key.split(",");
				String inDesc = temp[0];
				String intAddr = temp[1];
				temp = value.split(",");
				String outIedName = temp[0];
				String outAddr = temp[1];
				Tb1046IedEntity resvIed = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", iedName);
				Tb1046IedEntity sendIed = (Tb1046IedEntity) beanDao.getObject(Tb1046IedEntity.class, "f1046Name", outIedName);
				
				if (outAddr.endsWith(".q") || outAddr.endsWith(".t") 
						|| intAddr.endsWith(".q") || intAddr.endsWith(".t")
						|| isMoni(sendIed) || isMoni(resvIed)) {
					continue;
				}
				Tb1061PoutEntity pout = context.getPout(outIedName + outAddr);
				if (pout == null) {
					context.addError(iedName, "虚端子关联", intAddr, "找不到外部虚端子" + outAddr + "。");
					continue;
				}
				// 创建虚链路
				BaseCbEntity cbEntity = pout.getCbEntity();
				String linkKey = iedName + "." + outIedName + "." + cbEntity.getCbId();
				Tb1065LogicallinkEntity logicLink = linkCache.get(linkKey);
				if (logicLink == null) {
					Map<String, Object> params = new HashMap<>();
					params.put("baseCbByCdCode", cbEntity);
					params.put("tb1046IedByF1046CodeIedRecv", resvIed);
					params.put("tb1046IedByF1046CodeIedSend", sendIed);
					logicLink = (Tb1065LogicallinkEntity) beanDao.getObject(Tb1065LogicallinkEntity.class, params);
					if (logicLink == null) {
						logicLink = ParserUtil.createLogicLink(resvIed, cbEntity);
						beanDao.insert(logicLink);
					}
					linkCache.put(linkKey, logicLink);
				}
				// 创建虚回路
				Tb1062PinEntity pin = context.getPin(iedName + intAddr);
				if (pin == null) {
					// SV到da的情况
					String[] ins = intAddr.split("\\.");
					pin = context.getPin(iedName + ins[0] + "." + ins[1]);
					if (pin == null) {
						String lnName = SclUtil.getLnName(intAddr);
						if (lnName.startsWith("GOIN") || lnName.startsWith("SVIN")) {
							String outRef = pout.getF1061RefAddr();
							String fc = SclUtil.getFC(outRef);
//							String lnName = SclUtil.getLnName(intAddr);
							String doName = SclUtil.getDoName(intAddr);
							Rule type = F1011_NO.getType("", lnName, doName, inDesc, fc);
							intAddr = SclUtil.getFcdaRef(intAddr, fc);
							pin = ParserUtil.createPin(resvIed, intAddr, inDesc, type.getId(), 1);
							beanDao.insert(pin);
						} else {
//							context.addWarning(iedName, "逻辑链路", outIedName + cbEntity.getCbId(), "找不到接收虚端子" + intAddr);
							continue;
						}
					} else {
						pin.setF1062IsUsed(1);
						beanDao.update(pin);
					}
				} else {
					pin.setF1062IsUsed(1);
					beanDao.update(pin);
				}
				Tb1063CircuitEntity circuit = createCircuit(logicLink, pin, pout);
				circuits.add(circuit);
			}
		}
		beanDao.insertBatch(circuits);
		ConsoleManager.getInstance().append("一共导入 " + linkCache.size() +
				" 条逻辑链路，" + circuits.size() + "  条虚回路。");
	}
	
	private Tb1063CircuitEntity createCircuit(Tb1065LogicallinkEntity logiclink, Tb1062PinEntity pin, Tb1061PoutEntity pout) {
//		String poutRef = pout.getF1061RefAddr();
//		String fc = SclUtil.getFC(poutRef);
//		pinRefAddr = SclUtil.getFcdaRef(pinRefAddr, fc);
		Tb1046IedEntity iedResv = logiclink.getTb1046IedByF1046CodeIedRecv(); 
		Tb1046IedEntity iedSend = logiclink.getTb1046IedByF1046CodeIedSend(); 
		Tb1063CircuitEntity circuit = new Tb1063CircuitEntity();
		circuit.setF1063Code(rscp.nextTbCode(DBConstants.PR_CIRCUIT));
		circuit.setTb1065LogicallinkByF1065Code(logiclink);
		circuit.setTb1046IedByF1046CodeIedRecv(iedResv);
		circuit.setTb1046IedByF1046CodeIedSend(iedSend);
		circuit.setTb1061PoutByF1061CodePSend(pout);
//		// 接收
//		Tb1062PinEntity pin = new Tb1062PinEntity();
		circuit.setTb1062PinByF1062CodePRecv(pin);
//		pin.setF1062Code(rscp.nextTbCode(DBConstants.PR_PIN));
//		pin.setTb1046IedByF1046Code(iedResv);
//		pin.setF1062RefAddr(pinRefAddr);
//		pin.setF1062Desc(pinDesc);
//		pin.setF1062IsUsed(1);
//		Tb1016StatedataEntity stdata = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", pout.getDataCode());
//		if (stdata != null) {
//			pin.setF1011No(stdata.getF1011No());
//		} else {
//			Tb1006AnalogdataEntity mxdata = (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", pout.getDataCode());
//			pin.setF1011No(mxdata.getF1011No());
//		}
//		beanDao.insert(pin);
		return circuit;
	}
	
}
