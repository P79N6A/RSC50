/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class DsSettingParser extends IedParserBase<Tb1057SgcbEntity> {

	private static final String datSet = "dsSetting";
	
	public DsSettingParser(Tb1046IedEntity ied) {
		super(ied);
	}

	@Override
	public void parse() {
		String lnXpath = "/SCL/IED[@name='" + iedName + "']/AccessPoint/Server/LDevice/LN0";
		List<Element> elLNs = XMLDBHelper.selectNodes(lnXpath);
		for (Element elLN : elLNs) {
			Element elDat = DOM4JNodeHelper.selectSingleNode(elLN, "./DataSet[@name='" + datSet + "']");
			if (elDat == null) {
				continue;
			}
			Tb1057SgcbEntity sgcb = new Tb1057SgcbEntity();
			sgcb.setF1057Code(rscp.nextTbCode(DBConstants.PR_SGCB));
			sgcb.setTb1046IedByF1046Code(ied);
			sgcb.setF1057CbName("");	// TODO 待定
			sgcb.setF1057Dataset(datSet);
			sgcb.setF1057DsDesc(elDat.attributeValue("desc"));
			items.add(sgcb);
			List<Element> elFcdas = elDat.elements("FCDA");
			List<Tb1059SgfcdaEntity> sgFcdaList = new ArrayList<>();
			sgcb.setTb1059SgfcdasByF1057Code(sgFcdaList);
			int i = 0;
			for (Element fcdaEl : elFcdas) {
				String fcdaDesc = fcdaDAO.getFCDADesc(iedName, fcdaEl);
				Tb1059SgfcdaEntity sgFcda = new Tb1059SgfcdaEntity();
				sgFcdaList.add(sgFcda);
				sgFcda.setF1059Code(rscp.nextTbCode(DBConstants.PR_SG));
				sgFcda.setTb1057SgcbByF1057Code(sgcb);
				sgFcda.setF1059Index(i);
				sgFcda.setF1059Desc(fcdaDesc);
				sgFcda.setF1059RefAddr(SclUtil.getCfgRef(iedName, fcdaEl));
				sgFcda.setF1059DataType(DBConstants.DAT_TYP_FLOAT);		// TODO 待定
				String doName = fcdaEl.attributeValue("doName");
				Element doEl = DOM4JNodeHelper.selectSingleNode(elLN, "./DOI[@name='" + doName + "']");
				String step = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='stepSize']/*[@name='f']/Val");
				String min = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='minVal']/*[@name='f']/Val");
				String max = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='maxVal']/*[@name='f']/Val");
				String units = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='units']/*[@name='SIUnit']/Val");	// TODO 待定
				if (!StringUtil.isEmpty(step)) {
					sgFcda.setF1059StepSize(Float.valueOf(step));
				}
				if (!StringUtil.isEmpty(min)) {
					sgFcda.setF1059ValueMin(Float.valueOf(min));
				}
				if (!StringUtil.isEmpty(max)) {
					sgFcda.setF1059ValueMax(Float.valueOf(max));
				}
				if (!StringUtil.isEmpty(units)) {
					sgFcda.setF1059Unit(units);
				}
				i++;
			}
		}
		saveAll();
	}

}

