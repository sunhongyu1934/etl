package com.inno.service.ServiceImpl;

import com.inno.dao.Impl.JijinDaoImpl;
import com.inno.dao.JijinDao;
import com.inno.service.JijinService;
import com.inno.utils.Dup;
import com.inno.utils.MD5utils.MD5Util;
import com.inno.utils.redisUtils.RedisAction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JijinServiceImpl implements JijinService {
    private static JijinDao j=new JijinDaoImpl();
    private static RedisAction rs;
    private static List<Map<String,Object>> glist;
    private static List<Map<String,Object>> alist;

    static{
        rs=new RedisAction("10.44.51.90",6379);
        glist=j.findGid();
    }

    @Override
    public Map<String, Object> find() {
        return j.findList();
    }

    @Override
    public List<Map<String, Object>> clean(Map<String, Object> map) throws IOException, InterruptedException {
        List<Map<String,Object>> list=new ArrayList<>();
        alist= (List<Map<String,Object>>) map.get("fund_base1_sum");
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int a=0;
        System.out.println(((List<Map<String,Object>>)map.get("fund_base_sum")).size());
        for(Map<String,Object> mm: (List<Map<String,Object>>) map.get("fund_base_sum")){
            Map<String,Object> mmp=new HashMap<>();
            String fund_name=fund_name((String) mm.get("comp_full_name"));
            String cos[]=comp_full_nameandid((String) mm.get("g_id"),(String) mm.get("guanli_jigou"),fund_name);
            mmp.put("fund_id",mm.get("only_id"));
            mmp.put("fund_name",fund_name);
            mmp.put("fund_shortname",fund_shortname((String) mm.get("zhong_jian")));
            mmp.put("fund_englishname",fund_englishname((String) mm.get("ying_jian")));
            mmp.put("comp_id",cos[1]);
            mmp.put("comp_short_name",comp_short_name((String) mm.get("guanli_jigou")));
            mmp.put("comp_full_name",cos[0]);
            mmp.put("fund_type",fund_type((String) mm.get("jijin_type"),fund_name));
            mmp.put("collect_status",collect_status((String) mm.get("muji_statue")));
            mmp.put("captital_type",captital_type((String) mm.get("ziben_type")));
            mmp.put("finish_date",finish_date((String) mm.get("mui_time")));
            mmp.put("invest_num",invest_num((String) mm.get("touzi_sum")));
            mmp.put("organ_type",organ_type((String) mm.get("zuzhi_xingshi")));
            mmp.put("is_gov_trust",is_gov_trust((String) mm.get("zhengfu_flag")));
            mmp.put("create_date",create_date((String) mm.get("chengli_time")));
            mmp.put("keep_on_record",keep_on_record((String) mm.get("bei_an")));
            mmp.put("report_status",report_status((String) mm.get("shenbao_statue")));
            mmp.put("run_status",run_status(fund_name));
            mmp.put("fund_intro",fund_intro((String) mm.get("de_sc")));
            String mount=fund_amount((String) mm.get("moji_money"));
            mmp.put("fund_amount",mount);
            mmp.put("fund_money",fund_money((String) mm.get("moji_money")));
            mmp.put("fund_cur",fund_cur(mount));
            mmp.put("head_quarter",head_quarter((String) mm.get("zong_bu")));
            mmp.put("trust_org_name",trust_org_name(fund_name));
            mmp.put("manage_type",manage_type(fund_name));
            mmp.put("record_date",record_date(fund_name));
            mmp.put("last_up_date",last_up_date(fund_name));
            mmp.put("data_source","smt");
            if(rs.get("zhuce", String.valueOf(mm.get("only_id")))) {
                mmp.put("del_flag","0");
            }else{
                mmp.put("del_flag","1");
            }
            list.add(mmp);
            a++;
            System.out.println(simpleDateFormat.format(new Date())+"  [INFO]  "+"正在清理第： "+a+"   条");
        }
        return list;
    }

    @Override
    public void delete(List<Map<String,Object>> list) {
        List<String> ll=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for(Map<String,Object> mm:list){
            ll.add(String.valueOf(mm.get("fund_id")));
        }
        map.put("field",ll);
        j.delete(map);
    }

    @Override
    public void insert(List<Map<String,Object>> list) {
        Map<String,Object> map=new HashMap<>();
        map.put("field",list);
        j.insert(map);
    }


    public static String fund_name(String key){
        return Dup.renull(key);
    }

    public static String fund_shortname(String key){
        return Dup.renull(key);
    }

    public static String fund_englishname(String key){
        return Dup.renull(key);
    }

    public static String comp_id(String key,String keys){
        String onid = null;
        for(Map<String,Object> mm:glist){
            String gid= mm.get("s_id").toString();
            String quan=mm.get("company_abbreviation").toString();
            if(key.equals(gid)){
                onid= Dup.renull((String) mm.get("only_id"));
                break;
            }else if(keys.equals(quan)){
                onid= Dup.renull((String) mm.get("only_id"));
                break;
            }
        }
        return onid;
    }

    public static String comp_short_name(String key){
        return Dup.renull(key);
    }

    public static String[] comp_full_nameandid(String key,String keys,String qu) throws IOException, InterruptedException {
        String ff=null;
        for(Map<String,Object> mm:alist){
            String aquan= (String) mm.get("fundName");
            if(qu.equals(aquan)){
                ff= Dup.renull((String) mm.get("managerName"));
                break;
            }
        }

        if(!Dup.nullor(ff)) {
            String quan = null;
            for (Map<String, Object> mm : glist) {
                String gid = mm.get("s_id").toString();
                String jian = mm.get("company_abbreviation").toString();
                if (key.equals(gid)) {
                    quan = Dup.renull((String) mm.get("comp_full_name"));
                    break;
                } else if (keys.equals(jian)) {
                    quan = Dup.renull((String) mm.get("comp_full_name"));
                    break;
                }
            }
            if(Dup.nullor(quan)){
                return new String[]{quan, MD5Util.Onlyid(quan,"smt",new ArrayList<>())[0]};
            }else{
                return new String[]{null,null};
            }
        }else{
            return new String[]{ff, MD5Util.Onlyid(ff,"smt",new ArrayList<>())[0]};
        }
    }

    public static String fund_type(String key,String quan){
        String ftype=null;
        for(Map<String,Object> mm:alist){
            String aquan= (String) mm.get("fundName");
            if(quan.equals(aquan)){
                ftype= Dup.renull((String) mm.get("fundType"));
                break;
            }
        }
        if(Dup.nullor(ftype)){
            return Dup.renull(ftype);
        }else if(Dup.nullor(key)){
            return key;
        }else{
            return null;
        }
    }

    public static String collect_status(String key){
        return Dup.renull(key);
    }

    public static String captital_type(String key){
        return Dup.renull(key);
    }

    public static String finish_date(String key){
        if(Dup.nullor(key)) {
            if (Dup.nullor(key.replaceAll("\\s", "").replace("--", "").replaceAll("不披露", "").replace("USD", ""))) {
                return key.trim();
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    public static String invest_num(String key){
        if(Dup.nullor(key)) {
            if (Dup.nullor(key.replace("--", ""))) {
                return Dup.renull(key.replace("--", ""));
            } else {
                return null;
            }
        }else{
            return null;
        }

    }

    public static String organ_type(String key){
        return Dup.renull(key);
    }

    public static String is_gov_trust(String key){
        if(Dup.nullor(key)){
            if(key.contains("是")){
                return "1";
            }else if(key.contains("否")){
                return "0";
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public static String create_date(String key){
        if(Dup.nullor(key)) {
            if (Dup.nullor(key.replaceAll("\\s", "").replace("--", "").replaceAll("不披露", "").replace("USD", ""))) {
                return key.trim();
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    public static String keep_on_record(String key){
        if(Dup.nullor(key)){
            if(key.contains("是")){
                return "1";
            }else if(key.contains("否")){
                return "0";
            }else{
                return null;
            }
        }else{
            return null;
        }
    }


    public static String report_status(String key){
        return Dup.renull(key);
    }

    public static String run_status(String key){
        String tname=null;
        for(Map<String,Object> mm:alist){
            String quan= (String) mm.get("fundName");
            if(key.equals(quan)){
                tname=Dup.renull((String) mm.get("workingState"));
                break;
            }
        }
        return tname;
    }

    public static String fund_intro(String key){
        return Dup.renull(key);
    }

    public static String fund_amount(String key){
        if(Dup.nullor(key)) {
            if (Dup.nullor(key.replace("--", ""))) {
                return Dup.renull(key.replace("--", ""));
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    public static String fund_money(String key){
        if(Dup.nullor(key)) {
            if (Dup.nullor(key.replaceAll("\\s", "").replace("--", "").replaceAll("不披露", "").replace("USD", ""))) {
                return key.replaceAll("\\s", "").replace("--", "").replaceAll("不披露", "").replace("USD", "").replace(" ", "").replace(",", "");
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    public static String fund_cur(String key){
        if(Dup.nullor(key)){
            return key.split(" ",2)[0];
        }else{
            return null;
        }
    }

    public static String head_quarter(String key){
        return Dup.renull(key);
    }

    public static String trust_org_name(String key){
        String tname=null;
        for(Map<String,Object> mm:alist){
            String quan= (String) mm.get("fundName");
            if(key.equals(quan)){
                tname=Dup.renull((String) mm.get("mandatorName"));
                break;
            }
        }
        return tname;
    }

    public static String manage_type(String key){
        String gtype=null;
        for(Map<String,Object> mm:alist){
            String quan= (String) mm.get("fundName");
            if(key.equals(quan)){
                gtype=Dup.renull((String) mm.get("managerType"));
                break;
            }
        }
        return gtype;
    }

    public static String record_date(String key){
        String gtype=null;
        for(Map<String,Object> mm:alist){
            String quan= (String) mm.get("fundName");
            if(key.equals(quan)){
                gtype=(String) mm.get("putOnRecordDate");
                if(!Dup.nullor(gtype)){
                    gtype=null;
                }
                break;
            }
        }
        return gtype;
    }

    public static String last_up_date(String key){
        String gtype=null;
        for(Map<String,Object> mm:alist){
            String quan= (String) mm.get("fundName");
            if(key.equals(quan)){
                gtype=(String) mm.get("fundUpdate");
                if(!Dup.nullor(gtype)){
                    gtype=null;
                }
                break;
            }
        }
        return gtype;
    }

}
