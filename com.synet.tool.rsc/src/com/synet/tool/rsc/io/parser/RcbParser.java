/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.IedInfoDao;
import com.synet.tool.rsc.io.scd.SclUtil;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class RcbParser extends IedParserBase<Tb1054RcbEntity> {

	public RcbParser(Tb1046IedEntity ied) {
		super(ied);
	}

	@Override
	public void parse() {
		String ldXpath = "/SCL/IED[@name='" + iedName + "']/AccessPoint/Server/LDevice";
		List<Element> elLds = XMLDBHelper.selectNodes(ldXpath);
		for (Element elLd : elLds) {
			List<Element> elLNs = elLd.elements("LN0");
			for (Element elLN : elLNs) {
				List<Element> elRcbs = elLN.elements("ReportControl");
				for (Element elRcb : elRcbs) {
					String datSet = elRcb.attributeValue("datSet");
					Element elDat = DOM4JNodeHelper.selectSingleNode(elLN, "./DataSet[@name='" + datSet + "']");
					Tb1054RcbEntity rcb = new Tb1054RcbEntity();
					rcb.setF1054Code(rscp.nextTbCode(DBConstants.PR_RCB));
					rcb.setTb1046IedByF1046Code(ied);
					rcb.setF1054Dataset(datSet);
					rcb.setF1054DsDesc(elDat.attributeValue("desc"));
					int brcb = IedInfoDao.isBrcb(elRcb) ? 1 : 0;
					rcb.setF1054IsBrcb(brcb);
					rcb.setF1054Rptid(elRcb.attributeValue("rptID"));
					rcb.setF1054CbType(IedInfoDao.getRcbType(datSet));
					items.add(rcb);
					List<Element> elFcdas = elDat.elements("FCDA");
					List<Tb1058MmsfcdaEntity> mmsFcdaList = new ArrayList<>();
					rcb.setTb1058MmsfcdasByF1054Code(mmsFcdaList);
					int i = 0;
					for (Element fcdaEl : elFcdas) {
						String fcdaDesc = fcdaDAO.getFCDADesc(iedName, fcdaEl);
						Tb1058MmsfcdaEntity mmsFcda = new Tb1058MmsfcdaEntity();
						mmsFcdaList.add(mmsFcda);
						mmsFcda.setF1058Code(rscp.nextTbCode(DBConstants.PR_RCB));
						mmsFcda.setTb1046IedByF1046Code(ied);
						mmsFcda.setTb1054RcbByF1054Code(rcb);
						mmsFcda.setF1058Index(i);
						mmsFcda.setF1058Desc(fcdaDesc);
						mmsFcda.setF1058RefAddr(SclUtil.getCfgRef(iedName, fcdaEl));
						String fc = fcdaEl.attributeValue("fc");
						if ("ST".equals(fc)) {
							mmsFcda.setF1058DataType(DBConstants.DATA_ST);
							mmsFcda.setDataCode(addStatedata(fcdaEl, fcdaDesc, DBConstants.DAT_BRK)); // TODO 需根据描述进一步分析
						} else {
							mmsFcda.setF1058DataType(DBConstants.DATA_MX);
							mmsFcda.setDataCode(addAlgdata(fcdaEl, fcdaDesc, DBConstants.DAT_PROT_MX)); // TODO 需根据描述进一步分析
						}
						i++;
					}
				}
			}
		}
		saveAll();
	}

}

