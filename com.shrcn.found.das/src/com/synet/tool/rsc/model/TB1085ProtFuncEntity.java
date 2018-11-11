package com.synet.tool.rsc.model;

public class TB1085ProtFuncEntity {
	private String f1085CODE;
	private Tb1046IedEntity tb1046ByF1046CODE;
	private TB1084FuncClassEntity tb1804ByF1084CODE;
	
	public String getF1085CODE() {
		return f1085CODE;
	}
	public void setF1085CODE(String f1085code) {
		f1085CODE = f1085code;
	}
	public TB1084FuncClassEntity getTb1804ByF1084CODE() {
		return tb1804ByF1084CODE;
	}
	public void setTb1804ByF1084CODE(TB1084FuncClassEntity tb1804ByF1084CODE) {
		this.tb1804ByF1084CODE = tb1804ByF1084CODE;
	}
	public Tb1046IedEntity getTb1046ByF1046CODE() {
		return tb1046ByF1046CODE;
	}
	public void setTb1046ByF1046CODE(Tb1046IedEntity tb1046ByF1046CODE) {
		this.tb1046ByF1046CODE = tb1046ByF1046CODE;
	}

}
