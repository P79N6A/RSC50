package com.synet.tool.rsc.model;

/**
 * 保护参数
 * Created by chunc on 2018/8/7.
 */
public class Tb1060SpfcdaEntity {
    private String f1060Code;
    private String f1060RefAddr;
    private int f1060Index;
    private int f1060DataType;
    private String f1060Desc;
    private String f1060Unit;
    private Float f1060StepSize;
    private Float f1060ValueMin;
    private Float f1060ValueMax;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1060Code() {
        return f1060Code;
    }
    
    public void setF1060Code(String f1060Code) {
        this.f1060Code = f1060Code;
    }

    public String getF1060RefAddr() {
        return f1060RefAddr;
    }

    public void setF1060RefAddr(String f1060RefAddr) {
        this.f1060RefAddr = f1060RefAddr;
    }

    public int getF1060Index() {
        return f1060Index;
    }

    public void setF1060Index(int f1060Index) {
        this.f1060Index = f1060Index;
    }

    public int getF1060DataType() {
        return f1060DataType;
    }

    public void setF1060DataType(int f1060DataType) {
        this.f1060DataType = f1060DataType;
    }

    public String getF1060Desc() {
        return f1060Desc;
    }

    public void setF1060Desc(String f1060Desc) {
        this.f1060Desc = f1060Desc;
    }

    public String getF1060Unit() {
        return f1060Unit;
    }

    public void setF1060Unit(String f1060Unit) {
        this.f1060Unit = f1060Unit;
    }

    public Float getF1060StepSize() {
        return f1060StepSize;
    }

    public void setF1060StepSize(Float f1060StepSize) {
        this.f1060StepSize = f1060StepSize;
    }

    public Float getF1060ValueMin() {
        return f1060ValueMin;
    }

    public void setF1060ValueMin(Float f1060ValueMin) {
        this.f1060ValueMin = f1060ValueMin;
    }

    public Float getF1060ValueMax() {
        return f1060ValueMax;
    }

    public void setF1060ValueMax(Float f1060ValueMax) {
        this.f1060ValueMax = f1060ValueMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1060SpfcdaEntity that = (Tb1060SpfcdaEntity) o;

        if (f1060DataType != that.f1060DataType) return false;
        if (f1060Index != that.f1060Index) return false;
        if (f1060Code != null ? !f1060Code.equals(that.f1060Code) : that.f1060Code != null) return false;
        if (f1060Desc != null ? !f1060Desc.equals(that.f1060Desc) : that.f1060Desc != null) return false;
        if (f1060RefAddr != null ? !f1060RefAddr.equals(that.f1060RefAddr) : that.f1060RefAddr != null) return false;
        if (f1060StepSize != null ? !f1060StepSize.equals(that.f1060StepSize) : that.f1060StepSize != null)
            return false;
        if (f1060Unit != null ? !f1060Unit.equals(that.f1060Unit) : that.f1060Unit != null) return false;
        if (f1060ValueMax != null ? !f1060ValueMax.equals(that.f1060ValueMax) : that.f1060ValueMax != null)
            return false;
        if (f1060ValueMin != null ? !f1060ValueMin.equals(that.f1060ValueMin) : that.f1060ValueMin != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1060Code != null ? f1060Code.hashCode() : 0;
        result = 31 * result + (f1060RefAddr != null ? f1060RefAddr.hashCode() : 0);
        result = 31 * result + f1060Index;
        result = 31 * result + f1060DataType;
        result = 31 * result + (f1060Desc != null ? f1060Desc.hashCode() : 0);
        result = 31 * result + (f1060Unit != null ? f1060Unit.hashCode() : 0);
        result = 31 * result + (f1060StepSize != null ? f1060StepSize.hashCode() : 0);
        result = 31 * result + (f1060ValueMin != null ? f1060ValueMin.hashCode() : 0);
        result = 31 * result + (f1060ValueMax != null ? f1060ValueMax.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
