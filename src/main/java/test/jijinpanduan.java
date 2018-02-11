package test;

import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisClu;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class jijinpanduan {
    private static Connection conn;
    private static RedisClu redisAction;

    static{

        String driver1="com.mysql.cj.jdbc.Driver";
        String url1="jdbc:mysql://etl1.innotree.org:3308/clean_data";
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
        redisAction=new RedisClu();
    }

    public static void main(String args[]){
        jijinpanduan j=new jijinpanduan();
        final Ku k=j.new Ku();
        ExecutorService pool=Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    data(k);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Qin(k);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void data(Ku k) throws SQLException, InterruptedException {
        String sql="select c_name from sh_com where zhuce_or=1";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            String ming=rs.getString(rs.findColumn("c_name"));
            k.fang(ming);
        }
    }

    public static void Qin(Ku k) throws InterruptedException, IOException, SQLException {
        String sql="update sh_com set zhuce_or=?,only_id=? where c_name=?";
        PreparedStatement ps=conn.prepareStatement(sql);
        int a=0;
        while (true){
            String value=k.qu();
            if(!Dup.nullor(value)){
                break;
            }

            String onid= MD5Util.Onlyid(value,"ceshi",new ArrayList<>())[0];
            if(redisAction.get("zhuce",onid)){
                ps.setString(1,"0");
                ps.setString(2,onid);
                ps.setString(3,value);
            }else{
                ps.setString(1,"1");
                ps.setString(2,onid);
                ps.setString(3,value);
            }
            ps.executeUpdate();
            a++;
            System.out.println(a+"***************************************");
        }
    }



    class Ku{
        BlockingQueue<String> po=new LinkedBlockingQueue<>(10000);
        public void fang(String key) throws InterruptedException {
            po.put(key);
        }
        public String qu() throws InterruptedException {
            return po.poll(10, TimeUnit.SECONDS);
        }
    }

}
