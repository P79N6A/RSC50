package com.synet.tool.rsc.model;

import java.util.Collection;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1057SgcbEntity {
    private String f1057Code;
    private String f1046Code;
    private String f1057CbName;
    private String f1057Dataset;
    private String f1057DsDesc;
    private Tb1046IedEntity tb1046IedByF1046Code;
    private Collection<Tb1059SgfcdaEntity> tb1059SgfcdasByF1057Code;

    public String getF1057Code() {
        return f1057Code;
    }

    public void setF1057Code(String f1057Code) {
        this.f1057Code = f1057Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1057CbName() {
        return f1057CbName;
    }

    public void setF1057CbName(String f1057CbName) {
        this.f1057CbName = f1057CbName;
    }

    public String getF1057Dataset() {
        return f1057Dataset;
    }

    public void setF1057Dataset(String f1057Dataset) {
        this.f1057Dataset = f1057Dataset;
    }

    public String getF1057DsDesc() {
        return f1057DsDesc;
    }

    public void setF1057DsDesc(String f1057DsDesc) {
        this.f1057DsDesc = f1057DsDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1057SgcbEntity that = (Tb1057SgcbEntity) o;

        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1057CbName != null ? !f1057CbName.equals(that.f1057CbName) : that.f1057CbName != null) return false;
        if (f1057Code != null ? !f1057Code.equals(that.f1057Code) : that.f1057Code != null) return false;
        if (f1057Dataset != null ? !f1057Dataset.equals(that.f1057Dataset) : that.f1057Dataset != null) return false;
        if (f1057DsDesc != null ? !f1057DsDesc.equals(that.f1057DsDesc) : that.f1057DsDesc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1057Code != null ? f1057Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1057CbName != null ? f1057CbName.hashCode() : 0);
        result = 31 * result + (f1057Dataset != null ? f1057Dataset.hashCode() : 0);
        result = 31 * result + (f1057DsDesc != null ? f1057DsDesc.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }

    public Collection<Tb1059SgfcdaEntity> getTb1059SgfcdasByF1057Code() {
        return tb1059SgfcdasByF1057Code;
    }

    public void setTb1059SgfcdasByF1057Code(Collection<Tb1059SgfcdaEntity> tb1059SgfcdasByF1057Code) {
        this.tb1059SgfcdasByF1057Code = tb1059SgfcdasByF1057Code;
    }
}
