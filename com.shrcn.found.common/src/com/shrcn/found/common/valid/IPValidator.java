/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.valid;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;

import com.shrcn.found.common.util.StringUtil;


/**
 * Add one sentence class summary here. Add class description here.
 * 
 * @author 黄钦辉
 * @version 1.0, 2009-4-20
 */
public class IPValidator implements IInputValidator {
	
	private static String iptitle="IP警告";
	private static String commonTitle="警告";
	
	public static ValidatorStatus checkIP(String currIp,List<String> lstOtherIp){
		ValidatorStatus status=null;
		if("".equalsIgnoreCase(currIp)){
			 status=new ValidatorStatus(iptitle,"IP地址不能为空");
			 return status;
		}
		if(!checkValue(currIp)){
			 status=new ValidatorStatus(iptitle,"IP地址不合法");
			 return status;
		}
		int pos=lstOtherIp.indexOf(currIp);
		if(pos>-1){
			String format=MessageFormat.format("此IP地址[{0}]在{1}行已经存在", currIp,(pos+1));
			status=new ValidatorStatus(iptitle,format);
			 return status;
		}
		return status;
	}
	
	public static ValidatorStatus checkSubMask(String currSubNet){
		ValidatorStatus status=null;
		if("".equalsIgnoreCase(currSubNet)){
			 status=new ValidatorStatus(iptitle,"子网掩码不能为空");
			 return status;
		}
		if(!checkIpSubNetValue(currSubNet)){
			 status=new ValidatorStatus(iptitle,"子网掩码不合法");
			 return status;
		}
		return status;
	}
	
	public static ValidatorStatus checkGateWay(String currGateWay){
		ValidatorStatus status=null;
		if(!checkIpGateWayValue(currGateWay)){
			 status=new ValidatorStatus(commonTitle,"不合法");
		}
		
		return status;
	}

	/**
	 * 校验IP
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkValue(String value) {
		return DataTypeChecker.checkIP(value);
	}

	/**
	 * 校验子网掩码
	 * 
	 * @param value
	 * @return
	 */
	public static boolean checkIpSubNetValue(String value) {
		return DataTypeChecker.checkNetMask(value);
	}

	public static boolean checkIpGateWayValue(String value) {
		return DataTypeChecker.checkIP(value);
	}

	public static boolean checkIntegerValid(String newText) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return false;
		}

		return true;
	}

	public static boolean checkMacValid(String newText) {
		String text = "^([0-9a-fA-F]{2})(-[0-9a-fA-F]{2}){5}$";
		Pattern pattern = Pattern.compile(text);
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return false;
		}

		return true;
	}

	public static boolean checkHexValid(String newText) {
		String text = "^([0-9a-fA-F]){4}$";
		Pattern pattern = Pattern.compile(text);
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return false;
		}

		return true;
	}

	@Override
	public String isValid(String newText) {
		return null;
	}
	
	/**比较nowValue是否比comparedValue要小,如果是,则返回null,否则返回status信息
	 * @param nowValue
	 * @param comparedValue
	 * @return
	 */
	public static ValidatorStatus checkLessThan(String nowValue,String comparedValue){
		ValidatorStatus status=null;
		int now=StringUtil.str2int(nowValue);
		int comp=StringUtil.str2int(comparedValue);
		if(now>=comp){
			status=new ValidatorStatus("MinTime值警告",
					"MinTime列的值必须小于等于同行的MaxTime列的值");
		}
		return status;
	}
	
	/**比较nowValue是否比comparedValue要大,如果是,则返回null,否则返回status信息
	 * @param nowValue
	 * @param comparedValue
	 * @return
	 */
	public static ValidatorStatus checkGreaterThan(String nowValue,String comparedValue){
		ValidatorStatus status=null;
		int now=StringUtil.str2int(nowValue);
		int comp=StringUtil.str2int(comparedValue);
		if(now<=comp){
			 status=new ValidatorStatus("MaxTime警告",
					 "MaxTime列的值必须大于等于同行的MinTime列的值");
		}
		return status;
	}
	
	/**
	 * 对MinTime进行验证:数字验证、比后列同行值小验证
	 * @param value
	 * @param comparedValue
	 * @return
	 */
	public static ValidatorStatus checkMinTime(String value,String comparedValue){
		ValidatorStatus status = null;
		status=checkNum(value);
		if(status==null){
			status=checkLessThan(value, comparedValue);
		}
		return status;
	}
	
	
	/**
	 * 对MaxTime进行有效性验证:数字验证、比前列同行值大验证
	 * @param value
	 * @param comparedValue
	 * @return
	 */
	public static ValidatorStatus checkMaxTime(String value,String comparedValue){
		ValidatorStatus status = null;
		status=checkNum(value);
		if(status==null){
			status=checkGreaterThan(value, comparedValue);
		}
		return status;
	}

	public static ValidatorStatus checkNum(String currNum){
		ValidatorStatus status=null;
		if(!DataTypeChecker.checkDigit(currNum)){
			status=new ValidatorStatus(commonTitle,"必须为数字");
		}
		return status;
	}
}
