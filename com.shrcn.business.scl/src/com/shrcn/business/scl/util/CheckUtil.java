/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.util;

import java.util.List;

import com.shrcn.business.scl.PropertConstants;
import com.shrcn.found.common.valid.BasicTypeValidator;
import com.shrcn.found.common.valid.DataTypeChecker;
import com.shrcn.found.common.valid.IPValidator;
import com.shrcn.found.common.valid.ValidatorStatus;

/**
 * 校验检查APPID、VLAN-P
 * 
 * @author 孙春颖
 * @version 1.0, 2014-6-20
 */
public class CheckUtil {

	/**
	 * 检查APPID
	 * 1、规则校验；
	 * 2、重复性校验；
	 * 3、GSE、SMV特殊规则校验
	 * 
	 * @param newText
	 * @param attrName
	 * @param otherAPPID
	 * @return
	 */
	public static ValidatorStatus checkAPPID(String newText, String attrName,
			List<String> otherAPPID) {
		ValidatorStatus status = null;
		String title = "APPID警告";
		boolean flag = DataTypeChecker.checkHexDigitLetter(newText);
		if (!flag) {
			status = new ValidatorStatus(title, "必须为0~9或A~F");
		} else if (otherAPPID != null) {
			int pos = otherAPPID.indexOf(newText);
			if (pos > -1) {
				status = new ValidatorStatus(title, newText + "与现有装置AppID冲突！");
			} else {
				status = (attrName.equals("GSE")) ? checkGSEAPPID(newText, title) : checkSMVAPPID(newText, title);
			}
		}
		return status;
	}
	
	/**
	 * 对APPID进行长度及有效性验证
	 * @param appID
	 * @return
	 */
	public static ValidatorStatus checkGSEAPPID(String appID, String title) {
		int intAppID = Integer.parseInt(appID, 16);
		if (appID.length() > 4) {
			return new ValidatorStatus(title, "APPID字符长度小于或等于4");
		} else if (intAppID < 0 || intAppID > 16383) {
			return new ValidatorStatus(title, "必须为" + 
					PropertConstants.GSE_MIN_APPID +
					"到" + PropertConstants.GSE_MAX_APPID + "之间的16进制数！");
		}
		return null;
	}

	/**
	 * 对APPID进行长度及有效性验证
	 * 
	 * @param appID
	 * @return
	 */
	public static ValidatorStatus checkSMVAPPID(String appID, String title) {
		int intAppID = Integer.parseInt(appID, 16);
		if (appID.length() > 4) {
			return new ValidatorStatus(title, "APPID字符长度小于或等于4");
		} else if (intAppID > 32767 || intAppID < 16384) {
			return new ValidatorStatus(title, "必须为" + 
					PropertConstants.SMV_MIN_APPID +
					"到" + PropertConstants.SMV_MAX_APPID + "之间的16进制数！");
		}
		return null;
	}

	/**
	 * 对VLAN-P进行数字有效性、数字范围及长度验证
	 * 
	 * @param value
	 * @return
	 */
	public static ValidatorStatus checkVLANP(String value) {
		ValidatorStatus status = null;

		String title = "VLAN-P警告";
		if (value.length() <= 1) {
			boolean digitFlag = BasicTypeValidator.checkUnsignedIntValid(value);// 判断数字

			if (!digitFlag) {
				status = new ValidatorStatus(title, "VLAN-P必须为数字！");
			} else {
				int v = Integer.parseInt(value);
				if (v > 7 || v < 0) {
					status = new ValidatorStatus(title, "VLAN-P数字范围为0-7！");
				}
			}
		} else {
			status = new ValidatorStatus(title, "VLAN-P必须为0-7范围内的数字！");
		}

		return status;
	}
	
	/**
	 * 对VLAN-ID进行有效性及长度验证
	 * 
	 * @param value
	 */
	public static ValidatorStatus checkVLANID(String value){
		ValidatorStatus status=null;
		String title="VLAN-ID警告";
		boolean flag = DataTypeChecker.checkHexDigitLetter(value);
		if(!flag){
			status=new ValidatorStatus(title,"VLAN-ID字符必须为0~9或A~F！");
		}else{
			if(value.length()>3){
				status=new ValidatorStatus(title,"VLAN-ID字符长度小于或等于3！");
			}
		}
		return status;
	}
	
	
	/**要进行如下验证:
	 * (1)、有效性验证;
	 * (2)、MAC地址首字有效性验证;
	 * (3)、重复性验证;
	 * (4)、GSE、SMV规则性验证;
	 * @param newText MAC地址
	 * @param attrName "GSE"或"SMV"
	 * @return
	 */
	public static ValidatorStatus checkMAC(String newText, String attrName,
			List<String> otherMAC) {
		String title = "MAC地址警告";
		ValidatorStatus status = null;
		boolean result = IPValidator.checkMacValid(newText);
		if (!result) {
			status = new ValidatorStatus(title, newText + "为非法的MAC地址！");
		} else {
			result = DataTypeChecker.checkMacFirst(newText, attrName);
			if (result) {
				int pos = otherMAC.indexOf(newText);
				if (pos > -1) {
					status = new ValidatorStatus(title, newText + "与现有装置MAC地址冲突！");
				}
			} else {
				if (attrName.equals("GSE")) { //$NON-NLS-1$
					status = new ValidatorStatus(title, "前四字节不是01-0C-CD-01！");
				} else {
					status = new ValidatorStatus(title, "前四字节不是01-0C-CD-04！");
				}
			}
		}

		return status;
	}

	public static String[] getCheckNames(){
		return new String[]{"APPID", "GSEAPPID","SMVAPPID","VLANP","VLANID","MAC"};
	}
}
