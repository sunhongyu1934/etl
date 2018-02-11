package com.inno.controller.SmallController;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class overcomp {
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
        data();
    }

    public static void data() throws SQLException {
        String sql="insert into dw_dim_online.comp_id_online(comp_id,cover_flag) values(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="select comp_full_name from innotree_data_financing.comp_name_bulu_no";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        ResultSet rs2=ps2.executeQuery();
        List<String> list=new ArrayList<>();
        while (rs2.next()){
            String cname=rs2.getString(rs2.findColumn("comp_full_name"));
            String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
            String comp_id=UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

            list.add(comp_id);
        }


        Set<String> set=rd.getAllset("comp_in");
        for(String s:set){
            if(!list.contains(s)) {
                if(rd.get("comp_cover_in",s)) {
                    ps.setString(1, s);
                    ps.setInt(2,1);
                    ps.executeUpdate();
                }else{
                    ps.setString(1, s);
                    ps.setInt(2,0);
                    ps.executeUpdate();
                }
            }
        }

        rd.del("comp_in");
        rd.del("comp_cover_in");
    }
}
