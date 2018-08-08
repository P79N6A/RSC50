package com.shrcn.found.ui.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.ui.UIConstants;
import com.shrcn.found.ui.model.TableConfig;

/**
 * 读取表格的配置文件,分位两种情况，一种根据uiCfgPath的配置文件取出路径，加载
 * 一种直接写入路径加载
 * 路径对应的表格为两种，一种为单个表格，一种为多个表格（放在TabFolder中）
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-29
 */
/**
 * $Log: TableBuilder.java,v $
 * Revision 1.2  2013/05/23 08:33:26  scy
 * Update：更新引用
 *
 * Revision 1.1  2013/05/17 09:21:33  cxc
 * Update：修改导航树的结构
 *
 * Revision 1.2  2013/04/12 09:15:31  scy
 * Update：表格引用UDMKTable
 *
 * Revision 1.1  2013/04/10 06:21:09  zsy
 * Add:创建表格和表单
 *
 * Revision 1.27  2013/03/26 01:49:35  scy
 * Update：增加实时库点查询功能doRTDBQuery
 *
 * Revision 1.26  2013/02/22 08:35:38  cchun
 * Update:根据功能约束描述来更新
 *
 * Revision 1.25  2013/01/24 03:18:42  scy
 * Update：增加createVPRefTable()方法
 *
 * Revision 1.24  2013/01/05 12:34:10  cchun
 * Update:增加createIEDRefTable()
 *
 * Revision 1.23  2012/12/04 08:02:20  cchun
 * Add:计算器变量表格
 *
 * Revision 1.22  2012/11/30 08:52:17  cchun
 * Add:挑点表格
 *
 * Revision 1.21  2012/11/28 12:06:48  cchun
 * Update:改用ktable
 *
 * Revision 1.20  2012/11/28 06:15:10  cchun
 * Update:增加checkbox表格
 *
 * Revision 1.19  2012/11/21 08:20:05  cchun
 * Update:完成工程管理功能
 *
 * Revision 1.18  2012/11/15 07:46:07  cchun
 * Fix Bug:纠正注释
 *
 * Revision 1.17  2012/11/14 11:55:57  cchun
 * Add:本地IP表格
 *
 * Revision 1.16  2012/11/14 07:41:49  cchun
 * Update:使用DpaObjectTable
 *
 * Revision 1.15  2012/11/14 05:55:53  cchun
 * Refactor:转移refreshRownum()
 *
 * Revision 1.14  2012/11/13 12:54:04  cchun
 * Refactor:定义转发表表格
 *
 * Revision 1.13  2012/11/13 11:24:08  cchun
 * Update:createADIOTable()改用PointsTable
 *
 * Revision 1.12  2012/11/13 09:15:03  cchun
 * Update:添加refreshRownum()
 *
 * Revision 1.11  2012/11/13 05:19:20  cchun
 * Update:添加挑点表格
 *
 * Revision 1.10  2012/11/08 12:39:19  cchun
 * Refactor:修改包名，增加Update:增加
 *
 * Revision 1.9  2012/11/07 12:08:15  cchun
 * Add:增加createVarTable()
 *
 * Revision 1.8  2012/11/06 03:21:12  cchun
 * Update:添加fillFCsTabFolder()
 *
 * Revision 1.7  2012/11/05 07:58:06  cchun
 * Update:统一使用RTUTable代替子类名称,添加createVpTable()
 *
 * Revision 1.6  2012/11/02 09:39:13  cchun
 * Update:完成实时库显示
 *
 * Revision 1.5  2012/11/01 08:14:16  cchun
 * Update:增加createDcaRcbTable(),createDcaIedTable()
 *
 * Revision 1.4  2012/10/31 02:59:10  cchun
 * Update:清理引用
 *
 * Revision 1.3  2012/10/30 07:47:27  cchun
 * Refactor:移动位置
 *
 * Revision 1.2  2012/10/30 05:42:06  cchun
 * Refactor:统一由TableFactory创建表格
 *
 * Revision 1.1  2012/10/29 09:01:41  cchun
 * Refactor:修改位置
 *
 */
public class TableBuilder {

	private TableBuilder() {}
	
	/**
	 * 创建表格(Table)
	 * 
	 * @param clazz
	 * @param check
	 * @param container
	 * @param config
	 * @return
	 */
	public static RTable createTable(Class<?> clazz, boolean check, Composite container, TableConfig config) {
		return (RTable) ObjectUtil.newInstance(clazz,
				new Class[] {Composite.class, TableConfig.class, boolean.class},
				new Object[] {container, config, check});
	}
	
	/**
	 * 创建表格(KTable)
	 * 
	 * @param clazz
	 * @param check
	 * @param container
	 * @param config
	 * @return
	 */
	public static RKTable createKTable(Class<?> clazz, boolean check, Composite container, TableConfig config) {
		return (RKTable) ObjectUtil.newInstance(clazz,
				new Class[] {Composite.class, TableConfig.class, boolean.class},
				new Object[] {container, config, check});
	}
	
	/**
	 * 创建表格(Table)
	 * 
	 * @param clazz
	 * @param container
	 * @param config
	 * @return
	 */
	public static RTable createTable(Class<?> clazz, Composite container, TableConfig config) {
		return (RTable) ObjectUtil.newInstance(clazz,
				new Class[] {Composite.class, TableConfig.class},
				new Object[] {container, config});
	}
	
	/**
	 * 创建表格(KTable)
	 * 
	 * @param clazz
	 * @param container
	 * @param config
	 * @return
	 */
	public static RKTable createKTable(Class<?> clazz, Composite container, TableConfig config) {
		return (RKTable) ObjectUtil.newInstance(clazz,
				new Class[] {Composite.class, TableConfig.class},
				new Object[] {container, config});
	}
	
	/**
	 * 创建表格(Table)
	 * 
	 * @param check
	 * @param container
	 * @param config
	 * @return
	 */
	public static RTable createTable(boolean check, Composite container, TableConfig config) {
		return createTable(getTClazz(config), check, container, config);
	}
	
	/**
	 * 创建表格(KTable)
	 * 
	 * @param clazz
	 * @param check
	 * @param container
	 * @param config
	 * @return
	 */
	public static RKTable createKTable(boolean check, Composite container, TableConfig config) {
		return createKTable(getKTClazz(config), check, container, config);
	}
	
	/**
	 * 创建表格(Table)
	 * 
	 * @param clazz
	 * @param container
	 * @param config
	 * @return
	 */
	public static RTable createTable(Composite container, TableConfig config) {
		return createTable(getTClazz(config), container, config);
	}
	
	/**
	 * 创建表格(KTable)
	 * 
	 * @param clazz
	 * @param container
	 * @param config
	 * @return
	 */
	public static RKTable createKTable(Composite container, TableConfig config) {
		return createKTable(getKTClazz(config), container, config);
	}
	
	/**
	 * 根据包名获得表格对应的Class.
	 * 
	 * @param config
	 * @return
	 */
	private static Class<?> getClazz(TableConfig config) {
		return ObjectUtil.getClassByName(TableBuilder.class, config.gettClassName());
	}
	
	private static Class<?> getKTClazz(TableConfig config){
		Class<?> clazz = getClazz(config);
		return clazz == null ? RKTable.class : clazz;
	}
	
	private static Class<?> getTClazz(TableConfig config){
		Class<?> clazz = getClazz(config);
		return clazz == null ? RTable.class : clazz;
	}
	
	/**
	 * 根据配置填充TabFolder
	 * @param tabFolder
	 * @param tabName
	 */
	public static List<RKTable> fillTabFolder(Class<?> tableClass, CTabFolder tabFolder, List<TableConfig> configs) {
		return fillTabFolder(tableClass, false, tabFolder, configs);
	}
	
	/**
	 * 根据配置填充TabFolder
	 * @param tabFolder
	 * @param tabName
	 */
	public static List<RKTable> fillTabFolder(Class<?> tableClass, boolean check, CTabFolder tabFolder, List<TableConfig> configs) {
		List<RKTable> tables = new ArrayList<RKTable>();
		for (TableConfig config : configs) {
			RKTable table = null;
			if (check)
				table = createKTable(tableClass, check, tabFolder, config);
			else
				table = createKTable(tableClass, tabFolder, config);
			tables.add(table);
			final CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
			tabItem.setFont(UIConstants.FONT_CONTENT);
			tabItem.setText(table.getTableDesc());
			tabItem.setControl(table.getTable());
		}
		return tables;
	}
}
