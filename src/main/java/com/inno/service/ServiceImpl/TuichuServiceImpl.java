package com.inno.service.ServiceImpl;

import com.inno.dao.Impl.JigouDaoImpl;
import com.inno.dao.Impl.TuichuDaoImpl;
import com.inno.dao.JigouDao;
import com.inno.dao.TuichuDao;
import com.inno.service.TuichuService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisAction;
import com.inno.utils.spider.JsoupUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TuichuServiceImpl implements TuichuService {
    private static TuichuDao ji=new TuichuDaoImpl();
    private static RedisAction rs;
    static {
        rs=new RedisAction("10.44.51.90",6379);
    }

    @Override
    public List<Map<String, Object>> find() {
        List<Map<String,Object>> list=ji.findList();
        return list;
    }

    @Override
    public List<Map<String, Object>> clean(List<Map<String, Object>> list) throws IOException, InterruptedException {
        List<Map<String,Object>> ll=new ArrayList<>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int a=0;
        for(Map<String,Object> mm:list){
            Map<String,Object> map=new HashMap<>();
            String tuq= (String) mm.get("exit_quan");

            String exitjian=exit_name_short((String) mm.get("exit_enterprise"));
            String exitdate=exit_date((String) mm.get("exit_time"));
            String firstdate=first_invest_date((String) mm.get("first_time"));
            map.put("comp_id",mm.get("only_id"));
            map.put("tail_id",mm.get("tail_id"));
            map.put("comp_full_name",comp_full_name((String) mm.get("comp_full_name")));
            map.put("exit_date",exitdate);
            map.put("exit_comp_id",exit_comp_id(tuq));
            map.put("exit_name_short",exitjian);
            map.put("exit_full_name",exit_full_name(tuq));
            map.put("exit_way",exit_way((String) mm.get("exit_mode")));
            map.put("return_amount",return_amount((String) mm.get("return_money")));
            map.put("return_multiple",return_multiple((String) mm.get("return_multiple")));
            map.put("sum_invest_amount",sum_invest_amount((String) mm.get("cumulative_money")));
            map.put("first_invest_date",firstdate);
            map.put("data_source","smt");
            if(rs.get("zhuce", String.valueOf(mm.get("only_id")))) {
                map.put("del_flag","0");
            }else{
                map.put("del_flag","1");
            }

            if(Dup.nullor(exitjian)&&Dup.nullor(exitdate)&&Dup.nullor(firstdate)) {
                ll.add(map);
            }
            a++;
            System.out.println(simpleDateFormat.format(new Date())+"  [INFO]  "+"正在清理第： "+a+"   条");
        }
        return ll;
    }

    @Override
    public void delete(List<Map<String, Object>> list) {
        List<String> ll=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for(Map<String,Object> mm:list){
            ll.add(String.valueOf(mm.get("tail_id")));
        }
        map.put("field",ll);
        ji.delete(map);
    }

    @Override
    public void insert(List<Map<String, Object>> list) {
        Map<String,Object> map=new HashMap<>();
        map.put("field",list);
        ji.insert(map);
    }

    public static String comp_full_name(String key){
        return Dup.renull(key);
    }


    public static String exit_comp_id(String key) throws IOException, InterruptedException {
        if(Dup.nullor(key)){
            return MD5Util.Onlyid(key,"smt",new ArrayList<>())[0];
        }else{
            return null;
        }
    }

    public static String exit_full_name(String key){
        return Dup.renull(key);
    }

    public static String exit_name_short(String key){
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("不披露",""))){
                return Dup.renull(key);
            }else{
                return null;
            }
        }else{
            return null;
        }

    }

    public static String exit_date(String key){
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("--",""))){
                return key.replace("--","");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String exit_way(String key){
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("--",""))){
                return key.replace("--","");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String return_multiple(String key){
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("--",""))){
                return key.replace("--","");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String return_amount(String key) throws IOException {
        if(Dup.nullor(key)) {
            if (key.contains("RMB")) {
                String mon = key.split(" ", 2)[1].replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<", "");
                float d = Float.parseFloat("0.15");
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                float jie = Float.parseFloat(mon) * d;
                String jieguo;
                if (jie < 1) {
                    decimalFormat = new DecimalFormat("0.00");
                }
                jieguo = decimalFormat.format(jie);
                return jieguo;
            } else if (key.contains("USD")) {
                String mon = key.split(" ", 2)[1].replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<", "");
                return mon;
            }else if(Dup.nullor(key.replace("--","").replace(" ",""))){
                return huilv(key);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String sum_invest_amount(String key) throws IOException {
        if(Dup.nullor(key)) {
            if (key.contains("RMB")) {
                String mon = key.split(" ", 2)[1].replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<", "");
                float d = Float.parseFloat("0.15");
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                float jie = Float.parseFloat(mon) * d;
                String jieguo;
                if (jie < 1) {
                    decimalFormat = new DecimalFormat("0.00");
                }
                jieguo = decimalFormat.format(jie);
                return jieguo;
            } else if (key.contains("USD")) {
                String mon = key.split(" ", 2)[1].replace("M", "").replaceAll("\\s", "").replace(",", "").replace("<", "");
                return mon;
            }else if(Dup.nullor(key.replace("--","").replace(" ",""))){
                return huilv(key);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String first_invest_date(String key) throws IOException {
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("--",""))){
                return key.replace("--","");
            }else{
                return null;
            }
        }else{
            return null;
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
