package test;

import com.google.common.primitives.UnsignedLong;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.redisUtils.RedisAction;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.util.List;
import java.util.concurrent.*;

public class chuli {
    private static Connection conn;
    private static Connection conn2;
    //private static RedisAction rd;
    private static int a=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/tyc";
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





        String url2="jdbc:mysql://10.252.0.52:3306/tianyancha";
        String username2="etl_tmp";
        String password2="UsF4z5HE771KQpra";
        try {
            Class.forName(driver1).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        java.sql.Connection con2=null;
        try {
            con2 = DriverManager.getConnection(url2, username2, password2);
        }catch (Exception e){
            while(true){
                try {
                    con2 = DriverManager.getConnection(url2, username2, password2);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(con2!=null){
                    break;
                }
            }
        }
        conn=con;
        conn2=con2;
        //rd=new RedisAction("a027.hb2.innotree.org",6379);
    }

    public static void main(String args[]) throws SQLException, InterruptedException {
        chuli c=new chuli();
        Ca cc=c.new Ca();
        ExecutorService pool=Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    du(cc);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int x=1;x<=20;x++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        da(cc);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }



    }

    public static void du(Ca c) throws SQLException, InterruptedException {
        for(int a=1;a<=50;a++) {
            String sql = "select a.t_id as t_id from tyc_jichu_quan a RIGHT JOIN linshi_com b on a.quan_cheng=b.comp_full_name where a.t_id!='' and a.t_id is not null";
            PreparedStatement ps=conn2.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                try {
                    String tid = rs.getString(rs.findColumn("t_id"));
                    c.fang(tid);
                }catch (Exception e){
                    System.out.println("fang error");
                }
            }
            break;
        }
    }

    public static void da(Ca c) throws SQLException, InterruptedException {
        String sql = "update tyc_main set comp_full_name=? where t_id=?";
        PreparedStatement ps = conn2.prepareStatement(sql);
        RedisAction rd=new RedisAction("a027.hb2.innotree.org",6379);
        while (true) {
            String tid=c.qu();
            String cname=rd.getName(tid);
            ps.setString(1,cname);
            ps.setString(2,tid);
            ps.executeUpdate();
            a++;
            System.out.println(a+"*********************************************");
        }
    }


    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>(500000);
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.take();
        }
    }
}
