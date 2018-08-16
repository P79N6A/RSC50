package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1053PhysconnEntity {
    private String f1053Code;
    private String f1041Code;
    private String f1048CodeA;
    private String f1048CodeB;
    private Tb1048PortEntity tb1048PortByF1048CodeA;
    private Tb1048PortEntity tb1048PortByF1048CodeB;

    public String getF1053Code() {
        return f1053Code;
    }

    public void setF1053Code(String f1053Code) {
        this.f1053Code = f1053Code;
    }

    public String getF1041Code() {
        return f1041Code;
    }

    public void setF1041Code(String f1041Code) {
        this.f1041Code = f1041Code;
    }

    public String getF1048CodeA() {
        return f1048CodeA;
    }

    public void setF1048CodeA(String f1048CodeA) {
        this.f1048CodeA = f1048CodeA;
    }

    public String getF1048CodeB() {
        return f1048CodeB;
    }

    public void setF1048CodeB(String f1048CodeB) {
        this.f1048CodeB = f1048CodeB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1053PhysconnEntity that = (Tb1053PhysconnEntity) o;

        if (f1041Code != null ? !f1041Code.equals(that.f1041Code) : that.f1041Code != null) return false;
        if (f1048CodeA != null ? !f1048CodeA.equals(that.f1048CodeA) : that.f1048CodeA != null) return false;
        if (f1048CodeB != null ? !f1048CodeB.equals(that.f1048CodeB) : that.f1048CodeB != null) return false;
        if (f1053Code != null ? !f1053Code.equals(that.f1053Code) : that.f1053Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1053Code != null ? f1053Code.hashCode() : 0;
        result = 31 * result + (f1041Code != null ? f1041Code.hashCode() : 0);
        result = 31 * result + (f1048CodeA != null ? f1048CodeA.hashCode() : 0);
        result = 31 * result + (f1048CodeB != null ? f1048CodeB.hashCode() : 0);
        return result;
    }

    public Tb1048PortEntity getTb1048PortByF1048CodeA() {
        return tb1048PortByF1048CodeA;
    }

    public void setTb1048PortByF1048CodeA(Tb1048PortEntity tb1048PortByF1048CodeA) {
        this.tb1048PortByF1048CodeA = tb1048PortByF1048CodeA;
    }

    public Tb1048PortEntity getTb1048PortByF1048CodeB() {
        return tb1048PortByF1048CodeB;
    }

    public void setTb1048PortByF1048CodeB(Tb1048PortEntity tb1048PortByF1048CodeB) {
        this.tb1048PortByF1048CodeB = tb1048PortByF1048CodeB;
    }

}
