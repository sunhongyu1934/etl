package test;

import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;

public class sendlin {
    private static RedisClu rd;
    private static Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.44:3306/spider";
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
        int p=0;
        int a=0;

        for(int x=1;x<=15;x++) {
            String sql = "select only_id from dimension_sum.compid_zl limit " + p + ",1000000";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            boolean bo=true;
            while (rs.next()) {
                bo=false;
                String cid = rs.getString(rs.findColumn("only_id"));
                rd.set("comp_in", cid);
                a++;
                System.out.println(a + "*****************************************");
            }
            if(bo){
                break;
            }
            p=p+1000000;
        }
    }
}
