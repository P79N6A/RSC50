package com.synet.tool.rsc.util;

import java.util.List;

public class DataUtils {
	
	public static boolean listNotNull(List<?> list) {
		if(list != null &&list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean strNotNull(String str) {
		if(str != null && str != "") {
			return true;
		} else {
			return false;
		}
	}
	
	public static String[][] createDictItems(List<String> equDescs) {
		int size = equDescs.size();
		String [ ][ ] arr = new String [ size ][ ];  
		for (int i = 0; i < size; i++) {
			arr[i] = new String[2];
			for (int j = 0; j < 2; j++) {
				arr [i][j] = equDescs.get(i);
			}
		}
		return arr;
	}
}
