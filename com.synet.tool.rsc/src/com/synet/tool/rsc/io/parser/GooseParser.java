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
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.util.F1011_NO;

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
		for (Element cbNd : gseNds) {
			Tb1055GcbEntity gcb = new Tb1055GcbEntity();
			items.add(gcb);
			String cbCode = rscp.nextTbCode(DBConstants.PR_GCB);
			gcb.setCbCode(cbCode);
			gcb.setTb1046IedByF1046Code(ied);
			gcb.setCbName(cbNd.attributeValue("cbName"));
			gcb.setCbId(cbNd.attributeValue("cbId"));
			gcb.setDataset(cbNd.attributeValue("dsName"));
			gcb.setDsDesc(cbNd.attributeValue("dsDesc"));
			gcb.setMacAddr(cbNd.attributeValue("mac"));
			gcb.setVlanid(cbNd.attributeValue("vlanID"));
			gcb.setVlanPriority(cbNd.attributeValue("priority"));
			gcb.setAppid(cbNd.attributeValue("appID"));
			// 状态点
			Tb1016StatedataEntity st = createStatedata(cbNd.attributeValue("cbRef")+"状态", cbCode, F1011_NO.IED_WRN_GOOSE.getId());
			st.setTb1046IedByF1046Code(ied);
			sts.add(st);
			
			List<Tb1061PoutEntity> pouts = new ArrayList<>();
			gcb.setTb1061PoutsByCbCode(pouts);
			parsePOuts(cbNd, gcb.getCbCode(), pouts);
		}
		saveAll();
	}

}
