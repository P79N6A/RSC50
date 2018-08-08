package com.shrcn.tool.plin.model;

import java.sql.Timestamp;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.util.StringUtil;




/**
 * 测试用例
 * @author hanhouyang202552
 *
 */
public class TestCase {
	
	private int id;
	/** 索引序号. */
	private int index;
	/** 名称. */
	private String name;
	/** 测试日期**/
	private Timestamp testDate;
	/** 预期值. */
	private String preValue;
	/** 实际值. */
	private String realValue;
	/** 测试结果是否通过*/
	private String result;
	
	private PlinTest plinTest;
	
	public TestCase() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPreValue() {
		return preValue;
	}

	public void setPreValue(String preValue) {
		this.preValue = preValue;
	}

	public String getRealValue() {
		return realValue;
	}

	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}

	public PlinTest getPlinTest() {
		return plinTest;
	}

	public void setPlinTest(PlinTest plinTest) {
		this.plinTest = plinTest;
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
	public TestCase(int id, int index, String name, Timestamp testDate,
			String preValue, String realValue, String result, PlinTest plinTest) {
		super();
		this.id = id;
		this.index = index;
		this.name = name;
		this.testDate = testDate;
		this.preValue = preValue;
		this.realValue = realValue;
		this.result = result;
		this.plinTest = plinTest;
	}
	@Override
	public String toString() {
		return "TestCase [id=" + id + ", index=" + index + ", name=" + name
				+ ", testDate=" + testDate + ", preValue=" + preValue
				+ ", realValue=" + realValue + ", result=" + result;
	}
	
}
