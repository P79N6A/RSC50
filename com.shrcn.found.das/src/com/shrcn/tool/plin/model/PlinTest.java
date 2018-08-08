/**
 * Copyright (c) 2007-2014 上海思源弘瑞自动化有限公司.
 * All rights reserved. 
 * This program is an arithmetic for taxis of figure.
 */

package com.shrcn.tool.plin.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author hanhouyang202552
 * 测试对象
 */
public class PlinTest {
	private int id;
	/** 索引序号. */
	private int index;
	/** 名称. */
	private String name;
	/** 测试日期**/
	private Timestamp testDate;
	/** 结果. */
	private String result;
	/**一个测试对应多个测试用例**/
	private List<TestCase> testCases;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Timestamp getTestDate() {
		return testDate;
	}

	public void setTestDate(Timestamp testDate) {
		this.testDate = testDate;
	}

	public String getTime() {
		return StringUtil.getDateStr(testDate, Constants.STD_TIME_FORMAT);
	}

	@Override
	public String toString() {
		return "PlinTest [id=" + id + ", index=" + index + ", name=" + name
				+ ", testDate=" + testDate + ", result=" + result
				+ ", testCases=" + testCases + "]";
	}
	
}
