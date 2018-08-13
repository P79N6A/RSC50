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
    private String f1061TermNo;
    private String f1061CircNo;
    private String f1061Desc;
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


    public String getF1061TermNo() {
        return f1061TermNo;
    }

    public void setF1061TermNo(String f1061TermNo) {
        this.f1061TermNo = f1061TermNo;
    }

    public String getF1061CircNo() {
        return f1061CircNo;
    }

    public void setF1061CircNo(String f1061CircNo) {
        this.f1061CircNo = f1061CircNo;
    }

    public String getF1061Desc() {
        return f1061Desc;
    }

    public void setF1061Desc(String f1061Desc) {
        this.f1061Desc = f1061Desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1067CtvtsecondaryEntity that = (Tb1067CtvtsecondaryEntity) o;

        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1061CircNo != null ? !f1061CircNo.equals(that.f1061CircNo) : that.f1061CircNo != null) return false;
        if (f1061Desc != null ? !f1061Desc.equals(that.f1061Desc) : that.f1061Desc != null) return false;
        if (f1061TermNo != null ? !f1061TermNo.equals(that.f1061TermNo) : that.f1061TermNo != null) return false;
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
        result = 31 * result + (f1061TermNo != null ? f1061TermNo.hashCode() : 0);
        result = 31 * result + (f1061CircNo != null ? f1061CircNo.hashCode() : 0);
        result = 31 * result + (f1061Desc != null ? f1061Desc.hashCode() : 0);
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
