package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

public class TDcaiedAnswer extends BaseDcaied implements java.io.Serializable {

	private static final long serialVersionUID = 2311677574746159920L;
	private int objectOffset;
	private int objectNumber;
	private int chnId;
	private Set<TDcaObject> objects = new HashSet<TDcaObject>();
	
	
	public int getObjectOffset() {
		return objectOffset;
	}
	public void setObjectOffset(int objectOffset) {
		this.objectOffset = objectOffset;
	}
	public int getObjectNumber() {
		return objectNumber;
	}
	public void setObjectNumber(int objectNumber) {
		this.objectNumber = objectNumber;
	}
	public int getChnId() {
		return chnId;
	}
	public void setChnId(int chnId) {
		this.chnId = chnId;
	}
	public Set<TDcaObject> getObjects() {
		return objects;
	}
	public void setObjects(Set<TDcaObject> objects) {
		this.objects = objects;
	}
	public void addObject(TDcaObject object) {
		getObjects().add(object);
		object.setIedAnswer(this);
	}
	
	
}
