package com.synet.tool.rsc.io.parser;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.BaseCbEntity;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.util.F1011_NO;
import com.synet.tool.rsc.util.Rule;

public class ParserUtil {

	private static RSCProperties rscp = RSCProperties.getInstance();
	
	/**
	 * 创建状态量数据对象
	 * @param desc
	 * @param parentCode
	 * @param f1011No
	 * @return
	 */
	public static Tb1016StatedataEntity createStatedata(String desc, String ref, String parentCode, Tb1046IedEntity ied, int f1011No) {
		String dataCode = rscp.nextTbCode(DBConstants.PR_State);
		Tb1016StatedataEntity stdata = new Tb1016StatedataEntity();
		stdata.setF1016Code(dataCode);
		stdata.setF1016Desc(desc);
		stdata.setF1016AddRef(ref);
		stdata.setF1016Safelevel(0);
		stdata.setF1011No(f1011No);
		stdata.setParentCode(parentCode);
		stdata.setTb1046IedByF1046Code(ied);
		return stdata;
	}
	
	/**
	 * 添加模拟量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	public static Tb1006AnalogdataEntity createAlgdata(String ref, String fcdaDesc, Tb1046IedEntity ied, int f1011No) {
		String dataCode = rscp.nextTbCode(DBConstants.PR_Analog);
		Tb1006AnalogdataEntity algdata = new Tb1006AnalogdataEntity();
		algdata.setF1006Code(dataCode);
		algdata.setF1006Desc(fcdaDesc);
		algdata.setF1006AddRef(ref);
		algdata.setF1006Safelevel(0);
		algdata.setF1011No(f1011No);
		algdata.setParentCode(ied.getF1046Code());
		algdata.setTb1046IedByF1046Code(ied);
		return algdata;
	}
	
	public static String getCbRef(String iedName, String ldInst, String cbName, String fc) {
		return iedName + getCbId(ldInst, cbName, fc);
	}
	
	public static String getCbId(String ldInst, String cbName, String fc) {
		return ldInst + "/LLN0$" + fc + "$" + cbName;//PL6602PIGO/LLN0$GO$gocb0，IL6602MUSV/LLN0$MS$smvcb1
	}
	
	/**
	 * 创建开入虚端子
	 * @param iedResv
	 * @param pinRefAddr
	 * @param pinDesc
	 * @param f1011No
	 * @param f1062IsUsed
	 * @return
	 */
	public static Tb1062PinEntity createPin(Tb1046IedEntity iedResv, String pinRefAddr, String pinDesc, int f1011No, int f1062IsUsed) {
		Tb1062PinEntity pin = new Tb1062PinEntity();
		pin.setF1062Code(rscp.nextTbCode(DBConstants.PR_PIN));
		pin.setTb1046IedByF1046Code(iedResv);
		pin.setF1062RefAddr(pinRefAddr);
		pin.setF1062Desc(pinDesc);
		pin.setF1011No(f1011No);
		pin.setF1062IsUsed(f1062IsUsed);
		return pin;
	}
	
	/**
	 * 创建逻辑链路
	 * @param resvIed
	 * @param cbEntity
	 * @return
	 */
	public static Tb1065LogicallinkEntity createLogicLink(Tb1046IedEntity resvIed, BaseCbEntity cbEntity) {
		Tb1065LogicallinkEntity logicLink = new Tb1065LogicallinkEntity();
		logicLink.setF1065Code(rscp.nextTbCode(DBConstants.PR_LOGICLINK));
		int cbType = DBConstants.LINK_GOOSE;
		if (cbEntity instanceof Tb1056SvcbEntity) {
			cbType = DBConstants.LINK_SMV;
		}
		logicLink.setF1065Type(cbType);
		logicLink.setBaseCbByCdCode(cbEntity);
		Tb1046IedEntity sendIed = cbEntity.getTb1046IedByF1046Code();
		logicLink.setF1046CodeIedRecv(resvIed.getF1046Code());
		logicLink.setF1046CodeIedSend(sendIed.getF1046Code());
		logicLink.setTb1046IedByF1046CodeIedRecv(resvIed);
		logicLink.setTb1046IedByF1046CodeIedSend(sendIed);
		return logicLink;
	}
	
	/**
	 * 创建逻辑链路与物理回路间关系
	 * @param logicallinkEntity
	 * @param physconnEntity
	 * @return
	 */
	public static Tb1073LlinkphyrelationEntity createLLinkRelation(Tb1065LogicallinkEntity logicallinkEntity, Tb1053PhysconnEntity physconnEntity) {
		Tb1073LlinkphyrelationEntity llinkphyrelationEntity = new Tb1073LlinkphyrelationEntity();
		llinkphyrelationEntity.setTb1065LogicallinkByF1065Code(logicallinkEntity);
		llinkphyrelationEntity.setTb1053PhysconnByF1053Code(physconnEntity);
		llinkphyrelationEntity.setF1073Code(rscp.nextTbCode(DBConstants.PR_LPRELATION));
		return llinkphyrelationEntity;
	}
}
