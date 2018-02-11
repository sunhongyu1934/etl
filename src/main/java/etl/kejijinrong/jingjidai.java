package etl.kejijinrong;

import com.inno.utils.Dup;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jingjidai {

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
        data();
    }

    public static void data() throws SQLException {
        String di[]=new String[]{"京津冀","长三角"};
        Map<String,Map<String,String>> map=new HashMap<>();
        for(String s:di) {
            String yi = jingjidaijisuan.sum(s,"全社会R&D经费支出").split("###")[0];
            String er=jingjidaijisuan.baifen(s,"全社会R&D经费支出","GDP",100);
            String san=jingjidaijisuan.sum(s,"规上工业企业R&D经费支出").split("###")[0];
            String si=jingjidaijisuan.sumbi(s,"规上工业企业R&D经费支出","规上工业企业R&D经费支出占主营业务收入比重","规上工业企业R&D经费支出");
            String wu=jingjidaijisuan.sum(s,"科技公共财政支出").split("###")[0];
            String liu=jingjidaijisuan.sumbi(s,"科技公共财政支出","科技公共财政支出占公共财政支出的比重","科技公共财政支出");
            String qi=jingjidaijisuan.sum(s,"创业投资引导基金总额").split("###")[0];
            String ba=jingjidaijisuan.baifen(s,"创业投资引导基金总额","GDP",100);
            String jiu=jingjidaijisuan.sum(s,"R&D经费加计扣除所得税减免额").split("###")[0];
            String shi=jingjidaijisuan.sumbi(s,"R&D经费加计扣除所得税减免额","R&D经费加计扣除所得税减免额占企业研发经费比重","R&D经费加计扣除所得税减免额");
            String shier=jingjidaijisuan.baifenxin(s,"基础研究经费占R&D经费的比重",1);
            String shisan=jingjidaijisuan.sum(s,"R&D人员").split("###")[0];
            String shisi=jingjidaijisuan.baifen(s,"R&D人员","每万名就业人员中R&D人员",10000);
            String shiwu=jingjidaijisuan.sum(s,"“千人计划”入选人数").split("###")[0];
            String shiliu=jingjidaijisuan.sum(s,"“万人计划”入选人数").split("###")[0];
            String shiqi=jingjidaijisuan.sum(s,"国家重点实验室数量").split("###")[0];
            String shiba=jingjidaijisuan.sum(s,"省级重点实验室数量").split("###")[0];
            String shijiu=jingjidaijisuan.sum(s,"国家工程研究中心数量").split("###")[0];
            String ershi=jingjidaijisuan.sum(s,"省级工程研究中心数量").split("###")[0];
            String eryi=jingjidaijisuan.sum(s,"国家技术创新中心数量").split("###")[0];
            String erer=jingjidaijisuan.sum(s,"省级技术创新中心数量").split("###")[0];
            String ersan=jingjidaijisuan.sum(s,"国家国际科技合作基地数量").split("###")[0];
            String ersi=jingjidaijisuan.sum(s,"国家级科技企业孵化器数量").split("###")[0];
            String erwu=jingjidaijisuan.sum(s,"省级科技企业孵化器数量").split("###")[0];
            String erliu=jingjidaijisuan.sum(s,"国家大学科技园数量").split("###")[0];
            String erqi=jingjidaijisuan.sum(s,"国家备案众创空间数量").split("###")[0];
            String erba=jingjidaijisuan.sum(s,"外资研发中心数量").split("###")[0];
            String erjiu=jingjidaijisuan.sum(s,"高新技术企业数").split("###")[0];
            String sanshi=jingjidaijisuan.baifen(s,"高新技术企业数","规上工业企业数",100);
            String sanyi=jingjidaijisuan.sum(s,"高新技术企业主营业务收入").split("###")[0];
            String saner=jingjidaijisuan.sumbi(s,"高新技术企业主营业务收入","高新技术企业主营业务收入占规上工业企业主营业务收入比重","高新技术企业主营业务收入");
            String sansan=jingjidaijisuan.sum(s,"知识密集型服务业增加值").split("###")[0];
            String sansi=jingjidaijisuan.baifen(s,"知识密集型服务业增加值","GDP",100);
            String sanwu=jingjidaijisuan.sum(s,"国家级高新技术产业开发区营业总收入").split("###")[0];
            String sanliu=jingjidaijisuan.baifen(s,"国家级高新技术产业开发区营业总收入","GDP",100);
            String sanqi=jingjidaijisuan.sum(s,"技术市场成交合同金额").split("###")[0];
            String sanba=jingjidaijisuan.baifen(s,"技术市场成交合同金额","GDP",100);
            String sanjiu=jingjidaijisuan.sum(s,"发明专利拥有量").split("###")[0];
            String sishi=jingjidaijisuan.baifen(s,"发明专利拥有量","常住人口",10000);
            String siyi=jingjidaijisuan.sum(s,"PCT专利申请量").split("###")[0];
            String sier=jingjidaijisuan.sum(s,"国家级科技成果奖数量").split("###")[0];
            String sisan=jingjidaijisuan.sum(s,"SCI论文数").split("###")[0];
            String sisi=jingjidaijisuan.baifen(s,"SCI论文数","常住人口",1000000);
            String siwu=jingjidaijisuan.zibi(s,"GDP","万元GDP综合能耗","GDP");
            String siliu=jingjidaijisuan.zibi(s,"GDP","万元GDP用水量","GDP");
            String siqi=jingjidaijisuan.xinbi(s,"空气质量达到及好于二级的天数占全年的比重",365);
            String siba=jingjidaijisuan.sum(s,"高技术产品出口额").split("###")[0];
            String sijiu=jingjidaijisuan.sumbi(s,"高技术产品出口额","高技术产品出口额占商品出口额比重","高技术产品出口额");
            String wushi=jingjidaijisuan.sum(s,"实际使用外资金额").split("###")[0];
            String wuyi=jingjidaijisuan.baifen(s,"实际使用外资金额","GDP",100);
            String wuer=jingjidaijisuan.sum(s,"与其他城市共建园区营业总收入").split("###")[0];
            String wusan=jingjidaijisuan.baifen(s,"与其他城市共建园区营业总收入","GDP",100);
            String wusi=jingjidaijisuan.sum(s,"国家级科技企业孵化器在孵企业数量").split("###")[0];
            String wuwu=jingjidaijisuan.sum(s,"省级科技企业孵化器在孵企业数量").split("###")[0];
            String wuliu=jingjidaijisuan.sum(s,"国家大学科技园在孵企业数量").split("###")[0];
            String wuqi=jingjidaijisuan.sum(s,"新增注册企业数").split("###")[0];
            String wuba=jingjidaijisuan.baifen(s,"新增注册企业数","常住人口",1);
            String wujiu=jingjidaijisuan.sum(s,"新增科技型中小企业数量").split("###")[0];
            String liushi=jingjidaijisuan.baifen(s,"新增科技型中小企业数量","新增注册企业数",100);
            String liuyi=jingjidaijisuan.baifenxin(s,"公民具备基本科学素养的比例",1);
            String liuer=jingjidaijisuan.sum(s,"特色/支柱产业1产值").split("###")[0];
            String liusan=jingjidaijisuan.baifenxin(s,"特色/支柱产业1产值增速",1);
            String liusi=jingjidaijisuan.baifenxin(s,"特色/支柱产业1产值占比",1);
            String liuwu=jingjidaijisuan.baifenxin(s,"特色/支柱产业1代表性产品/服务的市场占有率",1);
            String liuliu=jingjidaijisuan.sum(s,"特色/支柱产业2产值").split("###")[0];
            String liuqi=jingjidaijisuan.baifenxin(s,"特色/支柱产业2产值增速",1);
            String liuba=jingjidaijisuan.baifenxin(s,"特色/支柱产业2产值占比",1);
            String liujiu=jingjidaijisuan.baifenxin(s,"特色/支柱产业2代表性产品/服务的市场占有率",1);
            String qishi=jingjidaijisuan.sum(s,"特色/支柱产业3产值").split("###")[0];
            String qiyi=jingjidaijisuan.baifenxin(s,"特色/支柱产业3产值增速",1);
            String qier=jingjidaijisuan.baifenxin(s,"特色/支柱产业3产值占比",1);
            String qisan=jingjidaijisuan.baifenxin(s,"特色/支柱产业3代表性产品/服务的市场占有率",1);
            String qisi=jingjidaijisuan.sum(s,"常住人口").split("###")[0];
            String qiwu=jingjidaijisuan.sum(s,"行政区域土地面积").split("###")[0];
            String qiliu=jingjidaijisuan.sum(s,"GDP").split("###")[0];
            String qiqi=jingjidaijisuan.baifen(s,"GDP","常住人口",1);
            String qiba=jingjidaijisuan.sum(s,"就业人员").split("###")[0];
            String qijiu=jingjidaijisuan.sum(s,"公共财政收入").split("###")[0];
            String bashi=jingjidaijisuan.sum(s,"公共财政支出").split("###")[0];
            String bayi=jingjidaijisuan.baifenxin(s,"城镇居民人均可支配收入",1);
            String baer=jingjidaijisuan.baifenxin(s,"农村居民人均可支配收入",1);
            String basan=jingjidaijisuan.sum(s,"规上工业企业数").split("###")[0];
            String basi=jingjidaijisuan.sum(s,"规上工业总产值").split("###")[0];
            String bawu=jingjidaijisuan.sum(s,"普通高等学校数量").split("###")[0];
            String baliu=jingjidaijisuan.sum(s,"普通高等学校在校学生数").split("###")[0];
            String baqi=jingjidaijisuan.baifenxin(s,"建成区绿化覆盖率",1);
            Map<String,String> maps=new HashMap<>();
            maps.put("全社会R&D经费支出",yi);
            maps.put("全社会R&D经费支出占地区GDP比重",er);
            maps.put("规上工业企业R&D经费支出",san);
            maps.put("规上工业企业R&D经费支出占主营业务收入比重",si);
            maps.put("科技公共财政支出",wu);
            maps.put("科技公共财政支出占公共财政支出的比重",liu);
            maps.put("创业投资引导基金总额",qi);
            maps.put("创业投资引导基金总额占地区GDP比重",ba);
            maps.put("R&D经费加计扣除所得税减免额",jiu);
            maps.put("R&D经费加计扣除所得税减免额占企业研发经费比重",shi);
            maps.put("基础研究经费占R&D经费的比重",shier);
            maps.put("R&D人员",shisan);
            maps.put("每万名就业人员中R&D人员",shisi);
            maps.put("“千人计划”入选人数",shiwu);
            maps.put("“万人计划”入选人数",shiliu);
            maps.put("国家重点实验室数量",shiqi);
            maps.put("省级重点实验室数量",shiba);
            maps.put("国家工程研究中心数量",shijiu);
            maps.put("省级工程研究中心数量",ershi);
            maps.put("国家技术创新中心数量",eryi);
            maps.put("省级技术创新中心数量",erer);
            maps.put("国家国际科技合作基地数量",ersan);
            maps.put("国家级科技企业孵化器数量",ersi);
            maps.put("省级科技企业孵化器数量",erwu);
            maps.put("国家大学科技园数量",erliu);
            maps.put("国家备案众创空间数量",erqi);
            maps.put("外资研发中心数量",erba);
            maps.put("高新技术企业数",erjiu);
            maps.put("高新技术企业数占规上工业企业数量比重",sanshi);
            maps.put("高新技术企业主营业务收入",sanyi);
            maps.put("高新技术企业主营业务收入占规上工业企业主营业务收入比重",saner);
            maps.put("知识密集型服务业增加值",sansan);
            maps.put("知识密集型服务业增加值占地区GDP的比重",sansi);
            maps.put("国家级高新技术产业开发区营业总收入",sanwu);
            maps.put("国家级高新技术产业开发区营业总收入占地区GDP比重",sanliu);
            maps.put("技术市场成交合同金额",sanqi);
            maps.put("技术市场成交合同金额占地区GDP比重",sanba);
            maps.put("发明专利拥有量",sanjiu);
            maps.put("万人发明专利拥有量",sishi);
            maps.put("PCT专利申请量",siyi);
            maps.put("国家级科技成果奖数量",sier);
            maps.put("SCI论文数",sisan);
            maps.put("百万人口SCI论文数",sisi);
            maps.put("万元GDP综合能耗",siwu);
            maps.put("万元GDP用水量",siliu);
            maps.put("空气质量达到及好于二级的天数占全年的比重",siqi);
            maps.put("高技术产品出口额",siba);
            maps.put("高技术产品出口额占商品出口额比重",sijiu);
            maps.put("实际使用外资金额",wushi);
            maps.put("实际使用外资金额占地区GDP比重",wuyi);
            maps.put("与其他城市共建园区营业总收入",wuer);
            maps.put("与其他城市共建园区营业总收入占地区GDP比重",wusan);
            maps.put("国家级科技企业孵化器在孵企业数量",wusi);
            maps.put("省级科技企业孵化器在孵企业数量",wuwu);
            maps.put("国家大学科技园在孵企业数量",wuliu);
            maps.put("新增注册企业数",wuqi);
            maps.put("每万人新增注册企业数",wuba);
            maps.put("新增科技型中小企业数量",wujiu);
            maps.put("新增科技型中小企业数量占新增注册企业的比重",liushi);
            maps.put("公民具备基本科学素养的比例",liuyi);
            maps.put("特色/支柱产业1产值",liuer);
            maps.put("特色/支柱产业1产值增速",liusan);
            maps.put("特色/支柱产业1产值占比",liusi);
            maps.put("特色/支柱产业1代表性产品/服务的市场占有率",liuwu);
            maps.put("特色/支柱产业2产值",liuliu);
            maps.put("特色/支柱产业2产值增速",liuqi);
            maps.put("特色/支柱产业2产值占比",liuba);
            maps.put("特色/支柱产业2代表性产品/服务的市场占有率",liujiu);
            maps.put("特色/支柱产业3产值",qishi);
            maps.put("特色/支柱产业3产值增速",qiyi);
            maps.put("特色/支柱产业3产值占比",qier);
            maps.put("特色/支柱产业3代表性产品/服务的市场占有率",qisan);
            maps.put("常住人口",qisi);
            maps.put("行政区域土地面积",qiwu);
            maps.put("GDP",qiliu);
            maps.put("人均GDP",qiqi);
            maps.put("就业人员",qiba);
            maps.put("公共财政收入",qijiu);
            maps.put("公共财政支出",bashi);
            maps.put("城镇居民人均可支配收入",bayi);
            maps.put("农村居民人均可支配收入",baer);
            maps.put("规上工业企业数",basan);
            maps.put("规上工业总产值",basi);
            maps.put("普通高等学校数量",bawu);
            maps.put("普通高等学校在校学生数",baliu);
            maps.put("建成区绿化覆盖率",baqi);
            map.put(s,maps);
        }
        ruku(map);
    }

    public static void ruku(Map<String,Map<String,String>> map) throws SQLException {
        String sql="select * from regional_innovation_dim_sys_classify where classify_name=? or classify_short=?";
        PreparedStatement ps=conn.prepareStatement(sql);

        String sql0="insert into regional_innovation_indicator_data(classify_id,classify_name,classify_short,classify_level,area_id,area_name,area_shortname,area_type,is_provincial_capital,data_src,classify_unit_tmp,data_normal_tmp,data_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps0=conn.prepareStatement(sql0);

        String sql1="select area_id,area_name,area_type,is_provincial_capital from regional_innovation_dim_sys_area where replace(replace(replace(replace(replace(replace(area_name,'省',''),'市',''),'自治区',''),'回族',''),'壮族',''),'维吾尔','')=?";
        PreparedStatement ps1=conn.prepareStatement(sql1);

        for(Map.Entry<String,Map<String,String>> entry:map.entrySet()){
            String area=entry.getKey().replaceAll("\\s","").replace("省","").replace("市","").replace("自治区","").replace("回族","").replace("壮族","").replace("维吾尔","").replace("维吾尔族","");
            DecimalFormat decimalFormat;//构造方法的字符格式这里如果小数不足2位,会以0补足.
            for(Map.Entry<String,String> entry1:entry.getValue().entrySet()){
                ps.setString(1,entry1.getKey());
                ps.setString(2,entry1.getKey());
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
                    System.out.println(entry1.getKey());
                }

                ps1.setString(1,area);
                ResultSet rs1=ps1.executeQuery();
                while (rs1.next()){
                    arid=rs1.getString(rs1.findColumn("area_id"));
                    arname=rs1.getString(rs1.findColumn("area_name"));
                    arty=rs1.getString(rs1.findColumn("area_type"));
                    aris=rs1.getString(rs1.findColumn("is_provincial_capital"));
                    if(!Dup.nullor(aris)){
                        aris="0";
                    }
                }

                String shu=entry1.getValue();
                if(shu.equals("/")){
                    shu=null;
                }
                if(Dup.nullor(shu)){
                    try {
                        float f = Float.parseFloat(shu);
                        if (f > 1) {
                            decimalFormat = new DecimalFormat(".00");
                        } else {
                            decimalFormat = new DecimalFormat("0.00");
                        }
                        p = decimalFormat.format(f);//format 返回的是字符串
                    }catch (Exception e){
                        p=null;
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
                ps0.setString(13,"2017-12-12 00:00:00");
                ps0.executeUpdate();
                aa++;
                System.out.println(aa+"**********************************************************");

            }
        }
    }


}
