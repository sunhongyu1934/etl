package com.inno.controller.SmallController;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class sendcomp {
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
    public static void main(String args[]) throws SQLException, InterruptedException {
        sendcomp s=new sendcomp();
        Ca c=s.new Ca();
        String ta=args[0];
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    delete(ta,c);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=30;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        exec(c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();
        while (true) {
            if (pool.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(2000);
        }
        data(ta);
    }

    public static void exec(Ca c) throws InterruptedException, SQLException {
        while (true){
            String ps=c.qu();
            if(ps==null){
                break;
            }
            if(!ps.contains("tyc.tyc_jichu_quan")) {
                String sql = "delete from " + ps.split("###")[0] + " where t_id='" + ps.split("###")[1] + "'";
                PreparedStatement pp = conn.prepareStatement(sql);
                pp.executeUpdate();
            }else{
                String sqld = "delete from tyc.tyc_jichu_quan where quan_cheng='"+ps.split("###")[1]+"'";
                PreparedStatement psd = conn.prepareStatement(sqld);
                psd.executeUpdate();
            }
            System.out.println("delete success "+ps.split("###")[0]);
            System.out.println(c.po.size()+"***********************************");
        }
    }

    public static void delete(String ta,Ca c) throws SQLException, InterruptedException {
        String[] tables=new String[]{"tyc.tyc_main","tyc.tyc_gudongxin","tyc.tyc_out_investment","tyc.tyc_core_team","tyc.tyc_webcat"};
        List<String> list=new ArrayList<>();
        String sqlq="select DISTINCT t_id from tyc.tyc_jichu_quan where quan_cheng in (select comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and (cover_flag=1 or cover_flag=2))";
        PreparedStatement psq=conn.prepareStatement(sqlq);
        ResultSet rsq=psq.executeQuery();
        while (rsq.next()){
            list.add(rsq.getString(rsq.findColumn("t_id")));
        }

        if(list!=null&&list.size()>0) {
            for (String s : tables) {
                System.out.println("begin delete: "+s);
                for (String ss : list) {
                    c.fang(s+"###"+ss);
                }
            }
        }


        List<String> listl=new ArrayList<>();
        String sqll="select DISTINCT comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and (cover_flag=1 or cover_flag=2)";
        PreparedStatement psl=conn.prepareStatement(sqll);
        ResultSet rsl=psl.executeQuery();
        while (rsl.next()){
            listl.add(rsl.getString(rsl.findColumn("comp_full_name")));
        }

        if(listl!=null&&listl.size()>0) {
            System.out.println("begin delete tyc_jichu_quan");
            for (String sq : listl) {
                c.fang("tyc.tyc_jichu_quan###"+sq);
            }

            System.out.println("delete tyc_jichu_quan success");
        }
    }

    public static void data(String ta) throws SQLException {
        String sql0="select id,comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and cover_flag=1 and comp_full_name!='' and comp_full_name is not null";
        PreparedStatement ps0=conn.prepareStatement(sql0);
        ResultSet rs0=ps0.executeQuery();

        String sql9="select id,comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and cover_flag=2 and comp_full_name!='' and comp_full_name is not null";
        PreparedStatement ps9=conn.prepareStatement(sql9);
        ResultSet rs9=ps9.executeQuery();

        String sql="select id,comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and comp_full_name not in (select quan_cheng from tyc.tyc_jichu_quan) and comp_full_name not in (select quan_cheng from tianyancha.tyc_jichu_quan) and comp_full_name!='' and comp_full_name is not null";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        String sql3="select id,comp_full_name from "+ta+" where (mark_time='' or mark_time is null) and comp_full_name!='' and comp_full_name is not null";
        PreparedStatement ps3=conn.prepareStatement(sql3);
        ResultSet rs3=ps3.executeQuery();


        int a=0;

        while (rs3.next()){
            String cname=rs3.getString(rs3.findColumn("comp_full_name"));

            String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
            String compid=UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

            rd.set("comp_in",compid);
            rd.set("comp_in_name",cname);
            a++;
            System.out.println(a+"--------------------------------------------------------");
        }


        while (rs.next()){
            String cname=rs.getString(rs.findColumn("comp_full_name"));

            rd.set("comp_zl",cname);

            a++;
            System.out.println(a+"******************************************");
        }

        while (rs0.next()){
            String cname=rs0.getString(rs0.findColumn("comp_full_name"));
            String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
            String compid=UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

            rd.set("comp_zl",cname);
            rd.set("comp_cover_in",compid);

            a++;
            System.out.println(a+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        }

        while (rs9.next()){
            String cname=rs9.getString(rs9.findColumn("comp_full_name"));
            String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
            String compid=UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

            rd.set("comp_zl",cname);
            rd.set("comp_cover_in",compid);

            a++;
            System.out.println(a+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        }

    }

    class Ca{
        BlockingQueue<String> po=new LinkedBlockingQueue<>();
        public void fang(String p) throws InterruptedException {
            po.put(p);
        }
        public String qu() throws InterruptedException {
            return po.poll(30, TimeUnit.SECONDS);
        }
    }
}
