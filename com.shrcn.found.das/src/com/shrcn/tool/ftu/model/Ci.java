package com.shrcn.tool.ftu.model;

public class Ci extends FourYao{
	private float factor;
	private float offset;
	private byte reportFlag ;
	private byte pointMode;
	
	public Ci() {
	}

	public Ci(int id) {
		super(id);
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
	public byte getReportFlag() {
		return reportFlag;
	}
	public void setReportFlag(byte reportFlag) {
		this.reportFlag = reportFlag;
	}
	public byte getPointMode() {
		return pointMode;
	}
	public void setPointMode(byte pointMode) {
		this.pointMode = pointMode;
	}
}
