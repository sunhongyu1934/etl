package com.inno.controller.SmallController;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class teambu {
    private static java.sql.Connection conn;
    private static RedisClu rd=new RedisClu();
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

    }

    public static void main(String args[]) throws SQLException, InterruptedException {
        teambu t=new teambu();
        Ca c=t.new Ca();
        ExecutorService pool= Executors.newCachedThreadPool();
        for(int a=1;a<=10;a++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        data(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    qu(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void qu(Ca c) throws InterruptedException {
        int a=0;
        while (true){
            try {
                String cname = rd.get("comp_in_name");
                c.fang(cname);
            }catch (Exception e){
                System.out.println("kong");
                a++;
            }
            if(a>=20){
                break;
            }
        }
    }

    public static void data(Ca c) throws InterruptedException, SQLException {
        String sql="select distinct t_id,comp_full_name,zhi_wu,p_name from tyc.tyc_main where t_id=? and p_name!='' and p_name is not null";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="select distinct t_id,comp_full_name,zhi_wu,p_name from tianyancha.tyc_main where t_id=? and p_name!='' and p_name is not null";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        String sql3="insert into clean_data.company_manager(tail_id,comp_id,comp_full_name,manager_name,manager_position,data_source,team_source) values(?,?,?,?,?,?,?)";
        PreparedStatement ps3=conn.prepareStatement(sql3);

        String sql4="delete from clean_data.company_manager where tail_id=?";
        PreparedStatement ps4=conn.prepareStatement(sql4);

        int ji=0;

        while (true){
            String cname=c.qu();
            if(!Dup.nullor(cname)){
                break;
            }
            String t_id=gettid(cname);
            ps.setString(1,t_id);
            ResultSet rs=ps.executeQuery();

            boolean ba=true;

            while (rs.next()){
                ba=false;
                String zhiwu=rs.getString(rs.findColumn("zhi_wu"));
                String rming=rs.getString(rs.findColumn("p_name"));

                List<String> list=new ArrayList<>();
                list.add(rming);

                String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                String onid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                String taid = UnsignedLong.valueOf(getMD5String(acname + list.toString()).substring(8, 24), 16).toString();

                ps4.setString(1,taid);
                ps4.executeUpdate();


                ps3.setString(1, taid);
                ps3.setString(2, onid);
                ps3.setString(3, cname);
                ps3.setString(4, rming);
                ps3.setString(5, zhiwu);
                ps3.setString(6, "tyc");
                ps3.setInt(7, 1);
                ps3.executeUpdate();


            }

            if(ba){
                ps2.setString(1,t_id);
                ResultSet rs2=ps2.executeQuery();

                while (rs2.next()){
                    String zhiwu=rs2.getString(rs2.findColumn("zhi_wu"));
                    String rming=rs2.getString(rs2.findColumn("p_name"));

                    List<String> list=new ArrayList<>();
                    list.add(rming);

                    String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                    String onid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                    String taid = UnsignedLong.valueOf(getMD5String(acname + list.toString()).substring(8, 24), 16).toString();

                    ps4.setString(1,taid);
                    ps4.executeUpdate();

                    ps3.setString(1, taid);
                    ps3.setString(2, onid);
                    ps3.setString(3, cname);
                    ps3.setString(4, rming);
                    ps3.setString(5, zhiwu);
                    ps3.setString(6, "tyc");
                    ps3.setInt(7, 1);
                    ps3.executeUpdate();

                }
            }

            ji++;
            System.out.println(ji+"******************************************************");
        }
    }

    public static String gettid(String quan) throws SQLException {
        String sql="select distinct t_id from tyc.tyc_jichu_quan where quan_cheng=?";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="select distinct t_id from tianyancha.tyc_jichu_quan where quan_cheng=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);

        String tid=null;

        ps.setString(1,quan);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            tid=rs.getString(rs.findColumn("t_id"));
        }
        if(!Dup.nullor(tid)){
            ps2.setString(1,quan);
            ResultSet rs2=ps2.executeQuery();
            while (rs2.next()){
                tid=rs2.getString(rs2.findColumn("t_id"));
            }
        }
        return tid;
    }

    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }
}
