package com.synet.tool.rsc.model;

/**
 * 定值FCDA点TB1059_SGFCDA
 * Created by chunc on 2018/8/7.
 */
public class Tb1059SgfcdaEntity {
    private String f1059Code;
    private String f1057Code;
    private String f1059RefAddr;
    private int f1059Index;
    private int f1059DataType;
    private String f1059Desc;
    private String f1059Unit;
    private Float f1059StepSize;
    private Float f1059ValueMin;
    private Float f1059ValueMax;
    private Float f1059BaseValue;
    private Tb1057SgcbEntity tb1057SgcbByF1057Code;

    public String getF1059Code() {
        return f1059Code;
    }

    public void setF1059Code(String f1059Code) {
        this.f1059Code = f1059Code;
    }

    public String getF1057Code() {
        return f1057Code;
    }

    public void setF1057Code(String f1057Code) {
        this.f1057Code = f1057Code;
    }

    public String getF1059RefAddr() {
        return f1059RefAddr;
    }

    public void setF1059RefAddr(String f1059RefAddr) {
        this.f1059RefAddr = f1059RefAddr;
    }

    public int getF1059Index() {
        return f1059Index;
    }

    public void setF1059Index(int f1059Index) {
        this.f1059Index = f1059Index;
    }

    public int getF1059DataType() {
        return f1059DataType;
    }

    public void setF1059DataType(int f1059DataType) {
        this.f1059DataType = f1059DataType;
    }

    public String getF1059Desc() {
        return f1059Desc;
    }

    public void setF1059Desc(String f1059Desc) {
        this.f1059Desc = f1059Desc;
    }

    public String getF1059Unit() {
        return f1059Unit;
    }

    public void setF1059Unit(String f1059Unit) {
        this.f1059Unit = f1059Unit;
    }

    public Float getF1059StepSize() {
        return f1059StepSize;
    }

    public void setF1059StepSize(Float f1059StepSize) {
        this.f1059StepSize = f1059StepSize;
    }

    public Float getF1059ValueMin() {
        return f1059ValueMin;
    }

    public void setF1059ValueMin(Float f1059ValueMin) {
        this.f1059ValueMin = f1059ValueMin;
    }

    public Float getF1059ValueMax() {
        return f1059ValueMax;
    }

    public void setF1059ValueMax(Float f1059ValueMax) {
        this.f1059ValueMax = f1059ValueMax;
    }

    public Float getF1059BaseValue() {
        return f1059BaseValue;
    }

    public void setF1059BaseValue(Float f1059BaseValue) {
        this.f1059BaseValue = f1059BaseValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1059SgfcdaEntity that = (Tb1059SgfcdaEntity) o;

        if (f1059DataType != that.f1059DataType) return false;
        if (f1059Index != that.f1059Index) return false;
        if (f1057Code != null ? !f1057Code.equals(that.f1057Code) : that.f1057Code != null) return false;
        if (f1059BaseValue != null ? !f1059BaseValue.equals(that.f1059BaseValue) : that.f1059BaseValue != null)
            return false;
        if (f1059Code != null ? !f1059Code.equals(that.f1059Code) : that.f1059Code != null) return false;
        if (f1059Desc != null ? !f1059Desc.equals(that.f1059Desc) : that.f1059Desc != null) return false;
        if (f1059RefAddr != null ? !f1059RefAddr.equals(that.f1059RefAddr) : that.f1059RefAddr != null) return false;
        if (f1059StepSize != null ? !f1059StepSize.equals(that.f1059StepSize) : that.f1059StepSize != null)
            return false;
        if (f1059Unit != null ? !f1059Unit.equals(that.f1059Unit) : that.f1059Unit != null) return false;
        if (f1059ValueMax != null ? !f1059ValueMax.equals(that.f1059ValueMax) : that.f1059ValueMax != null)
            return false;
        if (f1059ValueMin != null ? !f1059ValueMin.equals(that.f1059ValueMin) : that.f1059ValueMin != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1059Code != null ? f1059Code.hashCode() : 0;
        result = 31 * result + (f1057Code != null ? f1057Code.hashCode() : 0);
        result = 31 * result + (f1059RefAddr != null ? f1059RefAddr.hashCode() : 0);
        result = 31 * result + f1059Index;
        result = 31 * result + f1059DataType;
        result = 31 * result + (f1059Desc != null ? f1059Desc.hashCode() : 0);
        result = 31 * result + (f1059Unit != null ? f1059Unit.hashCode() : 0);
        result = 31 * result + (f1059StepSize != null ? f1059StepSize.hashCode() : 0);
        result = 31 * result + (f1059ValueMin != null ? f1059ValueMin.hashCode() : 0);
        result = 31 * result + (f1059ValueMax != null ? f1059ValueMax.hashCode() : 0);
        result = 31 * result + (f1059BaseValue != null ? f1059BaseValue.hashCode() : 0);
        return result;
    }

    public Tb1057SgcbEntity getTb1057SgcbByF1057Code() {
        return tb1057SgcbByF1057Code;
    }

    public void setTb1057SgcbByF1057Code(Tb1057SgcbEntity tb1057SgcbByF1057Code) {
        this.tb1057SgcbByF1057Code = tb1057SgcbByF1057Code;
    }
}
