package com.shrcn.tool.ftu.model;

public class Ao extends FourYao{
	private int timeOut;
	private float factor;
	private float offset;
	private byte datType;
	private byte controlMode;
	private short pointAttr;
	
	public Ao() {
	}

	
	public Ao(int id) {
		super(id);
	}


	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public float getFactor() {
		return factor;
	}
	public void setFactor(float factor) {
		this.factor = factor;
	}
	public float getOffset() {
		return offset;
	}
	public void setOffset(float offset) {
		this.offset = offset;
	}

	public short getPointAttr() {
		return pointAttr;
	}
	public void setPointAttr(short pointAttr) {
		this.pointAttr = pointAttr;
	}
	

	public byte getDatType() {
		return datType;
	}

	public void setDatType(byte datType) {
		this.datType = datType;
	}

	public byte getControlMode() {
		return controlMode;
	}

	public void setControlMode(byte controlMode) {
		this.controlMode = controlMode;
	}
	
	
}
