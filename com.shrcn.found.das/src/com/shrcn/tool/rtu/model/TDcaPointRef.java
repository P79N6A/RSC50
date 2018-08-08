package com.shrcn.tool.rtu.model;

import java.io.Serializable;

import com.shrcn.found.common.util.ObjectUtil;

/**
 * This is an object that contains data related to the T_DcaPointRef table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="T_DcaPointRef"
 */

public class TDcaPointRef implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// constructors
	public TDcaPointRef() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public TDcaPointRef(TDcaPointRefId id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private TDcaPointRefId id;

	// fields
	private java.lang.String pointtype;
	private int reftype;
	private int saddr;
	private int dpaindex;
	private int seqnum;
	private int objType;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id
	 */
	public TDcaPointRefId getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(TDcaPointRefId id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	/**
	 * Return the value associated with the column: POINTTYPE
	 */
	public java.lang.String getPointtype() {
		return pointtype;
	}

	/**
	 * Set the value related to the column: POINTTYPE
	 * 
	 * @param pointtype
	 *            the POINTTYPE value
	 */
	public void setPointtype(java.lang.String pointtype) {
		this.pointtype = pointtype;
	}

	/**
	 * Return the value associated with the column: POINTTYPE
	 */
	public int getReftype() {
		return reftype;
	}

	/**
	 * Set the value related to the column: POINTTYPE
	 * 
	 * @param reftype
	 *            the POINTTYPE value
	 */
	public void setReftype(int reftype) {
		this.reftype = reftype;
	}

	/**
	 * Return the value associated with the column: SADDR
	 */
	public int getSaddr() {
		return saddr;
	}

	/**
	 * Set the value related to the column: SADDR
	 * 
	 * @param saddr
	 *            the SADDR value
	 */
	public void setSaddr(int saddr) {
		this.saddr = saddr;
	}

	/**
	 * Return the value associated with the column: DPAINDEX
	 */
	public int getDpaindex() {
		return dpaindex;
	}

	/**
	 * Set the value related to the column: DPAINDEX
	 * 
	 * @param dpaindex
	 *            the DPAINDEX value
	 */
	public void setDpaindex(int dpaindex) {
		this.dpaindex = dpaindex;
	}

	/**
	 * Return the value associated with the column: SEQNUM
	 */
	public int getSeqnum() {
		return seqnum;
	}

	/**
	 * Set the value related to the column: SEQNUM
	 * 
	 * @param seqnum
	 *            the SEQNUM value
	 */
	public void setSeqnum(int seqnum) {
		this.seqnum = seqnum;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof TDcaPointRef))
			return false;
		else {
			TDcaPointRef tDcaPointRef = (TDcaPointRef) obj;
			if (null == this.getId() || null == tDcaPointRef.getId())
				return false;
			else
				return (this.getId().equals(tDcaPointRef.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":"
						+ this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public String toString() {
		return super.toString();
	}
	public TDcaPointRef copy() {
		return (TDcaPointRef) ObjectUtil.duplicate(this);
	}
}