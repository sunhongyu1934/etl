package etl.kejijinrong;

import com.inno.utils.Dup;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class area {
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

    public static void main(String args[]) throws IOException, SQLException {
        String sheet=args[0];
        //data("/data1/城市指标数据-20171224.xls",sheet);
        //data("/data1/省区指标数据-20171224（新1）.xlsx",sheet);
        data("/data1/城市指标数据-20180202 - 测试数据(2).xls",sheet);
        //data("C:\\Users\\13434\\Desktop\\科技部\\城市指标数据-20171224.xls");
    }

    public static void data(String path,String sheet) throws IOException, SQLException {
        Map<String,Map<String,String>> map=readxls(path,sheet);

        String sql="select * from regional_innovation_dim_sys_classify where classify_name=? or classify_short=?";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql0="insert into regional_innovation_indicator_data(classify_id,classify_name,classify_short,classify_level,area_id,area_name,area_shortname,area_type,is_provincial_capital,data_src,classify_unit_tmp,data_normal_tmp,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);

        String sql1="select area_id,area_name,area_type,is_provincial_capital from regional_innovation_dim_sys_area where replace(replace(replace(replace(replace(area_name,'市',''),'自治区',''),'回族',''),'壮族',''),'维吾尔','')=?";
        PreparedStatement ps1=conn.prepareStatement(sql1);

        for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
            String area=entry.getKey().replaceAll("\\s","").replace("市","").replace("自治区","").replace("回族","").replace("壮族","").replace("维吾尔","").replace("维吾尔族","");

            if(path.contains("省区")){
                area=area+"省";
            }

            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            for(Map.Entry<String,String> entry1:entry.getValue().entrySet()){
                ps.setString(1,entry1.getKey().split("（")[0]);
                ps.setString(2,entry1.getKey().split("（")[0]);
                ResultSet rs=ps.executeQuery();
                String clid = null;
                String clname= null;
                String clsh= null;
                String clle= null;
                String clun= null;
                String arid= null;
                String arname= null;
                String arty= null;
                String aris= null;
                String p=null;

                boolean bs=true;
                while (rs.next()){
                    bs=false;
                    clid=rs.getString(rs.findColumn("classify_id"));
                    clname=rs.getString(rs.findColumn("classify_name"));
                    clsh=rs.getString(rs.findColumn("classify_short"));
                    clle=rs.getString(rs.findColumn("classify_level"));
                    clun=rs.getString(rs.findColumn("classify_unit"));
                }
                if(bs){
                    System.out.println(entry1.getKey().split("（")[0]);
                }

                ps1.setString(1,area);
                ResultSet rs1=ps1.executeQuery();
                boolean bo=true;
                while (rs1.next()){
                    bo=false;
                    arid=rs1.getString(rs1.findColumn("area_id"));
                    arname=rs1.getString(rs1.findColumn("area_name"));
                    arty=rs1.getString(rs1.findColumn("area_type"));
                    aris=rs1.getString(rs1.findColumn("is_provincial_capital"));
                    if(!Dup.nullor(aris)){
                        aris="0";
                    }
                }
                if(bo){
                    ps1.setString(1,area.replace("省",""));
                    ResultSet rss=ps1.executeQuery();
                    while (rss.next()){
                        arid=rss.getString(rss.findColumn("area_id"));
                        arname=rss.getString(rss.findColumn("area_name"));
                        arty=rss.getString(rss.findColumn("area_type"));
                        aris=rss.getString(rss.findColumn("is_provincial_capital"));
                        if(!Dup.nullor(aris)){
                            aris="0";
                        }
                    }
                }

                String shu=entry1.getValue();
                if(shu.equals("/")){
                    shu="0";
                }
                if(Dup.nullor(shu)){
                    try {
                        double f = Double.parseDouble(shu);
                        p = decimalFormat.format(f);//format 返回的是字符串
                    }catch (Exception e){
                        p="0";
                    }
                }

                ps0.setString(1,clid);
                ps0.setString(2,clname);
                ps0.setString(3,clsh);
                ps0.setString(4,clle);
                ps0.setString(5,arid);
                ps0.setString(6,arname);
                ps0.setString(7,arname);
                ps0.setString(8,arty);
                ps0.setString(9,aris);
                ps0.setString(10,p);
                ps0.setString(11,clun);
                ps0.setString(12,p);
                ps0.setString(13,sheet.replace("年","")+"-12-12 00:00:00");
                ps0.executeUpdate();
                aa++;
                System.out.println(aa+"**********************************************************");

            }
        }

    }

    public static Map<String,Map<String,String>> readxls(String path,String sheets) throws IOException {
        InputStream in=new FileInputStream(path);
        List<String> list=new ArrayList<>();
        Map<String,Map<String,String>> map=new HashMap<>();
        if(path.contains("xlsx")){
            XSSFWorkbook xssf=new XSSFWorkbook(in);
            XSSFSheet sheet=xssf.getSheet(sheets);
            for(int ro=0;ro<sheet.getLastRowNum();ro++){
                XSSFRow row=sheet.getRow(ro);
                if(row==null){
                    continue;
                }
                int min=row.getFirstCellNum();
                int max=row.getLastCellNum();
                String area=null;
                Map<String,String> mm=new HashMap<>();
                for(int co=min;co<max;co++){
                    XSSFCell cell=row.getCell(co);
                    String ce = null;
                    if(cell==null||!Dup.nullor(cell.toString())){
                        if(ro>0&&co>0){
                            ce="0.00";
                        }else{
                            continue;
                        }
                    }
                    if(!Dup.nullor(ce)){
                        ce=cell.toString();
                    }

                    if(ro==0&&Dup.nullor(ce)){
                        list.add(ce);
                    }
                    if(ro>0&&co==0&&Dup.nullor(ce)){
                        area=ce;
                    }
                    if(ro>0&&co>0&&Dup.nullor(area)){
                        mm.put(list.get(co-1),ce);
                        map.put(area,mm);
                    }
                }
            }
        }else{
            HSSFWorkbook hssf=new HSSFWorkbook(in);
            HSSFSheet sheet=hssf.getSheet(sheets);
            for(int ro=0;ro<sheet.getLastRowNum();ro++){
                HSSFRow row=sheet.getRow(ro);
                if(row==null||!Dup.nullor(row.toString())){
                    continue;
                }
                int min=row.getFirstCellNum();
                int max=row.getLastCellNum();
                String area=null;
                Map<String,String> mm=new HashMap<>();
                for(int co=min;co<max;co++){
                    HSSFCell cell=row.getCell(co);
                    String ce=null;
                    if(cell==null||!Dup.nullor(cell.toString())){
                        if(ro>0&&co>0){
                            ce="0.00";
                        }else {
                            continue;
                        }
                    }

                    if(!Dup.nullor(ce)){
                        ce=cell.toString();
                    }

                    if(ro==0&&Dup.nullor(ce)){
                        list.add(ce);
                    }
                    if(ro>0&&co==0&&Dup.nullor(ce)){
                        area=ce;
                    }
                    if(ro>0&&co>0&&Dup.nullor(area)){
                        if(list.size()<=(co-1)){
                            continue;
                        }
                        mm.put(list.get(co-1),ce);
                        map.put(area,mm);
                    }
                }
            }
        }
        return map;
    }
}
