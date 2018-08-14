/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
/**
 * 
 */
package com.shrcn.business.scl.das;

import com.shrcn.business.scl.model.FCDAEntry;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

/**
 * 
 * @author zhouhuiming(mailto:zhm.3119@shrcn.com)
 * @version 1.0, 2010-5-13
 */
/**
 * $Log: FCDAMgrDao.java,v $
 * Revision 1.1  2013/03/29 09:36:21  cchun
 * Add:创建
 *
 * Revision 1.12  2012/03/09 07:35:49  cchun
 * Update:规范prefix和daName属性用法
 *
 * Revision 1.11  2011/09/13 08:37:02  cchun
 * Refactor:修改getIfString()参数
 *
 * Revision 1.10  2011/09/13 08:04:14  cchun
 * Update:修复fcda删除时关联删除，去掉无用的方法
 *
 * Revision 1.9  2010/12/29 06:42:48  cchun
 * Update:修改参数
 *
 * Revision 1.8  2010/12/20 02:34:50  cchun
 * Update:统一上、下移动处理逻辑
 *
 * Revision 1.7  2010/12/07 09:37:43  cchun
 * Update:去掉访问点过滤
 *
 * Revision 1.6  2010/11/08 08:32:22  cchun
 * Update: 清除没有用到的代码
 *
 * Revision 1.5  2010/11/08 07:13:18  cchun
 * Update:清理引用,优化查询
 *
 * Revision 1.4  2010/11/04 08:31:37  cchun
 * Fix Bug:修改DataTypeTemplateDao为静态调用，避免数据不同步错误
 *
 * Revision 1.3  2010/09/07 09:59:32  cchun
 * Update:更换过时接口
 *
 * Revision 1.2  2010/09/03 02:37:59  cchun
 * Update:修改数据项描述时按doName进行修改
 *
 * Revision 1.1  2010/06/18 09:49:20  cchun
 * Add:添加描述导出、导入功能
 *
 */
public class FCDAMgrDao extends IEDEditDAO {

	/**
	 * @param iedName
	 * @param apName
	 * @param ldInst
	 * @param lnName
	 */
	public FCDAMgrDao(String iedName, String ldInst, String lnName) {
		super(iedName, ldInst, lnName);
	}
	
	/**
	 * 删除FCDA
	 * @param 
	 * @return 
	 */
	public void deleteFCDA(String datName, FCDAEntry fcdaEntry) {
		String ldInst = fcdaEntry.getLdInst();
		String prefix = fcdaEntry.getPrefix();
		String lnClass = fcdaEntry.getLnClass();
		String lnInst = fcdaEntry.getLnInst();
		String doName = fcdaEntry.getDoName();
		String ifExp = getIfString(ldInst, prefix, lnClass, lnInst, doName);
		String fcdaXpath = getFCDAXpath(datName) + ifExp;
		String refXpath = "/scl:SCL/scl:IED/scl:AccessPoint/scl:Server/scl:LDevice/*/scl:Inputs/scl:ExtRef" +
				"[@iedName='" + iedName + "']" + ifExp;
		XMLDBHelper.removeNodes(fcdaXpath);
		XMLDBHelper.removeNodes(refXpath);
	}
	
	/**
	 * 获取指定数据集FCDA xpath
	 * @param datName
	 * @return
	 */
	public String getFCDAXpath(String datName) {
		return iedXPath + "/scl:AccessPoint/scl:Server/scl:LDevice[@inst='" + ldInst //$NON-NLS-1$
			+ "']/*/scl:DataSet[@name='" + datName + "']/scl:FCDA";
	}

	/**
	 * FCDA上移
	 * @param name
	 * @param selectedRow
	 */
	public synchronized void moveUp(String datName, int selectedRow) {
		String fcdaXpath = getFCDAXpath(datName);
		XMLDBHelper.moveTo(fcdaXpath, selectedRow, selectedRow - 1);
	}
	
	/**
	 * FCDA下移
	 * 
	 * @param name
	 * @param selectedRow
	 */
	public synchronized void moveDown(String datName, int selectedRow) {
		String fcdaXpath = getFCDAXpath(datName);
		XMLDBHelper.moveTo(fcdaXpath, selectedRow, selectedRow + 1);
	}
	
	/**
	 * 返回满足的条件
	 * 
	 * @param ldInst
	 *            FCDA的ldInstTb字段
	 * @param prefix
	 *            FCDA的Prefix字段
	 * @param lnClass
	 *            FCDA的lnClass字段
	 * @param lnInst
	 *            FCDA的lnInst字段
	 * @param doName
	 *            FCDA的doName字段
	 * @return
	 */
	public static String getIfString(String ldInst, String prefix, String lnClass,
			String lnInst, String doName) {
		StringBuilder temp = new StringBuilder();
		if (!StringUtil.isEmpty(ldInst)) {
			temp.append("[@ldInst='" + ldInst + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		temp.append(SCL.getLNAtts(prefix, lnClass, lnInst));
		if (!StringUtil.isEmpty(doName)) {
			temp.append("[@doName='" + doName + "']"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return temp.toString().trim();
	}
}
