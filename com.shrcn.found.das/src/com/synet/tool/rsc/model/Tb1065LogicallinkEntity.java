package com.synet.tool.rsc.model;

import java.util.Set;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1065LogicallinkEntity {
    private String f1065Code;
    private int f1065Type;
    private String f1065Appid;
    private Tb1046IedEntity tb1046IedByF1046CodeIedRecv;
    private Tb1046IedEntity tb1046IedByF1046CodeIedSend;
    private Set<Tb1063CircuitEntity> tb1063CircuitsByF1065Code;

    public String getF1065Code() {
        return f1065Code;
    }

    public void setF1065Code(String f1065Code) {
        this.f1065Code = f1065Code;
    }

    public int getF1065Type() {
        return f1065Type;
    }

    public void setF1065Type(int f1065Type) {
        this.f1065Type = f1065Type;
    }

    public String getF1065Appid() {
        return f1065Appid;
    }

    public void setF1065Appid(String f1065Appid) {
        this.f1065Appid = f1065Appid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1065LogicallinkEntity that = (Tb1065LogicallinkEntity) o;

        if (f1065Type != that.f1065Type) return false;
        if (f1065Appid != null ? !f1065Appid.equals(that.f1065Appid) : that.f1065Appid != null) return false;
        if (f1065Code != null ? !f1065Code.equals(that.f1065Code) : that.f1065Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1065Code != null ? f1065Code.hashCode() : 0;
        result = 31 * result + f1065Type;
        result = 31 * result + (f1065Appid != null ? f1065Appid.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeIedRecv() {
        return tb1046IedByF1046CodeIedRecv;
    }

    public void setTb1046IedByF1046CodeIedRecv(Tb1046IedEntity tb1046IedByF1046CodeIedRecv) {
        this.tb1046IedByF1046CodeIedRecv = tb1046IedByF1046CodeIedRecv;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeIedSend() {
        return tb1046IedByF1046CodeIedSend;
    }

    public void setTb1046IedByF1046CodeIedSend(Tb1046IedEntity tb1046IedByF1046CodeIedSend) {
        this.tb1046IedByF1046CodeIedSend = tb1046IedByF1046CodeIedSend;
    }

	public Set<Tb1063CircuitEntity> getTb1063CircuitsByF1065Code() {
		return tb1063CircuitsByF1065Code;
	}

	public void setTb1063CircuitsByF1065Code(
			Set<Tb1063CircuitEntity> tb1063CircuitsByF1065Code) {
		this.tb1063CircuitsByF1065Code = tb1063CircuitsByF1065Code;
	}

}
