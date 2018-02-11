package etl.kpmg;

import com.inno.utils.Dup;
import com.inno.utils.redisUtils.RedisClu;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class jisuan {
    private static Connection conn;
    static{
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.36:3306/kpmg_data";
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
        data();
    }

    public static void data() throws SQLException {
        String sql="select id,comp_rate,comp_rate_1,comp_rate_2,comp_rate_3,comp_rate_4,comp_rate_5,comp_rate_6,comp_rate_7,patent_rate,patent_rate_1,patent_rate_2,patent_rate_3,patent_rate_4,patent_rate_5,patent_rate_6,patent_rate_7,finance_num_rate,finance_num_rate_1,finance_num_rate_2,finance_num_rate_3,finance_num_rate_4,finance_num_rate_5,finance_num_rate_6,finance_num_rate_7,finance_amount_rate,finance_amount_rate_1,finance_amount_rate_2,finance_amount_rate_3,finance_amount_rate_4,finance_amount_rate_5,finance_amount_rate_6,finance_amount_rate_7 from kpmg_quarter_count";
        PreparedStatement ps=conn.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();

        while (rs.next()){
            String id=rs.getString(rs.findColumn("id"));

            List<String> list1=new ArrayList<>();
            List<String> list2=new ArrayList<>();
            List<String> list3=new ArrayList<>();
            List<String> list4=new ArrayList<>();

            list1.add(rs.getString(rs.findColumn("comp_rate")));
            list1.add(rs.getString(rs.findColumn("comp_rate_1")));
            list1.add(rs.getString(rs.findColumn("comp_rate_2")));
            list1.add(rs.getString(rs.findColumn("comp_rate_3")));
            list1.add(rs.getString(rs.findColumn("comp_rate_4")));
            list1.add(rs.getString(rs.findColumn("comp_rate_5")));
            list1.add(rs.getString(rs.findColumn("comp_rate_6")));
            list1.add(rs.getString(rs.findColumn("comp_rate_7")));

            list2.add(rs.getString(rs.findColumn("patent_rate")));
            list2.add(rs.getString(rs.findColumn("patent_rate_1")));
            list2.add(rs.getString(rs.findColumn("patent_rate_2")));
            list2.add(rs.getString(rs.findColumn("patent_rate_3")));
            list2.add(rs.getString(rs.findColumn("patent_rate_4")));
            list2.add(rs.getString(rs.findColumn("patent_rate_5")));
            list2.add(rs.getString(rs.findColumn("patent_rate_6")));
            list2.add(rs.getString(rs.findColumn("patent_rate_7")));

            list3.add(rs.getString(rs.findColumn("finance_num_rate")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_1")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_2")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_3")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_4")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_5")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_6")));
            list3.add(rs.getString(rs.findColumn("finance_num_rate_7")));

            list4.add(rs.getString(rs.findColumn("finance_amount_rate")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_1")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_2")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_3")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_4")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_5")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_6")));
            list4.add(rs.getString(rs.findColumn("finance_amount_rate_7")));


            String sqls="update kpmg_quarter_count set avg_comp_rate=?,avg_patent_rate=?,avg_finance_num_rate=?,avg_finance_amount_rate=?,grow_rate_avg=? where id=?";
            PreparedStatement pss=conn.prepareStatement(sqls);


            List<String> ll=new ArrayList<>();

            ll.add(qu(list1));
            ll.add(qu(list2));
            ll.add(qu(list3));
            ll.add(qu(list4));

            pss.setString(1,qu(list1));
            pss.setString(2,qu(list2));
            pss.setString(3,qu(list3));
            pss.setString(4,qu(list4));
            pss.setString(5,qu(ll));
            pss.setString(6,id);
            pss.executeUpdate();
        }




    }


    public static String qu(List<String> list){
        for(int x=0;x<list.size();x++){
            String s=list.get(x);
            if(!Dup.nullor(s)||s.equals("null")){
                list.remove(x);
                x=x-1;
            }
        }

        System.out.println(list);
        float sum=0;
        for(String s:list){
            float f = Float.parseFloat(s);
            sum = sum + f;
        }
        if(list!=null&&list.size()>0){
            sum=sum/list.size();
        }else{
            return null;
        }
        DecimalFormat decimalFormat;
        if(sum>1) {
            decimalFormat = new DecimalFormat(".00");
        }else if(sum>0&&sum<1){
            decimalFormat = new DecimalFormat("0.00");
        }else if(sum<0&&sum>-1){
            decimalFormat = new DecimalFormat("0.00");
        }else {
            decimalFormat = new DecimalFormat(".00");
        }
        return decimalFormat.format(sum);
    }
}
