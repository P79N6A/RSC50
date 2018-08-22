package com.synet.tool.rsc.model;

/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1063CircuitEntity {
    private String f1063Code;
    private int f1063Type;
    private Tb1046IedEntity tb1046IedByF1046CodeIedRecv;
    private Tb1046IedEntity tb1046IedByF1046CodeIedSend;
    
    private Tb1061PoutEntity tb1061PoutByF1061CodePSend;
    private Tb1062PinEntity tb1062PinByF1062CodePRecv;
    
    private Tb1061PoutEntity tb1061PoutByF1061CodeConvChk1;
    private Tb1061PoutEntity tb1061PoutByF1061CodeConvChk2;
    
    private Tb1065LogicallinkEntity tb1065LogicallinkByF1065Code;

    public String getF1063Code() {
        return f1063Code;
    }

    public void setF1063Code(String f1063Code) {
        this.f1063Code = f1063Code;
    }

    public int getF1063Type() {
		return f1063Type;
	}

	public void setF1063Type(int f1063Type) {
		this.f1063Type = f1063Type;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1063CircuitEntity that = (Tb1063CircuitEntity) o;

        if (f1063Type != that.f1063Type) return false;
        if (f1063Code != null ? !f1063Code.equals(that.f1063Code) : that.f1063Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1063Code != null ? f1063Code.hashCode() : 0;
        result = 31 * result + f1063Type;
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
