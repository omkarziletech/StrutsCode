package com.gp.cong.logisoft.edi.dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.gp.cong.common.CommonConstants;


public class DBUtil {
	private Connection logisoftConnection;
	private Properties prop = new Properties();
	public Connection getLogisoftConnection()throws Exception {
		prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
		Class.forName(prop.getProperty("driver"));
		String url = prop.getProperty("url");
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		logisoftConnection = DriverManager.getConnection(url,username,password);
		return logisoftConnection;
	}
	
	public void  clear(Connection conn)throws Exception {
			if(conn != null){
			conn.close();
			}
		}
}

