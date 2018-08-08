package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1061PoutEntity {
    private String f1061Code;
    private String f1046Code;
    private String cbCode;
    private String f1061RefAddr;
    private int f1061Index;
    private String f1061Desc;
    private Integer f1061Type;
    private String f1064Code;
    private String dataCode;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Tb1055GcbEntity tb1055GcbByCbCode;
    private Tb1056SvcbEntity tb1056SvcbByCbCode;
    private Tb1064StrapEntity tb1064StrapByF1064Code;
    private Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code;
    private Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code_0;
    private Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code_1;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_0;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_1;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_2;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_3;
    private Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_4;

    public String getF1061Code() {
        return f1061Code;
    }

    public void setF1061Code(String f1061Code) {
        this.f1061Code = f1061Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getCbCode() {
        return cbCode;
    }

    public void setCbCode(String cbCode) {
        this.cbCode = cbCode;
    }

    public String getF1061RefAddr() {
        return f1061RefAddr;
    }

    public void setF1061RefAddr(String f1061RefAddr) {
        this.f1061RefAddr = f1061RefAddr;
    }

    public int getF1061Index() {
        return f1061Index;
    }

    public void setF1061Index(int f1061Index) {
        this.f1061Index = f1061Index;
    }

    public String getF1061Desc() {
        return f1061Desc;
    }

    public void setF1061Desc(String f1061Desc) {
        this.f1061Desc = f1061Desc;
    }

    public Integer getF1061Type() {
        return f1061Type;
    }

    public void setF1061Type(Integer f1061Type) {
        this.f1061Type = f1061Type;
    }

    public String getF1064Code() {
        return f1064Code;
    }

    public void setF1064Code(String f1064Code) {
        this.f1064Code = f1064Code;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1061PoutEntity that = (Tb1061PoutEntity) o;

        if (f1061Index != that.f1061Index) return false;
        if (cbCode != null ? !cbCode.equals(that.cbCode) : that.cbCode != null) return false;
        if (dataCode != null ? !dataCode.equals(that.dataCode) : that.dataCode != null) return false;
        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1061Code != null ? !f1061Code.equals(that.f1061Code) : that.f1061Code != null) return false;
        if (f1061Desc != null ? !f1061Desc.equals(that.f1061Desc) : that.f1061Desc != null) return false;
        if (f1061RefAddr != null ? !f1061RefAddr.equals(that.f1061RefAddr) : that.f1061RefAddr != null) return false;
        if (f1061Type != null ? !f1061Type.equals(that.f1061Type) : that.f1061Type != null) return false;
        if (f1064Code != null ? !f1064Code.equals(that.f1064Code) : that.f1064Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1061Code != null ? f1061Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (cbCode != null ? cbCode.hashCode() : 0);
        result = 31 * result + (f1061RefAddr != null ? f1061RefAddr.hashCode() : 0);
        result = 31 * result + f1061Index;
        result = 31 * result + (f1061Desc != null ? f1061Desc.hashCode() : 0);
        result = 31 * result + (f1061Type != null ? f1061Type.hashCode() : 0);
        result = 31 * result + (f1064Code != null ? f1064Code.hashCode() : 0);
        result = 31 * result + (dataCode != null ? dataCode.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Tb1055GcbEntity getTb1055GcbByCbCode() {
        return tb1055GcbByCbCode;
    }

    public void setTb1055GcbByCbCode(Tb1055GcbEntity tb1055GcbByCbCode) {
        this.tb1055GcbByCbCode = tb1055GcbByCbCode;
    }

    public Tb1056SvcbEntity getTb1056SvcbByCbCode() {
        return tb1056SvcbByCbCode;
    }

    public void setTb1056SvcbByCbCode(Tb1056SvcbEntity tb1056SvcbByCbCode) {
        this.tb1056SvcbByCbCode = tb1056SvcbByCbCode;
    }

    public Tb1064StrapEntity getTb1064StrapByF1064Code() {
        return tb1064StrapByF1064Code;
    }

    public void setTb1064StrapByF1064Code(Tb1064StrapEntity tb1064StrapByF1064Code) {
        this.tb1064StrapByF1064Code = tb1064StrapByF1064Code;
    }

    public Collection<Tb1063CircuitEntity> getTb1063CircuitsByF1061Code() {
        return tb1063CircuitsByF1061Code;
    }

    public void setTb1063CircuitsByF1061Code(Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code) {
        this.tb1063CircuitsByF1061Code = tb1063CircuitsByF1061Code;
    }

    public Collection<Tb1063CircuitEntity> getTb1063CircuitsByF1061Code_0() {
        return tb1063CircuitsByF1061Code_0;
    }

    public void setTb1063CircuitsByF1061Code_0(Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code_0) {
        this.tb1063CircuitsByF1061Code_0 = tb1063CircuitsByF1061Code_0;
    }

    public Collection<Tb1063CircuitEntity> getTb1063CircuitsByF1061Code_1() {
        return tb1063CircuitsByF1061Code_1;
    }

    public void setTb1063CircuitsByF1061Code_1(Collection<Tb1063CircuitEntity> tb1063CircuitsByF1061Code_1) {
        this.tb1063CircuitsByF1061Code_1 = tb1063CircuitsByF1061Code_1;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code() {
        return tb1067CtvtsecondariesByF1061Code;
    }

    public void setTb1067CtvtsecondariesByF1061Code(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code) {
        this.tb1067CtvtsecondariesByF1061Code = tb1067CtvtsecondariesByF1061Code;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code_0() {
        return tb1067CtvtsecondariesByF1061Code_0;
    }

    public void setTb1067CtvtsecondariesByF1061Code_0(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_0) {
        this.tb1067CtvtsecondariesByF1061Code_0 = tb1067CtvtsecondariesByF1061Code_0;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code_1() {
        return tb1067CtvtsecondariesByF1061Code_1;
    }

    public void setTb1067CtvtsecondariesByF1061Code_1(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_1) {
        this.tb1067CtvtsecondariesByF1061Code_1 = tb1067CtvtsecondariesByF1061Code_1;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code_2() {
        return tb1067CtvtsecondariesByF1061Code_2;
    }

    public void setTb1067CtvtsecondariesByF1061Code_2(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_2) {
        this.tb1067CtvtsecondariesByF1061Code_2 = tb1067CtvtsecondariesByF1061Code_2;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code_3() {
        return tb1067CtvtsecondariesByF1061Code_3;
    }

    public void setTb1067CtvtsecondariesByF1061Code_3(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_3) {
        this.tb1067CtvtsecondariesByF1061Code_3 = tb1067CtvtsecondariesByF1061Code_3;
    }

    public Collection<Tb1067CtvtsecondaryEntity> getTb1067CtvtsecondariesByF1061Code_4() {
        return tb1067CtvtsecondariesByF1061Code_4;
    }

    public void setTb1067CtvtsecondariesByF1061Code_4(Collection<Tb1067CtvtsecondaryEntity> tb1067CtvtsecondariesByF1061Code_4) {
        this.tb1067CtvtsecondariesByF1061Code_4 = tb1067CtvtsecondariesByF1061Code_4;
    }
}
