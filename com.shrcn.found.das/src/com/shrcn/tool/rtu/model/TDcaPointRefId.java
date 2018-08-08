package com.shrcn.tool.rtu.model;

import java.io.Serializable;

public class TDcaPointRefId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int hashCode = Integer.MIN_VALUE;

	private int pointId;
	private int refId;

	public TDcaPointRefId() {
	}

	public TDcaPointRefId(int pointId, int refId) {

		this.setPointId(pointId);
		this.setRefId(refId);
	}

	/**
	 * Return the value associated with the column: POINTID
	 */
	public int getPointId() {
		return pointId;
	}

	/**
	 * Set the value related to the column: POINTID
	 * 
	 * @param pointId
	 *            the POINTID value
	 */
	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	/**
	 * Return the value associated with the column: REFID
	 */
	public int getRefId() {
		return refId;
	}

	/**
	 * Set the value related to the column: REFID
	 * 
	 * @param refId
	 *            the REFID value
	 */
	public void setRefId(int refId) {
		this.refId = refId;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof TDcaPointRefId))
			return false;
		else {
			TDcaPointRefId mObj = (TDcaPointRefId) obj;
			if (this.getPointId() != mObj.getPointId()) {
				return false;
			}
			if (this.getRefId() != mObj.getRefId()) {
				return false;
			}
			return true;
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			StringBuilder sb = new StringBuilder();
			sb.append(new java.lang.Integer(this.getPointId()).hashCode());
			sb.append(":");
			sb.append(new java.lang.Integer(this.getRefId()).hashCode());
			sb.append(":");
			this.hashCode = sb.toString().hashCode();
		}
		return this.hashCode;
	}

}