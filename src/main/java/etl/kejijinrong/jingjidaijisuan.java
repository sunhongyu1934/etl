package etl.kejijinrong;

import com.inno.utils.Dup;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jingjidaijisuan {
    private static Connection conn;
    private static List<String> list1=new ArrayList<>();
    private static List<String> list2=new ArrayList<>();
    private static List<String> list3=new ArrayList<>();
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.36:3306/innotree_data_project?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="base";
        String password="imkloKuLiqNMc6Cn";
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

        try {
            String sql0 = "select area_id from regional_innovation_dim_sys_area where economic_belt_name='京津冀'";
            PreparedStatement ps0 = conn.prepareStatement(sql0);
            ResultSet rs0 = ps0.executeQuery();
            while (rs0.next()) {
                String arid = rs0.getString(rs0.findColumn("area_id"));
                list1.add(arid);
            }

            String sql1 = "select area_id from regional_innovation_dim_sys_area where economic_belt_name='长三角'";
            PreparedStatement ps1 = conn.prepareStatement(sql1);
            ResultSet rs1 = ps1.executeQuery();
            while (rs1.next()) {
                String arid = rs1.getString(rs1.findColumn("area_id"));
                list2.add(arid);
            }

            String sql2 = "select area_id from regional_innovation_dim_sys_area where economic_belt_name='珠港澳'";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                String arid = rs2.getString(rs2.findColumn("area_id"));
                list3.add(arid);
            }


        }catch (Exception e){

        }
    }

    public static String danwei(String shu){
        String dan=shu.split("###")[1];
        String nei=shu.split("###")[0];
        if(dan.contains("亿")){
            nei= String.valueOf(Double.parseDouble(nei)*10000);
        }else if(dan.contains("万美元")){
            nei= String.valueOf(Double.parseDouble(nei)*6.5);
        }
        return nei;
    }

    public static String sum(String jingji,String clify) throws SQLException {
        List<String> list=new ArrayList<>();

        if(jingji.equals("京津冀")){
            list=list1;
        }else if(jingji.equals("长三角")){
            list=list2;
        }else if(jingji.equals("珠港澳")){
            list=list3;
        }

        String dw="";
        String sql0="select classify_unit from regional_innovation_dim_sys_classify where classify_name='"+clify+"'";
        PreparedStatement ps0=conn.prepareStatement(sql0);
        ResultSet rs0=ps0.executeQuery();

        while (rs0.next()){
            dw=rs0.getString(rs0.findColumn("classify_unit"));
        }

        if(list!=null&&list.size()>0) {
            String sql = "select replace(format(sum(data_normal_tmp),2),',','') as he from regional_innovation_indicator_data where data_date='2017-12-12 00:00:00' and classify_name='" + clify + "' and area_id in (";
            for (String s : list) {
                sql = sql + "'" + s + "',";
            }

            sql = sql.substring(0, sql.length() - 1);
            sql = sql + ")";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            String he = null;
            while (rs.next()) {
                he = rs.getString(rs.findColumn("he"));
            }

            if(Dup.nullor(he)) {
                return he+"###"+dw;
            }else{
                return "0"+"###"+dw;
            }
        }else {
            return "0"+"###"+dw;
        }

    }

    public static String baifen(String jingji,String f1,String f2,int c) throws SQLException {
        String rdhe=danwei(sum(jingji,f1));
        String gdhe=danwei(sum(jingji,f2));
        if(Double.parseDouble(gdhe)==0){
            return "0";
        }
        DecimalFormat format;
        double da=Double.parseDouble(rdhe)/Double.parseDouble(gdhe)*c;
        if(da>=1) {
            format = new DecimalFormat(".00");
            if(c==100){
                format = new DecimalFormat(".00");
            }
        }else if(da>-1&&da<1){
            format=new DecimalFormat("0.00");
            if(c==100){
                format = new DecimalFormat("0.00");
            }
        }else{
            format = new DecimalFormat(".00");
            if(c==100){
                format = new DecimalFormat(".00");
            }
        }
        return format.format(da);
    }

    public static String baifenxin(String jingji,String f1,int c) throws SQLException {
        List<String> list=new ArrayList<>();

        if(jingji.equals("京津冀")){
            list=list1;
        }else if(jingji.equals("长三角")){
            list=list2;
        }else if(jingji.equals("珠港澳")){
            list=list3;
        }

        String rdhe=sum(jingji,f1).split("###")[0];
        String gdhe= String.valueOf(list.size());
        if(Double.parseDouble(gdhe)==0){
            return "0";
        }
        DecimalFormat format;
        double da=Double.parseDouble(rdhe)/Double.parseDouble(gdhe)*c;
        if(da>=1) {
            format = new DecimalFormat(".00");
            if(c==100){
                format = new DecimalFormat(".00");
            }
        }else if(da>-1&&da<1){
            format=new DecimalFormat("0.00");
            if(c==100){
                format = new DecimalFormat("0.00");
            }
        }else{
            format = new DecimalFormat(".00");
            if(c==100){
                format = new DecimalFormat(".00");
            }
        }
        return format.format(da);
    }

    public static String sumbi(String jingji,String f1,String f2,String f3) throws SQLException {
        List<String> list=new ArrayList<>();

        if(jingji.equals("京津冀")){
            list=list1;
        }else if(jingji.equals("长三角")){
            list=list2;
        }else if(jingji.equals("珠港澳")){
            list=list3;
        }

        Map<String,String> map1=new HashMap<>();
        Map<String,String> map2=new HashMap<>();

        if(list!=null&&list.size()>0) {
            String sql = "select id,data_normal_tmp,classify_name,area_id,classify_unit_tmp from regional_innovation_indicator_data where data_date='2017-12-12 00:00:00' and (classify_name='"+f1+"' or classify_name='"+f2+"') and area_id in (";
            for (String s : list) {
                sql=sql+"'"+s+"',";
            }
            sql=sql.substring(0,sql.length()-1);
            sql=sql+")";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String data_normal_tmp=rs.getString(rs.findColumn("data_normal_tmp"));
                String classify_name=rs.getString(rs.findColumn("classify_name"));
                String area_id=rs.getString(rs.findColumn("area_id"));
                String classify_unit_tmp=rs.getString(rs.findColumn("classify_unit_tmp"));

                if(classify_name.equals(f1)){
                    map1.put(area_id,data_normal_tmp+"###"+classify_unit_tmp);
                }else if(classify_name.equals(f2)){
                    map2.put(area_id,data_normal_tmp+"###"+classify_unit_tmp);
                }
            }

            List<String> list0=new ArrayList<>();

            for(Map.Entry<String,String> entry: map1.entrySet()){
                for(Map.Entry<String,String> entry1:map2.entrySet()){
                    if(entry.getKey().equals(entry1.getKey())){
                        if(Double.parseDouble(danwei(entry1.getValue()))!=0) {
                            double da = Double.parseDouble(danwei(entry.getValue())) / Double.parseDouble(danwei(entry1.getValue())) * 100;
                            list0.add(String.valueOf(da));
                        }else{
                            list0.add("0");
                        }
                        break;
                    }
                }
            }
            String zs=lisun(list0);
            String gz=danwei(sum(jingji,f3));
            if(Double.parseDouble(zs)==0){
                return "0";
            }
            DecimalFormat format;
            double da=Double.parseDouble(gz)/Double.parseDouble(zs)*100;
            if(da>=1) {
                format = new DecimalFormat(".00");
            }else if(da>-1&&da<1){
                format=new DecimalFormat("0.00");
            }else{
                format = new DecimalFormat(".00");
            }
            return format.format(da);
        }else{
            return "0";
        }
    }

    public static String zibi(String jingji,String f1,String f2,String f3) throws SQLException {
        List<String> list=new ArrayList<>();

        if(jingji.equals("京津冀")){
            list=list1;
        }else if(jingji.equals("长三角")){
            list=list2;
        }else if(jingji.equals("珠港澳")){
            list=list3;
        }

        Map<String,String> map1=new HashMap<>();
        Map<String,String> map2=new HashMap<>();

        if(list!=null&&list.size()>0) {
            String sql = "select data_normal_tmp,classify_name,area_id,classify_unit_tmp from regional_innovation_indicator_data where data_date='2017-12-12 00:00:00' and (classify_name='"+f1+"' or classify_name='"+f2+"') and area_id in (";
            for (String s : list) {
                sql=sql+"'"+s+"',";
            }
            sql=sql.substring(0,sql.length()-1);
            sql=sql+")";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String data_normal_tmp=rs.getString(rs.findColumn("data_normal_tmp"));
                String classify_name=rs.getString(rs.findColumn("classify_name"));
                String area_id=rs.getString(rs.findColumn("area_id"));
                String classify_unit_tmp=rs.getString(rs.findColumn("classify_unit_tmp"));

                if(classify_name.equals(f1)){
                    map1.put(area_id,data_normal_tmp+"###"+classify_unit_tmp);
                }else if(classify_name.equals(f2)){
                    map2.put(area_id,data_normal_tmp+"###"+classify_unit_tmp);
                }
            }

            List<String> list0=new ArrayList<>();

            for(Map.Entry<String,String> entry: map1.entrySet()){
                for(Map.Entry<String,String> entry1:map2.entrySet()){
                    if(entry.getKey().equals(entry1.getKey())){
                        double da=Double.parseDouble(danwei(entry.getValue()))*Double.parseDouble(danwei(entry1.getValue()));
                        list0.add(String.valueOf(da));
                        break;
                    }
                }
            }
            String zs=lisun(list0);
            String gz=danwei(sum(jingji,f3));
            DecimalFormat format;
            double da=Double.parseDouble(zs)/Double.parseDouble(gz)*100;
            if(da>=1) {
                format = new DecimalFormat(".00");
            }else if(da>-1&&da<1){
                format=new DecimalFormat("0.00");
            }else{
                format = new DecimalFormat(".00");
            }
            return format.format(da);
        }else{
            return "0";
        }

    }

    public static String xinbi(String jingji,String f1,int c) throws SQLException {
        List<String> list=new ArrayList<>();

        if(jingji.equals("京津冀")){
            list=list1;
        }else if(jingji.equals("长三角")){
            list=list2;
        }else if(jingji.equals("珠港澳")){
            list=list3;
        }

        Map<String,String> map1=new HashMap<>();

        if(list!=null&&list.size()>0) {
            String sql = "select data_normal_tmp,classify_name,area_id,classify_unit_tmp from regional_innovation_indicator_data where data_date='2017-12-12 00:00:00' and classify_name='"+f1+"' and area_id in (";
            for (String s : list) {
                sql=sql+"'"+s+"',";
            }
            sql=sql.substring(0,sql.length()-1);
            sql=sql+")";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String data_normal_tmp=rs.getString(rs.findColumn("data_normal_tmp"));
                String classify_name=rs.getString(rs.findColumn("classify_name"));
                String area_id=rs.getString(rs.findColumn("area_id"));
                String classify_unit_tmp=rs.getString(rs.findColumn("classify_unit_tmp"));

                if(classify_name.equals(f1)){
                    map1.put(area_id,data_normal_tmp+"###"+classify_unit_tmp);
                }
            }

            List<String> list0=new ArrayList<>();

            for(Map.Entry<String,String> entry: map1.entrySet()){
                double da=Double.parseDouble(danwei(entry.getValue()))*c/100;
                list0.add(String.valueOf(da));
                break;

            }
            String zs= String.valueOf(Double.parseDouble(lisun(list0))/list0.size());
            DecimalFormat format;
            double da=Double.parseDouble(zs)/c*100;
            if(da>=1) {
                format = new DecimalFormat(".00");
            }else if(da>-1&&da<1){
                format=new DecimalFormat("0.00");
            }else{
                format = new DecimalFormat(".00");
            }
            return format.format(da);
        }else{
            return "0";
        }

    }




    public static String lisun(List<String> list){
        double da=0;
        if(list!=null&&list.size()>0){
            for(String s:list){
                if(!Dup.nullor(s)){
                    da=da+0;
                }else {
                    da = da + Double.parseDouble(s);
                }
            }
        }
        DecimalFormat format;
        if(da>1){
            format=new DecimalFormat("0.00");
        }else if(da>-1&&da<=1){
            format=new DecimalFormat("0.00");
        }else{
            format=new DecimalFormat("0.00");
        }
        return format.format(da);
    }
}
