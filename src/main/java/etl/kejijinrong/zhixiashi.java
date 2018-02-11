package etl.kejijinrong;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.Random;

public class zhixiashi {
    private static Connection conn;
    private static int aa=0;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.36:3306/innotree_data_project?useUnicode=true&useCursorFetch=true&defaultFetchSize=100&characterEncoding=utf-8&tcpRcvBuf=1024000";
        String username="base";
        String password="imkloKuLiqNMc6Cn";
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
        data2();
    }

    public static void data() throws SQLException {
        Random r=new Random();
        DecimalFormat decimalFormat=new DecimalFormat("0.00");

        String sql="select area_id,area_name,is_provincial_capital from regional_innovation_dim_sys_area where parent_name in ('北京市','上海市','重庆市','天津市')";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        String sql0="insert into regional_innovation_indicator_data(data_id,classify_id,classify_name,classify_short,classify_level,area_id,area_name,area_shortname,area_type,is_provincial_capital,data_src,classify_unit,classify_unit_tmp,data_normal,data_normal_tmp,data_date,data_date_src,data_source,del_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);


        while (rs.next()){
            double d=r.nextDouble()*5;
            String arid=rs.getString(rs.findColumn("area_id"))   ;
            String arname=rs.getString(rs.findColumn("area_name"));
            String isp=rs.getString(rs.findColumn("is_provincial_capital"));

            if(arname.equals("市辖区")){
                continue;
            }

            String sql1="select * from regional_innovation_indicator_data where area_name='鹤岗市' and data_date='2017-12-12 00:00:00'";
            PreparedStatement ps1=conn.prepareStatement(sql1);
            ResultSet rs1=ps1.executeQuery();
            while (rs1.next()){
                String shu=rs1.getString(rs1.findColumn("data_normal_tmp"));
                shu=decimalFormat.format(Double.parseDouble(shu)*d);
                ps0.setString(1,rs1.getString(rs1.findColumn("data_id")));
                ps0.setString(2,rs1.getString(rs1.findColumn("classify_id")));
                ps0.setString(3,rs1.getString(rs1.findColumn("classify_name")));
                ps0.setString(4,rs1.getString(rs1.findColumn("classify_short")));
                ps0.setString(5,rs1.getString(rs1.findColumn("classify_level")));
                ps0.setString(6,arid);
                ps0.setString(7,arname);
                ps0.setString(8,arname);
                ps0.setString(9,rs1.getString(rs1.findColumn("area_type")));
                ps0.setString(10,isp);
                ps0.setString(11,shu);
                ps0.setString(12,rs1.getString(rs1.findColumn("classify_unit")));
                ps0.setString(13,rs1.getString(rs1.findColumn("classify_unit_tmp")));
                ps0.setString(14,rs1.getString(rs1.findColumn("data_normal")));
                ps0.setString(15,shu);
                ps0.setString(16,rs1.getString(rs1.findColumn("data_date")));
                ps0.setString(17,rs1.getString(rs1.findColumn("data_date_src")));
                ps0.setString(18,rs1.getString(rs1.findColumn("data_source")));
                ps0.setString(19,rs1.getString(rs1.findColumn("del_flag")));
                ps0.executeUpdate();
                aa++;
                System.out.println(aa+"******************************************************************");
            }

        }
    }

    public static void data2() throws SQLException {
        Random r=new Random();
        DecimalFormat decimalFormat=new DecimalFormat("0.00");

        String sql0="insert into regional_innovation_indicator_data(data_id,classify_id,classify_name,classify_short,classify_level,area_id,area_name,area_shortname,area_type,is_provincial_capital,data_src,classify_unit,classify_unit_tmp,data_normal,data_normal_tmp,data_date,data_date_src,data_source,del_flag) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);

        int p=2017;
        for(int a=1;a<=6;a++) {
            double d=r.nextDouble()*5;
            String sql1="select * from regional_innovation_indicator_data where data_date='2017-12-12 00:00:00' and area_type=0";
            PreparedStatement ps1=conn.prepareStatement(sql1);
            ResultSet rs1=ps1.executeQuery();

            while (rs1.next()) {
                String shu = rs1.getString(rs1.findColumn("data_normal_tmp"));
                shu = decimalFormat.format(Double.parseDouble(shu) * d);
                ps0.setString(1, rs1.getString(rs1.findColumn("data_id")));
                ps0.setString(2, rs1.getString(rs1.findColumn("classify_id")));
                ps0.setString(3, rs1.getString(rs1.findColumn("classify_name")));
                ps0.setString(4, rs1.getString(rs1.findColumn("classify_short")));
                ps0.setString(5, rs1.getString(rs1.findColumn("classify_level")));
                ps0.setString(6, rs1.getString(rs1.findColumn("area_id")));
                ps0.setString(7, rs1.getString(rs1.findColumn("area_name")));
                ps0.setString(8, rs1.getString(rs1.findColumn("area_shortname")));
                ps0.setString(9, rs1.getString(rs1.findColumn("area_type")));
                ps0.setString(10, rs1.getString(rs1.findColumn("is_provincial_capital")));
                ps0.setString(11, shu);
                ps0.setString(12, rs1.getString(rs1.findColumn("classify_unit")));
                ps0.setString(13, rs1.getString(rs1.findColumn("classify_unit_tmp")));
                ps0.setString(14, rs1.getString(rs1.findColumn("data_normal")));
                ps0.setString(15, shu);
                ps0.setString(16, 2017-a+"-12-12 00:00:00");
                ps0.setString(17, rs1.getString(rs1.findColumn("data_date_src")));
                ps0.setString(18, rs1.getString(rs1.findColumn("data_source")));
                ps0.setString(19, rs1.getString(rs1.findColumn("del_flag")));
                ps0.executeUpdate();
                aa++;
                System.out.println(aa + "******************************************************************");
            }
        }
    }

}
