/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.common;

/**
 * 
 * @author 刘静(mailto:lj6061@shrcn.com)
 * @version 1.0, 2009-5-19
 */
/*
 * 修改历史
 * $Log: DefaultInfo.java,v $
 * Revision 1.1  2013/03/29 09:36:55  cchun
 * Add:创建
 *
 * Revision 1.15  2011/09/08 09:00:16  cchun
 * Update:调整格式
 *
 * Revision 1.14  2011/07/11 08:51:39  cchun
 * Refactor:清理不必要的常量定义
 *
 * Revision 1.13  2011/06/09 08:37:30  cchun
 * Update:去掉不必要的代码
 *
 * Revision 1.12  2011/01/07 02:19:33  cchun
 * Refactor:调整字符常量归属关系
 *
 * Revision 1.11  2010/11/02 07:02:33  cchun
 * Update:添加常量定义
 *
 * Revision 1.10  2010/08/10 08:01:49  cchun
 * Update:去掉不必要的常量
 *
 * Revision 1.9  2010/03/29 02:44:45  cchun
 * Update:提交
 *
 * Revision 1.8  2010/01/19 09:02:36  lj6061
 * add:统一国际化工程
 *
 * Revision 1.7  2009/10/22 09:17:01  cchun
 * Update:修改功能列表、关联IED图元默认名称
 *
 * Revision 1.6  2009/10/21 07:05:07  cchun
 * Update:添加功能列表常量
 *
 * Revision 1.5  2009/09/22 05:33:27  hqh
 * 添加数据类型变量
 *
 * Revision 1.4  2009/09/18 01:55:02  cchun
 * Update:母线默认名称改成中文
 *
 * Revision 1.3  2009/09/03 03:04:59  cchun
 * Update:增加母线间隔功能
 *
 * Revision 1.2  2009/08/28 07:41:52  cchun
 * Update:整理变量顺序，使之和菜单顺序一致
 *
 * Revision 1.1  2009/08/28 01:33:23  cchun
 * Refactor:重构包路径
 *
 * Revision 1.12  2009/08/18 09:40:38  cchun
 * Update:合并代码
 *
 * Revision 1.11.2.2  2009/08/10 06:20:33  lj6061
 * 导电设备下信息修改
 *
 * Revision 1.11.2.1  2009/08/10 06:07:54  lj6061
 * 导电设备下信息修改
 *
 * Revision 1.11  2009/07/16 07:18:30  lj6061
 * 修改为接口
 *
 * Revision 1.10  2009/07/16 06:33:36  lj6061
 * 修改常量数组
 *
 * Revision 1.9  2009/07/14 09:13:22  lj6061
 * 添加提示信息
 *
 * Revision 1.8  2009/06/15 09:40:03  lj6061
 * 更改代码中的对电压处理
 *
 * Revision 1.7  2009/05/31 10:58:59  lj6061
 * 添加复制粘贴操作
 *
 * Revision 1.6  2009/05/27 04:46:34  lj6061
 * 添加一次信息图片
 *
 * Revision 1.5  2009/05/26 06:17:03  lj6061
 * 工程不存在提示新建
 *
 * Revision 1.4  2009/05/23 11:29:46  lj6061
 * 添加节点菜单事件
 *
 * Revision 1.3  2009/05/22 09:19:03  lj6061
 * 修改一次节点
 *
 * Revision 1.2  2009/05/22 03:04:01  lj6061
 * 修改节点属性添加优先级
 *
 * Revision 1.1  2009/05/21 05:43:04  lj6061
 * 为树添加邮件插入事件
 *
 */
public interface DefaultInfo {
	
	/**
	 * 一次设备中没有发生关联LNode 的 iedName默认名字
	 */
	public static final String IED_NAME = "None"; //$NON-NLS-1$
	/**
	 * 一次设备中没有发生关联LNode 的 lnType默认值
	 */
	public static final String LNTYPE   = "null"; //$NON-NLS-1$
	/**
	 * 一次设备中没有发生关联LNode 的 ldInst默认值
	 */
	public static final String UNREL_LNODE_LDINST  = "";
	
	/**
	 * 一次设备中没有发生关联LNode 的 prefix默认值
	 */
	public static final String UNREL_LNODE_PREFIX  = "";
	
	//图元名称
	public static final String FUNLIST_NAME = Messages.getString("DefaultInfo.function"); //$NON-NLS-1$
	public static final String RELIED_NAME  = Messages.getString("DefaultInfo.ieds_association"); //$NON-NLS-1$
	// 新建变压器默认关联信息
	public static final String POWER_LN[]     = { "YPTR", "YEFN", "YLTC", "YPSH" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	public static final String POWER_PTW_NAME = Messages.getString("DefaultInfo.PTW_name"); //$NON-NLS-1$
	public static final String POWER_PTW_TYPE = "PTW"; //$NON-NLS-1$
	public static final String TERMINAL       = "T"; //$NON-NLS-1$
	public static final String CONNECTNODE    = "Point"; //$NON-NLS-1$
	// 变电站
	public static final String SUB_NAME       = Messages.getString("DefaultInfo.substation"); //$NON-NLS-1$
	// 电压等级
	public static final String VOLTAGELEVEL_NAME = Messages.getString("DefaultInfo.voltagelevel"); //$NON-NLS-1$
	// 功能
	public static final String FUNCTION_NAME     = Messages.getString("DefaultInfo.function"); //$NON-NLS-1$
	// 间隔
	public static final String BAY_NAME    = Messages.getString("DefaultInfo.bay"); //$NON-NLS-1$
	public static final String BUSBAR_NAME = Messages.getString("DefaultInfo.bus_bay"); //$NON-NLS-1$
	public static final String BUSBAR      = "Busbar"; //$NON-NLS-1$
	public static final String VIRTUAL_BAY = "VirtualBay"; //$NON-NLS-1$
	// 子设备
	public static final String SUBEQ_NAME   = Messages.getString("DefaultInfo.sub_equipment"); //$NON-NLS-1$
	public static final String SUBFUN_NAME  = Messages.getString("DefaultInfo.sub_function"); //$NON-NLS-1$
	public static final String CONNODE_NAME = Messages.getString("DefaultInfo.connectiveNode"); //$NON-NLS-1$
	public static final String LNNODE       = Messages.getString("DefaultInfo.lnode"); //$NON-NLS-1$
	public static String nullString         = "null"; //$NON-NLS-1$
	public static final String RELASE       = Messages.getString("DefaultInfo.relase"); //$NON-NLS-1$
	// 普通设备类型
	public static final String GEN_TRPE[]   = new String []{ "AXN", "BAT", "MOT" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public static final String CON_LN [] 	= new String []{"ZCON"}; //??? //$NON-NLS-1$
	
	// 设置节点优先级 0 最高 依次递减
	public static final int SUBS_ROOT     	= 0;
	public static final int SUBS_LNODE    	= 1;
	public static final int SUBS_TERMINAL 	= 2;
	public static final int SUBS_SUBEQ    	= 3;
	public static final int SUBS_TABCHG   	= 4;
	public static final int SUBS_POWER    	= 5;
	public static final int SUBS_TRANSFORM	= 6;
	public static final int SUBS_SUBFUN   	= 7;
	public static final int SUBS_GENERAL  	= 8;
	public static final int SUBS_CONDUCT  	= 9;
	public static final int SUBS_CONODE   	= 10;
	public static final int SUBS_VOLTAGE  	= 11;
	public static final int SUBS_BAY 	  	= 12;	
	public static final int SUBS_VOLTAGEL 	= 13;
	public static final int SUBS_FUNCTION 	= 14;
	
	public static final String SUBS_INSERT = Messages.getString("DefaultInfo.subs_insert"); //$NON-NLS-1$
	public static final String SUBS_COPY   = Messages.getString("DefaultInfo.subs_copy"); //$NON-NLS-1$
	public static final String SUBS_DEFATE = Messages.getString("DefaultInfo.subs_delete"); //$NON-NLS-1$
}
