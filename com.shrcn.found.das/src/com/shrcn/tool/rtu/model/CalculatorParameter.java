package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

/**
 * TApplication generated by hbm2java
 */
public class CalculatorParameter  implements java.io.Serializable {

	/**
	 *  
	 */ 
	private static final long serialVersionUID = 1L;
	

     private String name;
     private String desc;
     private String path;
     
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}


