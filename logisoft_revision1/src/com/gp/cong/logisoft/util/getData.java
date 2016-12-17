package com.gp.cong.logisoft.util;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class getData {

    public static void main(String[] args)  throws Exception {

        Connection con = null;
        int count = 0;
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://cong3:3308/logisoft_seacorp", "root", "congruence08");
                String sql = null;
                sql = "select * from fcl_buy_cost_type_rates";
                //sql= "select * from (fcl_buy left join fcl_buy_cost on fcl_buy.Fcl_std_id = fcl_buy_cost.Fcl_std_id)" +
                //"left join fcl_buy_cost_type_rates on fcl_buy_cost.Fcl_std_id = fcl_buy_cost_type_rates.fcl_cost_id " +
                //	"where  fcl_buy.ssline_no=?";
                PreparedStatement prest = (PreparedStatement) con.prepareStatement(sql);
                prest.setString(1, "00064");
                ResultSet rs = prest.executeQuery();
                int i = 0;
                while (rs.next()) {
                    String mov_name = rs.getString(1);

                    i++;
                }

                prest.close();
                con.close();
    }
}
