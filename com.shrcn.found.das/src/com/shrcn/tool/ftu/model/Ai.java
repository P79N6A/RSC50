package com.shrcn.tool.ftu.model;

public class Ai extends FourYao{
	private int lowLimit;
	private int highLimit;
	private float factor;
	private float offset;
	private float threshold;
	
	public Ai() {
	}

	public Ai(int id) {
		super(id);
	}

	public int getLowLimit() {
		return lowLimit;
	}
	public void setLowLimit(int lowLimit) {
		this.lowLimit = lowLimit;
	}
	public int getHighLimit() {
		return highLimit;
	}
	public void setHighLimit(int highLimit) {
		this.highLimit = highLimit;
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
	public float getThreshold() {
		return threshold;
	}
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

}
