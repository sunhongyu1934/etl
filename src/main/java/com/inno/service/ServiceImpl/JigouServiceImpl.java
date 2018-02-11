package com.inno.service.ServiceImpl;

import com.inno.dao.Impl.JigouDaoImpl;
import com.inno.dao.JigouDao;
import com.inno.service.JigouService;
import com.inno.utils.Dup;
import com.inno.utils.redisUtils.RedisClu;
import com.inno.utils.spider.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JigouServiceImpl implements JigouService {
    private static JigouDao ji=new JigouDaoImpl();
    private static RedisClu rs;
    static {
        rs=new RedisClu();
    }


    @Override
    public List<Map<String, Object>> find() {
        List<Map<String,Object>> list=ji.findList();
        return list;
    }

    @Override
    public List<Map<String, Object>> clean(List<Map<String, Object>> list) throws IOException {
        List<Map<String,Object>> ll=new ArrayList<>();
        for(Map<String,Object> mm:list){
            Map<String,Object> map=new HashMap<>();
            if(String.valueOf(mm.get("company_abbreviation")).contains("不公开")||String.valueOf(mm.get("company_abbreviation")).contains("个人投资者")){
                continue;
            }
            map.put("comp_full_name", comp_full_name(String.valueOf(mm.get("comp_full_name"))));
            map.put("comp_name",comp_name(String.valueOf(mm.get("company_abbreviation"))));
            map.put("capital_type", capital_type(String.valueOf(mm.get("capital_type"))));
            map.put("keep_on_record", keep_on_record(String.valueOf(mm.get("keep_on_record"))));
            map.put("record_date", record_date(String.valueOf(mm.get("filing_time"))));
            map.put("head_quarter", head_quarter(String.valueOf(mm.get("corporate_headquarters"))));
            map.put("invest_type", invest_type(String.valueOf(mm.get("VC"))));
            String gz = capital_money_m(String.valueOf(mm.get("managed_capital")));
            map.put("capital_money_m", gz);
            map.put("captial_amount", captial_amount(gz));
            map.put("capital_money_r", capital_money_r(String.valueOf(mm.get("managed_capital"))));
            map.put("capital_amount_r", capital_amount_r(String.valueOf(mm.get("managed_capital"))));
            map.put("capital_amount_m", capital_amount_m(String.valueOf(mm.get("managed_capital"))));
            map.put("regist_region", regist_region(String.valueOf(mm.get("registered_area"))));
            map.put("data_source", "smt");
            map.put("comp_id", mm.get("only_id"));
            if(rs.get("zhuce", String.valueOf(mm.get("only_id")))) {
                map.put("del_flag","0");
            }else{
                map.put("del_flag","1");
            }
            ll.add(map);

        }
        return ll;
    }

    @Override
    public void delete(List<Map<String,Object>> list) {
        List<String> ll=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for(Map<String,Object> mm:list){
            ll.add(String.valueOf(mm.get("comp_id")));
        }
        map.put("field",ll);
        ji.delete(map);
    }

    @Override
    public void insert(List<Map<String,Object>> list) {
        Map<String,Object> map=new HashMap<>();
        map.put("field",list);
        ji.insert(map);
    }

    public static String comp_full_name(String key){
        return Dup.renull(key);
    }

    public static String comp_name(String key){
        return Dup.renull(key);
    }

    public static String capital_type(String key){
        return key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]","");
    }

    public static String keep_on_record(String key){
        String va=key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]","");
        if(Dup.nullor(va)&&va.contains("是")){
            return "1";
        }else if(Dup.nullor(va)&&va.contains("否")){
            return "0";
        }else{
            return null;
        }
    }

    public static String record_date(String key){
        if(!Dup.nullor(key.trim().replaceAll("\\s",""))){
            return null;
        }else if(key.contains("1900-01-00")){
            return null;
        }else{
            return key.trim().replaceAll("\\s","");
        }

    }

    public static String head_quarter(String key){
        if(!Dup.nullor(key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]",""))){
            return null;
        }else{
            return key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]","");
        }
    }

    public static String invest_type(String key){
        if(!Dup.nullor(key.trim().replaceAll("\\s",""))){
            return null;
        }else{
            return key.trim().replaceAll("\\s","");
        }
    }


    public static String capital_money_m(String key) throws IOException {
        if(Dup.nullor(key)&&!key.contains("RMB")&&!key.contains("USD")&&Dup.nullor(key.split(" ",2)[0])){
            return huilv(key);
        }else if(Dup.nullor(key)&&key.contains("USD")){
            if(!Dup.nullor(key.replace("USD","").replace(" ","").replace("M","").replaceAll("\\s","").replace(",",""))){
                return null;
            }else {
                return key.replace("USD", "").replace(" ", "").replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<","");
            }
        }else{
            return null;
        }
    }

    public static String captial_amount(String key){
        if(Dup.nullor(key)){
            return key+"百万美元";
        }else {
            return null;
        }
    }

    public static String capital_amount_r(String key){
        if(Dup.nullor(key)&&(key.contains("RMB")||!Dup.nullor(key.split(" ",2)[0]))){
            return key;
        }else{
            return null;
        }
    }

    public static String capital_amount_m(String key){
        if(Dup.nullor(key)&&!key.contains("RMB")&&Dup.nullor(key.split(" ",2)[0])){
            return key;
        }else{
            return null;
        }
    }

    public static String capital_money_r(String key){
        if(Dup.nullor(key)&&(key.contains("RMB")||!Dup.nullor(key.split(" ",2)[0]))){
            if(!Dup.nullor(key.replace("RMB","").replace(" ","").replace("M","").replaceAll("\\s","").replace(",",""))){
                return null;
            }else {
                return key.replace("RMB", "").replace(" ", "").replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<","");
            }
        }else{
            return null;
        }
    }

    public static String regist_region(String key){
        if(!Dup.nullor(key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]",""))){
            return null;
        }else{
            return key.trim().replaceAll("\\s","").replaceAll("[^\\u4e00-\\u9fa5]","");
        }
    }





    public static String huilv(String key) throws IOException {
        String bi = key.split(" ", 2)[0];
        String mon = key.split(" ", 2)[1].replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<", "");
        try {
            Document doc;
            while (true) {
                doc = Jsoup.connect("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=" + bi + "%E7%BE%8E%E5%85%83&oq=USD%25E6%2598%25AF%25E4%25BB%2580%25E4%25B9%2588%25E8%25B4%25A7%25E5%25B8%2581&rsv_pq=aae9f68b00039e09&rsv_t=e87cfE01%2Bw5IToRDeegOJLNFA2LExklxW0JieEO2cEDdMhEB0X9DfUw3%2BRs&rqlang=cn&rsv_enter=1&inputT=2506&rsv_sug3=8&rsv_sug1=6&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2507")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(5000)
                        .get();
                if (doc != null && doc.outerHtml().length() > 44) {
                    break;
                }
            }
            String hui = JsoupUtils.getString(doc, "div.c-span21.c-span-last div.op_exrate_result div", 0).split("=", 2)[1].replace("美元", "");
            if (Dup.nullor(hui)) {
                try {
                    float d = Float.parseFloat(hui);
                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                    float jie = Float.parseFloat(mon) * d;
                    String jieguo;
                    if (jie < 1) {
                        decimalFormat = new DecimalFormat("0.00");
                    }
                    jieguo = decimalFormat.format(jie);

                    return jieguo;
                } catch (Exception e) {
                    return mon;
                }
            } else {
                return mon;
            }
        }catch (Exception e){
            return mon;
        }
    }

}
