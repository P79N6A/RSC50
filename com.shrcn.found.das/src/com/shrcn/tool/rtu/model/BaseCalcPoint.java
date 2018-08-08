/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.tool.rtu.model;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-11-7
 */
/**
 * $Log: BaseCalcPoint.java,v $
 * Revision 1.8  2013/05/16 11:04:15  cchun
 * Fix Bug:修复getDesc() null异常
 *
 * Revision 1.7  2013/04/07 06:17:29  scy
 * Update：删除变量的描述信息改为直接从DCA点获取
 *
 * Revision 1.6  2013/04/01 03:00:48  cchun
 * Update:添加copy()
 *
 * Revision 1.5  2012/11/30 12:32:18  cchun
 * Update:添加getRef()
 *
 * Revision 1.4  2012/11/19 07:22:39  cchun
 * Update:将fc改成dbtype,添加saddr
 *
 * Revision 1.3  2012/11/16 09:31:20  cchun
 * Update:增加getDcaPoint()
 *
 * Revision 1.2  2012/11/08 12:31:41  cchun
 * Update:修改表达式及其变量定义表
 *
 * Revision 1.1  2012/11/07 12:00:39  cchun
 * Update:修改计算器表达式和变量表
 *
 */
public abstract class BaseCalcPoint implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int saddr;
    private int varnum;
    private String varname;
    private String dbtype;
    private String ldname;
    private String alias;// 用于RCD文件的别名记录
    private TDcaMx mx;
    private TDcaSt st;
    private TDcaCo co;
    private int OPnum;
    private String des;
    
	public BaseCalcPoint() {
	}

	public BaseCalcPoint(int id, int varnum, String varname, TDcaMx mx,
			TDcaSt st, TDcaCo co) {
		super();
		this.id = id;
		this.varnum = varnum;
		this.varname = varname;
		this.mx = mx;
		this.st = st;
		this.co = co;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSaddr() {
		return saddr;
	}

	public void setSaddr(int saddr) {
		this.saddr = saddr;
	}

	public int getVarnum() {
		return varnum;
	}
	public void setVarnum(int varnum) {
		this.varnum = varnum;
	}
	public String getVarname() {
		return varname;
	}
	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getDesc() {
		BaseDcaPoint dcaPoint = getDcaPoint();
		return dcaPoint==null ? "" : dcaPoint.getDescription();
	}

	public String getLdname() {
		return ldname;
	}

	public void setLdname(String ldname) {
		this.ldname = ldname;
	}

	public TDcaMx getMx() {
		return mx;
	}
	public void setMx(TDcaMx mx) {
		this.mx = mx;
	}
	public TDcaSt getSt() {
		return st;
	}
	public void setSt(TDcaSt st) {
		this.st = st;
	}
	public TDcaCo getCo() {
		return co;
	}
	public void setCo(TDcaCo co) {
		this.co = co;
	}

	public BaseDcaPoint getDcaPoint() {
		if (getDbtype() == null)
			return null;
		return (BaseDcaPoint) ObjectUtil.getProperty(this, getDbtype().toLowerCase());
	}
    
	public String getRef() {
		BaseDcaPoint dcap = getDcaPoint();
		if (dcap == null)
			return "";
		return dcap.getLdref();
	}
	
	public String getFc() {
		if (mx != null) {
			return "MX";
		} else if (st != null) {
			return "ST";
		} else if (co != null) {
			return "CO";
		}
		return "";
	}
	
	protected void assignBaseValues(BaseCalcPoint p) {
    	p.setSaddr(saddr);
    	p.setVarnum(varnum);
    	p.setVarname(varname);
    	p.setDbtype(dbtype);
    	p.setLdname(ldname);
    	p.setAlias(alias);
    	p.setMx(mx);
    	p.setSt(st);
    	p.setCo(co);
    }
    
    public abstract BaseCalcPoint copy();

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getOPnum() {
		return OPnum;
	}

	public void setOPnum(int oPnum) {
		OPnum = oPnum;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	
}
