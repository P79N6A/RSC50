package com.synet.tool.rsc.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.shrcn.found.common.util.ObjectUtil;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;

public class IndexedJSONUtil {

	public static String getJsonString(Object obj) {
		List<Field> fields = ObjectUtil.getDeclaredFields(obj.getClass());
		List<Field> fList = new ArrayList<>();
		for (Field field : fields) {
			Class<?> fClass = field.getType();
			if (fClass.isPrimitive()
					|| Integer.class==fClass || String.class==fClass) {
				fList.add(field);
			}
		}
		Collections.sort(fList, new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				return f1.getName().compareTo(f2.getName());
			}});
		String json = "";
		for (Field field : fList) {
			String fname = field.getName();
			if (json.length() > 0) {
				json += ",";
			}
			json += fname + ":" + ObjectUtil.getFieldValue(obj, fname);
		}
		return json;
	}
	
	public static String getJson(Object obj) {
		return "{" + getJsonString(obj) + "}";
	}
	
	public static String getBoardJson(Tb1047BoardEntity board) {
		return "{" + getJsonString(board) + ","
				+ getSetJsonString("ports", board.getTb1048PortsByF1047Code())
				+ "}";
	}

	private static String getSetJsonString(String fname, Set<?> objs) {
		String objsJson = "";
		for (Object obj : objs) {
			if (objsJson.length() > 0) {
				objsJson += ",";
			}
			objsJson += getJson(obj);
		}
		return fname + ":{" + objsJson + "}";
	}

	public static String getRegionJson(Tb1049RegionEntity region) {
		return "{" + getJsonString(region) + ","
				+ getSetJsonString("cubicles", region.getTb1050CubiclesByF1049Code())
				+ "}";
	}
}
