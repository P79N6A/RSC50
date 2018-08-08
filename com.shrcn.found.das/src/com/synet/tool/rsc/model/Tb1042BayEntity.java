package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1042BayEntity {
    private String f1042Code;
    private String f1041Code;
    private String f1042Name;
    private String f1042Desc;
    private int f1042Voltage;
    private Integer f1042ConnType;
    private Integer f1042DevType;
    private Integer f1042IedSolution;
    private Tb1041SubstationEntity tb1041SubstationByF1041Code;
    private Collection<Tb1043EquipmentEntity> tb1043EquipmentsByF1042Code;

    public String getF1042Code() {
        return f1042Code;
    }

    public void setF1042Code(String f1042Code) {
        this.f1042Code = f1042Code;
    }

    public String getF1041Code() {
        return f1041Code;
    }

    public void setF1041Code(String f1041Code) {
        this.f1041Code = f1041Code;
    }

    public String getF1042Name() {
        return f1042Name;
    }

    public void setF1042Name(String f1042Name) {
        this.f1042Name = f1042Name;
    }

    public String getF1042Desc() {
        return f1042Desc;
    }

    public void setF1042Desc(String f1042Desc) {
        this.f1042Desc = f1042Desc;
    }

    public int getF1042Voltage() {
        return f1042Voltage;
    }

    public void setF1042Voltage(int f1042Voltage) {
        this.f1042Voltage = f1042Voltage;
    }

    public Integer getF1042ConnType() {
        return f1042ConnType;
    }

    public void setF1042ConnType(Integer f1042ConnType) {
        this.f1042ConnType = f1042ConnType;
    }

    public Integer getF1042DevType() {
        return f1042DevType;
    }

    public void setF1042DevType(Integer f1042DevType) {
        this.f1042DevType = f1042DevType;
    }

    public Integer getF1042IedSolution() {
        return f1042IedSolution;
    }

    public void setF1042IedSolution(Integer f1042IedSolution) {
        this.f1042IedSolution = f1042IedSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1042BayEntity that = (Tb1042BayEntity) o;

        if (f1042Voltage != that.f1042Voltage) return false;
        if (f1041Code != null ? !f1041Code.equals(that.f1041Code) : that.f1041Code != null) return false;
        if (f1042Code != null ? !f1042Code.equals(that.f1042Code) : that.f1042Code != null) return false;
        if (f1042ConnType != null ? !f1042ConnType.equals(that.f1042ConnType) : that.f1042ConnType != null)
            return false;
        if (f1042Desc != null ? !f1042Desc.equals(that.f1042Desc) : that.f1042Desc != null) return false;
        if (f1042DevType != null ? !f1042DevType.equals(that.f1042DevType) : that.f1042DevType != null) return false;
        if (f1042IedSolution != null ? !f1042IedSolution.equals(that.f1042IedSolution) : that.f1042IedSolution != null)
            return false;
        if (f1042Name != null ? !f1042Name.equals(that.f1042Name) : that.f1042Name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1042Code != null ? f1042Code.hashCode() : 0;
        result = 31 * result + (f1041Code != null ? f1041Code.hashCode() : 0);
        result = 31 * result + (f1042Name != null ? f1042Name.hashCode() : 0);
        result = 31 * result + (f1042Desc != null ? f1042Desc.hashCode() : 0);
        result = 31 * result + f1042Voltage;
        result = 31 * result + (f1042ConnType != null ? f1042ConnType.hashCode() : 0);
        result = 31 * result + (f1042DevType != null ? f1042DevType.hashCode() : 0);
        result = 31 * result + (f1042IedSolution != null ? f1042IedSolution.hashCode() : 0);
        return result;
    }

    public Tb1041SubstationEntity getTb1041SubstationByF1041Code() {
        return tb1041SubstationByF1041Code;
    }

    public void setTb1041SubstationByF1041Code(Tb1041SubstationEntity tb1041SubstationByF1041Code) {
        this.tb1041SubstationByF1041Code = tb1041SubstationByF1041Code;
    }

    public Collection<Tb1043EquipmentEntity> getTb1043EquipmentsByF1042Code() {
        return tb1043EquipmentsByF1042Code;
    }

    public void setTb1043EquipmentsByF1042Code(Collection<Tb1043EquipmentEntity> tb1043EquipmentsByF1042Code) {
        this.tb1043EquipmentsByF1042Code = tb1043EquipmentsByF1042Code;
    }
}
