package test;

import com.google.common.primitives.UnsignedLong;
import com.inno.utils.MD5utils.FenciUtils;
import com.inno.utils.redisUtils.RedisClu;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.inno.utils.MD5utils.MD5Util.getMD5String;

public class it_intro {
    private static Connection conn;
    private static RedisClu rd;
    static{
        rd=new RedisClu();

    }

    public static void main(String args[]) throws SQLException {
        /*String a=args[0];
        String b=args[1];
        String c=args[2];
        String d=args[3];

        String data=args[4];

        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider";
        String username="spider";
        String password="spider";

        if(data.equals("kaifa")){
            url1="jdbc:mysql://172.31.215.36:3306/innotree_data_project";
            username="sunhongyu";
            password="yJdviIeSuicKn8xX";
        }else if(data.equals("xianshang")){
            url1="jdbc:mysql://172.31.215.37:3306/innotree_data_online";
            username="test";
            password="123456";
        }
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




        chu4(a,b,c,d);*/

        /*it_intro i=new it_intro();
        Qy q=i.new Qy();
        ExecutorService pool=Executors.newCachedThreadPool();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    da(q);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        for(int a=1;a<=15;a++){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        qygx(q);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
        /*String t=args[0];
        String f=args[1];
        tail(t,f);*/

        String a=args[0];
        String b=args[1];
        String c=args[2];
        String d=args[3];
        String data=args[4];
        String driver1="com.mysql.jdbc.Driver";
        String url1="jdbc:mysql://172.31.215.38:3306/spider";
        String username="spider";
        String password="spider";

        if(data.equals("kaifa")){
            url1="jdbc:mysql://172.31.215.36:3306/innotree_data_project";
            username="sunhongyu";
            password="yJdviIeSuicKn8xX";
        }else if(data.equals("xianshang")){
            url1="jdbc:mysql://172.31.215.46:3306/innotree_data_online";
            username="base";
            password="imkloKuLiqNMc6Cn";
        }
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
        chu4(a,b,c,d);
        //tail();
    }

    public static void chu() throws SQLException {
        String sql="select c_id,company_full_name,company_introduction from it_company_pc where \n" +
                "company_full_name!='' and company_full_name is not null and company_full_name not like '%暂未收录%' and \n" +
                "company_introduction!='' and company_introduction is not null and company_introduction not like '%itjuzi%'";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql2="insert into it_intro(id,comp_full_name,intro) values(?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        ResultSet rs=ps.executeQuery();
        int a=0;
        while (rs.next()){
            String id=rs.getString(rs.findColumn("c_id"));
            String na=rs.getString(rs.findColumn("company_full_name"));
            String intro=rs.getString(rs.findColumn("company_introduction"));

            String acname= FenciUtils.chuli(na.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","").replace(" ","").replaceAll("\\s","").replace(" ","").trim());
            String onid= UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
            if(rd.get("zhuce",onid)){
                ps2.setString(1,id);
                ps2.setString(2,na);
                ps2.setString(3,intro);
                ps2.addBatch();
                a++;
                System.out.println(a+"*************************************");
            }
        }
        ps2.executeBatch();
    }

    public static void chu2() throws SQLException {
        int a = 0;
        for(int x=0;x<=9;x++) {
            String sql = "select DISTINCT comp_id from dimension_sum.comp_intro_total_sum"+x;
            PreparedStatement ps = conn.prepareStatement(sql);

            String sql2 = "update dimension_sum.comp_intro_total_sum"+x+" set is_reg=1 where comp_id=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String na = rs.getString(rs.findColumn("comp_id"));

                //String acname= FenciUtils.chuli(na.replace("省","").replace("市","").replace("区","").replace("(","").replace(")","").replace("（","").replace("）","").replace(" ","").replaceAll("\\s","").replace(" ","").trim());
                //String onid= UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                if (rd.get("zhuce", na)) {
                    ps2.setString(1, na);
                    ps2.executeUpdate();
                }
                a++;
                System.out.println(a + "*************************************");
            }
        }
    }

    public static void limo(Ca c) throws InterruptedException, SQLException {
        String sql2="insert into limo(comp_id,comp_full_name) values(?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        int a=0;
        while (true){
            try {
                String[] value = c.qu();
                if (value == null || value.length < 2) {
                    break;
                }
                if (rd.get("zhuce", value[0])) {
                    ps2.setString(1, value[0]);
                    ps2.setString(2, value[1]);
                    ps2.addBatch();
                    a++;
                    if (a % 1000 == 0) {
                        ps2.executeBatch();
                    }
                    System.out.println(a + "*************************************");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ps2.executeBatch();
    }

    public static void da(Qy q) throws SQLException {
        int s=0;
        for(int p=1;p<=35;p++) {
            String sql = "select * from company_qygx_xin limit "+s+",1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    String json = rs.getString(rs.findColumn("qygx_json"));
                    System.out.println(json);
                    System.exit(0);
                    String id = rs.getString(rs.findColumn("id"));
                    String tyc_id=rs.getString(rs.findColumn("tyc_id"));
                    String cs_id=rs.getString(rs.findColumn("cs_id"));
                    String company_name=rs.getString(rs.findColumn("company_name"));
                    q.fang(new String[]{json,id,tyc_id,cs_id,company_name});
                }catch (Exception e){
                    System.out.println("error");
                }
            }
            s=s+100000;
        }
    }

    public static void qygx(Qy q) throws InterruptedException, SQLException {
        String sql2="insert into company_qygx_xin(id,tyc_id,cs_id,company_name,qygx_json) values(?,?,?,?,?)";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        RedisClu rd=new RedisClu();
        int a = 0;
        while (true){
            try {
                String[] va = q.qu();
                JSONObject j = JSONObject.fromObject(va[0]);
                JSONArray ja = j.getJSONArray("nodes");
                JSONArray p=j.getJSONArray("links");
                for (int i = 0; i < ja.size(); i++) {
                    try {
                        JSONObject jo = ja.getJSONObject(i);
                        String na = jo.getString("name");
                        String type = jo.getString("type");
                        String noid=jo.getString("id");

                        if (type.equals("1")) {
                            String acname = FenciUtils.chuli(na.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                            String onid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                            if (rd.get("xianshang", onid)) {
                                for(int d=0;d<p.size();d++){
                                    JSONObject jj=p.getJSONObject(d);
                                    String target=jj.getString("target");
                                    String source=jj.getString("source");
                                    if(target.equals(noid)){
                                        jo.element("id", onid);
                                        jo.element("isClickable", "true");
                                        jj.element("target",onid);
                                    }
                                    if(source.equals(noid)){
                                        jo.element("id", onid);
                                        jo.element("isClickable", "true");
                                        jj.element("source",onid);
                                    }
                                }
                            }

                        }
                    } catch (Exception e) {
                        System.out.println("tiao error");
                    }
                }

                ps2.setString(1, va[1]);
                ps2.setString(2, va[2]);
                ps2.setString(3, va[3]);
                ps2.setString(4, va[4]);
                ps2.setString(5, j.toString());
                ps2.executeUpdate();
                a++;
                System.out.println(a + "*******************************************************");
            }catch (Exception e){
                System.out.println("chuli error");
            }
        }
    }

    public static void chu4(String t,String f,String d,String ff) throws SQLException {
        String sql2="update "+t+" set "+d+"=? where id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        int p = 0;
        for(int q=1;q<=10;q++) {
            String sql = "select id,"+f+" from "+t+" where "+d+"='' or "+d+" is null or "+d+"='0' limit "+p+","+ff;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            int a = 0;
            while (rs.next()) {
                try {
                    String id = rs.getString(rs.findColumn("id"));
                    String cname = rs.getString(rs.findColumn(f));

                    String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                    String onid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();

                    ps2.setString(1, onid);
                    ps2.setString(2, id);
                    ps2.executeUpdate();
                    a++;
                    System.out.println(a + "***************************************************");
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("error");
                }
            }
        }
    }

    public static void tail() throws SQLException {
        String sql2="update company_manager set tail_id=? where id=?";
        PreparedStatement ps2=conn.prepareStatement(sql2);
        int p = 0;
        for(int q=1;q<=10;q++) {
            String sql = "select id,comp_full_name,`manager_name` from company_manager limit "+p+",2000000";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            int a = 0;
            while (rs.next()) {
                try {
                    String id = rs.getString(rs.findColumn("id"));
                    String cname = rs.getString(rs.findColumn("comp_full_name"));
                    String pname = rs.getString(rs.findColumn("manager_name"));
                    List<String> list = new ArrayList<>();
                    list.add(pname);

                    String acname = FenciUtils.chuli(cname.replace("省", "").replace("市", "").replace("区", "").replace("(", "").replace(")", "").replace("（", "").replace("）", "").replace(" ", "").replaceAll("\\s", "").replace(" ", "").trim());
                    String onid = UnsignedLong.valueOf(getMD5String(acname).substring(8, 24), 16).toString();
                    String taid = UnsignedLong.valueOf(getMD5String(acname + list.toString()).substring(8, 24), 16).toString();

                    ps2.setString(1, taid);
                    ps2.setInt(2, Integer.parseInt(id));
                    a++;
                    ps2.executeUpdate();
                    System.out.println(a + "***************************************************");
                }catch (Exception e){
                    System.out.println("error");
                }
            }
            p=p+2000000;
        }
    }




    class Ca{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>();
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.poll(20, TimeUnit.SECONDS);
        }
    }

    public static class Gx{
        public List<no> nodes;
    }
    public static class no{
        public String name;
        public String type;
        public String id;
    }


    class Qy{
        BlockingQueue<String[]> po=new LinkedBlockingQueue<>(10000);
        public void fang(String[] key) throws InterruptedException {
            po.put(key);
        }
        public String[] qu() throws InterruptedException {
            return po.take();
        }
    }
}
