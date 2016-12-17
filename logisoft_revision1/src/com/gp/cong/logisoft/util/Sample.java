package com.gp.cong.logisoft.util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Sample {

    public static void main(String args[])throws Exception{
        Connection c = null;
        Statement stmnt = null;
        String filename = "sample.xls";
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            c = (Connection) DriverManager.getConnection("jdbcdbcriver={Microsoft Excel Driver (*.xls)};DBQ=" + filename);
            stmnt = (Statement) c.createStatement();
            String query = "Select * from [Sheet1$]";
            ResultSet rs = stmnt.executeQuery(query);

            while (rs.next()) {
            }
    }
}