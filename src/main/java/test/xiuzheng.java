package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisAction;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xiuzheng {
    private static RedisAction r=new RedisAction("10.44.51.90",6379);
    private static Connection conn;
    static{
        String driver1="com.mysql.cj.jdbc.Driver";
        String url2="jdbc:mysql://10.252.0.52:3306/tianyancha";
        String username2="etl_tmp";
        String password2="UsF4z5HE771KQpra";
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

        conn=con2;
    }
    public static void main(String args[]) throws SQLException {
        data();
    }

    public static void data() throws SQLException {
        int x=0;
        int j=0;
        for(int a=1;a<=16;a++) {
            String sql = "select distinct quan_cheng from tyc_jichu_quan limit "+x+",500000";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String cname = rs.getString(rs.findColumn("quan_cheng"));
                String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                String md5 = MD5Util.getMD5String(acname).substring(8, 24);
                String onid = UnsignedLong.valueOf(md5, 16).toString();

                r.remove("zhuce", acname);
                r.set("zhuce", onid);
                j++;
                System.out.println(j+"****************************************");
            }
            x=x+500000;
        }
    }


}
