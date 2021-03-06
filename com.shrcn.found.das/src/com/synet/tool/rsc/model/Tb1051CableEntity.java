package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1051CableEntity extends Deletable {
    private String f1051Code;
    private String f1051Name;
    private String f1051Desc;
    private Integer f1051Length;
    private Integer f1051CoreNum;
    private int f1051Type;
    private Tb1050CubicleEntity tb1050CubicleByF1050CodeA;
    private Tb1050CubicleEntity tb1050CubicleByF1050CodeB;
    private Tb1041SubstationEntity tb1041SubstationByF1041Code;

    public String getF1051Code() {
        return f1051Code;
    }

    public void setF1051Code(String f1051Code) {
        this.f1051Code = f1051Code;
    }

    public String getF1051Name() {
        return f1051Name;
    }

    public void setF1051Name(String f1051Name) {
        this.f1051Name = f1051Name;
    }

    public String getF1051Desc() {
        return f1051Desc;
    }

    public void setF1051Desc(String f1051Desc) {
        this.f1051Desc = f1051Desc;
    }

    public Integer getF1051Length() {
        return f1051Length;
    }

    public void setF1051Length(Integer f1051Length) {
        this.f1051Length = f1051Length;
    }

    public Integer getF1051CoreNum() {
        return f1051CoreNum;
    }

    public void setF1051CoreNum(Integer f1051CoreNum) {
        this.f1051CoreNum = f1051CoreNum;
    }

    public int getF1051Type() {
        return f1051Type;
    }

    public void setF1051Type(int f1051Type) {
        this.f1051Type = f1051Type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1051CableEntity that = (Tb1051CableEntity) o;

        if (f1051Type != that.f1051Type) return false;
        if (f1051Code != null ? !f1051Code.equals(that.f1051Code) : that.f1051Code != null) return false;
        if (f1051CoreNum != null ? !f1051CoreNum.equals(that.f1051CoreNum) : that.f1051CoreNum != null) return false;
        if (f1051Desc != null ? !f1051Desc.equals(that.f1051Desc) : that.f1051Desc != null) return false;
        if (f1051Length != null ? !f1051Length.equals(that.f1051Length) : that.f1051Length != null) return false;
        if (f1051Name != null ? !f1051Name.equals(that.f1051Name) : that.f1051Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1051Code != null ? f1051Code.hashCode() : 0;
        result = 31 * result + (f1051Name != null ? f1051Name.hashCode() : 0);
        result = 31 * result + (f1051Desc != null ? f1051Desc.hashCode() : 0);
        result = 31 * result + (f1051Length != null ? f1051Length.hashCode() : 0);
        result = 31 * result + (f1051CoreNum != null ? f1051CoreNum.hashCode() : 0);
        result = 31 * result + f1051Type;
        return result;
    }

    public Tb1050CubicleEntity getTb1050CubicleByF1050CodeA() {
        return tb1050CubicleByF1050CodeA;
    }

    public void setTb1050CubicleByF1050CodeA(Tb1050CubicleEntity tb1050CubicleByF1050CodeA) {
        this.tb1050CubicleByF1050CodeA = tb1050CubicleByF1050CodeA;
    }

    public Tb1050CubicleEntity getTb1050CubicleByF1050CodeB() {
        return tb1050CubicleByF1050CodeB;
    }

    public void setTb1050CubicleByF1050CodeB(Tb1050CubicleEntity tb1050CubicleByF1050CodeB) {
        this.tb1050CubicleByF1050CodeB = tb1050CubicleByF1050CodeB;
    }

    public Tb1041SubstationEntity getTb1041SubstationByF1041Code() {
        return tb1041SubstationByF1041Code;
    }

    public void setTb1041SubstationByF1041Code(Tb1041SubstationEntity tb1041SubstationByF1041Code) {
        this.tb1041SubstationByF1041Code = tb1041SubstationByF1041Code;
    }
}
