package com.shrcn.tool.rtu.model;

// Generated 2012-2-8 13:44:26 by Hibernate Tools 3.2.2.GA

/**
 * TDiexpr generated by hbm2java
 */
public class TDiexpr extends BaseCalcExpr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String outputeventtype;
	private String bayoid;
	private String baystate;
	private String exptype;

	public TDiexpr() {
	}

	public TDiexpr(int id) {
		super(id);
	}

	public TDiexpr(int id, String expression, int currentnumber,
			int totalnumber, TDcaMx mx, TDcaSt st, TDcaCo co, int saddr) {
		super(id, expression, currentnumber, totalnumber, mx, st, co, saddr);
	}

	public String getOutputeventtype() {
		return this.outputeventtype;
	}

	public void setOutputeventtype(String outputeventtype) {
		this.outputeventtype = outputeventtype;
	}

	public String getBayoid() {
		return this.bayoid;
	}

	public void setBayoid(String bayoid) {
		this.bayoid = bayoid;
	}

	public String getBaystate() {
		return baystate;
	}

	public void setBaystate(String baystate) {
		this.baystate = baystate;
	}

	public String getExptype() {
		return exptype;
	}

	public void setExptype(String exptype) {
		this.exptype = exptype;
	}

	@Override
	public BaseCalcExpr copy() {
		TDiexpr expr = new TDiexpr();
		assignValues(expr);
		expr.setOutputeventtype(outputeventtype);
		expr.setBayoid(bayoid);
		expr.setBaystate(baystate);
		expr.setExptype(exptype);
		return expr;
	}

}
