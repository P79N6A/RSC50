/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.io.scd.IedInfoDao;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.util.F1011_NO;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class SmvParser extends IedParserBase<Tb1056SvcbEntity> {

	public SmvParser(Tb1046IedEntity ied) {
		super(ied);
	}

	@Override
	public void parse() {
		List<Element> smvNds = IedInfoDao.getSmvConfigPub(iedName);
		for (Element cbNd : smvNds) {
			Tb1056SvcbEntity smv = new Tb1056SvcbEntity();
			items.add(smv);
			String cbCode = rscp.nextTbCode(DBConstants.PR_GCB);
			smv.setCbCode(cbCode);
			smv.setTb1046IedByF1046Code(ied);
			smv.setCbName(cbNd.attributeValue("cbName"));
			smv.setCbId(cbNd.attributeValue("cbId"));
			smv.setDataset(cbNd.attributeValue("dsName"));
			smv.setDsDesc(cbNd.attributeValue("dsDesc"));
			smv.setMacAddr(cbNd.attributeValue("mac"));
			smv.setVlanid(cbNd.attributeValue("vlanID"));
			smv.setVlanPriority(cbNd.attributeValue("priority"));
			smv.setAppid(cbNd.attributeValue("appID"));
			// 状态点
			Tb1016StatedataEntity st = IedParserBase.createStatedata(cbNd.attributeValue("cbRef")+"状态", cbCode, F1011_NO.IED_WRN_SV.getId());
			st.setTb1046IedByF1046Code(ied);
			beanDao.insert(st);
			
			List<Tb1061PoutEntity> pouts = new ArrayList<>();
			smv.setTb1061PoutsByCbCode(pouts);
			parsePOuts(cbNd, smv.getCbCode(), pouts);
		}
		saveAll();
	}

}
