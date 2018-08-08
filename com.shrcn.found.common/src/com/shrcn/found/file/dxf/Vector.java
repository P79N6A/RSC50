/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.file.dxf;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2011-6-15
 */
/**
 * $Log: Vector.java,v $
 * Revision 1.1  2012/06/18 09:38:15  cchun
 * Refactor:转移项目
 *
 * Revision 1.1  2011/06/17 06:32:53  cchun
 * Add:dxf导出API
 *
 */
public class Vector {
	final static int X=0;
	final static int Y=1;
	final static int Z=2;
	
	public final static Vector X_AXIS = new Vector(1,0,0);
	public final static Vector Y_AXIS = new Vector(0,1,0);
	public final static Vector Z_AXIS = new Vector(0,0,1);
	
	private double[] coord = {0,0,0};
	
	public Vector() {
		coord = new double[Z+1];
		//coord = {0,0,0};
	}
	
	public Vector(double x, double y) {
		coord = new double[Z+1];
		coord[X] = x;
		coord[Y] = y;
	}
	
	public Vector(double x, double y, double z) {
		coord = new double[Z+1];
		coord[X] = x;
		coord[Y] = y;
		coord[Z] = z;
	}
	
	public boolean equals(Vector v) {
		return this.sub(v).abs() < 0.0000001; 
	}
	
	public double abs() {
		//returns the length of the vector
		return Math.sqrt(coord[X]*coord[X] + coord[Y]*coord[Y] + coord[Z]*coord[Z]);
	}
	
	public String toString() {
		return "<" + coord[X] + "," + coord[Y] + ","
				+ coord[Z] + ">";
	}	
	
	public Vector normalize() {
		double abs = this.abs();
		return new Vector(coord[X]/abs,coord[Y]/abs,coord[Z]/abs);
	}
	
	public double getX() {
		return coord[X];
	}
	
	public double getY() {
		return coord[Y];
	}
	
	public double getZ() {
		return coord[Z];
	}
	
	public Vector mul(double factor) {
		// scales the vector
		return new Vector(coord[X]*factor,coord[Y]*factor,coord[Z]*factor);
	} 
	
	public double prod(Vector v) {
		// inner product of two vectors
		return coord[X]*v.getX() + coord[Y]*v.getY() + coord[Z]*v.getZ();
	}
	
	public Vector div (double factor)  {
		return this.mul(1/factor);
	}
	
	public Vector add(Vector v) {
		return new Vector(coord[X]+v.getX(), coord[Y]+v.getY(), coord[Z]+v.getZ());
	}
	
	public Vector sub(Vector v) {
		return this.add(v.mul(-1));
	}
	
	/**
	 * Returns the angle of a 2-dimensional vector (u,v) with the u-axis 
	 *
	 * @param v v-coordinate of the vector
	 * @param u u-coordinate of the vector
	 * @return a value from (-180..180)
	 */
	static public double atan2(double v, double u)  {
		if (u==0) {
			if (v>=0) return 90;
			else return -90;
		} 
		if (u>0)  return Math.atan(v/u)*180/Math.PI;
		if (v>=0) return 180 + Math.atan(v/u)*180/Math.PI;
		return Math.atan(v/u)*180/Math.PI-180;
	}
	
	public void setMaxCoord(Vector v) {
		if (v.getX() > coord[X]) coord[X] = v.getX();
		if (v.getY() > coord[Y]) coord[Y] = v.getY();
		if (v.getZ() > coord[Z]) coord[Z] = v.getZ();
	}
	
	public void setMinCoord(Vector v) {
		if (v.getX() < coord[X]) coord[X] = v.getX();
		if (v.getY() < coord[Y]) coord[Y] = v.getY();
		if (v.getZ() < coord[Z]) coord[Z] = v.getZ();
	}
};