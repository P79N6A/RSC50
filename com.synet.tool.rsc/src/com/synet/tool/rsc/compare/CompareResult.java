package com.synet.tool.rsc.compare;


public class CompareResult {

	private String name;
	private String desc;
	private OP oper;
	private Status status;
	private String newName;
	private String newDesc;
	private Difference difference;
	
	public CompareResult(String name, String desc) {
		this.name = name;
		this.desc = desc;
		this.status = Status.UnParsed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public OP getOper() {
		return oper;
	}

	public void setOper(OP oper) {
		this.oper = oper;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewDesc() {
		return newDesc;
	}

	public void setNewDesc(String newDesc) {
		this.newDesc = newDesc;
	}

	public Difference getDifference() {
		return difference;
	}

	public void setDifference(Difference difference) {
		this.difference = difference;
	}
	
	@Override
	public String toString() {
		return "装置名称：" + name + "，装置描述：" + desc + "，操作：" + oper.getDesc();
	}
}
