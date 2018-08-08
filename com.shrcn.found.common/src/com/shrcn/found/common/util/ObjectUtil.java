/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ognl.Ognl;
import ognl.OgnlException;

import org.eclipse.core.internal.registry.osgi.OSGIUtils;
import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;

import com.shrcn.found.common.log.SCTLogger;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-7-1
 */
/**
 * $Log: ObjectUtil.java,v $
 * Revision 1.6  2013/08/26 06:32:56  cchun
 * Update:修改getClassByName()增加插件自动识别处理
 *
 * Revision 1.5  2013/05/30 10:27:32  cchun
 * Update:修改existProperty()
 *
 * Revision 1.4  2013/05/21 03:21:49  cchun
 * Update:去掉getProperty()日志
 *
 * Revision 1.3  2013/04/06 05:24:54  cchun
 * 1、Refactor:统一使用newInstance()；2、Update:添加getClassByName()
 *
 * Revision 1.2  2013/04/02 13:27:57  cchun
 * Update:为invoke()增加权限
 *
 * Revision 1.1  2013/03/29 09:36:29  cchun
 * Add:创建
 *
 * Revision 1.14  2013/01/05 12:15:40  cchun
 * Update:添加getObjectList()
 *
 * Revision 1.13  2013/01/05 09:25:42  cchun
 * Update:为getClassByName()添加null判断
 *
 * Revision 1.12  2012/11/26 08:37:32  cchun
 * Update:增加isInteger()
 *
 * Revision 1.11  2012/11/13 01:21:16  cchun
 * Update:添加invoke()
 *
 * Revision 1.10  2012/11/12 01:31:29  cchun
 * Update:添加null判断
 *
 * Revision 1.9  2012/11/12 00:54:18  cchun
 * Update:添加getClassByName(),newInstance()
 *
 * Revision 1.8  2012/11/09 09:34:52  cchun
 * Update:添加setProperties()
 *
 * Revision 1.7  2012/11/08 12:28:34  cchun
 * Update:添加对象invoke()
 *
 * Revision 1.6  2012/11/05 07:49:54  cchun
 * Update:修改日志级别
 *
 * Revision 1.5  2012/11/02 09:38:42  cchun
 * Update:修改日志内容
 *
 * Revision 1.4  2012/10/22 07:23:52  cchun
 * Update:修改getProperty(),setProperty()实现
 *
 * Revision 1.3  2012/09/13 11:36:54  cchun
 * Update:增加invoke()
 *
 * Revision 1.2  2011/12/02 03:40:47  cchun
 * Update:封装ObjectUtil.setProperty(),ObjectUtil.getProperty()
 *
 * Revision 1.1  2011/07/11 08:57:47  cchun
 * Add:类反射工具类
 *
 * Revision 1.1  2011/07/04 09:38:11  cchun
 * Add:类反射工具
 *
 */
public class ObjectUtil {

	private static final OSGIUtils osgiutils = OSGIUtils.getDefault();
	
	/**
	 * 得到指定类。
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByName(Class<?> bundleClass, String className) {
		if (StringUtil.isEmpty(className))
			return null;
		Class<?> clazz = null;
		try {
			int idxSep = className.indexOf("/");
			if (idxSep>-1) {
				String bundleId = className.substring(0, idxSep);
				className = className.substring(idxSep+1);
				clazz = getClassByName(bundleId, className);
			} else {
				PackageAdmin packageAdmin = osgiutils.getPackageAdmin();
				if (packageAdmin == null)
					clazz = Class.forName(className);
				else
					clazz = packageAdmin.getBundle(bundleClass).loadClass(className);
			}
		} catch (ClassNotFoundException e) {
			SCTLogger.error("找不到类：" + e.getMessage());
		}
		return clazz;
	}
	
	/**
	 * 得到指定类。
	 * @param bundleID
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByName(String bundleID, String className) {
		Class<?> clazz = null;
		try {
			Bundle bundle = osgiutils.getBundle(bundleID);
			if (bundle == null)
				clazz = Class.forName(className);
			else
				clazz = bundle.loadClass(className);
				
		} catch (ClassNotFoundException e) {
			SCTLogger.error("找不到类：" + e.getMessage());
		}
		return clazz;
	}
	
	/**
	 * 调用静态无参数的方法
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Object invoke(Class<?> clazz, String methodName) {
		return invoke(clazz, methodName, new Class<?>[]{}, new Object[]{});
	}
	
	/**
	 * 调用对象方法。
	 * @param object
	 * @param methodName
	 * @return
	 */
	public static Object invoke(Object object, String methodName) {
		return invoke(object, methodName, new Class<?>[]{}, new Object[]{});
	}
	
	/**
	 * 调用静态方法
	 * @param clazz
	 * @param methodName
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object invoke(Class<?> clazz, String methodName, Class<?>[] argsType, Object...args) {
		try {
			Method method = getDeclaredMethod(clazz, methodName, argsType);
			return method.invoke(null, args);
		} catch (Exception e) {
			SCTLogger.error("反射调用失败：", e);
		}
		return null;
	}
	
	/**
	 * 调用对象方法。
	 * @param obj
	 * @param methodName
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object invoke(Object obj, String methodName, Class<?>[] argsType, Object...args) {
		try {
			Method method = getDeclaredMethod(obj.getClass(), methodName, argsType);
			if (null != method) {
				method.setAccessible(true);
				return method.invoke(obj, args);
			}
		} catch (Exception e) {
			SCTLogger.error("反射调用失败：", e);
		}
		return null;
	}
	
	/**
	 * 反射获取方法对象
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(Class<?> objClass, String methodName, Class<?>...argsType) {
		Method method = null;
		for (Class<?> clazz=objClass; clazz!=Object.class; clazz=clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, argsType);
				return method;
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	/**
	 * 获取class所有字段
	 * @param objClass
	 * @return
	 */
	public static List<Field> getDeclaredFields(Class<?> objClass) {
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> clazz=objClass; clazz!=Object.class; clazz=clazz.getSuperclass()) {
			fields.addAll(java.util.Arrays.asList(clazz.getDeclaredFields()));
		}
		return fields;
	}
	
	/**
	 * 创建对象
	 * @param clazz
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object newInstance(Class<?> clazz, Class<?>[] argsType, Object...args) {
		try {
			Constructor<?> cstr = clazz.getConstructor(argsType);
			return cstr.newInstance(args);
		} catch (SecurityException e) {
			SCTLogger.error("安全错误：", e);
		} catch (NoSuchMethodException e) {
			SCTLogger.error("无此方法：", e);
		} catch (IllegalArgumentException e) {
			SCTLogger.error("非法参数：", e);
		} catch (InstantiationException e) {
			SCTLogger.error("初始化错误：", e);
		} catch (IllegalAccessException e) {
			SCTLogger.error("非法访问：", e);
		} catch (InvocationTargetException e) {
			SCTLogger.error("调用错误：", e);
		}
		return null;
	}
	
	/**
	 * 构造对象。
	 * @param clazz
	 * @return
	 */
	public static Object newInstance(Class<?> clazz) {
		Object inst = null;
		try {
			inst = clazz.newInstance();
		} catch (InstantiationException e) {
			SCTLogger.error("初始化错误：" + e.getMessage());
		} catch (IllegalAccessException e) {
			SCTLogger.error("非法访问：" + e.getMessage());
		}
		return inst;
	}
	
	/**
	 * 实例化当前插件下的class对象。
	 * @param className
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object newInstance(String className, Class<?>[] argsType, Object...args) {
		try {
			Class<?> clazz = Class.forName(className);
			return newInstance(clazz, argsType, args);
		} catch (ClassNotFoundException e) {
			SCTLogger.error("未找到类：", e);
		}
		return null;
	}
	
	/**
	 * 实例化与指定class在同一插件下的对象（无参构造）。
	 * @param bundleClass
	 * @param className
	 * @return
	 */
	public static Object newInstance(Class<?> bundleClass, String className) {
		return newInstance(bundleClass, className, new Class<?>[]{}, new Object[0]);
	}
			
	/**
	 * 实例化与指定class在同一插件下的对象。
	 * @param bundleClass
	 * @param className
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object newInstance(Class<?> bundleClass, String className, Class<?>[] argsType, Object...args) {
		Class<?> clazz = getClassByName(bundleClass, className);
		return (clazz==null) ? null : newInstance(clazz, argsType, args);
	}
	
	/**
	 * 实例化指定插件下的对象。
	 * @param bundleID
	 * @param className
	 * @param argsType
	 * @param args
	 * @return
	 */
	public static Object newInstance(String bundleID, String className, Class<?>[] argsType, Object...args) {
		Class<?> clazz = getClassByName(bundleID, className);
		return newInstance(clazz, argsType, args);
	}
	
	/**
	 * 复制对象
	 * @param obj
	 * @return
	 */
	public static Object duplicate(Object obj) {
		Object copy = null;
		try {
			Class<?> objClass = obj.getClass();
			copy = newInstance(objClass);
			if (copy == null)
				return null;
			List<Field> fields = getDeclaredFields(objClass);
			for (Field field : fields) {
				if (!Modifier.isFinal(field.getModifiers()) ) {
					field.setAccessible(true);
					field.set(copy, field.get(obj)); // 禁止递归复制，避免死循环
				}
			}
		} catch (IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
		return copy;
	}
	
	/**
	 * 复制对象并改变class类型。
	 * @param obj
	 * @param newClass
	 * @return
	 */
	public static Object createCopy(Object obj, Class<?> newClass) {
		Object copy = newInstance(newClass);
		if (copy == null)
			return null;
		try {
			List<Field> fields = getDeclaredFields(obj.getClass());
			for (Field field : fields) {
				if (!Modifier.isFinal(field.getModifiers()) ) {
					field.setAccessible(true);
					setProperty(copy, field.getName(), field.get(obj));
				}
			}
		} catch (IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
		return copy;
	}
	
	/**
	 * 获取java bean属性值
	 * @param obj
	 * @param property
	 * @return
	 */
	public static Object getProperty(Object obj, String property) {
		Object v = null;
		try {
			v = Ognl.getValue(property, obj);
		} catch (OgnlException e) {
//			TDTLogger.warn("获取对象属性错误：" + e.getMessage());
		}
		return v;
	}
	
	/**
	 * 判断当前对象是否含有指定名称的属性
	 * @param obj
	 * @param property
	 * @return
	 */
	public static boolean existProperty(Object obj, String property) {
		try {
			Ognl.getValue(property, obj);
		} catch (OgnlException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断属性是否为整型。
	 * @param obj
	 * @param property
	 * @return
	 */
	public static boolean isInteger(Object obj, String property) {
		Field field = getField(obj.getClass(), property);
		if (field != null) {
			Class<?> type = field.getType();
			return type == Integer.class || type == int.class;
		}
		return false;
	}
	
	/**
	 * 判断属性是否为整型。
	 * @param obj
	 * @param property
	 * @return
	 */
	public static boolean isBoolean(Object obj, String property) {
		Field field = getField(obj.getClass(), property);
		if (field != null) {
			Class<?> type = field.getType();
			return type == Boolean.class || type == boolean.class;
		}
		return false;
	}
	
	/**
	 * 判断是否为基本类型
	 * @param type
	 * @return
	 */
	public static boolean isPrimitive(Class<?> type) {
		if (type.isPrimitive())
			return true;
		Class<?> typeField = null;
		try {
			typeField = (Class<?>) type.getField("TYPE").get(null);
		} catch (Exception e) {
		}
		return typeField!=null && typeField.isPrimitive();
	}
	
	/**
	 * 为java bean指定属性赋值
	 * @param obj
	 * @param property
	 * @param value
	 */
	public static void setProperty(Object obj, String property, Object value) {
		try {
			if (!existProperty(obj, property)) {
				return;
			}
			if (value == null) {
				Ognl.setValue(property, obj, null);
			} else{
				if (value instanceof String) {
					if (isInteger(obj, property)) {
						if("".equals(value)){
							Ognl.setValue(property, obj, null);
						}else{
							Ognl.setValue(property, obj, Integer.valueOf((String)value));
						}
						return;
					} else if (isBoolean(obj, property)) {
						Ognl.setValue(property, obj, Boolean.valueOf((String)value));
						return;
					}
				}
				Field field = getField(obj.getClass(), property);
				Class<?> ftype = (field == null) ? null : field.getType(); // 属性名和setter,getter不一致时会导致ftype为null
				if (ftype==null || ftype.isAssignableFrom(value.getClass()) || isPrimitive(ftype)) {
					Ognl.setValue(property, obj, value);
				} else {
					SCTLogger.warn("类型不匹配：属性为" + ftype.getName() + 
							"，值为" + value.getClass().getName() + "。");
				}
			}
		} catch (OgnlException e) {
			SCTLogger.warn("属性赋值错误：", e);
		}
	}
	
	/**
	 * 从Map给属性复制。
	 * @param obj
	 * @param map
	 */
	public static void setProperties(Object obj, Map<String, ?> map) {
		for (Entry<String, ?> entry : map.entrySet()) {
			setProperty(obj, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * 得到Object list。
	 * @param data
	 * @return
	 */
	public static List<Object> getObjectList(List<?> data) {
		List<Object> input = new ArrayList<Object>();
		input.addAll(data);
		return input;
	}
	/**
	 * 反射为对象成员赋值
	 * @param obj
	 * @param name
	 * @param value
	 */
    public static void setFieldValue(Object obj, String name, Object value) {
    	Class<?> clazz = obj.getClass();
    	try {
			Field f = getField(clazz, name);
			if (f != null) {
				f.setAccessible(true);
				f.set(obj, value);
			}
		} catch (SecurityException e) {
			SCTLogger.warn("", e);
		} catch (IllegalArgumentException e) {
			SCTLogger.warn("", e);
		} catch (IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
    }
    
    /**
     * 反射获取对象成员的值
     * @param obj
     * @param name
     * @return
     */
    public static Object getFieldValue(Object obj, String name) {
    	Class<?> clazz = obj.getClass();
		try {
			Field f = getField(clazz, name);
			if (f != null) {
				f.setAccessible(true);
				return f.get(obj);
			}
		} catch (SecurityException e) {
			SCTLogger.warn("", e);
		} catch (IllegalArgumentException e) {
			SCTLogger.warn("", e);
		} catch (IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
		return null;
    }
	
    public static Field getField(Class<?> clazz, String fieldName) {
    	while(clazz != Object.class) {
    		try {
    			Field f = clazz.getDeclaredField(fieldName);
				if (f != null) {
					return f;
				}
	    	} catch (SecurityException e) {
	    		SCTLogger.warn("", e);
	    	} catch (NoSuchFieldException e) {
	    		clazz = clazz.getSuperclass();
			}
    	}
    	return null;
    }
    
    /**
	 * 获取类路径
	 * @param clazz
	 * @return
	 */
	public static String getClassPath(Class<?> activeClass, Class<?> clazz) {
		return activeClass.getPackage().getName() + "/" + clazz.getName();
	}
	
	private static String getParamType(Type type) {
		String typename = null;
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) type;
			typename = ((Class<?>)ptype.getRawType()).getSimpleName() + "<"
					+ ((Class<?>)ptype.getActualTypeArguments()[0]).getSimpleName() + ">";
		} else {
			typename = ((Class<?>)type).getSimpleName();
		}
		return typename;
	}
	
	private static boolean printMethod(Object method, String seq) {
		Type[] gtypes = (Type[]) invoke(method, "getGenericParameterTypes");
		int modifier = (int) invoke(method, "getModifiers");
		String mtName = (String) invoke(method, "getName");
		if (mtName.indexOf('$')>0)
			return false;
		int dIdx = mtName.lastIndexOf('.');
		if (dIdx>0)
			mtName = mtName.substring(dIdx+1);
		String params = "";
		int i=1;
		for (Type type : gtypes) {
			if (!"".equals(params))
				params += ", ";
			params += getParamType(type) + " param" + i;
			i++;
		}
		String retype = "";
		if (method instanceof Method) {
			retype = getParamType(((Method)method).getGenericReturnType());
		}
		String signature = Modifier.toString(modifier) + " " +
				retype + " " + mtName + "(" + params + ")";
		System.out.println(seq + (StringUtil.isEmpty(seq)?"":" ") + mtName + "()");
		String usage = "".equals(retype)?"构造函数":"";
		System.out.println("用途：" + usage + "。");
		System.out.println("结构：\n" + signature);
		System.out.println("说明：");
		String retype1 = "".equals(retype)?mtName+"对象":
				("void".equals(retype)?"无":retype);
		System.out.println("a)	返回值：" + retype1 + "。");
		System.out.println("b)	输入参数：" + ("".equals(params)?"无":params) + "。");
		System.out.println("处理逻辑：无。");
		return true;
	}
	
	public static void printMethods(Class<?> clazz, String prefix) {
		Constructor[] conts = clazz.getConstructors();
		int i=1;
		for (Constructor cont : conts) {
			if(printMethod(cont, prefix+i))
				i++;
		}
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if(printMethod(method, prefix+i))
				i++;
		}
	}
	
	public static void printFields(Class<?> clazz) {
		String simpleName = clazz.getSimpleName();
		System.out.println("实体类" + simpleName);
		System.out.println("用途	：用于保存信息。");
		System.out.println("相关名称：" + clazz.getName());
		System.out.println("结构声明：");
		System.out.println("public class " + simpleName + " {");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			int modifier = field.getModifiers();
			if (!Modifier.isStatic(modifier))
				System.out.println("\t" + Modifier.toString(modifier) + " " +
					getParamType(field.getGenericType()) + " " + field.getName() + ";");
		}
		System.out.println("}");
		System.out.println("元素说明：");
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			int modifier = field.getModifiers();
			if (!Modifier.isStatic(modifier))
				System.out.println((i+1) + ")	" + fields[i].getName() + "：。");
		}
	}
	
	/**
	 * 对象克隆
	 * @param original
	 * @return
	 */
	public static Object clone(Object original) {
		Object clone = null;
		try {
			// Increased buffer size to speed up writing
			ByteArrayOutputStream bos = new ByteArrayOutputStream(5120);
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(original);
			out.flush();
			out.close();

			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			clone = in.readObject();

			in.close();
			bos.close();

			return clone;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}

		return null;
	}
}
