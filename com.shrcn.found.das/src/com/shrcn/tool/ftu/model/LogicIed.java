package com.shrcn.tool.ftu.model;

import java.util.List;


public class LogicIed {
	
	private int id;
	private String iedName;
	private int asduAddress;
	private int objectOffset;
	private int objectNumber;
	private List<IedObject> iedObjectList;
	
	public LogicIed() {
	}

	public LogicIed(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getAsduAddress() {
		return asduAddress;
	}
	public void setAsduAddress(int asduAddress) {
		this.asduAddress = asduAddress;
	}
	public int getObjectOffset() {
		return objectOffset;
	}
	public void setObjectOffset(int objectOffset) {
		this.objectOffset = objectOffset;
	}
	public int getObjectNumber() {
		return objectNumber;
	}
	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}

	public List<IedObject> getIedObjectList() {
		return iedObjectList;
	}

	public void setIedObjectList(List<IedObject> iedObjectList) {
		this.iedObjectList = iedObjectList;
	}
	
	public String getIedName() {
		return iedName;
	}
	
	public void setIedName(String iedName) {
		this.iedName = iedName;
	}
}
