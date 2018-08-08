/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

/**
 * 
 * @author 孙春颖
 * @version 1.0, 2013 3 5
 */
/**
 * $Log: TFjpoint.java,v $
 * Revision 1.4  2013/10/14 00:25:30  cxc
 * update:为了满足104采集中五防顺控点的配置在dpa点中增加infoType，pointNum属性
 *
 * Revision 1.3  2013/03/18 02:58:10  scy
 * Update：增加pointmode属性、get、set方法，对copy()方法更新
 *
 * Revision 1.2  2013/03/08 08:20:38  cchun
 * Update:Fjpoint添加fun、inf
 *
 * Revision 1.1  2013/03/06 23:57:41  scy
 * Add：创建福建规约对应的转发表对象
 *
 */
public class TFjpoint extends BaseDpaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String aetype;
	private String ontime;
	private String offtime;
	private String counttimes;
	private String controlmode;
	private String dividefactor;
	private String multifactor;
	private String offset;
	private String threshold;
	private String lowlimit;
    private String highlimit;
    private String reportcos;
    private String reportsoe;
    private String pointattr;
    private String fun;
    private String inf;
    private String pointmode;
	private String fjtype;
    
    /** 量纲. */
    private String dimen;
    /** 最小值. */
    private String min;
    /** 最大值. */
    private String max;
    /** 步长. */
    private String step;
    /** 整数位数. */
    private String preciseInt;
    /** 小数位数. */
    private String preciseDeci;


	public TFjpoint() {
		super();
	}

	public TFjpoint(int id) {
		super(id);
	}

	public String getAetype() {
		return aetype;
	}

	public void setAetype(String aetype) {
		this.aetype = aetype;
	}

	public String getOntime() {
		return ontime;
	}

	public void setOntime(String ontime) {
		this.ontime = ontime;
	}

	public String getOfftime() {
		return offtime;
	}

	public void setOfftime(String offtime) {
		this.offtime = offtime;
	}

	public String getCounttimes() {
		return counttimes;
	}

	public void setCounttimes(String counttimes) {
		this.counttimes = counttimes;
	}

	public String getControlmode() {
		return controlmode;
	}

	public void setControlmode(String controlmode) {
		this.controlmode = controlmode;
	}

	public String getDividefactor() {
		return dividefactor;
	}

	public void setDividefactor(String dividefactor) {
		this.dividefactor = dividefactor;
	}

	public String getMultifactor() {
		return multifactor;
	}

	public void setMultifactor(String multifactor) {
		this.multifactor = multifactor;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getLowlimit() {
		return lowlimit;
	}

	public void setLowlimit(String lowlimit) {
		this.lowlimit = lowlimit;
	}

	public String getHighlimit() {
		return highlimit;
	}

	public void setHighlimit(String highlimit) {
		this.highlimit = highlimit;
	}

	public String getReportcos() {
		return reportcos;
	}

	public void setReportcos(String reportcos) {
		this.reportcos = reportcos;
	}

	public String getReportsoe() {
		return reportsoe;
	}

	public void setReportsoe(String reportsoe) {
		this.reportsoe = reportsoe;
	}

	public String getPointattr() {
		return pointattr;
	}

	public void setPointattr(String pointattr) {
		this.pointattr = pointattr;
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

	public String getPointmode() {
		return pointmode;
	}

	public void setPointmode(String pointmode) {
		this.pointmode = pointmode;
	}

	@Override
	public void setInfoType(int infoType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPointNum(int pointNum) {
		// TODO Auto-generated method stub
		
	}

	public String getDimen() {
		return dimen;
	}

	public void setDimen(String dimen) {
		this.dimen = dimen;
	}

	public String getMinVal() {
		return min;
	}

	public void setMinVal(String min) {
		this.min = min;
	}

	public String getMaxVal() {
		return max;
	}

	public void setMaxVal(String max) {
		this.max = max;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getPreciseInt() {
		return preciseInt;
	}

	public void setPreciseInt(String preciseInt) {
		this.preciseInt = preciseInt;
	}

	public String getPreciseDeci() {
		return preciseDeci;
	}

	public void setPreciseDeci(String preciseDeci) {
		this.preciseDeci = preciseDeci;
	}

	public String getFjtype() {
		return fjtype;
	}

	public void setFjtype(String fjtype) {
		this.fjtype = fjtype;
	}


}
