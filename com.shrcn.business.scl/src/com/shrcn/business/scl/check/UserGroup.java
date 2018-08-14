/**
 * Copyright (c) 2007-2015 上海思源弘瑞自动化有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.business.scl.check;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2015-8-22
 */
public class UserGroup {

	private String name;
	private List<User> userList = new ArrayList<User>();
	public UserGroup() {}

	public List<User> getUserList() {
		return userList;
	}


	public void setUserList(List<User> userList) {
		this.userList = userList;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}