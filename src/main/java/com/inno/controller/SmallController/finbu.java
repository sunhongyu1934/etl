package com.inno.controller.SmallController;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;

import java.sql.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class finbu {
    private static java.sql.Connection conn;
    private static Connection conn2;
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



        String url2="jdbc:mysql://172.31.215.37:3306/innotree_data_online";
        String username2="base";
        String password2="imkloKuLiqNMc6Cn";
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

        conn2=con2;
        conn=con;

    }
    public static void main(String args[]) throws SQLException {
        String ta=args[0];
        data(ta);
    }

    public static void data(String ta) throws SQLException {
        String sql="select b.* from (select max(id) as id from tyc.tyc_jichu_quan where quan_cheng in (select comp_full_name from "+ta+") group by quan_cheng having(count(quan_cheng))>=1) a LEFT JOIN tyc.tyc_jichu_quan b on a.id=b.id";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        String sql0="select b.* from (select max(id) as id from tianyancha.tyc_jichu_quan where quan_cheng in (select comp_full_name from "+ta+") group by quan_cheng having(count(quan_cheng))>=1) a LEFT JOIN tianyancha.tyc_jichu_quan b on a.id=b.id";
        PreparedStatement ps0=conn.prepareStatement(sql0);
        ResultSet rs0=ps0.executeQuery();


        tui(rs);
        tui(rs0);
    }

    public static void tui(ResultSet rs) throws SQLException {
        int a=0;


        String sql2="insert into company_base_info(comp_id,comp_full_name,comp_english_name,comp_credit_code,comp_type,comp_corporation,comp_reg_captital,comp_create_date,comp_reg_authority,comp_bus_duration,comp_issue_date,comp_reg_addr,comp_web_url,comp_logo_tmp,comp_phone,comp_email,comp_bus_range,comp_introduction,comp_org_code,taxpayer_num,opreat_status,registe_num) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps2=conn2.prepareStatement(sql2);

        String sql3="select * from company_base_info where comp_id=?";
        PreparedStatement ps3=conn2.prepareStatement(sql3);

        while (rs.next()) {
            try {
                String quan = zichu(rs.getString(rs.findColumn("quan_cheng")));
                String acname = FenciUtils.chuli(quan.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                String comp_id = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

                String enquan = zichu(rs.getString(rs.findColumn("en_cname")));
                String tongyi = zichu(rs.getString(rs.findColumn("tongyi_xinyong")));
                String qilei = zichu(rs.getString(rs.findColumn("qiye_leixing")));
                String faren = zichu(rs.getString(rs.findColumn("fa_ren")));
                String zhuzi = zichu(rs.getString(rs.findColumn("zhuce_ziben")));
                String zhushi = zichu(rs.getString(rs.findColumn("zhuce_shijian")));
                String dengji = zichu(rs.getString(rs.findColumn("dengji_jiguan")));
                String yingye = zichu(rs.getString(rs.findColumn("yingye_nianxian")));
                String hezhun = zichu(rs.getString(rs.findColumn("hezhun_riqi")));
                String zhudi = zichu(rs.getString(rs.findColumn("zhuce_dizhi")));
                String web = zichu(rs.getString(rs.findColumn("w_eb")));
                String logo = zichu(rs.getString(rs.findColumn("logo")));
                String phone = zichu(rs.getString(rs.findColumn("p_hone")));
                String email = zichu(rs.getString(rs.findColumn("e_mail")));
                String jingfan = zichu(rs.getString(rs.findColumn("jingying_fanwei")));
                String desc = zichu(rs.getString(rs.findColumn("c_desc")));
                String zuzhi = zichu(rs.getString(rs.findColumn("zuzhijigou_daima")));
                String nashui = zichu(rs.getString(rs.findColumn("nashui_shibie")));
                String jingzhuang = zichu(rs.getString(rs.findColumn("jingying_zhuangtai")));
                String gongshang = zichu(rs.getString(rs.findColumn("gongshang_hao")));

                ps3.setString(1,comp_id);
                ResultSet rs3=ps3.executeQuery();

                int aa=0;
                while (rs3.next()){
                    aa++;
                }

                System.out.println(comp_id);
                if(aa==0) {
                    ps2.setString(1, comp_id);
                    ps2.setString(2, quan);
                    ps2.setString(3, enquan);
                    ps2.setString(4, tongyi);
                    ps2.setString(5, qilei);
                    ps2.setString(6, faren);
                    ps2.setString(7, zhuzi);
                    ps2.setString(8, zhushi);
                    ps2.setString(9, dengji);
                    ps2.setString(10, yingye);
                    ps2.setString(11, hezhun);
                    ps2.setString(12, zhudi);
                    ps2.setString(13, web);
                    ps2.setString(14, logo);
                    ps2.setString(15, phone);
                    ps2.setString(16, email);
                    ps2.setString(17, jingfan);
                    ps2.setString(18, desc);
                    ps2.setString(19, zuzhi);
                    ps2.setString(20, nashui);
                    ps2.setString(21, jingzhuang);
                    ps2.setString(22, gongshang);
                    ps2.executeUpdate();
                    a++;
                    System.out.println(a + "******************************************************");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String  zichu(String key){
        try {
            if (Dup.nullor(key)) {
                return key.replaceAll("\\s", "").replace("未公开", "");
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }
}
