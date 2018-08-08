package com.shrcn.tool.ftu.model;

public class Di extends FourYao{
	private byte pointMode;
	
	public Di() {
	}

	public Di(int id) {
		super(id);
	}
	public byte getPointMode() {
		return pointMode;
	}
	public void setPointMode(byte pointMode) {
		this.pointMode = pointMode;
	}
}
