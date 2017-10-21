package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisAction;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class jigou_comp {
    private static Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/clean_data";
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
    public static void main(String args[]) throws InterruptedException, SQLException, IOException {
        shengcheng();
    }

    public static void shengcheng() throws SQLException, IOException, InterruptedException {
        String sql1="update jigou_comp set c_id=?,s_id=? where id=?";
        PreparedStatement pss=conn.prepareStatement(sql1);

        String sql="select id,c_name,s_name from jigou_comp";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String cname=rs.getString(rs.findColumn("c_name"));
            String sname=rs.getString(rs.findColumn("s_name"));


            String cc= FenciUtils.chuli(cname.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","").replace(" ","").replaceAll("\\s","").replace(" ","").trim());
            String ss= FenciUtils.chuli(sname.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","").replace(" ","").replaceAll("\\s","").replace(" ","").trim());


            String cid= UnsignedLong.valueOf(getMD5String(cc).substring(8, 24), 16).toString();
            String sid=UnsignedLong.valueOf(getMD5String(ss).substring(8, 24), 16).toString();

            pss.setString(1,cid);
            pss.setString(2,sid);
            pss.setString(3,id);
            pss.addBatch();
            a++;
            System.out.println(a+"************************************************");
        }
        pss.executeBatch();
    }
}
