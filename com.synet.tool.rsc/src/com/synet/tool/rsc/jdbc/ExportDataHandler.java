package com.synet.tool.rsc.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.eclipse.core.runtime.IProgressMonitor;

import com.synet.tool.rsc.model.Tb1006AnalogdataEntity;
import com.synet.tool.rsc.model.Tb1016StatedataEntity;
import com.synet.tool.rsc.model.Tb1022FaultconfigEntity;
import com.synet.tool.rsc.model.Tb1026StringdataEntity;
import com.synet.tool.rsc.model.Tb1041SubstationEntity;
import com.synet.tool.rsc.model.Tb1042BayEntity;
import com.synet.tool.rsc.model.Tb1043EquipmentEntity;
import com.synet.tool.rsc.model.Tb1044TerminalEntity;
import com.synet.tool.rsc.model.Tb1045ConnectivitynodeEntity;
import com.synet.tool.rsc.model.Tb1046IedEntity;
import com.synet.tool.rsc.model.Tb1047BoardEntity;
import com.synet.tool.rsc.model.Tb1048PortEntity;
import com.synet.tool.rsc.model.Tb1049RegionEntity;
import com.synet.tool.rsc.model.Tb1050CubicleEntity;
import com.synet.tool.rsc.model.Tb1051CableEntity;
import com.synet.tool.rsc.model.Tb1052CoreEntity;
import com.synet.tool.rsc.model.Tb1053PhysconnEntity;
import com.synet.tool.rsc.model.Tb1054RcbEntity;
import com.synet.tool.rsc.model.Tb1055GcbEntity;
import com.synet.tool.rsc.model.Tb1056SvcbEntity;
import com.synet.tool.rsc.model.Tb1057SgcbEntity;
import com.synet.tool.rsc.model.Tb1058MmsfcdaEntity;
import com.synet.tool.rsc.model.Tb1059SgfcdaEntity;
import com.synet.tool.rsc.model.Tb1060SpfcdaEntity;
import com.synet.tool.rsc.model.Tb1061PoutEntity;
import com.synet.tool.rsc.model.Tb1062PinEntity;
import com.synet.tool.rsc.model.Tb1063CircuitEntity;
import com.synet.tool.rsc.model.Tb1064StrapEntity;
import com.synet.tool.rsc.model.Tb1065LogicallinkEntity;
import com.synet.tool.rsc.model.Tb1066ProtmmxuEntity;
import com.synet.tool.rsc.model.Tb1067CtvtsecondaryEntity;
import com.synet.tool.rsc.model.Tb1068ProtctrlEntity;
import com.synet.tool.rsc.model.Tb1069RcdchannelaEntity;
import com.synet.tool.rsc.model.Tb1070MmsserverEntity;
import com.synet.tool.rsc.model.Tb1071DauEntity;
import com.synet.tool.rsc.model.Tb1072RcdchanneldEntity;
import com.synet.tool.rsc.model.Tb1073LlinkphyrelationEntity;
import com.synet.tool.rsc.model.Tb1090LineprotfiberEntity;
import com.synet.tool.rsc.model.Tb1091IotermEntity;
import com.synet.tool.rsc.model.Tb1092PowerkkEntity;
import com.synet.tool.rsc.model.Tb1093VoltagekkEntity;

public class ExportDataHandler extends AbstractExportDataHandler {
	
	private static final int[] tb_indice = new int[] {
		// Substation
		TB1041_INDEX,
		TB1042_INDEX,
		
		// IED
		TB1046_INDEX,
		TB1070_INDEX,//TB1070_MMSServer
		TB1054_INDEX,
		TB1055_INDEX,
		TB1056_INDEX,
		TB1057_INDEX,
		// data
		TB1006_INDEX,
		TB1016_INDEX,
		TB1026_INDEX,
		// fcda
		TB1058_INDEX,
		TB1059_INDEX,
		TB1060_INDEX,
		TB1064_INDEX, // strap
		TB1061_INDEX, // pout
		TB1065_INDEX, // logiclink
		TB1063_INDEX, // circuit
		TB1062_INDEX, // pin
		
		// equipment
		TB1043_INDEX,
		TB1044_INDEX,
		TB1045_INDEX,
		TB1067_INDEX,
		TB1066_INDEX,//TB1066_ProtMMXU
		TB1068_INDEX,//TB1068_ProtCtrl
		TB1069_INDEX,//TB1069_RCDChannelA
		TB1072_INDEX,//TB1072_RCDChannelD
		
		// 区域、屏柜、光缆、光芯
		TB1049_INDEX,
		TB1050_INDEX,
		TB1051_INDEX,
		TB1052_INDEX,
		TB1053_INDEX,//TB1053_PhysConn
		TB1073_INDEX,//TB1073_LLinkPhyRelation

		TB1090_INDEX,
		TB1091_INDEX,
		TB1092_INDEX,
		TB1093_INDEX,

//		TB1022_INDEX,
		TB1071_INDEX //TB1071_DAU
	};
	
	@Override
	public boolean exportData(ConnParam connParam, IProgressMonitor monitor) {
		exportConnManager = new ExportConnManager();
		exportConnManager.setConnParam(connParam);
		connect = exportConnManager.getConnection();
		if (connect == null)
			return false;
		try {
			monitor.beginTask("开始导出", tb_indice.length*2);
			long start = System.currentTimeMillis();
			for (int idx = tb_indice.length-1; idx > -1; idx--) {
				clearTableDate(tb_indice[idx], monitor);
				monitor.worked(1);
			}
			for (int idx = 0; idx < tb_indice.length; idx++) {
				exprotTableDate(tb_indice[idx], monitor);
				monitor.worked(1);
			}
			long time = (System.currentTimeMillis() - start) / 1000;
			monitor.setTaskName("导出耗时：" + time + "秒");
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connect.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		} finally {
			if (connect != null) {
				exportConnManager.close(connect);
			}
		}
		return true;
	}
	
	@Override
	protected String getTb1006Sql() {
		return "INSERT INTO TB1006_ANALOGDATA(F1006_CODE,F1006_DESC,F1006_SAFELEVEL,PARENT_CODE,F1011_NO," +
				"F1006_BYNAME,F0008_NAME,F0009_NAME,F1006_CALCFLAG,F1006_PICNAME,F1006_PDRMODE,F1006_K,F1006_B," +
				"F1006_ZERODBAND,F1006_OVERFLOW,F1006_LOWFLOW,F1006_MAXINC,F1006_HIWARN,F1006_LOWARN,F1006_HIALARM," +
				"F1006_LOALARM,F1006_SAVEPERIOD,F1006_PLANTIME,F1006_DEADTIME,F1006_ALARMLEVEL,F1006_SAVETYPE)" +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//26个参数
	}
	
	@Override
	protected void setValueByTb1006(PreparedStatement preState, Object obj) throws SQLException {
		Tb1006AnalogdataEntity entity = (Tb1006AnalogdataEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1006Code());
		setSring(preState, index++, entity.getF1006Desc());
		setInt(preState, index++, entity.getF1006Safelevel());
		setSring(preState, index++, entity.getParentCode());
		setInt(preState, index++, entity.getF1011No());
		setSring(preState, index++, entity.getF1006Byname());
		setSring(preState, index++, entity.getF0008Name());
		setSring(preState, index++, entity.getF0009Name());
		setInt(preState, index++, entity.getF1006Calcflag());
		setSring(preState, index++, entity.getF1006Picname());
		setInt(preState, index++, entity.getF1006Pdrmode());
		setFloat(preState, index++, entity.getF1006K());
		setFloat(preState, index++, entity.getF1006B());
		setFloat(preState, index++, entity.getF1006Zerodband());
		setFloat(preState, index++, entity.getF1006Overflow());
		setFloat(preState, index++, entity.getF1006Lowflow());
		setFloat(preState, index++, entity.getF1006Maxinc());
		setFloat(preState, index++, entity.getF1006Hiwarn());
		setFloat(preState, index++, entity.getF1006Lowarn());
		setFloat(preState, index++, entity.getF1006Hialarm());
		setFloat(preState, index++, entity.getF1006Loalarm());
		setInt(preState, index++, entity.getF1006Saveperiod());
		setInt(preState, index++, entity.getF1006Plantime());
		setInt(preState, index++, entity.getF1006Deadtime());
		setInt(preState, index++, entity.getF1006Alarmlevel());
		setInt(preState, index++, entity.getF1006Savetype());
	}
	
	@Override
	protected String getTb1016Sql() {
		return "INSERT INTO TB1016_STATEDATA(F1016_CODE,F1016_DESC,F1016_SAFELEVEL,PARENT_CODE,F1011_NO," +
				"F1016_BYNAME,F0008_NAME,F0009_NAME,F1016_CALCFLAG,F1016_PICNAME,F1016_ISPDR,F1016_PDRNO," +
				"F1016_DPSFLAG,F1016_MAINSTFLAG,F1016_DPSCALCFLAG,F1016_SOE,F1016_SGLIMITVAL,F1016_ALARMPROCMODE," +
				"F1016_PROCBAND,F1016_SGPROCNAME,F1016_REVFLAG,F1016_ISSTA)" +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1016(PreparedStatement preState, Object obj) throws SQLException {
		Tb1016StatedataEntity entity = (Tb1016StatedataEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1016Code());
		setSring(preState, index++, entity.getF1016Desc());
		setInt(preState, index++, entity.getF1016Safelevel());
		setSring(preState, index++, entity.getParentCode());
		setInt(preState, index++, entity.getF1011No());
		setSring(preState, index++, entity.getF1016Byname());
		setSring(preState, index++, entity.getF0008Name());
		setSring(preState, index++, entity.getF0009Name());
		setInt(preState, index++, entity.getF1016Calcflag());
		setSring(preState, index++, entity.getF1016Picname());
		setInt(preState, index++, entity.getF1016Ispdr());
		setInt(preState, index++, entity.getF1016Pdrno());
		setInt(preState, index++, entity.getF1016Dpsflag());
		setInt(preState, index++, entity.getF1016Mainstflag());
		setInt(preState, index++, entity.getF1016Dpscalcflag());
		setSring(preState, index++, entity.getF1016Soe());
		setInt(preState, index++, entity.getF1016Sglimitval());
		setInt(preState, index++, entity.getF1016Alarmprocmode());
		setInt(preState, index++, entity.getF1016Procband());
		setSring(preState, index++, entity.getF1016Sgprocname());
		setInt(preState, index++, entity.getF1016Revflag());
		setInt(preState, index++, entity.getF1016Issta());
	}
	
	@Override
	protected String getTb1022Sql() {
		return "INSERT INTO TB1022_FAULTCONFIG(F1022_CODE,F1011_NO,F1022_FAULTLEVEL,F1022_T1,F1022_T2,F1022_K)" +
				" VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1022(PreparedStatement preState, Object obj) throws SQLException {
		Tb1022FaultconfigEntity entity = (Tb1022FaultconfigEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1022Code());
		setInt(preState, index++, entity.getF1011No());
		setInt(preState, index++, entity.getF1022Faultlevel());
		setInt(preState, index++, entity.getF1022T1());
		setInt(preState, index++, entity.getF1022T2());
		setInt(preState, index++, entity.getF1022K());
	}
	
	@Override
	protected String getTb1026Sql() {
		return "INSERT INTO TB1026_STRINGDATA(F1026_CODE,F1026_DESC,F1026_SAFELEVEL,PARENT_CODE,F1011_NO,F1026_BYNAME," +
				"F0008_NAME,F0009_NAME,F1026_CALCFLAG,F1026_PICNAME,F1026_ISPDR,F1026_PDRNO,F1026_ISSTA)" +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1026(PreparedStatement preState, Object obj) throws SQLException {
		Tb1026StringdataEntity entity = (Tb1026StringdataEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1026Code());
		setSring(preState, index++, entity.getF1026Desc());
		setInt(preState, index++, entity.getF1026Safelevel());
		setSring(preState, index++, entity.getParentCode());
		setInt(preState, index++, entity.getF1011No());
		setSring(preState, index++, entity.getF1026Byname());
		setSring(preState, index++, entity.getF0008Name());
		setSring(preState, index++, entity.getF0009Name());
		setInt(preState, index++, entity.getF1026Calcflag());
		setSring(preState, index++, entity.getF1026Picname());
		setInt(preState, index++, entity.getF1026Ispdr());
		setInt(preState, index++, entity.getF1026Pdrno());
		setInt(preState, index++, entity.getF1026Issta());
	}
	
	@Override
	protected String getTb1041Sql() {
		return "INSERT INTO TB1041_SUBSTATION(F1041_CODE,F1041_NAME,F1041_DESC,F1041_DQNAME,F1041_DQDESC,F1041_COMPANY,F1042_VOLTAGEH,F1042_VOLTAGEM,F1042_VOLTAGEL) VALUES " +
				"(?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1041(PreparedStatement preState, Object obj) throws SQLException {
		Tb1041SubstationEntity entity = (Tb1041SubstationEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1041Code());
		setSring(preState, index++, entity.getF1041Name());
		setSring(preState, index++, entity.getF1041Desc());
		setSring(preState, index++, entity.getF1041DqName());
		setSring(preState, index++, entity.getF1041Dqdesc());
		setSring(preState, index++, entity.getF1041Company());
		setInt(preState, index++, entity.getF1042VoltageH());
		setInt(preState, index++, entity.getF1042VoltageM());
		setInt(preState, index++, entity.getF1042VoltageL());
	}
	
	@Override
	protected String getTb1042Sql() {
		return "INSERT INTO TB1042_BAY(F1042_CODE,F1041_CODE,F1042_NAME,F1042_DESC,F1042_VOLTAGE," +
				"F1042_CONNTYPE,F1042_DEVTYPE,F1042_IEDSOLUTION)" +
				" VALUES (?,?,?,?,?,?,?,?)";
	}

	@Override
	protected void setValueByTb1042(PreparedStatement preState, Object obj) throws SQLException {
		Tb1042BayEntity entity = (Tb1042BayEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1042Code());
		setSring(preState, index++, entity.getTb1041SubstationByF1041Code().getF1041Code());
		setSring(preState, index++, entity.getF1042Name());
		setSring(preState, index++, entity.getF1042Desc());
		setInt(preState, index++, entity.getF1042Voltage());
		setInt(preState, index++, entity.getF1042ConnType());
		setInt(preState, index++, entity.getF1042DevType());
		setInt(preState, index++, entity.getF1042IedSolution());
	}
	
	@Override
	protected String getTb1043Sql() {
		return "INSERT INTO TB1043_EQUIPMENT(F1043_CODE,F1042_CODE,F1043_NAME,F1043_DESC,F1043_ISVIRTUAL," +
				"F1043_TYPE,F1016_CODE) VALUES (?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1043(PreparedStatement preState, Object obj) throws SQLException {
		Tb1043EquipmentEntity entity = (Tb1043EquipmentEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1043Code());
		setSring(preState, index++, entity.getTb1042BayByF1042Code().getF1042Code());
		setSring(preState, index++, entity.getF1043Name());
		setSring(preState, index++, entity.getF1043Desc());
		setInt(preState, index++, entity.getF1043IsVirtual());
		setInt(preState, index++, entity.getF1043Type());
		if (entity.getTb1016StatedataEntity() != null) {
			setSring(preState, index++, entity.getTb1016StatedataEntity().getF1016Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1044Sql() {
		return "INSERT INTO TB1044_TERMINAL(F1044_CODE,F1043_CODE,F1045_CODE,F1044_NAME,F1044_DESC)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1044(PreparedStatement preState, Object obj) throws SQLException {
		Tb1044TerminalEntity entity = (Tb1044TerminalEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1044Code());
		if (entity.getTb1043EquipmentByF1043Code() != null) {
			setSring(preState, index++, entity.getTb1043EquipmentByF1043Code().getF1043Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1045ConnectivitynodeByF1045Code() != null) {
			setSring(preState, index++, entity.getTb1045ConnectivitynodeByF1045Code().getF1045Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1044Name());
		setSring(preState, index++, entity.getF1044Desc());
	}
	
	@Override
	protected String getTb1045Sql() {
		return "INSERT INTO TB1045_CONNECTIVITYNODE(F1045_CODE,F1045_NAME,F1045_DESC) VALUES (?,?,?)";
	}
	
	@Override
	protected void setValueByTb1045(PreparedStatement preState, Object obj) throws SQLException {
		Tb1045ConnectivitynodeEntity entity = (Tb1045ConnectivitynodeEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1045Code());
		setSring(preState, index++, entity.getF1045Name());
		setSring(preState, index++, entity.getF1045Desc());
	}
	
	@Override
	protected String getTb1046Sql() {
//		return "INSERT INTO TB1046_IED(F1046_CODE,F1042_CODE,F1050_CODE,F1046_NAME,F1046_DESC,F1046_MANUFACTUROR," +
//				"F1046_MODEL,F1046_CONFIGVERSION,F1046_AORB,F1046_ISVIRTUAL,F1046_TYPE,F1046_CRC,F1046_ANETIP," +
//				"F1046_BNETIP,F1046_VERSION,F1046_PROTECTCATEGORY,F1046_PROTECTTYPE,F1046_PROTECTMODEL," +
//				"F1046_PROTECTCRC,F1046_OPERATEDATE,F1046_PRODUCTDATE,F1046_PRODUCTNO,F1046_DATAGATTYPE," +
//				"F1046_OUTTYPE,F1046_BOARDNUM) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		return "INSERT INTO TB1046_IED(F1046_CODE,F1042_CODE,F1050_CODE,F1046_NAME,F1046_DESC,F1046_MANUFACTUROR," +
		"F1046_MODEL,F1046_CONFIGVERSION,F1046_AORB,F1046_ISVIRTUAL,F1046_TYPE,F1046_CRC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1046(PreparedStatement preState, Object obj) throws SQLException {
		Tb1046IedEntity entity = (Tb1046IedEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1046Code());
		if (entity.getTb1042BaysByF1042Code() != null) {
			setSring(preState, index++, entity.getTb1042BaysByF1042Code().getF1042Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1050CubicleEntity() != null) {
			setSring(preState, index++, entity.getTb1050CubicleEntity().getF1050Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1046Name());
		setSring(preState, index++, entity.getF1046Desc());
		setSring(preState, index++, entity.getF1046Manufacturor());
		setSring(preState, index++, entity.getF1046Model());
		setSring(preState, index++, entity.getF1046ConfigVersion());
		setInt(preState, index++, entity.getF1046AorB());
		setInt(preState, index++, entity.getF1046IsVirtual());
		setInt(preState, index++, entity.getF1046Type());
		setSring(preState, index++, entity.getF1046Crc());
//		setSring(preState, index++, entity.getF1046aNetIp());
//		setSring(preState, index++, entity.getF1046bNetIp());
//		setSring(preState, index++, entity.getF1046version());
//		setSring(preState, index++, entity.getF1046protectCategory());
//		setSring(preState, index++, entity.getF1046protectType());
//		setSring(preState, index++, entity.getF1046protectModel());
//		setSring(preState, index++, entity.getF1046protectCrc());
//		setSring(preState, index++, entity.getF1046OperateDate());
//		setSring(preState, index++, entity.getF1046productDate());
//		setSring(preState, index++, entity.getF1046productNo());
//		setSring(preState, index++, entity.getF1046dataGatType());
//		setSring(preState, index++, entity.getF1046OutType());
//		setInt(preState, index++, entity.getF1046boardNum());
	}
	
	@Override
	protected String getTb1047Sql() {
		return "INSERT INTO	TB1047_BOARD(F1047_CODE,F1046_CODE,F1047_SLOT,F1047_DESC,F1047_TYPE)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1047(PreparedStatement preState, Object obj) throws SQLException {
		Tb1047BoardEntity entity = (Tb1047BoardEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1047Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1047Slot());
		setSring(preState, index++, entity.getF1047Desc());
		setSring(preState, index++, entity.getF1047Type());
	}
	
	@Override
	protected String getTb1048Sql() {
		return "INSERT INTO TB1048_PORT(F1048_CODE,F1047_CODE,F1048_NO,F1048_DESC,F1048_DIRECTION,F1048_PLUG)" +
				" VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1048(PreparedStatement preState, Object obj) throws SQLException {
		Tb1048PortEntity entity = (Tb1048PortEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1048Code());
		if (entity.getTb1047BoardByF1047Code() != null) {
			setSring(preState, index++, entity.getTb1047BoardByF1047Code().getF1047Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1048No());
		setSring(preState, index++, entity.getF1048Desc());
		setInt(preState, index++, entity.getF1048Direction());
		setInt(preState, index++, entity.getF1048Plug());
	}
	
	@Override
	protected String getTb1049Sql() {
		return "INSERT INTO TB1049_REGION(F1049_CODE,F1041_CODE,F1049_NAME,F1049_DESC,F1049_AREA)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1049(PreparedStatement preState, Object obj) throws SQLException {
		Tb1049RegionEntity entity = (Tb1049RegionEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1049Code());
		if (entity.getTb1041SubstationByF1041Code() != null) {
			setSring(preState, index++, entity.getTb1041SubstationByF1041Code().getF1041Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1049Name());
		setSring(preState, index++, entity.getF1049Desc());
		setInt(preState, index++, entity.getF1049Area());

	}
	
	@Override
	protected String getTb1050Sql() {
		return "INSERT INTO TB1050_CUBICLE(F1050_CODE,F1049_CODE,F1050_NAME,F1050_DESC) VALUES (?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1050(PreparedStatement preState, Object obj) throws SQLException {
		Tb1050CubicleEntity entity = (Tb1050CubicleEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1050Code());
		if (entity.getTb1049RegionByF1049Code() != null) {
		setSring(preState, index++, entity.getTb1049RegionByF1049Code().getF1049Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1050Name());
		setSring(preState, index++, entity.getF1050Desc());
	}
	
	@Override
	protected String getTb1051Sql() {
		return "INSERT INTO TB1051_CABLE(F1051_CODE,F1041_CODE,F1051_NAME,F1051_DESC,F1051_LENGTH," +
				"F1051_CORENUM,F1050_CODE_A,F1050_CODE_B,F1051_TYPE) VALUES (?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1051(PreparedStatement preState, Object obj) throws SQLException {
		Tb1051CableEntity entity = (Tb1051CableEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1051Code());
		if (entity.getTb1041SubstationByF1041Code() != null) {
			setSring(preState, index++, entity.getTb1041SubstationByF1041Code().getF1041Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1051Name());
		setSring(preState, index++, entity.getF1051Desc());
		setInt(preState, index++, entity.getF1051Length());
		setInt(preState, index++, entity.getF1051CoreNum());
		if (entity.getTb1050CubicleByF1050CodeA() != null) {
			setSring(preState, index++, entity.getTb1050CubicleByF1050CodeA().getF1050Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1050CubicleByF1050CodeB() != null) {
			setSring(preState, index++, entity.getTb1050CubicleByF1050CodeB().getF1050Code());
		} else {
			setSring(preState, index++, null);
		}
		setInt(preState, index++, entity.getF1051Type());
	}
	@Override
	protected String getTb1052Sql() {
		return "INSERT INTO TB1052_CORE(F1052_CODE,PARENT_CODE,F1052_TYPE,F1052_NO,F1048_CODE_A,F1048_CODE_B)" +
				" VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1052(PreparedStatement preState, Object obj) throws SQLException {
		Tb1052CoreEntity entity = (Tb1052CoreEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1052Code());
		setSring(preState, index++, entity.getParentCode());
		setInt(preState, index++, entity.getF1052Type());
		setInt(preState, index++, entity.getF1052No());
		setSring(preState, index++, entity.getF1048CodeA());
		setSring(preState, index++, entity.getF1048CodeB());
	}
	
	@Override
	protected String getTb1053Sql() {
		return "INSERT INTO TB1053_PHYSCONN(F1053_CODE,F1041_CODE,F1048_CODE_A,F1048_CODE_B) VALUES (?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1053(PreparedStatement preState, Object obj) throws SQLException {
		Tb1053PhysconnEntity entity = (Tb1053PhysconnEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1053Code());
		setSring(preState, index++, entity.getF1041Code());
		if (entity.getTb1048PortByF1048CodeA() != null) {
			setSring(preState, index++, entity.getTb1048PortByF1048CodeA().getF1048Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1048PortByF1048CodeB() != null) {
			setSring(preState, index++, entity.getTb1048PortByF1048CodeB().getF1048Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1054Sql() {
		return "INSERT INTO TB1054_RCB(F1054_CODE,F1046_CODE,F1054_RPTID,F1054_DATASET,F1054_DSDESC," +
				"F1054_ISBRCB,F1054_CBTYPE) VALUES (?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1054(PreparedStatement preState, Object obj) throws SQLException {
		Tb1054RcbEntity entity = (Tb1054RcbEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1054Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1054Rptid());
		setSring(preState, index++, entity.getF1054Dataset());
		setSring(preState, index++, entity.getF1054DsDesc());
		setInt(preState, index++, entity.getF1054IsBrcb());
		setInt(preState, index++, entity.getF1054CbType());
	}
	
	@Override
	protected String getTb1055Sql() {
		return "INSERT INTO TB1055_GCB(CB_CODE,F1046_CODE,CBNAME,CBID,MACADDR,VLANID,VLANPRIORITY,APPID," +
				"DATASET,DSDESC,F1071_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1055(PreparedStatement preState, Object obj) throws SQLException {
		Tb1055GcbEntity entity = (Tb1055GcbEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getCbCode());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getCbName());
		setSring(preState, index++, entity.getCbId());
		setSring(preState, index++, entity.getMacAddr());
		setSring(preState, index++, entity.getVlanid());
		setSring(preState, index++, entity.getVlanPriority());
		setSring(preState, index++, entity.getAppid());
		setSring(preState, index++, entity.getDataset());
		setSring(preState, index++, entity.getDsDesc());
		setSring(preState, index++, entity.getF1071Code());
	}
	
	@Override
	protected String getTb1056Sql() {
		return "INSERT INTO TB1056_SVCB(CB_CODE,F1046_CODE,CBNAME,CBID,MACADDR,VLANID,VLANPRIORITY,APPID,DATASET," +
				"DSDESC,F1071_CODE) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1056(PreparedStatement preState, Object obj) throws SQLException {
		Tb1056SvcbEntity entity = (Tb1056SvcbEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getCbCode());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getCbName());
		setSring(preState, index++, entity.getCbId());
		setSring(preState, index++, entity.getMacAddr());
		setSring(preState, index++, entity.getVlanid());
		setSring(preState, index++, entity.getVlanPriority());
		setSring(preState, index++, entity.getAppid());
		setSring(preState, index++, entity.getDataset());
		setSring(preState, index++, entity.getDsDesc());
		setSring(preState, index++, entity.getF1071Code());
	}
	
	@Override
	protected String getTb1057Sql() {
		return "INSERT INTO TB1057_SGCB(F1057_CODE,F1046_CODE,F1057_CBNAME,F1057_DATASET,F1057_DSDESC)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1057(PreparedStatement preState, Object obj) throws SQLException {
		Tb1057SgcbEntity entity = (Tb1057SgcbEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1057Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1057CbName());
		setSring(preState, index++, entity.getF1057Dataset());
		setSring(preState, index++, entity.getF1057DsDesc());
	}
	
	@Override
	protected String getTb1058Sql() {
		return "INSERT INTO TB1058_MMSFCDA(F1058_CODE,F1046_CODE,F1054_CODE,F1058_REFADDR,F1058_INDEX," +
				"F1058_DESC,F1058_DATATYPE,DATA_CODE) VALUES (?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1058(PreparedStatement preState, Object obj) throws SQLException {
		Tb1058MmsfcdaEntity entity = (Tb1058MmsfcdaEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1058Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1054RcbByF1054Code() != null) {
			setSring(preState, index++, entity.getTb1054RcbByF1054Code().getF1054Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1058RefAddr());
		setInt(preState, index++, entity.getF1058Index());
		setSring(preState, index++, entity.getF1058Desc());
		setInt(preState, index++, entity.getF1058DataType());
		setSring(preState, index++, entity.getDataCode());
	}
	
	@Override
	protected String getTb1059Sql() {
		return "INSERT INTO TB1059_SGFCDA(F1059_CODE,F1057_CODE,F1059_REFADDR,F1059_INDEX,F1059_DATATYPE," +
				"F1059_DESC,F1059_UNIT,F1059_STEPSIZE,F1059_VALUEMIN,F1059_VALUEMAX,F1059_BASEVALUE)" +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1059(PreparedStatement preState, Object obj) throws SQLException {
		Tb1059SgfcdaEntity entity = (Tb1059SgfcdaEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1059Code());
		if (entity.getTb1057SgcbByF1057Code() != null) {
			setSring(preState, index++, entity.getTb1057SgcbByF1057Code().getF1057Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1059RefAddr());
		setInt(preState, index++, entity.getF1059Index());
		setInt(preState, index++, entity.getF1059DataType());
		setSring(preState, index++, entity.getF1059Desc());
		setSring(preState, index++, entity.getF1059Unit());
		setFloat(preState, index++, entity.getF1059StepSize());
		setFloat(preState, index++, entity.getF1059ValueMin());
		setFloat(preState, index++, entity.getF1059ValueMax());
		setFloat(preState, index++, entity.getF1059BaseValue());
		
	}
	
	@Override
	protected String getTb1060Sql() {
		return "INSERT INTO TB1060_SPFCDA(F1060_CODE, F1046_CODE,F1060_REFADDR,F1060_INDEX,F1060_DATATYPE," +
				"F1060_DESC,F1060_UNIT,F1060_STEPSIZE,F1060_VALUEMIN,F1060_VALUEMAX)" +
				" VALUES (?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1060(PreparedStatement preState, Object obj) throws SQLException {
		Tb1060SpfcdaEntity entity = (Tb1060SpfcdaEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1060Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1060RefAddr());
		setInt(preState, index++, entity.getF1060Index());
		setInt(preState, index++, entity.getF1060DataType());
		setSring(preState, index++, entity.getF1060Desc());
		setSring(preState, index++, entity.getF1060Unit());
		setFloat(preState, index++, entity.getF1060StepSize());
		setFloat(preState, index++, entity.getF1060ValueMin());
		setFloat(preState, index++, entity.getF1060ValueMax());
	}
	
	@Override
	protected String getTb1061Sql() {
		return "INSERT INTO TB1061_POUT(F1061_CODE,F1046_CODE,CB_CODE,F1061_REFADDR,F1061_INDEX,F1061_DESC," +
				"F1064_CODE,DATA_CODE) VALUES (?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1061(PreparedStatement preState, Object obj) throws SQLException {
		Tb1061PoutEntity entity = (Tb1061PoutEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1061Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getCbCode());
		setSring(preState, index++, entity.getF1061RefAddr());
		setInt(preState, index++, entity.getF1061Index());
		setSring(preState, index++, entity.getF1061Desc());
		if (entity.getTb1064StrapByF1064Code() != null) {
			setSring(preState, index++, entity.getTb1064StrapByF1064Code().getF1064Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getDataCode());
	}
	
	@Override
	protected String getTb1062Sql() {
		return "INSERT INTO TB1062_PIN(F1062_CODE,F1046_CODE,F1062_REFADDR,F1062_DESC,F1062_ISUSED," +
				"F1064_CODE) VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1062(PreparedStatement preState, Object obj) throws SQLException {
		Tb1062PinEntity entity = (Tb1062PinEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1062Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1062RefAddr());
		setSring(preState, index++, entity.getF1062Desc());
		setInt(preState, index++, entity.getF1062IsUsed());
		if (entity.getTb1064StrapByF1064Code() != null) {
			setSring(preState, index++, entity.getTb1064StrapByF1064Code().getF1064Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1063Sql() {
		return "INSERT INTO TB1063_CIRCUIT(F1063_CODE,F1046_CODE_IEDSEND,F1061_CODE_PSEND,F1046_CODE_IEDRECV," +
				"F1062_CODE_PRECV,F1065_CODE,F1061_CODE_CONVCHK1,F1061_CODE_CONVCHK2)" +
				" VALUES (?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1063(PreparedStatement preState, Object obj) throws SQLException {
		Tb1063CircuitEntity entity = (Tb1063CircuitEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1063Code());
		if (entity.getTb1046IedByF1046CodeIedSend() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeIedSend().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodePSend() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodePSend().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1046IedByF1046CodeIedRecv() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeIedRecv().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1062PinByF1062CodePRecv() != null) {
			setSring(preState, index++, entity.getTb1062PinByF1062CodePRecv().getF1062Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1065LogicallinkByF1065Code() != null) {
			setSring(preState, index++, entity.getTb1065LogicallinkByF1065Code().getF1065Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeConvChk1() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeConvChk1().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeConvChk2() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeConvChk2().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1064Sql() {
		return "INSERT INTO TB1064_STRAP(F1064_CODE,F1046_CODE,F1064_TYPE,F1064_NUM,F1064_DESC," +
				"F1042_CODE_RELATEDBAY) VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1064(PreparedStatement preState, Object obj) throws SQLException {
		Tb1064StrapEntity entity = (Tb1064StrapEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1064Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setInt(preState, index++, entity.getF1064Type());
		setSring(preState, index++, entity.getF1064Num());
		setSring(preState, index++, entity.getF1064Desc());
		setSring(preState, index++, entity.getF1042CodeRelatedBay());
	}
	
	@Override
	protected String getTb1065Sql() {
		return "INSERT INTO TB1065_LOGICALLINK(F1065_CODE,F1065_TYPE,F1046_CODE_IEDSEND,F1046_CODE_IEDRECV," +
				"CB_CODE) VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1065(PreparedStatement preState, Object obj) throws SQLException {
		Tb1065LogicallinkEntity entity = (Tb1065LogicallinkEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1065Code());
		setInt(preState, index++, entity.getF1065Type());
		if (entity.getTb1046IedByF1046CodeIedSend() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeIedSend().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1046IedByF1046CodeIedRecv() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeIedRecv().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getBaseCbByCdCode() != null) {
			setSring(preState, index++, entity.getBaseCbByCdCode().getCbCode());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1066Sql() {
		return "INSERT INTO TB1066_PROTMMXU(F1066_CODE,F1067_CODE,F1066_TYPE,F1046_CODE_PROT,F1006_CODE_A," +
				"F1006_CODE_B,F1006_CODE_C) VALUES (?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1066(PreparedStatement preState, Object obj) throws SQLException {
		Tb1066ProtmmxuEntity entity = (Tb1066ProtmmxuEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1066Code());
		if (entity.getTb1067CtvtsecondaryByF1067Code() != null) {
			setSring(preState, index++, entity.getTb1067CtvtsecondaryByF1067Code().getF1067Code());
		} else {
			setSring(preState, index++, null);
		}
		setInt(preState, index++, entity.getF1066Type());
		if (entity.getTb1046IedByF1046CodeProt() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeProt().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getF1006CodeA() != null) {
			setSring(preState, index++, entity.getF1006CodeA().getF1006Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getF1006CodeB() != null) {
			setSring(preState, index++, entity.getF1006CodeB().getF1006Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getF1006CodeC() != null) {
			setSring(preState, index++, entity.getF1006CodeC().getF1006Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1067Sql() {
		return "INSERT INTO TB1067_CTVTSECONDARY(F1067_CODE,F1043_CODE,F1067_INDEX,F1067_MODEL,F1067_TYPE," +
				"F1061_CODE_A1,F1061_CODE_A2,F1061_CODE_B1,F1061_CODE_B2,F1061_CODE_C1,F1061_CODE_C2," +
				"F1067_CIRCNO,F1067_DESC) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1067(PreparedStatement preState, Object obj) throws SQLException {
		Tb1067CtvtsecondaryEntity entity = (Tb1067CtvtsecondaryEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1067Code());
		if (entity.getTb1043EquipmentByF1043Code() != null) {
			setSring(preState, index++, entity.getTb1043EquipmentByF1043Code().getF1043Code());
		} else {
			setSring(preState, index++, null);
		}
		setInt(preState, index++, entity.getF1067Index());
		setSring(preState, index++, entity.getF1067Model());
		setSring(preState, index++, entity.getF1067Type());
		if (entity.getTb1061PoutByF1061CodeA1() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeA1().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeA2() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeA2().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeB1() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeB1().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeB2() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeB2().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeC1() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeC1().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1061PoutByF1061CodeC2() != null) {
			setSring(preState, index++, entity.getTb1061PoutByF1061CodeC2().getF1061Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1067CircNo());
		setSring(preState, index++, entity.getF1067Desc());
	}
	
	@Override
	protected String getTb1068Sql() {
		return "INSERT INTO TB1068_PROTCTRL(F1068_CODE,F1046_CODE_PROT,F1046_CODE_IO,F1043_CODE,F1065_CODE)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1068(PreparedStatement preState, Object obj) throws SQLException {
		Tb1068ProtctrlEntity entity = (Tb1068ProtctrlEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1068Code());
		if(entity.getTb1046IedByF1046CodeProt() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeProt().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if(entity.getTb1046IedByF1046CodeIo() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046CodeIo().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1043EquipmentByF1043Code() != null) {
			setSring(preState, index++, entity.getTb1043EquipmentByF1043Code().getF1043Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1065LogicallinkByF1065Code() != null) {
			setSring(preState, index++, entity.getTb1065LogicallinkByF1065Code().getF1065Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1069Sql() {
		return "INSERT INTO TB1069_RCDCHANNELA(F1069_CODE,IED_CODE,F1069_INDEX,F1069_TYPE,F1069_PHASE," +
				"F1043_CODE,F1061_CODE,F1058_CODE) VALUES (?,?,?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1069(PreparedStatement preState, Object obj) throws SQLException {
		Tb1069RcdchannelaEntity entity = (Tb1069RcdchannelaEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1069Code());
		if (entity.getTb1046IedByIedCode() != null) {
			setSring(preState, index++, entity.getTb1046IedByIedCode().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1069Index());
		setInt(preState, index++, entity.getF1069Type());
		setInt(preState, index++, entity.getF1069Phase());
		setSring(preState, index++, entity.getF1043Code());
		setSring(preState, index++, entity.getF1061Code());
		setSring(preState, index++, entity.getF1058Code());
	}
	
	@Override
	protected String getTb1070Sql() {
		return "INSERT INTO TB1070_MMSSERVER(F1070_CODE,F1046_CODE,F1070_IP_A,F1070_IP_B) VALUES (?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1070(PreparedStatement preState, Object obj) throws SQLException {
		Tb1070MmsserverEntity entity = (Tb1070MmsserverEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1070Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1070IpA());
		setSring(preState, index++, entity.getF1070IpB());
	}
	
	@Override
	protected String getTb1071Sql() {
		return "INSERT INTO TB1071_DAU(F1071_CODE,F1071_DESC,F1071_IPADDR) VALUES (?,?,?)";
	}
	
	@Override
	protected void setValueByTb1071(PreparedStatement preState, Object obj) throws SQLException {
		Tb1071DauEntity entity = (Tb1071DauEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1071Code());
		setSring(preState, index++, entity.getF1071Desc());
		setSring(preState, index++, entity.getF1071IpAddr());
	}
	
	@Override
	protected String getTb1072Sql() {
		return "INSERT INTO TB1072_RCDCHANNELD(F1072_CODE,IED_CODE,F1072_INDEX,F1072_TYPE,F1061_CODE,F1058_CODE)" +
				" VALUES (?,?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1072(PreparedStatement preState, Object obj) throws SQLException {
		Tb1072RcdchanneldEntity entity = (Tb1072RcdchanneldEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1072Code());
		if (entity.getTb1046IedByIedCode() != null) {
			setSring(preState, index++, entity.getTb1046IedByIedCode().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1072Index());
		setInt(preState, index++, entity.getF1072Type());
		setSring(preState, index++, entity.getF1061Code());
		setSring(preState, index++, entity.getF1058Code());
	}
	
	@Override
	protected String getTb1073Sql() {
		return "INSERT INTO TB1073_LLINKPHYRELATION(F1073_CODE,F1065_CODE,F1053_CODE) VALUES (?,?,?)";
	}
	
	@Override
	protected void setValueByTb1073(PreparedStatement preState, Object obj) throws SQLException {
		Tb1073LlinkphyrelationEntity entity = (Tb1073LlinkphyrelationEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1073Code());
		if (entity.getTb1065LogicallinkByF1065Code() != null) {
			setSring(preState, index++, entity.getTb1065LogicallinkByF1065Code().getF1065Code());
		} else {
			setSring(preState, index++, null);
		}
		if (entity.getTb1053PhysconnByF1053Code() != null) {
			setSring(preState, index++, entity.getTb1053PhysconnByF1053Code().getF1053Code());
		} else {
			setSring(preState, index++, null);
		}
	}
	
	@Override
	protected String getTb1090Sql() {
		return "INSERT INTO TB1090_LINEPROTFIBER(F1090_CODE,F1046_CODE,F1090_DESC,F1090_FIBERNO.F1090_PORTNO)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1090(PreparedStatement preState, Object obj) throws SQLException {
		Tb1090LineprotfiberEntity entity = (Tb1090LineprotfiberEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1090Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1090Desc());
		setSring(preState, index++, entity.getF1090FiberNo());
		setSring(preState, index++, entity.getF1090PortNo());
	}
	
	@Override
	protected String getTb1091Sql() {
		return "INSERT INTO TB1091_IOTERM(F1091_CODE,F1046_CODE,F1091_DESC,F1091_TERMNO,F1091_CIRCNO)" +
				" VALUES (?,?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1091(PreparedStatement preState, Object obj) throws SQLException {
		Tb1091IotermEntity entity = (Tb1091IotermEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1091Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1091Desc());
		setSring(preState, index++, entity.getF1091TermNo());
		setSring(preState, index++, entity.getF1091CircNo());
	}
	
	@Override
	protected String getTb1092Sql() {
		return "INSERT INTO TB1092_POWERKK(F1092_CODE,F1046_CODE,F1092_DESC,F1092_KKNO) VALUES (?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1092(PreparedStatement preState, Object obj) throws SQLException {
		Tb1092PowerkkEntity entity = (Tb1092PowerkkEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1092Code());
		if (entity.getTb1046IedByF1046Code() != null) {
			setSring(preState, index++, entity.getTb1046IedByF1046Code().getF1046Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1092Desc());
		setSring(preState, index++, entity.getF1092KkNo());
	}
	
	@Override
	protected String getTb1093Sql() {
		return "INSERT INTO TB1093_VOLTAGEKK(F1093_CODE,F1067_CODE,F1093_DESC,F1093_KKNO) VALUES (?,?,?,?)";
	}
	
	@Override
	protected void setValueByTb1093(PreparedStatement preState, Object obj) throws SQLException {
		Tb1093VoltagekkEntity entity = (Tb1093VoltagekkEntity) obj;
		int index = 1;
		setSring(preState, index++, entity.getF1093Code());
		if (entity.getTb1067CtvtsecondaryByF1067Code() != null) {
			setSring(preState, index++, entity.getTb1067CtvtsecondaryByF1067Code().getF1067Code());
		} else {
			setSring(preState, index++, null);
		}
		setSring(preState, index++, entity.getF1093Desc());
		setSring(preState, index++, entity.getF1093KkNo());
	}
}
