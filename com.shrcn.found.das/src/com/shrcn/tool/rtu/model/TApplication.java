package com.shrcn.tool.rtu.model;

import java.util.HashSet;
import java.util.Set;

/**
 * TApplication generated by hbm2java
 */
public class TApplication  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
     private int id;
     private int index;
     private String runlevel;
     private String runmode;
     private int mxnums;
     private int stnums;
     private int conums;
     private int spnums;
     private int sgnums;
     private int senums;
     private TApptype apptype = new TApptype();
     
     private Set<TPropertyvalue> properties = new HashSet<TPropertyvalue>();
     

	public TApplication() {
    }
	
    public TApplication(int id) {
        this.id = id;
    }
    
    public TApplication(int id, int index, String runlevel, String runmode, int mxnums, int stnums, int conums, int spnums, int sgnums, int senums) {
       this.id = id;
       this.index = index;
       this.runlevel = runlevel;
       this.runmode = runmode;
       this.mxnums = mxnums;
       this.stnums = stnums;
       this.conums = conums;
       this.spnums = spnums;
       this.sgnums = sgnums;
       this.senums = senums;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getRunlevel() {
		return runlevel;
	}

	public void setRunlevel(String runlevel) {
		this.runlevel = runlevel;
	}

	public String getRunmode() {
		return runmode;
	}

	public void setRunmode(String runmode) {
		this.runmode = runmode;
	}

	public int getMxnums() {
        return this.mxnums;
    }
    
    public void setMxnums(int mxnums) {
        this.mxnums = mxnums;
    }
    public int getStnums() {
        return this.stnums;
    }
    
    public void setStnums(int stnums) {
        this.stnums = stnums;
    }
    public int getConums() {
        return this.conums;
    }
    
    public void setConums(int conums) {
        this.conums = conums;
    }
    public int getSpnums() {
        return this.spnums;
    }
    
    public void setSpnums(int spnums) {
        this.spnums = spnums;
    }
    public int getSgnums() {
        return this.sgnums;
    }
    
    public void setSgnums(int sgnums) {
        this.sgnums = sgnums;
    }
    public int getSenums() {
        return this.senums;
    }
    
    public void setSenums(int senums) {
        this.senums = senums;
    }

	public TApptype getApptype() {
		return apptype;
	}

	public void setApptype(TApptype apptype) {
		this.apptype = apptype;
	}

	public Set<TPropertyvalue> getProperties() {
		return properties;
	}

	public void setProperties(Set<TPropertyvalue> properties) {
		this.properties = properties;
	}

	public void addPropertyvalue(TPropertyvalue pv) {
		getProperties().add(pv);
		pv.setApplication(this);
	}

	/**
	 * 获取指定属性。
	 * @param property
	 * @return
	 */
	public TPropertyvalue getProperty(String property) {
		for (TPropertyvalue pv : getProperties()) {
			if (property.equals(pv.getProperty().getName())) {
				return pv;
			}
		}
		return null;
	}
	
	/**
	 * 获取指定属性值。
	 * @param property
	 * @return
	 */
	public String getPropertyValue(String property) {
		TPropertyvalue pv = getProperty(property);
		if (pv == null)
			return null;
		return pv.getValue();
	}
}


