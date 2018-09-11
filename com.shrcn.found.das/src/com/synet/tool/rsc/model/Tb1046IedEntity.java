package com.synet.tool.rsc.model;



/**
 * 装置
 * Created by chunc on 2018/8/7.
 */
public class Tb1046IedEntity {
    private String f1046Code;
    private String f1042Code;
    private String f1050Code;
    private String f1046Name;
    private String f1046Desc;
    private String f1046Manufacturor;
    private String f1046Model;
    private String f1046ConfigVersion;
    private Integer f1046AorB;
    private int f1046IsVirtual;
    private Integer f1046Type;
    private String f1046Crc;
    private Tb1042BayEntity tb1042BaysByF1042Code;
    private Tb1050CubicleEntity tb1050CubicleEntity;
    
    //手动配置属性
    private String f1046aNetIp;//A王IP
    private String f1046bNetIp;//B王IP
    private String f1046version;//版本
    private String f1046protectCategory;//保护分类
    private String f1046protectType;//保护型号
    private String f1046protectModel;//保护类型
    private String f1046protectCrc;//保护CRC码
    private String f1046OperateDate;//投运日期
    private String f1046productDate;//出厂日期
    private String f1046productNo;//出厂编号
    private String f1046dataGatType;//数据采集方式
    private String f1046OutType;//出口方式
    private String f1046boardNum;//板卡数量
    
    public Tb1046IedEntity() {
	}
    
    
    public Tb1046IedEntity(String f1046Manufacturor, String f1046Model,
			String f1046ConfigVersion) {
		super();
		this.f1046Manufacturor = f1046Manufacturor;
		this.f1046Model = f1046Model;
		this.f1046ConfigVersion = f1046ConfigVersion;
	}



	public String getF1042Code() {
		return f1042Code;
	}

	public void setF1042Code(String f1042Code) {
		this.f1042Code = f1042Code;
	}

	public String getF1046aNetIp() {
		return f1046aNetIp;
	}

	public void setF1046aNetIp(String f1046aNetIp) {
		this.f1046aNetIp = f1046aNetIp;
	}

	public String getF1046bNetIp() {
		return f1046bNetIp;
	}

	public void setF1046bNetIp(String f1046bNetIp) {
		this.f1046bNetIp = f1046bNetIp;
	}
	
	public String getF1046version() {
		return f1046version;
	}

	public void setF1046version(String f1046version) {
		this.f1046version = f1046version;
	}
	
	public String getF1046protectCategory() {
		return f1046protectCategory;
	}

	public void setF1046protectCategory(String f1046protectCategory) {
		this.f1046protectCategory = f1046protectCategory;
	}

	public String getF1046protectType() {
		return f1046protectType;
	}

	public void setF1046protectType(String f1046protectType) {
		this.f1046protectType = f1046protectType;
	}

	public String getF1046protectCrc() {
		return f1046protectCrc;
	}

	public void setF1046protectCrc(String f1046protectCrc) {
		this.f1046protectCrc = f1046protectCrc;
	}

	public String getF1046OperateDate() {
		return f1046OperateDate;
	}

	public void setF1046OperateDate(String f1046OperateDate) {
		this.f1046OperateDate = f1046OperateDate;
	}

	public String getF1046productDate() {
		return f1046productDate;
	}

	public void setF1046productDate(String f1046productDate) {
		this.f1046productDate = f1046productDate;
	}

	public String getF1046productNo() {
		return f1046productNo;
	}

	public void setF1046productNo(String f1046productNo) {
		this.f1046productNo = f1046productNo;
	}

	public String getF1046dataGatType() {
		return f1046dataGatType;
	}

	public void setF1046dataGatType(String f1046dataGatType) {
		this.f1046dataGatType = f1046dataGatType;
	}

	public String getF1046OutType() {
		return f1046OutType;
	}

	public void setF1046OutType(String f1046OutType) {
		this.f1046OutType = f1046OutType;
	}

	public String getF1046boardNum() {
		return f1046boardNum;
	}

	public void setF1046boardNum(String f1046boardNum) {
		this.f1046boardNum = f1046boardNum;
	}


	public String getF1046Code() {
        return f1046Code;
    }

    public void setF1046Code(String f1046Code) {
        this.f1046Code = f1046Code;
    }

    public String getF1050Code() {
        return f1050Code;
    }

    public void setF1050Code(String f1050Code) {
        this.f1050Code = f1050Code;
    }

    public String getF1046Name() {
        return f1046Name;
    }

    public void setF1046Name(String f1046Name) {
        this.f1046Name = f1046Name;
    }

    public String getF1046Desc() {
        return f1046Desc;
    }

    public void setF1046Desc(String f1046Desc) {
        this.f1046Desc = f1046Desc;
    }

    public String getF1046Manufacturor() {
        return f1046Manufacturor;
    }

    public void setF1046Manufacturor(String f1046Manufacturor) {
        this.f1046Manufacturor = f1046Manufacturor;
    }

    public String getF1046Model() {
        return f1046Model;
    }

    public void setF1046Model(String f1046Model) {
        this.f1046Model = f1046Model;
    }

    public String getF1046ConfigVersion() {
        return f1046ConfigVersion;
    }

    public void setF1046ConfigVersion(String f1046ConfigVersion) {
        this.f1046ConfigVersion = f1046ConfigVersion;
    }

    public Integer getF1046AorB() {
        return f1046AorB;
    }

    public void setF1046AorB(Integer f1046AorB) {
        this.f1046AorB = f1046AorB;
    }

    public int getF1046IsVirtual() {
        return f1046IsVirtual;
    }

    public void setF1046IsVirtual(int f1046IsVirtual) {
        this.f1046IsVirtual = f1046IsVirtual;
    }

    public Integer getF1046Type() {
        return f1046Type;
    }

    public void setF1046Type(Integer f1046Type) {
        this.f1046Type = f1046Type;
    }

    public String getF1046Crc() {
        return f1046Crc;
    }

    public void setF1046Crc(String f1046Crc) {
        this.f1046Crc = f1046Crc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tb1046IedEntity that = (Tb1046IedEntity) o;

        if (f1046IsVirtual != that.f1046IsVirtual) return false;
        if (f1046AorB != null ? !f1046AorB.equals(that.f1046AorB) : that.f1046AorB != null) return false;
        if (f1046Code != null ? !f1046Code.equals(that.f1046Code) : that.f1046Code != null) return false;
        if (f1042Code != null ? !f1042Code.equals(that.f1042Code) : that.f1042Code != null) return false;
        if (f1046ConfigVersion != null ? !f1046ConfigVersion.equals(that.f1046ConfigVersion) : that.f1046ConfigVersion != null)
            return false;
        if (f1046Crc != null ? !f1046Crc.equals(that.f1046Crc) : that.f1046Crc != null) return false;
        if (f1046Desc != null ? !f1046Desc.equals(that.f1046Desc) : that.f1046Desc != null) return false;
        if (f1046Manufacturor != null ? !f1046Manufacturor.equals(that.f1046Manufacturor) : that.f1046Manufacturor != null)
            return false;
        if (f1046Model != null ? !f1046Model.equals(that.f1046Model) : that.f1046Model != null) return false;
        if (f1046Name != null ? !f1046Name.equals(that.f1046Name) : that.f1046Name != null) return false;
        if (f1046Type != null ? !f1046Type.equals(that.f1046Type) : that.f1046Type != null) return false;
        if (f1050Code != null ? !f1050Code.equals(that.f1050Code) : that.f1050Code != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = f1046Code != null ? f1046Code.hashCode() : 0;
        result = 31 * result + (f1050Code != null ? f1050Code.hashCode() : 0);
        result = 31 * result + (f1042Code != null ? f1042Code.hashCode() : 0);
        result = 31 * result + (f1046Name != null ? f1046Name.hashCode() : 0);
        result = 31 * result + (f1046Desc != null ? f1046Desc.hashCode() : 0);
        result = 31 * result + (f1046Manufacturor != null ? f1046Manufacturor.hashCode() : 0);
        result = 31 * result + (f1046Model != null ? f1046Model.hashCode() : 0);
        result = 31 * result + (f1046ConfigVersion != null ? f1046ConfigVersion.hashCode() : 0);
        result = 31 * result + (f1046AorB != null ? f1046AorB.hashCode() : 0);
        result = 31 * result + f1046IsVirtual;
        result = 31 * result + (f1046Type != null ? f1046Type.hashCode() : 0);
        result = 31 * result + (f1046Crc != null ? f1046Crc.hashCode() : 0);
        return result;
    }

	public Tb1042BayEntity getTb1042BaysByF1042Code() {
		return tb1042BaysByF1042Code;
	}

	public void setTb1042BaysByF1042Code(Tb1042BayEntity tb1042BaysByF1042Code) {
		this.tb1042BaysByF1042Code = tb1042BaysByF1042Code;
	}

	public Tb1050CubicleEntity getTb1050CubicleEntity() {
		return tb1050CubicleEntity;
	}

	public void setTb1050CubicleEntity(Tb1050CubicleEntity tb1050CubicleEntity) {
		this.tb1050CubicleEntity = tb1050CubicleEntity;
	}

	public String getF1046protectModel() {
		return f1046protectModel;
	}

	public void setF1046protectModel(String f1046protectModel) {
		this.f1046protectModel = f1046protectModel;
	}

}
