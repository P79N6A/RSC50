/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.ui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-25
 */
/**
 * $Log: WizardContext.java,v $
 * Revision 1.1  2013/03/29 09:36:46  cchun
 * Add:创建
 *
 * Revision 1.5  2011/12/08 05:35:07  cchun
 * Update:去掉不需要的常量
 *
 * Revision 1.4  2011/10/17 07:36:04  cchun
 * Update:添加一二次关联环境变量
 *
 * Revision 1.3  2011/08/03 09:01:27  cchun
 * Update:增加ICD路径
 *
 * Revision 1.2  2011/08/01 08:19:57  cchun
 * Update:添加注释
 *
 * Revision 1.1  2011/07/27 07:34:39  cchun
 * Add:向导基础类
 *
 */
public class WizardContext {
	
	public static final String ICD 			= "icd file path"; 				// String 替换时选中的ICD文件路径
	public static final String IEDNAME 		= "selected ied name"; 			// String 替换时选中的IED
	public static final String INNERCHECK	= "innerCheck";					// boolean 模型检查是否通过
	public static final String IEDS			= "selected ied names";			// List<String> 用户选中待替换的IED
	public static final String ROOT 		= "selected icd root element";	// Element ICD内容根结点
	public static final String HASDIFFS		= "has differences";			// boolean 是否存在差异
	public static final String DIFFS		= "differences";				// List<Difference> 差异详细信息
	public static final String HASCONFLICTS	= "has conflicts";				// boolean 是否存在模板冲突
	public static final String CONFLICTS	= "conflicts";					// List<String> 模板冲突id属性集合
	public static final String HASREPLACED	= "has replaced";				// boolean 是否已经完成替换
	public static final String KEEP_INFO	= "keep info";					// int 需要保留的信息（信号关联、通信参数、控制块配置、描述）
	public static final String NEEDS_COMPARE	= "needs compare";				// boolean 是否已经完成替换
	
	
	// 由于IED的lnType属性可能因添加前缀而修改，故一二次关联需在替换之前和之后分别执行一遍
//	public static final String LNODES_INFO		= "lnodes warning infos";	// 一二次关联警告
//	public static final String LNODES_LNTYPE	= "lnodes lntype updated";	// 一二次关联待修改的类型信息
//	public static final String LNODES_RELEASE	= "lnodes deleted";			// 一二次关联待解除的信息

	private Map<String, Object> values = new HashMap<String, Object>();
	
	public WizardContext() {
	}
	
	public void putValue(String property, Object value) {
		values.put(property, value);
	}
	
	public Object getValue(String property) {
		return values.get(property);
	}
	
	public boolean getBoolValue(String key) {
		Object v = getValue(key);
		if (v != null) {
			return ((Boolean)v).booleanValue();
		}
		return false;
	}
	
	public int getIntegerValue(String key) {
		Object v = getValue(key);
		if (v != null) {
			return (int)v;
		}
		return 0;
	}
}
