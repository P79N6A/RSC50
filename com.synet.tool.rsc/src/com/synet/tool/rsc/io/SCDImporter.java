/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.das.FcdaDAO;
import com.shrcn.business.scl.das.IEDDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.io.parser.IIedParser;
import com.synet.tool.rsc.io.parser.RcbParser;
import com.synet.tool.rsc.io.parser.GooseParser;
import com.synet.tool.rsc.io.parser.SmvParser;
import com.synet.tool.rsc.io.scd.IedInfoDao;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.util.ProjectFileManager;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-14
 */
public class SCDImporter implements IImporter {

	private String scdPath;
	private RSCProperties rscp = RSCProperties.getInstance();
	private ProjectFileManager prjFileMgr = ProjectFileManager.getInstance();
	private BeanDaoService beanDao = BeanDaoImpl.getInstance();
	
	public SCDImporter(String scdPath) {
		this.scdPath = scdPath;
		FcdaDAO.getInstance().clear();
	}

	@Override
	public void execute() {
		XMLDBHelper.loadDocument(Constants.DEFAULT_SCD_DOC_NAME, scdPath);
		prjFileMgr.renameScd(Constants.CURRENT_PRJ_NAME, scdPath);
		List<Element> iedNds = IEDDAO.getAllIEDWithCRC();
		if (iedNds == null || iedNds.size() < 1) {
			return;
		}
		for (Element iedNd : iedNds) {
			Tb1046IedEntity ied = new Tb1046IedEntity();
			String iedName = iedNd.attributeValue("name");
			ied.setF1046Name(iedName);
			ied.setF1046Desc(iedNd.attributeValue("desc"));
			ied.setF1046Model(iedNd.attributeValue("type"));
			ied.setF1046Manufacturor(iedNd.attributeValue("manufacturer"));
			ied.setF1046ConfigVersion(iedNd.attributeValue("configVersion"));
			String vtcrc = iedNd.attributeValue("crc");
			ied.setF1046Crc(vtcrc);
			// code
			String iedCode = rscp.nextTbCode(DBConstants.PR_IED);
			ied.setF1046Code(iedCode);
			// A/B
			int aOrb = iedName.endsWith("B") ? 2 : 1;
			ied.setF1046AorB(aOrb);
			ied.setF1046IsVirtual(0);
			beanDao.insert(ied);
			
			Tb1026StringdataEntity strData = new Tb1026StringdataEntity();
			strData.setF1026Code(rscp.nextTbCode(DBConstants.PR_String));
			strData.setF1026Desc(vtcrc);
			strData.setParentCode(iedCode);
			beanDao.insert(strData);
			
			Map<String, IIedParser> pmap = new HashMap<String, IIedParser>();
			pmap.put("goose", new GooseParser(ied));
			pmap.put("smv", new SmvParser(ied));
			pmap.put("rcb", new RcbParser(ied));
			for (IIedParser parser : pmap.values()) {
				parser.parse();
			}
			// 根据解析结果修改类型
			if (pmap.get("rcb").getItems().size() > 0) {
				String ldXpath = SCL.getLDXPath(iedName, "PROT");
				int type = XMLDBHelper.existsNode(ldXpath) ?
						DBConstants.IED_PROT : DBConstants.IED_MONI;
				ied.setF1046Type(type);
				Tb1070MmsserverEntity mmsServer = new Tb1070MmsserverEntity();
				mmsServer.setF1070Code(rscp.nextTbCode(DBConstants.PR_MMSSvr));
				mmsServer.setTb1046IedByF1046Code(ied);
				String[] ips = IedInfoDao.getIPs(iedName);
				if (ips.length > 0) {
					mmsServer.setF1070IpA(ips[0]);
					if (ips.length > 1) {
						mmsServer.setF1070IpB(ips[1]);
					}
				} else {
					mmsServer.setF1070IpA("");
				}
				beanDao.insert(mmsServer);
			} else {
				if (pmap.get("smv").getItems().size() > 0) {
					ied.setF1046Type(DBConstants.IED_MU);
				} else {
					ied.setF1046Type(DBConstants.IED_TERM);
				}
			}
			beanDao.update(ied);
		}
	}

}

