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
			gcb.setF1055Code(rscp.nextTbCode(DBConstants.PR_GCB));
			gcb.setTb1046IedByF1046Code(ied);
			gcb.setF1055Cbname(gseNd.attributeValue("cbName"));
			gcb.setF1055Cbid(gseNd.attributeValue("cbId"));
			gcb.setF1055Dataset(gseNd.attributeValue("dsName"));
			gcb.setF1055DsDesc(gseNd.attributeValue("dsDesc"));
			gcb.setF1055MacAddr(gseNd.attributeValue("mac"));
			gcb.setF1055Vlanid(gseNd.attributeValue("vlanID"));
			gcb.setF1055VlanPriority(gseNd.attributeValue("priority"));
			gcb.setF1055Appid(gseNd.attributeValue("appID"));
			
			List<Tb1061PoutEntity> pouts = new ArrayList<>();
			gcb.setTb1061PoutsByF1055Code(pouts);
			parsePOuts(gseNd, gcb.getF1055Code(), pouts);
		}
		saveItems();
	}

}
