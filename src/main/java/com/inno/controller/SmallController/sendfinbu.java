package com.inno.controller.SmallController;

import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class sendfinbu {
    private static RedisClu rd;
    private static Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider";
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
        rd=new RedisClu();
    }
    public static void main(String args[]) throws SQLException {
        String ta=args[0];
        data(ta);
    }

    public static void data(String ta) throws SQLException {
        String sql="select id,comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and comp_full_name not in (select quan_cheng from tyc.tyc_jichu_quan) and comp_full_name not in (select quan_cheng from tianyancha.tyc_jichu_quan)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=simpleDateFormat.format(date);

        String sql2="update "+ta+" set mark_time='"+time+"' where id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        int a=0;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String cname=rs.getString(rs.findColumn("comp_full_name"));

            rd.set("fin",cname);

            ps2.setInt(1, Integer.parseInt(id));
            ps2.executeUpdate();
            a++;
            System.out.println(a+"******************************************");
        }

    }
}
