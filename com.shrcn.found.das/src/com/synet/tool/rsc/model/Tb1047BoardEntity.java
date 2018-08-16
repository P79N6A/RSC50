package com.synet.tool.rsc.model;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1047BoardEntity {
    private String f1047Code;
    private String f1046Code;
    private String f1047Slot;
    private String f1047Desc;
    private String f1047Type;
    private Tb1046IedEntity tb1046IedByF1046Code;

    public String getF1047Code() {
        return f1047Code;
    }

    public void setF1047Code(String f1047Code) {
        this.f1047Code = f1047Code;
    }

    public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1047Slot() {
        return f1047Slot;
    }

    public void setF1047Slot(String f1047Slot) {
        this.f1047Slot = f1047Slot;
    }

    public String getF1047Desc() {
        return f1047Desc;
    }

    public void setF1047Desc(String f1047Desc) {
        this.f1047Desc = f1047Desc;
    }

    public String getF1047Type() {
        return f1047Type;
    }

    public void setF1047Type(String f1047Type) {
        this.f1047Type = f1047Type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1047BoardEntity that = (Tb1047BoardEntity) o;

        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1047Code != null ? !f1047Code.equals(that.f1047Code) : that.f1047Code != null) return false;
        if (f1047Desc != null ? !f1047Desc.equals(that.f1047Desc) : that.f1047Desc != null) return false;
        if (f1047Slot != null ? !f1047Slot.equals(that.f1047Slot) : that.f1047Slot != null) return false;
        if (f1047Type != null ? !f1047Type.equals(that.f1047Type) : that.f1047Type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1047Code != null ? f1047Code.hashCode() : 0;
        result = 31 * result + (f1046Code != null ? f1046Code.hashCode() : 0);
        result = 31 * result + (f1047Slot != null ? f1047Slot.hashCode() : 0);
        result = 31 * result + (f1047Desc != null ? f1047Desc.hashCode() : 0);
        result = 31 * result + (f1047Type != null ? f1047Type.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046Code() {
        return tb1046IedByF1046Code;
    }

    public void setTb1046IedByF1046Code(Tb1046IedEntity tb1046IedByF1046Code) {
        this.tb1046IedByF1046Code = tb1046IedByF1046Code;
    }
}
