package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1067CtvtsecondaryEntity {
    private String f1067Code;
    private String f1043Code;
    private Integer f1067Index;
    private String f1067Model;
    private String f1067Type;
    private String f1067TermNo;
    private String f1067CircNo;
    private String f1067Desc;
    private Tb1061PoutEntity tb1061PoutByF1061CodeA1;
    private Tb1061PoutEntity tb1061PoutByF1061CodeA2;
    private Tb1061PoutEntity tb1061PoutByF1061CodeB1;
    private Tb1061PoutEntity tb1061PoutByF1061CodeB2;
    private Tb1061PoutEntity tb1061PoutByF1061CodeC1;
    private Tb1061PoutEntity tb1061PoutByF1061CodeC2;
    private Tb1043EquipmentEntity tb1043EquipmentByF1043Code;

    public String getF1067Code() {
        return f1067Code;
    }

    public void setF1067Code(String f1067Code) {
        this.f1067Code = f1067Code;
    }

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
    }

    public Integer getF1067Index() {
        return f1067Index;
    }

    public void setF1067Index(Integer f1067Index) {
        this.f1067Index = f1067Index;
    }

    public String getF1067Model() {
        return f1067Model;
    }

    public void setF1067Model(String f1067Model) {
        this.f1067Model = f1067Model;
    }

    public String getF1067Type() {
        return f1067Type;
    }

    public void setF1067Type(String f1067Type) {
        this.f1067Type = f1067Type;
    }

    public String getF1067TermNo() {
		return f1067TermNo;
	}

	public void setF1067TermNo(String f1067TermNo) {
		this.f1067TermNo = f1067TermNo;
	}

	public String getF1067CircNo() {
		return f1067CircNo;
	}

	public void setF1067CircNo(String f1067CircNo) {
		this.f1067CircNo = f1067CircNo;
	}

	public String getF1067Desc() {
		return f1067Desc;
	}

	public void setF1067Desc(String f1067Desc) {
		this.f1067Desc = f1067Desc;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1067CtvtsecondaryEntity that = (Tb1067CtvtsecondaryEntity) o;

        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1067CircNo != null ? !f1067CircNo.equals(that.f1067CircNo) : that.f1067CircNo != null) return false;
        if (f1067Desc != null ? !f1067Desc.equals(that.f1067Desc) : that.f1067Desc != null) return false;
        if (f1067TermNo != null ? !f1067TermNo.equals(that.f1067TermNo) : that.f1067TermNo != null) return false;
        if (f1067Code != null ? !f1067Code.equals(that.f1067Code) : that.f1067Code != null) return false;
        if (f1067Index != null ? !f1067Index.equals(that.f1067Index) : that.f1067Index != null) return false;
        if (f1067Model != null ? !f1067Model.equals(that.f1067Model) : that.f1067Model != null) return false;
        if (f1067Type != null ? !f1067Type.equals(that.f1067Type) : that.f1067Type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1067Code != null ? f1067Code.hashCode() : 0;
        result = 31 * result + (f1043Code != null ? f1043Code.hashCode() : 0);
        result = 31 * result + (f1067Index != null ? f1067Index.hashCode() : 0);
        result = 31 * result + (f1067Model != null ? f1067Model.hashCode() : 0);
        result = 31 * result + (f1067Type != null ? f1067Type.hashCode() : 0);
        result = 31 * result + (f1067TermNo != null ? f1067TermNo.hashCode() : 0);
        result = 31 * result + (f1067CircNo != null ? f1067CircNo.hashCode() : 0);
        result = 31 * result + (f1067Desc != null ? f1067Desc.hashCode() : 0);
        return result;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeA1() {
        return tb1061PoutByF1061CodeA1;
    }

    public void setTb1061PoutByF1061CodeA1(Tb1061PoutEntity tb1061PoutByF1061CodeA1) {
        this.tb1061PoutByF1061CodeA1 = tb1061PoutByF1061CodeA1;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeA2() {
        return tb1061PoutByF1061CodeA2;
    }

    public void setTb1061PoutByF1061CodeA2(Tb1061PoutEntity tb1061PoutByF1061CodeA2) {
        this.tb1061PoutByF1061CodeA2 = tb1061PoutByF1061CodeA2;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeB1() {
        return tb1061PoutByF1061CodeB1;
    }

    public void setTb1061PoutByF1061CodeB1(Tb1061PoutEntity tb1061PoutByF1061CodeB1) {
        this.tb1061PoutByF1061CodeB1 = tb1061PoutByF1061CodeB1;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeB2() {
        return tb1061PoutByF1061CodeB2;
    }

    public void setTb1061PoutByF1061CodeB2(Tb1061PoutEntity tb1061PoutByF1061CodeB2) {
        this.tb1061PoutByF1061CodeB2 = tb1061PoutByF1061CodeB2;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeC1() {
        return tb1061PoutByF1061CodeC1;
    }

    public void setTb1061PoutByF1061CodeC1(Tb1061PoutEntity tb1061PoutByF1061CodeC1) {
        this.tb1061PoutByF1061CodeC1 = tb1061PoutByF1061CodeC1;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeC2() {
        return tb1061PoutByF1061CodeC2;
    }

    public void setTb1061PoutByF1061CodeC2(Tb1061PoutEntity tb1061PoutByF1061CodeC2) {
        this.tb1061PoutByF1061CodeC2 = tb1061PoutByF1061CodeC2;
    }

    public Tb1043EquipmentEntity getTb1043EquipmentByF1043Code() {
        return tb1043EquipmentByF1043Code;
    }

    public void setTb1043EquipmentByF1043Code(Tb1043EquipmentEntity tb1043EquipmentByF1043Code) {
        this.tb1043EquipmentByF1043Code = tb1043EquipmentByF1043Code;
    }
}
