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
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public class GooseParser extends IedParserBase<Tb1055GcbEntity> {

	public GooseParser(Tb1046IedEntity ied) {
		super(ied);
	}

	@Override
	public void parse() {
		List<Element> gseNds = IedInfoDao.getGooseConfigPub(iedName);
		for (Element gseNd : gseNds) {
			Tb1055GcbEntity gcb = new Tb1055GcbEntity();
			items.add(gcb);
			gcb.setCbCode(rscp.nextTbCode(DBConstants.PR_GCB));
			gcb.setTb1046IedByF1046Code(ied);
			gcb.setCbName(gseNd.attributeValue("cbName"));
			gcb.setCbId(gseNd.attributeValue("cbId"));
			gcb.setDataset(gseNd.attributeValue("dsName"));
			gcb.setDsDesc(gseNd.attributeValue("dsDesc"));
			gcb.setMacAddr(gseNd.attributeValue("mac"));
			gcb.setVlanid(gseNd.attributeValue("vlanID"));
			gcb.setVlanPriority(gseNd.attributeValue("priority"));
			gcb.setAppid(gseNd.attributeValue("appID"));
			
			List<Tb1061PoutEntity> pouts = new ArrayList<>();
			gcb.setTb1061PoutsByCbCode(pouts);
			parsePOuts(gseNd, gcb.getCbCode(), pouts);
		}
		saveAll();
	}

}
