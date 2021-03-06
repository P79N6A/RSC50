package com.shrcn.tool.rtu.model;

// Generated 2012-2-8 13:44:26 by Hibernate Tools 3.2.2.GA

/**
 * TDipoint generated by hbm2java
 */
public class TDipoint extends BaseDpaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pointmode;
	private String reportcos;
	private String reportsoe;
	private String reverse;
	private String fun;
	private String inf;
	private String infotype;
	private String ditimetype;
	private String timeTag;
	private String ditype;
	private String alias;
	
	public TDipoint() {
	}

	public TDipoint(int id) {
		super(id);
	}

	public String getPointmode() {
		return this.pointmode;
	}

	public void setPointmode(String pointmode) {
		this.pointmode = pointmode;
	}

	public String getReportcos() {
		return this.reportcos;
	}

	public void setReportcos(String reportcos) {
		this.reportcos = reportcos;
	}

	public String getReportsoe() {
		return this.reportsoe;
	}

	public void setReportsoe(String reportsoe) {
		this.reportsoe = reportsoe;
	}

	public String getReverse() {
		return this.reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
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

	@Override
	public void setPointNum(int pointNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInfoType(int infoType) {
		// TODO Auto-generated method stub
		
	}

	public String getInfotype() {
		return infotype;
	}

	public void setInfotype(String infotype) {
		this.infotype = infotype;
	}

	public String getTimeTag() {
		return timeTag;
	}

	public void setTimeTag(String timeTag) {
		this.timeTag = timeTag;
	}

	public String getDitype() {
		return ditype;
	}

	public void setDitype(String ditype) {
		this.ditype = ditype;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDitimetype() {
		return ditimetype;
	}

	public void setDitimetype(String ditimetype) {
		this.ditimetype = ditimetype;
	}
}
