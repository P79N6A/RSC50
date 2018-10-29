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
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.util.DataUtils;
import com.synet.tool.rsc.util.F1011_NO;

public class LogicLinkParserNew {
	
	private Context context;
	private RSCProperties rscp = RSCProperties.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();

	public LogicLinkParserNew(Context context) {
		this.context = context;
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
				
//				Map<String, Object> params = new HashMap<>();
//				params.put("tb1046IedByF1046Code", sendIed);
//				params.put("f1061RefAddr", outAddr);
//				Tb1061PoutEntity pout = (Tb1061PoutEntity) beanDao.getObject(Tb1061PoutEntity.class, params);
				Tb1061PoutEntity pout = context.getPout(outAddr);
				if (pout == null) {
					context.addError(iedName, "虚端子关联", intAddr, "找不到外部虚端子" + outAddr + "。");
					continue;
				}
				// 创建虚链路
				BaseCbEntity cbEntity = pout.getCbEntity();
				String linkKey = iedName + "." + outIedName + "." + cbEntity.getCbCode();
				Tb1065LogicallinkEntity logicLink = linkCache.get(linkKey);
				if (logicLink == null) {
//					params.clear();
					Map<String, Object> params = new HashMap<>();
					params.put("baseCbByCdCode", cbEntity);
					params.put("tb1046IedByF1046CodeIedRecv", resvIed);
					params.put("tb1046IedByF1046CodeIedSend", sendIed);
					logicLink = (Tb1065LogicallinkEntity) beanDao.getObject(Tb1065LogicallinkEntity.class, params);
					if (logicLink == null) {
						logicLink = new Tb1065LogicallinkEntity();
						logicLink.setF1065Code(rscp.nextTbCode(DBConstants.PR_LOGICLINK));
						int cbType = DBConstants.LINK_GOOSE;
						if (cbEntity instanceof Tb1056SvcbEntity) {
							cbType = DBConstants.LINK_SMV;
						}
						logicLink.setF1065Type(cbType);
						logicLink.setBaseCbByCdCode(cbEntity);
						logicLink.setF1046CodeIedRecv(resvIed.getF1046Code());
						logicLink.setF1046CodeIedSend(sendIed.getF1046Code());
						logicLink.setTb1046IedByF1046CodeIedRecv(resvIed);
						logicLink.setTb1046IedByF1046CodeIedSend(sendIed);
						beanDao.insert(logicLink);
					}
					linkCache.put(linkKey, logicLink);
				}
				// 创建虚回路
				Tb1063CircuitEntity circuit = createCircuit(logicLink, intAddr, inDesc, pout);
				circuits.add(circuit);
			}
		}
		beanDao.insertBatch(circuits);
		ConsoleManager.getInstance().append("一共导入 " + linkCache.size() +
				" 条逻辑链路，" + circuits.size() + "  条虚回路。");
	}
	
	private Tb1063CircuitEntity createCircuit(Tb1065LogicallinkEntity logiclink, String pinRefAddr, String pinDesc, Tb1061PoutEntity pout) {
		String poutRef = pout.getF1061RefAddr();
		String fc = SclUtil.getFC(poutRef);
		pinRefAddr = SclUtil.getFcdaRef(pinRefAddr, fc);
		Tb1046IedEntity iedResv = logiclink.getTb1046IedByF1046CodeIedRecv(); 
		Tb1046IedEntity iedSend = logiclink.getTb1046IedByF1046CodeIedSend(); 
		Tb1063CircuitEntity circuit = new Tb1063CircuitEntity();
		circuit.setF1063Code(rscp.nextTbCode(DBConstants.PR_CIRCUIT));
		circuit.setTb1065LogicallinkByF1065Code(logiclink);
		circuit.setTb1046IedByF1046CodeIedRecv(iedResv);
		circuit.setTb1046IedByF1046CodeIedSend(iedSend);
		circuit.setTb1061PoutByF1061CodePSend(pout);
		// 接收
		Tb1062PinEntity pin = new Tb1062PinEntity();
		circuit.setTb1062PinByF1062CodePRecv(pin);
		pin.setF1062Code(rscp.nextTbCode(DBConstants.PR_PIN));
		pin.setTb1046IedByF1046Code(iedResv);
		pin.setF1062RefAddr(pinRefAddr);
		pin.setF1062Desc(pinDesc);
		pin.setF1062IsUsed(1);
		Tb1016StatedataEntity stdata = (Tb1016StatedataEntity) beanDao.getObject(Tb1016StatedataEntity.class, "f1016Code", pout.getDataCode());
		if (stdata != null) {
			pin.setF1011No(stdata.getF1011No());
		} else {
			Tb1006AnalogdataEntity mxdata = (Tb1006AnalogdataEntity) beanDao.getObject(Tb1006AnalogdataEntity.class, "f1006Code", pout.getDataCode());
			pin.setF1011No(mxdata.getF1011No());
		}
		beanDao.insert(pin);
		return circuit;
	}
	
}
