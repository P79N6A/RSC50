package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1044TerminalEntity {
    private String f1044Code;
    private String f1043Code;
    private String f1045Code;
    private String f1044Name;
    private String f1044Desc;
    private Tb1045ConnectivitynodeEntity tb1045ConnectivitynodeByF1045Code;
    private Tb1043EquipmentEntity tb1043EquipmentByF1043Code;

    public String getF1044Code() {
        return f1044Code;
    }

    public void setF1044Code(String f1044Code) {
        this.f1044Code = f1044Code;
    }

    public String getF1043Code() {
        return f1043Code;
    }

    public void setF1043Code(String f1043Code) {
        this.f1043Code = f1043Code;
    }

    public String getF1045Code() {
        return f1045Code;
    }

    public void setF1045Code(String f1045Code) {
        this.f1045Code = f1045Code;
    }

    public String getF1044Name() {
        return f1044Name;
    }

    public void setF1044Name(String f1044Name) {
        this.f1044Name = f1044Name;
    }

    public String getF1044Desc() {
        return f1044Desc;
    }

    public void setF1044Desc(String f1044Desc) {
        this.f1044Desc = f1044Desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1044TerminalEntity that = (Tb1044TerminalEntity) o;

        if (f1043Code != null ? !f1043Code.equals(that.f1043Code) : that.f1043Code != null) return false;
        if (f1044Code != null ? !f1044Code.equals(that.f1044Code) : that.f1044Code != null) return false;
        if (f1044Desc != null ? !f1044Desc.equals(that.f1044Desc) : that.f1044Desc != null) return false;
        if (f1044Name != null ? !f1044Name.equals(that.f1044Name) : that.f1044Name != null) return false;
        if (f1045Code != null ? !f1045Code.equals(that.f1045Code) : that.f1045Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1044Code != null ? f1044Code.hashCode() : 0;
        result = 31 * result + (f1043Code != null ? f1043Code.hashCode() : 0);
        result = 31 * result + (f1045Code != null ? f1045Code.hashCode() : 0);
        result = 31 * result + (f1044Name != null ? f1044Name.hashCode() : 0);
        result = 31 * result + (f1044Desc != null ? f1044Desc.hashCode() : 0);
        return result;
    }

    public Tb1045ConnectivitynodeEntity getTb1045ConnectivitynodeByF1045Code() {
        return tb1045ConnectivitynodeByF1045Code;
    }

    public void setTb1045ConnectivitynodeByF1045Code(Tb1045ConnectivitynodeEntity tb1045ConnectivitynodeByF1045Code) {
        this.tb1045ConnectivitynodeByF1045Code = tb1045ConnectivitynodeByF1045Code;
    }

    public Tb1043EquipmentEntity getTb1043EquipmentByF1043Code() {
        return tb1043EquipmentByF1043Code;
    }

    public void setTb1043EquipmentByF1043Code(Tb1043EquipmentEntity tb1043EquipmentByF1043Code) {
        this.tb1043EquipmentByF1043Code = tb1043EquipmentByF1043Code;
    }
}
