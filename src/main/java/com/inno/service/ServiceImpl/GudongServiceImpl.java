package com.inno.service.ServiceImpl;

import com.inno.bean.TongbuBean;
import com.inno.dao.GudongDao;
import com.inno.dao.Impl.GudongDaoImpl;
import com.inno.dao.Impl.JigouDaoImpl;
import com.inno.dao.Impl.TongbuDaoImpl;
import com.inno.dao.JigouDao;
import com.inno.dao.TongbuDao;
import com.inno.service.GudongService;
import com.inno.utils.ConfigureUtils.company_parse;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.mybatis_factory.MybatisUtils;
import com.inno.utils.redisUtils.RedisAction;
import org.dom4j.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GudongServiceImpl implements GudongService {
    private static GudongDao ji=new GudongDaoImpl();
    private static TongbuDao t=new TongbuDaoImpl();
    private static RedisAction rs;
    private static RedisAction rss;
    private static TongbuBean.Write tb=new TongbuBean.Write();
    static {
        rs=new RedisAction("10.44.51.90",6379);
        rss=new RedisAction("a027.hb2.innotree.org",6379);
        tb.setOnid("tail_id");
        tb.setDimname("gudong");
        tb.setTablename("innotree_data.company_shareholder");
    }


    @Override
    public List<Map<String, Object>> find(String lim) {
        List<Map<String,Object>> list=ji.findList(lim);
        return list;
    }

    @Override
    public List<Map<String,Object>> findshang(List<String> list,String a){
        return ji.findshang(list, String.valueOf(a));
    }

    @Override
    public void clean(List<Map<String, Object>> list) throws IOException, InterruptedException, ParseException {
        List<Map<String,Object>> ll=new ArrayList<>();
        int p=0;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map<String,Object> mm:list){
            try {
                Map<String, Object> map = new HashMap<>();
                String taid= String.valueOf(mm.get("tail_id"));
                if(Dup.nullor(taid)) {
                    String sh[] = shareholder_typeandid((String) mm.get("p_name"));
                    map.put("comp_id", mm.get("only_id"));
                    map.put("tail_id", mm.get("tail_id"));
                    map.put("comp_full_name", comp_full_name((String) mm.get("comp_full_name")));
                    map.put("shareholder_name", shareholder_name((String) mm.get("p_name")));
                    map.put("shareholder_type", sh[1]);
                    map.put("shareholder_id", sh[0]);
                    map.put("is_enable", "1");
                    map.put("data_source", "tyc");
                    if (rs.get("zhuce", String.valueOf(mm.get("only_id")))) {
                        map.put("del_flag", "0");
                    } else {
                        map.put("del_flag", "1");
                    }

                    boolean bo = true;
                    if (mm.get("renjiao_chuzi") != null) {
                        String ren = mm.get("renjiao_chuzi").toString();
                        if (!ren.contains("时间")) {
                            if (occurTimes(ren, ".") > 1) {
                                Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
                                Matcher m = pat.matcher(ren);
                                if (m.find()) {
                                    String[] rs = ren.split("[^0-9\\.]");
                                    int aa = 0;
                                    for (String s : rs) {
                                        if (aa == 0) {
                                            map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                                        } else {
                                            map.put("contribution_rate", null);
                                        }
                                        map.put("subscribe_amount", subscribe_amount(s));
                                        map.put("subscribe_date", subscribe_date(""));
                                        aa++;
                                        ll.add(map);
                                    }
                                }
                            } else {
                                map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                                map.put("subscribe_amount", subscribe_amount(ren));
                                map.put("subscribe_date", subscribe_date(""));
                            }
                        } else if (ren.contains("时间") && ren.split("时间").length == 2 && ren.split("时间")[1].length() == 11) {
                            map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                            map.put("subscribe_amount", subscribe_amount(ren.split("时间")[0]));
                            map.put("subscribe_date", subscribe_date(ren.split("时间")[1]));
                        } else if (ren.contains("时间") && ren.split("时间").length == 2 && ren.split("时间")[1].length() > 11) {
                            map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                            map.put("subscribe_amount", subscribe_amount(ren.split("时间")[0]));
                            map.put("subscribe_date", subscribe_date(ren.split("时间")[1].substring(0, 11)));
                            ll.add(map);
                            map.put("contribution_rate", contribution_rate(""));
                            map.put("subscribe_amount", subscribe_amount(ren.split("时间")[0]));
                            map.put("subscribe_date", subscribe_date(""));
                            ll.add(map);
                        } else if (ren.contains("时间") && ren.split("时间").length > 2) {
                            for (int a = 0; a < ren.split("时间").length - 1; a++) {
                                if (a == 0) {
                                    map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                                    map.put("subscribe_amount", subscribe_amount(ren.split("时间")[a]));
                                    map.put("subscribe_date", subscribe_date(ren.split("时间")[a + 1].substring(0, 11)));
                                } else {
                                    map.put("contribution_rate", contribution_rate(""));
                                    map.put("subscribe_amount", subscribe_amount(ren.split("时间")[a].substring(11)));
                                    map.put("subscribe_date", subscribe_date(ren.split("时间")[a + 1].substring(0, 11)));
                                }
                                ll.add(map);
                                bo = false;
                            }
                        }
                    } else {
                        map.put("contribution_rate", contribution_rate((String) mm.get("chuzi_bili")));
                        map.put("subscribe_amount", subscribe_amount(""));
                        map.put("subscribe_date", subscribe_date(""));
                    }
                    if (bo) {
                        ll.add(map);
                    }
                }
                p++;
                System.out.println(simpleDateFormat.format(new Date()) + "  [INFO]  " + "正在清理第： " + p + "   条");
                if (p % 20000 == 0) {
                    delete(ll);
                    insert(ll);
                    //t.delete(ll,tb);
                    //t.insertList(ll,tb);
                    ll = new ArrayList<>();
                }
            }catch (Exception e){
                System.out.println("error"+e.getMessage());
            }
        }
        if(ll!=null&&ll.size()>0){
            delete(ll);
            insert(ll);
            //t.delete(ll,tb);
            //t.insertList(ll,tb);
        }

    }


    public static void delete(List<Map<String,Object>> list) {
        List<String> ll=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for(Map<String,Object> mm:list){
            ll.add((String) mm.get("tail_id"));
        }
        map.put("field",ll);
        ji.delete(map);
    }

    public static void insert(List<Map<String,Object>> list) {
        Map<String,Object> map=new HashMap<>();
        map.put("field",list);
        ji.insert(map);
    }


    public static String comp_full_name(String key){
        return Dup.renull(key);
    }

    public static String shareholder_name(String key){
        return Dup.renull(key);
    }

    public static String[] shareholder_typeandid(String key) throws IOException, InterruptedException {
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("未公开",""))){
                String[] ons= MD5Util.Onlyid(key,"kk",new ArrayList<>());
                if(rs.get("zhuce",ons[0])){
                    return new String[]{ons[0],"comp"};
                }else if(key.length()>4){
                    return new String[]{ons[0],"comp"};
                }else{
                    return new String[]{null,"person"};
                }
            }else{
                return new String[]{null,null};
            }
        }else{
            return new String[]{null,null};
        }
    }

    public static String contribution_rate(String key){
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("未公开",""))){
                return Dup.renull(key).replace("%","").replace("<","").replace("未公开","");
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static int occurTimes(String string, String a) {
        int pos = -2;
        int n = 0;

        while (pos != -1) {
            if (pos == -2) {
                pos = -1;
            }
            pos = string.indexOf(a, pos + 1);
            if (pos != -1) {
                n++;
            }
        }
        return n;
    }

    public static String subscribe_amount(String key){
        try {
            if (Dup.nullor(key)) {
                if (Dup.nullor(key.replace("未公开", "").replace("null", "").replaceAll("[^0-9\\.]", ""))) {
                    String mon = Dup.renull(key).replace("万元", "").replace("人民币", "").replace("(", "").replace(")", "").replace(",", "").replace("万", "").replace("美元", "").replace("元", "").replaceAll("[^0-9\\.]", "");
                    if (Dup.nullor(mon)) {
                        if (key.contains("美元")&&key.contains("万")) {
                            float d = Float.parseFloat("0.15");
                            DecimalFormat decimalFormat = new DecimalFormat(".00");
                            float jie = Float.parseFloat(mon) * d;
                            String jieguo;
                            if (jie < 1) {
                                decimalFormat = new DecimalFormat("0.00");
                            }
                            jieguo = decimalFormat.format(jie);
                            if (jieguo.contains(".") && jieguo.split("\\.")[1].length() > 2) {
                                jieguo = jieguo.split("\\.", 2)[0] + "." + jieguo.split("\\.", 2)[1].substring(0, 2);
                            }
                            if(occurTimes(jieguo,".")>1){
                                return null;
                            }else {
                                return jieguo;
                            }
                        } else if (key.contains("美元") && !key.contains("万")) {
                            float d = Float.parseFloat("0.15");
                            DecimalFormat decimalFormat = new DecimalFormat(".00");
                            float jie = (Float.parseFloat(mon) * d) / 10000;
                            String jieguo;
                            if (jie < 1) {
                                decimalFormat = new DecimalFormat("0.00");
                            }
                            jieguo = decimalFormat.format(jie);
                            if (jieguo.contains(".") && jieguo.split("\\.")[1].length() > 2) {
                                jieguo = jieguo.split("\\.", 2)[0] + "." + jieguo.split("\\.", 2)[1].substring(0, 2);
                            }
                            if(occurTimes(jieguo,".")>1){
                                return null;
                            }else {
                                return jieguo;
                            }
                        } else {
                            if (mon.contains(".") && mon.split("\\.")[1].length() > 2) {
                                mon = mon.split("\\.", 2)[0] + "." + mon.split("\\.", 2)[1].substring(0, 2);
                            }
                            if(occurTimes(mon,".")>1){
                                return null;
                            }else {
                                return mon;
                            }
                        }
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static String subscribe_date(String key) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        if(Dup.nullor(key)){
            if(Dup.nullor(key.replace("未公开","").replace("：",""))){
                String time=Dup.renull(key).replace("：","").replace("未公开","");
                Long ti=simpleDateFormat.parse(time).getTime();
                if(ti>System.currentTimeMillis()){
                    return null;
                }else{
                    return time;
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
    }



}
