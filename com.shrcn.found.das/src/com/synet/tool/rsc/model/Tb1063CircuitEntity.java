package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1063CircuitEntity {
    private String f1063Code;
    private String f1046CodeIedSend;
    private String f1061CodePSend;
    private String f1046CodeIedRecv;
    private String f1062CodePRecv;
    private int f1020No;
    private String f1065Code;
    private String f1061CodeConvChk1;
    private String f1061CodeConvChk2;
    private Tb1046IedEntity tb1046IedByF1046CodeIedRecv;
    private Tb1046IedEntity tb1046IedByF1046CodeIedSend;
    private Tb1061PoutEntity tb1061PoutByF1061CodeConvChk1;
    private Tb1061PoutEntity tb1061PoutByF1061CodeConvChk2;
    private Tb1062PinEntity tb1062PinByF1062CodePRecv;
    private Tb1061PoutEntity tb1061PoutByF1061CodePSend;
    private Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code;

    public String getF1063Code() {
        return f1063Code;
    }

    public void setF1063Code(String f1063Code) {
        this.f1063Code = f1063Code;
    }

    public String getF1046CodeIedSend() {
        return f1046CodeIedSend;
    }

    public void setF1046CodeIedSend(String f1046CodeIedSend) {
        this.f1046CodeIedSend = f1046CodeIedSend;
    }

    public String getF1061CodePSend() {
        return f1061CodePSend;
    }

    public void setF1061CodePSend(String f1061CodePSend) {
        this.f1061CodePSend = f1061CodePSend;
    }

    public String getF1046CodeIedRecv() {
        return f1046CodeIedRecv;
    }

    public void setF1046CodeIedRecv(String f1046CodeIedRecv) {
        this.f1046CodeIedRecv = f1046CodeIedRecv;
    }

    public String getF1062CodePRecv() {
        return f1062CodePRecv;
    }

    public void setF1062CodePRecv(String f1062CodePRecv) {
        this.f1062CodePRecv = f1062CodePRecv;
    }

    public int getF1020No() {
        return f1020No;
    }

    public void setF1020No(int f1020No) {
        this.f1020No = f1020No;
    }

    public String getF1065Code() {
        return f1065Code;
    }

    public void setF1065Code(String f1065Code) {
        this.f1065Code = f1065Code;
    }

    public String getF1061CodeConvChk1() {
        return f1061CodeConvChk1;
    }

    public void setF1061CodeConvChk1(String f1061CodeConvChk1) {
        this.f1061CodeConvChk1 = f1061CodeConvChk1;
    }

    public String getF1061CodeConvChk2() {
        return f1061CodeConvChk2;
    }

    public void setF1061CodeConvChk2(String f1061CodeConvChk2) {
        this.f1061CodeConvChk2 = f1061CodeConvChk2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1063CircuitEntity that = (Tb1063CircuitEntity) o;

        if (f1020No != that.f1020No) return false;
        if (f1046CodeIedRecv != null ? !f1046CodeIedRecv.equals(that.f1046CodeIedRecv) : that.f1046CodeIedRecv != null)
            return false;
        if (f1046CodeIedSend != null ? !f1046CodeIedSend.equals(that.f1046CodeIedSend) : that.f1046CodeIedSend != null)
            return false;
        if (f1061CodeConvChk1 != null ? !f1061CodeConvChk1.equals(that.f1061CodeConvChk1) : that.f1061CodeConvChk1 != null)
            return false;
        if (f1061CodeConvChk2 != null ? !f1061CodeConvChk2.equals(that.f1061CodeConvChk2) : that.f1061CodeConvChk2 != null)
            return false;
        if (f1061CodePSend != null ? !f1061CodePSend.equals(that.f1061CodePSend) : that.f1061CodePSend != null)
            return false;
        if (f1062CodePRecv != null ? !f1062CodePRecv.equals(that.f1062CodePRecv) : that.f1062CodePRecv != null)
            return false;
        if (f1063Code != null ? !f1063Code.equals(that.f1063Code) : that.f1063Code != null) return false;
        if (f1065Code != null ? !f1065Code.equals(that.f1065Code) : that.f1065Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1063Code != null ? f1063Code.hashCode() : 0;
        result = 31 * result + (f1046CodeIedSend != null ? f1046CodeIedSend.hashCode() : 0);
        result = 31 * result + (f1061CodePSend != null ? f1061CodePSend.hashCode() : 0);
        result = 31 * result + (f1046CodeIedRecv != null ? f1046CodeIedRecv.hashCode() : 0);
        result = 31 * result + (f1062CodePRecv != null ? f1062CodePRecv.hashCode() : 0);
        result = 31 * result + f1020No;
        result = 31 * result + (f1065Code != null ? f1065Code.hashCode() : 0);
        result = 31 * result + (f1061CodeConvChk1 != null ? f1061CodeConvChk1.hashCode() : 0);
        result = 31 * result + (f1061CodeConvChk2 != null ? f1061CodeConvChk2.hashCode() : 0);
        return result;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeIedRecv() {
        return tb1046IedByF1046CodeIedRecv;
    }

    public void setTb1046IedByF1046CodeIedRecv(Tb1046IedEntity tb1046IedByF1046CodeIedRecv) {
        this.tb1046IedByF1046CodeIedRecv = tb1046IedByF1046CodeIedRecv;
    }

    public Tb1046IedEntity getTb1046IedByF1046CodeIedSend() {
        return tb1046IedByF1046CodeIedSend;
    }

    public void setTb1046IedByF1046CodeIedSend(Tb1046IedEntity tb1046IedByF1046CodeIedSend) {
        this.tb1046IedByF1046CodeIedSend = tb1046IedByF1046CodeIedSend;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeConvChk1() {
        return tb1061PoutByF1061CodeConvChk1;
    }

    public void setTb1061PoutByF1061CodeConvChk1(Tb1061PoutEntity tb1061PoutByF1061CodeConvChk1) {
        this.tb1061PoutByF1061CodeConvChk1 = tb1061PoutByF1061CodeConvChk1;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodeConvChk2() {
        return tb1061PoutByF1061CodeConvChk2;
    }

    public void setTb1061PoutByF1061CodeConvChk2(Tb1061PoutEntity tb1061PoutByF1061CodeConvChk2) {
        this.tb1061PoutByF1061CodeConvChk2 = tb1061PoutByF1061CodeConvChk2;
    }

    public Tb1062PinEntity getTb1062PinByF1062CodePRecv() {
        return tb1062PinByF1062CodePRecv;
    }

    public void setTb1062PinByF1062CodePRecv(Tb1062PinEntity tb1062PinByF1062CodePRecv) {
        this.tb1062PinByF1062CodePRecv = tb1062PinByF1062CodePRecv;
    }

    public Tb1061PoutEntity getTb1061PoutByF1061CodePSend() {
        return tb1061PoutByF1061CodePSend;
    }

    public void setTb1061PoutByF1061CodePSend(Tb1061PoutEntity tb1061PoutByF1061CodePSend) {
        this.tb1061PoutByF1061CodePSend = tb1061PoutByF1061CodePSend;
    }

    public Tb1065LogicallinkEntity getTb1065LogicallinkByF1065Code() {
        return tb1065LogicallinkByF1065Code;
    }

    public void setTb1065LogicallinkByF1065Code(Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code) {
        this.tb1065LogicallinkByF1065Code = tb1065LogicallinkByF1065Code;
    }
}
