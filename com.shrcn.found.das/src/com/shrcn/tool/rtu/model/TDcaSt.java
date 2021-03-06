package com.shrcn.tool.rtu.model;

// Generated 2012-2-8 13:44:26 by Hibernate Tools 3.2.2.GA

/**
 * TDcaSt generated by hbm2java
 */
public class TDcaSt extends BaseDcaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pointmode;
	private String dividefactor;
	private String multifactor;
	private String lowLimit;
	private String highLimit;
	
	/** Answer采集 **/
	private int offset;

	public TDcaSt() {
	}
	
	public TDcaSt(int id, int dcaindex, int objId, int saddr,
			String desc, String fc, int objectID, int entryID, int pointNum) {
		super(id, dcaindex, objId, saddr, desc, fc, objectID, entryID,
				pointNum);
	}
	
	public TDcaSt(int id, int dcaindex, int objId, int saddr,
			String desc, String fc, int objectID, int entryID, int pointNum,
			float ratio, String bytes, int bit, String type, int length,
			float threshold) {
		super(id, dcaindex, objId, saddr, desc, fc, objectID, entryID,
				pointNum, ratio, bytes, bit, type, length, threshold);
	}
	
	public TDcaSt(int id) {
		super(id);
	}

	public TDcaSt(int id, String ref, int subzone, int sharednums,
			String pointmode) {
		super(id, ref, subzone, sharednums);
		this.pointmode = pointmode;
	}

	public String getPointmode() {
		return this.pointmode;
	}

	public void setPointmode(String pointmode) {
		this.pointmode = pointmode;
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

	public String getLowLimit() {
		return lowLimit;
	}

	public void setLowLimit(String lowLimit) {
		this.lowLimit = lowLimit;
	}

	public String getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(String highLimit) {
		this.highLimit = highLimit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
