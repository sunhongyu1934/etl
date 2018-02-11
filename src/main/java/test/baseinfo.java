package test;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baseinfo {
    private static Connection conn;
    private static Connection conn2;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://47.95.31.183:3306/innotree_data_online";
        String username="test";
        String password="123456";
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

        String url2="jdbc:mysql://47.95.31.183:3306/kpmg_data";
        String username2="kpmg_w";
        String password2="WK0BfVHoPFRUD2Kx";
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
        chuli();
    }
    public static void chuli() throws SQLException {
        System.out.println("开始处理1");
        int s=0;
        int a = 0;
        for(int x=1;x<=8;x++) {
            String sql = "select comp_full_name,id from company_chain_info limit "+s+",500000";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("读取完毕");

            String sql2 = "update company_chain_info set del_flag=1 where id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);

            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("comp_full_name"));
                String cid = rs.getString(rs.findColumn("id"));
                Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]");
                Matcher m = p.matcher(cname);
                if (!m.find()) {
                    ps2.setString(1, cid);
                    ps2.executeUpdate();
                    a++;
                    System.out.println(a + "******************************************************");
                }
            }
            s=s+500000;
        }
    }

    public static void chuli2() throws SQLException {
        System.out.println("开始处理2");
        int s=0;
        int a = 0;
        for(int x=1;x<=8;x++) {
            String sql = "select comp_full_name,comp_id from company_base_info limit "+s+",500000";
            PreparedStatement ps = conn2.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("读取完毕");

            String sql2 = "update company_base_info set del_flag=1 where comp_id=?";
            PreparedStatement ps2 = conn2.prepareStatement(sql2);

            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("comp_full_name"));
                String cid = rs.getString(rs.findColumn("comp_id"));
                Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]");
                Matcher m = p.matcher(cname);
                if (!m.find()) {
                    ps2.setString(1, cid);
                    ps2.executeUpdate();
                    a++;
                    System.out.println(a + "******************************************************");
                }
            }
            s=s+500000;
        }
    }


}
