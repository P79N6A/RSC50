/**
 * Copyright (c) 2007-2014 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2014-10-20
 */
public class TDcaiedModbus extends BaseDcaied implements java.io.Serializable {

	private static final long serialVersionUID = 5089615152378186141L;

	private String transID;
	private String protocolID;
	private String unitID;
	private int objectOffset;
	private int objectNumber;
	private int headType;
	private int addrID;
	private int regSize;

	private Set<TDcaObject> objects = new HashSet<TDcaObject>();
	
	private int chnId;// 用于Modbus导出

	public TDcaiedModbus() {
	}
	
	public TDcaiedModbus(int id, int chnId, String iedname, int statuspoint, String transID, String protocolID, String unitID, int objectOffset, int objectNumber, int headType, int addrID){
		setId(id);
		setIedname(iedname);
		setStatuspoint(statuspoint);
		this.transID = transID;
		this.protocolID = protocolID;
		this.unitID = unitID;
		this.objectOffset = objectOffset;
		this.objectNumber = objectNumber;
		this.headType = headType;
		this.addrID = addrID;
		this.chnId = chnId;
	}

	public TDcaiedModbus copy() {
		TDcaiedModbus ied = (TDcaiedModbus) ObjectUtil.duplicate(this);
		ied.setObjects(null);
		return ied;
	}

	public Set<TDcaObject> getObjects() {
		return objects;
	}

	public void setObjects(Set<TDcaObject> objects) {
		this.objects = objects;
	}

	public void addObject(TDcaObject object) {
		getObjects().add(object);
		object.setIed(this);
	}

	public String getTransID() {
		return transID;
	}

	public void setTransID(String transID) {
		this.transID = transID;
	}

	public String getProtocolID() {
		return protocolID;
	}

	public void setProtocolID(String protocolID) {
		this.protocolID = protocolID;
	}

	public String getUnitID() {
		return unitID;
	}

	public void setUnitID(String unitID) {
		this.unitID = unitID;
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

	public int getHeadType() {
		return headType;
	}

	public void setHeadType(int headType) {
		this.headType = headType;
	}

	public int getAddrID() {
		return addrID;
	}

	public void setAddrID(int addrID) {
		this.addrID = addrID;
	}

	public int getChnId() {
		return chnId;
	}

	public void setChnId(int chnId) {
		this.chnId = chnId;
	}

	public int getRegSize() {
		return regSize;
	}

	public void setRegSize(int regSize) {
		this.regSize = regSize;
	}
}
