package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1090LineprotfiberEntity extends Deletable {
    private String f1090Code;
//    private String f1046Code;
    private String f1090Desc;
    private String f1090FiberNo;
    private String f1090PortNo;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1090Code() {
        return f1090Code;
    }

    public void setF1090Code(String f1090Code) {
        this.f1090Code = f1090Code;
    }

//    public String getF1046Code() {
//        return f1046Code;
//    }
//
//    public void setF1046Code(String f1046Code) {
//        this.f1046Code = f1046Code;
//    }

    public String getF1090Desc() {
        return f1090Desc;
    }

    public void setF1090Desc(String f1090Desc) {
        this.f1090Desc = f1090Desc;
    }

    public String getF1090FiberNo() {
        return f1090FiberNo;
    }

    public void setF1090FiberNo(String f1090FiberNo) {
        this.f1090FiberNo = f1090FiberNo;
    }

    public String getF1090PortNo() {
        return f1090PortNo;
    }

    public void setF1090PortNo(String f1090PortNo) {
        this.f1090PortNo = f1090PortNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1090LineprotfiberEntity that = (Tb1090LineprotfiberEntity) o;

//        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1090Code != null ? !f1090Code.equals(that.f1090Code) : that.f1090Code != null) return false;
        if (f1090Desc != null ? !f1090Desc.equals(that.f1090Desc) : that.f1090Desc != null) return false;
        if (f1090FiberNo != null ? !f1090FiberNo.equals(that.f1090FiberNo) : that.f1090FiberNo != null) return false;
        if (f1090PortNo != null ? !f1090PortNo.equals(that.f1090PortNo) : that.f1090PortNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1090Code != null ? f1090Code.hashCode() : 0;
//        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1090Desc != null ? f1090Desc.hashCode() : 0);
        result = 31 * result + (f1090FiberNo != null ? f1090FiberNo.hashCode() : 0);
        result = 31 * result + (f1090PortNo != null ? f1090PortNo.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
