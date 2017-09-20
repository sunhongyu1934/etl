package test;

import com.google.common.primitives.UnsignedLong;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.redisUtils.RedisAction;
import com.mysql.cj.core.util.StringUtils;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.List;
import java.util.concurrent.*;

public class chuli {
    private static Connection conn;
    private static RedisAction rd;
    static{
        String driver1="com.mysql.cj.jdbc.Driver";
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
        rd=new RedisAction("10.44.51.90",6379);
    }

    public static void main(String args[]) throws SQLException, InterruptedException {
        du();


    }

    public static void du() throws SQLException, InterruptedException {
        int a=5500000;
        int j=0;
        String sql2="insert into com_dictionaries(be_company_name,af_company_name,only_id,hash_id,register_or) values(?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        for(int x=1;x<=18;x++) {
            String sql = "select distinct quan_cheng from tyc.tyc_jichu_quan limit "+a+",500000";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                try {
                    String cname = rs.getString(rs.findColumn("quan_cheng"));
                    if(!StringUtils.isNullOrEmpty(cname)) {
                        String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "")).replace(" ", "");
                        String md5 = MD5Util.getMD5String(acname).substring(8, 24);
                        String onid = UnsignedLong.valueOf(md5, 16).toString();
                        String hasid = onid.substring(onid.length() - 1);

                        ps2.setString(1, cname);
                        ps2.setString(2, acname);
                        ps2.setString(3, onid);
                        ps2.setString(4, hasid);
                        ps2.setString(5, "0");
                        ps2.executeUpdate();
                        rd.set("zhuce",acname);
                        j++;
                        System.out.println(j+ "*******************************************************8");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            a=a+500000;
        }
    }
}
