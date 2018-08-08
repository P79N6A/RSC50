package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1073LlinkphyrelationEntity {
    private String f1073Code;
    private String f1065Code;
    private String f1053Code;
    private Tb1053PhysconnEntity tb1053PhysconnByF1053Code;
    private Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code;

    public String getF1073Code() {
        return f1073Code;
    }

    public void setF1073Code(String f1073Code) {
        this.f1073Code = f1073Code;
    }

    public String getF1065Code() {
        return f1065Code;
    }

    public void setF1065Code(String f1065Code) {
        this.f1065Code = f1065Code;
    }

    public String getF1053Code() {
        return f1053Code;
    }

    public void setF1053Code(String f1053Code) {
        this.f1053Code = f1053Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1073LlinkphyrelationEntity that = (Tb1073LlinkphyrelationEntity) o;

        if (f1053Code != null ? !f1053Code.equals(that.f1053Code) : that.f1053Code != null) return false;
        if (f1065Code != null ? !f1065Code.equals(that.f1065Code) : that.f1065Code != null) return false;
        if (f1073Code != null ? !f1073Code.equals(that.f1073Code) : that.f1073Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1073Code != null ? f1073Code.hashCode() : 0;
        result = 31 * result + (f1065Code != null ? f1065Code.hashCode() : 0);
        result = 31 * result + (f1053Code != null ? f1053Code.hashCode() : 0);
        return result;
    }

    public Tb1053PhysconnEntity getTb1053PhysconnByF1053Code() {
        return tb1053PhysconnByF1053Code;
    }

    public void setTb1053PhysconnByF1053Code(Tb1053PhysconnEntity tb1053PhysconnByF1053Code) {
        this.tb1053PhysconnByF1053Code = tb1053PhysconnByF1053Code;
    }

    public Tb1065LogicallinkEntity getTb1065LogicallinkByF1065Code() {
        return tb1065LogicallinkByF1065Code;
    }

    public void setTb1065LogicallinkByF1065Code(Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code) {
        this.tb1065LogicallinkByF1065Code = tb1065LogicallinkByF1065Code;
    }
}
