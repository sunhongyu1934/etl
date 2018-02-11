package com.inno.controller.CleanController;

import com.inno.service.GudongService;
import com.inno.service.ServiceImpl.GudongServiceImpl;
import com.inno.service.ServiceImpl.WebServiceImpl;
import com.inno.service.WebService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

public class Web {
    private static java.sql.Connection conn;
    private static RedisClu rd=new RedisClu();
    private static int ji=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/dimension_sum";
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
        Web w=new Web();
        Di d=w.new Di();
        Ca c=w.new Ca();
        //String zi=args[0];
        //String idb=args[1];
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data2(d);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    chuli2(d,c);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int j=1;j<=30;j++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        detail2(c);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void data(Di d,String zi,String idb) throws SQLException, InterruptedException {
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

            if(lid!=null&&lid.size()>0) {
                String sql = "select * from comp_web_sum" + x + " where only_id in ";
                StringBuffer str = new StringBuffer();
                for (String s : lid) {
                    str.append("\'" + s + "\'" + ",");
                }
                sql = sql + "(" + str.toString() + ")";
                sql = sql.replace(",)", ")");

                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                Map<String, List<String[]>> map = new HashMap<>();
                while (rs.next()) {
                    String quan = rs.getString(rs.findColumn("comp_full_name"));
                    String url = rs.getString(rs.findColumn("web_url"));
                    String onid = rs.getString(rs.findColumn("only_id"));
                    String source = rs.getString(rs.findColumn("source_y"));

                    if (Dup.nullor(url) && url.length() <= 50 && !url.contains("https://store.taobao.com/shop/noshop.htm")) {
                        if (map.get(onid) != null && map.get(onid).size() > 0) {
                            map.get(onid).add(new String[]{quan, url, onid, source});
                        } else {
                            List<String[]> list1 = new ArrayList<>();
                            list1.add(new String[]{quan, url, onid, source});
                            map.put(onid, list1);
                        }
                    }
                }
                d.fang(map);
            }
        }
    }

    public static void data2(Di d) throws SQLException, InterruptedException {
        List<String> list=new ArrayList<>();

        Set<String> set=rd.getAllset("comp_in");
        for(String s:set){
            list.add(s);
        }

        for(int x=0;x<=9;x++) {
            List<String> lid=new ArrayList<>();
            for(String s:list){
                if(Integer.parseInt(s.substring(s.length()-1))==x){
                    lid.add(s);
                }
            }

            if(lid!=null&&lid.size()>0) {
                String sql = "select * from comp_web_sum" + x + " where only_id in ";
                StringBuffer str = new StringBuffer();
                for (String s : lid) {
                    str.append("\'" + s + "\'" + ",");
                }
                sql = sql + "(" + str.toString() + ")";
                sql = sql.replace(",)", ")");

                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                Map<String, List<String[]>> map = new HashMap<>();
                while (rs.next()) {
                    String quan = rs.getString(rs.findColumn("comp_full_name"));
                    String url = rs.getString(rs.findColumn("web_url"));
                    String onid = rs.getString(rs.findColumn("only_id"));
                    String source = rs.getString(rs.findColumn("source_y"));

                    if (Dup.nullor(url) &&url.length() <= 50 && !url.contains("https://store.taobao.com/shop/noshop.htm")) {
                        if (map.get(onid) != null && map.get(onid).size() > 0) {
                            map.get(onid).add(new String[]{quan, url, onid, source});
                        } else {
                            List<String[]> list1 = new ArrayList<>();
                            list1.add(new String[]{quan, url, onid, source});
                            map.put(onid, list1);
                        }
                    }
                }
                d.fang(map);
            }
        }
    }

    public static void chuli(Di d) throws SQLException, InterruptedException, IOException {
        String sq="delete from clean_data.comp_web_only where only_id=?";
        PreparedStatement pss=conn.prepareStatement(sq);

        String sql="insert into clean_data.comp_web_only(web_url,only_id,source_y,f_en) values(?,?,?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
        int a=0;
        while (true){
            try {
                Map<String,List<String[]>> value = d.qu();
                for(Map.Entry<String,List<String[]>> entry:value.entrySet()){
                    try {
                        List<String[]> vas = entry.getValue();
                        TreeMap<String, String[]> mm = new TreeMap<>();
                        for (String[] va : vas) {
                            String[] we = web_url(va[1], va[0], va[3]);
                            if (we != null) {
                                mm.put(we[1], we);
                            }
                        }
                        if (mm != null && mm.size() > 0) {
                            String[] we = mm.get(mm.lastKey());
                            if (we != null) {
                                pss.setString(1, entry.getKey());
                                pss.executeUpdate();

                                ps.setString(1, we[0]);
                                ps.setString(2, entry.getKey());
                                ps.setString(3, we[2]);
                                ps.setString(4, we[1]);
                                ps.executeUpdate();
                            }
                        }
                        a++;
                        System.out.println(d.po.size() + "*****************************************************");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void chuli2(Di d,Ca c) throws SQLException, InterruptedException, IOException {
        int a=0;
        while (true){
            Map<String,List<String[]>> value = d.qu();
            for(Map.Entry<String,List<String[]>> entry:value.entrySet()){
                try {
                    List<String[]> vas = entry.getValue();
                    c.fang(vas);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void detail2(Ca c) throws IOException, SQLException, InterruptedException {
        while (true){
            try {
                List<String[]> vas = c.qu();
                if (vas == null || vas.size() == 0) {
                    break;
                }
                TreeMap<String, String[]> mm = new TreeMap<>();
                String onid = null;
                for (String[] va : vas) {
                    String[] we = web_url(va[1], va[0], va[3]);
                    onid = va[2];
                    if (we != null) {
                        mm.put(we[1], we);
                    }
                }
                if (mm != null && mm.size() > 0) {
                    String[] we = mm.get(mm.lastKey());
                    if (we != null) {
                        PreparedStatement ps0 = ruku(onid.substring(onid.length() - 1));
                        ps0.setString(1, we[0]);
                        ps0.setString(2, we[2]);
                        ps0.setString(3, onid);
                        ps0.executeUpdate();
                    }
                }
                ji++;
                System.out.println(ji + "*****************************************************");
            }catch (Exception e){
                System.out.println("error");
            }
        }
    }

    public static PreparedStatement ruku(String t) throws SQLException {
        String sql="update dw_dim_online.company_base_info_copy"+t+" set comp_web_url=?,web_source=? where comp_id=? and (web_source not like '%人工%' or web_source='' or web_source is null)";
        PreparedStatement ps0=conn.prepareStatement(sql);
        return ps0;
    }


    public static String[] web_url(String key,String va,String source) throws IOException {
        if(Dup.nullor(key)) {
            if(!key.contains("http://")&&!key.contains("https://")) {
                key="http://"+key;
            }

            String shor= FenciUtils.shortn(va);
            Connection.Response doc =null;
            int m=0;
            System.out.println(key);
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
                    }
                }catch (Exception e){

                }
                m++;
                if(m>=5){
                    doc=null;
                    break;
                }
            }

            if(doc!=null){
                if(doc.body().contains(va)&&(doc.statusCode()==200||doc.statusCode()==302)){
                    return new String[]{doc.url().toString(), String.valueOf(1000+Integer.parseInt(yuanfen(source))),source};
                }else if(doc.body().contains(shor)&&(doc.statusCode()==200||doc.statusCode()==302)){
                    return new String[]{doc.url().toString(),String.valueOf(800+Integer.parseInt(yuanfen(source))),source};
                }else if(!doc.body().contains(shor)&&!doc.body().contains(va)&&(doc.statusCode()==200||doc.statusCode()==302)){
                    return new String[]{doc.url().toString(),String.valueOf(60+Integer.parseInt(yuanfen(source))),source};
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
        if(source.equals("tyc")){
            return "100";
        }else if(source.equals("itleida")){
            return "80";
        }else if(source.equals("lg")){
            return "60";
        }else if(source.equals("36ke")){
            return "40";
        }else if(source.equals("zy")){
            return "30";
        }else if(source.equals("cyb")){
            return "20";
        }else if(source.equals("xixiu")){
            return "10";
        }else{
            return "0";
        }
    }


    class Di{
        BlockingQueue<Map<String,List<String[]>>> po=new LinkedBlockingQueue<>();
        public void fang(Map<String,List<String[]>> list ) throws InterruptedException {
            po.put(list);
        }
        public Map<String,List<String[]>> qu() throws InterruptedException {
            return po.poll(10,TimeUnit.SECONDS);
        }
    }

    class Ca{
        BlockingQueue<List<String[]>> po=new LinkedBlockingQueue<>();
        public void fang(List<String[]> key) throws InterruptedException {
            po.put(key);
        }
        public List<String[]> qu() throws InterruptedException {
            return po.poll(10,TimeUnit.SECONDS);
        }
    }



}
