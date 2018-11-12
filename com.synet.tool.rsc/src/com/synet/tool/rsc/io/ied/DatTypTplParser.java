package com.synet.tool.rsc.io.ied;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.ui.view.Problem;
import com.synet.tool.rsc.DBConstants;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-22
 */
public class DatTypTplParser {

	private Element dataTempEle;
	private List<String> lostTypes = new ArrayList<>();
	private Map<String, Element> typeMap = new HashMap<String, Element>();	// 索引
	private Map<String, Map<String, Object[]>> lnTypeMap = new HashMap<String, Map<String, Object[]>>();
	private List<Problem> problems;
	
	public DatTypTplParser(Element dataTempEle, List<Problem> problems) {
		this.dataTempEle = dataTempEle;
		this.problems = problems;
		init();
	}
	
	private void addError(String iedName, String subType, String ref, String desc) {
		problems.add(Problem.createError(iedName, subType, ref, desc));
	}
	
	public void init() {
		createIndex();
		parse();
	}

	private void createIndex() {
		List<Element> types = dataTempEle.elements();
		for (Element type : types) {
			String id = type.attributeValue("id");
			if ("LNodeType".equals(type.getName())) {
				typeMap.put(id, type);
			} else {
				typeMap.put(id, type);
			}
		}
	}

	private void parse() {
		List<Element> lntypes = dataTempEle.elements("LNodeType");
		for (Element lnodeType : lntypes) {
			String id = lnodeType.attributeValue("id");
			lnTypeMap.put(id, getLnTypeSubRefType(lnodeType));
		}
	}
	
	public Map<String, Object[]> getLnTypeSubRefType(String lnType) {
		return getLnTypeSubRefType(typeMap.get(lnType));
	}
	
	/**
	 * 遍历指定LNodeType下所有DO,DA，并返回所有DA的参引。
	 * @param id
	 * @return
	 */
	public Map<String, Object[]> getLnTypeSubRefType(Element lnodeType) {
		Map<String, Object[]> subRefs = new HashMap<String, Object[]>();
		List<?> doList = lnodeType.elements("DO");
		int index = 0;
		for (Object obj : doList) {
			Element doNd = (Element) obj;
			String name = doNd.attributeValue("name");
			String type = doNd.attributeValue("type");
			findSubRefTypes(name, type, subRefs, index);
			index++;
		}
		return subRefs;
	}

	/**
	 * 得到DO下所有SDO,DA(Struct)的参引
	 * @param id
	 * @return
	 */
	private void findSubRefTypes(String parentName, String id, Map<String, Object[]> subRefs, int idx) {
		Element subType = typeMap.get(id);
		if (subType == null) {
			if (!lostTypes.contains(id)) {
				addError(parentName, "数据模板", "", "找不到 " + id);
				lostTypes.add(id);
			}
			return;
		}
		boolean isDOType = "DOType".equals(subType.getName());
//		if ("DOType".equals(subType.getName())) {
//			subRefs.put(parentName, new Object[] { subType.attributeValue("cdc"), idx} );
//		}
		List<?> children = subType.elements();
		int index = 0;
		for (Object obj : children) {
			Element child = (Element) obj;
			String ndName = child.getName();
			String name = child.attributeValue("name"); 
			String type = child.attributeValue("type");
			String bType = child.attributeValue("bType");
			String fc = child.attributeValue("fc");
			if (isDOType && (!"SP".equals(fc) && !"SG".equals(fc))) {
				continue;
			}
			String parentRef = parentName + Constants.DOT + name;
			boolean hasSubs = "DO".equals(ndName) || "SDO".equals(ndName);
			if (!hasSubs) {
				if ("DA".equals(ndName) || "BDA".equals(ndName)) {
					hasSubs = "Struct".equals(bType);
//					if (hasSubs) { // 记录Struct类型
//						subRefs.put(parentRef, new Object[] {"Struct", index});
//					}
				}
			}
			if (hasSubs) {
				findSubRefTypes(parentRef, type, subRefs, index);
			} else {
				if ("Enum".equals(bType)) {
					subRefs.put(parentRef, new Object[] {bType + Constants.DOT + type, index});
					if (typeMap.get(type) == null && !lostTypes.contains(type)) {
						if (StringUtil.isEmpty(type)) {
							String ref = child.asXML();
							if (!lostTypes.contains(ref)) {
								addError(parentName, "数据模板", ref, "无type属性");
								lostTypes.add(ref);
							}
						} else {
							addError(parentName, "数据模板", "", "找不到 " + type);
							lostTypes.add(type);
						}
					}
				} else {
					subRefs.put(parentRef, new Object[] {bType, index});
				}
			}
			index++;
		}
	}

	public Map<String, Map<String, Object[]>> getLnTypeMap() {
		return lnTypeMap;
	}
	
	/**
	 * 得到fcda的数据类型bType
	 * @param fcdaEl
	 * @return
	 */
	public int getBType(Element ldEl, Element fcdaEl) {
		String fLnXpath = "." + SCL.getFcdaLNXPath(fcdaEl) + "/@lnType";
		String lnType = DOM4JNodeHelper.getAttributeValue(ldEl, fLnXpath);
		Map<String, Object[]> dodaTypeMap = lnTypeMap.get(lnType);
		if (dodaTypeMap == null) {
			return DBConstants.DAT_TYP_FLOAT;
		}
		String doName = fcdaEl.attributeValue(SCL.FCDA_DONAME);
		String daName = fcdaEl.attributeValue(SCL.FCDA_DANAME);
		boolean isSV = StringUtil.isEmpty(daName);
		Object[] dodatype = null;
		if (isSV) {
			for (String ref : dodaTypeMap.keySet()) {
				String bType = (String) dodaTypeMap.get(ref)[0];
				if (ref.startsWith(doName) && !"Struct".equals(bType)) {
					dodatype = dodaTypeMap.get(ref);
					break;
				}
			}
			if (dodatype == null) {
				System.out.println("未找到类型:" + lnType + "/" + doName);
				return DBConstants.DAT_TYP_FLOAT;
			}
		} else {
			String outDodaName = (doName + "." + daName);
			dodatype = dodaTypeMap.get(outDodaName);
			if (dodatype == null || dodatype.length < 1) {
				return DBConstants.DAT_TYP_FLOAT;
			}
		}
		String bType = (dodatype==null) ? "" : (String) dodatype[0];
//		return "FLOAT32".equalsIgnoreCase(bType) ? DBConstants.DAT_TYP_FLOAT : DBConstants.DAT_TYP_INT;
		if (bType.startsWith("FLOAT")) {
			return DBConstants.DAT_TYP_FLOAT;
		} else if (bType.startsWith("INT") || bType.toUpperCase().startsWith("BOOLEAN")) {
			return DBConstants.DAT_TYP_INT;
		} else if (bType.startsWith("Unicode")) {
			return DBConstants.DAT_TYP_STR;
		} else {
			return DBConstants.DAT_TYP_FLOAT;
		}
	}
}

