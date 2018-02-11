package test;

import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.util.concurrent.*;

public class chuli {
    private static Connection conn;
    private static int a=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.36:3306/innotree_data_assessment";
        String username="spider";
        String password="spider";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con=null;
        try {
            con = DriverManager.getConnection(url1, username, password);
        }catch (Exception e){
            while(true){
                try {
                    con = DriverManager.getConnection(url1, username, password);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con!=null){
                    break;
                }
            }
        }
        conn=con;
    }

    public static void main(String args[]) throws SQLException, InterruptedException {

    }

    public static void data() throws SQLException {
        String sql="select * from abc";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        String sql2="insert into company_financing(comp_id,comp_full_name,comp_short_name,invest_date,invest_comp_id,invest_name,invest_short_name,invest_time,invest_stage,)";
        while (rs.next()){

        }
    }



}
