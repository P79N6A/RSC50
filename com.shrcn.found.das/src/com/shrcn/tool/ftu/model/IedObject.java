package com.shrcn.tool.ftu.model;

import java.util.List;

public class IedObject {

	private int id;
	private LogicIed logicIed;
	private String objName;
	private int infoType;
	private int infoAddress;
	private int pointOffset;
	private int pointNumber;
	private int groupNo;
	private List<Object> dataList;
	
	public IedObject() {
	}

	public IedObject(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public List<Object> getDataList() {
		return dataList;
	}
	
	public void setDataList(List<Object> dataList) {
		this.dataList = dataList;
	}
	
	
	public String getObjName() {
		return objName;
	}
	
	public void setObjName(String objName) {
		this.objName = objName;
	}
	
//	private Map<String, List<Object>> objMap;
	
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}
	public int getInfoAddress() {
		return infoAddress;
	}
	public void setInfoAddress(int infoAddress) {
		this.infoAddress = infoAddress;
	}
	public int getPointOffset() {
		return pointOffset;
	}
	public void setPointOffset(int pointOffset) {
		this.pointOffset = pointOffset;
	}
	public int getPointNumber() {
		return pointNumber;
	}
	public void setPointNumber(int pointNumber) {
		this.pointNumber = pointNumber;
	}
	public int getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	
//	public Map<String, List<Object>> getObjMap() {
//		return objMap;
//	}
//	
//	public void setObjMap(Map<String, List<Object>> objMap) {
//		this.objMap = objMap;
//	}
	

	public LogicIed getLogicIed() {
		return logicIed;
	}
	
	public void setLogicIed(LogicIed logicIed) {
		this.logicIed = logicIed;
	}
	
}
