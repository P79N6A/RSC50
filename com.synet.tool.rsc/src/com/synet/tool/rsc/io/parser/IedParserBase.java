/**
 * Copyright (c) 2018-2019 上海泗业技术咨询有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.synet.tool.rsc.io.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.das.FcdaDAO;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.tool.found.das.BeanDaoService;
import com.shrcn.tool.found.das.impl.BeanDaoImpl;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.RSCProperties;
import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.util.F1011_NO;

 /**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2018-8-15
 */
public abstract class IedParserBase<T> implements IIedParser {

	protected Tb1046IedEntity ied;
	protected String iedName;
	protected String iedXpath;
	protected List<T> items = new ArrayList<>();
	protected List<Tb1006AnalogdataEntity> agls = new ArrayList<>();
	protected List<Tb1016StatedataEntity> sts = new ArrayList<>();
	protected List<Tb1026StringdataEntity> strs = new ArrayList<>();
	// key: lnType, value: (key:dodaName, value:bType/cdc)
	protected Map<String, Map<String, Object[]>> lnTypeMap = new HashMap<String, Map<String, Object[]>>();
	
	
	protected static RSCProperties rscp = RSCProperties.getInstance();
	protected FcdaDAO fcdaDAO = FcdaDAO.getInstance();
	protected BeanDaoService beanDao = BeanDaoImpl.getInstance();

	public IedParserBase(Tb1046IedEntity ied) {
		if (ied != null) {
			this.ied = ied;
			this.iedName = ied.getF1046Name();
			this.iedXpath = SCL.getIEDXPath(iedName);
		}
	}

	public List<T> getItems() {
		return items;
	}
	
	private void saveItems() {
		beanDao.insertBatch(items);
	}

	private void saveData() {
		beanDao.insertBatch(agls);
		beanDao.insertBatch(sts);
		beanDao.insertBatch(strs);
	}
	
	protected void saveAll() {
		saveItems();
		saveData();
	}

	protected void parsePOuts(Element cbNd, String cbCode, List<Tb1061PoutEntity> pouts) {
		String datSet = cbNd.attributeValue("dsName");
		List<Element> fcdaEls = cbNd.elements("fcda");
		for (Element fcdaEl : fcdaEls) {
			Tb1061PoutEntity pout = new Tb1061PoutEntity();
			pouts.add(pout);
			pout.setF1061Code(rscp.nextTbCode(DBConstants.PR_POUT));
			pout.setTb1046IedByF1046Code(ied);
			pout.setCbCode(cbCode);
			pout.setF1061RefAddr(fcdaEl.attributeValue("ref"));
			pout.setF1061Index(Integer.parseInt(fcdaEl.attributeValue("idx")));
			String fcdaDesc = fcdaDAO.getFCDADesc(iedName, fcdaEl);
			pout.setF1061Desc(fcdaDesc);
			String fc = fcdaEl.attributeValue("fc");
			String lnName = fcdaEl.attributeValue("lnClass");
			String doName = fcdaEl.attributeValue("doName");
			F1011_NO type = F1011_NO.getType(datSet, lnName, doName, fcdaDesc);
			pout.setF1011No(type.getId());
			if ("ST".equals(fc)) {
				Tb1016StatedataEntity statedata = addStatedata(fcdaEl, fcdaDesc, type.getId());
				pout.setDataCode(statedata.getF1016Code());
			} else {
				Tb1006AnalogdataEntity algdata = addAlgdata(fcdaEl, fcdaDesc, type.getId());
				String algcode = algdata.getF1006Code();
				pout.setDataCode(algcode);
			}
		}
	}
	
	/**
	 * 添加状态量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	protected Tb1016StatedataEntity addStatedata(Element fcdaEl, String fcdaDesc, int f1011No) {
		Tb1016StatedataEntity statedata = createStatedata(fcdaDesc, fcdaEl.attributeValue("ref"),
				ied.getF1046Code(), f1011No);
		sts.add(statedata);
		return statedata;
	}
	
	public static Tb1016StatedataEntity createStatedata(String desc, String parentCode, int f1011No) {
		return createStatedata(desc, "", parentCode, f1011No);
	}
	
	/**
	 * 创建状态量数据对象
	 * @param desc
	 * @param parentCode
	 * @param f1011No
	 * @return
	 */
	private static Tb1016StatedataEntity createStatedata(String desc, String ref, String parentCode, int f1011No) {
		String dataCode = rscp.nextTbCode(DBConstants.PR_State);
		Tb1016StatedataEntity stdata = new Tb1016StatedataEntity();
		stdata.setF1016Code(dataCode);
		stdata.setF1016Desc(desc);
		stdata.setF1016AddRef(ref);
		stdata.setF1016Safelevel(0);
		stdata.setParentCode(parentCode);
		stdata.setF1011No(f1011No);
		return stdata;
	}
	
	/**
	 * 添加模拟量数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	protected Tb1006AnalogdataEntity addAlgdata(Element fcdaEl, String fcdaDesc, int f1011No) {
		String dataCode = rscp.nextTbCode(DBConstants.PR_Analog);
		Tb1006AnalogdataEntity algdata = new Tb1006AnalogdataEntity();
		algdata.setF1006Code(dataCode);
		algdata.setF1006Desc(fcdaDesc);
		algdata.setF1006AddRef(fcdaEl.attributeValue("ref"));
		algdata.setF1006Safelevel(0);
		algdata.setParentCode(ied.getF1046Code());
		algdata.setF1011No(f1011No);
		agls.add(algdata);
		return algdata;
	}
	
	/**
	 * 添加字符串数据
	 * @param fcdaEl
	 * @param fcdaDesc
	 * @param f1011No
	 * @return
	 */
	protected String addStringdata(Element fcdaEl, String fcdaDesc, int f1011No) {
		String dataCode = rscp.nextTbCode(DBConstants.PR_String);
		Tb1026StringdataEntity strdata = new Tb1026StringdataEntity();
		strdata.setF1026Code(dataCode);
		strdata.setF1026Desc(fcdaDesc);
		strdata.setF1026Safelevel(0);
		strdata.setParentCode(ied.getF1046Code());
		strdata.setF1011No(f1011No);
		strs.add(strdata);
		return dataCode;
	}

	/**
	 * 得到fcda的数据类型bType
	 * @param fcdaEl
	 * @return
	 */
	protected int getBType(Element fcdaEl) {
		String ldXpath = SCL.getLDXPath(iedName, fcdaEl.attributeValue("ldInst"));
		String fLnXpath = ldXpath + SCL.getFcdaLNXPath(fcdaEl) + "/@lnType";
		String lnType = XMLDBHelper.getAttributeValue(fLnXpath);
		Map<String, Object[]> dodaTypeMap = lnTypeMap.get(lnType);
		if (dodaTypeMap == null) {
			return DBConstants.DAT_TYP_FLOAT;
		}
		String doName = fcdaEl.attributeValue(SCL.FCDA_DONAME);
		String daName = fcdaEl.attributeValue(SCL.FCDA_DANAME);
		boolean isSV = StringUtil.isEmpty(daName);
		String outDodaName = isSV ? doName : (doName + "." + daName);
		Object[] dodatype = dodaTypeMap.get(outDodaName);
		if (dodatype == null || dodatype.length < 1) {
			return DBConstants.DAT_TYP_FLOAT;
		}
		String bType = (String) dodatype[0];
		return "FLOAT32".equalsIgnoreCase(bType) ? DBConstants.DAT_TYP_FLOAT : DBConstants.DAT_TYP_INT;
	}
}

