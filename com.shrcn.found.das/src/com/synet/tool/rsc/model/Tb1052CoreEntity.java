package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1052CoreEntity {
    private String f1052Code;
    private String parentCode;
    private int f1052Type;
    private Integer f1052No;
    private Tb1048PortEntity tb1048PortByF1048CodeA;
    private Tb1048PortEntity tb1048PortByF1048CodeB;
    private Tb1051CableEntity tb1051CableByParentCode;
    private Tb1050CubicleEntity tb1050CubicleByParentCode;

    public String getF1052Code() {
        return f1052Code;
    }

    public void setF1052Code(String f1052Code) {
        this.f1052Code = f1052Code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getF1052Type() {
        return f1052Type;
    }

    public void setF1052Type(int f1052Type) {
        this.f1052Type = f1052Type;
    }

    public Integer getF1052No() {
        return f1052No;
    }

    public void setF1052No(Integer f1052No) {
        this.f1052No = f1052No;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1052CoreEntity that = (Tb1052CoreEntity) o;

        if (f1052Type != that.f1052Type) return false;
        if (tb1048PortByF1048CodeA != null ? !tb1048PortByF1048CodeA.equals(that.tb1048PortByF1048CodeA) : that.tb1048PortByF1048CodeA != null) return false;
        if (tb1048PortByF1048CodeB != null ? !tb1048PortByF1048CodeB.equals(that.tb1048PortByF1048CodeB) : that.tb1048PortByF1048CodeB != null) return false;
        if (f1052Code != null ? !f1052Code.equals(that.f1052Code) : that.f1052Code != null) return false;
        if (f1052No != null ? !f1052No.equals(that.f1052No) : that.f1052No != null) return false;
        if (parentCode != null ? !parentCode.equals(that.parentCode) : that.parentCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1052Code != null ? f1052Code.hashCode() : 0;
        result = 31 * result + (parentCode != null ? parentCode.hashCode() : 0);
        result = 31 * result + f1052Type;
        result = 31 * result + (f1052No != null ? f1052No.hashCode() : 0);
        result = 31 * result + (tb1048PortByF1048CodeA != null ? tb1048PortByF1048CodeA.hashCode() : 0);
        result = 31 * result + (tb1048PortByF1048CodeB != null ? tb1048PortByF1048CodeB.hashCode() : 0);
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

	public Tb1051CableEntity getTb1051CableByParentCode() {
        return tb1051CableByParentCode;
    }

    public void setTb1051CableByParentCode(Tb1051CableEntity tb1051CableByParentCode) {
        this.tb1051CableByParentCode = tb1051CableByParentCode;
    }

    public Tb1050CubicleEntity getTb1050CubicleByParentCode() {
        return tb1050CubicleByParentCode;
    }

    public void setTb1050CubicleByParentCode(Tb1050CubicleEntity tb1050CubicleByParentCode) {
        this.tb1050CubicleByParentCode = tb1050CubicleByParentCode;
    }

	public String getF1048CodeA() {
		return tb1048PortByF1048CodeA==null ? "" : tb1048PortByF1048CodeA.getF1048Code();
	}

	public String getF1048CodeB() {
		return tb1048PortByF1048CodeB==null ? "" : tb1048PortByF1048CodeB.getF1048Code();
	}
}
