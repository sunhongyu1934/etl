package com.inno.controller.CleanController;

import com.inno.service.LogoService;
import com.inno.service.ServiceImpl.LogoServiceImpl;
import com.inno.service.ServiceImpl.WebServiceImpl;
import com.inno.service.WebService;
import com.inno.utils.Dup;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Logo {
    private static java.sql.Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/dimension_sum";
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

    public static void main(String args[]){
        ExecutorService pool=Executors.newCachedThreadPool();
        Logo w=new Logo();
        Logo.Di d=w.new Di();
        String zi=args[0];
        String idb=args[1];
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(d,zi,idb);
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
                        chuli(d);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void data(Logo.Di d,String zi,String idb) throws SQLException, InterruptedException {
        String sql0="select "+zi+" from "+idb;
        PreparedStatement ps0=conn.prepareStatement(sql0);
        ResultSet rs0=ps0.executeQuery();
        List<String> list=new ArrayList<>();
        while (rs0.next()){
            list.add(rs0.getString(rs0.findColumn("comp_id")));
        }
        for(int x=0;x<=9;x++) {
            List<String> lid=new ArrayList<>();
            for(String s:list){
                if(Integer.parseInt(s.substring(s.length()-1))==x){
                    lid.add(s);
                }
            }
            String sql = "select * from comp_logo_sum"+x+"  where only_id in ";
            StringBuffer str=new StringBuffer();
            for(String s:lid){
                str.append("\'"+s+"\'"+",");
            }
            sql=sql+"("+str.toString()+")";
            sql=sql.replace(",)",")");

            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String quan=rs.getString(rs.findColumn("comp_full_name"));
                String url=rs.getString(rs.findColumn("logo_url"));
                String onid=rs.getString(rs.findColumn("only_id"));
                String source=rs.getString(rs.findColumn("source_y"));

                d.fang(new String[]{quan,url,onid,source});
            }
        }
    }

    public static void chuli(Logo.Di d) throws SQLException, InterruptedException, IOException {
        String sq="delete from clean_data.comp_logo where source_y=? and only_id=?";
        PreparedStatement pss=conn.prepareStatement(sq);

        String sql="insert into clean_data.comp_logo(com_name,logo_url,only_id,source_y,f_en) values(?,?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        int a=0;
        while (true){
            String[] va=d.qu();
            String[] we=web_url(va[1],va[3]);
            if(we!=null){
                pss.setString(1,va[3]);
                pss.setString(2,va[2]);
                pss.executeUpdate();

                ps.setString(1,va[0]);
                ps.setString(2,we[0]);
                ps.setString(3,va[2]);
                ps.setString(4,va[3]);
                ps.setString(5,we[1]);
                ps.executeUpdate();
            }
            a++;
            System.out.println(d.po.size()+"*****************************************************");
        }
    }




    public static String[] web_url(String key,String source) throws IOException {
        if(Dup.nullor(key)) {
            Connection.Response doc =null;
            while (true) {
                try {
                    doc = Jsoup.connect(key)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                            .ignoreContentType(true)
                            .ignoreHttpErrors(true)
                            .timeout(3000)
                            .method(Connection.Method.GET)
                            .execute();
                    if (doc != null && doc.body().length() > 44) {
                        break;
                    }else{
                        doc=null;
                        break;
                    }
                }catch (Exception e){
                    doc=null;
                    break;
                }
            }

            if(doc!=null){
                if(doc.statusCode()==200||doc.statusCode()==302){
                    return new String[]{doc.url().toString(), String.valueOf(100+Integer.parseInt(yuanfen(source)))};
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    public static String yuanfen(String source){
        if(source.equals("cyb")){
            return "100";
        }else if(source.equals("it")){
            return "80";
        }else if(source.equals("itleida")){
            return "60";
        }else if(source.equals("qcc")){
            return "40";
        }else if(source.equals("tyc")){
            return "30";
        }else if(source.equals("xixiu")){
            return "20";
        }else{
            return "0";
        }
    }
    class Di{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>(10000);
        public void fang(String[] list ) throws InterruptedException {
            po.put(list);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
