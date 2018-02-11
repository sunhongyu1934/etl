package etl;

import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
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

    public static void main( String[] args ) throws SQLException {
        data();
    }

    public static void data() throws SQLException {
        int aa=0;
        for(int a=4;a>=0;a--) {
            String sql = "select comp_full_name from dw_dim_online.comp_tupu_level"+a+" where flag=0 and mark=2";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String cname=rs.getString(rs.findColumn("comp_full_name"));
                rd.set("tupuflag",cname);
                aa++;
                System.out.println(aa+"*********************************************");
            }
        }

    }
}
