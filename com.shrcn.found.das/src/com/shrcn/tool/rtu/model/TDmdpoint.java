package com.shrcn.tool.rtu.model;

// Generated 2012-2-8 13:44:26 by Hibernate Tools 3.2.2.GA

/**
 * TDmdpoint generated by hbm2java
 */
public class TDmdpoint extends BaseDpaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dividefactor;
	private String multifactor;
	private String offset;
	private String threshold;
	private String lowlimit;
	private String highlimit;
	private String di0;
	private String di1;
	private String fun;
	private String inf;
	private String ctlmode;
	private String aetype;

	public TDmdpoint() {
	}

	public TDmdpoint(int id) {
		super(id);
	}

	public String getDividefactor() {
		return this.dividefactor;
	}

	public void setDividefactor(String dividefactor) {
		this.dividefactor = dividefactor;
	}

	public String getMultifactor() {
		return this.multifactor;
	}

	public void setMultifactor(String multifactor) {
		this.multifactor = multifactor;
	}

	public String getOffset() {
		return this.offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getThreshold() {
		return this.threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getLowlimit() {
		return this.lowlimit;
	}

	public void setLowlimit(String lowlimit) {
		this.lowlimit = lowlimit;
	}

	public String getHighlimit() {
		return this.highlimit;
	}

	public void setHighlimit(String highlimit) {
		this.highlimit = highlimit;
	}

	public String getDi0() {
		return this.di0;
	}

	public void setDi0(String di0) {
		this.di0 = di0;
	}

	public String getDi1() {
		return this.di1;
	}

	public void setDi1(String di1) {
		this.di1 = di1;
	}

	@Override
	public void setInfoType(int infoType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPointNum(int pointNum) {
		// TODO Auto-generated method stub

	}

	public String getFun() {
		return fun;
	}

	public void setFun(String fun) {
		this.fun = fun;
	}

	public String getInf() {
		return inf;
	}

	public void setInf(String inf) {
		this.inf = inf;
	}

	public String getCtlmode() {
		return ctlmode;
	}

	public void setCtlmode(String ctlmode) {
		this.ctlmode = ctlmode;
	}

	public String getAetype() {
		return aetype;
	}

	public void setAetype(String aetype) {
		this.aetype = aetype;
	}
}
