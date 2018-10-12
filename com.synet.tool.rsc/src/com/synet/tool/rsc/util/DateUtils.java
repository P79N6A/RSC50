package com.synet.tool.rsc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_DAY_PATTERN = "yyyy-MM-dd";
	public static final String DATE_DAY_PATTERN_ = "yyyyMMdd";

	public static String getDateStr(Date date, String pattern) {
		SimpleDateFormat sf = new SimpleDateFormat(pattern);
		return sf.format(date);
	}
}
