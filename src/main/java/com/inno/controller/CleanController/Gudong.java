package com.inno.controller.CleanController;

import com.inno.service.GudongService;
import com.inno.service.JigouService;
import com.inno.service.ServiceImpl.GudongServiceImpl;
import com.inno.service.ServiceImpl.JigouServiceImpl;
import com.inno.utils.redisUtils.RedisAction;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public class Gudong {
    private static GudongService j=new GudongServiceImpl();
    private static java.sql.Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/dimension_sum";
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
    public static void main(String args[]) throws IOException, InterruptedException, ParseException, SQLException {
        /*int p=0;
        while (true) {
            try {
                List<Map<String, Object>> list = j.find(String.valueOf(p));
                if (list == null || list.size() == 0) {
                    break;
                }
                j.clean(list);
                p = p + 50000;
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

        Set<String> set=new HashSet<>();
        String sql="select c_name from linshi_com";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String oid=rs.getString(rs.findColumn("c_name"));
            set.add(oid);
        }
        for(int a=0;a<=9;a++) {
            try {
                List<String> list = new ArrayList<>();
                for (String s : set) {
                    String has = s.substring(s.length() - 1);
                    if (Integer.parseInt(has) == a) {
                        list.add(s);
                    }
                }
                List<Map<String, Object>> ll = j.findshang(list, String.valueOf(a));
                j.clean(ll);
            }catch (Exception e){

            }
        }
    }
}
