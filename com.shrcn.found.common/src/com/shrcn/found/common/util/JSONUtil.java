/**
 * Copyright (c) 2007-2017 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.shrcn.found.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.shrcn.found.common.log.SCTLogger;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2017-12-27
 */
public class JSONUtil {

	/**
	 * 将Object转换为JSONObject并发出Post请求
	 * 
	 * @param url
	 * @param object
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject getJSONObject(Object object) {
		JSONObject jsonObject = new JSONObject();
		Class<?> clazz = object.getClass();
		try {
			while (clazz != Object.class) {
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					jsonObject.put(field.getName(), field.get(object));
				}
				clazz = clazz.getSuperclass();
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
		return jsonObject;
	}
	
	/**
	 * 将json对象转换成普通java对象
	 * @param jsonObject
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Object getObject(JSONObject jsonObject, Class<?> clazz) {
		Object object = ObjectUtil.newInstance(clazz);
		try {
			while (clazz != Object.class) {
					Field[] fields = clazz.getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						Object fv = jsonObject.get(field.getName());
						if (fv != null)
							field.set(object, fv);
					}
				clazz = clazz.getSuperclass();
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			SCTLogger.warn("", e);
		}
		return object;
	}
	
	/**
	 * 将查询结果转换成List。
	 * @param result
	 * @param clazz
	 * @return
	 */
	public static List<?> getResultList(String result, Class<?> clazz) {
		JSONObject json = JSONObject.parseObject(result);
		List<JSONObject> jsonList = (List<JSONObject>) json.get("result");
		List<Object> objList = new ArrayList<>();
		for (JSONObject jsonObj : jsonList) {
			Object object = getObject(jsonObj, clazz);
			objList.add(object);
		}
		return objList;
	}
	
	/**
	 * 将查询结果转换成java object。
	 * @param result
	 * @param clazz
	 * @return
	 */
	public static Object getResultObject(String result, Class<?> clazz) {
		JSONObject json = JSONObject.parseObject(result);
		JSONObject jsonObj = (JSONObject) json.get("result");
		return getObject(jsonObj, clazz);
	}
}

