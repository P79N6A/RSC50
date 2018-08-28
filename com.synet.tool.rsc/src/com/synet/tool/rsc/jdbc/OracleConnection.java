package com.synet.tool.rsc.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import oracle.jdbc.driver.OracleDriver;

/**
 *线上Oracle数据库连接管理
 *（数据存库，功能单一，以JDBC硬编码的方式的方式实现）
 */
public class OracleConnection {
	
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:ORCL";
	private static final String USER = "RSC";
	private static final String PASSWORD = "rsc123456";
   
	private static Properties getProperties() {
		Properties pro = new Properties();
        pro.put("user", USER);
        pro.put("password", PASSWORD);
        return pro;
	}
	
	private static Connection createConnection() {
         try {
        	Driver driver = new OracleDriver();
			DriverManager.deregisterDriver(driver);
	        Connection connect = driver.connect(URL, getProperties());
	        return connect;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public static Connection getConnection() {
		return createConnection();
	}
	
	/**
	 * 关闭数据库连接
	 * @param connection
	 */
	public static void close (Connection connection) {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		 Connection connect = null;
	     ResultSet resultSet = null;
	     
	     connect = getConnection();
	     PreparedStatement preState = connect.prepareStatement("select  * from STUDENT where ID=?");
	     preState.setInt(1, 2);
	     boolean b = preState.execute();
	     System.out.println(b);
	     resultSet = preState.getResultSet();
	     while(resultSet.next()) {
	    	 int id = resultSet.getInt("id");
             String name = resultSet.getString("name");
             String city = resultSet.getString("age");
             System.out.println(id+"   "+name+"   "+city);  //打印输出结果集
	     }
	}
}
