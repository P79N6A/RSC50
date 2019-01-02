package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1068ProtctrlEntity extends Deletable {
    private String f1068Code;
    private String f1046CodeProt;
    private String f1046CodeIo;
    private String f1043Code;
    private String f1065Code;
    private Tb1046IedEntity tb1046IedByF1046CodeIo;
    private Tb1046IedEntity tb1046IedByF1046CodeProt;
    private Tb1043EquipmentEntity tb1043EquipmentByF1043Code;
    private Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code;

    public String getF1068Code() {
        return f1068Code;
    }

    public void setF1068Code(String f1068Code) {
        this.f1068Code = f1068Code;
    }

    public String getF1046CodeProt() {
        return f1046CodeProt;
    }

    public void setF1046CodeProt(String f1046CodeProt) {
        this.f1046CodeProt = f1046CodeProt;
    }

    public String getF1046CodeIo() {
        return f1046CodeIo;
    }

    public void setF1046CodeIo(String f1046CodeIo) {
        this.f1046CodeIo = f1046CodeIo;
    }

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
    }

    public String getF1065Code() {
        return f1065Code;
    }

    public void setF1065Code(String f1065Code) {
        this.f1065Code = f1065Code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1068ProtctrlEntity that = (Tb1068ProtctrlEntity) o;

        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1046CodeIo != null ? !f1046CodeIo.equals(that.f1046CodeIo) : that.f1046CodeIo != null) return false;
        if (f1046CodeProt != null ? !f1046CodeProt.equals(that.f1046CodeProt) : that.f1046CodeProt != null)
            return false;
        if (f1065Code != null ? !f1065Code.equals(that.f1065Code) : that.f1065Code != null) return false;
        if (f1068Code != null ? !f1068Code.equals(that.f1068Code) : that.f1068Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1068Code != null ? f1068Code.hashCode() : 0;
        result = 31 * result + (f1046CodeProt != null ? f1046CodeProt.hashCode() : 0);
        result = 31 * result + (f1046CodeIo != null ? f1046CodeIo.hashCode() : 0);
        result = 31 * result + (f1043Code != null ? f1043Code.hashCode() : 0);
        result = 31 * result + (f1065Code != null ? f1065Code.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeIo() {
        return tb1046IedByF1046CodeIo;
    }

    public void setTb1046IedByF1046CodeIo(Tb1046IedEntity tb1046IedByF1046CodeIo) {
        this.tb1046IedByF1046CodeIo = tb1046IedByF1046CodeIo;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeProt() {
        return tb1046IedByF1046CodeProt;
    }

    public void setTb1046IedByF1046CodeProt(Tb1046IedEntity tb1046IedByF1046CodeProt) {
        this.tb1046IedByF1046CodeProt = tb1046IedByF1046CodeProt;
    }

    public Tb1043EquipmentEntity getTb1043EquipmentByF1043Code() {
        return tb1043EquipmentByF1043Code;
    }

    public void setTb1043EquipmentByF1043Code(Tb1043EquipmentEntity tb1043EquipmentByF1043Code) {
        this.tb1043EquipmentByF1043Code = tb1043EquipmentByF1043Code;
    }

    public Tb1065LogicallinkEntity getTb1065LogicallinkByF1065Code() {
        return tb1065LogicallinkByF1065Code;
    }

    public void setTb1065LogicallinkByF1065Code(Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code) {
        this.tb1065LogicallinkByF1065Code = tb1065LogicallinkByF1065Code;
    }
}
