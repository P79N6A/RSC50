
package com.shrcn.found.ui.util;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.common.valid.DataTypeChecker;
import com.shrcn.found.common.valid.IPValidator;
import com.shrcn.found.ui.model.IField;


/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-10-29
 */
/**
 * $Log: CheckUtil.java,v $
 * Revision 1.3  2013/11/04 09:30:08  cchun
 * 修改表格数据类型校验的错误, 如果类型错误, 就退回原来的值
 *
 * Revision 1.2  2013/08/12 03:38:22  scy
 * Update：更新框架的校验检查仅通过datType属性配置。
 *
 * Revision 1.1  2013/03/29 09:55:12  cchun
 * Add:创建
 *
 * Revision 1.5  2013/01/10 03:51:10  cchun
 * Fix Bug:修复短地址校验缺陷
 *
 * Revision 1.4  2012/11/20 09:28:22  cchun
 * Update:添加IP检查
 *
 * Revision 1.3  2012/10/30 07:41:44  cchun
 * Refactor:封转checkDataType()
 *
 * Revision 1.2  2012/10/29 08:54:00  cchun
 * Update:添加注释
 *
 */
public class UICheckUtil {

	/**
	 * 检查用户输入
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public static String checkInput(IField field, String value) {
		String desc = field.getTitle();
		String regex = field.getRegex();
		if (!StringUtil.isEmpty(regex)) {
			if (regex.startsWith("IP")) {
				if (StringUtil.isEmpty(value)) {
					if (regex.split("|")[1] == null) {
						return "IP地址不能为空！";
					}
				} else if (!DataTypeChecker.checkIP(value)) {
					return desc + "IP地址格式不正确！";
				}
				return null;
			} else if (!StringUtil.isEmpty(regex) && !value.matches(regex)) {
				return desc + "不满足正则表达式‘" + regex + "’！";
			}
		}
		return checkDataType(field, value);
	}
	
	public static String checkDataType(IField field, String value) {
		if (StringUtil.isEmpty(value)) {
			if (field.isEmpty()) {
				return null;
			} else {
				return field.getTitle() + "值不能为空！";
			}
		} else {
			return checkDataType(field.getDatatype(), field.getTitle(), value);
		}
	}
	
	/**
	 * 检查数据类型。
	 * @param datType
	 * @param desc
	 * @param value
	 * @return
	 */
	public static String checkDataType(String datType, String desc, String value) {
		if (!StringUtil.isEmpty(datType) && !StringUtil.isEmpty(value)) {
			//字典类型
			if(datType.startsWith("dic")){
				return null;
			}
			//正则类型
			if (datType.startsWith("regex")) {// 格式 regex[]
				if (value.matches(datType.substring(6))) {
					return null;
				} else {
					return desc + "输入格式不正确！";
				}
			}
			//有取值范围类型(非16进制)， 格式[0, 100]                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
			if (datType.contains("[") && !datType.startsWith("hex")) {
				String[] split = datType.split(",");
				String start = split[0].substring(split[0].indexOf("[") + 1);
				String end = split[1].substring(0, split[1].length() - 1);
				float v = Float.valueOf(value);
				if (v < Float.valueOf(start) || v > Float.valueOf(end)) {
					return desc + "取值范围为：" + datType.substring(datType.indexOf("[")) + "!";
				}
			} 
			//16进制类型，有取值范围
			if (datType.startsWith("hex") && datType.contains("[")) {
				if (!value.toUpperCase().endsWith("H")) {
					return desc + "值为十六进制须以 H 结束！";
				} else  {
					String[] split = datType.split(",");
					String start = split[0].substring(split[0].indexOf("[") + 1, split[0].length() - 1);
					String end = split[1].substring(0, split[1].length() - 2);
					int v = Integer.parseInt(value.substring(0, value.length() - 1), 16);
					int intStart = Integer.parseInt(start, 16);
					int intEnd = Integer.parseInt(end, 16);
					if (v > intEnd || v < intStart) {
						return desc + "取值范围为：" + datType.substring(datType.indexOf("[")) + "!";
					}
				}
			}

			// 直接为校验类型
			String msg = checkData(datType, desc, value);
			if(msg != null) {
				return msg;
			}
		}
		return null;
	}

	private static String checkData(String datType, final String desc, String value) {
		if (datType.startsWith("int") && !DataTypeChecker.checkInteger(value)) {
			return desc + "必须为整数！";
			
		}
		if (datType.startsWith("float") && !DataTypeChecker.checkFloat(value)) {
			return desc + "必须为浮点数！";
			
		}
		if (datType.equalsIgnoreCase("ip") && !DataTypeChecker.checkIP(value)) {
			return desc + "格式不正确！";
			
		}
		if (datType.equalsIgnoreCase("port") && !DataTypeChecker.checkPort(value)) {
			return desc + "必须在0~65536(不含)范围内！";
			
		}
		if (datType.equalsIgnoreCase("mac") && !IPValidator.checkMacValid(value)) {
			return desc + "正确格式 01-0C-CD-01-00-01！";
			
		}
		if(datType.equalsIgnoreCase("appID") && !DataTypeChecker.checkHexDigitLetter(value)){
			return desc + "必须为0~9或A~F！";
			
		}
		if (datType.startsWith("hex") && !DataTypeChecker.checkHexDigitLetter(value.substring(0, value.length() - 1))) {
			return desc + "必须为0~9或A~F！";
			
		}
		if (datType.equalsIgnoreCase("ipnet") && !DataTypeChecker.checkIPNet(value)) {
			return desc + "格式不正确！";
			
		}
		return null;
	}
	
}

