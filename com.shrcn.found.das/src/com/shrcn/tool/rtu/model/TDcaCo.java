package com.shrcn.tool.rtu.model;

// Generated 2012-2-8 13:44:26 by Hibernate Tools 3.2.2.GA

/**
 * TDcaCo generated by hbm2java
 */
public class TDcaCo extends BaseDcaPoint {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ctlmode;
	private String sbotimeout;
	private int test;
	private int sync;
	private int lock;
	private String calExp;
	private String offset;
	
	//Answer
//	private String saddr="";
	private String sendOnBuf="";
	private String answerOnBuf="";
	private String sendOffBuf="";
	private String answerOffBuf="";
	private String stValAddr="";
	
	
	

	public TDcaCo() {
	}
	
	public TDcaCo(int id, int dcaindex, int objId, int saddr,
			String desc, String fc, int objectID, int entryID, int pointNum) {
		super(id, dcaindex, objId, saddr, desc, fc, objectID, entryID, pointNum);
	}

	public TDcaCo(int id) {
		super(id);
	}

	public TDcaCo(int id, String ref, int subzone, int sharednums,
			String ctlmode, String sbotimeout, int test, int sync, int lock) {
		super(id, ref, subzone, sharednums);
		this.ctlmode = ctlmode;
		this.sbotimeout = sbotimeout;
		this.test = test;
		this.sync = sync;
		this.lock = lock;
	}

	public String getCtlmode() {
		return this.ctlmode;
	}

	public void setCtlmode(String ctlmode) {
		this.ctlmode = ctlmode;
	}

	public String getSbotimeout() {
		return this.sbotimeout;
	}

	public void setSbotimeout(String sbotimeout) {
		this.sbotimeout = sbotimeout;
	}

	public int getTest() {
		return this.test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public int getSync() {
		return this.sync;
	}

	public void setSync(int sync) {
		this.sync = sync;
	}

	public int getLock() {
		return this.lock;
	}

	public void setLock(int lock) {
		this.lock = lock;
	}

	public String getCalExp() {
		return calExp;
	}

	public void setCalExp(String calExp) {
		this.calExp = calExp;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}


	public String getSendOnBuf() {
		return sendOnBuf;
	}

	public void setSendOnBuf(String sendOnBuf) {
		this.sendOnBuf = sendOnBuf;
	}

	public String getAnswerOnBuf() {
		return answerOnBuf;
	}

	public void setAnswerOnBuf(String answerOnBuf) {
		this.answerOnBuf = answerOnBuf;
	}

	public String getSendOffBuf() {
		return sendOffBuf;
	}

	public void setSendOffBuf(String sendOffBuf) {
		this.sendOffBuf = sendOffBuf;
	}

	public String getAnswerOffBuf() {
		return answerOffBuf;
	}

	public void setAnswerOffBuf(String answerOffBuf) {
		this.answerOffBuf = answerOffBuf;
	}

	public String getStValAddr() {
		return stValAddr;
	}

	public void setStValAddr(String stValAddr) {
		this.stValAddr = stValAddr;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
