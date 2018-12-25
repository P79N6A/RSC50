package com.synet.tool.rsc.compare;

import java.util.ArrayList;
import java.util.List;

public class Difference {

	private String type;
	private String name;
	private String desc;
	private String newName;
	private String newDesc;
	private String msg;
	private OP op;
	private Difference parent;
	private List<Difference> children = new ArrayList<Difference>();

	public Difference(String type, String name) {
		this(null, type, name, null, null);
	}
	
	public Difference(Difference parent, String type, String name, String msg, OP op) {
		this.parent = parent;
		this.type = type;
		this.name = name;
		this.msg = msg;
		this.op = (op == null) ? OP.NONE : op;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}

	public Difference getParent() {
		return parent;
	}

	public void setParent(Difference parent) {
		this.parent = parent;
	}

	public List<Difference> getChildren() {
		return children;
	}

	public void setChildren(List<Difference> children) {
		this.children = children;
	}

	public void addChild(Difference child) {
		children.add(child);
	}
	
	private int getLevel() {
		int level = 0;
		Difference parent1 = getParent();
		while (parent1 != null) {
			level++;
			parent1 = parent1.getParent();
		}
		return level;
	}
	
	public void print() {
		int level = getLevel();
		String out = "";
		for (int i=0; i<level; i++) {
			out += "---";
		}
		out += type + "\t" + name + "\t" + msg + "\t" + (op==null ? "" : op.getDesc());
		System.out.println(out);
		for (Difference diff : getChildren()) {
			diff.print();
		}
	}
}
