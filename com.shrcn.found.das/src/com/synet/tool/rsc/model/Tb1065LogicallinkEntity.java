package com.synet.tool.rsc.model;

import java.util.List;
import java.util.Set;


/**
 * Created by chunc on 2018/8/7.
 */
public class Tb1065LogicallinkEntity extends Deletable {
    private String f1065Code;
    private String f1046CodeIedRecv;
    private String f1046CodeIedSend;
    private String circuits;
    private String phyconns;
    private int f1065Type;
    private BaseCbEntity baseCbByCdCode;
    private Tb1046IedEntity tb1046IedByF1046CodeIedRecv;
    private Tb1046IedEntity tb1046IedByF1046CodeIedSend;
    private Set<Tb1063CircuitEntity> tb1063CircuitsByF1065Code;
    private List<Tb1053PhysconnEntity> phyConns;

    public String getF1065Code() {
        return f1065Code;
    }

    public void setF1065Code(String f1065Code) {
        this.f1065Code = f1065Code;
    }

    public String getCircuits() {
		return circuits;
	}

	public void setCircuits(String circuits) {
		this.circuits = circuits;
	}

	public String getPhyconns() {
		return phyconns;
	}

	public void setPhyconns(String phyconns) {
		this.phyconns = phyconns;
	}

	public int getF1065Type() {
        return f1065Type;
    }

    public void setF1065Type(int f1065Type) {
        this.f1065Type = f1065Type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1065LogicallinkEntity that = (Tb1065LogicallinkEntity) o;

        if (f1065Type != that.f1065Type) return false;
        if (f1065Code != null ? !f1065Code.equals(that.f1065Code) : that.f1065Code != null) return false;
        if (f1046CodeIedRecv != null ? !f1046CodeIedRecv.equals(that.f1046CodeIedRecv) : that.f1046CodeIedRecv != null) return false;
        if (f1046CodeIedSend != null ? !f1046CodeIedSend.equals(that.f1046CodeIedSend) : that.f1046CodeIedSend != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1065Code != null ? f1065Code.hashCode() : 0;
        result = f1046CodeIedRecv != null ? f1046CodeIedRecv.hashCode() : 0;
        result = f1046CodeIedSend != null ? f1046CodeIedSend.hashCode() : 0;
        result = 31 * result + f1065Type;
        return result;
    }

    public String getF1046CodeIedRecv() {
		return f1046CodeIedRecv;
	}

	public void setF1046CodeIedRecv(String f1046CodeIedRecv) {
		this.f1046CodeIedRecv = f1046CodeIedRecv;
	}

	public String getF1046CodeIedSend() {
		return f1046CodeIedSend;
	}

	public void setF1046CodeIedSend(String f1046CodeIedSend) {
		this.f1046CodeIedSend = f1046CodeIedSend;
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

	public Set<Tb1063CircuitEntity> getTb1063CircuitsByF1065Code() {
		return tb1063CircuitsByF1065Code;
	}

	public void setTb1063CircuitsByF1065Code(
			Set<Tb1063CircuitEntity> tb1063CircuitsByF1065Code) {
		this.tb1063CircuitsByF1065Code = tb1063CircuitsByF1065Code;
	}

	public BaseCbEntity getBaseCbByCdCode() {
		return baseCbByCdCode;
	}

	public void setBaseCbByCdCode(BaseCbEntity baseCbByCdCode) {
		this.baseCbByCdCode = baseCbByCdCode;
	}

	public List<Tb1053PhysconnEntity> getPhyConns() {
		return phyConns;
	}

	public void setPhyConns(List<Tb1053PhysconnEntity> phyConns) {
		this.phyConns = phyConns;
	}

}
