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
}
