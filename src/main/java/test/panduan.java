package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisAction;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;

public class panduan {
    private static Connection conn;
    private static RedisAction redisAction;

    static{

        String driver1="com.mysql.cj.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/spider_dim";
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
        redisAction=new RedisAction("10.44.51.90",6379);
    }

    public static void main(String args[]) throws InterruptedException, SQLException, IOException {
        chuli();
    }

    public static void chuli() throws SQLException, IOException, InterruptedException {
        String sql="select id,sname from no_financing_comp_name";
        PreparedStatement ps= conn.prepareStatement(sql);

        String sql2="update no_financing_comp_name set only_id=?,reg_or=? where id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));
            String cname=rs.getString(rs.findColumn("sname"));
            String acname= FenciUtils.chuli(cname.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","")).replace(" ","");

            String md5 = MD5Util.getMD5String(acname).substring(8, 24);
            String onid= UnsignedLong.valueOf(md5, 16).toString();

            ps2.setString(1,onid);
            if(redisAction.get("zhuce",onid)){
               ps2.setString(2,"0");
            }else{
                ps2.setString(2,"1");
            }
            ps2.setString(3,id);
            ps2.addBatch();
            a++;
            System.out.println(a+"***********************************************8");
            if(a%1000==0){
                ps2.executeBatch();
            }
        }
        ps2.executeBatch();
    }
}
