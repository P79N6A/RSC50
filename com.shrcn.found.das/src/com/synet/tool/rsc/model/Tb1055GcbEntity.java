package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1055GcbEntity {
    private String f1055Code;
    private String f1046Code;
    private String f1055Cbname;
    private String f1055Cbid;
    private String f1055MacAddr;
    private String f1055Vlanid;
    private String f1055VlanPriority;
    private String f1055Appid;
    private String f1055Dataset;
    private String f1055Desc;
    private String f1071Code;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Collection<Tb1061PoutEntity> tb1061PoutsByF1055Code;

    public String getF1055Code() {
        return f1055Code;
    }

    public void setF1055Code(String f1055Code) {
        this.f1055Code = f1055Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1055Cbname() {
        return f1055Cbname;
    }

    public void setF1055Cbname(String f1055Cbname) {
        this.f1055Cbname = f1055Cbname;
    }

    public String getF1055Cbid() {
        return f1055Cbid;
    }

    public void setF1055Cbid(String f1055Cbid) {
        this.f1055Cbid = f1055Cbid;
    }

    public String getF1055MacAddr() {
        return f1055MacAddr;
    }

    public void setF1055MacAddr(String f1055MacAddr) {
        this.f1055MacAddr = f1055MacAddr;
    }

    public String getF1055Vlanid() {
        return f1055Vlanid;
    }

    public void setF1055Vlanid(String f1055Vlanid) {
        this.f1055Vlanid = f1055Vlanid;
    }

    public String getF1055VlanPriority() {
        return f1055VlanPriority;
    }

    public void setF1055VlanPriority(String f1055VlanPriority) {
        this.f1055VlanPriority = f1055VlanPriority;
    }

    public String getF1055Appid() {
        return f1055Appid;
    }

    public void setF1055Appid(String f1055Appid) {
        this.f1055Appid = f1055Appid;
    }

    public String getF1055Dataset() {
        return f1055Dataset;
    }

    public void setF1055Dataset(String f1055Dataset) {
        this.f1055Dataset = f1055Dataset;
    }

    public String getF1055Desc() {
        return f1055Desc;
    }

    public void setF1055Desc(String f1055Desc) {
        this.f1055Desc = f1055Desc;
    }

    public String getF1071Code() {
        return f1071Code;
    }

    public void setF1071Code(String f1071Code) {
        this.f1071Code = f1071Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1055GcbEntity that = (Tb1055GcbEntity) o;

        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1055Appid != null ? !f1055Appid.equals(that.f1055Appid) : that.f1055Appid != null) return false;
        if (f1055Cbid != null ? !f1055Cbid.equals(that.f1055Cbid) : that.f1055Cbid != null) return false;
        if (f1055Cbname != null ? !f1055Cbname.equals(that.f1055Cbname) : that.f1055Cbname != null) return false;
        if (f1055Code != null ? !f1055Code.equals(that.f1055Code) : that.f1055Code != null) return false;
        if (f1055Dataset != null ? !f1055Dataset.equals(that.f1055Dataset) : that.f1055Dataset != null) return false;
        if (f1055Desc != null ? !f1055Desc.equals(that.f1055Desc) : that.f1055Desc != null) return false;
        if (f1055MacAddr != null ? !f1055MacAddr.equals(that.f1055MacAddr) : that.f1055MacAddr != null) return false;
        if (f1055VlanPriority != null ? !f1055VlanPriority.equals(that.f1055VlanPriority) : that.f1055VlanPriority != null)
            return false;
        if (f1055Vlanid != null ? !f1055Vlanid.equals(that.f1055Vlanid) : that.f1055Vlanid != null) return false;
        if (f1071Code != null ? !f1071Code.equals(that.f1071Code) : that.f1071Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1055Code != null ? f1055Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1055Cbname != null ? f1055Cbname.hashCode() : 0);
        result = 31 * result + (f1055Cbid != null ? f1055Cbid.hashCode() : 0);
        result = 31 * result + (f1055MacAddr != null ? f1055MacAddr.hashCode() : 0);
        result = 31 * result + (f1055Vlanid != null ? f1055Vlanid.hashCode() : 0);
        result = 31 * result + (f1055VlanPriority != null ? f1055VlanPriority.hashCode() : 0);
        result = 31 * result + (f1055Appid != null ? f1055Appid.hashCode() : 0);
        result = 31 * result + (f1055Dataset != null ? f1055Dataset.hashCode() : 0);
        result = 31 * result + (f1055Desc != null ? f1055Desc.hashCode() : 0);
        result = 31 * result + (f1071Code != null ? f1071Code.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Collection<Tb1061PoutEntity> getTb1061PoutsByF1055Code() {
        return tb1061PoutsByF1055Code;
    }

    public void setTb1061PoutsByF1055Code(Collection<Tb1061PoutEntity> tb1061PoutsByF1055Code) {
        this.tb1061PoutsByF1055Code = tb1061PoutsByF1055Code;
    }
}
