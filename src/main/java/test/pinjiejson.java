package test;

import com.inno.utils.redisUtils.RedisAction;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pinjiejson {
    private static Connection conn;
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
        conn=con;
    }

    public static void main(String args[]) throws SQLException {
        chuli();
    }

    public static void chuli() throws SQLException {
        String sql="select DISTINCT pro_name,pro_id from project_base_info where pro_id in (select tProId from app_tgi)";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();


        String sql0="insert into project_app_cover(pid,app_name,`data`) values(?,?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);


        int a=0;
        while (rs.next()){
            String pname=rs.getString(rs.findColumn("pro_name"));
            String pid=rs.getString(rs.findColumn("pro_id"));

            Map<String,List<Map<String,String>>> map=new HashMap<>();
            List<Map<String,String>> list=new ArrayList<>();

            String sql2="select DISTINCT pro_name,pro_id from project_base_info where pro_id in (select DISTINCT gProId from app_tgi where tProId="+pid+")";
            PreparedStatement ps2=conn.prepareStatement(sql2);
            ResultSet rs2=ps2.executeQuery();

            while (rs2.next()){
                Map<String,String> mapp=new HashMap<>();
                String gname=rs2.getString(rs2.findColumn("pro_name"));
                String gid=rs.getString(rs.findColumn("pro_id"));
                mapp.put("pid",gid);
                mapp.put("name",gname);
                mapp.put("value","");
                list.add(mapp);
            }
            map.put("data",list);
            JSONObject jsonObject=JSONObject.fromObject(map);
            ps0.setString(1,pid);
            ps0.setString(2,pname);
            ps0.setString(3,jsonObject.toString());
            ps0.executeUpdate();
            a++;
            System.out.println(a+"**********************************************");
        }
    }

}
