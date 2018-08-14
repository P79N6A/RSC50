/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import org.dom4j.Element;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2009-4-20
 */
/*
 * 修改历史
 * $Log: ISCLDAOFactory.java,v $
 * Revision 1.1  2013/03/29 09:36:23  cchun
 * Add:创建
 *
 * Revision 1.23  2011/06/21 09:12:20  cchun
 * Update:去掉createCIDDAO()
 *
 * Revision 1.22  2010/12/21 07:28:30  cchun
 * Update:去掉无用方法
 *
 * Revision 1.21  2010/12/07 09:37:46  cchun
 * Update:去掉访问点过滤
 *
 * Revision 1.20  2010/05/26 08:10:32  cchun
 * Update:修改构造函数参数
 *
 * Revision 1.19  2009/12/18 08:03:58  cchun
 * Update:还原ReportDAO设计
 *
 * Revision 1.18  2009/12/10 07:50:21  lj6061
 * 修正由于数据集增加，删除，重命名，引发的关联改变
 *
 * Revision 1.17  2009/06/03 00:46:03  lj6061
 * 控制对Load方法的调用测试
 *
 * Revision 1.16  2009/05/26 05:34:52  hqh
 * 添加导入ied DAO
 *
 * Revision 1.15  2009/05/25 08:23:37  cchun
 * 添加采样值关联拖拽
 *
 * Revision 1.14  2009/05/23 10:18:37  cchun
 * Update:重构IEDDAO
 *
 * Revision 1.13  2009/05/08 12:07:05  cchun
 * Update:完善外部、内部信号视图
 *
 * Revision 1.12  2009/04/28 07:50:52  lj6061
 * 添加GSE功能
 *
 * Revision 1.11  2009/04/27 09:45:57  lj6061
 * 添加GSE控制页面
 *
 * Revision 1.10  2009/04/27 03:45:35  cchun
 * Update:添加goose数据方位类
 *
 * Revision 1.9  2009/04/24 02:54:32  hqh
 * 工厂添加创建通讯
 *
 * Revision 1.8  2009/04/22 09:16:04  wyh
 * 添加历史DAO
 *
 * Revision 1.7  2009/04/22 08:17:08  cchun
 * Update:修改ReportControl按LN过滤
 *
 * Revision 1.6  2009/04/22 02:23:11  cchun
 * Update:更新ReportControlDAO
 *
 * Revision 1.4  2009/04/21 05:57:03  lj6061
 * Update:为LNDAO添加属性
 *
 * Revision 1.3  2009/04/20 11:00:48  cchun
 * Update:实现ReportControlDAO
 *
 * Revision 1.2  2009/04/20 09:03:34  cchun
 * Update:添加LNDAO，ReportControlDAO
 *
 * Revision 1.1  2009/04/20 03:59:49  cchun
 * Add:添加数据对象工厂
 *
 */
public interface ISCLDAOFactory {
	
	/**
	 * 创建IEDDAO
	 * @return IEDDAO
	 */
	public IEDDAO createIEDDAO(String iedName, boolean needLoad);
	
	/**
	 * 创建LNDAO
	 * @return LNDAO
	 */
	public LNDAO createLNDAO(String iedName, String ldInst);
	
	/**
	 * 创建ReportControlDAO
	 * @return ReportControlDAO
	 */
	public ReportControlDAO createReportControlDAO(String iedName, String ldInst, Element lnData);
	
	/**
	 * 创建goose关联DAO
	 * @param iedName 装置xpath
	 * @param apName 访问点名
	 * @param ldInst LD实例名
	 * @param lnData LN节点数据
	 * @return
	 */
	public GOOSEInputDAO createGOOSEInputDAO(String iedName, String ldInst, Element lnData);
	
	/**
	 * 创建ControlDAO
	 */
	public ControlDAO createControlDAO(String iedName, String ldInst);
	
	/**
	 * 创建历史信息DAO
	 * @return
	 */
	public HistoryDAO createHistoryDAO();
	
	public CommunicationDAO createCommunicationDAO(String name);
	
	/** 创建GSE控制操作对象*/
	public GSEDAO createGSEDAO(String iedName, String ldInst);
	public LogDAO createLogDAO(String iedName, String ldInst);
	
	public UserCfgDAO createUserCfgDAO(String iedName);
}
