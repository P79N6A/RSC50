/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.shrcn.found.common.Constants;
import com.shrcn.found.file.xml.DOM4JNodeHelper;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-10-20
 */
public class DOIInstChecker {
	
	Map<String, Object[]> dodaMap;
	private List<Problem> problems;
	private String iedName;
	private String lnName;
	private String lnType;
	
	private String errRefs = "";
	
	public DOIInstChecker(String iedName, String lnName, String lnType, Map<String, Object[]> dodaMap, List<Problem> problems) {
		this.iedName = iedName;
		this.lnName = lnName;
		this.lnType = lnType;
		this.dodaMap = dodaMap;
		this.problems = problems;
	}
	
	private void addError(String subType, String ref, String desc) {
		problems.add(Problem.createError(iedName, subType, ref, desc));
	}
	
	private void addWarning(String subType, String ref, String desc) {
		problems.add(Problem.createWarning(iedName, subType, ref, desc));
	}
	
	public void checkSubRefs(String parentName, Element parentNode) {
//		List<Element> children = parentNode.elements();
		List<Element> children = DOM4JNodeHelper.selectNodes(parentNode, "./*[name()='SDI' or name()='DAI' or name()='DOI']");
		if (children.size() == 0) {
			if (!"".equals(parentName)) {
				Object[] subInfo = dodaMap.get(parentName);
				checkInstAndType(parentName, subInfo);
			}
		} else {
			int lastTplIndex = -1;
			int currIndex = -1;
			for (Element child : children) {
				String nodeName = child.getName();
				String name = child.attributeValue("name");
				String subRef = (parentName.length()>0?(parentName + Constants.DOT):"") + name;
				currIndex++;
				Object[] subInfo = dodaMap.get(subRef);
				if (nodeName.equals("DAI")) {
					checkInstAndType(subRef, subInfo);
				} else {
					checkSubRefs(subRef, child);
				}
				// 检查实例化顺序
				if (subInfo == null || subInfo.length < 2)
					continue;
				int currTplIndex = (int) subInfo[1];
				if (currTplIndex <= lastTplIndex || currIndex > currTplIndex) {
					errRefs += lnName + "." + subRef + "\n";
				} else {
					lastTplIndex = currTplIndex;
				}
			}
		}
	}
	
	private void checkInstAndType(String subRef, Object[] subInfo) {
		if (subInfo == null) {
			addError("LN实例化", lnName + "." + subRef, "该元素未定义，类型 " + lnType);
		} else {
			String bType = (String) subInfo[0];
			if ("Struct".equals(bType)) {
				addWarning("LN实例化", lnName + "." + subRef, "结构体不应作为<DAI>，类型 " + lnType);
			}
		}
	}

	public String getErrRefs() {
		return errRefs;
	}
	
}

