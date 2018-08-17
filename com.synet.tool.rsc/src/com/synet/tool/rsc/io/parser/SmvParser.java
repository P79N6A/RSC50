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
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;

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
			smv.setF1056Code(rscp.nextTbCode(DBConstants.PR_GCB));
			smv.setTb1046IedByF1046Code(ied);
			smv.setF1056CbName(cbNd.attributeValue("cbName"));
			smv.setF1056Cbid(cbNd.attributeValue("cbId"));
			smv.setF1056Dataset(cbNd.attributeValue("dsName"));
			smv.setF1056DsDesc(cbNd.attributeValue("dsDesc"));
			smv.setF1056MacAddr(cbNd.attributeValue("mac"));
			smv.setF1056Vlanid(cbNd.attributeValue("vlanID"));
			smv.setF1056VlanPriority(cbNd.attributeValue("priority"));
			smv.setF1056Appid(cbNd.attributeValue("appID"));
			
			List<Tb1061PoutEntity> pouts = new ArrayList<>();
			smv.setTb1061PoutsByF1056Code(pouts);
			parsePOuts(cbNd, smv.getF1056Code(), pouts);
		}
		saveAll();
	}

}
