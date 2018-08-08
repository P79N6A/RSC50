package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1041SubstationEntity {
    private String f1041Code;
    private String f1041Name;
    private String f1041Desc;
    private String f1041DqName;
    private String f1041Dqdesc;
    private String f1041Company;
    private Integer f1042VoltageH;
    private Integer f1042VoltageM;
    private Integer f1042VoltageL;
    private Collection<Tb1042BayEntity> tb1042BaysByF1041Code;
    private Collection<Tb1049RegionEntity> tb1049RegionsByF1041Code;
    private Collection<Tb1051CableEntity> tb1051CablesByF1041Code;

    public String getF1041Code() {
        return f1041Code;
    }

    public void setF1041Code(String f1041Code) {
        this.f1041Code = f1041Code;
    }

    public String getF1041Name() {
        return f1041Name;
    }

    public void setF1041Name(String f1041Name) {
        this.f1041Name = f1041Name;
    }

    public String getF1041Desc() {
        return f1041Desc;
    }

    public void setF1041Desc(String f1041Desc) {
        this.f1041Desc = f1041Desc;
    }

    public String getF1041DqName() {
        return f1041DqName;
    }

    public void setF1041DqName(String f1041DqName) {
        this.f1041DqName = f1041DqName;
    }

    public String getF1041Dqdesc() {
        return f1041Dqdesc;
    }

    public void setF1041Dqdesc(String f1041Dqdesc) {
        this.f1041Dqdesc = f1041Dqdesc;
    }

    public String getF1041Company() {
        return f1041Company;
    }

    public void setF1041Company(String f1041Company) {
        this.f1041Company = f1041Company;
    }

    public Integer getF1042VoltageH() {
        return f1042VoltageH;
    }

    public void setF1042VoltageH(Integer f1042VoltageH) {
        this.f1042VoltageH = f1042VoltageH;
    }

    public Integer getF1042VoltageM() {
        return f1042VoltageM;
    }

    public void setF1042VoltageM(Integer f1042VoltageM) {
        this.f1042VoltageM = f1042VoltageM;
    }

    public Integer getF1042VoltageL() {
        return f1042VoltageL;
    }

    public void setF1042VoltageL(Integer f1042VoltageL) {
        this.f1042VoltageL = f1042VoltageL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1041SubstationEntity that = (Tb1041SubstationEntity) o;

        if (f1041Code != null ? !f1041Code.equals(that.f1041Code) : that.f1041Code != null) return false;
        if (f1041Company != null ? !f1041Company.equals(that.f1041Company) : that.f1041Company != null) return false;
        if (f1041Desc != null ? !f1041Desc.equals(that.f1041Desc) : that.f1041Desc != null) return false;
        if (f1041DqName != null ? !f1041DqName.equals(that.f1041DqName) : that.f1041DqName != null) return false;
        if (f1041Dqdesc != null ? !f1041Dqdesc.equals(that.f1041Dqdesc) : that.f1041Dqdesc != null) return false;
        if (f1041Name != null ? !f1041Name.equals(that.f1041Name) : that.f1041Name != null) return false;
        if (f1042VoltageH != null ? !f1042VoltageH.equals(that.f1042VoltageH) : that.f1042VoltageH != null)
            return false;
        if (f1042VoltageL != null ? !f1042VoltageL.equals(that.f1042VoltageL) : that.f1042VoltageL != null)
            return false;
        if (f1042VoltageM != null ? !f1042VoltageM.equals(that.f1042VoltageM) : that.f1042VoltageM != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1041Code != null ? f1041Code.hashCode() : 0;
        result = 31 * result + (f1041Name != null ? f1041Name.hashCode() : 0);
        result = 31 * result + (f1041Desc != null ? f1041Desc.hashCode() : 0);
        result = 31 * result + (f1041DqName != null ? f1041DqName.hashCode() : 0);
        result = 31 * result + (f1041Dqdesc != null ? f1041Dqdesc.hashCode() : 0);
        result = 31 * result + (f1041Company != null ? f1041Company.hashCode() : 0);
        result = 31 * result + (f1042VoltageH != null ? f1042VoltageH.hashCode() : 0);
        result = 31 * result + (f1042VoltageM != null ? f1042VoltageM.hashCode() : 0);
        result = 31 * result + (f1042VoltageL != null ? f1042VoltageL.hashCode() : 0);
        return result;
    }

    public Collection<Tb1042BayEntity> getTb1042BaysByF1041Code() {
        return tb1042BaysByF1041Code;
    }

    public void setTb1042BaysByF1041Code(Collection<Tb1042BayEntity> tb1042BaysByF1041Code) {
        this.tb1042BaysByF1041Code = tb1042BaysByF1041Code;
    }

    public Collection<Tb1049RegionEntity> getTb1049RegionsByF1041Code() {
        return tb1049RegionsByF1041Code;
    }

    public void setTb1049RegionsByF1041Code(Collection<Tb1049RegionEntity> tb1049RegionsByF1041Code) {
        this.tb1049RegionsByF1041Code = tb1049RegionsByF1041Code;
    }

    public Collection<Tb1051CableEntity> getTb1051CablesByF1041Code() {
        return tb1051CablesByF1041Code;
    }

    public void setTb1051CablesByF1041Code(Collection<Tb1051CableEntity> tb1051CablesByF1041Code) {
        this.tb1051CablesByF1041Code = tb1051CablesByF1041Code;
    }
}
