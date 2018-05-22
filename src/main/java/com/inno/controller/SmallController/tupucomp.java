package com.inno.controller.SmallController;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class tupucomp {
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

    public static void main(String args[]) throws SQLException {
        String ta=args[0];
        data(ta);
    }

    public static boolean check() throws SQLException {
        String sql="select id from comp_name_bulu_first where (mark_time='' or mark_time is null) limit 10";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        boolean bo=false;
        while (rs.next()){
            bo=true;
        }
        return bo;
    }

    public static void data(String ta) throws SQLException {
        for(int b=1;b<=2;b++) {
            if(b==1){
                ta=ta+"_first";
            }else{
                ta=ta.replace("_first","");
            }

            String sql0 = "select id,comp_full_name from " + ta + " where (mark_time='' or mark_time is null) and cover_flag=2";
            PreparedStatement ps0 = conn.prepareStatement(sql0);
            ResultSet rs0 = ps0.executeQuery();

            java.util.Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(date);

            Set<String> set = rd.getAllset("comp_in_name");
            Set<String> ss = new HashSet<>();

            int a = 0;
            while (rs0.next()) {
                try {
                    String cname = rs0.getString(rs0.findColumn("comp_full_name"));
                    String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                    String compid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                    rd.set("tupu", compid);
                    ss.add(cname);
                    a++;
                    System.out.println(a + "************************************************************");
                } catch (Exception e) {

                }
            }
            System.out.println(set.size());
            if (set != null && set.size() > 0) {
                int pp = 0;
                String sql2 = "update " + ta + " set mark_time='" + time + "' where comp_full_name in (";
                for (String s : set) {
                    sql2 = sql2 + "'" + s.replace("'", "").replace("\"", "").replace("/", "").replace("\\", "") + "',";
                    if (pp % 10000 == 0 && pp != 0) {
                        sql2 = sql2.substring(0, sql2.length() - 1);
                        sql2 = sql2 + ")";
                        PreparedStatement ps2 = conn.prepareStatement(sql2);
                        System.out.println(ps2.executeUpdate() + "    is update");
                        sql2 = "update " + ta + " set mark_time='" + time + "' where comp_full_name in (";
                    }
                    pp++;
                }
                sql2 = sql2.substring(0, sql2.length() - 1);
                sql2 = sql2 + ")";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                System.out.println(ps2.executeUpdate() + "    is update");
            }
            data2(ss);
            boolean bo=check();
            if(bo){
                break;
            }

        }
    }

    public static void data2(Set<String> set) throws SQLException {
        if(set!=null&&set.size()>0) {
            for (int a = 4; a >= 0; a--) {
                String sql = "update dw_dim_online.comp_tupu_level" + a + " set flag=1,mark=1 where comp_full_name in (";
                for (String s : set) {
                    sql = sql + "'" + s.replace("'","").replace("\"","").replace("/","").replace("\\","") + "',";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql = sql + ")";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.executeUpdate();
            }
        }



        String sql0="insert into innotree_data_financing.comp_name_bulu(comp_full_name,cover_flag) values(?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);

        int ji=0;
        for(int b=4;b>=0;b--){
            String sql1="update dw_dim_online.comp_tupu_level"+b+" set mark=1 where comp_full_name=?";
            PreparedStatement ps1=conn.prepareStatement(sql1);

            String sql="select comp_full_name from dw_dim_online.comp_tupu_level"+b+" where flag=0 and mark=0";
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            boolean bo=true;
            while (rs.next()){
                String cname=rs.getString(rs.findColumn("comp_full_name"));

                if(!rd.get("tupuflag",cname)){
                    ps0.setString(1,cname);
                    ps0.setString(2,"2");
                    ps0.executeUpdate();

                    rd.set("tupuflag",cname);
                    ji++;
                    if(ji>=2000){
                        bo=false;
                        break;
                    }
                }

                ps1.setString(1,cname);
                ps1.executeUpdate();
            }
            if(!bo){
                break;
            }
        }

        if(ji==0){
            int de=0;

            for(int pp=4;pp>=0;pp--){
                String sqll="select id from dw_dim_online.comp_tupu_level"+pp+" where flag=1 and mark=1";
                PreparedStatement psl=conn.prepareStatement(sqll);
                ResultSet rsl=psl.executeQuery();
                while (rsl.next()){
                    de++;
                }
            }

            /*if(de==0) {
                String sqls="update dw_dim_online.comp_tupu_level0 set flag=0,mark=0";
                PreparedStatement pss=conn.prepareStatement(sqls);
                pss.executeUpdate();

                for(int qq=1;qq<=4;qq++){
                    String sqq="truncate dw_dim_online.comp_tupu_level"+qq;
                    PreparedStatement psqq=conn.prepareStatement(sqq);
                    psqq.executeUpdate();
                }

                rd.del("tupuflag");
            }*/
        }
    }


}
