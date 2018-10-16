package com.synet.tool.rsc.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shrcn.found.ui.util.DialogHelper;
import com.synet.tool.rsc.util.SqlHelper;

/**
 *线上Oracle数据库连接管理
 *（数据存库，功能单一，以JDBC硬编码的方式的方式实现）
 */
public class ExportConnManager {
	
//	private String URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
//	private String USER = "RSC";
//	private String PASSWORD = "rsc123456";
//	
	private ConnParam connParam;
   
	private Connection createConnection() {
		if (connParam == null || !connParam.checkParam()) {
			DialogHelper.showAsynError("导出数据的数据库连接参数错误！");
		}
		return SqlHelper.getConn(connParam);
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConnection() {
		return createConnection();
	}
	
	/**
	 * 关闭数据库连接
	 * @param connection
	 */
	public void close (Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ConnParam getConnParam() {
		return connParam;
	}

	public void setConnParam(ConnParam connParam) {
		this.connParam = connParam;
	}

	public static void main(String[] args) throws SQLException {
		 Connection connect = null;
	     ResultSet resultSet = null;
//	 	private String URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
//	 	private String USER = "RSC";
//	 	private String PASSWORD = "rsc123456";
	 //	
	     ConnParam connParam = new ConnParam();
	     connParam.setIp("localhost");
		 connParam.setPort("1521");
		 connParam.setDbName("ORCL");
		 connParam.setUser("RSC");
		 connParam.setPassword("rsc123456");
	     ExportConnManager exportConnManager = new ExportConnManager();
	     exportConnManager.setConnParam(connParam);
	     connect = exportConnManager.getConnection();
	     PreparedStatement preState = connect.prepareStatement("INSERT INTO TB1041_SUBSTATION(F1041_CODE,F1041_NAME,F1041_DESC,F1041_DQNAME,F1041_DQDESC,F1041_COMPANY,F1042_VOLTAGEH,F1042_VOLTAGEM,F1042_VOLTAGEL) VALUES (?,?,?,?,?,?,?,?,?)");
	     preState.setString(1, "bb");
	     preState.setString(2, "ddddd");
	     preState.setString(3, null);
	     preState.setString(4, null);
	     preState.setString(5, null);
	     preState.setString(6, null);
	     preState.setInt(7, 0);
	     preState.setInt(8, 0);
	     preState.setInt(9, 0);
	     boolean b = preState.execute();
	     System.out.println(b);
//	     resultSet = preState.getResultSet();
//	     while(resultSet.next()) {
//	    	 int id = resultSet.getInt("id");
//             String name = resultSet.getString("name");
//             String city = resultSet.getString("age");
//             System.out.println(id+"   "+name+"   "+city);  //打印输出结果集
//	     }
	}
}
