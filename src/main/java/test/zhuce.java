package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;

public class zhuce {
    private static Connection conn;
    private static RedisClu rd;
    private static int j=0;
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
        data();
        data2();
        data3();
        data4();
    }

    public static void data() throws SQLException {
        int d=0;
        while (true) {
            try {
                boolean bo = true;
                String sql = "select distinct quan_cheng from tyc.tyc_jichu_quan limit " + d + ",500000";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        bo = false;
                        String name = rs.getString(rs.findColumn("quan_cheng"));
                        if (Dup.nullor(name)) {
                            String acname = FenciUtils.chuli(name.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                            String onid = UnsignedLong.valueOf(MD5Util.getMD5String(acname).substring(8, 24), 16).toString();
                            rd.set("zhuce", onid);
                            j++;
                            System.out.println(1+"##################"+j + "**********************************************************");
                        }
                    } catch (Exception e) {
                        System.out.println("detail error");
                    }
                }
                if (bo) {
                    break;
                }
                d = d + 500000;
            }catch (Exception ee){
                System.out.println("error");
            }
        }
    }

    public static void data2() throws SQLException {
        int d=0;
        while (true) {
            try {
                boolean bo = true;
                String sql = "select distinct quan_cheng from tyc.tyc_jichu_quan1 limit " + d + ",500000";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        bo = false;
                        String name = rs.getString(rs.findColumn("quan_cheng"));
                        if (Dup.nullor(name)) {
                            String acname = FenciUtils.chuli(name.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                            String onid = UnsignedLong.valueOf(MD5Util.getMD5String(acname).substring(8, 24), 16).toString();
                            rd.set("zhuce", onid);
                            j++;
                            System.out.println(2+"#####################"+j + "**********************************************************");
                        }
                    } catch (Exception e) {
                        System.out.println("detail error");
                    }
                }
                if (bo) {
                    break;
                }
                d = d + 500000;
            }catch (Exception ee){
                System.out.println("error");
            }
        }
    }

    public static void data3() throws SQLException {
        int d=0;
        while (true) {
            try {
                boolean bo = true;
                String sql = "select distinct quan_cheng from tianyancha.tyc_jichu_quan limit " + d + ",500000";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        bo = false;
                        String name = rs.getString(rs.findColumn("quan_cheng"));
                        if (Dup.nullor(name)) {
                            String acname = FenciUtils.chuli(name.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                            String onid = UnsignedLong.valueOf(MD5Util.getMD5String(acname).substring(8, 24), 16).toString();
                            rd.set("zhuce", onid);
                            j++;
                            System.out.println(3+"################"+j + "**********************************************************");
                        }
                    } catch (Exception e) {
                        System.out.println("detail error");
                    }
                }
                if (bo) {
                    break;
                }
                d = d + 500000;
            }catch (Exception ee){
                System.out.println("error");
            }
        }
    }

    public static void data4() throws SQLException {
        int d=0;
        while (true) {
            try {
                boolean bo = true;
                String sql = "select distinct quan_cheng from tyc_xin.tyc_jichu_quan limit " + d + ",500000";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        bo = false;
                        String name = rs.getString(rs.findColumn("quan_cheng"));
                        if (Dup.nullor(name)) {
                            String acname = FenciUtils.chuli(name.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                            String onid = UnsignedLong.valueOf(MD5Util.getMD5String(acname).substring(8, 24), 16).toString();
                            rd.set("zhuce", onid);
                            j++;
                            System.out.println(4+"#####################"+j + "**********************************************************");
                        }
                    } catch (Exception e) {
                        System.out.println("detail error");
                    }
                }
                if (bo) {
                    break;
                }
                d = d + 500000;
            }catch (Exception ee){
                System.out.println("error");
            }
        }
    }
}
