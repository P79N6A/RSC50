/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.found.das.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.PrimaryKey;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 功能描述：根据实体类得到对应的表名、主键名、字段名工具类 </p>
 * 注：po类名须与对应映射文件名一致，即Student.java与Student.hbm.xml<br>
 * 
 * 
 * @author 孙春颖(mailto:scy@shrcn.com)
 * @version 1.0, 2014-12-17
 * 
 */
public class HEntityUtil {

	private static Configuration hibernateConf;

	private static Configuration getHibernateConf() {
		if (hibernateConf == null) {
			hibernateConf = SessionComponent.getInstance().getConfiguration();// 注解方式
//			hibernateConf.buildSessionFactory();// 注解方式必须的
		}
		return hibernateConf;
	}

	public static PersistentClass getPersistentClass(Class<?> clazz) {
		synchronized (HEntityUtil.class) {
			PersistentClass pc = getHibernateConf().getClassMapping(
					clazz.getName());
			if (pc == null) {
				pc = getHibernateConf().getClassMapping(clazz.getName());

			}
			return pc;
		}
	}

	/**
	 * 功能描述：获取实体对应的表名
	 * 
	 * @param clazz
	 *            实体类
	 * @return 表名
	 */
	public static String getTableName(Class<?> clazz) {
		return getPersistentClass(clazz).getTable().getName();
	}

	/**
	 * 功能描述：获取实体对应表的主键字段名称，只适用于唯一主键的情况
	 * 
	 * @param clazz
	 *            实体类
	 * @return 主键字段名称
	 */
	public static String getPrimaryKey(Class<?> clazz) {
		return getPrimaryKeys(clazz).getColumn(0).getName();
	}

	/**
	 * 功能描述：获取实体对应表的主键字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @return 主键对象primaryKey ，可用primaryKey.getColumn(i).getName()
	 */
	public static PrimaryKey getPrimaryKeys(Class<?> clazz) {
		return getPersistentClass(clazz).getTable().getPrimaryKey();
	}

	/**
	 * 功能描述：通过实体类和属性，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @param propertyName
	 *            属性名称
	 * @return 字段名称
	 */
	public static String getColumnName(Class<?> clazz, String propertyName) {
		PersistentClass persistentClass = getPersistentClass(clazz);
		Property property = persistentClass.getProperty(propertyName);
		Iterator<?> it = property.getColumnIterator();
		if (it.hasNext()) {
			Column column = (Column) it.next();
			return column.getName();
		}
		return null;
	}
	
	/**
	 * 功能描述：通过实体类，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @return 字段名称
	 */
	public static String getColumnNames(Class<?> clazz) {
		String columnNames = "";
		Iterator<?> columnIterator = getPersistentClass(clazz).getTable().getColumnIterator();
		for (Iterator<?> it = columnIterator; it.hasNext();) {
			Column column = (Column) it.next();
			if (columnNames.length() > 0)
				columnNames += ", ";
			columnNames += column.getName();
		}
		return columnNames;
	}
	
	/**
	 * 功能描述：通过实体类，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @return 字段名称
	 */
	public static String getColumnNamesExcludeKey(Class<?> clazz) {
		return getColumnNamesExcludeStr(clazz, getPrimaryKey(clazz));
	}
	
	/**
	 * 功能描述：通过实体类，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @param strs
	 *            排除的字段名称
	 * @return 字段名称
	 */
	public static String getColumnNamesExcludeStr(Class<?> clazz, String str) {
		String columnNames = "";
		Iterator<?> columnIterator = getPersistentClass(clazz).getTable().getColumnIterator();
		for (Iterator<?> it = columnIterator; it.hasNext();) {
			Column column = (Column) it.next();
			String name = column.getName();
			if (str.equalsIgnoreCase(name))
				continue;
			if (columnNames.length() > 0)
				columnNames += ", ";
			columnNames += name;
		}
		return columnNames;
	}
	
	/**
	 * 功能描述：通过实体类，获取实体类属性对应的表字段名称
	 * 
	 * @param clazz
	 *            实体类
	 * @param strs
	 *            排除的字段名称列表（必须为大写）。
	 * @return 字段名称
	 */
	public static String getColumnNamesExcludeStrs(Class<?> clazz, List<String> strs) {
		String columnNames = "";
		Iterator<?> columnIterator = getPersistentClass(clazz).getTable().getColumnIterator();
		for (Iterator<?> it = columnIterator; it.hasNext();) {
			Column column = (Column) it.next();
			String name = column.getName();
			if (strs.contains(name.toUpperCase()))
				continue;
			if (columnNames.length() > 0)
				columnNames += ", ";
			columnNames += name;
		}
		return columnNames;
	}
	
	public static String[] getPropertyNames(Class<?> clazz) {
		ClassMetadata cm = SessionComponent.getInstance().getSessionFactory().getClassMetadata(clazz);
		return cm.getPropertyNames();
	}
	
	/**
	 * 对数据进行排序
	 * @param data
	 */
	public static void sortById(List<?> data) {
		Collections.sort(data, new Comparator<Object>() {
			@Override
			public int compare(Object d1, Object d2) {
				int id1 = (int) ObjectUtil.getProperty(d1, "id");
				int id2 = (int) ObjectUtil.getProperty(d2, "id");
				return id1 - id2;
			}});
	}
}