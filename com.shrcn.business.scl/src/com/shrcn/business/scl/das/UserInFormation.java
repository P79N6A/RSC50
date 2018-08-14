package com.shrcn.business.scl.das;

import java.util.List;

import org.dom4j.Element;

import com.shrcn.found.common.util.StringUtil;
import com.shrcn.found.xmldb.XMLDBHelper;

public class UserInFormation{

	public static String userGroup="";
	public static String userName="";
	public static String userNum="";
	public static String userPassword="";
	public static String userpermission="00000000";
	
	
	public static String getUserGroup() {
		return userGroup;
	}
	public static void setUserGroup(String userGroup) {
		UserInFormation.userGroup = userGroup;
	}
	public static String getUserName() {
		return userName;
	}
	public static void setUserName(String userName) {
		UserInFormation.userName = userName;
	}
	public static String getUserNum() {
		return userNum;
	}
	public static void setUserNum(String userNum) {
		UserInFormation.userNum = userNum;
	}
	public static String getUserPassword() {
		return userPassword;
	}
	public static void setUserPassword(String userPassword) {
		UserInFormation.userPassword = userPassword;
	}
	public static String getUserpermission() {
		return userpermission;
	}
	public static void setUserpermission(String userpermission) {
		UserInFormation.userpermission = userpermission;
	}
}
