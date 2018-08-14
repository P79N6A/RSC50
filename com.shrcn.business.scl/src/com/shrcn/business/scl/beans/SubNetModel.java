package com.shrcn.business.scl.beans;

import java.util.ArrayList;
import java.util.List;

public class SubNetModel {
	private String subNetName;
	private List<SMVConfigModel> smvCMs= new ArrayList<SMVConfigModel>();
	private List<GSEConfigModel> gseCMs= new ArrayList<GSEConfigModel>();
	private List<MMSConfigModel> mmsCMS= new ArrayList<MMSConfigModel>();

    public SubNetModel(String subNetName) {
	      this.subNetName=subNetName;
    }

	public String getSubNetName() {
		return subNetName;
	}

	public void setSubNetName(String subNetName) {
		this.subNetName = subNetName;
	}

	public List<SMVConfigModel> getSmvCMs() {
		return smvCMs;
	}

	public void setSmvCMs(List<SMVConfigModel> smvCMs) {
		this.smvCMs = smvCMs;
	}

	public List<GSEConfigModel> getGseCMs() {
		return gseCMs;
	}

	public void setGseCMs(List<GSEConfigModel> gseCMs) {
		this.gseCMs = gseCMs;
	}

	public List<MMSConfigModel> getMmsCMS() {
		return mmsCMS;
	}

	public void setMmsCMS(List<MMSConfigModel> mmsCMS) {
		this.mmsCMS = mmsCMS;
	}
  

}