package com.synet.tool.rsc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;
import com.shrcn.found.file.util.FileManager;
import com.shrcn.found.file.util.FileManipulate;
import com.shrcn.found.ui.view.ConsoleManager;
import com.synet.tool.rsc.DBConstants;
import com.synet.tool.rsc.jdbc.ConnParam;

public class SqlHelper {
	
	private static String driver = "oracle.jdbc.driver.OracleDriver"; //驱动
   
	private static String getUrl(ConnParam connParam) {
		return "jdbc:oracle:thin:@" + connParam.getIp() + ":" + connParam.getPort() + ":" + connParam.getDbName();
	}
	
	public static void initOracle(ConnParam param) {
		String fname = DBConstants.sqlPath.substring(DBConstants.sqlPath.lastIndexOf('/') + 1);
		String fpath = Constants.cfgDir + "/" + fname;
		if (!new File(fpath).exists()) {
			InputStream is = SqlHelper.class.getClassLoader().getResourceAsStream(DBConstants.sqlPath);
			FileManager.saveInputStream(is, fpath);
			try {
				is.close();
			} catch (IOException e) {
				SCTLogger.error("", e);
			}
		}
		
		try {
			 // 建立连接
			Connection conn = getConn(param);
			// 创建ScriptRunner，用于执行SQL脚本
			ScriptRunner runner = new ScriptRunner(conn);
			FileManipulate.initDir(Constants.logDir);
			FileOutputStream fos = new FileOutputStream(Constants.logDir + "/sql.log");
			OutputStreamWriter logWriter = new OutputStreamWriter(fos);
			PrintWriter pw = new PrintWriter(logWriter);
			runner.setErrorLogWriter(pw);
			runner.setLogWriter(pw);
			// 执行SQL脚本
			FileInputStream fis = new FileInputStream(fpath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, Constants.CHARSET_UTF8));
			runner.runScript(reader);	
			// 关闭连接
			conn.close();
			pw.close();
			fos.close();
			reader.close();
			fis.close();
		} catch(SQLException | IOException e) {
			SCTLogger.error("数据库初始化异常：", e);
			ConsoleManager.getInstance().append("数据库初始化异常：" + e.getMessage());
		}
	}
	
//	public static void main(String[] args){
//        String path = "文件地址字符串";
//        String sql = getText(path);
//        List<String> sqlarr = getSql(sql);
//        for(int i=0; i<10; i++){
//            System.out.println(i+":"+sqlarr.get(i));
//        }
//        try{
//        SqlHelper.execute(getConn(),sqlarr);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }

	public static Connection getConn(ConnParam param) {
        String url = getUrl(param);
        String username = param.getUser();
        String password = param.getPassword();
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
        	SCTLogger.error("数据库连接异常：", e);
			ConsoleManager.getInstance().append("数据库连接异常：" + e.getMessage());
        }
        return conn;
    }
	
    public static void execute(Connection conn, List<String> sqlFile) throws Exception {  
        Statement stmt = null;  
        stmt = conn.createStatement();  
        for (String sql : sqlFile) {  
            sql = sql.trim();
            if(sql!=null&&!sql.equals(""))
            stmt.addBatch(sql);  
        }  
        int[] rows = stmt.executeBatch();  
        System.out.println("Row count:" + Arrays.toString(rows));  
        conn.close();
    }  



    /*
     * getText方法吧path路径里面的文件按行读数来放入一个大的String里面去
     * 并在换行的时候加入\r\n
     */
    public static String getText(String path){
        File file = new File(path);
        if(!file.exists()||file.isDirectory()){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try{
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            temp = br.readLine();
            while(temp!=null){
                if(temp.length()>=2){
                    String str1 = temp.substring(0, 1);
                    String str2 = temp.substring(0, 2);
                    if(str1.equals("#")||str2.equals("--")||str2.equals("/*")||str2.equals("//")){
                        temp = br.readLine();
                        continue;
                    }
                    sb.append(temp+"\r\n");
                }

                temp = br.readLine();
            }
            br.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
    /*
     * getSqlArray方法
     * 从文件的sql字符串中分析出能够独立执行的sql语句并返回
     */
    public static List<String> getSql(String sql){
        String s = sql;
        s = s.replaceAll("\r\n", "\r");
        s = s.replaceAll("\r", "\n");
        List<String> ret = new ArrayList<String>();
        String[] sqlarry = s.split(";");  //用;把所有的语句都分开成一个个单独的句子
        sqlarry = filter(sqlarry);
        ret = Arrays.asList(sqlarry);
        return ret;
    }

    public static String[] filter(String[] ss){
        List<String> strs = new ArrayList<String>();
        for(String s : ss){
            if(s!=null&&!s.equals("")){
                strs.add(s);
            }
        }
        String[] result = new String[strs.size()];
        for(int i=0; i<strs.size(); i++){
            result[i] = strs.get(i).toString();
        }
        return result;
    }


}