package com.synet.tool.rsc.io.parser;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;

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
		return iedName + ldInst + "/LLN0$" + fc + "$" + cbName;//PL6602PIGO/LLN0$GO$gocb0，IL6602MUSV/LLN0$MS$smvcb1
	}
	
}
