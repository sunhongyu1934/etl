package test;

import com.inno.utils.Dup;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Yingshe {
    private static Connection conn;
    private static int a=0;
    private static Map<String,String> map=new HashMap<>();
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://etl2.innotree.org:3308/spider";
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

        String sql="select * from gaishi_ys";
        try {
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String cid=rs.getString(rs.findColumn("chain_id"));
                String ytag=rs.getString(rs.findColumn("y_tag"));
                String ztag=rs.getString(rs.findColumn("g_tag"));
                map.put(ztag,ytag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void main(String args[]) throws SQLException {
        chuli();
    }
    public static void chuli() throws SQLException {
        String sql="select comp_id,comp_full_name,comp_fenlei from chelianwang_shequ";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String sql2="update chelianwang_shequ set comp_inno_tag=? where comp_id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        while (rs.next()){
            String id=rs.getString(rs.findColumn("comp_id"));
            String cname=rs.getString(rs.findColumn("comp_full_name"));
            String tags=rs.getString(rs.findColumn("comp_fenlei"));

            if(Dup.nullor(tags)) {
                if (tags.contains(",")) {
                    String[] tagss = tags.split(",");
                    StringBuffer str=new StringBuffer();
                    for (String s : tagss) {
                        String yin = map.get(s);
                        if (Dup.nullor(yin)) {
                            str.append(yin+",");
                        }
                    }
                    if(Dup.nullor(str.toString().replace(",",""))) {
                        ps2.setString(1, str.toString().substring(0, str.length() - 1));
                        ps2.setString(2, id);
                        ps2.executeUpdate();
                        a++;
                        System.out.println(a + "*******************************************");
                    }
                }else{
                    String yin = map.get(tags);
                    if(Dup.nullor(yin)) {
                        ps2.setString(1,yin);
                        ps2.setString(2, id);
                        ps2.executeUpdate();
                        a++;
                        System.out.println(a + "*******************************************");
                    }
                }
            }
        }
    }


}
