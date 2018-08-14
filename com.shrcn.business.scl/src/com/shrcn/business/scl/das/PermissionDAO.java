/**
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based Visual Device Develop System.
 */
package com.shrcn.business.scl.das;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.shrcn.business.scl.check.FunctionPermission;
import com.shrcn.business.scl.check.IntervalPermission;
import com.shrcn.business.scl.check.SignalsChecker;
import com.shrcn.business.scl.check.User;
import com.shrcn.business.scl.check.UserGroup;
import com.shrcn.business.scl.enums.EnumCtrlBlock;
import com.shrcn.business.scl.model.OutSingalFCDA;
import com.shrcn.business.scl.model.SCL;
import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.TimeCounter;

import com.shrcn.found.file.xml.DOM4JNodeHelper;
import com.shrcn.found.file.xml.XMLFileManager;
import com.shrcn.found.xmldb.XMLDBHelper;
import com.shrcn.found.xmldb.vtd.fun.OuterDataSetFunction;

public class PermissionDAO implements SCLDAO {

	private PermissionDAO() {}
	
	/*
	 * 导入xml
	 */
	public static  void importXML(String file){
		Document d = XMLFileManager.loadXMLFile(file);
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 导出xml
	 */
	public static  void exportXML(String file){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		XMLFileManager.saveDocument(d,file, "UTF-8");
	}
	
	/*
	 * 获取所有组名
	 */
	public static  String[] getALLGroupName(){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element function = root.element("function");
		List<Element>  userGroups = function.elements();
		String[] names = new String[userGroups.size()];
		for(int i=0;i<userGroups.size();i++){
			names[i]=userGroups.get(i).attributeValue("name");
		}
		return names;
	}
	
	/*
	 * 登陆验证
	 */
	public static  String[] checkLogin(String num,String password){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		if (d == null) {
			 Element root = DocumentHelper.createElement("permission");  
			 Document document = DocumentHelper.createDocument(root);  
			 Element function = root.addElement("function");
			 Element usergroup = function.addElement("userGroup").addAttribute("name", "管理员");
			 Element groupPermission = usergroup.addElement("groupPermission").addText("11111111");
			 Element user = usergroup.addElement("user");
			 user.addAttribute("name", "管理员");
			 user.addAttribute("num", "10000");
			 user.addAttribute("password", "1234");
			 Element userPermission = user.addElement("userPermission").addText("11111111");
			 XMLFileManager.saveDocument(document,"permission.xml", "UTF-8");
		}
		Element root = d.getRootElement();
		String hasNum = "false";
		String hasPassword = "false";
		Element user = DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup/user[@num='"+num+"']");
		if(user!=null){
			hasNum = "true";
			if(password.equals(user.attributeValue("password"))){
				UserInFormation.userGroup=user.getParent().attributeValue("name");
				UserInFormation.userName=user.attributeValue("name");
				UserInFormation.userNum=user.attributeValue("num");
				UserInFormation.userPassword=user.attributeValue("password");
				hasPassword= "true";	
				List<Boolean> userpermissionList=getUserPermission(UserInFormation.userGroup, UserInFormation.userName+"/"+UserInFormation.userNum);
				String userpermission="";
				for(int i=0;i<userpermissionList.size();i++){
					if(userpermissionList.get(i)){
						userpermission = userpermission+"1";
					}else{
						userpermission = userpermission+"0";
					}
				}
				UserInFormation.userpermission=userpermission;
			}
		}
		String[] result = {hasNum,hasPassword};
		return result;
	}
	
	/*
	 * 修改密码
	 */
	@SuppressWarnings("deprecation")
	public static  void changePassword(String groupName,String num,String password){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element user = DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']/user[@num='"+num+"']");
		user.setAttributeValue("password", password);
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	public static  String[] getUsersByGroupName(String groupName){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element function = root.element("function");
		List<Element>  userGroups = function.elements();
		Element userGroup = null;
		for(Element group:userGroups){
			if(groupName.equals(group.attributeValue("name"))){
				userGroup = group;
				break;
			}
		}
		List<Element>  users = userGroup.elements("user");
		String[] names = new String[users.size()];
		for(int i=0;i<users.size();i++){
			names[i]=users.get(i).attributeValue("name");
		}
		return names;
	}

	/*
	 *查询全部信息
	 */
	public static  List<UserGroup> getALLGroup(){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element function = root.element("function");
		List<Element>  userGroups = function.elements();
		List<UserGroup> GroupNameList =new ArrayList<UserGroup>();
		for(Element userGroup:userGroups){
			UserGroup group = new UserGroup();
			group.setName(userGroup.attributeValue("name"));
			group.setUserList(getUsersByGroup(userGroup.attributeValue("name")));
			GroupNameList.add(group);	
		}
		return GroupNameList;
	}

	/*
	 *根据组名查询所有用户
	 */
	public static  List<User> getUsersByGroup(String groupName){
		List<User>  userList = new ArrayList<User>();
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element>  users =  DOM4JNodeHelper.selectNodes(root, "./function/userGroup[@name='"+groupName+"']/user");
		for(Element user:users){
			User u=new User();
			u.setName(user.attributeValue("name"));
			u.setNum(user.attributeValue("num"));
			u.setPassword(user.attributeValue("password"));
			userList.add(u);
		}
		return userList;
	}
	
	/*
	 * 新增用户组
	 */
	public static void addGroup(String newGroupName) {
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element function = root.element("function");
		Element group = function.addElement("userGroup");
		group.addAttribute("name", newGroupName);
		Element permission = group.addElement("groupPermission");
		permission.addText("00000000");
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 删除用户组
	 */
	public static void deleteGroup(String groupName) {
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element function = DOM4JNodeHelper.selectSingleNode(root, "./function");
		Element group = DOM4JNodeHelper.selectSingleNode(function, "./userGroup[@name='"+groupName+"']");
		function.remove(group);
		
		List<Element>  groups = DOM4JNodeHelper.selectNodes(root, "./substation/userGroup[@name='"+groupName+"']");
		for(Element userGroupE:groups){
			Element substation = userGroupE.getParent();
			substation.remove(userGroupE);
		}
		
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 新增用户
	 */
	public static void addUser(String groupName,String username,String num,String password) {
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		//增加功能权限模块用户
		Element userGroup = DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']");
		Element user = userGroup.addElement("user");
		user.addAttribute("name", username);
		user.addAttribute("num", num);
		user.addAttribute("password", password);
		Element permission = user.addElement("userPermission");
		if("管理员".equals(groupName)){
			permission.addText("11111111");
		}else{
			permission.addText("00000000");
		}
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 删除用户
	 */
	public static void deleteUser(String groupName,String userName) {
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element group =  DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']");
		Element  user = DOM4JNodeHelper.selectSingleNode(group, "./user[@num='"+userName.split("/")[1]+"']");
		group.remove(user);

		Element  userGroupE = DOM4JNodeHelper.selectSingleNode(root, "./substation/userGroup[@name='"+groupName+"']");
		if(userGroupE!=null){
			Element  userE = DOM4JNodeHelper.selectSingleNode(userGroupE, "./user[@num='"+userName.split("/")[1]+"']");
			userGroupE.remove(userE);
		}

		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 查询组功能权限
	 */
	public static  List<Boolean> getGroupPermission(String groupName){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Boolean> permission = new ArrayList<Boolean>();
		Element userGroup = DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']");
		String groupPermission =userGroup.element("groupPermission").getText();
		if(groupPermission.length()==8){
			for(int i=0;i<8;i++){
				permission.add("1".equals(groupPermission.substring(i, i+1))? true:false); 
			}
		}
		return permission;
	}
	
	/*
	 * 查询组间隔权限
	 */
	public static  List<IntervalPermission> getGroupIntervalPermission(String groupName){
		List<IntervalPermission> IntervalPermissions = new ArrayList<IntervalPermission>();
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element> bays = DOM4JNodeHelper.selectNodes(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/VoltageLevel/Bay");
		for(Element bay:bays){
			IntervalPermission interval = new IntervalPermission();
			interval.setSubname(subName);
			interval.setLevel(bay.getParent().attributeValue("name"));
			interval.setInterval(bay.attributeValue("name"));
			interval.setBrowse("浏览");
			interval.setCheck1((bay.getText().substring(0, 1)).equals("1")? true:false);
			interval.setChange("修改");
			interval.setCheck2((bay.getText().substring(1, 2)).equals("1")? true:false);
			IntervalPermissions.add(interval);
		}
		return IntervalPermissions;
	}
	
	/*
	 * 查询组间隔权限
	 */
	public static  List<IntervalPermission> getGroupIntervalPermission(String subName,String groupName){
		List<IntervalPermission> IntervalPermissions = new ArrayList<IntervalPermission>();
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element> bays = DOM4JNodeHelper.selectNodes(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/VoltageLevel/Bay");
		for(Element bay:bays){
			IntervalPermission interval = new IntervalPermission();
			interval.setSubname(subName);
			interval.setLevel(bay.getParent().attributeValue("name"));
			interval.setInterval(bay.attributeValue("name"));
			interval.setBrowse("浏览");
			interval.setCheck1((bay.getText().substring(0, 1)).equals("1")? true:false);
			interval.setChange("修改");
			interval.setCheck2((bay.getText().substring(1, 2)).equals("1")? true:false);
			IntervalPermissions.add(interval);
		}
		return IntervalPermissions;
	}
	
	
	
	
	/*
	 * 查询用户间隔权限
	 */
	public static  List<IntervalPermission> getUserIntervalPermission(String groupName,String userName){
		List<IntervalPermission> IntervalPermissions = new ArrayList<IntervalPermission>();
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element> bays = DOM4JNodeHelper.selectNodes(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/user[@num='"+userName.split("/")[1]+"']/VoltageLevel/Bay"); 
		for(Element bayE:bays){
			//组间隔权限
			String bayName = bayE.attributeValue("name");
			String volName = bayE.getParent().attributeValue("name");
			Element gBayE = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/VoltageLevel[@name='"+volName+"']/Bay[@name='"+bayName+"']");
			String bay = bayE.getText();
			String gBay = gBayE.getText();
			
			 IntervalPermission interval = new IntervalPermission();
			 interval.setSubname(subName);
			 interval.setLevel(volName);
			 interval.setInterval(bayName);
			 interval.setBrowse("浏览");
			 interval.setCheck1((bay.substring(0, 1)).equals("1") || gBay.substring(0, 1).equals("1")? true:false);
			 interval.setChange("修改");
			 interval.setCheck2((bay.substring(1, 2)).equals("1") || gBay.substring(1, 2).equals("1")? true:false);
			 IntervalPermissions.add(interval);
		} 
		return IntervalPermissions;
	}

	/*
	 * 查询用户间隔权限
	 */
	public static  List<IntervalPermission> getUserIntervalPermission(String subName,String groupName,String userName){
		List<IntervalPermission> IntervalPermissions = new ArrayList<IntervalPermission>();
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element> bays = DOM4JNodeHelper.selectNodes(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/user[@num='"+userName.split("/")[1]+"']/VoltageLevel/Bay"); 
		for(Element bayE:bays){
			//组间隔权限
			String bayName = bayE.attributeValue("name");
			String volName = bayE.getParent().attributeValue("name");
			Element gBayE = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/VoltageLevel[@name='"+volName+"']/Bay[@name='"+bayName+"']");
			String bay = bayE.getText();
			String gBay = gBayE.getText();
			
			 IntervalPermission interval = new IntervalPermission();
			 interval.setSubname(subName);
			 interval.setLevel(volName);
			 interval.setInterval(bayName);
			 interval.setBrowse("浏览");
			 interval.setCheck1((bay.substring(0, 1)).equals("1") || gBay.substring(0, 1).equals("1")? true:false);
			 interval.setChange("修改");
			 interval.setCheck2((bay.substring(1, 2)).equals("1") || gBay.substring(1, 2).equals("1")? true:false);
			 IntervalPermissions.add(interval);
		} 
		return IntervalPermissions;
	}
	
	/*
	 * 查询用户指定间隔权限
	 */
	public static  IntervalPermission getIntervalPermission(String groupName,String userName,
			String subName, String volName,String bayName){
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		//用户间隔权限
		Element bayE = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/user[@num='"+userName.split("/")[1]+"']/VoltageLevel[@name='"+volName+"']/Bay[@name='"+bayName+"']"); 
		//组间隔权限
		Element gBayE = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+subName+"']/userGroup[@name='"+groupName+"']/VoltageLevel[@name='"+volName+"']/Bay[@name='"+bayName+"']");
		String bay = bayE.getText();
		String gBay = gBayE.getText();
			
		IntervalPermission interval = new IntervalPermission();
		interval.setSubname(subName);
		interval.setLevel(volName);
		interval.setInterval(bayName);
		interval.setBrowse("浏览");
		interval.setCheck1((bay.substring(0, 1)).equals("1") || gBay.substring(0, 1).equals("1")? true:false);
		interval.setChange("修改");
		interval.setCheck2((bay.substring(1, 2)).equals("1") || gBay.substring(1, 2).equals("1")? true:false);

		return interval;
	}
	
	/*
	 * 查询用户功能权限
	 */
	public static  List<Boolean> getUserPermission(String groupName,String userName){
		List<Boolean> Permission = getGroupPermission(groupName);	
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element userPermissionE =  DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']/user[@num='"+userName.split("/")[1]+"']/userPermission");
		String userPermission = userPermissionE.getText();
		if(userPermission.length()==8){
			for(int i=0;i<8;i++){
				if("1".equals(userPermission.substring(i, i+1))){
					Permission.set(i, true);
				};
			}
		}
		return Permission;
	}
	
	/*
	 * 保存组权限
	 */
	public static void saveGroup(String groupName,List<FunctionPermission> Permissions,
			List<IntervalPermission> intervalPermissions){
		String permission= "";
		for(FunctionPermission p:Permissions){
			permission = permission + (p.isSelect()==true? "1":"0");
		}
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		//保存组功能权限
		Element groupPermission = DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']/groupPermission");
		groupPermission.setText(permission);
	
		//保存组间隔权限
		for(IntervalPermission intervalPermission:intervalPermissions){
			Element  substation = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+intervalPermission.getSubname()+"']");
			Element  userGroup2 = DOM4JNodeHelper.selectSingleNode(substation, "./userGroup[@name='"+groupName+"']");
			Element  voltageLevel =  DOM4JNodeHelper.selectSingleNode(userGroup2, "./VoltageLevel[@name='"+intervalPermission.getLevel()+"']");
			Element  bay = DOM4JNodeHelper.selectSingleNode(voltageLevel, "./Bay[@name='"+intervalPermission.getInterval()+"']");
			if(intervalPermission.isCheck2()==true){
				intervalPermission.setCheck1(true);
			}
			bay.setText((intervalPermission.isCheck1()==true? "1":"0") + (intervalPermission.isCheck2()==true? "1":"0"));							
		}
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 保存用户权限
	 */
	public static void saveUser(String groupName,String userName,List<FunctionPermission> Permissions,
			List<IntervalPermission> intervalPermissions){
		String permission= "";
		for(FunctionPermission p:Permissions){
			permission = permission + (p.isSelect()==true? "1":"0");
		}
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		
		//保存用户功能权限
		Element function = root.element("function");
		Element userPermission =  DOM4JNodeHelper.selectSingleNode(root, "./function/userGroup[@name='"+groupName+"']/user[@num='"+userName.split("/")[1]+"']/userPermission");
		userPermission.setText(permission);
		
		//保存用户间隔权限
		for(IntervalPermission intervalPermission:intervalPermissions){
			Element  substation = DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+intervalPermission.getSubname()+"']");
			Element  userGroup2 = DOM4JNodeHelper.selectSingleNode(substation, "./userGroup[@name='"+groupName+"']");
			Element  user = DOM4JNodeHelper.selectSingleNode(userGroup2, "./user[@num='"+userName.split("/")[1]+"']");
			Element  voltageLevel =  DOM4JNodeHelper.selectSingleNode(user, "./VoltageLevel[@name='"+intervalPermission.getLevel()+"']");
			Element  bay = DOM4JNodeHelper.selectSingleNode(voltageLevel, "./Bay[@name='"+intervalPermission.getInterval()+"']");
	
			//组间隔权限
			String gBay = "00";
			Element  groupVoltageLevel = DOM4JNodeHelper.selectSingleNode(userGroup2, "./VoltageLevel[@name='"+intervalPermission.getLevel()+"']");
			Element  groupBay = DOM4JNodeHelper.selectSingleNode(groupVoltageLevel, "./Bay[@name='"+intervalPermission.getInterval()+"']");
			gBay = groupBay.getText();
		
			if(intervalPermission.isCheck2()==true){
				intervalPermission.setCheck1(true);
			}
			//用户间隔权限
			bay.setText((intervalPermission.isCheck1()==true&&gBay.substring(0,1).equals("0")? "1":"0")+
					(intervalPermission.isCheck2()==true&&gBay.substring(1,2).equals("0")? "1":"0"));							
		}
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	
	/*
	 * 是否有打开的substation
	 */
	public static boolean hasSCD(){
		try{
			Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		}catch (NullPointerException e) {
			return false;
		}
		return true;
	}
	
	
	/*
	 * 是否有打开的substation
	 */
	public static String getSubName(){
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		return subName;
	}
	
	/*
	 * 查看变电站是否存在
	 */
	public static boolean exit(){
		boolean exit = false;
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element>  substations = root.elements("substation");
		for(Element substation:substations){
			if(substation.attributeValue("name").equals(subName)){
				exit=true;
				break;
			}
		}
		return exit;
	}
	
	/* 无变电站配置文件时
	 * 生成间隔权限配置文件
	 */
	public static void createSubstationXml(){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		Element sub = root.addElement("substation").addAttribute("name", subName);
		List<UserGroup> UserGroups = getALLGroup();
		for(UserGroup userGroup:UserGroups){
			Element group = sub.addElement("userGroup").addAttribute("name", userGroup.getName());
			
			List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, "./VoltageLevel");
			for(Element voltageLevel:voltageLevels){
				String voltageName = voltageLevel.attributeValue("name");
				Element voltage = group.addElement("VoltageLevel").addAttribute("name", voltageName);
				List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
				for(Element bay:bays){
					String bayName = bay.attributeValue("name");
					Element b = voltage.addElement("Bay").addAttribute("name", bayName);
					if("管理员".equals(userGroup.getName())){
						b.setText("11");
					}else{
						b.setText("00");
					}
				}
			}
			
			List<User> Users = userGroup.getUserList();
			for(User user:Users){
				Element u = group.addElement("user");
				u.addAttribute("name", user.getName());
				u.addAttribute("num", user.getNum());
				for(Element voltageLevel:voltageLevels){
					String voltageName = voltageLevel.attributeValue("name");
					Element voltage = u.addElement("VoltageLevel").addAttribute("name", voltageName);
					List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
					for(Element bay:bays){
						String bayName = bay.attributeValue("name");
						Element b = voltage.addElement("Bay").addAttribute("name", bayName);
						if("管理员".equals(userGroup.getName())){
							b.setText("11");
						}else{
							b.setText("00");
						}
					}
				}
			}
		}
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/* 有变电站配置文件时
	 * 同步间隔权限配置文件_组和用户同步
	 */
	public static void initSubstationXml(){
		Element subNode;
		try{
			subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		}catch (Exception e) {
			return;
		}
		String subName = subNode.attributeValue("name");
		
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<UserGroup> UserGroups = getALLGroup();
		
		//同步组
		Element sub =  DOM4JNodeHelper.selectSingleNode(root, "./substation[@name='"+ subName +"']");
		List<Element> userGrouplist =  sub.elements("userGroup");
		for(UserGroup userGroup:UserGroups){
			boolean exit = false;
			for(Element userGroupE:userGrouplist){
				if(userGroup.getName().equals(userGroupE.attributeValue("name"))){
					exit = true;
					break;
				}
			}
			if(!exit){
				Element group = sub.addElement("userGroup").addAttribute("name", userGroup.getName());
				List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, "./VoltageLevel");
				for(Element voltageLevel:voltageLevels){
					String voltageName = voltageLevel.attributeValue("name");
					Element voltage = group.addElement("VoltageLevel").addAttribute("name", voltageName);
					List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
					for(Element bay:bays){
						String bayName = bay.attributeValue("name");
						Element b = voltage.addElement("Bay").addAttribute("name", bayName);
						b.setText("00");
					}
				}
				userGrouplist =  sub.elements("userGroup");
			}
								
			for(Element userGroupE:userGrouplist){
				if(userGroup.getName().equals(userGroupE.attributeValue("name"))){
					//同步用户
					List<User> Users = userGroup.getUserList();
					List<Element> userList =  userGroupE.elements("user");
					for(User user:Users){
						boolean isExit = false; 
						for(Element userE:userList){
							if((user.getName()+"/"+user.getNum()).equals((userE.attributeValue("name")+"/"+userE.attributeValue("num")))){
								isExit = true;
								break;
							}
						}
						if(!isExit){
							Element u = userGroupE.addElement("user");
							u.addAttribute("name", user.getName());
							u.addAttribute("num", user.getNum());
							List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, "./VoltageLevel");
							for(Element voltageLevel:voltageLevels){
								String voltageName = voltageLevel.attributeValue("name");
								Element voltage = u.addElement("VoltageLevel").addAttribute("name", voltageName);
								List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
								for(Element bay:bays){
									String bayName = bay.attributeValue("name");
									Element b = voltage.addElement("Bay").addAttribute("name", bayName);
									b.setText("00");
								}
							}
						}
					}
					break;
				}
			}
		}
		
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/* 有变电站配置文件时
	 * 同步间隔权限配置文件2_间隔同步
	 */
	public static void initSubstationXmlBay(){
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		Element subNode = XMLDBHelper.selectSingleNode(SCL.XPATH_SUBSTATION);
		String subName = subNode.attributeValue("name");
		
		List<Element> subList = root.elements("substation"); 
		for(Element subE:subList){
			if(subE.attributeValue("name").equals(subName)){
				root.remove(subE);
			}
		}
		
		Element sub = root.addElement("substation").addAttribute("name", subName);
		List<UserGroup> UserGroups = getALLGroup();
		for(UserGroup userGroup:UserGroups){
			Element group = sub.addElement("userGroup").addAttribute("name", userGroup.getName());
			
			List<Element> voltageLevels = DOM4JNodeHelper.selectNodes(subNode, "./VoltageLevel");
			for(Element voltageLevel:voltageLevels){
				String voltageName = voltageLevel.attributeValue("name");
				Element voltage = group.addElement("VoltageLevel").addAttribute("name", voltageName);
				List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
				for(Element bay:bays){
					String bayName = bay.attributeValue("name");
					Element b = voltage.addElement("Bay").addAttribute("name", bayName);
					b.setText("00");
				}
			}
			
			List<User> Users = userGroup.getUserList();
			for(User user:Users){
				Element u = group.addElement("user");
				u.addAttribute("name", user.getName());
				u.addAttribute("num", user.getNum());
				for(Element voltageLevel:voltageLevels){
					String voltageName = voltageLevel.attributeValue("name");
					Element voltage = u.addElement("VoltageLevel").addAttribute("name", voltageName);
					List<Element> bays = DOM4JNodeHelper.selectNodes(voltageLevel, "./Bay");
					for(Element bay:bays){
						String bayName = bay.attributeValue("name");
						Element b = voltage.addElement("Bay").addAttribute("name", bayName);
						b.setText("00");
					}
				}
			}
		}
		
		
		Document oldD = XMLFileManager.loadXMLFile("permission.xml");
		Element oldRoot = oldD.getRootElement();
		Element oldSub = (Element) oldRoot.selectSingleNode("./substation[@name='"+subName+"']");
		
		
		
		List<Element> newGroupList = sub.elements("userGroup");
		for(Element newGroup:newGroupList){
			Element oldGroup = (Element) oldSub.selectSingleNode("./userGroup[@name='"+newGroup.attributeValue("name")+"']");
			List<Element> newVoltageLevelList = newGroup.elements("VoltageLevel");
			for(Element newVoltageLevel:newVoltageLevelList){
				Element oldVoltageLevel = (Element) oldGroup.selectSingleNode("./VoltageLevel[@name='"+newVoltageLevel.attributeValue("name")+"']");
				if(oldVoltageLevel==null){
					continue;
				}
				List<Element> newBayList = newVoltageLevel.elements("Bay");
				for(Element newBay:newBayList){
					Element oldBay = (Element) oldVoltageLevel.selectSingleNode("./Bay[@name='"+newBay.attributeValue("name")+"']");
					if(oldBay==null){
						continue;
					}else{
						newBay.setText(oldBay.getText());
					}
				}
			}
				
			List<Element> newUserList = newGroup.elements("user");
			for(Element newUser:newUserList){
				Element oldUser = (Element) oldGroup.selectSingleNode("./user[@name='"+newUser.attributeValue("name")+"'][@num='"+newUser.attributeValue("num")+"']");
				List<Element> newVoltageLevels = newUser.elements("VoltageLevel");
				for(Element newVoltageLevel:newVoltageLevels){
					Element oldVoltageLevel = (Element) oldUser.selectSingleNode("./VoltageLevel[@name='"+newVoltageLevel.attributeValue("name")+"']");
					if(oldVoltageLevel==null){
						continue;
					}
					List<Element> newBays = newVoltageLevel.elements("Bay");
					for(Element newBay:newBays){
						Element oldBay = (Element) oldVoltageLevel.selectSingleNode("./Bay[@name='"+newBay.attributeValue("name")+"']");
						if(oldBay==null){
							continue;
						}else{
							newBay.setText(oldBay.getText());
						}
					}
				}
			}
		}
		
		XMLFileManager.saveDocument(d,"permission.xml", "UTF-8");
	}
	
	/*
	 * 获取所有substation名称
	 */
	public static List<String> getsubList(){
		List<String> subList = new ArrayList<>();
		Document d = XMLFileManager.loadXMLFile("permission.xml");
		Element root = d.getRootElement();
		List<Element>  substations = root.elements("substation");
		for(Element sub:substations){
			String subName = sub.attributeValue("name");
			subList.add(subName);
		}
		return subList;
	}
}
