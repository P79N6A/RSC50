/**
 * Copyright (c) 2007-2010 chenchun.
 * All rights reserved. This program is an application based on tcp/ip.
 */
package com.shrcn.tool.rtu.model;

import com.shrcn.found.common.util.ObjectUtil;
import com.shrcn.found.common.util.StringUtil;

/**
 * 
 * @author 陈春(mailto:chench80@126.com)
 * @version 1.0, 2012-2-9
 */
/**
 * $Log: BaseCalcExpr.java,v $ Revision 1.5 2012/11/23 06:15:35 cchun
 * Update:添加复制方法
 * 
 * Revision 1.4 2012/11/19 07:22:07 cchun Update:将fc改成dbtype
 * 
 * Revision 1.3 2012/11/08 12:31:41 cchun Update:修改表达式及其变量定义表
 * 
 * Revision 1.2 2012/11/07 12:00:39 cchun Update:修改计算器表达式和变量表
 * 
 * Revision 1.1 2012/10/25 11:25:45 cchun Refactor:方便hibernate初始化，移动位置
 * 
 * Revision 1.2 2012/10/22 07:43:28 cchun Update:根据应用修改实体Bean
 * 
 * Revision 1.1 2012/10/17 08:09:23 cchun Add:映射实体类
 * 
 */
public abstract class BaseCalcExpr implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String dbtype;
	private String desc;
	private String expression;
	private int currentnumber;
	private int totalnumber;
	private TDcaMx mx;
	private TDcaSt st;
	private TDcaCo co;
	private int saddr;
	private String alias;
	private String dataId;
	private int varNum;
	private String expressionNew;
	private String type;
	private String info;
    private String privateInfo;
	
	public BaseCalcExpr() {
	}

	public BaseCalcExpr(int id) {
		this.id = id;
	}

	public BaseCalcExpr(int id, String expression, int currentnumber,
			int totalnumber, TDcaMx mx, TDcaSt st, TDcaCo co, int saddr) {
		super();
		this.id = id;
		this.expression = expression;
		this.currentnumber = currentnumber;
		this.totalnumber = totalnumber;
		this.mx = mx;
		this.st = st;
		this.co = co;
		this.saddr = saddr;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public int getCurrentnumber() {
		return currentnumber;
	}

	public void setCurrentnumber(int currentnumber) {
		this.currentnumber = currentnumber;
	}

	public int getTotalnumber() {
		return totalnumber;
	}

	public void setTotalnumber(int totalnumber) {
		this.totalnumber = totalnumber;
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

	public int getSaddr() {
		return saddr;
	}

	public void setSaddr(int saddr) {
		this.saddr = saddr;
	}

	public void assignValues(BaseCalcExpr expr) {
		expr.setDbtype(dbtype);
		expr.setDesc(desc);
		expr.setExpression(expression);
		expr.setMx(mx);
		expr.setSt(st);
		expr.setCo(co);
		expr.setCurrentnumber(currentnumber);
		expr.setTotalnumber(totalnumber);
		expr.setSaddr(saddr);
		expr.setAlias(alias);
		expr.setDataId(dataId);
		expr.setVarNum(varNum);
		expr.setExpressionNew(expressionNew);
		expr.setType(type);
	}

	public abstract BaseCalcExpr copy();

	public BaseDcaPoint getDcaPoint() {
		String dbtype = getDbtype();
		if (StringUtil.isEmpty(dbtype))
			return null;
		return (BaseDcaPoint) ObjectUtil
				.getProperty(this, dbtype.toLowerCase());
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public int getVarNum() {
		return varNum;
	}

	public void setVarNum(int varNum) {
		this.varNum = varNum;
	}

	public String getExpressionNew() {
		return expressionNew;
	}

	public void setExpressionNew(String expressionNew) {
		this.expressionNew = expressionNew;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPrivateInfo() {
		return privateInfo;
	}

	public void setPrivateInfo(String privateInfo) {
		this.privateInfo = privateInfo;
	}
}
