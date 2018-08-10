package com.synet.tool.rsc.util;

import java.util.List;

public class DataUtils {
	
	public static boolean notNull(List<?> list) {
		if(list != null &&list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
