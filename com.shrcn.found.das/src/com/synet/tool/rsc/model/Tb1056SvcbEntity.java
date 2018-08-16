package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1056SvcbEntity {
    private String f1056Code;
    private String f1056CbName;
    private String f1056Cbid;
    private String f1056MacAddr;
    private String f1056Vlanid;
    private String f1056VlanPriority;
    private String f1056Appid;
    private String f1056Dataset;
    private String f1056DsDesc;
    private String f1071Code;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Collection<Tb1061PoutEntity> tb1061PoutsByF1056Code;

    public String getF1056Code() {
        return f1056Code;
    }

    public void setF1056Code(String f1056Code) {
        this.f1056Code = f1056Code;
    }

    public String getF1056CbName() {
        return f1056CbName;
    }

    public void setF1056CbName(String f1056CbName) {
        this.f1056CbName = f1056CbName;
    }

    public String getF1056Cbid() {
        return f1056Cbid;
    }

    public void setF1056Cbid(String f1056Cbid) {
        this.f1056Cbid = f1056Cbid;
    }

    public String getF1056MacAddr() {
        return f1056MacAddr;
    }

    public void setF1056MacAddr(String f1056MacAddr) {
        this.f1056MacAddr = f1056MacAddr;
    }

    public String getF1056Vlanid() {
        return f1056Vlanid;
    }

    public void setF1056Vlanid(String f1056Vlanid) {
        this.f1056Vlanid = f1056Vlanid;
    }

    public String getF1056VlanPriority() {
        return f1056VlanPriority;
    }

    public void setF1056VlanPriority(String f1056VlanPriority) {
        this.f1056VlanPriority = f1056VlanPriority;
    }

    public String getF1056Appid() {
        return f1056Appid;
    }

    public void setF1056Appid(String f1056Appid) {
        this.f1056Appid = f1056Appid;
    }

    public String getF1056Dataset() {
        return f1056Dataset;
    }

    public void setF1056Dataset(String f1056Dataset) {
        this.f1056Dataset = f1056Dataset;
    }

    public String getF1056DsDesc() {
        return f1056DsDesc;
    }

    public void setF1056DsDesc(String f1056DsDesc) {
        this.f1056DsDesc = f1056DsDesc;
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

        Tb1056SvcbEntity that = (Tb1056SvcbEntity) o;

        if (f1056Appid != null ? !f1056Appid.equals(that.f1056Appid) : that.f1056Appid != null) return false;
        if (f1056CbName != null ? !f1056CbName.equals(that.f1056CbName) : that.f1056CbName != null) return false;
        if (f1056Cbid != null ? !f1056Cbid.equals(that.f1056Cbid) : that.f1056Cbid != null) return false;
        if (f1056Code != null ? !f1056Code.equals(that.f1056Code) : that.f1056Code != null) return false;
        if (f1056Dataset != null ? !f1056Dataset.equals(that.f1056Dataset) : that.f1056Dataset != null) return false;
        if (f1056DsDesc != null ? !f1056DsDesc.equals(that.f1056DsDesc) : that.f1056DsDesc != null) return false;
        if (f1056MacAddr != null ? !f1056MacAddr.equals(that.f1056MacAddr) : that.f1056MacAddr != null) return false;
        if (f1056VlanPriority != null ? !f1056VlanPriority.equals(that.f1056VlanPriority) : that.f1056VlanPriority != null)
            return false;
        if (f1056Vlanid != null ? !f1056Vlanid.equals(that.f1056Vlanid) : that.f1056Vlanid != null) return false;
        if (f1071Code != null ? !f1071Code.equals(that.f1071Code) : that.f1071Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1056Code != null ? f1056Code.hashCode() : 0;
        result = 31 * result + (f1056CbName != null ? f1056CbName.hashCode() : 0);
        result = 31 * result + (f1056Cbid != null ? f1056Cbid.hashCode() : 0);
        result = 31 * result + (f1056MacAddr != null ? f1056MacAddr.hashCode() : 0);
        result = 31 * result + (f1056Vlanid != null ? f1056Vlanid.hashCode() : 0);
        result = 31 * result + (f1056VlanPriority != null ? f1056VlanPriority.hashCode() : 0);
        result = 31 * result + (f1056Appid != null ? f1056Appid.hashCode() : 0);
        result = 31 * result + (f1056Dataset != null ? f1056Dataset.hashCode() : 0);
        result = 31 * result + (f1056DsDesc != null ? f1056DsDesc.hashCode() : 0);
        result = 31 * result + (f1071Code != null ? f1071Code.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Collection<Tb1061PoutEntity> getTb1061PoutsByF1056Code() {
        return tb1061PoutsByF1056Code;
    }

    public void setTb1061PoutsByF1056Code(Collection<Tb1061PoutEntity> tb1061PoutsByF1056Code) {
        this.tb1061PoutsByF1056Code = tb1061PoutsByF1056Code;
    }
}
