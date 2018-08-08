package com.shrcn.tool.found.das;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyUtil {

	/**
	 * 替换信息
	 * 
	 * @param desc
	 * @param oldStr
	 * @param newStr
	 * @return
	 */
	public static String replaceName(String desc, String oldStr, String newStr){
		return desc.replace(oldStr, newStr);
	}
	
	/**
	 * 按条件统计行数
	 * @param tbName
	 * @param colName
	 * @param colValue
	 * @param total
	 * @throws SQLException
	 */
	public static void countNum(String tbName, String colName, int colValue, int[] total) 
			throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:default:connection");
        PreparedStatement p = connection
                .prepareStatement("SELECT count(*) FROM " + tbName
                        + " WHERE " + colName + "= ?");
        p.setInt(1, colValue);

        ResultSet rs = p.executeQuery();
        if (rs.next()) {
            total[0] = rs.getInt(1);
        }

        rs.close();
        p.close();
        
        connection.close();
	}
}
