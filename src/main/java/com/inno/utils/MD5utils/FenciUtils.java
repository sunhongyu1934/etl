package com.inno.utils.MD5utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.inno.utils.Dup;
import com.inno.utils.mybatis_factory.MybatisUtils;

import java.io.StringReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FenciUtils {
    private static Segment seg= HanLP.newSegment().enablePlaceRecognize(true);

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

        List<String> list=new ArrayList<>();
        String sql="select city from china_province_city";
        try {
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                list.add(rs.getString(rs.findColumn("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(String s:list){
            CustomDictionary.insert(s, "ns 1024");
        }
    }

    public static String chuli(String cnames){
        String cname=cnames.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim();
        if(Dup.nullor(cname)) {
            List<Term> list = seg.seg(cname);
            for (Term t : list) {
                if (t.toString().split("/")[1].equals("ns")) {
                    cname = t.word + cname.replace(t.word, "");
                }
            }
            if (cname.contains("中国")) {
                cname = "中国" + cname.replace("中国", "");
            }
            String cc = cname;
            return cc;
        }else{
            return null;
        }

    }

    public static String shortn(String came){
        String cname=came.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","").replace(" ","").replaceAll("\\s","").replace(" ","").trim();
        List<Term> list=seg.seg(cname);
        for(Term t:list){
            if(t.toString().split("/")[1].equals("ns")){
                cname=cname.replace(t.word,"");
            }
        }
        String cc=cname.replace("公司","").replace("有限","");
        return cc;
    }

}
