/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class DsParameterParser extends IedParserBase<Tb1060SpfcdaEntity> {

	private static final String datSet = "dsParameter";
	
	public DsParameterParser(Tb1046IedEntity ied, Map<String, Map<String, Object[]>> lnTypeMap) {
		super(ied);
		this.lnTypeMap = lnTypeMap;
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
			List<Element> elFcdas = elDat.elements("FCDA");
			int i = 0;
			for (Element fcdaEl : elFcdas) {
				String fcdaDesc = fcdaDAO.getFCDADesc(iedName, fcdaEl);
				Tb1060SpfcdaEntity sgFcda = new Tb1060SpfcdaEntity();
				items.add(sgFcda);
				sgFcda.setF1060Code(rscp.nextTbCode(DBConstants.PR_SP));
				sgFcda.setTb1046IedByF1046Code(ied);
				sgFcda.setF1060Index(i);
				sgFcda.setF1060Desc(fcdaDesc);
				sgFcda.setF1060RefAddr(SclUtil.getFcdaRef(fcdaEl));
				sgFcda.setF1060DataType(getBType(fcdaEl));
//				String doName = fcdaEl.attributeValue("doName");
//				Element doEl = DOM4JNodeHelper.selectSingleNode(elLN, "./DOI[@name='" + doName + "']");
//				if (doEl != null) {
//					String step = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='stepSize']/*[@name='f']/Val");
//					String min = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='minVal']/*[@name='f']/Val");
//					String max = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='maxVal']/*[@name='f']/Val");
//					String units = DOM4JNodeHelper.getNodeValue(doEl, "./*[@name='units']/*[@name='SIUnit']/Val");	// TODO 待定
//					if (!StringUtil.isEmpty(step)) {
//						sgFcda.setF1060StepSize(Float.valueOf(step));
//					}
//					if (!StringUtil.isEmpty(min)) {
//						sgFcda.setF1060ValueMin(Float.valueOf(min));
//					}
//					if (!StringUtil.isEmpty(max)) {
//						sgFcda.setF1060ValueMax(Float.valueOf(max));
//					}
//					if (!StringUtil.isEmpty(units)) {
//						sgFcda.setF1060Unit(units);
//					}
//				}
				i++;
			}
		}
		saveAll();
	}

}

