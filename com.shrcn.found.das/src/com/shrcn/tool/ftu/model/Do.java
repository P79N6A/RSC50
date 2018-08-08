package com.shrcn.tool.ftu.model;

public class Do extends FourYao{
	private int timeout;
	private byte controlMode;
	private short pointAttr;
	
	public Do() {
	}
	
	public Do(int id) {
		super(id);
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public byte getControlMode() {
		return controlMode;
	}
	public void setControlMode(byte controlMode) {
		this.controlMode = controlMode;
	}
	public short getPointAttr() {
		return pointAttr;
	}
	public void setPointAttr(short pointAttr) {
		this.pointAttr = pointAttr;
	}
	
}
