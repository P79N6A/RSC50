package com.synet.tool.rsc.model;


/**
 * 装置端口
 * Created by chunc on 2018/8/7.
 */
public class Tb1048PortEntity {
    private String f1048Code;
    private String f1047Code;
    private String f1048No;
    private String f1048Desc;
    private int f1048Direction;
    private int f1048Plug;
    private Tb1047BoardEntity tb1047BoardByF1047Code;
    // 端口光强
    private Tb1006AnalogdataEntity tb1006AnalogdataByF1048Code;
    
    public Tb1048PortEntity() {
	}

    public Tb1048PortEntity(String f1048Code,String f1048No, String f1048Desc,
			 int f1048Direction, int f1048Plug) {
		super();
		this.f1048Code = f1048Code;
		this.f1048No = f1048No;
		this.f1048Desc = f1048Desc;
		this.f1048Direction = f1048Direction;
		this.f1048Plug = f1048Plug;
	}

	public String getF1048Code() {
        return f1048Code;
    }

    public void setF1048Code(String f1048Code) {
        this.f1048Code = f1048Code;
    }

    public String getF1047Code() {
        return f1047Code;
    }

    public void setF1047Code(String f1047Code) {
        this.f1047Code = f1047Code;
    }

    public String getF1048No() {
        return f1048No;
    }

    public void setF1048No(String f1048No) {
        this.f1048No = f1048No;
    }

    public String getF1048Desc() {
        return f1048Desc;
    }

    public void setF1048Desc(String f1048Desc) {
        this.f1048Desc = f1048Desc;
    }

    public int getF1048Direction() {
        return f1048Direction;
    }

    public void setF1048Direction(int f1048Direction) {
        this.f1048Direction = f1048Direction;
    }

    public int getF1048Plug() {
        return f1048Plug;
    }

    public void setF1048Plug(int f1048Plug) {
        this.f1048Plug = f1048Plug;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1048PortEntity that = (Tb1048PortEntity) o;

        if (f1048Direction != that.f1048Direction) return false;
        if (f1048Plug != that.f1048Plug) return false;
        if (f1047Code != null ? !f1047Code.equals(that.f1047Code) : that.f1047Code != null) return false;
        if (f1048Code != null ? !f1048Code.equals(that.f1048Code) : that.f1048Code != null) return false;
        if (f1048Desc != null ? !f1048Desc.equals(that.f1048Desc) : that.f1048Desc != null) return false;
        if (f1048No != null ? !f1048No.equals(that.f1048No) : that.f1048No != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1048Code != null ? f1048Code.hashCode() : 0;
        result = 31 * result + (f1047Code != null ? f1047Code.hashCode() : 0);
        result = 31 * result + (f1048No != null ? f1048No.hashCode() : 0);
        result = 31 * result + (f1048Desc != null ? f1048Desc.hashCode() : 0);
        result = 31 * result + f1048Direction;
        result = 31 * result + f1048Plug;
        return result;
    }

    public Tb1047BoardEntity getTb1047BoardByF1047Code() {
        return tb1047BoardByF1047Code;
    }

    public void setTb1047BoardByF1047Code(Tb1047BoardEntity tb1047BoardByF1047Code) {
        this.tb1047BoardByF1047Code = tb1047BoardByF1047Code;
    }

	public Tb1006AnalogdataEntity getTb1006AnalogdataByF1048Code() {
		return tb1006AnalogdataByF1048Code;
	}

	public void setTb1006AnalogdataByF1048Code(
			Tb1006AnalogdataEntity tb1006AnalogdataByF1048Code) {
		this.tb1006AnalogdataByF1048Code = tb1006AnalogdataByF1048Code;
	}

}
